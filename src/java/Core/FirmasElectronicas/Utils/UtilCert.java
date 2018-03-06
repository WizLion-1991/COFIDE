package Core.FirmasElectronicas.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import org.apache.commons.ssl.Base64;

/**
 *Clase que abre certificados *.cer y nos regresa el texto del mismo en base 64
 * @author zeus
 */
public class UtilCert {

   private Certificate cert;
   private String strResult;

   /**
    * Constructor
    */
   public UtilCert() {
      this.strResult = "";
   }

   /**
    * Abre un certificado *.cer
    * @param strPath Es el path del archivo del certificado por abrir
    */
   public void OpenCert(String strPath) {
      File file = new File(strPath);
      if (file.exists()) {
         try {
            FileInputStream is = new FileInputStream(file);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            this.cert = cf.generateCertificate(is);
         } catch (CertificateException e) {
            this.strResult = "ERROR:CertificateException " + e.getMessage();
         } catch (IOException e) {
            this.strResult = "ERROR:IOException " + e.getMessage();
         }
      }
   }

   /**
    * Elimina el objeto del certificado
    */
   public void CloseCert() {
      this.cert = null;
   }

   /**
    * Regresa el contenido en base 64 en tipo texto
    * @return Es una cadena con el contenido del certificado
    */
   public String getCertContentBase64() {
      String strContent = "";
      //Convertimos a base 64
      byte[] b64Enc;
      try {
         b64Enc = Base64.encodeBase64(cert.getEncoded());
         strContent = new String(b64Enc);
      } catch (CertificateEncodingException ex) {
         this.strResult = "ERROR:CertificateException " + ex.getMessage();
      }
      return strContent;
   }

   /**
    * Obtiene el certificado
    * @return Regresa el certificado
    */
   public Certificate getCert() {
      return cert;
   }

   /**
    * Define el certificado
    * @param cert Es el certificado
    */
   public void setCert(Certificate cert) {
      this.cert = cert;
   }

   /**
    * Regresa el mensaje de la ultima accion
    * @return Es el resultado
    */
   public String getStrResult() {
      return strResult;
   }

   /**
    * Define el mensaje de la ultima accion
    * @param strResult Es el resultado
    */
   public void setStrResult(String strResult) {
      this.strResult = strResult;
   }

}
