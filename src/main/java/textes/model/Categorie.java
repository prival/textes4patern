package textes.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Categorie {

    private static final long serialVersionUID = -3009157732242241606L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "ordre")
    private int ordre;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "categorie")
    @OrderBy("ordre")
    private List<Texte> textes;

    protected Categorie() {
    }

    public Categorie(String libelle, int ordre) {
        this.libelle = libelle;
        this.ordre = ordre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public List<Texte> getTextes() {
        return textes;
    }

    public void setTextes(List<Texte> textes) {
        this.textes = textes;
    }

    @Override
    public String toString() {
        return String.format("Categorie[id=%d, libelle='%s', ordre='%d']", id, libelle, ordre);
    }
}
