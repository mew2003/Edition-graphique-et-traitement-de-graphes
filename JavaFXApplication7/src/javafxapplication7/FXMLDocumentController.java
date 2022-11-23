/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sebastien.dasilvaoli
 */
public class FXMLDocumentController implements Initializable{
    
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

    @FXML
    void getMousePosition(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void getMousePosition() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        System.out.println(p.getX());
        System.out.println(p.getY());
    }
}
