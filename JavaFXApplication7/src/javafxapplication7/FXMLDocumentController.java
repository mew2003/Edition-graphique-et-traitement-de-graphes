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
import app.GraphAction;
import app.Graphe;
import app.GrapheOrientePondere;
import app.GrapheProbabiliste;
import app.Lien;
import app.LienNonOriente;
import app.LienOriente;
import app.LienOrientePondere;
import app.LienProbabiliste;
import app.Noeud;
import app.ActionManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
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
    private MenuButton menuEdition;
    @FXML
    private ComboBox<String> listeLiens;
    @FXML
    private MenuButton menuGraphe;
    @FXML
    private MenuButton menuAide;
    @FXML
    private TextField nomNoeud;
    @FXML
    private TextField radiusNoeud;
    @FXML
    private TextField noeud2Lien;
    @FXML
    private MenuButton menuTraitement;
    @FXML
    private AnchorPane palette;
    @FXML
    private ToggleGroup boutonsPalette;
    @FXML
    private ImageView nodeButtonID;
    @FXML
    private ImageView arrowButtonID;
    @FXML
    private ImageView selectionButtonID;
    @FXML
    private ComboBox<String> listeNoeuds;
    @FXML
    private ComboBox<Object> listeElements;
    @FXML
    private MenuItem enregistrerSous;
    @FXML 
    private MenuItem nouveau;
    @FXML
    private MenuItem ouvrir;
    @FXML
    private MenuItem annuler;
    @FXML
    private MenuItem retablir;
    @FXML
    private MenuItem obtenirDeLAide;
    @FXML
    private MenuItem FAQ;
    @FXML
    private Parent root;
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
    
    /* Permet de gérer les actions du graphe */
    ActionManager actionManager = new ActionManager();
    
    // Permet de savoir si le graphe possède une sauvegarde
    boolean aUneSauvegarge = false;
    
    // Sélectionneur de fichier
    FileChooser fileChooser = new FileChooser();
    
    // Permet d'obtenir le dernier fichier sauvegarder
    File lastFile = null;
    
    // titre par défaut du logiciel
    String title = "Logiciel d'édition et de traitement de graphes";
    
    /* création des combinaisons de touches pour tous les raccourcis clavier */
    KeyCombination controlSave = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);
    KeyCombination controlSaveAs = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY, KeyCombination.SHIFT_ANY);
    KeyCombination controlOpen = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_ANY);
    KeyCombination controlDelete = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_ANY);
    KeyCombination controlHelp = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_ANY);
    KeyCombination controlNonOriente = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY);
    KeyCombination controlOriente = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_ANY);
    KeyCombination controlOrientePondere = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_ANY);
    KeyCombination controlProbabiliste = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);
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
    	    }else if (altTransition.match(keyEvent)) {
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
    	    }
        }
    };
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    /**
     * Initialisation de base pour tout graphe
     */
    public void initialisation() {
    	Main.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    	
    	listeElements.getItems().clear();
        zoneDessin.getChildren().clear();
        
        previewedCircle.setFill(Color.TRANSPARENT);
        previewedCircle.setStroke(Color.BLACK);
        previewedLine.setFill(Color.TRANSPARENT);
        previewedLine.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(previewedLine, previewedCircle);
        
        aside.setVisible(true);
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(false);
    }
    
    /**
     * Création d'un graphe non orienté simple
     * @param event click de la souris
     */
    @FXML
    void creerGrapheNonOriente(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
            Main.getStage().setTitle(title + " : Graphe non orienté");
    		initialisation();
    		enregistrerID.setDisable(false);
    		enregistrerSousID.setDisable(false);
    		setTraitement(false);
    		factory = manager.creerFactory("GrapheNonOriente");
    		graphe = factory.creerGraphe();
    		actualMode = 3;
    		aUneSauvegarge = false;
    	}
    }
    
    @FXML
    void creerGrapheOriente(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
            Main.getStage().setTitle(title + " : Graphe orienté");
    		initialisation();
    		setTraitement(false);
    		enregistrerSousID.setDisable(false);
    		enregistrerID.setDisable(false);
    		factory = manager.creerFactory("GrapheOriente");
    		graphe = factory.creerGraphe();
    		actualMode = 3;
    		aUneSauvegarge = false;
    	}
    }
    
    @FXML
    void creerGrapheOrientePondere(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
            Main.getStage().setTitle(title + " : Graphe orienté pondéré");
    		initialisation();
    		setTraitement(false);
    		valeurLien.setDisable(false);
    		enregistrerID.setDisable(false);
    		enregistrerSousID.setDisable(false);
    		factory = manager.creerFactory("GrapheOrientePondere");
    		graphe = factory.creerGraphe();
    		actualMode = 3;
    		aUneSauvegarge = false;
    	}
    }
    
    @FXML
    void creerGrapheProbabiliste(ActionEvent event) {
    	if (confirmerNouveauGraphe()) {
            Main.getStage().setTitle(title + " : Graphe probabiliste");
    		initialisation();
    		setTraitement(true);
    		enregistrerSousID.setDisable(false);
    		enregistrerID.setDisable(false);
    		factory = manager.creerFactory("GrapheProbabiliste");
    		graphe = factory.creerGraphe();
    		actualMode = 3;
    		aUneSauvegarge = false;
    	}
    }
    
    /**
     * afficher une fenêtre de confirmation pour la création d'un nouveau graphe
     * @return true si l'utilisateur a cliqué sur OK,
     *         false sinon
     */
    boolean confirmerNouveauGraphe() {
    	if (graphe != null && !(listeElements.getItems().isEmpty())) {
    		Alert dialogueConfirmationNouveauGraphe = new Alert(AlertType.CONFIRMATION);
    		dialogueConfirmationNouveauGraphe.setTitle("Nouveau graphe");
    		dialogueConfirmationNouveauGraphe.setHeaderText(null);
    		dialogueConfirmationNouveauGraphe.setContentText("Êtes-vous sûr(e) de vouloir créer un nouveau graphe ? Vous perdrez toutes vos modifcations actuelles.");
    		Optional<ButtonType> reponse = dialogueConfirmationNouveauGraphe.showAndWait();
    		if (reponse.get() == ButtonType.OK) {
    			return true;
    		} 
    		else {
    			return false;
    		}
    	} 
    	return true;
    }
    
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
     * met les options pour disponible que pour certains graphe dans le bon état (désactivé ou activé)
     * @param etat  l'état dans lequel mettre les options
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
        	Graphe previousState = graphe.clone();
            // Réinitialisation des noeuds à relier
            noeudARelier = new Noeud[2];
            creerNoeud(positions);
            Graphe nextState = graphe.clone();
            GraphAction action = new GraphAction(previousState, nextState);	
            actionManager.executeAction(action);
        } else if (actualMode == 2) {
        	Graphe previousState = graphe.clone();
        	// Si la création du lien réussis, réinitialisation des noeuds à relier
            if (creerLien(positions)) {
            	noeudARelier = new Noeud[2];
            }
            Graphe nextState = graphe.clone();
            GraphAction action = new GraphAction(previousState, nextState);	
            actionManager.executeAction(action);
        } else if (actualMode == 3) {
        	Graphe previousState = graphe.clone();
            noeudARelier = new Noeud[2];
            selectionner(positions);
            Graphe nextState = graphe.clone();
            GraphAction action = new GraphAction(previousState, nextState);	
            actionManager.executeAction(action);
        }   
    }
    
    /**
     * Création d'un noeud depuis l'interface graphique
     * @param positions positions X/Y de la souris
     */
    public void creerNoeud(double[] positions) {
    	Graphe previousState = graphe.clone();
        Noeud nouveauNoeud = graphe.creerNoeud(positions);
        nouveauNoeud.dessiner(zoneDessin);
        // Ajout du noeud dans la comboBox listant tous les éléments présent sur l'interface
        listeElements.getItems().addAll(nouveauNoeud);
        Graphe nextState = graphe.clone();
        GraphAction action = new GraphAction(previousState, nextState);	
        actionManager.executeAction(action);
    }
    
    /**
     * Création d'un lien depuis l'interface graphique
     * @param positions positions X/Y de la souris
     * @return true si le lien a pu être crée, false sinon
     */
    public boolean creerLien(double[] positions) {
    	Graphe previousState = graphe.clone();
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
        	} catch (Exception e) {
        		System.out.println(e);
        		return false;
        	}
        }
        Graphe nextState = graphe.clone();
        GraphAction action = new GraphAction(previousState, nextState);
        actionManager.executeAction(action);
        return noeudARelier[1] != null;
    }
    
    /**
     * Permet de récupérer l'élément sélectionner par l'utilisateur depuis l'interface graphique,
     * Selon si c'est un noeud ou un lien, affichage du menu d'édition de propriété correspondant à l'élément.
     * @param positions positions positions X/Y de la souris
     */
    public void selectionner(double[] positions) {
    	Graphe previousState = graphe.clone();
    	// récupère l'élément sélectionner
        Object o = graphe.elementClicked(positions, zoneDessin);
        /* Si l'élement est null, ne rien afficher
         * Dans le cas contraire, affichage du menu de propriété correspondant
         */
        /* Liste de tous les éléments présents sur la zone de dessin  */
        ObservableList<Node> childrens = zoneDessin.getChildren();
        if (o != null) {
        	listeElements.getSelectionModel().select(o);
            try {
                Noeud node = (Noeud) o;
                /* Affichage des propriété du noeud */
                editionProprietesLien.setVisible(false);
                editionProprietesNoeud.setVisible(true);
                nomNoeud.setText(node.getNom());
                posXNoeud.setText("" + node.getPositions()[0]);
                posYNoeud.setText("" + node.getPositions()[1]);
                radiusNoeud.setText("" + node.getRadius());
                selectedObject = node;
                Circle circle = node.getCircle();
                /* On regarde tous les éléments contenus dans la zone de dessin */
                for (Node n : childrens) {
                	/* Si l'élément est un cercle */
                	if(n instanceof Circle) {
                		/* Si cet élément est égal au cercle */
                		if(n.equals(circle)) {
                			draggedNode(n, circle, node);
                        }
                	}
                }
                Graphe nextState = graphe.clone();
                GraphAction action = new GraphAction(previousState, nextState);	
                actionManager.executeAction(action);
                
            	Main.getScene().setOnKeyPressed(event -> {
            	    if (event.getCode() == KeyCode.ENTER) {
            	    	validerModifNoeud.fire();
            	    } else if (event.getCode() == KeyCode.DELETE) {
            	    	supprimerNoeudButton.fire();
            	    }
            	});
            } catch (Exception e) {
                Lien link = (Lien) o;
                /* Affichage des propriétés du lien */
                editionProprietesLien.setVisible(true);
                editionProprietesNoeud.setVisible(false);
                this.temp1 = refreshLinkedNode1Element();
                this.temp2 = refreshLinkedNode1Element();
                noeud1Lien.setText(link.getNoeuds()[0].getNom());
                noeud2Lien.setText(link.getNoeuds()[1].getNom());
                selectedObject = link;
                /* Si le graphe est un graphe non orienté */
                if(link instanceof LienNonOriente) {
                	/* Initialisation de la variable lien 
                	 * si l'élément sélectionné est un lien non orienté 
                	 */
                    LienNonOriente lien = (LienNonOriente) link;
                    for(Node n : childrens) {
                    	if(n instanceof Line) {
                    		draggedLink(n, lien);
                    	}
                    }
                    Graphe nextState = graphe.clone();
                    GraphAction action = new GraphAction(previousState, nextState);	
                    actionManager.executeAction(action);
                /* Si le graphe est un graphe orienté */
                } else if(link instanceof LienOriente) {
                	/* Initialisation de la variable lien si l'élément sélectionné est un lien orienté */
                    LienOriente lien = (LienOriente) link;
                    for(Node n : childrens) {
                    	/* Si le lien est un QuadCurve */
                    	if(n instanceof QuadCurve) {
                    		draggedLink(n, lien);
                    	/* Si le lien est un Arc */
                    	} else if(n instanceof Arc) {
                    		draggedLink(n, lien);
                    	}
                    }
                    Graphe nextState = graphe.clone();
                    GraphAction action = new GraphAction(previousState, nextState);	
                    actionManager.executeAction(action);
                } else if(link instanceof LienOrientePondere) {
                	/* Initialisation de la variable lien si l'élément sélectionné est un lien probabiliste */
                	LienOrientePondere lien = (LienOrientePondere) link;
            		valeurLien.setText("" + lien.getValue());
            		for(Node n : childrens) {
            			/* Si le lien est un QuadCurve */
                    	if(n instanceof QuadCurve) {
                    		draggedLink(n, lien);
                    	/* Si le lien est un Arc */
                    	} else if(n instanceof Arc) {
                    		draggedLink(n, lien);
                    	}
            		}
            		Graphe nextState = graphe.clone();
                    GraphAction action = new GraphAction(previousState, nextState);	
                    actionManager.executeAction(action);
                /* Si le graphe est un graphe probabiliste */
            	} else if(link instanceof LienProbabiliste) {
            		/* Initialisation de la variable lien si l'élément sélectionné est un lien probabiliste */
            		LienProbabiliste lien = (LienProbabiliste) link;
            		valeurLien.setText("" + lien.getValue());
            		for(Node n : childrens) {
            			/* Si le lien est un QuadCurve */
                    	if(n instanceof QuadCurve) {
                    		draggedLink(n, lien);
                    	/* Si le lien est un Arc */
                    	} else if(n instanceof Arc) {
                    		draggedLink(n, lien);
                    	}
            		}
            		Graphe nextState = graphe.clone();
                    GraphAction action = new GraphAction(previousState, nextState);	
                    actionManager.executeAction(action);
            	}
                Main.getScene().setOnKeyPressed(event -> {
            	    if (event.getCode() == KeyCode.ENTER) {
            	    	validerModifLien.fire();
            	    } else if (event.getCode() == KeyCode.DELETE) {
            	    	supprimerLienButton.fire();
            	    }
            	});
            }
        } else {
        	/* Si l'élément sélectionné n'est pas un élément de la zone de dessin 
        	 * alors on affiche rien 
        	 */
            listeElements.getSelectionModel().clearSelection();
            editionProprietesLien.setVisible(false);
            editionProprietesNoeud.setVisible(false);
        }
    }
    
    @FXML
    /**
     * Permet d'annuler une action effectuée
     */
    public void undo() {
        Graphe graph = actionManager.undoAction();
        System.out.println(graphe.toString());
        setGraphe(graph);
        System.out.println(graphe.toString());
        try {
    		this.noeud1Lien.setText(this.temp1.getText());
    		this.noeud2Lien.setText(this.temp2.getText());
        } catch (NullPointerException e) {}
    }

    @FXML
    /**
     * Permet de rétablir l'action annulée
     */
    public void redo() {
        Graphe graph = actionManager.redoAction();
        setGraphe(graph);
        try {
    		this.noeud1Lien.setText(this.temp3.getText());
    		this.noeud2Lien.setText(this.temp4.getText());
        } catch(NullPointerException e) {}
    }
    
    /**
     * Permet de changer l'état du graphe actuel
     * @param graphe le graphe dont on veut l'état
     */
    public void setGraphe(Graphe graphe) {
    	if(graphe != null) {
    		Object object = cloneSelectedObject();	
    		AnchorPane temp3 = cloneDrawingZone();
    		System.out.println(object);
    		Circle temp = cloneCircle();
    		Line temp2 = cloneLine();
    		/* Supprime tous les noeuds et liens affichés actuellement dans la zone de dessin */
    		zoneDessin.getChildren().clear();
    		listeElements.getItems().clear();

    		/* Modifie l'attribut "graphe" */ 
    		this.graphe = graphe;

    		/* Ajoute les noeuds du graphe passé en paramètre */
    		if(graphe.getListeNoeuds() != null) {
    			for (Noeud noeud : graphe.getListeNoeuds()) {
    				Noeud monNoeud = noeud.clone();
    				monNoeud.dessiner(zoneDessin);
    				listeElements.getItems().add(noeud);
    			}
    		}
    		/* Ajoute les liens du graphe passé en paramètre */
    		if(graphe.getListeLiens() != null) {
    			for (Lien lien : graphe.getListeLiens()) {
    				Lien monLien = lien.clone();
    				monLien.dessiner(zoneDessin);
    				listeElements.getItems().add(lien);
    			}
    		}
    		this.previewedCircle = temp;
    		this.previewedLine = temp2;
    		if(previewedCircle != null && previewedCircle != null) {
    			zoneDessin.getChildren().addAll(previewedCircle, previewedLine);
    		}
    		if(object != null) {
    			this.selectedObject = object;
    		}
    		for(Node originalNode : temp3.getChildren()) {
    			for(Node copiedNode : zoneDessin.getChildren()) {
					copiedNode.setOnMouseDragged(originalNode.getOnMouseDragged());
    			}
    		}
    	}
    }
    

    public AnchorPane cloneDrawingZone() {
    	AnchorPane clone = new AnchorPane();
    	for(Node childrens : zoneDessin.getChildren()) {
    		System.out.println(childrens);
    		clone.getChildren().add(childrens);
    	}
    	return clone;
    }

    /**
     * Copie profondémment l'objet previewedLine
     * @return la copie profonde de l'objet previewedLine
     */
    public Line cloneLine() {
    	Line clone = new Line();
    	clone.setStartX(previewedLine.getStartX());
    	clone.setStartY(previewedLine.getStartY());
    	clone.setEndX(previewedLine.getEndX());
    	clone.setEndY(previewedLine.getEndY());
    	return clone;
    }
    
    /**
     * Copie profondémment l'objet previewedCircle
     * @return la copie profonde de l'objet previewedCircle
     */
    public Circle cloneCircle() {
    	Circle clone = new Circle();
    	clone.setCenterX(previewedCircle.getCenterX());
    	clone.setCenterY(previewedCircle.getCenterY());
    	clone.setRadius(previewedCircle.getRadius());
        clone.setFill(Color.TRANSPARENT);
        clone.setStroke(Color.BLACK);
    	return clone;
    }
    
    /**
     * Copie profondémment l'objet noeuds1Lien
     * @return la copie profonde de l'objet noeuds1Lien
     */
    public TextField refreshLinkedNode1Element() {
    	TextField clone = new TextField();
    	clone.setText(noeud1Lien.getText());
    	return clone;
    }
    
    /**
     * Copie profondémment l'objet noeuds2Lien
     * @return la copie profonde de l'objet noeuds2Lien
     */
    public TextField refreshLinkedNode2Element() {
    	TextField clone = new TextField();
    	clone.setText(noeud2Lien.getText());
    	return clone;
    }
    
    /**
     * Copie profondémment l'objet SelectedObject
     * @return la copie prodonde de l'objet selectedObject
     */
    public Object cloneSelectedObject() {
    	if(selectedObject instanceof Noeud) {
    		Noeud noeud = (Noeud) selectedObject;
    		Noeud clone = noeud.clone();
    		return clone;
    	} else if(selectedObject instanceof Lien) {
    		Lien lien = (Lien) selectedObject;
    		Lien clone = lien.clone();
    		return clone;
    	} else {
    		return null;
    	}
    	
    	
    }
    
    /**
     * Met en gras l'élement sélectionné, permet au noeud sélectionné de changer de position
     * par rapport à la souris
     * @param Node n élément contenu dans la zone de dessin
     * @param Object o l'élément cliqué
     * @param Noeud noeud le noeud dont on veut changer la position
     */
    void draggedNode(Node n, Object o, Noeud noeud) {
    	/* Lorsque le clic de la souris est maintenu et qu'elle bouge */
    	n.setOnMouseDragged(event -> {
    		/* on met en gras le noeud sélectionné */
    		((Shape) o).setStrokeWidth(3.0);
    		/* les positions du noeuds sont actualisé par rapport à la position de la souris */
    		posXNoeud.setText("" + event.getX());
            posYNoeud.setText("" + event.getY());
            /* Initialisation d'un tableau de double contenant les positions de la souris
             * en temps réel 
             */
            double[] EditPosition = {0,0};
            EditPosition[0] = event.getX();
            EditPosition[1] = event.getY();
            /* Changement de position du noeud par rapport à la souris */
            noeud.setPositions(EditPosition); 
            /* Actualise les positions des liens reliés au noeud par rapport à sa position */
            graphe.relocalisation();
        	/* Obtention de la copie profonde de l'objet sélectionné */
            Graphe previousState = graphe.clone();
         // Enregistre l'état du graphe après l'opération de déplacement du noeud
            Graphe nextState = graphe.clone();
            // Crée une instance de GraphAction représentant l'opération de déplacement du noeud
            GraphAction action = new GraphAction(previousState, nextState);   
            // Ajoute l'action à la pile d'actions gérée par l'objet ActionManager
            actionManager.executeAction(action);
    	});
    }
    
    /**
     * Met en gras l'élément sélectionné, permet au lien sélectionné de changer de noeuds
     * @param Node n élément contenu dans la zone de dessin
     * @param Object o l'élément sélectionné
     */
    void draggedLink(Node n, Object o) {
    	/* Obtention de la copie profonde de l'objet sélectionné */
        Graphe previousState = graphe.clone();
    	n.setOnMouseDragged(event -> {
    		/* Si un lien orienté est sélectionné on le met en gras */
    		if((o instanceof LienOriente)) {
        		try {
            		for(Shape line : ((LienOriente) o).getQuadCurved()) {
            			line.setStrokeWidth(3.0);
            		}
        			for(Shape line : ((LienOriente) o).getArc()) {
        				line.setStrokeWidth(3.0);
        			}
        		} catch (NullPointerException e) {}
        	/* Met en gras un lien orienté pondéré */
    		} else if (o instanceof LienOrientePondere) {
    			try {
            		for(Shape line : ((LienOrientePondere) o).getQuadCurved()) {
            			line.setStrokeWidth(3.0);
            		}
        			for(Shape line : ((LienOrientePondere) o).getArc()) {
        				line.setStrokeWidth(3.0);
        			}
        		} catch (NullPointerException e) {}
    		/* Met en gras un lien non orienté */
    		} else  if(o instanceof LienNonOriente) {
    			((LienNonOriente) o).getLine().setStrokeWidth(3.0);
    		/* Met en gras un lien probabiliste  */
    		} else if(o instanceof LienProbabiliste) {
    			try {
            		for(Shape line : ((LienProbabiliste) o).getQuadCurved()) {
            			line.setStrokeWidth(3.0);
            		}
        			for(Shape line : ((LienProbabiliste) o).getArc()) {
        				line.setStrokeWidth(3.0);
        			}
        		} catch (NullPointerException e) {}
    		}
    		/* Aperçu de la ligne en temps réel quand on déplace le lien */
    		previewedLine.setStrokeWidth(3.0);
            previewedLine.setStartX(((Lien) o).getNoeuds()[0].getPositions()[0]);
            previewedLine.setStartY(((Lien) o).getNoeuds()[0].getPositions()[1]);
            previewedLine.setEndX(event.getX());
            previewedLine.setEndY(event.getY());
    	});
    	n.setOnMouseReleased(event -> {
    		/* N'affiche plus la ligne d'aperçu */
    		exitPreview(event);
    		/* Récupère la position X et Y de la souris au moment ou le clic est relaché */
    		double[] pos = {event.getX(), event.getY()};
    		/* Vérifie que l'élément ou le clic de souris a été relaché est un noeud */
    		if(graphe.elementClicked(pos, zoneDessin) != null
    		   && graphe.elementClicked(pos, zoneDessin) != o) {
    			/* initialisation du nouveau noeud du lien */
    			try {
    				Noeud noeud = (Noeud) graphe.elementClicked(pos, zoneDessin);
        			/* Création d'un nouveau tableau de noeuds qui sera le celui du lien */
        			Noeud[] noeuds = {((Lien) o).getNoeuds()[0], noeud};
        			boolean ok = true;
        			for(int i = 0; i < listeElements.getItems().size() ; i++) {
        				String test = listeElements.getItems().get(i).toString();
        				/* Vérifie dans la liste des éléments que le lien n'existe pas déjà */
        				if(test.equals("Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]")) {
        					ok = false;
        				}
        			} 
    				/* Si le lien n'existe pas alors on créer un nouveau lien à l'emplacement souhaité */
        			if(ok) {
        				graphe.supprimerLien((Lien) o, zoneDessin, listeElements);
        				Lien nouveauLien = graphe.creerLien(noeuds[0], noeuds[1]);
        				nouveauLien.dessiner(zoneDessin);
        				listeElements.getItems().addAll(nouveauLien);
        			}
    			} catch (Exception e) {}
    		}
    	});
    	Graphe nextState = graphe.clone();
        GraphAction action = new GraphAction(previousState, nextState);
        actionManager.executeAction(action);
    }
    
    /**
     * Modification des propriété d'un noeud sélectionner par les nouvelles valeurs assigné par l'utilisateur
     * @param event click
     */
    @FXML
    void modifNoeud(ActionEvent event) {
    	Graphe previousState = graphe.clone();
        Noeud noeudAModif = (Noeud) selectedObject;
        double[] positions = {Double.parseDouble(posXNoeud.getText()), Double.parseDouble(posYNoeud.getText())};
        graphe.modifNomNoeud(noeudAModif, nomNoeud.getText());
        graphe.modifPos(noeudAModif, positions);
        graphe.modifRadius(noeudAModif, Double.parseDouble(radiusNoeud.getText()));
        Graphe nextState = graphe.clone();
        GraphAction action = new GraphAction(previousState, nextState);	
        actionManager.executeAction(action);
    }
    
    /**
     * Modification des propriété d'un lien sélectionner par les nouvelles valeurs assigné par l'utilisateur
     * @param event click
     */
    @FXML
    void modifLien(ActionEvent event) {
    	Graphe previousState = graphe.clone();
        Lien lienAModif = (Lien) selectedObject;
        String[] labelNoeudARelier = {
            noeud1Lien.getText(),
            noeud2Lien.getText()
        };
        try {
            Noeud n1 = graphe.getNode(labelNoeudARelier[0]);
            Noeud n2 = graphe.getNode(labelNoeudARelier[1]);
            Noeud[] nodes = {n1, n2};
            graphe.modifLien(lienAModif, nodes, zoneDessin);
            if (graphe instanceof GrapheProbabiliste) {
            	GrapheProbabiliste g = (GrapheProbabiliste) graphe;
            	g.modifValeur(lienAModif, Double.parseDouble(valeurLien.getText()));
            } else if (graphe instanceof GrapheOrientePondere){
            	GrapheOrientePondere g = (GrapheOrientePondere) graphe;
            	g.modifValeur(lienAModif, Double.parseDouble(valeurLien.getText()));
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        lienAModif.actualiser();
        Graphe nextState = graphe.clone();
        GraphAction action = new GraphAction(previousState, nextState);	
        actionManager.executeAction(action);
    }
    

    @FXML
    public void supprimerNoeud() {
    	if (selectedObject != null) {
    		Graphe previousState = graphe.clone();
        	Noeud noeudASuppr = (Noeud) selectedObject;
        	graphe.supprimerNoeud(noeudASuppr, zoneDessin, listeElements);
        	listeElements.getSelectionModel().clearSelection();
            editionProprietesLien.setVisible(false);
            editionProprietesNoeud.setVisible(false);
            Graphe nextState = graphe.clone();
            GraphAction action = new GraphAction(previousState, nextState);	
            actionManager.executeAction(action);	
            selectedObject = null;
            graphe.reset();
    	}
    }
    

    @FXML
    public void supprimerLien() {
    	if (selectedObject != null) {
    		Graphe previousState = graphe.clone();
        	Lien lienASuppr = (Lien) selectedObject;
        	listeElements.getItems().remove(selectedObject);
        	graphe.supprimerLien(lienASuppr, zoneDessin, listeElements);
        	listeElements.getSelectionModel().clearSelection();
            editionProprietesLien.setVisible(false);
            editionProprietesNoeud.setVisible(false);
            Graphe nextState = graphe.clone();
            GraphAction action = new GraphAction(previousState, nextState);	
            actionManager.executeAction(action);
            selectedObject = null;
            graphe.reset();
    	}
    }
    
    /**
     * Outil de preview sur la zone de dessin, selon le mode sélectionner
     * 1 : preview d'un cercle suivant la souris
     * 2 : preview d'un lien, si aucun noeud n'a été sélectionner, n'affiche rien
     *     si un noeud à été sélectionner, affiche un lien partant de ce noeud
     *     et va qui suit la souris
     * @param event déplacement de la souris
     */
    @FXML
    void preview(MouseEvent event) {
    	if(actualMode != 3) {
        	reset2();
    	}
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
    
    void reset2() {
    	ObservableList<Node> childrens = zoneDessin.getChildren();
		for (Node n : childrens) {
			n.setOnMouseDragged(null);
			n.setOnMouseReleased(null);
		}
    }

    /**
     * Affiche les propriétés de l'élement sélectionner
     * @param event sélection d'un élément dans la comboBox
     */
    @FXML
    void elementSelected(ActionEvent event) {
    	try {
            Noeud node = (Noeud) listeElements.getValue();
            editionProprietesLien.setVisible(false);
            editionProprietesNoeud.setVisible(true);
            nomNoeud.setText(node.getNom());
            posXNoeud.setText("" + node.getPositions()[0]);
            posYNoeud.setText("" + node.getPositions()[1]);
            radiusNoeud.setText("" + node.getRadius());
            selectedObject = node;
            Circle circle = node.getCircle();
            graphe.reset();
            circle.setStrokeWidth(3.0);
        } catch (Exception e) {}
    	try {
    		Lien link = (Lien) listeElements.getValue();
            editionProprietesLien.setVisible(true);
            editionProprietesNoeud.setVisible(false);
            this.temp3 = refreshLinkedNode1Element();
            this.temp4 = refreshLinkedNode2Element();
            noeud1Lien.setText(link.getNoeuds()[0].getNom());
            noeud2Lien.setText(link.getNoeuds()[1].getNom());
            this.temp1 = refreshLinkedNode1Element();
            this.temp2 = refreshLinkedNode2Element();
            selectedObject = link;
            if(link instanceof LienNonOriente) {
            	LienNonOriente lienNOR = (LienNonOriente) link;
            	graphe.reset();
            	lienNOR.getLine().setStrokeWidth(3.0); 
            } else if(link instanceof LienOriente) {
            	LienOriente lienOR = (LienOriente) link;
            	graphe.reset();
            	try {
            		for(Shape shape : lienOR.getArc()) {
            			shape.setStrokeWidth(3.0);
            		}
            	} catch (NullPointerException e) {}
	            	for(Shape quadCurved : lienOR.getQuadCurved()) {
	            		quadCurved.setStrokeWidth(3.0);
        		}
            } else if (link instanceof LienOrientePondere) {
            	LienOrientePondere lienORP = (LienOrientePondere) link;
            	graphe.reset();
            	try {
            		for(Shape shape : lienORP.getArc()) {
            			shape.setStrokeWidth(3.0);
            		}
            	} catch (NullPointerException e) {}
	            	for(Shape quadCurved : lienORP.getQuadCurved()) {
	            		quadCurved.setStrokeWidth(3.0);
        		}
            } else if(link instanceof LienProbabiliste) {
            	LienProbabiliste lienProba = (LienProbabiliste) link;
            	graphe.reset();
            	try {
            		for(Shape shape : lienProba.getArc()) {
            			shape.setStrokeWidth(3.0);
            		}
            	} catch (NullPointerException e) {}
	            	for(Shape quadCurved : lienProba.getQuadCurved()) {
	            		quadCurved.setStrokeWidth(3.0);
        		}	
            }
    	} catch (Exception e) {}
    	
    }

    /**
     * Envoi sur le fichier pdf du manuel d'utilisation sur un navigateur web
     * @param event clique sur l'option 'manuel d'utilisation' dans le menu d'aide
     */
    @FXML
    void manuelUtilisation(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://zzcc.store/"));
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
    	if (aUneSauvegarge) {
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
            aUneSauvegarge = true;
    	} catch (Exception e) {
    		System.err.println(e);
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
        aUneSauvegarge = true;
        enregistrerID.setDisable(false);
    	enregistrerSousID.setDisable(false);
    }
}
