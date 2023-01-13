/*
 * Classe parente des graphes
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
	 * Crée un noeud de la classe {@link app.Noeud}
	 * @param pos  positions X/Y où doit se situer le noeud
	 * @return le noeud créé
	 */
    public abstract Noeud creerNoeud(double[] pos);
    
    /**
     * Crée un lien de la classe {@link app.Lien}
     * @param noeud1  Premier noeud à relier
     * @param noeud2  Second noeud à relier
     * @return le lien créé
     */
    public abstract Lien creerLien(Noeud noeud1, Noeud noeud2);
    
    /**
     * Supprime un lien, sa représentation graphique 
     * et sa présence dans la liste des éléments du graphe
     * @param lienASuppr  le lien à supprimer
     * @param zoneDessin  la zone graphique où se situe le lien
     * @param listeElements  la liste d'éléments dans laquelle se situe le lien
     */
	public abstract void supprimerLien(Lien lienASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements);
	
	/**
	 * Supprime un noeud, sa représentation graphique
	 * et sa présence dans la liste des éléments du graphe
	 * @param noeudASuppr  le noeud à supprimer
	 * @param zoneDessin  la zone graphique où se situe le noeud
	 * @param listeElements  la liste d'éléments dans laquelle se situe le noeud
	 */
	public abstract void supprimerNoeud(Noeud noeudASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements);

	/**
	 * Permet d'obtenir l'élément sur lequel on clique sur l'interface
	 * @param positions  la position X/Y de la souris au moment du clique
	 * @param zoneDessin  la zone sur laquelle c'est effectué le clique
	 * @return Un object correspondant au noeud ou au lien si un élément est en
	 *         effet bien cliqué ou renvoie la valeur null si rien n'a été
	 *         trouvé.
	 */
    public abstract Object elementClicked(double[] positions, AnchorPane zoneDessin);
    
    /**
     * Renvoie le noeud correspondant au libelle saisi en argument
     * @param libelle  nom du noeud à rechercher
     * @return le noeud portant le nom du libelle
     */
    public abstract Noeud getNode(String libelle);
    
    /**
     * Modifie le nom d'un noeud
     * @param noeud  noeud à modifier
     * @param nouveauNom  nouveau nom du noeud
     */
    public abstract void modifNomNoeud(Noeud noeud, String nouveauNom);
    
    /**
     * Permet de modifier les positions X/Y d'un noeud
     * @param noeud  noeud à modifier
     * @param pos  nouvelles positions du noeud
     */
    public abstract void modifPos(Noeud noeud, double[] pos);
    
    /**
     * Permet de modifier les noeuds que relie un lien
     * @param lien  le lien à modifier
     * @param noeuds  les nouveaux noeuds que relie le lien
     * @param zoneDessin  la zone où se trouve le lien (permet l'actualisation de l'affichage)
     */
    public abstract void modifLien(Lien lien, Noeud[] noeuds, AnchorPane zoneDessin);
    
    /**
     * Permet de modifier le radius d'un noeud
     * @param noeud  le noeud à modifier
     * @param radius  le nouveau radius du noeud
     */
    public abstract void modifRadius(Noeud noeud, double radius);
    
    /**
     * Reset l'affichage de tous les noeuds / liens d'un graphe
     * (après une sélection d'un élément, enlève le contour de tous les éléments non sélectionnés)
     */
    public abstract void reset();
    
    /**
     * Permet d'actualiser tous les liens du graphe
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
