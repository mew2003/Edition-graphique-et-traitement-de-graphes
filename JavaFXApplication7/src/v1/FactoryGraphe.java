package v1;

import java.util.Set;

/**
 * Classe parent des factories de graphes
 * @author Mewen
 */
public abstract class FactoryGraphe {
    
    /**
     * Crée un noeud.
     * @param type chaîne de caractère correspondant au type de noeud souhaité
     * @param nom nom du noeud
     * @param pos position X,Y du noeud
     * @param radius radius du noeud
     * @return le noeud crée
     */
    public abstract Noeud creerNoeud(String type, String nom, double[] pos, double radius);

    /**
     * Crée un lien.
     * @param type chaîne de caractère correspondant au type de lien souhaité
     * @param noeuds Liste des noeuds à relier entre eux (2 noeuds exigés)
     * @return le lien crée
     */
    public abstract Lien creerLien(String type, Noeud[] noeuds);
    
    /**
     * Renvoie la liste de tous les types de noeuds existants
     * @return liste de tous les types de noeuds
     */
    public abstract Set<String> getTypesNoeuds();
    
    /**
     * Renvoie la liste de tous les types de liens existants
     * @return liste de tous les types de liens
     */
    public abstract Set<String> getTypesLiens();

}
