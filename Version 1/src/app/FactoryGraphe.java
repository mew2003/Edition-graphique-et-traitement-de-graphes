/*
 * Classe parent des factory de graphe
 */
package app;

/**
 * Classe parent des factories de graphes
 * @author Mewen
 */
public abstract class FactoryGraphe {
    
	/**
	 * Créer un graphe
	 * @return l'object graphe correspondant
	 */
    public abstract Graphe creerGraphe();

}
