// ClientFactorielle.java
package Clients;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner; 

// Cette classe contient le client qui demande la factorielle et obtient le résultat

public class ClientFactorielle extends Thread{
    private InetAddress adresse;
    private Socket socket;
    private String parametre;
    private int port;
    private String valeur;
	private Scanner sc;

    //On a pour 1er paramètre l'entier à calculer, le port et l'adresse où l'envoyer
	public static void main(String[] args) {
		//Creation du client
		ClientFactorielle client;
		try {
			client = new ClientFactorielle(args[0],Integer.parseInt(args[1]),args[2]);
			client.run();
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * Constructeur du client factorielle
     * le parametre correspond au nombre dont on cherche le factorielle
     * le port sur lequel on envoie le nombre à calculer
     * l'adresse du serveur qui calcule la factorielle
     */
	public ClientFactorielle(String parametre,int port, String adresse) throws IOException{
		super();
		this.port = port;
		this.parametre = parametre;
		this.adresse = InetAddress.getByName(adresse);
		socket = new Socket(adresse, this.port);

	}

	public void run(){
		try {
            //On envoie le nombre souhaité        
			PrintStream output = new PrintStream(socket.getOutputStream());
			output.print(this.parametre+"\n");
            //On écoute le serveur qui nous envoie un message            
			InputStream input = socket.getInputStream();
			sc = new Scanner(input);
			String msg;
			if(sc.hasNext()){
				msg = sc.nextLine();
				System.out.println("Factorielle de "+ parametre+" est : "+msg);
				this.valeur=msg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sc.close();
	}
	
	/**
	 * Getter de valeur
	 * @return Renvoie la valeur de valeur
	 */
	public String getValeur() {
		return this.valeur;
	}


}
