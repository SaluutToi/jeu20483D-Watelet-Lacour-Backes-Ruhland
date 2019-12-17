/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author emili
 */
public class FXMLScoreController implements Initializable {
    
    @FXML
    private AnchorPane fondScore;
    @FXML
    private Button buttonRetour;
    
    private Bdd bdd;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bdd = new Bdd();
        this.fondScore.getStyleClass().add("fondScore");
        this.buttonRetour.getStyleClass().add("buttonScore");
        this.afficherScore();
      
    }
    @FXML
    private void afficherScore()
    {
        int layoutY = 280;
        int layoutX = 448;
        ArrayList<String> listPseudo = bdd.getPseudosClassement();
        ArrayList<Integer> listScore = bdd.getScoresClassement();
        for(int i=0;i<listPseudo.size();i++){
            String sco = listPseudo.get(i)+" : "+listScore.get(i)+" points";
            Label s = new Label(sco);
            s.setPrefWidth(400);
            s.setLayoutX(layoutX);
            s.setLayoutY(layoutY);
            s.getStyleClass().add("textScore");
            this.fondScore.getChildren().add(s);
            layoutY = layoutY + 40;
        }
    }
    
    @FXML
    private void handleButtonR(ActionEvent event) {
        try {
            Stage stage = new Stage();
            this.fondScore.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLConnexionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
