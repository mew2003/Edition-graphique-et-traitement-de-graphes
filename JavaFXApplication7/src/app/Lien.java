/*
 * Classe parent des liens
 */
package app;

import javafx.scene.layout.AnchorPane;

/**
 * Permet les différents échanges avec un lien quelconques
 * @author mewen.derruau
 */
public abstract class Lien implements Cloneable {

	/**
	 * Renvoie les noeuds que relient un lien
	 * @return les noeuds relié
	 */
    public abstract Noeud[] getNoeuds();

    /**
     * Remplace les anciens noeuds que relié un lien par les nouveaux saisis en argument
     * @param value les nouveaux liens
     */
    public abstract void setNoeuds(Noeud[] value, AnchorPane zoneDessin);
    
    /**
     * Dessine sur la zone de dessin le lien
     * @param zoneDessin la zone graphique qui contient le dessin
     */
    public abstract void dessiner(AnchorPane zoneDessin);
    
    /**
     * Efface le lien sur la zone de dessin
     * @param zoneDessin la zone graphique qui contient le dessin
     */
    public abstract void effacer(AnchorPane zoneDessin);
    
    /**
     * Actualise l'affichage graphique du lien
     */
    public abstract void actualiser();
    
    public abstract Lien clone();
    
}
