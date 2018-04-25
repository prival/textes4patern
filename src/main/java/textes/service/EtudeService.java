package textes.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import textes.model.Etude;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class EtudeService {

    public List<Etude> findAllEtudes() {
        File folder = new File("src//main//resources//etudes//");
        File[] listOfFiles = folder.listFiles();

        List<Etude> result = new ArrayList<Etude>();

        for (int i = 0; i < listOfFiles.length; i++) {

            Etude etude = new Etude();

            if (listOfFiles[i].isFile()) {
                etude.setNom(listOfFiles[i].getName());
                result.add(etude);
            }
//            else if (listOfFiles[i].isDirectory()) {
//            }
        }

        return result;
    }

    public void createEtude(@Valid @RequestBody Etude etude) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src//main//resources//etudes//"+etude.getNom()));

            bw.write(etude.getTexte());

            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLignesEtudeByNom(String nom) {

        File file = new File("src//main//resources//etudes//" + nom);

        List<String> paragraphes = new ArrayList<String>();

        try {
            List<String> lignes = Files.readAllLines(file.toPath());

            StringBuilder paragraphe = new StringBuilder();

            for (String ligne : lignes) {
                if (!"".equals(ligne)) {
                    paragraphe.append(ligne);
                    paragraphe.append("\n\r");
                }
                else {
                    paragraphes.add(paragraphe.toString());
                    paragraphe = new StringBuilder();
                }
            }

            if (!"".equals(paragraphe.toString())) {
                paragraphes.add(paragraphe.toString());
            }
        }
            catch (IOException e) {
            e.printStackTrace();
        }

        return paragraphes;
    }
}
