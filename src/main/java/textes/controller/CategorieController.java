package textes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import textes.model.Categorie;
import textes.service.CategorieService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller pour les catégories.
 */
@Controller
public class CategorieController {

    @Autowired
    CategorieService categorieService;



    @GetMapping("categorie/{id}")
    public String categorieId(@PathVariable long id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);

        model.addAttribute("categorie", categorie);

        return "categorie";
    }

    @GetMapping("categories")
    public String categories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);

        Categorie categorie = new Categorie("", 1);
        model.addAttribute("categorie", categorie);

        return "categories";
    }

    @RequestMapping(value = { "/categorie/add" }, method = RequestMethod.POST)
    public String saveCategorie(
            @ModelAttribute("categorie") Categorie categorie) {

        categorieService.createCategorie(categorie);

        return "redirect:/admin";
    }

    @GetMapping("categorie/edit/{id}")
    public String editCategorie(@PathVariable long id, Model model) {
        Categorie categorie = categorieService.getCategorieById(id);
        model.addAttribute("categorie", categorie);

        return "categorieEdit";
    }


    @PostMapping("categorie/update")
    public String updateCategorie(
            HttpSession session, @ModelAttribute("categorie") Categorie categorie) {

        categorieService.createCategorie(categorie);

        session.setAttribute("categoriesMenu", categorieService.getAllCategories());

        return "redirect:/categorie";
    }


    @PostMapping("categorie/delete/{id}")
    public String deleteCategorie(
            HttpSession session, @PathVariable long id) {

        categorieService.deleteCategorie(id);

        session.setAttribute("categoriesMenu", categorieService.getAllCategories());

        return "redirect:/categorie";
    }


    @PostMapping("modifierOrdreCategorie")
    public void modifierOrdreCategorie(
            @Valid @RequestBody List<Categorie> categories,
            HttpSession session
    ) {
        categorieService.createCategorie(categories.get(0));
        categorieService.createCategorie(categories.get(1));
//        categorieService.createCategorie(id);

        session.setAttribute("categoriesMenu", categorieService.getAllCategories());

//        return ResponseEntity.ok();
//        return "redirect:/categorie";
    }

}
