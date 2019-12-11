/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;



public class FXMLDocumentController implements Initializable {
    
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        boolean b;
        this.jeu.ajoutCase();
        this.jeu.ajoutCase();
        this.jeu.ajoutCase();
        
        //On affiche les deux nouvelles Cases
        this.afficher("Classique");
        this.score.setText("0");
    }  
    
    
    //Méthodes pour déplacer les cases avec les boutons
    @FXML
    private void handleButtonHaut() {
        
        //On actualise les grilles de pane
        this.afficher(this.style);
        
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
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
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
        this.score.setText(""+this.jeu.getScore()+"");
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase(v);
        }
    }
    
    @FXML
    private void handleButtonBas() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        
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
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
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
        this.score.setText(""+this.jeu.getScore()+"");
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase(v);
        }
    }
    
    @FXML
    private void handleButtonGauche() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        
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
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
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
        this.score.setText(""+this.jeu.getScore()+"");
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase(v);
        }
    }
    
    @FXML
    private void handleButtonDroite() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        
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
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
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
        this.score.setText(""+this.jeu.getScore()+"");
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase(v);
        }
    }
    
    @FXML
    private void handleButtonSommet() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        
        boolean v = this.jeu.lanceDeplacement(4);
        /*
        //On met à jour les tableaux de objectifs
        this.calculObjectif(4);
        
        ThreadGroup groupe = new ThreadGroup("mon groupe");
        synchronized(groupe){
            for(int i=0;i<3;i++){
                for(int j=0; j<3; j++){
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
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
                                int nValeur = Integer.parseInt(grilleB[a][b].getAccessibleText())*2;
                                switch (fusionB[a][b]) {
                                    case 0:
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;
                                    case 2:
                                        try {
                                            Thread.sleep(3*116*3+9);
                                        } catch (InterruptedException ex) {
                                            System.out.println(ex);
                                        }       
                                        grilleB[a][b].getStyleClass().remove("pane"+grilleB[a][b].getAccessibleText()+style);
                                        grilleB[a][b].getStyleClass().add("pane"+nValeur+style);
                                        break;     
                                    case 3:
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
        }
        */
        
        this.afficher(this.style);
        this.score.setText(""+this.jeu.getScore()+"");
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase(v);
        }
    }
    
    @FXML
    private void handleButtonBase() {
        //On actualise les grilles de pane
        this.afficher(this.style);
        
        boolean v = this.jeu.lanceDeplacement(-4);
        /*
        //On met à jour les tableaux de objectifs
        this.calculObjectif(-4);
        
        ThreadGroup groupe = new ThreadGroup("mon groupe");
        synchronized(groupe){
            for(int i=0;i<3;i++){
                for(int j=0; j<3; j++){
                    this.aTemp =i;
                    this.bTemp=j;
                    //Grille SOMMET
                    if(this.grilleS[i][j] != null){
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
        this.score.setText(""+this.jeu.getScore()+"");
        if(this.jeu.getMeilleureCase() == 2048){
            this.finPartie(true);
        } else if(this.jeu.jeuFini()){
            this.finPartie(false);
        } else {
            this.ajoutCase(v);
        }
    }
    
    //Méthode pour déplacer les cases avec les touches
    @FXML
    public void keyPressed(KeyEvent ke) {
        String touche = ke.getText();
        System.out.println(ke);
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
    
    public void calculObjectif(int iGrille, int iLigne, int direction){
        boolean[] casesPrises = new boolean[3]; //grille temporaire pour savoir quelles sont les cases libres
        int[] fusionTemp = new int[3];
        //on initialise la grille à false
        for(int i=0;i<3;i++){
            casesPrises[i] = false;
            fusionTemp[i] = -1;
        }
        //on établit la ligne ou la colonne que l'on doit déplacer
        Pane[] cases = new Pane[3];
        int[] obj = new int[3];
        int[] result = new int[3];
        switch(direction){
            case 1: // HAUT
                obj[0] = 386;
                obj[1] = 502;
                obj[2] = 618;
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
        //On remplit les variables globales
        switch(iGrille){
            case 1: //SOMMET
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
    
    public void calculObjectif(int d){
        
        final int xSommet = 112;
        final int xMilieu = 469;
        final int xBase = 825;
        boolean[] casePriseTemp = new boolean[3];
        int[] fusionTemp = new int[3];
        
        //on initialise les grilles temporaires
        for(int i=0;i<3;i++){
            casePriseTemp[i] = false;
            fusionTemp[i] = -1;
        }
        
        //on remplit le tableau temporaires des objectifs, celui de fusions et celui des cases disponibles
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                int[] result = new int[3];
                Pane[] ligne = new Pane[3];
                ligne[0] = this.grilleS[i][j];
                ligne[1] = this.grilleM[i][j];
                ligne[2] = this.grilleB[i][j];
        
                if(d == 4){ //SOMMET
                    
                    //On vérifie tous les cas possible
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
    
    public void ajoutCase(boolean vrai){
        if(vrai){
        this.jeu.ajoutCase();
        
        for(int i=0;i<3;i++){
            for(int j=0; j<3; j++){
                //si il y a une case dans le jeu console
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
                if(this.jeu.getCase(i, j, this.jeu.getGrilleMilieu()) != null){
                    //mais pas dans l'interface -> c'est la nouvelle case
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
                if(this.jeu.getCase(i, j, this.jeu.getGrilleBase()) != null){
                    //mais pas dans l'interface -> c'est la nouvelle case
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
        }}
        System.out.println(this.jeu);
    }
    
    public void finPartie(boolean vrai){
        //On rend la bue du plateau de jeu opaque
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
        
        Button rejouer = new Button("Nouvelle Partie");
        rejouer.relocate(200, 300);
        rejouer.getStyleClass().add("button"+this.style);
        rejouer.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            newPartie();
        });
        
        Button quit = new Button("Retour au menu");
        quit.relocate(197, 350);
        quit.getStyleClass().add("button"+this.style);
        
        this.fond.getChildren().add(fondMessage);
        fondMessage.getChildren().add(quit);
        fondMessage.getChildren().add(rejouer);
        fondMessage.getChildren().add(scoreMess);
        fondMessage.getChildren().add(meilleureCase);
        
        this.fondMessageFin = fondFin;
        this.messageFin = fondMessage;
    }
    
    //Méthode pour charger une nouvelle partie
    @FXML
    private void newPartie(){
        
        this.fond.getChildren().removeAll(this.fondMessageFin);
        this.fond.getChildren().removeAll(this.messageFin);
        this.jeu = new Grille3D();
        
        boolean b;
        this.jeu.ajoutCase();
        this.jeu.ajoutCase();
        
        //On affiche les deux nouvelles Cases
        this.afficher("Classique");
        this.score.setText("0");
    }
    
    private void afficher(String s){
        
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
        
        //On attribue les nouveaux styles
        this.style = s;
        
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
    
    //Méthodes pour changer de style
    @FXML
    private void styleNuit() {
        this.afficher("Nuit");
    }
    @FXML
    private void styleClassique() {
        this.afficher("Classique");
    }
}
