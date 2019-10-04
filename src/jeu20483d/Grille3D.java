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
    public HashSet<Case> getGrilleBase(){
        return this.grilleBase;
    }
    
    public HashSet<Case> getGrilleMilieu(){
        return this.grilleMilieu;
    }
    
    public HashSet<Case> getGrilleSommet(){
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
    
    public boolean jeuFini(){
        
        boolean fin = true;
        
        //On vérifie si les trois grilles sont pleine
        if ((this.grilleBase.size() < TAILLE * TAILLE)||(this.grilleMilieu.size() < TAILLE * TAILLE)||(this.grilleSommet.size() < TAILLE * TAILLE)) {
            fin = false;
        }
        //On vérifie si un déplacement est encore possible
        else {
            for (Case c : this.grilleBase) {
                for (int i = 1; i <= 2; i++) {
                    if (c.getVoisinDirect(i) != null) {
                        if (c.valeursEgales(c.getVoisinDirect(i))) {
                            fin = false;
                        }
                    }
                }
                if (c.getVoisinDirect(4) != null) {
                    if (c.valeursEgales(c.getVoisinDirect(4))) {
                        fin = false;
                    }
                }
                if (c.getVoisinDirect(-4) != null) {
                    if (c.valeursEgales(c.getVoisinDirect(-4))) {
                        fin = false;
                    }
                }
            }
            
            for (Case c : this.grilleMilieu) {
                for (int i = 1; i <= 2; i++) {
                    if (c.getVoisinDirect(i) != null) {
                        if (c.valeursEgales(c.getVoisinDirect(i))) {
                            fin = false;
                        }
                    }
                }
                if (c.getVoisinDirect(4) != null) {
                    if (c.valeursEgales(c.getVoisinDirect(4))) {
                        fin = false;
                    }
                }
                if (c.getVoisinDirect(-4) != null) {
                    if (c.valeursEgales(c.getVoisinDirect(-4))) {
                        fin = false;
                    }
                }
            }
            
            for (Case c : this.grilleSommet) {
                for (int i = 1; i <= 2; i++) {
                    if (c.getVoisinDirect(i) != null) {
                        if (c.valeursEgales(c.getVoisinDirect(i))) {
                            fin = false;
                        }
                    }
                }
                if (c.getVoisinDirect(4) != null) {
                    if (c.valeursEgales(c.getVoisinDirect(4))) {
                        fin = false;
                    }
                }
                if (c.getVoisinDirect(-4) != null) {
                    if (c.valeursEgales(c.getVoisinDirect(-4))) {
                        fin = false;
                    }
                }
            }
        }
        return fin;
    }
    
    public void jeuPerdu(){
        System.out.println("La partie est finie. Votre score est " + this.score);
        System.exit(1);
    }
    
    public void jeuGagne(){
        System.out.println("Bravo ! Vous avez atteint 2048");
        System.exit(0);
    }
    
    public void calculScore(Case c){
        //On fusionne les deux cases
        c.setV(c.getV() * 2);
        
        //On regarde si c'est la nouvelle meilleure case
        if (this.meilleureCase < c.getV()) {
            this.meilleureCase = c.getV();
        }
        
        //On augmente le score
        this.score += c.getV();
        
        //On indique que le déplacement à eu lieu
        deplacement = true;
    }
    
    public void ajoutCase(){
        //ajoute aléatoirement une case sur l'une des trois grilles
    }
}   
