package app;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;

import static tools.clickDetection.*;

public class GrapheProbabiliste extends Graphe {

	private ArrayList<Noeud> listeNoeuds = new ArrayList<>();
    private ArrayList<Lien> listeLiens = new ArrayList<>();
    // Nombre de noeud/lien qui ont été crée depuis le lancement de l'application
    private int nbNoeud = 1;
    private int nbLien = 1;
	
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
    	Lien l = new LienProbabiliste(noeuds, nbLien++);
        listeLiens.add(l);
        return l;
	}
    
    @Override
    public void supprimerLien(Lien lienASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements) {
    	for (int i = 0 ; i < listeLiens.size() ; i++)  {
    		if (listeLiens.get(i) == lienASuppr) {
    			lienASuppr.effacer(zoneDessin);
    			listeLiens.remove(lienASuppr);
    			listeElements.getItems().remove(lienASuppr);
    			i--;
    		}
    	}
    }
    
    @Override
    public void supprimerNoeud(Noeud noeudASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements) {
    	
    	for (int i = 0 ; i < listeNoeuds.size() ; i++)  {
    		if (listeNoeuds.get(i) == noeudASuppr) {
    			noeudASuppr.effacer(zoneDessin);
    			listeNoeuds.remove(noeudASuppr);
    			i--;
    			//TODO modifier le remove de la liste d'éléments pour le passer dans 'supprimerLien'
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
		}
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
            } else if (n instanceof Arc) {
            	for (Lien li : listeLiens) {
                    if (isArcClicked(positions[0], positions[1], li)) {
                        return li;
                    }
                }
            } else if (n instanceof QuadCurve) {
            	for (Lien li : listeLiens) {
                    if (isQuadCurvedClicked(positions[0], positions[1], li)) {
                        return li;
                    }
                }
            }
        }
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
			    && l.getNoeuds()[1] == noeuds[1]
			    && l != lien) {
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
    		LienProbabiliste li = (LienProbabiliste) l;
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
	
	public void modifValeur(Lien lien, double newValue) {
		LienProbabiliste l = (LienProbabiliste) lien;
		Noeud noeudAVerif = lien.getNoeuds()[0];
		double ActualLeaving = 0.0;
		for (Lien li : listeLiens) {
			if (li.getNoeuds()[0] == noeudAVerif && lien != li) {
				LienProbabiliste lie = (LienProbabiliste) li;
				ActualLeaving += lie.getValue();
			}
		}
		ActualLeaving += newValue;
		if (ActualLeaving > 1.0) {
			throw new IllegalArgumentException("La somme des valeurs partant d'un noeud ne peut pas être supérieur à 1.0");
		}
		l.setValue(newValue);
	}
	
	public ArrayList<Noeud> getListeNoeuds() {
		return listeNoeuds;
	}

	public ArrayList<Lien> getListeLiens() {
		return listeLiens;
	}

	@Override
	public void setEtat(Graphe graphe) {
		if(graphe != null && graphe instanceof GrapheProbabiliste) {
			this.listeLiens = ((GrapheProbabiliste) graphe).listeLiens;
			this.listeNoeuds = ((GrapheProbabiliste) graphe).listeNoeuds;
			this.nbLien = ((GrapheProbabiliste) graphe).nbLien;
			this.nbNoeud = ((GrapheProbabiliste) graphe).nbNoeud;
		}		
	}

	@Override
	public Graphe clone() {
		GrapheProbabiliste clone = new GrapheProbabiliste();
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
