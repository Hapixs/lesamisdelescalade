package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.Commentary;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.ICommentaryService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/sites")
public class ClimbingSitesController {

    @Autowired
    private IClimbingSiteService climbingSiteService;
    @Autowired
    private ICommentaryService commentaryService;


    @RequestMapping
    public String getSiteList(Model model,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "items", required = false) Integer items,
                              @ModelAttribute(value = "climbingSiteFilter") ClimbingSite climbingSiteFilter,
                              @ModelAttribute(value = "succes", binding = false) String succes,
                              @ModelAttribute(value = "error", binding = false) String error) {

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        int page_size = items == null ? 12 : items;
        int page_number = page == null ? 0 : page;

        PageRequest pageRequest = PageRequest.of(page_number, page_size);

        climbingSiteFilter = climbingSiteFilter == null
                ? new ClimbingSite()
                : climbingSiteFilter;

        Page<ClimbingSite> sites = climbingSiteService.listAllByFilter(pageRequest, climbingSiteFilter);

        model.addAttribute("climbingsites", sites);
        model.addAttribute("climbingSiteFilter", climbingSiteFilter);
        model.addAttribute("difficultys", Difficulty.values());
        model.addAttribute("regions", Region.values());
        return "sites/sites";
    }

    @GetMapping("site")
    public String getSiteInformation(Model model,
                                     @ModelAttribute(value = "succes", binding = false) String succes,
                                     @ModelAttribute(value = "error", binding = false) String error,
                                     @RequestParam("id") Integer siteid,
                                     RedirectAttributes redirectAttributes) {

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);

        try {
            ClimbingSite site = climbingSiteService.findByIdIfExist(siteid);
            PageRequest pageRequest = PageRequest.of(0, 999999999);

            Page<Commentary> coms = commentaryService.findByClimbingSite(pageRequest, site);

            model.addAttribute("site", site);
            model.addAttribute("coms", coms);
            model.addAttribute("postCommentary", new Commentary());

        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (site introuvable). Veuillez nous excuser.");
            return "redirect:/sites/site";
        }

        return "sites/site";
    }

    @PutMapping("site")
    public String tagSite(@RequestParam("id")Integer siteid,
                          RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("id", siteid);

        try {
            ClimbingSite site = climbingSiteService.findByIdIfExist(siteid);
            site.setOfficial(!site.getOfficial());

            climbingSiteService.editClimbingSite(site, site.getId());
            redirectAttributes.addAttribute("succes", "Le site à bien été modifier.");
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (site introuvable). Veuillez nous excuser.");
            return "redirect:/sites";
        }
        return "redirect:/sites/site";
    }

    @GetMapping("/users/newsite")
    public String getSiteRegisterPage(Model model,
                                      @ModelAttribute(value = "succes", binding = false) String succes,
                                      @ModelAttribute(value = "error", binding = false) String error) {

        model.addAttribute("error", error);
        model.addAttribute("succes", succes);

        if(model.getAttribute("newSite") == null) model.addAttribute("newSite", new ClimbingSite());
        model.addAttribute("regions", Region.values());
        model.addAttribute("difficulties", Difficulty.values());

        return "sites/users/registerNewSite";
    }
    @PostMapping("/users/newsite")
    public String postNewSite(@ModelAttribute("newSite") ClimbingSite newSite,
                              RedirectAttributes redirectAttributes) {


        if(newSite.getDescription() == null
                || newSite.getDisplayName() == null
                || newSite.getDifficulty() == null
                || newSite.getLength() == null
                || newSite.getRegion() == null
                || newSite.getSectorAmount() == null
                || newSite.getWayAmount() == null ) {
            redirectAttributes.addAttribute("error", "Le site n'as pas été ajouter, veuillez remplir tout les champs.");
            redirectAttributes.addAttribute("newSite", newSite);

            return "redirect:/sites/users/newsite";
        }


        climbingSiteService.createClimbingSite(newSite);
        redirectAttributes.addAttribute("succes", "Le site à bien été partager.");
        return "redirect:/users/panel";
    }
}
