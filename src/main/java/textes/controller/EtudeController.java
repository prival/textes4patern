package textes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import textes.model.Etude;
import textes.service.EtudeService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
            HttpSession session,
            @ModelAttribute("etude") Etude etude) {

        etudeService.createEtude(etude);

        return "redirect:/index";
    }


    @GetMapping("/etude/{nom}")
    public String etudes(@PathVariable String nom, Model model) {
        List<String> paragraphes = etudeService.getLignesEtudeByNom(nom);

        model.addAttribute("paragraphes", paragraphes);

        return "etude";
    }


    @GetMapping("/paragraphe/{index}")
    public String paragraphe(@PathVariable String index, Model model) {

        return "etude";
    }

}
