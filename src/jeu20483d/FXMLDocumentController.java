/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;



public class FXMLDocumentController implements Initializable {
    
    //Initialisation des elements du la vue
    @FXML
    private AnchorPane fond;
    @FXML
    private Label titre;
    @FXML
    private Label textScore;
    @FXML
    private Label score;
    @FXML
    private Label textSommet;
    @FXML
    private Label textBase;
    @FXML
    private GridPane grilleSommet;
    @FXML
    private GridPane grilleMilieu;
    @FXML
    private GridPane grilleBase;
    @FXML
    private Button haut;
    @FXML
    private Button bas;
    @FXML
    private Button droite;
    @FXML
    private Button gauche;
    @FXML
    private Button base;
    @FXML
    private Button sommet;
    
    //variables globales non définies dans la vue
    private Grille3D jeu = new Grille3D();
    private Pane[][] grilleS = new Pane[3][3];
    private Pane[][] grilleM = new Pane[3][3];
    private Pane[][] grilleB = new Pane[3][3];
    private String style;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        boolean b;
        b = this.jeu.ajoutCase();
        b = this.jeu.ajoutCase();
        b = this.jeu.ajoutCase();
        
        //On affiche les deux nouvelles Cases
        this.afficherStyle("Classique");
        this.score.setText("0");
        System.out.print(jeu);
    }  
    
    
    //Méthodes pour déplacer les cases
    
    @FXML
    private void handleButtonHaut(ActionEvent event) {
        System.out.println("You clicked haut!");
    }
    
    @FXML
    private void handleButtonBas(ActionEvent event) {
        
        this.jeu.lanceDeplacement(-1);
        
        for(int j=2;j>-1;j--){
            for(int i=0;i<3;i++){
                if(this.grilleS[i][j] != null){                    
                    int objectif = this.calculObjectif(-1, this.grilleS[i][j], i, j, this.jeu.getGrilleSommet());
                    int x = (int) this.grilleS[i][j].getLayoutX();
                    int y = (int) this.grilleS[i][j].getLayoutY();
                    Pane pp = this.grilleS[i][j];
                    System.out.println(objectif);
                    deplacePane p = new deplacePane(-1, x, y, objectif, pp);
                    Thread th = new Thread(p); // on crée un contrôleur de Thread
                    th.setDaemon(true);
                    th.start();
                    
                }
                if(this.grilleM[i][j] != null){
                    int objectif = this.calculObjectif(-1, this.grilleM[i][j], i, j, this.jeu.getGrilleMilieu());
                    int x = (int) this.grilleM[i][j].getLayoutX();
                    int y = (int) this.grilleM[i][j].getLayoutY();
                    Pane pp = this.grilleM[i][j];
                    System.out.println(objectif);
                    deplacePane p = new deplacePane(-1, x, y, objectif, pp);
                    Thread th = new Thread(p); // on crée un contrôleur de Thread
                    th.setDaemon(true);
                    th.start();
                }
                if(this.grilleB[i][j] != null){
                    int objectif = this.calculObjectif(-1, this.grilleB[i][j], i, j, this.jeu.getGrilleBase());
                    int x = (int) this.grilleB[i][j].getLayoutX();
                    int y = (int) this.grilleB[i][j].getLayoutY();
                    Pane pp = this.grilleB[i][j];
                    System.out.println(objectif);
                    deplacePane p = new deplacePane(-1, x, y, objectif, pp);
                    //Thread th = new Thread(p); // on crée un contrôleur de Thread
                    p.setDaemon(true);
                    p.start();
                }
            }
        }
    }
    
    @FXML
    private void handleButtonGauche(ActionEvent event) {
        System.out.println("You clicked gauche!");
    }
    
    @FXML
    private void handleButtonDroite(ActionEvent event) throws InterruptedException {
        
        this.jeu.lanceDeplacement(2);
        ThreadGroup groupe = new ThreadGroup("mon groupe");
        
        for(int i=2;i>-1;i--){
            for(int j=0;j<3;j++){
                if(this.grilleS[i][j] != null){                    
                    int objectif = this.calculObjectif(2, this.grilleS[i][j], i, j, this.jeu.getGrilleSommet());
                    int x = (int) this.grilleS[i][j].getLayoutX();
                    int y = (int) this.grilleS[i][j].getLayoutY();
                    Pane pp = this.grilleS[i][j];
                    System.out.println(objectif);
                    deplacePane p = new deplacePane(2, x, y, objectif, pp);
                    Thread th = new Thread(groupe, p); // on crée un contrôleur de Thread 
                    th.setDaemon(true);
                    th.start();
                    
                }
                if(this.grilleM[i][j] != null){
                    int objectif = this.calculObjectif(2, this.grilleM[i][j], i, j, this.jeu.getGrilleMilieu());
                    int x = (int) this.grilleM[i][j].getLayoutX();
                    int y = (int) this.grilleM[i][j].getLayoutY();
                    Pane pp = this.grilleM[i][j];
                    System.out.println(objectif);
                    deplacePane p = new deplacePane(2, x, y, objectif, pp);
                    Thread th = new Thread(groupe, p); // on crée un contrôleur de Thread
                    th.setDaemon(true);
                    th.start();
                }
                if(this.grilleB[i][j] != null){
                    int objectif = this.calculObjectif(2, this.grilleB[i][j], i, j, this.jeu.getGrilleBase());
                    int x = (int) this.grilleB[i][j].getLayoutX();
                    int y = (int) this.grilleB[i][j].getLayoutY();
                    Pane pp = this.grilleB[i][j];
                    System.out.println(objectif);
                    deplacePane p = new deplacePane(2, x, y, objectif, pp);
                    //Thread th = new Thread(groupe, p); // on crée un contrôleur de Thread
                    p.setDaemon(true);
                    p.start();
                    
                }
            }
        }
    }
    
    @FXML
    private void handleButtonSommet(ActionEvent event) {
        System.out.println("You clicked sommet!");
    }
    
    @FXML
    private void handleButtonBase(ActionEvent event) {
        System.out.println("You clicked base!");
    }
    
    public int calculObjectif(int direction, Pane p, int x, int y, HashSet<Case> gCase){
        int objectif = 0;
        switch (direction){
                case 1: //Vers le haut
                    break;
                case 2: //vers la droite
                    //La case n'a pas était deplacée
                    if( (this.jeu.getCase(x, y, gCase) != null)&&(Integer.parseInt(p.getAccessibleText()) == this.jeu.getCase(x, y, gCase).getV())){
                        objectif = (int) p.getLayoutX();
                    } else {
                        int o = (int) p.getLayoutX();
                        boolean trouve = false;
                        for(int i=x+1;i<3;i++){ //On parcourt la rangée à la recherche de sa position
                            o = o + 116;
                            if((this.jeu.getCase(i, y, gCase)!=null)&&(!trouve)){
                                //La case se déplace sans fusionner avec une autre
                                if(Integer.parseInt(p.getAccessibleText()) == this.jeu.getCase(i, y, gCase).getV()){
                                    objectif = o;
                                    trouve = true;
                                } 
                                //La case se déplace et fusionne
                                else if(Integer.parseInt(p.getAccessibleText()) == (this.jeu.getCase(i, y, gCase).getV()*2)){
                                    objectif = o;
                                    trouve= true;
                                }
                            }
                        }
                    }
                    break;
                case -1: //vers le bas
                    if( (this.jeu.getCase(x, y, gCase) != null)&&(Integer.parseInt(p.getAccessibleText()) == this.jeu.getCase(x, y, gCase).getV())){
                        objectif = (int) p.getLayoutY();
                    } else {
                        int o = (int) p.getLayoutY();
                        for(int i=y+1;i<3;i++){ //On parcourt la rangée à la recherche de sa position
                            o = o + 116;
                            if(this.jeu.getCase(x, i, gCase)!=null){
                                //La case se déplace sans fusionner avec une autre
                                if(Integer.parseInt(p.getAccessibleText()) == this.jeu.getCase(x, i, gCase).getV()){
                                    objectif = o;
                                } 
                                //La case se déplace et fusionne
                                else if(Integer.parseInt(p.getAccessibleText()) == (this.jeu.getCase(x, i, gCase).getV()*2)){
                                    objectif = o;
                                }
                            }
                        }
                    }
                    break;
                case -2: //vers la gauche
                    break;    
        }
        return objectif;
    }
    
    
    //Méthodes pour changer de style
    
    @FXML
    private void styleNuit(ActionEvent event) {
        this.afficherStyle("Nuit");
    }
    
    @FXML
    private void styleClassique(ActionEvent event) {
        this.afficherStyle("Classique");
    }
    
    private void afficherStyle(String s){
        
        //On supprime les anciens styles
        this.fond.getStyleClass().remove("fond"+this.style);
        this.grilleSommet.getStyleClass().remove("gridpane"+this.style);
        this.grilleMilieu.getStyleClass().remove("gridpane"+this.style);
        this.grilleBase.getStyleClass().remove("gridpane"+this.style);
        this.textBase.getStyleClass().remove("text"+this.style);
        this.textScore.getStyleClass().remove("text"+this.style);
        this.textSommet.getStyleClass().remove("text"+this.style);
        this.score.getStyleClass().remove("text"+this.style);
        this.titre.getStyleClass().remove("text"+this.style);
        this.haut.getStyleClass().remove("button"+this.style);
        this.bas.getStyleClass().remove("button"+this.style);
        this.droite.getStyleClass().remove("button"+this.style);
        this.gauche.getStyleClass().remove("button"+this.style);
        this.sommet.getStyleClass().remove("button"+this.style);
        this.base.getStyleClass().remove("button"+this.style);
        
        this.style = s;
        
        //On attribue les styles
        this.fond.getStyleClass().add("fond"+this.style);
        this.grilleSommet.getStyleClass().add("gridpane"+this.style);
        this.grilleMilieu.getStyleClass().add("gridpane"+this.style);
        this.grilleBase.getStyleClass().add("gridpane"+this.style);
        this.textBase.getStyleClass().add("text"+this.style);
        this.textScore.getStyleClass().add("text"+this.style);
        this.textSommet.getStyleClass().add("text"+this.style);
        this.score.getStyleClass().add("text"+this.style);
        this.titre.getStyleClass().add("text"+this.style);
        this.haut.getStyleClass().add("button"+this.style);
        this.bas.getStyleClass().add("button"+this.style);
        this.droite.getStyleClass().add("button"+this.style);
        this.gauche.getStyleClass().add("button"+this.style);
        this.sommet.getStyleClass().add("button"+this.style);
        this.base.getStyleClass().add("button"+this.style);
        
        //On retire les cases déjà présentes dans les grilles
        for(int i=0; i<3;i++){
            for(int j =0;j<3;j++){
                if(this.grilleB[i][j] != null){
                    this.grilleB[i][j].setVisible(false);
                }
                if(this.grilleM[i][j] != null){
                    this.grilleM[i][j].setVisible(false);
                }
                if(this.grilleS[i][j] != null){
                    this.grilleS[i][j].setVisible(false);
                }
            }
        }
        
        int x;
        int y;
        int valeur;
        
        for(Case c: this.jeu.getGrilleSommet()){
            x = 113 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();
            
            Pane p = new Pane();
            p.setAccessibleText(""+valeur+"");
            Label l = new Label(""+valeur+"");
            l.setMinWidth(114);
            l.setMinHeight(114);
            l.getStyleClass().add("tuile"+valeur+this.style);
            p.getStyleClass().add("pane"+valeur+this.style);
            GridPane.setHalignment(l, HPos.CENTER);
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            
            p.setVisible(true);
            l.setVisible(true);
            
            this.grilleS[c.getX()][c.getY()] = p;
        }
        
        for(Case c: this.jeu.getGrilleMilieu()){
            x = 470 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();           
            Pane p = new Pane();
            p.setAccessibleText(""+valeur+"");
            Label l = new Label(""+valeur+"");
            l.setMinWidth(114);
            l.setMinHeight(114);
            l.getStyleClass().add("tuile"+valeur+this.style);
            p.getStyleClass().add("pane"+valeur+this.style);
            GridPane.setHalignment(l, HPos.CENTER);
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            p.setVisible(true);
            l.setVisible(true);
            
            this.grilleM[c.getX()][c.getY()] = p;
        }
        
        for(Case c: this.jeu.getGrilleBase()){
            x = 826 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();
            Pane p = new Pane();
            p.setAccessibleText(""+valeur+"");
            Label l = new Label(""+valeur+"");
            l.setMinWidth(114);
            l.setMinHeight(114);
            l.getStyleClass().add("tuile"+valeur+this.style);
            p.getStyleClass().add("pane"+valeur+this.style);
        GridPane.setHalignment(l, HPos.CENTER);
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            p.setVisible(true);
            l.setVisible(true);
           
            this.grilleB[c.getX()][c.getY()] = p;
        }
    }
}
