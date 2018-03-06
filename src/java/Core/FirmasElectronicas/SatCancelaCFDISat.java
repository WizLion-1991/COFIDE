/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas;

import Core.FirmasElectronicas.SAT3_2.cancelacion.Acuse;
import Core.FirmasElectronicas.SAT3_2.cancelacion.Cancelacion;
import Core.FirmasElectronicas.SAT3_2.cancelacion.CanonicalizationMethodType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.DigestMethodType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.KeyInfoType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.ReferenceType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.SignatureMethodType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.SignatureType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.SignedInfoType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.X509DataType;
import Core.FirmasElectronicas.SAT3_2.cancelacion.X509IssuerSerialType;
import Core.FirmasElectronicas.Utils.UtilCert;
import Core.FirmasElectronicas.pac.facturasegundos.WsGenericResp;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ZeusGalindo
 */
public class SatCancelaCFDISat extends SatCancelaCFDI {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strRFC;
   private String strUUID;
   private String strPassKey;

   public String getStrRFC() {
      return strRFC;
   }

   public void setStrRFC(String strRFC) {
      this.strRFC = strRFC;
   }

   public String getStrUUID() {
      return strUUID;
   }

   public void setStrUUID(String strUUID) {
      this.strUUID = strUUID;
   }

   public String getStrPassKey() {
      return strPassKey;
   }

   public void setStrPassKey(String strPassKey) {
      this.strPassKey = strPassKey;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public SatCancelaCFDISat(String strPathProperties) {
      super(strPathProperties);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   @Override
   public String timbra_Factura(String strArchivo) {
      String strResp = "OK";
      DatatypeFactory df = null;
      try {
         df = DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException dce) {
         throw new IllegalStateException(
                 "Exception while obtaining DatatypeFactory instance", dce);
      }

      if (!this.strUUID.isEmpty()) {
         //Abrimos el archivo del xml a sellar
         Core.FirmasElectronicas.SAT3_2.cancelacion.Cancelacion cancelacion = new Core.FirmasElectronicas.SAT3_2.cancelacion.Cancelacion();

         //Fecha
         GregorianCalendar gc = new GregorianCalendar();
         gc.setTimeInMillis(new Date().getTime());


         // 1. Create a canonicalized version of the data-to-be-signed

         // (Note no whitespace between elements)
         String strDataTBS = "<Cancelacion xmlns=\"http://cancelacfd.sat.gob.mx\""
                 + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                 + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                 + " Fecha=\"%FECHA%\" RfcEmisor=\"%RFC%\">"
                 + "<Folios><UUID>%UUID%</UUID></Folios></Cancelacion>";

         // Substitute our input values...
         strDataTBS = strDataTBS.replace("%FECHA%", "2013-10-09T14:14:40");
         strDataTBS = strDataTBS.replace("%RFC%", strRFC);
         strDataTBS = strDataTBS.replace("%UUID%", strUUID);
         System.out.println(strDataTBS);

         // 2. Compute the SHA-1 message digest value of this in base64 encoding
         // (because the string is only ASCII characters, we can hash the string directly)
         String strDigestBase64 = null;
         byte[] b64Enc = null;
         try {
            b64Enc = Base64.encodeBase64(SatCancelaCFDISat.sha1(strDataTBS));
            strDigestBase64 = new String(b64Enc);
         } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SatCancelaCFDISat.class.getName()).log(Level.SEVERE, null, ex);
         }
         System.out.println("SHA-1(DATA)=" + strDigestBase64);
         System.out.println(strDigestBase64);

         // 3. Create a canonicalized version of the SignedInfo element

         String strSignedInfoCanon = "<SignedInfo xmlns=\"http://www.w3.org/2000/09/xmldsig#\""
                 + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                 + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                 + "<CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></CanonicalizationMethod>"
                 + "<SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod>"
                 + "<Reference URI=\"\">"
                 + "<Transforms>"
                 + "<Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></Transform>"
                 + "</Transforms>"
                 + "<DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod>"
                 + "<DigestValue>%DIGESTVALUE%</DigestValue>"
                 + "</Reference>"
                 + "</SignedInfo>";

         // Substitute the base64 DigestValue we computed above
         strSignedInfoCanon = strSignedInfoCanon.replace("%DIGESTVALUE%", strDigestBase64);
         // Compute SHA-1 digest of this canonicalized SignedInfo
         String strDigestHex = null;
         try {
            strDigestHex = bytesToHex(SatCancelaCFDISat.sha1(strSignedInfoCanon));
         } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SatCancelaCFDISat.class.getName()).log(Level.SEVERE, null, ex);
         }
         System.out.println("SHA-1(SIGNEDINFO)=" + strDigestHex);

         String strSignature64 = "";
         byte[] b64Enc2 = null;
         try {
            PrivateKey key;
            key = ObtenerPrivateKey(this.strPATHKeys, this.strPassKey);
            if (key == null) {
               System.out.println("No se pudo abrir el sello");
            } else {
               //Guardamos la llave para los pacs
               this.llavePrivadaEmisor = key.getEncoded();
               //Sellamos
               try {
                  /*Codigo generico*/
                  byte[] data = strSignedInfoCanon.getBytes("UTF8");
                  Signature sig = Signature.getInstance("SHA1withRSA");
                  sig.initSign(key);
                  sig.update(data);
                  //Sellamos
                  byte[] signatureBytes = sig.sign();
                  //Convertimos a base 64
                  b64Enc2 = Base64.encodeBase64(signatureBytes);
                  strSignature64 = new String(b64Enc2);
                  sig = null;
                  /*Codigo generico*/
               } catch (IOException ex) {
                  Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
         } catch (SignatureException ex) {
            ex.fillInStackTrace();
            System.out.println(" " + ex.getMessage());
         } catch (InvalidKeyException ex) {
            ex.fillInStackTrace();
            System.out.println(" " + ex.getMessage());
         } catch (NoSuchAlgorithmException ex) {
            ex.fillInStackTrace();
            System.out.println(" " + ex.getMessage());
         }

         System.out.println("strSignature64:" + strSignature64);

         
         //Certificado digital
         UtilCert cert = new UtilCert();
         String strCertString64 = "";
         cert.OpenCert("/Users/ZeusGalindo/Documents/Zeus/SAT/PEREZCOLMENARES/Cer_Sello/cert_file1.cer");
         if (!cert.getStrResult().startsWith("ERROR")) {
            strCertString64 = cert.getCertContentBase64();
            try {
               this.certificadoEmisor = cert.getCert().getEncoded();
            } catch (CertificateEncodingException ex) {
               Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
          System.out.println(strCertString64);

         //Parametros de la peticion
         cancelacion.setFecha(df.newXMLGregorianCalendar("2013-10-09T14:14:40"));
         cancelacion.setRfcEmisor(this.strRFC);

         //SignatureType
         SignatureType signature = new SignatureType();
         signature.setSignatureValue(b64Enc2);
         //KeyInfoType
         KeyInfoType keyInfoType = new KeyInfoType();
         X509DataType x509DataType = new X509DataType();
         x509DataType.setX509Certificate(certificadoEmisor);
         X509IssuerSerialType x509IssuerSerialType = new X509IssuerSerialType();
         x509IssuerSerialType.setX509IssuerName("1.2.840.113549.1.9.2=Responsable: Cecilia Guillermina García Guerra,2.5.4.45=SAT970701NN3,L=Cuauhtémoc,ST=Distrito Federal,C=MX,2.5.4.17=06300,STREET=Av. Hidalgo 77\\, Col. Guerrero,1.2.840.113549.1.9.1=asisnet@sat.gob.mx,OU=Administración de Seguridad de la Información,O=Servicio de Administración Tributaria,CN=A.C. del Servicio de Administración Tributaria");
         BigInteger bi = new BigInteger("275106190557734483187066766774039376570516649265");
         x509IssuerSerialType.setX509SerialNumber(bi);
         x509DataType.setX509IssuerSerial(x509IssuerSerialType);
         keyInfoType.setId(x509DataType.toString());
         signature.setKeyInfo(keyInfoType);
         
         
         //SignedInfoType
         SignedInfoType signedInfoType = new SignedInfoType();
         SignatureMethodType signatureMethodType = new SignatureMethodType();
         signatureMethodType.setAlgorithm("http://www.w3.org/2000/09/xmldsig#rsa-sha1");
         CanonicalizationMethodType canonicalizationMethodType = new CanonicalizationMethodType();
         canonicalizationMethodType.setAlgorithm("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
         signedInfoType.setCanonicalizationMethod(canonicalizationMethodType);
         signedInfoType.setSignatureMethod(signatureMethodType);
         ReferenceType referenceType = new ReferenceType();
         DigestMethodType digestMethodType = new DigestMethodType();
         digestMethodType.setAlgorithm("http://www.w3.org/2000/09/xmldsig#sha1");
         referenceType.setDigestMethod(digestMethodType);
         referenceType.setDigestValue(b64Enc);
         signedInfoType.setReference(referenceType);
         signature.setSignedInfo(signedInfoType);

         cancelacion.setSignature(signature);
         Cancelacion.Folios folio = new Cancelacion.Folios();
         folio.setUUID(this.strUUID);
         cancelacion.getFolios().add(folio);

         Acuse acuse = cancelaCFD(cancelacion);
         System.out.println(" acuse " + acuse.getRfcEmisor());
         System.out.println(" acuse " + acuse.getCodEstatus());
         //Iteramos por los folios de respuesta
         Iterator<Acuse.Folios> it = acuse.getFolios().iterator();
         while (it.hasNext()) {
            Acuse.Folios folioQ = it.next();
            System.out.println("folio " + folioQ.getUUID() + " " + folioQ.getEstatusUUID());
         }
         strResp = acuse.getCodEstatus();


      } else {
         strResp = "ERROR:Falta indicar el UIID por actualizar";
      }
      return strResp;
   }
   // </editor-fold>

   private static Acuse cancelaCFD(Core.FirmasElectronicas.SAT3_2.cancelacion.Cancelacion cancelacion) {
      Core.FirmasElectronicas.SAT3_2.cancelacion.CancelaCFDService service = new Core.FirmasElectronicas.SAT3_2.cancelacion.CancelaCFDService();
      Core.FirmasElectronicas.SAT3_2.cancelacion.ICancelaCFDBinding port = service.getBasicHttpBindingICancelaCFDBinding();
      return port.cancelaCFD(cancelacion);
   }

   static byte[] sha1(String input) throws NoSuchAlgorithmException {
      MessageDigest mDigest = MessageDigest.getInstance("SHA1");
      mDigest.update(input.getBytes());
      byte[] output = mDigest.digest();
      return output;
   }

   public static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j = 0; j < b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   }

   public void callWS(String xmlInput) throws MalformedURLException, IOException {
      //Code to make a webservice HTTP request
      String responseString = "";
      String outputString = "";
      String wsURL = "https://cancelacion.facturaelectronica.sat.gob.mx:443/Cancelacion/CancelaCFDService.svc?singleWsdl";
      URL url = new URL(wsURL);
      URLConnection connection = url.openConnection();
      HttpURLConnection httpConn = (HttpURLConnection) connection;
      ByteArrayOutputStream bout = new ByteArrayOutputStream();

      byte[] buffer = new byte[xmlInput.length()];
      buffer = xmlInput.getBytes();
      bout.write(buffer);
      byte[] b = bout.toByteArray();
      String SOAPAction =
              "http://cancelacfd.sat.gob.mx/ICancelaCFDBinding/CancelaCFD";
      // Set the appropriate HTTP parameters.
      httpConn.setRequestProperty("Content-Length",
              String.valueOf(b.length));
      httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
      httpConn.setRequestProperty("SOAPAction", SOAPAction);
      httpConn.setRequestMethod("POST");
      httpConn.setDoOutput(true);
      httpConn.setDoInput(true);
      OutputStream out = httpConn.getOutputStream();
      //Write the content of the request to the outputstream of the HTTP Connection.
      out.write(b);
      out.close();
      //Ready with sending the request.

      //Read the response.
      InputStreamReader isr =
              new InputStreamReader(httpConn.getInputStream());
      BufferedReader in = new BufferedReader(isr);

      //Write the SOAP message response to a String.
      while ((responseString = in.readLine()) != null) {
         outputString = outputString + responseString;
      }
      //Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
//    Document document = parseXmlFile(outputString);
//    NodeList nodeLst = document.getElementsByTagName("GetWeatherResult");
//    String weatherResult = nodeLst.item(0).getTextContent();
//    System.out.println("Weather: " + weatherResult);

      //Write the SOAP message formatted to the console.
      String formattedSOAPResponse = formatXML(outputString);
      System.out.println(formattedSOAPResponse);
   }
   //format the XML in your String

   public String formatXML(String unformattedXml) {
      try {
         Document document = parseXmlFile(unformattedXml);
         OutputFormat format = new OutputFormat(document);
         format.setIndenting(true);
         format.setIndent(3);
         format.setOmitXMLDeclaration(true);
         Writer out = new StringWriter();
         XMLSerializer serializer = new XMLSerializer(out, format);
         serializer.serialize((org.w3c.dom.Document) document);
         return out.toString();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private Document parseXmlFile(String in) {
      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();
         InputSource is = new InputSource(new StringReader(in));
         return db.parse(is);
      } catch (ParserConfigurationException e) {
         throw new RuntimeException(e);
      } catch (SAXException e) {
         throw new RuntimeException(e);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Obtenemos la llave privada proporcionada por SAT
    *
    * @param strPath Es el path de la llave privada
    * @param strPass Es el password
    * @return Regresa el valor de la llave
    */
   public PrivateKey ObtenerPrivateKey(String strPath, String strPass) {
      FileInputStream in = null;//"C:/sat/Cer_Sello/aaa010101aaa_CSD_01.key"
      // A Java PrivateKey object is born.
      PrivateKey pk = null;
      boolean bolError = false;
      try {
         in = new FileInputStream(strPath);
      } catch (FileNotFoundException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
         bolError = true;
      }
      if (in != null) {
         // If the provided InputStream is encrypted, we need a password to decrypt
         // it. If the InputStream is not encrypted, then the password is ignored
         // (can be null).  The InputStream can be DER (raw ASN.1) or PEM (base64).
         PKCS8Key pkcs8 = null;

         try {
            pkcs8 = new PKCS8Key(in, strPass.toCharArray());
         } catch (GeneralSecurityException ex) {
            ex.fillInStackTrace();
            bolError = true;
         } catch (IOException ex) {
            ex.fillInStackTrace();
            bolError = true;
         }
         //Si no hubo error proseguimos
         if (!bolError) {
            // If an unencrypted PKCS8 key was provided, then this actually returns
            // exactly what was originally passed in (with no changes).  If an OpenSSL
            // key was provided, it gets reformatted as PKCS #8 first, and so these
            // bytes will still be PKCS #8, not OpenSSL.

            byte[] decrypted = pkcs8.getDecryptedBytes();
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decrypted);
            if (pkcs8.isRSA()) {
               try {
                  try {
                     pk = KeyFactory.getInstance("RSA").generatePrivate(spec);
                     pk = pkcs8.getPrivateKey();
                  } catch (InvalidKeySpecException ex) {
                     ex.fillInStackTrace();
                  }
               } catch (NoSuchAlgorithmException ex) {
                  ex.fillInStackTrace();
               }
            }
            // For lazier types (like me):
            pk = pkcs8.getPrivateKey();
         }
      }
      return pk;
   }
}
