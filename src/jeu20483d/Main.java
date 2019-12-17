/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author chloe
 */
public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLConnexion.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }


    //@param args the command line arguments **/

    public static void main(String[] args) { launch(args);
        /**Joueur j = new Joueur("max","b");
        Grille3D g = new Grille3D();
        Partie p = new Partie(j,g);
        IO.creerFichierPartie(p);
        p = IO.lecFichierPartie();
        System.out.println(p.getJoueur1().getPseudo());

        IO.testPort();**/
    }
    
}
