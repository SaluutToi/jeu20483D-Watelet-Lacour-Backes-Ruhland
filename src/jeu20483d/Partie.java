package jeu20483d;

import java.io.Serializable;

/**
 *
 * @author chloe
 */
public class Partie implements Serializable {
    private Joueur joueur1;
    private Grille3D grille1;
    private int score1;
    
    //Constructeur
    public Partie(Joueur j1, Grille3D g1){
        this.joueur1 = j1;
        this.grille1 = g1;
        this.score1 = g1.getScore();
    }
    
    //Getters
    public Joueur getJoueur1(){
        return this.joueur1;
    }
    
    public Grille3D getGrille1(){
        return this.grille1;
    }
    
    public int getScore1(){
        return this.score1;
    }
}
