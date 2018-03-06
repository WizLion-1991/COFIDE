//package Core.FirmasElectronicas;
//
//import Core.FirmasElectronicas.Utils.UtilCert;
//import Core.FirmasElectronicas.complementos.impuestoslocales.ImpuestosLocales;
//import Core.FirmasElectronicas.complementos.impuestoslocales.ImpuestosLocales.TrasladosLocales;
//import ERP.ERP_MapeoFormato;
//import ERP.ProcesoMaster;
//import com.mx.siweb.erp.nominas.ver12.COrigenRecurso;
//import com.mx.siweb.erp.nominas.ver12.CTipoNomina;
//import com.mx.siweb.erp.nominas.ver12.Nomina;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Deducciones;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Deducciones.Deduccion;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Emisor;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Emisor.EntidadSNCF;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Incapacidades;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Incapacidades.Incapacidad;
//import com.mx.siweb.erp.nominas.ver12.Nomina.OtrosPagos;
//import com.mx.siweb.erp.nominas.ver12.Nomina.OtrosPagos.OtroPago;
//import com.mx.siweb.erp.nominas.ver12.Nomina.OtrosPagos.OtroPago.SubsidioAlEmpleo;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Percepciones;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Percepciones.JubilacionPensionRetiro;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Percepciones.Percepcion;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Percepciones.Percepcion.HorasExtra;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Percepciones.SeparacionIndemnizacion;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Receptor;
//import com.mx.siweb.erp.nominas.ver12.Nomina.Receptor.SubContratacion;
//import com.mx.siweb.sat.complementos.ine.INE;
//import com.mx.siweb.sat.complementos.ine.INE.Entidad;
//import com.mx.siweb.sat.complementos.ine.INE.Entidad.Contabilidad;
//import com.mx.siweb.sat.complementos.ine.TAmbito;
//import com.mx.siweb.sat.complementos.ine.TClaveEntidad;
//import com.mx.siweb.sat.complementos.ine.TTipoComite;
//import com.mx.siweb.sat.complementos.ine.TTipoProc;
//import com.mx.siweb.sat.complementos.pagos.Pagos;
//import com.mx.siweb.sat.complementos.pagos.Pagos.Pago;
//import com.mx.siweb.sat.complementos.pagos.Pagos.Pago.DoctoRelacionado;
//import com.mx.siweb.sat.facturacion33.CPais;
//import com.mx.siweb.sat.facturacion33.CTipoDeComprobante;
//import com.mx.siweb.sat.facturacion33.CTipoFactor;
//import com.mx.siweb.sat.facturacion33.CUsoCFDI;
//import com.mx.siweb.sat.facturacion33.Comprobante;
//import com.mx.siweb.sat.facturacion33.Comprobante.CfdiRelacionados;
//import com.mx.siweb.sat.facturacion33.Comprobante.CfdiRelacionados.CfdiRelacionado;
//import com.mx.siweb.sat.facturacion33.Comprobante.Complemento;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.CuentaPredial;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Impuestos;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Impuestos.Retenciones;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Impuestos.Traslados;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.InformacionAduanera;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Parte;
//import com.mx.siweb.sat.facturacion33.Comprobante.Conceptos.Concepto.Parte.InformacionAduanera;
//import com.mx.siweb.sat.facturacion33.Comprobante.Emisor;
//import com.mx.siweb.sat.facturacion33.Comprobante.Impuestos;
//import com.mx.siweb.sat.facturacion33.Comprobante.Impuestos.Retenciones;
//import com.mx.siweb.sat.facturacion33.Comprobante.Impuestos.Retenciones.Retencion;
//import com.mx.siweb.sat.facturacion33.Comprobante.Impuestos.Traslados;
//import com.mx.siweb.sat.facturacion33.Comprobante.Impuestos.Traslados.Traslado;
//import com.mx.siweb.sat.facturacion33.Comprobante.Receptor;
//import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
//import comSIWeb.ContextoApt.VariableSession;
//import comSIWeb.Operaciones.Bitacora;
//import comSIWeb.Operaciones.Conexion;
//import comSIWeb.Utilerias.Fechas;
//import comSIWeb.Utilerias.Mail;
//import comSIWeb.Utilerias.NumberString;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.StringWriter;
//import java.math.BigDecimal;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.security.Signature;
//import java.security.SignatureException;
//import java.security.cert.CertificateEncodingException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.logging.Level;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Marshaller;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import org.apache.commons.ssl.Base64;
//import org.apache.logging.log4j.LogManager;
//import org.w3c.dom.Document;
//
//public class SATXml3_3 extends SATXml {
//
//    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SATXml3_3.class.getName());
//    private Comprobante objComp;
//    protected SATXml3_3.PACS_CFDIS enumPAC;
//    protected String strPathConfigPAC;
//    protected byte[] certificadoEmisor;
//    protected byte[] llavePrivadaEmisor;
//    protected String strfolioFiscalUUID;
//    protected String strFechaTimbre;
//    protected String strSelloSAT;
//    protected String strSelloCFD;
//    protected String strNoCertSAT;
//    protected String strPathQR;
//    protected String strPATHBase;
//    protected boolean bolNominas = false;
//    protected boolean bolImpuestoLocales = false;
//    protected boolean bolComplementoINE = false;
//    protected boolean bolRenombrarXml = false;
//    protected String strPatronNomXml;
//    protected String strPatronNomPDF;
//    protected String strNomFilePdf;
//    protected String strNomFileXml;
//    protected String strNomIniXml;
//    protected String strNombreReceptor;
//    protected String strFechaCFDI;
//    protected String strFechaNominaGenera;
//    protected boolean blEmail1;
//    protected boolean blEmail2;
//    protected boolean blEmail3;
//    protected boolean blEmail4;
//    protected boolean blEmail5;
//    protected boolean blEmail6;
//    protected boolean blEmail7;
//    protected boolean blEmail8;
//    protected boolean blEmail9;
//    protected boolean blEmail10;
//    protected boolean bolEsNotaCargo = false;
//    protected boolean bolEsMc = false;
//    protected boolean bolEsNotaCargoProveedor = false;
//    protected boolean bolUsoFormatoJasper = false;
//    static int intSucursal = 0;
//    protected int intEmpId = 0;
//    protected String strSerie;
//    protected String strConfirmacion;
//    protected String strTipoRelacion;
//    protected String strIdFacturaRelacion;
//    protected String strResidenciaFiscal;
//    protected String strNumRegIdTrib;
//    protected String strUsoCFDI;
//    protected String strCuentaPredial;
//    protected String strRfcProvCertif;
//    protected String strTablaDoc;
//    protected String strPrefijoTabla;
//
//    public boolean isBolEsMc() {
//        return bolEsMc;
//    }
//
//    public void setBolEsMc(boolean bolEsMc) {
//        this.bolEsMc = bolEsMc;
//    }
//
//    public boolean isBolComplementoINE() {
//        return bolComplementoINE;
//    }
//
//    public void setBolComplementoINE(boolean bolComplementoINE) {
//        this.bolComplementoINE = bolComplementoINE;
//    }
//
//    public boolean isBolEsNotaCargoProveedor() {
//        return bolEsNotaCargoProveedor;
//    }
//
//    public void setBolEsNotaCargoProveedor(boolean bolEsNotaCargoProveedor) {
//        this.bolEsNotaCargoProveedor = bolEsNotaCargoProveedor;
//    }
//
//    public String getStrPATHBase() {
//        return strPATHBase;
//    }
//
//    public void setStrPATHBase(String strPATHBase) {
//        this.strPATHBase = strPATHBase;
//    }
//
//    public boolean isBolUsoFormatoJasper() {
//        return bolUsoFormatoJasper;
//    }
//
//    public void setBolUsoFormatoJasper(boolean bolUsoFormatoJasper) {
//        this.bolUsoFormatoJasper = bolUsoFormatoJasper;
//    }
//
//    public boolean isBolEsNotaCargo() {
//        return bolEsNotaCargo;
//    }
//
//    public void setBolEsNotaCargo(boolean bolEsNotaCargo) {
//        this.bolEsNotaCargo = bolEsNotaCargo;
//    }
//
//    public boolean isBlEmail1() {
//        return blEmail1;
//    }
//
//    public void setBlEmail1(boolean blEmail1) {
//        this.blEmail1 = blEmail1;
//    }
//
//    public boolean isBlEmail2() {
//        return blEmail2;
//    }
//
//    public void setBlEmail2(boolean blEmail2) {
//        this.blEmail2 = blEmail2;
//    }
//
//    public boolean isBlEmail3() {
//        return blEmail3;
//    }
//
//    public void setBlEmail3(boolean blEmail3) {
//        this.blEmail3 = blEmail3;
//    }
//
//    public boolean isBlEmail4() {
//        return blEmail4;
//    }
//
//    public void setBlEmail4(boolean blEmail4) {
//        this.blEmail4 = blEmail4;
//    }
//
//    public boolean isBlEmail5() {
//        return blEmail5;
//    }
//
//    public void setBlEmail5(boolean blEmail5) {
//        this.blEmail5 = blEmail5;
//    }
//
//    public boolean isBlEmail6() {
//        return blEmail6;
//    }
//
//    public void setBlEmail6(boolean blEmail6) {
//        this.blEmail6 = blEmail6;
//    }
//
//    public boolean isBlEmail7() {
//        return blEmail7;
//    }
//
//    public void setBlEmail7(boolean blEmail7) {
//        this.blEmail7 = blEmail7;
//    }
//
//    public boolean isBlEmail8() {
//        return blEmail8;
//    }
//
//    public void setBlEmail8(boolean blEmail8) {
//        this.blEmail8 = blEmail8;
//    }
//
//    public boolean isBlEmail9() {
//        return blEmail9;
//    }
//
//    public void setBlEmail9(boolean blEmail9) {
//        this.blEmail9 = blEmail9;
//    }
//
//    public boolean isBlEmail10() {
//        return blEmail10;
//    }
//
//    public void setBlEmail10(boolean blEmail10) {
//        this.blEmail10 = blEmail10;
//    }
//
//    public String getStrPatronNomXml() {
//        return strPatronNomXml;
//    }
//
//    public void setStrPatronNomXml(String strPatronNomXml) {
//        this.strPatronNomXml = strPatronNomXml;
//    }
//
//    public String getStrPatronNomPDF() {
//        return strPatronNomPDF;
//    }
//
//    public void setStrPatronNomPDF(String strPatronNomPDF) {
//        this.strPatronNomPDF = strPatronNomPDF;
//    }
//
//    public String getStrNomIniXml() {
//        return strNomIniXml;
//    }
//
//    public void setStrNomIniXml(String strNomIniXml) {
//        this.strNomIniXml = strNomIniXml;
//    }
//
//    public boolean isBolImpuestoLocales() {
//        return bolImpuestoLocales;
//    }
//
//    public void setBolImpuestoLocales(boolean bolImpuestoLocales) {
//        this.bolImpuestoLocales = bolImpuestoLocales;
//    }
//
//    public boolean isBolNominas() {
//        return bolNominas;
//    }
//
//    public void setBolNominas(boolean bolNominas) {
//        this.bolNominas = bolNominas;
//    }
//
//    public SATXml3_3.PACS_CFDIS getEnumPAC() {
//        return enumPAC;
//    }
//
//    public void setEnumPAC(SATXml3_3.PACS_CFDIS enumPAC) {
//        this.enumPAC = enumPAC;
//    }
//
//    public String getStrPathConfigPAC() {
//        return strPathConfigPAC;
//    }
//
//    public void setStrPathConfigPAC(String strPathConfigPAC) {
//        this.strPathConfigPAC = strPathConfigPAC;
//    }
//
//    public String getStrNomFilePdf() {
//        return strNomFilePdf;
//    }
//
//    public void setStrNomFilePdf(String strNomFilePdf) {
//        this.strNomFilePdf = strNomFilePdf;
//    }
//
//    public String getStrNomFileXml() {
//        return strNomFileXml;
//    }
//
//    public void setStrNomFileXml(String strNomFileXml) {
//        this.strNomFileXml = strNomFileXml;
//    }
//
//    public SATXml3_3(Comprobante objComp, String strSerie, String strConfirmacion, String strTipoRelacion, String strIdFacturaRelacion, String strResidenciaFiscal, String strNumRegIdTrib, String strUsoCFDI, String strCuentaPredial, int intTransaccion, String strPath, String NoAprobacion, String FechaAprobacion, String NoSerieCert, String strPathKey, String strPassKey, VariableSession varSesiones, Conexion oConn) {
//        super(intTransaccion, strPath, NoAprobacion, FechaAprobacion, NoSerieCert, strPathKey, strPassKey, varSesiones, oConn);
//        this.objComp = objComp;
//        this.strSerie = strSerie;
//        this.strConfirmacion = strConfirmacion;
//        this.strTipoRelacion = strTipoRelacion;
//        this.strIdFacturaRelacion = strIdFacturaRelacion;
//        this.strResidenciaFiscal = strResidenciaFiscal;
//        this.strNumRegIdTrib = strNumRegIdTrib;
//        this.strUsoCFDI = strUsoCFDI;
//        this.strCuentaPredial = strCuentaPredial;
//    }
//
//    public SATXml3_3(int intTransaccion, String strPath, String NoAprobacion, String FechaAprobacion, String NoSerieCert, String strPathKey, String strPassKey, VariableSession varSesiones, Conexion oConn) {
//        super(intTransaccion, strPath, NoAprobacion, FechaAprobacion, NoSerieCert, strPathKey, strPassKey, varSesiones, oConn);
//        enumPAC = SATXml3_3.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
//        strPathConfigPAC = "";
//        bolUsaTrxComoFolio = false;
//        log.debug("intTransaccion:" + intTransaccion);
//
//        blEmail1 = true;
//        blEmail2 = true;
//        blEmail3 = true;
//        blEmail4 = true;
//        blEmail5 = true;
//        blEmail6 = true;
//        blEmail7 = true;
//        blEmail8 = true;
//        blEmail9 = true;
//        blEmail10 = true;
//        bolUsoFormatoJasper = true;
//    }
//
//    public SATXml3_3() {
//        enumPAC = SATXml3_3.PACS_CFDIS.FACTURA_EN_SEGUNDOS;
//        strPathConfigPAC = "";
//        bolUsaTrxComoFolio = false;
//        blEmail1 = true;
//        blEmail2 = true;
//        blEmail3 = true;
//        blEmail4 = true;
//        blEmail5 = true;
//        blEmail6 = true;
//        blEmail7 = true;
//        blEmail8 = true;
//        blEmail9 = true;
//        blEmail10 = true;
//        bolUsoFormatoJasper = true;
//    }
//
//    public String GeneraXml() {
//        log.debug("Inicializamos valores....");
//        strFolio = "";
//        strSerie = "";
//        strConfirmacion = "";
//        strTipoRelacion = "";
//        strIdFacturaRelacion = "";
//        strResidenciaFiscal = "";
//        strNumRegIdTrib = "";
//        strUsoCFDI = "";
//        strCuentaPredial = "";
//        String strRes = "OK";
//        BigDecimal BGSubtotal = null;
//        BigDecimal BGDescuento = null;
//        BigDecimal BGTotal = null;
//        BigDecimal BGImpuesto1 = null;
//        BigDecimal BGImpuestoRetenido1 = null;
//        BigDecimal BGImpuestoRetenido2 = null;
//        BigDecimal BGTasa1 = null;
//        String strTipoComprobante = "I";
//
//        if (bolEsNc) {
//            strTipoComprobante = "E";
//        }
//        if (bolNominas) {
//            strTipoComprobante = "N";
//            bolComplementoFiscal = true;
//        }
//        if (bolEsMc) {
//            bolComplementoFiscal = true;
//            strTipoComprobante = "P";
//        }
//        if (isBolEsNotaCargoProveedor()) {
//            strTipoComprobante = "E";
//        }
//        boolean bolEsFactura = false;
//        intSucursal = 0;
//        intEmpId = 0;
//        int intCliente = 0;
//        int intClienteFinal = 0;
//
//        strNombreReceptor = "";
//        String strRFCReceptor = "";
//        String strCalleReceptor = "";
//        String strColoniaReceptor = "";
//        String strLocalidadReceptor = "";
//        String strMunicipioReceptor = "";
//        String strCPReceptor = "";
//        String strEdoReceptor = "";
//        String strNumero = "";
//        String strNumeroInt = "";
//        strFechaCFDI = "";
//        strFechaNominaGenera = "";
//
//        String strNombreEmisor = "";
//        String strRFC = "";
//        String strCalle = "";
//        String strNumeroEmisor = "";
//        String strNumeroIntEmisor = "";
//        String strCP = "";
//        String strColonia = "";
//        String strEdo = "";
//        String strLocalidad = "";
//        String strMunicipio = "";
//        String strPais = "";
//        String strFechaFactura = "";
//        String strHoraFactura = "";
//        int intUsoIEPS = 0;
//        BigDecimal BGTasaIEPS = null;
//        BigDecimal BGImporteIEPS = null;
//        String strMailCte = "";
//        String strMailCte2 = "";
//        String strMailCte3 = "";
//        String strMailCte4 = "";
//        String strMailCte5 = "";
//        String strMailCte6 = "";
//        String strMailCte7 = "";
//        String strMailCte8 = "";
//        String strMailCte9 = "";
//        String strMailCte10 = "";
//        int intFolio = 0;
//        int intClienteEnviaMail = 1;
//        String strFAC_NOMFORMATO = "";
//
//        String strFAC_CONDPAGO = "";
//        String strFAC_FORMADEPAGO = "";
//        String strFAC_METODODEPAGO = "";
//        String strFAC_NUMCTA = "";
//        String strRegimenFiscal = "";
//        String strCalleSuc = "";
//        String strColoniaSuc = "";
//        String strLocalidadSuc = "";
//        String strMunicipioSuc = "";
//        String strCPSuc = "";
//        String strEdoSuc = "";
//        String strPaisSuc = "";
//        String strExpedidoEn = "";
//        String strMoneda = "";
//        BigDecimal BGTasaPeso = null;
//        String strConceptoNomina = "";
//        ArrayList<TrasladosIVA> lstTrasladosIVA = new ArrayList();
//        double dblFactorIsr = 0.0D;
//
//        log.debug("Consultamos datos de la base de datos....");
//        String strSql;
//        if (!bolEsNc) {
//            if (bolNominas) {
//                String strSql = "SELECT NOM_FOLIO,NOM_FECHA,NOM_PERCEPCIONES,NOM_DEDUCCIONES,NOM_PERCEPCION_TOTAL, NOM_CONDPAGO,NOM_FORMADEPAGO,NOM_METODODEPAGO, SC_ID,EMP_ID,EMP_NUM,NOM_HORA, NOM_NOMFORMATO,NOM_ISR_RETENIDO,  NOM_NUMCUENTA,NOM_MONEDA,NOM_CONCEPTO,NOM_REGIMENFISCAL,NOM_TASAPESO,NOM_CONFIRMACION, NOM_TREL_CLAVE,NOM_ID_NOM_RELACION,NOM_RESIDENCIA_FISCAL,NOM_NUM_REG_ID_TRIB, NOM_USO_CFDI FROM rhh_nominas WHERE NOM_ID = " + intTransaccion;
//
//                try {
//                    ResultSet rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strFolio = rs.getString("NOM_FOLIO");
//
//                        strFechaFactura = fecha.getFechaActual();
//                        strFechaNominaGenera = rs.getString("NOM_FECHA");
//                        strHoraFactura = fecha.getHoraActualHHMMSS();
//                        BGSubtotal = rs.getBigDecimal("NOM_PERCEPCIONES");
//                        BGDescuento = rs.getBigDecimal("NOM_DEDUCCIONES");
//                        BGTotal = rs.getBigDecimal("NOM_PERCEPCION_TOTAL");
//                        intSucursal = rs.getInt("SC_ID");
//                        BGImpuestoRetenido1 = rs.getBigDecimal("NOM_ISR_RETENIDO");
//                        intCliente = rs.getInt("EMP_NUM");
//                        intEmpId = rs.getInt("EMP_ID");
//                        strFAC_NOMFORMATO = rs.getString("NOM_NOMFORMATO");
//                        strFAC_FORMADEPAGO = rs.getString("NOM_FORMADEPAGO");
//
//                        strFAC_CONDPAGO = rs.getString("NOM_CONDPAGO");
//                        strFAC_METODODEPAGO = rs.getString("NOM_METODODEPAGO");
//                        strFAC_NUMCTA = rs.getString("NOM_NUMCUENTA");
//                        strConceptoNomina = rs.getString("NOM_CONCEPTO");
//                        strMoneda = rs.getInt("NOM_MONEDA") + "";
//                        strRegimenFiscal = rs.getString("NOM_REGIMENFISCAL");
//                        if (!strFolio.isEmpty()) {
//                            try {
//                                intFolio = Integer.valueOf(strFolio).intValue();
//                            } catch (NumberFormatException ex) {
//                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//                                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getDoc", oConn);
//                                strRes = "ERROR:" + ex.getMessage();
//                            }
//                        }
//                        BGTasaPeso = rs.getBigDecimal("NOM_TASAPESO");
//                        if (BGTasaPeso.doubleValue() == 0.0D) {
//                            BGTasaPeso = new BigDecimal("1");
//                        }
//                        strConfirmacion = rs.getString("NOM_CONFIRMACION");
//                        strTipoRelacion = rs.getString("NOM_TREL_CLAVE");
//                        strIdFacturaRelacion = rs.getString("NOM_ID_NOM_RELACION");
//                        strResidenciaFiscal = rs.getString("NOM_RESIDENCIA_FISCAL");
//                        strNumRegIdTrib = rs.getString("NOM_NUM_REG_ID_TRIB");
//                        strUsoCFDI = rs.getString("NOM_USO_CFDI");
//                    }
//                    if (rs.getStatement() != null) {
//                    }
//
//                    rs.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//                    bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getClie", oConn);
//                    strRes = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage();
//                }
//            } else if (bolEsNotaCargo) {
//                strTablaDoc = "vta_notas_cargos";
//                strPrefijoTabla = "NCA_";
//
//                String strSql = "SELECT NCA_FOLIO_C,NCA_FECHA,NCA_IMPORTE,NCA_DESCUENTO,DFA_ID, NCA_CONDPAGO,NCA_FORMADEPAGO,NCA_METODODEPAGO, NCA_TOTAL,SC_ID,EMP_ID,NCA_IMPUESTO1,NCA_RETIVA,NCA_RETISR,NCA_TASA1,NCA_TASA2,CT_ID,NCA_HORA, NCA_SERIE,NCA_NOAPROB,NCA_FECHAAPROB,NCA_NOMFORMATO,  NCA_USO_IEPS,NCA_TASA_IEPS,NCA_IMPORTE_IEPS,NCA_NUMCUENTA,NCA_MONEDA,NCA_REGIMENFISCAL,NCA_TASAPESO,NCA_CONFIRMACION,NCA_TREL_CLAVE,NCA_RESIDENCIA_FISCAL,NCA_NUM_REG_ID_TRIB,  NCA_USO_CFDI FROM vta_notas_cargos WHERE NCA_ID = " + intTransaccion;
//
//                try {
//                    ResultSet rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strFolio = rs.getString("NCA_FOLIO_C");
//                        if (bolUsaTrxComoFolio) {
//                            strFolio = (intTransaccion + "");
//                        }
//                        strFechaFactura = rs.getString("NCA_FECHA");
//                        BGSubtotal = rs.getBigDecimal("NCA_IMPORTE");
//                        BGDescuento = rs.getBigDecimal("NCA_DESCUENTO");
//                        BGTotal = rs.getBigDecimal("NCA_TOTAL");
//                        intSucursal = rs.getInt("SC_ID");
//                        BGImpuesto1 = rs.getBigDecimal("NCA_IMPUESTO1");
//                        BGImpuestoRetenido1 = rs.getBigDecimal("NCA_RETIVA");
//                        BGImpuestoRetenido2 = rs.getBigDecimal("NCA_RETISR");
//                        BGTasa1 = rs.getBigDecimal("NCA_TASA1");
//                        intCliente = rs.getInt("CT_ID");
//                        intClienteFinal = rs.getInt("DFA_ID");
//                        intEmpId = rs.getInt("EMP_ID");
//                        strFAC_NOMFORMATO = rs.getString("NCA_NOMFORMATO");
//                        strHoraFactura = rs.getString("NCA_HORA");
//                        intUsoIEPS = rs.getInt("NCA_USO_IEPS");
//                        BGTasaIEPS = rs.getBigDecimal("NCA_TASA_IEPS");
//                        BGImporteIEPS = rs.getBigDecimal("NCA_IMPORTE_IEPS");
//                        strFAC_FORMADEPAGO = rs.getString("NCA_FORMADEPAGO");
//                        strFAC_CONDPAGO = rs.getString("NCA_CONDPAGO");
//                        strFAC_METODODEPAGO = rs.getString("NCA_METODODEPAGO");
//                        strFAC_NUMCTA = rs.getString("NCA_NUMCUENTA");
//                        strMoneda = rs.getString("NCA_MONEDA");
//                        strRegimenFiscal = rs.getString("NCA_REGIMENFISCAL");
//                        strSerie = rs.getString("NCA_SERIE");
//                        if (!strFolio.isEmpty()) {
//                            try {
//                                intFolio = Integer.valueOf(strFolio).intValue();
//                            } catch (NumberFormatException ex) {
//                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//                                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getDoc", oConn);
//                                strRes = "ERROR:" + ex.getMessage();
//                            }
//                        }
//                        BGTasaPeso = rs.getBigDecimal("NCA_TASAPESO");
//                        strConfirmacion = rs.getString("NCA_CONFIRMACION");
//                        strTipoRelacion = rs.getString("NCA_TREL_CLAVE");
//                        strIdFacturaRelacion = rs.getString("NCA_ID_NCA_RELACION");
//                        strResidenciaFiscal = rs.getString("NCA_RESIDENCIA_FISCAL");
//                        strNumRegIdTrib = rs.getString("NCA_NUM_REG_ID_TRIB");
//                        strUsoCFDI = rs.getString("NCA_USO_CFDI");
//
//                        if (rs.getDouble("NCA_TASA2") > 0.0D) {
//                            bolComplementoFiscal = true;
//                            bolImpuestoLocales = true;
//                        }
//                    }
//                    if (rs.getStatement() != null) {
//                    }
//
//                    rs.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                    bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getClie", oConn);
//                    strRes = "ERROR:" + ex.getMessage();
//                }
//            } else if (bolEsNotaCargoProveedor) {
//                strTablaDoc = "vta_notas_cargosprov";
//                strPrefijoTabla = "NCA_";
//
//                String strSql = "SELECT NCA_FOLIO_C,NCA_FECHA,NCA_IMPORTE,NCA_DESCUENTO,DFA_ID, NCA_CONDPAGO,NCA_FORMADEPAGO,NCA_METODODEPAGO, NCA_TOTAL,SC_ID,EMP_ID,NCA_IMPUESTO1,NCA_RETIVA,NCA_RETISR,NCA_TASA1,NCA_TASA2,PV_ID,NCA_HORA, NCA_SERIE,NCA_NOAPROB,NCA_FECHAAPROB,NCA_NOMFORMATO,  NCA_USO_IEPS,NCA_TASA_IEPS,NCA_IMPORTE_IEPS,NCA_NUMCUENTA,NCA_MONEDA,NCA_REGIMENFISCAL,NCA_TASAPESO,NCA_CONFIRMACION,NCA_TREL_CLAVE,NCA_ID_NCA_RELACION,NCA_RESIDENCIA_FISCAL,NCA_NUM_REG_ID_TRIB,  NCA_USO_CFDI FROM vta_notas_cargosprov WHERE NCA_ID = " + intTransaccion;
//
//                try {
//                    ResultSet rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strFolio = rs.getString("NCA_FOLIO_C");
//                        if (bolUsaTrxComoFolio) {
//                            strFolio = (intTransaccion + "");
//                        }
//                        strFechaFactura = rs.getString("NCA_FECHA");
//                        BGSubtotal = rs.getBigDecimal("NCA_IMPORTE");
//                        BGDescuento = rs.getBigDecimal("NCA_DESCUENTO");
//                        BGTotal = rs.getBigDecimal("NCA_TOTAL");
//                        intSucursal = rs.getInt("SC_ID");
//                        BGImpuesto1 = rs.getBigDecimal("NCA_IMPUESTO1");
//                        BGImpuestoRetenido1 = rs.getBigDecimal("NCA_RETIVA");
//                        BGImpuestoRetenido2 = rs.getBigDecimal("NCA_RETISR");
//                        BGTasa1 = rs.getBigDecimal("NCA_TASA1");
//                        intCliente = rs.getInt("PV_ID");
//                        intClienteFinal = rs.getInt("DFA_ID");
//                        intEmpId = rs.getInt("EMP_ID");
//                        strFAC_NOMFORMATO = rs.getString("NCA_NOMFORMATO");
//                        strHoraFactura = rs.getString("NCA_HORA");
//                        intUsoIEPS = rs.getInt("NCA_USO_IEPS");
//                        BGTasaIEPS = rs.getBigDecimal("NCA_TASA_IEPS");
//                        BGImporteIEPS = rs.getBigDecimal("NCA_IMPORTE_IEPS");
//                        strFAC_FORMADEPAGO = rs.getString("NCA_FORMADEPAGO");
//                        strFAC_CONDPAGO = rs.getString("NCA_CONDPAGO");
//                        strFAC_METODODEPAGO = rs.getString("NCA_METODODEPAGO");
//                        strFAC_NUMCTA = rs.getString("NCA_NUMCUENTA");
//                        strMoneda = rs.getString("NCA_MONEDA");
//                        strRegimenFiscal = rs.getString("NCA_REGIMENFISCAL");
//                        strSerie = rs.getString("NCA_SERIE");
//                        if (!strFolio.isEmpty()) {
//                            try {
//                                intFolio = Integer.valueOf(strFolio).intValue();
//                            } catch (NumberFormatException ex) {
//                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//                                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getDoc", oConn);
//                                strRes = "ERROR:" + ex.getMessage();
//                            }
//                        }
//                        BGTasaPeso = rs.getBigDecimal("NCA_TASAPESO");
//                        strConfirmacion = rs.getString("NCA_CONFIRMACION");
//                        strTipoRelacion = rs.getString("NCA_TREL_CLAVE");
//                        strIdFacturaRelacion = rs.getString("NCA_ID_NCA_RELACION");
//                        strResidenciaFiscal = rs.getString("NCA_RESIDENCIA_FISCAL");
//                        strNumRegIdTrib = rs.getString("NCA_NUM_REG_ID_TRIB");
//                        strUsoCFDI = rs.getString("NCA_USO_CFDI");
//
//                        if (rs.getDouble("NCA_TASA2") > 0.0D) {
//                            bolComplementoFiscal = true;
//                            bolImpuestoLocales = true;
//                        }
//                    }
//                    if (rs.getStatement() != null) {
//                    }
//
//                    rs.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                    bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getClie", oConn);
//                    strRes = "ERROR:" + ex.getMessage();
//                }
//            } else if (bolEsMc) {
//                strTablaDoc = "vta_mov_cte_mas";
//                strPrefijoTabla = "MCM_";
//
//                log.debug("Consultamos Pago");
//                String strSql = "SELECT MCM_ID , MCM_FECHA , MCM_HORA , MCM_FOLIO_C , MCM_TOTOPER , MCM_METODODEPAGO , MCM_FORMADEPAGO , CT_ID , SC_ID , EMP_ID , MCM_MONEDA , MCM_TASAPESO , MCM_TREL_CLAVE , MCM_ID_FAC_RELACION , MCM_USO_CFDI , MCM_NUM_REG_ID_TRIB , MCM_RESIDENCIA_FISCAL , MCM_CONFIRMACION, MCM_ES_ANTICIPO  FROM vta_mov_cte_mas WHERE MCM_ID=" + intTransaccion + ";";
//
//                log.debug("strSql" + strSql);
//                try {
//                    ResultSet rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strFolio = rs.getString("MCM_FOLIO_C");
//                        strFechaFactura = rs.getString("MCM_FECHA");
//                        BGSubtotal = new BigDecimal(0.0D);
//                        BGTotal = new BigDecimal(0.0D);
//                        intSucursal = rs.getInt("SC_ID");
//                        intCliente = rs.getInt("CT_ID");
//                        intEmpId = rs.getInt("EMP_ID");
//                        strHoraFactura = rs.getString("MCM_HORA");
//                        strFAC_FORMADEPAGO = rs.getString("MCM_FORMADEPAGO");
//                        strFAC_METODODEPAGO = "PPD";
//                        strMoneda = "XXX";
//                        if (!rs.getString("MCM_MONEDA").equals("1")) {
//                            BGTasaPeso = rs.getBigDecimal("MCM_TASAPESO");
//                        }
//                        if (rs.getString("MCM_ES_ANTICIPO").equals("1")) {
//                            strTipoRelacion = "07";
//                        } else {
//                            strTipoRelacion = "";
//                        }
//                        String sqlTwo = "SELECT RF_CLAVE FROM vta_empregfiscal a, view_vta_regimen_fiscal b WHERE a.REGF_ID=b.RF_ID AND a.EMP_ID=" + intEmpId + ";";
//                        ResultSet rsR = oConn.runQuery(sqlTwo, true);
//                        while (rsR.next()) {
//                            strRegimenFiscal = rsR.getString("RF_CLAVE");
//                        }
//                        rsR.close();
//                        strUsoCFDI = "P01";
//                    }
//                    rs.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                    bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getClie", oConn);
//                    strRes = "ERROR:" + ex.getMessage();
//                }
//            } else {
//                strTablaDoc = "vta_facturas";
//                strPrefijoTabla = "FAC_";
//
//                log.debug("aqui consultamos...");
//                String strSql = "SELECT FAC_FOLIO_C,FAC_FECHA,FAC_IMPORTE,FAC_DESCUENTO,DFA_ID, FAC_CONDPAGO,FAC_FORMADEPAGO,FAC_METODODEPAGO, FAC_TOTAL,SC_ID,EMP_ID,FAC_IMPUESTO1,FAC_RETIVA,FAC_RETISR,FAC_TASA1,FAC_TASA2,CT_ID,FAC_HORA, FAC_SERIE,FAC_NOAPROB,FAC_FECHAAPROB,FAC_NOMFORMATO, FAC_TASAPESO, FAC_USO_IEPS,FAC_TASA_IEPS,FAC_IMPORTE_IEPS,FAC_NUMCUENTA,FAC_MONEDA,FAC_REGIMENFISCAL,FAC_INE,FAC_RESIDENCIA_FISCAL,FAC_NUM_REG_ID_TRIB,FAC_ID_FAC_RELACION, FAC_CONFIRMACION,FAC_TREL_CLAVE,FAC_USO_CFDI,FAC_ISR_FACTOR  FROM vta_facturas WHERE FAC_ID = " + intTransaccion;
//
//                log.debug("strSql" + strSql);
//                try {
//                    ResultSet rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strFolio = rs.getString("FAC_FOLIO_C");
//                        if (bolUsaTrxComoFolio) {
//                            strFolio = (intTransaccion + "");
//                        }
//                        bolEsFactura = true;
//                        strFechaFactura = rs.getString("FAC_FECHA");
//                        BGSubtotal = rs.getBigDecimal("FAC_IMPORTE");
//                        BGDescuento = rs.getBigDecimal("FAC_DESCUENTO");
//                        BGTotal = rs.getBigDecimal("FAC_TOTAL");
//                        intSucursal = rs.getInt("SC_ID");
//                        BGImpuesto1 = rs.getBigDecimal("FAC_IMPUESTO1");
//                        BGImpuestoRetenido1 = rs.getBigDecimal("FAC_RETIVA");
//                        BGImpuestoRetenido2 = rs.getBigDecimal("FAC_RETISR");
//                        BGTasa1 = rs.getBigDecimal("FAC_TASA1");
//                        dblFactorIsr = rs.getDouble("FAC_ISR_FACTOR");
//                        intCliente = rs.getInt("CT_ID");
//                        intClienteFinal = rs.getInt("DFA_ID");
//                        intEmpId = rs.getInt("EMP_ID");
//                        strFAC_NOMFORMATO = rs.getString("FAC_NOMFORMATO");
//                        strHoraFactura = rs.getString("FAC_HORA");
//                        intUsoIEPS = rs.getInt("FAC_USO_IEPS");
//                        BGTasaIEPS = rs.getBigDecimal("FAC_TASA_IEPS");
//                        BGImporteIEPS = rs.getBigDecimal("FAC_IMPORTE_IEPS");
//                        strFAC_FORMADEPAGO = rs.getString("FAC_FORMADEPAGO");
//                        strFAC_CONDPAGO = rs.getString("FAC_CONDPAGO");
//                        strFAC_METODODEPAGO = rs.getString("FAC_METODODEPAGO");
//                        strFAC_NUMCTA = rs.getString("FAC_NUMCUENTA");
//                        strMoneda = rs.getString("FAC_MONEDA");
//                        BGTasaPeso = rs.getBigDecimal("FAC_TASAPESO");
//                        strSerie = rs.getString("FAC_SERIE");
//                        strConfirmacion = rs.getString("FAC_CONFIRMACION");
//                        strTipoRelacion = rs.getString("FAC_TREL_CLAVE");
//                        strIdFacturaRelacion = rs.getString("FAC_ID_FAC_RELACION");
//                        strRegimenFiscal = rs.getString("FAC_REGIMENFISCAL");
//                        strResidenciaFiscal = rs.getString("FAC_RESIDENCIA_FISCAL");
//                        strNumRegIdTrib = rs.getString("FAC_NUM_REG_ID_TRIB");
//                        strUsoCFDI = rs.getString("FAC_USO_CFDI");
//                        if (!strFolio.isEmpty()) {
//                            try {
//                                intFolio = Integer.valueOf(strFolio).intValue();
//                            } catch (NumberFormatException ex) {
//                                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//                                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getDoc", oConn);
//                                strRes = "ERROR:" + ex.getMessage();
//                            }
//                        }
//
//                        if (rs.getDouble("FAC_TASA2") > 0.0D) {
//                            bolComplementoFiscal = true;
//                            bolImpuestoLocales = true;
//                        }
//
//                        if (rs.getInt("FAC_INE") == 1) {
//                            bolComplementoFiscal = true;
//                            bolComplementoINE = true;
//                        }
//                    }
//                    rs.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                    bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getClie", oConn);
//                    strRes = "ERROR:" + ex.getMessage();
//                }
//            }
//        } else {
//            strTablaDoc = "vta_ncredito";
//            strPrefijoTabla = "NC_";
//
//            strSql = "SELECT NC_FOLIO_C,NC_FECHA,NC_IMPORTE,NC_DESCUENTO, NC_CONDPAGO,NC_FORMADEPAGO,NC_METODODEPAGO, NC_TOTAL,SC_ID,EMP_ID,NC_IMPUESTO1,NC_RETIVA,NC_RETISR,NC_TASA1,CT_ID,NC_HORA, NC_SERIE,NC_NOAPROB,NC_FECHAAPROB,NC_NOMFORMATO,  NC_USO_IEPS,NC_TASA_IEPS,NC_IMPORTE_IEPS,NC_MONEDA,NC_NUMCUENTA,NC_REGIMENFISCAL,NC_TASAPESO,NC_CONFIRMACION,NC_TREL_CLAVE,NC_ID_NC_RELACION,NC_RESIDENCIA_FISCAL,NC_NUM_REG_ID_TRIB, NC_USO_CFDI  FROM vta_ncredito WHERE NC_ID = " + intTransaccion;
//
//            try {
//                ResultSet rs = oConn.runQuery(strSql, true);
//                while (rs.next()) {
//                    strFolio = rs.getString("NC_FOLIO_C");
//                    if (bolUsaTrxComoFolio) {
//                        strFolio = (intTransaccion + "");
//                    }
//                    strFechaFactura = rs.getString("NC_FECHA");
//                    BGSubtotal = rs.getBigDecimal("NC_IMPORTE");
//                    BGDescuento = rs.getBigDecimal("NC_DESCUENTO");
//                    BGTotal = rs.getBigDecimal("NC_TOTAL");
//                    intSucursal = rs.getInt("SC_ID");
//                    BGImpuesto1 = rs.getBigDecimal("NC_IMPUESTO1");
//                    BGImpuestoRetenido1 = rs.getBigDecimal("NC_RETIVA");
//                    BGImpuestoRetenido2 = rs.getBigDecimal("NC_RETISR");
//                    BGTasa1 = rs.getBigDecimal("NC_TASA1");
//                    intCliente = rs.getInt("CT_ID");
//                    intEmpId = rs.getInt("EMP_ID");
//                    strFAC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
//                    strHoraFactura = rs.getString("NC_HORA");
//                    intUsoIEPS = rs.getInt("NC_USO_IEPS");
//                    BGTasaIEPS = rs.getBigDecimal("NC_TASA_IEPS");
//                    BGImporteIEPS = rs.getBigDecimal("NC_IMPORTE_IEPS");
//                    strFAC_FORMADEPAGO = rs.getString("NC_FORMADEPAGO");
//                    strFAC_CONDPAGO = rs.getString("NC_CONDPAGO");
//                    strFAC_METODODEPAGO = rs.getString("NC_METODODEPAGO");
//                    if (strFAC_METODODEPAGO.isEmpty()) {
//                        strFAC_METODODEPAGO = "NO IDENTIFICADO";
//                    }
//                    strFAC_NUMCTA = rs.getString("NC_NUMCUENTA");
//                    strMoneda = rs.getString("NC_MONEDA");
//                    strRegimenFiscal = rs.getString("NC_REGIMENFISCAL");
//                    strSerie = rs.getString("NC_SERIE");
//
//                    if (!strFolio.isEmpty()) {
//                        try {
//                            intFolio = Integer.valueOf(strFolio).intValue();
//                        } catch (NumberFormatException ex) {
//                            log.error(ex.getMessage());
//                            bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getDoc", oConn);
//                            strRes = "ERROR:" + ex.getMessage();
//                        }
//                    }
//                    BGTasaPeso = rs.getBigDecimal("NC_TASAPESO");
//                    strConfirmacion = rs.getString("NC_CONFIRMACION");
//                    strTipoRelacion = rs.getString("NC_TREL_CLAVE");
//                    strIdFacturaRelacion = rs.getString("NC_ID_NC_RELACION");
//                    strResidenciaFiscal = rs.getString("NC_RESIDENCIA_FISCAL");
//                    strNumRegIdTrib = rs.getString("NC_NUM_REG_ID_TRIB");
//                    strUsoCFDI = rs.getString("NC_USO_CFDI");
//                }
//                if (rs.getStatement() != null) {
//                }
//
//                rs.close();
//            } catch (SQLException ex) {
//                log.error(ex.getMessage());
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getClie", oConn);
//                strRes = "ERROR:" + ex.getMessage();
//            }
//        }
//
//        strFechaCFDI = strFechaFactura;
//        try {
//            strSql = "SELECT EMP_RAZONSOCIAL,EMP_RFC,EMP_CALLE,EMP_COLONIA,EMP_LOCALIDAD,EMP_MUNICIPIO,EMP_CP,EMP_ESTADO,EMP_TIPOCOMP,EMP_TIPOCOMPNC,EMP_ACUSEFACTURA,EMP_ES_DONATARIA ,EMP_DONA_NUM_AUTORIZA,EMP_DONA_FECHA_AUTORIZA,EMP_DONA_LEYENDA,EMP_NUMERO,EMP_NUMINT FROM vta_empresas WHERE EMP_ID = " + intEmpId;
//
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strNombreEmisor = FormateaTextoXml(rs.getString("EMP_RAZONSOCIAL"), false);
//                strNumeroEmisor = FormateaTextoXml(rs.getString("EMP_NUMERO"), false);
//                strNumeroIntEmisor = FormateaTextoXml(rs.getString("EMP_NUMINT"), false);
//                strRFC = FormateaTextoXml(rs.getString("EMP_RFC").replace("-", ""), false);
//                strCalle = FormateaTextoXml(rs.getString("EMP_CALLE"));
//                strColonia = FormateaTextoXml(rs.getString("EMP_COLONIA"));
//                strLocalidad = FormateaTextoXml(rs.getString("EMP_LOCALIDAD"));
//                strMunicipio = FormateaTextoXml(rs.getString("EMP_MUNICIPIO"));
//                strCP = FormateaTextoXml(rs.getString("EMP_CP"));
//                strEdo = FormateaTextoXml(rs.getString("EMP_ESTADO"));
//                strPais = FormateaTextoXml("MEXICO");
//                intEMP_ACUSEFACTURA = rs.getInt("EMP_ACUSEFACTURA");
//
//                if (intEMP_TIPOCOMP == 0) {
//                    if (!bolEsNc) {
//                        if (bolEsNotaCargo) {
//                            intEMP_TIPOCOMP = 8;
//                        } else {
//                            intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
//                        }
//                    } else {
//                        intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMPNC");
//                    }
//                }
//
//                if (rs.getInt("EMP_ES_DONATARIA") == 1) {
//                    bolDonataria = true;
//                    bolComplementoFiscal = true;
//                    strDonaNumAutoriza = rs.getString("EMP_DONA_NUM_AUTORIZA");
//                    strDonaFechaAutoriza = rs.getString("EMP_DONA_FECHA_AUTORIZA");
//                    strDonaLeyenda = rs.getString("EMP_DONA_LEYENDA");
//                }
//            }
//            if (rs.getStatement() != null) {
//            }
//
//            rs.close();
//
//            strSql = "SELECT * FROM vta_sucursal WHERE SC_ID = " + intSucursal;
//
//            rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strCalleSuc = FormateaTextoXml(rs.getString("SC_CALLE"));
//                strColoniaSuc = FormateaTextoXml(rs.getString("SC_COLONIA"));
//                strLocalidadSuc = FormateaTextoXml(rs.getString("SC_LOCALIDAD"));
//                strMunicipioSuc = FormateaTextoXml(rs.getString("SC_MUNICIPIO"));
//                strCPSuc = FormateaTextoXml(rs.getString("SC_CP"));
//                strEdoSuc = FormateaTextoXml(rs.getString("SC_ESTADO"));
//                strPaisSuc = FormateaTextoXml("MEXICO");
//            }
//            if (rs.getStatement() != null) {
//            }
//
//            rs.close();
//
//            if (bolUsoLugarExpEmp) {
//                if (bolNominas) {
//                    strExpedidoEn = strCP;
//                } else {
//                    strExpedidoEn = strCP;
//                }
//            } else if (bolNominas) {
//                strExpedidoEn = strCPSuc;
//            } else {
//                strExpedidoEn = strCPSuc;
//            }
//
//            if (!bolNominas) {
//                if (!bolEsNotaCargoProveedor) {
//                    strSql = "SELECT CT_RAZONSOCIAL,CT_RFC,CT_CALLE,CT_COLONIA,CT_LOCALIDAD,CT_MUNICIPIO,CT_CP,CT_ESTADO,CT_NUMERO,CT_NUMINT,CT_EMAIL1,CT_EMAIL2,CT_ENVIO_FACTURA,CT_EMAIL3,CT_EMAIL4,CT_EMAIL5,CT_EMAIL6,CT_EMAIL7,CT_EMAIL8,CT_EMAIL9,CT_EMAIL10,CT_NUMPREDIAL  FROM vta_cliente WHERE CT_ID = " + intCliente;
//
//                    rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strNombreReceptor = FormateaTextoXml(rs.getString("CT_RAZONSOCIAL"), false);
//                        strRFCReceptor = FormateaTextoXml(rs.getString("CT_RFC").replace("-", ""), false);
//                        strCalleReceptor = FormateaTextoXml(rs.getString("CT_CALLE"));
//                        strColoniaReceptor = FormateaTextoXml(rs.getString("CT_COLONIA"));
//                        strLocalidadReceptor = FormateaTextoXml(rs.getString("CT_LOCALIDAD"));
//                        strMunicipioReceptor = FormateaTextoXml(rs.getString("CT_MUNICIPIO"));
//                        strCPReceptor = FormateaTextoXml(rs.getString("CT_CP"));
//                        strEdoReceptor = FormateaTextoXml(rs.getString("CT_ESTADO"));
//                        strNumero = FormateaTextoXml(rs.getString("CT_NUMERO"));
//                        strNumeroInt = FormateaTextoXml(rs.getString("CT_NUMINT"));
//                        strMailCte = rs.getString("CT_EMAIL1");
//                        strMailCte2 = rs.getString("CT_EMAIL2");
//                        strMailCte3 = rs.getString("CT_EMAIL3");
//                        strMailCte4 = rs.getString("CT_EMAIL4");
//                        strMailCte5 = rs.getString("CT_EMAIL5");
//                        strMailCte6 = rs.getString("CT_EMAIL6");
//                        strMailCte7 = rs.getString("CT_EMAIL7");
//                        strMailCte8 = rs.getString("CT_EMAIL8");
//                        strMailCte9 = rs.getString("CT_EMAIL9");
//                        strMailCte10 = rs.getString("CT_EMAIL10");
//                        intClienteEnviaMail = rs.getInt("CT_ENVIO_FACTURA");
//                        strCuentaPredial = rs.getString("CT_NUMPREDIAL");
//                    }
//                    if (rs.getStatement() != null) {
//                    }
//
//                    rs.close();
//
//                    if (intClienteFinal != 0) {
//                        strSql = "SELECT DFA_RAZONSOCIAL,DFA_RFC,DFA_CALLE,DFA_COLONIA,DFA_LOCALIDAD, DFA_MUNICIPIO,DFA_ESTADO,DFA_NUMERO,DFA_NUMINT, DFA_CP,DFA_EMAIL FROM vta_cliente_facturacion  where DFA_ID=" + intClienteFinal;
//
//                        rs = oConn.runQuery(strSql, true);
//                        while (rs.next()) {
//                            strNombreReceptor = FormateaTextoXml(rs.getString("DFA_RAZONSOCIAL"), false);
//                            strRFCReceptor = FormateaTextoXml(rs.getString("DFA_RFC").replace("-", ""), false);
//                            strCalleReceptor = FormateaTextoXml(rs.getString("DFA_CALLE"));
//                            strColoniaReceptor = FormateaTextoXml(rs.getString("DFA_COLONIA"));
//                            strLocalidadReceptor = FormateaTextoXml(rs.getString("DFA_LOCALIDAD"));
//                            strMunicipioReceptor = FormateaTextoXml(rs.getString("DFA_MUNICIPIO"));
//                            strCPReceptor = FormateaTextoXml(rs.getString("DFA_CP"));
//                            strEdoReceptor = FormateaTextoXml(rs.getString("DFA_ESTADO"));
//                            strNumero = FormateaTextoXml(rs.getString("DFA_NUMERO"));
//                            strNumeroInt = FormateaTextoXml(rs.getString("DFA_NUMINT"));
//                            strMailCte = rs.getString("DFA_EMAIL");
//                        }
//                        if (rs.getStatement() != null) {
//                        }
//
//                        rs.close();
//                    }
//                } else {
//                    strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_CALLE,PV_COLONIA,PV_LOCALIDAD,PV_MUNICIPIO,PV_CP,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_EMAIL1,PV_EMAIL2  FROM vta_proveedor WHERE PV_ID = " + intCliente;
//
//                    rs = oConn.runQuery(strSql, true);
//                    while (rs.next()) {
//                        strNombreReceptor = FormateaTextoXml(rs.getString("PV_RAZONSOCIAL"), false);
//                        strRFCReceptor = FormateaTextoXml(rs.getString("PV_RFC").replace("-", ""), false);
//                        strCalleReceptor = FormateaTextoXml(rs.getString("PV_CALLE"));
//                        strColoniaReceptor = FormateaTextoXml(rs.getString("PV_COLONIA"));
//                        strLocalidadReceptor = FormateaTextoXml(rs.getString("PV_LOCALIDAD"));
//                        strMunicipioReceptor = FormateaTextoXml(rs.getString("PV_MUNICIPIO"));
//                        strCPReceptor = FormateaTextoXml(rs.getString("PV_CP"));
//                        strEdoReceptor = FormateaTextoXml(rs.getString("PV_ESTADO"));
//                        strNumero = FormateaTextoXml(rs.getString("PV_NUMERO"));
//                        strNumeroInt = FormateaTextoXml(rs.getString("PV_NUMINT"));
//                        strMailCte = rs.getString("PV_EMAIL1");
//                        strMailCte2 = rs.getString("PV_EMAIL2");
//                        strMailCte3 = "";
//                        strMailCte4 = "";
//                        strMailCte5 = "";
//                        strMailCte6 = "";
//                        strMailCte7 = "";
//                        strMailCte8 = "";
//                        strMailCte9 = "";
//                        strMailCte10 = "";
//                        intClienteEnviaMail = 0;
//                    }
//                    if (rs.getStatement() != null) {
//                    }
//
//                    rs.close();
//
//                    if (intClienteFinal != 0) {
//                        strSql = "SELECT DFA_RAZONSOCIAL,DFA_RFC,DFA_CALLE,DFA_COLONIA,DFA_LOCALIDAD, DFA_MUNICIPIO,DFA_ESTADO,DFA_NUMERO,DFA_NUMINT, DFA_CP,DFA_EMAIL FROM vta_cliente_facturacion  where DFA_ID=" + intClienteFinal;
//
//                        rs = oConn.runQuery(strSql, true);
//                        while (rs.next()) {
//                            strNombreReceptor = FormateaTextoXml(rs.getString("DFA_RAZONSOCIAL"), false);
//                            strRFCReceptor = FormateaTextoXml(rs.getString("DFA_RFC").replace("-", ""), false);
//                            strCalleReceptor = FormateaTextoXml(rs.getString("DFA_CALLE"));
//                            strColoniaReceptor = FormateaTextoXml(rs.getString("DFA_COLONIA"));
//                            strLocalidadReceptor = FormateaTextoXml(rs.getString("DFA_LOCALIDAD"));
//                            strMunicipioReceptor = FormateaTextoXml(rs.getString("DFA_MUNICIPIO"));
//                            strCPReceptor = FormateaTextoXml(rs.getString("DFA_CP"));
//                            strEdoReceptor = FormateaTextoXml(rs.getString("DFA_ESTADO"));
//                            strNumero = FormateaTextoXml(rs.getString("DFA_NUMERO"));
//                            strNumeroInt = FormateaTextoXml(rs.getString("DFA_NUMINT"));
//                            strMailCte = rs.getString("DFA_EMAIL");
//                        }
//                        if (rs.getStatement() != null) {
//                        }
//
//                        rs.close();
//                    }
//                }
//            } else {
//                strSql = "SELECT EMP_NOMBRE,EMP_RFC,EMP_CALLE,EMP_COLONIA,EMP_LOCALIDAD,EMP_MUNICIPIO,EMP_CP,EMP_ESTADO,EMP_NUMERO,EMP_NUMINT FROM rhh_empleados WHERE EMP_NUM = " + intCliente;
//
//                rs = oConn.runQuery(strSql, true);
//                while (rs.next()) {
//                    strNombreReceptor = FormateaTextoXml(rs.getString("EMP_NOMBRE"), false);
//                    strRFCReceptor = FormateaTextoXml(rs.getString("EMP_RFC").replace("-", ""), false);
//                    strCalleReceptor = FormateaTextoXml(rs.getString("EMP_CALLE"));
//                    strColoniaReceptor = FormateaTextoXml(rs.getString("EMP_COLONIA"));
//                    strLocalidadReceptor = FormateaTextoXml(rs.getString("EMP_LOCALIDAD"));
//                    strMunicipioReceptor = FormateaTextoXml(rs.getString("EMP_MUNICIPIO"));
//                    strCPReceptor = FormateaTextoXml(rs.getString("EMP_CP"));
//                    strEdoReceptor = FormateaTextoXml(rs.getString("EMP_ESTADO"));
//                    strNumero = FormateaTextoXml(rs.getString("EMP_NUMERO"));
//                    strNumeroInt = FormateaTextoXml(rs.getString("EMP_NUMINT"));
//                    strMailCte = "";
//                    strMailCte2 = "";
//                }
//                if (rs.getStatement() != null) {
//                }
//
//                rs.close();
//            }
//
//            if (strRegimenFiscal.isEmpty()) {
//                String strSqlRegFis = "SELECT RF_CLAVE FROM vta_empregfiscal,view_vta_regimen_fiscal\nwhere vta_empregfiscal.REGF_ID = view_vta_regimen_fiscal.RF_ID and EMP_ID = " + intEmpId;
//
//                rs = oConn.runQuery(strSqlRegFis, true);
//                while (rs.next()) {
//                    strRegimenFiscal = rs.getString("RF_CLAVE");
//                }
//                rs.close();
//            }
//
//            if (!bolEsMc) {
//                String strSqlMoneda = "SELECT MON_DESCRIPCION,MON_SIGLAS FROM vta_monedas  where MON_ID = " + strMoneda;
//
//                rs = oConn.runQuery(strSqlMoneda, true);
//                while (rs.next()) {
//                    if (rs.getString("MON_SIGLAS").isEmpty()) {
//                        strMoneda = rs.getString("MON_DESCRIPCION");
//                    } else {
//                        strMoneda = rs.getString("MON_SIGLAS");
//                    }
//                }
//                rs.close();
//            }
//
//            if (!strIdFacturaRelacion.isEmpty()) {
//                String strSqlUUID = "SELECT FAC_UUID FROM vta_facturas  where FAC_ID = " + strIdFacturaRelacion;
//
//                rs = oConn.runQuery(strSqlUUID, true);
//                while (rs.next()) {
//                    strIdFacturaRelacion = rs.getString("FAC_UUID");
//                }
//                rs.close();
//            }
//        } catch (SQLException ex) {
//            log.error("Error(Datos)" + ex.getMessage() + " " + ex.getLocalizedMessage());
//
//            strRes = "ERROR:" + ex.getMessage();
//        }
//
//        log.debug("Comenzamos a llenar objetos CFDI...");
//        com.mx.siweb.sat.facturacion33.ObjectFactory objFactory = new com.mx.siweb.sat.facturacion33.ObjectFactory();
//        objComp = objFactory.createComprobante();
//
//        objComp.setVersion("3.3");
//
//        if (!strSerie.isEmpty()) {
//            objComp.setSerie(strSerie);
//        }
//
//        if (!strFolio.isEmpty()) {
//            objComp.setFolio(strFolio + "");
//        }
//
//        SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMddHH:mm:ss");
//        Date dateTmp = new Date();
//        try {
//            dateTmp = formateaDate.parse(strFechaFactura + strHoraFactura);
//        } catch (ParseException ex) {
//            log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//        }
//        objComp.setFecha(dateTmp);
//
//        if (!bolEsMc) {
//            objComp.setFormaPago(strFAC_FORMADEPAGO);
//            if ((!bolNominas)
//                    && (!strFAC_CONDPAGO.isEmpty())) {
//                objComp.setCondicionesDePago(strFAC_CONDPAGO);
//            }
//        }
//
//        objComp.setNoCertificado(RecuperaNoSerieCertificado());
//
//        log.debug("Abrimos el certificado...");
//        UtilCert cert = new UtilCert();
//        log.debug("Phat: " + strPathCert);
//        cert.OpenCert(strPathCert);
//        if (!cert.getStrResult().startsWith("ERROR")) {
//            objComp.setCertificado(cert.getCertContentBase64());
//            try {
//                certificadoEmisor = cert.getCert().getEncoded();
//            } catch (CertificateEncodingException ex) {
//                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//            }
//        }
//        if (bolEsMc) {
//            objComp.setSubTotal(new BigDecimal(NumberString.FormatearDecimal(BGSubtotal.doubleValue(), 0).replace(",", "")));
//        } else {
//            objComp.setSubTotal(new BigDecimal(NumberString.FormatearDecimal(BGSubtotal.doubleValue(), 2).replace(",", "")));
//        }
//
//        if (((bolEsFactura) || (bolEsNc))
//                && (BGDescuento.doubleValue() > 0.0D)) {
//            objComp.setSubTotal(new BigDecimal(NumberString.FormatearDecimal(BGSubtotal.doubleValue() + BGDescuento.doubleValue(), 2).replace(",", "")));
//        }
//
//        double dblTotTotal = BGTotal.doubleValue();
//        double dblRetl = 0.0D;
//        double dblRet2 = 0.0D;
//        if (BGImpuestoRetenido1 != null) {
//            dblRetl = BGImpuestoRetenido1.doubleValue();
//        }
//        if (BGImpuestoRetenido2 != null) {
//            dblRet2 = BGImpuestoRetenido2.doubleValue();
//        }
//        if (bolNominas) {
//            if (BGDescuento.doubleValue() > 0.0D) {
//                objComp.setDescuento(new BigDecimal(NumberString.FormatearDecimal(BGDescuento.doubleValue(), 2).replace(",", "")));
//            }
//        } else {
//            dblTotTotal = dblTotTotal - dblRetl - dblRet2;
//        }
//
//        if (bolNominas) {
//            if (BGDescuento.doubleValue() > 0.0D) {
//                objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal(objComp.getSubTotal().doubleValue() - objComp.getDescuento().doubleValue(), 2).replace(",", "")));
//            } else {
//                objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal(objComp.getSubTotal().doubleValue(), 2).replace(",", "")));
//            }
//
//        } else if (bolEsMc) {
//            objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal(dblTotTotal, 0).replace(",", "")));
//        } else {
//            objComp.setTotal(new BigDecimal(NumberString.FormatearDecimal(dblTotTotal, 2).replace(",", "")));
//        }
//
//        if ((BGDescuento != null)
//                && (BGDescuento.doubleValue() > 0.0D)) {
//
//            if (!bolNominas) {
//
//                objComp.setDescuento(new BigDecimal(NumberString.FormatearDecimal(BGDescuento.doubleValue(), 2).replace(",", "")));
//            }
//        }
//
//        objComp.setTipoDeComprobante(CTipoDeComprobante.fromValue(strTipoComprobante));
//        if ((!bolEsMc)
//                && (!strFAC_METODODEPAGO.isEmpty())) {
//            objComp.setMetodoPago(com.mx.siweb.sat.facturacion33.CMetodoPago.fromValue(strFAC_METODODEPAGO));
//        }
//
//        objComp.setLugarExpedicion(strExpedidoEn);
//        if (!strConfirmacion.isEmpty()) {
//            objComp.setConfirmacion(strConfirmacion);
//        }
//
//        if (!strMoneda.isEmpty()) {
//            objComp.setMoneda(com.mx.siweb.sat.facturacion33.CMoneda.fromValue(strMoneda));
//        }
//
//        if ((!bolEsMc)
//                && (!bolNominas)) {
//            objComp.setTipoCambio(new BigDecimal(NumberString.FormatearDecimal(BGTasaPeso.doubleValue(), 6).replace(",", "")));
//        }
//
//        Comprobante.CfdiRelacionados cfdirelacionados = objFactory.createComprobanteCfdiRelacionados();
//        cfdirelacionados.setTipoRelacion(strTipoRelacion);
//
//        Comprobante.CfdiRelacionados.CfdiRelacionado cfdirelacionado = objFactory.createComprobanteCfdiRelacionadosCfdiRelacionado();
//        cfdirelacionado.setUUID(strIdFacturaRelacion);
//
//        cfdirelacionados.getCfdiRelacionado().add(cfdirelacionado);
//
//        Comprobante.Emisor emisor = objFactory.createComprobanteEmisor();
//        emisor.setRfc(strRFC);
//        emisor.setNombre(strNombreEmisor);
//        emisor.setRegimenFiscal(strRegimenFiscal);
//
//        Comprobante.Receptor receptor = objFactory.createComprobanteReceptor();
//        log.debug("INICIO SET...");
//        log.debug("strRFCReceptor... " + strRFCReceptor);
//        receptor.setRfc(strRFCReceptor);
//        log.debug("strNombreReceptor... " + strNombreReceptor);
//        receptor.setNombre(strNombreReceptor);
//        if (!strResidenciaFiscal.isEmpty()) {
//            receptor.setResidenciaFiscal(CPais.fromValue(strResidenciaFiscal));
//        }
//        if (!strNumRegIdTrib.isEmpty()) {
//            receptor.setNumRegIdTrib(strNumRegIdTrib);
//        }
//        log.debug("strUsoCFDI... " + strUsoCFDI);
//        if (!strUsoCFDI.isEmpty()) {
//            receptor.setUsoCFDI(CUsoCFDI.fromValue(strUsoCFDI));
//        }
//        log.debug("FIN SET...");
//
//        Comprobante.Conceptos conceps = objFactory.createComprobanteConceptos();
//
//        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyyMMdd");
//
//        if (bolEsMc) {
//            log.debug("Consultamos detalles de pagos...");
//
//            strSql = "SELECT MC_ID,SC_ID,MC_IMPUESTO1,MC_FOLIO,CT_ID,MC_MONEDA,MC_TASAPESO,FAC_ID,TKT_ID,PD_ID,EMP_ID,MC_ABONO FROM vta_mov_cte WHERE MC_ESPAGO=1 AND MCM_ID=" + intTransaccion + ";";
//
//            try {
//                ResultSet rsP = oConn.runQuery(strSql, true);
//                while (rsP.next()) {
//                    Comprobante.Conceptos.Concepto objC = objFactory.createComprobanteConceptosConcepto();
//                    String strUnidaddeMedida = "ACT";
//                    objC.setClaveUnidad(strUnidaddeMedida);
//                    String strConceptoPago = "Pago";
//                    objC.setDescripcion(strConceptoPago);
//                    String strClaveProdServ = "84111506";
//                    objC.setClaveProdServ(strClaveProdServ);
//                    objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(0.0D, 0).replace(",", "")));
//                    objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(0.0D, 0).replace(",", "")));
//                    objC.setCantidad(new BigDecimal(NumberString.FormatearDecimal(1.0D, 2).replace(",", "")));
//                    conceps.getConcepto().add(objC);
//
//                    Comprobante.Complemento localComplemento = objFactory.createComprobanteComplemento();
//                }
//
//                rsP.close();
//            } catch (SQLException ex) {
//                log.error(ex.getMessage());
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getPart", oConn);
//                strRes = "ERROR:" + ex.getMessage();
//            }
//        } else if (bolNominas) {
//
//            strSql = "select sum(NOMD_UNITARIO) as tot from rhh_nominas_deta n  WHERE (TP_ID <> 0 OR RTOP_ID <> 0) and  n.NOM_ID  = " + intTransaccion;
//            try {
//                ResultSet rs2 = oConn.runQuery(strSql, true);
//                while (rs2.next()) {
//                    Comprobante.Conceptos.Concepto objC = objFactory.createComprobanteConceptosConcepto();
//
//                    String strUnidaddeMedida = "ACT";
//                    strConceptoNomina = "Pago de nómina";
//                    objC.setDescripcion(strConceptoNomina);
//                    objC.setCantidad(new BigDecimal(1));
//                    objC.setClaveUnidad(strUnidaddeMedida);
//                    objC.setClaveProdServ("84111505");
//                    double FACD_VALORUNIT = rs2.getDouble("tot");
//                    objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 2).replace(",", "")));
//                    objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 2).replace(",", "")));
//                    if (BGDescuento.doubleValue() > 0.0D) {
//                        objC.setDescuento(BGDescuento);
//                    }
//
//                    conceps.getConcepto().add(objC);
//                }
//
//                rs2.close();
//            } catch (SQLException ex) {
//                log.error(ex.getMessage());
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getPart", oConn);
//                strRes = "ERROR:" + ex.getMessage();
//            }
//        } else {
//            if (!bolEsNc) {
//                if (bolEsNotaCargo) {
//                    strSql = "SELECT NCAD_ID,NCAD_CVE,PR_ID,NCAD_DESCRIPCION,trim(NCAD_COMENTARIO) as comentario,NCAD_CANTIDAD,NCAD_PRECIO,NCAD_NOSERIE,NCAD_IMPORTE,NCAD_IMPUESTO1,NCAD_TASAIVA1,NCAD_UNIDAD_MEDIDA,PDD_ID,NCAD_CVE_PRODSERV,NCAD_CVE_UNIDAD,NCAD_DESCUENTO,NCAD_IMPUESTO2,NCAD_IMPUESTO3  FROM vta_notas_cargosdeta WHERE NCA_ID  = " + intTransaccion;
//
//                } else if (bolEsNotaCargoProveedor) {
//                    strSql = "SELECT NCAD_ID,NCAD_CVE,PR_ID,NCAD_DESCRIPCION,trim(NCAD_COMENTARIO) as comentario,NCAD_CANTIDAD,NCAD_PRECIO,NCAD_NOSERIE,NCAD_IMPORTE,NCAD_IMPUESTO1,NCAD_TASAIVA1,NCAD_UNIDAD_MEDIDA,PDD_ID,NCAD_CVE_PRODSERV,NCAD_CVE_UNIDAD,NCAD_DESCUENTO,NCAD_IMPUESTO2,NCAD_IMPUESTO3  FROM vta_notas_cargosprovdeta WHERE NCA_ID  = " + intTransaccion;
//
//                } else if (bolEsLocal) {
//                    strSql = "SELECT FACD_ID,FACD_CVE,PR_ID,FACD_DESCRIPCION,FACD_COMENTARIO as comentario,FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_IMPUESTO2,FACD_IMPUESTO3,FACD_TASAIVA1,FACD_UNIDAD_MEDIDA,FACD_CVE_PRODSERV,FACD_CVE_UNIDAD,FACD_DESCUENTO,FACD_RET_IVA,FACD_RET_ISR,FACD_RET_FLETE  FROM vta_facturasdeta WHERE FAC_ID  = " + intTransaccion;
//
//                    log.debug("Detalle factura1...");
//                    bolEsFactura = true;
//                } else {
//                    strSql = "SELECT FACD_ID,FACD_CVE,PR_ID,FACD_DESCRIPCION,trim(FACD_COMENTARIO) as comentario,FACD_CANTIDAD,FACD_PRECIO,FACD_NOSERIE,FACD_IMPORTE,FACD_IMPUESTO1,FACD_IMPUESTO2,FACD_IMPUESTO3,FACD_TASAIVA1,FACD_UNIDAD_MEDIDA,PDD_ID,FACD_CVE_PRODSERV,FACD_CVE_UNIDAD,FACD_DESCUENTO,FACD_RET_IVA,FACD_RET_ISR,FACD_RET_FLETE  FROM vta_facturasdeta WHERE FAC_ID  = " + intTransaccion;
//
//                    log.debug("Detalle factura...");
//                    bolEsFactura = true;
//                }
//
//            } else if (bolEsLocal) {
//                strSql = "SELECT NCD_ID,NCD_CVE,PR_ID,NCD_DESCRIPCION,NCD_COMENTARIO as comentario,NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_TASAIVA1,NCD_UNIDAD_MEDIDA,NCD_CVE_PRODSERV,NCD_CVE_UNIDAD,NCD_DESCUENTO,NCD_IMPUESTO2,NCD_IMPUESTO3  FROM vta_ncreditodeta WHERE NC_ID  = " + intTransaccion;
//            } else {
//                strSql = "SELECT NCD_ID,NCD_CVE,PR_ID,NCD_DESCRIPCION,trim(NCD_COMENTARIO) as comentario,NCD_CANTIDAD,NCD_PRECIO,NCD_NOSERIE,NCD_IMPORTE,NCD_IMPUESTO1,NCD_TASAIVA1,NCD_UNIDAD_MEDIDA,NCD_CVE_PRODSERV,NCD_CVE_UNIDAD,NCD_DESCUENTO ,NCD_IMPUESTO2,NCD_IMPUESTO3 FROM vta_ncreditodeta WHERE NC_ID  = " + intTransaccion;
//            }
//
//            String strPrefijoDeta = "FACD";
//            if (bolEsNc) {
//                strPrefijoDeta = "NCD";
//            }
//            if ((bolEsNotaCargo) || (bolEsNotaCargoProveedor)) {
//                strPrefijoDeta = "NCAD";
//            }
//            try {
//                ResultSet rs = oConn.runQuery(strSql, true);
//                while (rs.next()) {
//                    int intPDD_ID = 0;
//                    if ((!bolEsNc)
//                            && (!bolEsLocal)) {
//                        intPDD_ID = rs.getInt("PDD_ID");
//                    }
//
//                    String strUnidaddeMedida = rs.getString(strPrefijoDeta + "_UNIDAD_MEDIDA");
//                    if (strUnidaddeMedida.isEmpty()) {
//                        strUnidaddeMedida = "NO APLICA";
//                    }
//                    Comprobante.Conceptos.Concepto objC = objFactory.createComprobanteConceptosConcepto();
//                    objC.setClaveProdServ(rs.getString(strPrefijoDeta + "_CVE_PRODSERV"));
//                    if (rs.getString(strPrefijoDeta + "_CVE").isEmpty()) {
//                        objC.setNoIdentificacion("Servicio");
//                    } else {
//                        objC.setNoIdentificacion(rs.getString(strPrefijoDeta + "_CVE"));
//                    }
//                    String sqlUnidad = "SELECT UNI_NOMBRE,UNI_CLAVE FROM view_vta_unidades WHERE UNI_ID='" + strUnidaddeMedida + "';";
//                    ResultSet rsUnidad = oConn.runQuery(sqlUnidad, true);
//                    String srtClaveUnidad = "";
//                    if (rsUnidad.next()) {
//                        srtClaveUnidad = rsUnidad.getString("UNI_CLAVE");
//                    }
//                    rsUnidad.close();
//
//                    objC.setClaveUnidad(srtClaveUnidad);
//
//                    objC.setCantidad(new BigDecimal(NumberString.FormatearDecimal(rs.getBigDecimal(strPrefijoDeta + "_CANTIDAD").doubleValue(), 2).replace(",", "")));
//                    if ((rs.getString(strPrefijoDeta + "_CVE").equals("...")) || (rs.getString(strPrefijoDeta + "_CVE").equals(""))) {
//                        objC.setDescripcion(FormateaTextoXml(rs.getString("comentario")));
//                    } else {
//                        objC.setDescripcion(FormateaTextoXml(rs.getString(new StringBuilder().append(strPrefijoDeta).append("_DESCRIPCION").toString()) + rs.getString("comentario")));
//                    }
//
//                    double dblImporteDeta = rs.getDouble(strPrefijoDeta + "_IMPORTE");
//                    double dblImpuesto1Deta = rs.getDouble(strPrefijoDeta + "_IMPUESTO1");
//                    double dblImpuesto2Deta = rs.getDouble(strPrefijoDeta + "_IMPUESTO2");
//                    double dblImpuesto3Deta = rs.getDouble(strPrefijoDeta + "_IMPUESTO3");
//                    double dblTasaIVA1Deta = rs.getDouble(strPrefijoDeta + "_TASAIVA1");
//                    double dblImporteSinIva = 0.0D;
//                    double dblValorUnit = 0.0D;
//                    double dblDescuentoDeta = rs.getDouble(strPrefijoDeta + "_DESCUENTO");
//
//                    int intRetIVA = 0;
//                    int intRetISR = 0;
//                    int intRetIVAFlete = 0;
//                    if (bolEsFactura) {
//                        intRetIVA = rs.getInt(strPrefijoDeta + "_RET_IVA");
//                        intRetISR = rs.getInt(strPrefijoDeta + "_RET_ISR");
//                        intRetIVAFlete = rs.getInt(strPrefijoDeta + "_RET_FLETE");
//                    }
//
//                    if (bolEsLocal) {
//                        if ((!rs.getString(strPrefijoDeta + "_CVE").equals("...")) && (!rs.getString(strPrefijoDeta + "_CVE").equals(""))) {
//                            dblImporteSinIva = dblImporteDeta - dblImpuesto1Deta;
//                        } else {
//                            dblImporteSinIva = dblImporteDeta;
//                        }
//                    } else {
//                        dblImporteSinIva = dblImporteDeta - dblImpuesto1Deta;
//                    }
//
//                    boolean bolEncontroTraslado = false;
//                    Iterator<TrasladosIVA> itTraslados = lstTrasladosIVA.iterator();
//                    while (itTraslados.hasNext()) {
//                        TrasladosIVA traslado = (TrasladosIVA) itTraslados.next();
//                        if (traslado.getDblTasaIVA() == dblTasaIVA1Deta) {
//                            bolEncontroTraslado = true;
//                            traslado.setDblImporteTasaIVA(dblImpuesto1Deta + traslado.getDblImporteTasaIVA());
//                        }
//                    }
//                    if (!bolEncontroTraslado) {
//                        TrasladosIVA traslado = new TrasladosIVA();
//                        traslado.setDblTasaIVA(dblTasaIVA1Deta);
//                        traslado.setDblImporteTasaIVA(dblImpuesto1Deta);
//                        lstTrasladosIVA.add(traslado);
//                    }
//
//                    if ((dblImpuesto1Deta > 0.0D) || (BGImpuestoRetenido1.doubleValue() > 0.0D) || (BGImpuestoRetenido2.doubleValue() > 0.0D)) {
//                        double bgTasaIVA = dblTasaIVA1Deta / 100.0D;
//                        Comprobante.Conceptos.Concepto.Impuestos objImpuestos = objFactory.createComprobanteConceptosConceptoImpuestos();
//                        Comprobante.Conceptos.Concepto.Impuestos.Traslados objTrasladados = objFactory.createComprobanteConceptosConceptoImpuestosTraslados();
//                        Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado objTraslado = objFactory.createComprobanteConceptosConceptoImpuestosTrasladosTraslado();
//                        objTraslado.setBase(new BigDecimal(NumberString.FormatearDecimal(dblImporteSinIva, 6).replace(",", "")));
//                        objTraslado.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImpuesto1Deta, 2).replace(",", "")));
//                        objTraslado.setImpuesto("002");
//                        objTraslado.setTipoFactor(CTipoFactor.TASA);
//                        objTraslado.setTasaOCuota(new BigDecimal(NumberString.FormatearDecimal(bgTasaIVA, 6).replace(",", "")));
//                        objTrasladados.getTraslado().add(objTraslado);
//                        objImpuestos.setTraslados(objTrasladados);
//                        objC.setImpuestos(objImpuestos);
//
//                        if (!bolNominas) {
//                            if (((BGImpuestoRetenido1.doubleValue() > 0.0D) && (intRetIVA == 0)) || ((BGImpuestoRetenido2.doubleValue() > 0.0D) && (intRetISR == 0))) {
//                                Comprobante.Conceptos.Concepto.Impuestos.Retenciones retencionesDeta = objFactory.createComprobanteConceptosConceptoImpuestosRetenciones();
//                                if ((BGImpuestoRetenido1.doubleValue() > 0.0D) && (intRetIVA == 0)) {
//                                    float dblFactorIVA = 0.106667F;
//                                    double dblImporteBaseIVA = dblImporteDeta - dblImpuesto1Deta - dblImpuesto2Deta - dblImpuesto3Deta;
//                                    double dblRetIVA = dblImporteBaseIVA * dblFactorIVA;
//
//                                    if (intRetIVAFlete == 0) {
//                                        dblRetIVA = dblImpuesto1Deta / 50.0D * 2.0D;
//                                        dblFactorIVA = 0.04F;
//                                    }
//
//                                    Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion ret = objFactory.createComprobanteConceptosConceptoImpuestosRetencionesRetencion();
//                                    ret.setBase(new BigDecimal(NumberString.FormatearDecimal(dblImporteBaseIVA, 6).replace(",", "")));
//                                    ret.setTasaOCuota(new BigDecimal(NumberString.FormatearDecimal(dblFactorIVA, 6).replace(",", "")));
//                                    ret.setTipoFactor(CTipoFactor.TASA);
//                                    DecimalFormat df = new DecimalFormat("#.00");
//                                    ret.setImporte(new BigDecimal(df.format(dblRetIVA)));
//
//                                    ret.setImpuesto("002");
//                                    retencionesDeta.getRetencion().add(ret);
//                                }
//                                if ((BGImpuestoRetenido2.doubleValue() > 0.0D) && (intRetISR == 0)) {
//                                    double dblImporteRetISR = dblImporteDeta - dblImpuesto1Deta - dblImpuesto2Deta - dblImpuesto3Deta;
//                                    double dblRetISR = dblImporteRetISR * (dblFactorIsr / 100.0D);
//                                    double dblFactorISR = 0.1D;
//                                    Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion ret = objFactory.createComprobanteConceptosConceptoImpuestosRetencionesRetencion();
//                                    ret.setBase(new BigDecimal(NumberString.FormatearDecimal(dblImporteRetISR, 6).replace(",", "")));
//                                    ret.setTasaOCuota(new BigDecimal(NumberString.FormatearDecimal(dblFactorISR, 6).replace(",", "")));
//                                    ret.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblRetISR, 2).replace(",", "")));
//                                    ret.setTipoFactor(CTipoFactor.TASA);
//                                    ret.setImpuesto("001");
//                                    retencionesDeta.getRetencion().add(ret);
//                                }
//                                objImpuestos.setRetenciones(retencionesDeta);
//                            }
//                        }
//                    }
//
//                    dblValorUnit = dblImporteSinIva / rs.getDouble(strPrefijoDeta + "_CANTIDAD");
//
//                    if (dblValorUnit != 0.0D) {
//                        objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(dblValorUnit, 2).replace(",", "")));
//                        objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImporteSinIva, 2).replace(",", "")));
//                        if (((bolEsFactura) || (bolEsNc))
//                                && (dblDescuentoDeta != 0.0D)) {
//                            double dblDescuentoDetaUnitario = dblDescuentoDeta / rs.getDouble(strPrefijoDeta + "_CANTIDAD");
//                            double dblValorUnitTotal = dblValorUnit + dblDescuentoDetaUnitario;
//                            double dblImporteSinIvaTotal = dblImporteSinIva + dblDescuentoDeta;
//                            objC.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(dblValorUnitTotal, 2).replace(",", "")));
//                            objC.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImporteSinIvaTotal, 2).replace(",", "")));
//                        }
//                    } else {
//                        objC.setValorUnitario(new BigDecimal(0.0D));
//                        objC.setImporte(new BigDecimal(0.0D));
//                    }
//                    if (dblDescuentoDeta != 0.0D) {
//                        objC.setDescuento(new BigDecimal(NumberString.FormatearDecimal(dblDescuentoDeta, 2).replace(",", "")));
//                    }
//
//                    String strPedimento = "";
//                    if ((!rs.getString(strPrefijoDeta + "_CVE").equals("..."))
//                            && (!rs.getString(strPrefijoDeta + "_CVE").equals("SHIPPING")) && (!bolEsNc)) {
//
//                        String strAduana = "";
//                        String strFecha = "";
//
//                        strSql = "SELECT vta_facturasdeta.FAC_ID,  vta_facturasdeta.FACD_CVE,  vta_facturasdeta.FACD_DESCRIPCION,  vta_facturasdeta.FACD_IMPORTE,  vta_facturasdeta.FACD_CANTIDAD,  vta_movproddeta.PL_NUMLOTE,  vta_movproddeta.MPD_SALIDAS,  vta_movproddeta.MPD_FECHA,  vta_movproddeta.MPD_COSTO,  vta_movproddeta.MPD_IDORIGEN,  vta_facturasdeta.FACD_ID,  vta_movproddeta.PR_CODIGO,  vta_movproddeta.PR_ID,  vta_prodlote.PED_ID,  vta_pedimentos.PED_COD,  vta_pedimentos.PED_DESC,  vta_pedimentos.PED_FECHA_ENTRA, vta_aduana.AD_CVE,  vta_aduana.AD_DESCRIPCION FROM vta_movproddeta INNER JOIN vta_facturasdeta ON vta_movproddeta.PR_ID = vta_facturasdeta.PR_ID AND vta_facturasdeta.FACD_ID = vta_movproddeta.MPD_IDORIGEN INNER JOIN vta_prodlote ON vta_prodlote.PR_ID = vta_movproddeta.PR_ID AND vta_prodlote.PL_NUMLOTE = vta_movproddeta.PL_NUMLOTE INNER JOIN vta_pedimentos ON vta_pedimentos.PED_ID = vta_prodlote.PED_ID INNER JOIN vta_aduana ON vta_aduana.AD_ID = vta_pedimentos.AD_ID WHERE vta_facturasdeta.PDD_ID = 0 AND vta_facturasdeta.FACD_ID=" + rs.getString(new StringBuilder().append(strPrefijoDeta).append("_ID").toString()) + " AND vta_facturasdeta.PR_ID =" + rs.getString("PR_ID");
//
//                        if (intPDD_ID != 0) {
//
//                            strSql = "SELECT vta_facturasdeta.FAC_ID, vta_facturasdeta.FACD_CVE, vta_facturasdeta.FACD_ID, vta_facturasdeta.FACD_DESCRIPCION, vta_facturasdeta.FACD_IMPORTE, vta_facturasdeta.FACD_CANTIDAD, vta_facturasdeta.PR_ID, vta_pedidosdeta.PD_ID, vta_pedidosdeta.PDD_CVE, vta_facturasdeta.PDD_ID, vta_pedidosdeta.PDD_ID, vta_facturasdeta.FACD_NOSERIE, vta_movproddeta.PL_NUMLOTE, vta_movproddeta.MPD_IDORIGEN, vta_prodlote.PL_ID,vta_prodlote.PED_ID,vta_aduana.AD_CVE, vta_aduana.AD_DESCRIPCION, vta_pedimentos.PED_COD, vta_pedimentos.PED_DESC, vta_pedimentos.PED_FECHA_ENTRA FROM vta_aduana INNER JOIN vta_pedimentos ON vta_aduana.AD_ID = vta_pedimentos.AD_ID INNER JOIN vta_prodlote ON vta_pedimentos.PED_ID = vta_prodlote.PED_ID INNER JOIN  vta_movproddeta ON vta_prodlote.PR_ID = vta_movproddeta.PR_ID AND vta_prodlote.PL_NUMLOTE = vta_movproddeta.PL_NUMLOTE INNER JOIN vta_pedidosdeta ON vta_movproddeta.PR_ID = vta_pedidosdeta.PR_ID AND vta_movproddeta.MPD_IDORIGEN = vta_pedidosdeta.PDD_ID INNER JOIN vta_facturasdeta ON vta_pedidosdeta.PDD_ID = vta_facturasdeta.PDD_ID WHERE vta_facturasdeta.PDD_ID <> 0 AND vta_facturasdeta.FACD_ID=" + rs.getString(new StringBuilder().append(strPrefijoDeta).append("_ID").toString()) + " AND vta_facturasdeta.PR_ID =" + rs.getString("PR_ID");
//                        }
//
//                        ResultSet rs2 = oConn.runQuery(strSql, true);
//                        while (rs2.next()) {
//                            strAduana = rs2.getString("AD_DESCRIPCION");
//                            strFecha = rs2.getString("PED_FECHA_ENTRA");
//                            strPedimento = rs2.getString("PED_COD");
//                            Date dtFecha = new Date();
//                            try {
//                                dtFecha = formatoDeFecha.parse(strFecha);
//
//                                Comprobante.Conceptos.Concepto.InformacionAduanera t = objFactory.createComprobanteConceptosConceptoInformacionAduanera();
//                                t.setNumeroPedimento(strPedimento);
//
//                                objC.getInformacionAduanera().add(t);
//                            } catch (ParseException ex) {
//                                log.error(ex.getMessage());
//                            }
//                        }
//                        rs2.close();
//                    }
//
//                    if (!strCuentaPredial.isEmpty()) {
//                        Comprobante.Conceptos.Concepto.CuentaPredial cpre = objFactory.createComprobanteConceptosConceptoCuentaPredial();
//                        cpre.setNumero(strCuentaPredial);
//                        objC.setCuentaPredial(cpre);
//                    }
//
//                    if (!strPedimento.isEmpty()) {
//                        Comprobante.Conceptos.Concepto.Parte parte = objFactory.createComprobanteConceptosConceptoParte();
//                        parte.setClaveProdServ(rs.getString(strPrefijoDeta + "_CVE_PRODSERV"));
//                        if (rs.getString(strPrefijoDeta + "_CVE").isEmpty()) {
//                            parte.setNoIdentificacion("Servicio");
//                        } else {
//                            parte.setNoIdentificacion(rs.getString(strPrefijoDeta + "_CVE"));
//                        }
//                        parte.setCantidad(new BigDecimal(NumberString.FormatearDecimal(rs.getBigDecimal(strPrefijoDeta + "_CANTIDAD").doubleValue(), 2).replace(",", "")));
//                        parte.setUnidad(srtClaveUnidad);
//                        if ((rs.getString(strPrefijoDeta + "_CVE").equals("...")) || (rs.getString(strPrefijoDeta + "_CVE").equals(""))) {
//                            parte.setDescripcion(FormateaTextoXml(rs.getString("comentario")));
//                        } else {
//                            parte.setDescripcion(FormateaTextoXml(rs.getString(new StringBuilder().append(strPrefijoDeta).append("_DESCRIPCION").toString()) + rs.getString("comentario")));
//                        }
//                        if (dblValorUnit != 0.0D) {
//                            parte.setValorUnitario(new BigDecimal(NumberString.FormatearDecimal(dblValorUnit, 2).replace(",", "")));
//                            parte.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImporteSinIva, 2).replace(",", "")));
//                        } else {
//                            parte.setValorUnitario(new BigDecimal(0.0D));
//                            parte.setImporte(new BigDecimal(0.0D));
//                        }
//
//                        Comprobante.Conceptos.Concepto.Parte.InformacionAduanera tPart = objFactory.createComprobanteConceptosConceptoParteInformacionAduanera();
//                        tPart.setNumeroPedimento(strPedimento);
//                        parte.getInformacionAduanera().add(tPart);
//                    }
//
//                    conceps.getConcepto().add(objC);
//                }
//                if (rs.getStatement() != null) {
//                }
//
//                rs.close();
//            } catch (SQLException ex) {
//                log.error(ex.getMessage() + " " + ex.getLocalizedMessage());
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "getPart", oConn);
//                strRes = "ERROR:" + ex.getMessage();
//            }
//        }
//
//        log.debug("Nodo impuestos...");
//        Comprobante.Impuestos imp = objFactory.createComprobanteImpuestos();
//        boolean bolEncontroImpuestos = false;
//        if (!bolNominas) {
//
//            if (!bolEsMc) {
//
//                Comprobante.Impuestos.Traslados.Traslado objTraslado = null;
//                Comprobante.Impuestos.Traslados objTraslados = objFactory.createComprobanteImpuestosTraslados();
//                int intCuantos = 0;
//
//                Object itTraslados = lstTrasladosIVA.iterator();
//                while (((Iterator) itTraslados).hasNext()) {
//                    TrasladosIVA traslado = (TrasladosIVA) ((Iterator) itTraslados).next();
//                    if (traslado.getDblTasaIVA() > 0.0D) {
//                        objTraslado = objFactory.createComprobanteImpuestosTrasladosTraslado();
//
//                        objTraslado.setImpuesto("002");
//                        objTraslado.setTipoFactor(CTipoFactor.TASA);
//                        if (objTraslado.getTipoFactor() != CTipoFactor.EXENTO) {
//                            double bgTasaIVA = traslado.getDblTasaIVA() / 100.0D;
//                            objTraslado.setTasaOCuota(new BigDecimal(NumberString.FormatearDecimal(bgTasaIVA, 6).replace(",", "")));
//                        }
//                        objTraslado.setImporte(new BigDecimal(NumberString.FormatearDecimal(traslado.getDblImporteTasaIVA(), 2).replace(",", "")));
//                        objTraslados.getTraslado().add(objTraslado);
//                        intCuantos++;
//                    }
//                }
//
//                if (intUsoIEPS == 1) {
//                    bolEncontroImpuestos = true;
//                    Comprobante.Impuestos.Traslados.Traslado objTrasladoIEPS = objFactory.createComprobanteImpuestosTrasladosTraslado();
//                    objTraslados.getTraslado().add(objTrasladoIEPS);
//                    objTrasladoIEPS.setImpuesto("003");
//                    objTrasladoIEPS.setTipoFactor(CTipoFactor.CUOTA);
//                    objTrasladoIEPS.setImporte(BGImporteIEPS);
//                    intCuantos++;
//                }
//
//                if (intCuantos > 0) {
//                    bolEncontroImpuestos = true;
//                    imp.setTraslados(objTraslados);
//                    imp.setTotalImpuestosTrasladados(new BigDecimal(NumberString.FormatearDecimal(BGImpuesto1.add(BGImporteIEPS).doubleValue(), 2).replace(",", "")));
//                }
//
//                if ((BGImpuestoRetenido1.doubleValue() > 0.0D) || (BGImpuestoRetenido2.doubleValue() > 0.0D)) {
//                    bolEncontroImpuestos = true;
//
//                    Comprobante.Impuestos.Retenciones rets = objFactory.createComprobanteImpuestosRetenciones();
//
//                    double dblTotalImpuestosRetenidos = 0.0D;
//                    dblTotalImpuestosRetenidos += BGImpuestoRetenido1.doubleValue() + BGImpuestoRetenido2.doubleValue();
//                    if (BGImpuestoRetenido1.doubleValue() > 0.0D) {
//                        Comprobante.Impuestos.Retenciones.Retencion ret = objFactory.createComprobanteImpuestosRetencionesRetencion();
//                        ret.setImporte(new BigDecimal(NumberString.FormatearDecimal(BGImpuestoRetenido1.doubleValue(), 2).replace(",", "")));
//                        ret.setImpuesto("002");
//                        rets.getRetencion().add(ret);
//                    }
//
//                    if (BGImpuestoRetenido2.doubleValue() > 0.0D) {
//                        Comprobante.Impuestos.Retenciones.Retencion ret2 = objFactory.createComprobanteImpuestosRetencionesRetencion();
//                        ret2.setImporte(new BigDecimal(NumberString.FormatearDecimal(BGImpuestoRetenido2.doubleValue(), 2).replace(",", "")));
//                        ret2.setImpuesto("001");
//                        rets.getRetencion().add(ret2);
//                    }
//                    imp.setRetenciones(rets);
//                    imp.setTotalImpuestosRetenidos(new BigDecimal(NumberString.FormatearDecimal(dblTotalImpuestosRetenidos, 2).replace(",", "")));
//                }
//            }
//        }
//
//        objComp.setEmisor(emisor);
//        objComp.setReceptor(receptor);
//        objComp.setConceptos(conceps);
//        if (bolNominas) {
//            if (bolEncontroImpuestos) {
//                objComp.setImpuestos(imp);
//            }
//        } else if (bolEncontroImpuestos) {
//            objComp.setImpuestos(imp);
//        }
//
//        log.debug("Complementos fiscales...");
//        ObtenerComplementos(objFactory, objComp);
//
//        strSelloCFD = GeneraFirma(objComp);
//        objComp.setSello(strSelloCFD);
//        if (strSelloCFD.startsWith("ERROR:")) {
//            strRes = strSelloCFD;
//        }
//
//        if (strRes.equals("OK")) {
//            try {
//                JAXBContext jaxbContext;
//                JAXBContext jaxbContext;
//                if ((bolComplementoFiscal) && (bolDonataria) && (!bolNominas)) {
//                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class});
//                } else {
//                    JAXBContext jaxbContext;
//                    if ((bolComplementoFiscal) && (bolNominas)) {
//                        jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.erp.nominas.ver12.ObjectFactory.class});
//                    } else {
//                        JAXBContext jaxbContext;
//                        if ((bolComplementoFiscal) && (bolImpuestoLocales)) {
//                            jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory.class});
//                        } else {
//                            JAXBContext jaxbContext;
//                            if ((bolComplementoFiscal) && (bolComplementoINE)) {
//                                jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.sat.complementos.ine.ObjectFactory.class});
//                            } else {
//                                JAXBContext jaxbContext;
//                                if (bolEsMc) {
//                                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.sat.complementos.pagos.ObjectFactory.class});
//                                } else {
//                                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class});
//                                }
//                            }
//                        }
//                    }
//                }
//                Marshaller marshaller = jaxbContext.createMarshaller();
//
//                String strListXSD = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd";
//                if ((bolComplementoFiscal) && (bolDonataria) && (!bolNominas)) {
//                    strListXSD = strListXSD + " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
//                }
//                if ((bolComplementoFiscal) && (bolNominas)) {
//                    strListXSD = strListXSD + " http://www.sat.gob.mx/nomina12 http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina12.xsd";
//                }
//                if ((bolComplementoFiscal) && (bolImpuestoLocales)) {
//                    strListXSD = strListXSD + " http://www.sat.gob.mx/implocal http://www.sat.gob.mx/cfd/implocal/implocal.xsd";
//                }
//                if ((bolComplementoFiscal) && (bolComplementoINE)) {
//                    strListXSD = strListXSD + " http://www.sat.gob.mx/ine http://www.sat.gob.mx/sitio_internet/cfd/ine/ine10.xsd";
//                }
//
//                marshaller.setProperty("jaxb.schemaLocation", strListXSD);
//
//                marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
//                marshaller.setProperty("jaxb.encoding", "UTF-8");
//
//                if (satAddenda != null) {
//                    Object prefix = new MyNamespacePrefixMapper();
//                    marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);
//                }
//
//                if (!bolEsLocal) {
//
//                    marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
//                    marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
//                }
//
//                StringBuilder strPathFile = new StringBuilder("");
//                StringBuilder strNomFile = new StringBuilder("");
//                ERP_MapeoFormato mapeoXml = null;
//
//                if (!bolEsNc) {
//                    if (bolNominas) {
//                        mapeoXml = new ERP_MapeoFormato(7);
//                        strNomFile.append(getNombreFileXml(mapeoXml));
//                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
//                    } else if (bolEsNotaCargo) {
//                        mapeoXml = new ERP_MapeoFormato(8);
//                        strNomFile.append(getNombreFileXml(mapeoXml));
//                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
//                    } else if (bolEsNotaCargoProveedor) {
//                        mapeoXml = new ERP_MapeoFormato(9);
//                        strNomFile.append(getNombreFileXml(mapeoXml));
//                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
//                    } else {
//                        mapeoXml = new ERP_MapeoFormato(1);
//                        strNomFile.append(getNombreFileXml(mapeoXml));
//                        strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
//                    }
//                } else {
//                    mapeoXml = new ERP_MapeoFormato(5);
//                    strNomFile.append(getNombreFileXml(mapeoXml));
//                    strPathFile.append(strPath).append(System.getProperty("file.separator")).append(strNomFile);
//                }
//                marshaller.marshal(objComp, new java.io.FileOutputStream(strPathFile.toString()));
//
//                String strRespTimb = GeneraTimbrado(strNomFile.toString());
//
//                if (strRespTimb.equals("OK")) {
//                    String strQResp = null;
//                    if (!bolEsNc) {
//                        if (!bolNominas) {
//                            if (bolEsNotaCargo) {
//                                strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), strfolioFiscalUUID, strPath, intTransaccion, 5);
//                            } else if (bolEsNotaCargoProveedor) {
//                                strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), strfolioFiscalUUID, strPath, intTransaccion, 6);
//                            } else {
//                                strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), strfolioFiscalUUID, strPath, intTransaccion, 1);
//                            }
//                        } else {
//                            strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), strfolioFiscalUUID, strPath, intTransaccion, 3);
//                        }
//                    } else {
//                        strQResp = GeneracionQR.generaQR(objComp.getEmisor().getRfc(), objComp.getReceptor().getRfc(), new DecimalFormat("0000000000.000000").format(objComp.getTotal().doubleValue()), strfolioFiscalUUID, strPath, intTransaccion, 2);
//                    }
//                    if (strQResp.startsWith("OK")) {
//                        strPathQR = strQResp.replace("OK", "");
//                    }
//
//                    if (satAddenda != null) {
//                        satAddenda.makeNameSpaceDeclaration(strPathFile.toString().replace("%UUID%", strfolioFiscalUUID), intTransaccion, oConn);
//                    }
//
//                    if (!bolEsNc) {
//                        if (bolNominas) {
//
//                            String strUpdate = "UPDATE rhh_nominas set NOM_CADENAORIGINAL = '" + strCadenaOriginalReal + "',NOM_SELLO='" + strSelloCFD + "',NOM_CADENA_TIMBRE='" + GeneraCadenaOriginalTimbre() + "',NOM_PATH_CBB='" + strPathQR + "',NOM_NOSERIECERTTIM='" + strNoCertSAT + "',NOM_RFCPROVCERTIF='" + strRfcProvCertif + "',NOM_ES_CFDI33=1  where NOM_ID = " + intTransaccion;
//
//                            oConn.runQueryLMD(strUpdate);
//                        } else if (bolEsNotaCargo) {
//
//                            String strUpdate = "UPDATE vta_notas_cargos set NCA_CADENAORIGINAL = '" + strCadenaOriginalReal + "',NCA_SELLO='" + strSelloCFD + "',NCA_CADENA_TIMBRE='" + GeneraCadenaOriginalTimbre() + "',NCA_PATH_CBB='" + strPathQR + "',NCA_NOSERIECERTTIM='" + strNoCertSAT + "',NCA_TIPOCOMP=" + intEMP_TIPOCOMP + " ,NCA_RFCPROVCERTIF='" + strRfcProvCertif + "',NCA_ES_CFDI33=1 where NCA_ID = " + intTransaccion;
//
//                            oConn.runQueryLMD(strUpdate);
//                        } else if (bolEsNotaCargoProveedor) {
//
//                            String strUpdate = "UPDATE vta_notas_cargosprov set NCA_CADENAORIGINAL = '" + strCadenaOriginalReal + "',NCA_SELLO='" + strSelloCFD + "',NCA_CADENA_TIMBRE='" + GeneraCadenaOriginalTimbre() + "',NCA_PATH_CBB='" + strPathQR + "',NCA_NOSERIECERTTIM='" + strNoCertSAT + "',NCA_TIPOCOMP=" + intEMP_TIPOCOMP + " ,NCA_RFCPROVCERTIF='" + strRfcProvCertif + "',NCA_ES_CFDI33=1 where NCA_ID = " + intTransaccion;
//
//                            oConn.runQueryLMD(strUpdate);
//
//                        } else {
//                            String strUpdate = "UPDATE vta_facturas set FAC_CADENAORIGINAL = '" + strCadenaOriginalReal + "',FAC_SELLO='" + strSelloCFD + "',FAC_CADENA_TIMBRE='" + GeneraCadenaOriginalTimbre() + "',FAC_PATH_CBB='" + strPathQR + "',FAC_NOSERIECERTTIM='" + strNoCertSAT + "',FAC_TIPOCOMP=" + intEMP_TIPOCOMP + " ,FAC_RFCPROVCERTIF='" + strRfcProvCertif + "',FAC_ES_CFDI33=1 where FAC_ID = " + intTransaccion;
//
//                            oConn.runQueryLMD(strUpdate);
//                        }
//
//                    } else {
//                        String strUpdate = "UPDATE vta_ncredito set NC_CADENAORIGINAL = '" + strCadenaOriginalReal + "',NC_SELLO='" + strSelloCFD + "',NC_CADENA_TIMBRE='" + GeneraCadenaOriginalTimbre() + "',NC_PATH_CBB='" + strPathQR + "',NC_NOSERIECERTTIM='" + strNoCertSAT + "',NC_TIPOCOMP=" + intEMP_TIPOCOMP + " ,NC_RFCPROVCERTIF='" + strRfcProvCertif + "',NC_ID_FAC_RELACION=1,NC_ES_CFDI33=1  where NC_ID = " + intTransaccion;
//
//                        oConn.runQueryLMD(strUpdate);
//                    }
//                } else {
//                    strRes = "ERROR:" + strRespTimb;
//                }
//            } catch (FileNotFoundException ex) {
//                log.error(ex.getMessage());
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "genXMLFile", oConn);
//                strRes = "ERROR:" + ex.getMessage();
//            } catch (JAXBException ex) {
//                log.error(ex.getMessage());
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "genXML", oConn);
//                strRes = "ERROR:" + ex.getMessage();
//            }
//
//            if (strRes.equals("OK")) {
//                log.debug("Entramos al mail...");
//                if (bolSendMailMasivo) {
//                    log.debug("envio masivo");
//
//                    if (!bolNominas) {
//                        if (!bolEsNotaCargoProveedor) {
//
//                            try {
//
//                                log.debug("evaluamos formatos...");
//                                if (bolUsoFormatoJasper) {
//                                    log.debug("Genera formato Jasper...");
//                                    String strRespForm = GeneraImpresionPDFJasper(strPath, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO, strTablaDoc, strPrefijoTabla);
//                                    log.debug("strRespForm:" + strRespForm);
//                                    if (!strRespForm.startsWith("ERROR:")) {
//                                        if (!bolEsLocal) {
//                                            try {
//                                                GeneraMail(intClienteEnviaMail, strMailCte, strMailCte2, strMailCte3, strMailCte4, strMailCte5, strMailCte6, strMailCte7, strMailCte8, strMailCte9, strMailCte10, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
//                                            } catch (Exception ex) {
//                                                log.error(" " + ex.getLocalizedMessage());
//                                                log.error(" " + ex.getMessage());
//                                            }
//                                        }
//                                    } else {
//                                        strRes = strRespForm;
//                                    }
//                                } else {
//                                    log.debug("evaluamos impresión PDF..." + strFAC_NOMFORMATO);
//                                    String strRespForm = GeneraImpresionPDF(strPath, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
//                                    if (strRespForm.equals("OK")) {
//                                        if (!bolEsLocal) {
//                                            log.debug("intentamos enviar mail....");
//                                            try {
//                                                GeneraMail(intClienteEnviaMail, strMailCte, strMailCte2, strMailCte3, strMailCte4, strMailCte5, strMailCte6, strMailCte7, strMailCte8, strMailCte9, strMailCte10, intEMP_TIPOCOMP, intEmpId, strFAC_NOMFORMATO);
//                                            } catch (Exception ex) {
//                                                log.error(" " + ex.getLocalizedMessage());
//                                                log.error(" " + ex.getMessage());
//                                            }
//                                        }
//                                    } else {
//                                        strRes = strRespForm;
//                                    }
//                                }
//                            } catch (Exception ex) {
//                                log.error(" Error envio de correos... ");
//                                log.error(" ex " + ex.getMessage());
//                                log.error(" ex " + ex.getLocalizedMessage());
//                                ex.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        return strRes;
//    }
//
//    private void ObtenerComplementos(com.mx.siweb.sat.facturacion33.ObjectFactory objFactory, Comprobante objComp) {
//        if (bolComplementoFiscal) {
//            if ((bolDonataria) && (!bolNominas)) {
//                Core.FirmasElectronicas.complementos.donatarias.Donatarias donataria = ObtenNodoDonatarias();
//                Comprobante.Complemento complemento = objFactory.createComprobanteComplemento();
//                complemento.getAny().add(donataria);
//                objComp.getComplemento().add(complemento);
//            }
//            if (bolNominas) {
//                Nomina nomina = ObtenNodoNominas();
//                Comprobante.Complemento complemento = objFactory.createComprobanteComplemento();
//                complemento.getAny().add(nomina);
//                objComp.getComplemento().add(complemento);
//            }
//            if (bolImpuestoLocales) {
//                ImpuestosLocales impuestosLocales = ObtenNodoImpuestosLocales(objComp);
//                Comprobante.Complemento complemento = objFactory.createComprobanteComplemento();
//                complemento.getAny().add(impuestosLocales);
//                objComp.getComplemento().add(complemento);
//            }
//            if (bolComplementoINE) {
//                INE ine = ObtenNodoINE(objComp);
//                Comprobante.Complemento complemento = objFactory.createComprobanteComplemento();
//                complemento.getAny().add(ine);
//                objComp.getComplemento().add(complemento);
//            }
//            if (bolEsMc) {
//                try {
//                    Pagos pagos = ObtenNodoPagos(objComp);
//                    Comprobante.Complemento complemento = objFactory.createComprobanteComplemento();
//                    complemento.getAny().add(pagos);
//                    objComp.getComplemento().add(complemento);
//                } catch (ParseException ex) {
//                    log.debug("Error en nodo pagos");
//                    log.error(ex.getMessage());
//                }
//            }
//        }
//    }
//
//    protected Pagos ObtenNodoPagos(Comprobante objComp) throws ParseException {
//        log.debug("Comenzamos a llenar complemento de pago");
//        com.mx.siweb.sat.complementos.pagos.ObjectFactory factory = new com.mx.siweb.sat.complementos.pagos.ObjectFactory();
//        Pagos pagos = factory.createPagos();
//        SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMdd");
//        pagos.setVersion("1.0");
//
//        String strPagoDeta = "SELECT MC_FECHA,MC_MONEDA,MC_ABONO,a.FAC_ID,FAC_FOLIO,FAC_SERIE,FAC_FOLIO_C,FAC_METODODEPAGO,FAC_MONEDA,FAC_TASAPESO,FAC_SALDO,MCM_FORMADEPAGO FROM vta_mov_cte a, vta_facturas b, vta_mov_cte_mas c WHERE a.FAC_ID=b.FAC_ID AND c.MCM_ID=a.MCM_ID AND a.MC_ESPAGO=1 AND a.MCM_ID=" + intTransaccion + "";
//
//        log.debug(strPagoDeta);
//        try {
//            ResultSet rs = oConn.runQuery(strPagoDeta, true);
//            while (rs.next()) {
//                Pagos.Pago objPago = factory.createPagosPago();
//                objPago.setFechaPago(formateaDate.parse(rs.getString("MC_FECHA")));
//                objPago.setFormaDePagoP(rs.getString("MCM_FORMADEPAGO"));
//                String strMoneda = rs.getString("MC_MONEDA");
//                String strSqlMoneda = "SELECT MON_DESCRIPCION,MON_SIGLAS FROM vta_monedas  where MON_ID = " + strMoneda;
//
//                ResultSet rs2 = oConn.runQuery(strSqlMoneda, true);
//                while (rs2.next()) {
//                    if (rs2.getString("MON_SIGLAS").isEmpty()) {
//                        strMoneda = rs2.getString("MON_DESCRIPCION");
//                    } else {
//                        strMoneda = rs2.getString("MON_SIGLAS");
//                    }
//                }
//                rs2.close();
//                objPago.setMonedaP(com.mx.siweb.sat.complementos.pagos.CMoneda.fromValue(strMoneda));
//                if (!strMoneda.equals("MXN")) {
//                    objPago.setTipoCambioP(rs.getBigDecimal("FAC_TASAPESO"));
//                }
//                objPago.setMonto(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("MC_ABONO"), 2).replace(",", "")));
//
//                Pagos.Pago.DoctoRelacionado docRel = factory.createPagosPagoDoctoRelacionado();
//                docRel.setIdDocumento(rs.getString("FAC_FOLIO"));
//                if (!rs.getString("FAC_SERIE").isEmpty()) {
//                    docRel.setSerie(rs.getString("FAC_SERIE"));
//                }
//                String strMoneda2 = rs.getString("FAC_MONEDA");
//                docRel.setFolio(rs.getString("FAC_FOLIO_C"));
//                String strSqlMoneda2 = "SELECT MON_DESCRIPCION,MON_SIGLAS FROM vta_monedas  where MON_ID = " + strMoneda2;
//
//                ResultSet rs3 = oConn.runQuery(strSqlMoneda2, true);
//                while (rs3.next()) {
//                    if (rs3.getString("MON_SIGLAS").isEmpty()) {
//                        strMoneda2 = rs3.getString("MON_DESCRIPCION");
//                    } else {
//                        strMoneda2 = rs3.getString("MON_SIGLAS");
//                    }
//                }
//                rs3.close();
//                docRel.setMonedaDR(com.mx.siweb.sat.complementos.pagos.CMoneda.fromValue(strMoneda2));
//                docRel.setImpSaldoAnt(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_SALDO") + rs.getDouble("MC_ABONO"), 2).replace(",", "")));
//                docRel.setImpPagado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("MC_ABONO"), 2).replace(",", "")));
//                docRel.setImpSaldoInsoluto(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_SALDO"), 2).replace(",", "")));
//                if (rs.getString("FAC_METODODEPAGO").equals("PPD")) {
//                    String sqlParcialidad = "SELECT COUNT(*)AS PARCIALIDAD FROM vta_mov_cte WHERE MC_ANULADO=0 AND FAC_ID=" + rs.getString("FAC_ID") + ";";
//                    ResultSet rs4 = oConn.runQuery(sqlParcialidad, true);
//                    String strNumParcialidad = "";
//                    while (rs4.next()) {
//                        strNumParcialidad = rs4.getString("PARCIALIDAD");
//                    }
//                    rs4.close();
//                    docRel.setNumParcialidad(new java.math.BigInteger(strNumParcialidad));
//                }
//                docRel.setMetodoDePagoDR(com.mx.siweb.sat.complementos.pagos.CMetodoPago.fromValue(rs.getString("FAC_METODODEPAGO")));
//                objPago.getDoctoRelacionado().add(docRel);
//
//                pagos.getPago().add(objPago);
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        log.debug("Termina complemento de pagos");
//        return pagos;
//    }
//
//    protected Nomina ObtenNodoNominas() {
//        com.mx.siweb.erp.nominas.ver12.ObjectFactory factory = new com.mx.siweb.erp.nominas.ver12.ObjectFactory();
//
//        SimpleDateFormat formateaDate = new SimpleDateFormat("yyyyMMdd");
//
//        Nomina nomina = factory.createNomina();
//        Nomina.Emisor emisor = factory.createNominaEmisor();
//        Nomina.Receptor receptor = factory.createNominaReceptor();
//        Nomina.Percepciones percepciones = factory.createNominaPercepciones();
//        Nomina.Deducciones deducciones = factory.createNominaDeducciones();
//        Nomina.OtrosPagos otrosPagos = factory.createNominaOtrosPagos();
//
//        nomina.setVersion("1.2");
//
//        int intNumDiasIncapacidad = 0;
//        int intTipoIncapacidad = 0;
//        double dblImporteIncapacidad = 0.0D;
//        boolean bolHaySeparacionJubila = false;
//        int intNOM_HORA_EXTRA_DIAS1 = 0;
//        int intNOM_HORA_EXTRA_HORAS1 = 0;
//        double dblNOM_HORA_EXTRA_IMPORTE1 = 0.0D;
//        int intNOM_HORA_EXTRA_DIAS2 = 0;
//        int intNOM_HORA_EXTRA_HORAS2 = 0;
//        double dblNOM_HORA_EXTRA_IMPORTE2 = 0.0D;
//        int intNOM_HORA_EXTRA_DIAS3 = 0;
//        int intNOM_HORA_EXTRA_HORAS3 = 0;
//        double dblNOM_HORA_EXTRA_IMPORTE3 = 0.0D;
//        String strSql = "select rhh_nominas.*,(select r1.RC_CVE from rhh_regimen_contratacion r1 where r1.RC_ID = rhh_nominas.RC_ID) as tipoRegimen,(select r2.DP_DESCRIPCION from rhh_departamento r2 where r2.DP_ID = rhh_nominas.DP_ID) as Departamento,(select r3.RHP_NOMBRE from rhh_perfil_puesto r3 where r3.RHP_ID = rhh_empleados.RHP_ID) as Puesto,EMP_CURP,EMP_NO_SEG,EMP_CLABE,EMP_FECHA_INICIO_REL_LABORAL,EMP_BANCO,EMP_TIPO_CONTRATO,EMP_TIPO_JORNADA, EMP_PERIODICIDAD_PAGO,EMP_SALARIO_DIARIO,EMP_SALARIO_INTEGRADO,RP_ID,RHN_TIPO,NOM_RFCPATROORIGEN,EMP_SINDICALIZADO,EMP_RFCLABORA,EMP_PORCENTAJETIEMPO,NOM_ORIGENRECURSO,NOM_MONTORECURSOPROPIO\n,NOM_VALORMERCADO,NOM_PERCEPCIONES,NOM_DEDUCCIONES,NOM_VALORMERCADO,NOM_TOTALUNAEXHIBICION,NOM_TOTALPARCIALIDAD\n,NOM_MONTODIARIO,NOM_INGRESOACUMULABLE,NOM_INGRESONOACUMULABLE,NOM_TOTALPAGADO\n,NOM_NUMANIOSSERVICIO,NOM_ULTIMOSUELDOMENSORD,NOM_INGRESOACUMULABLE_SEP,NOM_INGRESONOACUMULABLE_SEP, (select e.ESP_C_EDO from estadospais e where e.ESP_NOMBRE =  EMP_ESTADO) as EDO_CVE from rhh_nominas_master,rhh_nominas,rhh_empleados  where  rhh_nominas_master.RHN_ID = rhh_nominas.RHN_ID and  rhh_nominas.EMP_NUM= rhh_empleados.EMP_NUM  AND NOM_ANULADA = 0 and NOM_SE_TIMBRO = 0 and NOM_ID = " + intTransaccion;
//
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                intNumDiasIncapacidad = rs.getInt("NOM_DIAS_INCAPACIDAD");
//                intTipoIncapacidad = rs.getInt("TI_ID");
//                dblImporteIncapacidad = rs.getDouble("NOM_INCAPACIDAD_DESCUENTO");
//                intNOM_HORA_EXTRA_DIAS1 = rs.getInt("NOM_HORA_EXTRA_DIAS1");
//                intNOM_HORA_EXTRA_HORAS1 = rs.getInt("NOM_HORA_EXTRA_HORAS1");
//                dblNOM_HORA_EXTRA_IMPORTE1 = rs.getInt("NOM_HORA_EXTRA_IMPORTE1");
//                intNOM_HORA_EXTRA_DIAS2 = rs.getInt("NOM_HORA_EXTRA_DIAS2");
//                intNOM_HORA_EXTRA_HORAS2 = rs.getInt("NOM_HORA_EXTRA_HORAS2");
//                dblNOM_HORA_EXTRA_IMPORTE2 = rs.getInt("NOM_HORA_EXTRA_IMPORTE2");
//                intNOM_HORA_EXTRA_DIAS3 = rs.getInt("NOM_HORA_EXTRA_DIAS3");
//                intNOM_HORA_EXTRA_HORAS3 = rs.getInt("NOM_HORA_EXTRA_HORAS3");
//                dblNOM_HORA_EXTRA_IMPORTE3 = rs.getInt("NOM_HORA_EXTRA_IMPORTE3");
//
//                Date dateTmp = null;
//                try {
//                    dateTmp = formateaDate.parse(rs.getString("NOM_FECHA"));
//                    nomina.setFechaPago(dateTmp);
//                    dateTmp = formateaDate.parse(rs.getString("NOM_FECHA_INICIAL_PAGO"));
//                    nomina.setFechaInicialPago(dateTmp);
//                    dateTmp = formateaDate.parse(rs.getString("NOM_FECHA_FINAL_PAGO"));
//                    nomina.setFechaFinalPago(dateTmp);
//                } catch (ParseException ex) {
//                    log.error(ex);
//                }
//                if (rs.getString("RHN_TIPO").equals("E")) {
//                    nomina.setTipoNomina(CTipoNomina.E);
//                } else {
//                    nomina.setTipoNomina(CTipoNomina.O);
//                }
//
//                nomina.setTotalPercepciones(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_PERCEPCIONES"), 3).replace(",", "")));
//                if (rs.getDouble("NOM_DEDUCCIONES") > 0.0D) {
//                    nomina.setTotalDeducciones(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_DEDUCCIONES"), 3).replace(",", "")));
//                }
//
//                nomina.setTotalOtrosPagos(new BigDecimal("0.00"));
//
//                nomina.setNumDiasPagados(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_NUM_DIAS_PAGADOS"), 3).replace(",", "")));
//
//                if (rs.getDouble("NOM_NUM_DIAS_PAGADOS") == 0.0D) {
//                    nomina.setNumDiasPagados(new BigDecimal("0.001"));
//                }
//
//                if (rs.getString("EMP_TIPO_CONTRATO") != null) {
//                    if ((!rs.getString("EMP_TIPO_CONTRATO").equals("09")) && (!rs.getString("EMP_TIPO_CONTRATO").equals("10")) && (!rs.getString("EMP_TIPO_CONTRATO").equals("99"))) {
//
//                        if (!rs.getString("NOM_REGISTRO_PATRONAL").isEmpty()) {
//                            emisor.setRegistroPatronal(rs.getString("NOM_REGISTRO_PATRONAL"));
//                        }
//                    }
//                } else if (!rs.getString("NOM_REGISTRO_PATRONAL").isEmpty()) {
//                    emisor.setRegistroPatronal(rs.getString("NOM_REGISTRO_PATRONAL"));
//                }
//
//                strSql = "SELECT EMP_CURP FROM vta_empresas WHERE EMP_ID = " + intEmpId;
//
//                ResultSet rs3 = oConn.runQuery(strSql, true);
//                while (rs3.next()) {
//                    if (!rs3.getString("EMP_CURP").isEmpty()) {
//                        emisor.setCurp(rs3.getString("EMP_CURP"));
//                    }
//                }
//                rs3.close();
//
//                if (!rs.getString("NOM_RFCPATROORIGEN").isEmpty()) {
//                    emisor.setRfcPatronOrigen(rs.getString("NOM_RFCPATROORIGEN"));
//                }
//
//                if ((rs.getDouble("NOM_MONTORECURSOPROPIO") > 0.0D) && (!rs.getString("NOM_ORIGENRECURSO").isEmpty())) {
//                    if (rs.getString("NOM_ORIGENRECURSO").equals("IP")) {
//                        emisor.getEntidadSNCF().setOrigenRecurso(COrigenRecurso.IP);
//                    } else if (rs.getString("NOM_ORIGENRECURSO").equals("IF")) {
//                        emisor.getEntidadSNCF().setOrigenRecurso(COrigenRecurso.IF);
//                    } else {
//                        emisor.getEntidadSNCF().setOrigenRecurso(COrigenRecurso.IM);
//                    }
//
//                    emisor.getEntidadSNCF().setMontoRecursoPropio(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_MONTORECURSOPROPIO"), 2).replace(",", "")));
//                }
//
//                if (!rs.getString("EMP_CURP").isEmpty()) {
//                    receptor.setCurp(rs.getString("EMP_CURP"));
//                }
//                if (!rs.getString("EMP_NO_SEG").isEmpty()) {
//                    receptor.setNumSeguridadSocial(rs.getString("EMP_NO_SEG"));
//                }
//                if (!rs.getString("EMP_FECHA_INICIO_REL_LABORAL").isEmpty()) {
//                    try {
//                        dateTmp = formateaDate.parse(rs.getString("EMP_FECHA_INICIO_REL_LABORAL"));
//                        receptor.setFechaInicioRelLaboral(dateTmp);
//
//                        long intDiasAnt = Fechas.difDiasEntre2fechasStr(rs.getString("EMP_FECHA_INICIO_REL_LABORAL"), rs.getString("NOM_FECHA_FINAL_PAGO"));
//                        intDiasAnt += 1L;
//                        intDiasAnt = (int) Math.ceil(intDiasAnt / 7L);
//                        log.debug("intDiasAnt:" + intDiasAnt);
//                        receptor.setAntigüedad("P" + intDiasAnt + "W");
//                    } catch (ParseException ex) {
//                        log.error(ex);
//                    }
//                }
//
//                if (rs.getInt("NOM_ANTIGUEDAD") != 0) {
//                    receptor.setAntigüedad("P" + rs.getInt("NOM_ANTIGUEDAD") + "W");
//                }
//
//                if ((rs.getString("EMP_TIPO_CONTRATO") != null)
//                        && (!rs.getString("EMP_TIPO_CONTRATO").isEmpty())) {
//                    receptor.setTipoContrato(rs.getString("EMP_TIPO_CONTRATO"));
//                }
//
//                if ((rs.getString("EMP_TIPO_JORNADA") != null)
//                        && (!rs.getString("EMP_TIPO_JORNADA").isEmpty())) {
//                    receptor.setTipoJornada(rs.getString("EMP_TIPO_JORNADA"));
//                }
//
//                receptor.setTipoRegimen(rs.getString("tipoRegimen"));
//
//                receptor.setNumEmpleado(rs.getString("EMP_NUM"));
//                if ((rs.getString("Departamento") != null)
//                        && (!rs.getString("Departamento").trim().isEmpty())) {
//                    receptor.setDepartamento(rs.getString("Departamento"));
//                }
//
//                if ((rs.getString("Puesto") != null)
//                        && (!rs.getString("Puesto").trim().isEmpty())) {
//                    receptor.setPuesto(rs.getString("Puesto"));
//                }
//
//                if (rs.getInt("RP_ID") > 0) {
//                    receptor.setRiesgoPuesto(rs.getString("RP_ID"));
//                }
//                if (rs.getString("EMP_PERIODICIDAD_PAGO") != null) {
//                    receptor.setPeriodicidadPago(rs.getString("EMP_PERIODICIDAD_PAGO"));
//                }
//                if ((rs.getString("EMP_BANCO") != null)
//                        && (!rs.getString("EMP_BANCO").trim().isEmpty())) {
//                    receptor.setBanco(rs.getString("EMP_BANCO"));
//                }
//
//                if (!rs.getString("NOM_NUMCUENTA").isEmpty()) {
//                    int intNOM_NUMCUENTA = 0;
//                    try {
//                        intNOM_NUMCUENTA = Integer.valueOf(rs.getString("NOM_NUMCUENTA")).intValue();
//                    } catch (NumberFormatException ex) {
//                        log.error(ex.getMessage());
//                    }
//
//                    receptor.setCuentaBancaria(rs.getString("NOM_NUMCUENTA"));
//                }
//
//                if (rs.getDouble("EMP_SALARIO_DIARIO") > 0.0D) {
//                    receptor.setSalarioBaseCotApor(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("EMP_SALARIO_DIARIO"), 2).replace(",", "")));
//                }
//
//                if (rs.getDouble("EMP_SALARIO_INTEGRADO") > 0.0D) {
//                    receptor.setSalarioDiarioIntegrado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("EMP_SALARIO_INTEGRADO"), 2).replace(",", "")));
//                }
//
//                if (rs.getInt("EMP_SINDICALIZADO") == 1) {
//                    receptor.setSindicalizado("Sí");
//                } else {
//                    receptor.setSindicalizado("No");
//                }
//                if ((rs.getString("EDO_CVE") != null)
//                        && (!rs.getString("EDO_CVE").isEmpty())) {
//                    receptor.setClaveEntFed(com.mx.siweb.erp.nominas.ver12.CEstado.fromValue(rs.getString("EDO_CVE")));
//                }
//
//                if (!rs.getString("EMP_RFCLABORA").isEmpty()) {
//                    Nomina.Receptor.SubContratacion sub = factory.createNominaReceptorSubContratacion();
//                    sub.setRfcLabora(rs.getString("EMP_RFCLABORA"));
//                    sub.setPorcentajeTiempo(rs.getBigDecimal("EMP_PORCENTAJETIEMPO"));
//                    receptor.getSubContratacion().add(sub);
//                }
//
//                if ((rs.getDouble("NOM_TOTALUNAEXHIBICION") > 0.0D) || (rs.getDouble("NOM_TOTALPARCIALIDAD") > 0.0D)) {
//                    Nomina.Percepciones.JubilacionPensionRetiro jubila = factory.createNominaPercepcionesJubilacionPensionRetiro();
//                    jubila.setIngresoAcumulable(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_INGRESOACUMULABLE"), 2).replace(",", "")));
//                    jubila.setIngresoNoAcumulable(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_INGRESONOACUMULABLE"), 2).replace(",", "")));
//                    jubila.setMontoDiario(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_MONTODIARIO"), 2).replace(",", "")));
//                    jubila.setTotalParcialidad(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_TOTALPARCIALIDAD"), 2).replace(",", "")));
//                    jubila.setTotalUnaExhibicion(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_TOTALUNAEXHIBICION"), 2).replace(",", "")));
//                    percepciones.setJubilacionPensionRetiro(jubila);
//                    if (rs.getDouble("NOM_TOTALUNAEXHIBICION") > 0.0D) {
//                        percepciones.setTotalJubilacionPensionRetiro(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_TOTALUNAEXHIBICION"), 2).replace(",", "")));
//                    } else {
//                        percepciones.setTotalJubilacionPensionRetiro(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_TOTALPARCIALIDAD"), 2).replace(",", "")));
//                    }
//                    bolHaySeparacionJubila = true;
//                }
//
//                if (rs.getDouble("NOM_TOTALPAGADO") > 0.0D) {
//                    Nomina.Percepciones.SeparacionIndemnizacion separacion = factory.createNominaPercepcionesSeparacionIndemnizacion();
//                    separacion.setIngresoAcumulable(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_INGRESOACUMULABLE"), 2).replace(",", "")));
//                    separacion.setIngresoNoAcumulable(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_INGRESONOACUMULABLE"), 2).replace(",", "")));
//                    separacion.setNumAñosServicio(rs.getInt("NOM_NUMANIOSSERVICIO"));
//                    separacion.setTotalPagado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_TOTALPAGADO"), 2).replace(",", "")));
//                    separacion.setUltimoSueldoMensOrd(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("NOM_ULTIMOSUELDOMENSORD"), 2).replace(",", "")));
//                    percepciones.setSeparacionIndemnizacion(separacion);
//                    bolHaySeparacionJubila = true;
//                }
//            }
//
//            rs.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//
//        double dblPercepcionesGravadas = 0.0D;
//        double dblPercepcionesExentas = 0.0D;
//        double dblDeduccionesRetenidos = 0.0D;
//        double dblDeduccionesOtras = 0.0D;
//        double dblTotalDeducciones = 0.0D;
//        double dblTotalPercepciones = 0.0D;
//        double dblTotalOtrosPagos = 0.0D;
//        boolean bolHayDeducciones = false;
//        boolean bolHayPercepciones = false;
//        boolean bolHayOtrosPagos = false;
//        strSql = "select  \tn.NOMD_ID, \n\tn.NOM_ID, \n\tn.TP_ID, \n(select p1.TP_CVE from rhh_tipo_percepcion p1 where p1.TP_ID = n.TP_ID) as cve_tipo_percep,\n(select p2.TD_CVE from rhh_tipo_deduccion  p2 where p2.TD_ID = n.TD_ID) as cve_tipo_deducc,\n\tn.TI_ID, \n\tn.TD_ID, \n\tn.NOMD_CANTIDAD, \n\tn.NOMD_UNITARIO, \n\tn.NOMD_GRAVADO, \n\tn.NOMD_IMPORTE_GRAVADO, \n\tn.NOMD_IMPORTE_EXENTO, \n\tn.PERC_ID, \n (select e1.PERC_CVE from rhh_percepciones e1 where e1.PERC_ID = n.PERC_ID ) as cve_percep,\n(select e2.PERC_DESCRIPCION from rhh_percepciones e2 where e2.PERC_ID = n.PERC_ID ) as con_percep,\n(select d1.DEDU_CVE from rhh_deducciones d1 where d1.DEDU_ID = n.DEDU_ID ) as cve_deduc,\n(select d2.DEDU_DESCRIPCION from rhh_deducciones d2 where d2.DEDU_ID = n.DEDU_ID ) as con_deduc,(select d1.RTOP_CLAVE from rhh_tipo_otro_pago d1 where d1.RTOP_ID = n.RTOP_ID ) as cve_otros,\n(select d2.RTOP_DESCRIPCION from rhh_tipo_otro_pago d2 where d2.RTOP_ID = n.RTOP_ID ) as con_otros,\tn.DEDU_ID,n.RTOP_ID  from rhh_nominas_deta n  where  n.NOM_ID = " + intTransaccion;
//
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                if (rs.getInt("TP_ID") > 0) {
//                    Nomina.Percepciones.Percepcion percepcion = factory.createNominaPercepcionesPercepcion();
//                    percepcion.setClave(rs.getString("cve_percep"));
//                    percepcion.setTipoPercepcion(rs.getString("cve_tipo_percep"));
//                    percepcion.setConcepto(rs.getString("con_percep"));
//                    double dblGravado = rs.getDouble("NOMD_IMPORTE_GRAVADO");
//                    double dblExento = rs.getDouble("NOMD_IMPORTE_EXENTO");
//
//                    dblPercepcionesGravadas += dblGravado;
//                    dblPercepcionesExentas += dblExento;
//                    percepcion.setImporteGravado(new BigDecimal(NumberString.FormatearDecimal(dblGravado, 2).replace(",", "")));
//                    percepcion.setImporteExento(new BigDecimal(NumberString.FormatearDecimal(dblExento, 2).replace(",", "")));
//                    percepciones.getPercepcion().add(percepcion);
//                    bolHayPercepciones = true;
//                    dblTotalPercepciones += dblGravado + dblExento;
//
//                    if (rs.getString("cve_tipo_percep").equals("019")) {
//
//                        if (intNOM_HORA_EXTRA_DIAS1 > 0) {
//                            Nomina.Percepciones.Percepcion.HorasExtra horasExtra = factory.createNominaPercepcionesPercepcionHorasExtra();
//                            horasExtra.setDias(intNOM_HORA_EXTRA_DIAS1);
//                            horasExtra.setTipoHoras("01");
//                            horasExtra.setHorasExtra(intNOM_HORA_EXTRA_HORAS1);
//                            horasExtra.setImportePagado(new BigDecimal(NumberString.FormatearDecimal(dblNOM_HORA_EXTRA_IMPORTE1, 2).replace(",", "")));
//                            percepcion.getHorasExtra().add(horasExtra);
//                        }
//                        if (intNOM_HORA_EXTRA_DIAS2 > 0) {
//                            Nomina.Percepciones.Percepcion.HorasExtra horasExtra = factory.createNominaPercepcionesPercepcionHorasExtra();
//                            horasExtra.setDias(intNOM_HORA_EXTRA_DIAS2);
//                            horasExtra.setTipoHoras("02");
//                            horasExtra.setHorasExtra(intNOM_HORA_EXTRA_HORAS2);
//                            horasExtra.setImportePagado(new BigDecimal(NumberString.FormatearDecimal(dblNOM_HORA_EXTRA_IMPORTE2, 2).replace(",", "")));
//                            percepcion.getHorasExtra().add(horasExtra);
//                        }
//                        if (intNOM_HORA_EXTRA_DIAS3 > 0) {
//                            Nomina.Percepciones.Percepcion.HorasExtra horasExtra = factory.createNominaPercepcionesPercepcionHorasExtra();
//                            horasExtra.setDias(intNOM_HORA_EXTRA_DIAS3);
//                            horasExtra.setTipoHoras("03");
//                            horasExtra.setHorasExtra(intNOM_HORA_EXTRA_HORAS3);
//                            horasExtra.setImportePagado(new BigDecimal(NumberString.FormatearDecimal(dblNOM_HORA_EXTRA_IMPORTE3, 2).replace(",", "")));
//                            percepcion.getHorasExtra().add(horasExtra);
//                        }
//
//                    }
//
//                } else if (rs.getInt("RTOP_ID") > 0) {
//                    Nomina.OtrosPagos.OtroPago otroPago = factory.createNominaOtrosPagosOtroPago();
//                    otroPago.setTipoOtroPago(rs.getString("cve_otros"));
//                    otroPago.setClave(rs.getString("cve_otros"));
//                    otroPago.setConcepto(rs.getString("con_otros"));
//
//                    double dblImporte = rs.getDouble("NOMD_UNITARIO");
//                    otroPago.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImporte, 2).replace(",", "")));
//                    otrosPagos.getOtroPago().add(otroPago);
//                    if (rs.getString("cve_otros").equals("002")) {
//                        Nomina.OtrosPagos.OtroPago.SubsidioAlEmpleo subsidio = factory.createNominaOtrosPagosOtroPagoSubsidioAlEmpleo();
//                        subsidio.setSubsidioCausado(new BigDecimal(NumberString.FormatearDecimal(dblImporte, 2).replace(",", "")));
//                        otroPago.setSubsidioAlEmpleo(subsidio);
//                    }
//
//                    bolHayOtrosPagos = true;
//                    dblTotalOtrosPagos += dblImporte;
//                } else {
//                    Nomina.Deducciones.Deduccion deduccion = factory.createNominaDeduccionesDeduccion();
//                    deduccion.setTipoDeduccion(rs.getString("cve_tipo_deducc"));
//                    deduccion.setClave(rs.getString("cve_deduc"));
//                    deduccion.setConcepto(rs.getString("con_deduc"));
//                    double dblImporte = rs.getDouble("NOMD_UNITARIO");
//
//                    deduccion.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImporte, 2).replace(",", "")));
//
//                    deducciones.getDeduccion().add(deduccion);
//                    bolHayDeducciones = true;
//                    dblTotalDeducciones += deduccion.getImporte().doubleValue();
//                    if (rs.getString("cve_tipo_deducc").equals("002")) {
//                        dblDeduccionesRetenidos += deduccion.getImporte().doubleValue();
//                    } else {
//                        dblDeduccionesOtras += deduccion.getImporte().doubleValue();
//                    }
//                }
//            }
//
//            rs.close();
//            if (bolHayPercepciones) {
//                percepciones.setTotalGravado(new BigDecimal(NumberString.FormatearDecimal(dblPercepcionesGravadas, 2).replace(",", "")));
//                percepciones.setTotalExento(new BigDecimal(NumberString.FormatearDecimal(dblPercepcionesExentas, 2).replace(",", "")));
//                percepciones.setTotalSueldos(new BigDecimal(NumberString.FormatearDecimal(dblPercepcionesGravadas + dblPercepcionesExentas, 2).replace(",", "")));
//            }
//            if (bolHayDeducciones) {
//                if (dblDeduccionesRetenidos > 0.0D) {
//                    deducciones.setTotalImpuestosRetenidos(new BigDecimal(NumberString.FormatearDecimal(dblDeduccionesRetenidos, 2).replace(",", "")));
//                }
//                deducciones.setTotalOtrasDeducciones(new BigDecimal(NumberString.FormatearDecimal(dblDeduccionesOtras, 2).replace(",", "")));
//            }
//
//            if ((bolHayPercepciones) || (bolHaySeparacionJubila)) {
//                nomina.setPercepciones(percepciones);
//                nomina.setTotalPercepciones(new BigDecimal(NumberString.FormatearDecimal(dblTotalPercepciones, 2).replace(",", "")));
//            }
//
//            if ((bolHayDeducciones)
//                    && (dblTotalDeducciones > 0.0D)) {
//                nomina.setDeducciones(deducciones);
//                nomina.setTotalDeducciones(new BigDecimal(NumberString.FormatearDecimal(dblTotalDeducciones, 2).replace(",", "")));
//            }
//
//            if (bolHayOtrosPagos) {
//                nomina.setOtrosPagos(otrosPagos);
//                nomina.setTotalOtrosPagos(new BigDecimal(NumberString.FormatearDecimal(dblTotalOtrosPagos, 2).replace(",", "")));
//            }
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//
//        if (intNumDiasIncapacidad > 0) {
//            Nomina.Incapacidades.Incapacidad incapacidad = factory.createNominaIncapacidadesIncapacidad();
//            incapacidad.setDiasIncapacidad(intNumDiasIncapacidad);
//            incapacidad.setTipoIncapacidad(intTipoIncapacidad + "");
//            incapacidad.setImporteMonetario(new BigDecimal(NumberString.FormatearDecimal(dblImporteIncapacidad, 2).replace(",", "")));
//            nomina.getIncapacidades().getIncapacidad().add(incapacidad);
//        }
//
//        nomina.setEmisor(emisor);
//        nomina.setReceptor(receptor);
//        return nomina;
//    }
//
//    protected ImpuestosLocales ObtenNodoImpuestosLocales(Comprobante objComp) {
//        Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory factory = new Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory();
//
//        ImpuestosLocales impuestosLocales = factory.createImpuestosLocales();
//
//        String strSql = "select FAC_IMPUESTO2,FAC_TASA2,TI_ID2,TI2_NOMBRE from vta_facturas, vta_tasa_impuesto2  WHERE vta_facturas.TI_ID2 = vta_tasa_impuesto2.TI2_ID AND  FAC_ID = " + intTransaccion;
//
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                impuestosLocales.setVersion("1.0");
//                impuestosLocales.setTotaldeTraslados(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_IMPUESTO2"), 2).replace(",", "")));
//                impuestosLocales.setTotaldeRetenciones(BigDecimal.ZERO);
//
//                ImpuestosLocales.TrasladosLocales traslados = factory.createImpuestosLocalesTrasladosLocales();
//                traslados.setImporte(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_IMPUESTO2"), 2).replace(",", "")));
//                traslados.setTasadeTraslado(new BigDecimal(NumberString.FormatearDecimal(rs.getDouble("FAC_TASA2"), 2).replace(",", "")));
//                traslados.setImpLocTrasladado(rs.getString("TI2_NOMBRE"));
//                impuestosLocales.getRetencionesLocalesAndTrasladosLocales().add(traslados);
//            }
//            if (rs.getStatement() != null) {
//            }
//
//            rs.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//
//        return impuestosLocales;
//    }
//
//    protected INE ObtenNodoINE(Comprobante objComp) {
//        com.mx.siweb.sat.complementos.ine.ObjectFactory factory = new com.mx.siweb.sat.complementos.ine.ObjectFactory();
//
//        INE ine = factory.createINE();
//        ine.setVersion("1.0");
//
//        String strSql = "select  FAC_INE_TIPO_PROC ,FAC_INE_TIPO_COMITE ,FAC_INE_ID_CONTABILIDAD  from vta_facturas where FAC_ID = " + intTransaccion;
//
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                if (rs.getInt("FAC_INE_ID_CONTABILIDAD") != 0) {
//                    ine.setIdContabilidad(Integer.valueOf(rs.getInt("FAC_INE_ID_CONTABILIDAD")));
//                }
//                if (rs.getInt("FAC_INE_TIPO_PROC") != 0) {
//                    if (rs.getInt("FAC_INE_TIPO_PROC") == 1) {
//                        ine.setTipoProceso(TTipoProc.ORDINARIO);
//                    }
//                    if (rs.getInt("FAC_INE_TIPO_PROC") == 2) {
//                        ine.setTipoProceso(TTipoProc.PRECAMPAÑA);
//                    }
//                    if (rs.getInt("FAC_INE_TIPO_PROC") == 3) {
//                        ine.setTipoProceso(TTipoProc.CAMPAÑA);
//                    }
//                }
//                if (rs.getInt("FAC_INE_TIPO_COMITE") != 0) {
//                    if (rs.getInt("FAC_INE_TIPO_COMITE") == 1) {
//                        ine.setTipoComite(TTipoComite.EJECUTIVO_NACIONAL);
//                    }
//                    if (rs.getInt("FAC_INE_TIPO_COMITE") == 2) {
//                        ine.setTipoComite(TTipoComite.EJECUTIVO_ESTATAL);
//                    }
//                }
//            }
//            rs.close();
//
//            strSql = "select  *  from vta_facturas_ine where FAC_ID = " + intTransaccion;
//
//            rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                INE.Entidad entidad = factory.createINEEntidad();
//
//                if (!rs.getString("FINE_AMBITO").equals("")) {
//                    if (rs.getString("FINE_AMBITO").equals("Local")) {
//                        entidad.setAmbito(TAmbito.LOCAL);
//                    }
//                    if (rs.getString("FINE_AMBITO").equals("Federal")) {
//                        entidad.setAmbito(TAmbito.FEDERAL);
//                    }
//                }
//                if (!rs.getString("FINE_ENTIDAD").equals("")) {
//                    defineINEEntidad(rs.getString("FINE_ENTIDAD"), entidad);
//                }
//                if (rs.getInt("FINE_ID_CONTABILIDAD") != 0) {
//                    INE.Entidad.Contabilidad contabilidad = factory.createINEEntidadContabilidad();
//                    contabilidad.setIdContabilidad(rs.getInt("FINE_ID_CONTABILIDAD"));
//                    entidad.getContabilidad().add(contabilidad);
//                }
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//
//        return ine;
//    }
//
//    private void defineINEEntidad(String strEntidadCve, INE.Entidad entidad) {
//        if (strEntidadCve.equals("AGU")) {
//            entidad.setClaveEntidad(TClaveEntidad.AGU);
//        }
//        if (strEntidadCve.equals("BCN")) {
//            entidad.setClaveEntidad(TClaveEntidad.BCN);
//        }
//        if (strEntidadCve.equals("BCS")) {
//            entidad.setClaveEntidad(TClaveEntidad.BCS);
//        }
//        if (strEntidadCve.equals("CAM")) {
//            entidad.setClaveEntidad(TClaveEntidad.CAM);
//        }
//        if (strEntidadCve.equals("CHH")) {
//            entidad.setClaveEntidad(TClaveEntidad.CHH);
//        }
//        if (strEntidadCve.equals("CHP")) {
//            entidad.setClaveEntidad(TClaveEntidad.CHP);
//        }
//        if (strEntidadCve.equals("COA")) {
//            entidad.setClaveEntidad(TClaveEntidad.COA);
//        }
//        if (strEntidadCve.equals("COL")) {
//            entidad.setClaveEntidad(TClaveEntidad.COL);
//        }
//        if (strEntidadCve.equals("DIF")) {
//            entidad.setClaveEntidad(TClaveEntidad.DIF);
//        }
//        if (strEntidadCve.equals("DUR")) {
//            entidad.setClaveEntidad(TClaveEntidad.DUR);
//        }
//        if (strEntidadCve.equals("GRO")) {
//            entidad.setClaveEntidad(TClaveEntidad.GRO);
//        }
//        if (strEntidadCve.equals("GUA")) {
//            entidad.setClaveEntidad(TClaveEntidad.GUA);
//        }
//        if (strEntidadCve.equals("HID")) {
//            entidad.setClaveEntidad(TClaveEntidad.HID);
//        }
//        if (strEntidadCve.equals("JAL")) {
//            entidad.setClaveEntidad(TClaveEntidad.JAL);
//        }
//        if (strEntidadCve.equals("MEX")) {
//            entidad.setClaveEntidad(TClaveEntidad.MEX);
//        }
//        if (strEntidadCve.equals("MIC")) {
//            entidad.setClaveEntidad(TClaveEntidad.MIC);
//        }
//        if (strEntidadCve.equals("MOR")) {
//            entidad.setClaveEntidad(TClaveEntidad.MOR);
//        }
//        if (strEntidadCve.equals("NAY")) {
//            entidad.setClaveEntidad(TClaveEntidad.NAY);
//        }
//        if (strEntidadCve.equals("NLE")) {
//            entidad.setClaveEntidad(TClaveEntidad.NLE);
//        }
//        if (strEntidadCve.equals("OAX")) {
//            entidad.setClaveEntidad(TClaveEntidad.OAX);
//        }
//        if (strEntidadCve.equals("PUE")) {
//            entidad.setClaveEntidad(TClaveEntidad.PUE);
//        }
//        if (strEntidadCve.equals("QTO")) {
//            entidad.setClaveEntidad(TClaveEntidad.QTO);
//        }
//        if (strEntidadCve.equals("ROO")) {
//            entidad.setClaveEntidad(TClaveEntidad.ROO);
//        }
//        if (strEntidadCve.equals("SIN")) {
//            entidad.setClaveEntidad(TClaveEntidad.SIN);
//        }
//        if (strEntidadCve.equals("SON")) {
//            entidad.setClaveEntidad(TClaveEntidad.SON);
//        }
//        if (strEntidadCve.equals("TAB")) {
//            entidad.setClaveEntidad(TClaveEntidad.TAB);
//        }
//        if (strEntidadCve.equals("TAM")) {
//            entidad.setClaveEntidad(TClaveEntidad.TAM);
//        }
//        if (strEntidadCve.equals("TLA")) {
//            entidad.setClaveEntidad(TClaveEntidad.TLA);
//        }
//        if (strEntidadCve.equals("VER")) {
//            entidad.setClaveEntidad(TClaveEntidad.VER);
//        }
//        if (strEntidadCve.equals("YUC")) {
//            entidad.setClaveEntidad(TClaveEntidad.YUC);
//        }
//        if (strEntidadCve.equals("ZAC")) {
//            entidad.setClaveEntidad(TClaveEntidad.ZAC);
//        }
//    }
//
//    private String GeneraCadenaOriginalTimbre() {
//        StringBuilder strCadena = new StringBuilder();
//        strCadena.append("||1.0");
//        strCadena.append("|").append(strfolioFiscalUUID);
//        strCadena.append("|").append(strFechaTimbre);
//        strCadena.append("|").append(strSelloCFD);
//        strCadena.append("|").append(strNoCertSAT);
//        strCadena.append("||");
//        return strCadena.toString();
//    }
//
//    private String GeneraFirma(Comprobante objComp) {
//        String strValorSello = null;
//        if (strPassKey != null) {
//            String strValorEncrip = GeneraCadenaOriginal(objComp);
//            strCadenaOriginalReal = strValorEncrip;
//
//            try {
//                log.debug("Inicia timbrado..." + strPathKey + " " + strPassKey);
//                PrivateKey key = ObtenerPrivateKey(strPathKey, strPassKey);
//                if (key == null) {
//                    strValorSello = "ERROR:NO SE PUDO ABRIR EL SELLO";
//                    log.error(strValorSello);
//                } else {
//                    log.debug("Obtenemos llave...");
//
//                    llavePrivadaEmisor = key.getEncoded();
//
//                    try {
//                        byte[] data = strValorEncrip.getBytes("UTF8");
//                        Signature sig = Signature.getInstance("SHA256withRSA");
//                        sig.initSign(key);
//                        sig.update(data);
//                        log.debug("sellamos...");
//
//                        byte[] signatureBytes = sig.sign();
//
//                        log.debug("base de 64...");
//                        byte[] b64Enc = Base64.encodeBase64(signatureBytes);
//                        strValorSello = new String(b64Enc);
//                    } catch (IOException ex) {
//                        log.error("Error IO " + ex.getMessage());
//                        strValorSello = "ERROR:" + ex.getMessage();
//                    }
//                }
//            } catch (SignatureException ex) {
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "signature", oConn);
//                log.error("kp(1)" + ex.getMessage());
//                strValorSello = "ERROR:" + ex.getMessage();
//            } catch (InvalidKeyException ex) {
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "invalidKey", oConn);
//                log.error("kp(2)" + ex.getMessage());
//                strValorSello = "ERROR:" + ex.getMessage();
//            } catch (NoSuchAlgorithmException ex) {
//                bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "noSuchAlgoritm", oConn);
//                log.error("kp(3)" + ex.getMessage());
//                strValorSello = "ERROR:" + ex.getMessage();
//            }
//        } else {
//            strValorSello = "ERROR:NO ESPECIFICO EL PASSWORD DE LA LLAVE";
//            log.error(strValorSello);
//        }
//
//        return strValorSello;
//    }
//
//    private String GeneraCadenaOriginal(Comprobante objComp) {
//        TransformerFactory tFactory = TransformerFactory.newInstance();
//        StringWriter strCadenaStr = new StringWriter();
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        dbf.setNamespaceAware(true);
//        DocumentBuilder db = null;
//        try {
//            db = dbf.newDocumentBuilder();
//        } catch (ParserConfigurationException ex) {
//            java.util.logging.Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Document doc = db.newDocument();
//        DOMSource xmlDomSource = null;
//        try {
//            log.debug("Comenzamos..." + bolComplementoFiscal + " " + bolNominas);
//            JAXBContext jaxbContext;
//            JAXBContext jaxbContext;
//            if (satAddenda == null) {
//                JAXBContext jaxbContext;
//                if ((bolComplementoFiscal) && (bolDonataria) && (!bolNominas)) {
//                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class});
//                } else {
//                    JAXBContext jaxbContext;
//                    if ((bolComplementoFiscal) && (bolNominas)) {
//                        jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.erp.nominas.ver12.ObjectFactory.class});
//                    } else {
//                        JAXBContext jaxbContext;
//                        if ((bolComplementoFiscal) && (bolImpuestoLocales)) {
//                            jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory.class});
//                        } else {
//                            JAXBContext jaxbContext;
//                            if ((bolComplementoFiscal) && (bolComplementoINE)) {
//                                jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.sat.complementos.ine.ObjectFactory.class});
//                            } else {
//                                JAXBContext jaxbContext;
//                                if (bolEsMc) {
//                                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.sat.complementos.pagos.ObjectFactory.class});
//                                } else {
//                                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class});
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                JAXBContext jaxbContext;
//                if ((bolComplementoFiscal) && (bolDonataria) && (!bolNominas)) {
//                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, Core.FirmasElectronicas.complementos.donatarias.ObjectFactory.class});
//                } else {
//                    JAXBContext jaxbContext;
//                    if ((bolComplementoFiscal) && (bolNominas)) {
//                        jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.erp.nominas.ver12.ObjectFactory.class});
//                    } else {
//                        JAXBContext jaxbContext;
//                        if ((bolComplementoFiscal) && (bolImpuestoLocales)) {
//                            jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, Core.FirmasElectronicas.complementos.impuestoslocales.ObjectFactory.class});
//                        } else {
//                            JAXBContext jaxbContext;
//                            if ((bolComplementoFiscal) && (bolComplementoINE)) {
//                                jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.sat.complementos.ine.ObjectFactory.class});
//                            } else {
//                                JAXBContext jaxbContext;
//                                if (bolEsMc) {
//                                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class, com.mx.siweb.sat.complementos.pagos.ObjectFactory.class});
//                                } else {
//                                    jaxbContext = JAXBContext.newInstance(new Class[]{com.mx.siweb.sat.facturacion33.ObjectFactory.class});
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            log.debug("Comenzamos...2");
//            Marshaller marshaller = jaxbContext.createMarshaller();
//
//            String strListXSD = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd";
//            if ((bolComplementoFiscal) && (bolDonataria) && (!bolNominas)) {
//                strListXSD = strListXSD + " http://www.sat.gob.mx/donat http://www.sat.gob.mx/sitio_internet/cfd/donat/donat11.xsd";
//            }
//            if ((bolComplementoFiscal) && (bolNominas)) {
//                strListXSD = strListXSD + " http://www.sat.gob.mx/nomina12 http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina12.xsd";
//            }
//            if ((bolComplementoFiscal) && (bolImpuestoLocales)) {
//                strListXSD = strListXSD + " http://www.sat.gob.mx/implocal http://www.sat.gob.mx/cfd/implocal/implocal.xsd";
//            }
//            if ((bolComplementoFiscal) && (bolComplementoINE)) {
//                strListXSD = strListXSD + " http://www.sat.gob.mx/ine http://www.sat.gob.mx/sitio_internet/cfd/ine/ine10.xsd";
//            }
//            if (bolEsMc) {
//                strListXSD = strListXSD + " http://www.sat.gob.mx/Pagos http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos10.xsd";
//            }
//            log.debug("Comenzamos...3");
//            marshaller.setProperty("jaxb.schemaLocation", strListXSD);
//
//            marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
//            marshaller.setProperty("jaxb.encoding", "UTF-8");
//
//            if (satAddenda != null) {
//                NamespacePrefixMapper prefix = new MyNamespacePrefixMapper();
//                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefix);
//            }
//
//            if (!bolEsLocal) {
//
//                marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
//                marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
//            }
//            log.debug("Comenzamos...4");
//
//            marshaller.marshal(objComp, doc);
//            log.debug("Comenzamos...5");
//
//            xmlDomSource = new DOMSource(doc);
//            log.debug("Comenzamos...6");
//
//            xmlDomSource.setSystemId("sat.xml");
//            log.debug("Comenzamos...7");
//        } catch (JAXBException ex) {
//            log.error(ex.getMessage());
//            ex.printStackTrace();
//            bitacora.GeneraBitacora(ex.getMessage(), varSesiones.getStrUser(), "genXML" + ex.getMessage(), oConn);
//        }
//        try {
//            log.debug("xmlDomSource:" + xmlDomSource);
//            Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(strPath + System.getProperty("file.separator") + "cadenaoriginal_3_3.xslt"));
//            transformer.transform(xmlDomSource, new javax.xml.transform.stream.StreamResult(strCadenaStr));
//        } catch (TransformerException ex) {
//            java.util.logging.Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
//            log.error(ex.getMessage());
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(SATXml3_0.class.getName()).log(Level.SEVERE, null, ex);
//            log.error(ex.getMessage());
//        }
//        log.debug("***************" + strCadenaStr);
//        return strCadenaStr.toString();
//    }
//
//    protected String getNombreFileXml(ERP_MapeoFormato mapeo) {
//        log.debug("Iniciamos Mapeo..");
//        log.debug("Tipo Comprobante:" + mapeo.getIntTipoComp());
//        log.debug("Nombre Archivo:" + mapeo.getStrNomArchivo());
//        log.debug("Nombre Formato:" + mapeo.getStrNomFormato());
//        log.debug("Nombre:" + mapeo.getStrNomXML("FACTURA"));
//
//        if (strPatronNomXml == null) {
//            String strFechaDocTmp = strFechaCFDI;
//            if (mapeo.getIntTipoComp() == 1) {
//                strPatronNomXml = mapeo.getStrNomXML("FACTURA");
//            }
//            if (mapeo.getIntTipoComp() == 5) {
//                strPatronNomXml = mapeo.getStrNomXML("NCREDITO");
//            }
//            if (mapeo.getIntTipoComp() == 7) {
//                strPatronNomXml = mapeo.getStrNomXML("NOMINA");
//                strFechaDocTmp = strFechaNominaGenera;
//            }
//            if (mapeo.getIntTipoComp() == 8) {
//                strPatronNomXml = mapeo.getStrNomXML("NOTACARGO");
//            }
//            if (mapeo.getIntTipoComp() == 9) {
//                strPatronNomXml = mapeo.getStrNomXML("NOTACARGOPROV");
//            }
//            log.debug("strNomFileXml:" + strNomFileXml);
//            log.debug("strPatronNomXml:" + strPatronNomXml);
//            log.debug("strNomIniXml:" + strNomIniXml);
//            log.debug("intTransaccion:" + intTransaccion);
//            log.debug("strNombreReceptor:" + strNombreReceptor);
//            strNomFileXml = strPatronNomXml.replace("%Transaccion%", intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaDocTmp).replace(" ", "_");
//            if (strNomFileXml.contains("%RFC%")) {
//                strNomFileXml = strNomFileXml.replace("%RFC%", objComp.getEmisor().getRfc());
//            }
//        }
//        log.debug("strNomFileXml.." + strNomFileXml);
//        return strNomFileXml;
//    }
//
//    protected String GeneraMail(int intClienteEnviaMail, String strMailCte, String strMailCte2, String strMailCte3, String strMailCte4, String strMailCte5, String strMailCte6, String strMailCte7, String strMailCte8, String strMailCte9, String strMailCte10, int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO) {
//        String strResp = "OK";
//
//        String strsmtp_server = "";
//        String strsmtp_user = "";
//        String strsmtp_pass = "";
//        String strsmtp_port = "";
//        String strsmtp_usaTLS = "";
//        String strsmtp_usaSTLS = "";
//        String strNumFolio = strFolio;
//
//        String strSql = "select * from cuenta_contratada where ctam_id = 1";
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strsmtp_server = rs.getString("smtp_server");
//                strsmtp_user = rs.getString("smtp_user");
//                strsmtp_pass = rs.getString("smtp_pass");
//                strsmtp_port = rs.getString("smtp_port");
//                strsmtp_usaTLS = rs.getString("smtp_usaTLS");
//                strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
//            }
//            if (rs.getStatement() != null) {
//            }
//
//            rs.close();
//        } catch (SQLException ex) {
//            java.util.logging.Logger.getLogger(SATXml.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        String strNomTemplate = "FACTURA";
//        if (bolEsNc) {
//            strNomTemplate = "NCREDITO";
//        }
//        if (bolEsNotaCargo) {
//            strNomTemplate = "NOTA_CARGO";
//        }
//        String[] lstMail = getMailTemplate(strNomTemplate);
//
//        if ((!strsmtp_server.equals(""))
//                && (!strsmtp_user.equals(""))
//                && (!strsmtp_pass.equals(""))) {
//            Mail mail = new Mail();
//            mail.setBolDepuracion(false);
//
//            if (intEMP_ACUSEFACTURA == 1) {
//                mail.setBolAcuseRecibo(true);
//            }
//
//            String strLstMail = "";
//
//            strSql = "select EMAIL from usuarios where BOL_MAIL_FACT = 1 and (select count(UE_ID) from vta_userempresa  WHERE EMP_ID = " + varSesiones.getIntIdEmpresa() + " AND ID_USUARIOS = usuarios.id_usuarios ) > 0 ";
//            try {
//                ResultSet rs = oConn.runQuery(strSql, true);
//                while (rs.next()) {
//                    if (mail.isEmail(rs.getString("EMAIL"))) {
//                        strLstMail = strLstMail + rs.getString("EMAIL") + ",";
//                    }
//                }
//                if (rs.getStatement() != null) {
//                }
//
//                rs.close();
//                if (strLstMail.endsWith(",")) {
//                    strLstMail = strLstMail.substring(0, strLstMail.length() - 1);
//                }
//            } catch (SQLException ex) {
//                log.error(ex.getMessage());
//            }
//
//            boolean bolMailCteValido1 = false;
//            boolean bolMailCteValido2 = false;
//            boolean bolMailCteValido3 = false;
//            boolean bolMailCteValido4 = false;
//            boolean bolMailCteValido5 = false;
//            boolean bolMailCteValido6 = false;
//            boolean bolMailCteValido7 = false;
//            boolean bolMailCteValido8 = false;
//            boolean bolMailCteValido9 = false;
//            boolean bolMailCteValido10 = false;
//            if (mail.isEmail(strMailCte)) {
//                bolMailCteValido1 = true;
//            }
//            if (mail.isEmail(strMailCte2)) {
//                bolMailCteValido2 = true;
//            }
//            if (mail.isEmail(strMailCte3)) {
//                bolMailCteValido3 = true;
//            }
//            if (mail.isEmail(strMailCte4)) {
//                bolMailCteValido4 = true;
//            }
//            if (mail.isEmail(strMailCte5)) {
//                bolMailCteValido5 = true;
//            }
//            if (mail.isEmail(strMailCte6)) {
//                bolMailCteValido6 = true;
//            }
//            if (mail.isEmail(strMailCte7)) {
//                bolMailCteValido7 = true;
//            }
//            if (mail.isEmail(strMailCte8)) {
//                bolMailCteValido8 = true;
//            }
//            if (mail.isEmail(strMailCte9)) {
//                bolMailCteValido9 = true;
//            }
//            if (mail.isEmail(strMailCte10)) {
//                bolMailCteValido10 = true;
//            }
//
//            if (!strListMailsEsp.isEmpty()) {
//                strLstMail = strLstMail + "," + strListMailsEsp;
//            }
//
//            if ((!strLstMail.equals("")) || (bolMailCteValido1) || (bolMailCteValido2) || (bolMailCteValido3) || (bolMailCteValido4) || (bolMailCteValido5) || (bolMailCteValido6) || (bolMailCteValido7) || (bolMailCteValido8) || (bolMailCteValido9) || (bolMailCteValido10)) {
//                String strMsgMail = lstMail[1];
//                strMsgMail = strMsgMail.replace("%folio%", strNumFolio);
//                String strAsunto = lstMail[0].replace("%folio%", strNumFolio);
//                mail.setAsunto(strAsunto);
//
//                mail.setMensaje(strMsgMail);
//
//                String strSqlMail = "select * from vta_facturas where FAC_ID = " + intTransaccion;
//                if (bolEsNc) {
//                    strSqlMail = "select * from vta_ncredito where NC_ID = " + intTransaccion;
//                }
//                if (bolEsNotaCargo) {
//                    strSqlMail = "select * from vta_notas_cargos where NCA_ID = " + intTransaccion;
//                }
//                try {
//                    ResultSet rsMail = oConn.runQuery(strSqlMail, true);
//                    mail.setReplaceContent(rsMail);
//                    rsMail.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                } catch (Exception ex) {
//                    log.error(ex.getMessage());
//                }
//
//                strSqlMail = "select * from vta_empresas where EMP_ID = " + varSesiones.getIntIdEmpresa();
//                try {
//                    ResultSet rsMail = oConn.runQuery(strSqlMail, true);
//                    mail.setReplaceContent(rsMail);
//                    rsMail.close();
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                } catch (Exception ex) {
//                    log.error(ex.getMessage());
//                }
//
//                mail.setUsuario(strsmtp_user);
//                mail.setContrasenia(strsmtp_pass);
//                mail.setHost(strsmtp_server);
//                mail.setPuerto(strsmtp_port);
//
//                if (!bolEsNc) {
//                    if (bolEsNotaCargo) {
//                        log.debug("Adjunto....XML:" + strNomFileXml);
//                        log.debug("Adjunto....PDF:" + strNomFilePdf);
//                        mail.setFichero(strPath + strNomFileXml);
//                        mail.setFichero(strPath + strNomFilePdf);
//                    } else {
//                        log.debug("Adjunto....XML:" + strNomFileXml);
//                        log.debug("Adjunto....PDF:" + strNomFilePdf);
//                        mail.setFichero(strPath + strNomFileXml);
//                        mail.setFichero(strPath + strNomFilePdf);
//                    }
//                } else {
//                    log.debug("Adjunto....XML:" + strNomFileXml);
//                    log.debug("Adjunto....PDF:" + strNomFilePdf);
//                    mail.setFichero(strPath + strNomFileXml);
//                    mail.setFichero(strPath + strNomFilePdf);
//                }
//                if (strsmtp_usaTLS.equals("1")) {
//                    mail.setBolUsaTls(true);
//                }
//                if (strsmtp_usaSTLS.equals("1")) {
//                    mail.setBolUsaStartTls(true);
//                }
//
//                if (!strLstMail.equals("")) {
//                    mail.setDestino(strLstMail);
//                    boolean bol = mail.sendMail();
//                    if (bol) {
//                    }
//                }
//
//                if ((bolMailCteValido1) && (blEmail1)) {
//                    log.debug("Envio mail 1 de cliente  " + strMailCte);
//                    mail.setDestino(strMailCte);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido2) && (blEmail2)) {
//                    log.debug("Envio mail 2 de cliente  " + strMailCte2);
//                    mail.setDestino(strMailCte2);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido3) && (blEmail3)) {
//                    log.debug("Envio mail 3 de cliente  " + strMailCte3);
//                    mail.setDestino(strMailCte3);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido4) && (blEmail4)) {
//                    log.debug("Envio mail 4 de cliente  " + strMailCte4);
//                    mail.setDestino(strMailCte4);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido5) && (blEmail5)) {
//                    log.debug("Envio mail 5 de cliente  " + strMailCte5);
//                    mail.setDestino(strMailCte5);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido6) && (blEmail6)) {
//                    log.debug("Envio mail 6 de cliente  " + strMailCte6);
//                    mail.setDestino(strMailCte6);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido7) && (blEmail7)) {
//                    log.debug("Envio mail 7 de cliente  " + strMailCte7);
//                    mail.setDestino(strMailCte7);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido8) && (blEmail8)) {
//                    log.debug("Envio mail 8 de cliente  " + strMailCte8);
//                    mail.setDestino(strMailCte8);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido9) && (blEmail9)) {
//                    log.debug("Envio mail 9 de cliente  " + strMailCte9);
//                    mail.setDestino(strMailCte9);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//
//                if ((bolMailCteValido10) && (blEmail10)) {
//                    log.debug("Envio mail 10 de cliente  " + strMailCte10);
//                    mail.setDestino(strMailCte10);
//                    boolean bol = mail.sendMail();
//                    if (!bol) {
//                        strResp = "Fallo el envio del Mail.";
//                    }
//                }
//            }
//
//            if ((!bolMailCteValido1) && (!bolMailCteValido2) && (!bolMailCteValido3) && (!bolMailCteValido4) && (!bolMailCteValido5) && (!bolMailCteValido6) && (!bolMailCteValido7) && (!bolMailCteValido8) && (!bolMailCteValido9) && (!bolMailCteValido10)) {
//                strResp = "El mail no es valido..";
//            }
//        }
//        return strResp;
//    }
//
//    protected String GeneraImpresionPDFJasper(String strPath, int intEMP_TIPOCOMP, int intEmpId, String strFAC_NOMFORMATO, String strTabla, String strPrefijo)
//            throws SQLException {
//        log.debug("Generando formato en jasper... FOLIO:" + strFolio + " EMP_ID:" + this.intEmpId + " SC_ID:" + intSucursal + "");
//
//        ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
//        strNomFilePdf = getNombreFilePDF(mapeo).replace("%UUID%", strfolioFiscalUUID);
//
//        ProcesoMaster proceso = new ProcesoMaster(oConn, varSesiones);
//        proceso.setStrPATHBase(strPATHBase);
//        proceso.setStrNomFileJasper(strNomFilePdf);
//        log.debug("strPATHBase:" + strPATHBase);
//        log.debug("strNomFilePdf:" + strNomFilePdf);
//
//        String[] lstParamsName = {"doc_folio1", "doc_folio2", "emp_id", "sc_id", "doc_id"};
//        String[] lstParamsValue = {strFolio, strFolio, this.intEmpId + "", intSucursal + "", "0"};
//
//        String sqlFormato = "SELECT REP_ID from " + strTabla + " a, vta_tipocomp b, repo_master c WHERE a." + strPrefijo + "TIPOCOMP=b.TCF_ID AND a.EMP_ID=" + this.intEmpId + " AND a." + strPrefijo + "FOLIO_C>='" + strFolio + "' AND a." + strPrefijo + "FOLIO_C<='" + strFolio + "' AND b.TCF_FORMATO_ABRV=c.REP_ABRV;";
//
//        log.debug("sqlFormato: " + sqlFormato);
//        int intReporte = 0;
//        ResultSet rs = oConn.runQuery(sqlFormato, true);
//        while (rs.next()) {
//            intReporte = rs.getInt("REP_ID");
//        }
//        rs.close();
//
//        String strResultF = proceso.doGeneraFormatoJasper(intReporte, strNomFilePdf, "PDF", null, lstParamsName, lstParamsValue, strPath);
//
//        log.debug("strResultF:" + strResultF);
//        return strResultF;
//    }
//
//    private String getNombreFilePDF(ERP_MapeoFormato mapeo) {
//        if (strNomFilePdf == null) {
//            String strFechaDocTmp = strFechaCFDI;
//
//            if ((mapeo.getIntTipoComp() == 1)
//                    || (mapeo.getIntTipoComp() == 2)
//                    || (mapeo.getIntTipoComp() == 3)
//                    || (mapeo.getIntTipoComp() == 4)) {
//                log.debug("Aqui estamos....");
//                strPatronNomPDF = mapeo.getStrNomPDF("FACTURA");
//            }
//            if (mapeo.getIntTipoComp() == 5) {
//                strPatronNomPDF = mapeo.getStrNomPDF("NCREDITO");
//            }
//            if (mapeo.getIntTipoComp() == 7) {
//                strPatronNomPDF = mapeo.getStrNomPDF("NOMINA");
//                strFechaDocTmp = strFechaNominaGenera;
//            }
//            if (mapeo.getIntTipoComp() == 8) {
//                strPatronNomPDF = mapeo.getStrNomPDF("NOTACARGO");
//                strFechaDocTmp = strFechaNominaGenera;
//            }
//            strNomFilePdf = (strPatronNomPDF.replace("%nom_archivo%", mapeo.getStrNomArchivo()).replace("%strNumFolio%", strFolio).replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaDocTmp).replace(" ", "_") + "");
//            if (strNomFilePdf.contains("%RFC%")) {
//                strNomFilePdf = strNomFilePdf.replace("%RFC%", objComp.getEmisor().getRfc());
//            }
//        }
//
//        return strNomFilePdf;
//    }
//
//    protected String GeneraTimbrado(String strNomFile) {
//        String strRes = "OK";
//        Timbrado_Pacs timbrado = null;
//        if (enumPAC == SATXml3_3.PACS_CFDIS.FACTURA_EN_SEGUNDOS) {
//            timbrado = new TimbradoFacturaSegundos(strPathConfigPAC);
//        }
//        if (enumPAC == SATXml3_3.PACS_CFDIS.TIMBRE_FISCAL) {
//            timbrado = new TimbradoTimbreFiscal(strPathConfigPAC);
//        }
//        if (timbrado != null) {
//            timbrado.setIntIdDoc(intTransaccion);
//            if (!bolEsNc) {
//                if (bolNominas) {
//                    timbrado.setStrTablaDoc("rhh_nominas");
//                    timbrado.setStrPrefijoDoc("NOM_");
//                    timbrado.setBolNomina(true);
//                } else if (bolEsNotaCargo) {
//                    timbrado.setStrTablaDoc("vta_notas_cargos");
//                    timbrado.setStrPrefijoDoc("NCA_");
//                } else if (bolEsNotaCargoProveedor) {
//                    timbrado.setStrTablaDoc("vta_notas_cargosprov");
//                    timbrado.setStrPrefijoDoc("NCA_");
//                } else if (bolEsMc) {
//                    timbrado.setStrTablaDoc("vta_mov_cte_mas");
//                    timbrado.setStrPrefijoDoc("MCM_");
//                } else {
//                    timbrado.setStrTablaDoc("vta_facturas");
//                    timbrado.setStrPrefijoDoc("FAC_");
//                }
//            } else {
//                timbrado.setStrTablaDoc("vta_ncredito");
//                timbrado.setStrPrefijoDoc("NC_");
//            }
//            timbrado.setStrPathXml(strPath);
//            timbrado.setoConn(oConn);
//            timbrado.setLlavePrivadaEmisor(llavePrivadaEmisor);
//            timbrado.setCertificadoEmisor(certificadoEmisor);
//            timbrado.setStrPasswordEmisor(strPassKey);
//            timbrado.setVersionCFDI("3.3");
//            strRes = timbrado.timbra_Factura(strNomFile);
//            strfolioFiscalUUID = timbrado.getStrfolioFiscalUUID();
//            strFechaTimbre = timbrado.getStrFechaTimbre();
//            strSelloSAT = timbrado.getStrSelloSAT();
//            strNoCertSAT = timbrado.getStrNoCertSAT();
//            strRfcProvCertif = timbrado.getStrRfcProvCertif();
//            if (strfolioFiscalUUID != null) {
//                strNomFileXml = strNomFileXml.replace("%UUID%", strfolioFiscalUUID);
//            }
//        } else {
//            strRes = "ERROR: EL PAC no existe";
//        }
//        return strRes;
//    }
//}
