package app;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

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
		//TODO: Bloquer la création de lien dans le cas ou un lien similaire existe déjà, y compris boucle (possible si l'autre lien est dans l'autre sens) 
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
            }
            if (n instanceof Line) {
                for (Lien li : listeLiens) {
                    if (isLinkClicked(positions[0], positions[1], li, ((Line) n).getStrokeWidth() / 10)) {
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
	public void modifLien(Lien lien, Noeud[] noeuds) {
		//TODO: link
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