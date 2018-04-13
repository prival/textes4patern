package textes.controller;

import textes.component.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import textes.model.Categorie;
import textes.model.Texte;
import textes.service.CategorieService;
import textes.service.TexteService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller pour la partie Admin.
 */
@Controller
public class AdminController {

    @Autowired
    private User user;

    @Autowired
    CategorieService categorieService;

    @Autowired
    TexteService texteService;

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {

        session.setAttribute("User", user);

        List<Categorie> categories = categorieService.getAllCategories();

        // pour choisir une catégorie en créant une texte
        model.addAttribute("categories", categories);

        // on remet les catégories en session ....
        session.setAttribute("categoriesMenu", categories);

        // la texte à créer
        Texte texte = new Texte();
        model.addAttribute("texte", texte);

        return "admin";
    }

}
