package jeu20483d;

/**
 *
 * @author chloe
 * @version 1.0
 */
public class PartieCompet extends Partie {
    private Joueur joueur2;
    private Grille3D grille2;
    private int score2;

    /**
     * Constructeur
     * @param j1
     * @param g1
     */
    public PartieCompet(Joueur j1, Grille3D g1) {
        super(j1, g1);
    }
    
    //Getters
    public Joueur getJoueur2(){
        return this.joueur2;
    }
    
    public Grille3D getGrille2(){
        return this.grille2;
    }
    
    public int getScore2(){
        return this.score2;
    }
}
