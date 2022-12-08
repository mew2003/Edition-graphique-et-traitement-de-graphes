package v1;

public abstract class Lien {

    public abstract Noeud[] getNoeuds();

    public abstract void setNoeuds(Noeud[] value);

    public abstract void dessinerLien();

    public abstract Lien creerLien(Noeud[] noeuds);

}
