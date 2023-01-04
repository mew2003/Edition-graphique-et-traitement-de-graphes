/*
 * Contrôleur principal de l'application (gère la partie interface graphique)
 */
package javafxapplication7;

import java.awt.Event;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.spi.LocaleNameProvider;

import app.FactoryGraphe;
import app.FactoryGrapheManager;
import app.Graphe;
import app.GrapheNonOriente;
import app.Lien;
import app.LienNonOriente;
import app.LienOriente;
import app.Noeud;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

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
        valeurLien.setEditable(false);
        factory = manager.creerFactory("GrapheNonOriente");
        graphe = factory.creerGraphe();
    }
    
    @FXML
    void creerGrapheOriente(ActionEvent event) {
    	initialisation();
        valeurLien.setEditable(false);
        factory = manager.creerFactory("GrapheOriente");
        graphe = factory.creerGraphe();
    }
    
    @FXML
    void creerGrapheProbabiliste(ActionEvent event) {
    	initialisation();
        factory = manager.creerFactory("GrapheProbabiliste");
        graphe = factory.creerGraphe();
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
    	/* Liste de tous les éléments présents sur la zone de dessin  */
        ObservableList<Node> childrens = zoneDessin.getChildren();
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

        System.out.println(positions[0] + " " + positions[1]);
    	try {
            if (noeudARelier[0] == null) {
                noeudARelier[0] = (Noeud) graphe.elementClicked(positions, zoneDessin);
                System.out.println(noeudARelier[0]);
            } else {
                noeudARelier[1] = (Noeud) graphe.elementClicked(positions, zoneDessin);
                System.out.println(noeudARelier[1]);
            }
        } catch (Exception e) {} 
    	System.out.println(noeudARelier[0] + " " + noeudARelier[1]);
    	
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
                editionProprietesLien.setVisible(false);
                editionProprietesNoeud.setVisible(true);
                nomNoeud.setText(node.getNom());
                posXNoeud.setText("" + node.getPositions()[0]);
                posYNoeud.setText("" + node.getPositions()[1]);
                radiusNoeud.setText("" + node.getRadius());
                selectedObject = node;
                Circle circle = node.getCircle();
                for (Node n : childrens) {
                	if(n instanceof Circle) {
                		if(n.toString().equals(circle.toString())) {
                			circle.setStrokeWidth(3.0);
                        	n.setOnMouseDragged(event -> {
                        		circle.setStrokeWidth(3.0);
                        		posXNoeud.setText("" + event.getX());
                                posYNoeud.setText("" + event.getY());
                                double[] EditPosition = {0,0};
                                EditPosition[0] = event.getX();
                                EditPosition[1] = event.getY();
                                node.setPositions(EditPosition); 
                                graphe.relocalisation();
                        	});
                        } else {
                        	((Circle) n).setStrokeWidth(1.0);
                        }
                	} else if(n instanceof Line) {
                		((Line) n).setStrokeWidth(1.0);       		
                	}
                }
            } catch (Exception e) {
                Lien link = (Lien) o;
                editionProprietesLien.setVisible(true);
                editionProprietesNoeud.setVisible(false);
                noeud1Lien.setText(link.getNoeuds()[0].getNom());
                noeud2Lien.setText(link.getNoeuds()[1].getNom());
                selectedObject = link;
                if(link instanceof LienNonOriente) {
                    LienNonOriente lien = (LienNonOriente) link;
                    for(Node n : childrens) {
                    	if(n instanceof Line) {
                    		if(n.equals(lien.getLine())) {
                    			n.setOnMouseDragged(event -> {
                    				lien.getLine().setStrokeWidth(3.0);
                		            previewedLine.setStartX(lien.getNoeuds()[0].getPositions()[0]);
                		            previewedLine.setStartY(lien.getNoeuds()[0].getPositions()[1]);
                		            previewedLine.setEndX(event.getX());
                		            previewedLine.setEndY(event.getY());
                    			});
                    			n.setOnMouseReleased(event -> {
                    				exitPreview(event);
                    				double[] pos = {event.getX(), event.getY()};
                    				if(graphe.elementClicked(pos, zoneDessin) != null) {
                    					Noeud noeud = (Noeud) graphe.elementClicked(pos, zoneDessin);
                    					Noeud[] noeuds = {link.getNoeuds()[0], noeud};
                        				if(!(noeud.equals(noeuds[0]))) {
                        					lien.setNoeuds(noeuds, zoneDessin);
                        				}
                    				}
                    			});
                    		}
                    	}
                    }
                } else if(link instanceof LienOriente) {
                    LienOriente lien = (LienOriente) link;
                    for(Node n : childrens) {
                    	if(n instanceof Line) {
                    		if(n.equals(lien.getLine()[0])) {
                    			n.setOnMouseDragged(event -> {
                    				for(Line line : lien.getLine()) {
                    					line.setStrokeWidth(3.0);
                    				}
                		            previewedLine.setStartX(lien.getNoeuds()[0].getPositions()[0]);
                		            previewedLine.setStartY(lien.getNoeuds()[0].getPositions()[1]);
                		            previewedLine.setEndX(event.getX());
                		            previewedLine.setEndY(event.getY());
                    			});
                    			n.setOnMouseReleased(event -> {
                    				exitPreview(event);
                    				double[] pos = {event.getX(), event.getY()};
                    				if(graphe.elementClicked(pos, zoneDessin) != null) {
                    					Noeud noeud = (Noeud) graphe.elementClicked(pos, zoneDessin);
                    					Noeud[] noeuds = {link.getNoeuds()[0], noeud};
                        				if(!(noeud.equals(noeuds[0]))) {
                        					lien.setNoeuds(noeuds, zoneDessin);
                        				}
                    				}
                    			});
                    		}
                    	}
                    }
                }
            }
        } else {
            listeElements.getSelectionModel().clearSelection();
            editionProprietesLien.setVisible(false);
            editionProprietesNoeud.setVisible(false);
        }
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
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    

    @FXML
    public void supprimerNoeud() {
    	System.out.println("NON");
    }
    

    @FXML
    public void supprimerLien() {
    	Lien lienASuppr = (Lien) selectedObject;
    	graphe.supprimerLien(lienASuppr, zoneDessin);
    	listeElements.getItems().remove(selectedObject);
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
        	ObservableList<Node> childrens = zoneDessin.getChildren();
    		for (Node n : childrens) {
    			n.setOnMouseDragged(null);
    		}
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
    }
    @FXML void addLinkClicked(MouseEvent event) {
    	actualMode = 2;
    }
    @FXML void SelectClicked(MouseEvent event) {
    	actualMode = 3;
    }

    /**
     * Affiche les propriétés de l'élement sélectionner
     * @param event sélection d'un élément dans la comboBox
     */
    @FXML
    void elementSelected(ActionEvent event) {
    	/* Liste de tous les éléments présents sur la zone de dessin  */
        ObservableList<Node> childrens = zoneDessin.getChildren();
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
            	for(Line l : lienOR.getLine()) {
            		l.setStrokeWidth(3.0);
            	}
            	for(Shape shape : lienOR.getArc()) {
            		shape.setStrokeWidth(3.0);
            	}
            }            
    	} catch (Exception e) {}
    	
    }
}
