import java.io.Serializable;
import java.util.HashMap;
/**
 * Computer represents the game's opponent player
 * 
 * @author jenniferluong
 *
 */
public class Computer implements Serializable {
   
   /** Map that stores patterns and how often it was used*/
   private HashMap<Pattern, Integer> mMap;

   /**
    * Initializes a Computer class
    * Includes a map to store patterns
    */
   public Computer() {
      mMap = new HashMap<Pattern, Integer>();
   }

   /**
    * Stores a pattern once there are four inputs
    * @param patt String pattern created in main
    */
   public void storePattern(String patt) {
      
      if (patt.length() == 4) {
         Pattern key = new Pattern(patt);
         if (mMap.containsKey(key)) {
            mMap.put(key, mMap.get(key) + 1);
         } else {
            mMap.put(key, 1);
         }
      }
   }

   /**
    * Creates a prediction based on a pattern
    * If a pattern isn't found, then a guess a returned
    * 
    * @param patt       Pattern created by user
    * @return prediction
    */
   public String predict(String patt) {

      if (!mMap.isEmpty()) {
         String threeChoice = patt;
         int storeF = 0, storeW = 0, storeG = 0;

         String compareF = threeChoice + "F";
         Pattern compF = new Pattern(compareF);

         String compareW = threeChoice + "W";
         Pattern compW = new Pattern(compareW);

         String compareG = threeChoice + "G";
         Pattern compG = new Pattern(compareG);

         if (mMap.containsKey(compF) || mMap.containsKey(compW) || mMap.containsKey(compG)) {
            if (mMap.containsKey(compF)) {
               storeF = mMap.get(compF);
            }

            if (mMap.containsKey(compW)) {
               storeW = mMap.get(compW);
            }

            if (mMap.containsKey(compG)) {
               storeG = mMap.get(compG);
            }
            if (storeG > storeW) {
               if (storeG > storeF) {
                  return "F";
               } else {
                  return "W";
               }
            } else {
               if (storeW > storeF) {
                  return "G";
               }
               else {
                  return"W";
               }
            }
         }
         else {
            int random = 1 + (int) (Math.random() * 3);
            if (random == 1) {
               return "W";
            } else if (random == 2) {
               return "G";
            } else {
               return "F";
            }         
         }
      } 
      else {
         int random = 1 + (int) (Math.random() * 3);
         if (random == 1) {
            return "W";
         } else if (random == 2) {
            return "G";
         } else {
            return "F";
         }
      }
   }
}
