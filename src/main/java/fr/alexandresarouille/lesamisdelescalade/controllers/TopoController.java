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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("topos")
public class TopoController {

    @Autowired
    private ITopoService topoService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IReservationService reservationService;

    /*@GetMapping("register")
    public String registerTopo(Model model) {

        model.addAttribute("topo", new Topo());
        return "topos/register";
    }

    @PostMapping("register")
    public String createTopo(Model model,
                             @ModelAttribute("topo") Topo topo
                             ,Authentication auth) {

        if(auth == null || !auth.isAuthenticated())
            return "redirect:/";

        try {
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

            topo.setReleaseDate(new Date());
            topo.setUser(user);
            topo.setAvailable(true);

            topoService.createTopo(topo);

        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }

        return registerTopo(model);
    }*/

    @RequestMapping("topos")
    public String listTopo(Model model, Authentication auth) {

        PageRequest pagerequest = PageRequest.of(0, 10);

        Page<Topo> topos;
        User user = null;

        if(auth != null && auth.isAuthenticated())
            try {
                user = userService.findByUsernameOrEmailIfExist(auth.getName());
            } catch (EntityNotExistException e) {
                e.printStackTrace();
            }

        // TODO: remettre user
        topos = topoService.listAllWithNotUser(pagerequest, null);

        model.addAttribute("topos", topos);

        return "topos/topos";
    }

    @RequestMapping("askForReservation")
    public String createReservation(Model model,
                                    Authentication auth,
                                    @RequestParam("id") Integer id) {

        try {
            System.out.println("e");
            if(auth == null || !auth.isAuthenticated())
                return "redirect:/topos/topos";
            System.out.println("e");
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

            Topo topo = topoService.findByIdIfExist(id);

            Reservation reservation = new Reservation(user, topo);

            reservationService.createReservation(reservation);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }

        return "redirect:/topos/topos";
    }

    @RequestMapping("reservations")
    public String listReservations(Model model, @RequestParam("id") Integer id) {
        try {
            Topo topo = topoService.findByIdIfExist(id);
            PageRequest pageRequest = PageRequest.of(0, 10);

            Page<Reservation> reservations = reservationService.listAllByTopo(pageRequest, topo);

            model.addAttribute("reservations", reservations);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }

        return "/topos/reservations";
    }

    @RequestMapping("confirmReservation")
    public String confirmReservation(Model model, @RequestParam("id") Integer id) {
        try {
            Reservation reservation = reservationService.findByIdIfExist(id);
            Topo topo = new Topo();
            topo.setAvailable(false);

            topoService.editTopo(topo, reservation.getTopo().getId());
            Page<Reservation> res = reservationService.listAllByTopo(PageRequest.of(0, 99999999), reservation.getTopo());

            for(Reservation r : res.toList())
                if(!r.getId().equals(reservation.getId()))
                    reservationService.deleteReservation(r.getId());
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }

        return "redirect:/user/topos";
    }

    @RequestMapping("setavailable")
    public String setTopoAvailable(Model model, @RequestParam("id") Integer id) {
        try {
            Topo topo = topoService.findByIdIfExist(id);

            List<Reservation> reservationList = reservationService.listAllByTopo(PageRequest.of(0, 999999), topo).getContent();

            for(Reservation r : reservationList)
                reservationService.deleteReservation(r.getId());

            Topo changes = new Topo();
            changes.setAvailable(true);
            topoService.editTopo(changes, topo.getId());
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }
        return "redirect:/user/topos";
    }
}


