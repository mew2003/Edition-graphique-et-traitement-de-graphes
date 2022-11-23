/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication7.source;

/**
 *
 * @author mewen.derruau
 */
public class Enveloppe {
    
    double x;
    double y;
    int w;
    int h;
    
    public Enveloppe(double x, double y) {
        this.x = x;
        this.y = y;
        this.w = 0;
        this.h = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
    
}
