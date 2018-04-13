package textes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import textes.model.Categorie;
import textes.model.Texte;
import textes.service.CategorieService;
import textes.service.TexteService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller pour l'export des données.
 */
@Controller
public class ExportController {

    @Autowired
    TexteService texteService;

    @Autowired
    CategorieService categorieService;


    @PostMapping("export")
    public ResponseEntity<String> export(Model model, HttpServletResponse response) {

        StringBuffer stringBuffer = new StringBuffer();

        List<Categorie> categories = categorieService.getAllCategories();

        for (Categorie categorie : categories) {
            String requete = "insert into categorie values ("
                    + categorie.getId() + ", '" + categorie.getLibelle().replace("'", "''") + "', " + categorie.getOrdre() + ");";

            stringBuffer.append(requete + "<br>");
        }

        List<Texte> textes = texteService.getAllTextes();

        for (Texte texte : textes) {
            String requete = "insert into texte(id, libelle, ordre, ingredients, etapes, id_categorie) values ("
                    + texte.getId() + ", '" + texte.getLibelle().replace("'", "''") + "', " + texte.getOrdre() + ", '"
                    + texte.getFirstText().replace("'", "''").replace("\r\n", "<br>") + "', '" + texte.getSecondText().replace("'", "''").replace("\r\n", "<br>") + "', " + texte.getCategorie().getId() + ");";

            stringBuffer.append(requete + "<br>");
        }

        stringBuffer.append("<br>");

        return new ResponseEntity<String>(stringBuffer.toString(), HttpStatus.OK);
    }
}
