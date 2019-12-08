package jeu20483d;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.Scanner;


public class IO implements Runnable {


    static int port;
    final String IDhote = "maxime-X556UQ/127.0.1.1";
    final String IDnet = "";
    private final Socket s;
    Socket connection = new Socket(IDhote, port);

    public IO(Socket s) throws IOException {
        this.s = s;
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());
        }catch(IOException e){e.printStackTrace();}
    }

    //Ouverture du serveur sur la machine hote
    //Création socket serveur
    public static Socket connectionServeur() throws IOException {
        try {
            System.out.println("Port ? :");
            Scanner sc = new Scanner(System.in);
            port = sc.nextInt();
            ServerSocket sersoc = new ServerSocket(port);
            Socket cnx = sersoc.accept();
            return cnx;
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }

        // Création socket client
        public static Socket connectionClient() throws IOException {
            try {
                System.out.println("Port ? :");
                Scanner sc = new Scanner(System.in);
                port = sc.nextInt();
                Socket cnx = new Socket(getIP(), port);
                return cnx;
            } catch (IOException e) {e.printStackTrace();}
            return null;
        }

        /**récupère message string via socket entrante
        public static String getMessage(Socket cnx) throws IOException {
            String message;
            try {

                DataInputStream entree = new DataInputStream(new BufferedInputStream(new FileInputStream(m)));
                BufferedReader read = new BufferedReader(new InputStreamReader(m));
                message = entree.read(m);
                return message;
            } catch (IOException e) {
                System.out.println("Erreur lors de la récupération d'information");
            }
            return null;
        } **/

        //envoi un message string via socket sortante
        public static void sendMessage(Socket cnx, String s) throws IOException {
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


    /**Premier test de message
    public static void TestMessage() throws IOException {
        if (getIP().equals(getIP())) {
            testPort();
            System.out.print(getMessage(connectionServeur()));
        } else {
            System.out.println("Message ? :");
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            sendMessage(connectionClient(), str);
        }
    }**/

    public static Socket socketProxy(){
        SocketAddress proxyAdress = new InetSocketAddress("10.10.10.10",8080);
        Proxy prox = new Proxy(Proxy.Type.SOCKS, proxyAdress);
        Socket s = new Socket(prox);
        return s;
    }


    public static void creerFichierPartie (Partie p) {
        try {
            ObjectOutputStream sortie = new ObjectOutputStream(new FileOutputStream("partie.dat"));
            sortie.writeObject(p);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static Partie lecFichierPartie () {
        try {
            ObjectInputStream entree = new ObjectInputStream(new FileInputStream("partie.dat"));
            Partie p;
            p = (Partie) entree.readObject();
            return p;
        }catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }return null;
    }
    @Override
    public void run() {
        try{
            ServerSocket listener = new ServerSocket(port);
            while(true){
                Socket s = listener.accept();
                InputStream in = s.getInputStream();
                OutputStream out = s.getOutputStream();
                ObjectInputStream objIn = new ObjectInputStream(in);
                ObjectOutputStream objOut = new ObjectOutputStream(out);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{

        }
    }
}
