/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;


/**
 *
 * @author chloe
 */
public class deplacePane extends Thread {
    private int obj;
    private int x;
    private int y;
    private int d;
    private Pane p;
    
    public deplacePane(int d, int x, int y, int o, Pane p){
        this.d = d;
        this.obj = o;
        this.x = x;
        this.y = y;
        this.p = p;
    }
    
    @Override
    public void run(){
        if(this.d == 2){ //droite
            while(this.x < this.obj){
                this.x++;
                p.relocate(x, y); 
                try {
                    sleep(3);
                } catch (InterruptedException ex) {
                    Logger.getLogger(deplacePane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else if(this.d == -1){ //bas
            while(this.y < this.obj){
                this.y++;
                p.relocate(x, y);
            }
        }
        
    }
}
