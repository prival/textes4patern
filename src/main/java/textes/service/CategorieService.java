package textes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import textes.exception.ResourceNotFoundException;
import textes.model.Categorie;
import textes.model.Etude;
import textes.model.Texte;
import textes.repository.CategorieRepository;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategorieService {


    @Autowired
    CategorieRepository categorieRepository;

    public List<Categorie> getAllCategories() {

        File folder = new File("src//main//resources//langues//");
        File[] listOfFiles = folder.listFiles();

        List<Categorie> result = new ArrayList<Categorie>();

        for (int i = 0; i < listOfFiles.length; i++) {

            Categorie categorie = new Categorie();

            if (listOfFiles[i].isDirectory()) {
                String nomLangue = listOfFiles[i].getName();
                categorie.setLibelle(nomLangue);

                File folderLangue = new File("src//main//resources//langues//" + nomLangue);
                File[] listOfFilesLangue = folderLangue.listFiles();

                List<Texte> textes = new ArrayList<Texte>();

                for (int j = 0; j < listOfFilesLangue.length; j++) {
                    String fileName = listOfFilesLangue[j].getName();

                    // verifie que le nom du ficher ne se termine pas par "~"
                    if (listOfFilesLangue[j].isFile() && !"~".equals(fileName.substring(fileName.length() - 1))) {
                        Texte texte = new Texte();
                        texte.setLibelle(listOfFilesLangue[j].getName());
                        textes.add(texte);
                    }
                }

                categorie.setTextes(textes);
                result.add(categorie);
            }
        }

        return result;
    }


    public Texte getNotesLanguesByLibelle(String nomLangue) {

        Texte texte = new Texte();

        try {
            File file = new File("src//main//resources//langues//" + nomLangue + "_notes");

            List<String> lignes = Files.readAllLines(file.toPath());

            StringBuffer contenuTexte = new StringBuffer();

            for (String ligne : lignes) {
//                if (!"".equals(ligne)) {
                contenuTexte.append(ligne);
                contenuTexte.append("\n");
//                }
            }

            texte.setTexte(contenuTexte.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return texte;
        }
    }


    public Categorie getCategorieById(@PathVariable(value = "id") Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", "id", id));
    }

    public void createCategorie(@Valid @RequestBody Categorie categorie) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src//main//resources//langues//" + categorie.getLibelle()));

            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategorie(@Valid @RequestBody long id) {

    }
}
