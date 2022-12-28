package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class LienOriente extends Lien {
	
    // Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private Line line, arrow1, arrow2;
    
    //TODO: Ajouté la flèche (Graphiquement)
    
    private String nom;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";
   
    private final double LONGUEUR_ARROW = 10.0;
    
    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     * @param nbLien permet le nom unique du lien
     */
    public LienOriente(Noeud[] noeuds, int nbLien) {
        this.nom = DEFAULT_NAME + nbLien;
        this.noeuds = noeuds;
    }

	@Override
	public Noeud[] getNoeuds() {
		return noeuds;
	}

	@Override
	public void setNoeuds(Noeud[] value) {
		this.noeuds = value;
		double[] linePos = lineDrawingPositions();
        line.setStartX(linePos[0]);
        line.setStartY(linePos[1]);
        line.setEndX(linePos[2]);
        line.setEndY(linePos[3]);
	}

    @Override
    public void dessiner(AnchorPane zoneDessin) {
        double[] linePos = lineDrawingPositions();
        double[] arrowPos = arrowPositions(linePos);
        this.line = new Line(linePos[0], linePos[1], linePos[2], linePos[3]);
        this.arrow1 = new Line();
        this.arrow2 = new Line();
        line.setFill(Color.TRANSPARENT);
        line.setStroke(Color.BLACK);
        arrow1.setFill(Color.TRANSPARENT);
        arrow1.setStroke(Color.BLACK);
        arrow2.setFill(Color.TRANSPARENT);
        arrow2.setStroke(Color.BLACK);
        zoneDessin.getChildren().addAll(line);
    }
    
    private double[] arrowPositions(double[] linePos) {
    	double hauteur = Math.abs(linePos[1] - linePos[3]);
    	double largeur = Math.abs(linePos[0] - linePos[2]);
    	double degreInclinaison = Math.atan(hauteur/largeur) * (180/Math.PI);
    	System.out.println("SX : " + linePos[0] + " SY : " + linePos[1] + " EX : " + linePos[2] + " EY : " + linePos[3]);
    	System.out.println("h : " + hauteur + " l : " + largeur + " D : " + degreInclinaison);
		return null;
	}

	@Override
    public void actualiser() {
        double[] linePos = lineDrawingPositions();
        line.setStartX(linePos[0]);
        line.setStartY(linePos[1]);
        line.setEndX(linePos[2]);
        line.setEndY(linePos[3]);
    }
	
    /**
     * Donne les positions X/Y du départ et de la fin du lien par rapport au noeuds qu'il relie
     * @return les positions
     */
    public double[] lineDrawingPositions() {
        double[] posNoeud1 = noeuds[0].getPositions();
        double[] posNoeud2 = noeuds[1].getPositions();
        double L = Math.sqrt(Math.pow(posNoeud2[0] - posNoeud1[0],2) + Math.pow(posNoeud2[1] - posNoeud1[1],2));
        double[] vecteurAAPrime = {(posNoeud2[0]-posNoeud1[0]) * noeuds[0].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[0].getRadius() / L};
        double[] result = {posNoeud1[0] + vecteurAAPrime[0], posNoeud1[1] + vecteurAAPrime[1], posNoeud2[0] - vecteurAAPrime[0], posNoeud2[1] - vecteurAAPrime[1]};
        return result;
    }

}
