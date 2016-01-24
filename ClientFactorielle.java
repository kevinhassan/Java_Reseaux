// ClientFactorielle.java
package Client;

import java.util.*;
import java.net.*;
import java.io.*;

// Cette classe contient le client qui demande la factorielle et obtient le résultat
class ClientFactorielle extends Thread {

    private InetAddress adresse;
    private Socket socket;
    private String parametre;
    private int port;
    private String valeur;
    /**
     * Constructeur du client factorielle
     * le parametre correspond au nombre dont on cherche le factorielle
     * le port sur lequel on envoie le nombre à calculer
     * l'adresse du serveur qui calcule la factorielle
     */
    public ClientFactorielle(String parametre,int port, String adresse) throws IOException{
        this.port = port;
        this.parametre = parametre;
        this.adresse = InetAddress.getByName(adresse);
        socket = new Socket(adresse, this.port);
    }

    public void run(){
        try{
            //On envoie le nombre souhaité        
            PrintStream output = new PrintStream(socket.getOutputStream());
            output.print(this.parametre);
            //On écoute le serveur qui nous envoie un message            
            InputStream input = socket.getInputStream();
            Scanner sc = new Scanner(input);
            String msg;
            if(sc.hasNext()){
                msg = sc.nextLine();
                System.out.println("Le résultat de fact( "+parametre+") est : "+msg);
                this.valeur = msg;
            }
        }catch(Exception e){
            e.printStackTrace();        
        }
    }

    public static void main(String[] args) {
        try {
            ClientFactorielle client = new ClientFactorielle(args[0],Integer.parseInt(args[1]),args[2]);
            client.run();
        }//try
        catch (Exception e) {
            e.printStackTrace();
        }//catch
    }//main

    public String getValeur() {
        return valeur;
    }
}//ClientFactorielle
