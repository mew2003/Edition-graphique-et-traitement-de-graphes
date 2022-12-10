/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import v1.FactoryGraphe;
import v1.FactoryGrapheManager;

/**
 * FXML Controller class
 *
 * @author maxime.froissant
 */


public class FXMLController implements Initializable {
    
    private static FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    
    private static FactoryGraphe factory;
    
    private Stage primaryStage;

    private Scene scene;

    private Parent root;
    
    @FXML
    private Button valider;

    @FXML
    private ComboBox<String> typesGraphes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Set<String> listeNoeuds = manager.getRegiteredFactories();
        typesGraphes.getItems().addAll(listeNoeuds);
        typesGraphes.getSelectionModel().selectFirst();
        factory = manager.creerFactory(typesGraphes.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    private void switchToMain(javafx.event.ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show(); 
    }
    
}
