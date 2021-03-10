package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.IReservationService;
import fr.alexandresarouille.lesamisdelescalade.services.ITopoService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
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
                                       Authentication auth,
                                       @ModelAttribute(value = "succes", binding = false) String succes,
                                       @ModelAttribute(value = "error", binding = false) String error) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        return "user/panel";
    }
}
