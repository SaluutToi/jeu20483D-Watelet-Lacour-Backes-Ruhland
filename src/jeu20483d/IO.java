package jeu20483d;

import java.io.*;
import java.net.*;

/**
 * Gère les échangent réseaux et fichiers
 * @author mbackes
 * @version 1.0
 */


public class IO implements Runnable {


    static int port = 3306;
    final static String IDhote = "maxime-X556UQ/127.0.1.1";
    private Socket s;
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    Socket connection = new Socket(IDhote, port);

    /**
     * Constructeur de flux objet Sockets
     * @param s
     * @throws IOException
     */

    public IO(Socket s) throws IOException {
        this.s = s;
        try{
            this.objIn = new ObjectInputStream(s.getInputStream());
            this.objOut = new ObjectOutputStream(s.getOutputStream());
        }catch(IOException e) {e.printStackTrace();}
    }

    /**
     * Création serveur solo
     * @throws IOException
     */

    public static void serveur() throws IOException {
        try {
            ServerSocket listen = new ServerSocket(port);
            while (true){
                Socket s = listen.accept();
                IO connexion = new IO(s);
                Thread process_connexion = new Thread(connexion);
                process_connexion.start();
            }
        } catch (IOException e) {e.printStackTrace();}
    }

    /**
     * Création socket client
     * @throws IOException
     */

    public static void client() throws IOException {
        try {
            Socket s = new Socket(IDhote, port);
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            Partie p = (Partie) in.readObject();

        } catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
    }

    /**
     * Test et affiche les port diponible sur la machine
     */

    public static void testPort() {
        for (int i = 1024; i <= 49151; i++) {
            try {
                Socket soc = new Socket("127.0.0.1", i);
                System.out.println("La machine autorise les connexions sur le port : " + i);
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    /**
     * retourne l'adresse IP de la machine en String
     * @return
     */

    public static String getIP() {
        try {
            InetAddress IP = InetAddress.getLocalHost();
            String s = IP.toString();
            return s;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }


    /**
     *
     * @param p
     */
    public static void creerFichierPartie (Partie p) {
        try {
            String s = p.getJoueur1().getPseudo();
            ObjectOutputStream sortie = new ObjectOutputStream(new FileOutputStream(s + "_partie.dat"));
            sortie.writeObject(p);
        }catch(IOException e) {e.printStackTrace();}
    }

    /**
     *
     * @param j
     * @return
     */
    public static Partie lecFichierPartie (Joueur j) {
        try {
            String s = j.getPseudo();
            File f = new File(s + "_partie.dat");
            ObjectInputStream entree = new ObjectInputStream(new FileInputStream(f));
            Partie p;
            p = (Partie) entree.readObject();
            return p;
        }catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }return null;
    }
    
    @Override
    public void run() {
        s = connection;
        try {
            while(true){
                IO io = new IO(s);
                Partie partie = (Partie) io.objIn.readObject();
                if (partie == null)break;
                System.out.println(partie.toString());
                objOut.flush();
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            try {
                s.close();
            }catch(IOException e) {e.printStackTrace();}
        }
    }
}
