package tools;

import java.util.ArrayList;
import java.util.HashMap;

import app.GrapheProbabiliste;
import app.Lien;
import app.LienProbabiliste;
import app.Noeud;

public class probabilite {

	/**
	 * Verifies si un graphe probabiliste est valide
	 * Pour qu'un graphe probabiliste soit considéré comme valide, 
	 * il doit respecter les points suivants :
	 * - Il doit comporter au minimum 1 noeud
	 * - La somme des liens partant d'un noeud doit être égale à 1 
	 *   pour tous les noeuds du graphe
	 * - Un lien doit posséder une valeur se situant entre 0 et 1
	 * @param grapheProbabiliste un graphe à vérifier
	 * @return true si le graphe est valide, false sinon
	 */
	public static boolean verifierGraphe(GrapheProbabiliste graphe) {
		ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
		if (listeNoeuds.size() < 1) return false; // Moins d'un noeud sur le graphe
		HashMap<Noeud, Double> nodesValues = new HashMap<>();
		
		for (Noeud n : listeNoeuds) {
			nodesValues.put(n, 0.0); // Valeur attribué a 0 par noeud par défaut
		}
		
		for (Lien lien : graphe.getListeLiens()) {
			LienProbabiliste lienP = (LienProbabiliste) lien;
			Noeud valuedNode = lienP.getNoeuds()[0];
			// Ajout pour chaque noeud d'une valeur d'un lien dont il est rattaché 
			nodesValues.put(valuedNode, nodesValues.get(valuedNode)+lienP.getValue());
		}
		
		for (Double values : nodesValues.values()) {
			if (values != 1.0) return false;
		}
		return true;
	}
	
	public static void matriceDeTransition() {
		
	}
	/**
	 * Vérifie l'existence d'un chemin entre 2 noeuds
	 * @param noeud1  le premier noeud
	 * @param noeud2  le deuxième noeud (peut être identique au premier)
	 * @param graphe  le graphe d'où sont issus les noeuds
	 * @return true si il y a un chemin, false si il n'y en a pas
	 */
	public static boolean existenceChemin(Noeud noeud1, Noeud noeud2, GrapheProbabiliste graphe) {
		boolean isChemin = false;
		/* parcours tous les liens du graphe */
		for (Lien lien : graphe.getListeLiens()) {
			/* si les 2 noeuds du lien sont différents et que le noeud1 du lien est identique à noeud1*/
			if (lien.getNoeuds()[0] != lien.getNoeuds()[1] && lien.getNoeuds()[0] == noeud1) {
				/* si le noeud2 du lien correspond au noeud2 alors il y a un chemin */
				if (lien.getNoeuds()[1] == noeud2) {
					return true;
				}
				/* vérifie si à partir du noeud suivant le chemin continue jusqu'au noeud2 */
				isChemin = existenceChemin(lien.getNoeuds()[1], noeud2, graphe);
			/* si les 2 noeuds du lien sont identique et que 
			 * le noeud1 est identique au noeud1 du lien et que le noeud2 est identique au noeud2 du lien 
			 * alors il y a un chemin entre ces 2 noeuds */
			} else if (noeud1 == lien.getNoeuds()[0] && noeud2 == lien.getNoeuds()[1]) {
				return true;
			}
		}
		return isChemin;
	}
}
