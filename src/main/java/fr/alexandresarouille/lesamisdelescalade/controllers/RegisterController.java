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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private IUserService userService;


    @GetMapping
    public String getRegisterPage(Model model,
                                  @ModelAttribute(value = "succes", binding = false) String succes,
                                  @ModelAttribute(value = "error", binding = false) String error) {
        User user = new User();
        model.addAttribute("newUser", user);
        model.addAttribute("succes", succes);
        model.addAttribute("error", error);
        return "register";
    }

    @PostMapping
    public String registerNewUser(Model model,
                                  RedirectAttributes redirectAttributes,
                                  @ModelAttribute(value = "succes", binding = false) String succes,
                                  @ModelAttribute(value = "error", binding = false) String error,
                                  @ModelAttribute("newUser") User user) {

        try {
            if(user.getEmail().isEmpty()){
                error = "Merci d'indiquer une adresse mail valide.";
            } else if(user.getUsername().isEmpty() || user.getUsername().length()<4 || user.getUsername().length() > 16){
                error = "Votre nom d'utilisateur doit contenir entre 4 et 16 caractères.";
            } else if(user.getPassword().isEmpty() || user.getPassword().length() < 5) {
                error = "Votre mot de passe doit contenir au moins 5 caractères.";
            }

            if(error != null && !error.isEmpty())
                redirectAttributes.addAttribute("error", error);
            else {
                userService.createUserAccount(user);
                redirectAttributes.addAttribute("succes", "Votre compte à bien été créer, vous pouvez vous connecter.");
            }

        } catch (EntityAlreadyExistException e) {
            redirectAttributes.addAttribute("error", "Il semblerais qu'un compte avec la même adresse mail / nom d'utilisateur existe déjà.");
        }
        return "redirect:/register";
    }


}
