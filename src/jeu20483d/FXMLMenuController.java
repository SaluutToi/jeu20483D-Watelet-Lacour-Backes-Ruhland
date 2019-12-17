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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author chloe
 */
public class FXMLMenuController implements Initializable {
    
    @FXML
    private AnchorPane fond;
    @FXML
    private Pane fondMessage;
    @FXML
    private Label titre;
    @FXML
    private Label text;
    @FXML
    private Button jouer;
    @FXML
    private Button rejouer;
    @FXML
    private Button deco;
    @FXML
    private Button quitter;
    @FXML
    private Button classement;
    
    private Bdd bdd;
    private Joueur joueur;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fond.getStyleClass().add("fondMenu");
        this.fondMessage.getStyleClass().add("fondMenuMessage");
        this.jouer.getStyleClass().add("buttonMenu");
        this.rejouer.getStyleClass().add("buttonMenu");
        this.deco.getStyleClass().add("buttonMenu");
        this.quitter.getStyleClass().add("buttonMenu");
        this.classement.getStyleClass().add("buttonMenu");
        this.bdd = new Bdd();
        this.joueur = Bdd.lecFichierJoueur();
    }
    
    @FXML
    private void handleButtonJouer(){
        try {
            Stage stage = new Stage();
            this.fond.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLJeu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleButtonRejouer(){
        //TODO: désérialisation de la partie
    }
    
    @FXML
    private void handleButtonDeco(){
        //Enregistrement dans la bdd des modificatons de la classe joueur effectuées au cours du jeu
        this.bdd.updateJoueur(this.joueur);
        try {
            Stage stage = new Stage();
            this.fond.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLConnexion.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handleButtonQuitter(){
        //Enregistrement dans la bdd des modificatons de la classe joueur effectuées au cours du jeu
        this.bdd.updateJoueur(this.joueur);
        //On quitte le jeu
        System.exit(0);
    }
    
    @FXML
    private void handleButtonClassement(){
        
    }
}
