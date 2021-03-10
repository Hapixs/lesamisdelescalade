package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IReservationService;
import fr.alexandresarouille.lesamisdelescalade.services.ITopoService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationsController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IReservationService reservationService;
    @Autowired
    private ITopoService topoService;

    @RequestMapping("/user/reservations")
    public String getPersonalReservations(
            Model model,
            HttpServletRequest request,
            Authentication auth,
            RedirectAttributes redirectAttributes,
            @ModelAttribute(value = "succes", binding = false) String succes,
            @ModelAttribute(value = "error", binding = false) String error){

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        if(!auth.isAuthenticated())
            return "redirect:/login";

        User user;

        try{
            user = userService.findByUsernameOrEmailIfExist(auth.getName());
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite.");
            return getPreviousPageByRequest(request).orElse("/");
        }

        Pageable pageable = PageRequest.of(0, 10);

        Page<Reservation> reservationPage = reservationService.listAllByUser(pageable, user);

        model.addAttribute("reservations", reservationPage);
        return "reservations/users/reservations";
    }

    @PostMapping("/reservation")
    public String postReservation(HttpServletRequest request,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes,
                                  @RequestParam("topoId") Integer topoId) {

        User user;
        Topo topo;

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        try{
            if(auth == null || !auth.isAuthenticated()) {
                redirectAttributes.addAttribute("error", "Merci de vous connecter pour effectuer cela.");
                return  "redirect:/topos/topos";
            }
            user = userService.findByUsernameOrEmailIfExist(auth.getName());
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite.");
            return  "redirect:/topos/topos";
        }

        try {
            topo = topoService.findByIdIfExist(topoId);
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite.");
            return  "redirect:/topos/topos";
        }

        Optional<Reservation> hasReservation = reservationService.getReservationByTopoAndUser(topo, user);

        if(hasReservation.isPresent()){
            redirectAttributes.addAttribute("error", "Vous avez déjà fait une réservation pour ce topo.");
            return  "redirect:/topos/topos";
        }

        Reservation reservation = new Reservation(user, topo);
        reservationService.createReservation(reservation);

        redirectAttributes.addAttribute("succes", "Votre réservation à bien été enregistrée.");
        return  "redirect:/topos/topos";
    }

    @GetMapping("/topos/reservations")
    public String listTopoReservations(Model model,
                                       HttpServletRequest request,
                                       @RequestParam("id") Integer id,
                                       RedirectAttributes redirectAttributes,
                                       @ModelAttribute(value = "succes", binding = false) String succes,
                                       @ModelAttribute(value = "error", binding = false) String error) {

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        Topo topo;

        try {
            topo = topoService.findByIdIfExist(id);
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (topo introuvable). Veuillez nous excuser.");
            return "redirect:/topos";
        }

        PageRequest pageRequest = PageRequest.of(0, 99999999);
        Page<Reservation> reservations = reservationService.listAllByTopo(pageRequest, topo);

        model.addAttribute("reservations", reservations);
        return "reservations/topos/reservations";
    }

    @PostMapping("/topos/reservations")
    public String confirmReservation(HttpServletRequest request,
                                     @RequestParam("id") Integer id,
                                     RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        Reservation reservation;

        try {
            reservation = reservationService.findByIdIfExist(id);
            reservation.setAccepted(true);
            reservationService.editReservation(reservation, reservation.getId());
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (Réservation introuvable). Veuillez nous excuser.");
            return getPreviousPageByRequest(request).orElse("/");
        }

        Topo topo = reservation.getTopo();
        topo.setCurrentReservation(reservation);
        topo.setAvailable(false);

        try {
            topoService.editTopo(topo, topo.getId());
            redirectAttributes.addAttribute("succes", "La réservation à bien été confirmée.");
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (Topo introuvable). Veuillez nous excuser.");
            return getPreviousPageByRequest(request).orElse("/");
        }

        return "redirect:/topos/users/topos";
    }

    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Referer")).map(url -> "redirect:" + url);
    }
}
