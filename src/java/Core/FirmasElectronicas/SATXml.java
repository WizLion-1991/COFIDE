/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas;

import Core.FirmasElectronicas.Addendas.SATAddenda;
import Core.FirmasElectronicas.SAT22.Comprobante;
import Core.FirmasElectronicas.SAT22.Comprobante.Addenda;
import Core.FirmasElectronicas.SAT22.Comprobante.Complemento;
import Core.FirmasElectronicas.SAT22.Comprobante.Conceptos;
import Core.FirmasElectronicas.SAT22.Comprobante.Conceptos.Concepto;
import Core.FirmasElectronicas.SAT22.Comprobante.Emisor;
import Core.FirmasElectronicas.SAT22.Comprobante.Emisor.RegimenFiscal;
import Core.FirmasElectronicas.SAT22.Comprobante.Impuestos;
import Core.FirmasElectronicas.SAT22.Comprobante.Impuestos.Retenciones;
import Core.FirmasElectronicas.SAT22.Comprobante.Impuestos.Retenciones.Retencion;
import Core.FirmasElectronicas.SAT22.Comprobante.Impuestos.Traslados;
import Core.FirmasElectronicas.SAT22.Comprobante.Impuestos.Traslados.Traslado;
import Core.FirmasElectronicas.SAT22.Comprobante.Receptor;
import Core.FirmasElectronicas.SAT22.ObjectFactory;
import Core.FirmasElectronicas.SAT22.TInformacionAduanera;
import Core.FirmasElectronicas.SAT22.TUbicacion;
import Core.FirmasElectronicas.SAT22.TUbicacionFiscal;
import Core.FirmasElectronicas.Utils.UtilCert;
import Core.FirmasElectronicas.complementos.donatarias.Donatarias;
import ERP.ERP_MapeoFormato;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.NumberString;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.ssl.PKCS8Key;
import org.apache.commons.ssl.Base64;
import org.apache.logging.log4j.LogManager;

/**
 * Clase para generacion de comprobantes fiscales
 *
 * @author zeus
 */
public class SATXml {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected int intTransaccion = 0;
   protected Conexion oConn;
   protected String strCadenaOriginalReal;
   protected String strPath;
   protected String NoAprobacion;
   protected String FechaAprobacion;
   protected String NoSerieCert;
   protected String strPathKey;
   protected String strPathCert;
   protected String strPassKey;
   protected Bitacora bitacora;
   protected VariableSession varSesiones;
   protected Fechas fecha;
   protected String strPATHFonts;
   private Comprobante objComp;
   protected boolean bolEsLocal = false;
   protected boolean bolSendMailMasivo;
   protected boolean bolEsNc = false;
   protected int intEMP_TIPOCOMP = 0;
   protected String strListMailsEsp;
   protected String strFolio;
   protected SATAddenda satAddenda;
   protected Class classAddenda;
   protected boolean bolComplementoFiscal = false;
   protected boolean bolDonataria = false;
   protected String strDonaNumAutoriza = null;
   protected String strDonaFechaAutoriza = null;
   protected String strDonaLeyenda = null;
   int intEMP_ACUSEFACTURA = 0;
   protected boolean bolUsoLugarExpEmp = false;
   protected boolean bolUsaTrxComoFolio = false;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SATXml.class.getName());

   /**
    * Nos regresa si usaremos la transaccion como el folio por mostrar
    *
    * @return
    */
   public boolean isBolUsaTrxComoFolio() {
      return bolUsaTrxComoFolio;
   }

   /**
    * Definimos que usaremos la transaccion como el folio por mostrar
    *
    * @param bolUsaTrxComoFolio Con true activamos esta propiedad
    */
   public void setBolUsaTrxComoFolio(boolean bolUsaTrxComoFolio) {
      this.bolUsaTrxComoFolio = bolUsaTrxComoFolio;
   }

   /**
    * Nos regresa que el lugar de expedicion lo saque de la empresa
    *
    * @return Indica true si se usa la empresa como lugar de expedicion
    */
   public boolean isBolUsoLugarExpEmp() {
      return bolUsoLugarExpEmp;
   }

   /**
    * Indica que el lugar de expedicion lo saque de la empresa
    *
    * @param bolUsoLugarExpEmp Indica true si se usa la empresa como lugar de
    * expedicion
    */
   public void setBolUsoLugarExpEmp(boolean bolUsoLugarExpEmp) {
      this.bolUsoLugarExpEmp = bolUsoLugarExpEmp;
   }

   /**
    * Regresa el objeto a usar para generar la addenda
    *
    * @return Un objeto JAXB para generar la addenda
    */
   public SATAddenda getSatAddenda() {
      return satAddenda;
   }

   /**
    * Definimos el objeto a usar para generar la addenda
    *
    * @param satAddenda Es el objeto JAXB para generar la addenda
    * @param classAddenda Es la clase de la addena
    */
   public void setSatAddenda(SATAddenda satAddenda, Class classAddenda) {
      this.classAddenda = classAddenda;
      this.satAddenda = satAddenda;
   }

   /**
    * Obtenemos el numero de transaccion que se esta procesando
    *
    * @return Nos regresa el numero de transaccion
    */
   public int getIntTransaccion() {
      return intTransaccion;
   }

   /**
    * Definimos el numero de transaccion que se esta procesando
    *
    * @param intTransaccion Es el numero de transaccion
    */
   public void setIntTransaccion(int intTransaccion) {
      this.intTransaccion = intTransaccion;
   }

   /*Datos que se recuperaran de un archivo*/

   protected String RecuperaNoAprobacion() {
      return this.NoAprobacion;
   }

   protected String RecuperaFechaAprobacion() {
      return this.FechaAprobacion;
   }

   protected String RecuperaNoSerieCertificado() {
      /*OJO VALIDAR SOLO 20 POSICIONES*/
      String NoSerieCertTmp = this.NoSerieCert;
      if (NoSerieCertTmp.length() > 20) {
         NoSerieCertTmp = NoSerieCertTmp.substring(0, 20);
      }
      return NoSerieCertTmp;
   }

   /**
    * Regresa el path donde se encuentran las fuentes
    *
    * @return Es una cadena de texto con un path
    */
   public String getStrPATHFonts() {
      return strPATHFonts;
   }

   /**
    * Define el path donde se encuentran las fuentes
    *
    * @param strPATHFonts Es una cadena de texto con un path
    */
   public void setStrPATHFonts(String strPATHFonts) {
      this.strPATHFonts = strPATHFonts;
   }

   /**
    * Nos dice si estamos en modo local
    *
    * @return Es un valor boolean
    */
   public boolean isBolEsLocal() {
      return bolEsLocal;
   }

   /**
    * Define si esta en modo local
    *
    * @param bolEsLocal Es un valor boolean
    */
   public void setBolEsLocal(boolean bolEsLocal) {
      this.bolEsLocal = bolEsLocal;
   }

   /**
    * Nos dice si se envia el mail del formato
    *
    * @return Regresa true si se envia el mail
    */
   public boolean isBolSendMailMasivo() {
      return bolSendMailMasivo;
   }

   /**
    * Indica si se envia el mail masivo
    *
    * @param bolSendMailMasivo Nos regresa true si se envia el mail del formato
    */
   public void setBolSendMailMasivo(boolean bolSendMailMasivo) {
      this.bolSendMailMasivo = bolSendMailMasivo;
   }

   /**
    * Nos indica si es una nota de credito
    *
    * @return Regresa true si es nota de credito
    */
   public boolean isBolEsNc() {
      return bolEsNc;
   }

   /**
    * Indica si es una nota de credito
    *
    * @param bolEsNc true si es nota de credito
    */
   public void setBolEsNc(boolean bolEsNc) {
      this.bolEsNc = bolEsNc;
   }

   /**
    * Regresa el tipo de comprobante
    *
    * @return Es el id del tipo de comprobante
    */
   public int getIntEMP_TIPOCOMP() {
      return intEMP_TIPOCOMP;
   }

   /**
    * Definimos el tipo de comprobante
    *
    * @param intEMP_TIPOCOMP Es el id del tipo de comprobante
    */
   public void setIntEMP_TIPOCOMP(int intEMP_TIPOCOMP) {
      this.intEMP_TIPOCOMP = intEMP_TIPOCOMP;
   }

   /**
    * Obtenemos la lista de mails especiales
    *
    * @return Regresa una cadena con mails separados con comas
    */
   public String getStrListMailsEsp() {
      return strListMailsEsp;
   }

   /**
    * Definimos una lista de mail especiales a los que se les mandara correos
    *
    * @param strListMailsEsp Es una cadena con mails separados con comas
    */
   public void setStrListMailsEsp(String strListMailsEsp) {
      this.strListMailsEsp = strListMailsEsp;
   }

   /**
    * Regresa la fecha de aprobacion de los folios
    *
    * @return Regresa la fecha
    */
   public String getFechaAprobacion() {
      return FechaAprobacion;
   }

   /**
    * Define la fecha de aprobacion de los folios
    *
    * @param FechaAprobacion Es la fecha de aprobacion en formato AAAMMDD
    */
   public void setFechaAprobacion(String FechaAprobacion) {
      this.FechaAprobacion = FechaAprobacion;
   }

   /**
    * Regresa la fecha de aprobacion
    *
    * @return Es la fecha de aprobacion en formato AAAMMDD
    */
   public String getNoAprobacion() {
      return NoAprobacion;
   }

   /**
    * Define el numero de aprobacion
    *
    * @param NoAprobacion Es el numero de aprobacion
    */
   public void setNoAprobacion(String NoAprobacion) {
      this.NoAprobacion = NoAprobacion;
   }

   /**
    * Regresa el numero de serie del certificado
    *
    * @return Regresa el numero de serie
    */
   public String getNoSerieCert() {
      return NoSerieCert;
   }

   /**
    * Define el numero de serie del certificado
    *
    * @param NoSerieCert Es el numero de serie
    */
   public void setNoSerieCert(String NoSerieCert) {
      this.NoSerieCert = NoSerieCert;
   }

   /**
    * Regresa el password de la llave privada
    *
    * @return regresa la llave
    */
   public String getStrPassKey() {
      return strPassKey;
   }

   /**
    * Define el password de la llave privada
    *
    * @param strPassKey Es la llave
    */
   public void setStrPassKey(String strPassKey) {
      this.strPassKey = strPassKey;
   }

   /**
    * Regresa el path donde se almacenaran los XML
    *
    * @return Regresa el path de los XML
    */
   public String getStrPath() {
      return strPath;
   }

   /**
    * Define el path donde se guardaran los XML
    *
    * @param strPath Define el path de los XML
    */
   public void setStrPath(String strPath) {
      this.strPath = strPath;
   }

   /**
    * Es el path de la llave privada para firmar los comprobantes fiscales
    *
    * @return Es una cadena con un path
    */
   public String getStrPathKey() {
      return strPathKey;
   }

   /**
    * Es el path de la llave privada para firmar los comprobantes fiscales
    *
    * @param strPathKey Es una cadena con un path
    */
   public void setStrPathKey(String strPathKey) {
      this.strPathKey = strPathKey;
   }

   /**
    * Regresa las variables de sesion
    *
    * @return Regresa el objeto con las variables de sesion
    */
   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   /**
    * Define las variables de sesion
    *
    * @param varSesiones Define el objeto con las variables de sesion
    */
   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

   /**
    * Regresa la conexion
    *
    * @return Regresa la conexion a la base de datos
    */
   public Conexion getoConn() {
      return oConn;
   }

   /**
    * Define la conexion
    *
    * @param oConn Define la conexion a la base de datos
    */
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   /**
    * Obtiene el objeto que representa el comprobante
    *
    * @return Regresa un objeto JAXB que representa el comprobante fiscal
    */
   public Comprobante getObjComp() {
      return objComp;
   }

   /**
    * Define el objeto que representa el comprobante
    *
    * @param objComp Es un objeto JAXB que representa el comprobante fiscal
    */
   public void setObjComp(Comprobante objComp) {
      this.objComp = objComp;
   }

   /**
    * Regresa la ruta del certificado de la llave privada
    *
    * @return Es una cadena
    */
   public String getStrPathCert() {
      return strPathCert;
   }

   /**
    * Define la ruta del certificado de la llave privada
    *
    * @param strPathCert Es una cadena
    */
   public void setStrPathCert(String strPathCert) {
      this.strPathCert = strPathCert;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor del objeto SAT que realiza la firma electrnica
    *
    * @param intTransaccion Es el nmero de transaccion
    * @param strPath Es el path dnde guardaremos el XML
    * @param NoAprobacion Es el anio de aprobacion
    * @param FechaAprobacion Es la fecha de aprobacion
    * @param NoSerieCert Es el numero de serie del certificado
    * @param strPathKey Es la path donde esta la llave
    * @param strPassKey Es el password donde de la llave
    * @param varSesiones Son las variables de sesion
    * @param oConn Es la conexin a la bd
    */
   public SATXml(int intTransaccion, String strPath, String NoAprobacion, String FechaAprobacion, String NoSerieCert,
      String strPathKey, String strPassKey, VariableSession varSesiones,
      Conexion oConn) {
      this.intTransaccion = intTransaccion;
      this.oConn = oConn;
      this.strPath = strPath;
      this.strCadenaOriginalReal = "";
      this.NoAprobacion = NoAprobacion;
      this.FechaAprobacion = FechaAprobacion;
      this.NoSerieCert = NoSerieCert;
      this.strPathKey = strPathKey;
      this.strPassKey = strPassKey;
      this.bitacora = new Bitacora();
      this.varSesiones = varSesiones;
      this.fecha = new Fechas();
      this.bolSendMailMasivo = true;
      this.strListMailsEsp = "";
      this.strPathCert = "";
      this.strListMailsEsp = "";
      this.strFolio = "";
   }

   /**
    * Constructor del objeto SAT que realiza la firma electrnica
    */
   public SATXml() {
      this.intTransaccion = 0;
      this.strPath = "";
      this.strCadenaOriginalReal = "";
      this.NoAprobacion = "";
      this.FechaAprobacion = "";
      this.NoSerieCert = "";
      this.strPathKey = "";
      this.strPassKey = "";
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.bolSendMailMasivo = true;
      this.strListMailsEsp = "";
      this.strPathCert = "";
      this.strListMailsEsp = "";
      this.strFolio = "";
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // <editor-fold defaultstate="collapsed" desc="Generacion de XML">
   /**
    * Genera el XML para el documento electronico
    *
    * @return Regresa OK en caso de que todo halla sido satisfactorio
    */
   public String GeneraXml() {

      // <editor-fold defaultstate="collapsed" desc="Inicializamos los valores del comprobante">
      String strRes = "OK";
      BigDecimal BGSubtotal = null;
      BigDecimal BGDescuento = null;
      BigDecimal BGTotal = null;
      BigDecimal BGImpuesto1 = null;
      BigDecimal BGImpuestoRetenido1 = null;
      BigDecimal BGTasa1 = null;
      int intUsoIEPS = 0;
      BigDecimal BGTasaIEPS = null;
      BigDecimal BGImporteIEPS = null;
      String strTipoComprobante = "ingreso";
      //Si es nota de credito no es ingresos
      if (bolEsNc) {
         strTipoComprobante = "egreso";
      }
      int intSucursal = 0;
      int intEmpId = 0;
      int intCliente = 0;
      /*Datos del receptor*/
      String strNombreReceptor = "";
      String strRFCReceptor = "";
      String strCalleReceptor = "";
      String strColoniaReceptor = "";
      String strLocalidadReceptor = "";
      String strMunicipioReceptor = "";
      String strCPReceptor = "";
      String strEdoReceptor = "";
      String strNumero = "";
      String strNumeroInt = "";
      /*Consultamos los datos del emisor*/
      String strNombreEmisor = "";
      String strRFC = "";
      String strCalle = "";
      String strCP = "";
      String strColonia = "";
      String strEdo = "";
      String strLocalidad = "";
      String strMunicipio = "";
      String strPais = "";
      String strFechaFactura = "";
      String strHoraFactura = "";
      String strFAC_SERIE = "";
      String strFAC_NOAPROB = "";
      String strANIO_NOAPROB = "";
      String strFAC_CONDPAGO = "";
      String strFAC_FORMADEPAGO = "";
      String strFAC_METODODEPAGO = "";
      String strFAC_NUMCTA = "";
      String strMailCte = "";
      String strMailCte2 = "";
      int intFolio = 0;
      String strCalleSuc = "";
      String strColoniaSuc = "";
      String strLocalidadSuc = "";
      String strMunicipioSuc = "";
      String strCPSuc = "";
      String strEdoSuc = "";
      String strPaisSuc = "";
      String strExpedidoEn = "";
      String strFAC_NOMFORMATO = "";
      ResultSet rs;
      String strSql = "";
      ArrayList<String> lstRegimenFiscal = new ArrayList<String>();
      String strMoneda = "";

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Consultamos datos de la base de datos">
      //Validamos si es nota de credito
      if (!bolEsNc) {
         /*Consultamos datos del docto*/
         strSql = "SELECT FAC_FOLIO,FAC_FECHA,FAC_IMPORTE,FAC_DESCUENTO,"
            + " FAC_CONDPAGO,FAC_FORMADEPAGO,FAC_METODODEPAGO,"
            + " FAC_TOTAL,SC_ID,EMP_ID,FAC_IMPUESTO1,FAC_RETIVA,FAC_TASA1,CT_ID,FAC_HORA,"
            + " FAC_SERIE,FAC_NOAPROB,FAC_FECHAAPROB,FAC_NOMFORMATO,"
            + " FAC_USO_IEPS,FAC_TASA_IEPS,FAC_IMPORTE_IEPS,FAC_NUMCUENTA,FAC_MONEDA "
            + " FROM vta_facturas WHERE FAC_ID = " + this.intTransaccion;
         try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strFolio = rs.getString("FAC_FOLIO");
               strFechaFactura = rs.getString("FAC_FECHA");
               BGSubtotal = rs.getBigDecimal("FAC_IMPORTE");
               BGDescuento = rs.getBigDecimal("FAC_DESCUENTO");
               BGTotal = rs.getBigDecimal("FAC_TOTAL");
               intSucursal = rs.getInt("SC_ID");
               BGImpuesto1 = rs.getBigDecimal("FAC_IMPUESTO1");
               BGImpuestoRetenido1 = rs.getBigDecimal("FAC_RETIVA");
               BGTasa1 = rs.getBigDecimal("FAC_TASA1");
               intCliente = rs.getInt("CT_ID");
               intEmpId = rs.getInt("EMP_ID");
               strFAC_NOMFORMATO = rs.getString("FAC_NOMFORMATO");
               strHoraFactura = rs.getString("FAC_HORA");
               strFAC_SERIE = rs.getString("FAC_SERIE");
               strFAC_NOAPROB = rs.getString("FAC_NOAPROB");
               strANIO_NOAPROB = rs.getString("FAC_FECHAAPROB").substring(0, 4);
               intUsoIEPS = rs.getInt("FAC_USO_IEPS");
               BGTasaIEPS = rs.getBigDecimal("FAC_TASA_IEPS");
               BGImporteIEPS = rs.getBigDecimal("FAC_IMPORTE_IEPS");
               strFAC_FORMADEPAGO = rs.getString("FAC_FORMADEPAGO");
               strFAC_CONDPAGO = rs.getString("FAC_CONDPAGO");
               strFAC_METODODEPAGO = rs.getString("FAC_METODODEPAGO");
               strFAC_NUMCTA = rs.getString("FAC_NUMCUENTA");
               strMoneda = rs.getString("FAC_MONEDA");
               try {
                  intFolio = Integer.valueOf(strFolio);
               } catch (NumberFormatException ex) {
                  ex.fillInStackTrace();
                  this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                  strRes = "ERROR:" + ex.getMessage();
               }
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         } catch (SQLException ex) {
            ex.fillInStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
            strRes = "ERROR:" + ex.getMessage();
         }
      } else {
         /*Consultamos datos del docto*/
         strSql = "SELECT NC_FOLIO,NC_FECHA,NC_IMPORTE,NC_DESCUENTO,"
            + " NC_CONDPAGO,NC_FORMADEPAGO,NC_METODODEPAGO,"
            + " NC_TOTAL,SC_ID,EMP_ID,NC_IMPUESTO1,NC_RETIVA,NC_TASA1,CT_ID,NC_HORA,"
            + " NC_SERIE,NC_NOAPROB,NC_FECHAAPROB,NC_NOMFORMATO,"
            + " NC_USO_IEPS,NC_TASA_IEPS,NC_IMPORTE_IEPS,NC_MONEDA "
            + " FROM vta_ncredito WHERE NC_ID = " + this.intTransaccion;
         try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               System.out.println("ESTAS HACIENDO CFD.....");
               strFolio = rs.getString("NC_FOLIO");
               strFechaFactura = rs.getString("NC_FECHA");
               BGSubtotal = rs.getBigDecimal("NC_IMPORTE");
               BGDescuento = rs.getBigDecimal("NC_DESCUENTO");
               BGTotal = rs.getBigDecimal("NC_TOTAL");
               intSucursal = rs.getInt("SC_ID");
               BGImpuesto1 = rs.getBigDecimal("NC_IMPUESTO1");
               BGImpuestoRetenido1 = rs.getBigDecimal("NC_RETIVA");
               BGTasa1 = rs.getBigDecimal("NC_TASA1");
               intCliente = rs.getInt("CT_ID");
               intEmpId = rs.getInt("EMP_ID");
               strFAC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
               strHoraFactura = rs.getString("NC_HORA");
               strFAC_SERIE = rs.getString("NC_SERIE");
               strFAC_NOAPROB = rs.getString("NC_NOAPROB");
               strANIO_NOAPROB = rs.getString("NC_FECHAAPROB").substring(0, 4);
               intUsoIEPS = rs.getInt("NC_USO_IEPS");
               BGTasaIEPS = rs.getBigDecimal("NC_TASA_IEPS");
               BGImporteIEPS = rs.getBigDecimal("NC_IMPORTE_IEPS");
               strFAC_FORMADEPAGO = rs.getString("NC_FORMADEPAGO");
               strFAC_CONDPAGO = rs.getString("NC_CONDPAGO");
               strFAC_METODODEPAGO = rs.getString("NC_METODODEPAGO");
               if (strFAC_METODODEPAGO.isEmpty()) {
                  strFAC_METODODEPAGO = "NO IDENTIFICADO";
               }
               strMoneda = rs.getString("NC_MONEDA");
               try {
                  intFolio = Integer.valueOf(strFolio);
               } catch (NumberFormatException ex) {
                  ex.fillInStackTrace();
                  this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                  strRes = "ERROR:" + ex.getMessage();
               }
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         } catch (SQLException ex) {
            ex.fillInStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
            strRes = "ERROR:" + ex.getMessage();
         }
      }
      try {
         /*Sacamos los datos de la empresa*/
         strSql = "SELECT EMP_RAZONSOCIAL,EMP_RFC,"
            + "EMP_CALLE,EMP_COLONIA,EMP_LOCALIDAD,EMP_MUNICIPIO,EMP_CP,"
            + "EMP_ESTADO,EMP_TIPOCOMP,EMP_TIPOCOMPNC,EMP_ACUSEFACTURA,EMP_ES_DONATARIA"
            + ",EMP_DONA_NUM_AUTORIZA,EMP_DONA_FECHA_AUTORIZA,EMP_DONA_LEYENDA "
            + "FROM vta_empresas WHERE EMP_ID = " + intEmpId;
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strNombreEmisor = FormateaTextoXml(rs.getString("EMP_RAZONSOCIAL"));
            strRFC = FormateaTextoXml(rs.getString("EMP_RFC"));
            strCalle = FormateaTextoXml(rs.getString("EMP_CALLE"));
            strColonia = FormateaTextoXml(rs.getString("EMP_COLONIA"));
            strLocalidad = FormateaTextoXml(rs.getString("EMP_LOCALIDAD"));
            strMunicipio = FormateaTextoXml(rs.getString("EMP_MUNICIPIO"));
            strCP = FormateaTextoXml(rs.getString("EMP_CP"));
            strEdo = FormateaTextoXml(rs.getString("EMP_ESTADO"));
            strPais = FormateaTextoXml("MEXICO");
            intEMP_ACUSEFACTURA = rs.getInt("EMP_ACUSEFACTURA");
            //Si el tipo de comprobante es cero tomamos el de la empresa
            if (intEMP_TIPOCOMP == 0) {
               if (!bolEsNc) {
                  intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
               } else {
                  intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMPNC");
               }
            }
            //Identificamos si es donataria
            if (rs.getInt("EMP_ES_DONATARIA") == 1) {
               this.bolDonataria = true;
               this.bolComplementoFiscal = true;
               this.strDonaNumAutoriza = rs.getString("EMP_DONA_NUM_AUTORIZA");
               this.strDonaFechaAutoriza = rs.getString("EMP_DONA_FECHA_AUTORIZA");
               this.strDonaLeyenda = rs.getString("EMP_DONA_LEYENDA");
            }
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
         /*Sacamos los datos de la sucursal*/
         strSql = "SELECT * "
            + "FROM vta_sucursal WHERE SC_ID = " + intSucursal;
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCalleSuc = FormateaTextoXml(rs.getString("SC_CALLE"));
            strColoniaSuc = FormateaTextoXml(rs.getString("SC_COLONIA"));
            strLocalidadSuc = FormateaTextoXml(rs.getString("SC_LOCALIDAD"));
            strMunicipioSuc = FormateaTextoXml(rs.getString("SC_MUNICIPIO"));
            strCPSuc = FormateaTextoXml(rs.getString("SC_CP"));
            strEdoSuc = FormateaTextoXml(rs.getString("SC_ESTADO"));
            strPaisSuc = FormateaTextoXml("MEXICO");
         }
         //Lugar de expedicion
         if (this.bolUsoLugarExpEmp) {
            strExpedidoEn = strCalle + " " + strColonia + " " + strMunicipio + " " + strCP;
         } else {
            strExpedidoEn = strCalleSuc + " " + strColoniaSuc + " " + strMunicipioSuc + " " + strCPSuc;
         }
         /*Sacamos los datos del cliente*/
         strSql = "SELECT CT_RAZONSOCIAL,CT_RFC,"
            + "CT_CALLE,CT_COLONIA,CT_LOCALIDAD,CT_MUNICIPIO,CT_CP,"
            + "CT_ESTADO,CT_NUMERO,CT_NUMINT,CT_EMAIL1,CT_EMAIL2 "
            + "FROM vta_cliente WHERE CT_ID = " + intCliente;
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strNombreReceptor = FormateaTextoXml(rs.getString("CT_RAZONSOCIAL"), false);
            strRFCReceptor = FormateaTextoXml(rs.getString("CT_RFC").replace("-", ""), false);
            strCalleReceptor = FormateaTextoXml(rs.getString("CT_CALLE"));
            strColoniaReceptor = FormateaTextoXml(rs.getString("CT_COLONIA"));
            strLocalidadReceptor = FormateaTextoXml(rs.getString("CT_LOCALIDAD"));
            strMunicipioReceptor = FormateaTextoXml(rs.getString("CT_MUNICIPIO"));
            strCPReceptor = FormateaTextoXml(rs.getString("CT_CP"));
            strEdoReceptor = FormateaTextoXml(rs.getString("CT_ESTADO"));
            strNumero = FormateaTextoXml(rs.getString("CT_NUMERO"));
            strNumeroInt = FormateaTextoXml(rs.getString("CT_NUMINT"));
            strMailCte = rs.getString("CT_EMAIL1");
            strMailCte2 = rs.getString("CT_EMAIL2");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
         //Obtenemos el regimel fiscal
         String strSqlRegFis = "select REGF_DESCRIPCION from vta_empregfiscal,vta_regimenfiscal "
            + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID "
            + " AND vta_empregfiscal.EMP_ID = " + intEmpId;
         rs = oConn.runQuery(strSqlRegFis, true);
         while (rs.next()) {
            lstRegimenFiscal.add(rs.getString("REGF_DESCRIPCION"));
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
         //Obtenemos el nombre de la moneda
         String strSqlMoneda = "SELECT MON_DESCRIPCION FROM vta_monedas "
            + " where MON_ID = " + strMoneda;
         rs = oConn.runQuery(strSqlMoneda, true);
         while (rs.next()) {
            strMoneda = rs.getString("MON_DESCRIPCION");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
         strRes = "ERROR:" + ex.getMessage();
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Generamos objeto para los comprobantes">
      ObjectFactory objFactory = new ObjectFactory();
      this.objComp = (Comprobante) objFactory.createComprobante();

      // <editor-fold defaultstate="collapsed" desc="Nodo Comprobante">
      /*Nodo Comprobante
       a. version
       b. serie
       c. folio
       d. fecha
       e. noAprobacion
       f. anoAprobacion
       g. tipoDeComprobante
       h. formaDePago
       i. condicionesDePago
       j. subTotal
       k. descuento
       l. total
      
       */
      objComp.setVersion("2.2");//Version 2.2
      if (!strFAC_SERIE.isEmpty()) {
         objComp.setSerie(strFAC_SERIE);
      }
      objComp.setFolio(intFolio + "");
      //XMLGregorianCalendar xmlfecha = this.fecha.RegresaXMLGregorianCalendar(strFechaFactura, strHoraFactura);
      SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMddHH:mm:ss");
      Date dateTmp = new Date();
      try {
         dateTmp = formateaDate.parse(strFechaFactura + strHoraFactura);
      } catch (ParseException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      }
      objComp.setFecha(dateTmp);
      objComp.setNoAprobacion(BigInteger.valueOf(Long.valueOf(strFAC_NOAPROB)));
      objComp.setAnoAprobacion(BigInteger.valueOf(Long.valueOf(strANIO_NOAPROB)));
      objComp.setTipoDeComprobante(strTipoComprobante);
      if (strFAC_FORMADEPAGO.isEmpty()) {
         strFAC_FORMADEPAGO = "Pago en una sola exhibición";
      }
      objComp.setFormaDePago(strFAC_FORMADEPAGO);
      if (!strFAC_CONDPAGO.isEmpty()) {
         objComp.setCondicionesDePago(strFAC_CONDPAGO);
      }
      objComp.setNoCertificado(RecuperaNoSerieCertificado());
      objComp.setSubTotal(new BigDecimal(NumberString.FormatearDecimal((BGSubtotal).doubleValue(), 2).replace(",", "")));
      objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal((BGTotal).doubleValue(), 2).replace(",", "")));
      objComp.setDescuento(new BigDecimal(NumberString.FormatearDecimal((BGDescuento).doubleValue(), 2).replace(",", "")));
      if (!strFAC_METODODEPAGO.isEmpty()) {
         objComp.setMetodoDePago(strFAC_METODODEPAGO);//Version 2.2
      }
      objComp.setLugarExpedicion(strExpedidoEn); //Version 2.2
      if (!strFAC_NUMCTA.isEmpty()) {
         objComp.setNumCtaPago(strFAC_NUMCTA);
      }
      if (!strMoneda.isEmpty()) {
         objComp.setMoneda(strMoneda);
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Nodo Emisor">
      /*Nodo Emisor
       Informacin del nodo Emisor
       a. rfc
       b. nombre
       3. Informacin del nodo DomicilioFiscal
       a. calle
       b. noExterior
       c. noInterior
       d. colonia
       e. localidad
       f. referencia
       g. municipio
       h. estado
       i. pais
       j. codigoPostal
       */
      Emisor emisor = objFactory.createComprobanteEmisor();
      emisor.setNombre(strNombreEmisor);
      emisor.setRfc(strRFC);
      /*Domicilio Fiscal*/
      TUbicacionFiscal domicilioFiscal = objFactory.createTUbicacionFiscal();
      domicilioFiscal.setCalle(strCalle);
      domicilioFiscal.setCodigoPostal(strCP);
      domicilioFiscal.setColonia(strColonia);
      domicilioFiscal.setEstado(strEdo);
      //domicilioFiscal.setLocalidad(strLocalidad);
      domicilioFiscal.setMunicipio(strMunicipio);
      domicilioFiscal.setPais(strPais);
      emisor.setDomicilioFiscal(domicilioFiscal);

      /*RegimenFiscal*/
      //Version 2.2
      RegimenFiscal regimenFiscal = objFactory.createComprobanteEmisorRegimenFiscal();
      Iterator<String> it = lstRegimenFiscal.iterator();
      while (it.hasNext()) {
         String strRegimen = it.next();
         regimenFiscal.setRegimen(strRegimen);
      }
      emisor.getRegimenFiscal().add(regimenFiscal);
      //emisor.setExpedidoEn(null);
      //TUbicacion  ubica = objFactory.createTUbicacion();
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Nodo Receptor">
      /*Nodo Receptor
       Informacin del nodo Receptor
       a. rfc
       b. nombre
       6. Informacin del nodo Domicilio
       a. calle
       b. noExterior
       c. noInterior
       d. colonia
       e. localidad
       f. referencia
       g. municipio
       h. estado
       i. pais
       j. codigoPostal
       */
      Receptor receptor = objFactory.createComprobanteReceptor();
      receptor.setNombre(strNombreReceptor);
      receptor.setRfc(strRFCReceptor);
      /*Domicilio Fiscal Nodo Receptor*/
      TUbicacion dRecep = objFactory.createTUbicacion();
      dRecep.setCalle(strCalleReceptor);
      dRecep.setColonia(strColoniaReceptor);
      dRecep.setCodigoPostal(strCPReceptor);
      //dRecep.setLocalidad(strLocalidadReceptor);
      dRecep.setMunicipio(strMunicipioReceptor);
      dRecep.setPais(strPais);
      dRecep.setNoExterior(strNumero);
      if (!strNumeroInt.trim().equals("")) {
         dRecep.setNoInterior(strNumeroInt);
      }
      dRecep.setEstado(strEdoReceptor);
      receptor.setDomicilio(dRecep);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Concepto">
      /*Concepto
       cantidad
       b. unidad
       c. noIdentificacion
       d. descripcion
       e. valorUnitario
       f. importe
       */
      Conceptos conceps = objFactory.createComprobanteConceptos();
      //Validamos el tipo de sistema y la tabla por abrir para recuperar el detalle de la operacion
      if (!bolEsNc) {
         if (this.bolEsLocal) {
            strSql = "SELECT FACD_ID,FACD_CVE,PR_ID,FACD_DESCRIPCION,FACD_COMENTARIO as comentario,"
               + "FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_UNIDAD_MEDIDA "
               + " FROM vta_facturasdeta WHERE FAC_ID  = " + this.intTransaccion;
         } else {
            strSql = "SELECT FACD_ID,FACD_CVE,PR_ID,FACD_DESCRIPCION,trim(FACD_COMENTARIO) as comentario,"
               + "FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_UNIDAD_MEDIDA,PDD_ID "
               + " FROM vta_facturasdeta WHERE FAC_ID  = " + this.intTransaccion;
         }
      } else if (this.bolEsLocal) {
         strSql = "SELECT NCD_ID,NCD_CVE,PR_ID,NCD_DESCRIPCION,NCD_COMENTARIO as comentario,"
            + "NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_UNIDAD_MEDIDA "
            + " FROM vta_ncreditodeta WHERE NC_ID  = " + this.intTransaccion;
      } else {
         strSql = "SELECT NCD_ID,NCD_CVE,PR_ID,NCD_DESCRIPCION,trim(NCD_COMENTARIO) as comentario,"
            + "NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_UNIDAD_MEDIDA "
            + " FROM vta_ncreditodeta WHERE NC_ID  = " + this.intTransaccion;
      }
      ResultSet rs2;
      //Para formatear fechas
      SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyyMMdd");
      String strPrefijoDeta = "FACD";
      if (bolEsNc) {
         strPrefijoDeta = "NCD";
      }
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intPDD_ID = 0;
            if (!bolEsNc) {
               if (!this.bolEsLocal) {
                  intPDD_ID = rs.getInt("PDD_ID");
               }
            }
            /*Creamos elemento del concepto*/
            String strUnidaddeMedida = rs.getString(strPrefijoDeta + "_UNIDAD_MEDIDA");
            if (strUnidaddeMedida.isEmpty()) {
               strUnidaddeMedida = "NO APLICA";
            }
            Concepto objC = objFactory.createComprobanteConceptosConcepto();
            if (rs.getString(strPrefijoDeta + "_CVE").equals("...") || rs.getString(strPrefijoDeta + "_CVE").equals("")) {
               //Servicios
               objC.setDescripcion(FormateaTextoXml(rs.getString("comentario")));
            } else {
               objC.setDescripcion(FormateaTextoXml(rs.getString(strPrefijoDeta + "_DESCRIPCION")));
            }
            objC.setCantidad(rs.getBigDecimal(strPrefijoDeta + "_CANTIDAD"));
            //CALCULAMOS IMPORTES SIN IVA
            double FACD_IMPORTE = rs.getDouble(strPrefijoDeta + "_IMPORTE");
            double FACD_IMPUESTO1 = rs.getDouble(strPrefijoDeta + "_IMPUESTO1");
            double FACD_IMPORTESINIVA = 0;
            double FACD_VALORUNIT = 0;
            if (this.bolEsLocal) {
               if (!rs.getString(strPrefijoDeta + "_CVE").equals("...") && !rs.getString(strPrefijoDeta + "_CVE").equals("")) {
                  //Es factura comercial
                  FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_IMPUESTO1;
               } else {
                  FACD_IMPORTESINIVA = FACD_IMPORTE;
               }
            } else {
               FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_IMPUESTO1;
            }
            FACD_VALORUNIT = FACD_IMPORTESINIVA / rs.getDouble(strPrefijoDeta + "_CANTIDAD");
            objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 5).replace(",", "")));
            objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(FACD_IMPORTESINIVA, 5).replace(",", "")));
            objC.setUnidad(strUnidaddeMedida);
            /*Informacion de la Aduana*/
            if (!rs.getString(strPrefijoDeta + "_CVE").equals("...")
               && !rs.getString(strPrefijoDeta + "_CVE").equals("SHIPPING")
               && !bolEsNc) {
               //Obtenemos informacion de la aduana

               String strAduana = "";
               String strFecha = "";
               String strPedimento = "";
               strSql = "SELECT vta_facturasdeta.FAC_ID, "
                  + " vta_facturasdeta.FACD_CVE, "
                  + " vta_facturasdeta.FACD_DESCRIPCION, "
                  + " vta_facturasdeta.FACD_IMPORTE, "
                  + " vta_facturasdeta.FACD_CANTIDAD, "
                  + " vta_movproddeta.PL_NUMLOTE, "
                  + " vta_movproddeta.MPD_SALIDAS, "
                  + " vta_movproddeta.MPD_FECHA, "
                  + " vta_movproddeta.MPD_COSTO, "
                  + " vta_movproddeta.MPD_IDORIGEN, "
                  + " vta_facturasdeta.FACD_ID, "
                  + " vta_movproddeta.PR_CODIGO, "
                  + " vta_movproddeta.PR_ID, "
                  + " vta_prodlote.PED_ID, "
                  + " vta_pedimentos.PED_COD, "
                  + " vta_pedimentos.PED_DESC, "
                  + " vta_pedimentos.PED_FECHA_ENTRA,"
                  + " vta_aduana.AD_CVE, "
                  + " vta_aduana.AD_DESCRIPCION"
                  + " FROM vta_movproddeta INNER JOIN vta_facturasdeta ON vta_movproddeta.PR_ID = vta_facturasdeta.PR_ID AND vta_facturasdeta.FACD_ID = vta_movproddeta.MPD_IDORIGEN"
                  + " INNER JOIN vta_prodlote ON vta_prodlote.PR_ID = vta_movproddeta.PR_ID AND vta_prodlote.PL_NUMLOTE = vta_movproddeta.PL_NUMLOTE"
                  + " INNER JOIN vta_pedimentos ON vta_pedimentos.PED_ID = vta_prodlote.PED_ID"
                  + " INNER JOIN vta_aduana ON vta_aduana.AD_ID = vta_pedimentos.AD_ID"
                  + " WHERE vta_facturasdeta.PDD_ID = 0"
                  + " AND vta_facturasdeta.FACD_ID=" + rs.getString(strPrefijoDeta + "_ID")
                  + " AND vta_facturasdeta.PR_ID =" + rs.getString("PR_ID");
               //Si el documento viene de un pedido hacemos la busqueda por pedidos
               if (intPDD_ID != 0) {
                  strSql = "SELECT vta_facturasdeta.FAC_ID, "
                     + "vta_facturasdeta.FACD_CVE, "
                     + "vta_facturasdeta.FACD_ID, "
                     + "vta_facturasdeta.FACD_DESCRIPCION, "
                     + "vta_facturasdeta.FACD_IMPORTE, "
                     + "vta_facturasdeta.FACD_CANTIDAD, "
                     + "vta_facturasdeta.PR_ID, "
                     + "vta_pedidosdeta.PD_ID, "
                     + "vta_pedidosdeta.PDD_CVE, "
                     + "vta_facturasdeta.PDD_ID, "
                     + "vta_pedidosdeta.PDD_ID, "
                     + "vta_facturasdeta.FACD_NOSERIE, "
                     + "vta_movproddeta.PL_NUMLOTE, "
                     + "vta_movproddeta.MPD_IDORIGEN, "
                     + "vta_prodlote.PL_ID,"
                     + "vta_prodlote.PED_ID,"
                     + "vta_aduana.AD_CVE, "
                     + "vta_aduana.AD_DESCRIPCION, "
                     + "vta_pedimentos.PED_COD, "
                     + "vta_pedimentos.PED_DESC, "
                     + "vta_pedimentos.PED_FECHA_ENTRA "
                     + " FROM vta_aduana INNER JOIN vta_pedimentos ON vta_aduana.AD_ID = vta_pedimentos.AD_ID"
                     + " INNER JOIN vta_prodlote ON vta_pedimentos.PED_ID = vta_prodlote.PED_ID"
                     + " INNER JOIN  vta_movproddeta ON vta_prodlote.PR_ID = vta_movproddeta.PR_ID AND vta_prodlote.PL_NUMLOTE = vta_movproddeta.PL_NUMLOTE"
                     + " INNER JOIN vta_pedidosdeta ON vta_movproddeta.PR_ID = vta_pedidosdeta.PR_ID AND vta_movproddeta.MPD_IDORIGEN = vta_pedidosdeta.PDD_ID"
                     + " INNER JOIN vta_facturasdeta ON vta_pedidosdeta.PDD_ID = vta_facturasdeta.PDD_ID"
                     + " WHERE vta_facturasdeta.PDD_ID <> 0"
                     + " AND vta_facturasdeta.FACD_ID=" + rs.getString(strPrefijoDeta + "_ID")
                     + " AND vta_facturasdeta.PR_ID =" + rs.getString("PR_ID");
               }
               //Sacamos informacin de la aduana
               rs2 = oConn.runQuery(strSql, true);
               while (rs2.next()) {
                  strAduana = rs2.getString("AD_DESCRIPCION");
                  strFecha = rs2.getString("PED_FECHA_ENTRA");
                  strPedimento = rs2.getString("PED_COD");
                  Date dtFecha = new Date();
                  try {
                     dtFecha = formatoDeFecha.parse(strFecha);
                     //Informacion de la aduana
                     TInformacionAduanera t = objFactory.createTInformacionAduanera();
                     t.setAduana(strAduana);
                     t.setFecha(dtFecha);
                     t.setNumero(strPedimento);
                     objC.getInformacionAduanera().add(t);
                  } catch (ParseException ex) {
                     Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
                  }
               }
               if (rs2.getStatement() != null) {
                  rs2.getStatement().close();
               }
               rs2.close();
            }
            /*Anadimos el concepto*/
            conceps.getConcepto().add(objC);
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getPart", oConn);
         strRes = "ERROR:" + ex.getMessage();
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Nodo impuestos">
      /**
       * Nodo impuestos
       */
      Impuestos imp = objFactory.createComprobanteImpuestos();
      /*Nodo traslados
       */
      Traslados objTraslados = objFactory.createComprobanteImpuestosTraslados();
      //Traslado IVA
      Traslado objTraslado = objFactory.createComprobanteImpuestosTrasladosTraslado();
      objTraslados.getTraslado().add(objTraslado);
      objTraslado.setImporte(BGImpuesto1);
      objTraslado.setImpuesto("IVA");
      objTraslado.setTasa(BGTasa1);
      //Traslado IEPS
      if (intUsoIEPS == 1) {
         Traslado objTrasladoIEPS = objFactory.createComprobanteImpuestosTrasladosTraslado();
         objTraslados.getTraslado().add(objTrasladoIEPS);
         objTrasladoIEPS.setImporte(BGImporteIEPS);
         objTrasladoIEPS.setImpuesto("IEPS");
         objTrasladoIEPS.setTasa(BGTasaIEPS);
      }
      //Anadimos totales
      imp.setTraslados(objTraslados);
      imp.setTotalImpuestosTrasladados(BGImpuesto1.add(BGImporteIEPS));
      //Validamos si es por honorarios para marcar la retencion
      if (BGImpuestoRetenido1.doubleValue() > 0) {
         /*Nodo retencion
          Informacin de cada nodo Retencion
          nota: esta secuencia a, b, deber ser repetida por cada nodo Retencin relacionado, el total
          de impuestos retenidos no se repite.
          a. impuesto
          b. importe
          c. totalImpuestosRetenidos
          */
         Retencion ret = objFactory.createComprobanteImpuestosRetencionesRetencion();
         ret.setImporte(BGImpuestoRetenido1);
         ret.setImpuesto("IVA");
         Retenciones rets = objFactory.createComprobanteImpuestosRetenciones();
         rets.getRetencion().add(ret);
         imp.setRetenciones(rets);
         imp.setTotalImpuestosRetenidos(BGImpuestoRetenido1);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Llenamos el comprobante con su detalle">
      objComp.setEmisor(emisor);
      objComp.setReceptor(receptor);
      objComp.setConceptos(conceps);
      objComp.setImpuestos(imp);
      // </editor-fold>

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Complementos fiscales dependiendo del rubro">
      ObtenerComplementos(objFactory, objComp);
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Generamos la firma">
      String strSello = GeneraFirma(objComp);
      objComp.setSello(strSello);
      if (strSello.startsWith("ERROR:")) {
         strRes = strSello;
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Pasos para el timbrado y Addenda">
      if (strRes.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Addenda">
         if (satAddenda != null) {
            /*Nodo especial para la Addenda*/
            Addenda addenda = objFactory.createComprobanteAddenda();
            //Llenamos la addenda
            satAddenda.FillAddenda(addenda, objComp, strPath, intTransaccion, oConn);
            //addenda.getAny().add(strXMLAddenda);
            objComp.setAddenda(addenda);
         }

         //Validamos si añadimos el certificado digital
         if (!this.strPathCert.isEmpty()) {
            UtilCert cert = new UtilCert();
            cert.OpenCert(this.strPathCert);
            if (!cert.getStrResult().startsWith("ERROR")) {
               objComp.setCertificado(cert.getCertContentBase64());
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Generamos el documento XML">
         try {
            JAXBContext jaxbContext;
            if (satAddenda == null) {
               if (this.bolComplementoFiscal && this.bolDonataria) {
                  jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
               } else {
                  jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class);
               }

            } else if (this.bolComplementoFiscal && this.bolDonataria) {
               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class, classAddenda, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
            } else {
               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class, classAddenda);
            }
            Marshaller marshaller = jaxbContext.createMarshaller();
            /*Vinculamos URI'S que solicita hacienda*/
            //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema-instance");
            //marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/2 http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv2.xsd");
            String strListXSD = "http://www.sat.gob.mx/cfd/2 http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv22.xsd";//Version 2.2
            if (this.bolComplementoFiscal && this.bolDonataria) {
               strListXSD += " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
            }
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, strListXSD);
            /*Formato de salida del XML*/
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //For change prefix
            if (satAddenda != null) {
               NamespacePrefixMapper prefix = new MyNamespacePrefixMapper();
               marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);
            }
            //Dependiendo de la version
            if (this.bolEsLocal) {
               System.out.println("Es local...");
               //marshaller.setProperty("com.sun.xml.internal.bind.xmlDeclaration", Boolean.FALSE);
               marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
               try {
                  marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
               } catch (PropertyException pex) {
                  pex.printStackTrace();
                  System.out.println("Error: " + pex.getMessage());
                  marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
               }

            } else {
               marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
               marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            }

            /*Guardamos comprobante*/
            String strNomFile = "";
            if (!bolEsNc) {
               strNomFile = strPath + "XmlSAT" + this.intTransaccion + " .xml";
            } else {
               strNomFile = strPath + "NC_XML" + this.intTransaccion + ".xml";
            }
            System.out.println("strNomFile:" + strNomFile);
            marshaller.marshal(objComp, new FileOutputStream(strNomFile));
            //Si hay una addenda ponemos los namespaces necesarios
            if (satAddenda != null) {
               satAddenda.makeNameSpaceDeclaration(strNomFile, intTransaccion, oConn);
            }
         } catch (FileNotFoundException ex) {
            ex.fillInStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXMLFile", oConn);
            strRes = "ERROR:" + ex.getMessage();
         } catch (JAXBException ex2) {
            ex2.printStackTrace();
            System.out.println("Hubo un error JAXBException ");
            //this.bitacora.GeneraBitacora(ex.getErrorCode(), this.varSesiones.getStrUser(), "genXML", oConn);
            System.out.println("ex2 " + ex2.getErrorCode() + " " + ex2.getMessage() + " " + ex2.toString());
            if (ex2 != null) {
               strRes = "ERROR:" + ex2.getErrorCode();
               System.out.println(" " + ex2.getErrorCode() + " " + ex2.getMessage());
            }
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Almacenamos el sello y la cadena original en la transaccion">
         if (!bolEsNc) {
            String strUpdate = "UPDATE vta_facturas "
               + "set FAC_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
               + ",FAC_SELLO='" + strSello + "'"
               + ",FAC_TIPOCOMP=" + intEMP_TIPOCOMP + " "
               + " where FAC_ID = " + this.intTransaccion;
            oConn.runQueryLMD(strUpdate);
         } else {
            String strUpdate = "UPDATE vta_ncredito "
               + "set NC_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
               + ",NC_SELLO='" + strSello + "'"
               + ",NC_TIPOCOMP=" + intEMP_TIPOCOMP + " "
               + " where NC_ID = " + this.intTransaccion;
            oConn.runQueryLMD(strUpdate);
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si todo fue OK generamos el PDF">
         if (strRes.equals("OK")) {
            if (bolSendMailMasivo) {
               String strRespForm = GeneraImpresionPDF(strPath, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
               if (strRespForm.equals("OK")) {
                  //Mandamos mail con la factura
                  if (!bolEsLocal) {
                     String strMsg = GeneraMail(strMailCte, strMailCte2, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                     if (!strMsg.equals("OK")) {
                        this.bitacora.GeneraBitacora("Envio mail:Fallido", oConn.getStrUsuario(), this.strFolio, oConn);
                     } else {
                        this.bitacora.GeneraBitacora("Envio mail:OK", oConn.getStrUsuario(), this.strFolio, oConn);
                        if (!bolEsNc) {
                           String strUpdate = "UPDATE vta_facturas "
                              + "set "
                              + "FAC_SENDMAIL=1"
                              + " where FAC_ID = " + this.intTransaccion;
                           oConn.runQueryLMD(strUpdate);
                        } else {
                           String strUpdate = "UPDATE vta_ncredito "
                              + "set "
                              + "NC_SENDMAIL=1"
                              + " where NC_ID = " + this.intTransaccion;
                           oConn.runQueryLMD(strUpdate);
                        }
                     }
                  }
               } else {
                  strRes = strRespForm;
               }
            }
         }
         // </editor-fold>
      }
      // </editor-fold>
      return strRes;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Envio de mail">
   /**
    * Envia el mail al cliente
    *
    * @param strMailCte Es el mail del cliente
    * @param strMailCte2 Es el segundo mail del cliente
    * @param intEMP_TIPOCOMP Es el tipo de comprobante
    * @param intEmpId Es el id de la empresa
    * @param strFAC_NOMFORMATO Es el nombre del formato
    * @return Regresa OK si fue exitoso el envio del mail
    */
   protected String GeneraMail(String strMailCte, String strMailCte2, int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO) {
      String strResp = "OK";
      //Nombre de archivo
      ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
      //Obtenemos datos del smtp
      String strsmtp_server = "";
      String strsmtp_user = "";
      String strsmtp_pass = "";
      String strsmtp_port = "";
      String strsmtp_usaTLS = "";
      String strsmtp_usaSTLS = "";
      //Buscamos los datos del SMTP
      String strSql = "select * from cuenta_contratada where ctam_id = 1";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strsmtp_server = rs.getString("smtp_server");
            strsmtp_user = rs.getString("smtp_user");
            strsmtp_pass = rs.getString("smtp_pass");
            strsmtp_port = rs.getString("smtp_port");
            strsmtp_usaTLS = rs.getString("smtp_usaTLS");
            strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      }
      //Obtenemos los textos para el envio del mail
      String strNomTemplate = "FACTURA";
      if (bolEsNc) {
         strNomTemplate = "NCREDITO";
      }
      String[] lstMail = getMailTemplate(strNomTemplate);

      /**
       * Si estan llenos todos los datos mandamos el mail
       */
      if (!strsmtp_server.equals("")
         && !strsmtp_user.equals("")
         && !strsmtp_pass.equals("")) {
         //armamos el mail
         Mail mail = new Mail();
         //Activamos envio de acuse de recibo
         if (intEMP_ACUSEFACTURA == 1) {
            mail.setBolAcuseRecibo(true);
         }
         //Obtenemos los usuarios a los que mandaremos el mail
         String strLstMail = "";
         strSql = "select * from usuarios where BOL_MAIL_FACT = 1";
         try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               if (mail.isEmail(rs.getString("EMAIL"))) {
                  strLstMail += rs.getString("EMAIL") + ",";
               }
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
            if (strLstMail.endsWith(",")) {
               strLstMail = strLstMail.substring(0, strLstMail.length() - 1);
            }
         } catch (SQLException ex) {
            Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
         }
         //Validamos si el mail del cliente es valido
         boolean bolMailCteValido1 = false;
         boolean bolMailCteValido2 = false;
         if (mail.isEmail(strMailCte)) {
            //strLstMail += "," + strMailCte;
            bolMailCteValido1 = true;
         }
         if (mail.isEmail(strMailCte2)) {
            //strLstMail += "," + strMailCte2;
            bolMailCteValido2 = true;
         }
         //Validamos lista de mails especiales
         if (!this.strListMailsEsp.isEmpty()) {
            strLstMail += "," + this.strListMailsEsp;
         }
         //Mandamos mail si hay usuarios
         if (!strLstMail.equals("") || bolMailCteValido1 || bolMailCteValido2) {
            String strMsgMail = lstMail[1];
            strMsgMail = strMsgMail.replace("%folio%", this.objComp.getFolio());
            //Establecemos parametros
            mail.setUsuario(strsmtp_user);
            mail.setContrasenia(strsmtp_pass);
            mail.setHost(strsmtp_server);
            mail.setPuerto(strsmtp_port);
            mail.setAsunto(lstMail[0].replace("%folio%", this.objComp.getFolio()));
            mail.setMensaje(strMsgMail);
            //Adjuntamos XML y PDF
            if (!bolEsNc) {
               mail.setFichero(strPath + "XmlSAT" + this.intTransaccion + " .xml");
               mail.setFichero(strPath + mapeo.getStrNomArchivo() + this.strFolio + ".pdf");
            } else {
               mail.setFichero(strPath + "NC_XML" + this.intTransaccion + ".xml");
               mail.setFichero(strPath + mapeo.getStrNomArchivo() + this.strFolio + ".pdf");
            }
            if (strsmtp_usaTLS.equals("1")) {
               mail.setBolUsaTls(true);
            }
            if (strsmtp_usaSTLS.equals("1")) {
               mail.setBolUsaStartTls(true);
            }
            //Envio a los usuarios
            if (!strLstMail.equals("")) {
               mail.setDestino(strLstMail);
               boolean bol = mail.sendMail();
               if (!bol) {
                  //strResp = "Fallo el envio del Mail.";
               }
            }
            //Enviamos al primer mail del cliente
            if (bolMailCteValido1) {
               mail.setDestino(strMailCte);
               boolean bol = mail.sendMail();
               if (!bol) {
                  strResp = "Fallo el envio del Mail.";
               }
            }
            //Enviamos al segundo mail del cliente
            if (bolMailCteValido2) {
               mail.setDestino(strMailCte2);
               boolean bol = mail.sendMail();
               if (!bol) {
                  strResp = "Fallo el envio del Mail.";
               }
            }

         }
         //Si los correos del cliente no son validos indicamos que no se pudo enviar
         if (!bolMailCteValido1 && !bolMailCteValido2) {
            strResp = "El mail no es valido..";
         }

      }
      return strResp;
   }

   /**
    * Obtenemos los valores del template para el mail
    *
    * @param strNom Es el nombre del template
    * @return Regresa un arreglo con los valores del template
    */
   public String[] getMailTemplate(String strNom) {
      String[] listValores = new String[2];
      String strSql = "select MT_ASUNTO,MT_CONTENIDO from mailtemplates where MT_ABRV ='" + strNom + "'";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            listValores[0] = rs.getString("MT_ASUNTO");
            listValores[1] = rs.getString("MT_CONTENIDO");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      }
      return listValores;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Impresion y PDF">
   /**
    * Genera el formato de impresion en PDF
    *
    * @param strPath Es el path
    * @param intEMP_TIPOCOMP Es el tipo de comprobante
    * @param intEmpId Es el id de la empresa
    * @param strFAC_NOMFORMATO Es el nombre del formato
    * @return Regresa OK si se genero el formato
    */
   protected String GeneraImpresionPDF(String strPath, int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO) {
      String strResp = "OK";
      //Posicion inicial para el numero de pagina
      String strPosX = null;
      String strTitle = "";
      strTitle = "Factura ";
      /*Validaciones dependiendo del tipo de comprobante*/
      ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
      try {
         Document document = new Document();
         PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strPath + mapeo.getStrNomArchivo() + this.strFolio + ".pdf"));
         //Objeto que dibuja el numero de paginas
         PDFEventPage pdfEvent = new PDFEventPage();
         pdfEvent.setStrTitleApp(strTitle);
         //Colocamos el numero donde comienza X por medio del parametro del web Xml por si necesitamos algun ajuste
         if (strPosX != null) {
            try {
               int intPosX = Integer.valueOf(strPosX);
               pdfEvent.setIntXPageNum(intPosX);
            } catch (NumberFormatException ex) {
            }
         } else {
            pdfEvent.setIntXPageNum(300);
            pdfEvent.setIntXPageNumRight(50);
            pdfEvent.setIntXPageTemplate(252.3f);
         }
         //Anexamos el evento
         writer.setPageEvent(pdfEvent);
         document.open();
         Formateador format = new Formateador();
         format.setIntTypeOut(Formateador.FILE);
         format.setStrPath(strPath);
         if (!strFAC_NOMFORMATO.equals("")) {
            format.InitFormat(oConn, strFAC_NOMFORMATO);
         } else {
            //Buscamos si la empresa usa CBB
            int intEMP_USACODBARR = 0;
            int intEMP_CFD_CFDI = 0;
            String strSql = "select EMP_USACODBARR,EMP_CFD_CFDI from vta_empresas where EMP_ID = " + intEmpId;
            ResultSet rs = oConn.runQuery(strSql, true);
            //Buscamos el nombre del archivo
            while (rs.next()) {
               intEMP_USACODBARR = rs.getInt("EMP_USACODBARR");
               intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
            String strNomFormato = mapeo.getStrNomFormato();
            if (intEMP_USACODBARR == 1) {
               strNomFormato += "_CBB";
            }
            if (intEMP_CFD_CFDI == 1) {
               strNomFormato += "_cfdi";
            }
            format.InitFormat(oConn, strNomFormato);
         }
         String strRes = format.DoFormat(oConn, this.intTransaccion);
         if (strRes.equals("OK")) {
            CIP_Formato fPDF = new CIP_Formato();
            fPDF.setDocument(document);
            fPDF.setWriter(writer);
            fPDF.setStrPathFonts(strPATHFonts);
            fPDF.EmiteFormato(format.getFmXML());
         } else {
            strResp = strRes;
         }
         document.close();
         writer.close();
      } catch (SQLException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      } catch (FileNotFoundException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
         strResp = "ERROR:" + ex.getMessage();
      } catch (DocumentException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
         strResp = "ERROR:" + ex.getMessage();
      }
      return strResp;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Firmas digitales">
   /**
    * Genera la firma de un comprobante Fiscal
    *
    * @param objComp Es el comprobante fiscal que se firmara
    * @return Nos regresa la cadena firmada
    */
   public String GeneraFirma(Comprobante objComp) {
      String strValorEncrip = GeneraCadenaOriginal(objComp);
      strCadenaOriginalReal = strValorEncrip;
      String strValorSello = "";
      try {
         PrivateKey key;
         key = ObtenerPrivateKey(this.strPathKey, this.strPassKey);
         if (key == null) {
            strValorSello = "ERROR:NO SE PUDO ABRIR EL SELLO";
         } else {
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
               /*Codigo generico*/
            } catch (IOException ex) {
               Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
               strValorSello = "ERROR:" + ex.getMessage();
            }
         }
      } catch (SignatureException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "signature", oConn);
         ex.fillInStackTrace();
         strValorSello = "ERROR:" + ex.getMessage();
      } catch (InvalidKeyException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "invalidKey", oConn);
         ex.fillInStackTrace();
         strValorSello = "ERROR:" + ex.getMessage();
      } catch (NoSuchAlgorithmException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "noSuchAlgoritm", oConn);
         ex.fillInStackTrace();
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
         log.error("FileNotFoundException-..." + ex.getMessage());
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
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "PKCS8Key", oConn);
            ex.fillInStackTrace();
            log.error("GeneralSecurityException-..." + ex.getMessage());
            bolError = true;
         } catch (IOException ex) {
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "IoExcep", oConn);
            ex.fillInStackTrace();
            log.error("IOException-..." + ex.getMessage());
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
                     this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "RSA", oConn);
                     ex.fillInStackTrace();
                     log.error("InvalidKeySpecException-..." + ex.getMessage());
                  }
               } catch (NoSuchAlgorithmException ex) {
                  this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "NORSAAlgoritm", oConn);
                  ex.fillInStackTrace();
                  log.error("NoSuchAlgorithmException-..." + ex.getMessage());
               }
            }
            // For lazier types (like me):
            pk = pkcs8.getPrivateKey();
         }
      }
      return pk;
   }

   /**
    * Obtenemos la llave privada proporcionada por SAT
    *
    * @param strPath Es el path donde esta la llave
    * @param strPass Es el password
    * @return Regresa true si se pudo abrir
    */
   public static boolean ValidaPrivateKey(String strPath, String strPass) {
      FileInputStream in = null;//"C:/sat/Cer_Sello/aaa010101aaa_CSD_01.key"
      // A Java PrivateKey object is born.
      PrivateKey pk = null;
      boolean bolValido = true;
      try {
         in = new FileInputStream(strPath);
      } catch (FileNotFoundException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
         bolValido = false;
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
            bolValido = false;
         } catch (IOException ex) {
            ex.fillInStackTrace();
            bolValido = false;
         }
         //Si no hubo error proseguimos
         if (bolValido) {
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
                     bolValido = false;
                  }
               } catch (NoSuchAlgorithmException ex) {
                  ex.fillInStackTrace();
                  bolValido = false;
               }
            }
            // For lazier types (like me):
            pk = pkcs8.getPrivateKey();
         }
      }
      return bolValido;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Cadena original">
   /**
    * Genera la cadena original por sellar
    *
    * @param objComp Es el objeto comprobante
    * @return Regresa la cadena original
    */
   public String GeneraCadenaOriginal(Comprobante objComp) {
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
         Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
      }
      org.w3c.dom.Document doc = db.newDocument();
      DOMSource xmlDomSource = null;
      try {
         JAXBContext jaxbContext;
         if (satAddenda == null) {
            if (this.bolComplementoFiscal && this.bolDonataria) {
               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
            } else {
               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class);
            }

         } else if (this.bolComplementoFiscal && this.bolDonataria) {
            jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class, classAddenda, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
         } else {
            jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT22.ObjectFactory.class, classAddenda);
         }
         Marshaller marshaller = jaxbContext.createMarshaller();
         /*Vinculamos URI'S que solicita hacienda*/
         //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema-instance");
         //marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/2 http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv2.xsd");
         String strListXSD = "http://www.sat.gob.mx/cfd/2 http://www.sat.gob.mx/sitio_internet/cfd/2/cfdv22.xsd";//Version 2.2
         if (this.bolComplementoFiscal && this.bolDonataria) {
            strListXSD += " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
         }
         marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, strListXSD);
         /*Formato de salida del XML*/
         marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
         marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
         //For change prefix
         if (satAddenda != null) {
            NamespacePrefixMapper prefix = new MyNamespacePrefixMapper();
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);
         }
         //Dependiendo de la version
         if (this.bolEsLocal) {
            //marshaller.setProperty("com.sun.xml.internal.bind.xmlDeclaration", Boolean.FALSE);
         } else {
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
         }
         /*Guardamos comprobante*/
         marshaller.marshal(objComp, doc);
         // Use the DOM Document to define a DOMSource object.
         xmlDomSource = new DOMSource(doc);
         // Set the base URI for the DOMSource so any relative URIs it contains can
         // be resolved.
         xmlDomSource.setSystemId("sat.xml");
      } catch (JAXBException ex) {
         ex.fillInStackTrace();
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXML", oConn);
      }
      try {
         Transformer transformer = tFactory.newTransformer(new StreamSource(this.strPath + System.getProperty("file.separator") + "cadenaoriginal_2_2.xslt"));
         transformer.transform(xmlDomSource, new StreamResult(strCadenaStr));
      } catch (TransformerException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      }
      //System.out.println("***************" + strCadenaStr);
      return strCadenaStr.toString();
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Varios">
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
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Complementos fiscales">
   private void ObtenerComplementos(ObjectFactory objFactory, Comprobante objComp) {
      //Validamos que apliquen los complementos
      if (this.bolComplementoFiscal == true) {
         if (this.bolDonataria == true) {
            // Donatarias
            Donatarias donataria = ObtenNodoDonatarias();
            Complemento complemento = objFactory.createComprobanteComplemento();
            complemento.getAny().add(donataria);
            objComp.setComplemento(complemento);
         }
      }
   }

   /**
    * Regresa el objeto del complemento de donataria
    *
    * @return Regresa el objeto Donataria del complemento del SAT
    */
   protected Donatarias ObtenNodoDonatarias() {
      Core.FirmasElectronicas.complementos.donatarias.ObjectFactory factory
         = new Core.FirmasElectronicas.complementos.donatarias.ObjectFactory();
      Donatarias donataria = factory.createDonatarias();
      donataria.setVersion("1.1");
      donataria.setNoAutorizacion(this.strDonaNumAutoriza);
      donataria.setLeyenda(this.strDonaLeyenda);
      XMLGregorianCalendar xmlfecha = this.fecha.RegresaXMLGregorianCalendar(this.strDonaFechaAutoriza, "12:30:00");
      donataria.setFechaAutorizacion(xmlfecha.toGregorianCalendar().getTime());
      return donataria;
   }
   // </editor-fold>
   // </editor-fold>
}
