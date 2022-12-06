package v1;

public abstract class Lien {

    private Noeud[] noeuds = new Noeud[2];

    public Noeud[] getNoeuds() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.noeuds;
    }

    public void setNoeuds(Noeud[] value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.noeuds = value;
    }

    public void dessinerLien() {
    }

    public Lien(List<Noeud> noeuds);

}
