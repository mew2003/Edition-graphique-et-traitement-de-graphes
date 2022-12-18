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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
    }
    
    // Création du manager permettant de créer tout type de graphe
    FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    
    // Création de la factory GrapheNonOriente (v1)
    FactoryGraphe f = manager.creerFactory("GrapheNonOriente");
    
    Graphe graphe = f.creerGraphe();
    
    // Permet le stockage des noeuds à relier par un lien (maximum 2 noeuds)
    Noeud[] noeudARelier = new Noeud[2];
    
    Object selectedObject;
    
    /**
     * TODO: Comment this method
     * @param evt 
     */
    @FXML
    public void dessin(MouseEvent evt) {
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
                    System.out.println(link.toString());
                }
            } else {
                editionProprietesLien.setVisible(false);
                editionProprietesNoeud.setVisible(false);
            }
        }   
    }
    
    /**
     * Ouvre l'interface de création de graphique au clic
     * sur l'option nouveau du menu Graphe
     * @param event permet de gérer le clic
     * @throws IOException s'il y a un problème d'entrée-sortie
     */
    @FXML
    private void switchToNew(javafx.event.ActionEvent event) throws IOException {
        
        // charge la fenêtre d'accueil pour créer un nouveau graphe
        root = FXMLLoader.load(getClass().getResource("FXML.fxml"));
        primaryStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show(); 
    }

    @FXML
    void modifNoeud(ActionEvent event) {
        Noeud noeudAModif = (Noeud) selectedObject;
        double[] positions = {Double.parseDouble(posXNoeud.getText()), Double.parseDouble(posYNoeud.getText())};
        noeudAModif.setNom(nomNoeud.getText());
        noeudAModif.setPositions(positions);
        noeudAModif.setRadius(Double.parseDouble(radiusNoeud.getText()));
    }
}
