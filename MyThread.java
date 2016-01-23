// MyThread.java

class OneThread extends Thread{

    public void run() {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < (start + 5000)) {
            System.out.println("Line printed by the thread");
            try {
                Thread.sleep(500);
            }//try
            catch (InterruptedException e) {}//catch
        }//while
    }//run

}//OneThread

public class MyThread {

    public static void main(String[] argv) {
        OneThread thread = new OneThread();
        thread.start();
        while(thread.isAlive()) {
            System.out.println("Line printed by the main");
            try {
                thread.sleep(800);
            }//try
            catch (InterruptedException e) {}//catch
        }//while
    }//main

}//MyThread
