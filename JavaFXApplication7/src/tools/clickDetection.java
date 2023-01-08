package tools;

import app.Lien;
import app.LienNonOriente;
import app.LienOriente;
import app.LienProbabiliste;
import app.Noeud;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

public class clickDetection {
	
	static double precisionLigne = 1.0;
	static double precisionQuadCurved = 5.0;

	/**
     * Vérifie qu'une position X,Y soit situer à l'emplacement d'un noeud
     * @param mouseX position X de la souris
     * @param mouseY position Y de la souris
     * @param noeud Noeud à vérifier
     * @return true si les positions se trouve en effet à l'emplacement d'un noeud
     *         false dans le cas contraire.
     */
    public static boolean isNodeClicked(double mouseX, double mouseY, Noeud noeud) {
        return mouseX > noeud.getPositions()[0] - noeud.getRadius() && mouseX < noeud.getPositions()[0] + noeud.getRadius()
               && mouseY > noeud.getPositions()[1] - noeud.getRadius() && mouseY < noeud.getPositions()[1] + noeud.getRadius();
    }
    
    /**
     * Vérifie qu'une position X,Y soit situer à l'emplacement d'un lien
     * avec une précision +/- donnée.
     * @param mouseX position X de la souris
     * @param mouseY position Y de la souris
     * @param lien Lien à vérifier
     * @param precision precision +/- pour laquelle la position de la souris
     *                  peut-être tolérée.
     * @return true si les positions se trouve en effet à l'emplacement d'un lien
     *         false dans le cas contraire.
     */
    public static boolean isLinkClicked(double mouseX, double mouseY, Lien lien) {
    	Line lienAVerif;
    	LienNonOriente lienM = (LienNonOriente) lien;
    	lienAVerif = lienM.getLine();

		if (lienAVerif == null) {
    		return false;
    	}
        double[] linePos = {lienAVerif.getStartX(), lienAVerif.getStartY(), lienAVerif.getEndX(), lienAVerif.getEndY()};
        // Distance entre les points
        double distN1N2 = distance(linePos[0], linePos[1], linePos[2], linePos[3]);
        double distN1L = distance(linePos[0], linePos[1], mouseX, mouseY);
        double distN2L = distance(linePos[2], linePos[3], mouseX, mouseY);
        // Degré de précision tolérer (MIN / MAX)
        double floor = (distN1L + distN2L) - precisionLigne;
        double ceil = (distN1L + distN2L) + precisionLigne;
        return floor < distN1N2 && distN1N2 < ceil;
    }
    
    public static boolean isArcClicked(double mouseX, double mouseY, Lien lien) {
    	Arc arc = null;
    	if (lien instanceof LienOriente) {
    		LienOriente lienO = (LienOriente) lien;
    		arc = (Arc) lienO.getArc()[0];
    	} else if (lien instanceof LienProbabiliste) {
    		LienProbabiliste lienP = (LienProbabiliste) lien;
    		arc = (Arc) lienP.getArc()[0];
    	}
		return arc != null && mouseX > arc.getCenterX() - arc.getRadiusX() && mouseX < arc.getCenterX() + arc.getRadiusX()
        	   && mouseY > arc.getCenterY() - arc.getRadiusX() && mouseY < arc.getCenterY() + arc.getRadiusX();
    }
    
    public static boolean isQuadCurvedClicked(double mouseX, double mouseY, Lien lien) {
    	QuadCurve quadCurve;
    	if (lien instanceof LienProbabiliste) {
    		LienProbabiliste lienP = (LienProbabiliste) lien;
    		quadCurve = (QuadCurve) lienP.getQuadCurved()[0];
    	} else {
    		LienOriente lienO = (LienOriente) lien;
    		quadCurve = (QuadCurve) lienO.getQuadCurved()[0];
    	}

		if (quadCurve == null) return false; //Si le lien ne possède pas de quadCurve (si c'est une courbe)
    	double[] mousePos = {mouseX, mouseY};
    	double[] triangleA = {quadCurve.getStartX(), quadCurve.getStartY()};
    	double[] triangleC = {quadCurve.getEndX(), quadCurve.getEndY()};
    	double[] triangleB = {quadCurve.getControlX(), quadCurve.getControlY()};
    	double triangleArea = triangleArea(triangleA, triangleB, triangleC);
    	double PBC = triangleArea(mousePos, triangleB, triangleC);
    	double APC = triangleArea(triangleA, mousePos, triangleC);
    	double ABP = triangleArea(triangleA, triangleB, mousePos);
    	double areaFind = PBC + APC + ABP;
    	double floor = triangleArea - precisionLigne;
        double ceil = triangleArea + precisionLigne;
        return floor < areaFind && areaFind < ceil;	
    }
    
    public static double triangleArea(double[] point1, double[] point2, double[] point3) {
    	return Math.abs((point1[0] * (point2[1] - point3[1]) 
    			+ point2[0] * (point3[1] - point1[1]) 
    			+ point3[0] * (point1[1] - point2[1]))/2);
    }
    
    /**
     * Calcul la distance entre deux points
     * @param x1 position X point 1
     * @param y1 position Y point 1
     * @param x2 position X point 2
     * @param y2 position Y point 2
     * @return la distance entre les deux points
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
