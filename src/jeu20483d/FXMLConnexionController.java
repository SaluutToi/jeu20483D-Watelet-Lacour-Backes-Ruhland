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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 *
 * @author emili
 */
public class FXMLConnexionController implements Initializable {
    
    @FXML
    private AnchorPane fondConnexion;
    @FXML
    private Button buttonC;
    @FXML
    private Button buttonI;
    @FXML
    private TextField textMailC;
    @FXML
    private TextField textMdpC;
    @FXML
    private TextField textMailI;
    @FXML
    private TextField textMdpI;
    @FXML
    private TextField textPseudo;
    @FXML
    private TextField textConfirmerMdp;
    
    private Bdd bdd;
    private Joueur j;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = new Bdd();
        this.fondConnexion.getStyleClass().add("fondConnexion");
        this.buttonC.getStyleClass().add("buttonConnexion");
        this.buttonI.getStyleClass().add("buttonConnexion");
    } 

    @FXML
    private void handleButtonC(ActionEvent event) {
        if (bdd.connexion(textMailC.getText(), textMdpC.getText())){
            j = new Joueur()
            try {
                Stage stage = (Stage) buttonC.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    @FXML
    private void handleButtonI(ActionEvent event){
        //TOD0: inscire le joueur dans la bdd
        
            if(bdd.ajouter(textMailI.getText(), textPseudo.getText(), textMdpI.getText()))
            {
                try {
            Stage stage = (Stage) buttonC.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
                }
            }
            
}
