/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
    private final Pane[][] grilleS = new Pane[3][3];
    private final Pane[][] grilleM = new Pane[3][3];
    private final Pane[][] grilleB = new Pane[3][3];
    private final int[][] tabObjS = new int[3][3];
    private final int[][] tabObjM = new int[3][3];
    private final int[][] tabObjB = new int[3][3];
    private int xTemp; private int yTemp; private int aTemp; private int bTemp;
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
    }  
    
    
    //Méthodes pour déplacer les cases
    
    @FXML
    private void handleButtonHaut(ActionEvent event) {
        
        //On actualise les grilles de pane
        this.afficherStyle(this.style);
        
        this.jeu.lanceDeplacement(1);
        //On met à jour les tableaux de objectifs
        for(int i=0; i<3;i++){
            int [] objS = this.calculObjectif(1, this.grilleS, i);
            int [] objM = this.calculObjectif(1, this.grilleM, i);
            int [] objB = this.calculObjectif(1, this.grilleB, i);
                
            for(int j=0;j<3;j++){
                    this.tabObjS[i][j] = objS[j];
                    this.tabObjM[i][j] = objM[j];
                    this.tabObjB[i][j] = objB[j];
            }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("S "+a+" "+b+" obj"+ tabObjS[a][b]);
                                while (y > tabObjS[a][b]) {
                                    y -= 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleS[a][b].relocate(x, y);
                                            grilleS[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("M "+a+" "+b+" obj"+ tabObjM[a][b]);
                                while (y > tabObjM[a][b]) {
                                    y -= 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleM[a][b].relocate(x, y); 
                                            grilleM[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("B "+a+" "+b+" obj"+ tabObjB[a][b]);
                                while (y > tabObjB[a][b]) {
                                    y -= 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleB[a][b].relocate(x, y);
                                            grilleB[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
    }
    
    @FXML
    private void handleButtonBas(ActionEvent event) {
        //On actualise les grilles de pane
        this.afficherStyle(this.style);
        
        this.jeu.lanceDeplacement(-1);
        //On met à jour les tableaux de objectifs
        for(int i=0; i<3;i++){
            int [] objS = this.calculObjectif(-1, this.grilleS, i);
            int [] objM = this.calculObjectif(-1, this.grilleM, i);
            int [] objB = this.calculObjectif(-1, this.grilleB, i);
                
            for(int j=0;j<3;j++){
                    this.tabObjS[i][j] = objS[j];
                    this.tabObjM[i][j] = objM[j];
                    this.tabObjB[i][j] = objB[j];
            }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("S "+a+" "+b+" obj"+ tabObjS[a][b]);
                                while (y < tabObjS[a][b]) {
                                    y += 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleS[a][b].relocate(x, y);
                                            grilleS[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("M "+a+" "+b+" obj"+ tabObjM[a][b]);
                                while (y < tabObjM[a][b]) {
                                    y += 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleM[a][b].relocate(x, y); 
                                            grilleM[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("B "+a+" "+b+" obj"+ tabObjB[a][b]);
                                while (y < tabObjB[a][b]) {
                                    y += 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleB[a][b].relocate(x, y);
                                            grilleB[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
    }
    
    @FXML
    private void handleButtonGauche(ActionEvent event) {
        //On actualise les grilles de pane
        this.afficherStyle(this.style);
        
        this.jeu.lanceDeplacement(-2);
        //On met à jour les tableaux de objectifs
        for(int j=0; j<3;j++){
            int [] objS = this.calculObjectif(-2, this.grilleS, j);
            int [] objM = this.calculObjectif(-2, this.grilleM, j);
            int [] objB = this.calculObjectif(-2, this.grilleB, j);
                
            for(int i=0;i<3;i++){
                    this.tabObjS[i][j] = objS[i];
                    this.tabObjM[i][j] = objM[i];
                    this.tabObjB[i][j] = objB[i];
            }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("S "+a+" "+b+" obj"+ tabObjS[a][b]);
                                while (x > tabObjS[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleS[a][b].relocate(x, y);
                                            grilleS[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("M "+a+" "+b+" obj"+ tabObjM[a][b]);
                                while (x > tabObjM[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleM[a][b].relocate(x, y); 
                                            grilleM[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("B "+a+" "+b+" obj"+ tabObjB[a][b]);
                                while (x > tabObjB[a][b]) {
                                    x -= 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleB[a][b].relocate(x, y);
                                            grilleB[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
    }
    
    @FXML
    private void handleButtonDroite(ActionEvent event) throws InterruptedException {
        //On actualise les grilles de pane
        this.afficherStyle(this.style);
        
        this.jeu.lanceDeplacement(2);
        
        //On met à jour les tableaux de objectifs
        for(int j=0; j<3;j++){
            int [] objS = this.calculObjectif(2, this.grilleS, j);
            int [] objM = this.calculObjectif(2, this.grilleM, j);
            int [] objB = this.calculObjectif(2, this.grilleB, j);
                
            for(int i=0;i<3;i++){
                    this.tabObjS[i][j] = objS[i];
                    this.tabObjM[i][j] = objM[i];
                    this.tabObjB[i][j] = objB[i];
            }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("S "+a+" "+b+" obj"+ tabObjS[a][b]);
                                while (x < tabObjS[a][b]) {
                                    x += 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleS[a][b].relocate(x, y);
                                            grilleS[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("M "+a+" "+b+" obj"+ tabObjM[a][b]);
                                while (x < tabObjM[a][b]) {
                                    x += 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleM[a][b].relocate(x, y); 
                                            grilleM[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("B "+a+" "+b+" obj"+ tabObjB[a][b]);
                                while (x < tabObjB[a][b]) {
                                    x += 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleB[a][b].relocate(x, y);
                                            grilleB[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
    }
    
    @FXML
    private void handleButtonSommet(ActionEvent event) {
        //On actualise les grilles de pane
        this.afficherStyle(this.style);
        
        this.jeu.lanceDeplacement(4);
        
        //On met à jour les tableaux de objectifs
        for(int j=0; j<3;j++){
            for(int i=0;i<3;i++){
                int [] obj = this.calculObjectif(i, j, 4);
                this.tabObjS[i][j] = obj[0];
                this.tabObjM[i][j] = obj[1];
                this.tabObjB[i][j] = obj[2];
            }
        }
        
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("S "+a+" "+b+" obj"+ tabObjS[a][b]);
                                while (x > tabObjS[a][b]) {
                                    x -= 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleS[a][b].relocate(x, y);
                                            grilleS[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("M "+a+" "+b+" obj"+ tabObjM[a][b]);
                                while (x > tabObjM[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleM[a][b].relocate(x, y); 
                                            grilleM[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("B "+a+" "+b+" obj"+ tabObjB[a][b]);
                                while (x > tabObjB[a][b]) {
                                    x -= 1; 
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleB[a][b].relocate(x, y); 
                                            grilleB[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
    }
    
    @FXML
    private void handleButtonBase(ActionEvent event) {
        //On actualise les grilles de pane
        this.afficherStyle(this.style);
        
        this.jeu.lanceDeplacement(-4);
        //On met à jour les tableaux de objectifs
        for(int j=0; j<3;j++){
            for(int i=0;i<3;i++){
                int [] obj = this.calculObjectif(i, j, -4);
                this.tabObjS[i][j] = obj[0];
                this.tabObjM[i][j] = obj[1];
                this.tabObjB[i][j] = obj[2];
            }
        }
        
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("S "+a+" "+b+" obj"+ tabObjS[a][b]);
                                while (x < tabObjS[a][b]) {
                                    x += 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleS[a][b].relocate(x, y); 
                                            grilleS[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("M "+a+" "+b+" obj"+ tabObjM[a][b]);
                                while (x < tabObjM[a][b]) {
                                    x += 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleM[a][b].relocate(x, y);
                                            grilleM[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
                            private int a=aTemp;
                            private int b=bTemp;
                            private int x=xTemp;
                            private int y=yTemp;
                            @Override
                            public Void call() throws Exception { // implémentation de la méthode protected abstract V call() dans la classe Task
                                System.out.println("B "+a+" "+b+" obj"+ tabObjB[a][b]);
                                while (x < tabObjB[a][b]) {
                                    x += 1;
                                    Platform.runLater(new Runnable() { // classe anonyme
                                        @Override
                                        public void run() {
                                            grilleB[a][b].relocate(x, y); 
                                            grilleB[a][b].setVisible(true);    
                                        }
                                    });
                                    try {
                                        Thread.sleep(3);
                                    } catch (InterruptedException ex) {
                        
                                    }
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
    }
    
    public int[] calculObjectif(int direction, Pane[][] p, int ligne){
        int[] result = new int[3];
        Pane[] pLigne;
        switch (direction){
                case 1: //Vers le haut
                    pLigne = new Pane[3];
                    for(int i=0;i<3;i++){
                        pLigne[i] = p[ligne][i];
                    }
                    if (pLigne[0] == null){
                        if(pLigne[1] != null){
                            result[1] = (int) (pLigne[1].getLayoutY() - 116);
                            if(pLigne[2] != null){
                                if(pLigne[2].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                    result[2] = (int) (pLigne[2].getLayoutY() - 2*116);
                                } else {
                                    result[2] = (int) (pLigne[2].getLayoutY() - 116);
                                }
                            }
                        } else {
                            if(pLigne[2] != null){
                                result[2] = (int) (pLigne[2].getLayoutY() - 2*116);
                            }
                        }
                    } else {
                        result[0] = (int) pLigne[0].getLayoutY();
                        if(pLigne[1] != null){
                            result[1] = (int) pLigne[1].getLayoutY();
                            if(pLigne[1].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                                result[1] =- 116;
                                if(pLigne[2] != null){
                                    result[2] = (int) pLigne[2].getLayoutY() - 116;
                                }
                            } else {
                                if(pLigne[2] != null){
                                    result[2] = (int) pLigne[2].getLayoutY();
                                    if(pLigne[2].getAccessibleText().equals(pLigne[2].getAccessibleText())){
                                        result[2] =- 116;
                                    }
                                }
                            }
                        } else {
                            if(pLigne[2] != null){
                                if(pLigne[2].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                                    result[2] = (int) pLigne[0].getLayoutY() - 2*116;
                                } else {
                                    result[2] = (int) pLigne[2].getLayoutY() - 116;
                                }
                            }
                        }
                    }
                    break;
                case 2: //vers la droite
                    pLigne = new Pane[3];
                    for(int i=0;i<3;i++){
                        pLigne[i] = p[i][ligne];
                    }
                    if (pLigne[2] == null){
                        if(pLigne[1] != null){
                            result[1] = (int) (pLigne[1].getLayoutX() + 116);
                            if(pLigne[0] != null){
                                if(pLigne[0].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                    result[0] = (int) (pLigne[0].getLayoutX() + 2*116);
                                } else {
                                    result[0] = (int) (pLigne[0].getLayoutX() + 116);
                                }
                            }
                        } else {
                            if(pLigne[0] != null){
                                result[0] = (int) (pLigne[0].getLayoutX() + 2*116);
                            }
                        }
                    } else {
                        result[2] = (int) pLigne[2].getLayoutX();
                        if(pLigne[1] != null){
                            result[1] = (int) pLigne[1].getLayoutX();
                            if(pLigne[1].getAccessibleText().equals(pLigne[2].getAccessibleText())){
                                result[1] =+ 116;
                                if(pLigne[0] != null){
                                    result[0] = (int) pLigne[0].getLayoutX() + 116;
                                }
                            } else {
                                if(pLigne[0] != null){
                                    result[0] = (int) pLigne[0].getLayoutX();
                                    if(pLigne[0].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                        result[0] =+ 116;
                                    }
                                }
                            }
                        } else {
                            if(pLigne[0] != null){
                                if(pLigne[0].getAccessibleText().equals(pLigne[2].getAccessibleText())){
                                    result[0] = (int) pLigne[0].getLayoutX() + 2*116;
                                } else {
                                    result[0] = (int) pLigne[0].getLayoutX() + 116;
                                }
                            }
                        }
                    }
                    break;
                case -1: //vers le bas
                    pLigne = new Pane[3];
                    for(int i=0;i<3;i++){
                        pLigne[i] = p[ligne][i];
                    }
                    if (pLigne[2] == null){
                        if(pLigne[1] != null){
                            result[1] = (int) (pLigne[1].getLayoutY() + 116);
                            if(pLigne[0] != null){
                                if(pLigne[0].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                    result[0] = (int) (pLigne[0].getLayoutY() + 2*116);
                                } else {
                                    result[0] = (int) (pLigne[0].getLayoutY() + 116);
                                }
                            }
                        } else {
                            if(pLigne[0] != null){
                                result[0] = (int) (pLigne[0].getLayoutY() + 2*116);
                            }
                        }
                    } else {
                        result[2] = (int) pLigne[2].getLayoutY();
                        if(pLigne[1] != null){
                            result[1] = (int) pLigne[1].getLayoutY();
                            if(pLigne[1].getAccessibleText().equals(pLigne[2].getAccessibleText())){
                                result[1] =+ 116;
                                if(pLigne[0] != null){
                                    result[0] = (int) pLigne[0].getLayoutY() + 116;
                                }
                            } else {
                                if(pLigne[0] != null){
                                    result[0] = (int) pLigne[0].getLayoutY();
                                    if(pLigne[0].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                        result[0] =+ 116;
                                    }
                                }
                            }
                        } else {
                            if(pLigne[0] != null){
                                if(pLigne[0].getAccessibleText().equals(pLigne[2].getAccessibleText())){
                                    result[0] = (int) pLigne[0].getLayoutY() + 2*116;
                                } else {
                                    result[0] = (int) pLigne[0].getLayoutY() + 116;
                                }
                            }
                        }
                    }
                    break;
                case -2: //vers la gauche
                    pLigne = new Pane[3];
                    for(int i=0;i<3;i++){
                        pLigne[i] = p[i][ligne];
                    }
                    if (pLigne[0] == null){
                        if(pLigne[1] != null){
                            result[1] = (int) (pLigne[1].getLayoutX() - 116);
                            if(pLigne[2] != null){
                                if(pLigne[2].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                    result[2] = (int) (pLigne[2].getLayoutX() - 2*116);
                                } else {
                                    result[2] = (int) (pLigne[2].getLayoutX() - 116);
                                }
                            }
                        } else {
                            if(pLigne[2] != null){
                                result[2] = (int) (pLigne[2].getLayoutX() - 2*116);
                            }
                        }
                    } else {
                        result[0] = (int) pLigne[0].getLayoutX();
                        if(pLigne[1] != null){
                            result[1] = (int) pLigne[1].getLayoutX();
                            if(pLigne[1].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                                result[1] =- 116;
                                if(pLigne[2] != null){
                                    result[2] = (int) pLigne[2].getLayoutX() - 116;
                                }
                            } else {
                                if(pLigne[2] != null){
                                    result[2] = (int) pLigne[2].getLayoutX();
                                    if(pLigne[2].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                        result[2] =- 116;
                                    }
                                }
                            }
                        } else {
                            if(pLigne[2] != null){
                                if(pLigne[2].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                                    result[2] = (int) pLigne[2].getLayoutX() - 2*116;
                                } else {
                                    result[2] = (int) pLigne[2].getLayoutX() - 116;
                                }
                            }
                        }
                    }
                    break;    
        }
        for(int i=0; i<3; i++){
            System.out.println("ligne"+ligne+" obj "+i+":"+result[i]);
        }
        return result;
    }
    
    
    public int[] calculObjectif(int i, int j, int d){
        int[] result = new int[3];
        int xSommet = 112;
        int xMilieu = 469;
        int xBase = 825;
        Pane[] pLigne = new Pane[3];
        pLigne[0] = this.grilleS[i][j];
        pLigne[1] = this.grilleM[i][j];
        pLigne[2] = this.grilleB[i][j];
        
        if(d == 4){ //sommet
            if(pLigne[0] == null){
                if(pLigne[1] != null){
                    result[1] = xSommet + 116*i;
                    if(pLigne[2] != null){
                        if(pLigne[2].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                            result[2] = xSommet + 116*i;
                        } else {
                            result[2] = xMilieu + 116*i;
                        }
                    }
                } else {
                    if(pLigne[2] != null){
                        result[2] = xSommet + 116*i;
                    }
                }
            } else {
                result[0] = xSommet + 116*i;
                if(pLigne[1] != null){
                    result[1] = xMilieu + 116*i;
                    if(pLigne[1].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                        result[1] = xSommet + 116*i;
                        if(pLigne[2] != null){
                            result[2] = xMilieu + 116*i;
                        }
                    } else {
                        if(pLigne[2] != null){
                            result[2] = xBase + 116*i;
                            if(pLigne[2].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                result[2] = xMilieu + 116*i;
                            }
                        }
                    }
                } else {
                    if(pLigne[2] != null){
                        if(pLigne[2].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                            result[2] = xSommet + 116*i;
                        } else {
                            result[2] = xMilieu + 116*i;
                        }
                    }
                }
            }
        } else if(d == -4){ //base
            if(pLigne[2] == null){
                if(pLigne[1] != null){
                    result[1] = xBase + 116*i;
                    if(pLigne[0] != null){
                        if(pLigne[0].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                            result[0] = xBase + 116*i;
                        } else {
                            result[0] = xMilieu + 116*i;
                        }
                    }
                } else {
                    if(pLigne[0] != null){
                        result[0] = xBase + 116*i;
                    }
                }
            } else {
                result[2] = xBase + 116*i;
                if(pLigne[1] != null){
                    result[1] = xMilieu + 116*i;
                    if(pLigne[1].getAccessibleText().equals(pLigne[2].getAccessibleText())){
                        result[1] = xBase + 116*i;
                        if(pLigne[0] != null){
                            result[0] = xMilieu + 116*i;
                        }
                    } else {
                        if(pLigne[0] != null){
                            result[0] = xSommet + 116*i;
                            if(pLigne[0].getAccessibleText().equals(pLigne[1].getAccessibleText())){
                                result[0] = xMilieu + 116*i;
                            }
                        }
                    }
                } else {
                    if(pLigne[0] != null){
                        if(pLigne[2].getAccessibleText().equals(pLigne[0].getAccessibleText())){
                            result[0] = xBase + 116*i;
                        } else {
                            result[0] = xMilieu + 116*i;
                        }
                    }
                }
            }
        }
        return result;
    }
    
    
    //Méthode pour charger une nouvelle partie
    
    @FXML
    private void newPartie(ActionEvent event){
        
        this.jeu = new Grille3D();
        
        boolean b;
        b = this.jeu.ajoutCase();
        b = this.jeu.ajoutCase();
        
        //On affiche les deux nouvelles Cases
        this.afficherStyle("Classique");
        this.score.setText("0");
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
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                this.fond.getChildren().removeAll(this.grilleS[i][j]);
                this.fond.getChildren().removeAll(this.grilleM[i][j]);
                this.fond.getChildren().removeAll(this.grilleB[i][j]);
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
