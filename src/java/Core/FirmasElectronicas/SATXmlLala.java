package Core.FirmasElectronicas;

import Core.FirmasElectronicas.Addendas.SATAddendaLala;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Complemento;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Conceptos;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Conceptos.Concepto;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Emisor;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Emisor.RegimenFiscal;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Impuestos;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Impuestos.Retenciones;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Impuestos.Retenciones.Retencion;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Impuestos.Traslados;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Impuestos.Traslados.Traslado;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Receptor;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.AdditionalInformation;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.BaseAmount;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.Buyer;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.LineItem;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.OrderIdentification;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.OrderIdentification.ReferenceIdentification;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.PayableAmount;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.RequestForPaymentIdentification;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.Seller;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.ShipTo;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.SpecialInstruction;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.Tax;
import Core.FirmasElectronicas.SAT_AMECE.Comprobante.Addenda.RequestForPayment.TotalAmount;
import Core.FirmasElectronicas.SAT_AMECE.ObjectFactory;
import Core.FirmasElectronicas.SAT_AMECE.TUbicacion;
import Core.FirmasElectronicas.SAT_AMECE.TUbicacionFiscal;

import Core.FirmasElectronicas.Utils.UtilCert;
import Core.FirmasElectronicas.complementos.donatarias.Donatarias;
import ERP.ERP_MapeoFormato;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.NumberString;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Utilerias.Mail;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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

/**
 *Clase para generar XML del SAT versión 3.2   
 * @author zeus
 */
public class SATXmlLala extends SATXml {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Comprobante objComp;
   protected PACS_CFDIS enumPAC;
   protected String strPathConfigPAC;
   protected byte[] certificadoEmisor;
   protected byte[] llavePrivadaEmisor;
   protected String strfolioFiscalUUID;
   protected String strFechaTimbre;
   protected String strSelloSAT;
   protected String strSelloCFD;
   protected String strNoCertSAT;
   protected String strPathQR;

   /**
    * Lista de PACS
    */
   public static enum PACS_CFDIS {

      FACTURA_EN_SEGUNDOS,
      TIMBRE_FISCAL
   }

   /**
    * Regresa el PAC a usar para Timbrar la factura
    * @return Regresa un el valor de la constante del PAC
    */
   public PACS_CFDIS getEnumPAC() {
      return enumPAC;
   }

   /**
    * Define el PAC a usar para Timbrar la factura
    * @param enumPAC Es  la constante del PAC a usar
    */
   public void setEnumPAC(PACS_CFDIS enumPAC) {
      this.enumPAC = enumPAC;
   }

   /**
    * Regresa la url donde se ubica el archivo de configuración del PAC
    * @return Es una cadena con un path
    */
   public String getStrPathConfigPAC() {
      return strPathConfigPAC;
   }

   /**
    * Define el path donde se ubica el archivo de configuracion del PAC
    * @param strPathConfigPAC Es una cadena con un path
    */
   public void setStrPathConfigPAC(String strPathConfigPAC) {
      this.strPathConfigPAC = strPathConfigPAC;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor para objeto que genera facturas con cfdi's
    * @param intTransaccion Es el id de la transacción
    * @param strPath Es el path a usar
    * @param NoAprobacion Es el numero de aprobacion
    * @param FechaAprobacion Es la fecha de aprobación
    * @param NoSerieCert Es el numero de serie del certificado
    * @param strPathKey Es el path de la llave privada
    * @param strPassKey Es el password de la llave privada
    * @param varSesiones Es el objeto con los valores de la variable de sesion
    * @param oConn Es la conexion
    */
   public SATXmlLala(int intTransaccion, String strPath, String NoAprobacion, String FechaAprobacion, String NoSerieCert, String strPathKey, String strPassKey, VariableSession varSesiones, Conexion oConn) {
      super(intTransaccion, strPath, NoAprobacion, FechaAprobacion, NoSerieCert, strPathKey, strPassKey, varSesiones, oConn);
      this.enumPAC = SATXmlLala.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
      this.strPathConfigPAC = "";
   }

   /**
    * Constructor vacio
    */
   public SATXmlLala() {
      super();
      this.enumPAC = SATXmlLala.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
      this.strPathConfigPAC = "";
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos Factura CFDI">
   // <editor-fold defaultstate="collapsed" desc="Generacion de XML">
   /**
    * Genera el XML para el documento electronico
    * @return Regresa OK en caso de que todo halla sido satisfactorio
    */
   @Override
   public String GeneraXml() {

      // <editor-fold defaultstate="collapsed" desc="Inicializamos los valores del comprobante">
      strFolio = "";
      String strRes = "OK";
      BigDecimal BGSubtotal = null;
      BigDecimal BGDescuento = null;
      BigDecimal BGTotal = null;
      BigDecimal BGImpuesto1 = null;
      BigDecimal BGImpuestoRetenido1 = null;
      BigDecimal BGTasa1 = null;
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
      int intUsoIEPS = 0;
      BigDecimal BGTasaIEPS = null;
      BigDecimal BGImporteIEPS = null;
      String strMailCte = "";
      String strMailCte2 = "";
      int intFolio = 0;
      String strFAC_NOMFORMATO = "";
      ResultSet rs;
      String strSql = "";
      String strFAC_CONDPAGO = "";
      String strFAC_FORMADEPAGO = "";
      String strFAC_METODODEPAGO = "";
      String strFAC_NUMCTA = "";
      String strCalleSuc = "";
      String strColoniaSuc = "";
      String strLocalidadSuc = "";
      String strMunicipioSuc = "";
      String strCPSuc = "";
      String strEdoSuc = "";
      String strPaisSuc = "";
      String strExpedidoEn = "";
      /**23/02/2013 ADDENDA AMECE*/
      String strHM_REFERENCEDATE = "";
      String strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY = "";
      String strHM_DIV = "";
      String strHM_ON = "";
      String strHM_SOC = "";
      /**23/02/2013 ADDENDA AMECE*/
      ArrayList<String> lstRegimenFiscal = new ArrayList<String>();
      String strMoneda = "";
      // </editor-fold >

      // <editor-fold defaultstate="collapsed" desc="Consultamos datos de la base de datos">

      //Validamos si es nota de credito
      if (!bolEsNc) {
         /*Consultamos datos del docto*/
         strSql = "SELECT FAC_FOLIO,FAC_FECHA,FAC_IMPORTE,FAC_DESCUENTO,"
                 + " FAC_CONDPAGO,FAC_FORMADEPAGO,FAC_METODODEPAGO,"
                 + " FAC_TOTAL,SC_ID,EMP_ID,FAC_IMPUESTO1,FAC_RETIVA,FAC_TASA1,CT_ID,FAC_HORA,"
                 + " FAC_SERIE,FAC_NOAPROB,FAC_FECHAAPROB,FAC_NOMFORMATO, "
                 + " FAC_USO_IEPS,FAC_TASA_IEPS,FAC_IMPORTE_IEPS,FAC_NUMCUENTA,FAC_MONEDA"
                 + ",HM_SOC,HM_DIV,HM_ON,HM_REFERENCEDATE,HM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY "
                 + " FROM vta_facturas WHERE FAC_ID = " + this.intTransaccion;
         try {
            rs = oConn.runQuery(strSql);
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
               if (!rs.getString("FAC_FECHAAPROB").isEmpty()) {
                  strANIO_NOAPROB = rs.getString("FAC_FECHAAPROB").substring(0, 4);
               }
               /**23/02/2013 ADDENDA AMECE*/
               strHM_SOC = rs.getString("HM_SOC");
               strHM_DIV = rs.getString("HM_DIV");
               strHM_ON = rs.getString("HM_ON");
               strHM_REFERENCEDATE = rs.getString("HM_REFERENCEDATE");
               strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY = rs.getString("HM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
               /**23/02/2013 ADDENDA AMECE*/
               intUsoIEPS = rs.getInt("FAC_USO_IEPS");
               BGTasaIEPS = rs.getBigDecimal("FAC_TASA_IEPS");
               BGImporteIEPS = rs.getBigDecimal("FAC_IMPORTE_IEPS");
               strFAC_FORMADEPAGO = rs.getString("FAC_FORMADEPAGO");
               strFAC_CONDPAGO = rs.getString("FAC_CONDPAGO");
               strFAC_METODODEPAGO = rs.getString("FAC_METODODEPAGO");
               strFAC_NUMCTA = rs.getString("FAC_NUMCUENTA");
               strMoneda = rs.getString("FAC_MONEDA");
               if (!strFolio.isEmpty()) {
                  try {
                     intFolio = Integer.valueOf(strFolio);
                  } catch (NumberFormatException ex) {
                     ex.fillInStackTrace();
                     this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                     strRes = "ERROR:" + ex.getMessage();
                  }
               }
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
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
                 + " NC_SERIE,NC_NOAPROB,NC_FECHAAPROB,NC_NOMFORMATO, "
                 + " NC_USO_IEPS,NC_TASA_IEPS,NC_IMPORTE_IEPS,NC_MONEDA "
                 + " FROM vta_ncredito WHERE NC_ID = " + this.intTransaccion;
         try {
            rs = oConn.runQuery(strSql);
            while (rs.next()) {
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
               if (!rs.getString("NC_FECHAAPROB").isEmpty()) {
                  strANIO_NOAPROB = rs.getString("NC_FECHAAPROB").substring(0, 4);
               }
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
            //if(rs.getStatement() != null )rs.getStatement().close(); 
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
                 + "EMP_ESTADO,EMP_TIPOCOMP,EMP_TIPOCOMPNC,EMP_ACUSEFACTURA,EMP_ES_DONATARIA "
                 + ",EMP_DONA_NUM_AUTORIZA,EMP_DONA_FECHA_AUTORIZA,EMP_DONA_LEYENDA "
                 + "FROM vta_empresas WHERE EMP_ID = " + intEmpId;
         rs = oConn.runQuery(strSql);
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
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();

         /*Sacamos los datos de la sucursal*/
         strSql = "SELECT * "
                 + "FROM vta_sucursal WHERE SC_ID = " + intSucursal;
         rs = oConn.runQuery(strSql);
         while (rs.next()) {
            strCalleSuc = FormateaTextoXml(rs.getString("SC_CALLE"));
            strColoniaSuc = FormateaTextoXml(rs.getString("SC_COLONIA"));
            strLocalidadSuc = FormateaTextoXml(rs.getString("SC_LOCALIDAD"));
            strMunicipioSuc = FormateaTextoXml(rs.getString("SC_MUNICIPIO"));
            strCPSuc = FormateaTextoXml(rs.getString("SC_CP"));
            strEdoSuc = FormateaTextoXml(rs.getString("SC_ESTADO"));
            strPaisSuc = FormateaTextoXml("MEXICO");
         }
         rs.close();
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
         rs = oConn.runQuery(strSql);
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
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
         //Obtenemos el regimel fiscal
         String strSqlRegFis = "select REGF_DESCRIPCION from vta_empregfiscal,vta_regimenfiscal "
                 + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID "
                 + " AND vta_empregfiscal.EMP_ID = " + intEmpId;
         rs = oConn.runQuery(strSqlRegFis);
         while (rs.next()) {
            lstRegimenFiscal.add(rs.getString("REGF_DESCRIPCION"));
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
         //Obtenemos el nombre de la moneda
         String strSqlMoneda = "SELECT MON_DESCRIPCION FROM vta_monedas "
                 + " where MON_ID = " + strMoneda;
         rs = oConn.runQuery(strSqlMoneda);
         while (rs.next()) {
            strMoneda = rs.getString("MON_DESCRIPCION");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
         strRes = "ERROR:" + ex.getMessage();
      }

      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Generamos objeto para los comprobantes">
      ObjectFactory objFactory = new ObjectFactory();
      this.objComp = objFactory.createComprobante();

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
      2. Informacin
       */
      objComp.setVersion("3.2");
      //objComp.setSerie(strFAC_SERIE);
      //objComp.setFolio(intFolio + "");
      //XMLGregorianCalendar xmlfecha = this.fecha.RegresaXMLGregorianCalendar(strFechaFactura, strHoraFactura);
      SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMddHH:mm:ss");
      Date dateTmp = new Date();
      try {
         dateTmp = formateaDate.parse(strFechaFactura + strHoraFactura);
      } catch (ParseException ex) {
         Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
      }
      objComp.setFecha(dateTmp);
      //objComp.setFecha(xmlfecha.toGregorianCalendar().getTime());
      objComp.setFormaDePago(strFAC_FORMADEPAGO);
      if (!strFAC_CONDPAGO.isEmpty()) {
         objComp.setCondicionesDePago(strFAC_CONDPAGO);
      }
      objComp.setNoCertificado(RecuperaNoSerieCertificado());
      //Certificado digital
      UtilCert cert = new UtilCert();
      cert.OpenCert(this.strPathCert);
      if (!cert.getStrResult().startsWith("ERROR")) {
         objComp.setCertificado(cert.getCertContentBase64());
         try {
            this.certificadoEmisor = cert.getCert().getEncoded();
         } catch (CertificateEncodingException ex) {
            Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      objComp.setSubTotal(new BigDecimal(NumberString.FormatearDecimal((BGSubtotal).doubleValue(), 2).replace(",", "")));
      objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal((BGTotal).doubleValue(), 2).replace(",", "")));
      objComp.setTipoDeComprobante(strTipoComprobante);
      if (!strFAC_METODODEPAGO.isEmpty()) {
         objComp.setMetodoDePago(strFAC_METODODEPAGO);
      }
      if (!strFAC_NUMCTA.isEmpty()) {
         objComp.setNumCtaPago(strFAC_NUMCTA);
      }
      objComp.setLugarExpedicion(strExpedidoEn);
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

      // <editor-fold defaultstate="collapsed" desc="Conceptos">
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
            strSql = "SELECT FACD_CVE,PR_ID,FACD_DESCRIPCION,FACD_COMENTARIO as comentario,"
                    + "FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_UNIDAD_MEDIDA "
                    + " FROM vta_facturasdeta WHERE FAC_ID  = " + this.intTransaccion;
         } else {
            strSql = "SELECT FACD_CVE,PR_ID,FACD_DESCRIPCION,trim(FACD_COMENTARIO) as comentario,"
                    + "FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_UNIDAD_MEDIDA "
                    + " FROM vta_facturasdeta WHERE FAC_ID  = " + this.intTransaccion;
         }
      } else {
         if (this.bolEsLocal) {
            strSql = "SELECT NCD_CVE,PR_ID,NCD_DESCRIPCION,NCD_COMENTARIO as comentario,"
                    + "NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_UNIDAD_MEDIDA "
                    + " FROM vta_ncreditodeta WHERE NC_ID  = " + this.intTransaccion;
         } else {
            strSql = "SELECT NCD_CVE,PR_ID,NCD_DESCRIPCION,trim(NCD_COMENTARIO) as comentario,"
                    + "NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_UNIDAD_MEDIDA "
                    + " FROM vta_ncreditodeta WHERE NC_ID  = " + this.intTransaccion;
         }
      }
      ResultSet rs2;
      String strPrefijoDeta = "FACD";
      if (bolEsNc) {
         strPrefijoDeta = "NCD";
      }
      try {
         rs = oConn.runQuery(strSql);
         while (rs.next()) {
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
            if (rs.getString(strPrefijoDeta + "_CVE").isEmpty()) {
               objC.setNoIdentificacion("Servicio");
            } else {
               objC.setNoIdentificacion(rs.getString(strPrefijoDeta + "_CVE"));
            }
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
            if (!rs.getString(strPrefijoDeta + "_CVE").equals("...") && !rs.getString(strPrefijoDeta + "_CVE").equals("SHIPPING")) {
               //Obtenemos informacion de la aduana
               String strAduana = "";
               String strFecha = "";
               String strPedimento = "";
               strSql = "SELECT PL_NUMLOTE,PL_FECHA,PL_PEDIMENTO FROM vta_prodlote "
                       + "where PR_ID=" + rs.getString("PR_ID") + " "
                       + "AND PL_NUMLOTE ='" + rs.getString(strPrefijoDeta + "_NOSERIE") + "'";
               //Sacamos informacin de la aduana
               rs2 = oConn.runQuery(strSql);
               while (rs2.next()) {
                  strAduana = rs2.getString("PL_PEDIMENTO");
                  strFecha = rs2.getString("PL_FECHA");
                  strPedimento = rs2.getString("PL_NUMLOTE");
               }
               //Informacion de la aduana
               /*
               InformacionAduanera t = objFactory.createConceptoInformacionAduanera();
               t.setAduana(strAduana);
               t.setFecha(fecha2);
               t.setNumero(strPedimento);
               objC.setInformacionAduanera(t);
                * */
            }
            /*Anadimos el concepto*/
            conceps.getConcepto().add(objC);
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getPart", oConn);
         strRes = "ERROR:" + ex.getMessage();
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Nodo Impuestos">
      Impuestos imp = objFactory.createComprobanteImpuestos();
      /*Nodo traslados
       */
      Traslado objTraslado = objFactory.createComprobanteImpuestosTrasladosTraslado();
      Traslados objTraslados = objFactory.createComprobanteImpuestosTraslados();
      objTraslados.getTraslado().add(objTraslado);
      objTraslado.setImporte(new BigDecimal(NumberString.FormatearDecimal((BGImpuesto1).doubleValue(), 2).replace(",", "")));
      objTraslado.setImpuesto("IVA");
      objTraslado.setTasa(new BigDecimal(NumberString.FormatearDecimal((BGTasa1).doubleValue(), 2).replace(",", "")));
      //Traslado IEPS
      if (intUsoIEPS == 1) {
         Traslado objTrasladoIEPS = objFactory.createComprobanteImpuestosTrasladosTraslado();
         objTraslados.getTraslado().add(objTrasladoIEPS);
         objTrasladoIEPS.setImporte(BGImporteIEPS);
         objTrasladoIEPS.setImpuesto("IEPS");
         objTrasladoIEPS.setTasa(BGTasaIEPS);
      }
      imp.setTraslados(objTraslados);
      imp.setTotalImpuestosTrasladados(new BigDecimal(NumberString.FormatearDecimal((BGImpuesto1.add(BGImporteIEPS)).doubleValue(), 2).replace(",", "")));
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
         ret.setImporte(new BigDecimal(NumberString.FormatearDecimal((BGImpuestoRetenido1).doubleValue(), 2).replace(",", "")));
         ret.setImpuesto("IVA");
         Retenciones rets = objFactory.createComprobanteImpuestosRetenciones();
         rets.getRetencion().add(ret);
         imp.setRetenciones(rets);
         imp.setTotalImpuestosRetenidos(new BigDecimal(NumberString.FormatearDecimal((BGImpuestoRetenido1).doubleValue(), 2).replace(",", "")));
      }
      Complemento comp = objFactory.createComprobanteComplemento();
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Llenamos el comprobante con su detalle">
      objComp.setEmisor(emisor);
      objComp.setReceptor(receptor);
      objComp.setConceptos(conceps);
      objComp.setImpuestos(imp);
      objComp.setComplemento(comp);
      // </editor-fold>

      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Complementos fiscales dependiendo del rubro">
      ObtenerComplementos(objFactory, objComp);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Generamos la firma">
      this.strSelloCFD = GeneraFirma(objComp);
      objComp.setSello(this.strSelloCFD);
      if (this.strSelloCFD.startsWith("ERROR:")) {
         strRes = this.strSelloCFD;
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Pasos para el timbrado y Addenda">
      if (strRes.equals("OK")) {

         // <editor-fold defaultstate="collapsed" desc="Addenda">
         /**23/02/2013 ADDENDA AMECE*/
         //Generamos la addenda
         /*Nodo especial para la Addenda AMECE*/
         //Addenda addenda = objFactory.createComprobanteAddenda();
         //Llenamos la addenda
         //FillAddenda(addenda, objComp, objFactory, strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY, strHM_DIV, strHM_SOC, strHM_ON, strHM_REFERENCEDATE);
         //objComp.setAddenda(addenda);
         /**23/02/2013 ADDENDA AMECE*/
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Generamos el documento XML">
         try {
            JAXBContext jaxbContext;
            if (satAddenda == null) {
               if (this.bolComplementoFiscal && this.bolDonataria) {
                  jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
               } else {
                  jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class);
               }
            } else {
               if (this.bolComplementoFiscal && this.bolDonataria) {
                  jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
               } else {
                  jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class);
               }
            }
            Marshaller marshaller = jaxbContext.createMarshaller();
            /*Vinculamos URI'S que solicita hacienda*/
            //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema-instance");
            String strListXSD = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd";//Version 3.2
            if (this.bolComplementoFiscal && this.bolDonataria) {
               strListXSD += " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
            }
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, strListXSD);
            /*Formato de salida del XML*/
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //For change prefix
            NamespacePrefixMapper prefix = new MyNamespacePrefixMapper();
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);
           
            //Dependiendo de la version
            if (this.bolEsLocal) {
               //marshaller.setProperty("com.sun.xml.internal.bind.xmlDeclaration", Boolean.FALSE);
            } else {
               marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
               marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            }
            /*Guardamos comprobante*/
            StringBuilder strPathFile = new StringBuilder("");
            StringBuilder strNomFile = new StringBuilder("");
            if (!bolEsNc) {
               strNomFile.append("XmlSAT").append(this.intTransaccion).append(" .xml");
               strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
            } else {
               strNomFile.append("NC_XML").append(this.intTransaccion).append(".xml");
               strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
            }
            marshaller.marshal(objComp, new FileOutputStream(strPathFile.toString()));

            //Llamado al proceso deTimbrado
            String strRespTimb = GeneraTimbrado(strNomFile.toString());
            //Addenda 
            SATAddendaLala lala = new SATAddendaLala();
            //Validamos si se timbro correctamente
            if (strRespTimb.equals("OK")) {
               //Generamos el QR
               String strQResp = null;
               if (!bolEsNc) {
                  strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 1);
               } else {
                  strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 2);
               }
               if (strQResp.startsWith("OK")) {
                  strPathQR = strQResp.replace("OK", "");
               }
               lala.makeNameSpaceDeclaration(strPathFile.toString(), intTransaccion, oConn);
            } else {
               //Aunque no timbre generamos la addenda
               lala.makeNameSpaceDeclaration(strPathFile.toString(), intTransaccion, oConn);
               strRes = "ERROR:" + strRespTimb;
            }
         } catch (FileNotFoundException ex) {
            ex.fillInStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXMLFile", oConn);
            strRes = "ERROR:" + ex.getMessage();
         } catch (JAXBException ex) {
            ex.fillInStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXML", oConn);
            strRes = "ERROR:" + ex.getMessage();
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Almacenamos el sello y la cadena original en la transaccion">
         if (!bolEsNc) {
            String strUpdate = "UPDATE vta_facturas "
                    + "set FAC_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                    + ",FAC_SELLO='" + this.strSelloCFD + "'"
                    + ",FAC_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                    + ",FAC_PATH_CBB='" + this.strPathQR + "'"
                    + ",FAC_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                    + ",FAC_TIPOCOMP=" + intEMP_TIPOCOMP + " "
                    + " where FAC_ID = " + this.intTransaccion;
            oConn.runQueryLMD(strUpdate);
         } else {
            String strUpdate = "UPDATE vta_ncredito "
                    + "set NC_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                    + ",NC_SELLO='" + this.strSelloCFD + "'"
                    + ",NC_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                    + ",NC_PATH_CBB='" + this.strPathQR + "'"
                    + ",NC_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                    + ",NC_TIPOCOMP=" + intEMP_TIPOCOMP + " "
                    + " where NC_ID = " + this.intTransaccion;
            oConn.runQueryLMD(strUpdate);
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si todo fue OK generamos el PDF">
         if (strRes.equals("OK")) {
            if (bolSendMailMasivo) {
               //Buscamos los datos del folio
               strSql = "select FAC_FOLIO from vta_facturas where FAC_ID = " + this.intTransaccion;
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     this.strFolio = rs.getString("FAC_FOLIO");
                  }
                  //if(rs.getStatement() != null )rs.getStatement().close(); 
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
               }
               try {
                  String strRespForm = GeneraImpresionPDF(strPath, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                  if (strRespForm.equals("OK")) {
                     //Mandamos mail con la factura
                     if (!bolEsLocal) {
                        try {
                           GeneraMail(strMailCte, strMailCte2, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                        } catch (Exception ex) {
                           System.out.println(" " + ex.getLocalizedMessage());
                           System.out.println(" " + ex.getMessage());
                        }
                     }
                  } else {
                     strRes = strRespForm;
                  }
               } catch (Exception ex) {
                  System.out.println(" ex " + ex.getMessage());
                  System.out.println(" ex " + ex.getLocalizedMessage());
               }
            }
         }
         // </editor-fold>
      }
      // </editor-fold>
      return strRes;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Timbrado de la Factura">
   /**
    * Realiza el proceso de timbrado con llamadas a los webservices
    * de los diferentes proveedores que se implementen
    * @param strNomFile Nombre del Archivo por timbrar
    * @return Regresa OK en caso de que sea exitosa la operación o Error en su defecto
    */
   protected String GeneraTimbrado(String strNomFile) {
      String strRes = "OK";
      Timbrado_Pacs timbrado = null;
      if (this.enumPAC == SATXmlLala.PACS_CFDIS.FACTURA_EN_SEGUNDOS) {
         timbrado = new TimbradoFacturaSegundos(this.strPathConfigPAC);
      }
      if (this.enumPAC == SATXmlLala.PACS_CFDIS.TIMBRE_FISCAL) {
         timbrado = new TimbradoTimbreFiscal(this.strPathConfigPAC);
      }
      if (timbrado != null) {
         timbrado.setIntIdDoc(intTransaccion);
         if (!bolEsNc) {
            timbrado.setStrTablaDoc("vta_facturas");
            timbrado.setStrPrefijoDoc("FAC_");
         } else {
            timbrado.setStrTablaDoc("vta_ncredito");
            timbrado.setStrPrefijoDoc("NC_");
         }
         timbrado.setStrPathXml(this.strPath);
         timbrado.setoConn(oConn);
         timbrado.setLlavePrivadaEmisor(this.llavePrivadaEmisor);
         timbrado.setCertificadoEmisor(this.certificadoEmisor);
         timbrado.setStrPasswordEmisor(this.strPassKey);
         strRes = timbrado.timbra_Factura(strNomFile);
         this.strfolioFiscalUUID = timbrado.getStrfolioFiscalUUID();
         this.strFechaTimbre = timbrado.getStrFechaTimbre();
         this.strSelloSAT = timbrado.getStrSelloSAT();
         this.strNoCertSAT = timbrado.getStrNoCertSAT();
      } else {
         strRes = "ERROR: EL PAC no existe";
      }
      return strRes;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos adicionales">
   /**Llena la addenda*/
   private String GeneraFirma(Comprobante objComp) {
      String strValorEncrip = GeneraCadenaOriginal(objComp);
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

   /**Genera la cadena original*/
   private String GeneraCadenaOriginal(Comprobante objComp) {
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
//         if (satAddenda == null) {
//            if (this.bolComplementoFiscal && this.bolDonataria) {
//               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
//            } else {
               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class);
//            }
//         } else {
//            if (this.bolComplementoFiscal && this.bolDonataria) {
//               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class, classAddenda, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
//            } else {
//               jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_AMECE.ObjectFactory.class, classAddenda);
//            }
//         }
         Marshaller marshaller = jaxbContext.createMarshaller();
         /*Vinculamos URI'S que solicita hacienda*/
         //marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd");
         String strListXSD = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd";//Version 3.2
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
         Transformer transformer = tFactory.newTransformer(new StreamSource(this.strPath + System.getProperty("file.separator") + "cadenaoriginal_3_2.xslt"));
         transformer.transform(xmlDomSource, new StreamResult(strCadenaStr));
      } catch (TransformerException ex) {
         Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
      } catch (Exception ex) {
         Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
      }
      //System.out.println("***************" + strCadenaStr);
      return strCadenaStr.toString();
   }

   /**Genera la cadena original del Timbre fiscal Digital
   1. version
   2. UUID
   3. FechaTimbrado
   4. selloCFD
   5. noCertificadoSAT
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
   // </editor-fold>
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Envio de mail">
   /**
    * Envia el mail al cliente
    * @param strMailCte Es el mail del cliente
    * @param strMailCte2 Es el segundo mail del cliente
    * @param intEMP_TIPOCOMP Es el tipo de comprobante
    * @param intEmpId Es el id de la empresa
    * @param strFAC_NOMFORMATO Es el nombre del formato
    * @return Regresa OK si fue exitoso el envio del mail
    */
   @Override
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
      String strNumFolio = this.strFolio;

      //Buscamos los datos del SMTP
      String strSql = "select * from cuenta_contratada where ctam_id = 1";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strsmtp_server = rs.getString("smtp_server");
            strsmtp_user = rs.getString("smtp_user");
            strsmtp_pass = rs.getString("smtp_pass");
            strsmtp_port = rs.getString("smtp_port");
            strsmtp_usaTLS = rs.getString("smtp_usaTLS");
            strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
      }

      /**Si estan llenos todos los datos mandamos el mail*/
      if (!strsmtp_server.equals("")
              && !strsmtp_user.equals("")
              && !strsmtp_pass.equals("")) {
         //armamos el mail
         Mail mail = new Mail();
         mail.setBolDepuracion(false);
         //Activamos envio de acuse de recibo
         if (intEMP_ACUSEFACTURA == 1) {
            mail.setBolAcuseRecibo(true);
         }
         //Obtenemos los usuarios a los que mandaremos el mail
         String strLstMail = "";
         strSql = "select * from usuarios where BOL_MAIL_FACT = 1";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               if (mail.isEmail(rs.getString("EMAIL"))) {
                  strLstMail += rs.getString("EMAIL") + ",";
               }
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
            if (strLstMail.endsWith(",")) {
               strLstMail = strLstMail.substring(0, strLstMail.length() - 1);
            }
         } catch (SQLException ex) {
            Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
         }
         //Validamos si el mail del cliente es valido
         if (mail.isEmail(strMailCte)) {
            strLstMail += "," + strMailCte;
         }
         if (mail.isEmail(strMailCte2)) {
            strLstMail += "," + strMailCte2;
         }
         //Validamos lista de mails especiales
         if (!this.strListMailsEsp.isEmpty()) {
            strLstMail += "," + this.strListMailsEsp;
         }
         //Mandamos mail si hay usuarios
         if (!strLstMail.equals("")) {
            String strMsgMail = "Estimado Usuario<br> "
                    + "Le informamos que tiene una nueva Factura realizada "
                    + "con folio " + strNumFolio + ", por lo que le enviamos"
                    + " adjunto el XML y el documento electronico en PDF: "
                    + "<br>"
                    + "<b><u>Archivo XML</u></b><br>" + ""
                    + "<b><u>Archivo PDF</u></b><br>" + "";
            //Establecemos parametros
            mail.setUsuario(strsmtp_user);
            mail.setContrasenia(strsmtp_pass);
            mail.setHost(strsmtp_server);
            mail.setPuerto(strsmtp_port);
            if (!bolEsNc) {
               mail.setAsunto("Factura con folio " + strNumFolio + "...");
            } else {
               mail.setAsunto("Nota de credito con folio " + strNumFolio + "...");
               strMsgMail = "Estimado Usuario<br> "
                       + "Le informamos que tiene una nueva Nota de Credito realizada "
                       + "con folio " + strNumFolio + ", por lo que le enviamos"
                       + " adjunto el XML y el documento electronico en PDF: "
                       + "<br>"
                       + "<b><u>Archivo XML</u></b><br>" + ""
                       + "<b><u>Archivo PDF</u></b><br>" + "";
            }
            mail.setDestino(strLstMail);
            mail.setMensaje(strMsgMail);
            //Adjuntamos XML y PDF
            if (!bolEsNc) {
               mail.setFichero(strPath + "XmlSAT" + this.intTransaccion + " .xml");
               mail.setFichero(strPath + mapeo.getStrNomArchivo() + "_cfdi" + strNumFolio + ".pdf");
            } else {
               mail.setFichero(strPath + "NC_XML" + this.intTransaccion + ".xml");
               mail.setFichero(strPath + mapeo.getStrNomArchivo() + "_cfdi" + strNumFolio + ".pdf");
            }
            if (strsmtp_usaTLS.equals("1")) {
               mail.setBolUsaTls(true);
            }
            if (strsmtp_usaSTLS.equals("1")) {
               mail.setBolUsaStartTls(true);
            }
            boolean bol = mail.sendMail();
            if (!bol) {
               strResp = "Fallo el envio del Mail.";
            }
         }
      }
      return strResp;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Impresion y PDF">
   /**
    * Genera el formato de impresion en PDF
    * @param strPath Es el path
    * @param intEMP_TIPOCOMP Es el tipo de comprobante
    * @param intEmpId Es el id de la empresa
    * @param strFAC_NOMFORMATO Es el nombre del formato
    * @return Regresa OK si se genero el formato
    */
   @Override
   protected String GeneraImpresionPDF(String strPath, int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO) {
      String strResp = "OK";
      /*Validaciones dependiendo del tipo de comprobante*/
      ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
      try {
         Document document = new Document();
         PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strPath + mapeo.getStrNomArchivo() + "_cfdi" + this.strFolio + ".pdf"));
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
            //if(rs.getStatement() != null )rs.getStatement().close(); 
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
         strResp = "ERROR:" + ex.getMessage();
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
   // </editor-fold>

   /**Llena la addenda @deprecated*/
   private void FillAddenda(Addenda addenda, Comprobante objComp, ObjectFactory objFactory, String strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY, String strHM_DIV, String strHM_ON, String strHM_SOC, String strHM_REFERENCEDATE) {
      SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");

      //requestForPayment
      RequestForPayment request = objFactory.createComprobanteAddendaRequestForPayment();
      request.setType("SimpleInvoiceType");
      request.setContentVersion("1.3.1");
      request.setDocumentStatus("ORIGINAL");
      request.setDocumentStructureVersion("AMC7.1");
      request.setDeliveryDate(objComp.getFecha());

      //requestForPaymentIdentification
      RequestForPaymentIdentification identification = objFactory.createComprobanteAddendaRequestForPaymentRequestForPaymentIdentification();
      identification.setEntityType("INVOICE");
      identification.setUniqueCreatorIdentification(objComp.getSerie() + objComp.getFolio());
      request.setRequestForPaymentIdentification(identification);
      //specialInstruction
      SpecialInstruction specialInstruction = objFactory.createComprobanteAddendaRequestForPaymentSpecialInstruction();
      specialInstruction.setCode("SOC");
      specialInstruction.getContent().add(objFactory.createComprobanteAddendaRequestForPaymentSpecialInstructionText(strHM_SOC));
      request.getSpecialInstruction().add(specialInstruction);
      //orderIdentification
      OrderIdentification orderIdentification = objFactory.createComprobanteAddendaRequestForPaymentOrderIdentification();
      ReferenceIdentification ref = objFactory.createComprobanteAddendaRequestForPaymentOrderIdentificationReferenceIdentification();
      ref.setType("ON");
      ref.setValue(strHM_ON);
      orderIdentification.getReferenceIdentification().add(ref);
      //Validamos si viene la fecha de referencia
      if(!strHM_REFERENCEDATE.isEmpty()){
         try {
            Date dateReference = formatoDeFecha.parse(strHM_REFERENCEDATE);
            orderIdentification.setReferenceDate(dateReference);
         } catch (ParseException ex) {
            Logger.getLogger(SATXmlLala.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      request.setOrderIdentification(orderIdentification);
      //AdditionalInformation
      AdditionalInformation adition = objFactory.createComprobanteAddendaRequestForPaymentAdditionalInformation();
      AdditionalInformation.ReferenceIdentification referenceidentification = objFactory.createComprobanteAddendaRequestForPaymentAdditionalInformationReferenceIdentification();
      referenceidentification.setType("DIV");
      referenceidentification.setValue(strHM_DIV);
      adition.getReferenceIdentification().add(referenceidentification);
      request.setAdditionalInformation(adition);
      //DeliveryNote
      //buyer
      Buyer provedor = objFactory.createComprobanteAddendaRequestForPaymentBuyer();
      request.setBuyer(provedor);
      //seller
      Seller sel = objFactory.createComprobanteAddendaRequestForPaymentSeller();
      Seller.AlternatePartyIdentification alternatepartyidentification = objFactory.createComprobanteAddendaRequestForPaymentSellerAlternatePartyIdentification();
      sel.setGln("7507003100001");
      alternatepartyidentification.setType("SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
      alternatepartyidentification.setValue(strHM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY);
      sel.setAlternatePartyIdentification(alternatepartyidentification);
      request.setSeller(sel);
      //ShipTo
      ShipTo ship = objFactory.createComprobanteAddendaRequestForPaymentShipTo();
      ShipTo.NameAndAddress nameandadddress = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddress();
      ship.setGln("7507003123772");
      JAXBElement<String> street = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressStreetAddressOne(objComp.getReceptor().getDomicilio().getCalle());
      JAXBElement<String> city = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressCity(objComp.getReceptor().getDomicilio().getPais());
      JAXBElement<String> name = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressName(objComp.getReceptor().getNombre());
      if (name.getValue().length() > 35) {
         String steee = new String(name.getValue());
         steee = steee.substring(0, 34);
         name.setValue(steee);
      }
      JAXBElement<String> code = objFactory.createComprobanteAddendaRequestForPaymentShipToNameAndAddressPostalCode(objComp.getReceptor().getDomicilio().getCodigoPostal());
      nameandadddress.getNameAndStreetAddressOneAndCity().add(name);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(street);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(city);
      nameandadddress.getNameAndStreetAddressOneAndCity().add(code);
      ship.setNameAndAddress(nameandadddress);
      request.setShipTo(ship);
      //InvoiceCreator
      //Customs
      //currency
      RequestForPayment.Currency currency = objFactory.createComprobanteAddendaRequestForPaymentCurrency();
      currency.setCurrencyISOCode("MXN");
      currency.getCurrencyFunction().add("BILLING_CURRENCY");
      request.getCurrency().add(currency);
      //paymentTerms
      //shipmentDetail
      //allowanceCharge

      //lineItem
      Conceptos conceptos = objComp.getConceptos();
      Iterator<Concepto> it = conceptos.getConcepto().iterator();
      long lngConta = 0;
      double totLineTot = 0;
      while (it.hasNext()) {
         Concepto concepto = it.next();
         lngConta++;

         LineItem line = objFactory.createComprobanteAddendaRequestForPaymentLineItem();
         line.setType("SimpleInvoiceLineItemType");
         line.setNumber(BigInteger.valueOf(lngConta));
         LineItem.TradeItemIdentification tradeitemidentification = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemIdentification();
         line.setTradeItemIdentification(tradeitemidentification);
         LineItem.AlternateTradeItemIdentification alternatetradeitemidentification = objFactory.createComprobanteAddendaRequestForPaymentLineItemAlternateTradeItemIdentification();
         alternatetradeitemidentification.setType("BUYER_ASSIGNED");
         alternatetradeitemidentification.setValue(concepto.getNoIdentificacion());
         line.getAlternateTradeItemIdentification().add(alternatetradeitemidentification);
         LineItem.TradeItemDescriptionInformation tradeitemdescriptioninformation = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemDescriptionInformation();
         tradeitemdescriptioninformation.setLanguage("ES");
         tradeitemdescriptioninformation.setLongText(concepto.getDescripcion());
         line.setTradeItemDescriptionInformation(tradeitemdescriptioninformation);
         LineItem.InvoicedQuantity invoicedquantity = objFactory.createComprobanteAddendaRequestForPaymentLineItemInvoicedQuantity();
         invoicedquantity.setUnitOfMeasure("MIL");
         invoicedquantity.setValue(new BigDecimal(NumberString.FormatearDecimal(concepto.getCantidad().doubleValue(), 2).replace(",", "")));
         line.setInvoicedQuantity(invoicedquantity);
         LineItem.GrossPrice grossprice = objFactory.createComprobanteAddendaRequestForPaymentLineItemGrossPrice();
         grossprice.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getImporte().doubleValue(), 2).replace(",", "")));
         line.setGrossPrice(grossprice);
         LineItem.NetPrice netprice = objFactory.createComprobanteAddendaRequestForPaymentLineItemNetPrice();
         netprice.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getValorUnitario().doubleValue(), 2).replace(",", "")));
         line.setNetPrice(netprice);
         LineItem.TradeItemTaxInformation tradeitemtaxinformation = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemTaxInformation();
         tradeitemtaxinformation.setTaxTypeDescription("VAT");
         tradeitemtaxinformation.setReferenceNumber("V0");
         Comprobante.Addenda.RequestForPayment.LineItem.TradeItemTaxInformation.TradeItemTaxAmount tradeitem = objFactory.createComprobanteAddendaRequestForPaymentLineItemTradeItemTaxInformationTradeItemTaxAmount();
         tradeitem.setTaxPercentage(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue(), 2).replace(",", "")));
         double dblImporteIVA = (objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue() / 100) * concepto.getImporte().doubleValue();
         tradeitem.setTaxAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(dblImporteIVA).doubleValue(), 2).replace(",", "")));
         tradeitemtaxinformation.setTradeItemTaxAmount(tradeitem);
         line.getTradeItemTaxInformation().add(tradeitemtaxinformation);
         LineItem.TotalLineAmount totallineamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmount();
         double totLine = dblImporteIVA + concepto.getImporte().doubleValue();
         totLineTot += totLine;

         line.setTotalLineAmount(totallineamount);
         LineItem.TotalLineAmount.GrossAmount grossamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmountGrossAmount();
         grossamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totLine).doubleValue(), 2).replace(",", "")));
         totallineamount.setGrossAmount(grossamount);
         LineItem.TotalLineAmount.NetAmount netamount = objFactory.createComprobanteAddendaRequestForPaymentLineItemTotalLineAmountNetAmount();
         double totbrut = (concepto.getCantidad().doubleValue() * concepto.getValorUnitario().doubleValue());
         netamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totbrut).doubleValue(), 2).replace(",", "")));
         totallineamount.setNetAmount(netamount);
         line.setTotalLineAmount(totallineamount);
         request.getLineItem().add(line);
         //totalAmount
         TotalAmount totalamount = objFactory.createComprobanteAddendaRequestForPaymentTotalAmount();
         totalamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(BigDecimal.valueOf(totLine).doubleValue(), 2).replace(",", "")));
         request.setTotalAmount(totalamount);
         //BaseAmount
         BaseAmount baseamount = objFactory.createComprobanteAddendaRequestForPaymentBaseAmount();
         baseamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(objComp.getSubTotal().doubleValue(), 2).replace(",", "")));
         request.setBaseAmount(baseamount);
         //tax
         Tax tax = objFactory.createComprobanteAddendaRequestForPaymentTax();
         tax.setType("VAT");
         tax.setTaxPercentage(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa().doubleValue(), 2).replace(",", "")));
         tax.setTaxAmount(new BigDecimal(NumberString.FormatearDecimal(objComp.getImpuestos().getTraslados().getTraslado().get(0).getImporte().doubleValue(), 2).replace(",", "")));
         tax.setTaxCategory("TRANSFERIDO");
         request.getTax().add(tax);
         //payableAmount
         PayableAmount payableamount = objFactory.createComprobanteAddendaRequestForPaymentPayableAmount();
         payableamount.setAmount(new BigDecimal(NumberString.FormatearDecimal(concepto.getImporte().doubleValue(), 2).replace(",", "")));
         request.setPayableAmount(payableamount);




      }


      //Asignamos a la addenda
      addenda.setRequestForPayment(request);
   }
}
