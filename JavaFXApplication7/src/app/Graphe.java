/*
 * Classe parent des graphes
 */
package app;

import javafx.scene.layout.AnchorPane;

/**
 * Permet les différents échanges avec un graphe quelconque
 * @author mewen.derruau
 */
public abstract class Graphe {

	/**
	 * Créer un noeud de la classe {@link app.Noeud}
	 * @param pos positions X/Y ou doit se situer le noeud
	 * @return le noeud créer
	 */
    public abstract Noeud creerNoeud(double[] pos);
    
    /**
     * Créer un lien de la classe {@link app.Lien}
     * @param noeud1 Premier noeud à relier
     * @param noeud2 Second noeud à relier
     * @return le lien créer
     */
    public abstract Lien creerLien(Noeud noeud1, Noeud noeud2);
    
     /**
     * Permet d'obtenir l'élément clique sur l'interface
     * @param positions position X/Y de la souris au moment du click
     * @return Un object correspondant au noeud ou lien si un élément est en
     *         effet bien clique ou renvoie la valeur null si rien n'a été
     *         trouvé.
     */
    public abstract Object elementClicked(double[] positions, AnchorPane zoneDessin);
    
    /**
     * Renvoie le noeud correspondant au libelle saisie en argument
     * @param libelle nom du noeud à rechercher
     * @return le noeud portant le nom du libelle
     */
    public abstract Noeud getNode(String libelle);
    
    /**
     * Modifie le nom d'un noeud
     * @param noeud noeud à modifier
     * @param nouveauNom nouveau nom du noeud
     */
    public abstract void modifNomNoeud(Noeud noeud, String nouveauNom);
    
    /**
     * Permet de modifier les positions X/Y d'un noeud
     * @param noeud noeud à modifier
     * @param pos nouvelles positions du noeud
     */
    public abstract void modifPos(Noeud noeud, double[] pos);
    
    /**
     * Permet de modifier les noeuds que relient un lien
     * @param lien Lien à modifier
     * @param noeuds nouveau noeuds que relient le lien
     */
    public abstract void modifLien(Lien lien, Noeud[] noeuds);
    
    public abstract void modifRadius(Noeud noeud, double radius);
}
