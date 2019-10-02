package jeu20483d;

import java.util.HashSet;

/**
 *
 * @author chloe
 */

public class Case implements Parametre {
    
    private int x, y, v;
    private HashSet<Case> grille;
    
    //Constructeur
    public  Case(int abscisse, int ordonne, int valeur){
        this.x = abscisse;
        this.y = ordonne;
        this.v = valeur;
    }

    //Getters
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public int getV(){
        return this.v;
    }
    
    public HashSet getGrille(){
        return this.grille;
    }
    
    //Setters
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }

    public void setV(int valeur) {
        this.v = valeur;
    }
    
    public void setGrille(HashSet g) {
        this.grille = g;
    }
    
    //Affichage
    @Override
    public String toString() {
        return "Case(" + this.x + "," + this.y + "," + this.v + ")";
    }
    
    //Méthodes
    @Override
    public boolean equals(Object obj) { 
        if (obj instanceof Case) {
            Case c = (Case) obj;
            return (this.x == c.x && this.y == c.y);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.x * 7 + this.y * 13;
    }

    public boolean valeursEgales(Case c) {
        if (c != null) {
            return this.v == c.v;
        } else {
            return false;
        }
    }
    
    public Case getVoisinDirect(int direction){
        Case c = new Case(0,0,0);
        return c;
    }
    
    public Case getVoisinGrille(HashSet g){
        Case c = new Case(0,0,0);
        return c;
    }
}
