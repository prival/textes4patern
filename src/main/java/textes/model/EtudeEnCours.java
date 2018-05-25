package textes.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class EtudeEnCours {

    private static final long serialVersionUID = -3009116532242241606L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "libelle", unique = true)
    private String libelle;

    @Column(name = "ordre")
    private int ordre;

    public EtudeEnCours() {}

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
}
