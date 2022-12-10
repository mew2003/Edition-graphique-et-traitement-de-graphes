package v1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

/**
 * Classe parent des liens
 * @author Mewen
 */
public abstract class Lien {

    /**
     * Permet d'obtenir les noeuds que relie un lien
     * @return les noeuds reliés
     */
    public abstract Noeud[] getNoeuds();

    /**
     * Modifie les noeuds que relie un lien
     * @param value la nouvelle liste de noeuds
     */
    public abstract void setNoeuds(Noeud[] value);

    /**
     * Dessine un lien sur la zone graphique passé en argument
     * @param zoneDessin zone graphique sur laquelle dessiner
     */
    public abstract void dessinerLien(AnchorPane zoneDessin);

    /**
     * Instanciation d'un lien
     * @param noeuds noeuds à raccorder par un lien
     * @return l'instanciation du lien
     */
    public abstract Lien creerLien(Noeud[] noeuds);
    
    /**
     * Renvoie la représentation graphique du lien
     * @return la représentation graphique
     */
    public abstract Line getLine();

}
