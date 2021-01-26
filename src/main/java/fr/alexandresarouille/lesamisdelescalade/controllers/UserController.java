package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IClimbingSiteService siteService;

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



}
