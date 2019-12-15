package jeu20483d;

import java.io.Serializable;

/**
 *
 * @author chloe
 */
public class Joueur implements Serializable {
    private String pseudo;
    private transient String mdp;
    private int meilleurScore, partiesGagnees;
    
    //Constructeur
    public Joueur(String p, String m){
        this.pseudo = p;
        this.mdp = m;
        this.meilleurScore = 0;
        this.partiesGagnees = 0;
    }
    
    //Getters
    public String getPseudo(){
        return this.pseudo;
    }
    
    public String getMDP(){
        return this.mdp;
    }
    
    public int getMeilleurScore(){
        return this.meilleurScore;
    }
    
    public int getPartiesGagnees(){
        //idée: nombre parties gagnées en solo, en coop ou en compet
        return this.partiesGagnees;
    }
    
    //Setters
    public void setPseudo(String p){
        this.pseudo = p;
        //enregistrement modif dans bdd
    }
    
    public void setMDP(String m){
        this.mdp = m;
        //enregistrement modif dans bdd
    }
    
    public void setMeilleurScore(int s){
        this.meilleurScore = s;
        //enregistrement modif dans bdd
    }
    
    public void getPartiesGagnees(int g){
        //idée: nombre parties gagnées en solo, en coop ou en compet
        this.partiesGagnees = g;
        //enregistrement modif dans bdd
    }
    
    //Affichage -> "Chloé, vous avez gagné 0 parties et votre meilleur score est de 0." ou juste "Chloé" ?
}
