/*
 * Classe parent des liens
 */
package app;

import javafx.scene.layout.AnchorPane;

/**
 * Permet les différents échanges avec un lien quelconques
 * @author mewen.derruau
 */
public abstract class Lien {

	/**
	 * Renvoie les noeuds qu'un lien relie
	 * @return les noeuds reliés
	 */
    public abstract Noeud[] getNoeuds();

    /**
     * Remplace les anciens noeuds qu'un lien reliait, par les nouveaux saisis en arguments
     * @param value  les nouveaux liens
     */
    public abstract void setNoeuds(Noeud[] value, AnchorPane zoneDessin);
    
    /**
     * Dessine le lien sur la zone de dessin
     * @param zoneDessin  la zone graphique qui contient le dessin
     */
    public abstract void dessiner(AnchorPane zoneDessin);
    
    /**
     * Efface le lien sur la zone de dessin
     * @param zoneDessin  la zone graphique qui contient le dessin
     */
    public abstract void effacer(AnchorPane zoneDessin);
    
    /**
     * Actualise l'affichage graphique du lien
     */
    public abstract void actualiser();
    
}
