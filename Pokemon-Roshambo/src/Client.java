import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * Client panel
 *    Shows GUI display
 *    How the user interacts with cpu
 *    
 * @author jenniferluong
 *
 */
public class Client extends JPanel implements MouseListener {
   /** Creates connection to Server */
   private Socket sock;
   /** Port number to connect to Server */
   private int port = 2070;
   /** Send messages to Server */
   private PrintStream write;
   /** Read messages from Server */
   private BufferedReader read;
   
   /** X-value of mouse-clicked */
   private int x;
   /** Y-value of mouse-clicked */
   private int y;
   
   /** User's choice */
   private String choice;
   /** CPU's choice */
   private String prediction;
   /** Winner of round */
   private int winner;
   /** User's score */
   private int score;
   /** CPU's score */
   private int cpuScore;

   /** Font of score */
   private Font scoreFont;
   /** Font of winner */
   private Font winFont;

   
   /**
    * Initializes Client information to connect to server
    *    Socket, IO Streams
    */
   public Client() {
      try {
         System.out.println("Requesting connection...");
         sock = new Socket ("localhost", port);
         read = new BufferedReader(new InputStreamReader (sock.getInputStream()));
         write = new PrintStream (sock.getOutputStream());
         System.out.println("Connected.");
         
      } catch (IOException e) {
         e.printStackTrace();
      }  
      
      addMouseListener(this);
      setFocusable(true);

      scoreFont = new Font("ARIAL", Font.BOLD, 40);
      winFont = new Font("ARIAL", Font.BOLD, 35);
      
      choice = "";
      prediction = "";
      score = 0;
      cpuScore = 0;
      
   }

   /**
    * Draws all components of GUI 
    * Choices (user and CPU) and scoreboard
    */
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      
      Color darkRed = new Color (186, 48, 48);
      Color blue = new Color (48,78,186);
      Color green = new Color (52, 131, 73);

      try {
         BufferedImage title = ImageIO.read(new File ("Title.png"));
         BufferedImage bckgrd = ImageIO.read(new File ("Background.png"));
         g.drawImage(bckgrd, 0, 0, 700, 600, this);
         g.drawImage(title, 200, 50, 300, 88, this);

      } catch (IOException e) {
         System.out.println("File not found");
      }


      g.setColor(Color.BLACK);
//      User Choice Display
      g.drawRect(175, 275, 150, 100);
//      CPU Choice Display
      g.drawRect(375, 275, 150, 100);
   
      
      g.setColor(darkRed);
      g.fillRect(75, 400, 150, 100);
      g.setColor(Color.BLACK);
      g.drawRect(75, 400, 150, 100);
      
      g.setColor(blue);
      g.fillRect(275, 400, 150, 100);
      g.setColor(Color.BLACK);
      g.drawRect(275, 400, 150, 100);
      
      g.setColor(green);
      g.fillRect(475, 400, 150, 100);
      g.setColor(Color.BLACK);
      g.drawRect(475, 400, 150, 100);

      g.setColor(Color.WHITE);
      g.fillRect(580, 525, 45, 20);
      g.setColor(Color.BLACK);
      g.drawString("QUIT", 585, 540);

      try {
         BufferedImage fire = ImageIO.read(new File ("Fire.png"));
         BufferedImage water = ImageIO.read(new File ("Water.png"));
         BufferedImage grass = ImageIO.read(new File ("Grass.png"));

//       OPTIONS
         g.drawImage(fire, 75, 400, 150, 100, this);
         g.drawImage(water, 275, 400, 150, 100, this);
         g.drawImage(grass, 475, 400, 150, 100, this);

         
//       USER'S CHOICE
         if (prediction.equals("F")) {
            g.drawImage(fire, 375, 275, 150, 100, this);
         }            
         else if (prediction.equals("W")) {
            g.drawImage(water, 375, 275, 150, 100, this);
         }
         else if (prediction.equals("G")) {
            g.drawImage(grass, 375, 275, 150, 100, this);
         }

//       CPU'S CHOICE
         if (choice.equals("F")) {
            g.drawImage(fire, 175, 275, 150, 100, this);
         }
         else if (choice.equals("W")) {
            g.drawImage(water, 175, 275, 150, 100, this);
         }
         else if (choice.equals("G")) {
            g.drawImage(grass, 175, 275, 150, 100, this);
         }
         
      } catch (IOException e) {
         e.printStackTrace();
      }
      
      g.setColor(Color.WHITE);

      g.drawString("Your choice", 175, 270);
      g.drawString("Computer's choice", 375, 270);
      
      g.setFont(winFont);
      if (winner == 1) {
         g.drawString("TIE", 325, 200);
      }
      else if (winner == 2) {
         g.drawString("WIN", 325, 200);
      }
      else if (winner == 3) {
         g.drawString("LOST", 310, 200);
      }
      
      g.setFont(scoreFont);
      g.drawString(score + "", 225, 200);
      g.drawString(cpuScore + "", 450, 200);
      
      repaint();

   }
   
   /**
    * Reads in the CPU prediction
    * Decides who wins round
    */
   public void read() {
      while (true) {
         try {
            prediction = read.readLine();
            if (choice.equals(prediction)) {
               //tie
               winner = 1;
            } 
            else if ((choice.equals("F") && prediction.equals("G"))
                  || (choice.equals("W") && prediction.equals("F"))
                  || (choice.equals("G") && prediction.equals("W"))) {
               //player wins
               play("Win.wav");
               winner = 2;
               score++;

            } 
            else {
               //cpu wins
               winner = 3;
               cpuScore++;
               play("Lose.wav");

            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
   
   public static void play (String filename) {
      try {
         Clip clip = AudioSystem.getClip();
         clip.open(AudioSystem.getAudioInputStream(new File (filename)));
         clip.start();
         
      } catch (LineUnavailableException e) {
         System.out.println("Audio Error");
      } catch (IOException e) {
         System.out.println("File not found");
      } catch (UnsupportedAudioFileException e) {
         System.out.println("Wrong File Type");
      }
   }
   
   /**
    * Gets user's choice
    * Writes to CPU to predict
    */
   @Override
   public void mouseClicked(MouseEvent e) {
      x = e.getX();
      y = e.getY();
      
      if (y > 400 && y < 500) {
         if (x > 75 && x < 225) {
            choice = "F";
         }            
         else if (x > 275 && x < 425) {
            choice = "W";
         }
         else if (x > 475 && x < 625) {
            choice = "G";
         }
         write.println(choice);
         write.flush();
      }
      else if (y > 525 && y < 545) {
         if (x > 580 && x < 625) {
            try {
               write.println("EXIT");
               write.flush();
               sock.close();
               System.exit(0);
               
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }
      }
   }


   
   //MAIN
   public static void main (String [] args) {
      JFrame f = new JFrame();
      Client p = new Client();
      
      f.setBounds(325, 125, 700, 600);//x,y,w,h of window
      f.setTitle("Pokemon Mind Reader");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.getContentPane().add(p);
      f.setVisible(true);

      p.read();
      
   }

   
   
   
   
   
   
   
   @Override
   public void mousePressed(MouseEvent e) {

   }
   
   @Override
   public void mouseReleased(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void mouseEntered(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }

   @Override
   public void mouseExited(MouseEvent e) {
      // TODO Auto-generated method stub
      
   }
}
