/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication7.source;

/**
 *
 * @author mewen.derruau
 */
public class main {
    public static void main(String[] args) {
        GrapheSimpleNonOriente graphe = new GrapheSimpleNonOriente();
        graphe.ajouterNoeud("rond", "Noeud1",0, 0);
        graphe.ajouterNoeud("rectangle", "Noeud2", 0, 0);
        System.out.println(graphe);
    }
}
