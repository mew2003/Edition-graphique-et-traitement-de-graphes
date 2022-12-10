package v1;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 * Classe parent des noeuds
 * @author Mewen
 */
public abstract class Noeud {

    /**
     * Renvoie le nom du noeud
     * @return nom du noeud
     */
    public abstract String getNom();

    /**
     * Remplace l'ancien nom du noeud par la nouveau saisie en argument
     * @param value nouveau nom du noeud
     */
    public abstract void setNom(String value);
 
    /**
     * Renvoie les positions x,y du noeud
     * @return position x,y du noeud
     */
    public abstract double[] getPositions();

    /**
     * Remplace les anciennes positions du noeud par les nouvelles
     * @param positions nouvelles positions du noeud (x,y)
     */
    public abstract void setPositions(double[] positions);

    /**
     * Renvoie le radius du noeud
     * @return radius du noeud
     */
    public abstract double getRadius();

    /**
     * Remplace l'ancien radius du noeud par le nouveau
     * @param radius nouveau radius du noeud
     */
    public abstract void setRadius(double radius);

    /**
     * Dessine un noeud sur la zone graphique passé en argument
     * @param zoneDessin zone graphique sur laquelle dessiner
     */
    public abstract void dessinerNoeud(AnchorPane zoneDessin);

    /**
     * Instanciation d'un noeud
     * @param nom nom du noeud
     * @param pos position x,y du noeud
     * @param radius radius du noeud
     * @return l'instanciation du noeud
     */
    public abstract Noeud creerNoeud(String nom, double[] pos, double radius);
    
    /**
     * Renvoie la représentation graphique du noeud
     * @return la représentation graphique
     */
    public abstract Circle getCircle();

}
