package jeu20483d;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 *
 * @author chloe
 */
public class Bdd {
        private Connection conn;
        private String adresse;
        private String user;
        private String password;
    
    //Constructeur 
    public Bdd (){
    }
    
    //Getter
    public String getAdresse(){
        return this.adresse;
    }
    
    //Setter
    public void setAdresse(String a){
        this.adresse = a;
    }
    
    //Méthodes
    public void ouvrir() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            BufferedReader br = new BufferedReader(new FileReader("bdd.txt"));
            String ligne= br.readLine(); 
            String[] infosBdd = ligne.split(";"); 
            this.adresse = infosBdd[0];
            this.user = infosBdd[1];
            this.password=infosBdd[2];
            this.conn = DriverManager.getConnection(adresse, user, password);
            System.out.println("Connexion établie");
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException | FileNotFoundException e)
        {
            System.out.println(e.toString());
        }   catch (IOException ex) {   
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public void fermer(){
        
        if(conn!=null){
            try{
                conn.close();
                
            }
        catch(Exception e){
            e.printStackTrace();
        }
    }
        
    }
    
    public void supprimer(Joueur j) {
        this.ouvrir();
        
            
            try {
                Statement s = conn.createStatement();
                String q = "Delete from joueur where pseudo='"+j.getPseudo()+"'";
                s.executeUpdate(q);
                
            } catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.fermer();
    }
    
    public boolean supprimer(Partie p){
        return true;
    }
    public boolean ajouter(String mail, String pseudo, String mdp){
        this.ouvrir();
        boolean b = false;
        try {
            Statement s = conn.createStatement();
            String q = "Select mail from joueur where mail='"+mail+"'";
            ResultSet rs = s.executeQuery(q);
            if (!rs.next())
            {
                Statement t = conn.createStatement();
                String r ="Insert into joueur (mail, pseudo, mdp) values ('"+mail+"','"+pseudo+"', '"+mdp+"')";
                t.executeUpdate(r);
                b= true;
            } 
        }
        catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.fermer();
        return b;
    }
    
    public void ajouterPartie(Joueur j){
        this.ouvrir();
        try {
            BufferedReader br = new BufferedReader(new FileReader("partie.dat"));
        String partie =  br.lines().collect(Collectors.joining());
        Statement s = conn.createStatement();
        String q = "Insert into joueur (partie) values ('"+partie+"') where pseudo like '"+j.getPseudo()+"'";
        }
        catch (FileNotFoundException | SQLException e)
        {
            System.out.println(e.toString());
        }
        this.fermer();
    }
    
    public boolean chercher(Joueur j){
        return true;
    }
    
    public boolean connexion(String mail, String mdp){
        this.ouvrir();
        boolean b = false;
        System.out.println(this.conn);
        try 
        {
            Statement s = this.conn.createStatement();
            String q = "SELECT * FROM joueur WHERE mail='"+mail+"' and mdp='"+mdp+"'";
            ResultSet rs = s.executeQuery(q);
            if (rs.next())
            {
                b=true;
            }
        }
        catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.fermer();
        return b;
        
    }
    public void writeScore(Joueur j)
    {
        this.ouvrir();
        try 
        {
            Statement s = this.conn.createStatement();
            String q = "select score from joueur where pseudo like '"+j.getPseudo()+"'";
            ResultSet rs = s.executeQuery(q);
            if (!rs.next() || rs.getInt(1)<j.getMeilleurScore())
            {
                Statement ss = this.conn.createStatement();
                String qq = "insert into joueur (score) values ("+j.getMeilleurScore()+")";
            }
        }
        catch (SQLException e){
            System.out.println(e.toString());
        }
        this.fermer();
    }

    public Map<String, Integer> getScores() {
        this.ouvrir();
        Map<String,Integer> scores = new HashMap<>();
        try{
            Statement s = conn.createStatement();
            String q = "Select pseudo, score from joueur";
            ResultSet rs = s.executeQuery(q);
            
            while (rs.next())
            {
                scores.put(rs.getString(1), rs.getInt(2));
            }
           
        }
        catch (SQLException e)
        {
            System.out.println(e.toString());
        }
        this.fermer();    
    return scores;
    
    }
    public String getPseudo(String mail) {
        String r = null;
        try {
        Statement s = conn.createStatement();
        String q = "select pseudo from joueur where mail='"+mail+"'";
        ResultSet rs = s.executeQuery(q);
            
            if (rs.next())
            {
                r = rs.getString(1);
            }
     
    }       catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
    return r;
    }
    public String getStyle(String mail)
    {
        String r = null;
        try {
        Statement s = conn.createStatement();
        String q = "select style from joueur where mail='"+mail+"'";
        ResultSet rs = s.executeQuery(q);
            
            if (rs.next())
            {
                r = rs.getString(1);
            }
     
    }       catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
    return r;
    }
    
    public int getScoreJoueur(String mail)
    {
        int r = 0;
        try {
        Statement s = conn.createStatement();
        String q = "select score from joueur where mail='"+mail+"'";
        ResultSet rs = s.executeQuery(q);
            
            if (rs.next())
            {
                r = rs.getInt(1);
            }
     
    }       catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
    return r;
    }
    
    public int getNbParties(String mail)
    {
        int r = 0;
        try {
        Statement s = conn.createStatement();
        String q = "select nbPartiesGagnees from joueur where mail='"+mail+"'";
        ResultSet rs = s.executeQuery(q);
            
            if (rs.next())
            {
                r = rs.getInt(1);
            }
     
    }       catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
    return r;
    }
    
}

