/**
 * Représentation d'un graphe simple non orienté
 */
package app;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import static tools.clickDetection.*;

/**
 * Pour rappel, un graphe non orienté doit respecter les principes suivants :
 * - Il peut y avoir N nombre de noeuds
 * - Il peut y avoir entre 0 et L = N(N+1)/2 nombre de liens
 * - Il ne peut pas y avoir de boucle (un lien qui part d'un noeud et revient à ce même noeud)
 * - Un noeud ne peut pas avoir plus de N-1 lien
 * - Deux liens ne peuvent partir du même noeud et aller vers un autre même noeud
 * @author mewen.derruau
 */
public class GrapheNonOriente extends Graphe {
	
    private ArrayList<Noeud> listeNoeuds = new ArrayList<>();
    private ArrayList<Lien> listeLiens = new ArrayList<>();
    // Nombre de noeud/lien qui ont été crée depuis le lancement de l'application
    private int nbNoeud = 1;
    private int nbLien = 1;

    GrapheNonOriente() {}

    @Override
    public Noeud creerNoeud(double[] pos) {
    	Noeud n = new NoeudXOROriente(pos, nbNoeud++);
    	listeNoeuds.add(n);
        return n;
    } 

    @Override
    public Lien creerLien(Noeud noeud1, Noeud noeud2) {
    	for (Lien l : listeLiens) {
    		if (l.getNoeuds()[0] == noeud1 && l.getNoeuds()[1] == noeud2
    		   || l.getNoeuds()[0] == noeud2 && l.getNoeuds()[1] == noeud1) {
    			throw new IllegalArgumentException("Deux liens ne peuvent pas avoir en commun les mêmes noeuds");
    		}
    	}
        Noeud[] noeuds = {noeud1, noeud2};
    	Lien l = new LienNonOriente(noeuds, nbLien++);
        listeLiens.add(l);
        return l;
    }
    
    @Override
    public Lien supprimerLien(Lien lienASuppr, AnchorPane zoneDessin) {
    	int i = 0;
    	for (Lien l : listeLiens) {
    		if (l == lienASuppr) {
    			lienASuppr.effacer(zoneDessin);
    			listeLiens.remove(i);
    		}
    		i++;
    	}
    	
    	return null;
    }
    
    @Override
    public String toString() {
    	String chaine = "GrapheNonOriente, noeuds [";
    	for (Noeud i : listeNoeuds) {
    		chaine += i.toString() + ", ";
    	}
    	chaine += "], liens [";
    	for (Lien i : listeLiens) {
    		chaine += i.toString() + ", ";
    	}
    	chaine += "]";
        return chaine;
    }

    @Override
    public Object elementClicked(double[] positions, AnchorPane zoneDessin) {
        ObservableList<Node> childrens = zoneDessin.getChildren();
        for (Node n : childrens) {
            if (n instanceof Circle) {
                for (Noeud no : listeNoeuds) {
                    if (isNodeClicked(positions[0], positions[1], no)) {  
                    	reset();
                        return no;
                    }
                }
            } else if (n instanceof Line) {
                for (Lien li : listeLiens) {
                    if (isLinkClicked(positions[0], positions[1], li, ((Line) n).getStrokeWidth() / 10)) {
                        LienNonOriente lienN = (LienNonOriente) li;
                        reset();
                        lienN.getLine().setStrokeWidth(3.0);
                    	return li;
                    }
                }
            }
        }
        reset();
        return null;
    }
    
    @Override
    public void reset() {
    	for (Noeud n : listeNoeuds) {
    		n.getCircle().setStrokeWidth(1.0);
    	}
    	for (Lien l : listeLiens) {
    		LienNonOriente li = (LienNonOriente) l;
    		li.getLine().setStrokeWidth(1.0);
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
		if (noeuds[0] == noeuds[1]) throw new IllegalArgumentException("Impossible de créer une boucle pour un lien simple non orienté");
		for (Lien l : listeLiens) {
    		if (l.getNoeuds()[0] == noeuds[0] && l.getNoeuds()[1] == noeuds[1]
    		   || l.getNoeuds()[0] == noeuds[1] && l.getNoeuds()[1] == noeuds[0]) {
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
}
