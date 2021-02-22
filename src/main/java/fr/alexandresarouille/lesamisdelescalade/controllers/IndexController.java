package fr.alexandresarouille.lesamisdelescalade.controllers;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {


    @RequestMapping
    public String getIndexPage(Model model,
                               @ModelAttribute(value = "succes", binding = false) String succes,
                               @ModelAttribute(value = "error", binding = false) String error,
                               HttpSession httpSession) {

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);
        return "index";
    }
}
