package v1;

import javafx.scene.layout.AnchorPane;

public abstract class Lien {

    public abstract Noeud[] getNoeuds();

    public abstract void setNoeuds(Noeud[] value);

    public abstract void dessinerLien(AnchorPane zoneDessin);

    public abstract Lien creerLien(Noeud[] noeuds);

}
