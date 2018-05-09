package textes.service;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import textes.model.Commentaire;
import textes.model.Etude;

import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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

    /**
     * Découpe le fichier en 50 pages avec un nombre de lignes moyen par page
     * @param nom
     * @return
     */
    public List<List<String>> getDecoupe50EtudeByNomAvecLignes(String nom) {

        File file = new File("src//main//resources//etudes//" + nom);

        List<List<String>> decoupes = new ArrayList<List<String>>();

        try {
            int nbLignes = countLines("src//main//resources//etudes//" + nom);

            int nbLignesParDecoupe = nbLignes / 50;
            int nbCurrentLigneDecoupe = 1;

            List<String> lignes = Files.readAllLines(file.toPath());

            List<String> paragraphes = new ArrayList<String>();

            for (String ligne : lignes) {
                paragraphes.add(ligne);
                if (nbCurrentLigneDecoupe > nbLignesParDecoupe) {
                    decoupes.add(paragraphes);
                    paragraphes = new ArrayList<String>();
                    nbCurrentLigneDecoupe = 0;
                }

                nbCurrentLigneDecoupe++;
            }

            if (paragraphes.size()>0) {
                decoupes.add(paragraphes);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return decoupes;
    }


    /**
     * Découpe un texte en 50 pages (si possible) en utilisant une moyenne de mots à avoir par page
     * mais en ne coupant pas au milieu d'une ligne
     * A chaque page faite, on recalcule le nombre de mots restant pour redefinir la moyenne de mots pour les pages restantes
     * @param nom
     * @return
     */
    public List<List<String>> getDecoupe50EtudeByNomAvecMots(String nom) {

        File file = new File("src//main//resources//etudes//" + nom);

        List<List<String>> decoupes = new ArrayList<List<String>>();

        try {
            int currentIndexLigne = 0; // ligne courante
            int currentIndexPage = 0; // page courante de la decoupe

            // nb mots total
            int nbMots = countWords("src//main//resources//etudes//" + nom, 0);

            // nb de mots moyens par page
            int nbMotsParDecoupe = nbMots / 50;

            // nb courant de mots contenu dans la page en cours
            int nbCurrentMotDecoupe = 0;

            List<String> lignes = Files.readAllLines(file.toPath());

            List<String> paragraphes = new ArrayList<String>();

            for (String ligne : lignes) {
                currentIndexLigne++;
                paragraphes.add(ligne);

                // si on a dépassé le nb de mots à avoir dans la page
                if (nbCurrentMotDecoupe > nbMotsParDecoupe) {

                    // nouvelle page
                    decoupes.add(paragraphes);
                    paragraphes = new ArrayList<String>();
                    currentIndexPage++;
                    nbCurrentMotDecoupe = 0;

                    // recalcul du nb moyen de mots
                    nbMots = countWords("src//main//resources//etudes//" + nom, currentIndexLigne);

                    nbMotsParDecoupe = nbMots / (50-currentIndexPage);
                }

                nbCurrentMotDecoupe += ligne.split(" ").length;
            }

            if (paragraphes.size()>0) {
                decoupes.add(paragraphes);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return decoupes;
    }


    /**
     * Avoir un listing des mots par page, avec nombre d'occurences pour chaque mot
     * @param decoupes
     * @return
     */
    public List<HashMap<String, Integer>> getMotsDecoupe(List<List<String>> decoupes) {

        Pattern p = Pattern.compile("^[a-zA-ZÉÀéèêàùîôç]");

        List<HashMap<String, Integer>> mots = new ArrayList<HashMap<String, Integer>>();

        for (List<String> decoupe : decoupes) {
            HashMap<String, Integer> motsDecoupe = new HashMap<String, Integer>();

            List<String> motsTrouve = new ArrayList<String>();

            for (String ligne : decoupe) {
//                String[] motsLigne = ligne.split("\\W+|^[éèêàùçÉÀ]");
                String[] motsLigne = ligne.split("[ .!?,;’'()]");

                for (int i=0; i< motsLigne.length; i++) {
                    String motLigne = motsLigne[i];
                    Matcher m = p.matcher(motLigne);
                    if (!"".equals(motLigne) && m.find() && motLigne.length()>3) {
                        motsTrouve.add(motLigne);
                    }
                }
            }

            Collections.sort(motsTrouve);

            String currentMot = "";

            for (String mot : motsTrouve) {
                if (!currentMot.equals(mot)) {
                    currentMot = mot;
                    motsDecoupe.put(mot, 1);
                }
                else {
                    motsDecoupe.put(mot, motsDecoupe.get(mot) + 1);
                }
            }

            mots.add(motsDecoupe);
        }

        return mots;
    }

    public List<String> getCommentairesEtude(String nomFichier) {

        List<String> result = new ArrayList<String>();

        try {
            File file = new File("src//main//resources//etudes//~commentaires//" + nomFichier);

            List<String> lignes = Files.readAllLines(file.toPath());

            StringBuffer ligneCommentaire = new StringBuffer();

            int nextPage = 1;

            for (String ligne : lignes) {
                if (("~~"+nextPage).equals(ligne)) {
                    if (nextPage>1) {
                        result.add(ligneCommentaire.toString());
                        ligneCommentaire = new StringBuffer();
                    }
                    nextPage++;
                }
                else {
                    ligneCommentaire.append(ligne);
                }
            }
        }
        catch (NoSuchFileException e) {
            try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src//main//resources//etudes//~commentaires//" + nomFichier));

            for(int i=1;i<=50;i++) {
                bw.write("~~"+i);
                bw.write("\n");
            }

            bw.close();
            e.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public void saveCommentaire(Commentaire commentaire) {
        File file = new File("src//main//resources//etudes//~commentaires//" + commentaire.getNom());

        try {
            List<String> lignes = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

            int currentPosition = 0;

            for (String ligne : lignes) {
                currentPosition++;
                if (("~~"+commentaire.getPage()).equals(ligne)) {

                    int nextPage = commentaire.getPage() + 1;

                    while (!("~~"+nextPage).equals(lignes.get(currentPosition))) {
                        lignes.remove(currentPosition);
                    }
                    break;
                }
            }

            lignes.add(currentPosition, commentaire.getCommentaire());

            Files.write(file.toPath(), lignes, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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


    /**
     * Compte le nombre de lignes dans un fichier
     * @param filename
     * @return
     * @throws IOException
     */
    public int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }


    /**
     * Compte le nombre de mots dans un fichier à partir d'une ligne index
     * @param filename
     * @param index
     * @return
     * @throws IOException
     */
    public int countWords(String filename, int index) throws IOException {

        int count=0;
        int currentIndex = 0;

        List<String> lignes = Files.readAllLines((new File(filename)).toPath());

        for (String ligne : lignes) {
            currentIndex++;
            if (currentIndex>=index) {
                count += ligne.split(" ").length;
            }
        }

        return count;
    }
}
