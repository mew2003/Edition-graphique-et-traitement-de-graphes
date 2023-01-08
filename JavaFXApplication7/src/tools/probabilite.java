package tools;

import java.util.ArrayList;
import java.util.HashMap;

import app.GrapheProbabiliste;
import app.Lien;
import app.LienProbabiliste;
import app.Noeud;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;

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
	 * @param showValidMessage false -> n'affiche pas de message si le graphe est valide
	 *                         true -> affiche un message dans tous les cas
	 * @return true si le graphe est valide, false sinon
	 */
	public static boolean verifierGraphe(GrapheProbabiliste graphe, boolean showValidMessage) {
		boolean result = true;
		ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
		if (listeNoeuds.size() < 1) result = false; // Moins d'un noeud sur le graphe
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
			if (values != 1.0) result = false;
		}
		showVerif(result, showValidMessage);
		return result;
	}
	
	/**
	 * Affichage graphique de la validité d'un graphe
	 * @param estValide graphe valide ou non
	 * @param showValidMessage false -> n'affiche pas de message si le graphe est valide
	 *                         true -> affiche un message dans tous les cas
	 */
	private static void showVerif(boolean estValide, boolean showValidMessage) {
		if (estValide && !showValidMessage) return;
		Alert alert = new Alert(estValide ? AlertType.INFORMATION : AlertType.ERROR);
		alert.setTitle("Vérification du graphe");
		alert.setHeaderText("Résultat : ");
		String text = estValide ? "Le graphe est valide !" : "Le graphe comporte des erreurs";
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	/**
	 * Calcule la matrice de transition d'un graphe
	 * @param graphe le graphe à calculer
	 */
	public static double[][] matriceDeTransition(GrapheProbabiliste graphe) {
		//Si le graphe n'es pas correct, ne pas effectuer le calcul
		if (!verifierGraphe(graphe, false)) return null;
		
		ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
		ArrayList<Lien> listeLiens = graphe.getListeLiens();
		LienProbabiliste lienP = null;
		
		double[][] matrix = new double[listeNoeuds.size()][listeNoeuds.size()];
		for (Lien lien : listeLiens) {
			lienP = (LienProbabiliste) lien;
			matrix[listeNoeuds.indexOf(lien.getNoeuds()[1])][listeNoeuds.indexOf(lien.getNoeuds()[0])] = lienP.getValue();
		}
		return matrix;
	}
	
	public static void showMatrix(GrapheProbabiliste graphe) {
		double[][] matrix = matriceDeTransition(graphe);
		if (matrix == null) return;
		int nbElements = graphe.getListeNoeuds().size();
		showMatrix(createVisualsMatrix(matrix, graphe), 50 * nbElements, 20 * nbElements);
	}
	
	public static HBox createVisualsMatrix(double[][] matrix, GrapheProbabiliste graphe) {
		HBox columns = new HBox();
		VBox row = new VBox();
		VBox newRow = new VBox();
		
		columns.getChildren().add(new VBox());
		row = (VBox) columns.getChildren().get(0);
		row.getChildren().add(new Label("X"));
		for (Noeud n : graphe.getListeNoeuds()) {
			row.getChildren().add(new Label(n.getNom()));
			newRow = new VBox();
			newRow.getChildren().add(new Label(n.getNom()));
			columns.getChildren().add(newRow);
		}
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				row = (VBox) columns.getChildren().get(i + 1);
				row.getChildren().add(new Label("" + matrix[i][j]));
			}
		}
		
		for (Node rows : columns.getChildren()) {
			row = (VBox) rows;
			for (Node cell : row.getChildren()) {
				((Label) cell).setMinSize(50, 20);
			}
		}
		
		return columns;
	}
	
	/**
	 * Affichage graphique de la matrice de transition
	 * @param columns tableau résultant du calcule de la matrice de transition
	 * @param width largeur des cellules
	 * @param height hauteur des cellules
	 */
	public static void showMatrix(HBox columns, double width, double height) {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.getDialogPane().getChildren().add(columns);
    	dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        dialog.getDialogPane().setMinSize(width + 50, height + 75);
        dialog.setResizable(true);
    	dialog.showAndWait();
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
