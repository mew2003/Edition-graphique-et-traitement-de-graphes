/**
 * Représentation d'un graphe pondéré
 */
package app;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

import static tools.clickDetection.*;

/**
 * Pour rappel, un graphe orienté pondéré doit respecter les principes suivants :
 * - Il peut y avoir N nombre de noeuds
 * - Il peut y avoir entre 0 et L = N^N nombre de liens
 * - Il ne peut pas y avoir deux liens partant d'un noeud vers un autre même noeud
 * - Un noeud ne peut pas y avoir plus de N lien partant et N lien arrivant vers ce même noeud.
 * - Chaque lien peut comporter une valeur compris entre -∞ et +∞
 * @author mewen.derruau
 */
public class GrapheOrientePondere extends Graphe {

	private ArrayList<Noeud> listeNoeuds = new ArrayList<>();
    private ArrayList<Lien> listeLiens = new ArrayList<>();
    
    // Nombre de noeud/lien qui ont été crée depuis le lancement de l'application
    private int nbNoeud = 0;
    private int nbLien = 1;
	
    @Override
    public Noeud creerNoeud(double[] pos) {
    	nbNoeud++;
    	boolean estValide = true;
    	do {
    		estValide = true;
    		for (Noeud n : listeNoeuds) {
    			if (n.getNom().equals(n.getDEFAULT_NAME() + (nbNoeud))) {
    				estValide = false;
    				nbNoeud++;
    			}
    		}
    	} while (!estValide);
    	Noeud n = new NoeudXOROriente(pos, nbNoeud);
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
    	Lien l = new LienOrientePondere(noeuds, nbLien++);
        listeLiens.add(l);
        return l;
	}
    
    @Override
    public void supprimerLien(Lien lienASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements) {
		lienASuppr.effacer(zoneDessin);
		listeLiens.remove(lienASuppr);
		listeElements.getItems().remove(lienASuppr);
    }
    
    @Override
    public void supprimerNoeud(Noeud noeudASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements) {
		noeudASuppr.effacer(zoneDessin);
		listeNoeuds.remove(noeudASuppr);

		for (int j = 0 ; j < listeLiens.size() ; j++) {
			if (listeLiens.get(j).getNoeuds()[0] == noeudASuppr) {
				supprimerLien(listeLiens.get(j), zoneDessin, listeElements);
				j--;
			} else if (listeLiens.get(j).getNoeuds()[1] == noeudASuppr) {
				supprimerLien(listeLiens.get(j), zoneDessin, listeElements);
				j--;
			}
	    }
    }

	@Override
	public Object elementClicked(double[] positions, AnchorPane zoneDessin) {
		ObservableList<Node> childrens = zoneDessin.getChildren();
        for (Node n : childrens) {
            if (n instanceof Circle) {
                for (Noeud no : listeNoeuds) {
                    if (isNodeClicked(positions[0], positions[1], no)) return no;
                }
            } else if (n instanceof Arc) {
            	for (Lien li : listeLiens) {
                    if (isArcClicked(positions[0], positions[1], li)) return li;
                }
            } else if (n instanceof QuadCurve) {
            	for (Lien li : listeLiens) {
                    if (isQuadCurvedClicked(positions[0], positions[1], li)) return li;
                }
            }
        }
        reset();
        return null;
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
		noeud.setPositions(pos);
		for (Lien l : listeLiens) {
			if (l.getNoeuds()[0] == noeud || l.getNoeuds()[1] == noeud) {
				l.actualiser();
			}
		}
	}

	@Override
	public void modifLien(Lien lien, Noeud[] noeuds, AnchorPane zoneDessin) {
		for (Lien l : listeLiens) {
			if (l.getNoeuds()[0] == noeuds[0] 
			    && l.getNoeuds()[1] == noeuds[1]) {
				throw new IllegalArgumentException("Deux liens ne peuvent pas avoir en commun les mêmes noeuds");
			}
		}
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
	
    @Override
    public void reset() {
    	for (Noeud n : listeNoeuds) {
    		n.getCircle().setStrokeWidth(1.0);
    	}
    	for (Lien l : listeLiens) {
    		LienOrientePondere li = (LienOrientePondere) l;
    		for(Shape shape : li.getArc()) {
    			if(shape != null) {
    				shape.setStrokeWidth(1.0);
    			}
    		}
    		for(Shape line : li.getQuadCurved()) {
    			if(line != null) {
    				line.setStrokeWidth(1.0);
    			}
    		}
    	}
    }

    @Override
    public void relocalisation() {
    	for(Lien l : listeLiens) {
    		for(Noeud n : listeNoeuds) {
    			if(l.getNoeuds()[0].getNom().equals(n.getNom())) {
    				l.actualiser();
    			}
    		}
    	}
    }
	
    /**
     * Remplace l'ancienne valeur d'un lien par la nouvelle saisie en argument
     * @param lien le lien à modifier
     * @param newValue nouvelle valeur du lien
     */
	public void modifValeur(Lien lien, double newValue) {
		((LienOrientePondere) lien).setValue(newValue);
	}
	
	/**
	 * Renvoie la liste des noeuds
	 */
	public ArrayList<Noeud> getListeNoeuds() {
		return listeNoeuds;
	}

	/**
	 * Renvoie la liste des liens
	 */
	public ArrayList<Lien> getListeLiens() {
		return listeLiens;
	}

	@Override
	public void setEtat(Graphe graphe) {
		if(graphe != null && graphe instanceof GrapheOrientePondere) {
			this.listeLiens = ((GrapheOrientePondere) graphe).listeLiens;
			this.listeNoeuds = ((GrapheOrientePondere) graphe).listeNoeuds;
			this.nbLien = ((GrapheOrientePondere) graphe).nbLien;
			this.nbNoeud = ((GrapheOrientePondere) graphe).nbNoeud;
		}		
	}

	@Override
	public Graphe clone() {
		GrapheOrientePondere clone = new GrapheOrientePondere();
	    clone.listeLiens = new ArrayList<>();
	    for (Lien lien : this.listeLiens) {
	      clone.listeLiens.add((Lien) lien.clone());
	    }
	    clone.listeNoeuds = new ArrayList<>();
	    for (Noeud noeud : this.listeNoeuds) {
	      clone.listeNoeuds.add((Noeud) noeud.clone());
	    }
		clone.nbLien = this.nbLien;
		clone.nbNoeud = this.nbNoeud;
		return clone;
	}
	
}
