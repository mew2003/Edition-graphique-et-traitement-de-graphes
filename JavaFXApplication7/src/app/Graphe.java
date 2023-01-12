/*
 * Classe parent des graphes
 */
package app;

import java.io.Serializable;
import java.util.List;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

/**
 * Permet les différents échanges avec un graphe quelconque
 * @author mewen.derruau
 */
public abstract class Graphe implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
     * @return le lien créé
     */
    public abstract Lien creerLien(Noeud noeud1, Noeud noeud2);
    
    /**
     * Supprime un lien, sa représentation graphique 
     * et sa présence dans la liste des éléments du graphe
     * @param lienASuppr lien à supprimer
     * @param zoneDessin zone graphique ou se situe le lien
     * @param listeElements liste d'élément dans laquelle se situe le lien
     */
	public abstract void supprimerLien(Lien lienASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements);
	
	/**
	 * Supprime un noeud, sa représentation graphique
	 * et sa présence dans la liste des éléments du graphe
	 * @param noeudASuppr noeud à supprimer
	 * @param zoneDessin zone graphique ou se situe le noeud
	 * @param listeElements liste d'élément dans laquelle se situe le noeud
	 */
	public abstract void supprimerNoeud(Noeud noeudASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements);

	/**
	 * Permet d'obtenir l'élément clique sur l'interface
	 * @param positions position X/Y de la souris au moment du click
	 * @param zoneDessin la zone sur laquelle c'est effectué le click
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
     * @param zoneDessin zone ou se trouve le lien 
     *                   (permet l'actualisation de l'affichage)
     */
    public abstract void modifLien(Lien lien, Noeud[] noeuds, AnchorPane zoneDessin);
    
    /**
     * Permet de modifier le radius d'un noeud
     * @param noeud noeud à modifier
     * @param radius nouveau radius du noeud
     */
    public abstract void modifRadius(Noeud noeud, double radius);
    
    /**
     * Reset l'affichage de tout les noeuds / liens d'un graphe
     * (après une sélection d'un élément)
     */
    public abstract void reset();
    
    /**
     * Permet d'actualiser tout les liens du graphe
     */
    public abstract void relocalisation();
    
    public abstract void setEtat(Graphe graphe);
    
    public abstract Graphe clone();

    /**
     * Permet d'obtenir la liste des noeuds
     * @return la liste des noeuds
     */
    public abstract List<Noeud> getListeNoeuds();
    
    /**
     * Permet d'obtenir la liste des liens
     * @return la liste des liens
     */
    public abstract List<Lien> getListeLiens();
}
