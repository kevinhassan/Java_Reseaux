// Chat.java

import java.io.*;
import java.net.*;
import java.util.*;

public class Chat {

    // Client part

    class ClientThread extends Thread {
    
        Socket socket;
        InputStream sInput;
        OutputStream sOutput;
        String name;
    
        ClientThread(Socket socket, String name) {
	    try {
	        this.socket = socket;
	        sOutput = socket.getOutputStream();
	        sInput  = socket.getInputStream();
	        this.name = name;
	    }//try
	    catch (Exception e) {}
        }//ClientThread

        public void run() {
            Scanner sc = new Scanner(sInput);
            while (true)
                if (sc.hasNext()) {
                    String msg = sc.nextLine();
                    System.out.println(name + ": " + msg);
                    broadcast(name + ": " + msg);
                }//if
        }//run

    }//ClientThread

    // Server part
    
    ArrayList<ClientThread> socks = new ArrayList<ClientThread>();
    int port;
    int num = 0;

    Chat(int port) {
	this.port = port;
    }//Chat

    synchronized void broadcast(String msg) {
	for (ClientThread e : socks) {
            PrintStream output = new PrintStream(e.sOutput);
            output.println(msg);
        }//for
    }//broadcast
    
    void run() {
	try {
	    ServerSocket sServer = new ServerSocket(port);
	    while (true) {
		Socket s = sServer.accept();
		ClientThread c = new ClientThread(s, "Thread " + num);;
                System.out.println("Connection of client " + num);
                num++;
		socks.add(c);
		c.start();
	    }//while
	}//try
	catch (Exception e) {}
    }//run

    public static void main(String [] argv) {
	Chat c = new Chat (50000);
	c.run();
    }//main

}//ChatServer
	    
