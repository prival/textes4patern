package textes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import textes.model.Etude;
import textes.model.EtudeEnCours;
import textes.service.EtudeEnCoursService;
import textes.service.EtudeService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EtudeEnCoursController {

    @Autowired
    EtudeEnCoursService etudeEnCoursService;

    @GetMapping("/etudesEnCours")
    public String etudes(Model model) {

        List<EtudeEnCours> etudesEnCours = etudeEnCoursService.getAllEtudesEnCours();
        model.addAttribute("etudesEnCours", etudesEnCours);

        return "etudesEnCours";
    }

    @RequestMapping(value = { "/addEtudeEnCours" }, method = RequestMethod.POST)
    public String addEtude(
            HttpSession session,
            @RequestParam("nomEtude") String nomEtude) {

        EtudeEnCours etudeEnCours = new EtudeEnCours();
        etudeEnCours.setLibelle(nomEtude);
        etudeEnCours.setOrdre(1);
        etudeEnCoursService.createEtudeEnCours(etudeEnCours);

        return "redirect:/index";
    }

    @RequestMapping(value = { "/deleteEtudeEnCours" }, method = RequestMethod.POST)
    public String deleteEtude(
            HttpSession session,
            @RequestParam("idEtudeEnCours") int idEtudeEnCours) {

        etudeEnCoursService.deleteEtudeEnCours(idEtudeEnCours);

        return "redirect:/index";
    }

}
