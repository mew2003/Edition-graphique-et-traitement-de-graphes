/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication7.source;

/**
 *
 * @author mewen.derruau
 */
public class NoeudRectangle extends Noeud {

    Enveloppe enveloppe;
    String nom;
    
    public NoeudRectangle(String type, String nom) {
        if (!"rectangle".equals(type)) throw new IllegalArgumentException();
        this.nom = nom;
    }

    @Override
    public Noeud creerNoeud(Enveloppe enveloppe) {
        this.enveloppe = enveloppe;
        return null;
    }
    
    @Override
    public String toString() {
        return "Noeud " + nom + " (rectangle) ; pos x : " + enveloppe.getX() + " ; pos y : " + enveloppe.getY() + " ; weight : " + enveloppe.getW() + " ; height : " + enveloppe.getH();
    }
}
