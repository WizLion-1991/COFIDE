package comSIWeb.Utilerias;

/**
 *Clase para generacion de varios datos
 * @author zeus
 */
public class generateData {

   public static String NUMEROS = "0123456789";
   public static String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   public static String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
   public static String ESPECIALES = "ñÑ";

   /**
    * Regresa un numero de ping de 4 digitos
    * @return Es el numero de ping
    */
   public static String getPinNumber() {
      return getPassword(NUMEROS, 4);
   }

   /**
    * Regresa un password de 8 digitos
    * @return
    */
   public static String getPassword() {
      return getPassword(8);
   }

   /**
    * Regresa un password de n numero de tamanio
    * @param length Es el ancho del password
    * @return Regresa un password
    */
   public static String getPassword(int length) {
      return getPassword(NUMEROS + MAYUSCULAS + MINUSCULAS, length);
   }

   /**
    * Regresa un password de n numero de tamanio y una llave
    * @param key Es la llave
    * @param length Es el ancho
    * @return Regresa un password
    */
   public static String getPassword(String key, int length) {
      String pswd = "";

      for (int i = 0; i < length; i++) {
         pswd += (key.charAt((int) (Math.random() * key.length())));
      }

      return pswd;
   }
}
