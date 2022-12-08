package v1;

public class LienNonOriente extends Lien {

    private Noeud[] noeuds;

    public LienNonOriente(Noeud[] noeuds) {
        this.noeuds = noeuds;
    }
    
    public LienNonOriente() {}
    
    @Override
    public Noeud[] getNoeuds() {
        return noeuds;
    }

    @Override
    public void setNoeuds(Noeud[] value) {
        this.noeuds = value;
    }

    @Override
    public void dessinerLien() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Lien creerLien(Noeud[] noeuds) {
        return new LienNonOriente(noeuds);
    }

    @Override
    public String toString() {
        return "Lien : {" + noeuds[0] + " | " + noeuds[1] + "}";
    }
}
