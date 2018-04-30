package textes.service;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import textes.model.Etude;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.text.BreakIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<String> getPhrasesEtudeByNom(String nom) {

        File file = new File("src//main//resources//etudes//" + nom);

        List<String> phrases = new ArrayList<String>();

        try {
            List<String> lignes = Files.readAllLines(file.toPath());

            for (String ligne : lignes) {

                BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.FRANCE);

                iterator.setText(ligne);
                int start = iterator.first();
                for (int end = iterator.next();
                     end != BreakIterator.DONE;
                     start = end, end = iterator.next()) {
                        phrases.add(ligne.substring(start,end));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return phrases;
    }

    public HashMap<String, Integer> getMotsEtudeByNom(String nom) {

        File file = new File("src//main//resources//etudes//" + nom);

        HashMap<String, Integer> result = new HashMap<String, Integer>();

        try {
            List<String> lignes = Files.readAllLines(file.toPath());

            for (String ligne : lignes) {

                ligne = ligne.replace(".", "");
                ligne = ligne.replace(",", "");
                ligne = ligne.replace(";", "");
                ligne = ligne.replace("?", "");
                ligne = ligne.replace("!", "");
                ligne = ligne.replace("(", "");
                ligne = ligne.replace(")", "");
                ligne = ligne.replace("[", "");
                ligne = ligne.replace("]", "");

                String[] mots = ligne.split(" ");

                for (int i=0; i< mots.length; i++) {
                    String mot = mots[i];
                    result = contenuDans(result, mot);
                }

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        result = sortHashMapByValues(result);

        return result;
    }

    public List<String> getMotsByTexte(String texte) {

        List<String> result = new ArrayList<String>();

        texte = texte.replace(".", "");
        texte = texte.replace(",", "");
        texte = texte.replace(";", "");
        texte = texte.replace("?", "");
        texte = texte.replace("!", "");
        texte = texte.replace("(", "");
        texte = texte.replace(")", "");
        texte = texte.replace("[", "");
        texte = texte.replace("]", "");

        String[] mots = texte.split(" ");

        for (int i=0; i< mots.length; i++) {
            String mot = mots[i];
            if (!contenuDans(result, mot)) {
                result.add(mot);
            }
        }

        return result;
    }

    public boolean contenuDans(List<String> mots, String motATrouve) {
        for (String mot : mots) {
            if(motATrouve.toLowerCase().equals(mot.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public HashMap<String, Integer> contenuDans(HashMap<String, Integer> mots, String motATrouve) {
        Iterator it = mots.entrySet().iterator();
        boolean trouve = false;

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            if(motATrouve.toLowerCase().equals(((String)pair.getKey()).toLowerCase())) {
                pair.setValue(((Integer)pair.getValue())+1);
                trouve = true;
            }
        }

        if (!trouve) {
            mots.put(motATrouve, 1);
        }

        return mots;
    }

    public LinkedHashMap sortHashMapByValues(HashMap passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = passedMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Integer)val);
                    break;
                }

            }

        }
        return sortedMap;

    }
}
