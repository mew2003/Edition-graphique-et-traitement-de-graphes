import java.awt.Graphics;

public class NoeudRectangle extends Noeud {
    public NoeudRectangle creerNoeud(Enveloppe enveloppe) {
        NoeudRectangle n = new NoeudRectangle();
        n.enveloppe = enveloppe;
        return n;
    }
    @Override
    public void dessiner(Graphics g) {
        g.drawRect(enveloppe.x, enveloppe.y, enveloppe.w, enveloppe.h);
    }
}