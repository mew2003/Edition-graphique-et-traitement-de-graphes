/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

import javafx.scene.layout.AnchorPane;

/**
 *
 * @author mewen.derruau
 */
public abstract class Lien {

    public abstract Noeud[] getNoeuds();

    public abstract void setNoeuds(Noeud[] value);
    
    public abstract void dessiner(AnchorPane zoneDessin);
    
}
