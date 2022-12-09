/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;


import java.awt.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import v1.FactoryGraphe;
import v1.FactoryGrapheManager;
import v1.Noeud;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 *
 * @author sebastien.dasilvaoli
 */
public class FXMLDocumentController implements Initializable{
    
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
    private ComboBox<?> listeElements;
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
    
    private Stage primaryStage;

    private Scene scene;

    private Parent root;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Set<String> typesNoeuds = f.getTypesNoeuds();
        Set<String> typesLiens = f.getTypesLiens();
        listeNoeuds.getItems().addAll(typesNoeuds);
        listeLiens.getItems().addAll(typesLiens);
        listeNoeuds.getSelectionModel().selectFirst();
        listeLiens.getSelectionModel().selectFirst();
    }
    
    FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    FactoryGraphe f = manager.creerFactory("GrapheNonOriente");
    ArrayList<Noeud> graphesNoeuds = new ArrayList<>();
    final String DEFAULT_NAME = "default";
    int indexNoeud = 1;
    final double DEFAULT_RADIUS = 20.0;
    
    @FXML
    public void dessin(MouseEvent evt) {
        
        if (noeud.isSelected()) {
            double[] positions = {evt.getX(), evt.getY()};
            graphesNoeuds.add(f.creerNoeud(listeNoeuds.getSelectionModel().getSelectedItem(), DEFAULT_NAME + indexNoeud++, positions, DEFAULT_RADIUS));
            graphesNoeuds.get(graphesNoeuds.size() - 1).dessinerNoeud(zoneDessin);
        } else if (lien.isSelected()) {
            
        } else {
            ObservableList<Node> children = zoneDessin.getChildren();
            for (Node n : children) {
                //if (evt.getX() > n.getLayoutX() - n.getScaleX())
                for (Noeud no : graphesNoeuds) {
                    if (no.getCircle().equals(n)) {
                        if (evt.getX() > no.getPositions()[0] - no.getRadius() && evt.getX() < no.getPositions()[0] + no.getRadius()
                            && evt.getY() > no.getPositions()[1] - no.getRadius() && evt.getY() < no.getPositions()[1] + no.getRadius()) {  
                            System.out.println(n + " | " + no.toString());
                        }
                    }
                }
            }
            
        } 
        
//            Label nomNoeud = new Label();
//            nomNoeud.setText("yo");
//            nomNoeud.setLayoutX(evt.getX());
//            nomNoeud.setLayoutY(evt.getY());
    }
    
    private void switchToMain(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
         primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         primaryStage.setScene(scene);
         primaryStage.show(); 
    }
}
