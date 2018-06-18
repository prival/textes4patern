package textes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import textes.exception.ResourceNotFoundException;
import textes.model.Texte;
import textes.repository.TexteRepository;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class TexteService {

    @Autowired
    TexteRepository texteRepository;

    public List<Texte> getAllTextes() {
        return texteRepository.findAll();
    }

    public Texte getTexteById(@PathVariable(value = "id") Long id) {
        return texteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Texte", "id", id));
    }

    public Texte getTexteByLibelle(String nomCategorie, String nomTexte) {

        Texte texte = new Texte();

        try {
            File file = new File("src//main//resources//langues//" + nomCategorie + "//" + nomTexte);

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

    public void createTexte(@Valid @RequestBody Texte texte) {

        try {
            String categoriePath = "src//main//resources//etudes//" + texte.getCategorie().getLibelle() + "//";

            BufferedWriter bw = new BufferedWriter(new FileWriter(categoriePath + texte.getLibelle()));

            bw.write(texte.getTexte());

            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void updateTexte(int ordre, long id) { texteRepository.updateTexte(ordre, id);}

//    public void deleteTexte(@Valid @RequestBody long id) {
//        texteRepository.deleteById(id);
//    }

}
