package textes.model;

public class Mark {

    String nomFichier;
    int noDecoupe;
    int noLigne;
    int offsetLeft;
    int offsetRight;

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public int getNoDecoupe() {
        return noDecoupe;
    }

    public void setNoDecoupe(int noDecoupe) {
        this.noDecoupe = noDecoupe;
    }

    public int getNoLigne() {
        return noLigne;
    }

    public void setNoLigne(int noLigne) {
        this.noLigne = noLigne;
    }

    public int getOffsetLeft() {
        return offsetLeft;
    }

    public void setOffsetLeft(int offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    public int getOffsetRight() {
        return offsetRight;
    }

    public void setOffsetRight(int offsetRight) {
        this.offsetRight = offsetRight;
    }
}
