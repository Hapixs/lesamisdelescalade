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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String listTopo(Model model, Authentication auth, RedirectAttributes redirectAttributes,
                           @ModelAttribute(value = "succes", binding = false) String succes,
                           @ModelAttribute(value = "error", binding = false) String error,
                           @RequestParam(value = "page", required = false) Integer page,
                           @RequestParam(value = "items", required = false) Integer items) {

        int page_size = items == null ? 10 : items;
        int page_number = page == null ? 0 : page;

        PageRequest pagerequest = PageRequest.of(page_number, page_size);

        Page<Topo> topos;

        User user = null;

        if(auth != null && auth.isAuthenticated())
            try {
                user = userService.findByUsernameOrEmailIfExist(auth.getName());
            } catch (EntityNotExistException e) {
                redirectAttributes.addAttribute("error", "Une erreur c'est produite lors de l'affichage des topo.");
                return "redirect:/index";
            }

        topos = topoService.listAllWithNotUser(pagerequest, user);

        model.addAttribute("topos", topos);

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        return "topos/topos";
    }

    @RequestMapping("askForReservation")
    public String createReservation(Model model,
                                    Authentication auth,
                                    @RequestParam("id") Integer id,
                                    RedirectAttributes redirectAttributes) {

        try {

            if(auth == null || !auth.isAuthenticated())
                return "redirect:/topos/topos";


            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

            Topo topo = topoService.findByIdIfExist(id);

            Optional<Reservation> hasReservation = reservationService.getReservationByTopoAndUser(topo, user);

            if(hasReservation.isPresent()){
                redirectAttributes.addAttribute("error", "Vous avez déjà fait une réservation pour ce topo.");
                return "redirect:/topos/topos";
            }
            redirectAttributes.addAttribute("succes", "Votre réservation à bien été enregistrée.");
            Reservation reservation = new Reservation(user, topo);

            reservationService.createReservation(reservation);
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (topo introuvable). Veuillez nous excuser.");
        }

        return "redirect:/topos/topos";
    }

    @RequestMapping("reservations")
    public String listReservations(Model model, @RequestParam("id") Integer id,
                                   @ModelAttribute(value = "succes", binding = false) String succes,
                                   @ModelAttribute(value = "error", binding = false) String error) {
        try {
            Topo topo = topoService.findByIdIfExist(id);
            PageRequest pageRequest = PageRequest.of(0, 99999999);

            Page<Reservation> reservations = reservationService.listAllByTopo(pageRequest, topo);

            model.addAttribute("reservations", reservations);

        } catch (EntityNotExistException e) {
            model.addAttribute("error", "Une erreur c'est produite (topo introuvable). Veuillez nous excuser.");
            return "/topos/reservations";
        }

        model.addAttribute("succes" ,succes);
        model.addAttribute("error", error);

        return "/topos/reservations";
    }

    @RequestMapping("confirmReservation")
    public String confirmReservation(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Reservation reservation = reservationService.findByIdIfExist(id);
            reservation.setAccepted(true);
            reservationService.editReservation(reservation, reservation.getId());

            Topo topo = reservation.getTopo();
            topo.setCurrentReservation(reservation);
            topo.setAvailable(false);
            topoService.editTopo(topo, topo.getId());
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (Réservation introuvable). Veuillez nous excuser.");
        }

        return "redirect:/user/topos";
    }

    @RequestMapping("setavailable")
    public String setTopoAvailable(Model model, @RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        Topo topo = null;
        try {
            topo = topoService.findByIdIfExist(id);
        } catch (EntityNotExistException e){
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (topo introuvable). Veuillez nous excuser.");
            return "redirect:/user/topos";
        }

        Optional<Reservation> reservation;

        try {

            reservation = reservationService.getConfirmedReservationForTopo(topo);

            if(reservation.isPresent()) {
                reservationService.deleteReservation(reservation.get().getId());
            }

            topo.setCurrentReservation(null);
            topo.setAvailable(true);
            topoService.editTopo(topo, topo.getId());

        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (Réservation introuvable). Veuillez nous excuser.");
        }

        return "redirect:/user/topos";
    }
}


