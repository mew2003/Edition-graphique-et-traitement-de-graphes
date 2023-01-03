/*
 * Classe parent des noeuds
 */
package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 * Permet les différents échanges avec un noeud quelconques
 * @author mewen.derruau
 */
public abstract class Noeud {

	/**
	 * Renvoie les positions X/Y du noeud
	 * @return les positions
	 */
    public abstract double[] getPositions();

    /**
     * Renvoie le radius du noeud
     * @return le radius
     */
    public abstract double getRadius();

    /**
     * Renvoie le nom du noeud
     * @return le nom
     */
    public abstract String getNom();

    /**
     * Modifie les positions du noeud pas les nouvelles saisies en argument
     * @param positions nouvelles positions du noeud
     */
    public abstract void setPositions(double[] positions);

    /**
     * Modifie le radius du noeud par le nouveau saisie en argument
     * @param radius nouveau radius du noeud
     */
    public abstract void setRadius(double radius);
    
    /**
     * Dessine sur la zone de dessin le noeud
     * @param zoneDessin la zone graphique qui contient le dessin
     */
    public abstract void dessiner(AnchorPane zoneDessin);
    
    /**
     * Modifie le nom du noeud par le nouveau en argument
     * @param nom nouveau nom du noeud
     */
    public abstract void setNom(String nom);

	public abstract Circle getCircle();
}
