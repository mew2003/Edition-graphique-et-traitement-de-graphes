/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

/**
 *
 * @author mewen.derruau
 */
abstract class Noeud {

	public abstract double[] getPositions();

	public abstract double getRadius();
	
	public abstract String getNom();
	
    public abstract void setNom(String value);

    public abstract void setPositions(double[] positions);

    public abstract void setRadius(double radius);
    
}
