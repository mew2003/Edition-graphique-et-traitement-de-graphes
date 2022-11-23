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
    private RadioButton arc;

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
        // TODO
    }    
    
    @FXML
    private AnchorPane zoneDessin;
    
    @FXML
    public void dessin(MouseEvent evt) {
        
        Circle circle = new Circle(evt.getX(), evt.getY(), RADIUS);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        zoneDessin.getChildren().add(circle);
        
        System.out.println(evt.getX());
        System.out.println(evt.getY());
    }
}
