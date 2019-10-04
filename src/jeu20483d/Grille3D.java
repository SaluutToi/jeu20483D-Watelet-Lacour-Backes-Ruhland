package jeu20483d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

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
    
    public Case getCase(int x, int y, HashSet<Case> g){
        for (Case c : g) {
            if(c.getX() == x && c.getY() == y){
                return c;
            }
        }
        return null;
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
        int[][] tableau1 = new int[TAILLE][TAILLE];
        for (Case c : this.grilleSommet) {
            tableau1[c.getY()][c.getX()] = c.getV();
        }
        String result = "";
        for (int i = 0; i < tableau1.length; i++) {
            result += Arrays.toString(tableau1[i]) + "\n";
        }
        result = result + "\n";
        int[][] tableau2 = new int[TAILLE][TAILLE];
        for (Case c : this.grilleMilieu) {
            tableau2[c.getY()][c.getX()] = c.getV();
        }
        for (int i = 0; i < tableau2.length; i++) {
            result += Arrays.toString(tableau2[i]) + "\n";
        }
        result = result + "\n";
        int[][] tableau3 = new int[TAILLE][TAILLE];
        for (Case c : this.grilleBase) {
            tableau3[c.getY()][c.getX()] = c.getV();
        }
        for (int i = 0; i < tableau3.length; i++) {
            result += Arrays.toString(tableau3[i]) + "\n";
        }
        return result;
    }
    
    
    //Méthodes
    public Case[] caseExtreme(int direction, HashSet <Case> g){
        Case[] result = new Case[TAILLE];
        for (Case c : g) {
            switch (direction) {
                case HAUT:
                    if ((result[c.getX()] == null) || (result[c.getX()].getY() > c.getY())) {
                        result[c.getX()] = c;
                    }
                    break;
                case BAS:
                    if ((result[c.getX()] == null) || (result[c.getX()].getY() < c.getY())) {
                        result[c.getX()] = c;
                    }
                    break;
                case GAUCHE:
                    if ((result[c.getY()] == null) || (result[c.getY()].getX() > c.getX())) {
                        result[c.getY()] = c;
                    }
                    break;
                default:
                    if ((result[c.getY()] == null) || (result[c.getY()].getX() < c.getX())) {
                        result[c.getY()] = c;
                    }
                    break;
            }
        }
        return result;
    }
    
    public void deplacer(int direction, HashSet <Case> g, Case[] extremites, int rangee, int compteur){
        if (extremites[rangee] != null) {
            if ((direction == HAUT && extremites[rangee].getY() != compteur)
                    || (direction == BAS && extremites[rangee].getY() != TAILLE - 1 - compteur)
                    || (direction == GAUCHE && extremites[rangee].getX() != compteur)
                    || (direction == DROITE && extremites[rangee].getX() != TAILLE - 1 - compteur)) {
                g.remove(extremites[rangee]);
                switch (direction) {
                    case HAUT:
                        extremites[rangee].setY(compteur);
                        break;
                    case BAS:
                        extremites[rangee].setY(TAILLE - 1 - compteur);
                        break;
                    case GAUCHE:
                        extremites[rangee].setX(compteur);
                        break;
                    default:
                        extremites[rangee].setX(TAILLE - 1 - compteur);
                        break;
                }
                g.add(extremites[rangee]);
                deplacement = true;
            }
            Case voisin = extremites[rangee].getVoisinDirect(-direction);
            if (voisin != null) {
                if (extremites[rangee].valeursEgales(voisin)) {
                    this.calculScore(extremites[rangee]);
                    extremites[rangee] = voisin.getVoisinDirect(-direction);
                    g.remove(voisin);
                    this.deplacer(direction, g, extremites, rangee, compteur + 1);
                } else {
                    extremites[rangee] = voisin;
                    this.deplacer(direction, g, extremites, rangee, compteur + 1);
                }
            }
        }
    }
    
    public void deplacerEntreGrille(int direction){ //Les cases sont déplacer de gRecois à gDonne
        for(int i=0;i<TAILLE;i++){
            for(int j=0;j<TAILLE;j++){
                if(direction == BASE){
                    Case c = new Case(i, j, 0);
                    if(!this.grilleBase.contains(c)){
                        if(this.grilleMilieu.contains(c)){
                            Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                            this.grilleBase.add(caseDeplace);
                            this.grilleMilieu.remove(caseDeplace);
                            this.deplacement = true;
                            
                            if(this.grilleSommet.contains(c)){
                                Case caseDeplace2 = this.getCase(i, j, this.grilleSommet);
                                this.grilleMilieu.add(caseDeplace2);
                                this.grilleSommet.remove(caseDeplace2);
                                this.deplacement = true;
                            }
                        } else if(this.grilleSommet.contains(c)){
                            Case caseDeplace = this.getCase(i, j, grilleSommet);
                            this.grilleBase.add(caseDeplace);
                            this.grilleSommet.remove(caseDeplace);
                            this.deplacement = true;
                        }
                    } else if(grilleMilieu.contains(c) && this.getCase(i, j, grilleBase).valeursEgales(this.getCase(i, j, grilleMilieu))){
                        Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                        this.getCase(i, j, grilleBase).setV(this.getCase(i, j, grilleBase).getV()*2);
                        this.grilleMilieu.remove(caseDeplace);
                        this.deplacement = true;
                        
                         if(this.grilleSommet.contains(c)){
                            Case caseDeplace2 = this.getCase(i, j, this.grilleSommet);
                            this.grilleMilieu.add(caseDeplace2);
                            this.grilleSommet.remove(caseDeplace2);
                            this.deplacement = true;
                        }
                    }    
                } else if (direction == SOMMET){
                    Case c = new Case(i, j, 0);
                    if(!this.grilleSommet.contains(c)){
                        if(this.grilleMilieu.contains(c)){
                            Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                            this.grilleSommet.add(caseDeplace);
                            this.grilleMilieu.remove(caseDeplace);
                            this.deplacement = true;
                            
                            if(this.grilleBase.contains(c)){
                                Case caseDeplace2 = this.getCase(i, j, this.grilleBase);
                                this.grilleMilieu.add(caseDeplace2);
                                this.grilleBase.remove(caseDeplace2);
                                this.deplacement = true;
                            }
                        } else if(this.grilleBase.contains(c)){
                            Case caseDeplace = this.getCase(i, j, grilleBase);
                            this.grilleSommet.add(caseDeplace);
                            this.grilleBase.remove(caseDeplace);
                            this.deplacement = true;
                        }
                    } else if(grilleMilieu.contains(c) && this.getCase(i, j, grilleSommet).valeursEgales(this.getCase(i, j, grilleMilieu))){
                        Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                        this.getCase(i, j, grilleSommet).setV(this.getCase(i, j, grilleSommet).getV()*2);
                        this.grilleMilieu.remove(caseDeplace);
                        this.deplacement = true;
                        
                         if(this.grilleBase.contains(c)){
                            Case caseDeplace2 = this.getCase(i, j, this.grilleBase);
                            this.grilleMilieu.add(caseDeplace2);
                            this.grilleBase.remove(caseDeplace2);
                            this.deplacement = true;
                        }
                    }
                }
            }
        }
    }
    
    public boolean lanceDeplacement(int direction){
        deplacement = false; // pour vérifier si on a bougé au moins une case après le déplacement, avant d'en rajouter une nouvelle
        if(direction == HAUT || direction == BAS || direction == GAUCHE || direction == DROITE ){
            for (int i = 0; i < TAILLE; i++) {
                    this.deplacer(direction, this.grilleBase, this.caseExtreme(direction, grilleBase), i, 0);
                    this.deplacer(direction, this.grilleMilieu, this.caseExtreme(direction, grilleMilieu), i, 0);
                    this.deplacer(direction, this.grilleSommet, this.caseExtreme(direction, grilleSommet), i, 0);
            }
        } else if (direction == BASE || direction == SOMMET){
            this.deplacerEntreGrille(direction);
        }
        return deplacement;
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
    
    public boolean ajoutCase(){
        if ((this.grilleBase.size() < TAILLE * TAILLE)||(this.grilleMilieu.size() < TAILLE * TAILLE)||(this.grilleSommet.size() < TAILLE * TAILLE)) {          
            //On choisit une grille au hasard
            boolean trouve = false;
            Random ra = new Random();
            int numGrille = ra.nextInt(3); //0: grilleBase, 1: grilleMilieu, 2:grilleSommet
            while(!trouve){
                numGrille = ra.nextInt(3);
                if((this.grilleBase.size() < TAILLE * TAILLE)&&(numGrille == 0)){
                    trouve = true;
                } else if((this.grilleMilieu.size() < TAILLE * TAILLE)&&(numGrille == 1)){
                    trouve = true;
                } else if(this.grilleSommet.size() < TAILLE * TAILLE){
                    trouve = true;
                }  
            }
            ArrayList<Case> casesLibres = new ArrayList<>();
            ArrayList<Integer> valeurs = new ArrayList<>();
            valeurs.add(2);valeurs.add(2);valeurs.add(4);
            int valeur = valeurs.get(ra.nextInt(valeurs.size()));
            // on crée toutes les cases encore libres de la grille choisie
            switch (numGrille) {
                case 0:
                    for (int x = 0; x < TAILLE; x++) {
                        for (int y = 0; y < TAILLE; y++) {
                            Case c = new Case(x, y, valeur);
                            if (!this.grilleBase.contains(c)) { 
                                casesLibres.add(c);
                            }
                        }
                    }
                    break;
                case 1:
                    for (int x = 0; x < TAILLE; x++) {
                        for (int y = 0; y < TAILLE; y++) {
                            Case c = new Case(x, y, valeur);
                            if (!this.grilleMilieu.contains(c)) { 
                                casesLibres.add(c);
                            }
                        }
                    }
                    break;
                case 2:
                    for (int x = 0; x < TAILLE; x++) {
                        for (int y = 0; y < TAILLE; y++) {
                            Case c = new Case(x, y, valeur);
                            if (!this.grilleSommet.contains(c)) { 
                                casesLibres.add(c);
                            }
                        }
                    }
                    break;
            }
            // on en choisit une au hasard et on l'ajoute à la grille
            Case ajout = casesLibres.get(ra.nextInt(casesLibres.size()));
            switch (numGrille) {
                case 0:
                    ajout.setGrille(this.grilleBase);
                    this.grilleBase.add(ajout);
                    if((this.grilleBase.size() == 1)||(this.meilleureCase == 2 && ajout.getV() == 4)){
                        this.meilleureCase = 4;
                    }
                    break;
                case 1:
                    ajout.setGrille(this.grilleMilieu);
                    this.grilleMilieu.add(ajout);
                    if((this.grilleMilieu.size() == 1)||(this.meilleureCase == 2 && ajout.getV() == 4)){
                        this.meilleureCase = 4;
                    }
                    break;
                case 2:
                    ajout.setGrille(this.grilleSommet);
                    this.grilleSommet.add(ajout);
                    if((this.grilleSommet.size() == 1)||(this.meilleureCase == 2 && ajout.getV() == 4)){
                        this.meilleureCase = 4;
                    }
                    break;
            }
            return true;
        } else {
            return false;
        }
    }
}