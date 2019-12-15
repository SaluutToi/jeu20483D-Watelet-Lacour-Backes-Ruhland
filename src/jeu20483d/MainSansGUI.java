package jeu20483d;

import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author chloe
 */
public class MainSansGUI implements Parametre {
    public static void main(String[] args){
        Grille3D g = new Grille3D();
        g.ajoutCase();
        g.ajoutCase();
        System.out.println("________________________________");
        System.out.println(g);
        Scanner sc = new Scanner(System.in);
        while (!g.jeuFini()) {
            System.out.println("Votre score est de:"+ g.getScore());
            System.out.println("Déplacer vers la Droite (d), Gauche (q), Haut (z), Bas (s), Sommet (r) ou Base (f)?");
            String s = sc.nextLine();
            s.toLowerCase();
            if (!(s.equals("d") || s.equals("droite")
                    || s.equals("q") || s.equals("gauche")
                    || s.equals("z") || s.equals("haut")
                    || s.equals("s") || s.equals("bas")
                    || s.equals("f") || s.equals("base")
                    || s.equals("r") || s.equals("sommet"))) {
                System.out.println("Vous devez écrire d pour Droite, q pour Gauche, z pour Haut, s pour Bas, r pour Sommet ou f pour Base");
            } else {
                int direction;
                if (s.equals("d") || s.equals("droite")) {
                    direction = DROITE;
                } else if (s.equals("q") || s.equals("gauche")) {
                    direction = GAUCHE;
                } else if (s.equals("z") || s.equals("haut")) {
                    direction = HAUT;
                } else if(s.equals("s") || s.equals("bas")){
                    direction = BAS;
                } else if(s.equals("r") || s.equals("sommet")){
                    direction = SOMMET;
                } else {
                    direction = BASE;
                }
                boolean b2 = g.lanceDeplacement(direction);
                if (b2) {
                    g.ajoutCase();
                }
                System.out.println("________________________________");
                System.out.println(g);
                if (g.getMeilleureCase()>=OBJECTIF) g.jeuGagne();
            }
        }
        
        if(g.getMeilleureCase() == 2048){
            g.jeuGagne();
        } else {
            g.jeuPerdu();
        }
        
    }
}
