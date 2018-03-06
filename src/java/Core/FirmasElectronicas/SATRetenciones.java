package Core.FirmasElectronicas;

import Core.FirmasElectronicas.Utils.UtilCert;
import ERP.Ticket;
import com.mx.siweb.sat.retenciones.*;
import Tablas.Rhh_Ret_Retenciones;
import com.mx.siweb.sat.retenciones.Retenciones.Complemento;
import com.mx.siweb.sat.retenciones.Retenciones.Emisor;
import com.mx.siweb.sat.retenciones.Retenciones.Periodo;
import com.mx.siweb.sat.retenciones.Retenciones.Receptor;
import com.mx.siweb.sat.retenciones.Retenciones.Receptor.Extranjero;
import com.mx.siweb.sat.retenciones.Retenciones.Receptor.Nacional;
import com.mx.siweb.sat.retenciones.Retenciones.Totales;
import com.mx.siweb.sat.retenciones.Retenciones.Totales.ImpRetenidos;
import com.mx.siweb.sat.retenciones.dividendos.Dividendos;
import com.mx.siweb.sat.retenciones.dividendos.Dividendos.DividOUtil;
import com.mx.siweb.sat.retenciones.dividendos.Dividendos.Remanente;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.NumberString;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.ssl.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase se encarga del proceso de timbrado de las retenciones
 *
 * @author ZeusGalindo
 */
public class SATRetenciones {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SATRetenciones.class.getName());
   protected byte[] certificadoEmisor;
   protected byte[] llavePrivadaEmisor;
   protected SATXml3_0.PACS_CFDIS enumPAC;
   protected String strPathConfigPAC;
   protected int intTransaccion = 0;
   protected String strCadenaOriginalReal;
   protected String strPath;
   protected String NoSerieCert;
   protected String strPathCerts;
   protected String strPathKey;
   protected String strPathCert;
   protected String strPassKey;
   protected String strPassKeyMaster;
   protected Bitacora bitacora;
   protected VariableSession varSesiones;
   protected Fechas fecha;
   protected String strPATHFonts;
   protected String strCertificadoBase64;
   private ObjectFactory objFactory;
   private boolean bolUsaDividendos;
   private Retenciones cfdiRet;
   private String strSelloCFD;
   private String strfolioFiscalUUID;
   private String strFechaTimbre;
   private String strSelloSAT;
   private String strNoCertSAT;
   private String strFilePathXml;
   private String strNumSerieCert;
   private String strPathQR;
   private String strEMP_RFC;
   private String strEMP_RAZONSOCIAL;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   /**
    * Regresa el PAC a usar para Timbrar la factura
    *
    * @return Regresa un el valor de la constante del PAC
    */
   public SATXml3_0.PACS_CFDIS getEnumPAC() {
      return enumPAC;
   }

   /**
    * Define el PAC a usar para Timbrar la factura
    *
    * @param enumPAC Es la constante del PAC a usar
    */
   public void setEnumPAC(SATXml3_0.PACS_CFDIS enumPAC) {
      this.enumPAC = enumPAC;
   }

   /**
    * Regresa la url donde se ubica el archivo de configuración del PAC
    *
    * @return Es una cadena con un path
    */
   public String getStrPathConfigPAC() {
      return strPathConfigPAC;
   }

   /**
    * Define el path donde se ubica el archivo de configuracion del PAC
    *
    * @param strPathConfigPAC Es una cadena con un path
    */
   public void setStrPathConfigPAC(String strPathConfigPAC) {
      this.strPathConfigPAC = strPathConfigPAC;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor del objeto SAT que realiza la firma electrnica
    *
    * @param intTransaccion Es el numero de transaccion
    * @param strPath Es el path dnde guardaremos el XML
    * @param strPathCerts Es la path donde esta la llave
    * @param strPassKeyMaster Es el password donde de la llave
    * @param varSesiones Son las variables de sesion
    * @param oConn Es la conexin a la bd
    */
   public SATRetenciones(int intTransaccion, String strPath,
           String strPathCerts, String strPassKeyMaster,
           VariableSession varSesiones, Conexion oConn) {
      this.enumPAC = SATXml3_0.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
      this.intTransaccion = intTransaccion;
      this.oConn = oConn;
      this.strPath = strPath;
      this.strCadenaOriginalReal = "";
      this.strPathCerts = strPathCerts;
      this.strPassKeyMaster = strPassKeyMaster;
      this.bitacora = new Bitacora();
      this.varSesiones = varSesiones;
      this.fecha = new Fechas();
      this.bolUsaDividendos = false;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   public String GeneraXml() {
      String strRes = "OK";

      //Recuperamos los valores
      Rhh_Ret_Retenciones objRet = new Rhh_Ret_Retenciones();
      objRet.ObtenDatos(intTransaccion, oConn);
      GetDataEmpresa(objRet);
      //Evaluamos los campos obligatorios
      String strEvalOblig = EvaluaObligatorios(objRet);
      if (strEvalOblig.equals("OK")) {
         //Generamos los objetos
         objFactory = new ObjectFactory();
         cfdiRet = objFactory.createRetenciones();
         //Llena los objetos del CFDI
         LlenaRetenciones(objRet, cfdiRet);
         //Sellado
         strSelloCFD = SellaRetencion();
         if (strSelloCFD.startsWith("ERROR")) {
            strRes = strEvalOblig;
         } else {
            //Generamos el xml
            log.debug("Firma:" + strSelloCFD);
            cfdiRet.setSello(strSelloCFD);
            String strRespGenXml = GeneraXmlTimbrado();
            if (strRespGenXml.equals("OK")) {
               log.debug("Genera el xml firmado");
               //Procedemos a timbrar con el PAC
               String strTimbrado = TimbradoRetenciones();
               if (strTimbrado.equals("OK")) {
                  log.debug("Timbro el cfdi");
                  log.debug("Genera el QR");
                  GeneraQR();
                  log.debug("Guarda el documento...");
                  //Guardamos los datos del timbrado....
                  String strUpdate = "UPDATE rhh_ret_retenciones "
                          + "set RET_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                          + ",RET_SELLO='" + this.strSelloCFD + "'"
                          + ",RET_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                          + ",RET_PATH_CBB='" + this.strPathQR + "'"
                          + ",RET_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                          + ",RET_NUMCERT='" + this.strNumSerieCert + "'"
                          + ",RET_SE_TIMBRO=1"
                          + " where RET_ID = " + this.intTransaccion;
                  oConn.runQueryLMD(strUpdate);
               } else {
                  strRes = strTimbrado;
               }
            } else {
               strRes = strRespGenXml;
            }
         }
      } else {
         strRes = strEvalOblig;
      }

      return strRes;
   }

   /**
    * Genera la imagen QR
    */
   private void GeneraQR() {
      //Generamos el QR
      String strQResp = null;
      if (cfdiRet.getReceptor().getNacionalidad().equals("Nacional")) {
         strQResp = GeneracionQR.generaQR(cfdiRet.getEmisor().getRFCEmisor(), cfdiRet.getReceptor().getNacional().getRFCRecep(), new DecimalFormat("0000000000.000000").format(cfdiRet.getTotales().getMontoTotOperacion().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 4);
      } else {
         strQResp = GeneracionQR.generaQR(cfdiRet.getEmisor().getRFCEmisor(), cfdiRet.getReceptor().getExtranjero().getNumRegIdTrib(), new DecimalFormat("0000000000.000000").format(cfdiRet.getTotales().getMontoTotOperacion().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 4);
      }

      if (strQResp.startsWith("OK")) {
         strPathQR = strQResp.replace("OK", "");
      }
   }

   /**
    * Obtiene la configuracion de la empresa
    */
   private void GetDataEmpresa(Rhh_Ret_Retenciones objRet) {
      String strSql = "SELECT EMP_RFC,EMP_RAZONSOCIAL,"
              + "EMP_NOMKEY,EMP_NOMCERT,EMP_NOSERIECERT,"
              + "AES_DECRYPT(EMP_PASSKEY, '" + strPassKeyMaster + "') AS unencrypted "
              + "FROM vta_empresas "
              + "WHERE EMP_ID = " + objRet.getFieldInt("EMP_ID") + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strPathKey = this.strPathCerts + System.getProperty("file.separator") + rs.getString("EMP_NOMKEY");
            strPassKey = rs.getString("unencrypted");
            strPathCert = this.strPathCerts + System.getProperty("file.separator") + rs.getString("EMP_NOMCERT");
            strNumSerieCert = rs.getString("EMP_NOSERIECERT");
            strEMP_RFC = rs.getString("EMP_RFC");
            strEMP_RAZONSOCIAL = rs.getString("EMP_RAZONSOCIAL");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

   }

   /**
    * Evalua los campos obligatorios
    */
   private String EvaluaObligatorios(Rhh_Ret_Retenciones objRet) {
      String strRes = "OK";
      if (objRet.getFieldString("RET_FECHAEXP").isEmpty()) {
         strRes = "ERROR: Se requiere la fecha de emision";
      }
      if (objRet.getFieldString("RET_CVERETENC").isEmpty()) {
         strRes = "ERROR: Se requiere la clave de retencion";
      }
//      if (objRet.getFieldString("RET_RFCEMISOR").isEmpty()) {
//         strRes = "ERROR: Se requiere el rfc de emision";
//      }
      if (objRet.getFieldString("RET_RFCRECEP").isEmpty()) {
         strRes = "ERROR: Se requiere el rfc de recepcion";
      }
      if (objRet.getFieldInt("RET_MESINI") == 0) {
         strRes = "ERROR: Se requiere el mes inicial";
      }
      if (objRet.getFieldInt("RET_MESFIN") == 0) {
         strRes = "ERROR: Se requiere el mes final";
      }
      if (objRet.getFieldInt("RET_EJERC") == 0) {
         strRes = "ERROR: Se requiere el año del ejercicio";
      }
//      if (objRet.getFieldDouble("RET_MONTOTOTOPERACION") == 0) {
//         strRes = "ERROR: Se requiere el monto total de la operacion ";
//      }
//      if (objRet.getFieldDouble("RET_MONTOTOTGRAV") == 0) {
//         strRes = "ERROR: Se requiere el monto total gravado ";
//      }
//      if (objRet.getFieldDouble("RET_MONTOTOTEXENT") == 0) {
//         strRes = "ERROR: Se requiere el monto total exento";
//      }
//      if (objRet.getFieldDouble("RET_MONTOTOTRET") == 0) {
//         strRes = "ERROR: Se requiere el monto total de la retencion";
//      }
      //Evaluamos si es un dividendo
      if (objRet.getFieldInt("RET_ES_DIVIDENDO") == 1) {

         if (objRet.getFieldString("RET_CLAVTIPDIVOUTIL").isEmpty()) {
            strRes = "ERROR: Se requiere la clave del tipo de dividendo o utilidad";
         }
         if (objRet.getFieldInt("RET_NAC_O_EXTR") == 1) {
//            if (objRet.getFieldDouble("RET_MONTOISR_ACREDIRETMEXICO") == 0) {
//               strRes = "ERROR: Se requiere el importe o retención del dividendo o utilidad en territorio nacional";
//            }
         } else {
//            if (objRet.getFieldDouble("RET_MONTOISR_ACREDIRETEXTRANJ") == 0) {
//               strRes = "ERROR: Se requiere el importe o retención del dividendo o utilidad en territorio extranjero";
//            }
         }
         if (objRet.getFieldString("RET_TIPOSOCDISTDIV").isEmpty()) {
            strRes = "ERROR: Se requiere especificar si es  sociedad nacional o extranjera";
         }
      }
      // <editor-fold defaultstate="collapsed" desc="Certificado digital">
      if (!this.strPathCert.isEmpty()) {
         UtilCert cert = new UtilCert();
         cert.OpenCert(this.strPathCert);
         if (!cert.getStrResult().startsWith("ERROR")) {
            strCertificadoBase64 = cert.getCertContentBase64();
         } else {
            strRes = cert.getStrResult();
         }
      }
      // </editor-fold>
      return strRes;
   }

   /**
    * Llena los objetos del CFDI de retenciones para generar el XML
    */
   private void LlenaRetenciones(Rhh_Ret_Retenciones objRet, Retenciones cfdiRet) {
      // <editor-fold defaultstate="collapsed" desc="Documento principal de Retenciones">
      cfdiRet.setVersion("1.0");
      if (!objRet.getFieldString("RET_FOLIOINT").isEmpty()) {
         cfdiRet.setFolioInt(objRet.getFieldString("RET_FOLIOINT"));
      }
      cfdiRet.setNumCert(strNumSerieCert);
      cfdiRet.setCert(strCertificadoBase64);

      Date dateTmp = new Date();

//      Date dateTmp = new Date();
//      try {
//         dateTmp = formateaDate.parse(objRet.getFieldString("RET_FECHAEXP") + formateaDateHorActual.format(dateTmp));
//      } catch (ParseException ex) {
//         log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//
//      }
      cfdiRet.setFechaExp(dateTmp);
      cfdiRet.setCveRetenc(objRet.getFieldString("RET_CVERETENC"));
      if (!objRet.getFieldString("RET_DESCRETENC").isEmpty()) {
         cfdiRet.setDescRetenc(objRet.getFieldString("RET_DESCRETENC"));
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Emisor">
      Emisor emisor = objFactory.createRetencionesEmisor();
      cfdiRet.setEmisor(emisor);
      if (!objRet.getFieldString("RET_CURPE").isEmpty()) {
         emisor.setCURPE(objRet.getFieldString("RET_CURPE"));
      }
      if (!objRet.getFieldString("RET_NOMDENRAZSOCE").isEmpty()) {
         emisor.setNomDenRazSocE(FormateaTextoXml(objRet.getFieldString("RET_NOMDENRAZSOCE")));
      } else {
         emisor.setNomDenRazSocE(FormateaTextoXml(this.strEMP_RAZONSOCIAL));
      }
      if (!objRet.getFieldString("RET_RFCEMISOR").isEmpty()) {
         emisor.setRFCEmisor(objRet.getFieldString("RET_RFCEMISOR"));
      } else {
         log.debug("De la base strEMP_RFC...");
         emisor.setRFCEmisor(strEMP_RFC);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Receptor">
      Receptor receptor = objFactory.createRetencionesReceptor();
      if (objRet.getFieldInt("RET_NAC_O_EXTR") == 1) {
         //Nacional
         receptor.setNacionalidad("Nacional");
         Nacional nacional = objFactory.createRetencionesReceptorNacional();
         receptor.setNacional(nacional);
         if (!objRet.getFieldString("RET_RFCRECEP").isEmpty()) {
            nacional.setRFCRecep(objRet.getFieldString("RET_RFCRECEP"));
         }
         if (!objRet.getFieldString("RET_NOMDENRAZSOCR").isEmpty()) {
            nacional.setNomDenRazSocR(objRet.getFieldString("RET_NOMDENRAZSOCR"));
         }
         if (!objRet.getFieldString("RET_CURPR").isEmpty()) {
            nacional.setCURPR(objRet.getFieldString("RET_CURPR"));
         }
      } else {
         //Extranjero
         receptor.setNacionalidad("Extranjero");
         Extranjero extranjero = objFactory.createRetencionesReceptorExtranjero();
         receptor.setExtranjero(extranjero);
         if (!objRet.getFieldString("RET_RFCRECEP").isEmpty()) {
            extranjero.setNumRegIdTrib(objRet.getFieldString("RET_RFCRECEP"));
         }
         if (!objRet.getFieldString("RET_NOMDENRAZSOCR").isEmpty()) {
            extranjero.setNomDenRazSocR(objRet.getFieldString("RET_NOMDENRAZSOCR"));
         }
      }
      cfdiRet.setReceptor(receptor);
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Periodo">
      Periodo periodo = objFactory.createRetencionesPeriodo();
      periodo.setMesIni(objRet.getFieldInt("RET_MESINI"));
      periodo.setMesFin(objRet.getFieldInt("RET_MESFIN"));
      periodo.setEjerc(objRet.getFieldInt("RET_EJERC"));
      cfdiRet.setPeriodo(periodo);
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Totales">
      Totales totales = objFactory.createRetencionesTotales();
      cfdiRet.setTotales(totales);
      totales.setMontoTotOperacion(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTOTOTOPERACION")), 2).replace(",", "")));
      totales.setMontoTotGrav(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTOTOTGRAV")), 2).replace(",", "")));
      totales.setMontoTotExent(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTOTOTEXENT")), 2).replace(",", "")));
      totales.setMontoTotRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTOTOTRET")), 2).replace(",", "")));
      if (objRet.getFieldDouble("RET_MONTORET_ISR") > 0) {
         ImpRetenidos impRetenidos = objFactory.createRetencionesTotalesImpRetenidos();
         impRetenidos.setBaseRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_BASERET_ISR")), 2).replace(",", "")));
         impRetenidos.setMontoRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTORET_ISR")), 2).replace(",", "")));
         impRetenidos.setImpuesto("01");
         if (objRet.getFieldDouble("RET_TIPOPAGORET_ISR") == 1) {
            impRetenidos.setTipoPagoRet("Pago definitivo");
         } else {
            impRetenidos.setTipoPagoRet("Pago provisional");
         }
         totales.getImpRetenidos().add(impRetenidos);
      }
      if (objRet.getFieldDouble("RET_MONTORET_IVA") > 0) {
         ImpRetenidos impRetenidos = objFactory.createRetencionesTotalesImpRetenidos();
         impRetenidos.setBaseRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_BASERET_IVA")), 2).replace(",", "")));
         impRetenidos.setMontoRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTORET_IVA")), 2).replace(",", "")));
         impRetenidos.setImpuesto("02");
         if (objRet.getFieldDouble("RET_TIPOPAGORET_IVA") == 1) {
            impRetenidos.setTipoPagoRet("Pago definitivo");
         } else {
            impRetenidos.setTipoPagoRet("Pago provisional");
         }
         totales.getImpRetenidos().add(impRetenidos);
      }
      if (objRet.getFieldDouble("RET_MONTORET_IEPS") > 0) {
         ImpRetenidos impRetenidos = objFactory.createRetencionesTotalesImpRetenidos();
         impRetenidos.setBaseRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_BASERET_IEPS")), 2).replace(",", "")));
         impRetenidos.setMontoRet(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTORET_IEPS")), 2).replace(",", "")));
         impRetenidos.setImpuesto("03");
         if (objRet.getFieldDouble("RET_TIPOPAGORET_IEPS") == 1) {
            impRetenidos.setTipoPagoRet("Pago definitivo");
         } else {
            impRetenidos.setTipoPagoRet("Pago provisional");
         }
         totales.getImpRetenidos().add(impRetenidos);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Complemento">
      // <editor-fold defaultstate="collapsed" desc="Dividendos">
      if (objRet.getFieldInt("RET_ES_DIVIDENDO") == 1) {
         bolUsaDividendos = true;
         Complemento complemento = objFactory.createRetencionesComplemento();
         com.mx.siweb.sat.retenciones.dividendos.ObjectFactory objFactoryDividendo = new com.mx.siweb.sat.retenciones.dividendos.ObjectFactory();
         Dividendos dividendos = objFactoryDividendo.createDividendos();
         complemento.getAny().add(dividendos);
         dividendos.setVersion("1.0");
         // <editor-fold defaultstate="collapsed" desc="Utilidad">
         DividOUtil util = objFactoryDividendo.createDividendosDividOUtil();

         dividendos.setDividOUtil(util);

         util.setCveTipDivOUtil(objRet.getFieldString("RET_CLAVTIPDIVOUTIL"));
         if (objRet.getFieldDouble("RET_MONTOISR_ACREDIRETMEXICO") > 0) {
            util.setMontISRAcredRetMexico(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTOISR_ACREDIRETMEXICO")), 2).replace(",", "")));
         }
         if (objRet.getFieldDouble("RET_MONTOISR_ACREDIRETEXTRANJ") > 0) {
            util.setMontISRAcredRetExtranjero(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTOISR_ACREDIRETEXTRANJ")), 2).replace(",", "")));
         }
         if (objRet.getFieldDouble("RET_MONTRETEXTDIVIEXTRA") > 0) {
            util.setMontRetExtDivExt(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTRETEXTDIVIEXTRA")), 2).replace(",", "")));
         }
         
         if(objRet.getFieldInt("RET_TIPOSOCDISTDIV") == 1){
            util.setTipoSocDistrDiv("Sociedad Nacional");
         }else{
            util.setTipoSocDistrDiv("Sociedad Extranjera");
         }
         if (objRet.getFieldDouble("RET_MONISR_ACREDNACIO") > 0) {
            util.setMontISRAcredNal(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONISR_ACREDNACIO")), 2).replace(",", "")));
         }
         if (objRet.getFieldDouble("RET_MONTDIVACUNNACIO") > 0) {
            util.setMontDivAcumNal(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTDIVACUNNACIO")), 2).replace(",", "")));
         }
         if (objRet.getFieldDouble("RET_MONTDIVACUNEXTRANJ") > 0) {
            util.setMontDivAcumExt(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_MONTDIVACUNEXTRANJ")), 2).replace(",", "")));
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Remanente">
         if (objRet.getFieldDouble("RET_PORPORCREMNET") > 0) {
            Remanente remanente = objFactoryDividendo.createDividendosRemanente();
            dividendos.setRemanente(remanente);
            remanente.setProporcionRem(new BigDecimal(NumberString.FormatearDecimal((objRet.getFieldDouble("RET_PORPORCREMNET")), 2).replace(",", "")));
         }
         // </editor-fold>
         cfdiRet.setComplemento(complemento);

      }

      // </editor-fold>
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Addenda">
      // </editor-fold>
   }

   private String SellaRetencion() {
      String strValorEncrip = GeneraCadenaOriginal();
      log.debug("strCadenaOriginal:" + strValorEncrip);
      strCadenaOriginalReal = strValorEncrip;
      String strValorSello = "";
      try {
         PrivateKey key;
         key = ObtenerPrivateKey(this.strPathKey, this.strPassKey);
         if (key == null) {
            strValorSello = "ERROR:NO SE PUDO ABRIR EL SELLO";
         } else {
            //Guardamos la llave para los pacs
            this.llavePrivadaEmisor = key.getEncoded();
            //Sellamos
            try {
               /*Codigo generico*/
               byte[] data = strValorEncrip.getBytes("UTF8");
               Signature sig = Signature.getInstance("SHA1withRSA");
               sig.initSign(key);
               sig.update(data);
               //Sellamos
               byte[] signatureBytes = sig.sign();
               //Convertimos a base 64
               byte[] b64Enc = Base64.encodeBase64(signatureBytes);
               strValorSello = new String(b64Enc);
               log.debug("strValorSello:" + strValorSello);
               /*Codigo generico*/
            } catch (IOException ex) {
               strValorSello = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
         }
      } catch (SignatureException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "signature", oConn);
         log.error(ex.getMessage());
         strValorSello = "ERROR:" + ex.getMessage();
      } catch (InvalidKeyException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "invalidKey", oConn);
         log.error(ex.getMessage());
         strValorSello = "ERROR:" + ex.getMessage();
      } catch (NoSuchAlgorithmException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "noSuchAlgoritm", oConn);
         log.error(ex.getMessage());
         strValorSello = "ERROR:" + ex.getMessage();
      }
      return strValorSello;
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
         log.error(ex.getMessage());
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
            log.error(ex.getMessage());
            bolError = true;
         } catch (IOException ex) {
            log.error(ex.getMessage());
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
                     log.error(ex.getMessage());
                  }
               } catch (NoSuchAlgorithmException ex) {
                  log.error(ex.getMessage());

               }
            }
            // For lazier types (like me):
            pk = pkcs8.getPrivateKey();
         }
      }
      return pk;
   }

   /**
    * Genera la cadena original
    */
   private String GeneraCadenaOriginal() {
      //declaramos objetos de jaxb y xalan
      TransformerFactory tFactory = TransformerFactory.newInstance();
      StringWriter strCadenaStr = new StringWriter();
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      DocumentBuilder db = null;
      //Convertimos en dom el objeto de jaxb
      try {
         db = dbf.newDocumentBuilder();
      } catch (ParserConfigurationException ex) {
         log.error(ex.getMessage());
      }
      org.w3c.dom.Document doc = db.newDocument();
      DOMSource xmlDomSource = null;
      try {
         JAXBContext jaxbContext;
         log.debug("Comenzamos..." + bolUsaDividendos);

         if (bolUsaDividendos) {
            jaxbContext = JAXBContext.newInstance(com.mx.siweb.sat.retenciones.ObjectFactory.class, com.mx.siweb.sat.retenciones.dividendos.ObjectFactory.class);
         } else {
            jaxbContext = JAXBContext.newInstance(com.mx.siweb.sat.retenciones.ObjectFactory.class);

         }

         log.debug("Comenzamos...2");
         Marshaller marshaller = jaxbContext.createMarshaller();
         /*Vinculamos URI'S que solicita hacienda*/
         String strListXSD = "http://www.sat.gob.mx/esquemas/retencionpago/1 http://www.sat.gob.mx/esquemas/retencionpago/1/retencionpagov1.xsd";
         if (bolUsaDividendos) {
            strListXSD += " http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos/dividendos.xsd";
         }
         log.debug("Comenzamos...3");
         marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, strListXSD);
         /*Formato de salida del XML*/
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
         marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
         marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
         marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
         log.debug("Comenzamos...4");
         /*Guardamos comprobante*/
         marshaller.marshal(this.cfdiRet, doc);
         log.debug("Comenzamos...5");
         // Use the DOM Document to define a DOMSource object.
         xmlDomSource = new DOMSource(doc);
         log.debug("Comenzamos...6");
         // Set the base URI for the DOMSource so any relative URIs it contains can
         // be resolved.
         xmlDomSource.setSystemId("cfdi_retenciones.xml");
         log.debug("Comenzamos...7");
      } catch (JAXBException ex) {
         log.error(ex.getMessage());
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXML", oConn);
      }
      try {
         String strPathNew = this.strPath
                 + System.getProperty("file.separator") + "cadenaoriginal_retenciones"
                 + System.getProperty("file.separator") + "retenciones.xslt";
         log.debug(xmlDomSource.toString());
         log.debug("strPathNewXslt:" + strPathNew);
         Transformer transformer = tFactory.newTransformer(new StreamSource(strPathNew));
         transformer.transform(xmlDomSource, new StreamResult(strCadenaStr));
      } catch (TransformerException ex) {
         log.error(ex.getMessage());
      } catch (Exception ex) {
         log.error(ex.getMessage());
      }
      log.debug("***************" + strCadenaStr);
      return strCadenaStr.toString();
   }

   private String GeneraXmlTimbrado() {
      String strRes = "OK";
      // <editor-fold defaultstate="collapsed" desc="Generamos el documento XML">
      try {
         JAXBContext jaxbContext;
         log.debug("Comenzamos..." + bolUsaDividendos);

         if (bolUsaDividendos) {
            jaxbContext = JAXBContext.newInstance(com.mx.siweb.sat.retenciones.ObjectFactory.class, com.mx.siweb.sat.retenciones.dividendos.ObjectFactory.class);
         } else {
            jaxbContext = JAXBContext.newInstance(com.mx.siweb.sat.retenciones.ObjectFactory.class);

         }

         Marshaller marshaller = jaxbContext.createMarshaller();
         /*Vinculamos URI'S que solicita hacienda*/
         //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema-instance");
         String strListXSD = "http://www.sat.gob.mx/esquemas/retencionpago/1 http://www.sat.gob.mx/esquemas/retencionpago/1/retencionpagov1.xsd";//Version 3.2
         if (bolUsaDividendos) {
            strListXSD += " http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos/dividendos.xsd";
         }
         marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, strListXSD);
         /*Formato de salida del XML*/
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
         marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

         //Dependiendo de la version
         marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
         marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

         /*Guardamos comprobante*/
         StringBuilder strPathFile = new StringBuilder("");
         StringBuilder strNomFile = new StringBuilder("");

         strNomFile.append("Retenciones_").append(this.intTransaccion).append(".xml");
         strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
         log.debug("strPathFile:" + strPathFile);
         marshaller.marshal(this.cfdiRet, new FileOutputStream(strPathFile.toString()));
         log.debug("Ya genero el XML firmado....");
         strFilePathXml = strNomFile.toString();

      } catch (FileNotFoundException ex) {
         log.error("Error 1:" + ex.getMessage());
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXMLFile", oConn);
         strRes = "ERROR:" + ex.getMessage();
      } catch (JAXBException ex) {
         log.error("Error 2:" + ex.getMessage());
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXML", oConn);
         strRes = "ERROR:" + ex.getMessage();
      }
      // </editor-fold>
      return strRes;
   }

   /**
    * Realiza el proceso de timbrado con llamadas a los webservices de los
    * diferentes proveedores que se implementen
    *
    * @return Regresa OK en caso de que sea exitosa la operación o Error en su
    * defecto
    */
   protected String TimbradoRetenciones() {
      String strRes = "OK";
      Timbrado_Pacs timbrado = null;
      if (this.enumPAC == SATXml3_0.PACS_CFDIS.FACTURA_EN_SEGUNDOS) {
         timbrado = new TimbradoFacturaSegundos(this.strPathConfigPAC);
      }
      if (this.enumPAC == SATXml3_0.PACS_CFDIS.TIMBRE_FISCAL) {
         timbrado = new TimbradoTimbreFiscal(this.strPathConfigPAC);
      }
      if (timbrado != null) {
         timbrado.setIntIdDoc(intTransaccion);

         timbrado.setStrTablaDoc("rhh_ret_retenciones");
         timbrado.setStrPrefijoDoc("RET_");

         timbrado.setStrPathXml(this.strPath);
         timbrado.setoConn(oConn);
         timbrado.setLlavePrivadaEmisor(this.llavePrivadaEmisor);
         timbrado.setCertificadoEmisor(this.certificadoEmisor);
         timbrado.setStrPasswordEmisor(this.strPassKey);
         strRes = timbrado.timbra_Recibo(strFilePathXml);
         this.strfolioFiscalUUID = timbrado.getStrfolioFiscalUUID();
         this.strFechaTimbre = timbrado.getStrFechaTimbre();
         this.strSelloSAT = timbrado.getStrSelloSAT();
         this.strNoCertSAT = timbrado.getStrNoCertSAT();
      } else {
         strRes = "ERROR: EL PAC no existe";
      }
      return strRes;
   }

   /**
    * Genera la cadena original del Timbre fiscal Digital 1. version 2. UUID 3.
    * FechaTimbrado 4. selloCFD 5. noCertificadoSAT
    */
   private String GeneraCadenaOriginalTimbre() {
      StringBuilder strCadena = new StringBuilder();
      strCadena.append("||1.0");
      strCadena.append("|").append(this.strfolioFiscalUUID);
      strCadena.append("|").append(this.strFechaTimbre);
      strCadena.append("|").append(this.strSelloCFD);
      strCadena.append("|").append(this.strNoCertSAT);
      strCadena.append("||");
      return strCadena.toString();
   }

   public static String TimbradoMasivo(String strFolioInicial, String strFolioFinal,
           int intIdEmpresa, VariableSession sesion, String strMyPassSecret,
           String strPathXml, String strPathCerts, String strPathConfigPac, Conexion oConn
   ) {
      String strRes = "OK";
      boolean bolEncontro = false;
      String strSql = "SELECT "
              + "RET_ID "
              + "FROM rhh_ret_retenciones "
              + "WHERE EMP_ID = " + intIdEmpresa + " "
              + " AND RET_FOLIOINT >='" + strFolioInicial + "' "
              + " AND RET_FOLIOINT <='" + strFolioInicial + "'"
              + " AND RET_SE_TIMBRO = 0  "
              + "ORDER BY RET_FOLIOINT";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intIdRet = rs.getInt("RET_ID");
            log.debug("Generacion de timbrado de " + intIdRet);
            SATRetenciones retenciones = new SATRetenciones(intIdRet, strPathXml,
                    strPathCerts, strMyPassSecret,
                    sesion, oConn);
            retenciones.setStrPathConfigPAC(strPathConfigPac);
            strRes = retenciones.GeneraXml();
            bolEncontro = true;
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Sino encontro marca error
      if (!bolEncontro) {
         strRes = "Error: no hay comprobantes por timbrar";
      }
      return strRes;
   }

   /**
    * Reemplaza los caracteres no permitidos por Hacienda
    *
    * @param strValor Es el valor por formatear
    * @return Regresa el valor formateado
    */
   protected String FormateaTextoXml(String strValor) {
      return FormateaTextoXml(strValor, false);
   }

   /**
    * Reemplaza los caracteres no permitidos por Hacienda
    *
    * @param strValor Es el valor por formatear
    * @param bolAmpersand Indica si se valida el ampersand
    * @return Regresa el valor formateado
    */
   protected String FormateaTextoXml(String strValor, boolean bolAmpersand) {
      /*
       En el caso del & se deber usar la secuencia &amp;
       En el caso del  se deber usar la secuencia &quot;
       En el caso del < se deber usar la secuencia &lt;
       En el caso del > se deber usar la secuencia &gt;
       En el caso del  se deber usar la secuencia &#36;
       */
      strValor = strValor.replace(Character.toString((char) 10), " ");
      strValor = strValor.replace(Character.toString((char) 13), " ");
      //Aqui iran todos los patrones
      ArrayList<Pattern> lstPatterns = new ArrayList<Pattern>();
      ArrayList<String> lstSustituye = new ArrayList<String>();
      // compilamos el patron
      Pattern patronVarios = Pattern.compile("[ ]+");
      Pattern patronIni = Pattern.compile("^ ");
      Pattern patronFin = Pattern.compile(" $");
      lstPatterns.add(patronVarios);
      lstSustituye.add(" ");
      lstPatterns.add(patronIni);
      lstSustituye.add("");
      lstPatterns.add(patronFin);
      lstSustituye.add("");
      Iterator<Pattern> it = lstPatterns.iterator();
      int intCont = -1;
      while (it.hasNext()) {
         Pattern patron = it.next();
         intCont++;
         // creamos el Matcher a partir del patron, la cadena como parametro
         Matcher encaja = patron.matcher(strValor);
         // invocamos el metodo replaceAll
         strValor = encaja.replaceAll(lstSustituye.get(intCont));
      }
      strValor = strValor.replace("*", " ");
      if (bolAmpersand) {
         strValor = strValor.replace("&", "&amp;");
      }
      strValor = strValor.replace("\"", "&quot;");
      strValor = strValor.replace("<", "&lt;");
      strValor = strValor.replace(">", "&gt;");
      strValor = strValor.replace("'", "&#36;");
      if (strValor.endsWith(" ")) {
         strValor = strValor.substring(0, strValor.length() - 1);
      }
      strValor = strValor.replace("  ", " ");
      return strValor;
   }

   /**
    * Cancela un comprobante de retencione
    *
    * @param intRetId Es el id del comprobante
    * @param strMyPassSecret Es el password
    * @param intEmpId Es la empresa
    * @param strPATHKeys Es el path de las llaves
    * @param strPATHXml Es el path de los xml
    * @param oConn Es la conexion a la bd
    * @param sesion Es la variable de sesion
    * @return Regresa OK si todo fue exitoso
    */
   public static String CancelaComprobanteRetencion(int intRetId, String strMyPassSecret,
           int intEmpId, String strPATHKeys, String strPATHXml, Conexion oConn,
           VariableSession sesion) {
      String strResultLast = "OK";
      //Evaluamos que no este cancelado
      //Recuperamos los valores
      Rhh_Ret_Retenciones objRet = new Rhh_Ret_Retenciones();
      objRet.ObtenDatos(intRetId, oConn);
      if (objRet.getFieldInt("RET_ANULADO") == 0) {
         // <editor-fold defaultstate="collapsed" desc="Obtenemos datos del sello">
         String strNomKey = null;
         String strNomCert = null;
         String strPassKey = null;

         // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
         String strSql = "SELECT "
                 + "EMP_NOMKEY,EMP_NOMCERT,"
                 + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted "
                 + "FROM vta_empresas "
                 + "WHERE EMP_ID = " + intEmpId + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strNomKey = rs.getString("EMP_NOMKEY");
               strNomCert = rs.getString("EMP_NOMCERT");
               strPassKey = rs.getString("unencrypted");
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
            // <editor-fold defaultstate="collapsed" desc="Preparamos y ejecutamos objeto de cancelacion">
            byte[] certificadoEmisor = PreparaCertificado(strPATHKeys + System.getProperty("file.separator") + strNomCert);
            //byte[] llavePrivadaEmisor = PreparaLlave(this.strPATHKeys + System.getProperty("file.separator") + strNomKey, strPassKey);
            byte[] llavePrivadaEmisor = getPrivateKey(strPATHKeys + System.getProperty("file.separator") + strNomKey);
            StringBuilder strNomFile = new StringBuilder("");
            //Instanciamos objeto que cancela
            SatCancelaCFDI cancela = new SatCancelaCFDI(strPATHKeys);

            cancela.setStrTablaDoc("rhh_ret_retenciones");
            cancela.setStrPrefijoDoc("RET_");
            strNomFile.append("Retenciones_").append(intRetId).append(".xml");
            cancela.setIntIdDoc(intRetId);
            cancela.setStrPathXml(strPATHXml);
            cancela.setoConn(oConn);
            cancela.setLlavePrivadaEmisor(llavePrivadaEmisor);
            cancela.setCertificadoEmisor(certificadoEmisor);
            cancela.setStrPasswordEmisor(strPassKey);
            strResultLast = cancela.timbra_Recibo(strNomFile.toString());

            if (strResultLast.equals("OK")) {
               Fechas fecha1 = new Fechas();
               objRet.setFieldInt("RET_ANULADO", 1);
               objRet.setFieldInt("RET_ID_USER_ANULA", sesion.getIntNoUser());
               objRet.setFieldString("RET_FECHA_ANUL", fecha1.getFechaActual());
               objRet.Modifica(oConn);
            }
            // </editor-fold>

         } catch (SQLException ex) {
            strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
      // </editor-fold>

         // </editor-fold>
      } else {
         strResultLast = "ERROR:La retencion ya fue anulada.";
      }

      return strResultLast;

   }

   //Prepara la llave a usar para cancelar el comprobante
   protected static byte[] PreparaCertificado(String strPathCert) {
      byte[] certificado = null;
      UtilCert cert = new UtilCert();
      cert.OpenCert(strPathCert);
      if (!cert.getStrResult().startsWith("ERROR")) {
         try {
            certificado = cert.getCert().getEncoded();
         } catch (CertificateEncodingException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
      }
      return certificado;
   }

   /**
    * Obtiene la llave privada
    *
    * @param strPath Es el path del archivo
    * @return Regresa la llave en binario
    */
   public static byte[] getPrivateKey(String strPath) {
      RandomAccessFile f = null;
      byte[] b = null;
      try {
         f = new RandomAccessFile(strPath, "r");
         b = new byte[(int) f.length()];
         f.read(b);
      } catch (FileNotFoundException ex) {
         java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
      }

      return b;
   }
   // </editor-fold>
}
