package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;

public class LienProbabiliste extends Lien {
	
	// Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private Line arrow1, arrow2;
    
    private Arc arc = null;
    
    private String nom;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";
   
    private final double LONGUEUR_ARROW = 10.0;
    
    private final double ROTATION_ARROW = 0.52;
    
    private final double BOUCLE_ANGLE = -14.49;
    
    private final double BOUCLE_DEPART = 0.52;
    
    private final double BOUCLE_SIZE = 208.98;

	public LienProbabiliste(Noeud[] noeuds, int nbLien) {
		this.nom = DEFAULT_NAME + nbLien;
        this.noeuds = noeuds;
	}
	
	@Override
	public Noeud[] getNoeuds() {
		return noeuds;
	}

	public Arc getArc() {
		return arc;
	}

	@Override
	public void setNoeuds(Noeud[] value, AnchorPane zoneDessin) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dessiner(AnchorPane zoneDessin) {
		// TODO Auto-generated method stub

	}

	@Override
	public double[] lineDrawingPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void actualiser() {
		// TODO Auto-generated method stub

	}
	
	@Override
    public String toString() {
        return "Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]";
    }

}
