package jeu20483d;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author chloe
 */

public class Grille3D implements Parametre {
    
    private HashSet<Case> grilleBase, grilleMilieu, grilleSommet;
    private int score, meilleureCase;
    private boolean deplacement;
    
    //Constructeur
    public Grille3D(){
        this.grilleBase = new HashSet <>();
        this.grilleMilieu = new HashSet<>();
        this.grilleSommet = new HashSet<>();
        this.score = 0;
        this.meilleureCase = 0;
    }
    
    //Getters
    public HashSet getGrilleBase(){
        return this.grilleBase;
    }
    
    public HashSet getGrilleMilieu(){
        return this.grilleMilieu;
    }
    
    public HashSet getGrilleSommet(){
        return this.grilleSommet;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public int getMeilleureCase(){
        return this.meilleureCase;
    }
    
    //Setters
    public void setGrilleBase(HashSet <Case> gB){
        this.grilleBase = gB;
    }
    
    public void setGrilleMilieu(HashSet <Case> gM){
        this.grilleMilieu = gM;
    }
    
    public void setGrilleSommet(HashSet <Case> gS){
        this.grilleSommet = gS;
    }
    
    //Affichage
    @Override
    public String toString() {
        int[][] tableau = new int[TAILLE][TAILLE];
        for (Case c : this.grilleBase) {
            tableau[c.getY()][c.getX()] = c.getV();
        }
        String result = "";
        for (int i = 0; i < tableau.length; i++) {
            result += Arrays.toString(tableau[i]) + "\n";
        }
        result = result + "\n";
        for (Case c : this.grilleMilieu) {
            tableau[c.getY()][c.getX()] = c.getV();
        }
        for (int i = 0; i < tableau.length; i++) {
            result += Arrays.toString(tableau[i]) + "\n";
        }
        result = result + "\n";
        for (Case c : this.grilleSommet) {
            tableau[c.getY()][c.getX()] = c.getV();
        }
        for (int i = 0; i < tableau.length; i++) {
            result += Arrays.toString(tableau[i]) + "\n";
        }
        return result;
    }
    
    
    //Méthodes
    public Case[] CaseExtreme(int direction, HashSet <Case> g){
        //cf getCaseExtremites() 2048-L2
        Case[] c = new Case[10];
        return c;
    }
    
    public void deplacer(int direction, HashSet <Case> g){
        //déplace les cases au sein d'une même grille
        //utilise CaseExtreme()
        //cf déplacerCaseRecursifs() 2048-L2
    }
    
    public void deplacerEntreGrille(int direction){
        //déplace les cases à travers les grilles en fonction de la direction
    }
    
    public void lanceDeplacement(int direction){
        //lance les déplacement suivant la direction entrée
        //utilise deplacer() et deplacerEntreGrille() suivant la direction entrée
    }
    
    public boolean jeuPerdu(){
        return false;
    }
    
    public boolean jeuGagne(){
        return true;
    }
    
    public void calculScore(){
        //cf méthode fusion 2048-L2
        //utilisé à chaque deplacment dès que deux cases sont fusionnées
    }
    
    public void AjoutCase(){
        //ajoute aléatoirement une case sur l'une des trois grilles
    }
}   
