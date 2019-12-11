package jeu20483d;

import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author chloe
 */

public class Case implements Parametre, Serializable {
    
    private int x, y, v;
    private HashSet<Case> grille;
    private Grille3D grille3D;
    
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
    
    public HashSet<Case> getGrille(){
        return this.grille;
    }
    
    public Grille3D getGrilles3D(){
        return this.grille3D;
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
    
    public void setGrille(HashSet<Case> g) {
        this.grille = g;
    }
    
    public void setGrilles3D(Grille3D g){
        this.grille3D = g;
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
        switch (direction) {
            case HAUT:
                for (int i = this.y - 1; i >= 0; i--) {
                    for (Case c : grille) {
                        if (c.getX() == this.x && c.getY() == i) {
                            return c;
                        }
                    }
                }   break;
            case BAS:
                for (int i = this.y + 1; i < TAILLE; i++) {
                    for (Case c : grille) {
                        if (c.getX() == this.x && c.getY() == i) {
                            return c;
                        }
                    }
                }   break;
            case GAUCHE:
                for (int i = this.x - 1; i >= 0; i--) {
                    for (Case c : grille) {
                        if (c.getX() == i && c.getY() == this.y) {
                            return c;
                        }
                    }
                }   break;
            case DROITE:
                for (int i = this.x + 1; i < TAILLE; i++) {
                    for (Case c : grille) {
                        if (c.getX() == i && c.getY() == this.y) {
                            return c;
                        }
                    }
                }   break;
            default:
                break;
        }
        return null;
    }
    
    public Case getVoisinGrille(int direction){
        if(direction == SOMMET){
           for(Case c: grille3D.getGrilleSommet()){
                if((c.x == this.x)&&(c.y == this.y)){
                    return c;
                }
            } 
        } else if (direction == BASE){
            for(Case c: grille3D.getGrilleBase()){
                if((c.x == this.x)&&(c.y == this.y)){
                    return c;
                }
            }
        }
        return null;
    }
}
