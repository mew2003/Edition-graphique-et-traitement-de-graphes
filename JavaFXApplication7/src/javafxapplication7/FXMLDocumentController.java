/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sebastien.dasilvaoli
 */
public class FXMLDocumentController implements Initializable{
    final int RADIUS = 20;
    
    @FXML
    private RadioButton noeud;

    @FXML
    private MenuButton menuAide;

    @FXML
    private AnchorPane zoneDessin;

    @FXML
    private RadioButton lien;

    @FXML
    private RadioButton selection;

    @FXML
    private MenuButton menuEdition;

    @FXML
    private MenuButton menuTraitement;

    @FXML
    private AnchorPane palette;

    @FXML
    private ToggleGroup boutonsPalette;

    @FXML
    private AnchorPane editionProprietes;

    @FXML
    private MenuButton menuGraphe;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    public void dessin(MouseEvent evt) {
        
        if (noeud.isSelected()) {
            Circle circle = new Circle(evt.getX(), evt.getY(), RADIUS);
            Label nomNoeud = new Label();
            nomNoeud.setText("yo");
            nomNoeud.setLayoutX(evt.getX());
            nomNoeud.setLayoutY(evt.getY());
            circle.setFill(Color.TRANSPARENT);
            circle.setStroke(Color.BLACK);
            zoneDessin.getChildren().addAll(circle, nomNoeud);
        }
    }
}
