/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author chloe
 * @versioon 1.0
 */
public class LancementMain extends Application {
    public static void main(String[] args){
        if(args.length==0){
            Main.main(args);
        } else {
            MainSansGUI.main(args);
        }
    }

    /**
     * Initialise la fenetre pour la vue
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {}
}
