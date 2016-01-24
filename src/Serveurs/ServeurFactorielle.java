// ServeurFactorielle.java
package Serveurs;

import java.io.PrintStream;
import java.net.*;
import java.util.*;

import Clients.ClientFactorielle;

/**
 * Cette classe contient le serveur qui permet de créer des sous clients qui vont se connecter au serveurs
 * pour réaliser les factorielles (n-1)
 */
public class ServeurFactorielle extends Thread{
	/**
	*	Port d'écoute
	*/
	private int port;
    /*
        On décide de choisir une table de hashage afin d'avoir pour clef l'entier dont on souhaite appliquer la factorielle
        Et le résultat associé à cette clef
    */
	private HashMap<Integer,Integer> buffer;
	private ServerSocket serverSocket;

	/*
    	Constructeur du serveur associé à un port d'écoute et à un buffer (table de hashage)
	*/
	public ServeurFactorielle(int port){
		super();
		this.port = port;
		buffer = new HashMap<Integer,Integer>();

	}

	private class ClientThread extends Thread{
		
		private int port;
		private Socket socket;
		private int parametre;
		private PrintStream output;
		/**
         *  Le buffer du serveur est consulté à chaque demande de factorielle
		 */
		private HashMap<Integer,Integer> buffer;

        /*
            Constructeur de clientThread
        */
		public ClientThread(Socket socket,HashMap<Integer,Integer> buffer,int port) throws Exception{
			super();
			this.socket = socket;
			this.port=port;
			Scanner entree = new Scanner(socket.getInputStream());
			this.parametre = Integer.parseInt(entree.next());
			this.output = new PrintStream(socket.getOutputStream());
			this.buffer=buffer;

		}

		/**
		 * Fonction declenchant le traitement du client.
		 * Elle s'occupera notamment de creer les sous clients.
		 */
		public void run(){
			try{	
				if(this.buffer.containsKey(parametre))//On a deja effectue le calcul
					System.out.println("Factorielle de "+parametre+" vaut : "+this.buffer.get(parametre));
				else
				{
					if(parametre <0)
					{
						output.print("L'entier n'est pas définit "+"\n");
					}
					else if(parametre == 0)
					{
						this.buffer.put(0,1);	
						output.print(Integer.toString(this.buffer.get(parametre))+"\n");
					}	
					else
					{
						ClientFactorielle c = new ClientFactorielle(Integer.toString(parametre-1),this.port,"localhost");
						c.run();
						while(c.getValeur()==null);
						this.buffer.put(parametre,parametre*Integer.parseInt(c.getValeur()));		
						output.print(Integer.toString(this.buffer.get(parametre))+"\n");
					}
				}
				socket.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
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
		server.start();
	}
}
