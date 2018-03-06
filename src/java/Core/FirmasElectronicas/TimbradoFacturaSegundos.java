package Core.FirmasElectronicas;

import Core.FirmasElectronicas.pac.facturasegundos.WsGenericResp;
import Core.FirmasElectronicas.pac.facturasegundos.WsGenericRespExt;
import Core.FirmasElectronicas.pac.facturasegundos.WsInsertaClienteResp;
import java.io.File;
import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.logging.log4j.LogManager;

/**
 * Clase que realiza las operaciones de timbrado del PAC Factura en Segundos
 *
 * @author zeus
 */
public class TimbradoFacturaSegundos extends Timbrado_Pacs {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(TimbradoFacturaSegundos.class.getName());

   public TimbradoFacturaSegundos(String strPathProperties) {
      super(strPathProperties + System.getProperty("file.separator") + "configPac1.properties");
      log.debug("strPathProperties:" + strPathProperties);
   }

   @Override
   public String timbra_Factura(String strArchivo) {
      String strResp = "OK";
      log.debug("Archivo por timbrar:" + strArchivo);
      log.debug("this.strUrlService:" + this.strUrlService);
      if (this.strUrlService.isEmpty() || this.strUser.isEmpty() || this.strPassword.isEmpty()) {
         strResp = "ERROR:Falta indicar parametros para el PAC";
      } else {
         if (this.intIdDoc != 0) {
            //Abrimos el archivo del xml a sellar
            String strXml = this.getContent(this.strPathXml + System.getProperty("file.separator") + strArchivo);
            //Enviamos la peticion a sellar
            WsGenericResp resp = timbraEnviaCFDI(this.strUrlService, this.strUser, this.strPassword,
                    this.certificadoEmisor, this.llavePrivadaEmisor,
                    this.strPasswordEmisor, strXml);
            if (resp != null) {
               System.out.println("getNumError " + resp.getNumError());
               System.out.println("getErrorMessage " + resp.getErrorMessage());

               System.out.println("getCadenaOriginal " + resp.getCadenaOriginal());
               System.out.println("getSelloDigitalEmisor " + resp.getSelloDigitalEmisor());

               System.out.println("getFolioUUID " + resp.getFolioUUID());
               System.out.println("getFechaHoraTimbrado " + resp.getFechaHoraTimbrado());
               System.out.println("getSelloDigitalTimbreSAT " + resp.getSelloDigitalTimbreSAT());
               System.out.println("getXML " + resp.getXML());

               //Si el numero de error es cero fue exitoso
               if (resp.getNumError() == 0) {
                  this.strfolioFiscalUUID = resp.getFolioUUID();
                  this.strFechaTimbre = resp.getFechaHoraTimbrado().toString();
                  this.strSelloSAT = resp.getSelloDigitalTimbreSAT();
                  //Actualizamos el documento XML
                  this.updateXmlTimbrado(resp, strArchivo);
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
      //strResp = "OK"; //pruebas 
      return strResp;
   }

   @Override
   public String timbra_Recibo(String strArchivo) {
      String strResp = "OK";
      log.debug("this.strUrlService:" + this.strUrlService);
      if (this.strUrlService.isEmpty() || this.strUser.isEmpty() || this.strPassword.isEmpty()) {
         strResp = "ERROR:Falta indicar parametros para el PAC";
      } else {
         if (this.intIdDoc != 0) {
            //Abrimos el archivo del xml a sellar
            String strXml = this.getContent(this.strPathXml + System.getProperty("file.separator") + strArchivo);
            //Enviamos la peticion a sellar
            WsGenericRespExt resp = timbraEnviaCFDIRetenciones(this.strUrlService, this.strUser, this.strPassword,
                    this.certificadoEmisor, this.llavePrivadaEmisor,
                    this.strPasswordEmisor, strXml);
            if (resp != null) {
               System.out.println("getNumError " + resp.getNumError());
               System.out.println("getErrorMessage " + resp.getErrorMessage());

               System.out.println("getCadenaOriginal " + resp.getCadenaOriginal());
               System.out.println("getSelloDigitalEmisor " + resp.getSelloDigitalEmisor());

               System.out.println("getFolioUUID " + resp.getFolioUUID());
               System.out.println("getFechaHoraTimbrado " + resp.getFechaHoraTimbrado());
               System.out.println("getSelloDigitalTimbreSAT " + resp.getSelloDigitalTimbreSAT());
               System.out.println("getXML " + resp.getXML());

               //Si el numero de error es cero fue exitoso
               if (resp.getNumError() == 0) {
                  this.strfolioFiscalUUID = resp.getFolioUUID();
                  this.strFechaTimbre = resp.getFechaHoraTimbrado().toString();
                  this.strSelloSAT = resp.getSelloDigitalTimbreSAT();
                  //Actualizamos el documento XML
                  this.updateXmlTimbrado(resp, strArchivo);
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
      //strResp = "OK"; //pruebas 
      return strResp;
   }

   private void updateXmlTimbrado(WsGenericResp resp, String strArchivo) {
      try {

         //Actualizamos el documento
         StringBuilder strUpdate = new StringBuilder("Update ");
         strUpdate.append(this.strTablaDoc);
         if (this.isBolNomina()) {
            strUpdate.append(" set ").append(this.strPrefijoDoc).append("UUID = '").append(resp.getFolioUUID()).append("'");
         } else {
            strUpdate.append(" set ").append(this.strPrefijoDoc).append("FOLIO = '").append(resp.getFolioUUID()).append("'");
         }
         strUpdate.append(" ,").append(this.strPrefijoDoc).append("SELLOTIMBRE = '").append(resp.getSelloDigitalTimbreSAT()).append("'");
         strUpdate.append(" ,").append(this.strPrefijoDoc).append("HORA_TIMBRE = '").append(resp.getFechaHoraTimbrado()).append("'");
         strUpdate.append(" where ").append(this.strPrefijoDoc).append("ID = ").append(this.intIdDoc);
         oConn.runQueryLMD(strUpdate.toString());

         //Borramos el archivo temporal anterior
         File filexBorrar = new File(this.strPathXml + strArchivo);
         if (filexBorrar.exists()) {
            try {
               filexBorrar.delete();
            } catch (Exception ex) {
               log.error("No se pudo borrar archivo temporal " + ex.getMessage());
            }
         }
         //Reemplazamos el uuid
         strArchivo = strArchivo.replace("%UUID%", resp.getFolioUUID());

         //Escribimos el archivo
         FileManage.createFileFromByteArray(resp.getXML(),
                 this.strPathXml, "Acuse" + strArchivo.trim().replace(".xml", "") + ".xml");

         //Escribimos el archivo
         FileManage.createFileFromByteArray(resp.getXML(),
                 this.strPathXml, strArchivo);
         //Obtenemos el numero de serie del certificado
         this.getDataXMLCFDI(this.strPathXml + strArchivo);

      } catch (FileNotFoundException ex) {
         log.error(ex.getMessage());
      } catch (IOException ex) {
         log.error(ex.getMessage());
      }
   }

   //Actualiza el timbrado del recibo
   private void updateXmlTimbrado(WsGenericRespExt resp, String strArchivo) {
      try {

         //Actualizamos el documento
         StringBuilder strUpdate = new StringBuilder("Update ");
         strUpdate.append(this.strTablaDoc);
         strUpdate.append(" set ").append(this.strPrefijoDoc).append("UUID = '").append(resp.getFolioUUID()).append("'");
         strUpdate.append(" ,").append(this.strPrefijoDoc).append("SELLOTIMBRE = '").append(resp.getSelloDigitalTimbreSAT()).append("'");
         strUpdate.append(" ,").append(this.strPrefijoDoc).append("HORA_TIMBRE = '").append(resp.getFechaHoraTimbrado()).append("'");
         strUpdate.append(" where ").append(this.strPrefijoDoc).append("ID = ").append(this.intIdDoc);
         oConn.runQueryLMD(strUpdate.toString());

         //Borramos el archivo temporal anterior
         File filexBorrar = new File(this.strPathXml + strArchivo);
         if (filexBorrar.exists()) {
            try {
               filexBorrar.delete();
            } catch (Exception ex) {
               log.error("No se pudo borrar archivo temporal " + ex.getMessage());
            }
         }
         //Reemplazamos el uuid
         strArchivo = strArchivo.replace("%UUID%", resp.getFolioUUID());
         //Escribimos el archivo
         FileManage.createFileFromByteArray(resp.getXML(),
                 this.strPathXml, "Acuse" + strArchivo.trim().replace(".xml", "") + ".xml");

         //Escribimos el archivo
         FileManage.createFileFromByteArray(resp.getXML(),
                 this.strPathXml, strArchivo);
         //Obtenemos el numero de serie del certificado
         this.getDataXMLCFDI(this.strPathXml + strArchivo);

      } catch (FileNotFoundException ex) {
         log.error(ex.getMessage());
      } catch (IOException ex) {
         log.error(ex.getMessage());
      }
   }
   //Envia la peticion al servicio web de certificados

   private static WsGenericResp timbraEnviaCFDI(String strUrlService, java.lang.String user,
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
      return port.timbraEnviaCFDIxp(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI, "3.2");
   }

   //Envia la peticion al servicio web de certificados de retenciones
   private static WsGenericRespExt timbraEnviaCFDIRetenciones(String strUrlService, java.lang.String user,
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
      return port.timbraEnviaRetenciones(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI, null, false, false, "1.0");
   }

   /**
    * Otorga acceso al servicio de timbrado al contribuyente marcado
    *
    * @param contribuyenteRFC Es el rfc del contribuyentex¡
    * @param contribuyenteRazonSocial Es la razón social del contribuyente
    * @return Regresa OK si todo salio bien
    */
   public String OtorgaNuevoAcceso(String contribuyenteRFC, String contribuyenteRazonSocial) {
      String strResult = "OK";
      //Enviamos la peticion a sellar
      WsInsertaClienteResp resp = otorgarAccesoContribuyente(this.strUser, this.strPassword, contribuyenteRFC, contribuyenteRazonSocial);
      if (resp.isIsError()) {
         log.debug("Hubo un error");
         strResult = resp.getErrorMessage() + " " + resp.getNumError();
      }
      return strResult;
   }

   private static WsInsertaClienteResp otorgarAccesoContribuyente(java.lang.String user, java.lang.String userPassword, java.lang.String contribuyenteRFC, java.lang.String contribuyenteRazonSocial) {
      Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService service = new Core.FirmasElectronicas.pac.facturasegundos.InterconectaWsService();
      Core.FirmasElectronicas.pac.facturasegundos.InterconectaWs port = service.getInterconectaWsPort();
      return port.otorgarAccesoContribuyente(user, userPassword, contribuyenteRFC, contribuyenteRazonSocial);
   }
}
