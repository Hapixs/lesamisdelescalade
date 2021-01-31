package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AttributesController {

    @ModelAttribute
    public void addAttributes(Model model) {

        model.addAttribute("regions", Region.values());
        model.addAttribute("difficulties", Difficulty.values());

    }
}
