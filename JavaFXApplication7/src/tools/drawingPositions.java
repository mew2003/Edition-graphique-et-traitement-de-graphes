package tools;

import app.Noeud;

public class drawingPositions {
	
    private final static double BOUCLE_DEPART = 0.52;  
    
    private final static double LONGUEUR_ARROW = 10.0;
    
    private final static double ROTATION_ARROW = 0.52;

	public static double[] lineDrawingPositions(Noeud[] noeuds) {
    	double[] posNoeud1 = noeuds[0].getPositions();
        double[] posNoeud2 = noeuds[1].getPositions();
        double L = Math.sqrt(Math.pow(posNoeud2[0] - posNoeud1[0],2) + Math.pow(posNoeud2[1] - posNoeud1[1],2));
        double[] vecteurAAPrime = {(posNoeud2[0]-posNoeud1[0]) * noeuds[0].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[0].getRadius() / L};
        double[] vecteurAAPrime2 = {(posNoeud2[0]-posNoeud1[0]) * noeuds[1].getRadius() / L ,(posNoeud2[1] - posNoeud1[1]) * noeuds[1].getRadius() / L};
        double[] result = {posNoeud1[0] + vecteurAAPrime[0], posNoeud1[1] + vecteurAAPrime[1], posNoeud2[0] - vecteurAAPrime2[0], posNoeud2[1] - vecteurAAPrime2[1]};
        return result;
    }
	
	public static double[] departArc(Noeud[] noeuds) {
    	double[] pointC = {noeuds[0].getPositions()[0], noeuds[0].getPositions()[1] - noeuds[0].getRadius()};
		//Translation 0,0
		pointC[0] -= noeuds[0].getPositions()[0];
        pointC[1] -= noeuds[0].getPositions()[1];
		double[] pointCPrime = {Math.cos(BOUCLE_DEPART)*pointC[0]+(-Math.sin(BOUCLE_DEPART)*pointC[1]),
                			    Math.sin(BOUCLE_DEPART)*pointC[0]+Math.cos(BOUCLE_DEPART)*pointC[1]}; 
		//Translation point de départ
		pointCPrime[0] += noeuds[0].getPositions()[0];
		pointCPrime[1] += noeuds[0].getPositions()[1];
		return pointCPrime;
    }
	
	public static double[] arrowPositions(double[] linePos) {
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
}
