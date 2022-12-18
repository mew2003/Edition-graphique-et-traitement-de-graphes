/*
 * Controleur principal de l'application (gère la partie interface graphique)
 */
package javafxapplication7;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import temp.FactoryGraphe;
import temp.FactoryGrapheManager;
import temp.Graphe;
import temp.Lien;
import temp.Noeud;

/**
 *
 * @author sebastien.dasilvaoli
 */
public class FXMLDocumentController implements Initializable{
    
    // Liste des éléments avec intéractions contenu dans l'interface
    @FXML
    private javafx.scene.control.TextField noeud1Lien;
    @FXML
    private AnchorPane editionProprietesLien;
    @FXML
    private javafx.scene.control.TextField posXNoeud;
    @FXML
    private javafx.scene.control.TextField valeurLien;
    @FXML
    private AnchorPane editionProprietesNoeud;
    @FXML
    private javafx.scene.control.TextField posYNoeud;
    @FXML
    private AnchorPane zoneDessin;
    @FXML
    private MenuButton menuEdition;
    @FXML
    private ComboBox<String> listeLiens;
    @FXML
    private MenuButton menuGraphe;
    @FXML
    private RadioButton noeud;
    @FXML
    private MenuButton menuAide;
    @FXML
    private javafx.scene.control.TextField nomNoeud;
    @FXML
    private RadioButton lien;
    @FXML
    private RadioButton selection;
    @FXML
    private javafx.scene.control.TextField radiusNoeud;
    @FXML
    private javafx.scene.control.TextField noeud2Lien;
    @FXML
    private MenuButton menuTraitement;
    @FXML
    private AnchorPane palette;
    @FXML
    private ToggleGroup boutonsPalette;
    @FXML
    private ComboBox<String> listeNoeuds;
    @FXML
    private ComboBox<String> listeElements;
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
    private Stage primaryStage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Button validerModifNoeud;
    @FXML
    private Button validerModifLien;
    @FXML
    private AnchorPane aside;
    
    /**
     * Initialisation de l'interface,
     * Création d'une liste des types de noeuds et de liens obtensible
     * et ajout de ces derniers dans les comboBox de l'application.
     * ComboBox affichant la première valeur disponible dans la liste.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        previewedCircle.setFill(Color.TRANSPARENT);
        previewedCircle.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(previewedCircle);
        previewedLine.setFill(Color.TRANSPARENT);
        previewedLine.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(previewedLine);
    }
    
    @FXML
    void creerGrapheNonOriente(ActionEvent event) {
        aside.setVisible(true);
        valeurLien.setEditable(false);
        f = manager.creerFactory("GrapheNonOriente");
        graphe = f.creerGraphe();
    }
    
    // Création du manager permettant de créer tout type de graphe
    FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    
    // Création de la factory GrapheNonOriente (v1)
    FactoryGraphe f;
    
    Graphe graphe;
    
    // Permet le stockage des noeuds à relier par un lien (maximum 2 noeuds)
    Noeud[] noeudARelier = new Noeud[2];
    
    Object selectedObject;
    
    Circle previewedCircle = new Circle(-100, -100, 25);
    
    Line previewedLine = new Line(-100, -100, -100, -100);
    
    /**
     * TODO: Comment this method
     * @param evt 
     */
    @FXML
    public void zoneDessinEvent(MouseEvent evt) {
        // Position de la souris de l'utilisateur lors du click
        double[] positions = {evt.getX(), evt.getY()};
        
        /* Selon l'option choisi par l'utilisateur parmis une liste de radio button 
           - Créer un noeud
           - Créer un lien
           - Sélectionne un élément de l'application (noeud ou lien) 
        */
        if (noeud.isSelected()) {
            // Réinitialisation de la création de lien
            noeudARelier = new Noeud[2];
            
            // Création et ajout du noeud dans la liste des noeuds de l'appli.
            Noeud nouveauNoeud = graphe.creerNoeud(positions);
            nouveauNoeud.dessiner(zoneDessin);
            // Ajout du noeud dans la comboBox listant tous les éléments présent sur l'interface
            listeElements.getItems().addAll(nouveauNoeud.toString());
        } else if (lien.isSelected()) {
            /* Permet de :
               - Vérifier que l'élément sélectionner soit bien un noeud
               - Ajouter ce noeud dans la liste des noeuds à relier. */
            try {
                if (noeudARelier[0] == null) {
                    noeudARelier[0] = (Noeud) graphe.elementClicked(positions, zoneDessin);
                } else {
                    noeudARelier[1] = (Noeud) graphe.elementClicked(positions, zoneDessin);
                }
            } catch (Exception e) {} // Dans le cas ou ce n'est pas un noeud, ne rien faire
            
            // Si deux noeuds ont été sélectionner les relier.
            if (noeudARelier[1] != null) {
                
                Lien nouveauLien = graphe.creerLien(noeudARelier[0], noeudARelier[1]);
                nouveauLien.dessiner(zoneDessin);
                listeElements.getItems().addAll(nouveauLien.toString());
                
                previewedLine.setStartX(-100);
                previewedLine.setStartY(-100);
                previewedLine.setEndX(-100);
                previewedLine.setEndY(-100);
                // Réinitialisation de la création de lien
                noeudARelier = new Noeud[2]; 
            }
        } else {
            // Réinitialisation de la création de lien
            noeudARelier = new Noeud[2];
            // récupère l'élément séléctionner
            Object o = graphe.elementClicked(positions, zoneDessin);
            /* Si l'élement est null, ne rien afficher
               Dans le cas contraire, l'object cliqué est afficher dans
               l'édition de propriété avec ces valeurs. */
            if (o != null) {
                try {
                    Noeud node = (Noeud) o;
                    editionProprietesLien.setVisible(false);
                    editionProprietesNoeud.setVisible(true);
                    nomNoeud.setText(node.getNom());
                    posXNoeud.setText("" + node.getPositions()[0]);
                    posYNoeud.setText("" + node.getPositions()[1]);
                    radiusNoeud.setText("" + node.getRadius());
                    selectedObject = node;
                } catch (Exception e) {
                    Lien link = (Lien) o;
                    editionProprietesLien.setVisible(true);
                    editionProprietesNoeud.setVisible(false);
                    noeud1Lien.setText(link.getNoeuds()[0].getNom());
                    noeud2Lien.setText(link.getNoeuds()[1].getNom());
                    selectedObject = link;
                }
            } else {
                editionProprietesLien.setVisible(false);
                editionProprietesNoeud.setVisible(false);
            }
        }   
    }

    @FXML
    void modifNoeud(ActionEvent event) {
        // TODO: Corriger bug -> si l'on modifie la position, le lien reste à l'ancien emplacement
        Noeud noeudAModif = (Noeud) selectedObject;
        double[] positions = {Double.parseDouble(posXNoeud.getText()), Double.parseDouble(posYNoeud.getText())};
        noeudAModif.setNom(nomNoeud.getText());
        noeudAModif.setPositions(positions);
        noeudAModif.setRadius(Double.parseDouble(radiusNoeud.getText()));
    }
    
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
            lienAModif.setNoeuds(nodes);
        } catch (Exception e) {
            System.err.println("Noeud Inexistant");
        }
    }
    
    @FXML
    void preview(MouseEvent event) {
        if (noeud.isSelected()) {
            previewedCircle.setCenterX(event.getX());
            previewedCircle.setCenterY(event.getY());
        } else if (lien.isSelected() && noeudARelier[0] != null) {
            previewedLine.setStartX(noeudARelier[0].getPositions()[0]);
            previewedLine.setStartY(noeudARelier[0].getPositions()[1]);
            previewedLine.setEndX(event.getX());
            previewedLine.setEndY(event.getY());
        }
    }
    
    @FXML
    void exitPreview(MouseEvent event) {
        previewedCircle.setCenterX(-100);
        previewedCircle.setCenterY(-100);
        previewedLine.setEndX(-100);
        previewedLine.setEndY(-100);
    }
}
