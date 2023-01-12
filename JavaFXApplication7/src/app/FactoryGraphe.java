/*
 * Classe parente des factories de graphes
 */
package app;

/**
 * Classe parente des factories de graphes
 * @author Mewen
 */
public abstract class FactoryGraphe {
    
	/**
	 * Créer un graphe
	 * @return l'object graphe correspondant
	 */
    public abstract Graphe creerGraphe();

}
