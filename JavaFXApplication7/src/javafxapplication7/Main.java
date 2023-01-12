package javafxapplication7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 */
public class Main extends Application {
	
	private static Scene scene;
	
	private static Stage editableStage;
    
    @Override
    public void start(Stage stage) throws Exception {
    	editableStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        scene = new Scene(root);
        editableStage.setScene(scene);
        editableStage.setTitle("Logiciel d'édition et de traitement de graphes");
        editableStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Scene getScene() {
    	return scene;
    }
    
    public static Stage getStage() {
    	return editableStage;
    }
}