package tools;

import app.Lien;
import app.LienNonOriente;
import app.LienOriente;
import app.Noeud;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;

public class clickDetection {

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
    public static boolean isLinkClicked(double mouseX, double mouseY, Lien lien, double precision) {
    	Line lienAVerif;
    	if (lien instanceof LienNonOriente) {
    		LienNonOriente lienM = (LienNonOriente) lien;
    		lienAVerif = lienM.getLine();
    	} else {
    		LienOriente lienM = (LienOriente) lien;
    		lienAVerif = lienM.getLine();
    	}
		if (lienAVerif == null) {
    		return false;
    	}
        double[] linePos = {lienAVerif.getStartX(), lienAVerif.getStartY(), lienAVerif.getEndX(), lienAVerif.getEndY()};
        // Distance entre les points
        double distN1N2 = distance(linePos[0], linePos[1], linePos[2], linePos[3]);
        double distN1L = distance(linePos[0], linePos[1], mouseX, mouseY);
        double distN2L = distance(linePos[2], linePos[3], mouseX, mouseY);
        // Degré de précision tolérer (MIN / MAX)
        double floor = (distN1L + distN2L) - precision;
        double ceil = (distN1L + distN2L) + precision;
        return floor < distN1N2 && distN1N2 < ceil;
    }
    
    public static boolean isArcClicked(double mouseX, double mouseY, Lien lien) {
    	LienOriente lienO = (LienOriente) lien;
    	Arc arc = lienO.getArc();
    	return arc != null && mouseX > arc.getCenterX() - arc.getRadiusX() && mouseX < arc.getCenterX() + arc.getRadiusX()
        	   && mouseY > arc.getCenterY() - arc.getRadiusX() && mouseY < arc.getCenterY() + arc.getRadiusX();
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
