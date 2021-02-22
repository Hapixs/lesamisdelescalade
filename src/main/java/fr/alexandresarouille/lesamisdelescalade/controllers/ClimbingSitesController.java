package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.Commentary;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Difficulty;
import fr.alexandresarouille.lesamisdelescalade.entities.enums.Region;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.ICommentaryService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sites")
public class ClimbingSitesController {

    @Autowired
    private IClimbingSiteService climbingSiteService;
    @Autowired
    private ICommentaryService commentaryService;
    @Autowired
    private IUserService userService;


    @RequestMapping
    public String getSiteList(Model model,
                              HttpSession session,
                              @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "items", required = false) Integer items,
                              @ModelAttribute(value = "climbingSiteFilter") ClimbingSite climbingSiteFilter,
                              @ModelAttribute(value = "succes", binding = false) String succes,
                              @ModelAttribute(value = "error", binding = false) String error) {

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
        model.addAttribute("error", error);
        model.addAttribute("succes", succes);


        return "climbingsites";
    }

    @GetMapping("site")
    public String getSiteInformation(Model model,
                                     @ModelAttribute(value = "succes", binding = false) String succes,
                                     @ModelAttribute(value = "error", binding = false) String error,
                                     @RequestParam("id") Integer siteid,
                                     RedirectAttributes redirectAttributes) {
        try {
            ClimbingSite site = climbingSiteService.findByIdIfExist(siteid);
            PageRequest pageRequest = PageRequest.of(0, 999999999);

            Page<Commentary> coms = commentaryService.findByClimbingSite(pageRequest, site);

            model.addAttribute("site", site);
            model.addAttribute("coms", coms);
            model.addAttribute("postCommentary", new Commentary());

        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (site introuvable). Veuillez nous excuser.");
            return "redirect:/index";
        }

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);

        return "climbingsite";
    }

    @PostMapping("site")
    public String postCommentaryOnSite(Model model,
                                       @ModelAttribute("postCommentary") Commentary commentary,
                                       @ModelAttribute(value = "succes", binding = false) String succes,
                                       @ModelAttribute(value = "error", binding = false) String error,
                                       RedirectAttributes redirectAttributes,
                                       @RequestParam("id") Integer siteid,
                                       Authentication auth) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/login";

        redirectAttributes.addAttribute("id", siteid);

        ClimbingSite site = null;
        try {
            site = climbingSiteService.findByIdIfExist(siteid);
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (site introuvable). Veuillez nous excuser.");
            return "redirect:/sites";
        }


        User user = null;
        try {
             user = userService.findByUsernameOrEmailIfExist(auth.getName());
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (utilisateur introuvable). Veuillez nous excuser.");
            return "redirect:/sites/site";
        }

        if(commentary.getContent().isEmpty()) {
            error = "Votre commentaire doit contenir du texte.";
        } else {
            commentary.setClimbingSite(site);
            commentary.setUser(user);

            commentaryService.createCommentary(commentary);
            succes = "Votre commentaire vient d'être poster.";
        }

        redirectAttributes.addAttribute("succes", succes);
        redirectAttributes.addAttribute("error", error);

        return "redirect:/sites/site";
    }

    @GetMapping("editCommentary")
    public String editCommentary(Model model,
                                 @ModelAttribute(value = "succes", binding = false) String succes,
                                 @ModelAttribute(value = "error", binding = false) String error,
                                 @RequestParam("comid") Integer comid,
                                 RedirectAttributes redirectAttributes){

        try {
            Commentary com = commentaryService.findByIdIfExist(comid);
            model.addAttribute("commentary", com);
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (commentaire introuvable). Veuillez nous excuser.");
            return "redirect:/sites";
        }

        model.addAttribute("succes", succes);
        model.addAttribute("error", error);

        return "editCommentary";
    }

    @PostMapping("editCommentary")
    public String editCommentary(Model model,
                                 @RequestParam("comid") Integer comid,
                                 @ModelAttribute("commentary") Commentary commentary,
                                 RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute("comid", comid);

        try {
            if(commentary.getContent().isEmpty()) {
                redirectAttributes.addAttribute("error","Le commentaire doit contenir du text.");
                redirectAttributes.addAttribute("commentary", commentary);
                return "redirect:/sites/editCommentary";
            }
            commentary = commentaryService.editCommentary(commentary, comid);
            redirectAttributes.addAttribute("succes", "Les commentaire vient d'être modifier.");
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (commentaire introuvable). Veuillez nous excuser.");
            return "redirect:/sites";
        }
        redirectAttributes.addAttribute("id", commentary.getClimbingSite().getId());
        return "redirect:/sites/site";
    }

    @RequestMapping("removeCommentary")
    public String removeCommentary(Model model,
                                   @RequestParam("comid") Integer comid,
                                   RedirectAttributes redirectAttributes) {
        Commentary com = null;
        try {
            com = commentaryService.findByIdIfExist(comid);
            commentaryService.deleteCommentary(com.getId());
            redirectAttributes.addAttribute("succes", "Le commentaire à bien été supprimer.");
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (commentaire introuvable). Veuillez nous excuser.");
            return "redirect:/sites";
        }

        redirectAttributes.addAttribute("id", com.getClimbingSite().getId());
        return "redirect:/sites/site";
    }

    @RequestMapping("tagSite")
    public String tagSite(Model model,
                          @RequestParam("siteid")Integer siteid,
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
}
