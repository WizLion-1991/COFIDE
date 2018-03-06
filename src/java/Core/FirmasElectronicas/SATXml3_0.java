package Core.FirmasElectronicas;

import Core.FirmasElectronicas.SAT3_2.*;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Complemento;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Conceptos;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Conceptos.Concepto;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Emisor;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Emisor.RegimenFiscal;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Impuestos;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Impuestos.Retenciones;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Impuestos.Retenciones.Retencion;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Impuestos.Traslados;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Impuestos.Traslados.Traslado;
import Core.FirmasElectronicas.SAT3_2.Comprobante.Receptor;
import Core.FirmasElectronicas.Utils.UtilCert;
import Core.FirmasElectronicas.complementos.donatarias.Donatarias;
import Core.FirmasElectronicas.complementos.impuestoslocales.ImpuestosLocales;
import Core.FirmasElectronicas.complementos.impuestoslocales.ImpuestosLocales.TrasladosLocales;
import Core.FirmasElectronicas.complementos.nominas.Nomina;
import Core.FirmasElectronicas.complementos.nominas.Nomina.Deducciones;
import Core.FirmasElectronicas.complementos.nominas.Nomina.Deducciones.Deduccion;
import Core.FirmasElectronicas.complementos.nominas.Nomina.HorasExtra;
import Core.FirmasElectronicas.complementos.nominas.Nomina.Incapacidad;
import Core.FirmasElectronicas.complementos.nominas.Nomina.Percepciones;
import Core.FirmasElectronicas.complementos.nominas.Nomina.Percepciones.Percepcion;
import ERP.ERP_MapeoFormato;
import ERP.ProcesoMaster;
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
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import comSIWeb.Utilerias.Mail;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.logging.log4j.LogManager;

/**
 * Clase para generar XML del SAT versión 3.2
 *
 * @author zeus
 */
public class SATXml3_0 extends SATXml {

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
    protected String strPATHBase;
    protected boolean bolNominas = false;
    protected boolean bolImpuestoLocales = false;
    protected boolean bolRenombrarXml = false;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SATXml3_0.class.getName());
    private String strPatronNomXml;
    private String strPatronNomPDF;
    private String strNomFilePdf;
    private String strNomFileXml;
    private String strNomIniXml;
    private String strNombreReceptor;
    private String strFechaCFDI;
    private String strFechaNominaGenera;
    protected boolean blEmail1;
    protected boolean blEmail2;
    protected boolean blEmail3;
    protected boolean blEmail4;
    protected boolean blEmail5;
    protected boolean blEmail6;
    protected boolean blEmail7;
    protected boolean blEmail8;
    protected boolean blEmail9;
    protected boolean blEmail10;
    protected boolean bolEsNotaCargo = false;
    protected boolean bolEsNotaCargoProveedor = false;
    protected boolean bolUsoFormatoJasper = false;
    protected int intSucursal = 0;
    protected int intEmpId = 0;

    public boolean isBolEsNotaCargoProveedor() {
        return bolEsNotaCargoProveedor;
    }

    public void setBolEsNotaCargoProveedor(boolean bolEsNotaCargoProveedor) {
        this.bolEsNotaCargoProveedor = bolEsNotaCargoProveedor;
    }

    public String getStrPATHBase() {
        return strPATHBase;
    }

    public void setStrPATHBase(String strPATHBase) {
        this.strPATHBase = strPATHBase;
    }

    public boolean isBolUsoFormatoJasper() {
        return bolUsoFormatoJasper;
    }

    /**
     * Con true definimos que usaremos los formatos en jasper
     *
     * @param bolUsoFormatoJasper
     */
    public void setBolUsoFormatoJasper(boolean bolUsoFormatoJasper) {
        this.bolUsoFormatoJasper = bolUsoFormatoJasper;
    }

    public boolean isBolEsNotaCargo() {
        return bolEsNotaCargo;
    }

    public void setBolEsNotaCargo(boolean bolEsNotaCargo) {
        this.bolEsNotaCargo = bolEsNotaCargo;
    }

    public boolean isBlEmail1() {
        return blEmail1;
    }

    public void setBlEmail1(boolean blEmail1) {
        this.blEmail1 = blEmail1;
    }

    public boolean isBlEmail2() {
        return blEmail2;
    }

    public void setBlEmail2(boolean blEmail2) {
        this.blEmail2 = blEmail2;
    }

    public boolean isBlEmail3() {
        return blEmail3;
    }

    public void setBlEmail3(boolean blEmail3) {
        this.blEmail3 = blEmail3;
    }

    public boolean isBlEmail4() {
        return blEmail4;
    }

    public void setBlEmail4(boolean blEmail4) {
        this.blEmail4 = blEmail4;
    }

    public boolean isBlEmail5() {
        return blEmail5;
    }

    public void setBlEmail5(boolean blEmail5) {
        this.blEmail5 = blEmail5;
    }

    public boolean isBlEmail6() {
        return blEmail6;
    }

    public void setBlEmail6(boolean blEmail6) {
        this.blEmail6 = blEmail6;
    }

    public boolean isBlEmail7() {
        return blEmail7;
    }

    public void setBlEmail7(boolean blEmail7) {
        this.blEmail7 = blEmail7;
    }

    public boolean isBlEmail8() {
        return blEmail8;
    }

    public void setBlEmail8(boolean blEmail8) {
        this.blEmail8 = blEmail8;
    }

    public boolean isBlEmail9() {
        return blEmail9;
    }

    public void setBlEmail9(boolean blEmail9) {
        this.blEmail9 = blEmail9;
    }

    public boolean isBlEmail10() {
        return blEmail10;
    }

    public void setBlEmail10(boolean blEmail10) {
        this.blEmail10 = blEmail10;
    }

    public String getStrPatronNomXml() {
        return strPatronNomXml;
    }

    /**
     * Define el patron del archivo XLS que se enviara por correo
     *
     * @param strPatronNomXml
     */
    public void setStrPatronNomXml(String strPatronNomXml) {
        this.strPatronNomXml = strPatronNomXml;
    }

    public String getStrPatronNomPDF() {
        return strPatronNomPDF;
    }

    /**
     * Define el patron del archivo PDF que se enviara por correo
     *
     * @param strPatronNomPDF
     */
    public void setStrPatronNomPDF(String strPatronNomPDF) {
        this.strPatronNomPDF = strPatronNomPDF;
    }

    public String getStrNomIniXml() {
        return strNomIniXml;
    }

    /**
     * Define el nombre inicial del Xml
     *
     * @param strNomIniXml
     */
    public void setStrNomIniXml(String strNomIniXml) {
        this.strNomIniXml = strNomIniXml;
    }

    /**
     * Regresa true si se generara el nodo de impuestos locales
     *
     * @return True/false
     */
    public boolean isBolImpuestoLocales() {
        return bolImpuestoLocales;
    }

    /**
     * Define si se generara el nodo de impuestos locales
     *
     * @param bolImpuestoLocales True/false
     */
    public void setBolImpuestoLocales(boolean bolImpuestoLocales) {
        this.bolImpuestoLocales = bolImpuestoLocales;
    }

    /**
     * Regresa true si tienen complemento de nominas
     *
     * @return boolean
     */
    public boolean isBolNominas() {
        return bolNominas;
    }

    /**
     * Define si se estan generando recibos de nomina
     *
     * @param bolNominas Es la bandera de recibo de nomina
     */
    public void setBolNominas(boolean bolNominas) {
        this.bolNominas = bolNominas;
    }

    /**
     * Lista de PACS
     */
    public static enum PACS_CFDIS {

        FACTURA_EN_SEGUNDOS,
        TIMBRE_FISCAL
    }

    /**
     * Regresa el PAC a usar para Timbrar la factura
     *
     * @return Regresa un el valor de la constante del PAC
     */
    public PACS_CFDIS getEnumPAC() {
        return enumPAC;
    }

    /**
     * Define el PAC a usar para Timbrar la factura
     *
     * @param enumPAC Es la constante del PAC a usar
     */
    public void setEnumPAC(PACS_CFDIS enumPAC) {
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

    /**
     * Regresa el nombre del archivo Pdf por generar
     *
     * @return
     */
    public String getStrNomFilePdf() {
        return strNomFilePdf;
    }

    public void setStrNomFilePdf(String strNomFilePdf) {
        this.strNomFilePdf = strNomFilePdf;
    }

    /**
     * Regresa el nombre del archivo xml por generar
     *
     * @return
     */
    public String getStrNomFileXml() {
        return strNomFileXml;
    }

    public void setStrNomFileXml(String strNomFileXml) {
        this.strNomFileXml = strNomFileXml;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    /**
     * Constructor para objeto que genera facturas con cfdi's
     *
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
    public SATXml3_0(int intTransaccion, String strPath, String NoAprobacion, String FechaAprobacion, String NoSerieCert, String strPathKey, String strPassKey, VariableSession varSesiones, Conexion oConn) {
        super(intTransaccion, strPath, NoAprobacion, FechaAprobacion, NoSerieCert, strPathKey, strPassKey, varSesiones, oConn);
        this.enumPAC = SATXml3_0.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
        this.strPathConfigPAC = "";
        bolUsaTrxComoFolio = false;
//      strPatronNomPDF = "%nom_archivo%_cfdi%strNumFolio%.pdf";
//      this.strPatronNomXml = "XmlSAT%Transaccion% .xml";
        this.blEmail1 = true;
        this.blEmail2 = true;
        this.blEmail3 = true;
        this.blEmail4 = true;
        this.blEmail5 = true;
        this.blEmail6 = true;
        this.blEmail7 = true;
        this.blEmail8 = true;
        this.blEmail9 = true;
        this.blEmail10 = true;
    }

    /**
     * Constructor vacio
     */
    public SATXml3_0() {
        super();
        this.enumPAC = SATXml3_0.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
        this.strPathConfigPAC = "";
        bolUsaTrxComoFolio = false;
        this.blEmail1 = true;
        this.blEmail2 = true;
        this.blEmail3 = true;
        this.blEmail4 = true;
        this.blEmail5 = true;
        this.blEmail6 = true;
        this.blEmail7 = true;
        this.blEmail8 = true;
        this.blEmail9 = true;
        this.blEmail10 = true;

//      strPatronNomPDF = "%nom_archivo%_cfdi%strNumFolio%.pdf";
//      this.strPatronNomXml = "XmlSAT%Transaccion% .xml";
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos Factura CFDI">
    // <editor-fold defaultstate="collapsed" desc="Generacion de XML">
    /**
     * Genera el XML para el documento electronico
     *
     * @return Regresa OK en caso de que todo halla sido satisfactorio
     */
    @Override
    public String GeneraXml() {

        // <editor-fold defaultstate="collapsed" desc="Inicializamos los valores del comprobante">
        log.debug("Inicializamos valores....");
        strFolio = "";
        String strRes = "OK";
        BigDecimal BGSubtotal = null;
        BigDecimal BGDescuento = null;
        BigDecimal BGTotal = null;
        BigDecimal BGImpuesto1 = null;
        BigDecimal BGImpuestoRetenido1 = null;
        BigDecimal BGImpuestoRetenido2 = null;
        BigDecimal BGTasa1 = null;
        String strTipoComprobante = "ingreso";
        //Si es nota de credito no es ingresos
        if (bolEsNc) {
            strTipoComprobante = "egreso";
        }
        if (this.bolNominas) {
            strTipoComprobante = "egreso";
            this.bolComplementoFiscal = true;
        }
        if (this.bolEsNotaCargoProveedor) {
            strTipoComprobante = "egreso";
        }
        intSucursal = 0;
        intEmpId = 0;
        int intCliente = 0;
        int intClienteFinal = 0;

        /*Datos del receptor*/
        strNombreReceptor = "";
        String strRFCReceptor = "";
        String strCalleReceptor = "";
        String strColoniaReceptor = "";
        String strLocalidadReceptor = "";
        String strMunicipioReceptor = "";
        String strCPReceptor = "";
        String strEdoReceptor = "";
        String strNumero = "";
        String strNumeroInt = "";
        strFechaCFDI = "";
        strFechaNominaGenera = "";
        /*Consultamos los datos del emisor*/
        String strNombreEmisor = "";
        String strRFC = "";
        String strCalle = "";
        String strNumeroEmisor = "";
        String strNumeroIntEmisor = "";
        String strCP = "";
        String strColonia = "";
        String strEdo = "";
        String strLocalidad = "";
        String strMunicipio = "";
        String strPais = "";
        String strFechaFactura = "";
        String strHoraFactura = "";
        int intUsoIEPS = 0;
        BigDecimal BGTasaIEPS = null;
        BigDecimal BGImporteIEPS = null;
        String strMailCte = "";
        String strMailCte2 = "";
        String strMailCte3 = "";
        String strMailCte4 = "";
        String strMailCte5 = "";
        String strMailCte6 = "";
        String strMailCte7 = "";
        String strMailCte8 = "";
        String strMailCte9 = "";
        String strMailCte10 = "";
        int intFolio = 0;
        int intClienteEnviaMail = 1;
        String strFAC_NOMFORMATO = "";
        ResultSet rs;
        String strSql;
        String strFAC_CONDPAGO = "";
        String strFAC_FORMADEPAGO = "";
        String strFAC_METODODEPAGO = "";
        String strFAC_NUMCTA = "";
        String strRegimenFiscal = "";
        String strCalleSuc = "";
        String strColoniaSuc = "";
        String strLocalidadSuc = "";
        String strMunicipioSuc = "";
        String strCPSuc = "";
        String strEdoSuc = "";
        String strPaisSuc = "";
        String strExpedidoEn = "";
        ArrayList<String> lstRegimenFiscal = new ArrayList<String>();
        String strMoneda = "";
        String strConceptoNomina = "";
        ArrayList<TrasladosIVA> lstTrasladosIVA = new ArrayList<TrasladosIVA>();
        // </editor-fold >

        // <editor-fold defaultstate="collapsed" desc="Consultamos datos de la base de datos">
        log.debug("Consultamos datos de la base de datos....");
        //Validamos si es nota de credito
        if (!bolEsNc) {
            //Validamos si es recibo de nominas
            if (this.bolNominas) {
                // <editor-fold defaultstate="collapsed" desc="Consultamos datos de la nomina">
                System.out.println("#####################################query hacia rhh_nominas#####################################");
                strSql = "SELECT NOM_FOLIO,NOM_FECHA,NOM_PERCEPCIONES,NOM_DEDUCCIONES,NOM_PERCEPCION_TOTAL,"
                        + " NOM_CONDPAGO,NOM_FORMADEPAGO,NOM_METODODEPAGO,"
                        + " SC_ID,EMP_ID,EMP_NUM,NOM_HORA,"
                        + " NOM_NOMFORMATO,NOM_ISR_RETENIDO, "
                        + " NOM_NUMCUENTA,NOM_MONEDA,NOM_CONCEPTO,NOM_REGIMENFISCAL "
                        + " FROM rhh_nominas WHERE NOM_ID = " + this.intTransaccion;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strFolio = rs.getString("NOM_FOLIO");
                        //Tomamos la fecha actual.
                        strFechaFactura = fecha.getFechaActual();//Rs.getString("NOM_FECHA");
                        strFechaNominaGenera = rs.getString("NOM_FECHA");//Rs.getString("NOM_FECHA");
                        strHoraFactura = fecha.getHoraActualHHMMSS();//Rs.getString("NOM_HORA");
                        BGSubtotal = rs.getBigDecimal("NOM_PERCEPCIONES");
                        BGDescuento = rs.getBigDecimal("NOM_DEDUCCIONES");
                        BGTotal = rs.getBigDecimal("NOM_PERCEPCION_TOTAL");
                        intSucursal = rs.getInt("SC_ID");
                        BGImpuestoRetenido1 = rs.getBigDecimal("NOM_ISR_RETENIDO");
                        intCliente = rs.getInt("EMP_NUM");
                        intEmpId = rs.getInt("EMP_ID");
                        strFAC_NOMFORMATO = rs.getString("NOM_NOMFORMATO");

                        strFAC_FORMADEPAGO = rs.getString("NOM_FORMADEPAGO");
                        strFAC_CONDPAGO = rs.getString("NOM_CONDPAGO");
                        strFAC_METODODEPAGO = rs.getString("NOM_METODODEPAGO");
                        strFAC_NUMCTA = rs.getString("NOM_NUMCUENTA");
                        strConceptoNomina = rs.getString("NOM_CONCEPTO");
                        strMoneda = rs.getInt("NOM_MONEDA") + "";
                        strRegimenFiscal = rs.getString("NOM_REGIMENFISCAL");
                        if (!strFolio.isEmpty()) {
                            try {
                                intFolio = Integer.valueOf(strFolio);
                            } catch (NumberFormatException ex) {
                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
                                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                                strRes = "ERROR:" + ex.getMessage();
                            }
                        }
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
                    this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
                    strRes = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage();
                }
                // </editor-fold>
            } else if (this.bolEsNotaCargo) {
                // <editor-fold defaultstate="collapsed" desc="Consultamos datos la nota de cargo">
                System.out.println("#####################################query hacia vta_notas_cargos#####################################");
                strSql = "SELECT NCA_FOLIO_C,NCA_FECHA,NCA_IMPORTE,NCA_DESCUENTO,DFA_ID,"
                        + " NCA_CONDPAGO,NCA_FORMADEPAGO,NCA_METODODEPAGO,"
                        + " NCA_TOTAL,SC_ID,EMP_ID,NCA_IMPUESTO1,NCA_RETIVA,NCA_RETISR,NCA_TASA1,NCA_TASA2,CT_ID,NCA_HORA,"
                        + " NCA_SERIE,NCA_NOAPROB,NCA_FECHAAPROB,NCA_NOMFORMATO, "
                        + " NCA_USO_IEPS,NCA_TASA_IEPS,NCA_IMPORTE_IEPS,NCA_NUMCUENTA,NCA_MONEDA,NCA_REGIMENFISCAL "
                        + " FROM vta_notas_cargos WHERE NCA_ID = " + this.intTransaccion;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strFolio = rs.getString("NCA_FOLIO_C");
                        if (bolUsaTrxComoFolio) {
                            strFolio = intTransaccion + "";
                        }
                        strFechaFactura = rs.getString("NCA_FECHA");
                        BGSubtotal = rs.getBigDecimal("NCA_IMPORTE");
                        BGDescuento = rs.getBigDecimal("NCA_DESCUENTO");
                        BGTotal = rs.getBigDecimal("NCA_TOTAL");
                        intSucursal = rs.getInt("SC_ID");
                        BGImpuesto1 = rs.getBigDecimal("NCA_IMPUESTO1");
                        BGImpuestoRetenido1 = rs.getBigDecimal("NCA_RETIVA");
                        BGImpuestoRetenido2 = rs.getBigDecimal("NCA_RETISR");
                        BGTasa1 = rs.getBigDecimal("NCA_TASA1");
                        intCliente = rs.getInt("CT_ID");
                        intClienteFinal = rs.getInt("DFA_ID");
                        intEmpId = rs.getInt("EMP_ID");
                        strFAC_NOMFORMATO = rs.getString("NCA_NOMFORMATO");
                        strHoraFactura = rs.getString("NCA_HORA");
                        intUsoIEPS = rs.getInt("NCA_USO_IEPS");
                        BGTasaIEPS = rs.getBigDecimal("NCA_TASA_IEPS");
                        BGImporteIEPS = rs.getBigDecimal("NCA_IMPORTE_IEPS");
                        strFAC_FORMADEPAGO = rs.getString("NCA_FORMADEPAGO");
                        strFAC_CONDPAGO = rs.getString("NCA_CONDPAGO");
                        strFAC_METODODEPAGO = rs.getString("NCA_METODODEPAGO");
                        strFAC_NUMCTA = rs.getString("NCA_NUMCUENTA");
                        strMoneda = rs.getString("NCA_MONEDA");
                        strRegimenFiscal = rs.getString("NCA_REGIMENFISCAL");
                        if (!strFolio.isEmpty()) {
                            try {
                                intFolio = Integer.valueOf(strFolio);
                            } catch (NumberFormatException ex) {
                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
                                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                                strRes = "ERROR:" + ex.getMessage();
                            }
                        }
                        //Validamos si hay impuesto local(ES EL IMPUESTO 2)
                        if (rs.getDouble("NCA_TASA2") > 0) {
                            this.bolComplementoFiscal = true;
                            this.bolImpuestoLocales = true;
                        }
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                    this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
                    strRes = "ERROR:" + ex.getMessage();
                }
                // </editor-fold>
            } else if (this.bolEsNotaCargoProveedor) {
                // <editor-fold defaultstate="collapsed" desc="Consultamos datos la nota de cargo de proveedor">
                System.out.println("#####################################query hacia vta_notas_cargosprov#####################################");
                strSql = "SELECT NCA_FOLIO_C,NCA_FECHA,NCA_IMPORTE,NCA_DESCUENTO,DFA_ID,"
                        + " NCA_CONDPAGO,NCA_FORMADEPAGO,NCA_METODODEPAGO,"
                        + " NCA_TOTAL,SC_ID,EMP_ID,NCA_IMPUESTO1,NCA_RETIVA,NCA_RETISR,NCA_TASA1,NCA_TASA2,PV_ID,NCA_HORA,"
                        + " NCA_SERIE,NCA_NOAPROB,NCA_FECHAAPROB,NCA_NOMFORMATO, "
                        + " NCA_USO_IEPS,NCA_TASA_IEPS,NCA_IMPORTE_IEPS,NCA_NUMCUENTA,NCA_MONEDA,NCA_REGIMENFISCAL "
                        + " FROM vta_notas_cargosprov WHERE NCA_ID = " + this.intTransaccion;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strFolio = rs.getString("NCA_FOLIO_C");
                        if (bolUsaTrxComoFolio) {
                            strFolio = intTransaccion + "";
                        }
                        strFechaFactura = rs.getString("NCA_FECHA");
                        BGSubtotal = rs.getBigDecimal("NCA_IMPORTE");
                        BGDescuento = rs.getBigDecimal("NCA_DESCUENTO");
                        BGTotal = rs.getBigDecimal("NCA_TOTAL");
                        intSucursal = rs.getInt("SC_ID");
                        BGImpuesto1 = rs.getBigDecimal("NCA_IMPUESTO1");
                        BGImpuestoRetenido1 = rs.getBigDecimal("NCA_RETIVA");
                        BGImpuestoRetenido2 = rs.getBigDecimal("NCA_RETISR");
                        BGTasa1 = rs.getBigDecimal("NCA_TASA1");
                        intCliente = rs.getInt("PV_ID");
                        intClienteFinal = rs.getInt("DFA_ID");
                        intEmpId = rs.getInt("EMP_ID");
                        strFAC_NOMFORMATO = rs.getString("NCA_NOMFORMATO");
                        strHoraFactura = rs.getString("NCA_HORA");
                        intUsoIEPS = rs.getInt("NCA_USO_IEPS");
                        BGTasaIEPS = rs.getBigDecimal("NCA_TASA_IEPS");
                        BGImporteIEPS = rs.getBigDecimal("NCA_IMPORTE_IEPS");
                        strFAC_FORMADEPAGO = rs.getString("NCA_FORMADEPAGO");
                        strFAC_CONDPAGO = rs.getString("NCA_CONDPAGO");
                        strFAC_METODODEPAGO = rs.getString("NCA_METODODEPAGO");
                        strFAC_NUMCTA = rs.getString("NCA_NUMCUENTA");
                        strMoneda = rs.getString("NCA_MONEDA");
                        strRegimenFiscal = rs.getString("NCA_REGIMENFISCAL");
                        if (!strFolio.isEmpty()) {
                            try {
                                intFolio = Integer.valueOf(strFolio);
                            } catch (NumberFormatException ex) {
                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
                                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                                strRes = "ERROR:" + ex.getMessage();
                            }
                        }
                        //Validamos si hay impuesto local(ES EL IMPUESTO 2)
                        if (rs.getDouble("NCA_TASA2") > 0) {
                            this.bolComplementoFiscal = true;
                            this.bolImpuestoLocales = true;
                        }
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                    this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
                    strRes = "ERROR:" + ex.getMessage();
                }
                // </editor-fold>
            } else {
                // <editor-fold defaultstate="collapsed" desc="Consultamos datos la factura">
                System.out.println("##################################### query hacia vta_facturas #####################################");
                strSql = "SELECT FAC_FOLIO_C,FAC_FECHA,FAC_IMPORTE,FAC_DESCUENTO,DFA_ID,"
                        + " FAC_CONDPAGO,FAC_FORMADEPAGO,FAC_METODODEPAGO,"
                        + " FAC_TOTAL,SC_ID,EMP_ID,FAC_IMPUESTO1,FAC_RETIVA,FAC_RETISR,FAC_TASA1,FAC_TASA2,CT_ID,FAC_HORA,"
                        + " FAC_SERIE,FAC_NOAPROB,FAC_FECHAAPROB,FAC_NOMFORMATO, "
                        + " FAC_USO_IEPS,FAC_TASA_IEPS,FAC_IMPORTE_IEPS,FAC_NUMCUENTA,FAC_MONEDA,FAC_REGIMENFISCAL "
                        + " FROM vta_facturas WHERE FAC_ID = " + this.intTransaccion;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strFolio = rs.getString("FAC_FOLIO_C");
                        if (bolUsaTrxComoFolio) {
                            strFolio = intTransaccion + "";
                        }
                        strFechaFactura = rs.getString("FAC_FECHA");
                        BGSubtotal = rs.getBigDecimal("FAC_IMPORTE");
                        BGDescuento = rs.getBigDecimal("FAC_DESCUENTO");
                        BGTotal = rs.getBigDecimal("FAC_TOTAL");
                        intSucursal = rs.getInt("SC_ID");
                        BGImpuesto1 = rs.getBigDecimal("FAC_IMPUESTO1");
                        BGImpuestoRetenido1 = rs.getBigDecimal("FAC_RETIVA");
                        BGImpuestoRetenido2 = rs.getBigDecimal("FAC_RETISR");
                        BGTasa1 = rs.getBigDecimal("FAC_TASA1");
                        intCliente = rs.getInt("CT_ID");
                        intClienteFinal = rs.getInt("DFA_ID");
                        intEmpId = rs.getInt("EMP_ID");
                        strFAC_NOMFORMATO = rs.getString("FAC_NOMFORMATO");
                        strHoraFactura = rs.getString("FAC_HORA");
                        intUsoIEPS = rs.getInt("FAC_USO_IEPS");
                        BGTasaIEPS = rs.getBigDecimal("FAC_TASA_IEPS");
                        BGImporteIEPS = rs.getBigDecimal("FAC_IMPORTE_IEPS");
                        strFAC_FORMADEPAGO = rs.getString("FAC_FORMADEPAGO");
                        strFAC_CONDPAGO = rs.getString("FAC_CONDPAGO");
                        strFAC_METODODEPAGO = rs.getString("FAC_METODODEPAGO");
                        strFAC_NUMCTA = rs.getString("FAC_NUMCUENTA");
                        strMoneda = rs.getString("FAC_MONEDA");
                        strRegimenFiscal = rs.getString("FAC_REGIMENFISCAL");
                        if (!strFolio.isEmpty()) {
                            try {
                                intFolio = Integer.valueOf(strFolio);
                            } catch (NumberFormatException ex) {
                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
                                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                                strRes = "ERROR:" + ex.getMessage();
                            }
                        }
                        //Validamos si hay impuesto local(ES EL IMPUESTO 2)
                        if (rs.getDouble("FAC_TASA2") > 0) {
                            this.bolComplementoFiscal = true;
                            this.bolImpuestoLocales = true;
                        }
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                    this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
                    strRes = "ERROR:" + ex.getMessage();
                }
                // </editor-fold>   
            }
        } else {
            // <editor-fold defaultstate="collapsed" desc="Consultamos datos de la nota de credito">
            System.out.println("##################################### query hacia vta_ncredito #####################################");
            strSql = "SELECT NC_FOLIO_C,NC_FECHA,NC_IMPORTE,NC_DESCUENTO,"
                    + " NC_CONDPAGO,NC_FORMADEPAGO,NC_METODODEPAGO,"
                    + " NC_TOTAL,SC_ID,EMP_ID,NC_IMPUESTO1,NC_RETIVA,NC_RETISR,NC_TASA1,CT_ID,NC_HORA,"
                    + " NC_SERIE,NC_NOAPROB,NC_FECHAAPROB,NC_NOMFORMATO, "
                    + " NC_USO_IEPS,NC_TASA_IEPS,NC_IMPORTE_IEPS,NC_MONEDA,NC_NUMCUENTA,NC_REGIMENFISCAL "
                    + " FROM vta_ncredito WHERE NC_ID = " + this.intTransaccion;
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strFolio = rs.getString("NC_FOLIO_C");
                    if (bolUsaTrxComoFolio) {
                        strFolio = intTransaccion + "";
                    }
                    strFechaFactura = rs.getString("NC_FECHA");
                    BGSubtotal = rs.getBigDecimal("NC_IMPORTE");
                    BGDescuento = rs.getBigDecimal("NC_DESCUENTO");
                    BGTotal = rs.getBigDecimal("NC_TOTAL");
                    intSucursal = rs.getInt("SC_ID");
                    BGImpuesto1 = rs.getBigDecimal("NC_IMPUESTO1");
                    BGImpuestoRetenido1 = rs.getBigDecimal("NC_RETIVA");
                    BGImpuestoRetenido2 = rs.getBigDecimal("NC_RETISR");
                    BGTasa1 = rs.getBigDecimal("NC_TASA1");
                    intCliente = rs.getInt("CT_ID");
                    intEmpId = rs.getInt("EMP_ID");
                    strFAC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
                    strHoraFactura = rs.getString("NC_HORA");
                    intUsoIEPS = rs.getInt("NC_USO_IEPS");
                    BGTasaIEPS = rs.getBigDecimal("NC_TASA_IEPS");
                    BGImporteIEPS = rs.getBigDecimal("NC_IMPORTE_IEPS");
                    strFAC_FORMADEPAGO = rs.getString("NC_FORMADEPAGO");
                    strFAC_CONDPAGO = rs.getString("NC_CONDPAGO");
                    strFAC_METODODEPAGO = rs.getString("NC_METODODEPAGO");
                    if (strFAC_METODODEPAGO.isEmpty()) {
                        strFAC_METODODEPAGO = "NO IDENTIFICADO";
                    }
                    strFAC_NUMCTA = rs.getString("NC_NUMCUENTA");
                    strMoneda = rs.getString("NC_MONEDA");
                    strRegimenFiscal = rs.getString("NC_REGIMENFISCAL");
                    //Validamos que tenga folio
                    if (!strFolio.isEmpty()) {
                        try {
                            intFolio = Integer.valueOf(strFolio);
                        } catch (NumberFormatException ex) {
                            log.error(ex.getMessage());
                            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getDoc", oConn);
                            strRes = "ERROR:" + ex.getMessage();
                        }
                    }
                }
                if (rs.getStatement() != null) {
                    //rs.getStatement().close();
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
                strRes = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
        }
        strFechaCFDI = strFechaFactura;
        try {
            // <editor-fold defaultstate="collapsed" desc="Sacamos los datos de la empresa">
            System.out.println("##################################### query hacia vta_empresas #####################################");
            strSql = "SELECT EMP_RAZONSOCIAL,EMP_RFC,"
                    + "EMP_CALLE,EMP_COLONIA,EMP_LOCALIDAD,EMP_MUNICIPIO,EMP_CP,"
                    + "EMP_ESTADO,EMP_TIPOCOMP,EMP_TIPOCOMPNC,EMP_ACUSEFACTURA,EMP_ES_DONATARIA "
                    + ",EMP_DONA_NUM_AUTORIZA,EMP_DONA_FECHA_AUTORIZA,EMP_DONA_LEYENDA"
                    + ",EMP_NUMERO,EMP_NUMINT "
                    + "FROM vta_empresas WHERE EMP_ID = " + intEmpId;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strNombreEmisor = FormateaTextoXml(rs.getString("EMP_RAZONSOCIAL"), false);
                strNumeroEmisor = FormateaTextoXml(rs.getString("EMP_NUMERO"), false);
                strNumeroIntEmisor = FormateaTextoXml(rs.getString("EMP_NUMINT"), false);
                strRFC = FormateaTextoXml(rs.getString("EMP_RFC").replace("-", ""), false);
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
                        if (this.bolEsNotaCargo) {
                            intEMP_TIPOCOMP = 8;
                        } else {
                            intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
                        }
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
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Sacamos los datos de la sucursal">
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
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Lugar de expedicion">
            if (this.bolUsoLugarExpEmp) {
                strExpedidoEn = strCalle + " " + strColonia + " " + strMunicipio + " " + strCP;
            } else {
                strExpedidoEn = strCalleSuc + " " + strColoniaSuc + " " + strMunicipioSuc + " " + strCPSuc;
            }
            // </editor-fold>

            //Validamos si sacamos datos de nominas o de clientes
            if (!this.bolNominas) {

                if (!this.bolEsNotaCargoProveedor) {
                    // <editor-fold defaultstate="collapsed" desc="Sacamos los datos del cliente">
                    System.out.println("##################################### query hacia vta_cliente #####################################");
                    strSql = "SELECT CT_RAZONSOCIAL,CT_RFC,"
                            + "CT_CALLE,CT_COLONIA,CT_LOCALIDAD,CT_MUNICIPIO,CT_CP,"
                            + "CT_ESTADO,CT_NUMERO,CT_NUMINT,CT_EMAIL1,CT_EMAIL2,CT_ENVIO_FACTURA"
                            + ",CT_EMAIL3,CT_EMAIL4,CT_EMAIL5"
                            + ",CT_EMAIL6,CT_EMAIL7,CT_EMAIL8,CT_EMAIL9,CT_EMAIL10  "
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
                        strMailCte3 = rs.getString("CT_EMAIL3");
                        strMailCte4 = rs.getString("CT_EMAIL4");
                        strMailCte5 = rs.getString("CT_EMAIL5");
                        strMailCte6 = rs.getString("CT_EMAIL6");
                        strMailCte7 = rs.getString("CT_EMAIL7");
                        strMailCte8 = rs.getString("CT_EMAIL8");
                        strMailCte9 = rs.getString("CT_EMAIL9");
                        strMailCte10 = rs.getString("CT_EMAIL10");
                        intClienteEnviaMail = rs.getInt("CT_ENVIO_FACTURA");
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                    //si es cliente final sacamos los datos de el
                    if (intClienteFinal != 0) {
                        strSql = "SELECT DFA_RAZONSOCIAL,DFA_RFC,DFA_CALLE,DFA_COLONIA,DFA_LOCALIDAD,"
                                + " DFA_MUNICIPIO,DFA_ESTADO,DFA_NUMERO,DFA_NUMINT,"
                                + " DFA_CP,DFA_EMAIL FROM vta_cliente_facturacion "
                                + " where DFA_ID=" + intClienteFinal;
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            strNombreReceptor = FormateaTextoXml(rs.getString("DFA_RAZONSOCIAL"), false);
                            strRFCReceptor = FormateaTextoXml(rs.getString("DFA_RFC").replace("-", ""), false);
                            strCalleReceptor = FormateaTextoXml(rs.getString("DFA_CALLE"));
                            strColoniaReceptor = FormateaTextoXml(rs.getString("DFA_COLONIA"));
                            strLocalidadReceptor = FormateaTextoXml(rs.getString("DFA_LOCALIDAD"));
                            strMunicipioReceptor = FormateaTextoXml(rs.getString("DFA_MUNICIPIO"));
                            strCPReceptor = FormateaTextoXml(rs.getString("DFA_CP"));
                            strEdoReceptor = FormateaTextoXml(rs.getString("DFA_ESTADO"));
                            strNumero = FormateaTextoXml(rs.getString("DFA_NUMERO"));
                            strNumeroInt = FormateaTextoXml(rs.getString("DFA_NUMINT"));
                            strMailCte = rs.getString("DFA_EMAIL");
                        }
                        if (rs.getStatement() != null) {
                            //rs.getStatement().close();
                        }
                        rs.close();
                    }
                    // </editor-fold>
                } else {
                    // <editor-fold defaultstate="collapsed" desc="Sacamos los datos del proveedor">
                    strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,"
                            + "PV_CALLE,PV_COLONIA,PV_LOCALIDAD,PV_MUNICIPIO,PV_CP,"
                            + "PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_EMAIL1,PV_EMAIL2  "
                            + "FROM vta_proveedor WHERE PV_ID = " + intCliente;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strNombreReceptor = FormateaTextoXml(rs.getString("PV_RAZONSOCIAL"), false);
                        strRFCReceptor = FormateaTextoXml(rs.getString("PV_RFC").replace("-", ""), false);
                        strCalleReceptor = FormateaTextoXml(rs.getString("PV_CALLE"));
                        strColoniaReceptor = FormateaTextoXml(rs.getString("PV_COLONIA"));
                        strLocalidadReceptor = FormateaTextoXml(rs.getString("PV_LOCALIDAD"));
                        strMunicipioReceptor = FormateaTextoXml(rs.getString("PV_MUNICIPIO"));
                        strCPReceptor = FormateaTextoXml(rs.getString("PV_CP"));
                        strEdoReceptor = FormateaTextoXml(rs.getString("PV_ESTADO"));
                        strNumero = FormateaTextoXml(rs.getString("PV_NUMERO"));
                        strNumeroInt = FormateaTextoXml(rs.getString("PV_NUMINT"));
                        strMailCte = rs.getString("PV_EMAIL1");
                        strMailCte2 = rs.getString("PV_EMAIL2");
                        strMailCte3 = "";
                        strMailCte4 = "";
                        strMailCte5 = "";
                        strMailCte6 = "";
                        strMailCte7 = "";
                        strMailCte8 = "";
                        strMailCte9 = "";
                        strMailCte10 = "";
                        intClienteEnviaMail = 0;
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                    //si es cliente final sacamos los datos de el
                    if (intClienteFinal != 0) {
                        strSql = "SELECT DFA_RAZONSOCIAL,DFA_RFC,DFA_CALLE,DFA_COLONIA,DFA_LOCALIDAD,"
                                + " DFA_MUNICIPIO,DFA_ESTADO,DFA_NUMERO,DFA_NUMINT,"
                                + " DFA_CP,DFA_EMAIL FROM vta_cliente_facturacion "
                                + " where DFA_ID=" + intClienteFinal;
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            strNombreReceptor = FormateaTextoXml(rs.getString("DFA_RAZONSOCIAL"), false);
                            strRFCReceptor = FormateaTextoXml(rs.getString("DFA_RFC").replace("-", ""), false);
                            strCalleReceptor = FormateaTextoXml(rs.getString("DFA_CALLE"));
                            strColoniaReceptor = FormateaTextoXml(rs.getString("DFA_COLONIA"));
                            strLocalidadReceptor = FormateaTextoXml(rs.getString("DFA_LOCALIDAD"));
                            strMunicipioReceptor = FormateaTextoXml(rs.getString("DFA_MUNICIPIO"));
                            strCPReceptor = FormateaTextoXml(rs.getString("DFA_CP"));
                            strEdoReceptor = FormateaTextoXml(rs.getString("DFA_ESTADO"));
                            strNumero = FormateaTextoXml(rs.getString("DFA_NUMERO"));
                            strNumeroInt = FormateaTextoXml(rs.getString("DFA_NUMINT"));
                            strMailCte = rs.getString("DFA_EMAIL");
                        }
                        if (rs.getStatement() != null) {
                            //rs.getStatement().close();
                        }
                        rs.close();
                    }
                    // </editor-fold>
                }
            } else {
                // <editor-fold defaultstate="collapsed" desc="Sacamos los datos del empleado">
                strSql = "SELECT EMP_NOMBRE,EMP_RFC,"
                        + "EMP_CALLE,EMP_COLONIA,EMP_LOCALIDAD,EMP_MUNICIPIO,EMP_CP,"
                        + "EMP_ESTADO,EMP_NUMERO,EMP_NUMINT "
                        + "FROM rhh_empleados WHERE EMP_NUM = " + intCliente;
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strNombreReceptor = FormateaTextoXml(rs.getString("EMP_NOMBRE"), false);
                    strRFCReceptor = FormateaTextoXml(rs.getString("EMP_RFC").replace("-", ""), false);
                    strCalleReceptor = FormateaTextoXml(rs.getString("EMP_CALLE"));
                    strColoniaReceptor = FormateaTextoXml(rs.getString("EMP_COLONIA"));
                    strLocalidadReceptor = FormateaTextoXml(rs.getString("EMP_LOCALIDAD"));
                    strMunicipioReceptor = FormateaTextoXml(rs.getString("EMP_MUNICIPIO"));
                    strCPReceptor = FormateaTextoXml(rs.getString("EMP_CP"));
                    strEdoReceptor = FormateaTextoXml(rs.getString("EMP_ESTADO"));
                    strNumero = FormateaTextoXml(rs.getString("EMP_NUMERO"));
                    strNumeroInt = FormateaTextoXml(rs.getString("EMP_NUMINT"));
                    strMailCte = "";//Rs.getString("EMP_EMAIL1");
                    strMailCte2 = "";//Rs.getString("EMP_EMAIL2");
                }
                if (rs.getStatement() != null) {
                    //rs.getStatement().close();
                }
                rs.close();
                // </editor-fold>
            }

            // <editor-fold defaultstate="collapsed" desc="Obtenemos el regimel fiscal">
            //Sino viene el regimen fiscal lo buscamos
            if (!strRegimenFiscal.isEmpty()) {
                lstRegimenFiscal.add(strRegimenFiscal);
            } else {
                String strSqlRegFis = "select REGF_DESCRIPCION from vta_empregfiscal,vta_regimenfiscal "
                        + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID "
                        + " AND vta_empregfiscal.EMP_ID = " + intEmpId;
                rs = oConn.runQuery(strSqlRegFis, true);
                while (rs.next()) {
                    lstRegimenFiscal.add(rs.getString("REGF_DESCRIPCION"));
                    break;
                }
                if (rs.getStatement() != null) {
                    //rs.getStatement().close();
                }
                rs.close();

            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Obtenemos el nombre de la moneda">
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
            // </editor-fold>
        } catch (SQLException ex) {
            log.error("Error(Datos)" + ex.getMessage() + " " + ex.getLocalizedMessage());
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getClie", oConn);
            strRes = "ERROR:" + ex.getMessage();
        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Generamos objeto para los comprobantes">
        log.debug("Comenzamos a llenar objetos CFDI...");
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
        if (!this.strFolio.isEmpty()) {
            objComp.setFolio(this.strFolio + "");
        }
        //XMLGregorianCalendar xmlfecha = this.fecha.RegresaXMLGregorianCalendar(strFechaFactura, strHoraFactura);
        SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        Date dateTmp = new Date();
        try {
            dateTmp = formateaDate.parse(strFechaFactura + strHoraFactura);
        } catch (ParseException ex) {
            log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        objComp.setFecha(dateTmp);
        //objComp.setFecha(xmlfecha.toGregorianCalendar().getTime());
        objComp.setFormaDePago(strFAC_FORMADEPAGO);
        if (!strFAC_CONDPAGO.isEmpty()) {
            objComp.setCondicionesDePago(strFAC_CONDPAGO);
        }
        objComp.setNoCertificado(RecuperaNoSerieCertificado());
        //Certificado digital
        log.debug("Abrimos el certificado...");
        UtilCert cert = new UtilCert();
        cert.OpenCert(this.strPathCert);
        if (!cert.getStrResult().startsWith("ERROR")) {
            objComp.setCertificado(cert.getCertContentBase64());
            try {
                this.certificadoEmisor = cert.getCert().getEncoded();
            } catch (CertificateEncodingException ex) {
                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
            }
        }
        objComp.setSubTotal(new BigDecimal(NumberString.FormatearDecimal((BGSubtotal).doubleValue(), 2).replace(",", "")));
        //Al total hay que quitarle los impuestos retenidos
        double dblTotTotal = (BGTotal).doubleValue();
        double dblRetl = 0;
        double dblRet2 = 0;
        if (BGImpuestoRetenido1 != null) {
            dblRetl = (BGImpuestoRetenido1).doubleValue();
        }
        if (BGImpuestoRetenido2 != null) {
            dblRet2 = (BGImpuestoRetenido2).doubleValue();
        }
        if (this.bolNominas) {
            //No hacemos descuento porque ya existe...
        } else {
            dblTotTotal = dblTotTotal - dblRetl - dblRet2;
        }
        objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal(dblTotTotal, 2).replace(",", "")));
        //Anadimos el descuento
        if (BGDescuento != null) {
            if (BGDescuento.doubleValue() > 0) {
                //Validamos si es la nomina para descontarle el ISR
                //BGImpuestoRetenido1
                if (this.bolNominas) {
                    double dblDescTmp2 = (BGDescuento).doubleValue();
                    double dblImpRetTmp2 = (BGImpuestoRetenido1).doubleValue();
                    double dblTotDesctmp2 = dblDescTmp2 - dblImpRetTmp2;
                    if (dblTotDesctmp2 > 0) {
                        objComp.setDescuento(new BigDecimal(NumberString.FormatearDecimal(dblTotDesctmp2, 2).replace(",", "")));
                    }
                } else {
                    objComp.setDescuento(new BigDecimal(NumberString.FormatearDecimal((BGDescuento).doubleValue(), 2).replace(",", "")));
                }

            }
        }
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
        if (!strLocalidad.isEmpty()) {
            domicilioFiscal.setLocalidad(strLocalidad);
        }
        if (!strNumeroEmisor.isEmpty()) {
            domicilioFiscal.setNoExterior(strNumeroEmisor);
        }
        if (!strNumeroIntEmisor.isEmpty()) {
            domicilioFiscal.setNoInterior(strNumeroIntEmisor);
        }
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
        if (!strLocalidadReceptor.isEmpty()) {
            dRecep.setLocalidad(strLocalidadReceptor);
        }
        dRecep.setMunicipio(strMunicipioReceptor);
        dRecep.setPais(strPais);
        dRecep.setNoExterior(strNumero);
        if (!strNumeroInt.trim().equals("")) {
            dRecep.setNoInterior(strNumeroInt);
        }
        dRecep.setEstado(strEdoReceptor);
        //Evaluamos si tiene todos los datos, en caso contrario no agregamos el nodo
        if (dRecep.getCalle().isEmpty()
                && dRecep.getColonia().isEmpty()
                && dRecep.getCodigoPostal().isEmpty()
                && dRecep.getMunicipio().isEmpty()
                && dRecep.getNoExterior().isEmpty()
                && dRecep.getEstado().isEmpty()) {
            //Si los campos principales son vacios no agregamos el nodo
        } else {
            receptor.setDomicilio(dRecep);
        }

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
        //Para formatear fechas
        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyyMMdd");
        //Validamos si es un recibo de nominas para el detalle
        if (this.bolNominas) {
            // <editor-fold defaultstate="collapsed" desc="Recibos de nomina">
            strSql = "select sum(NOMD_UNITARIO) as tot from   rhh_nominas_deta n  WHERE TP_ID <> 0 and  n.NOM_ID  = " + this.intTransaccion;
            ResultSet rs2;
            try {
                rs2 = oConn.runQuery(strSql, true);
                while (rs2.next()) {

                    Concepto objC = objFactory.createComprobanteConceptosConcepto();
                    // <editor-fold defaultstate="collapsed" desc="Llenamos el concepto">
                    String strUnidaddeMedida = "Servicio";
                    objC.setDescripcion(strConceptoNomina);
                    objC.setCantidad(new BigDecimal(1));
                    objC.setUnidad(strUnidaddeMedida);
                    double FACD_VALORUNIT = rs2.getDouble("tot");
                    objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 2).replace(",", "")));
                    objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 2).replace(",", "")));
                    // </editor-fold>
                    /*Anadimos el concepto*/
                    conceps.getConcepto().add(objC);
                }
                if (rs2.getStatement() != null) {
                    rs2.getStatement().close();
                }
                rs2.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getPart", oConn);
                strRes = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
        } else {
            // <editor-fold defaultstate="collapsed" desc="Validamos el tipo de sistema y la tabla por abrir para recuperar el detalle de la operacion">
            if (!bolEsNc) {
                if (this.bolEsNotaCargo) {
                    // <editor-fold defaultstate="collapsed" desc="Notas cargo">
                    strSql = "SELECT NCAD_ID,NCAD_CVE,PR_ID,NCAD_DESCRIPCION,trim(NCAD_COMENTARIO) as comentario,"
                            + "NCAD_CANTIDAD,NCAD_PRECIO,NCAD_NOSERIE,NCAD_IMPORTE,NCAD_IMPUESTO1,NCAD_TASAIVA1,NCAD_UNIDAD_MEDIDA,PDD_ID "
                            + " FROM vta_notas_cargosdeta WHERE NCA_ID  = " + this.intTransaccion;
                    // </editor-fold>
                } else if (this.bolEsNotaCargoProveedor) {
                    // <editor-fold defaultstate="collapsed" desc="Notas cargo de proveedor">
                    strSql = "SELECT NCAD_ID,NCAD_CVE,PR_ID,NCAD_DESCRIPCION,trim(NCAD_COMENTARIO) as comentario,"
                            + "NCAD_CANTIDAD,NCAD_PRECIO,NCAD_NOSERIE,NCAD_IMPORTE,NCAD_IMPUESTO1,NCAD_TASAIVA1,NCAD_UNIDAD_MEDIDA,PDD_ID "
                            + " FROM vta_notas_cargosprovdeta WHERE NCA_ID  = " + this.intTransaccion;
                    // </editor-fold>
                } else // <editor-fold defaultstate="collapsed" desc="Factura">
                {
                    if (this.bolEsLocal) {
                        strSql = "SELECT FACD_ID,FACD_CVE,PR_ID,FACD_DESCRIPCION,FACD_COMENTARIO as comentario,"
                                + "FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_DESCUENTO,FACD_TASAIVA1,FACD_UNIDAD_MEDIDA "
                                + " FROM vta_facturasdeta WHERE FAC_ID  = " + this.intTransaccion;
                    } else {
                        strSql = "SELECT FACD_ID,FACD_CVE,PR_ID,FACD_DESCRIPCION,trim(FACD_COMENTARIO) as comentario,"
                                + "FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_DESCUENTO,FACD_TASAIVA1,FACD_UNIDAD_MEDIDA,PDD_ID "
                                + " FROM vta_facturasdeta WHERE FAC_ID  = " + this.intTransaccion;
                    } // </editor-fold>
                }
            } else // <editor-fold defaultstate="collapsed" desc="Nota de credito">
            {
                if (this.bolEsLocal) {
                    strSql = "SELECT NCD_ID,NCD_CVE,PR_ID,NCD_DESCRIPCION,NCD_COMENTARIO as comentario,"
                            + "NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_TASAIVA1,NCD_UNIDAD_MEDIDA "
                            + " FROM vta_ncreditodeta WHERE NC_ID  = " + this.intTransaccion;
                } else {
                    strSql = "SELECT NCD_ID,NCD_CVE,PR_ID,NCD_DESCRIPCION,trim(NCD_COMENTARIO) as comentario,"
                            + "NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_TASAIVA1,NCD_UNIDAD_MEDIDA "
                            + " FROM vta_ncreditodeta WHERE NC_ID  = " + this.intTransaccion;
                } // </editor-fold> 
            }
            ResultSet rs2;
            String strPrefijoDeta = "FACD";
            if (bolEsNc) {
                strPrefijoDeta = "NCD";
            }
            if (this.bolEsNotaCargo || this.bolEsNotaCargoProveedor) {
                strPrefijoDeta = "NCAD";
            }
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    // <editor-fold defaultstate="collapsed" desc="Creamos elemento del concepto">
                    int intPDD_ID = 0;
                    if (!bolEsNc) {
                        if (!this.bolEsLocal) {
                            intPDD_ID = rs.getInt("PDD_ID");
                        }
                    }
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
                    objC.setCantidad(new BigDecimal(NumberString.FormatearDecimal((rs.getBigDecimal(strPrefijoDeta + "_CANTIDAD")).doubleValue(), 2).replace(",", "")));
                    if (rs.getString(strPrefijoDeta + "_CVE").isEmpty()) {
                        objC.setNoIdentificacion("Servicio");
                    } else {
                        objC.setNoIdentificacion(rs.getString(strPrefijoDeta + "_CVE"));
                    }
                    // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc="CALCULAMOS IMPORTES SIN IVA">
                    double FACD_IMPORTE = rs.getDouble(strPrefijoDeta + "_IMPORTE");
                    double FACD_IMPUESTO1 = rs.getDouble(strPrefijoDeta + "_IMPUESTO1");
                    double FACD_TASAIVA1 = rs.getDouble(strPrefijoDeta + "_TASAIVA1");
                    double FACD_IMPORTESINIVA = 0;
                    double FACD_DESCUENTO = rs.getDouble(strPrefijoDeta + "_DESCUENTO");
                    double FACD_VALORUNIT = rs.getDouble(strPrefijoDeta + "_PRECIO");
                    double FACD_CANTIDAD = rs.getInt(strPrefijoDeta + "_CANTIDAD");
//                    double FACD_VALORUNIT = 0;

                    // <editor-fold defaultstate="collapsed" desc="Evaluamos si ya existe en la lista de traslados">
                    boolean bolEncontroTraslado = false;
                    Iterator<TrasladosIVA> itTraslados = lstTrasladosIVA.iterator();
                    while (itTraslados.hasNext()) {
                        TrasladosIVA traslado = itTraslados.next();
                        if (traslado.getDblTasaIVA() == FACD_TASAIVA1) {
                            bolEncontroTraslado = true;
                            traslado.setDblImporteTasaIVA(FACD_IMPUESTO1 + traslado.getDblImporteTasaIVA());
                        }
                    }
                    if (!bolEncontroTraslado) {
                        TrasladosIVA traslado = new TrasladosIVA();
                        traslado.setDblTasaIVA(FACD_TASAIVA1);
                        traslado.setDblImporteTasaIVA(FACD_IMPUESTO1);
                        lstTrasladosIVA.add(traslado);
                    }
                    // </editor-fold>

                    if (this.bolEsLocal) {
                        if (!rs.getString(strPrefijoDeta + "_CVE").equals("...") && !rs.getString(strPrefijoDeta + "_CVE").equals("")) {
                            //Es factura comercial

                            FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_IMPUESTO1;
//                            FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_DESCUENTO;
//                            FACD_IMPORTESINIVA = (FACD_VALORUNIT * FACD_CANTIDAD);
                        } else {
                            FACD_IMPORTESINIVA = (FACD_VALORUNIT * FACD_CANTIDAD);
                        }
                    } else {
                        FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_IMPUESTO1;
//                        FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_DESCUENTO;
//                        FACD_IMPORTESINIVA = (FACD_VALORUNIT * FACD_CANTIDAD);
                    }
//                    FACD_VALORUNIT = FACD_IMPORTESINIVA / rs.getDouble(strPrefijoDeta + "_CANTIDAD");
//                    FACD_VALORUNIT = FACD_VALORUNIT;
                    if (FACD_VALORUNIT != 0) {
                        objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 2).replace(",", "")));
                        objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(FACD_IMPORTESINIVA, 2).replace(",", "")));
                    } else {
                        objC.setValorUnitario(new BigDecimal(0.0));
                        objC.setImporte(new BigDecimal(0.0));
                    }

                    // </editor-fold>
                    objC.setUnidad(strUnidaddeMedida);
                    /*Informacion de la Aduana*/
                    if (!rs.getString(strPrefijoDeta + "_CVE").equals("...")
                            && !rs.getString(strPrefijoDeta + "_CVE").equals("SHIPPING")
                            && !bolEsNc) {
                        // <editor-fold defaultstate="collapsed" desc="Obtenemos informacion de la aduana">
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
                        // </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc="Si el documento viene de un pedido hacemos la busqueda por pedidos">
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
                                    + "vta_pedimentos.PED_FECHA_ENTRA"
                                    + " FROM vta_aduana INNER JOIN vta_pedimentos ON vta_aduana.AD_ID = vta_pedimentos.AD_ID"
                                    + " INNER JOIN vta_prodlote ON vta_pedimentos.PED_ID = vta_prodlote.PED_ID"
                                    + " INNER JOIN  vta_movproddeta ON vta_prodlote.PR_ID = vta_movproddeta.PR_ID AND vta_prodlote.PL_NUMLOTE = vta_movproddeta.PL_NUMLOTE"
                                    + " INNER JOIN vta_pedidosdeta ON vta_movproddeta.PR_ID = vta_pedidosdeta.PR_ID AND vta_movproddeta.MPD_IDORIGEN = vta_pedidosdeta.PDD_ID"
                                    + " INNER JOIN vta_facturasdeta ON vta_pedidosdeta.PDD_ID = vta_facturasdeta.PDD_ID"
                                    + " WHERE vta_facturasdeta.PDD_ID <> 0"
                                    + " AND vta_facturasdeta.FACD_ID=" + rs.getString(strPrefijoDeta + "_ID")
                                    + " AND vta_facturasdeta.PR_ID =" + rs.getString("PR_ID");
                        }
                        // </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc="Sacamos informacin de la aduana">
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
                                log.error(ex.getMessage());
                            }
                        }
                        rs2.close();
                        // </editor-fold>
                    }
                    /*Anadimos el concepto*/
                    conceps.getConcepto().add(objC);
                }
                if (rs.getStatement() != null) {
                    //rs.getStatement().close();
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "getPart", oConn);
                strRes = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>

            // </editor-fold>
        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Nodo Impuestos">
        log.debug("Nodo impuestos...");
        Impuestos imp = objFactory.createComprobanteImpuestos();
        if (this.bolNominas) {

            // <editor-fold defaultstate="collapsed" desc="Marcamos las retenciones de ISR">
            Retencion ret = objFactory.createComprobanteImpuestosRetencionesRetencion();
            ret.setImporte(new BigDecimal(NumberString.FormatearDecimal((BGImpuestoRetenido1).doubleValue(), 2).replace(",", "")));
            ret.setImpuesto("ISR");
            Retenciones rets = objFactory.createComprobanteImpuestosRetenciones();
            rets.getRetencion().add(ret);
            imp.setRetenciones(rets);
            imp.setTotalImpuestosRetenidos(new BigDecimal(NumberString.FormatearDecimal((BGImpuestoRetenido1).doubleValue(), 2).replace(",", "")));

            // </editor-fold>
        } else {
            // <editor-fold defaultstate="collapsed" desc="Nodo traslados">

            Traslados objTraslados = objFactory.createComprobanteImpuestosTraslados();
            int intCuantos = 0;
            //Iteramos por todos los IVAS que contenga el documento
            Iterator<TrasladosIVA> itTraslados = lstTrasladosIVA.iterator();
            while (itTraslados.hasNext()) {
                TrasladosIVA traslado = itTraslados.next();
                if (traslado.getDblTasaIVA() > 0) {
                    Traslado objTraslado = objFactory.createComprobanteImpuestosTrasladosTraslado();
                    objTraslado.setImporte(new BigDecimal(NumberString.FormatearDecimal(traslado.getDblImporteTasaIVA(), 2).replace(",", "")));
                    objTraslado.setImpuesto("IVA");
                    objTraslado.setTasa(new BigDecimal(NumberString.FormatearDecimal(traslado.getDblTasaIVA(), 2).replace(",", "")));
                    objTraslados.getTraslado().add(objTraslado);
                    intCuantos++;
                }
            }

            // <editor-fold defaultstate="collapsed" desc="Traslado IEPS">
            if (intUsoIEPS == 1) {
                Traslado objTrasladoIEPS = objFactory.createComprobanteImpuestosTrasladosTraslado();
                objTraslados.getTraslado().add(objTrasladoIEPS);
                objTrasladoIEPS.setImporte(BGImporteIEPS);
                objTrasladoIEPS.setImpuesto("IEPS");
                objTrasladoIEPS.setTasa(BGTasaIEPS);
                intCuantos++;
            }
            // </editor-fold>

            //Validar sino hay impuestos para no añadir el nodo
            if (intCuantos > 0) {
                imp.setTraslados(objTraslados);
                imp.setTotalImpuestosTrasladados(new BigDecimal(NumberString.FormatearDecimal((BGImpuesto1.add(BGImporteIEPS)).doubleValue(), 2).replace(",", "")));
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Validamos si es por honorarios para marcar la retencion">
            if (BGImpuestoRetenido1.doubleValue() > 0 || BGImpuestoRetenido2.doubleValue() > 0) {
                /*Nodo retencion
             Informacin de cada nodo Retencion
             nota: esta secuencia a, b, deber ser repetida por cada nodo Retencin relacionado, el total
             de impuestos retenidos no se repite.
             a. impuesto
             b. importe
             c. totalImpuestosRetenidos
                 */
                Retenciones rets = objFactory.createComprobanteImpuestosRetenciones();

                double dblTotalImpuestosRetenidos = 0;
                dblTotalImpuestosRetenidos += BGImpuestoRetenido1.doubleValue() + BGImpuestoRetenido2.doubleValue();
                if (BGImpuestoRetenido1.doubleValue() > 0) {
                    Retencion ret = objFactory.createComprobanteImpuestosRetencionesRetencion();
                    ret.setImporte(new BigDecimal(NumberString.FormatearDecimal((BGImpuestoRetenido1).doubleValue(), 2).replace(",", "")));
                    ret.setImpuesto("IVA");
                    rets.getRetencion().add(ret);
                }

                if (BGImpuestoRetenido2.doubleValue() > 0) {
                    Retencion ret2 = objFactory.createComprobanteImpuestosRetencionesRetencion();
                    ret2.setImporte(new BigDecimal(NumberString.FormatearDecimal((BGImpuestoRetenido2).doubleValue(), 2).replace(",", "")));
                    ret2.setImpuesto("ISR");
                    rets.getRetencion().add(ret2);
                }
                imp.setRetenciones(rets);
                imp.setTotalImpuestosRetenidos(new BigDecimal(NumberString.FormatearDecimal(dblTotalImpuestosRetenidos, 2).replace(",", "")));
            }
            // </editor-fold>

        }

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Llenamos el comprobante con su detalle">
        objComp.setEmisor(emisor);
        objComp.setReceptor(receptor);
        objComp.setConceptos(conceps);
        objComp.setImpuestos(imp);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Complementos fiscales dependiendo del rubro">
        log.debug("Complementos fiscales...");
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

            // <editor-fold defaultstate="collapsed" desc="Generamos el documento XML">
            try {
                JAXBContext jaxbContext;
                //if (satAddenda == null) {
                if (this.bolComplementoFiscal && this.bolDonataria && !this.bolNominas) {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
                } else if (this.bolComplementoFiscal && this.bolNominas) {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.nominas.ObjectFactory.class);
                } else if (this.bolComplementoFiscal && this.bolImpuestoLocales) {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory.class);
                } else {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class);
                }

                Marshaller marshaller = jaxbContext.createMarshaller();
                /*Vinculamos URI'S que solicita hacienda*/
                //marshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "http://www.w3.org/2001/XMLSchema-instance");
                String strListXSD = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd";//Version 3.2
                if (this.bolComplementoFiscal && this.bolDonataria && !this.bolNominas) {
                    strListXSD += " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
                }
                if (this.bolComplementoFiscal && this.bolNominas) {
                    strListXSD += " http://www.sat.gob.mx/nomina http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina11.xsd";
                }
                if (this.bolComplementoFiscal && this.bolImpuestoLocales) {
                    strListXSD += " http://www.sat.gob.mx/implocal http://www.sat.gob.mx/cfd/implocal/implocal.xsd";
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
                StringBuilder strPathFile = new StringBuilder("");
                StringBuilder strNomFile = new StringBuilder("");
                ERP_MapeoFormato mapeoXml = null;
                //Obtenemos el nombre del archivo xml
                if (!bolEsNc) {
                    if (this.bolNominas) {
                        mapeoXml = new ERP_MapeoFormato(7);
                        strNomFile.append(getNombreFileXml(mapeoXml));
                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
                    } else if (this.bolEsNotaCargo) {
                        mapeoXml = new ERP_MapeoFormato(8);
                        strNomFile.append(getNombreFileXml(mapeoXml));
                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
                    } else if (this.bolEsNotaCargoProveedor) {
                        mapeoXml = new ERP_MapeoFormato(9);
                        strNomFile.append(getNombreFileXml(mapeoXml));
                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
                    } else {
                        mapeoXml = new ERP_MapeoFormato(1);
                        strNomFile.append(getNombreFileXml(mapeoXml));
                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
                    }
                } else {
                    mapeoXml = new ERP_MapeoFormato(5);
                    strNomFile.append(getNombreFileXml(mapeoXml));
                    strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
                }
                marshaller.marshal(objComp, new FileOutputStream(strPathFile.toString()));

                //Llamado al proceso deTimbrado
                String strRespTimb = GeneraTimbrado(strNomFile.toString());

                //Validamos si se timbro correctamente
                if (strRespTimb.equals("OK")) {
                    //Generamos el QR
                    String strQResp = null;
                    if (!bolEsNc) {
                        if (!this.bolNominas) {
                            if (this.bolEsNotaCargo) {
                                strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 5);
                            } else if (this.bolEsNotaCargoProveedor) {
                                strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 6);
                            } else {
                                strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 1);
                            }
                        } else {
                            strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 3);
                        }
                    } else {
                        strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), this.strfolioFiscalUUID, this.strPath, intTransaccion, 2);
                    }
                    if (strQResp.startsWith("OK")) {
                        strPathQR = strQResp.replace("OK", "");
                    }
                    //Si hay una addenda ponemos los namespaces necesarios
                    if (satAddenda != null) {
                        satAddenda.makeNameSpaceDeclaration(strPathFile.toString(), intTransaccion, oConn);
                    }

                    // <editor-fold defaultstate="collapsed" desc="Almacenamos el sello y la cadena original en la transaccion">
                    if (!bolEsNc) {
                        if (this.bolNominas) {
                            String strUpdate = "UPDATE rhh_nominas "
                                    + "set NOM_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                                    + ",NOM_SELLO='" + this.strSelloCFD + "'"
                                    + ",NOM_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                                    + ",NOM_PATH_CBB='" + this.strPathQR + "'"
                                    + ",NOM_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                                    + " where NOM_ID = " + this.intTransaccion;
                            oConn.runQueryLMD(strUpdate);
                        } else if (this.bolEsNotaCargo) {
                            String strUpdate = "UPDATE vta_notas_cargos "
                                    + "set NCA_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                                    + ",NCA_SELLO='" + this.strSelloCFD + "'"
                                    + ",NCA_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                                    + ",NCA_PATH_CBB='" + this.strPathQR + "'"
                                    + ",NCA_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                                    + ",NCA_TIPOCOMP=" + intEMP_TIPOCOMP + " "
                                    + " where NCA_ID = " + this.intTransaccion;
                            oConn.runQueryLMD(strUpdate);
                        } else if (this.bolEsNotaCargoProveedor) {
                            String strUpdate = "UPDATE vta_notas_cargosprov "
                                    + "set NCA_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                                    + ",NCA_SELLO='" + this.strSelloCFD + "'"
                                    + ",NCA_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                                    + ",NCA_PATH_CBB='" + this.strPathQR + "'"
                                    + ",NCA_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                                    + ",NCA_TIPOCOMP=" + intEMP_TIPOCOMP + " "
                                    + " where NCA_ID = " + this.intTransaccion;
                            oConn.runQueryLMD(strUpdate);
                        } else {
                            String strUpdate = "UPDATE vta_facturas "
                                    + "set FAC_CADENAORIGINAL = '" + this.strCadenaOriginalReal + "'"
                                    + ",FAC_SELLO='" + this.strSelloCFD + "'"
                                    + ",FAC_CADENA_TIMBRE='" + this.GeneraCadenaOriginalTimbre() + "'"
                                    + ",FAC_PATH_CBB='" + this.strPathQR + "'"
                                    + ",FAC_NOSERIECERTTIM='" + this.strNoCertSAT + "'"
                                    + ",FAC_TIPOCOMP=" + intEMP_TIPOCOMP + " "
                                    + " where FAC_ID = " + this.intTransaccion;
                            oConn.runQueryLMD(strUpdate);
                        }
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

                } else {
                    strRes = "ERROR:" + strRespTimb;
                }
            } catch (FileNotFoundException ex) {
                log.error(ex.getMessage());
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXMLFile", oConn);
                strRes = "ERROR:" + ex.getMessage();
            } catch (JAXBException ex) {
                log.error(ex.getMessage());
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXML", oConn);
                strRes = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Si todo fue OK generamos el PDF">
            if (strRes.equals("OK")) {
                if (bolSendMailMasivo) {
                    //Generamos formato en jasper
                    if (this.bolNominas) {
                        //Por el momento no se hace envio automatico...
                    } else if (this.bolEsNotaCargoProveedor) {
                        //Por el momento no se hace envio automatico...
                    } else {
                        // <editor-fold defaultstate="collapsed" desc="Formatos de ventas, notas de credito y notas de cargo">
                        try {
                            //Evaluamos el tipo de formato en que se enviaran los PDF's
                            if (this.bolUsoFormatoJasper) {
                                log.debug("Genera formato Jasper...");
                                String strRespForm = GeneraImpresionPDFJasper(strPath, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                                log.debug("strRespForm:" + strRespForm);
                                if (!strRespForm.startsWith("ERROR:")) {
                                    //Mandamos mail con la factura
                                    if (!bolEsLocal) {
                                        try {
                                            GeneraMail(intClienteEnviaMail, strMailCte, strMailCte2, strMailCte3, strMailCte4, strMailCte5,
                                                    strMailCte6, strMailCte7, strMailCte8, strMailCte9, strMailCte10,
                                                    intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                                        } catch (Exception ex) {
                                            log.error(" " + ex.getLocalizedMessage());
                                            log.error(" " + ex.getMessage());
                                        }
                                    }
                                } else {
                                    strRes = strRespForm;
                                }
                            } else {
                                String strRespForm = GeneraImpresionPDF(strPath, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                                if (strRespForm.equals("OK")) {
                                    //Mandamos mail con la factura
                                    if (!bolEsLocal) {
                                        try {
                                            GeneraMail(intClienteEnviaMail, strMailCte, strMailCte2, strMailCte3, strMailCte4, strMailCte5,
                                                    strMailCte6, strMailCte7, strMailCte8, strMailCte9, strMailCte10,
                                                    intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
                                        } catch (Exception ex) {
                                            log.error(" " + ex.getLocalizedMessage());
                                            log.error(" " + ex.getMessage());
                                        }
                                    }
                                } else {
                                    strRes = strRespForm;
                                }
                            }

                        } catch (Exception ex) {
                            log.error(" Error envio de correos... ");
                            log.error(" ex " + ex.getMessage());
                            log.error(" ex " + ex.getLocalizedMessage());
                        }
                        // </editor-fold>   
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
     * Realiza el proceso de timbrado con llamadas a los webservices de los
     * diferentes proveedores que se implementen
     *
     * @param strNomFile Nombre del Archivo por timbrar
     * @return Regresa OK en caso de que sea exitosa la operación o Error en su
     * defecto
     */
    protected String GeneraTimbrado(String strNomFile) {
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
            if (!bolEsNc) {
                if (this.bolNominas) {
                    timbrado.setStrTablaDoc("rhh_nominas");
                    timbrado.setStrPrefijoDoc("NOM_");
                    timbrado.setBolNomina(true);
                } else if (this.bolEsNotaCargo) {
                    timbrado.setStrTablaDoc("vta_notas_cargos");
                    timbrado.setStrPrefijoDoc("NCA_");
                } else if (this.bolEsNotaCargoProveedor) {
                    timbrado.setStrTablaDoc("vta_notas_cargosprov");
                    timbrado.setStrPrefijoDoc("NCA_");
                } else {
                    timbrado.setStrTablaDoc("vta_facturas");
                    timbrado.setStrPrefijoDoc("FAC_");

                }
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
            if (this.strfolioFiscalUUID != null) {
                this.strNomFileXml = strNomFileXml.replace("%UUID%", this.strfolioFiscalUUID);
            }

        } else {
            strRes = "ERROR: EL PAC no existe";
        }
        return strRes;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos adicionales">
    /**
     * Llena la addenda
     */
    private String GeneraFirma(Comprobante objComp) {
        String strValorSello = null;
        if (this.strPassKey != null) {
            String strValorEncrip = GeneraCadenaOriginal(objComp);
            strCadenaOriginalReal = strValorEncrip;

            try {
                PrivateKey key;
                log.debug("Inicia timbrado..." + this.strPathKey + " " + this.strPassKey);
                key = ObtenerPrivateKey(this.strPathKey, this.strPassKey);
                if (key == null) {
                    strValorSello = "ERROR:NO SE PUDO ABRIR EL SELLO";
                    log.error(strValorSello);
                } else {
                    log.debug("Obtenemos llave...");
                    //Guardamos la llave para los pacs
                    this.llavePrivadaEmisor = key.getEncoded();
                    //Sellamos
                    try {
                        /*Codigo generico*/
                        byte[] data = strValorEncrip.getBytes("UTF8");
                        Signature sig = Signature.getInstance("SHA1withRSA");
                        sig.initSign(key);
                        sig.update(data);
                        log.debug("sellamos...");
                        //Sellamos
                        byte[] signatureBytes = sig.sign();
                        //Convertimos a base 64
                        log.debug("base de 64...");
                        byte[] b64Enc = Base64.encodeBase64(signatureBytes);
                        strValorSello = new String(b64Enc);
                        /*Codigo generico*/
                    } catch (IOException ex) {
                        log.error("Error IO " + ex.getMessage());
                        strValorSello = "ERROR:" + ex.getMessage();
                    }
                }
            } catch (SignatureException ex) {
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "signature", oConn);
                log.error("kp(1)" + ex.getMessage());
                strValorSello = "ERROR:" + ex.getMessage();
            } catch (InvalidKeyException ex) {
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "invalidKey", oConn);
                log.error("kp(2)" + ex.getMessage());
                strValorSello = "ERROR:" + ex.getMessage();
            } catch (NoSuchAlgorithmException ex) {
                this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "noSuchAlgoritm", oConn);
                log.error("kp(3)" + ex.getMessage());
                strValorSello = "ERROR:" + ex.getMessage();
            }
        } else {
            strValorSello = "ERROR:NO ESPECIFICO EL PASSWORD DE LA LLAVE";
            log.error(strValorSello);
        }

        return strValorSello;
    }

    /**
     * Genera la cadena original
     */
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
            log.debug("Comenzamos..." + this.bolComplementoFiscal + " " + this.bolNominas);
            if (satAddenda == null) {
                if (this.bolComplementoFiscal && this.bolDonataria && !this.bolNominas) {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
                } else if (this.bolComplementoFiscal && this.bolNominas) {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.nominas.ObjectFactory.class);
                } else if (this.bolComplementoFiscal && this.bolImpuestoLocales) {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory.class);
                } else {
                    jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class);
                }
            } else if (this.bolComplementoFiscal && this.bolDonataria && !this.bolNominas) {
                jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class);
            } else if (this.bolComplementoFiscal && this.bolNominas) {
                jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.nominas.ObjectFactory.class);
            } else if (this.bolComplementoFiscal && this.bolImpuestoLocales) {
                jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class, Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory.class);
            } else {
                jaxbContext = JAXBContext.newInstance(Core.FirmasElectronicas.SAT3_2.ObjectFactory.class);
            }
            log.debug("Comenzamos...2");
            Marshaller marshaller = jaxbContext.createMarshaller();
            /*Vinculamos URI'S que solicita hacienda*/
            //marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd");
            String strListXSD = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd";//Version 3.2
            if (this.bolComplementoFiscal && this.bolDonataria && !this.bolNominas) {
                strListXSD += " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
            }
            if (this.bolComplementoFiscal && this.bolNominas) {
                strListXSD += " http://www.sat.gob.mx/nomina http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina11.xsd";
            }
            if (this.bolComplementoFiscal && this.bolImpuestoLocales) {
                strListXSD += " http://www.sat.gob.mx/implocal http://www.sat.gob.mx/cfd/implocal/implocal.xsd";
            }
            log.debug("Comenzamos...3");
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
            log.debug("Comenzamos...4");
            /*Guardamos comprobante*/
            marshaller.marshal(objComp, doc);
            log.debug("Comenzamos...5");
            // Use the DOM Document to define a DOMSource object.
            xmlDomSource = new DOMSource(doc);
            log.debug("Comenzamos...6");
            // Set the base URI for the DOMSource so any relative URIs it contains can
            // be resolved.
            xmlDomSource.setSystemId("sat.xml");
            log.debug("Comenzamos...7");
        } catch (JAXBException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), this.varSesiones.getStrUser(), "genXML" + ex.getMessage(), oConn);
        }
        try {
            log.debug("xmlDomSource:" + xmlDomSource);
            Transformer transformer = tFactory.newTransformer(new StreamSource(this.strPath + System.getProperty("file.separator") + "cadenaoriginal_3_2.xslt"));
            transformer.transform(xmlDomSource, new StreamResult(strCadenaStr));
        } catch (TransformerException ex) {
            Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
            log.error(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
            log.error(ex.getMessage());
        }
        log.debug("***************" + strCadenaStr);
        return strCadenaStr.toString();
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
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Envio de mail">
    /**
     * Envia el mail al cliente
     *
     * @param intClienteEnviaMail Con 1 indica que se le envia mail al cliente
     * @param strMailCte Es el mail del cliente
     * @param strMailCte2 Es el segundo mail del cliente
     * @param strMailCte3 Es el tercer mail del cliente
     * @param strMailCte4 Es el cuarto mail del cliente
     * @param strMailCte5 Es el quinto mail del cliente
     * @param strMailCte6 Es el sexto mail del cliente
     * @param strMailCte7 Es el septimo mail del cliente
     * @param strMailCte8 Es el octavo mail del cliente
     * @param strMailCte9 Es el noveno mail del cliente
     * @param strMailCte10 Es el decimo mail del cliente
     * @param intEMP_TIPOCOMP Es el tipo de comprobante
     * @param intEmpId Es el id de la empresa
     * @param strFAC_NOMFORMATO Es el nombre del formato
     * @return Regresa OK si fue exitoso el envio del mail
     */
    protected String GeneraMail(int intClienteEnviaMail, String strMailCte, String strMailCte2,
            String strMailCte3, String strMailCte4, String strMailCte5,
            String strMailCte6, String strMailCte7, String strMailCte8,
            String strMailCte9, String strMailCte10,
            int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO) {
        String strResp = "OK";
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
        if (this.bolEsNotaCargo) {
            strNomTemplate = "NOTA_CARGO";
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
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            if (intEMP_ACUSEFACTURA == 1) {
                mail.setBolAcuseRecibo(true);
            }
            //Obtenemos los usuarios a los que mandaremos el mail, siempre y cuando tenga acceso a esta empresa
            String strLstMail = "";
            strSql = "select EMAIL from usuarios where BOL_MAIL_FACT = 1 and (select count(UE_ID) from vta_userempresa "
                    + " WHERE EMP_ID = " + this.varSesiones.getIntIdEmpresa() + " AND ID_USUARIOS = usuarios.id_usuarios ) > 0 ";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
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
                log.error(ex.getMessage());
            }
            //Validamos si el mail del cliente es valido
            boolean bolMailCteValido1 = false;
            boolean bolMailCteValido2 = false;
            boolean bolMailCteValido3 = false;
            boolean bolMailCteValido4 = false;
            boolean bolMailCteValido5 = false;
            boolean bolMailCteValido6 = false;
            boolean bolMailCteValido7 = false;
            boolean bolMailCteValido8 = false;
            boolean bolMailCteValido9 = false;
            boolean bolMailCteValido10 = false;
            if (mail.isEmail(strMailCte)) {
                bolMailCteValido1 = true;
            }
            if (mail.isEmail(strMailCte2)) {
                bolMailCteValido2 = true;
            }
            if (mail.isEmail(strMailCte3)) {
                bolMailCteValido3 = true;
            }
            if (mail.isEmail(strMailCte4)) {
                bolMailCteValido4 = true;
            }
            if (mail.isEmail(strMailCte5)) {
                bolMailCteValido5 = true;
            }
            if (mail.isEmail(strMailCte6)) {
                bolMailCteValido6 = true;
            }
            if (mail.isEmail(strMailCte7)) {
                bolMailCteValido7 = true;
            }
            if (mail.isEmail(strMailCte8)) {
                bolMailCteValido8 = true;
            }
            if (mail.isEmail(strMailCte9)) {
                bolMailCteValido9 = true;
            }
            if (mail.isEmail(strMailCte10)) {
                bolMailCteValido10 = true;
            }
            //Validamos lista de mails especiales
            if (!this.strListMailsEsp.isEmpty()) {
                strLstMail += "," + this.strListMailsEsp;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("") || bolMailCteValido1 || bolMailCteValido2 || bolMailCteValido3 || bolMailCteValido4 || bolMailCteValido5
                    || bolMailCteValido6 || bolMailCteValido7 || bolMailCteValido8 || bolMailCteValido9 || bolMailCteValido10) {
                String strMsgMail = lstMail[1];
                strMsgMail = strMsgMail.replace("%folio%", strNumFolio);
                String strAsunto = lstMail[0].replace("%folio%", strNumFolio);
                mail.setAsunto(strAsunto);
//            mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                //Reemplazamos campos personalizados
                String strSqlMail = "select * from vta_facturas where FAC_ID = " + this.intTransaccion;
                if (bolEsNc) {
                    strSqlMail = "select * from vta_ncredito where NC_ID = " + this.intTransaccion;
                }
                if (this.bolEsNotaCargo) {
                    strSqlMail = "select * from vta_notas_cargos where NCA_ID = " + this.intTransaccion;
                }
                ResultSet rsMail;
                try {
                    rsMail = oConn.runQuery(strSqlMail, true);
                    mail.setReplaceContent(rsMail);
                    rsMail.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                //Reemplazamos campos personalizados en la empresa...
                strSqlMail = "select * from vta_empresas where EMP_ID = " + this.varSesiones.getIntIdEmpresa();
                try {
                    rsMail = oConn.runQuery(strSqlMail, true);
                    mail.setReplaceContent(rsMail);
                    rsMail.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
                //Establecemos parametros
                mail.setUsuario(strsmtp_user);
                mail.setContrasenia(strsmtp_pass);
                mail.setHost(strsmtp_server);
                mail.setPuerto(strsmtp_port);
                //Adjuntamos XML y PDF
                if (!bolEsNc) {
                    if (this.bolEsNotaCargo) {
                        log.debug("Adjunto....XML:" + strNomFileXml);
                        log.debug("Adjunto....PDF:" + strNomFilePdf);
                        mail.setFichero(strPath + strNomFileXml);
                        mail.setFichero(strPath + strNomFilePdf);
                    } else {
                        log.debug("Adjunto....XML:" + strNomFileXml);
                        log.debug("Adjunto....PDF:" + strNomFilePdf);
                        mail.setFichero(strPath + strNomFileXml);
                        mail.setFichero(strPath + strNomFilePdf);
                    }
                } else {
                    log.debug("Adjunto....XML:" + strNomFileXml);
                    log.debug("Adjunto....PDF:" + strNomFilePdf);
                    mail.setFichero(strPath + strNomFileXml);
                    mail.setFichero(strPath + strNomFilePdf);
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
                if (bolMailCteValido1 && (this.blEmail1)) {
                    log.debug("Envio mail 1 de cliente  " + strMailCte);
                    mail.setDestino(strMailCte);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al segundo mail del cliente
                if (bolMailCteValido2 && (this.blEmail2)) {
                    log.debug("Envio mail 2 de cliente  " + strMailCte2);
                    mail.setDestino(strMailCte2);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al tercer mail del cliente
                if (bolMailCteValido3 && (this.blEmail3)) {
                    log.debug("Envio mail 3 de cliente  " + strMailCte3);
                    mail.setDestino(strMailCte3);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al cuarto mail del cliente
                if (bolMailCteValido4 && (this.blEmail4)) {
                    log.debug("Envio mail 4 de cliente  " + strMailCte4);
                    mail.setDestino(strMailCte4);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al quinto mail del cliente
                if (bolMailCteValido5 && (this.blEmail5)) {
                    log.debug("Envio mail 5 de cliente  " + strMailCte5);
                    mail.setDestino(strMailCte5);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al quinto mail del cliente
                if (bolMailCteValido6 && (this.blEmail6)) {
                    log.debug("Envio mail 6 de cliente  " + strMailCte6);
                    mail.setDestino(strMailCte6);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al quinto mail del cliente
                if (bolMailCteValido7 && (this.blEmail7)) {
                    log.debug("Envio mail 7 de cliente  " + strMailCte7);
                    mail.setDestino(strMailCte7);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al quinto mail del cliente
                if (bolMailCteValido8 && (this.blEmail8)) {
                    log.debug("Envio mail 8 de cliente  " + strMailCte8);
                    mail.setDestino(strMailCte8);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al quinto mail del cliente
                if (bolMailCteValido9 && (this.blEmail9)) {
                    log.debug("Envio mail 9 de cliente  " + strMailCte9);
                    mail.setDestino(strMailCte9);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
                //Enviamos al quinto mail del cliente
                if (bolMailCteValido10 && (this.blEmail10)) {
                    log.debug("Envio mail 10 de cliente  " + strMailCte10);
                    mail.setDestino(strMailCte10);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
            }
            //Si los correos del cliente no son validos indicamos que no se pudo enviar
            if (!bolMailCteValido1 && !bolMailCteValido2 && !bolMailCteValido3 && !bolMailCteValido4 && !bolMailCteValido5
                    && !bolMailCteValido6 && !bolMailCteValido7 && !bolMailCteValido8 && !bolMailCteValido9 && !bolMailCteValido10) {
                strResp = "El mail no es valido..";
            }
        }
        return strResp;
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
    @Override
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
            //strNumFolio
            strNomFilePdf = getNombreFilePDF(mapeo).replace("%UUID%", strfolioFiscalUUID);
            log.debug("strNomFilePdf:" + strNomFilePdf);
            //"%nom_archivo%_cfdi%strNumFolio%.pdf"
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strPath
                    + strNomFilePdf));
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
            format.setIntTypeOut(Formateador.OBJECT);
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
                log.debug("strNomFormato:" + strNomFormato);
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
            log.error(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        } catch (DocumentException ex) {
            log.error(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        }
        return strResp;
    }

    /**
     * Genera el nombre del pdf
     */
    private String getNombreFilePDF(ERP_MapeoFormato mapeo) {
        if (strNomFilePdf == null) {
            String strFechaDocTmp = strFechaCFDI;
            if (mapeo.getIntTipoComp() == 1) {
                strPatronNomPDF = mapeo.getStrNomPDF("FACTURA");
            }
            if (mapeo.getIntTipoComp() == 5) {
                strPatronNomPDF = mapeo.getStrNomPDF("NCREDITO");
            }
            if (mapeo.getIntTipoComp() == 7) {
                strPatronNomPDF = mapeo.getStrNomPDF("NOMINA");
                strFechaDocTmp = strFechaNominaGenera;
            }
            if (mapeo.getIntTipoComp() == 8) {
                strPatronNomPDF = mapeo.getStrNomPDF("NOTACARGO");
                strFechaDocTmp = strFechaNominaGenera;
            }
            strNomFilePdf = this.strPatronNomPDF.replace("%nom_archivo%", mapeo.getStrNomArchivo()).replace("%strNumFolio%", this.strFolio).replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaDocTmp).replace(" ", "_") + "";
            if (strNomFilePdf.contains("%RFC%")) {
                strNomFilePdf = strNomFilePdf.replace("%RFC%", this.objComp.getEmisor().getRfc());
            }
        }

        return strNomFilePdf;
    }

    /**
     * Genera el nombre del xml
     */
    private String getNombreFileXml(ERP_MapeoFormato mapeo) {
        if (this.strPatronNomXml == null) {
            String strFechaDocTmp = strFechaCFDI;
            if (mapeo.getIntTipoComp() == 1) {
                strPatronNomXml = mapeo.getStrNomXML("FACTURA");
            }
            if (mapeo.getIntTipoComp() == 5) {
                strPatronNomXml = mapeo.getStrNomXML("NCREDITO");
            }
            if (mapeo.getIntTipoComp() == 7) {
                strPatronNomXml = mapeo.getStrNomXML("NOMINA");
                strFechaDocTmp = strFechaNominaGenera;
            }
            if (mapeo.getIntTipoComp() == 8) {
                strPatronNomXml = mapeo.getStrNomXML("NOTACARGO");
            }
            if (mapeo.getIntTipoComp() == 9) {
                strPatronNomXml = mapeo.getStrNomXML("NOTACARGOPROV");
            }
            log.debug("strNomFileXml:" + strNomFileXml);
            log.debug("strPatronNomXml:" + strPatronNomXml);
            log.debug("strNomIniXml:" + strNomIniXml);
            log.debug("intTransaccion:" + intTransaccion);
            log.debug("strNombreReceptor:" + strNombreReceptor);
            strNomFileXml = this.strPatronNomXml.replace("%Transaccion%", this.intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaDocTmp).replace(" ", "_");
            if (strNomFileXml.contains("%RFC%")) {
                strNomFileXml = strNomFileXml.replace("%RFC%", this.objComp.getEmisor().getRfc());
            }
        }

        return strNomFileXml;
    }

    /**
     * Genera el formato de impresion en PDF
     *
     * @param strPath Es el path
     * @param intEMP_TIPOCOMP Es el tipo de comprobante
     * @param intEmpId Es el id de la empresa
     * @param strFAC_NOMFORMATO Es el nombre del formato
     * @return Regresa OK si se genero el formato
     */
    protected String GeneraImpresionPDFJasper(String strPath, int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO) {
        String strResultF;
        log.debug("Generando formato en jasper... FOLIO:" + this.strFolio + " EMP_ID:" + this.intEmpId + " SC_ID:" + this.intSucursal + "");
        //Obtenemos el nombre del CFDI
        ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
        strNomFilePdf = getNombreFilePDF(mapeo).replace("%UUID%", strfolioFiscalUUID);

        //Objeto para generar el jasper
        ProcesoMaster proceso = new ProcesoMaster(oConn, varSesiones);
        proceso.setStrPATHBase(this.strPATHBase);
        proceso.setStrNomFileJasper(strNomFilePdf);
        log.debug("strPATHBase:" + strPATHBase);
        log.debug("strNomFilePdf:" + strNomFilePdf);

        //Obtenemos el formato individual  de CFDI
        String[] lstParamsName = {"doc_folio1", "doc_folio2", "emp_id", "sc_id", "doc_id"};
        String[] lstParamsValue = {this.strFolio, this.strFolio, this.intEmpId + "", this.intSucursal + "", "0"};
        String strNomFormatoCFDI = "NOTA_CARGO";

        //Evaluamos si se usara un formato especial
        strResultF = proceso.doGeneraFormatoJasper(0, strNomFormatoCFDI, "PDF", null,
                lstParamsName,
                lstParamsValue, strPath);
        log.debug("strResultF:" + strResultF);
        return strResultF;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Complementos fiscales">
    private void ObtenerComplementos(ObjectFactory objFactory, Comprobante objComp) {
        //Validamos que apliquen los complementos
        if (this.bolComplementoFiscal) {
            if (this.bolDonataria) {
                // Donatarias
                Donatarias donataria = ObtenNodoDonatarias();
                Complemento complemento = objFactory.createComprobanteComplemento();
                complemento.getAny().add(donataria);
                objComp.setComplemento(complemento);
            }
            if (this.bolNominas) {
                // Nominas
                Nomina nomina = ObtenNodoNominas();
                Complemento complemento = objFactory.createComprobanteComplemento();
                complemento.getAny().add(nomina);
                objComp.setComplemento(complemento);
            }
            if (this.bolImpuestoLocales) {
                //Impuestos Locales
                ImpuestosLocales impuestosLocales = this.ObtenNodoImpuestosLocales(objComp);
                Complemento complemento = objFactory.createComprobanteComplemento();
                complemento.getAny().add(impuestosLocales);
                objComp.setComplemento(complemento);
            }
        }
    }

    /**
     * Regresa el objeto del complemento de nominas
     *
     * @return Regresa el objeto Donataria del complemento del SAT
     */
    protected Nomina ObtenNodoNominas() {
        Core.FirmasElectronicas.complementos.nominas.ObjectFactory factory
                = new Core.FirmasElectronicas.complementos.nominas.ObjectFactory();

        SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMdd");

        //Generamos objetos principales
        Nomina nomina = factory.createNomina();
        Percepciones percepciones = factory.createNominaPercepciones();
        Deducciones deducciones = factory.createNominaDeducciones();

        //Definimos campos
        nomina.setVersion("1.1");

        //Consultamos datos globales
        String strSql = "select rhh_nominas.*"
                + ",(select r1.RC_CVE from rhh_regimen_contratacion r1 where r1.RC_ID = rhh_nominas.RC_ID) as tipoRegimen"
                + ",(select r2.DP_DESCRIPCION from rhh_departamento r2 where r2.DP_ID = rhh_nominas.DP_ID) as Departamento"
                + ",(select r3.RHP_NOMBRE from rhh_perfil_puesto r3 where r3.RHP_ID = rhh_empleados.RHP_ID) as Puesto"
                + ",EMP_CURP,EMP_NO_SEG,EMP_CLABE,EMP_FECHA_INICIO_REL_LABORAL,EMP_BANCO,EMP_TIPO_CONTRATO,EMP_TIPO_JORNADA,"
                + "EMP_PERIODICIDAD_PAGO,EMP_SALARIO_DIARIO,EMP_SALARIO_INTEGRADO,RP_ID"
                + " from rhh_nominas,rhh_empleados "
                + " where "
                + " rhh_nominas.EMP_NUM= rhh_empleados.EMP_NUM "
                + " AND NOM_ANULADA = 0 and NOM_SE_TIMBRO = 0 and NOM_ID = " + this.intTransaccion;
        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (!rs.getString("NOM_REGISTRO_PATRONAL").isEmpty()) {
                    nomina.setRegistroPatronal(rs.getString("NOM_REGISTRO_PATRONAL"));
                }
                nomina.setNumEmpleado(rs.getString("EMP_NUM"));
                nomina.setCURP(rs.getString("EMP_CURP"));
                nomina.setTipoRegimen(rs.getInt("tipoRegimen"));
                if (!rs.getString("EMP_NO_SEG").isEmpty()) {
                    nomina.setNumSeguridadSocial(rs.getString("EMP_NO_SEG"));
                }
                //Fechas
                Date dateTmp = null;
                try {
                    dateTmp = formateaDate.parse(rs.getString("NOM_FECHA"));
                    nomina.setFechaPago(dateTmp);
                    dateTmp = formateaDate.parse(rs.getString("NOM_FECHA_INICIAL_PAGO"));
                    nomina.setFechaInicialPago(dateTmp);
                    dateTmp = formateaDate.parse(rs.getString("NOM_FECHA_FINAL_PAGO"));
                    nomina.setFechaFinalPago(dateTmp);
                } catch (ParseException ex) {
                    log.error(ex);
                }

                //nomina.setNumDiasPagados(new BigDecimal(rs.getDouble("NOM_NUM_DIAS_PAGADOS")));
                nomina.setNumDiasPagados(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_NUM_DIAS_PAGADOS"), 6).replace(",", "")));

                if (rs.getString("Departamento") != null) {
                    nomina.setDepartamento(rs.getString("Departamento"));
                }
                try {
                    if (!rs.getString("EMP_CLABE").isEmpty()) {
                        nomina.setCLABE(rs.getString("EMP_CLABE"));
                    }
                } catch (NumberFormatException ex) {
                    log.error(" " + ex.getMessage());
                }
                if (!rs.getString("EMP_FECHA_INICIO_REL_LABORAL").isEmpty()) {
                    try {
                        dateTmp = formateaDate.parse(rs.getString("EMP_FECHA_INICIO_REL_LABORAL"));
                        nomina.setFechaInicioRelLaboral(dateTmp);
                    } catch (ParseException ex) {
                        log.error(ex);
                    }
                }
                if (rs.getString("EMP_BANCO") != null) {
                    if (!rs.getString("EMP_BANCO").trim().isEmpty()) {
                        nomina.setBanco(rs.getString("EMP_BANCO"));
                    }
                }
                if (rs.getString("Puesto") != null) {
                    nomina.setPuesto(rs.getString("Puesto"));
                }
                if (rs.getString("EMP_TIPO_CONTRATO") != null) {
                    nomina.setTipoContrato(rs.getString("EMP_TIPO_CONTRATO"));
                }
                if (rs.getString("EMP_TIPO_JORNADA") != null) {
                    nomina.setTipoJornada(rs.getString("EMP_TIPO_JORNADA"));
                }
                if (rs.getString("EMP_PERIODICIDAD_PAGO") != null) {
                    nomina.setPeriodicidadPago(rs.getString("EMP_PERIODICIDAD_PAGO"));
                }
                if (rs.getDouble("EMP_SALARIO_DIARIO") > 0) {
                    nomina.setSalarioBaseCotApor(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("EMP_SALARIO_DIARIO"), 6).replace(",", "")));
                }
                if (rs.getInt("RP_ID") > 0) {
                    nomina.setRiesgoPuesto(rs.getInt("RP_ID"));
                }
                //Antiguedad
                nomina.setAntiguedad(rs.getInt("NOM_ANTIGUEDAD"));

                if (rs.getDouble("EMP_SALARIO_INTEGRADO") > 0) {
                    nomina.setSalarioDiarioIntegrado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("EMP_SALARIO_INTEGRADO"), 6).replace(",", "")));
                }
                //Evaluamos las incapacidades
                if (rs.getInt("NOM_DIAS_INCAPACIDAD") > 0) {
                    Incapacidad incapacidad = factory.createNominaIncapacidad();
                    incapacidad.setDiasIncapacidad(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_DIAS_INCAPACIDAD"), 6).replace(",", "")));
                    incapacidad.setTipoIncapacidad(rs.getInt("TI_ID"));
                    incapacidad.setDescuento(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_INCAPACIDAD_DESCUENTO"), 6).replace(",", "")));
                    nomina.getIncapacidad().add(incapacidad);
                }

                //Evaluamos las horas extras
                if (rs.getInt("NOM_HORA_EXTRA_DIAS1") > 0) {
                    HorasExtra horasExtra = factory.createNominaHorasExtra();
                    horasExtra.setDias(rs.getInt("NOM_HORA_EXTRA_DIAS1"));
                    horasExtra.setHorasExtra(rs.getInt("NOM_HORA_EXTRA_HORAS1"));
                    horasExtra.setTipoHoras("Dobles");
                    horasExtra.setImportePagado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_HORA_EXTRA_IMPORTE1"), 6).replace(",", "")));
                    nomina.getHorasExtra().add(horasExtra);
                }
                if (rs.getInt("NOM_HORA_EXTRA_DIAS2") > 0) {
                    HorasExtra horasExtra = factory.createNominaHorasExtra();
                    horasExtra.setDias(rs.getInt("NOM_HORA_EXTRA_DIAS2"));
                    horasExtra.setHorasExtra(rs.getInt("NOM_HORA_EXTRA_HORAS2"));
                    horasExtra.setTipoHoras("Triples");
                    horasExtra.setImportePagado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_HORA_EXTRA_IMPORTE2"), 6).replace(",", "")));
                    nomina.getHorasExtra().add(horasExtra);
                }
            }
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        //Obtenemos los conceptos
        double dblPercepcionesGravadas = 0;
        double dblPercepcionesExentas = 0;
        double dblDeduccionesGravadas = 0;
        double dblDeduccionesExentas = 0;
        boolean bolHayDeducciones = false;
        boolean bolHayPercepciones = false;
        strSql = "select "
                + " 	n.NOMD_ID, \n"
                + "	n.NOM_ID, \n"
                + "	n.TP_ID, \n"
                + "(select p1.TP_CVE from rhh_tipo_percepcion p1 where p1.TP_ID = n.TP_ID) as cve_tipo_percep,\n"
                + "(select p2.TD_CVE from rhh_tipo_deduccion  p2 where p2.TD_ID = n.TD_ID) as cve_tipo_deducc,\n"
                + "	n.TI_ID, \n"
                + "	n.TD_ID, \n"
                + "	n.NOMD_CANTIDAD, \n"
                + "	n.NOMD_UNITARIO, \n"
                + "	n.NOMD_GRAVADO, \n"
                + "	n.NOMD_IMPORTE_GRAVADO, \n"
                + "	n.NOMD_IMPORTE_EXENTO, \n"
                + "	n.PERC_ID, \n"
                + " (select e1.PERC_CVE from rhh_percepciones e1 where e1.PERC_ID = n.PERC_ID ) as cve_percep,\n"
                + "(select e2.PERC_DESCRIPCION from rhh_percepciones e2 where e2.PERC_ID = n.PERC_ID ) as con_percep,\n"
                + "(select d1.DEDU_CVE from rhh_deducciones d1 where d1.DEDU_ID = n.DEDU_ID ) as cve_deduc,\n"
                + "(select d2.DEDU_DESCRIPCION from rhh_deducciones d2 where d2.DEDU_ID = n.DEDU_ID ) as con_deduc,"
                + "	n.DEDU_ID "
                + " from rhh_nominas_deta n "
                + " where "
                + " n.NOM_ID = " + this.intTransaccion;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getInt("TP_ID") > 0) {
                    //Percepciones
                    Percepcion percepcion = factory.createNominaPercepcionesPercepcion();
                    percepcion.setClave(rs.getString("cve_percep"));
                    percepcion.setTipoPercepcion(rs.getString("cve_tipo_percep"));
                    percepcion.setConcepto(rs.getString("con_percep"));
                    double dblGravado = rs.getDouble("NOMD_IMPORTE_GRAVADO");
                    double dblExento = rs.getDouble("NOMD_IMPORTE_EXENTO");
                    //Cambio desglose exento y gravado 02/10/2015
                    dblPercepcionesGravadas += dblGravado;
                    dblPercepcionesExentas += dblExento;

//               if (rs.getInt("NOMD_GRAVADO") == 1) {
//                  dblPercepcionesGravadas += rs.getDouble("NOMD_UNITARIO");
//                  dblGravado = rs.getDouble("NOMD_UNITARIO");
//               } else {
//                  dblPercepcionesExentas += rs.getDouble("NOMD_UNITARIO");
//                  dblExento = rs.getDouble("NOMD_UNITARIO");
//               }
                    percepcion.setImporteGravado(new BigDecimal(NumberString.FormatearDecimal(dblGravado, 6).replace(",", "")));
                    percepcion.setImporteExento(new BigDecimal(NumberString.FormatearDecimal(dblExento, 6).replace(",", "")));

                    percepciones.getPercepcion().add(percepcion);
                    bolHayPercepciones = true;
                } else {
                    //Deducciones
                    Deduccion deduccion = factory.createNominaDeduccionesDeduccion();
                    deduccion.setClave(rs.getString("cve_deduc"));
                    deduccion.setTipoDeduccion(rs.getString("cve_tipo_deducc"));
                    deduccion.setConcepto(rs.getString("con_deduc"));

                    double dblGravado = rs.getDouble("NOMD_IMPORTE_GRAVADO");
                    double dblExento = rs.getDouble("NOMD_IMPORTE_EXENTO");
                    dblDeduccionesGravadas += dblGravado;
                    dblDeduccionesExentas += dblExento;
                    //Cambio desglose exento y gravado 02/10/2015
//               double dblGravado = 0;
//               double dblExento = 0;
//               if (rs.getInt("NOMD_GRAVADO") == 1) {
//                  dblDeduccionesGravadas += rs.getDouble("NOMD_UNITARIO");
//                  dblGravado = rs.getDouble("NOMD_UNITARIO");
//               } else {
//                  dblDeduccionesExentas += rs.getDouble("NOMD_UNITARIO");
//                  dblExento = rs.getDouble("NOMD_UNITARIO");
//               }
                    deduccion.setImporteGravado(new BigDecimal(NumberString.FormatearDecimal(dblGravado, 6).replace(",", "")));
                    deduccion.setImporteExento(new BigDecimal(NumberString.FormatearDecimal(dblExento, 6).replace(",", "")));
                    deducciones.getDeduccion().add(deduccion);
                    bolHayDeducciones = true;
                }
            }
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
            percepciones.setTotalGravado(new BigDecimal(NumberString.FormatearDecimal(dblPercepcionesGravadas, 6).replace(",", "")));
            percepciones.setTotalExento(new BigDecimal(NumberString.FormatearDecimal(dblPercepcionesExentas, 6).replace(",", "")));
            deducciones.setTotalGravado(new BigDecimal(NumberString.FormatearDecimal(dblDeduccionesGravadas, 6).replace(",", "")));
            deducciones.setTotalExento(new BigDecimal(NumberString.FormatearDecimal(dblDeduccionesExentas, 6).replace(",", "")));

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        //Solo si hay deducciones ponemos el nodo de deducciones.
        if (bolHayDeducciones) {
            nomina.setDeducciones(deducciones);
        }
        //Solo si hay deducciones ponemos el nodo de deducciones.
        if (bolHayPercepciones) {
            nomina.setPercepciones(percepciones);
        }
        return nomina;
    }

    /**
     * Regresa el objeto del complemento de impuestos locales
     *
     * @param objComp Es el comprobante
     * @return Regresa el objeto ImpuestosLocales del complemento del SAT
     */
    protected ImpuestosLocales ObtenNodoImpuestosLocales(Comprobante objComp) {
        Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory factory
                = new Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory();
        ImpuestosLocales impuestosLocales = factory.createImpuestosLocales();
        //Buscamos los datos del impuesto 2
        String strSql = "select FAC_IMPUESTO2,FAC_TASA2,TI_ID2,TI2_NOMBRE from vta_facturas, vta_tasa_impuesto2 "
                + " WHERE vta_facturas.TI_ID2 = vta_tasa_impuesto2.TI2_ID AND "
                + " FAC_ID = " + this.intTransaccion;
        try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //Asignamos los valores a los nodos del complemento
                impuestosLocales.setVersion("1.0");
                impuestosLocales.setTotaldeTraslados(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_IMPUESTO2"), 2).replace(",", "")));
                impuestosLocales.setTotaldeRetenciones(BigDecimal.ZERO);
                //Desglose de traslados
                TrasladosLocales traslados = factory.createImpuestosLocalesTrasladosLocales();
                traslados.setImporte(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_IMPUESTO2"), 2).replace(",", "")));
                traslados.setTasadeTraslado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_TASA2"), 2).replace(",", "")));
                traslados.setImpLocTrasladado(rs.getString("TI2_NOMBRE"));
                impuestosLocales.getRetencionesLocalesAndTrasladosLocales().add(traslados);
            }
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        return impuestosLocales;
    }
    // </editor-fold>
}
