package jeu20483d;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class IO {


    protected int port = 0;
    protected final String IDhote = "maxime-X556UQ/127.0.1.1";
    protected final String IDnet = "";

    //Ouverture du serveur sur la machine hote
    //Création socket serveur
    public Socket connectionServeur() throws IOException {
        try {
            System.out.println("Port ? :");
            Scanner sc = new Scanner(System.in);
            port = sc.nextInt();
            ServerSocket sersoc = new ServerSocket(port);
            Socket cnx = sersoc.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Création socket client
    public Socket connectionClient() throws IOException {
        try {
            System.out.println("Port ? :");
            Scanner sc = new Scanner(System.in);
            port = sc.nextInt();
            Socket cnx = new Socket(IDhote, port);
            return cnx;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //récupère message string via socket entrante
    public String getMessage(Socket cnx) throws IOException {
        String message;
        try {
            InputStream m = cnx.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(m));
            message = entree.readLine();
            return message;
        } catch (IOException e) {
            System.out.println("Erreur lors de la récupération d'information");
        }
        return null;
    }

    //envoi un message string via socket sortante
    public void sendMessage(Socket cnx, String s) throws IOException {

        try {
            OutputStream m = cnx.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(m);
            sortie.write(s);
            sortie.flush();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'envoi d'information");
        }
    }

    //Test et affiche les port diponible sur la machine
    public static void testPort() {
        for (int i = 1; i <= 1024; i++) {
            try {
                Socket soc = new Socket("127.0.0.1", i);
                System.out.println("La machine autorise les connexions sur le port : " + i);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
        }
    }

    //retourne l'adresse IP de la machine en String
    public static String getIP() {
        try {
            InetAddress IP = InetAddress.getLocalHost();
            String s = IP.toString();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Premier test de message
    public void TestMessage() throws IOException {
        if (getIP().equals(IDhote)) {
            testPort();
            System.out.print(getMessage(connectionServeur()));
        } else {
            System.out.println("Message ? :");
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            sendMessage(connectionClient(), str);
        }
    }

}
