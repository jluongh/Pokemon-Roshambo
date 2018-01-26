import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Server
 *    Runs program to allow Client program to connect to
 * 
 * @author jenniferluong
 *
 */
public class Server extends Thread {

   /** Listens for incoming connections */
   private ServerSocket server;
   /** Allows client and server to communicate */
   private Socket sock;
   /** Port number to create connection */
   private int port = 2070;
   /** Receives messages from client */
   private BufferedReader read;
   /** Sends messages to client */
   private PrintStream write;
   /** Game's CPU player */
   private Computer cpu;
   /** Stored patterns */
   private File file;
   
   /**
    * Initializes all of server's information to connect clients
    *    ServerSocket, socket, IO streams
    * Reads in saved file
    */
   public Server() {
      try {
         server = new ServerSocket (port);
         
         System.out.println("Waiting...");

         sock = server.accept();
         read = new BufferedReader (new InputStreamReader(sock.getInputStream()));
         write = new PrintStream (sock.getOutputStream());

         System.out.println("Connected.");
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      file = new File("game2.dat");

      if (file.exists()) {
         try {
            System.out.println("Opening file...");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            cpu = (Computer) in.readObject();
            in.close();
            
         } catch (IOException e) {
            System.out.println("Error processing file.");
         } catch (ClassNotFoundException e) {
            System.out.println("Cannot find class.");
         }
      }
      else {
         cpu = new Computer();
      }
   }
   
   /**
    * Constantly checks for data from client
    */
   public void run() {
      try {
         read();
         Thread.sleep(60);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
     
   }
   
   /**
    * Reads in client's data
    * Creates a prediction off of choices and past patterns
    * Writes prediction to client
    */
   public void read() {
      Boolean flag = false;
      String move = "", choice = "";
      while (!flag) {
         try {
            choice = read.readLine();
            if (!choice.equals("EXIT")) {
               String prediction = cpu.predict(move);
               write.println(prediction); 
               
               move += choice;
               cpu.storePattern(move);
               
               if (move.length() == 4) {
                  move = move.substring(1);
               }
            }
            else {
               server.close();
               try {
                  ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                  out.writeObject(cpu);
                  out.close();
                  System.out.println("Saved...");
               } catch (IOException e) {
                  System.out.println("Error processing file. 2");
               }
               flag = true;
               
              
            }
         } catch (IOException e) {
         }
      }
   }
   
   public static void main (String[] args) {
      Server game = new Server();
      game.start();
   }
}
