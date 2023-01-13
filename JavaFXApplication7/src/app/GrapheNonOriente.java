/**
 * Représentation d'un graphe simple non orienté
 */
package app;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
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
    
    // Nombre de noeuds qui ont été crée depuis le lancement de l'application
    private int nbNoeud = 0;

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
    		if (l.getNoeuds()[0] == noeud1 && l.getNoeuds()[1] == noeud2
    		   || l.getNoeuds()[0] == noeud2 && l.getNoeuds()[1] == noeud1) {
    			throw new IllegalArgumentException("Deux liens ne peuvent pas avoir en commun les mêmes noeuds");
    		}
    	}
        Noeud[] noeuds = {noeud1, noeud2};
    	Lien l = new LienNonOriente(noeuds);
        listeLiens.add(l);
        return l;
    }
    
    @Override
    public void supprimerLien(Lien lienASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements) {
		lienASuppr.effacer(zoneDessin);
		listeElements.getItems().remove(lienASuppr);
		listeLiens.remove(lienASuppr);
    }
    
    @Override
    public void supprimerNoeud(Noeud noeudASuppr, AnchorPane zoneDessin, ComboBox<Object> listeElements) {
		noeudASuppr.effacer(zoneDessin);
		listeNoeuds.remove(noeudASuppr);

		for (int j = 0; j < listeLiens.size(); j++) {
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
    public String toString() {
        StringBuilder chaine = new StringBuilder("GrapheNonOriente, noeuds [");
        for (Noeud i : listeNoeuds) {
            chaine.append(i.toString()).append(", ");
        }
        chaine.append("], liens [");
        for (Lien i : listeLiens) {
            chaine.append(i.toString()).append(", ");
        }
        chaine.append("]");
        return chaine.toString();
    }

    @Override
    public Object elementClicked(double[] pos, AnchorPane zoneDessin) {
        ObservableList<Node> childrens = zoneDessin.getChildren();
        for (Node n : childrens) {
            if (n instanceof Circle) {
                for (Noeud no : listeNoeuds) {
                    if (isNodeClicked(pos[0], pos[1], no)) return no;
                }
            } else if (n instanceof Line) {
                for (Lien li : listeLiens) {
                    if (isLinkClicked(pos[0], pos[1], li)) return li;
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
    		((LienNonOriente) l).getLine().setStrokeWidth(1.0);
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
		noeud.setPositions(pos);
		for (Lien l : listeLiens) {
			if (l.getNoeuds()[0] == noeud || l.getNoeuds()[1] == noeud) {
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

	@Override
	public void setEtat(Graphe graphe) {
		if(graphe != null && graphe instanceof GrapheNonOriente) {
			this.listeLiens = ((GrapheNonOriente) graphe).listeLiens;
			this.listeNoeuds = ((GrapheNonOriente) graphe).listeNoeuds;
			this.nbNoeud = ((GrapheNonOriente) graphe).nbNoeud;
		}		
	}

	@Override
	public Graphe clone() {
		GrapheNonOriente clone = new GrapheNonOriente();
	    clone.listeLiens = new ArrayList<>();
	    for (Lien lien : this.listeLiens) {
	        clone.listeLiens.add(lien.clone());
	    }
	    clone.listeNoeuds = new ArrayList<>();
	    for (Noeud noeud : this.listeNoeuds) {
	        clone.listeNoeuds.add(noeud.clone());
	    }
		clone.nbNoeud = this.nbNoeud;
		return clone;
	}

	@Override
	public List<Noeud> getListeNoeuds() {
		return this.listeNoeuds;
	}

	@Override
	public List<Lien> getListeLiens() {
		return this.listeLiens;
	}
}
