/*
 * Controleur principal de l'application (gère la partie interface graphique)
 */
package javafxapplication7;

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
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import v1.FactoryGraphe;
import v1.FactoryGrapheManager;
import v1.Noeud;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import v1.Lien;

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
        Set<String> typesNoeuds = f.getTypesNoeuds();
        Set<String> typesLiens = f.getTypesLiens();
        listeNoeuds.getItems().addAll(typesNoeuds);
        listeLiens.getItems().addAll(typesLiens);
        listeNoeuds.getSelectionModel().selectFirst();
        listeLiens.getSelectionModel().selectFirst();
    }
    
    // Création du manager permettant de créer tout type de graphe
    FactoryGrapheManager manager = FactoryGrapheManager.getInstance();
    
    // Création de la factory GrapheNonOriente (v1)
    FactoryGraphe f = manager.creerFactory("GrapheNonOriente");
    
    // Liste des noeuds présent sur l'interface
    ArrayList<Noeud> graphesNoeuds = new ArrayList<>();
    
    // Liste des liens présent sur l'interface
    ArrayList<Lien> graphesLiens = new ArrayList<>();
    
    // Nom par défaut d'un noeud
    final String DEFAULT_NAME = "default";
    
    /* Indexage du nombre de noeud crée depuis le lancement de l'application.
       Permet la création de noeud à nom unique. */
    int indexNoeud = 1;
    
    // Radius par défaut d'un noeud
    final double DEFAULT_RADIUS = 20.0;
    
    // Permet le stockage des noeuds à relier par un lien (maximum 2 noeuds)
    Noeud[] noeudARelier = new Noeud[2];
    
    // Contient le dernier object cliqué par l'utilisateur (noeud ou lien)
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
            graphesNoeuds.add(f.creerNoeud(listeNoeuds.getSelectionModel().getSelectedItem(), 
                                             DEFAULT_NAME + indexNoeud++,
                                             positions, DEFAULT_RADIUS));
            Noeud nouveauNoeud = graphesNoeuds.get(graphesNoeuds.size() - 1);
            nouveauNoeud.dessinerNoeud(zoneDessin);
            // Ajout du noeud dans la comboBox listant tous les éléments présent sur l'interface
            listeElements.getItems().addAll(nouveauNoeud.toString());
        } else if (lien.isSelected()) {
            /* Permet de :
               - Vérifier que l'élément sélectionner soit bien un noeud
               - Ajouter ce noeud dans la liste des noeuds à relier. */
            try {
                if (noeudARelier[0] == null) {
                    noeudARelier[0] = (Noeud) elementClicked(positions[0], positions[1]);
                } else {
                    noeudARelier[1] = (Noeud) elementClicked(positions[0], positions[1]);
                }
            } catch (Exception e) {} // Dans le cas ou ce n'est pas un noeud, ne rien faire
            
            // Si deux noeuds ont été sélectionner les relier.
            if (noeudARelier[1] != null) {
                graphesLiens.add(f.creerLien(listeLiens.getSelectionModel().getSelectedItem(), noeudARelier));
                graphesLiens.get(graphesLiens.size() - 1).dessinerLien(zoneDessin);
                
                // Réinitialisation de la création de lien
                noeudARelier = new Noeud[2]; 
            }
        } else {
            // Réinitialisation de la création de lien
            noeudARelier = new Noeud[2]; 
            
            // récupère l'élément séléctionner
            Object o = elementClicked(positions[0], positions[1]);
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
    
    /**
     * Permet d'obtenir l'élément cliqué sur l'interface
     * @param mouseX position X de la souris au moment du click
     * @param mouseY position Y de la souris au moment du click
     * @return Un object correspondant au noeud ou lien si un élément est en
     *         effet bien cliqué ou renvoie la valeur null si rien n'a été
     *         selectionné.
     */
    public Object elementClicked(double mouseX, double mouseY) {
        ObservableList<Node> children = zoneDessin.getChildren();
        for (Node n : children) {
            if (n instanceof Circle) {
                for (Noeud no : graphesNoeuds) {
                    if (isNodeClicked(mouseX, mouseY, no)) {
                        return no;
                    }
                }
            }
            if (n instanceof Line) {
                for (Lien li : graphesLiens) {
                    if (isLinkClicked(mouseX, mouseY, li, ((Line) n).getStrokeWidth() / 10)) {
                        return li;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Vérifie qu'une position X,Y soit situer à l'emplacement d'un noeud
     * @param mouseX position X de la souris
     * @param mouseY position Y de la souris
     * @param noeud Noeud à vérifier
     * @return true si les positions se trouve en effet à l'emplacement d'un noeud
     *         false dans le cas contraire.
     */
    public boolean isNodeClicked(double mouseX, double mouseY, Noeud noeud) {
        return mouseX > noeud.getPositions()[0] - noeud.getRadius() && mouseX < noeud.getPositions()[0] + noeud.getRadius()
               && mouseY > noeud.getPositions()[1] - noeud.getRadius() && mouseY < noeud.getPositions()[1] + noeud.getRadius();
    }
    
    /**
     * Vérifie qu'une position X,Y soit situer à l'emplacement d'un noeud
     * avec une précision +/- donnée.
     * @param mouseX position X de la souris
     * @param mouseY position Y de la souris
     * @param lien Lien à vérifier
     * @param precision precision +/- pour laquelle la position de la souris
     *                  peut-être tolérer.
     * @return true si les positions se trouve en effet à l'emplacement d'un lien
     *         false dans le cas contraire.
     */
    public boolean isLinkClicked(double mouseX, double mouseY, Lien lien, double precision) {
        double[] node1 = lien.getNoeuds()[0].getPositions();
        double[] node2 = lien.getNoeuds()[1].getPositions();
        // Distance entre les points
        double distN1N2 = distance(node1[0], node1[1], node2[0], node2[1]);
        double distN1L = distance(node1[0], node1[1], mouseX, mouseY);
        double distN2L = distance(node2[0], node2[1], mouseX, mouseY);
        // Degré de précision tolérer (MIN / MAX)
        double floor = (distN1L + distN2L) - precision;
        double ceil = (distN1L + distN2L) + precision;
        return floor < distN1N2 && distN1N2 < ceil;
    }
    
    /**
     * Calcul la distance entre deux points
     * @param x1 position X point 1
     * @param y1 position Y point 1
     * @param x2 position X point 2
     * @param y2 position Y point 2
     * @return la distance entre les deux points
     */
    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    /**
     * TODO: Comment this method
     * @return 
     */
    public Object modifNoeud() {
        System.out.println("test");
        Noeud noeudAModif = (Noeud) selectedObject;
        noeudAModif.setNom(nomNoeud.getText());
        return null; // stub
    }
}
