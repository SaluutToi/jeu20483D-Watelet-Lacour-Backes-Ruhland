/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class FXMLJeuController implements Initializable {
    
    //Initialisation des elements de la vue:
    
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
    
    //variables globales non définies dans la vue:
    
    private Grille3D jeu = new Grille3D();
    //Pour gérer les panes:
    private final Pane[][] grilleS = new Pane[3][3];
    private final Pane[][] grilleM = new Pane[3][3];
    private final Pane[][] grilleB = new Pane[3][3];
    //pour gérer les objectifs des panes lorsque l'on effectue un déplacement:
    private final int[][] objS = new int[3][3];
    private final int[][] objM = new int[3][3];
    private final int[][] objB = new int[3][3];
    //Pour mettre à jour les grilles après un déplacement: false la case est libre, true la case est prise.
    private boolean[][] casePriseSommet = new boolean[3][3];
    private boolean[][] casePriseMilieu = new boolean[3][3];
    private boolean[][] casePriseBase = new boolean[3][3];
    //pour gérer les fusions des cases
    private final int[][] fusionS = new int[3][3];
    private final int[][] fusionM = new int[3][3];
    private final int[][] fusionB = new int[3][3];
    
    //Pour établir les attribue des threads:
    private int xTemp; private int yTemp; private int aTemp; private int bTemp; private int fusion;
    //Pour permettre de changer le style:
    private String style;
    private Pane fondMessageFin;
    private Pane messageFin;
    private boolean aide= false;
    private Pane fondAide;
    private Joueur joueur;

    /**
     * Controller qui initialise la vue
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.joueur = Bdd.lecFichierJoueur();
        this.style = joueur.getStyle();
        
        this.jeu.ajoutCase();
        this.jeu.ajoutCase();
        this.jeu.ajoutCase();
        
        //On affiche les deux nouvelles Cases
        this.afficher(this.style);
        this.score.setText("0");
    }

    public void reinitialize(URL url, ResourceBundle rb, Partie p){
        this.joueur = Bdd.lecFichierJoueur();
        this.style = joueur.getStyle();
        this.jeu = p.getGrille1();
        this.afficher(this.style);
        String s = String.valueOf(this.jeu.getScore());
        this.score.setText(s);
    }

    /**
     * ecoute les entrees claviers
     * @param ke
     */
    @FXML
    private void keyPressed(KeyEvent ke) {
        String touche = ke.getText();
        if(touche.compareTo("q") == 0){ //GAUCHE
            this.handleButtonGauche();
        } else if(touche.compareTo("s") == 0){ //BAS
            this.handleButtonBas();
        }else if(touche.compareTo("d") == 0){ //DROITE
            this.handleButtonDroite();
        }else if(touche.compareTo("z") == 0){ //HAUT
            this.handleButtonHaut();
        }else if(touche.compareTo("r") == 0){ //SOMMET  
            this.handleButtonSommet();
        } else if(touche.compareTo("f") == 0){ //BASE
            this.handleButtonBase();
        }
    }

    /**
     * Controller deplace les cases vers le haut
     */
    @FXML
    private void handleButtonHaut() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        //On déplace les cases
        boolean v = this.jeu.lanceDeplacement(1);
        /*
        //On met à jour les tableaux de objectifs
        for(int i=0;i<3;i++){
            this.calculObjectif(1, i, 1);
            this.calculObjectif(2, i, 1);
            this.calculObjectif(3, i, 1);
        }
        ThreadGroup groupe = new ThreadGroup("groupe");
        synchronized(groupe){
            //On parcourt toutes les cases
            for(int i=0;i<3;i++){                  
                for(int j=2; j>-1; j--){
                    //On stocke les coordonées du pane par rapport à la grille
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleS[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleS[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private final int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (y > objS[a][b]) {
                                    y -= 1; 
                                    Platform.runLater(() -> {
                                        grilleS[a][b].relocate(x, y);
                                        grilleS[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                                      
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleS[a][b].getAccessibleText())*2;
                                switch (fusionS[a][b]) {
                                    case 0:
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call
                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                    //Grille MILIEU
                    if(this.grilleM[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleM[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleM[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private final int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (y > objM[a][b]) {
                                    y -= 1; 
                                    Platform.runLater(() -> {
                                        grilleM[a][b].relocate(x, y);
                                        grilleM[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleM[a][b].getAccessibleText())*2;
                                switch (fusionM[a][b]) {
                                    case 0:
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;  
                            } // end call
                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true); 
                        th.start();
                    }  
                    //Grille BASE
                    if(this.grilleB[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleB[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleB[i][j].getLayoutY();
                       Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private final int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (y > objB[a][b]) {
                                    y -= 1;
                                    Platform.runLater(() -> {
                                        grilleB[a][b].relocate(x, y);
                                        grilleB[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                }
            }
        }*/
        
        this.afficher(this.style);
        
        //On met à jours le score
        this.score.setText(""+this.jeu.getScore()+"");
        //On vérifie si la partie est finie
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase();
        }
    }

    /**
     * Controller deplace les cases vers le bas
     */
    @FXML
    private void handleButtonBas() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        //On déplace les cases
        boolean v = this.jeu.lanceDeplacement(-1);
        /*
        //On met à jour les tableaux de objectifs
        for(int i=0;i<3;i++){
            this.calculObjectif(1, i, -1);
            this.calculObjectif(2, i, -1);
            this.calculObjectif(3, i, -1);
        }
        ThreadGroup groupe = new ThreadGroup("groupe");
        synchronized(groupe){
            //On parcourt toutes les cases
            for(int i=0;i<3;i++){                  
                for(int j=0; j<3; j++){
                    //On stocke les coordonnées du pane par rapport à la grille
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleS[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleS[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private final int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (y < objS[a][b]) {
                                    y += 1; 
                                    Platform.runLater(() -> {
                                        grilleS[a][b].relocate(x, y);
                                        grilleS[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleS[a][b].getAccessibleText())*2;
                                switch (fusionS[a][b]) {
                                    case 0:
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                    //Grille MILIEU
                    if(this.grilleM[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleM[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleM[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private final int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (y < objM[a][b]) {
                                    y += 1; 
                                    Platform.runLater(() -> {
                                        grilleM[a][b].relocate(x, y);
                                        grilleM[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleM[a][b].getAccessibleText())*2;
                                switch (fusionM[a][b]) {
                                    case 0:
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call

                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true); 
                        th.start();
                    }  
                    //Grille BASE
                    if(this.grilleB[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleB[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleB[i][j].getLayoutY();
                       Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private final int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (y < objB[a][b]) {
                                    y += 1;
                                    Platform.runLater(() -> {
                                        grilleB[a][b].relocate(x, y);
                                        grilleB[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                }
            }
        }*/
        
        this.afficher(this.style);
        
        //On met à jour le score
        this.score.setText(""+this.jeu.getScore()+"");
        //On vérifie si les partie est finie
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase();
        }
    }

    /**
     * Controller deplace les cases vers la gauche
     */
    @FXML
    private void handleButtonGauche() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        //On déplace les cases
        boolean v = this.jeu.lanceDeplacement(-2);
        /*
        //On met à jour les tableaux de objectifs
        for(int i=0;i<3;i++){
            this.calculObjectif(1, i, -2);
            this.calculObjectif(2, i, -2);
            this.calculObjectif(3, i, -2);
        }
        ThreadGroup groupe = new ThreadGroup("groupe");
        synchronized(groupe){
            //On parcourt toutes les cases
            for(int j=0;j<3;j++){                  
                for(int i=0; i<3; i++){
                    //On stocke les coordonnées du pane par rapport à la grille
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleS[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleS[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x > objS[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(() -> {
                                        grilleS[a][b].relocate(x, y);
                                        grilleS[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleS[a][b].getAccessibleText())*2;
                                switch (fusionS[a][b]) {
                                    case 0:
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call
                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                    //Grille MILIEU
                    if(this.grilleM[i][j] != null){
                        //On stocke les coordonnées du pane par rapport la vue
                        this.xTemp = (int) this.grilleM[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleM[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x > objM[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(() -> {
                                        grilleM[a][b].relocate(x, y);
                                        grilleM[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleM[a][b].getAccessibleText())*2;
                                switch (fusionM[a][b]) {
                                    case 0:
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call
                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true); 
                        th.start();
                    }  
                    //Grille BASE
                    if(this.grilleB[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleB[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleB[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x > objB[a][b]) {
                                    x -= 1;
                                    Platform.runLater(() -> {
                                        grilleB[a][b].relocate(x, y);
                                        grilleB[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                }
            }
        }*/
        
        this.afficher(this.style);
        
        //On met à jour la score
        this.score.setText(""+this.jeu.getScore()+"");
        //On vérifie si la partie est finie
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase();
        }
    }

    /**
     * Controller deplace les cases vers la droite
     */
    @FXML
    private void handleButtonDroite() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        //On déplace les cases
        boolean v = this.jeu.lanceDeplacement(2);
        /*
        //On met à jour les tableaux de objectifs
        for(int i=0;i<3;i++){
            this.calculObjectif(1, i, 2);
            this.calculObjectif(2, i, 2);
            this.calculObjectif(3, i, 2);
        }
        ThreadGroup groupe = new ThreadGroup("groupe");
        synchronized(groupe){
            //On parcourt toutes les cases
            for(int j=0;j<3;j++){                  
                for(int i=2; i>-1; i--){
                    //On stocke les coordonnées du pane par rapport à la grille
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleS[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleS[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x < objS[a][b]) {
                                    x += 1; 
                                    Platform.runLater(() -> {
                                        grilleS[a][b].relocate(x, y);
                                        grilleS[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                                        System.out.println(ex);
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleS[a][b].getAccessibleText())*2;
                                switch (fusionS[a][b]) {
                                    case 0:
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call
                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                    //Grille MILIEU
                    if(this.grilleM[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleM[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleM[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x < objM[a][b]) {
                                    x += 1; 
                                    Platform.runLater(() -> {
                                        grilleM[a][b].relocate(x, y);
                                        grilleM[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                                       System.out.println(ex);
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleM[a][b].getAccessibleText())*2;
                                switch (fusionM[a][b]) {
                                    case 0:
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call

                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true); 
                        th.start();
                    }  
                    //Grille BASE
                    if(this.grilleB[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleB[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleB[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x < objB[a][b]) {
                                    x += 1;
                                    Platform.runLater(() -> {
                                        grilleB[a][b].relocate(x, y);
                                        grilleB[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                                        System.out.println(ex);
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 1:
                                        try {
                                            Thread.sleep(1*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 2:
                                        try {
                                            Thread.sleep(2*116*3);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true); 
                        th.start();
                    }
                }
            }
        }*/
        
        this.afficher(this.style);
        
        //On met à jours le score
        this.score.setText(""+this.jeu.getScore()+"");
        //On vérifie si la partie est finie
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase();
        }
    }

    /**
     * Controller deplace les cases vers le sommet
     */
    @FXML
    private void handleButtonSommet() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        //On déplace les cases
        boolean v = this.jeu.lanceDeplacement(4);
        /*
        //On met à jour les tableaux de objectifs
        this.calculObjectif(4);
        //On déplace les panes
        ThreadGroup groupe = new ThreadGroup("mon groupe");
        synchronized(groupe){
            for(int i=0;i<3;i++){
                for(int j=0; j<3; j++){
                    //On stocke les coordonnées du pane par rapport à la grille
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleS[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleS[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x > objS[a][b]) {
                                    x -= 1;
                                    Platform.runLater(() -> {
                                        grilleS[a][b].relocate(x, y);
                                        grilleS[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleS[a][b].getAccessibleText())*2;
                                switch (fusionS[a][b]) {
                                    case 0:
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 3:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 4:
                                        try {
                                            Thread.sleep(2*3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); // on crée un contrôleur de Thread
                        th.setDaemon(true);
                        th.start();
                    }
                    //Grille MILIEU
                    if(this.grilleM[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleM[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleM[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x > objM[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(() -> {
                                        grilleM[a][b].relocate(x, y);
                                        grilleM[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleM[a][b].getAccessibleText())*2;
                                switch (fusionM[a][b]) {
                                    case 0:
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 3:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 4:
                                        try {
                                            Thread.sleep(2*3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); // on crée un contrôleur de Thread
                        th.setDaemon(true);
                        th.start();
                    }  
                    //Grille BASE
                    if(this.grilleB[i][j] != null){
                        //On stocke les coordonnées du pane par rapport à la vue
                        this.xTemp = (int) this.grilleB[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleB[i][j].getLayoutY();
                       Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x > objB[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(() -> {
                                        grilleB[a][b].relocate(x, y);
                                        grilleB[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 3:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 4:
                                        try {
                                            Thread.sleep(2*3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null; 
                            } // end call

                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true);
                        th.start();
                    }
                }
            }
        }*/
                
        this.afficher(this.style);
                
        //On met à jour le score
        this.score.setText(""+this.jeu.getScore()+"");
        //On vérifie si la partie est finie
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase();
        }
    }

    /**
     * Controller deplacement vers la base
     */
    @FXML
    private void handleButtonBase() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        //On déplac les cases
        boolean v = this.jeu.lanceDeplacement(-4);
        /*
        //On met à jour les tableaux de objectifs
        this.calculObjectif(-4);
        //On déplace les panes
        ThreadGroup groupe = new ThreadGroup("mon groupe");
        synchronized(groupe){
            for(int i=0;i<3;i++){
                for(int j=0; j<3; j++){ 
                    //On sotcke les coordonnées du pane par rapport à la grille
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
                        //On stocke les coordonnées du panes par rapport à la vue
                        this.xTemp = (int) this.grilleS[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleS[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x < objS[a][b]) {
                                    x += 1;
                                    Platform.runLater(() -> {
                                        grilleS[a][b].relocate(x, y);
                                        grilleS[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleS[a][b].getAccessibleText())*2;
                                switch (fusionS[a][b]) {
                                    case 0:
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 3:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 4:
                                        try {
                                            Thread.sleep(2*3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleS[a][b].getStyleClass().remove("pane"+grilleS[a][b].getAccessibleText()+style);
                                        grilleS[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call

                        };
                        Thread th = new Thread(groupe, task); 
                        th.setDaemon(true);
                        th.start();
                    }
                    //Grille MILIEU
                    if(this.grilleM[i][j] != null){
                        //On stocke les coordonnées du pane par rapportr à la vue
                        this.xTemp = (int) this.grilleM[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleM[i][j].getLayoutY();
                        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x < objM[a][b]) {
                                    x += 1;
                                    Platform.runLater(() -> {
                                        grilleM[a][b].relocate(x, y);
                                        grilleM[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                }
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleM[a][b].getAccessibleText())*2;
                                switch (fusionM[a][b]) {
                                    case 0:
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 3:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 4:
                                        try {
                                            Thread.sleep(2*3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleM[a][b].getStyleClass().remove("pane"+grilleM[a][b].getAccessibleText()+style);
                                        grilleM[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call

                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true);
                        th.start();
                    }  
                    //Grille BASE
                    if(this.grilleB[i][j] != null){
                        //On stocke les coodonnées du pane par rappot à la vue
                        this.xTemp = (int) this.grilleB[i][j].getLayoutX();
                        this.yTemp = (int) this.grilleB[i][j].getLayoutY();
                       Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                            private final int a=aTemp;
                            private final int b=bTemp;
                            private int x=xTemp;
                            private final int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                while (x < objB[a][b]) {
                                    x += 1;
                                    Platform.runLater(() -> {
                                        grilleB[a][b].relocate(x, y);
                                        grilleB[a][b].setVisible(true);
                                    } // classe anonyme
                                    );
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
                                } 
                                //En fonction de la valeur de fusionB[a][b] on attend un certain temps avant de changer la classe du pane si il y a eu une fusion
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 3:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 4:
                                        try {
                                            Thread.sleep(2*3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    default:
                                        break;
                                }
                                return null;
                            } // end call
                        };
                        Thread th = new Thread(groupe, task);
                        th.setDaemon(true);
                        th.start();
                    }
                }
            }
        }*/
        
        this.afficher(this.style);
        
        //On met à jour le score
        this.score.setText(""+this.jeu.getScore()+"");
        //On véirife si la partie est finie
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase();
        }
    }

    /**
     *
     * @param d
     */
    private void calculObjectif(int d){
        //Coordonnée X du coin haut-gauche de chaque grille servant à définir les objectifs:
        final int xSommet = 112;
        final int xMilieu = 469;
        final int xBase = 825;
        //On initialise les grilles temporaires qui serviront à remplir les varibales globales indiquant:
        boolean[] casePriseTemp = new boolean[3]; //les cases prises
        int[] fusionTemp = new int[3]; //les fusions
        for(int i=0;i<3;i++){
            casePriseTemp[i] = false;
            fusionTemp[i] = -1;
        }
        //On remplit les tableaux temporaires des objectifs, celui de fusions et celui des cases disponibles
        //On parcourt les grilles
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                int[] result = new int[3];
                Pane[] ligne = new Pane[3];
                ligne[0] = this.grilleS[i][j];
                ligne[1] = this.grilleM[i][j];
                ligne[2] = this.grilleB[i][j];
                if(d == 4){ //SOMMET
                    //On vérifie tous les cas possibles
                    //cas: [ ][2][3] ou [ ][ ][3] ou [ ][2][ ] ou [ ][ ][ ]
                    if(ligne[0] == null){
                        //cas: [ ][2][3] ou  [ ][2][ ]
                        if(ligne[1] != null){
                            result[1] = xSommet + 116*i; //on décale la case 2
                            casePriseTemp[0] = true;
                            //cas: [ ][2][3]
                            if(ligne[2] != null){
                                //si les deux cases ont la même valeurs
                                if(ligne[2].getAccessibleText().equals(ligne[1].getAccessibleText())){
                                    result[2] = xSommet + 116*i;
                                    fusionTemp[2] = 0;
                                    fusionTemp[1] = 3;
                                //si elles n'ont pas la même valeur
                                } else {
                                    result[2] = xMilieu + 116*i;
                                    casePriseTemp[2] = true;
                               }
                            }
                        //cas: [ ][ ][3] ou [ ][ ][ ]
                        } else {
                            //cas: [ ][ ][3]
                            if(ligne[2] != null){
                                result[2] = xSommet + 116*i;
                                casePriseTemp[0] = true;
                            }
                        }
                    //cas: [1][2][3] ou [1][ ][3] ou [1][2][ ] ou [1][ ][ ]
                    } else { 
                        result[0] = xSommet + 116*i;
                        casePriseTemp[0] = true;
                        //cas: [1][2][3] ou [1][2][ ]
                        if(ligne[1] != null){
                            if(ligne[1].getAccessibleText().equals(ligne[0].getAccessibleText())){ 
                                result[1] = xSommet + 116*i;
                                fusionTemp[0] = 3;
                                fusionTemp[1] = 0;
                                //cas: [12][ ][3]
                                if(ligne[2] != null){ 
                                    result[2] = xMilieu + 116*i;
                                    casePriseTemp[1] = true;
                                } 
                            } else { 
                                result[1] = xMilieu + 116*i;
                                casePriseTemp[1] = true;
                                //cas: [1][2][3]
                                if(ligne[2] != null){ 
                                    if(ligne[2].getAccessibleText().equals(ligne[1].getAccessibleText())){
                                        result[2] = xMilieu + 116*i;
                                        fusionTemp[1] = 3;
                                        fusionTemp[2] = 0;
                                    } else {
                                        result[2] = xBase + 116*i;
                                        casePriseTemp[2] = true;
                                    }
                                }
                            }
                        //cas: [1][ ][3] ou [1][ ][ ]
                        } else {
                            //cas: [1][ ][3]
                            if(ligne[2] != null){
                                //si 1 et 3 ont la même valeur
                                if(ligne[2].getAccessibleText().equals(ligne[0].getAccessibleText())){
                                    result[2] = xSommet + 116*i;
                                    fusionTemp[2] = 0;
                                    fusionTemp[0] = 4;
                                //si 1 et 3 n'ont pas la même valeur
                                } else {
                                    result[2] = xMilieu + 116*i;
                                    casePriseTemp[1] = true;
                                }
                            }
                        }
                    }
         
                } else if(d == -4){ //BASE
                    //cas: [3][2][ ] ou [3][ ][ ] ou [ ][2][ ] ou [ ][ ][ ]
                    if(ligne[2] == null){
                        //cas: [3][2][ ] ou [ ][2][ ]
                        if(ligne[1] != null){
                            result[1] = xBase + 116*i; 
                            casePriseTemp[2] = true;
                            //cas: [1][2][ ]
                            if(ligne[0] != null){
                                if(ligne[0].getAccessibleText().equals(ligne[1].getAccessibleText())){
                                    result[0] = xBase + 116*i;
                                    fusionTemp[1] = 3;
                                    fusionTemp[0] = 0;
                                } else {
                                    result[0] = xMilieu + 116*i;
                                    casePriseTemp[1] = true;
                                }
                            }
                        //cas: [3][ ][ ] ou [ ][ ][ ]
                        } else {
                            //cas: [3][ ][ ]
                            if(ligne[0] != null){
                                result[0] = xBase + 116*i;
                                casePriseTemp[2] = true;
                            }
                        }
                    //cas: [3][2][1] ou [ ][2][1] ou [3][ ][1] ou [ ][ ][1]
                    } else {
                        result[2] = xBase + 116*i;
                        casePriseTemp[2] = true;
                        //cas: [3][2][1] ou [ ][2][1]
                        if(ligne[1] != null){
                            if(ligne[1].getAccessibleText().equals(ligne[2].getAccessibleText())){
                                result[1] = xBase + 116*i;
                                fusionTemp[2] = 3;
                                fusionTemp[1] = 0;
                                //cas: [3][ ][21]
                                if(ligne[0] != null){
                                    result[0] = xMilieu + 116*i;
                                    casePriseTemp[1] = true;
                                }
                            } else {
                                result[1] = xMilieu + 116*i;
                                casePriseTemp[1] = true;
                                //cas: [3][2][1]
                                if(ligne[0] != null){
                                    if(ligne[0].getAccessibleText().equals(ligne[1].getAccessibleText())){
                                        result[0] = xMilieu + 116*i;
                                        fusionTemp[1] = 3;
                                        fusionTemp[0] = 0;
                                    } else {
                                        result[0] = xSommet + 116*i;
                                        casePriseTemp[0] = true;
                                    }
                                }
                            }
                        //cas: [a][ ][c]
                        } else {
                            if(ligne[0] != null){
                                if(ligne[2].getAccessibleText().equals(ligne[0].getAccessibleText())){
                                    result[0] = xBase + 116*i;
                                    fusionTemp[2] = 4;
                                    fusionTemp[0] = 0;
                                } else {
                                    result[0] = xMilieu + 116*i; 
                                    casePriseTemp[1] = true;
                                }
                            }
                        }
                    }
                }
                //On remplis les variables globales
                this.objS[i][j] = result[0];
                this.objM[i][j] = result[1];
                this.objB[i][j] = result[2];
                this.casePriseSommet[i][j] = casePriseTemp[0];
                this.casePriseMilieu[i][j] = casePriseTemp[1];
                this.casePriseBase[i][j] = casePriseTemp[2];
                this.fusionS[i][j] = fusionTemp[0];
                this.fusionM[i][j] = fusionTemp[1];
                this.fusionB[i][j] = fusionTemp[2];
            }
        }
    }

    /**
     * Controller de calcul de score
     * @param iGrille
     * @param iLigne
     * @param direction
     */
    private void calculObjectif(int iGrille, int iLigne, int direction){
        //On initialise les grilles temporaires qui serviront à remplir les varibales globales indiquant:
        boolean[] casesPrises = new boolean[3]; //les cases prises
        int[] fusionTemp = new int[3]; //les fusions
        for(int i=0;i<3;i++){
            casesPrises[i] = false;
            fusionTemp[i] = -1;
        }
        int[] result = new int[3];
        //On établit la ligne ou la colonne que l'on doit déplacer ainsi que les objectifs correspondants
        Pane[] cases = new Pane[3];
        int[] obj = new int[3];
        //En fonction de la direction
        switch(direction){
            case 1: // HAUT
                obj[0] = 386;
                obj[1] = 502;
                obj[2] = 618;
                //Et de la grille
                switch(iGrille){
                    case 1: //SOMMET
                        cases[0] = this.grilleS[iLigne][0];
                        cases[1] = this.grilleS[iLigne][1];
                        cases[2] = this.grilleS[iLigne][2];
                        break;
                    case 2: //MILIEU
                        cases[0] = this.grilleM[iLigne][0];
                        cases[1] = this.grilleM[iLigne][1];
                        cases[2] = this.grilleM[iLigne][2];
                        break;
                    case 3: //BASE
                        cases[0] = this.grilleB[iLigne][0];
                        cases[1] = this.grilleB[iLigne][1];
                        cases[2] = this.grilleB[iLigne][2];
                        break;
                }
                break;
            case 2: //DROITE
                switch(iGrille){
                    case 1: //SOMMET
                        cases[0] = this.grilleS[2][iLigne];
                        cases[1] = this.grilleS[1][iLigne];
                        cases[2] = this.grilleS[0][iLigne];
                        obj[0] = 344;
                        obj[1] = 228;
                        obj[2] = 112;
                        break;
                    case 2: //MILIEU
                        cases[0] = this.grilleM[2][iLigne];
                        cases[1] = this.grilleM[1][iLigne];
                        cases[2] = this.grilleM[0][iLigne];
                        obj[0] = 701;
                        obj[1] = 585;
                        obj[2] = 469;
                        break;
                    case 3: //BASE
                        cases[0] = this.grilleB[2][iLigne];
                        cases[1] = this.grilleB[1][iLigne];
                        cases[2] = this.grilleB[0][iLigne];
                        obj[0] = 1057;
                        obj[1] = 941;
                        obj[2] = 825;
                        break;
                }
                break;
            case -1: //BAS
                obj[0] = 618;
                obj[1] = 502;
                obj[2] = 386;
                switch(iGrille){
                    case 1: //SOMMET
                        cases[0] = this.grilleS[iLigne][2];
                        cases[1] = this.grilleS[iLigne][1];
                        cases[2] = this.grilleS[iLigne][0];
                        break;
                    case 2: //MILIEU
                        cases[0] = this.grilleM[iLigne][2];
                        cases[1] = this.grilleM[iLigne][1];
                        cases[2] = this.grilleM[iLigne][0];
                        break;
                    case 3: //BASE
                        cases[0] = this.grilleB[iLigne][2];
                        cases[1] = this.grilleB[iLigne][1];
                        cases[2] = this.grilleB[iLigne][0];
                        break;
                }
                break;
            case -2: //GAUCHE
                switch(iGrille){
                    case 1: //SOMMET
                        cases[0] = this.grilleS[0][iLigne];
                        cases[1] = this.grilleS[1][iLigne];
                        cases[2] = this.grilleS[2][iLigne];
                        obj[2] = 344;
                        obj[1] = 228;
                        obj[0] = 112;
                        break;
                    case 2: //MILIEU
                        cases[0] = this.grilleM[0][iLigne];
                        cases[1] = this.grilleM[1][iLigne];
                        cases[2] = this.grilleM[2][iLigne];
                        obj[2] = 701;
                        obj[1] = 585;
                        obj[0] = 469;
                        break;
                    case 3: //BASE
                        cases[0] = this.grilleB[0][iLigne];
                        cases[1] = this.grilleB[1][iLigne];
                        cases[2] = this.grilleB[2][iLigne];
                        obj[2] = 1057;
                        obj[1] = 941;
                        obj[0] = 825;
                        break;
                }
                break;
        }
        //On vérifie tous les cas possibles en mettant àjours les variables temporaires
        if(cases[0]==null){
            if(cases[1]!=null){
                result[1]=obj[0];
                casesPrises[0] = true;
                if(cases[2]!=null){
                    if(cases[2].getAccessibleText().equals(cases[1].getAccessibleText())){
                        fusionTemp[1] = 1;
                        fusionTemp[2] = 0;
                        result[2]=obj[0];
                    } else {
                        result[2]=obj[1];
                        casesPrises[1] = true;
                    }
                }
            } else {
               if(cases[2]!=null){
                   result[2]=obj[0];
                   casesPrises[0]=true;
               }
            }
        } else {
            result[0]=obj[0];
            casesPrises[0]=true;
            if(cases[1]!=null){
                casesPrises[1]=true;
                if(cases[1].getAccessibleText().equals(cases[0].getAccessibleText())){
                    fusionTemp[0] = 1;
                    fusionTemp[1] = 0;
                    result[1]=obj[0];
                    if(cases[2]!=null){
                        result[2]=obj[1];
                    }
                } else {
                    result[1]=obj[1];
                    if(cases[2]!=null){
                        if(cases[2].getAccessibleText().equals(cases[1].getAccessibleText())){
                            fusionTemp[1] = 1;
                            fusionTemp[2] = 0;
                            result[2]=obj[1];
                        } else {
                            result[2]=obj[2];
                            casesPrises[2]=true;
                        }
                    }
                }
            } else {
                if(cases[2]!=null){
                    if(cases[2].getAccessibleText().equals(cases[0].getAccessibleText())){
                        fusionTemp[0] = 2;
                        fusionTemp[2] = 0;
                        result[2]=obj[0];
                    } else{
                        result[2]=obj[1];
                        casesPrises[1]=true;
                    }
                }
            }
        }
        //On remplit les variables globales en fonction de la grille
        switch(iGrille){
            case 1: //SOMMET
                //Et de la direction
                switch(direction){
                    case 1: //HAUT
                        this.objS[iLigne][0]=result[0];
                        this.objS[iLigne][1]=result[1];
                        this.objS[iLigne][2]=result[2];
                        this.casePriseSommet[iLigne][0]=casesPrises[0];
                        this.casePriseSommet[iLigne][1]=casesPrises[1];
                        this.casePriseSommet[iLigne][2]=casesPrises[2];
                        this.fusionS[iLigne][0]=fusionTemp[0];
                        this.fusionS[iLigne][1]=fusionTemp[1];
                        this.fusionS[iLigne][2]=fusionTemp[2];
                        break;
                    case -1: //BAS
                        this.objS[iLigne][0]=result[2];
                        this.objS[iLigne][1]=result[1];
                        this.objS[iLigne][2]=result[0];
                        this.casePriseSommet[iLigne][0]=casesPrises[2];
                        this.casePriseSommet[iLigne][1]=casesPrises[1];
                        this.casePriseSommet[iLigne][2]=casesPrises[0];
                        this.fusionS[iLigne][0]=fusionTemp[2];
                        this.fusionS[iLigne][1]=fusionTemp[1];
                        this.fusionS[iLigne][2]=fusionTemp[0];
                        break;
                    case 2: //DROITE
                        this.objS[0][iLigne]=result[2];
                        this.objS[1][iLigne]=result[1];
                        this.objS[2][iLigne]=result[0];
                        this.casePriseSommet[0][iLigne]=casesPrises[2];
                        this.casePriseSommet[1][iLigne]=casesPrises[1];
                        this.casePriseSommet[2][iLigne]=casesPrises[0];
                        this.fusionS[0][iLigne]=fusionTemp[2];
                        this.fusionS[1][iLigne]=fusionTemp[1];
                        this.fusionS[2][iLigne]=fusionTemp[0];
                        break;
                    case -2: //GAUCHE
                        this.objS[0][iLigne]=result[0];
                        this.objS[1][iLigne]=result[1];
                        this.objS[2][iLigne]=result[2];
                        this.casePriseSommet[0][iLigne]=casesPrises[0];
                        this.casePriseSommet[1][iLigne]=casesPrises[1];
                        this.casePriseSommet[2][iLigne]=casesPrises[2];
                        this.fusionS[0][iLigne]=fusionTemp[0];
                        this.fusionS[1][iLigne]=fusionTemp[1];
                        this.fusionS[2][iLigne]=fusionTemp[2];
                        break;
                }
                break;
            case 2: //MILIEU
                switch(direction){
                    case 1: //HAUT
                        this.objM[iLigne][0]=result[0];
                        this.objM[iLigne][1]=result[1];
                        this.objM[iLigne][2]=result[2];
                        this.casePriseMilieu[iLigne][0]=casesPrises[0];
                        this.casePriseMilieu[iLigne][1]=casesPrises[1];
                        this.casePriseMilieu[iLigne][2]=casesPrises[2];
                        this.fusionM[iLigne][0]=fusionTemp[0];
                        this.fusionM[iLigne][1]=fusionTemp[1];
                        this.fusionM[iLigne][2]=fusionTemp[2];
                        break;
                    case -1: //BAS
                        this.objM[iLigne][0]=result[2];
                        this.objM[iLigne][1]=result[1];
                        this.objM[iLigne][2]=result[0];
                        this.casePriseMilieu[iLigne][0]=casesPrises[2];
                        this.casePriseMilieu[iLigne][1]=casesPrises[1];
                        this.casePriseMilieu[iLigne][2]=casesPrises[0];
                        this.fusionM[iLigne][0]=fusionTemp[2];
                        this.fusionM[iLigne][1]=fusionTemp[1];
                        this.fusionM[iLigne][2]=fusionTemp[0];
                        break;
                    case 2: //DROITE
                        this.objM[0][iLigne]=result[2];
                        this.objM[1][iLigne]=result[1];
                        this.objM[2][iLigne]=result[0];
                        this.casePriseMilieu[0][iLigne]=casesPrises[2];
                        this.casePriseMilieu[1][iLigne]=casesPrises[1];
                        this.casePriseMilieu[2][iLigne]=casesPrises[0];
                        this.fusionM[0][iLigne]=fusionTemp[2];
                        this.fusionM[1][iLigne]=fusionTemp[1];
                        this.fusionM[2][iLigne]=fusionTemp[0];
                        break;
                    case -2: //GAUCHE
                        this.objM[0][iLigne]=result[0];
                        this.objM[1][iLigne]=result[1];
                        this.objM[2][iLigne]=result[2];
                        this.casePriseMilieu[0][iLigne]=casesPrises[0];
                        this.casePriseMilieu[1][iLigne]=casesPrises[1];
                        this.casePriseMilieu[2][iLigne]=casesPrises[2];
                        this.fusionM[0][iLigne]=fusionTemp[0];
                        this.fusionM[1][iLigne]=fusionTemp[1];
                        this.fusionM[2][iLigne]=fusionTemp[2];
                        break;
                }
                break;
            case 3: //BASE
                switch(direction){
                    case 1: //HAUT
                        this.objB[iLigne][0]=result[0];
                        this.objB[iLigne][1]=result[1];
                        this.objB[iLigne][2]=result[2];
                        this.casePriseBase[iLigne][0]=casesPrises[0];
                        this.casePriseBase[iLigne][1]=casesPrises[1];
                        this.casePriseBase[iLigne][2]=casesPrises[2];
                        this.fusionB[iLigne][0]=fusionTemp[0];
                        this.fusionB[iLigne][1]=fusionTemp[1];
                        this.fusionB[iLigne][2]=fusionTemp[2];
                        break;
                    case -1: //BAS
                        this.objB[iLigne][0]=result[2];
                        this.objB[iLigne][1]=result[1];
                        this.objB[iLigne][2]=result[0];
                        this.casePriseBase[iLigne][0]=casesPrises[2];
                        this.casePriseBase[iLigne][1]=casesPrises[1];
                        this.casePriseBase[iLigne][2]=casesPrises[0];
                        this.fusionB[iLigne][0]=fusionTemp[2];
                        this.fusionB[iLigne][1]=fusionTemp[1];
                        this.fusionB[iLigne][2]=fusionTemp[0];
                        break;
                    case 2: //DROITE
                        this.objB[0][iLigne]=result[2];
                        this.objB[1][iLigne]=result[1];
                        this.objB[2][iLigne]=result[0];
                        this.casePriseBase[0][iLigne]=casesPrises[2];
                        this.casePriseBase[1][iLigne]=casesPrises[1];
                        this.casePriseBase[2][iLigne]=casesPrises[0];
                        this.fusionB[0][iLigne]=fusionTemp[2];
                        this.fusionB[1][iLigne]=fusionTemp[1];
                        this.fusionB[2][iLigne]=fusionTemp[0];
                        break;
                    case -2: //GAUCHE
                        this.objB[0][iLigne]=result[0];
                        this.objB[1][iLigne]=result[1];
                        this.objB[2][iLigne]=result[2];
                        this.casePriseBase[0][iLigne]=casesPrises[0];
                        this.casePriseBase[1][iLigne]=casesPrises[1];
                        this.casePriseBase[2][iLigne]=casesPrises[2];
                        this.fusionB[0][iLigne]=fusionTemp[0];
                        this.fusionB[1][iLigne]=fusionTemp[1];
                        this.fusionB[2][iLigne]=fusionTemp[2];
                        break;
                }
                break;
        }
    }

    /**
     * Controller Ajoute une case à la vue
     */
    private void ajoutCase(){
            //On ajoute une case
            this.jeu.ajoutCase();
            //On la cherche pour afficher le pane au bon endroit sur la vue
            for(int i=0;i<3;i++){
                for(int j=0; j<3; j++){
                    //si il y a une case dans la grille Sommet
                    if(this.jeu.getCase(i, j, this.jeu.getGrilleSommet()) != null){
                        //mais pas dans l'interface -> c'est la nouvelle case
                        if(this.casePriseSommet[i][j] == false){
                            this.fond.getChildren().removeAll(this.grilleS[i][j]);  
                            //on l'affiche
                            Case c = this.jeu.getCase(i, j, this.jeu.getGrilleSommet());
                            int x = 113 + c.getX()*116;
                            int y = 386 + c.getY()*116;
                            int valeur = c.getV();
                            Pane p = new Pane();
                            p.setAccessibleText(""+valeur+"");
                            Label l = new Label();
                            l.setMinWidth(114);
                            l.setMinHeight(114);
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
                    }
                    //On procède pareillement dans la grille Milieu
                    if(this.jeu.getCase(i, j, this.jeu.getGrilleMilieu()) != null){
                        if(this.casePriseMilieu[i][j] == false){
                            this.fond.getChildren().removeAll(this.grilleM[i][j]);
                            //on l'affiche
                            Case c = this.jeu.getCase(i, j, this.jeu.getGrilleMilieu());
                            int x = 470 + c.getX()*116;
                            int y = 386 + c.getY()*116;
                            int valeur = c.getV();
                            Pane p = new Pane();
                            p.setAccessibleText(""+valeur+"");
                            Label l = new Label();
                            l.setMinWidth(114);
                            l.setMinHeight(114);
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
                    }
                    //Et dans le grille Base
                    if(this.jeu.getCase(i, j, this.jeu.getGrilleBase()) != null){
                        if(this.casePriseBase[i][j] == false){
                            this.fond.getChildren().removeAll(this.grilleB[i][j]);
                            //on l'affiche
                            Case c = this.jeu.getCase(i, j, this.jeu.getGrilleBase());
                            int x = 826 + c.getX()*116;
                            int y = 386 + c.getY()*116;
                            int valeur = c.getV();
                            Pane p = new Pane();
                            p.setAccessibleText(""+valeur+"");
                            Label l = new Label();
                            l.setMinWidth(114);
                            l.setMinHeight(114);
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
            }
    }

    /**
     * Controller Test les conditions de fin de partie
     * @param vrai
     */
    private void finPartie(boolean vrai){
        //On rend la vue du jeu un peu caché cf css
        Pane fondFin = new Pane();
        fondFin.setPrefHeight(958);
        fondFin.setPrefWidth(1293);
        fondFin.relocate(0, 0);
        fondFin.getStyleClass().add("fondFin"+this.style);
        this.fond.getChildren().add(fondFin);
        //On affiche les message de fin
        Pane fondMessage = new Pane();
        fondMessage.setPrefWidth(597);
        fondMessage.setPrefHeight(458);
        fondMessage.relocate(344,250);
        fondMessage.getStyleClass().add("fondMessage"+this.style);
        this.fond.getChildren().add(fondMessage);
        //En fonction de si le joueur à gagné ou pas
        if(!vrai){
            Label message = new Label("Dommage, vous avez perdu !");
            message.relocate(0, 50);
            message.setPrefWidth(597);
            message.getStyleClass().add("textFin"+this.style);
            fondMessage.getChildren().add(message);
        } else {
            Label message = new Label("Félicitation, vous avez Gagné !");
            message.relocate(0, 50);
            message.setPrefWidth(597);
            message.getStyleClass().add("textFin"+this.style);
            fondMessage.getChildren().add(message);
        }
        Label scoreMess = new Label("Votre score est de " + this.jeu.getScore());
        scoreMess.relocate(0, 126);
        scoreMess.setPrefWidth(597);
        scoreMess.getStyleClass().add("textScoreMeilleureCase"+this.style);
        Label meilleureCase = new Label("Votre meilleure case est "+ this.jeu.getMeilleureCase());
        meilleureCase.relocate(0, 196);
        meilleureCase.setPrefWidth(597);
        meilleureCase.getStyleClass().add("textScoreMeilleureCase"+this.style);
        fondMessage.getChildren().add(scoreMess);
        fondMessage.getChildren().add(meilleureCase);
        //On affiche deux boutons
        Button rejouer = new Button("Nouvelle Partie");
        rejouer.relocate(217, 300);
        rejouer.getStyleClass().add("button"+this.style);
        rejouer.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            this.newPartie();
        });
        Button quit = new Button("Retour au menu");
        quit.relocate(214, 350);
        quit.getStyleClass().add("button"+this.style);
        quit.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            this.menuNonSauvegarde();
        });
        fondMessage.getChildren().add(quit);
        fondMessage.getChildren().add(rejouer);
        fondMessage.getChildren().add(scoreMess);
        fondMessage.getChildren().add(meilleureCase);
        //On enregistre les panes pour pouvoir les suprimer
        this.fondMessageFin = fondFin;
        this.messageFin = fondMessage;
    }

    /**
     * Controller Affiche tous les éléments de la scene
     * @param s
     */
    private void afficher(String s){
      //On supprime l'ancien style sur tous les élèment de la vue
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
      //On aenregistre le nouveau style
        this.style = s;
        this.joueur.setStyle(this.style);
      //On attribue le nouveau style
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
      //On met à jours la grille en changeant l'affichage
        //On retire les cases déjà présentes dans les grilles
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                this.fond.getChildren().removeAll(this.grilleS[i][j]);
                this.fond.getChildren().removeAll(this.grilleM[i][j]);
                this.fond.getChildren().removeAll(this.grilleB[i][j]);
            }
        }
        //On affiche les cases en mettant à jours les variables globales
        int x;
        int y;
        int valeur;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                this.casePriseBase[i][j] = false;
                this.casePriseMilieu[i][j] = false;
                this.casePriseSommet[i][j] = false;
            }
        }
        for(Case c: this.jeu.getGrilleSommet()){
            x = 113 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();
            
            Pane p = new Pane();
            p.setAccessibleText(""+valeur+"");
            Label l = new Label();
            l.setMinWidth(114);
            l.setMinHeight(114);
            p.getStyleClass().add("pane"+valeur+this.style);
            GridPane.setHalignment(l, HPos.CENTER);
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            
            p.setVisible(true);
            l.setVisible(true);
            
            this.grilleS[c.getX()][c.getY()] = p;
            this.casePriseSommet[c.getX()][c.getY()] = true;
        }
        for(Case c: this.jeu.getGrilleMilieu()){
            x = 470 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();           
            Pane p = new Pane();
            p.setAccessibleText(""+valeur+"");
            Label l = new Label();
            l.setMinWidth(114);
            l.setMinHeight(114);
            p.getStyleClass().add("pane"+valeur+this.style);
            GridPane.setHalignment(l, HPos.CENTER);
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            p.setVisible(true);
            l.setVisible(true);
            
            this.grilleM[c.getX()][c.getY()] = p;
            this.casePriseMilieu[c.getX()][c.getY()] = true;
        }
        for(Case c: this.jeu.getGrilleBase()){
            x = 826 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();
            Pane p = new Pane();
            p.setAccessibleText(""+valeur+"");
            Label l = new Label();
            l.setMinWidth(114);
            l.setMinHeight(114);
            p.getStyleClass().add("pane"+valeur+this.style);
            GridPane.setHalignment(l, HPos.CENTER);
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            p.setVisible(true);
            l.setVisible(true);
           
            this.grilleB[c.getX()][c.getY()] = p;
            this.casePriseBase[c.getX()][c.getY()] = true;
        }
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(!this.casePriseSommet[i][j]){
                    this.grilleS[i][j] = null;
                }
                if(!this.casePriseMilieu[i][j]){
                    this.grilleM[i][j] = null;
                }
                if(!this.casePriseBase[i][j]){
                    this.grilleB[i][j] = null;
                }
            }
        }
    }

    /**
     * Controller du menu d'aide des touches
     */
    @FXML
    private void aideTouche(){
        //TRUE si le choix "touche" dans l'onglet aide est selectionné
        aide = !aide;
        //On affiche le message
        if(aide){
            Pane p = new Pane();
            p.getStyleClass().add("fondMessageAide");
            p.setPrefWidth(374);
            p.setPrefHeight(231);
            p.relocate(29, 61);
            Label l = new Label("Touches: ");
            Label l1 = new Label("z pour haut, s pour bas");
            Label l2 = new Label("q pour gauche, d pour droite");
            Label l3 = new Label("r pour sommet, f pour base");
            l.getStyleClass().add("textAide");
            l1.getStyleClass().add("textAide");
            l2.getStyleClass().add("textAide");
            l3.getStyleClass().add("textAide");
            l.setPrefWidth(374);
            l1.setPrefWidth(374);
            l2.setPrefWidth(374);
            l3.setPrefWidth(374);
            l.relocate(0, 20);
            l1.relocate(0, 74);
            l2.relocate(0, 128);
            l3.relocate(0, 182);
            this.fondAide=p;
            this.fond.getChildren().add(this.fondAide);
            this.fondAide.getChildren().add(l);
            this.fondAide.getChildren().add(l1);
            this.fondAide.getChildren().add(l2);
            this.fondAide.getChildren().add(l3);
        //Si on de décoche le choix "touche", on supprime le message
        } else {
            this.fond.getChildren().removeAll(this.fondAide);
        }
    }

    /**
     * Controller relance une partie
     */
    @FXML
    private void newPartie(){
        //On enregistre le nouveau meilleur score
        if(this.jeu.getScore()>this.joueur.getMeilleurScore()){
            this.joueur.setMeilleurScore(this.jeu.getScore());
        }
        //Si le joueur a gagné la partie, on le comptabilise
        if(this.jeu.getMeilleureCase() == 2048){
            this.joueur.setPartiesGagnees(this.joueur.getPartiesGagnees()+1);
        }
        //On retire le message de fin
        this.fond.getChildren().removeAll(this.fondMessageFin);
        this.fond.getChildren().removeAll(this.messageFin);
        //On initialise à nouveau le jeu
        this.jeu = new Grille3D();
        this.jeu.ajoutCase();
        this.jeu.ajoutCase();
        this.afficher(this.style);
        this.score.setText("0");
    }

    /**
     * Controller theme Nuit
     */
    @FXML
    private void styleNuit() {
        this.afficher("Nuit");
    }

    /**
     * Controller theme Classique
     */
    @FXML
    private void styleClassique() {
        this.afficher("Classique");
    }

    /**
     * Controller theme de Noel
     */
    @FXML
    private void styleNoel() {
        this.afficher("Noel");
    }

    /**
     * Controller menu avec sauvegarde
     */
    @FXML
    private void menuSauvegarde(){
        //On enregistre le nouveau meilleur score
        if(this.jeu.getScore()>this.joueur.getMeilleurScore()){
            this.joueur.setMeilleurScore(this.jeu.getScore());
        }
        //On sérialise le joueur
        Bdd.creerFichierJoueur(joueur);
        try {
            Stage stage = new Stage();
            this.fond.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Controller Menu sans sauvegarde
     */
    @FXML
    private void menuNonSauvegarde(){
        //On enregistre le nouveau meilleur score
        if(this.jeu.getScore()>this.joueur.getMeilleurScore()){
            this.joueur.setMeilleurScore(this.jeu.getScore());
        }
        //Si le joueur a gagné la partie, on le comptabilise
        if(this.jeu.getMeilleureCase() == 2048){
            this.joueur.setPartiesGagnees(this.joueur.getPartiesGagnees()+1);
        }
        //On sérialise le joueur
        Bdd.creerFichierJoueur(joueur);
        try {
            Stage stage = new Stage();
            this.fond.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
