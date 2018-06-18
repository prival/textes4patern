package textes.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class Texte {

    private static final long serialVersionUID = -3009116532242241606L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "texte")
    @Type(type="text")
    private String texte;

    @Column(name = "ordre")
    private int ordre;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private Categorie categorie;

    public Texte() {}

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

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return String.format("Categorie[id=%d, libelle='%s', ordre='%d']", id, libelle, ordre);
    }
}
