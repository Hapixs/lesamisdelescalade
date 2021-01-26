package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityAlreadyExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private IUserService userService;


    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        User user = new User();
        model.addAttribute("newUser", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(Model model, @ModelAttribute("newUser") User user) {
        try {
            userService.createUserAccount(user);
        } catch (EntityAlreadyExistException e) {
            e.printStackTrace();
        }
        return "redirect:/register";
    }
}
