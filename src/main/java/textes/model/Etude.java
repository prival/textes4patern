package textes.model;

public class Etude {

    private static final long serialVersionUID = -3009116532231241706L;

    private String nom;

    private String texte;

    public Etude() {}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }
}
