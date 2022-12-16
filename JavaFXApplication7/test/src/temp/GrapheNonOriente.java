/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

import java.util.ArrayList;

/**
 *
 * @author mewen.derruau
 */
public class GrapheNonOriente extends Graphe {
	
	private ArrayList<Noeud> listeNoeuds = new ArrayList<>();
	private ArrayList<Lien> listeLiens = new ArrayList<>();

    GrapheNonOriente() {}

    @Override
    public Noeud creerNoeud(String nom, double[] pos, double radius) {
    	Noeud n = new NoeudNonOriente(nom, pos, radius);
    	listeNoeuds.add(n);
        return n;
    } 

    @Override
    public Lien creerLien(String noeud1Libelle, String noeud2Libelle) {
    	Noeud[] noeuds = new Noeud[2];
    	for (Noeud n : listeNoeuds) {
    		if (n.getNom().equals(noeud1Libelle)) {
    			noeuds[0] = n;
    		} else if (n.getNom().equals(noeud2Libelle)) {
    			noeuds[1] = n;
    		}
    	}
    	Lien l = new LienNonOriente(noeuds);
    	listeLiens.add(l);
        return l;
    }
    
    public String toString() {
    	String chaine = "GrapheNonOriente, noeuds [";
    	for (Noeud i : listeNoeuds) {
    		chaine += i.toString() + ", ";
    	}
    	chaine += "], liens [";
    	for (Lien i : listeLiens) {
    		chaine += i.toString() + ", ";
    	}
    	chaine += "]";
        return chaine;
    }
    
}
