/*
 * Contrôleur principal de l'application (gère la partie interface graphique)
 */
package javafxapplication7;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import app.FactoryGraphe;
import app.FactoryGrapheManager;
import app.Graphe;
import app.GrapheOrientePondere;
import app.GrapheProbabiliste;
import app.Lien;
import app.LienNonOriente;
import app.LienOriente;
import app.LienOrientePondere;
import app.LienProbabiliste;
import app.Noeud;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import tools.probabilite;

/**
 * Contrôleur de l'application
 */
public class FXMLDocumentController implements Initializable {
    
    // Liste des éléments avec interactions contenu dans l'interface
    @FXML
    private TextField noeud1Lien;
    @FXML
    private TextField temp1;
    @FXML
    private TextField temp2;
    @FXML
    private TextField temp3;
    @FXML
    private TextField temp4;
    @FXML
    private AnchorPane editionProprietesLien;
    @FXML
    private TextField posXNoeud;
    @FXML
    private TextField valeurLien;
    @FXML
    private AnchorPane editionProprietesNoeud;
    @FXML
    private TextField posYNoeud;
    @FXML
    private AnchorPane zoneDessin;
    @FXML
    private TextField nomNoeud;
    @FXML
    private TextField radiusNoeud;
    @FXML
    private TextField noeud2Lien;
    @FXML
    private ComboBox<Object> listeElements;
    @FXML
    private Button validerModifNoeud;
    @FXML
    private Button supprimerNoeudButton;
    @FXML
    private Button supprimerLienButton;
    @FXML
    private Button validerModifLien;
    @FXML
    private AnchorPane aside;
    @FXML
    private MenuItem nonOrienteButton;
    @FXML
    private MenuItem orienteButton;
    @FXML
    private MenuItem orientePondereButton;
    @FXML
    private MenuItem probabilisteButton;
    @FXML
    private MenuItem verifierGrapheId;
    @FXML
    private MenuItem existenceCheminId;
    @FXML
    private MenuItem matriceDeTransitionId;
    @FXML
    private MenuItem probabiliteCheminId;
    @FXML
    private Menu listeClassificationID;
    @FXML
    private MenuItem classificationSommetsID;
    @FXML
    private MenuItem legendeID;
    @FXML
    private MenuItem loiDeProbabiliteID;
    @FXML
    private MenuItem enregistrerSousID;
    @FXML
    private MenuItem enregistrerID;
    @FXML
    private MenuItem ouvrirID;
    @FXML
    private MenuItem manuelUtilisationButton;
    
    // Création du manager permettant de créer toutes les factories
    FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    
    // Factory de graphe
    FactoryGraphe factory;
    
    // Graphe actuellement utilisé
    Graphe graphe;
      
    // Permet le stockage des noeuds à relier par un lien (maximum 2 noeuds)
    Noeud[] noeudARelier = new Noeud[2];
    
    // Object actuellement sélectionner par l'utilisateur depuis l'outil de sélection
    Object selectedObject;
    
    // Cercle permettant de voir en avance un noeud qui peut être crée par l'utilisateur
    Circle previewedCircle = new Circle(-100, -100, 25);
    
    // Ligne permettant de voir en avance un lien qui peut être crée par l'utilisateur
    Line previewedLine = new Line(-100, -100, -100, -100);
    
    /* Mode dans lequel se trouve l'utilisateur
     * 0 = Initialisation
     * 1 = Noeud
     * 2 = Lien
     * 3 = Outil sélection
     */
    int actualMode = 0;
    
    // Permet de savoir si le graphe possède une sauvegarde
    boolean aUneSauvegarde = false;
    
    // Sélectionneur de fichier
    FileChooser fileChooser = new FileChooser();
    
    // Permet d'obtenir le dernier fichier sauvegarder
    File lastFile = null;
    
    // Fichier de sauvegarde de l'état du graphe avant une action
    File graphSave = new File("undo");
    
    // Fichier de sauvegarde de l'état du graphe après une action
    File graphSave2 = new File("redo");
    
    // titre par défaut du logiciel
    String title = "Logiciel d'édition et de traitement de graphes";
    
    /* création des combinaisons de touches pour tous les raccourcis clavier */
    KeyCombination controlSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    KeyCombination controlSaveAs = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    KeyCombination controlOpen = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
    KeyCombination controlDelete = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
    KeyCombination controlHelp = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN);
    KeyCombination controlNonOriente = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
    KeyCombination controlOriente = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
    KeyCombination controlOrientePondere = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
    KeyCombination controlProbabiliste = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
    KeyCombination altVerification = new KeyCodeCombination(KeyCode.V, KeyCombination.ALT_DOWN);
    KeyCombination altTransition = new KeyCodeCombination(KeyCode.T, KeyCombination.ALT_DOWN);
    KeyCombination altExistence = new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN);
    KeyCombination altClassification = new KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN);
    KeyCombination altLegende = new KeyCodeCombination(KeyCode.G, KeyCombination.ALT_DOWN);
    KeyCombination altProbabilite = new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN);
    KeyCombination altLoiProbabilite = new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_DOWN);
    KeyCombination altNode = new KeyCodeCombination(KeyCode.N, KeyCombination.ALT_DOWN);
    KeyCombination altArrow = new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN);
    KeyCombination altSelection = new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN);
    KeyCodeCombination controlZ = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    KeyCodeCombination controlY = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
    
    /* gère les raccourcis claviers */
    EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        public void handle(final KeyEvent keyEvent) {
            /* vérification de chaque raccourcis clavier quand une touche est pressée */
            if (controlSave.match(keyEvent)) {
            	enregistrerID.fire();
    	    } else if (controlSaveAs.match(keyEvent)) {
    	    	enregistrerSousID.fire();
    	    } else if (controlOpen.match(keyEvent)) {
    	    	ouvrirID.fire();
    	    } else if (selectedObject != null && controlDelete.match(keyEvent)) {
    	    	if (selectedObject instanceof Lien) {
    	    		supprimerLienButton.fire();
    	    	} else {
    	    		supprimerNoeudButton.fire();
    	    	}
    	    } else if (controlHelp.match(keyEvent)) {
    	    	manuelUtilisationButton.fire();
    	    } else if (controlNonOriente.match(keyEvent)) {
    	    	nonOrienteButton.fire();
    	    } else if (controlOriente.match(keyEvent)) {
    	    	orienteButton.fire();
    	    } else if (controlOrientePondere.match(keyEvent)) {
    	    	orientePondereButton.fire();
    	    } else if (controlProbabiliste.match(keyEvent)) {
    	    	probabilisteButton.fire();
    	    } else if (altVerification.match(keyEvent)) {
    	    	verifierGrapheId.fire();
    	    } else if (altTransition.match(keyEvent)) {
    	    	matriceDeTransitionId.fire();
    	    } else if (altExistence.match(keyEvent)) {
    	    	existenceCheminId.fire();
    	    } else if (altClassification.match(keyEvent)) {
    	    	classificationSommetsID.fire();
    	    } else if (altLegende.match(keyEvent)) {
    	    	legendeID.fire();
    	    } else if (altProbabilite.match(keyEvent)) {
    	    	probabiliteCheminId.fire();
    	    } else if (altLoiProbabilite.match(keyEvent)) {
    	    	loiDeProbabiliteID.fire();
    	    } else if (altNode.match(keyEvent)) {
    	    	addNodeClicked(null);
    	    } else if (altArrow.match(keyEvent)) {
    	    	addLinkClicked(null);
    	    } else if (altSelection.match(keyEvent)) {
    	    	SelectClicked(null);
    	    } else if (controlZ.match(keyEvent)) {
    	    	undo();
    	    } else if (controlY.match(keyEvent)) {
    	    	redo();
    	    }
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    /**
     * Initialisation de base pour tout graphe
     */
    public void initialisation() {
    	// Ajout des raccourcis clavier
    	Main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    	
    	// Suppression de tous les éléments crées
    	listeElements.getItems().clear();
        zoneDessin.getChildren().clear();
        
        // Ajout des previews
        previewedCircle.setFill(Color.TRANSPARENT);
        previewedCircle.setStroke(Color.BLACK);
        previewedLine.setFill(Color.TRANSPARENT);
        previewedLine.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(previewedLine, previewedCircle);
        
        // Actualisation des affichages
        aside.setVisible(true);
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(false);
        enregistrerSousID.setDisable(false);
		enregistrerID.setDisable(false);
        
		// Création du graphe et autres mise à jour
        valeurLien.setText("0.0");
		actualMode = 3;
		aUneSauvegarde = false;
    }
    
    /**
     * Création des différents type de graphe
     * @param event click de la souris
     */
    @FXML
    void creerGrapheNonOriente(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
    		setTraitement(false);
    		factory = manager.creerFactory("GrapheNonOriente");
    		initialisation();
    		graphe = factory.creerGraphe();
    		Main.getStage().setTitle(title + " : " + graphe.toString());
    		graphSave.delete();
    		graphSave2.delete();
    	}
    }
    @FXML
    void creerGrapheOriente(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
    		setTraitement(false);
    		factory = manager.creerFactory("GrapheOriente");
    		initialisation();
    		graphe = factory.creerGraphe();
    		Main.getStage().setTitle(title + " : " + graphe.toString());
    		graphSave.delete();
    		graphSave2.delete();
    	}
    }
    @FXML
    void creerGrapheOrientePondere(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
    		setTraitement(false);
    		valeurLien.setDisable(false);
    		factory = manager.creerFactory("GrapheOrientePondere");
    		initialisation();
    		graphe = factory.creerGraphe();
    		Main.getStage().setTitle(title + " : " + graphe.toString());
    		graphSave.delete();
    		graphSave2.delete();
    	}
    }
    @FXML
    void creerGrapheProbabiliste(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
    		setTraitement(true);
    		factory = manager.creerFactory("GrapheProbabiliste");
    		initialisation();
    		graphe = factory.creerGraphe();
    		Main.getStage().setTitle(title + " : " + graphe.toString());
    		graphSave.delete();
    		graphSave2.delete();
    	}
    }
    
    /**
     * afficher une fenêtre de confirmation pour la création d'un nouveau graphe
     * @return true si l'utilisateur a cliqué sur OK,
     *         false sinon
     */
    boolean confirmerNouveauGraphe() {
    	if (graphe != null && !(listeElements.getItems().isEmpty())) {
    		Alert alertNouveauGraphe = new Alert(AlertType.CONFIRMATION);
    		alertNouveauGraphe.setTitle("Nouveau graphe");
    		alertNouveauGraphe.setHeaderText(null);
    		alertNouveauGraphe.setContentText("Êtes-vous sûr(e) de vouloir créer un nouveau graphe ?" 
    		                                  + " Vous perdrez toutes vos modifcations actuelles.");
    		Optional<ButtonType> reponse = alertNouveauGraphe.showAndWait();
    		return reponse.get() == ButtonType.OK;
    	} 
    	return true;
    }
    
    /**
     * Appel méthode sur les traitements de graphe
     * @param event click de souris
     */
    @FXML
    void verifierGraphe(ActionEvent event) {
    	probabilite.verifierGraphe((GrapheProbabiliste) graphe, true);
    }
    @FXML
    void matriceDeTransition(ActionEvent event) {
    	probabilite.showMatrix((GrapheProbabiliste) graphe);
    }
    @FXML
    void existenceChemin(ActionEvent event) {
    	probabilite.showExistenceChemin((GrapheProbabiliste) graphe); 
    }
    @FXML 
    void probabiliteChemin(ActionEvent event) {
    	probabilite.showProbabiliteChemin((GrapheProbabiliste) graphe);
    }
    @FXML
    void classificationSommets(ActionEvent event) {
    	probabilite.classificationSommets((GrapheProbabiliste) graphe);
    }
    @FXML
    void legendeClassification(ActionEvent event) {
    	probabilite.legendeClassification();
    }
    @FXML
    void loiDeProbabilite(ActionEvent event) {
    	probabilite.showLoiDeProba((GrapheProbabiliste) graphe);
    }
    
    /**
     * Active ou désactive les options de traitements de graphes
     * @param etat l'état dans lequel mettre les options
     */
    public void setTraitement(boolean etat) {
    	valeurLien.setDisable(!etat);
    	verifierGrapheId.setDisable(!etat);
    	matriceDeTransitionId.setDisable(!etat);
    	existenceCheminId.setDisable(!etat);
    	listeClassificationID.setDisable(!etat);
    	probabiliteCheminId.setDisable(!etat);
    	loiDeProbabiliteID.setDisable(!etat);
    }
    
    /**
     * Selon le mode dans lequel se trouve l'utilisateur
     * - Créer un noeud
     * - Créer un lien
     * - Sélectionne un élément de l'application (noeud ou lien) 
     * @param evt Click de souris 
     */
    @FXML
    public void zoneDessinEvent(MouseEvent evt) {
        // Position de la souris de l'utilisateur lors du click
    	double[] positions = {evt.getX(), evt.getY()};

        if (actualMode == 1) {
            // Réinitialisation des noeuds à relier
            noeudARelier = new Noeud[2];
            creerNoeud(positions);
        } else if (actualMode == 2) {
        	// Si la création du lien réussis, réinitialisation des noeuds à relier
            if (creerLien(positions)) {
            	noeudARelier = new Noeud[2];
            }
        } else if (actualMode == 3) {
            noeudARelier = new Noeud[2];
            selectionner(positions);
        }   
    }
    
    /**
     * Création d'un noeud depuis l'interface graphique
     * @param positions positions X/Y de la souris
     */
    public void creerNoeud(double[] positions) {
    	enregistrerEtat(graphSave.getName());
        Noeud nouveauNoeud = graphe.creerNoeud(positions);
        nouveauNoeud.dessiner(zoneDessin);
        // Ajout du noeud dans la comboBox listant tous les éléments présent sur l'interface
        listeElements.getItems().addAll(nouveauNoeud);
        enregistrerEtat(graphSave2.getName());
    }
    
    /**
     * Création d'un lien depuis l'interface graphique
     * @param positions positions X/Y de la souris
     * @return true si le lien a pu être crée, false sinon
     */
    public boolean creerLien(double[] positions) {
    	enregistrerEtat(graphSave.getName());
    	/* Permet de :
         * - Vérifier que l'élément sélectionner soit bien un noeud
         * - Ajouter ce noeud dans la liste des noeuds à relier. 
         */
    	try {
            if (noeudARelier[0] == null) {
                noeudARelier[0] = (Noeud) graphe.elementClicked(positions, zoneDessin);
            } else {
                noeudARelier[1] = (Noeud) graphe.elementClicked(positions, zoneDessin);
            }
        } catch (Exception e) {} 
    	
        // Si deux noeuds ont été sélectionner -> création du lien.
        if (noeudARelier[1] != null) {
        	try {
        		Lien nouveauLien = graphe.creerLien(noeudARelier[0], noeudARelier[1]);
                nouveauLien.dessiner(zoneDessin);
                listeElements.getItems().addAll(nouveauLien);
                
                previewedLine.setStartX(-100);
                previewedLine.setStartY(-100);
                previewedLine.setEndX(-100);
                previewedLine.setEndY(-100);
                enregistrerEtat(graphSave2.getName());
        	} catch (Exception e) {
        		System.out.println(e);
        		return false;
        	}
        }
        return noeudARelier[1] != null;
    }
    
    /**
     * Permet de récupérer l'élément sélectionner par l'utilisateur depuis l'interface graphique,
     * Selon si c'est un noeud ou un lien, affichage du menu d'édition de propriété correspondant à l'élément.
     * @param positions positions X/Y de la souris
     */
    public void selectionner(double[] positions) {
    	// récupère l'élément sélectionner
        Object element = graphe.elementClicked(positions, zoneDessin);
    	selectedObject = element;
    	
        if (element != null) {
        	listeElements.getSelectionModel().select(element);
        	if (element instanceof Noeud) {
        		noeudSelectionner();
        	} else {
        		lienSelectionner();
        	}
        } else {
        	// Pas d'éléments sélectionner
            listeElements.getSelectionModel().clearSelection();
            editionProprietesLien.setVisible(false);
            editionProprietesNoeud.setVisible(false);
        }
    }
    
    /**
     * Affiche les propriétés de l'élement sélectionner
     * @param event sélection d'un élément dans la comboBox
     */
    @FXML
    void elementSelected(ActionEvent event) {
    	Object element = listeElements.getValue();
    	if (element instanceof Noeud) {
    		selectedObject = element;
    		noeudSelectionner();
    	} else if (element instanceof Lien) {
    		selectedObject = element;
    		lienSelectionner();
    	}
    	RefreshStroke();
    }
    
    /**
     * Actualise tout les éléments en gras du graphe
     */
    public void RefreshStroke() {
    	graphe.reset();
    	Shape[] line = null;
    	if (selectedObject instanceof Noeud) {
    		((Noeud) selectedObject).getCircle().setStrokeWidth(3.0);
    	} else if (selectedObject instanceof LienNonOriente) {
			((LienNonOriente) selectedObject).getLine().setStrokeWidth(3.0);
		} else if (selectedObject instanceof LienOriente) {
			LienOriente lien = (LienOriente) selectedObject;
			line = lien.getQuadCurved();
			if (line[0] == null) line = lien.getArc();
    	} else if (selectedObject instanceof LienOrientePondere) {
    		LienOrientePondere lien = (LienOrientePondere) selectedObject;
    		line = lien.getQuadCurved();
			if (line[0] == null) line = lien.getArc();
    	} else if (selectedObject instanceof LienProbabiliste) {
    		LienProbabiliste lien = (LienProbabiliste) selectedObject;
    		line = lien.getQuadCurved();
			if (line[0] == null) line = lien.getArc();
    	}
    	if (line != null) {
    		for(Shape l : line) {
    			 l.setStrokeWidth(3.0);
    		}
    	}
    }
    
    /**
     * Affiche les propriétés du graphe et active la possibilité de le déplacer
     */
    public void noeudSelectionner() {
    	RefreshStroke();
    	Noeud noeud = (Noeud) selectedObject;
        
        /* Affichage des propriété du noeud */
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(true);
        nomNoeud.setText(noeud.getNom());
        posXNoeud.setText("" + noeud.getPositions()[0]);
        posYNoeud.setText("" + noeud.getPositions()[1]);
        radiusNoeud.setText("" + noeud.getRadius());
        
        /* On regarde tous les éléments contenus dans la zone de dessin */
        Circle circle = noeud.getCircle();
        for (Node n : zoneDessin.getChildren()) {
        	if(n.equals(circle)) {
        		draggedNode(n, circle, noeud);
        	}
        }
        // Raccourcis clavier
        Main.getScene().setOnKeyPressed(event -> {
    	    if (event.getCode() == KeyCode.ENTER) {
    	    	validerModifNoeud.fire();
    	    } else if (event.getCode() == KeyCode.DELETE) {
    	    	supprimerNoeudButton.fire();
    	    }
    	});
    }
    
    /**
     * Affiche les propriétés du lien et active la possibilité de le déplacer
     */
    public void lienSelectionner() {
    	RefreshStroke();
    	Lien lien = (Lien) selectedObject;
    	
        /* Affichage des propriétés du lien */
        editionProprietesLien.setVisible(true);
        editionProprietesNoeud.setVisible(false);
        noeud1Lien.setText(lien.getNoeuds()[0].getNom());
        noeud2Lien.setText(lien.getNoeuds()[1].getNom());
    	if (lien instanceof LienOrientePondere) {
    		LienOrientePondere link = (LienOrientePondere) lien;
    		valeurLien.setText("" + link.getValue());
    	} else if (lien instanceof LienProbabiliste) {
    		LienProbabiliste link = (LienProbabiliste) lien;
    		valeurLien.setText("" + link.getValue());
    	}
    	
    	// Permet le drag and click
    	for(Node n : zoneDessin.getChildren()) {
    		if (!(n instanceof Label)) {
            	draggedLink(n, lien);
    		}
        }
    	// Raccourcis clavier
        Main.getScene().setOnKeyPressed(event -> {
    	    if (event.getCode() == KeyCode.ENTER) {
    	    	validerModifLien.fire();
    	    } else if (event.getCode() == KeyCode.DELETE) {
    	    	supprimerLienButton.fire();
    	    }
    	});
    }

    /**
     * Change l'état du graphe
     * @param nomDuFichier nom du fichier
     */
    public void setGraphe(String nomDuFichier) {
    	try {
    		if(graphSave.canRead() || graphSave2.canRead()) {
    			FileInputStream fileIn = new FileInputStream(nomDuFichier);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                this.graphe = (Graphe) in.readObject();
                in.close();
                fileIn.close();
            	// Suppression de tous les éléments restant sur la zone graphique et dans la liste déroulante
            	zoneDessin.getChildren().clear();
            	listeElements.getItems().clear();
            	// Réinitialisation de l'application
                initialisation();
                aside.setVisible(true);
                // Mode sélectionner pour éviter les problème d'affichage
                actualMode = 3;
                // Pour éviter de voir la preview figé après avoir annuler 
                previewedCircle.setCenterX(-100);
                previewedCircle.setCenterY(-100);
                previewedLine.setStartX(-100);
                previewedLine.setStartY(-100);
                previewedLine.setEndX(-100);
                previewedLine.setEndY(-100);
                // Dessin de tout les noeuds et liens du graphe ouvert
                for (Noeud n : graphe.getListeNoeuds()) {
                	n.dessiner(zoneDessin);
                	listeElements.getItems().add(n);
                }
                for (Lien l : graphe.getListeLiens()) {
                	l.dessiner(zoneDessin);
                	listeElements.getItems().add(l);
                }
                /* Si le graphe est un graphe probabiliste 
                 * -> lancement de toute les méthodes liés à ce dernier
                 */
                if (graphe instanceof GrapheProbabiliste) {
                	setTraitement(true);
                } else if (graphe instanceof GrapheOrientePondere) {
                	setTraitement(false);
                	valeurLien.setDisable(false);
                } else {
                	setTraitement(false);
                }
    		} 
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();        }
    }
    
    /**
     * Annule une action
     */
    public void undo() {
    	setGraphe(graphSave.getName());
    }
    
    /**
     * Restaure une action
     */
    public void redo() {
    	setGraphe(graphSave2.getName());
    }
    
    /**
     * Met en gras l'élement sélectionné, permet au noeud sélectionné de changer de position
     * par rapport à la position de la souris
     * @param element élément contenu dans la zone de dessin
     * @param objectSelected l'élément cliqué
     * @param noeud le noeud dont on veut changer la position
     */
    void draggedNode(Node element, Object objectSelected, Noeud noeud) {
    	reset2();
    	
    	/* Lorsque l'on click sur le noeud */
    	element.setOnMousePressed(envent ->{
    		enregistrerEtat(graphSave.getName());
    	});
    	
    	/* Lorsque qu'un drag and click est effectué */
    	element.setOnMouseDragged(event -> {
    		/* les positions du noeuds sont actualisé par rapport à la position de la souris */
    		RefreshStroke();
    		posXNoeud.setText("" + event.getX());
            posYNoeud.setText("" + event.getY());
            /* Changement de position du noeud par rapport à la souris */
            noeud.setPositions(new double[] {event.getX(), event.getY()}); 
            /* Actualise les positions des liens reliés au noeud par rapport à sa position */
            graphe.relocalisation();
    	});
    	
    	/* Lorsque l'on relâche le click souris */
    	element.setOnMouseReleased(event ->{
    		enregistrerEtat(graphSave2.getName());
    	});
    }
    
    /**
     * Met en gras l'élément sélectionné, permet au lien sélectionné de changer de noeuds
     * @param element élément contenu dans la zone de dessin
     * @param objectSelected l'élément sélectionné
     */
    void draggedLink(Node element, Lien objectSelected) {
    	
    	/* Lorsque l'on click sur le noeud */
    	element.setOnMousePressed(envent ->{
    		enregistrerEtat(graphSave.getName());
    	});
    	
    	// Drag
    	element.setOnMouseDragged(event -> {
    		/* Aperçu de la ligne en temps réel quand on déplace le lien */
    		previewedLine.setStrokeWidth(3.0);
            previewedLine.setStartX(objectSelected.getNoeuds()[0].getPositions()[0]);
            previewedLine.setStartY(objectSelected.getNoeuds()[0].getPositions()[1]);
            previewedLine.setEndX(event.getX());
            previewedLine.setEndY(event.getY());
            // Relâchement de la souris
        	element.setOnMouseReleased(evente -> {
        		/* N'affiche plus la ligne d'aperçu */
        		exitPreview(evente);
        		/* Récupère la position X et Y de la souris au moment ou le clic est relâché */
        		double[] pos = {evente.getX(), evente.getY()};
        		/* Effectuer une action seulement quand on relâche sur un noeud */
        		if (graphe.elementClicked(pos, zoneDessin) instanceof Noeud) {
        			/* Récupération du noeud sélectionner */
    				Noeud noeud = (Noeud) graphe.elementClicked(pos, zoneDessin);
        			
        			/*
        			 * S'il est possible de crée un lien ayant le noeud de départ 
        			 * et le noeud ou la souris a été relâché -> le crée sinon ne rien faire
        			 */
        			try {
        				Lien nouveauLien = graphe.creerLien(objectSelected.getNoeuds()[0], noeud);
        				graphe.supprimerLien((Lien) objectSelected, zoneDessin, listeElements);
        				nouveauLien.dessiner(zoneDessin);
        				listeElements.getItems().addAll(nouveauLien);
                        enregistrerEtat(graphSave2.getName());
        				reset2();
        			} catch (Exception e) {}
        		}
        	});
    	});
    }
    
    /**
     * Modification des propriété d'un noeud sélectionner par les nouvelles valeurs assigné par l'utilisateur
     * @param event click
     */
    @FXML
    void modifNoeud(ActionEvent event) {
    	enregistrerEtat(graphSave.getName());
        Noeud noeudAModif = (Noeud) selectedObject;
        double[] positions = {Double.parseDouble(posXNoeud.getText()), Double.parseDouble(posYNoeud.getText())};
        graphe.modifNomNoeud(noeudAModif, nomNoeud.getText());
        graphe.modifPos(noeudAModif, positions);
        graphe.modifRadius(noeudAModif, Double.parseDouble(radiusNoeud.getText()));
        enregistrerEtat(graphSave2.getName());
    }
    
    /**
     * Modification des propriété d'un lien sélectionner par les nouvelles valeurs assigné par l'utilisateur
     * @param event click
     */
    @FXML
    void modifLien(ActionEvent event) {
    	enregistrerEtat(graphSave.getName());
        Lien lienAModif = (Lien) selectedObject;
        String[] labelNoeudARelier = {
            noeud1Lien.getText(),
            noeud2Lien.getText()
        };
        try {
            Noeud n1 = graphe.getNode(labelNoeudARelier[0]);
            Noeud n2 = graphe.getNode(labelNoeudARelier[1]);
            if (graphe instanceof GrapheProbabiliste) {
            	GrapheProbabiliste g = (GrapheProbabiliste) graphe;
            	g.modifValeur(lienAModif, Double.parseDouble(valeurLien.getText()));
            } else if (graphe instanceof GrapheOrientePondere){
            	GrapheOrientePondere g = (GrapheOrientePondere) graphe;
            	g.modifValeur(lienAModif, Double.parseDouble(valeurLien.getText()));
            }
            graphe.modifLien(lienAModif, new Noeud[] {n1, n2}, zoneDessin);
        } catch (Exception e) {
            System.err.println(e);
        }
        lienAModif.actualiser();
        enregistrerEtat(graphSave2.getName());
    }
    
    /**
     * Permet la suppression d'un noeud
     */
    @FXML
    public void supprimerNoeud() {
    	enregistrerEtat(graphSave.getName());
    	graphe.supprimerNoeud((Noeud) selectedObject, zoneDessin, listeElements);
    	listeElements.getSelectionModel().clearSelection();
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(false);
        selectedObject = null;
        graphe.reset();
        enregistrerEtat(graphSave2.getName());
    }
    
    /**
     * Permet la suppression d'un lien
     */
    @FXML
    public void supprimerLien() {
        enregistrerEtat(graphSave.getName());
    	graphe.supprimerLien((Lien) selectedObject, zoneDessin, listeElements);
    	listeElements.getItems().remove(selectedObject);
    	listeElements.getSelectionModel().clearSelection();
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(false);
        selectedObject = null;
        graphe.reset();
        enregistrerEtat(graphSave2.getName());
    }
    
    /**
     * Outil de preview sur la zone de dessin, selon le mode sélectionner
     * 1 : preview d'un cercle suivant la souris
     * 2 : preview d'un lien, si aucun noeud n'a été sélectionner, n'affiche rien
     *     si un noeud à été sélectionner, affiche un lien partant de ce noeud
     *     et qui suit la souris
     * @param event déplacement de la souris
     */
    @FXML
    void preview(MouseEvent event) {
        if (actualMode == 1) {
            previewedCircle.setCenterX(event.getX());
            previewedCircle.setCenterY(event.getY());
        } else if (actualMode == 2 && noeudARelier[0] != null) {
            previewedLine.setStartX(noeudARelier[0].getPositions()[0]);
            previewedLine.setStartY(noeudARelier[0].getPositions()[1]);
            previewedLine.setEndX(event.getX());
            previewedLine.setEndY(event.getY());
        }
    }
    
    /**
     * Fin de la preview quand la souris de l'utilisateur quitte la zone de dessin
     * @param event sortie de la souris de la zone de dessin
     */
    @FXML
    void exitPreview(MouseEvent event) {
        previewedCircle.setCenterX(-100);
        previewedCircle.setCenterY(-100);
        previewedLine.setStartX(-100);
        previewedLine.setStartY(-100);
        previewedLine.setEndX(-100);
        previewedLine.setEndY(-100);
    }
    
    /**
     * Changement du mode actuel selon l'option choisie par l'utilisateur 
     * (reset pour empêcher le click and drag)
     * @param event click
     */
    @FXML void addNodeClicked(MouseEvent event) {
    	actualMode = 1; 
    	reset2();
    }
    @FXML void addLinkClicked(MouseEvent event) {
    	actualMode = 2;
    	reset2();
    }
    @FXML void SelectClicked(MouseEvent event) {
    	actualMode = 3;
    }
    
    /**
     * Réinitialisation des setters javaFX des nodes
     */
    void reset2() {
		for (Node n : zoneDessin.getChildren()) {
			n.setOnMousePressed(null);
			n.setOnMouseDragged(null);
			n.setOnMouseReleased(null);
		}
    }

    /**
     * Envoi sur le fichier pdf du manuel d'utilisation sur un navigateur web
     * @param event clique sur l'option 'manuel d'utilisation' dans le menu d'aide
     */
    @FXML
    void manuelUtilisation(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://drive.google.com/file/d/1c7ZyCAJWZMPLB5eMrz7G2Zr4UHACM7b5/view"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Enregistre le graphe sous quand le bouton "Enregistrer graphe" est clique
     * @param event click souris
     */
    @FXML
    void enregistrerSous(ActionEvent event) {
    	enregistrerSousLeGraphe();
    }
    
    /**
     * Selon s'il existe déjà une sauvegarde, enregistre sous ou enregistre
     * @param event click souris
     */
    @FXML
    void enregistrer(ActionEvent event) {
    	if (aUneSauvegarde) {
    		enregistrerLeGraphe(lastFile);
    	} else {
    		enregistrerSousLeGraphe();
    	}
    }
    
    /**
     * Ouvre la fenêtre d'enregistrement d'un graphe
     */
    void enregistrerSousLeGraphe() {
    	fileChooser.setTitle("Sauvegarder un graphe");
    	/* Si l'utilisateur a déjà sauvegarder un fichier,
    	 * rouvrir la fenêtre d'enregistrement à ce chemin
    	 */
    	if (lastFile != null) {
    		fileChooser.setInitialDirectory(lastFile.getParentFile());
    	}
    	// extensions de fichier autorisé
    	fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichier graphe", "*.graph"));
    	try {
    		// récupération du chemin et nom du fichier enregistrer
    		lastFile = fileChooser.showSaveDialog(null);
    		if (lastFile != null) {
    		    // création du fichier et écriture
		        enregistrerLeGraphe(lastFile);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Enregistre un fichier
     * @param file fichier à sauvegarder
     */
    void enregistrerLeGraphe(File file) {
    	try {
    		FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(graphe);
            out.close();
            fos.close();
            aUneSauvegarde = true;
    	} catch (Exception e) {
    		System.err.println(e);
    	}
    }
    
    /**
     * Enregistre l'état du graphe
     * @param grapheAEnregistrer
     */
    void enregistrerEtat(String nomFichier) {
    	try {
    		FileOutputStream fos = new FileOutputStream(nomFichier	);
    		ObjectOutputStream out = new ObjectOutputStream(fos);
    		out.writeObject(graphe);
    		out.close();
            fos.close();
    	} catch (IOException e) {
            e.printStackTrace();
    	}
    }
    
    /**
     * Restaure un fichier contenant un graphe
     * @param event click de souris
     */
    @FXML
    void ouvrir(ActionEvent event) {
    	fileChooser.setTitle("Sauvegarder un graphe");
    	/* Si l'utilisateur a déjà sauvegarder un fichier,
    	 * rouvrir la fenêtre d'enregistrement à ce chemin
    	 */
    	if (lastFile != null) {
    		fileChooser.setInitialDirectory(lastFile.getParentFile());
    	}
    	// extensions de fichier autorisé
    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier graphe", "*.graph"));
    	try {
    		lastFile = fileChooser.showOpenDialog(null);
        	if (lastFile != null) {
        		try {
                    FileInputStream fileIn = new FileInputStream(lastFile);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    graphe = (Graphe) in.readObject();
                    in.close();
                    fileIn.close();
                    
                    restauration();
                } catch (Exception e) {
                	System.err.println("Le fichier n'a pas pu être lu");
                }
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Restaure les données nécessaire pour la création de graphe
     */
    public void restauration() {
    	// Suppression de tous les éléments restant sur la zone graphique et dans la liste déroulante
    	zoneDessin.getChildren().clear();
    	listeElements.getItems().clear();
    	// Réinitialisation de l'application
        initialisation();
        aside.setVisible(true);
        actualMode = 3;
        // Dessin de tout les noeuds et liens du graphe ouvert
        for (Noeud n : graphe.getListeNoeuds()) {
        	n.dessiner(zoneDessin);
        }
        listeElements.getItems().addAll(graphe.getListeNoeuds());
        for (Lien l : graphe.getListeLiens()) {
        	l.dessiner(zoneDessin);
        	listeElements.getItems().add(l);
        }
        /* Si le graphe est un graphe probabiliste 
         * -> lancement de toute les méthodes liés à ce dernier
         */
        if (graphe instanceof GrapheProbabiliste) {
        	setTraitement(true);
        } else if (graphe instanceof GrapheOrientePondere) {
        	setTraitement(false);
        	valeurLien.setDisable(false);
        } else {
        	setTraitement(false);
        }
        aUneSauvegarde = true;
		Main.getStage().setTitle(title + " : " + graphe);
    }
}
