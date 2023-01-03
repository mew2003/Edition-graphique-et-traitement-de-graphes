package app;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Arc;

public class GrapheOriente extends Graphe {
	
	private ArrayList<Noeud> listeNoeuds = new ArrayList<>();
    private ArrayList<Lien> listeLiens = new ArrayList<>();
    // Nombre de noeud/lien qui ont été crée depuis le lancement de l'application
    private int nbNoeud = 1;
    private int nbLien = 1;
    
    GrapheOriente() {}

	@Override
	public Noeud creerNoeud(double[] pos) {
		Noeud n = new NoeudXOROriente(pos, nbNoeud++);
    	listeNoeuds.add(n);
        return n;
	}

	@Override
	public Lien creerLien(Noeud noeud1, Noeud noeud2) {
		for (Lien l : listeLiens) {
    		if (l.getNoeuds()[0] == noeud1 && l.getNoeuds()[1] == noeud2) {
    			throw new IllegalArgumentException("Deux liens ne peuvent pas avoir en commun les mêmes noeuds dans le même ordre");
    		}
    	}
        Noeud[] noeuds = {noeud1, noeud2};
    	Lien l = new LienOriente(noeuds, nbLien++);
        listeLiens.add(l);
        return l;
	}

	@Override
	public Object elementClicked(double[] positions, AnchorPane zoneDessin) {
		ObservableList<Node> childrens = zoneDessin.getChildren();
        for (Node n : childrens) {
            if (n instanceof Circle) {
                for (Noeud no : listeNoeuds) {
                    if (isNodeClicked(positions[0], positions[1], no)) {                      
                        return no;
                    }
                }
            } else if (n instanceof Line) {
                for (Lien li : listeLiens) {
                    if (isLinkClicked(positions[0], positions[1], li, ((Line) n).getStrokeWidth() / 10)) {
                        return li;
                    }
                }
            } else if (n instanceof Arc) {
            	for (Lien li : listeLiens) {
                    if (isArcClicked(positions[0], positions[1], li)) {
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
    public boolean isLinkClicked(double mouseX, double mouseY, Lien lien, double precision) {
    	Line lienAVerif = lien.getLine();
    	if (lienAVerif == null) {
    		return false; //Stub TODO: /!\ Attention, si le lien est une boucle, sa 'ligne' n'existe pas (c'est un arc)
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
    
    public boolean isArcClicked(double mouseX, double mouseY, Lien lien) {
    	LienOriente lienO = (LienOriente) lien;
    	Arc arc = lienO.getArc();
    	return mouseX > arc.getCenterX() - arc.getRadiusX() && mouseX < arc.getCenterX() + arc.getRadiusX()
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
    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

	@Override
	public Noeud getNode(String libelle) {
		for (Noeud n : listeNoeuds) {
            if (n.getNom().equals(libelle)) {
                return n;
            }
        }
        throw new IllegalArgumentException("Le noeud n'existe pas");
	}

	@Override
	public void modifNomNoeud(Noeud noeud, String nouveauNom) {
		for (Noeud n : listeNoeuds) {
    		if (nouveauNom.equals(n.getNom())) {
    			return;
    		}
    	}
    	noeud.setNom(nouveauNom);
	}

	@Override
	public void modifPos(Noeud noeud, double[] pos) {
		for (Lien l : listeLiens) {
			if (l.getNoeuds()[0] == noeud || l.getNoeuds()[1] == noeud) {
				noeud.setPositions(pos);
				l.actualiser();
			}
		}
	}

	@Override
	public void modifLien(Lien lien, Noeud[] noeuds, AnchorPane zoneDessin) {
		lien.setNoeuds(noeuds, zoneDessin);
	}

	@Override
	public void modifRadius(Noeud noeud, double radius) {
		noeud.setRadius(radius);
		for (Lien l : listeLiens) {
			if (l.getNoeuds()[0] == noeud || l.getNoeuds()[1] == noeud) {
				l.actualiser();
			}
		}
	}

}
