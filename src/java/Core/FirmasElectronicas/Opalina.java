package Core.FirmasElectronicas;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.apache.commons.ssl.Base64;

/**
 * Este programa se encarga de las funciones para encriptar datos
 */
public class Opalina {

   /**
    * Genera una llave aleatorio
    * @return Regresa una cadena de la llave aleatoria
    * @throws NoSuchAlgorithmException
    */
   public String makeWord() throws NoSuchAlgorithmException {
      String strNewWord = "";
      // Get the KeyGenerator
      KeyGenerator kgen = KeyGenerator.getInstance("AES");
      kgen.init(128); // 192 and 256 bits may not be available

      // Generate the secret key specs.
      SecretKey skey = kgen.generateKey();

      byte[] raw = skey.getEncoded();

      //Encripta la llave en base 64
      byte[] b64Enc = Base64.encodeBase64(raw);
      strNewWord = new String(b64Enc);
      return strNewWord;
   }

   /**
    * Encripta la cadena enviada
    * @param strToEncrypt Es la cadena por encriptar
    * @param strPass Es el password
    * @return Regresa la cadena encriptada
    * @throws NoSuchPaddingException
    * @throws NoSuchAlgorithmException
    * @throws InvalidKeyException
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    */
   public String Encripta(String strToEncrypt, String strPass)
           throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
      String strEncriptada = "";
      //Desencripta la llave de base64 a bytes
      byte[] raw = Base64.decodeBase64(strPass.getBytes());

      //Instancia la llave secreta para encriptar en Opalina
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      // Instantiate the cipher
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
      byte[] encrypted =
              cipher.doFinal(strToEncrypt.getBytes());
      //Encripta la frase en base 64
      byte[] b64Enc = Base64.encodeBase64(encrypted);
      strEncriptada = new String(b64Enc);
      return strEncriptada;
   }

   /**
    * Desencripta la palabra
    * @param strToDesEncrypt Es la palabra para desencriptar
    * @param strPass Es el password
    * @return Nos regresa la palabra desencriptada
    * @throws NoSuchAlgorithmException
    * @throws NoSuchPaddingException
    * @throws InvalidKeyException
    * @throws IllegalBlockSizeException
    * @throws BadPaddingException
    */
   public String DesEncripta(String strToDesEncrypt, String strPass)
           throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
      //Desencripta la llave y la palabra de base64 a bytes
      byte[] raw = Base64.decodeBase64(strPass.getBytes());
      byte[] encrypted = Base64.decodeBase64(strToDesEncrypt.getBytes());
      //Instancia la llave secreta para encriptar en Opalina
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      // Instantiate the cipher
      Cipher cipher = Cipher.getInstance("AES");

      cipher.init(Cipher.DECRYPT_MODE, skeySpec);
      byte[] original =
              cipher.doFinal(encrypted);
      String originalString = new String(original);
      return originalString;
   }

   /**
    * Turns array of bytes into string
    *
    * @param buf	Array of bytes to convert to hex string
    * @return	Generated hex string
    */
   public static String asHex(byte buf[]) {
      StringBuilder strbuf = new StringBuilder(buf.length * 2);
      int i;

      for (i = 0; i < buf.length; i++) {
         if (((int) buf[i] & 0xff) < 0x10) {
            strbuf.append("0");
         }

         strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
      }

      return strbuf.toString();
   }
}
