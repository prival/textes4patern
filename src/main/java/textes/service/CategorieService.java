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
import textes.repository.CategorieRepository;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                categorie.setLibelle(listOfFiles[i].getName());
                result.add(categorie);
            }
        }

        return result;
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
