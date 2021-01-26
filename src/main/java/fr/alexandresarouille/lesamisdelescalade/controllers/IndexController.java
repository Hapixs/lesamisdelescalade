package fr.alexandresarouille.lesamisdelescalade.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {


    @RequestMapping
    public String getIndexPage(Model model,
                               HttpSession httpSession) {

        return "index";
    }
}
