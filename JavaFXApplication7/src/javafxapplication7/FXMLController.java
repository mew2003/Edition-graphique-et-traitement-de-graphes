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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import temp.FactoryGraphe;
import temp.FactoryGrapheManager;

/**
 * FXML Controller class
 *
 * @author maxime.froissant
 */


public class FXMLController implements Initializable {
    
    private static FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    
    private Stage primaryStage;

    private Scene scene;
    
    private Stage stage;
    
    @FXML
    private Button valider;

    @FXML
    private ComboBox<String> typesGraphes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Set<String> listeFactory = manager.getRegiteredFactories();
        typesGraphes.getItems().addAll(listeFactory);
        typesGraphes.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
