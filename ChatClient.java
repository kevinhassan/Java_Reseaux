// ChatClient.java

import java.util.*;
import java.net.*;
import java.io.*;

class ChatClient {

    class Listen extends Thread {

        Socket socket;
        InputStream sInput;
    
        Listen(Socket socket) {
	    try {
	        this.socket = socket;
	        sInput  = socket.getInputStream();
	    }//try
	    catch (Exception e) {}
        }//Listen

        public void run() {
            Scanner sc = new Scanner(sInput);
            while (true) {
                if (sc.hasNext()) {
                    String msg = sc.nextLine();
                    System.out.println(msg);
                }//if
            }//while
        }//run

    }//Listen

    void run() throws SocketException, IOException, UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        Socket socket = new Socket(address, 50000);
        Listen l = new Listen(socket);
        l.start();
        PrintStream output = new PrintStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);
        String msg;
        while (true) {
            msg = sc.nextLine();
            output.println(msg);
        }//while
    }//run

    public static void main(String argv[]) {
        try {
            ChatClient c = new ChatClient();
            c.run();
        }//try
        catch (Exception e) {}//catch
    }//main

}//ChatClient
