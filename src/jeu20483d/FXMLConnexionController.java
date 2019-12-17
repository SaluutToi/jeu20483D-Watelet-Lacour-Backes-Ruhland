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
import javafx.scene.control.Alert;
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

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = new Bdd();
        this.fondConnexion.getStyleClass().add("fondConnexion");
        this.buttonC.getStyleClass().add("buttonConnexion");
        this.buttonI.getStyleClass().add("buttonConnexion");
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleButtonC(ActionEvent event) {
        if (textMailC.getText().length()==0 || textMailC.getText().length()==0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous n'avez pas rempli tous les champs.");
            alert.setContentText("Vous avez une autre chance.");

            alert.showAndWait();
        }
        else if (bdd.connexion(textMailC.getText(), textMdpC.getText())){
            String pseudo = bdd.getPseudo(textMailC.getText());
            int score = bdd.getScoreJoueur(textMailC.getText());
            int partieG = bdd.getNbParties(textMailC.getText());
            String style = bdd.getStyle(textMailC.getText());
            Joueur joueurCo = new Joueur(pseudo, textMdpC.getText(), textMailC.getText());
            joueurCo.setMeilleurScore(score);
            joueurCo.setPartiesGagnees(partieG);
            joueurCo.setStyle(style);
            Bdd.creerFichierJoueur(joueurCo);
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
        else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Identifiants incorrects.");
                alert.setContentText("Vous avez une autre chance.");

                alert.showAndWait();
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    private void handleButtonI(ActionEvent event){
        if (textMailI.getText().length()==0 || textPseudo.getText().length()==0 || textMdpI.getText().length()==0 || textConfirmerMdp.getText().length()==0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Vous n'avez pas rempli tous les champs.");
            alert.setContentText("Vous avez une autre chance.");

            alert.showAndWait();
        }
        else if (textMdpI.getText()!=textConfirmerMdp.getText())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Les mots de passe ne corresponent pas.");
            alert.setContentText("Vous avez une autre chance.");

            alert.showAndWait();
        }
        else if(bdd.ajouter(textMailI.getText(), textPseudo.getText(), textMdpI.getText()))
            {
            Joueur jCo = new Joueur(textPseudo.getText(), textMdpI.getText(), textMailI.getText());
            Bdd.creerFichierJoueur(jCo);
            try {
                Stage stage = (Stage) buttonC.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
}
