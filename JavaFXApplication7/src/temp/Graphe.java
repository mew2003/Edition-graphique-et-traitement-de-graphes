/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

import javafx.scene.layout.AnchorPane;

/**
 *
 * @author mewen.derruau
 */
public abstract class Graphe {
	
    public abstract Noeud creerNoeud(double[] pos);
    
    public abstract Lien creerLien(Noeud noeud1, Noeud noeud2);
    
     /**
     * Permet d'obtenir l'élément cliqué sur l'interface
     * @param positions position X/Y de la souris au moment du click
     * @return Un object correspondant au noeud ou lien si un élément est en
     *         effet bien cliqué ou renvoie la valeur null si rien n'a été
     *         selectionné.
     */
    public abstract Object elementClicked(double[] positions, AnchorPane zoneDessin);
}
