/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu20483d;

import static java.lang.Thread.sleep;
import javafx.scene.layout.Pane;


/**
 *
 * @author chloe
 */
public class deplacePane implements Runnable {
    private final int obj;
    private int x;
    private int y;
    private final int d;
    private final Pane p;
    
    public deplacePane(int d, int x, int y, int o, Pane p){
        this.d = d;
        this.obj = o;
        this.x = x;
        this.y = y;
        this.p = p;
    }
    
    @Override
    public void run(){
        switch (this.d) {
            case 2:
                //droite
                while(this.x < this.obj){
                    this.x++;
                    p.relocate(x, y);
                    try {
                        sleep(3);
                    } catch (InterruptedException ex) {
                        
                    }
                }  
                break;
            case -1:
                //bas
                while(this.y < this.obj){
                    this.y++;
                    p.relocate(x, y);
                    try {
                        sleep(3);
                    } catch (InterruptedException ex) {
                        
                    }
                }   
                break;
            case 1:
                //haut
                while(this.y > this.obj){
                    this.y--;
                    p.relocate(x, y);
                    try {
                        sleep(3);
                    } catch (InterruptedException ex) {
                        
                    }
                }   
                break;
            case -2:
                //gauche
                while(this.x > this.obj){
                    this.x--;
                    p.relocate(x, y);
                    try {
                        sleep(3);
                    } catch (InterruptedException ex) {
                        
                    }
                }   
                break;
            case 4:
                //sommet
                while(this.x>this.obj){
                    this.x--;
                    p.relocate(x, y);
                    try {
                        sleep(3);
                    } catch (InterruptedException ex) {
                        
                    }
                }
                break;
            default:
                while(this.x<this.obj){
                    this.x++;
                    p.relocate(x, y);
                    try {
                        sleep(3);
                    } catch (InterruptedException ex) {
                        
                    }
                }
                //base
                break;
        }
        
    }
}
