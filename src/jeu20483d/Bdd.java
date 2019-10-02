package jeu20483d;

/**
 *
 * @author chloe
 */
public class Bdd {
    private String adresse;
    
    //Constructeur 
    public Bdd (String a){
        this.adresse = a;
    }
    
    //Getter
    public String getAdresse(){
        return this.adresse;
    }
    
    //Setter
    public void setAdresse(String a){
        this.adresse = a;
    }
    
    //Méthodes
    public boolean ouvrir(){
        return true;
    }
    
    public boolean fermer(){
        return true;
    }
    
    public boolean supprimer(Joueur j){
        return true;
    }
    
    public boolean supprimer(Partie p){
        return true;
    }
    public boolean ajouter(Joueur j){
        return true;
    }
    
    public boolean ajouter(Partie p){
        return true;
    }
    
    public boolean chercher(Joueur j){
        return true;
    }
}
