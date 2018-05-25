package textes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import textes.model.Commentaire;
import textes.model.Etude;
import textes.model.Mark;
import textes.service.EtudeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EtudeController {

    @Autowired
    EtudeService etudeService;

    @GetMapping("/etudes")
    public String etudes(Model model) {

        List<Etude> etudes = etudeService.findAllEtudes();
        model.addAttribute("etudes", etudes);

        Etude etude = new Etude();
        model.addAttribute("etude", etude);

        return "etudes";
    }

    @RequestMapping(value = { "/addEtude" }, method = RequestMethod.POST)
    public String addEtude(
            @ModelAttribute("etude") Etude etude) {

        etudeService.createEtude(etude);

        return "redirect:/index";
    }

    @RequestMapping(value = { "/deleteEtude" }, method = RequestMethod.POST)
    public String deleteEtude(
            @ModelAttribute("etude") Etude etude) {

        etudeService.deleteEtude(etude);

        return "redirect:/index";
    }


    @GetMapping("/etude/{nom}")
    public String etudes(@PathVariable String nom, Model model, HttpSession session) {
        List<String> paragraphes = etudeService.getLignesEtudeByNom(nom);

//        session.setAttribute("paragraphes", paragraphes);

        model.addAttribute("paragraphes", paragraphes);

        return "etude";
    }


    @PostMapping("/paragraphe/{index}")
    public String paragraphe(@PathVariable String index, Model model, HttpSession session) {

        List<String> paragraphes = (List<String>) session.getAttribute("paragraphes");

        String paragraphe = paragraphes.get(Integer.parseInt(index));

        List<String> mots = etudeService.getMotsByTexte(paragraphe);

        model.addAttribute("mots", mots);

        return "paragraphe";
    }


    @GetMapping("/mots/{nom}")
    public String mots(@PathVariable String nom, Model model, HttpSession session) {
        List<List<String>> decoupes = etudeService.getDecoupe50EtudeByNomAvecMots(nom);

//        List<String> decoupes = etudeService.getLignesEtudeByNom(nom);

        List<HashMap<String, Integer>> mots = etudeService.getMotsDecoupe(decoupes);

//        List<HashMap<String, Integer>> mots = etudeService.getMotsLignes(decoupes);

        model.addAttribute("nom", nom);
//        model.addAttribute("decoupes", decoupes);
        model.addAttribute("mots", mots);

        return "mots";
    }


    @GetMapping("/decoupe/{nom}")
    public String decoupe2(@PathVariable String nom, Model model, HttpSession session) {
        List<List<String>> decoupes = etudeService.getDecoupe50EtudeByNomAvecMots(nom);

        List<String> commentaires = etudeService.getCommentairesEtude(nom);

//        List<HashMap<String, Integer>> mots = etudeService.getMotsDecoupe(decoupes);

        model.addAttribute("nom", nom);
        model.addAttribute("decoupes", decoupes);
        model.addAttribute("commentaires", commentaires);
//        model.addAttribute("mots", mots);

        return "decoupe";
    }


    @PostMapping("/saveCommentaire")
    public ResponseEntity<?> saveCommentaireViaAjax(
            Model model,
            @Valid @RequestBody Commentaire commentaire, Errors errors) {

        etudeService.saveCommentaire(commentaire);


        List<String> commentaires = etudeService.getCommentairesEtude(commentaire.getNom());
        model.addAttribute("commentaires", commentaires);

        return ResponseEntity.ok("ok");
    }


    @GetMapping("/commentaires/{nom}")
    public String commentaires(@PathVariable String nom, Model model, HttpSession session) {

        List<String> commentaires = etudeService.getCommentairesEtude(nom);

        model.addAttribute("nom", nom);
        model.addAttribute("commentaires", commentaires);

        return "commentaires";
    }


    @PostMapping("/markMot")
    public ResponseEntity<?> markMotViaAjax(
            Model model,
            @Valid @RequestBody Mark mark, Errors errors) {

        etudeService.markMot(mark);

        return ResponseEntity.ok("ok");
    }


    @PostMapping("/unmarkMot")
    public ResponseEntity<?> unmarkMotViaAjax(
            Model model,
            @Valid @RequestBody Mark mark, Errors errors) {

        etudeService.unmarkMot(mark);

        return ResponseEntity.ok("ok");
    }
}
