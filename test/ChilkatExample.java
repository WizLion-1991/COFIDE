
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.ssl.Base64;

/**
 *
 * @author zeus
 */
public class ChilkatExample {

   public static void main(String argv[]) {
      File file1 = new File("i:/00001000000102324829.cer");
      java.security.cert.Certificate cert = importCertificate(file1);
      System.out.println("" + cert.toString());

      //Convertimos a base 64
      byte[] b64Enc;
      try {
         b64Enc = Base64.encodeBase64(cert.getEncoded());
         String strCert = new String(b64Enc);
         System.out.println("strCert:" + strCert);
      } catch (CertificateEncodingException ex) {
         Logger.getLogger(ChilkatExample.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   public static java.security.cert.Certificate importCertificate(File file) {
      try {
         FileInputStream is = new FileInputStream(file);
         CertificateFactory cf = CertificateFactory.getInstance("X.509");
         java.security.cert.Certificate cert = cf.generateCertificate(is);
         return cert;
      } catch (CertificateException e) {
      } catch (IOException e) {
      }
      return null;
   }
}
