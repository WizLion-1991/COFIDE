/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;

/**
 *Clase que tiene funciones para rellenar cadenas de programas que parsean a cierto formato, como llenar
 * espacios o ceros
 * @author aleph_79
 */
public class Parser {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">


   public static String fill(String str, int len) {
      if (str == null) {
         str = "";
      }
      if (str.length() > len) {
         str = str = str.substring(0, len);
      }

      while (str.length() < len) {
         str += " ";
      }
      return str;
   }

   public static String fill(Integer i, int len) {
      String str = i.toString();

      while (str.length() < len) {
         str += " ";
      }
      return str;
   }

   public static String fillLeftS(String str, int len) {

      if (str == null) {
         str = "";
      }

      if (str.length() > len) {
         str = str.substring(str.length() - len, str.length());
      }
      while (str.length() < len) {
         str = " " + str;
      }
      return str;
   }

   public static String fillLeft(String str, int len) {
      if (str.length() > len) {
         str = str.substring(str.length() - len, str.length());
      }
      while (str.length() < len) {
         str = "0" + str;
      }
      return str;
   }

   public static String fillRight(String str, int len) {

      if (str.length() > len) {
         str = str.substring(0, len);
      }

      while (str.length() < len) {
         str = str + "0";
      }
      return str;
   }

   public static String fillRightS(String str, int len) {

      if (str == null) {
         str = "";
      }

      if (str.length() > len) {
         str = str.substring(0, len);
      }

      while (str.length() < len) {
         str = str + " ";
      }
      return str;
   }

   public static String tabsToSpaces(String in, int tabSize) {
      StringBuffer buf = new StringBuffer();
      int width = 0;
      for (int i = 0; i < in.length(); i++) {
         switch (in.charAt(i)) {
            case '\t':
               int count = tabSize - (width % tabSize);
               width += count;
               while (--count >= 0) {
                  buf.append(' ');
               }
               break;
            case '\n':
               //width = 0;
               //buf.append(in.charAt(i));
               break;
            default:
               width++;
               buf.append(in.charAt(i));
               break;
         }
      }
      return buf.toString();
   }
   // </editor-fold>
}
