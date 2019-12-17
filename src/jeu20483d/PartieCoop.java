package jeu20483d;

/**
 *
 * @author chloe
 */
public class PartieCoop extends Partie {
    private Joueur joueur2;
    
    //Constructeur

    /**
     * Constructeur PartieCoop
     * @param j1
     * @param g1
     * @param j2
     */
    public PartieCoop(Joueur j1, Grille3D g1, Joueur j2) {
        super(j1, g1);
        this.joueur2 = j2;
    }
    
    //Getters
    public Joueur getJoueur2(){
        return this.joueur2;
    }
}
