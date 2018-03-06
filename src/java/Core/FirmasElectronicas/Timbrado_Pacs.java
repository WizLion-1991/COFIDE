package Core.FirmasElectronicas;

import comSIWeb.Operaciones.Conexion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

/**
 *Clase abstracta que se toma como base para todas las clases
 * de timbrado de cada PAC
 * @author zeus
 */
public abstract class Timbrado_Pacs {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Timbrado_Pacs.class.getName());
   protected byte[] certificadoEmisor;
   protected byte[] llavePrivadaEmisor;
   protected String strUser;
   protected String strPassword;
   protected String strPasswordEmisor;
   protected String strUrlService;
   protected Properties properties;
   protected String strPathProperties;
   protected String strPathXml;
   protected String strPathKey;
   protected String strTablaDoc;
   protected String strPrefijoDoc;
   protected int intIdDoc = 0;
   protected Conexion oConn;
   protected String strfolioFiscalUUID;
   protected String strFechaTimbre;
   protected String strNoCertSAT;
   protected String strSelloSAT;
   
   private boolean bolNomina = false;

   public boolean isBolNomina() {
      return bolNomina;
   }

   public void setBolNomina(boolean bolNomina) {
      this.bolNomina = bolNomina;
   }
   //Obtenemos propiedades del archivo de configuracion

   private void LoadProperties() {
      properties = new Properties();
      try {
         properties.load(new FileInputStream(strPathProperties));
         this.strUser = properties.getProperty("Usuario");
         this.strPassword = properties.getProperty("Password");
         this.strUrlService = properties.getProperty("Url");
      } catch (IOException e) {
         log.error(e.getMessage() );
      }

   }

   /**
    * Regresa el certificado del emisor
    * @return Es el archivo en formato byte[]
    */
   public byte[] getCertificadoEmisor() {
      return certificadoEmisor;
   }

   /**
    * Define el certificado del emisor
    * @param certificadoEmisor Es el archivo en formato byte[]
    */
   public void setCertificadoEmisor(byte[] certificadoEmisor) {
      this.certificadoEmisor = certificadoEmisor;
   }

   /**
    * Regresa la llave privada del emisor
    * @return Es el archivo en formato byte[]
    */
   public byte[] getLlavePrivadaEmisor() {
      return llavePrivadaEmisor;
   }

   /**
    * Define la llave privada del emisor
    * @param llavePrivadaEmisor Es el archivo en formato byte[]
    */
   public void setLlavePrivadaEmisor(byte[] llavePrivadaEmisor) {
      this.llavePrivadaEmisor = llavePrivadaEmisor;
   }

   /**
    * Regresa el password del emisor
    * @return Regresa una cadena con el password del emisor
    */
   public String getStrPasswordEmisor() {
      return strPasswordEmisor;
   }

   /**
    * Define el password del emisor
    * @param strPasswordEmisor Es una cadena con el password del emisor
    */
   public void setStrPasswordEmisor(String strPasswordEmisor) {
      this.strPasswordEmisor = strPasswordEmisor;
   }

   /**
    * Regresa el path donde se encontrara el CFDI
    * @return Regresa un string con un path
    */
   public String getStrPathXml() {
      return strPathXml;
   }

   /**
    * Define el path donde se encontrara el CFDI
    * @param strPathXml Define un string con un path
    */
   public void setStrPathXml(String strPathXml) {
      this.strPathXml = strPathXml;
   }

   /**
    * Regresa la conexion a usar
    * @return Regresa el objeto conexion
    */
   public Conexion getoConn() {
      return oConn;
   }

   /**
    * Definimos la conexion a usar
    * @param oConn Define la conexion
    */
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   /**
    * Regresa el nombre del prefijo del documento a usar
    * @return
    */
   public String getStrPrefijoDoc() {
      return strPrefijoDoc;
   }

   /**
    * Define el prefijo del documento a usar
    * @param strPrefijoDoc
    */
   public void setStrPrefijoDoc(String strPrefijoDoc) {
      this.strPrefijoDoc = strPrefijoDoc;
   }

   /**
    * Regresa el nombre de la tabla del documento
    * @return Es el nombre de una tabla
    */
   public String getStrTablaDoc() {
      return strTablaDoc;
   }

   /**
    * Define el nombre de la tabla del documento
    * @param strTablaDoc Es el nombre de una tabla
    */
   public void setStrTablaDoc(String strTablaDoc) {
      this.strTablaDoc = strTablaDoc;
   }

   /**
    * Regresa el id del documento por sellar
    * @return Regresa un entero
    */
   public int getIntIdDoc() {
      return intIdDoc;
   }

   /**
    * Es el id del documento por sellar
    * @param intIdDoc Es un entero
    */
   public void setIntIdDoc(int intIdDoc) {
      this.intIdDoc = intIdDoc;
   }

   /**
    * Regresa el folio fiscal del timbrado
    * @return Es un cadena con el folio fiscal
    */
   public String getStrfolioFiscalUUID() {
      return strfolioFiscalUUID;
   }

   /**
    * Define el folio fiscal del timbrado
    * @param strfolioFiscalUUID Es un cadena con el folio fiscal
    */
   public void setStrfolioFiscalUUID(String strfolioFiscalUUID) {
      this.strfolioFiscalUUID = strfolioFiscalUUID;
   }

   /**
    * Regresa la fecha del timbrado
    * @return Es la fecha del timbrado
    */
   public String getStrFechaTimbre() {
      return strFechaTimbre;
   }

   /**
    * Define la fecha del timbrado
    * @param strFechaTimbre Es la fecha del timbrado
    */
   public void setStrFechaTimbre(String strFechaTimbre) {
      this.strFechaTimbre = strFechaTimbre;
   }

   /**
    * Regresa el sello del SAT
    * @return Regresa el sello del SAt
    */
   public String getStrSelloSAT() {
      return strSelloSAT;
   }

   /**
    * Define el sello del SAT
    * @param strSelloSAT Define el sello del SAT
    */
   public void setStrSelloSAT(String strSelloSAT) {
      this.strSelloSAT = strSelloSAT;
   }

   /**
    * Regresa el numero de certificado del SAT
    * @return Es el numero de certificado
    */
   public String getStrNoCertSAT() {
      return strNoCertSAT;
   }

   /**
    * Define el numero de certificado del SAt
    * @param strNoCertSAT Es el numero de certificado
    */
   public void setStrNoCertSAT(String strNoCertSAT) {
      this.strNoCertSAT = strNoCertSAT;
   }

   // </editor-fold>
   
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Timbrado_Pacs() {
      strPathProperties = "";
      LoadProperties();
   }

   public Timbrado_Pacs(String strPathProperties) {
      this.strPathProperties = strPathProperties;
      LoadProperties();
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos Timbrado">
   public abstract String timbra_Factura(String strArchivo);
   public abstract String timbra_Recibo(String strArchivo);

   /**
    * Nos regresa el contenido del archivo indicado
    * @param strFile Es el path del archivo por leer
    * @return Regresa una cadena con el contenido del archivo en utf-8
    */
   protected String getContent(String strFile) {
      StringBuilder strRes = new StringBuilder();
      try {
         File fileDir = new File(strFile);

         BufferedReader in = new BufferedReader(
                 new InputStreamReader(
                 new FileInputStream(fileDir), "UTF8"));
         String str;
         while ((str = in.readLine()) != null) {
            System.out.println(str);
            strRes.append(str);
         }
         in.close();
      } catch (UnsupportedEncodingException e) {
         System.out.println(e.getMessage());
      } catch (IOException e) {
         System.out.println(e.getMessage());
      } catch (Exception e) {
         System.out.println(e.getMessage());
      }
      return strRes.toString();
   }

   /**
    * Recupera información del CFDI
    * @param strFile Es el path del cfdi
    */
   protected void getDataXMLCFDI(String strFile) {
      this.strNoCertSAT = "";
      try {
         File fXmlFile = new File(strFile);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         dbFactory.setNamespaceAware(true);
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(fXmlFile);
         doc.setStrictErrorChecking(false);
         NodeList nList = doc.getDocumentElement().getChildNodes();
         if (nList != null) {
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               //Facturas
               if (nNode.getNodeName().equals("cfdi:Complemento")) {
                  if (nNode.hasChildNodes()) {
                     NodeList nList2 = nNode.getChildNodes();
                     for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
                        Node nNode2 = nList2.item(temp2);
                        if (nNode2.getNodeName().equals("tfd:TimbreFiscalDigital")) {
                           NamedNodeMap atts = nNode2.getAttributes();
                           if (atts != null) {
                              this.strNoCertSAT = atts.getNamedItem("noCertificadoSAT").getNodeValue();
                           }
                        }
                     }
                  }
               }
               //Retenciones
               if (nNode.getNodeName().equals("retenciones:Complemento")) {
                  if (nNode.hasChildNodes()) {
                     NodeList nList2 = nNode.getChildNodes();
                     for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
                        Node nNode2 = nList2.item(temp2);
                        if (nNode2.getNodeName().equals("tfd:TimbreFiscalDigital")) {
                           NamedNodeMap atts = nNode2.getAttributes();
                           if (atts != null) {
                              this.strNoCertSAT = atts.getNamedItem("noCertificadoSAT").getNodeValue();
                           }
                        }
                     }
                  }
               }
               
               
               
            }
         }

      } catch (Exception e) {
         System.out.println("error " + e.getMessage());
      }
   }
   // </editor-fold>
}
