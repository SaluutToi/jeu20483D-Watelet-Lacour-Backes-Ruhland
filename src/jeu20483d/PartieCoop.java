package jeu20483d;

/**
 *
 * @author chloe
 */
public class PartieCoop extends Partie {
    private Joueur joueur2;
    
    //Constructeur
    public PartieCoop(Joueur j1, Grille3D g1, Joueur j2) {
        super(j1, g1);
        this.joueur2 = j2;
    }
    
    //Getters
    public Joueur getJoueur2(){
        return this.joueur2;
    }
    
    //Partie sert uniquement à enregistrer dans la bdd donc pas besoin de setters?
}
