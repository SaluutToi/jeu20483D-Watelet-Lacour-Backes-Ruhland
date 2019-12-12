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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 *
 * @author emili
 */
public class ConnexionController /*implements Initializable*/ {
    
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
    
    Bdd bdd = new Bdd();

    @FXML
    private void handleButtonC(ActionEvent event) {
        if (bdd.connexion(textMailC.getText(), textMdpC.getText()))
        {
            
            try {
                Stage stage = (Stage) buttonC.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(ConnexionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
    }
   
    
    
}
