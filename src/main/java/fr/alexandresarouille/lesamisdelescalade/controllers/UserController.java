package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.Reservation;
import fr.alexandresarouille.lesamisdelescalade.entities.Topo;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.IReservationService;
import fr.alexandresarouille.lesamisdelescalade.services.ITopoService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IClimbingSiteService siteService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITopoService topoService;
    @Autowired
    private IReservationService reservationService;


    @RequestMapping("/panel")
    public String getUserPersonalPanel(Model model,
                                       HttpSession httpSession,
                                       Authentication auth) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/";

        return "user/panel";
    }

    @GetMapping("/registerNewSite")
    public String getSiteRegisterPage(Model model,  Authentication auth) {
        if( auth == null || !auth.isAuthenticated())
            return "redirect:/";

        model.addAttribute("newSite", new ClimbingSite());
        model.addAttribute("regions", Region.values());
        model.addAttribute("difficulties", Difficulty.values());

        return "user/registerNewSite";
    }
    @PostMapping("/registerNewSite")
    public String postNewSite(Model model,
                              @ModelAttribute("newSite") ClimbingSite newSite,
                              Authentication auth) {
        if( auth == null || !auth.isAuthenticated())
            return "redirect:/";

        siteService.createClimbingSite(newSite);

        return getSiteRegisterPage(model, auth);
    }


    @RequestMapping("/topos")
    public String listPersonalTopo(Model model, Authentication auth) {
        if( auth == null || !auth.isAuthenticated())
            return "redirect:/";

        try {
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Topo> topos = topoService.listByUser(pageRequest, user);
            HashMap<Integer, User> reservationCompletes = new HashMap<>();
            for(Topo t : topos.toList()) {
                if(!t.getAvailable()) {
                    List<Reservation> reservations = reservationService.listAllByTopo(PageRequest.of(0, 1), t).getContent();
                    reservationCompletes.put(t.getId(), reservations.get(0).getUser());
                }
            }
            model.addAttribute("reservationsComplete", reservationCompletes);
            model.addAttribute("topos", topos);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }

        return "user/topos";
    }

    @GetMapping("/registerNewTopo")
    public String getTopoRegisterPage(Model model) {

        model.addAttribute("topo", new Topo());
        model.addAttribute("regions", Region.values());

        return "/user/registerNewTopo";
    }
    @PostMapping("/registerNewTopo")
    public String postNewTopo(Model model,
                              @ModelAttribute("topo") Topo topo,
                              Authentication auth) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/";

        try {
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

            topo.setUser(user);
            topo.setAvailable(true);
            topo.setReleaseDate(new Date());

            topoService.createTopo(topo);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }
        return getTopoRegisterPage(model);
    }

}
