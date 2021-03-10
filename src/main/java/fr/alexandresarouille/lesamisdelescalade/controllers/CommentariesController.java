package fr.alexandresarouille.lesamisdelescalade.controllers;

import fr.alexandresarouille.lesamisdelescalade.entities.ClimbingSite;
import fr.alexandresarouille.lesamisdelescalade.entities.Commentary;
import fr.alexandresarouille.lesamisdelescalade.entities.User;
import fr.alexandresarouille.lesamisdelescalade.exception.EntityNotExistException;
import fr.alexandresarouille.lesamisdelescalade.services.IClimbingSiteService;
import fr.alexandresarouille.lesamisdelescalade.services.ICommentaryService;
import fr.alexandresarouille.lesamisdelescalade.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("commentaries")
public class CommentariesController {

    @Autowired
    private ICommentaryService commentaryService;
    @Autowired
    private IClimbingSiteService climbingSiteService;
    @Autowired
    private IUserService userService;


    @PostMapping
    public String postCommentary(
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Authentication auth,
            @ModelAttribute("postCommentary") Commentary commentary,
            @ModelAttribute(value = "succes", binding = false) String succes,
            @ModelAttribute(value = "error", binding = false) String error,
            @RequestParam("id") Integer siteid) {

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        redirectAttributes.addAttribute("id", siteid);

        ClimbingSite site;

        try {
            site = climbingSiteService.findByIdIfExist(siteid);
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (site introuvable). Veuillez nous excuser.");
            return getPreviousPageByRequest(request).orElse("/");
        }

        User user;

        try {
            user = userService.findByUsernameOrEmailIfExist(auth.getName());
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (utilisateur introuvable). Veuillez nous excuser.");
            return getPreviousPageByRequest(request).orElse("/");
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

        return getPreviousPageByRequest(request).orElse("/sites/site");
    }


    @RequestMapping("/admin/remove")
    public String deleteCommentary(HttpServletRequest request,
                                   @RequestParam("comid") Integer comid,
                                   RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("error", "");
        redirectAttributes.addAttribute("succes", "");

        Commentary com;

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

    @GetMapping("/admin/edit")
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

        return "commentaries/commentary";
    }

    @PostMapping("/admin/edit")
    public String editCommentary(
                                 @RequestParam("comid") Integer comid,
                                 @RequestParam("siteid") Integer siteid,
                                 @ModelAttribute("commentary") Commentary commentary,
                                 RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute("id", siteid);

        try {
            if(commentary.getContent().isEmpty()) {
                redirectAttributes.addAttribute("error","Le commentaire doit contenir du text.");
                redirectAttributes.addAttribute("commentary", commentary);
                redirectAttributes.addAttribute("comid", comid);
                return "redirect:/commentaries/admin/edit";
            }
            commentaryService.editCommentary(commentary, comid);
            redirectAttributes.addAttribute("succes", "Les commentaire vient d'être modifier.");
        } catch (EntityNotExistException e) {
            redirectAttributes.addAttribute("error", "Une erreur c'est produite (commentaire introuvable). Veuillez nous excuser.");
            return "redirect:/sites";
        }



        return "redirect:/sites/site";
    }

    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Referer")).map(url -> "redirect:" + url);
    }
}
