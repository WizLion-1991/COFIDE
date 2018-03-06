package Core.FirmasElectronicas;

import Core.FirmasElectronicas.pac.facturasegundos.WsGenericResp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Clase que realiza la cancelacion de un CFDI
 *
 * @author zeus
 */
public class SatCancelaCFDI extends Timbrado_Pacs {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SatCancelaCFDI.class.getName());
   private String strTipoComprobante;
   protected String strPATHXml;
   protected String strPATHKeys;

   /**
    * Regresa el tipo de comprobante por cancelar
    *
    * @return Regresa el tipo en base a las constantes del objeto TICKET
    */
   public String getStrTipoComprobante() {
      return strTipoComprobante;
   }

   /**
    * Define el tipo de comprobante por cancelar
    *
    * @param strTipoComprobante Define el tipo en base a las constantes del
    * objeto TICKET
    */
   public void setStrTipoComprobante(String strTipoComprobante) {
      this.strTipoComprobante = strTipoComprobante;
   }

   /**
    * Es el path donde guardamos las llaves privadas
    *
    * @return Regresa una cadena
    */
   public String getStrPATHKeys() {
      return strPATHKeys;
   }

   /**
    * Es el path donde guardamos las llaves privadas
    *
    * @param strPATHKeys Regresa una cadena
    */
   public void setStrPATHKeys(String strPATHKeys) {
      this.strPATHKeys = strPATHKeys;
   }

   /**
    * Es el path donde guardamos los xml de las facturas electronica
    *
    * @return Regresa una cadena
    */
   public String getStrPATHXml() {
      return strPATHXml;
   }

   /**
    * Es el path donde guardamos los xml de las facturas electronica
    *
    * @param strPATHXml Regresa una cadena
    */
   public void setStrPATHXml(String strPATHXml) {
      this.strPATHXml = strPATHXml;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public SatCancelaCFDI(String strPathProperties) {
      super(strPathProperties + System.getProperty("file.separator") + "configPac1.properties");
   }
   // </editor-fold>

   /**Envia peticion de cancelacion de un CFDI*/
   private static WsGenericResp cancelaEnviaCFDI(String strUrlService, java.lang.String user,
           java.lang.String userPassword,
           byte[] certificadoEmisor,
           byte[] llavePrivadaEmisor,
           java.lang.String llavePrivadaEmisorPassword,
           java.lang.String xmlCFDI) {
      //Qname
      QName qName = new QName("http://ws.interconecta.tsp.com/", "InterconectaWsService");
      //Url
      URL url = null;
      Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService service = null;

      try {
         url = new URL(strUrlService);
         service = new Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService(url, qName);
      } catch (MalformedURLException ex) {
         service = new Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService();
      } catch (Exception ex) {
         log.error(ex.getMessage());
         return null;
      }
      Core.FirmasElectronicas.pac.facturasegundos.InterconectaWs port = service.getInterconectaWsPort();
      return port.cancelaCFDI32(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI);
   }
   /**Envia peticion de cancelacion de un retencion*/
   private static WsGenericResp cancelaEnviaCFDIRecibos(String strUrlService, java.lang.String user,
           java.lang.String userPassword,
           byte[] certificadoEmisor,
           byte[] llavePrivadaEmisor,
           java.lang.String llavePrivadaEmisorPassword,
           java.lang.String xmlCFDI) {
      //Qname
      QName qName = new QName("http://ws.interconecta.tsp.com/", "InterconectaWsService");
      //Url
      URL url = null;
      Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService service = null;

      try {
         url = new URL(strUrlService);
         service = new Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService(url, qName);
      } catch (MalformedURLException ex) {
         service = new Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService();
      } catch (Exception ex) {
         return null;
      }
      Core.FirmasElectronicas.pac.facturasegundos.InterconectaWs port = service.getInterconectaWsPort();
      return port.cancelaCFDIRetenciones(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI,"","1.0");
   }

   @Override
   public String timbra_Factura(String strArchivo) {
      String strResp = "OK";
      //Removemos la addenda
      this.QuitarAddenda(this.strPathXml + System.getProperty("file.separator") + strArchivo);
      //Validamos el servicio
      if (this.strUrlService.isEmpty() || this.strUser.isEmpty() || this.strPassword.isEmpty()) {
         strResp = "ERROR:Falta indicar parametros para el PAC";
      } else {
         if (this.intIdDoc != 0) {
            //Abrimos el archivo del xml a sellar
            String strXml = this.getContent(this.strPathXml + System.getProperty("file.separator") + strArchivo);
            //Enviamos la peticion a sellar
            WsGenericResp resp = cancelaEnviaCFDI(this.strUrlService, this.strUser, this.strPassword,
                    this.certificadoEmisor, this.llavePrivadaEmisor,
                    this.strPasswordEmisor, strXml);
            if (resp != null) {
               System.out.println("getNumError " + resp.getNumError());
               System.out.println("getErrorMessage " + resp.getErrorMessage());

               System.out.println("getCadenaOriginal " + resp.getCadenaOriginal());
               System.out.println("getSelloDigitalEmisor " + resp.getSelloDigitalEmisor());

               System.out.println("getFolioCodCancelacion " + resp.getFolioCodCancelacion());
               System.out.println("getFolioUUID " + resp.getFolioUUID());
               System.out.println("getFechaHoraTimbrado " + resp.getFechaHoraTimbrado());
               System.out.println("getSelloDigitalTimbreSAT " + resp.getSelloDigitalTimbreSAT());
               System.out.println("getXML " + resp.getXML());

               //Si el numero de error es cero fue exitoso
               if (resp.getNumError() == 0) {
                  if (resp.getFolioCodCancelacion().equals("205")) {
                     strResp = "ERROR:El cfdi aun no se encuentra en los registros del SAT intente mas tarde nuevamente.";
                  } else {
                     //Actualizamos el documento XML
                     this.updateXmlTimbrado(resp, strArchivo);
                  }
               } else {
                  strResp = resp.getErrorMessage();
               }
            } else {
               strResp = "ERROR:No se pudo conectar al PAC";
            }
         } else {
            strResp = "ERROR:Falta indicar el id por actualizar";
         }
      }
      return strResp;
   }

   private void updateXmlTimbrado(WsGenericResp resp, String strArchivo) {
      try {

         //Actualizamos el documento
         StringBuilder strUpdate = new StringBuilder("Update  ");
         strUpdate.append(this.strTablaDoc);
         strUpdate.append(" set ").append(this.strPrefijoDoc).append("FOLIO_ANUL = '").append(resp.getFolioCodCancelacion()).append("'");
         strUpdate.append(" ,").append(this.strPrefijoDoc).append("SELLOTIMBRE_ANUL = '").append(resp.getSelloDigitalTimbreSAT()).append("'");
         strUpdate.append(" ,").append(this.strPrefijoDoc).append("HORA_TIMBRE_ANUL = '").append(resp.getFechaHoraTimbrado()).append("'");
         strUpdate.append(" where ").append(this.strPrefijoDoc).append("ID = ").append(this.intIdDoc);
         oConn.runQueryLMD(strUpdate.toString());

         //Escribimos el archivo
         FileManage.createFileFromByteArray(resp.getXML(),
                 this.strPathXml, "Acuse_Cancel" + strArchivo.trim().replace(".xml", "") + ".xml");

      } catch (FileNotFoundException ex) {
         log.error(ex.getMessage());
      } catch (IOException ex) {
         log.error(ex.getMessage());
      }
   }

   @Override
   public String timbra_Recibo(String strArchivo) {
      String strResp = "OK";
      if (this.strUrlService.isEmpty() || this.strUser.isEmpty() || this.strPassword.isEmpty()) {
         strResp = "ERROR:Falta indicar parametros para el PAC";
      } else {
         if (this.intIdDoc != 0) {
            //Abrimos el archivo del xml a sellar
            String strXml = this.getContent(this.strPathXml + System.getProperty("file.separator") + strArchivo);
            //Enviamos la peticion a sellar
            WsGenericResp resp = cancelaEnviaCFDIRecibos(this.strUrlService, this.strUser, this.strPassword,
                    this.certificadoEmisor, this.llavePrivadaEmisor,
                    this.strPasswordEmisor, strXml);
            if (resp != null) {
               System.out.println("getNumError " + resp.getNumError());
               System.out.println("getErrorMessage " + resp.getErrorMessage());

               System.out.println("getCadenaOriginal " + resp.getCadenaOriginal());
               System.out.println("getSelloDigitalEmisor " + resp.getSelloDigitalEmisor());

               System.out.println("getFolioCodCancelacion " + resp.getFolioCodCancelacion());
               System.out.println("getFolioUUID " + resp.getFolioUUID());
               System.out.println("getFechaHoraTimbrado " + resp.getFechaHoraTimbrado());
               System.out.println("getSelloDigitalTimbreSAT " + resp.getSelloDigitalTimbreSAT());
               System.out.println("getXML " + resp.getXML());

               //Si el numero de error es cero fue exitoso
               if (resp.getNumError() == 0) {
                  if (resp.getFolioCodCancelacion().equals("205")) {
                     strResp = "ERROR:El cfdi aun no se encuentra en los registros del SAT intente mas tarde nuevamente.";
                  } else {
                     //Actualizamos el documento XML
                     this.updateXmlTimbrado(resp, strArchivo);
                  }
               } else {
                  strResp = resp.getErrorMessage();
               }
            } else {
               strResp = "ERROR:No se pudo conectar al PAC";
            }
         } else {
            strResp = "ERROR:Falta indicar el id por actualizar";
         }
      }
      return strResp;
   }
   
   /**
    *Quita la addenda en caso de encontrarla
    * @param strPathRemove Es el path del Xml con la addenda
    */
   public void QuitarAddenda(String strPathRemove) {
      boolean bolFoundAddenda = false;
      try {
         File fXmlFile = new File(strPathRemove);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         dbFactory.setNamespaceAware(true);
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
         doc.setStrictErrorChecking(false);
         NodeList nList = doc.getDocumentElement().getChildNodes();
         Node nNodeAddenda = null;
         if (nList != null) {
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               log.debug("nNode:" + nNode);
               if (nNode.getNodeName().equals("cfdi:Addenda")) {
                  bolFoundAddenda = true;
                  nNodeAddenda = nNode;
               }//Fin IF cfdi:Complemento
            }//Fin FOR
         }//Fin IF nList != null
         //Save XML
         if (bolFoundAddenda) {
            log.debug("Encontro Addenda...");
            doc.removeChild(nNodeAddenda);//Quitamos el nodo de addenda
            DOMSource source = new DOMSource(doc);
            // PrintStream will be responsible for writing
            // the text data to the file
            PrintStream ps = new PrintStream(strPathRemove);
            StreamResult result = new StreamResult(ps);
            // Once again we are using a factory of some sort,
            // this time for getting a Transformer instance,
            // which we use to output the XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // The actual output to a file goes here
            transformer.transform(source, result);
         }

      } catch (ParserConfigurationException e) {
         log.error("error " + e.getMessage());
      } catch (SAXException e) {
         log.error("error " + e.getMessage());
      } catch (IOException e) {
         log.error("error " + e.getMessage());
      } catch (DOMException e) {
         log.error("error " + e.getMessage());
      } catch (IllegalArgumentException e) {
         log.error("error " + e.getMessage());
      } catch (TransformerException e) {
         log.error("error " + e.getMessage());
      }
   }
}
