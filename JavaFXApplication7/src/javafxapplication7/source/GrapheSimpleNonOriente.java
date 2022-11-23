/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication7.source;

import java.util.ArrayList;

/**
 *
 * @author mewen.derruau
 */
public class GrapheSimpleNonOriente {
    
    ArrayList<Noeud> listeNoeuds;
    ArrayList<Lien> listeLiens;
    
    public GrapheSimpleNonOriente() {
        listeNoeuds = new ArrayList<>();
        listeLiens = new ArrayList<>();
    }
    
    public boolean ajouterNoeud(String type, String nom , double x, double y) {
        Noeud noeud;
        try {
            noeud = new NoeudRond(type, nom);
            listeNoeuds.add(noeud);
            noeud.creerNoeud(new Enveloppe(x, y));
            return true;
        } catch (Exception e) {}
        try {
            noeud = new NoeudRectangle(type, nom);
            listeNoeuds.add(noeud);
            noeud.creerNoeud(new Enveloppe(x, y));
            return true;
        } catch (Exception e) {}
        return false;
    }
    
    @Override
    public String toString() {
        String chaine = "";
        for (Noeud noeud : listeNoeuds) {
            chaine += noeud.toString() + "\n";
        }
        
        for (Lien lien : listeLiens) {
            chaine += lien.toString();
        }
        return chaine;
    }
}
