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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
                                       Authentication auth,
                                       @ModelAttribute(value = "succes", binding = false) String succes,
                                       @ModelAttribute(value = "error", binding = false) String error) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        return "user/panel";
    }

    @GetMapping("/registerNewSite")
    public String getSiteRegisterPage(Model model,
                                      Authentication auth,
                                      @ModelAttribute(value = "succes", binding = false) String succes,
                                      @ModelAttribute(value = "error", binding = false) String error) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

        if(model.getAttribute("newSite") == null) model.addAttribute("newSite", new ClimbingSite());
        model.addAttribute("regions", Region.values());
        model.addAttribute("difficulties", Difficulty.values());

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        return "user/registerNewSite";
    }
    @PostMapping("/registerNewSite")
    public String postNewSite(Model model,
                              @ModelAttribute("newSite") ClimbingSite newSite,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

        if(newSite.getDescription() == null
        || newSite.getDisplayName() == null
        || newSite.getDifficulty() == null
        || newSite.getLength() == null
        || newSite.getRegion() == null
        || newSite.getSectorAmount() == null
        || newSite.getWayAmount() == null ) {
            redirectAttributes.addAttribute("error", "Le site n'as pas été ajouter, veuillez remplir tout les champs.");
            redirectAttributes.addAttribute("newSite", newSite);

            return "redirect:/user/registerNewSite";
        }


        siteService.createClimbingSite(newSite);
        redirectAttributes.addAttribute("succes", "Le site à bien été partager.");
        model.addAllAttributes(redirectAttributes.asMap());

        return "redirect:/user/panel";
    }


    @RequestMapping("/topos")
    public String listPersonalTopo(Model model, Authentication auth,
                                   @ModelAttribute(value = "succes", binding = false) String succes,
                                   @ModelAttribute(value = "error", binding = false) String error) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

        try {
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());

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

        } catch (EntityNotExistException e) {
            model.addAttribute("error", "Une erreur c'est produite (utilisateur introuvable). Veuillez nous excuser");
        }

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);

        return "user/topos";
    }

    @GetMapping("/registerNewTopo")
    public String getTopoRegisterPage(Model model,
                                      @ModelAttribute(value = "succes", binding = false) String succes,
                                      @ModelAttribute(value = "error", binding = false) String error) {

        model.addAttribute("topo", new Topo());
        model.addAttribute("regions", Region.values());

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);

        return "/user/registerNewTopo";
    }
    @PostMapping("/registerNewTopo")
    public String postNewTopo(Model model,
                              @ModelAttribute("topo") Topo topo,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

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
        return "redirect:/user/topos";
    }

}
