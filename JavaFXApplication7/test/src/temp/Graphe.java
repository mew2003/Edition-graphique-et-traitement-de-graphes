/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

/**
 *
 * @author mewen.derruau
 */
abstract class Graphe {
	
    public abstract Noeud creerNoeud(String nom, double[] pos, double radius);
    
    public abstract Lien creerLien(String noeud1, String noeud2);
}
