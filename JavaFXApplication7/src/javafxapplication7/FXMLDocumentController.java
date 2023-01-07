/*
 * Contrôleur principal de l'application (gère la partie interface graphique)
 */
package javafxapplication7;

import java.net.URL;
import java.util.ResourceBundle;
import app.FactoryGraphe;
import app.FactoryGrapheManager;
import app.Graphe;
import app.GrapheProbabiliste;
import app.Lien;
import app.LienNonOriente;
import app.LienOriente;
import app.LienProbabiliste;
import app.Noeud;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import tools.probabilite;

/**
 * Contrôleur de l'application
 */
public class FXMLDocumentController implements Initializable {
    
    // Liste des éléments avec interactions contenu dans l'interface
    @FXML
    private TextField noeud1Lien;
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
    private Button validerModifLien;
    @FXML
    private AnchorPane aside;    
    @FXML
    private MenuItem verifierGrapheId;
    @FXML
    private MenuItem existenceCheminId;
    @FXML
    private MenuItem matriceDeTransitionId;
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    /**
     * Initialisation de base pour tout graphe
     */
    public void initialisation() {
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
    	initialisation();
    	valeurLien.setDisable(true);
    	verifierGrapheId.setDisable(true);
    	matriceDeTransitionId.setDisable(true);
    	existenceCheminId.setDisable(true);
        factory = manager.creerFactory("GrapheNonOriente");
        graphe = factory.creerGraphe();
    }
    
    @FXML
    void creerGrapheOriente(ActionEvent event) {
    	initialisation();
    	valeurLien.setDisable(true);
    	verifierGrapheId.setDisable(true);
    	matriceDeTransitionId.setDisable(true);
    	existenceCheminId.setDisable(true);
        factory = manager.creerFactory("GrapheOriente");
        graphe = factory.creerGraphe();
    }
    
    @FXML
    void creerGrapheProbabiliste(ActionEvent event) {
    	initialisation();
    	valeurLien.setDisable(false);
    	verifierGrapheId.setDisable(false);
    	matriceDeTransitionId.setDisable(false);
    	existenceCheminId.setDisable(false);
        factory = manager.creerFactory("GrapheProbabiliste");
        graphe = factory.creerGraphe();
    }
    
    @FXML
    void verifierGraphe(ActionEvent event) {
    	boolean result = probabilite.verifierGraphe((GrapheProbabiliste) graphe);
    	Alert alert = new Alert(result ? AlertType.INFORMATION : AlertType.ERROR);
		alert.setTitle("Vérification du graphe");
		alert.setHeaderText("Résultat : ");
		String text = result ? "Le graphe est valide !" : "Le graphe comporte des erreurs";
		alert.setContentText(text);
		alert.showAndWait();
    }
    
    @FXML
    void matriceDeTransition(ActionEvent event) {
    	TableView<Object> tableView = probabilite.matriceDeTransition((GrapheProbabiliste) graphe);
    	Dialog<ButtonType> dialog = new Dialog<>();
    	dialog.getDialogPane().setContent(tableView);
    	dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
    	dialog.showAndWait();
    }
    
    @FXML
    void existenceChemin(ActionEvent event) {
    	
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
        Noeud nouveauNoeud = graphe.creerNoeud(positions);
        nouveauNoeud.dessiner(zoneDessin);
        // Ajout du noeud dans la comboBox listant tous les éléments présent sur l'interface
        listeElements.getItems().addAll(nouveauNoeud);
    }
    
    /**
     * Création d'un lien depuis l'interface graphique
     * @param positions positions X/Y de la souris
     * @return true si le lien a pu être crée, false sinon
     */
    public boolean creerLien(double[] positions) {
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
        return noeudARelier[1] != null;
    }
    
    /**
     * Permet de récupérer l'élément sélectionner par l'utilisateur depuis l'interface graphique,
     * Selon si c'est un noeud ou un lien, affichage du menu d'édition de propriété correspondant à l'élément.
     * @param positions positions positions X/Y de la souris
     */
    public void selectionner(double[] positions) {
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
            } catch (Exception e) {
                Lien link = (Lien) o;
                /* Affichage des propriétés du lien */
                editionProprietesLien.setVisible(true);
                editionProprietesNoeud.setVisible(false);
                noeud1Lien.setText(link.getNoeuds()[0].getNom());
                noeud2Lien.setText(link.getNoeuds()[1].getNom());
                if (link instanceof LienProbabiliste) {
                	LienProbabiliste l = (LienProbabiliste) link;
                	valeurLien.setText("" + l.getValue());
                }
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
            	}
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
    	});
    }
    
    @FXML
    /**
     * Met en gras l'élément sélectionné, permet au lien sélectionné de changer de noeuds
     * @param Node n élément contenu dans la zone de dessin
     * @param Object o l'élément sélectionné
     */
    void draggedLink(Node n, Object o) {
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
        	/* Si ce n'est pas un lien orienté on met en gras le lien non orienté */
    		} else if(o instanceof LienNonOriente) {
    			((LienNonOriente) o).getLine().setStrokeWidth(3.0);
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
        				/* Si le lien n'existe pas alors on créer un nouveau lien à l'emplacement souhaité */
            			if(ok) {
            				graphe.supprimerLien((Lien) o, zoneDessin, listeElements);
            				Lien nouveauLien = graphe.creerLien(noeuds[0], noeuds[1]);
            				nouveauLien.dessiner(zoneDessin);
            				listeElements.getItems().addAll(nouveauLien);
            			}
        			} 
    			} catch (Exception e) {}
    		}
    	});
    }

    /**
     * Modification des propriété d'un noeud sélectionner par les nouvelles valeurs assigné par l'utilisateur
     * @param event click
     */
    @FXML
    void modifNoeud(ActionEvent event) {
        Noeud noeudAModif = (Noeud) selectedObject;
        double[] positions = {Double.parseDouble(posXNoeud.getText()), Double.parseDouble(posYNoeud.getText())};
        graphe.modifNomNoeud(noeudAModif, nomNoeud.getText());
        graphe.modifPos(noeudAModif, positions);
        graphe.modifRadius(noeudAModif, Double.parseDouble(radiusNoeud.getText()));
    }
    
    /**
     * Modification des propriété d'un lien sélectionner par les nouvelles valeurs assigné par l'utilisateur
     * @param event click
     */
    @FXML
    void modifLien(ActionEvent event) {
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
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        lienAModif.actualiser();
    }
    

    @FXML
    public void supprimerNoeud() {
    	Noeud noeudASuppr = (Noeud) selectedObject;
    	graphe.supprimerNoeud(noeudASuppr, zoneDessin, listeElements);
    	listeElements.getSelectionModel().clearSelection();
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(false);
    }
    

    @FXML
    public void supprimerLien() {
    	Lien lienASuppr = (Lien) selectedObject;
    	listeElements.getItems().remove(selectedObject);
    	graphe.supprimerLien(lienASuppr, zoneDessin, listeElements);
    	listeElements.getSelectionModel().clearSelection();
        editionProprietesLien.setVisible(false);
        editionProprietesNoeud.setVisible(false);
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
            noeud1Lien.setText(link.getNoeuds()[0].getNom());
            noeud2Lien.setText(link.getNoeuds()[1].getNom());
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
            }            
    	} catch (Exception e) {}
    	
    }
}
