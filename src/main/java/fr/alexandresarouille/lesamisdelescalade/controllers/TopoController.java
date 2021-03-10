package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IReservationService;
import fr.alexandresarouille.lesamisdelescalade.services.ITopoService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("topos")
public class TopoController {

    @Autowired
    private ITopoService topoService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IReservationService reservationService;

    @RequestMapping("topos")
    public String listTopo(Model model,
                           Authentication auth,
                           @ModelAttribute(value = "succes", binding = false) String succes,
                           @ModelAttribute(value = "error", binding = false) String error,
                           @RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "items", required = false) Integer items) {

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        int page_size = items == null ? 10 : items;
        int page_number = page == null ? 0 : page;

        PageRequest pagerequest = PageRequest.of(page_number, page_size);

        Page<Topo> topos;
        User user = null;

        if(auth != null && auth.isAuthenticated())
            try {
                user = userService.findByUsernameOrEmailIfExist(auth.getName());
            } catch (EntityNotExistException ignored) { }

        topos = topoService.listAllWithNotUser(pagerequest, user);

        model.addAttribute("topos", topos);
        return "topos/topos";
    }

    @GetMapping("/users/newtopo")
    public String getTopoRegisterPage(Model model,
                                      @ModelAttribute(value = "succes", binding = false) String succes,
                                      @ModelAttribute(value = "error", binding = false) String error) {

        model.addAttribute("topo", new Topo());
        model.addAttribute("regions", Region.values());

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);

        return "/topos/users/registerNewTopo";
    }

    @PostMapping("/users/newtopo")
    public String postNewTopo(@ModelAttribute("topo") Topo topo,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {

        try {
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

            topo.setUser(user);
            topo.setAvailable(true);
            topo.setReleaseDate(new Date());

            topoService.createTopo(topo);

            redirectAttributes.addAttribute("succes", "Le topo à bien été mis en ligne.");
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (utilisateur introuvable). Veuillez nous excuser.");
        }
        return "redirect:/topos/users/newtopo";
    }

    @GetMapping("/users/topos")
    public String listPersonalTopo(Model model,
                                   HttpServletRequest request,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes,
                                   @ModelAttribute(value = "succes", binding = false) String succes,
                                   @ModelAttribute(value = "error", binding = false) String error) {

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        User user;

        try {
            user = userService.findByUsernameOrEmailIfExist(auth.getName());
        } catch (EntityNotExistException e) {
            model.addAttribute("error", "Une erreur c'est produite (utilisateur introuvable). Veuillez nous excuser");
            return "redirect:/";
        }

        PageRequest pageRequest = PageRequest.of(0, 9999999);

        Page<Topo> reservedTopo = topoService.listAllReservedForUser(pageRequest, user);
        Page<Topo> unReservedTopo = topoService.listAllNotReservedForUser(pageRequest, user);

        HashMap<Topo, String> reservedEmail = new HashMap<>();

        for(Topo t : reservedTopo){
            Optional<Reservation> res = reservationService.getConfirmedReservationForTopo(t);
            res.ifPresent(reservation -> reservedEmail.put(t, reservation.getUser().getEmail()));
        }

        model.addAttribute("reservedTopo", reservedTopo);
        model.addAttribute("unReservedTopo", unReservedTopo);
        model.addAttribute("reservedEmailByTopo", reservedEmail);
        return "topos/users/topos";
    }

    @GetMapping("/users/available")
    public String setTopoAvailable(HttpServletRequest request,
                                   @RequestParam("id") Integer id,
                                   RedirectAttributes redirectAttributes) {
        Topo topo;

        try {
            topo = topoService.findByIdIfExist(id);
        } catch (EntityNotExistException e){
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (topo introuvable). Veuillez nous excuser.");
            return "redirect:/users/panel";
        }

        Optional<Reservation> reservation;

        try {
            reservation = reservationService.getConfirmedReservationForTopo(topo);
            if(reservation.isPresent()) {
                reservationService.deleteReservation(reservation.get().getId());
            }

        } catch (EntityNotExistException ignored) { }

        try {
            topo.setCurrentReservation(null);
            topo.setAvailable(true);
            topoService.editTopo(topo, topo.getId());

        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (Topo introuvable). Veuillez nous excuser.");
            return "redirect:/users/panel";
        }
        redirectAttributes.addAttribute("succes", "Le topo est de nouveau disponible");
        return "redirect:/topos/users/topos";
    }
}


