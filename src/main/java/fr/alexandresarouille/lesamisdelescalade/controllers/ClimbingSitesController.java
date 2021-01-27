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

    @GetMapping("site")
    public String getSiteInformation(Model model, @RequestParam("siteid") Integer siteid) {
        try {
            ClimbingSite site = climbingSiteService.findByIdIfExist(siteid);
            PageRequest pageRequest = PageRequest.of(0, 10);

            Page<Commentary> coms = commentaryService.findByClimbingSite(pageRequest, site);

            model.addAttribute("site", site);
            model.addAttribute("coms", coms);
            model.addAttribute("postCommentary", new Commentary());

        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }
        return "climbingsite";
    }

    @PostMapping("site")
    public String postCommentaryOnSite(Model model,
                                       @ModelAttribute("postCommentary") Commentary commentary,
                                       @RequestParam("siteid") Integer siteid,
                                       Authentication auth) {

        if( auth == null || !auth.isAuthenticated())
            return "redirect:/";

        try {
            ClimbingSite site = climbingSiteService.findByIdIfExist(siteid);
            User user = userService.findByUsernameOrEmailIfExist(auth.getName());


            commentary.setClimbingSite(site);
            commentary.setUser(user);

            commentaryService.createCommentary(commentary);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }
        return getSiteInformation(model, siteid);
    }

    @GetMapping("editCommentary")
    public String editCommentary(Model model,
                                 @RequestParam("comid") Integer comid){

        try {
            Commentary com = commentaryService.findByIdIfExist(comid);
            System.out.println(com.getId());
            model.addAttribute("commentary", com);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }
        return "editCommentary";
    }

    @PostMapping("editCommentary")
    public String editCommentary(Model model,
                                 @RequestParam("comid") Integer comid,
                                 @ModelAttribute("commentary") Commentary commentary,
                                 RedirectAttributes redirectAttributes){


        try {
            commentary = commentaryService.editCommentary(commentary, comid);
        } catch (EntityNotExistException e) {
            e.printStackTrace();
        }
        redirectAttributes.addAttribute("siteid", commentary.getClimbingSite().getId());
        return "redirect:/sites/site";
    }
}
