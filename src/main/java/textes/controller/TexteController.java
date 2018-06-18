package textes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import textes.model.Categorie;
import textes.model.Texte;
import textes.service.CategorieService;
import textes.service.TexteService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller pour les textes.
 */
@Controller
public class TexteController {


    @Autowired
    TexteService texteService;

    @Autowired
    CategorieService categorieService;


    @GetMapping("/texte/{nomCategorie}/{nomTexte}")
    public String showTexte(@PathVariable String nomCategorie, @PathVariable String nomTexte, Model model) {
        Texte texte = texteService.getTexteByLibelle(nomCategorie, nomTexte);
        List<Categorie> categories = categorieService.getAllCategories();

        model.addAttribute("texte", texte);
        model.addAttribute("categories", categories);

        return "texte";
    }


    @RequestMapping(value = { "/addTexte" }, method = RequestMethod.POST)
    public String saveTexte(
            HttpSession session,
            @ModelAttribute("texte") Texte texte) {

        texteService.createTexte(texte);

        return "redirect:/index";
    }


    @GetMapping("/texte/edit/{id}")
    public String editTexte(@PathVariable long id, Model model) {

        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);

        Texte texte = texteService.getTexteById(id);

        model.addAttribute("texte", texte);

        return "texteEdit";
    }


    @PostMapping("/texte/update/{id}")
    public String updateTexte(
            @ModelAttribute("texte") Texte texte) {

        texteService.createTexte(texte);

        return "redirect:/texte/{id}";
    }


    @PostMapping("/texte/delete/{id}")
    public String deleteTexte(
            HttpSession session,
            @PathVariable  long id) {

        // TODO : faire par texte pour si besoin enlever le texte de la session

//        texteService.deleteTexte(id);

        return "redirect:/admin";
    }


    @PostMapping("modifierOrdreTextes")
    public void modifierOrdreTexte(
            @Valid @RequestBody List<Texte> textes,
            HttpSession session
    ) {
//        texteService.updateTexte(textes.get(0).getOrdre(), textes.get(0).getId());
//        texteService.updateTexte(textes.get(1).getOrdre(), textes.get(1).getId());
//        categorieService.createCategorie(id);

        session.setAttribute("categoriesMenu", categorieService.getAllCategories());

//        return ResponseEntity.ok();
//        return "redirect:/categorie";
    }
}
