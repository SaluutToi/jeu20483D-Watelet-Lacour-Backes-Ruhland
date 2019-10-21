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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author chloe
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane fond;
    @FXML
    private Label score;
    
    //variables globales non définies dans la vue
    private Grille3D jeu = new Grille3D();
    private final Pane pp = new Pane();
    private final Label ll = new Label("fuck");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        boolean b;
        b = this.jeu.ajoutCase();
        b = this.jeu.ajoutCase();
        
        //On affiche les deux nouvelles Cases
        this.afficher();
        this.score.setText("0");
        System.out.print(jeu);
    }  
    
    @FXML
    private void handleButtonHaut(ActionEvent event) {
        System.out.println("You clicked haut!");
    }
    
    @FXML
    private void handleButtonBas(ActionEvent event) {
        System.out.println("You clicked bas!");
    }
    
    @FXML
    private void handleButtonGauche(ActionEvent event) {
        System.out.println("You clicked gauche!");
    }
    
    @FXML
    private void handleButtonDroite(ActionEvent event) {
        System.out.println("You clicked droite!");
    }
    
    @FXML
    private void handleButtonSommet(ActionEvent event) {
        System.out.println("You clicked sommet!");
    }
    
    @FXML
    private void handleButtonBase(ActionEvent event) {
        System.out.println("You clicked base!");
    }
    
    private void afficher(){
        
        int x;
        int y;
        int valeur;
        
        for(Case c: this.jeu.getGrilleSommet()){
            x = 112 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();
            Pane p = new Pane();
            Label l = new Label(""+valeur+"");
            l.getStyleClass().add("tuile");
            p.getStyleClass().add("pane");
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            
            p.setVisible(true);
            l.setVisible(true);
            System.out.println(l.getStyleClass());
        }
        
        for(Case c: this.jeu.getGrilleMilieu()){
            x = 469 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();           
            Pane p = new Pane();
            Label l = new Label(""+valeur+"");
            l.getStyleClass().add("");
            p.getStyleClass().add("");
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            p.setVisible(true);
            l.setVisible(true);
            System.out.println(l.getStyleClass());
            
        }
        
        for(Case c: this.jeu.getGrilleBase()){
            x = 825 + c.getX()*116;
            y = 386 + c.getY()*116;
            valeur = c.getV();
            Pane p = new Pane();
            Label l = new Label(""+valeur+"");
            l.getStyleClass().add("tuile");
            p.getStyleClass().add("pane");
            this.fond.getChildren().add(p);
            p.getChildren().add(l);
            p.setLayoutX(x);
            p.setLayoutY(y);
            p.setVisible(true);
            l.setVisible(true);
            System.out.println(l.getStyleClass());
            
        }
    }
}
