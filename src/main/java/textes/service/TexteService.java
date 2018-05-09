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
import java.io.FileWriter;
import java.io.IOException;
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

    public void createTexte(@Valid @RequestBody Texte texte) {

        try {
            String categoriePath = "src//main//resources//etudes//" + texte.getCategorie().getLibelle() + "//";

            BufferedWriter bw = new BufferedWriter(new FileWriter(categoriePath + texte.getLibelle()));

            bw.write(texte.getFirstText());

            bw.close();

            bw = new BufferedWriter(new FileWriter(categoriePath + texte.getLibelle() + "~"));

            bw.write(texte.getSecondText());

            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTexte(int ordre, long id) { texteRepository.updateTexte(ordre, id);}

    public void deleteTexte(@Valid @RequestBody long id) {
        texteRepository.deleteById(id);
    }

}
