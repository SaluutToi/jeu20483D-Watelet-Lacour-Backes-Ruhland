package jeu20483d;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 *
 * @author chloe
 * @version 1.0
 */
public class Bdd {
        private Connection conn;
        private String adresse;
        private String user;
        private String password;
    


    /**
     * Constructeur
     */
    public Bdd (){}
    


    /**
     * Getter
     * @return renvoie l'adresse mail
     */
    public String getAdresse(){
        return this.adresse;
    }

    //Getter

    /**
     *
     * @return renvoie le nom utilisateur
     */
    public String getUser() { return this.user; }
    
    //Setter

    /**
     *
     * @param a adresse
     */
    public void setAdresse(String a){
        this.adresse = a;
    }
    


    /**
     * Ouvre la Bdd
     */
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

    /**
     * Ferme la Bdd
     */
    public void fermer(){
        if(conn!=null){
            try{
                conn.close();   
            }
            catch(SQLException e){
                
            }
        }
    }

    /**
     *
     * @return Vérifie la connection
     */
    public boolean estConnecté(){
        if (conn != null)return true;
        else return false;
    }

    /**
     *
     * @param j
     */
    public void supprimer(Joueur j) {
        this.ouvrir();
            try {
                Statement s = conn.createStatement();
                String q = "Delete * from joueur where pseudo='"+j.getPseudo()+"'";
                s.executeUpdate(q);
            } catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.fermer();
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean supprimer(Partie p){
        return true;
    }

    /**
     *
     * @param mail
     * @param pseudo
     * @param mdp
     * @return booléen qui confirme l'ajout
     */
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

    /**
     *
     * @param j
     */
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

    /**
     *
     * @param j
     * @return
     */
    public boolean chercher(Joueur j){
        return true;
    }

    /**
     *
     * @param mail
     * @param mdp
     * @return connection d'un utilisateur particulier
     */
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

    /**
     *
     * @param j
     */
    public void writeScore(Joueur j){
        this.ouvrir();
        try {
            Statement s = this.conn.createStatement();
            String q = "select score from joueur where pseudo like '"+j.getPseudo()+"'";
            ResultSet rs = s.executeQuery(q);
            if (!rs.next() || rs.getInt(1)<j.getMeilleurScore()){
                Statement ss = this.conn.createStatement();
                String qq = "insert into joueur (score) values ("+j.getMeilleurScore()+")";
            }
        } catch (SQLException e){
            System.out.println(e.toString());
        }
        this.fermer();
    }

    /**
     *
     * @param j
     */
    public void updateJoueur(Joueur j){
        this.ouvrir();
        try {
            Statement s =this.conn.createStatement();
            String q = "Update joueur set style='"+j.getStyle()+"' where pseudo='"+j.getPseudo()+"'";
            s.executeUpdate(q);
        }   
        catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            Statement s =this.conn.createStatement();
            String q = "Update joueur set nbPartiesGagnees='"+j.getPartiesGagnees()+"' where pseudo='"+j.getPseudo()+"'";
            s.executeUpdate(q);
        }   
        catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            Statement s =this.conn.createStatement();
            String q = "Update joueur set score='"+j.getMeilleurScore()+"' where pseudo='"+j.getPseudo()+"'";
            s.executeUpdate(q);
        }   
        catch (SQLException ex) {
                Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.fermer();
    }

    /**
     *
     * @return Liste des scores
     */
    public ArrayList getScoresClassement() {
        this.ouvrir();
        ArrayList<Integer> scores = new ArrayList<Integer>();
        try{
            Statement s = conn.createStatement();
            String q = "Select score from joueur order by score desc";
            ResultSet rs = s.executeQuery(q);
            while (rs.next())
            {
                scores.add(rs.getInt(1));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.toString());
        }
        this.fermer();    
        return scores;
    }

    /**
     *
     * @return Classement des pseudos
     */
    public ArrayList getPseudosClassement() {
        this.ouvrir();
        ArrayList<String> scores = new ArrayList<String>();
        try{
            Statement s = conn.createStatement();
            String q = "Select pseudo from joueur order by score desc";
            ResultSet rs = s.executeQuery(q);
            while (rs.next())
            {
                scores.add(rs.getString(1));
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.toString());
        }
        this.fermer();    
        return scores;
    }

    /**
     *
     * @param mail
     * @return L'adresse Email d'un utilisateur
     */
    public String getPseudo(String mail) {
        this.ouvrir();
        String r = null;
        try {
            Statement s = conn.createStatement();
            String q = "select pseudo from joueur where mail='"+mail+"'";
            ResultSet rs = s.executeQuery(q);
            if (rs.next()){
                r = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fermer();
        return r;
    }

    /**
     *
     * @param mail
     * @return Le nom du thème couleur utilisé par l'utilisateur
     */
    public String getStyle(String mail){
        this.ouvrir();
        String r = null;
        try {
            Statement s = conn.createStatement();
            String q = "select style from joueur where mail='"+mail+"'";
            ResultSet rs = s.executeQuery(q);
            if (rs.next()){
                r = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fermer();
        return r;
    }

    /**
     *
     * @param mail
     * @return Le score d'un joueur
     */
    public int getScoreJoueur(String mail){
        this.ouvrir();
        int r = 0;
        try {
            Statement s = conn.createStatement();
            String q = "select score from joueur where mail='"+mail+"'";
            ResultSet rs = s.executeQuery(q);
            if (rs.next()){
                r = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fermer();
        return r;
    }

    /**
     *
     * @param mail
     * @return nombre de partie jouées par l'utilisateur
     */
    public int getNbParties(String mail){
        this.ouvrir();
        int r = 0;
        try {
            Statement s = conn.createStatement();
            String q = "select nbPartiesGagnees from joueur where mail='"+mail+"'";
            ResultSet rs = s.executeQuery(q);
            if (rs.next()){
                r = rs.getInt(1);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Bdd.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fermer();
        return r;
    }

    /**
     *
     * @param j
     */
    public static void creerFichierJoueur (Joueur j) {
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(new FileOutputStream("joueur.dat"));
            sortie.writeObject(j);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @return le joueur ayant joué précédemment
     */
    public static Joueur lecFichierJoueur () {
        try {
            ObjectInputStream entree = new ObjectInputStream(new FileInputStream("joueur.dat"));
            Joueur j;
            j = (Joueur) entree.readObject();
            return j;
        }catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }return null;
    }
    
}

