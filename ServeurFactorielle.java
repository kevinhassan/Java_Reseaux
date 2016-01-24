// ServeurFactorielle.java

import java.io.*;
import java.net.*;
import java.util.*;

import ClientFactorielle.*;

/**
 * Cette classe contient le serveur qui permet de créer des sous clients qui vont se connecter au serveurs
 * pour réaliser les factorielles (n-1)
 */

class ServeurFactorielle extends Thread{

    private int port;
    /*
        On décide de choisir une table de hashage afin d'avoir pour clef l'entier dont on souhaite appliquer la factorielle
        Et le résultat associé à cette clef
    */
    private HashMap<Integer,Integer> buffer;

    /*
        Constructeur du serveur associé à un port d'écoute et à un buffer (table de hashage)
    */
    public ServeurFactorielle(int port){
        this.port = port;
        buffer = new HashMap<Integer,Integer>();
    }

    /*
        Classe interne au serveur qui permet de gerer les flux I/O des clients connectés au serveur
    */
    class ClientThread extends Thread{
        private int port;
        private Socket socket;
        private int parametre;
        private PrintStream output;
        /*
         *  Le buffer du serveur est consulté à chaque demande de factorielle
         */
        private HashMap<Integer,Integer> buffer;


        /*
            Constructeur de clientThread
        */
        public ClientThread(Socket socket,HashMap<Integer,Integer> buffer,int port) throws Exception{
            super();//Permet de faire appel au constructeur de la classe parente à cette classe
            this.socket = socket;
            this.port = port;
            //On analyse ce qui est reçut par le client
            Scanner input = new Scanner(socket.getInputStream());
            this.parametre = Integer.parseInt(input.next());//On récupère les factorielles(n-1)
            this.output = new PrintStream(socket.getOutputStream());
            this.buffer=buffer;//On partage avec chaque client la table de hachage

        }

        /*
            Cette fonction permet de créer les sous clients
        */
        public void run(){
            try{
                if(this.buffer.containsKey(parametre))//Si on a déjà effectué le calcul (la clef existe dans la table de hashage)
                    System.out.println("Factorielle de "+parametre+" vaut : "+this.buffer.get(parametre));//On cherche le résultat de la clef dans le buffer
                else
                {
                    if (parametre < 0)//Si on ne respecte pas les précondition alors on ferme la connexion avec le serveur
                        socket.close();
                    else if(parametre == 0) // Sinon si c'est 0 on met pour la clef 0 la valeur 1
                        this.buffer.put(0,1);
                    else
                    {
                        ClientFactorielle c = new ClientFactorielle(Integer.toString(parametre-1),this.port,"localhost");//On crée un sous-client avec pour parametre : parametre-1
                        c.start();
                        while(c.getRetour()==null);//On attent tant que les clients n'ont pas renvoyés les résultats
                        this.buffer.put(parametre,parametre*Integer.parseInt(c.getRetour()));//On ajoute dans le buffer dans la clef correspondant à parametre le resultat de factorielle
                    }
                }
                //On envoie au client initial le resultat de factorielle(n)
                output.print(Integer.toString(this.buffer.get(parametre))+"\n");
                //Après l'envoie on coupe la connexion
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /*
            Cette fonction appartient au serveur
            A chaque connexion d'un client on crée un clientThread
     */
    public void run(){
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            while(true)
            {
                Socket socket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(socket, buffer, port);
                clientThread.run();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ServeurFactorielle server = new ServeurFactorielle(Integer.parseInt(args[0]));
        server.run();
    }
}
