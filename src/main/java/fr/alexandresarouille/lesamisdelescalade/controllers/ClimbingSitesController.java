package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sites")
public class ClimbingSitesController {

    @Autowired
    private IClimbingSiteService climbingSiteService;

    @RequestMapping
    public String getSiteList(Model model,
                              HttpSession session,
                              @ModelAttribute(value = "climbingSiteFilter") ClimbingSite climbingSiteFilter) {
        int page_size = 10;
        int page_number = 0;

        PageRequest pageRequest = PageRequest.of(page_number, page_size);

        climbingSiteFilter = climbingSiteFilter == null
                ? new ClimbingSite()
                : climbingSiteFilter;

        Page<ClimbingSite> page = climbingSiteService.listAllByFilter(pageRequest, climbingSiteFilter);

        model.addAttribute("climbingsites", page);
        model.addAttribute("climbingSiteFilter", climbingSiteFilter);
        model.addAttribute("difficultys", Difficulty.values());
        model.addAttribute("regions", Region.values());

        return "climbingsites";
    }
}
