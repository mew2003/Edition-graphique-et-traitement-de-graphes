package app;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;

public class LienOriente extends Lien {
	
    // Noeud que relie le lien
    private Noeud[] noeuds;
    
    // Représentation graphique du lien
    private Line line, arrow1, arrow2;
    
    private Arc arc;
    
    private String nom;
    
    // Nom par défaut d'un lien
    private final String DEFAULT_NAME = "default";
   
    private final double LONGUEUR_ARROW = 10.0;
    
    private final double ROTATION_ARROW = Math.toRadians(30.0);
    
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
    public String toString() {
        return "Lien : [" + noeuds[0].getNom() + ", " + noeuds[1].getNom() + "]";
    }

    @Override
    public void dessiner(AnchorPane zoneDessin) {
    	if (noeuds[0] == noeuds[1]) {
    		double[] depart = departArc();
    		double[] linePos = {depart[0], depart[1] - noeuds[0].getRadius() / 2.0, depart[0], depart[1]};
    		double[] arrowPos = arrowPositions(linePos);
    		this.arrow1 = new Line(depart[0], depart[1], arrowPos[0], arrowPos[1]);
            this.arrow2 = new Line(depart[0], depart[1], arrowPos[2], arrowPos[3]);
            arc.setFill(Color.TRANSPARENT);
    		arc.setStroke(Color.BLACK);
    		zoneDessin.getChildren().addAll(arc);
    	} else {
    		double[] linePos = lineDrawingPositions();
            double[] arrowPos = arrowPositions(linePos);
            this.line = new Line(linePos[0], linePos[1], linePos[2], linePos[3]);
            this.arrow1 = new Line(linePos[2], linePos[3], arrowPos[0], arrowPos[1]);
            this.arrow2 = new Line(linePos[2], linePos[3], arrowPos[2], arrowPos[3]);
            line.setFill(Color.TRANSPARENT);
            line.setStroke(Color.BLACK);
            zoneDessin.getChildren().addAll(line);
    	}
    	arrow1.setFill(Color.TRANSPARENT);
        arrow1.setStroke(Color.BLACK);
        arrow2.setFill(Color.TRANSPARENT);
        arrow2.setStroke(Color.BLACK);
    	zoneDessin.getChildren().addAll(arrow1, arrow2);
    }
    
    private double[] departArc() {
    	double[] pointC = {noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius()};
		double arcRadius = noeuds[0].getRadius() / 2.0;
		arc = new Arc(pointC[0], pointC[1], arcRadius, arcRadius, -14.49, 208.98); //Trouver sur Geogebra
		//Translation 0,0
		pointC[0] -= noeuds[0].getPositions()[0];
        pointC[1] -= noeuds[0].getPositions()[1];
		double[] pointCPrime = {Math.cos(Math.toRadians(28.84))*pointC[0]+(-Math.sin(Math.toRadians(28.84))*pointC[1]),
                			    Math.sin(Math.toRadians(28.84))*pointC[0]+Math.cos(Math.toRadians(28.84))*pointC[1]}; // Trouver sur Geogebra
		//Translation point de départ
		pointCPrime[0] += noeuds[0].getPositions()[0];
		pointCPrime[1] += noeuds[0].getPositions()[1];
		return pointCPrime;
    }
    
    private double[] arrowPositions(double[] linePos) {
    	double L = Math.sqrt(Math.pow(linePos[2] - linePos[0],2) + Math.pow(linePos[3] - linePos[1],2));
        double[] vecteurAAPrime = {(linePos[2]-linePos[0]) * LONGUEUR_ARROW / L, (linePos[3] - linePos[1]) * LONGUEUR_ARROW / L};
        double[] pointC = {linePos[2] - vecteurAAPrime[0], linePos[3] - vecteurAAPrime[1]};
        //Translation 0,0
        pointC[0] -= linePos[2];
        pointC[1] -= linePos[3];
        double[] pointCPrime = {Math.cos(ROTATION_ARROW)*pointC[0]+(-Math.sin(ROTATION_ARROW)*pointC[1]),
        		                Math.sin(ROTATION_ARROW)*pointC[0]+Math.cos(ROTATION_ARROW)*pointC[1],
        		                Math.cos(-ROTATION_ARROW)*pointC[0]+(-Math.sin(-ROTATION_ARROW)*pointC[1]),
        		                Math.sin(-ROTATION_ARROW)*pointC[0]+Math.cos(-ROTATION_ARROW)*pointC[1]};
        //Translation position initiale
        pointCPrime[0] += linePos[2];
        pointCPrime[1] += linePos[3];
        pointCPrime[2] += linePos[2];
        pointCPrime[3] += linePos[3];
		return pointCPrime;
	}

	@Override
    public void actualiser() {
		if (noeuds[0] == noeuds[1]) {
			double[] pointC = {noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius()};
    		double arcRadius = noeuds[0].getRadius() / 2.0;
    		arc.setCenterX(pointC[0]);
    		arc.setCenterY(pointC[1]);
    		arc.setRadiusX(arcRadius);
    		arc.setRadiusY(arcRadius);
		} else {
			double[] linePos = lineDrawingPositions();
	        line.setStartX(linePos[0]);
	        line.setStartY(linePos[1]);
	        line.setEndX(linePos[2]);
	        line.setEndY(linePos[3]);
		}
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
        double[] vecteurAAPrime2 = {(posNoeud2[0]-posNoeud1[0]) * noeuds[1].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[1].getRadius() / L};
        double[] result = {posNoeud1[0] + vecteurAAPrime[0], posNoeud1[1] + vecteurAAPrime[1], posNoeud2[0] - vecteurAAPrime2[0], posNoeud2[1] - vecteurAAPrime2[1]};
        return result;
    }

	@Override
	public Line getLine() {
		return line;
	}

}
