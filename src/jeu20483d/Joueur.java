package jeu20483d;

import java.io.Serializable;

/**
 *
 * @author chloe
 */
public class Joueur implements Serializable {
    private String pseudo, mdp, style, mail;
    private int meilleurScore, partiesGagnees;
    
    //Constructeur
    public Joueur(String p, String mdp, String m){
        this.pseudo = p;
        this.mdp = mdp;
        this.mail = m;
        this.meilleurScore=0;
        this.partiesGagnees=0;
        this.style = "Classique";
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
        return this.partiesGagnees;
    }
    public String getMail(){
        return this.mail;
    }
    public String getStyle(){
        return this.style;
    }
    
    //Setters
    public void setPseudo(String p){
        this.pseudo = p;
    }
    public void setMDP(String m){
        this.mdp = m;
    }
    public void setMeilleurScore(int s){
        this.meilleurScore = s;
    }
    public void setPartiesGagnees(int g){
        this.partiesGagnees = g;
    }
    public void setStyle(String s){
        this.style = s;
    }
    public void setMail(String s){
        this.style = s;
    }
}
