package jeu20483d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author chloe
 */

public class Grille3D implements Parametre, Serializable {
    
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
    
    
    public void deplacer(HashSet<Case> grille, int direction, int ligne){
        //On établit la ligne ou la colonne que l'on doit déplacer et les objectifs correspondants
        int [] obj = new int[3];
        Case [] cases = new Case[3];
        //On déplace les cases de la ligne/colonnes en fonction de la direction
        switch(direction){
            case HAUT:
                //On établit la colonne à déplacer et les objectifs associés
                obj[0]=0;
                obj[1]=1;
                obj[2]=2;
                for(int j=0;j<3;j++){
                    if(this.getCase(ligne, j, grille)!=null){
                        cases[j]=this.getCase(ligne, j, grille);
                    }
                }
                //On vérifie tous les cas possibles et on déplace les cases en conséquence
                if(cases[0]==null){
                    if(cases[1]!=null){
                        grille.remove(cases[1]);
                        cases[1].setY(obj[0]);
                        grille.add(cases[1]);
                        deplacement = true;
                        if(cases[2]!=null){
                            if(cases[2].getV()==cases[1].getV()){
                                grille.remove(cases[1]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setY(obj[0]);
                                grille.add(cases[2]);
                            } else {
                                grille.remove(cases[2]);
                                cases[2].setY(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            grille.remove(cases[2]);
                            cases[2].setY(obj[0]);
                            grille.add(cases[2]);
                        }
                    }
                } else {
                    if(cases[1]!=null){
                        if(cases[1].getV()==cases[0].getV()){
                            grille.remove(cases[0]);
                            grille.remove(cases[1]);
                            this.fusion(cases[1]);
                            cases[1].setY(obj[0]);
                            grille.add(cases[1]);
                            deplacement = true;
                            if(cases[2]!=null){
                                grille.remove(cases[2]);
                                cases[2].setY(obj[1]);
                                grille.add(cases[2]);
                            }
                        } else {
                            if(cases[2]!=null){
                                if(cases[2].getV() == cases[1].getV()){
                                    deplacement = true;
                                    grille.remove(cases[1]);
                                    grille.remove(cases[2]);
                                    this.fusion(cases[2]);
                                    cases[2].setY(obj[1]);
                                    grille.add(cases[2]);
                                }   
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            if(cases[2].getV() == cases[0].getV()){
                                grille.remove(cases[0]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setY(obj[0]);
                                grille.add(cases[2]);
                            } else{
                                grille.remove(cases[2]);
                                cases[2].setY(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    }
                }
                break;
            //On fait de même pour la direction BAS
            case BAS:
                obj[0]=2;
                obj[1]=1;
                obj[2]=0;
                if(this.getCase(ligne, 2, grille)!=null){
                    cases[0]=this.getCase(ligne, 2, grille);
                }
                if(this.getCase(ligne, 0, grille)!=null){
                    cases[2]=this.getCase(ligne, 0, grille);
                }
                if(this.getCase(ligne, 1, grille)!=null){
                    cases[1]=this.getCase(ligne, 1, grille);
                }
                if(cases[0]==null){
                    if(cases[1]!=null){
                        grille.remove(cases[1]);
                        cases[1].setY(obj[0]);
                        grille.add(cases[1]);
                        deplacement = true;
                        if(cases[2]!=null){
                            if(cases[2].getV()==cases[1].getV()){
                                grille.remove(cases[1]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setY(obj[0]);
                                grille.add(cases[2]);
                            } else {
                                grille.remove(cases[2]);
                                cases[2].setY(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            grille.remove(cases[2]);
                            cases[2].setY(obj[0]);
                            grille.add(cases[2]);
                        }
                    }
                } else {
                    if(cases[1]!=null){
                        if(cases[1].getV()==cases[0].getV()){
                            grille.remove(cases[0]);
                            grille.remove(cases[1]);
                            this.fusion(cases[1]);
                            cases[1].setY(obj[0]);
                            grille.add(cases[1]);
                            deplacement = true;
                            if(cases[2]!=null){
                                grille.remove(cases[2]);
                                cases[2].setY(obj[1]);
                                grille.add(cases[2]);
                            }
                        } else {
                            if(cases[2]!=null){
                                if(cases[2].getV() == cases[1].getV()){
                                    deplacement = true;
                                    grille.remove(cases[1]);
                                    grille.remove(cases[2]);
                                    this.fusion(cases[2]);
                                    cases[2].setY(obj[1]);
                                    grille.add(cases[2]);
                                }   
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            if(cases[2].getV() == cases[0].getV()){
                                grille.remove(cases[0]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setY(obj[0]);
                                grille.add(cases[2]);
                            } else{
                                grille.remove(cases[2]);
                                cases[2].setY(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    }
                }
                break;
            //On fait de même pour la direction DROITE
            case DROITE:
                obj[0]=2;
                obj[1]=1;
                obj[2]=0;
                if(this.getCase(2, ligne, grille)!=null){
                    cases[0]=this.getCase(2, ligne, grille);
                }
                if(this.getCase(0, ligne, grille)!=null){
                    cases[2]=this.getCase(0, ligne, grille);
                }
                if(this.getCase(1, ligne, grille)!=null){
                    cases[1]=this.getCase(1, ligne, grille);
                }
                if(cases[0]==null){
                    if(cases[1]!=null){
                        grille.remove(cases[1]);
                        cases[1].setX(obj[0]);
                        grille.add(cases[1]);
                        deplacement = true;
                        if(cases[2]!=null){
                            if(cases[2].getV()==cases[1].getV()){
                                grille.remove(cases[1]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setX(obj[0]);
                                grille.add(cases[2]);
                            } else {
                                grille.remove(cases[2]);
                                cases[2].setX(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            grille.remove(cases[2]);
                            cases[2].setX(obj[0]);
                            grille.add(cases[2]);
                        }
                    }
                } else {
                    if(cases[1]!=null){
                        if(cases[1].getV()==cases[0].getV()){
                            grille.remove(cases[0]);
                            grille.remove(cases[1]);
                            this.fusion(cases[1]);
                            cases[1].setX(obj[0]);
                            grille.add(cases[1]);
                            deplacement = true;
                            if(cases[2]!=null){
                                grille.remove(cases[2]);
                                cases[2].setX(obj[1]);
                                grille.add(cases[2]);
                            }
                        } else {
                            if(cases[2]!=null){
                                if(cases[2].getV() == cases[1].getV()){
                                    deplacement = true;
                                    grille.remove(cases[1]);
                                    grille.remove(cases[2]);
                                    this.fusion(cases[2]);
                                    cases[2].setX(obj[1]);
                                    grille.add(cases[2]);
                                }   
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            if(cases[2].getV() == cases[0].getV()){
                                grille.remove(cases[0]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setX(obj[0]);
                                grille.add(cases[2]);
                            } else{
                                grille.remove(cases[2]);
                                cases[2].setX(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    }
                }
                break;
            //On fait de même pour la direction GAUCHE
            case GAUCHE:
                obj[0]=0;
                obj[1]=1;
                obj[2]=2;
                for(int i=0;i<3;i++){
                    if(this.getCase(i, ligne, grille)!=null){
                        cases[i]=this.getCase(i, ligne, grille);
                    }
                }
                if(cases[0]==null){
                    if(cases[1]!=null){
                        grille.remove(cases[1]);
                        cases[1].setX(obj[0]);
                        grille.add(cases[1]);
                        deplacement = true;
                        if(cases[2]!=null){
                            if(cases[2].getV()==cases[1].getV()){
                                grille.remove(cases[1]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setX(obj[0]);
                                grille.add(cases[2]);
                            } else {
                                grille.remove(cases[2]);
                                cases[2].setX(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            grille.remove(cases[2]);
                            cases[2].setX(obj[0]);
                            grille.add(cases[2]);
                        }
                    }
                } else {
                    if(cases[1]!=null){
                        if(cases[1].getV()==cases[0].getV()){
                            grille.remove(cases[0]);
                            grille.remove(cases[1]);
                            this.fusion(cases[1]);
                            cases[1].setX(obj[0]);
                            grille.add(cases[1]);
                            deplacement = true;
                            if(cases[2]!=null){
                                grille.remove(cases[2]);
                                cases[2].setX(obj[1]);
                                grille.add(cases[2]);
                            }
                        } else {
                            if(cases[2]!=null){
                                if(cases[2].getV() == cases[1].getV()){
                                    deplacement = true;
                                    grille.remove(cases[1]);
                                    grille.remove(cases[2]);
                                    this.fusion(cases[2]);
                                    cases[2].setX(obj[1]);
                                    grille.add(cases[2]);
                                }   
                            }
                        }
                    } else {
                        if(cases[2]!=null){
                            deplacement = true;
                            if(cases[2].getV() == cases[0].getV()){
                                grille.remove(cases[0]);
                                grille.remove(cases[2]);
                                this.fusion(cases[2]);
                                cases[2].setX(obj[0]);
                                grille.add(cases[2]);
                            } else{
                                grille.remove(cases[2]);
                                cases[2].setX(obj[1]);
                                grille.add(cases[2]);
                            }
                        }
                    }
                }
                break;
        }
    }
    
    public void deplacer(int direction){
        if(direction == BASE){
            //On parcourt les 3 grilles 
            for(int i=0;i<TAILLE;i++){
                for(int j=0;j<TAILLE;j++){
                    Case c = new Case(i, j, 0);
                    //On vérifie toutes les possibilités et on déplace les cases en conséquence
                    if(!this.grilleBase.contains(c)){
                        if(this.grilleMilieu.contains(c)){
                            Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                            this.grilleBase.add(caseDeplace); 
                            this.grilleMilieu.remove(caseDeplace);
                            this.deplacement = true;
                            if(this.grilleSommet.contains(c)){
                                Case caseDeplace2 = this.getCase(i, j, this.grilleSommet);
                                if(caseDeplace2.valeursEgales(caseDeplace)){
                                    this.grilleBase.remove(caseDeplace);
                                    this.fusion(caseDeplace2);
                                    this.grilleBase.add(caseDeplace2);
                                    this.grilleSommet.remove(caseDeplace2);
                                    this.deplacement = true;
                                } else {
                                    this.grilleMilieu.add(caseDeplace2);
                                    this.grilleSommet.remove(caseDeplace2);
                                    this.deplacement = true;
                                }
                            }
                        } else {
                            if(this.grilleSommet.contains(c)){
                                Case caseDeplace = this.getCase(i, j, grilleSommet);
                                this.grilleBase.add(caseDeplace);
                                this.grilleSommet.remove(caseDeplace);
                                this.deplacement = true;
                            }
                        }
                    } else {
                        if(grilleMilieu.contains(c)){
                            Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                            if(caseDeplace.valeursEgales(this.getCase(i, j, grilleBase))){
                                this.fusion(caseDeplace);
                                this.grilleBase.remove(this.getCase(i, j, grilleBase));
                                this.grilleBase.add(caseDeplace);
                                this.grilleMilieu.remove(caseDeplace);
                                this.deplacement = true;
                        
                                if(this.grilleSommet.contains(c)){
                                    Case caseDeplace2 = this.getCase(i, j, this.grilleSommet);
                                    this.grilleMilieu.add(caseDeplace2);
                                    this.grilleSommet.remove(caseDeplace2);
                                    this.deplacement = true;
                                }
                            } else {
                                if(this.grilleSommet.contains(c)){
                                    Case caseDeplace2 = this.getCase(i, j, this.grilleSommet);
                                    if(caseDeplace2.valeursEgales(caseDeplace)){
                                        this.grilleMilieu.remove(caseDeplace);
                                        this.fusion(caseDeplace2);
                                        this.grilleMilieu.add(caseDeplace2);
                                        this.grilleSommet.remove(caseDeplace2);
                                        this.deplacement = true;
                                    }
                                }
                            }
                        } else {
                            if(this.grilleSommet.contains(c)){
                                Case caseDeplace = this.getCase(i, j, this.grilleSommet);
                                if(caseDeplace.valeursEgales(this.getCase(i, j, grilleBase))){
                                    this.grilleBase.remove(this.getCase(i, j, grilleBase));
                                    this.fusion(caseDeplace);
                                    this.grilleBase.add(caseDeplace);
                                    this.grilleSommet.remove(caseDeplace);
                                    this.deplacement = true;
                                } else {
                                    this.grilleMilieu.add(caseDeplace);
                                    this.grilleSommet.remove(caseDeplace);
                                    this.deplacement = true;
                                }
                            }
                        }
                    }
                } 
            }
        //On applique le même principe pour la direction SOMMET
        } else if (direction == SOMMET){
            for(int i=0;i<TAILLE;i++){
                for(int j=0;j<TAILLE;j++){
                    Case c = new Case(i, j, 0);
                    if(!this.grilleSommet.contains(c)){
                        if(this.grilleMilieu.contains(c)){
                            Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                            this.grilleSommet.add(caseDeplace); 
                            this.grilleMilieu.remove(caseDeplace);
                            this.deplacement = true;
                            if(this.grilleBase.contains(c)){
                                Case caseDeplace2 = this.getCase(i, j, this.grilleBase);
                                if(caseDeplace2.valeursEgales(caseDeplace)){
                                    this.grilleSommet.remove(caseDeplace);
                                    this.fusion(caseDeplace2);
                                    this.grilleSommet.add(caseDeplace2);
                                    this.grilleBase.remove(caseDeplace2);
                                    this.deplacement = true;
                                } else {
                                    this.grilleMilieu.add(caseDeplace2);
                                    this.grilleBase.remove(caseDeplace2);
                                    this.deplacement = true;
                                }
                            }
                        } else {
                            if(this.grilleBase.contains(c)){
                                Case caseDeplace = this.getCase(i, j, grilleBase);
                                this.grilleSommet.add(caseDeplace);
                                this.grilleBase.remove(caseDeplace);
                                this.deplacement = true;
                            }
                        }
                    } else {
                        if(grilleMilieu.contains(c)){
                            Case caseDeplace = this.getCase(i, j, this.grilleMilieu);
                            if(caseDeplace.valeursEgales(this.getCase(i, j, grilleSommet))){
                                this.fusion(caseDeplace);
                                this.grilleSommet.remove(this.getCase(i, j, grilleSommet));
                                this.grilleSommet.add(caseDeplace);
                                this.grilleMilieu.remove(caseDeplace);
                                this.deplacement = true;
                        
                                if(this.grilleBase.contains(c)){
                                    Case caseDeplace2 = this.getCase(i, j, this.grilleBase);
                                    this.grilleMilieu.add(caseDeplace2);
                                    this.grilleBase.remove(caseDeplace2);
                                    this.deplacement = true;
                                }
                            } else {
                                if(this.grilleBase.contains(c)){
                                    Case caseDeplace2 = this.getCase(i, j, this.grilleBase);
                                    if(caseDeplace2.valeursEgales(caseDeplace)){
                                        this.grilleMilieu.remove(caseDeplace);
                                        this.fusion(caseDeplace2);
                                        this.grilleMilieu.add(caseDeplace2);
                                        this.grilleBase.remove(caseDeplace2);
                                        this.deplacement = true;
                                    }
                                }
                            }
                        } else {
                            if(this.grilleBase.contains(c)){
                                Case caseDeplace = this.getCase(i, j, this.grilleBase);
                                if(caseDeplace.valeursEgales(this.getCase(i, j, grilleSommet))){
                                    this.grilleSommet.remove(this.getCase(i, j, grilleSommet));
                                    this.fusion(caseDeplace);
                                    this.grilleSommet.add(caseDeplace);
                                    this.grilleBase.remove(caseDeplace);
                                    this.deplacement = true;
                                } else {
                                    this.grilleMilieu.add(caseDeplace);
                                    this.grilleBase.remove(caseDeplace);
                                    this.deplacement = true;
                                }
                            }
                        }
                    }
                } 
            }
        }
    }
    
    public boolean lanceDeplacement(int direction){
        deplacement = false; // pour vérifier si on a bougé au moins une case après le déplacement, avant d'en rajouter une nouvelle
        switch(direction){
            case HAUT:
                for(int i=0;i<3;i++){
                    this.deplacer(this.grilleSommet, HAUT, i);
                    this.deplacer(this.grilleMilieu, HAUT, i);
                    this.deplacer(this.grilleBase, HAUT, i);
                }
                break;
            case BAS:
                for(int i=0;i<3;i++){
                    this.deplacer(this.grilleSommet, BAS, i);
                    this.deplacer(this.grilleMilieu, BAS, i);
                    this.deplacer(this.grilleBase, BAS, i);
                }
                break;
            case GAUCHE:
                for(int i=0;i<3;i++){
                    this.deplacer(this.grilleSommet, GAUCHE, i);
                    this.deplacer(this.grilleMilieu, GAUCHE, i);
                    this.deplacer(this.grilleBase, GAUCHE, i);
                }
                break;
            case DROITE:
                for(int i=0;i<3;i++){
                    this.deplacer(this.grilleSommet, DROITE, i);
                    this.deplacer(this.grilleMilieu, DROITE, i);
                    this.deplacer(this.grilleBase, DROITE, i);
                }
                break;
            case SOMMET:
                this.deplacer(direction);
                break;
            case BASE:
                this.deplacer(direction);
                break;
            default:
                break;
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
                int x = c.getX();
                int y = c.getY();
                if(this.getCase(x, y, this.grilleMilieu) != null){
                    if(this.getCase(x, y, grilleMilieu).valeursEgales(c)){
                        fin = false;
                    }
                } else if(this.getCase(x, y, this.grilleSommet) != null){
                    if(this.getCase(x, y, grilleSommet).valeursEgales(c)){
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
            }
            for (Case c : this.grilleSommet) {
                for (int i = 1; i <= 2; i++) {
                    if (c.getVoisinDirect(i) != null) {
                        if (c.valeursEgales(c.getVoisinDirect(i))) {
                            fin = false;
                        }
                    }
                }
                int x = c.getX();
                int y = c.getY();
                if(this.getCase(x, y, this.grilleMilieu) != null){
                    if(this.getCase(x, y, grilleMilieu).valeursEgales(c)){
                        fin = false;
                    }
                } else if(this.getCase(x, y, this.grilleBase) != null){
                    if(this.getCase(x, y, grilleBase).valeursEgales(c)){
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
    
    public void fusion(Case c){
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
                } else if((this.grilleSommet.size() < TAILLE * TAILLE)&&(numGrille == 2)){
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
        }
    }
}
