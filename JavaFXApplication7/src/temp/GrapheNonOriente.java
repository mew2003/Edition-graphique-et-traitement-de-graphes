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
public class GrapheNonOriente extends Graphe {

    GrapheNonOriente() {}

    @Override
    public Noeud creerNoeud(String nom, double[] pos, double radius) {
        return new NoeudNonOriente(nom, pos, radius);
    }

    @Override
    public Lien creerLien() {
        return null;
    }
    
    public String toString() {
        return "GrapheNonOriente";
    }
    
}
