import java.io.Serializable;

/**
 * Pattern creates patterns of a certain length of String Used to store keys in
 * the Computer class
 * 
 * @author jenniferluong
 *
 */
public class Pattern implements Serializable {
   /** Key represents the pattern */
   private String mKey;

   /**
    * Initializes the pattern to a String
    * 
    * @param input
    *           pattern
    */
   public Pattern(String input) {
      mKey = input;
   }

   /**
    * Checks to see if object inputted is a pattern return true if they are
    * equal else, return false
    */
   @Override
   public boolean equals(Object o) {
      if (o instanceof Pattern) {
         Pattern s = (Pattern) o;
         return (mKey.equals(s.mKey));
      } else {
         return false;
      }
   }

   /**
    * mKey is in form of a String which already has its own hash code return
    * hashcode of the key
    */
   @Override
   public int hashCode() {
      return mKey.hashCode();
   }

   /**
    * Gets the string pattern
    * 
    * @return pattern
    */
   public String getPattern() {
      return mKey;
   }

}
