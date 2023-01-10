package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import app.GrapheProbabiliste;
import app.Lien;
import app.LienProbabiliste;
import app.Noeud;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;

public class probabilite {
	
	private final static Color[] COULEUR = {Color.YELLOW, Color.CYAN, Color.INDIANRED};

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
		
		// Création de la matrice de n,n taille et attribution des toutes les valeurs du graphe
		double[][] matrix = new double[listeNoeuds.size()][listeNoeuds.size()];
		for (Lien lien : listeLiens) {
			lienP = (LienProbabiliste) lien;
			matrix[listeNoeuds.indexOf(lien.getNoeuds()[1])]
				  [listeNoeuds.indexOf(lien.getNoeuds()[0])] = lienP.getValue();
		}
		return matrix;
	}
	
	/**
	 * Calcule est affiche graphiquement la matrice d'un graphe
	 * @param graphe graphe à afficher
	 */
	public static void showMatrix(GrapheProbabiliste graphe) {
		double[][] matrix = matriceDeTransition(graphe);
		if (matrix == null) return; //Si le graphe n'est pas valide
		int nbElements = graphe.getListeNoeuds().size();
		showMatrix(createVisualsMatrix(matrix, graphe), 50 * nbElements, 20 * nbElements);
	}
	
	public static HBox createVisualsMatrix(double[][] matrix, GrapheProbabiliste graphe) {
		HBox columns = new HBox();
		VBox row = new VBox();
		VBox newRow = new VBox();
		
		// Affiche le nom de chaque noeud sur la première ligne et la première colonne
		columns.getChildren().add(new VBox());
		row = (VBox) columns.getChildren().get(0);
		row.getChildren().add(new Label("X"));
		for (Noeud n : graphe.getListeNoeuds()) {
			row.getChildren().add(new Label(n.getNom()));
			newRow = new VBox();
			newRow.getChildren().add(new Label(n.getNom()));
			columns.getChildren().add(newRow);
		}
		
		// Saisie de chaque valeur dans l'affichage graphique
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				row = (VBox) columns.getChildren().get(i + 1);
				row.getChildren().add(new Label("" + matrix[i][j]));
			}
		}
		
		// Pour la visibilité de la matrice
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
	    // create a set to store the nodes that have been visited
	    Set<Noeud> visited = new HashSet<>();
	    // create a queue to store the nodes that need to be visited
	    Queue<Noeud> queue = new LinkedList<>();
	    // add the starting node to the queue
	    queue.add(noeud1);

	    // continue searching while there are nodes in the queue
	    while (!queue.isEmpty()) {
	        // get the next node from the queue
	        Noeud current = queue.poll();
	        // if the current node is the destination node, we have found a path
	        if (current == noeud2) {
	            return true;
	        }
	        // mark the current node as visited
	        visited.add(current);
	        // get the links for the current node
	        for (Lien lien : graphe.getListeLiens()) {
	            // check if the current node is the first node in the link and the second node has not been visited
	            if (lien.getNoeuds()[0] == current && !visited.contains(lien.getNoeuds()[1])) {
	                queue.add(lien.getNoeuds()[1]);
	            // check if the current node is the second node in the link and the first node has not been visited
	            }
	        }
	    }
	    // if the queue is empty and we have not found the destination node, there is no path
	    return false;
	}

	/**
	 * Affiche visuellement la classification des sommets
	 * @param graphe le graphe dont ont cherche la classification
	 */
	public static void classificationSommets(GrapheProbabiliste graphe) {
		ArrayList<Noeud[]> group = getNodesGroup(graphe);
		ArrayList<Noeud>[] nodesStates = getNodesStates(group, graphe);
		
		// Attribution pour chaque noeud d'un classe un couleur pré-définie
		for (int i = 0; i < nodesStates.length; i++) {
			for (Noeud n : nodesStates[i]) {
				n.getCircle().setFill(COULEUR[i]);
			}
		}
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Légende");
		alert.setHeaderText("Légende des couleurs du graphe");
		String text = "Jaune = Transitoire\nCyan = Ergodique\nRouge = Absorbant";
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	/**
	 * Crée des groupes de noeuds qui ont un lien à double sens entre eux
	 * @param graphe le graphe dont ont cherche à regrouper les noeuds
	 * @return groupeNoeud les noeuds groupe
	 */
	public static ArrayList<Noeud[]> getNodesGroup(GrapheProbabiliste graphe) {
		ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
		ArrayList<Noeud[]> groupeNoeud = new ArrayList<>();
		/* Permet de créer une nouvelle ligne dans le tableau quand on doit créer un nouveau groupe
		 * (les noeuds ne correspondant à aucun des groupes déjà existant)
		 */
		boolean add = true; 
		// Le premier noeud est déjà présent dans un groupe, ajout de l'autre dans le même groupe
		boolean firstFound = false;
		// Deuxième noeud est déjà présent dans un groupe, ajout de l'autre dans le même groupe
		boolean secondFound = false;
		
		for (Noeud n : listeNoeuds) {
			for (Noeud nAComparer : listeNoeuds) {
				// Si il existe un chemin à double sens alors les deux noeuds font partis du même groupe
				if (existenceChemin(n, nAComparer, graphe) && existenceChemin(nAComparer, n, graphe)) {
					firstFound = false;
					secondFound = false;
					/* Création de l'emplacement du noeud (agrandissement d'un groupe 
					 * déjà existant, création d'un nouveau groupe ou ne fait rien selon la situation)
					 */
					for (int i = 0; i < groupeNoeud.size(); i++) {
						add = true;
						for (int j = 0; j < groupeNoeud.get(i).length; j++) {
							if (n == groupeNoeud.get(i)[j]) {
								// n se situe déjà dans le regroupement des noeuds
								firstFound = true;
								add = false;
							}
							if (nAComparer == groupeNoeud.get(i)[j]) {
								// nAComparer se situe déjà dans le regroupement des noeuds
								secondFound = true;
								add = false;
							}
						}
						if (firstFound && !secondFound) {
							//le noeud n est présent, ajouté nAComparer
							Noeud[] newGroup = new Noeud[groupeNoeud.get(i).length + 1];
							for (int k = 0; k < groupeNoeud.get(i).length; k++) {
								newGroup[k] = groupeNoeud.get(i)[k];
							}
							newGroup[newGroup.length - 1] = nAComparer;
							groupeNoeud.set(i, newGroup);
						} else if (!firstFound && secondFound) {
							//le noeud nAComparer est présent, ajouté n
							Noeud[] newGroup = new Noeud[groupeNoeud.get(i).length + 1];
							for (int k = 0; k < groupeNoeud.get(i).length; k++) {
								newGroup[k] = groupeNoeud.get(i)[k];
							}
							newGroup[newGroup.length - 1] = n;
							groupeNoeud.set(i, newGroup);
						}
					}
					if (add) {
						//Aucun des noeuds n'a été ajouté, ajouté les deux
						if (n == nAComparer) {
							groupeNoeud.add(new Noeud[1]);
							groupeNoeud.get(groupeNoeud.size() - 1)[0] = n;
						} else {
							groupeNoeud.add(new Noeud[2]);
							groupeNoeud.get(groupeNoeud.size() - 1)[0] = n;
							groupeNoeud.get(groupeNoeud.size() - 1)[1] = nAComparer;
						}
					}
				}
			}
		}
		return groupeNoeud;
	}
	
	/**
	 * Permet l'obtension des états de chaque noeud d'un groupe de noeud
	 * @param nodesGroup le groupe de noeud dont on cherche à attribuer la classification
	 * @param graphe graphe auquel appartient la liste des noeuds
	 * @return les noeuds classifiés
	 */
	public static ArrayList<Noeud>[] getNodesStates(ArrayList<Noeud[]> nodesGroup, GrapheProbabiliste graphe) {
		ArrayList<Noeud>[] result = new ArrayList[3];
		result[0] = new ArrayList<>(); //Transitoires
		result[1] = new ArrayList<>(); //Ergodiques
		result[2] = new ArrayList<>(); //Absorbant
		boolean transitoire = false;
		
		for (Noeud[] group : nodesGroup) {
			transitoire = false;
			for (Noeud[] groupAVerif : nodesGroup) {
				if (group != groupAVerif && existenceChemin(group[0], groupAVerif[0], graphe)) {
					transitoire = true;
					break;
				}
			}
			if (transitoire) {
				// groupe transitoire
				result[0].addAll(Arrays.asList(group));
			} else if (!transitoire && group.length != 1) {
				// groupe ergodique
				result[1].addAll(Arrays.asList(group));
			} else if (group.length == 1) {
				// groupe absorbant
				result[2].addAll(Arrays.asList(group));
			}
		}
		return result;
	}

	/**
	 * Affiche la fenêtre pour la vérification de l'existence d'un chemin entre 2 noeuds
	 * @param graphe  le graphe courant
	 */
	public static void showExistenceChemin(GrapheProbabiliste graphe) {
	
		Dialog<ButtonType> dialog = new Dialog<>();

    	ComboBox<Noeud> comboBox1 = new ComboBox<>();
    	ComboBox<Noeud> comboBox2 = new ComboBox<>();
    	ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
    	HBox contentH = new HBox();
    	ButtonType valider = new ButtonType("Valider");
    	
    	/* gère la taille de la fenêtre et des boîtes pour que les éléments soient bien placés */
    	dialog.getDialogPane().setMinHeight(130.0);
    	dialog.getDialogPane().setMinWidth(130.0);
    	contentH.setSpacing(40.0);
    	contentH.setMinHeight(20.0);
    	contentH.setMinWidth(20.0);
    	contentH.setStyle("-fx-padding: 50px 0px 0px 10px;");
    	
    	dialog.setTitle("Existance d'un chemin");
    	
    	/* Si il y a des noeuds dans le graphe */
    	if (listeNoeuds.size() != 0) {
         	dialog.setHeaderText("Testez l'existence d'un chemin entre 2 noeuds");
         	/* récupère la liste des noeuds et l'ajoute dans 2 comboBox */
    		comboBox1.getItems().addAll(listeNoeuds);
    		comboBox2.getItems().addAll(listeNoeuds);
    		/* gère la taille des comboBox */
         	comboBox1.setMinWidth(150.0);
         	comboBox2.setMinWidth(150.0);
         	/* ajoute les comboBox dans une HBox pour qu'elles soient aligné horizontalement */
         	contentH.getChildren().add(comboBox1);
         	contentH.getChildren().add(comboBox2);
         	/* Ajoute la HBox et un bouton valider à la fenêtre*/
         	dialog.getDialogPane().getChildren().add(contentH);
        	dialog.getDialogPane().getButtonTypes().add(valider);
         	
        	/* execute le code à l'intérieur quand n'importe quel bouton est cliqué */
        	dialog.setResultConverter(buttonType -> {
        		/* si le bouton 'valider' est cliqué */
        	    if (buttonType.equals(valider)) {
        	    	/* récupère les noeuds sélectionnés dans les comboBox */
        	    	Noeud noeudATester1 = comboBox1.getSelectionModel().getSelectedItem();
        	    	Noeud noeudATester2 = comboBox2.getSelectionModel().getSelectedItem();
        	    	showResultExistenceChemin(noeudATester1, noeudATester2, graphe);
        	    }
        	    /* ne sert à rien mais est obligatoire */
        	    return null;
        	});
        	
        /* si il n'y a pas de noeud dans le graphe */
    	} else {
         	dialog.setHeaderText("Il n'y a aucun noeud dans ce graphe");
    	}

    	/* permet de faire fonctionner la croix en créant un bouton de type close invisible */
    	dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        /* affiche la fenêtre */
        dialog.showAndWait();
	
	}
	
	/**
	 * Affiche la fenêtre de résultat de l'existence d'un chemin entre 2 noeud
	 * @param noeud1  le premier noeud
	 * @param noeud2  le deuxième noeud (peut être identique au premier)
	 * @param graphe  le graphe d'où sont issus les noeuds
	 */
	public static void showResultExistenceChemin(Noeud noeud1, Noeud noeud2, GrapheProbabiliste graphe) {
    	Dialog<ButtonType> result = new Dialog<>();
		
    	/* Si aucun des noeuds est null */
    	if (noeud1 != null && noeud2 != null) {
    		/* vérifie l'existence d'un chemin entre ces 2 noeuds et set le texte à afficher en fonction du résultat */
    		if (probabilite.existenceChemin(noeud1, noeud2, graphe)) {
	        	result.setHeaderText("Il existe au moins un chemin entre ces 2 noeuds");
	        } else {
	        	result.setHeaderText("Il n'y a pas de chemin entre ces 2 noeuds");
	        }
    		/* permet de faire fonctionner la croix en créant un bouton de type close visible */
	        result.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButtonResult = result.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButtonResult.managedProperty().bind(closeButtonResult.visibleProperty());
            closeButtonResult.setVisible(true);
            /* affiche la fenêtre de résultat */
            result.showAndWait();
    	}
	}
}





