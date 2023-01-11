package tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import app.GrapheProbabiliste;
import app.Lien;
import app.LienProbabiliste;
import app.Noeud;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;

public class probabilite {
	
	private final static Color[] COULEUR = {Color.YELLOW, Color.CYAN, Color.INDIANRED};
	
	private final static DecimalFormat DF = new DecimalFormat("#.######");

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
		
		// Affichage graphique de la légende
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
					if (add && !firstFound && !secondFound) {
						/* Aucun des noeuds n'a été ajouté à un groupe déjà existant, 
						   donc crée un groupe et ajoute les deux dans ce groupe */
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
			// détection groupe transitoire
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

	public static boolean existenceChemin(Noeud depart, Noeud arrivee, GrapheProbabiliste graphe) {
	    // Stock les noeuds déjà visités
	    Set<Noeud> visited = new HashSet<>();
	    // Stock les noeuds qui doivent être visités
	    Queue<Noeud> queue = new LinkedList<>();
	    // ajoute le noeud de départ à la queue
	    queue.add(depart);

	    // continue de chercher tant que il y a des noeuds dans la queue
	    while (!queue.isEmpty()) {
	        // récupère le noeud suivant dans la queue en l'enlevant de celle ci
	        Noeud current = queue.poll();
	        // si le noeud courant est égal au noeud d'arrivee, il y a un chemin
	        if (current == arrivee) {
	            return true;
	        }
	        // met le noeud courant dans la liste des noeuds déjà visité
	        visited.add(current);
	        // récupère la liste de tous les liens du graphe
	        for (Lien lien : graphe.getListeLiens()) {
	            // vérifie si le noeud courant est le premier noeud de ce lien et le que le second noeud n'a pas été visité
	        	if (lien.getNoeuds()[0] == current && !visited.contains(lien.getNoeuds()[1])) {
	        		// si c'est le cas ajoute le second noeud dans la queue des noeuds qui doivent être visités
	                queue.add(lien.getNoeuds()[1]);
	            }
	        }
	    }
	    // si la queue est vide et qu'on a pas trouvé l'arrivée, il n'y a pas de chemin
	    return false;
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
    	
    	dialog.setTitle("existence d'un chemin");
    	
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
        	/* permet de pouvoir appuyer sur 'entré' pour valider */
        	Button okButton = (Button) dialog.getDialogPane().lookupButton(valider);
        	dialog.getDialogPane().getScene().setOnKeyPressed(event -> {
        	    if (event.getCode() == KeyCode.ENTER) {
        	        okButton.fire();
        	    }
        	});
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
	 * @param noeud1  le premier noeud (le départ)
	 * @param noeud2  le deuxième noeud (l'arrivée, peut être identique au premier)
	 * @param graphe  le graphe d'où sont issus les noeuds
	 */
	public static void showResultExistenceChemin(Noeud noeud1, Noeud noeud2, GrapheProbabiliste graphe) {
    	Dialog<ButtonType> result = new Dialog<>();
		
    	/* Si aucun des noeuds est null */
    	if (noeud1 != null && noeud2 != null) {
    		/* vérifie l'existence d'un chemin entre ces 2 noeuds et set le texte à afficher en fonction du résultat */
    		if (existenceChemin(noeud1, noeud2, graphe)) {
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
	
	/**
	 * Affiche la loi de probabilité de la matrice du graphe en argument 
	 * selon l'état originelle qu'affecte l'utilisateur et le nombre de transition choisi
	 * @param graphe graphe sur lequel est effectué la recherche de la loi de probabilité
	 */
	public static void showLoiDeProba(GrapheProbabiliste graphe) {
		// matrice du graphe (à inverser car elle est réceptionné dans le mauvais sens)
		double[][] originalMatrix = inverserMatrice(matriceDeTransition(graphe));
		if (originalMatrix == null) { //Si le graphe n'est pas valide
			System.err.println("Graphe non valide");
			return; 
		}
		// valeurs que l'utilisateur à choisi
		double[] userValue = demandeLoiValeur(graphe);
		if (userValue == null) { //Si les données de l'utilisateur ne sont pas valide
			System.out.println("Données saisies invalides");
			return; 
		}
		// résultat de la loi de probabilité
		double[] result = getLaw(originalMatrix, userValue);

		visualLawResult(graphe, result, userValue);
	}
	
	/**
	 * Permet d'afficher graphiquement le résultat du calcul de la loi de probabilité
	 * @param graphe graphe qui a été traité
	 * @param law loi obtenu
	 * @param userValue valeur saisie par l'utilisateur 
	 */
	public static void visualLawResult(GrapheProbabiliste graphe, double[] law, double[] userValue) {
	    Dialog<ButtonType> dialog = new Dialog<>();
	    ArrayList<Noeud> listeNoeud = graphe.getListeNoeuds();

	    HBox nodeName = new HBox();
	    HBox probability = new HBox();
	    VBox container = new VBox();
	    nodeName.setSpacing(5.0);
	    probability.setSpacing(5.0);

	    /*      Affiche tous les résultats sous le format suivant :
	     * 		NomNoeud1	|	NomNoeud2	|	...	  |   NomNoeudX
	     * 	   ProbaNoeud1  |  ProbaNoeud2  |   ...   |  ProbaNoeudX
	     */
	    for (int i = 0; i < listeNoeud.size(); i++) {
	        Label nameLabel = new Label(listeNoeud.get(i).getNom());
	        nameLabel.setMinSize(100.0, 25.0);
	        nameLabel.setAlignment(Pos.CENTER);
	        nameLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	        nodeName.getChildren().add(nameLabel);

	        Label probabilityLabel = new Label("" + law[i]);
	        probabilityLabel.setMinSize(100.0, 25.0);
	        probabilityLabel.setAlignment(Pos.CENTER);
	        probabilityLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	        probability.getChildren().add(probabilityLabel);
	    }

	    // Pour que le container ne soit pas sur le header text
	    container.setStyle("-fx-padding : 90px 0px 0px 10px");
	    container.getChildren().addAll(nodeName, probability);

	    /* Element déjà saisie par l'utilisateur et rappelé sous le format : 
	     * NomNoeud1: ProbaNoeud1, NomNoeud2: ProbaNoeud2, ...: ..., NomNoeudX: ProbaNoeudX,
	     * Pour un nombre de transition: NBTransition
	     */
	    String texte = "Résultat de la loi de probabilité pour les valeurs de départ :\n";
	    for (int i = 0; i < userValue.length - 1; i++) {
	        texte += listeNoeud.get(i).getNom() + ": " + userValue[i] + ", ";
	    }
	    texte += "\nPour un nombre de transition : " + userValue[userValue.length - 1];

	    dialog.setHeaderText(texte);
	    dialog.getDialogPane().getChildren().add(container);
	    dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

	    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
	    closeButton.managedProperty().bind(closeButton.visibleProperty());
	    dialog.setResizable(true);
	    dialog.getDialogPane().setMinHeight(190.0);
	    dialog.setTitle("Loi de probabilité");
	    dialog.showAndWait();
	}
	
	/**
	 * Permet d'obtenir la loi de probabilité d'un matrice selon des 
	 * noeuds de départ et un nombre de transition choisi
	 * @param originalMatrix matrice dont on cherche la loi de probabilité
	 * @param userValue Valeur de départ + nombre de transition
	 * 					Format : N + 1 (taille du tableau ou N représente 
	 *                           le nombre de noeud dans le graphe et 
	 *                           '1' le nombre de transition)
	 *                  Exemple : un graphe de 3 noeuds (N1, N2, N3)
	 *                  		 on souhaite avoir une probabilité de partir de N1 = 0.4
	 *                           et N2 = 0.6 avec un nombre de transition = 4
	 *                           l'array doit donc être constitué de la manière suivante :
	 *                           {0.4, 0.6, 0.0, 4.0}
	 * @return la loi de probabilité
	 */
	public static double[] getLaw(double[][] originalMatrix, double[] userValue) {
		// Initialisation d'un tableau de même taille que userValue
		double[] result = new double[userValue.length - 1];
		// Elevation de la matrice à la puissance donnée
		double[][] elevatedMatrix = power(originalMatrix, (int) userValue[userValue.length - 1]);
		// Multiplication de la matrice avec les valeurs utilisateur
		for (int i = 0; i < elevatedMatrix.length; i++) {
			for (int j = 0; j < elevatedMatrix[i].length; j++) {
				result[i] += userValue[j] * elevatedMatrix[j][i];
			}
		}
		return result;
	}
	
	/**
	 * Elevation d'un matrice à une puissance donnée
	 * @param matrice matrice à élever
	 * @param puissance puissance
	 * @return la matrice élever à la puissance
	 */
    public static double[][] power(double[][] matrice, int puissance) {
        double[][] result = new double[matrice.length][matrice.length];

        // Initialisation d'une matrice identité de même taille
        for (int i = 0; i < matrice.length; i++) {
            result[i][i] = 1;
        }

        // Copie de la matrice pour les calculs intermédiaire
        double[][] temp = matrice.clone(); 
        // Boucle d'appel multiplication matrice
        while (puissance > 0) {
            if (puissance % 2 == 1) {
                result = multiply(result, temp);
            }
            temp = multiply(temp, temp);
            puissance /= 2;
        }
        return result;
    }

    /**
     * Multiplie deux matrices entre elles
     * @param matriceA première matrice
     * @param matriceB deuxième matrice
     * @return le résultat de la multiplication des deux matrices
     */
    private static double[][] multiply(double[][] matriceA, double[][] matriceB) {
    	int nbRow = matriceA.length;
    	// Création d'un matrice de même taille
        double[][] nouvelleMatrice = new double[nbRow][nbRow];
        // Multiplication des matrices
        for (int i = 0; i < nbRow; i++) {
            for (int j = 0; j < nbRow; j++) {
                for (int k = 0; k < nbRow; k++) {
                	nouvelleMatrice[i][j] += matriceA[i][k] * matriceB[k][j];
                }
            }
        }
        return nouvelleMatrice;
    }
	
    /**
     * Interface graphique demandant à l'utilisateur de saisir ses valeurs 
     * pour le calcul de la loi de probabilité d'un graphe
     * @param graphe le graphe concernée
     * @return la saisie utilisateur sous le format suivant :
     *  	   {Noeud1Proba, Noeud2Proba, ..., NoeudXProba, NombreTransition}
     */
	public static double[] demandeLoiValeur(GrapheProbabiliste graphe) {
		ArrayList<Noeud> listeNoeud = graphe.getListeNoeuds();
		
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.setResizable(true);
	    dialog.setTitle("Loi de probabilité");
	    dialog.setHeaderText("Choisissez les valeurs d'origines :");

	    /* Ajout pour chaque noeud de son libellé 
	     * + d'une zone de texte à sa droite dans une grid
	     */
	    GridPane grid = new GridPane();
	    Label label = new Label();
	    TextField[] textFields = new TextField[listeNoeud.size()+1];
	    // permet de suivre le nombre de noeud/label/textField actuellement dans la grid
	    int i = 0; 
	    for (; i < listeNoeud.size(); i++) {
	    	label = new Label(listeNoeud.get(i).getNom());
	    	label.setMaxSize(125.0, 25.0);
	    	textFields[i] = new TextField();
	        grid.add(label, 1, i+1);
	        grid.add(textFields[i], 2, i+1);
	    }
	    // Ajout du label + textField concernant le nombre de transitions
	    label = new Label("Nombre de transitions");
	    textFields[i] = new TextField();
	    grid.add(label, 1, i+1);
	    grid.add(textFields[i], 2, i+1);
	    grid.setHgap(10.0);
	    grid.setVgap(5.0);

	    dialog.getDialogPane().setContent(grid);

	    // Ajout d'un bouton OK
	    ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().add(okButton);

	    /* Affiche la fenêtre graphique et attend une action de l'utilisateur
	     * la valeur retournée est le bouton sur lequel l'utilisateur à clique
	     * ou vide si il clique sur la croix pour fermer la fenêtre
	     */
	    Optional<ButtonType> result = dialog.showAndWait();
	    
	    // total des valeurs saisies par l'utilisateur sur l'interface graphique
	    double total = 0.0;
	    // creation d'un array du nombre de noeud présent dans le graphe
	    double[] values = new double[textFields.length];
	    try {
	    	if (Double.parseDouble(textFields[textFields.length - 1].getText()) < 0) {
		    	return null;
		    } else {
		    	values[values.length - 1] = Double.parseDouble(textFields[textFields.length - 1].getText());
		    }
	    } catch (Exception e) {
	    	return null;
	    }
	    if (result.isPresent() && result.get() == okButton) {
	        for (i = 0; i < textFields.length - 1; i++) {
	        	// Vérification que la saisie de l'utilisateur soir bien un nombre
	        	try {
		            values[i] = Double.parseDouble(textFields[i].getText());
			        total += values[i];
		            // Pas de valeur négative autorisé
		            if (values[i] < 0) {
		            	values = null;
		            }
		            // Dans le cas d'une exception bloqué tout ajout de valeur
	        	} catch (Exception e) {
	        		values = null;
	        	}
	        }
	    }
	    // valeur incorrect (la somme des probabilité de démarré à un noeud doit être égale à 1)
	    if (total != 1.0) {
	    	values = null;
	    }
	    return values;
	}
	
	/**
	 * Inverse le sens d'une matrice
	 * @param matrice matrice à inverse
	 * @return la matrice inversé
	 */
	public static double[][] inverserMatrice(double[][] matrice) {
		double[][] inverse = new double[matrice.length][matrice[0].length];
		for (int i = 0; i < matrice.length; i++) {
			for (int j = 0; j < matrice[i].length; j++) {
				inverse[j][i] = matrice[i][j];
			}
		}
		return inverse;
	}

	public static double probabiliteChemin(Noeud depart, Noeud arrivee, int transition, GrapheProbabiliste graphe) {
		ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
		int indexDepart = listeNoeuds.indexOf(depart);
		int indexArrivee = listeNoeuds.indexOf(arrivee);
		double[] values = new double[listeNoeuds.size() + 1];
		
		for (int i = 0 ; i < listeNoeuds.size() ; i++)  {
			if (i == indexDepart) {
				values[i] = 1;
			} else {
				values[i] = 0;
			}
		}
		values[values.length - 1] = transition;

		double[] matrix = getLaw(inverserMatrice(matriceDeTransition(graphe)), values);

		return matrix[indexArrivee];
	}

	/**
	 * Affiche la fenêtre pour le calcule de la probabilité d'un chemin 
	 * entre 2 noeuds en un certain nombre de transitions
	 * @param graphe  le graphe courant
	 */
	public static void showProbabiliteChemin(GrapheProbabiliste graphe) {
	
		Dialog<ButtonType> dialog = new Dialog<>();
	
		ComboBox<Noeud> comboBox1 = new ComboBox<>();
		ComboBox<Noeud> comboBox2 = new ComboBox<>();
		TextField transitions = new TextField();
		Label texteComboBox1 = new Label();
		Label texteComboBox2 = new Label();
		Label texteTransition = new Label();
		ArrayList<Noeud> listeNoeuds = graphe.getListeNoeuds();
		HBox contentH1 = new HBox();
		HBox contentH2 = new HBox();
		VBox contentV = new VBox();
		ButtonType valider = new ButtonType("Valider");
		
		dialog.setTitle("Probabilité d'un chemin");
		
		/* Si il y a des noeuds dans le graphe */
		if (verifierGraphe(graphe, false)) {
			/* gère la taille de la fenêtre et des boîtes pour que les éléments soient bien placés */
			dialog.getDialogPane().setMinHeight(200.0);
			dialog.getDialogPane().setMinWidth(400.0);
			contentH1.setSpacing(40.0);
			contentH1.setMinHeight(20.0);
			contentH1.setMinWidth(20.0);
			contentH2.setSpacing(40.0);
			contentH2.setMinHeight(20.0);
			contentH2.setMinWidth(20.0);
			contentV.setMinHeight(100.0);
			contentV.setMinWidth(200.0);
			contentV.setStyle("-fx-padding: 50px 0px 0px 10px;");
			contentV.setSpacing(10.0);
			
	     	dialog.setHeaderText("Testez la probabilité d'un chemin entre 2 noeuds en un certain nombre de transitions");
	     	/* récupère la liste des noeuds et l'ajoute dans 2 comboBox */
			comboBox1.getItems().addAll(listeNoeuds);
			comboBox2.getItems().addAll(listeNoeuds);
			/* gère la taille des comboBox */
	     	comboBox1.setMinWidth(150.0);
	     	comboBox2.setMinWidth(150.0);
	     	transitions.setMinWidth(150.0);
	     	/* gère la taille et le texte des labels */
	     	texteComboBox1.setMinWidth(150);
	     	texteComboBox1.setText("Noeud de départ :");
	     	texteComboBox2.setMinWidth(150 );
	     	texteComboBox2.setText("Noeud d'arrivée :");
	     	texteTransition.setMinWidth(150);
	     	texteTransition.setText("Nombre de transitions :");
	     	
	     	/* ajoute les labels dans une HBox pour qu'ils soient alignés horizontalement */
	     	contentH1.getChildren().add(texteComboBox1);
	     	contentH1.getChildren().add(texteComboBox2);
	     	contentH1.getChildren().add(texteTransition);
	     	/* ajoute les champs dans une HBox pour qu'ils soient alignés horizontalement */
	     	contentH2.getChildren().add(comboBox1);
	     	contentH2.getChildren().add(comboBox2);
	     	contentH2.getChildren().add(transitions);
	     	/* ajoute les 2 HBox dans une VBox pour qu'elles soient alignées verticalement */
	     	contentV.getChildren().add(contentH1);
	     	contentV.getChildren().add(contentH2);
	     	
	     	/* Ajoute la HBox et un bouton valider à la fenêtre*/
	     	dialog.getDialogPane().getChildren().add(contentV);
	    	dialog.getDialogPane().getButtonTypes().add(valider);
	    	/* permet de pouvoir appuyer sur 'entré' pour valider */
        	Button okButton = (Button) dialog.getDialogPane().lookupButton(valider);
        	dialog.getDialogPane().getScene().setOnKeyPressed(event -> {
        	    if (event.getCode() == KeyCode.ENTER) {
        	        okButton.fire();
        	    }
        	});
	     	
	    	/* execute le code à l'intérieur quand n'importe quel bouton est cliqué */
	    	dialog.setResultConverter(buttonType -> {
	    		/* si le bouton 'valider' est cliqué */
	    	    if (buttonType.equals(valider)) {
	    	    	/* récupère les noeuds sélectionnés dans les comboBox */
	    	    	Noeud noeudATester1 = comboBox1.getSelectionModel().getSelectedItem();
	    	    	Noeud noeudATester2 = comboBox2.getSelectionModel().getSelectedItem();
	    	    	String textTransitions = transitions.getText();
	    	    	try {
	    	    	    int nbTransitions = Integer.parseInt(textTransitions);
	    	    	    showResultProbabiliteChemin(noeudATester1, noeudATester2, nbTransitions, graphe);
	    	    	} catch (NumberFormatException e) {
	    	    	    showResultProbabiliteChemin(noeudATester1, noeudATester2, 0, graphe);
	    	    	}
	    	    }
	    	    /* ne sert à rien mais est obligatoire */
	    	    return null;
	    	});
	    	
			/* permet de faire fonctionner la croix en créant un bouton de type close invisible */
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
		    closeButton.managedProperty().bind(closeButton.visibleProperty());
		    closeButton.setVisible(false);
		    /* affiche la fenêtre */
		    dialog.showAndWait();
		}
	}
	
	/**
	 * Affiche la fenêtre de résultat de la probabilité d'un chemin 
	 * entre 2 noeud en un certain nombre de transitions
	 * @param noeud1  le premier noeud (le départ)
	 * @param noeud2  le deuxième noeud (l'arrivée, peut être identique au premier)
	 * @param nbTransitions  le nombre de transitions entré par l'utilisateur
	 * @param graphe  le graphe d'où sont issus les noeuds
	 */
	public static void showResultProbabiliteChemin(Noeud noeud1, Noeud noeud2, int nbTransitions, GrapheProbabiliste graphe) {
    	Dialog<ButtonType> result = new Dialog<>();
		
    	/* Si aucun des noeuds est null */
    	if (noeud1 != null && noeud2 != null && nbTransitions != 0) {
    		
    		Double proba = probabiliteChemin(noeud1, noeud2, nbTransitions, graphe);

    		result.setTitle("Probabiltié d'un chemin");
    		result.setHeaderText("En " + nbTransitions + " transition(s) il y a une probabilité de " + proba 
    				             + " de passer du " + noeud1 + " au " + noeud2);
    		/* permet de faire fonctionner la croix en créant un bouton de type close visible */
	        result.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButtonResult = result.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButtonResult.managedProperty().bind(closeButtonResult.visibleProperty());
            closeButtonResult.setVisible(true);
            /* affiche la fenêtre de résultat */
            result.showAndWait();
    	} else {
    		result.setTitle("Erreur");
    		result.setHeaderText("Une des valeurs saisient est nulle ou invalide");
    		result.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButtonResult = result.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButtonResult.managedProperty().bind(closeButtonResult.visibleProperty());
            closeButtonResult.setVisible(true);
            /* affiche la fenêtre de résultat */
            result.showAndWait();
    	}
	}
}
