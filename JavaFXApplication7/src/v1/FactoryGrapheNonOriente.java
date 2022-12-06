package v1;

import java.util.ArrayList;
import java.util.List;

public class FactoryGrapheNonOriente extends FactoryGraphe {
    
    private static List<FactoryGrapheNonOriente> instance = new ArrayList<FactoryGrapheNonOriente> ();

    public static FactoryGrapheNonOriente getInstance() {
    }

    public Noeud creerNoeud(String nom, double posX, double posY, double radius) {
    }

    public Lien creerLien(List<Noeud> noeuds) {
    }

}
