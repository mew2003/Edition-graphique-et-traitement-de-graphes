import java.awt.Graphics;

/* Enveloppe rectangulaire qui sert à créer des noeuds et des liens */
public class Enveloppe {
    /* position x de l'enveloppe */
    public int x;
    /* position y de l'enveloppe */
    public int y;
    /* longueur de l'enveloppe */
    public int w;
    /* hauteur de l'enveloppe */
    public int h;
    public void dessiner(Graphics g) {
        g.drawRect(this.x, this.y, this.w, this.h);
    }
    public void effacer(Graphics g) {
        g.clearRect(this.x, this.y, this.w, this.h);
    }
}