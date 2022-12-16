package temp;

/**
 * Lien non orienté
 * Pour rappel, un lien non orienté doit respecter les principes suivants :
 * - Il est représenter par un ligne allant d'un noeud à un autre
 * - Un lien ne peut pas partir d'un noeud pour aller vers ce même noeud
 * @author Mewen
 */
public class LienNonOriente extends Lien {

    // Noeud que relie le lien
    private Noeud[] noeuds;

    /**
     * Crée un lien reliant 2 noeuds
     * @param noeuds les noeuds à relié
     */
    public LienNonOriente(Noeud[] noeuds) {
        this.noeuds = noeuds;
    }
    
    @Override
    public Noeud[] getNoeuds() {
        return noeuds;
    }

    @Override
    public void setNoeuds(Noeud[] value) {
        this.noeuds = value;
    }

    @Override
    public String toString() {
        return "Lien : {" + noeuds[0] + " | " + noeuds[1] + "}";
    }
}
