package ERP;

import Core.FirmasElectronicas.Opalina;
import Core.FirmasElectronicas.SATXml;
import Core.FirmasElectronicas.SATXml3_0;
import Core.FirmasElectronicas.SatCancelaCFDI;
import Core.FirmasElectronicas.Utils.UtilCert;
import ERP.BusinessEntities.PoliCtas;
import Tablas.vta_cotiza;
import Tablas.vta_facturadeta;
import Tablas.vta_facturas;
import Tablas.vta_mov_cte_deta;
import Tablas.vta_movproddeta;
import Tablas.vta_pedidos;
import Tablas.vta_pedidos_cajas;
import Tablas.vta_pedidos_cajas_master;
import Tablas.vta_pedidosdeta;
import Tablas.vta_tickets;
import Tablas.vta_ticketsdeta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Mail;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Este objeto genera las transacciones de ventas de tickets
 *
 * @author zeus
 */
public class Ticket extends ProcesoMaster implements ProcesoInterfaz {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    protected TableMaster document;
    protected ArrayList<TableMaster> lstMovs;
    protected ArrayList<vta_mov_cte_deta> lstPagos;
    protected String strFechaAnul;
    protected int intSistemaCostos = 0;
    protected String strPrefijoMaster;
    protected String strPrefijoDeta;
    protected String strNomTablaMaster;
    protected String strNomTablaDeta;
    protected String strTipoVta;
    public final static String TICKET = "TICKET";
    public final static String FACTURA = "FACTURA";
    public final static String COTIZACION = "COTIZACION";
    public final static String PEDIDO = "PEDIDO";
    protected String strPATHXml;
    protected String strPATHKeys;
    protected String strMyPassSecret;
    protected boolean bolEsLocal = false;
    protected String strPATHFonts;
    protected boolean bolAfectaInv = true;
    protected boolean bolFacturaTicket = false;
    protected int intPedidoGenero = 0;
    //Campos para impuestos a usar en el BakOrder
    protected int intSC_SOBRIMP1_2 = 0;
    protected int intSC_SOBRIMP1_3 = 0;
    protected int intSC_SOBRIMP2_3 = 0;
    protected double dblSC_TASA1 = 0;
    protected double dblSC_TASA2 = 0;
    protected double dblSC_TASA3 = 0;
    protected int intEMP_ID = 0;
    protected int intEMP_TIPOCOMP = 0;
    protected boolean bolSendMailMasivo;
    protected int intFAC_TIPOCOMP = 0;
    //Arreglos para guardar Anticipos
    protected int[] arID_Ant;
    protected double[] arTotalAntUsar;
    protected int numAnticipos;
    protected SATXml SAT = null;
    protected boolean bolAfectoCargosAbonos;
    /**
     * Cuenta contable por default en las partidas
     */
    protected String strCtaEmpty;
    protected boolean bolAvisoFolios = true;
    protected boolean bolQuedanPocosFolios = false;
    protected int intFoliosRestantes = 0;//Contiene la cantidad de folios restantes despues de esta venta
    protected boolean bolUsoLugarExpEmp = false;
    protected int[] lstPedidosOrigen = null;
    protected ArrayList<vta_movproddeta> lstSeries;
    private static final Logger log = LogManager.getLogger(Ticket.class.getName());
    protected boolean bolUsaTrxComoFolio = false;
    protected boolean bolRecoveryIdLost = true;

    /**
     * emial's
     *
     */
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

    /**
     * email's
     */
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

    public int[] getArID_Ant() {
        return arID_Ant;
    }

    public void setArID_Ant(int[] arID_Ant) {
        this.arID_Ant = arID_Ant;
    }

    public double[] getArTotalAntUsar() {
        return arTotalAntUsar;
    }

    public void setArTotalAntUsar(double[] arTotalAntUsar) {
        this.arTotalAntUsar = arTotalAntUsar;
    }

    public int getNumAnticipos() {
        return numAnticipos;
    }

    public void setNumAnticipos(int numAnticipos) {
        this.numAnticipos = numAnticipos;
    }

    public boolean isBolRecoveryIdLost() {
        return bolRecoveryIdLost;
    }

    public void setBolRecoveryIdLost(boolean bolRecoveryIdLost) {
        this.bolRecoveryIdLost = bolRecoveryIdLost;
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
     * Regresa la bandera de si quedan pocos folios
     *
     * @return Regresa true si quedan pocos folios
     */
    public boolean isBolQuedanPocosFolios() {
        return bolQuedanPocosFolios;
    }

    /**
     * Define si quedan pocos folios
     *
     * @param bolQuedanPocosFolios Con true definimos si quedan pocos folios
     */
    public void setBolQuedanPocosFolios(boolean bolQuedanPocosFolios) {
        this.bolQuedanPocosFolios = bolQuedanPocosFolios;
    }

    /**
     * Regresa la cantidad de folios que quedan despues de la ultima venta
     *
     * @return Es un entero con el numero de folios restantes
     */
    public int getIntFoliosRestantes() {
        return intFoliosRestantes;
    }

    /**
     * Define la cantidad de folios que quedan
     *
     * @param intFoliosRestantes Es un entero con el numero de folios restantes
     */
    public void setIntFoliosRestantes(int intFoliosRestantes) {
        this.intFoliosRestantes = intFoliosRestantes;
    }

    /**
     * Regresa el movimiento maestro de tickets
     *
     * @return Nos regresa el movimiento de tickets
     */
    public TableMaster getDocument() {
        return this.document;
    }

    /**
     * Anade un objeto de detalle al arreglo de objetos
     *
     * @param deta Es el objeto de detalle
     */
    public void AddDetalle(TableMaster deta) {
        this.lstMovs.add(deta);
    }

    /**
     * Nos regresa el sistema de costos PEPS UEPS Y PROMEDIO
     *
     * @return es un entero(ver detalles en clase INVENTARIOS)
     */
    public int getIntSistemaCostos() {
        return intSistemaCostos;
    }

    /**
     * Definimos el sistema de costos PEPS UEPS Y PROMEDIO
     *
     * @param intSistemaCostos es un entero(ver detalles en clase INVENTARIOS)
     */
    public void setIntSistemaCostos(int intSistemaCostos) {
        this.intSistemaCostos = intSistemaCostos;
    }

    /**
     * Anade un objeto de forma de pago al arreglo de objetos
     *
     * @param deta Es el objeto de detalle
     */
    public void AddDetalle(vta_mov_cte_deta deta) {
        this.lstPagos.add(deta);
    }

    /**
     * Nos regresa el valor del tipo de venta
     *
     * @return son las cadenas TICKET FACTURA PEDIDO COTIZACION
     */
    public String getStrTipoVta() {
        return strTipoVta;
    }

    /**
     * Establece el valor del tipo de venta
     *
     * @param strTipoVta son las cadenas TICKET FACTURA PEDIDO COTIZACION
     */
    public void setStrTipoVta(String strTipoVta) {
        if (strTipoVta.equals(Ticket.TICKET)) {
            this.strTipoVta = strTipoVta;
            this.strPrefijoMaster = "TKT";
            this.strPrefijoDeta = "TKTD";
            this.document = new vta_tickets();
        }
        if (strTipoVta.equals(Ticket.FACTURA)) {
            this.strTipoVta = strTipoVta;
            this.strNomTablaMaster = "vta_facturas";
            this.strNomTablaDeta = "vta_facturasdeta";
            this.strPrefijoMaster = "FAC";
            this.strPrefijoDeta = "FACD";
            this.document = new vta_facturas();
        }
        if (strTipoVta.equals(Ticket.PEDIDO)) {
            this.strTipoVta = strTipoVta;
            this.strNomTablaMaster = "vta_pedidos";
            this.strNomTablaDeta = "vta_pedidosdeta";
            this.strPrefijoMaster = "PD";
            this.strPrefijoDeta = "PDD";
            this.document = new vta_pedidos();
        }
        if (strTipoVta.equals(Ticket.COTIZACION)) {
            this.strTipoVta = strTipoVta;
            this.strNomTablaMaster = "vta_cotiza";
            this.strNomTablaDeta = "vta_cotizadeta";
            this.strPrefijoMaster = "COT";
            this.strPrefijoDeta = "COTD";
            this.document = new vta_cotiza();
        }
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
     * Nos dice si el movimiento afecta inventarioos
     *
     * @return Regres true/false
     */
    public boolean isBolAfectaInv() {
        return bolAfectaInv;
    }

    /**
     * Definimos si el movimiento afecta inventarios
     *
     * @param bolAfectaInv Definimos true/false
     */
    public void setBolAfectaInv(boolean bolAfectaInv) {
        this.bolAfectaInv = bolAfectaInv;
    }

    /**
     * Definimos si esta es una operacion de factura de ticket
     *
     * @return regresa un valor boolean
     */
    public boolean isBolFacturaTicket() {
        return bolFacturaTicket;
    }

    /**
     * Definimos si esta es una operacion de factura de ticket
     *
     * @param bolFacturaTicket Es un valor boolean
     */
    public void setBolFacturaTicket(boolean bolFacturaTicket) {
        this.bolFacturaTicket = bolFacturaTicket;
    }

    /**
     * Regresa el id del pedido que genero la venta
     *
     * @return Nos regresa un valor numerico
     */
    public int getIntPedidoGenero() {
        return intPedidoGenero;
    }

    /**
     * Establece el id del pedido que genero la venta
     *
     * @param intPedidoGenero Es un valor numerido
     */
    public void setIntPedidoGenero(int intPedidoGenero) {
        this.intPedidoGenero = intPedidoGenero;
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
     * Definimos la palabra secreta
     *
     * @param strMyPassSecret Es un cadena
     */
    public void setStrMyPassSecret(String strMyPassSecret) {
        this.strMyPassSecret = strMyPassSecret;
    }

    /**
     * Nos regresa el id de la empresa default
     *
     * @return Es un numero con el id de la empresa
     */
    public int getIntEMP_ID() {
        return intEMP_ID;
    }

    /**
     * Definimos el id de la empresa
     *
     * @param intEMP_ID Es un numero con el id de la empresa
     */
    public void setIntEMP_ID(int intEMP_ID) {
        this.intEMP_ID = intEMP_ID;
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
     * Nos regresa el tipo de comprobante seleccionado
     *
     * @return Regresa el id del comprobante
     */
    public int getIntFAC_TIPOCOMP() {
        return intFAC_TIPOCOMP;
    }

    /**
     * Es el tipo de comprobante de la factura
     *
     * @param intFAC_TIPOCOMP Es el id del comprobante
     */
    public void setIntFAC_TIPOCOMP(int intFAC_TIPOCOMP) {
        this.intFAC_TIPOCOMP = intFAC_TIPOCOMP;
    }

    /**
     * Regresa el objeto a usar para el XML del SAT
     *
     * @return Regresa el objeto del SAT
     */
    public SATXml getSAT() {
        return SAT;
    }

    /**
     * Define el objeto a usar para el XML del SAT
     *
     * @param SAT Define el objeto del SAT
     */
    public void setSAT(SATXml SAT) {
        this.SAT = SAT;
    }

    /**
     * Regresa cuenta vacia para cuando las partidas no estan categorizadas
     *
     * @return Es una cadena
     */
    public String getStrCtaEmpty() {
        return strCtaEmpty;
    }

    /**
     * Define cuenta vacia para cuando las partidas no estan categorizadas
     *
     * @param strCtaEmpty Es una cadena
     */
    public void setStrCtaEmpty(String strCtaEmpty) {
        this.strCtaEmpty = strCtaEmpty;
    }

    /**
     * Regresa si se envian avisos por folios
     *
     * @return Es un boolean
     */
    public boolean isBolAvisoFolios() {
        return bolAvisoFolios;
    }

    /**
     * Indica si se envian avisos cuando queden pocos folios
     *
     * @param bolAvisoFolios Es un boolean
     */
    public void setBolAvisoFolios(boolean bolAvisoFolios) {
        this.bolAvisoFolios = bolAvisoFolios;
    }

    /**
     * Define la lista de numeros de serie ocupados por un pedido facturado o
     * remisionado
     *
     * @return Regresa la lista de movimientos de almacen con el numero de serie
     */
    public ArrayList<vta_movproddeta> getLstSeries() {
        return lstSeries;
    }

    /**
     * Regresa la lista de numeros de serie ocupados por un pedido facturado o
     * remisionado
     *
     * @param lstSeries Regresa la lista de movimientos de almacen con el numero
     * de serie
     */
    public void setLstSeries(ArrayList<vta_movproddeta> lstSeries) {
        this.lstSeries = lstSeries;
    }

    /**
     * Regresa la lista de numeros de serie ocupados por un pedido facturado o
     * remisionado
     *
     * @param intMPD_ID Es el id del movimiento
     */
    public void addItemLstSeries(int intMPD_ID) {
        if (lstSeries == null) {
            lstSeries = new ArrayList<vta_movproddeta>();
        }
        vta_movproddeta mov = new vta_movproddeta();
        mov.ObtenDatos(intMPD_ID, oConn);
        lstSeries.add(mov);
    }

    /**
     * Indica si genero movimiento para clientes (cargos / abonos)
     *
     * @return
     */
    public boolean isBolAfectoCargosAbonos() {
        return bolAfectoCargosAbonos;
    }

    /**
     * Indica si genero movimiento para clientes (cargos / abonos)
     *
     * @param bolAfectoCargosAbonos
     */
    public void setBolAfectoCargosAbonos(boolean bolAfectoCargosAbonos) {
        this.bolAfectoCargosAbonos = bolAfectoCargosAbonos;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    /**
     * Construtor del objeto tickets
     *
     * @param oConn Es la conexion
     * @param varSesiones Son las variables de sesion
     */
    public Ticket(Conexion oConn, VariableSession varSesiones) {
        super(oConn, varSesiones);
        this.document = new vta_tickets();
        this.lstMovs = new ArrayList<TableMaster>();
        this.lstPagos = new ArrayList<vta_mov_cte_deta>();
        this.strPrefijoMaster = "TKT";
        this.strPrefijoDeta = "TKTD";
        this.strNomTablaMaster = "vta_tickets";
        this.strNomTablaDeta = "vta_ticketsdeta";
        this.strFechaAnul = "";
        this.strTipoVta = "TICKET";
        this.strPATHXml = "";
        this.strPATHKeys = "";
        this.strMyPassSecret = "";
        this.bolSendMailMasivo = true;
        this.strCtaEmpty = "0000";
        this.bolAfectoCargosAbonos = true;
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
    }

    /**
     * Construtor del objeto tickets
     *
     * @param oConn Es la conexion
     * @param varSesiones Son las variables de sesion
     * @param request Es la peticion
     */
    public Ticket(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
        super(oConn, varSesiones, request);
        this.document = new vta_tickets();
        this.lstMovs = new ArrayList<TableMaster>();
        this.lstPagos = new ArrayList<vta_mov_cte_deta>();
        this.strPrefijoMaster = "TKT";
        this.strPrefijoDeta = "TKTD";
        this.strNomTablaMaster = "vta_tickets";
        this.strNomTablaDeta = "vta_ticketsdeta";
        this.strFechaAnul = "";
        this.strTipoVta = "TICKET";
        this.strPATHXml = "";
        this.strPATHKeys = "";
        this.strMyPassSecret = "";
        this.bolSendMailMasivo = true;
        this.strCtaEmpty = "0000";
        this.bolAfectoCargosAbonos = true;
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
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    @Override
    public void Init() {
        System.out.println("ENTRO INIT");
        this.document.setFieldInt(this.strPrefijoMaster + "_US_ALTA", varSesiones.getIntNoUser());
        //Si nos pasan el id del movimiento recuperamos todos los datos
        if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") != 0) {
            this.strFechaAnul = this.document.getFieldString(this.strPrefijoMaster + "_FECHAANUL");
            //¿Validamos o asignamos la tabla?
            this.document.ObtenDatos(this.document.getFieldInt(this.strPrefijoMaster + "_ID"), oConn);
        }
    }

    @Override
    public void doTrx() {
        System.out.println("ENTRO doTx");
        // <editor-fold defaultstate="collapsed" desc="Inicializamos valores">
        this.strResultLast = "OK";
        this.document.setFieldString(this.strPrefijoMaster + "_FECHACREATE", this.fecha.getFechaActual());
        this.document.setFieldString(this.strPrefijoMaster + "_HORA", this.fecha.getHoraActualHHMMSS());
        //Valores para retencion
        int intCT_TIPOPERS = 0;
        int intEMP_TIPOPERS = 0;
        //Valores para contabilidad
        int intEMP_USECONTA = 0;
        int intEMP_VTA_DETA = 0;
        int intEMP_CFD_CFDI = 0;
        String strCtaVtasGlobal = "";
        String strCtaVtasIVAGlobal = "";
        String strCtaVtasIVATasa = "";
        String strCtaVtasCteGlobal = "";
        String strCtaVtas = "";
        String strCtaVtasCte = "";
        String strEMP_URLCP = "";
        String strEMP_PASSCP = "";
        String strEMP_USERCP = "";
        boolean bolFirmaSAT = false;
        boolean bolUsoSello = false;
        String strNoAprob = "";
        String strFechaAprob = "";
        String strSerie = "";
        String strNoSerieCert = "";
        String strNomKey = "";
        String strNomCert = "";
        String strPassKey = "";
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
        if (this.document.getFieldInt("SC_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la sucursal";
        }
        if (this.document.getFieldString(this.strPrefijoMaster + "_FECHA").equals("")) {
            this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
        }
        if (this.document.getFieldInt("CT_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir el cliente";
        }
        if (this.document.getFieldString("FAC_FORMADEPAGO").isEmpty()) {
            this.strResultLast = "ERROR:Falta definir la forma de pago";
        }
        if (this.document.getFieldString("FAC_METODODEPAGO").isEmpty()) {
            this.strResultLast = "ERROR:Falta definir el metodo de pago";
        }
        if (this.lstMovs.isEmpty()) {
            this.strResultLast = "ERROR:Debe tener por lo menos un item la venta";
        }
//      if (!this.document.getFieldString("FAC_METODODEPAGO").equals("EFECTIVO") 
//              && !this.document.getFieldString("FAC_METODODEPAGO").equals("NO IDENTIFICADO") 
//              && !this.document.getFieldString("FAC_METODODEPAGO").equals("NO APLICA")) {
//         if (this.document.getFieldString("FAC_NUMCUENTA") != null) {
//            if (this.document.getFieldString("FAC_NUMCUENTA").isEmpty()) {
//               this.strResultLast = "ERROR:Debe ingresar un numero de cuenta";
//            } else {
//               //Evaluamos que el numero de cuenta sea mayor de 4
//               if (this.document.getFieldString("FAC_NUMCUENTA").length() < 4) {
//                  this.strResultLast = "ERROR:Debe ingresar un numero de cuenta de 4 caracteres mínimo.";
//               }
//            }
//         } else {
//            this.strResultLast = "ERROR:Debe ingresar un numero de cuenta";
//         }
//
//      }
//      if (this.document.getFieldString("FAC_NUMCUENTA") != null) {
//         if (this.document.getFieldString("FAC_NUMCUENTA").isEmpty()) {
//         } else {
//            //Evaluamos que el numero de cuenta sea mayor de 4
//            if (this.document.getFieldString("FAC_NUMCUENTA").length() < 4) {
//               this.strResultLast = "ERROR:Debe ingresar un numero de cuenta de 4 caracteres mínimo.";
//            }
//         }
//      }
        //Asignamos empresa...
        if (this.document.getFieldInt("EMP_ID") == 0) {
            this.document.setFieldInt("EMP_ID", this.intEMP_ID);
        }
        //Evaluamos la fecha de cierre
        boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
        if (!bolEvalCierre) {
            this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
        }
        // </editor-fold>

        //Validamos si pasamos las validaciones
        if (this.strResultLast.equals("OK")) {

            // <editor-fold defaultstate="collapsed" desc="Copiar datos del cliente">
            boolean bolFindCte = false;
            String strSql = "SELECT CT_RAZONSOCIAL,CT_RFC,CT_CALLE,CT_COLONIA,CT_LOCALIDAD,"
                    + " CT_MUNICIPIO,CT_ESTADO,CT_NUMERO,CT_NUMINT,CT_CONTACTE,CT_CONTAVTA,"
                    + " CT_CP,CT_DIASCREDITO,CT_LPRECIOS,CT_TIPOPERS FROM vta_cliente "
                    + " where CT_ID=" + this.document.getFieldInt("CT_ID") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    this.document.setFieldString(this.strPrefijoMaster + "_RAZONSOCIAL", rs.getString("CT_RAZONSOCIAL"));
                    this.document.setFieldString(this.strPrefijoMaster + "_RFC", rs.getString("CT_RFC"));
                    this.document.setFieldString(this.strPrefijoMaster + "_CALLE", rs.getString("CT_CALLE"));
                    this.document.setFieldString(this.strPrefijoMaster + "_COLONIA", rs.getString("CT_COLONIA"));
                    this.document.setFieldString(this.strPrefijoMaster + "_LOCALIDAD", rs.getString("CT_LOCALIDAD"));
                    this.document.setFieldString(this.strPrefijoMaster + "_MUNICIPIO", rs.getString("CT_MUNICIPIO"));
                    this.document.setFieldString(this.strPrefijoMaster + "_ESTADO", rs.getString("CT_ESTADO"));
                    this.document.setFieldString(this.strPrefijoMaster + "_NUMERO", rs.getString("CT_NUMERO"));
                    this.document.setFieldString(this.strPrefijoMaster + "_NUMINT", rs.getString("CT_NUMINT"));
                    this.document.setFieldString(this.strPrefijoMaster + "_CP", rs.getString("CT_CP"));
                    if (this.document.getFieldInt(this.strPrefijoMaster + "_DIASCREDITO") == 0) {
                        this.document.setFieldInt(this.strPrefijoMaster + "_DIASCREDITO", rs.getInt("CT_DIASCREDITO"));
                    }
                    if (this.document.getFieldInt(this.strPrefijoMaster + "_LPRECIOS") == 0) {
                        this.document.setFieldInt(this.strPrefijoMaster + "_LPRECIOS", rs.getInt("CT_LPRECIOS"));
                    }
                    strCtaVtas = rs.getString("CT_CONTAVTA");
                    strCtaVtasCte = rs.getString("CT_CONTACTE");
                    intCT_TIPOPERS = rs.getInt("CT_TIPOPERS");
                    bolFindCte = true;
                }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
                //Evaluamos si es un cliente final para copiar sus datos
                if (this.document.getFieldInt("DFA_ID") != 0) {
                    strSql = "SELECT DFA_RAZONSOCIAL,DFA_RFC,DFA_CALLE,DFA_COLONIA,DFA_LOCALIDAD,"
                            + " DFA_MUNICIPIO,DFA_ESTADO,DFA_NUMERO,DFA_NUMINT,"
                            + " DFA_CP FROM vta_cliente_facturacion "
                            + " where DFA_ID=" + this.document.getFieldInt("DFA_ID") + "";
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        this.document.setFieldString(this.strPrefijoMaster + "_RAZONSOCIAL", rs.getString("DFA_RAZONSOCIAL"));
                        this.document.setFieldString(this.strPrefijoMaster + "_RFC", rs.getString("DFA_RFC"));
                        this.document.setFieldString(this.strPrefijoMaster + "_CALLE", rs.getString("DFA_CALLE"));
                        this.document.setFieldString(this.strPrefijoMaster + "_COLONIA", rs.getString("DFA_COLONIA"));
                        this.document.setFieldString(this.strPrefijoMaster + "_LOCALIDAD", rs.getString("DFA_LOCALIDAD"));
                        this.document.setFieldString(this.strPrefijoMaster + "_MUNICIPIO", rs.getString("DFA_MUNICIPIO"));
                        this.document.setFieldString(this.strPrefijoMaster + "_ESTADO", rs.getString("DFA_ESTADO"));
                        this.document.setFieldString(this.strPrefijoMaster + "_NUMERO", rs.getString("DFA_NUMERO"));
                        this.document.setFieldString(this.strPrefijoMaster + "_NUMINT", rs.getString("DFA_NUMINT"));
                        this.document.setFieldString(this.strPrefijoMaster + "_CP", rs.getString("DFA_CP"));
                    }
                    //if(rs.getStatement() != null )rs.getStatement().close(); 
                    rs.close();
                }
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Si el cliente se encontro">
            if (bolFindCte) {

                // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara el ticket">
                strSql = "SELECT EMP_ID,SC_TASA1,SC_TASA2,SC_TASA3,"
                        + "SC_SOBRIMP1_2,SC_SOBRIMP1_3,SC_SOBRIMP2_3 "
                        + "FROM vta_sucursal WHERE SC_ID = " + this.document.getFieldInt("SC_ID") + "";
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        this.document.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
                        this.dblSC_TASA1 = rs.getDouble("SC_TASA1");
                        this.dblSC_TASA2 = rs.getDouble("SC_TASA2");
                        this.dblSC_TASA3 = rs.getDouble("SC_TASA3");
                        this.intSC_SOBRIMP1_2 = rs.getInt("SC_SOBRIMP1_2");
                        this.intSC_SOBRIMP1_3 = rs.getInt("SC_SOBRIMP1_3");
                        this.intSC_SOBRIMP2_3 = rs.getInt("SC_SOBRIMP2_3");
                    }
                    //if(rs.getStatement() != null )rs.getStatement().close();
                    rs.close();
                } catch (SQLException ex) {
                    this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
                //Si se definio el id de la empresa independiente de la sucursal
                if (this.intEMP_ID != 0) {
                    this.document.setFieldInt("EMP_ID", this.intEMP_ID);
                    this.bolFolioGlobal = false;
                }

                // <editor-fold defaultstate="collapsed" desc="Validamos regimen fiscal">
                int intCuantos = 0;
                String strSqlRegFis = "select count(REGF_DESCRIPCION) as cuantos from vta_empregfiscal,vta_regimenfiscal "
                        + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID "
                        + " AND vta_empregfiscal.EMP_ID = " + this.document.getFieldInt("EMP_ID");
                try {
                    ResultSet rs = oConn.runQuery(strSqlRegFis, true);
                    while (rs.next()) {
                        intCuantos = rs.getInt("cuantos");
                    }
                    //if(rs.getStatement() != null )rs.getStatement().close(); 
                    rs.close();
                } catch (SQLException ex) {
                    this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
                //Version 2.2
                if (intCuantos == 0) {
                    this.strResultLast = "ERROR:FALTA ASIGNAR UN REGIMEN FISCAL A LA EMPRESA";
                }
                // </editor-fold>

                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="En caso de factura obtenemos los parametros adicionales para la firma electronica">
                int intEMP_AVISOFOLIO = 0;
                if (this.strTipoVta.equals(Ticket.FACTURA)) {
                    boolean bolTmp = oConn.isBolMostrarQuerys();
                    oConn.setBolMostrarQuerys(false);
                    if (this.bolEsLocal) {
                        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(LOCAL)">
                        strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                                + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,"/**
                                 * ,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE
                                 */
                                + "EMP_PASSKEY AS unencrypted,EMP_CFD_CFDI,"
                                + "EMP_FIRMA,EMP_USACODBARR "
                                + "FROM vta_empresas "
                                + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                        try {
                            ResultSet rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                                this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
                                if (rs.getInt("EMP_USACODBARR") == 1) {
                                    bolUsoSello = true;
                                }
                                if (rs.getInt("EMP_FIRMA") == 1) {
                                    bolFirmaSAT = true;
                                    strNoAprob = rs.getString("EMP_NOAPROB");
                                    strFechaAprob = rs.getString("EMP_FECHAPROB");
                                    strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                                    strNomKey = rs.getString("EMP_NOMKEY");
                                    strNomCert = rs.getString("EMP_NOMCERT");
                                    strPassKey = rs.getString("unencrypted");
                                    intEMP_AVISOFOLIO = rs.getInt("EMP_AVISOFOLIO");
                                    intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                                    /*intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                            strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                            strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                            strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");*/
                                    //La encripcion se hace desde el programa
                                    Opalina opa = new Opalina();
                                    try {
                                        strPassKey = opa.DesEncripta(strPassKey, strMyPassSecret);
                                    } catch (NoSuchAlgorithmException ex) {
                                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                                    } catch (NoSuchPaddingException ex) {
                                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                                    } catch (InvalidKeyException ex) {
                                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                                    } catch (IllegalBlockSizeException ex) {
                                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                                    } catch (BadPaddingException ex) {
                                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                                    }
                                }
                            }
                            //if(rs.getStatement() != null )rs.getStatement().close(); 
                            rs.close();
                        } catch (SQLException ex) {
                            this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }
                        // </editor-fold>
                    } else {
                        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
                        strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                                + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,"
                                + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,EMP_CFD_CFDI,"
                                + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_URLCP,EMP_USACODBARR,EMP_VTA_DETA "
                                + "FROM vta_empresas "
                                + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                        try {
                            ResultSet rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                                this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
                                if (rs.getInt("EMP_USACODBARR") == 1) {
                                    bolUsoSello = true;
                                }
                                if (rs.getInt("EMP_FIRMA") == 1) {
                                    bolFirmaSAT = true;
                                    strNoAprob = rs.getString("EMP_NOAPROB");
                                    strFechaAprob = rs.getString("EMP_FECHAPROB");
                                    strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                                    strNomKey = rs.getString("EMP_NOMKEY");
                                    strNomCert = rs.getString("EMP_NOMCERT");
                                    strPassKey = rs.getString("unencrypted");
                                    intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                                    intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                                    strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                                    strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                                    strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                                    strEMP_PASSCP = rs.getString("EMP_PASSCP");
                                    strEMP_USERCP = rs.getString("EMP_USERCP");
                                    strEMP_URLCP = rs.getString("EMP_URLCP");
                                    intEMP_AVISOFOLIO = rs.getInt("EMP_AVISOFOLIO");
                                    intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                                }
                            }
                            //if(rs.getStatement() != null )rs.getStatement().close(); 
                            rs.close();
                        } catch (SQLException ex) {
                            this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }
                        // </editor-fold>
                    }
                    oConn.setBolMostrarQuerys(bolTmp);
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Asignamos el tipo de comprobante">
                this.document.setFieldInt(this.strPrefijoMaster + "_TIPOCOMP", this.intEMP_TIPOCOMP);
                if (this.intFAC_TIPOCOMP != 0) {
                    this.document.setFieldInt(this.strPrefijoMaster + "_TIPOCOMP", this.intFAC_TIPOCOMP);
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Calcular el importe del descuento total">
                double dblDescuentoTotal = 0;
                Iterator<TableMaster> itG = this.lstMovs.iterator();
                while (itG.hasNext()) {
                    TableMaster deta = itG.next();
                    dblDescuentoTotal += deta.getFieldDouble(this.strPrefijoDeta + "_DESCUENTO");
                }
                this.document.setFieldDouble(this.strPrefijoMaster + "_DESCUENTO", dblDescuentoTotal);
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Evaluamos si es una factura/ticket que viene de varios pedidos">
                if (this.lstPedidosOrigen != null) {
                    this.document.setFieldInt(this.strPrefijoMaster + "_ES_POR_PEDIDOS", 1);
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Proceso de guardado del documento">
                if (this.strResultLast.equals("OK")) {
                    int intId = 0;

                    // <editor-fold defaultstate="collapsed" desc="Iniciar transaccion">
                    if (this.bolTransaccionalidad) {
                        this.oConn.runQueryLMD("BEGIN");
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Folios para el documento">
                    this.document.setBolGetAutonumeric(true);
                    //Validamos si es ticket o factura
                    if (this.strTipoVta.equals(Ticket.FACTURA)
                            || this.strTipoVta.equals(Ticket.TICKET)
                            || this.strTipoVta.equals(Ticket.PEDIDO)
                            || this.strTipoVta.equals(Ticket.COTIZACION)) {
                        boolean bolCalcula = true;
                        /*if (this.strTipoVta.equals(Ticket.FACTURA) && intEMP_CFD_CFDI == 1) {
                   bolCalcula = false;
                   }*/
                        if (bolCalcula) {
                            // <editor-fold defaultstate="collapsed" desc="Calculo del folio">
                            Folios folio = new Folios();
                            //Determinamos el tipo de ticket
                            int intTipoFolio = Folios.TICKET;
                            if (this.strTipoVta.equals(Ticket.FACTURA)) {
                                intTipoFolio = Folios.FACTURA;
                            }
                            if (this.strTipoVta.equals(Ticket.PEDIDO)) {
                                intTipoFolio = Folios.PEDIDOS;
                            }
                            if (this.strTipoVta.equals(Ticket.COTIZACION)) {
                                intTipoFolio = Folios.COTIZACIONES;
                            }
                            String strFolio = "";
                            folio.setBolEsLocal(this.bolEsLocal);
                            if (this.document.getFieldString(this.strPrefijoMaster + "_FOLIO").equals("")) {
                                if (this.document.getFieldString(this.strPrefijoMaster + "_SERIE") != null) {
                                    folio.setStrSerie(this.document.getFieldString(this.strPrefijoMaster + "_SERIE"));
                                }
                                strFolio = folio.doFolio(oConn, intTipoFolio, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                                this.document.setFieldString(this.strPrefijoMaster + "_FOLIO", strFolio);
                                if (intEMP_CFD_CFDI == 1) {
                                    this.document.setFieldString(this.strPrefijoMaster + "_FOLIO_C", strFolio);
                                }
                            } else {
                                strFolio = this.document.getFieldString(this.strPrefijoMaster + "_FOLIO");
                                if (intEMP_CFD_CFDI == 1) {
                                    this.document.setFieldString(this.strPrefijoMaster + "_FOLIO_C", strFolio);
                                }
                                /*folio.updateFolio(oConn, intTipoFolio, this.document.getFieldString(this.strPrefijoMaster + "_FOLIO"),
                         this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));*/
                            }
                            // </editor-fold>

                            // <editor-fold defaultstate="collapsed" desc="En caso de ser factura validamos si el folio es valido en caso de tener facturacion electronica">
                            if ((this.strTipoVta.equals(Ticket.FACTURA) && bolFirmaSAT && intEMP_CFD_CFDI == 0)
                                    || (this.strTipoVta.equals(Ticket.FACTURA) && bolUsoSello)) {
                                int intFolio = 0;
                                try {
                                    intFolio = Integer.valueOf(strFolio);
                                    int intFE_ID = 0;
                                    int intFE_FOLIOFIN = 0;
                                    strSql = "SELECT * FROM vta_foliosempresa WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID")
                                            + " AND " + intFolio + ">= FE_FOLIOINI  AND " + intFolio + "<=FE_FOLIOFIN AND FE_ESNC = 0 ";
                                    try {
                                        boolean bolFindFolio = false;
                                        ResultSet rs = oConn.runQuery(strSql, true);
                                        while (rs.next()) {
                                            bolFindFolio = true;
                                            strNoAprob = rs.getString("FE_NOAPROB");
                                            strFechaAprob = rs.getString("FE_FECHA");
                                            strSerie = rs.getString("FE_SERIE");
                                            intFE_ID = rs.getInt("FE_ID");
                                            intFE_FOLIOFIN = rs.getInt("FE_FOLIOFIN");
                                        }
                                        //if(rs.getStatement() != null )rs.getStatement().close(); 
                                        rs.close();
                                        if (!bolFindFolio) {
                                            // <editor-fold defaultstate="collapsed" desc="Buscamos si existe un folio con un valor superior al contador actual">
                                            // </editor-fold>
                                            this.strResultLast = "ERROR:NO CUENTA CON FOLIOS PARA HACER SU FACTURA";
                                        } else {
                                            //Guardamos los datos en la factura
                                            this.document.setFieldString("FAC_SERIE", strSerie);
                                            this.document.setFieldString("FAC_NOAPROB", strNoAprob);
                                            this.document.setFieldString("FAC_FECHAAPROB", strFechaAprob);
                                            //Validamos si quedan pocos folios para enviar un aviso
                                            AvisoFolios(intFE_ID, intEMP_AVISOFOLIO, intFE_FOLIOFIN, intFolio);
                                        }
                                    } catch (SQLException ex) {
                                        this.strResultLast = "ERROR:" + ex.getMessage();
                                        ex.fillInStackTrace();
                                    }
                                } catch (NumberFormatException ex) {
                                    this.strResultLast = "Error:" + ex.getMessage();
                                }
                            }
                            // </editor-fold>

                        }
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Si es pedido colocamos el estatus">
                    if (this.strTipoVta.equals(Ticket.PEDIDO)) {
                        this.document.setFieldInt("PD_STATUS", 1);
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Si todo esta ok guardamos el documento">
                    if (this.strResultLast.equals("OK")) {
                        String strRes1 = this.document.Agrega(oConn);
                        if (!strRes1.equals("OK")) {
                            this.strResultLast = strRes1;
                        }
                    }
                    // </editor-fold>

                    if (this.strResultLast.equals("OK")) {
                        // <editor-fold defaultstate="collapsed" desc="Facturacion Recurrente">
                        if (this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA)) {
                            if (this.document.getFieldInt(this.strPrefijoMaster + "_ESRECU") == 1 && this.intPedidoGenero == 0) {
                                FacRecurrente facRecurrente = new FacRecurrente(this.oConn, this.varSesiones);
                                log.debug("Entramos a generar una facturacion/remision recurrente ..." + this.strTipoVta);
                                facRecurrente.NewPedRec(this.strTipoVta, this.document, this.lstMovs);
                                if (!facRecurrente.getStrResultLast().equals("OK")) {
                                    this.strResultLast = facRecurrente.getStrResultLast();
                                }
                            }

                            //Reemplazamos los comodines que definimos
                            FormatoDescripNotas();
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Guardar detalle">
                        intId = Integer.valueOf(this.document.getValorKey());
                        Iterator<TableMaster> it = this.lstMovs.iterator();
                        while (it.hasNext()) {
                            TableMaster deta = it.next();
                            deta.setFieldInt(this.strPrefijoMaster + "_ID", intId);
                            deta.setBolGetAutonumeric(true);
                            String strRes2 = deta.Agrega(oConn);
                            //Validamos si todo fue satisfactorio
                            if (!strRes2.equals("OK")) {
                                this.strResultLast = strRes2;
                                break;
                            }
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Actualizamos el pedido en caso de que proceda">
                        String strResTmp = PedidoConvertidoenVenta(intId);
                        if (!strResTmp.equals("OK")) {
                            this.strResultLast = strResTmp;
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Afectaciones a otros objetos de negocio">
                        if (this.strResultLast.equals("OK")) {

                            // <editor-fold defaultstate="collapsed" desc="Inventarios">
                            int intInv = 0;
                            //Aplicamos inventarios unicamente si el ticket no es de servicios
                            //Y es un ticket o una factura
                            if (this.document.getFieldInt(this.strPrefijoMaster + "_ESSERV") == 0
                                    && (this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA))) {
                                //Si esta activa la bandera de afectar inventarios procedemos
                                if (bolAfectaInv) {
                                    //Crear objeto de inventario y aplicarlo
                                    Inventario inv = new Inventario(this.oConn, this.varSesiones, this.request);
                                    //Inicializamos
                                    inv.Init();
                                    //Definimos valores
                                    inv.setBolTransaccionalidad(false);
                                    inv.setIntTipoOperacion(Inventario.SALIDA);
                                    inv.setIntTipoCosteo(this.intSistemaCostos);//SistemaCostos
                                    inv.getMovProd().setFieldInt("TIN_ID", Inventario.SALIDA);
                                    //Asignamos los valores master
                                    inv.getMovProd().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                    inv.getMovProd().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                                    inv.getMovProd().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                                    inv.getMovProd().setFieldInt("MP_TURNO", this.document.getFieldInt(this.strPrefijoMaster + "_TURNO"));
                                    inv.getMovProd().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                                    inv.getMovProd().setFieldString("MP_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                    inv.getMovProd().setFieldString("MP_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                                    //Asignamos los valores de las partidas
                                    it = this.lstMovs.iterator();
                                    while (it.hasNext()) {
                                        TableMaster deta = it.next();
                                        //Validamos los numeros de serie
                                        //Validamos si maneja numeros de serie
                                        log.debug("Valores de las series" + deta.getFieldString(this.strPrefijoDeta + "_NOSERIE"));
                                        if (!deta.getFieldString(this.strPrefijoDeta + "_NOSERIE").equals("") && !deta.getFieldString(this.strPrefijoDeta + "_NOSERIE").equals("null")) {
                                            String[] lstSeriesI = deta.getFieldString(this.strPrefijoDeta + "_NOSERIE").split(",");
                                            //Recorremos cada numero de serie para ingresarlo
                                            for (int y = 0; y < lstSeriesI.length; y++) {
                                                //Copiamos valores del ticket
                                                vta_movproddeta detaInv = new vta_movproddeta();
                                                detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                                                detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                                detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt(this.strPrefijoDeta + "_ID"));
                                                if (deta.getFieldInt(this.strPrefijoDeta + "_ESDEVO") == 1) {
                                                    detaInv.setFieldDouble("MPD_ENTRADAS", 1);
                                                } else {
                                                    detaInv.setFieldDouble("MPD_SALIDAS", 1);
                                                }
                                                detaInv.setFieldString("PR_CODIGO", deta.getFieldString(this.strPrefijoDeta + "_CVE"));
                                                detaInv.setFieldString("PL_NUMLOTE", lstSeriesI[y]);
                                                detaInv.setFieldString("MPD_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                                detaInv.setFieldInt("MPD_SERIE_VENDIDO", 1);
                                                inv.AddDetalle(detaInv);

                                            }
                                        } else {
                                            //Copiamos valores del ticket
                                            vta_movproddeta detaInv = new vta_movproddeta();
                                            detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                                            detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                            detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt(this.strPrefijoDeta + "_ID"));
                                            if (deta.getFieldInt(this.strPrefijoDeta + "_ESDEVO") == 1) {
                                                detaInv.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD"));
                                            } else {
                                                detaInv.setFieldDouble("MPD_SALIDAS", deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD"));
                                                log.debug(deta.getFieldString(this.strPrefijoDeta + "_CVE") + "Cantidad por sacar de inventarios " + deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD"));
                                            }
                                            detaInv.setFieldString("PR_CODIGO", deta.getFieldString(this.strPrefijoDeta + "_CVE"));
                                            detaInv.setFieldString("PL_NUMLOTE", deta.getFieldString(this.strPrefijoDeta + "_NOSERIE"));
                                            detaInv.setFieldString("MPD_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                            inv.AddDetalle(detaInv);
                                        }

                                    }
                                    //Generamos la operacion de inventarios
                                    inv.doTrx();
                                    if (!inv.getStrResultLast().equals("OK")) {
                                        //Fallo algo al aplicar el cargo
                                        this.strResultLast = inv.getStrResultLast();
                                    } else {
                                        //Obtenemos el id del movimiento de inventario para ligar los costos
                                        intInv = inv.getMovProd().getFieldInt("MP_ID");
                                    }
                                }
                            }
                            // </editor-fold>

                            //Si todo resulto bien proseguimos
                            if (this.strResultLast.equals("OK")) {
                                if (bolAfectoCargosAbonos) {
                                    // <editor-fold defaultstate="collapsed" desc="Cargo clientes">
                                    //El cargo solo aplica con los tickets y facturas
                                    if ((this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA))) {
                                        //Crear objeto de cta cliente y aplicarlo
                                        //Generamos cargo por el importe de la deuda
                                        movCliente cta_clie = new movCliente(this.oConn, this.varSesiones, this.request);
                                        cta_clie.setBolTransaccionalidad(false);
                                        //Asignamos valores
                                        cta_clie.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                                        cta_clie.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                        cta_clie.getCta_clie().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                                        cta_clie.getCta_clie().setFieldInt("MC_ESPAGO", 0);
                                        cta_clie.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                                        cta_clie.getCta_clie().setFieldInt("MC_TASAPESO", 1);
                                        cta_clie.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                        cta_clie.getCta_clie().setFieldString("MC_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                                        cta_clie.getCta_clie().setFieldDouble("MC_CARGO", this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL"));
                                        //Validamos el tipo de comprobante para la parte de honorarios
                                        if (this.bolEsLocal) {
                                            if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 0) {
                                                cta_clie.getCta_clie().setFieldDouble("MC_CARGO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                                            }
                                        } else {
                                            if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 2) {
                                                cta_clie.getCta_clie().setFieldDouble("MC_CARGO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                                            }
                                        }
                                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                                        cta_clie.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                                        cta_clie.Init();
                                        cta_clie.doTrx();

                                        //Fallo algo al aplicar el cargo
                                        if (!cta_clie.getStrResultLast().equals("OK")) {
                                            this.strResultLast = cta_clie.getStrResultLast();
                                        }
                                    }
                                    // </editor-fold>

                                    //Validamos que el cargo halla pasado
                                    if (this.strResultLast.equals("OK")) {
                                        // <editor-fold defaultstate="collapsed" desc="Abono">
                                        //Validamos si hay pagos
                                        if (this.lstPagos.size() > 0) {
                                            //Generamos el abono por el importe de los pagos
                                            int intCont = 0;
                                            movCliente cta_cliePago = new movCliente(this.oConn, this.varSesiones, this.request);
                                            cta_cliePago.setBolCaja(true);
                                            cta_cliePago.setBolTransaccionalidad(false);
                                            Iterator<vta_mov_cte_deta> itPago = this.lstPagos.iterator();
                                            double dblImportePago = 0;
                                            double dblImpuestoPago1 = 0;
                                            double dblImpuestoPago2 = 0;
                                            double dblImpuestoPago3 = 0;
                                            boolean boolUsaAnticipo = false;
                                            while (itPago.hasNext()) {
                                                vta_mov_cte_deta detaPago = itPago.next();
                                                //Si la entrada es un saldo a favor lo ignoramos por el momento
                                                if (detaPago.getFieldString("MCD_FORMAPAGO").equals("SALDOFAVOR")) {
                                                    boolUsaAnticipo = true;
                                                    continue;
                                                }
                                                detaPago.setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                                                detaPago.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                                detaPago.setFieldDouble("MCD_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                                                detaPago.setFieldDouble("MCD_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                                                detaPago.setFieldDouble("MCD_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                                                dblImportePago += detaPago.getFieldDouble("MCD_IMPORTE");
                                                //Calculamos proporcion del pago
                                                if (this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL") > 0) {
                                                    double dblPropor = detaPago.getFieldDouble("MCD_IMPORTE") / this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL");
                                                    double dblImpuesto1 = this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1") * dblPropor;
                                                    double dblImpuesto2 = this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2") * dblPropor;
                                                    double dblImpuesto3 = this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3") * dblPropor;
                                                    detaPago.setFieldDouble("MCD_IMPUESTO1", dblImpuesto1);
                                                    detaPago.setFieldDouble("MCD_IMPUESTO2", dblImpuesto2);
                                                    detaPago.setFieldDouble("MCD_IMPUESTO3", dblImpuesto3);
                                                    dblImpuestoPago1 += dblImpuesto1;
                                                    dblImpuestoPago2 += dblImpuesto2;
                                                    dblImpuestoPago3 += dblImpuesto3;
                                                }
                                                cta_cliePago.AddDetalle(detaPago);
                                            }
                                            //Asignamos valores
                                            cta_cliePago.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                                            cta_cliePago.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                            cta_cliePago.getCta_clie().setFieldInt("MC_ESPAGO", 1);
                                            cta_cliePago.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                                            cta_cliePago.getCta_clie().setFieldInt("MC_TASAPESO", 1);
                                            cta_cliePago.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                                            cta_cliePago.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_ABONO", dblImportePago);
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_IMPUESTO1", dblImpuestoPago1);
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_IMPUESTO2", dblImpuestoPago2);
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_IMPUESTO3", dblImpuestoPago3);
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                                            cta_cliePago.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                                            cta_cliePago.Init();
                                            if (this.lstPagos.size() > 1) {
                                                cta_cliePago.doTrx();
                                                //Validamos que el o los pagos halla pasado
                                                if (!cta_cliePago.getStrResultLast().equals("OK")) {
                                                    //Fallo algo al aplicar el cargo
                                                    this.strResultLast = cta_cliePago.getStrResultLast();
                                                }
                                            } else {
                                                if (this.lstPagos.size() == 1 && !boolUsaAnticipo) {
                                                    cta_cliePago.doTrx();
                                                    //Validamos que el o los pagos halla pasado
                                                    if (!cta_cliePago.getStrResultLast().equals("OK")) {
                                                        //Fallo algo al aplicar el cargo
                                                        this.strResultLast = cta_cliePago.getStrResultLast();
                                                    }
                                                }
                                            }

                                            //Si el movimiento usa saldo a favor creamos los movimientos
                                            if (boolUsaAnticipo) {
                                                movCliente cta_cliePago2 = new movCliente(this.oConn, this.varSesiones, this.request);
//                                 cta_cliePago2.setBolCaja(true);
//                                 cta_cliePago2.setBolTransaccionalidad(false);

                                                dblImportePago = 0;
                                                dblImpuestoPago1 = 0;
                                                dblImpuestoPago2 = 0;
                                                dblImpuestoPago3 = 0;
                                                int intCantAnti = this.numAnticipos;
                                                for (int k = 0; k < intCantAnti; k++) {
                                                    cta_cliePago2 = new movCliente(this.oConn, this.varSesiones, this.request);
                                                    cta_cliePago2.setBolCaja(true);
                                                    cta_cliePago2.setBolTransaccionalidad(false);

                                                    vta_mov_cte_deta detaPago2 = new vta_mov_cte_deta();
                                                    detaPago2.setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                                                    detaPago2.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                                    detaPago2.setFieldDouble("MCD_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                                                    detaPago2.setFieldDouble("MCD_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                                                    detaPago2.setFieldDouble("MCD_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                                                    detaPago2.setFieldString("MCD_FORMAPAGO", "SALDOFAVOR");
                                                    detaPago2.setFieldDouble("MCD_IMPORTE", this.arTotalAntUsar[k]);
                                                    dblImportePago += detaPago2.getFieldDouble("MCD_IMPORTE");
                                                    //Calculamos proporcion del pago
                                                    if (this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL") > 0) {
                                                        double dblPropor = detaPago2.getFieldDouble("MCD_IMPORTE") / this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL");
                                                        double dblImpuesto1 = this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1") * dblPropor;
                                                        double dblImpuesto2 = this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2") * dblPropor;
                                                        double dblImpuesto3 = this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3") * dblPropor;
                                                        detaPago2.setFieldDouble("MCD_IMPUESTO1", dblImpuesto1);
                                                        detaPago2.setFieldDouble("MCD_IMPUESTO2", dblImpuesto2);
                                                        detaPago2.setFieldDouble("MCD_IMPUESTO3", dblImpuesto3);
                                                        dblImpuestoPago1 += dblImpuesto1;
                                                        dblImpuestoPago2 += dblImpuesto2;
                                                        dblImpuestoPago3 += dblImpuesto3;
                                                    }
                                                    cta_cliePago2.AddDetalle(detaPago2);

                                                    //Asignamos valores
                                                    cta_cliePago2.getCta_clie().setFieldInt("MC_USA_ANTICIPO", 1);
                                                    cta_cliePago2.getCta_clie().setFieldInt("MC_ANTI_ID", this.arID_Ant[k]);
                                                    cta_cliePago2.setDblSaldoFavorUsado(this.arTotalAntUsar[k]);

                                                    cta_cliePago2.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                                                    cta_cliePago2.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                                    cta_cliePago2.getCta_clie().setFieldInt("MC_ESPAGO", 1);
                                                    cta_cliePago2.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                                                    cta_cliePago2.getCta_clie().setFieldInt("MC_TASAPESO", 1);
                                                    cta_cliePago2.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                                                    cta_cliePago2.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_ABONO", this.arTotalAntUsar[k]);
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_IMPUESTO1", dblImpuestoPago1);
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_IMPUESTO2", dblImpuestoPago2);
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_IMPUESTO3", dblImpuestoPago3);
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                                                    cta_cliePago2.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));

                                                    cta_cliePago2.Init();
                                                    cta_cliePago2.doTrx();
                                                }

                                                //Validamos que el o los pagos halla pasado
                                                if (!cta_cliePago2.getStrResultLast().equals("OK")) {
                                                    //Fallo algo al aplicar el cargo
                                                    this.strResultLast = cta_cliePago2.getStrResultLast();
                                                }

                                            }

                                        }
                                        // </editor-fold>
                                    }

                                }

                            }
                            // <editor-fold defaultstate="collapsed" desc="Calculos adicionales a la generacion del documento">
                            if (this.strResultLast.equals("OK")) {

                                // <editor-fold defaultstate="collapsed" desc="Costo del detalle Y actualizarlo">
                                //Recorremos todas las partidas para buscar y  actualizar su costo
                                it = this.lstMovs.iterator();
                                while (it.hasNext()) {
                                    TableMaster deta = it.next();
                                    strSql = "SELECT sum(MPD_SALIDAS * MPD_COSTO) as costo FROM vta_movproddeta "
                                            + " WHERE vta_movproddeta.MPD_IDORIGEN = " + deta.getFieldInt(this.strPrefijoDeta + "_ID") + " "
                                            + " AND MP_ID = " + intInv;
                                    try {
                                        ResultSet rs = oConn.runQuery(strSql, true);
                                        while (rs.next()) {
                                            double dblCosto = rs.getDouble("costo");
                                            double dblCostoAplica = dblCosto / deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD");
                                            //Actualizamos el costo
                                            String strUpdate = "UPDATE " + this.strNomTablaDeta + " set "
                                                    + this.strPrefijoDeta + "_COSTO =  " + dblCostoAplica + " "
                                                    + " WHERE " + this.strNomTablaDeta + "." + this.strPrefijoDeta + "_ID = " + deta.getFieldInt(this.strPrefijoDeta + "_ID");
                                            oConn.runQueryLMD(strUpdate);
                                        }
                                        //if(rs.getStatement() != null )rs.getStatement().close(); 
                                        rs.close();
                                    } catch (SQLException ex) {
                                        this.strResultLast = "ERROR:" + ex.getMessage();
                                        ex.fillInStackTrace();
                                    }
                                }
                                // </editor-fold>

                                // <editor-fold defaultstate="collapsed" desc="Calculamos la ganancia global en base al costo de la operacion">
                                //y el importe de ventas
                                strSql = "SELECT sum(" + this.strPrefijoDeta + "_IMPORTE) as venta,"
                                        + "sum(" + this.strPrefijoDeta + "_COSTO*" + this.strPrefijoDeta + "_CANTIDAD) as costo "
                                        + "FROM " + this.strNomTablaDeta + " "
                                        + " WHERE " + this.strNomTablaDeta + "." + this.strPrefijoMaster + "_ID = "
                                        + " " + this.document.getFieldInt(this.strPrefijoMaster + "_ID") + " ";
                                try {
                                    ResultSet rs = oConn.runQuery(strSql, true);
                                    rs = oConn.runQuery(strSql, true);
                                    while (rs.next()) {
                                        double dblVenta = rs.getDouble("venta");
                                        double dblCosto = rs.getDouble("costo");
                                        double dblGanancia = dblVenta - dblCosto;
                                        //Actualizamos la ganancia y el costo total
                                        String strUpdate = "UPDATE " + this.strNomTablaMaster + " set "
                                                + this.strPrefijoMaster + "_GANANCIA =  " + dblGanancia + ", "
                                                + this.strPrefijoMaster + "_COSTO =  " + dblCosto + " "
                                                + " WHERE " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                                        oConn.runQueryLMD(strUpdate);
                                    }
                                    //if(rs.getStatement() != null )rs.getStatement().close(); 
                                    rs.close();
                                } catch (SQLException ex) {
                                    this.strResultLast = "ERROR:" + ex.getMessage();
                                    ex.fillInStackTrace();
                                }
                                // </editor-fold>

                                // <editor-fold defaultstate="collapsed" desc="Firma Electronica, Contabilidad">
                                if (this.strResultLast.equals("OK")) {

                                    // <editor-fold defaultstate="collapsed" desc="Firma electronica">
                                    if (bolFirmaSAT) {
                                        if (SAT == null) {
                                            //Validamos si se usa un CFD o CFDI
                                            if (intEMP_CFD_CFDI == 0) {
                                                //CFD
                                                //Instanciamos objeto para generar el XML que pide el SAT
                                                this.SAT = new SATXml(intId, this.strPATHXml,
                                                        strNoAprob, strFechaAprob, strNoSerieCert,
                                                        this.strPATHKeys + strNomKey, strPassKey, this.varSesiones,
                                                        oConn);
                                            } else {
                                                //CFDI
                                                //Instanciamos objeto para generar el XML que pide el SAT
                                                this.SAT = new SATXml3_0(intId, this.strPATHXml,
                                                        strNoAprob, strFechaAprob, strNoSerieCert,
                                                        this.strPATHKeys + strNomKey, strPassKey, this.varSesiones,
                                                        oConn);
                                                SATXml3_0 sat3 = (SATXml3_0) this.SAT;
                                                sat3.setStrPathConfigPAC(this.strPATHKeys);
//                                                sat3.setBlEmail1(blEmail1);
//                                                sat3.setBlEmail2(blEmail2);
//                                                sat3.setBlEmail3(blEmail3);
//                                                sat3.setBlEmail4(blEmail4);
//                                                sat3.setBlEmail5(blEmail5);
//                                                sat3.setBlEmail6(blEmail6);
//                                                sat3.setBlEmail7(blEmail7);
//                                                sat3.setBlEmail8(blEmail8);
//                                                sat3.setBlEmail9(blEmail9);
//                                                sat3.setBlEmail10(blEmail10);
                                            }
                                        } else {
                                            this.SAT.setIntTransaccion(intId);
                                            this.SAT.setStrPath(this.strPATHXml);
                                            this.SAT.setNoAprobacion(strNoAprob);
                                            this.SAT.setFechaAprobacion(strFechaAprob);
                                            this.SAT.setNoSerieCert(strNoSerieCert);
                                            this.SAT.setStrPathKey(this.strPATHKeys + strNomKey);
                                            this.SAT.setStrPassKey(strPassKey);
                                            this.SAT.setVarSesiones(varSesiones);
                                            this.SAT.setoConn(oConn);
                                        }
                                        //Validamos que el path del certificado no sea vacio
                                        if (!strNomCert.isEmpty()) {
                                            SAT.setStrPathCert(this.strPATHKeys + strNomCert);
                                        }
                                        SAT.setBolEsLocal(bolEsLocal);
                                        SAT.setStrPATHFonts(strPATHFonts);
                                        SAT.setBolSendMailMasivo(bolSendMailMasivo);
                                        SAT.setIntEMP_TIPOCOMP(this.intFAC_TIPOCOMP);

                                        if (this.bolUsoLugarExpEmp) {
                                            SAT.setBolUsoLugarExpEmp(bolUsoLugarExpEmp);
                                        }
                                        if (this.bolUsaTrxComoFolio) {
                                            SAT.setBolUsaTrxComoFolio(bolUsaTrxComoFolio);
                                        }
                                        //Atrapamos cualquier error con el timbrado de la factura para evitar la perdida
                                        try {
                                            String strRes = SAT.GeneraXml();
                                            if (!strRes.equals("OK")) {
                                                this.strResultLast = strRes;
                                            }
                                        } catch (Exception ex) {
                                            this.strResultLast = "ERROR:" + ex.getMessage();
                                        }

                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Vemos si paso las validaciones para guardar la contabilidad">
                                    if (this.strResultLast.equals("OK")) {
                                        if (this.strTipoVta.equals(Ticket.FACTURA)) {
                                            // <editor-fold defaultstate="collapsed" desc="Solo procede si la empresa esta configurada">
                                            if (intEMP_USECONTA == 1) {
                                                log.debug("Generamos la contabilidad.....");
                                                //Objeto para calculo de poliza contable
                                                ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                                                //Calcula la poliza para facturas
                                                contaUtil.CalculaPolizaContableFacturas(this.document.getFieldInt("EMP_ID"),
                                                        strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
                                                        strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
                                                        this.document.getFieldInt("FAC_MONEDA"), document, this.document.getFieldInt("TI_ID"), intId,
                                                        strNomTablaMaster, strPrefijoMaster, "NEW");
                                            }
                                            // </editor-fold>
                                        }
                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Formato de impresion codigo de barras">
                                    if (bolSendMailMasivo) {
                                    }
                                    // </editor-fold>

                                    // <editor-fold defaultstate="collapsed" desc="Bitacora">
                                    this.saveBitacora(this.strTipoVta, "NUEVA", intId);
                                    // </editor-fold>
                                }
                                // </editor-fold>
                            }
                            // </editor-fold>
                        }
                        // </editor-fold>
                    }

                    // <editor-fold defaultstate="collapsed" desc="Termina la transaccion">
                    if (this.bolTransaccionalidad) {
                        if (this.strResultLast.equals("OK")) {
                            this.oConn.runQueryLMD("COMMIT");
                        } else {
                            this.oConn.runQueryLMD("ROLLBACK");
                            // <editor-fold defaultstate="collapsed" desc="Proceso de recuperacion de folio">
                            //Determinamos el tipo de ticket
                            int intTipoFolio = Folios.TICKET;
                            String strNomFolioC = "_FOLIO";
                            if (this.strTipoVta.equals(Ticket.FACTURA)) {
                                intTipoFolio = Folios.FACTURA;
                                if (intEMP_CFD_CFDI == 1) {
                                    strNomFolioC = "_FOLIO_C";
                                }
                            }
                            if (this.strTipoVta.equals(Ticket.PEDIDO)) {
                                intTipoFolio = Folios.PEDIDOS;
                            }
                            if (this.strTipoVta.equals(Ticket.COTIZACION)) {
                                intTipoFolio = Folios.COTIZACIONES;
                            }
                            Folios folio = new Folios();
                            //Agregamos la serie si corresponde
                            if (this.document.getFieldString(this.strPrefijoMaster + "_SERIE") != null) {
                                folio.setStrSerie(this.document.getFieldString(this.strPrefijoMaster + "_SERIE"));
                            }
                            folio.recoveryFolioLost(oConn, intTipoFolio,
                                    this.document.getFieldString(this.strPrefijoMaster + strNomFolioC),
                                    bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                            //Solo si esta configurado recuperar el id
                            if (bolRecoveryIdLost) {
                                //Recuperamos el id usado
                                recoveryIdLost(oConn, intTipoFolio,
                                        intId,
                                        this.document.getFieldInt("EMP_ID"));
                            }
                            // </editor-fold>
                        }
                    }
                    // </editor-fold>  
                }
                // </editor-fold>

            } else {
                this.strResultLast = "ERROR:El cliente seleccionado no existe";
            }
            // </editor-fold>
        }
    }

    @Override
    public void doTrxAnul() {
        // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
        this.strResultLast = "OK";
        //Validamos que todos los campos basico se encuentren
        if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la operacion por anular";
        }
        //Evaluamos la fecha de cierre
        if (!this.strTipoVta.equals(Ticket.PEDIDO) && !this.strTipoVta.equals(Ticket.COTIZACION)) {
            boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
            if (!bolEvalCierre) {
                this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
            }
        }
        //Evaluamos si es un pedido para validar que no tenga facturas relacionadas
        if (this.strTipoVta.equals(Ticket.PEDIDO)) {
            try {
                int intCuantosLink = 0;
                //Consultamos si hay facturas ligadas a este pedido..
                String strSqlSearch1 = "select count(FAC_ID) as cuantos from vta_facturas "
                        + " where pd_id = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID") + " and FAC_ANULADA = 0";
                ResultSet rsSearch = oConn.runQuery(strSqlSearch1, true);
                while (rsSearch.next()) {
                    intCuantosLink += rsSearch.getInt("cuantos");
                }
                if (rsSearch.getStatement() != null) {
                    rsSearch.getStatement().close();
                }
                rsSearch.close();
                //Consultamos si hay tickets ligadas a este pedido..
                strSqlSearch1 = "select count(TKT_ID) as cuantos from vta_tickets"
                        + "  where pd_id = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID") + " and TKT_ANULADA = 0";
                rsSearch = oConn.runQuery(strSqlSearch1, true);
                while (rsSearch.next()) {
                    intCuantosLink += rsSearch.getInt("cuantos");
                }
                rsSearch.close();
                //Consultamos si hay facturas surtidas ligadas a este pedido..
                strSqlSearch1 = "select COUNT(DISTINCT vta_facturas.FAC_ID) as cuantos\n"
                        + " from vta_facturas, vta_facturasdeta,vta_pedidosdeta ,vta_pedidos\n"
                        + " where vta_facturas.FAC_ID = vta_facturasdeta.FAC_ID\n"
                        + " and vta_facturas.FAC_ANULADA = 0 "
                        + " and vta_pedidosdeta.PDD_ID = vta_facturasdeta.PDD_ID\n"
                        + " and vta_pedidos.PD_ID = vta_pedidosdeta.PD_ID\n"
                        + " and vta_pedidos.PD_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                rsSearch = oConn.runQuery(strSqlSearch1, true);
                while (rsSearch.next()) {
                    intCuantosLink += rsSearch.getInt("cuantos");
                }
                rsSearch.close();
                //Consultamos si hay surtidos de mercancia ligados a este pedido
                boolean bolMercanciaSurt = false;
                strSqlSearch1 = "select count(DISTINCT vta_movprod.MP_ID) as cuantos\n"
                        + " from vta_pedidosdeta,vta_movproddeta ,vta_pedidos,vta_movprod\n"
                        + " where \n"
                        + "  vta_movproddeta.MPD_IDORIGEN = vta_pedidosdeta.PDD_ID\n"
                        + " and vta_pedidos.PD_ID = vta_pedidosdeta.PD_ID\n"
                        + " and vta_movproddeta.MPD_ENTRADAS = 0\n"
                        + " and vta_movprod.MP_ANULADO = 0 "
                        + " AND vta_movprod.MP_ID = vta_movproddeta.MP_ID\n"
                        + " AND vta_movprod.PD_ID = vta_pedidos.PD_ID\n"
                        + " and vta_pedidos.PD_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                rsSearch = oConn.runQuery(strSqlSearch1, true);
                while (rsSearch.next()) {
                    intCuantosLink += rsSearch.getInt("cuantos");
                    if (rsSearch.getInt("cuantos") > 0) {
                        bolMercanciaSurt = true;
                    }
                }
                rsSearch.close();
                if (intCuantosLink > 0) {
                    this.strResultLast = "ERROR:No se puede cancelar el pedido debido a que hay ventas vinculadas";
                    if (bolMercanciaSurt) {
                        this.strResultLast += " o mercancia surtida ";
                    }
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage());
                this.strResultLast = "ERROR:" + ex.getMessage();
            }
        }
        // <editor-fold defaultstate="collapsed" desc="Validamos que no tenga pagos masivos aplicados">

        // </editor-fold>
        // </editor-fold>
        //Validamos que halla pasado las validaciones
        if (this.strResultLast.equals("OK")) {

            // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulada">
            if (this.document.getFieldInt(this.strPrefijoMaster + "_ANULADA") == 0) {

                // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion">
                if (this.bolTransaccionalidad) {
                    this.oConn.runQueryLMD("BEGIN");
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Si es pedido colocamos el estatus">
                if (this.strTipoVta.equals(Ticket.PEDIDO)) {
                    this.document.setFieldInt("PD_STATUS", 6);
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Ajustamos la existencia de los productos">
                String strResultInv = "OK";
                if (this.document.getFieldInt(this.strPrefijoMaster + "_ESSERV") == 0
                        && (this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA))) {
                    // <editor-fold defaultstate="collapsed" desc="Inventarios">
                    //Si esta activa la bandera de afectar inventarios procedemos
                    if (bolAfectaInv) {
                        log.debug("Si afecta inventarios...");
                        //Aplica inventarios
                        int intInvId = 0;
                        String strSql = "SELECT MP_ID from vta_movprod where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                        try {
                            ResultSet rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                intInvId = rs.getInt("MP_ID");
                            }
                            //if(rs.getStatement() != null )rs.getStatement().close(); 
                            rs.close();
                        } catch (SQLException ex) {
                            this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }
                        log.debug("Id de InvId " + intInvId);
                        //Si no existe movimiento de inventario no realizamos ajuste
                        //probablemente no funcionaba cuando se genero el ticket
                        if (intInvId != 0) {
                            //Crear objeto de inventario y aplicarlo
                            Inventario inv = new Inventario(this.oConn, this.varSesiones, this.request);
                            //Definimos el id del inventario
                            inv.getMovProd().setFieldInt("MP_ID", intInvId);
                            //Inicializamos
                            inv.Init();
                            //Definimos valores
                            inv.setBolTransaccionalidad(false);
                            inv.doTrxAnul();
                            strResultInv = inv.getStrResultLast();
                        } else {
                            log.debug("Buscamos si la venta se surtio de un pedido....");
                            if (this.document.getFieldInt(this.strPrefijoMaster + "_ES_POR_PEDIDOS") == 1) {
                                log.debug("Viene de un pedido que se surtio...");
                                //Buscamos en las partidad el id a detalle del movimiento
                                doGeneraEntradaPedidoSurtido(this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
                            }

                        }

                    }
                    // </editor-fold>
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Actualizamos el pedido en caso de que proceda">
                String strResTmp = AnulaPedidoConvertidoenVenta();
                if (!strResTmp.equals("OK")) {
                    strResultInv = strResTmp;
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Si es recurrente actualizamos la fecha">
                if (this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA)) {
                    if (this.document.getFieldInt(this.strPrefijoMaster + "_ESRECU") == 1 && this.intPedidoGenero == 0) {
                        try {
                            String strFechaVenci = "";
                            //Recuperamos la fecha de vencimiento del pedido
                            String strSql = "select PD_VENCI from vta_pedidos where PD_ID = " + this.document.getFieldInt("PD_RECU_ID");
                            ResultSet rs = oConn.runQuery(strSql, bolFolioGlobal);
                            while (rs.next()) {
                                strFechaVenci = rs.getString("PD_VENCI");
                            }
                            //if(rs.getStatement() != null )rs.getStatement().close(); 
                            rs.close();
                            //Solo si es diferente de vacio
                            if (!strFechaVenci.equals("")) {
                                String strMesAct = strFechaVenci.substring(4, 6);
                                String strAnioAct = strFechaVenci.substring(0, 4);
                                String strDiaAct = this.document.getFieldInt(strPrefijoMaster + "_DIAPER") + "";
                                if (this.document.getFieldInt(strPrefijoMaster + "_DIAPER") < 10) {
                                    strDiaAct = "0" + this.document.getFieldInt(strPrefijoMaster + "_DIAPER");
                                }
                                String strFechaBase = strAnioAct + strMesAct + strDiaAct;
                                FacRecurrente facRecurrente = new FacRecurrente(this.oConn, this.varSesiones);
                                String strFechaVenci2 = facRecurrente.calculaFechaVenciCancel(this, strFechaBase, strPrefijoMaster);
                                //Actualizamos la nueva fecha de vencimiento en el pedido origen
                                String strUpdatePedido = "update vta_pedidos set PD_VENCI = '" + strFechaVenci2 + "' "
                                        + " where PD_ID = " + this.document.getFieldInt("PD_RECU_ID");
                                this.oConn.runQueryLMD(strUpdatePedido);
                            }

                        } catch (SQLException ex) {
                            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
                // </editor-fold>

                //Validamos si no sucedio algun error al cancelar el inventario y todo sigue OK
                if (!strResultInv.equals("OK")) {
                    this.strResultLast = strResultInv;
                } else {

                    // <editor-fold defaultstate="collapsed" desc="Cancelacion de cargos">
                    String strSql = "SELECT MC_ID from vta_mov_cte "
                            + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                            + " AND MC_ESPAGO = 0 AND MC_ANULADO = 0 ";
                    try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            //Quitamos la aplicacion de los cargos
                            movCliente cta_cte = new movCliente(this.oConn, this.varSesiones, this.request);
                            //Definimos el id del inventario
                            cta_cte.getCta_clie().setFieldInt("MC_ID", rs.getInt("MC_ID"));
                            //Inicializamos
                            cta_cte.Init();
                            //Definimos valores
                            cta_cte.setBolTransaccionalidad(false);
                            //Indicamos si el movimiento es por una facturacion de tickets
                            if (this.bolFacturaTicket) {
                                cta_cte.getCta_clie().setFieldInt("MC_FT", 1);
                            }
                            cta_cte.doTrxAnul();
                            if (!cta_cte.getStrResultLast().equals("OK")) {
                                this.strResultLast = cta_cte.getStrResultLast();
                                break;
                            }
                        }
                        //if(rs.getStatement() != null )rs.getStatement().close(); 
                        rs.close();
                    } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                    }
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Cancelacion de pagos">
                    if (this.strResultLast.equals("OK")) {
                        //Cancelar pagos
                        strSql = "SELECT MC_ID from vta_mov_cte "
                                + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                                + " AND MC_ESPAGO = 1 AND MC_ANULADO = 0 ";
                        try {
                            ResultSet rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                //Quitamos la aplicacion de los cargos
                                movCliente cta_cte = new movCliente(this.oConn, this.varSesiones, this.request);
                                //Definimos el id del inventario
                                cta_cte.getCta_clie().setFieldInt("MC_ID", rs.getInt("MC_ID"));
                                //Inicializamos
                                cta_cte.Init();
                                //Indicamos si el movimiento es por una facturacion de tickets
                                if (this.bolFacturaTicket) {
                                    cta_cte.getCta_clie().setFieldInt("MC_FT", 1);
                                }
                                //Definimos valores
                                cta_cte.setBolTransaccionalidad(false);
                                cta_cte.doTrxAnul();
                                if (!cta_cte.getStrResultLast().equals("OK")) {
                                    this.strResultLast = cta_cte.getStrResultLast();
                                    break;
                                }
                            }
                            //if(rs.getStatement() != null )rs.getStatement().close(); 
                            rs.close();
                        } catch (SQLException ex) {
                            this.strResultLast = "ERROR:" + ex.getMessage();
                            ex.fillInStackTrace();
                        }

                    }
                    // </editor-fold>
                }
                if (this.strResultLast.equals("OK")) {

                    // <editor-fold defaultstate="collapsed" desc="Guardamos el documento principal">
                    this.document.setFieldInt(this.strPrefijoMaster + "_US_ANUL", this.varSesiones.getIntNoUser());
                    this.document.setFieldInt(this.strPrefijoMaster + "_ANULADA", 1);
                    this.document.setFieldString(this.strPrefijoMaster + "_HORANUL", this.fecha.getHoraActual());
                    if (strFechaAnul.equals("")) {
                        this.document.setFieldString(this.strPrefijoMaster + "_FECHAANUL", this.fecha.getFechaActual());
                    } else {
                        this.document.setFieldString(this.strPrefijoMaster + "_FECHAANUL", strFechaAnul);
                    }
                    String strResp1 = this.document.Modifica(oConn);
                    // </editor-fold>

                    if (!strResp1.equals("OK")) {
                        this.strResultLast = strResp1;
                    } else {
                        //Otros movimientos
                        if (this.strTipoVta.equals(Ticket.FACTURA)) {
                            // <editor-fold defaultstate="collapsed" desc="Obtenemos banderas de la empresa">
                            int intEMP_USECONTA = 0;
                            int intEMP_AVISOCANCEL = 0;
                            String strCtaVtasGlobal = "";
                            String strCtaVtasIVAGlobal = "";
                            String strCtaVtasIVATasa = "";
                            String strCtaVtasCteGlobal = "";
                            String strEMP_PASSCP = "";
                            String strEMP_USERCP = "";
                            String strEMP_URLCP = "";
                            int intEMP_VTA_DETA = 0;
                            int intEMP_CFD_CFDI = 0;
                            //Consultamos la info de la empresa
                            String strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,EMP_VTA_DETA,EMP_URLCP,"
                                    + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,EMP_PASSCP,EMP_USERCP,EMP_AVISOCANCEL,EMP_CFD_CFDI "
                                    + " FROM vta_empresas "
                                    + " WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                            try {
                                ResultSet rs = oConn.runQuery(strSql, true);
                                while (rs.next()) {
                                    intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                                    intEMP_AVISOCANCEL = rs.getInt("EMP_AVISOCANCEL");
                                    strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                                    strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                                    strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                                    strEMP_PASSCP = rs.getString("EMP_PASSCP");
                                    strEMP_USERCP = rs.getString("EMP_USERCP");
                                    intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                                    intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                                    strEMP_URLCP = rs.getString("EMP_URLCP");
                                }
                                //if(rs.getStatement() != null )rs.getStatement().close(); 
                                rs.close();
                            } catch (SQLException ex) {
                                this.strResultLast = "ERROR:" + ex.getMessage();
                                ex.fillInStackTrace();
                            }
                            // </editor-fold>
                            // <editor-fold defaultstate="collapsed" desc="Enviamos aviso de cancelacion al SAT si es CDFI">
                            if (intEMP_CFD_CFDI == 1 && this.document.getFieldInt("FAC_ES_CFD") == 0 && this.document.getFieldInt("FAC_ES_CBB") == 0) {
                                String strResp = CancelaComprobante();
                                if (!strResp.equals("OK")) {
                                    this.strResultLast = strResp;
                                }
                            }
                            // </editor-fold>

                            if (this.strResultLast.equals("OK")) {

                                //<editor-fold defaultstate="collapsed" desc="Contabilidad">
                                if (intEMP_USECONTA == 1) {
                                    //Instanciamos objetos para calcular la poliz contable
                                    ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                                    //Calcula la poliza para facturas
                                    contaUtil.CalculaPolizaContableFacturas(this.document.getFieldInt("EMP_ID"),
                                            strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
                                            strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
                                            this.document.getFieldInt("FAC_MONEDA"), document, this.document.getFieldInt("TI_ID"),
                                            this.document.getFieldInt("FAC_ID"),
                                            strNomTablaMaster, strPrefijoMaster, "CANCEL");
                                }
                                // </editor-fold>

                                // <editor-fold defaultstate="collapsed" desc="Mail de aviso de cancelacion">
                                //Validamos si se envia mail de aviso
                                if (intEMP_AVISOCANCEL == 1) {
                                    EnvioMailCancel();
                                }
                                // </editor-fold>   
                            }

                        }
                    }

                    // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
                    this.saveBitacora(this.strTipoVta, "ANULAR", this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
                    // </editor-fold>

                    // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
                    if (this.bolTransaccionalidad) {
                        if (this.strResultLast.equals("OK")) {
                            this.oConn.runQueryLMD("COMMIT");
                        } else {
                            this.oConn.runQueryLMD("ROLLBACK");
                        }
                    }
                    // </editor-fold>
                }
            } else {
                this.strResultLast = "ERROR:La operacion ya fue anulada";
            }
            // </editor-fold>

        }

    }

    protected String CancelaComprobante() {

        // <editor-fold defaultstate="collapsed" desc="Obtenemos datos del sello">
        String strNomKey = null;
        String strNomCert = null;
        String strPassKey = null;
        if (this.bolEsLocal) {
            // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(LOCAL)">
            String strSql = "SELECT "
                    + "EMP_NOMKEY,EMP_NOMCERT,"/**
                     * ,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE
                     */
                    + "EMP_PASSKEY AS unencrypted "
                    + "FROM vta_empresas "
                    + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strNomKey = rs.getString("EMP_NOMKEY");
                    strNomCert = rs.getString("EMP_NOMCERT");
                    strPassKey = rs.getString("unencrypted");
                    //La encripcion se hace desde el programa
                    Opalina opa = new Opalina();
                    try {
                        strPassKey = opa.DesEncripta(strPassKey, strMyPassSecret);
                    } catch (NoSuchAlgorithmException ex) {
                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                    } catch (NoSuchPaddingException ex) {
                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                    } catch (InvalidKeyException ex) {
                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                    } catch (IllegalBlockSizeException ex) {
                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                    } catch (BadPaddingException ex) {
                        log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                    }
                }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
            // </editor-fold>
        } else {
            // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
            String strSql = "SELECT "
                    + "EMP_NOMKEY,EMP_NOMCERT,"
                    + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted "
                    + "FROM vta_empresas "
                    + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strNomKey = rs.getString("EMP_NOMKEY");
                    strNomCert = rs.getString("EMP_NOMCERT");
                    strPassKey = rs.getString("unencrypted");
                }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            // </editor-fold>
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Preparamos y ejecutamos objeto de cancelacion">
        log.debug("Certificado..." + this.strPATHKeys + System.getProperty("file.separator") + strNomCert);
        log.debug("Key..." + this.strPATHKeys + System.getProperty("file.separator") + strNomKey);
        byte[] certificadoEmisor = PreparaCertificado(this.strPATHKeys + System.getProperty("file.separator") + strNomCert);
        //byte[] llavePrivadaEmisor = PreparaLlave(this.strPATHKeys + System.getProperty("file.separator") + strNomKey, strPassKey);
        byte[] llavePrivadaEmisor = getPrivateKey(this.strPATHKeys + System.getProperty("file.separator") + strNomKey);
        StringBuilder strNomFile = new StringBuilder("");
        //Instanciamos objeto que cancela
        SatCancelaCFDI cancela = new SatCancelaCFDI(this.strPATHKeys);
        if (this.strPrefijoMaster.equals("FAC")) {
            cancela.setStrTablaDoc("vta_facturas");
            cancela.setStrPrefijoDoc("FAC_");
            strNomFile.append("XmlSAT").append(this.document.getFieldInt(this.strPrefijoMaster + "_ID")).append(" .xml");
        } else {
            cancela.setStrTablaDoc("vta_ncredito");
            cancela.setStrPrefijoDoc("NC_");
            strNomFile.append("NC_XML").append(this.document.getFieldInt(this.strPrefijoMaster + "_ID")).append(".xml");
        }
        //Validamos si existe o es de la nueva version
        try {
            File file = new File(this.strPATHXml + System.getProperty("file.separator") + strNomFile.toString());//specify the file path 
            log.debug("file: " + file.getAbsolutePath());
            if (!file.exists()) {
                //Version 2.0
                strNomFile = new StringBuilder("");
                ERP_MapeoFormato mapeoXml = null;
                if (this.strPrefijoMaster.equals("FAC")) {
                    mapeoXml = new ERP_MapeoFormato(1);
                } else {
                    mapeoXml = new ERP_MapeoFormato(5);
                }
                strNomFile.append(getNombreFileXml(mapeoXml, this.document.getFieldInt(this.strPrefijoMaster + "_ID"),
                        this.document.getFieldString(this.strPrefijoMaster + "_RAZONSOCIAL"),
                        this.document.getFieldString(this.strPrefijoMaster + "_FECHA"),
                        this.document.getFieldString(this.strPrefijoMaster + "_FOLIO")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        cancela.setIntIdDoc(this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
        cancela.setStrPathXml(this.strPATHXml);
        cancela.setoConn(oConn);
        cancela.setLlavePrivadaEmisor(llavePrivadaEmisor);
        cancela.setCertificadoEmisor(certificadoEmisor);
        cancela.setStrPasswordEmisor(strPassKey);
        String strResp = cancela.timbra_Factura(strNomFile.toString());
        // </editor-fold>

        return strResp;

    }
    // <editor-fold defaultstate="collapsed" desc="Metodos Auxiliares para cancelacion">
    //Prepara la llave a usar para cancelar el comprobante

    protected byte[] PreparaLlave(String strPathKey, String strPassKey) {
        byte[] Llave = null;
        try {
            PrivateKey key;
            SATXml sat = new SATXml();
            key = sat.ObtenerPrivateKey(strPathKey, strPassKey);
            System.out.println(" key " + key.getAlgorithm() + " " + key.getFormat());
            Llave = key.getEncoded();
            sat = null;
        } catch (Exception ex) {
            log.error(" Error al abrir sello al cancelar documento:" + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        }
        return Llave;
    }
    //Prepara la llave a usar para cancelar el comprobante

    protected byte[] PreparaCertificado(String strPathCert) {
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
    // </editor-fold>

    public void doTrxRevive() {
        //Aplica solo cuando se cancela una factura y los tickets se reviven
        this.strResultLast = "OK";
        //Validamos que todos los campos basico se encuentren
        if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la operacion por REVIVIR";
        }
        //Inicializamos la operacion
        if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
        }
        //Validamos que este anulado
        if (this.document.getFieldInt(this.strPrefijoMaster + "_ANULADA") == 1) {
            String strFiltroFactRem = "";
            //Indicamos si el movimiento es por una facturacion de tickets
            if (this.bolFacturaTicket) {
                strFiltroFactRem = " AND MC_FT = 1 ";
            }
            //Revivir cargos
            String strSql = "SELECT MC_ID from vta_mov_cte "
                    + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                    + " AND MC_ESPAGO = 0 AND MC_ANULADO = 1 " + strFiltroFactRem;
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    //Quitamos la aplicacion de los cargos
                    movCliente cta_cte = new movCliente(this.oConn, this.varSesiones, this.request);
                    //Definimos el id del del cargo
                    cta_cte.getCta_clie().setFieldInt("MC_ID", rs.getInt("MC_ID"));
                    //Inicializamos
                    cta_cte.Init();
                    //Indicamos si el movimiento es por una facturacion de tickets
                    if (this.bolFacturaTicket) {
                        cta_cte.getCta_clie().setFieldInt("MC_FT", 0);
                    }
                    //Definimos valores
                    cta_cte.setBolTransaccionalidad(false);
                    cta_cte.doTrxRevive();
                    if (!cta_cte.getStrResultLast().equals("OK")) {
                        this.strResultLast = cta_cte.getStrResultLast();
                        break;
                    }
                }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            //termina revivir cargos
            if (this.strResultLast.equals("OK")) {
                //Revivir pagos
                strSql = "SELECT MC_ID from vta_mov_cte "
                        + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                        + " AND MC_ESPAGO = 1 AND MC_ANULADO = 1 " + strFiltroFactRem;
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        //Quitamos la aplicacion de los cargos
                        movCliente cta_cte = new movCliente(this.oConn, this.varSesiones, this.request);
                        //Definimos el id del pago
                        cta_cte.getCta_clie().setFieldInt("MC_ID", rs.getInt("MC_ID"));
                        //Inicializamos
                        cta_cte.Init();
                        //Indicamos si el movimiento es por una facturacion de tickets
                        if (this.bolFacturaTicket) {
                            cta_cte.getCta_clie().setFieldInt("MC_FT", 0);
                        }
                        //Definimos valores
                        cta_cte.setBolTransaccionalidad(false);
                        cta_cte.doTrxRevive();
                        if (!cta_cte.getStrResultLast().equals("OK")) {
                            this.strResultLast = cta_cte.getStrResultLast();
                            break;
                        }
                    }
                    //if(rs.getStatement() != null )rs.getStatement().close(); 
                    rs.close();
                } catch (SQLException ex) {
                    this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
            }
            //Termina revivir de pagos
            if (this.strResultLast.equals("OK")) {
                //Definimos campos
                this.document.setFieldInt(this.strPrefijoMaster + "_US_ANUL", 0);
                this.document.setFieldInt(this.strPrefijoMaster + "_ANULADA", 0);
                this.document.setFieldInt("FAC_ID", 0);
                this.document.setFieldString(this.strPrefijoMaster + "_HORANUL", "");
                this.document.setFieldString(this.strPrefijoMaster + "_FECHAANUL", "");
                String strResp1 = this.document.Modifica(oConn);
                if (!strResp1.equals("OK")) {
                    this.strResultLast = strResp1;
                } else {
                    if (this.strTipoVta.equals(Ticket.FACTURA)
                            && this.strTipoVta.equals(Ticket.TICKET)) {
                        //Actualizamos la poliza contable
                        PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
                        poli.setStrOper("REVIVE");
                        int intValOper = PolizasContables.TICKET;
                        if (this.strTipoVta.equals(Ticket.FACTURA)) {
                            intValOper = PolizasContables.FACTURA;
                        }
                        poli.callRemote(this.document.getFieldInt(this.strPrefijoMaster + "_ID"), intValOper);
                    }
                }
                //Guardamos la bitacora
                this.saveBitacora(this.strTipoVta, "REVIVIR", this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
            }
        } else {
            this.strResultLast = "ERROR:LA OPERACION NO ESTA ANULADA";
        }
        //Terminamos la operacion
        if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }
        }
    }

    public void doTrxSaldo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doTrxMod() {
        this.strResultLast = "OK";
        //Validamos que todos los campos basico se encuentren
        if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la operacion por modificar";
        }

        //Validamos si es un ticket recurrente para actualizar la fecha de vencimiento
        if (this.getStrTipoVta().equals(Ticket.PEDIDO)) {
            if (this.document.getFieldInt(this.strPrefijoMaster + "_ESRECU") == 1) {
                log.debug("Es un pedido recurrente");
                String strVenciNext = this.document.getFieldString(this.strPrefijoMaster + "_VENCI");
                if (this.document.getFieldInt(this.strPrefijoMaster + "_DIAPER") > 9) {
                    strVenciNext = strVenciNext.substring(0, 6) + "" + this.document.getFieldInt(this.strPrefijoMaster + "_DIAPER");
                } else {
                    strVenciNext = strVenciNext.substring(0, 6) + "0" + this.document.getFieldInt(this.strPrefijoMaster + "_DIAPER");
                }
                log.debug("strVenciNext:" + strVenciNext);
                this.document.setFieldString(this.strPrefijoMaster + "_VENCI", strVenciNext);
            }
        }

        //Inicializamos la operacion
        if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
        }
        //Validamos que no este anulado
        if (this.document.getFieldInt(this.strPrefijoMaster + "_ANULADA") == 0) {
            //Definimos campos
            this.document.setFieldInt(this.strPrefijoMaster + "_US_MOD", this.varSesiones.getIntNoUser());
            String strResp1 = this.document.Modifica(oConn);
            if (!strResp1.equals("OK")) {
                this.strResultLast = strResp1;
            } else {
                //Borrar el detalle
                String strNomTable = "";
                if (this.getStrTipoVta().equals(Ticket.PEDIDO)) {
                    strNomTable = "vta_pedidosdeta";
                }
                if (this.getStrTipoVta().equals(Ticket.COTIZACION)) {
                    strNomTable = "vta_cotizadeta";
                }
                if (this.getStrTipoVta().equals(Ticket.TICKET)) {
                    strNomTable = "vta_ticketsdeta";
                }
                if (this.getStrTipoVta().equals(Ticket.FACTURA)) {
                    strNomTable = "vta_facturasdeta";
                }
                String strDelete = "delete from " + strNomTable + " where " + this.strPrefijoMaster + "_ID" + " = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                oConn.runQueryLMD(strDelete);
                //guardar detalle
                int intId = Integer.valueOf(this.document.getValorKey());
                Iterator<TableMaster> it = this.lstMovs.iterator();
                while (it.hasNext()) {
                    TableMaster deta = it.next();
                    deta.setFieldInt(this.strPrefijoMaster + "_ID", intId);
                    deta.setBolGetAutonumeric(true);
                    String strRes2 = deta.Agrega(oConn);
                    //Validamos si todo fue satisfactorio
                    if (!strRes2.equals("OK")) {
                        this.strResultLast = strRes2;
                        break;
                    }
                }
                if (this.strResultLast.equals("OK")) {
                    //Actualizamos la poliza contable
                    if (this.strTipoVta.equals(Ticket.FACTURA)
                            && this.strTipoVta.equals(Ticket.TICKET)) {
                        //Actualizamos la poliza contable
                        PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
                        poli.setStrOper("MODIFY");
                        poli.callRemote(this.document.getFieldInt(this.strPrefijoMaster + "_ID"), PolizasContables.TICKET);
                    }
                }
            }
            //Guardamos la bitacora
            this.saveBitacora(this.strTipoVta, "MODIFICAR", this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
        } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
        }
        //Terminamos la operacion
        if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }
        }
    }

    /*
    Marca el pedido como vendido y calcula el bakOrder
     */
    private String PedidoConvertidoenVenta(int intIdFactura) {
        String strRes = "OK";
        //Validamos si procede
        if (this.strTipoVta.equals(Ticket.FACTURA)
                || this.strTipoVta.equals(Ticket.TICKET)) {
            //
            // <editor-fold defaultstate="collapsed" desc="Se genero el movimiento por un pedido">
            if (this.intPedidoGenero != 0) {
                // <editor-fold defaultstate="collapsed" desc="Marcamos el pedido como vendido">
                String strUpdate = "UPDATE vta_pedidos set ";
                if (this.strTipoVta.equals(Ticket.FACTURA)) {
                    strUpdate += " FAC_ID = " + this.document.getValorKey();
                } else {
                    strUpdate += " TKT_ID = " + this.document.getValorKey();
                }
                strUpdate += " WHERE PD_ID = " + this.intPedidoGenero;
                oConn.runQueryLMD(strUpdate);
                // </editor-fold>
                //*******************Calculamos el bakOrder***************************
                //Arreglos con el detalle del nuevo pedido de Bak Order
                ArrayList<TableMaster> lstDetaBK = new ArrayList<TableMaster>();
                //Obtenemos los datos del pedido original
                vta_pedidosdeta detaTmp = new vta_pedidosdeta();
                ArrayList<TableMaster> lstDeta = detaTmp.ObtenDatosVarios(" PD_ID = " + this.intPedidoGenero, oConn);
                // <editor-fold defaultstate="collapsed" desc="Comparamos las filas">
                Iterator<TableMaster> itPedido = lstDeta.iterator();
                while (itPedido.hasNext()) {
                    TableMaster detaPedido = itPedido.next();
                    boolean bolEncontro = false;
                    double dblCantidadDeuda = 0;
                    // <editor-fold defaultstate="collapsed" desc="Iteramos en la partida de la venta para ver cuanto se facturo">
                    Iterator<TableMaster> it = this.lstMovs.iterator();
                    while (it.hasNext()) {
                        TableMaster deta = it.next();
                        if (deta.getFieldInt("PR_ID") == detaPedido.getFieldInt("PR_ID")) {
                            bolEncontro = true;
                            //Comparamos las cantidades
                            if (deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD")
                                    < detaPedido.getFieldDouble("PDD_CANTIDAD")) {
                                dblCantidadDeuda = detaPedido.getFieldDouble("PDD_CANTIDAD")
                                        - deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD");
                            }
                        }
                    }
                    // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc="Si encontro la fila y tiene adeudo añadimos la partida">
                    if (bolEncontro && dblCantidadDeuda > 0) {
                        TableMaster BkPartida = (TableMaster) detaPedido.clone();
                        BkPartida.setFieldDouble("PDD_CANTIDAD", dblCantidadDeuda);
                        lstDetaBK.add(BkPartida);
                    }
                    // </editor-fold>
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Validamos si hubo diferencias procedemos a la generacion del BAK ORDER">
                if (!lstDetaBK.isEmpty()) {
                    //Generamos un nuevo pedido....
                    vta_pedidos pedido = new vta_pedidos();
                    pedido.ObtenDatos(this.intPedidoGenero, oConn);
                    //Cambiamos los datos del nuevo pedido
                    pedido.setFieldInt("TKT_ID", 0);
                    pedido.setFieldInt("FAC_ID", 0);
                    pedido.setFieldInt("PD_ESBK", 1);//Marca de que un bakorder
                    pedido.setFieldInt("PD_IDBK", this.intPedidoGenero);
                    pedido.setValorKey("0");
                    if (this.strTipoVta.equals(Ticket.FACTURA)) {
                        pedido.setFieldInt("FAC_IDBK", Integer.valueOf(this.document.getValorKey()));
                    } else {
                        pedido.setFieldInt("TKT_IDBK", Integer.valueOf(this.document.getValorKey()));
                    }
                    pedido.setFieldString("PD_FECHACREATE", this.fecha.getFechaActual());
                    pedido.setFieldString("PD_FECHA", this.fecha.getFechaActual());
                    pedido.setFieldString("PD_HORA", this.fecha.getHoraActual());
                    pedido.setFieldDouble("PD_SALDO", 0.0);
                    pedido.setFieldInt("PD_US_ALTA", varSesiones.getIntNoUser());
                    //Recalculamos el detalle
                    double dblBKImporte = 0;
                    double dblBKImpuesto1 = 0;
                    double dblBKImpuesto2 = 0;
                    double dblBKImpuesto3 = 0;
                    double dblBKTotal = 0;
                    Impuestos imp = new Impuestos(this.dblSC_TASA1, this.dblSC_TASA2,
                            this.dblSC_TASA3,
                            this.intSC_SOBRIMP1_2, this.intSC_SOBRIMP1_3, this.intSC_SOBRIMP2_3);
                    // <editor-fold defaultstate="collapsed" desc="Recalculamos el importe">
                    itPedido = lstDetaBK.iterator();
                    while (itPedido.hasNext()) {
                        TableMaster tbn = itPedido.next();
                        double dblImporteBK = tbn.getFieldDouble("PDD_CANTIDAD") * tbn.getFieldDouble("PDD_PRECIO");
                        tbn.setFieldDouble("PDD_IMPORTE", dblImporteBK);
                        dblBKTotal += dblImporteBK;
                        double dblBase1 = 0;
                        double dblBase2 = 0;
                        double dblBase3 = 0;
                        if (tbn.getFieldDouble("PDD_TASAIVA1") != 0) {
                            dblBKImpuesto1 = dblImporteBK - (dblImporteBK / (1 + (tbn.getFieldDouble("PDD_TASAIVA1") / 100)));
                            dblBase1 = dblImporteBK;
                        }
                        if (tbn.getFieldDouble("PDD_TASAIVA2") != 0) {
                            dblBase2 = dblImporteBK;
                        }
                        if (tbn.getFieldDouble("PDD_TASAIVA3") != 0) {
                            dblBase3 = dblImporteBK;
                        }
                        imp.CalculaImpuesto(dblBase1, dblBase2, dblBase3);
                        dblBKImpuesto1 += imp.dblImpuesto1;
                        dblBKImpuesto2 += imp.dblImpuesto2;
                        dblBKImpuesto3 += imp.dblImpuesto3;
                    }
                    dblBKImporte += dblBKTotal - imp.dblImpuesto1 - imp.dblImpuesto2 - imp.dblImpuesto3;
                    // </editor-fold>
                    pedido.setFieldDouble("PD_TOTAL", dblBKTotal);
                    pedido.setFieldDouble("PD_IMPORTE", dblBKImporte);
                    pedido.setFieldDouble("PD_IMPUESTO1", dblBKImpuesto1);
                    pedido.setFieldDouble("PD_IMPUESTO2", dblBKImpuesto2);
                    pedido.setFieldDouble("PD_IMPUESTO3", dblBKImpuesto3);
                    // <editor-fold defaultstate="collapsed" desc="Insertamos el detalle del pedido">
                    String strResBK = pedido.Agrega(oConn);
                    if (strResBK.equals("OK")) {
                        //Agregamos el detalle
                        itPedido = lstDetaBK.iterator();
                        while (itPedido.hasNext()) {
                            TableMaster tbn = itPedido.next();
                            strResBK = tbn.Agrega(oConn);
                            if (!strResBK.equals("OK")) {
                                strRes = strResBK;
                                break;
                            }
                        }
                    } else {
                        strRes = strResBK;
                    }
                    // </editor-fold>
                }
                // </editor-fold>
            } else {
                //Varios pedidos en una sola factura
                if (this.lstPedidosOrigen != null) {
                    strRes = ActualizaStatusPedidosOrigen();
                }
            }
            //Evaluamos si hay que actualizar el estatus del surtido de facturas/remisiones
            log.debug("Lista de series...." + this.lstSeries);
            if (this.lstSeries != null) {
                if (!this.lstSeries.isEmpty()) {
                    ActualizaStatusSeries(true, intIdFactura);
                }
            }
            // </editor-fold>
        }
        return strRes;
    }

    /*
    Desmarca el pedido como vendido y calcula el bakOrder
     */
    private String AnulaPedidoConvertidoenVenta() {
        String strRes = "OK";
        //Validamos si procede
        if (this.strTipoVta.equals(Ticket.FACTURA)
                || this.strTipoVta.equals(Ticket.TICKET)) {
            if (this.document.getFieldInt("PD_ID") != 0) {
                //DesMarcamos el pedido como vendido
                String strUpdate = "UPDATE vta_pedidos set ";
                if (this.strTipoVta.equals(Ticket.FACTURA)) {
                    strUpdate += " FAC_ID = 0 ";
                } else {
                    strUpdate += " TKT_ID = 0 ";
                }
                strUpdate += " WHERE PD_ID = " + this.document.getFieldInt("PD_ID");
                oConn.runQueryLMD(strUpdate);
                //Anulamos el bakOrder
            } else {
                //Buscamos si un pedido
                if (this.document.getFieldInt(this.strPrefijoMaster + "_ES_POR_PEDIDOS") != 0) {
                    strRes = AnulaStatusPedidosOrigen();
                }
            }

        }
        return strRes;
    }

    /**
     * En caso de que la factura se halla originado por uno o varios pedidos
     */
    private String ActualizaStatusPedidosOrigen() {
        String strRes = "OK";
        //Iteramos por cada pedido
        for (int k = 0; k < this.lstPedidosOrigen.length; k++) {
            int intPedidoId = this.lstPedidosOrigen[k];
            boolean bolFacturadoCompleto = true;
            //Obtenemos su detalle
            vta_pedidosdeta deta = new vta_pedidosdeta();
            ArrayList<TableMaster> lstDeta = deta.ObtenDatosVarios(" PD_ID = " + intPedidoId, oConn);
            Iterator<TableMaster> it = lstDeta.iterator();
            while (it.hasNext()) {
                TableMaster tbn = it.next();
                // <editor-fold defaultstate="collapsed" desc="Iteramos por las partidas de la factura">
                Iterator<TableMaster> it2 = this.lstMovs.iterator();
                while (it2.hasNext()) {
                    TableMaster detaFact = it2.next();
                    //Comparamos el detalle con el detalle de los items del pedido
                    if (tbn.getFieldInt("PDD_ID") == detaFact.getFieldInt("PDD_ID")) {
                        double dblCantidad = 0;
                        String strCveDeta = "";
                        if (this.strTipoVta.equals(Ticket.TICKET)) {
                            dblCantidad = detaFact.getFieldDouble("TKTD_CANTIDAD");
                            strCveDeta = detaFact.getFieldString("TKTD_CVE");
                        } else {
                            dblCantidad = detaFact.getFieldDouble("FACD_CANTIDAD");
                            strCveDeta = detaFact.getFieldString("FACD_CVE");
                        }
                        //Validamos que no se factura mas de lo surtido
                        if ((tbn.getFieldDouble("PDD_CANT_FACT") + dblCantidad)
                                > tbn.getFieldDouble("PDD_CANTIDADSURTIDA")) {
                            strRes = "ERROR: La cantidad por facturar"
                                    + " es mayor a la cantidad surtida "
                                    + "en el item " + strCveDeta;
                            break;
                        }
                        //Actualizamos la cantidad facturada
                        double dblNuevaFacturada = tbn.getFieldDouble("PDD_CANT_FACT") + dblCantidad;
                        tbn.setFieldDouble("PDD_CANT_FACT", dblNuevaFacturada);
                        tbn.Modifica(oConn);
                    }
                }
                // </editor-fold>
                //Validamos si lo facturado es igual a lo pedido
                if (tbn.getFieldDouble("PDD_CANT_FACT") < tbn.getFieldDouble("PDD_CANTIDAD")) {
                    bolFacturadoCompleto = false;
                }
            }
            // <editor-fold defaultstate="collapsed" desc="Marcamos el estatus del pedido">
            int intStatus = 0;
            if (bolFacturadoCompleto) {
                intStatus = 5;
            } else {
                intStatus = 9;
            }
            StringBuilder strUpdate = new StringBuilder("UPDATE vta_pedidos set PD_STATUS = ");
            strUpdate.append(intStatus).append(" WHERE PD_ID = ").append(intPedidoId);
            this.oConn.runQueryLMD(strUpdate.toString());
            // </editor-fold>
        }
        return strRes;
    }

    /**
     * En caso de que la factura se halla originado por uno o varios pedidos
     */
    private String AnulaStatusPedidosOrigen() {
        String strRes = "OK";
        log.debug("ANULA TICKET/FACTURA DE PEDIDO....");
        //Lista de pedidos facturados
        ArrayList<Integer> lstPediFacturados = new ArrayList<Integer>();
        // <editor-fold defaultstate="collapsed" desc="Del detalle de los pedidos obtenemos que pedidos se facturaron">
        if (this.strTipoVta.equals(Ticket.FACTURA)) {
            log.debug("FACTURA");
            vta_facturadeta deta = new vta_facturadeta();
            this.lstMovs = deta.ObtenDatosVarios(this.strPrefijoMaster + "_ID  = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID"), oConn);
        } else {
            log.debug("TICKET");
            vta_ticketsdeta deta = new vta_ticketsdeta();
            this.lstMovs = deta.ObtenDatosVarios(this.strPrefijoMaster + "_ID  = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID"), oConn);
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Buscamos los ids de los pedidos">
        Iterator<TableMaster> it4 = this.lstMovs.iterator();
        while (it4.hasNext()) {
            TableMaster tbn = it4.next();
            String strSql = "Select PD_ID from vta_pedidosdeta where PDD_ID = " + tbn.getFieldInt("PDD_ID");
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    int inPedidoId = rs.getInt("PD_ID");
                    if (!lstPediFacturados.contains(inPedidoId)) {
                        lstPediFacturados.add(inPedidoId);
                    }
                }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Recorremos los pedidos y obtenemos su detalle">
        Iterator<Integer> it3 = lstPediFacturados.iterator();
        while (it3.hasNext()) {
            int intPedidoId = it3.next();
            //Obtenemos su detalle
            vta_pedidosdeta deta = new vta_pedidosdeta();
            ArrayList<TableMaster> lstDetaPedi = deta.ObtenDatosVarios(" PD_ID = " + intPedidoId, oConn);
            Iterator<TableMaster> it = lstDetaPedi.iterator();
            while (it.hasNext()) {
                TableMaster tbn = it.next();
                // <editor-fold defaultstate="collapsed" desc="Iteramos por las partidas de la factura">
                Iterator<TableMaster> it2 = this.lstMovs.iterator();
                while (it2.hasNext()) {
                    TableMaster detaFact = it2.next();
                    //Comparamos el detalle con el detalle de los items del pedido
                    if (tbn.getFieldInt("PDD_ID") == detaFact.getFieldInt("PDD_ID")) {
                        //Actualizamos la cantidad facturada
                        double dblNuevaFacturada = tbn.getFieldDouble("PDD_CANT_FACT") - detaFact.getFieldDouble("FACD_CANTIDAD");
                        tbn.setFieldDouble("PDD_CANT_FACT", dblNuevaFacturada);
                        tbn.Modifica(oConn);
                    }
                    // <editor-fold defaultstate="collapsed" desc="Evaluamos si tienen numero de serie">
                    log.debug("Serie " + detaFact.getFieldString(this.strPrefijoDeta + "_NOSERIE"));
                    if (!detaFact.getFieldString(this.strPrefijoDeta + "_NOSERIE").isEmpty()) {
                        //separamos los numeros de serie para buscarlos en los movimientos de almacen
                        String[] lstSeriesX = detaFact.getFieldString(this.strPrefijoDeta + "_NOSERIE").split(",");
                        for (int jk = 0; jk < lstSeriesX.length; jk++) {
                            StringBuilder strSqlMovs = new StringBuilder();
                            log.debug("Serie por revisar..." + lstSeriesX[jk]);
                            strSqlMovs.append("select MPD_ID from vta_movproddeta  where " + " MPD_SERIE_VENDIDO = 1 AND MPD_SERIE_FACTURA = ").
                                    append(this.document.getFieldInt(this.strPrefijoMaster + "_ID")).
                                    append(" AND MPD_IDORIGEN = ").
                                    append(detaFact.getFieldInt("PDD_ID")).append(" and PL_NUMLOTE ='").
                                    append(lstSeriesX[jk]).append("'");
                            try {
                                ResultSet rs = this.oConn.runQuery(strSqlMovs.toString(), true);
                                while (rs.next()) {
                                    int intMPD_ID = rs.getInt("MPD_ID");
                                    if (lstSeries == null) {
                                        lstSeries = new ArrayList<vta_movproddeta>();
                                    }
                                    vta_movproddeta mov = new vta_movproddeta();
                                    mov.ObtenDatos(intMPD_ID, oConn);
                                    lstSeries.add(mov);
                                }
                                //if(rs.getStatement() != null )rs.getStatement().close(); 
                                rs.close();
                            } catch (SQLException ex) {
                                log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                            }

                        }
                    }
                    // </editor-fold>
                }
                // </editor-fold>
            }
            //A cada uno de estos pedidos si ya tienen estatus de cerrados se los quitamos
            int intStatus = 9;
            StringBuilder strUpdate = new StringBuilder("UPDATE vta_pedidos set PD_STATUS = ");
            strUpdate.append(intStatus).append(" WHERE PD_ID = ").append(intPedidoId);
            this.oConn.runQueryLMD(strUpdate.toString());
        }
        //Evaluamos si hay movimientos de numero de serie para desmarcar el uso de ellos
        if (lstSeries != null) {
            if (!lstSeries.isEmpty()) {
                ActualizaStatusSeries(false, 0);
            }
        }
        // </editor-fold>
        return strRes;
    }

    /**
     * Formatea los conceptos de las partidas dependiendo de comodines
     * establecidos
     */
    protected void FormatoDescripNotas() {
        Iterator<TableMaster> it = this.lstMovs.iterator();
        while (it.hasNext()) {
            TableMaster deta = it.next();
            String strDesc = deta.getFieldString(this.strPrefijoDeta + "_DESCRIPCION");
            String strComentario = deta.getFieldString(this.strPrefijoDeta + "_COMENTARIO");
            if (strDesc.contains("[") && strDesc.contains("]")) {
                if (strDesc.contains("[DIA_FAC]")) {
                    String strDia = this.document.getFieldString(this.strPrefijoMaster + "_FECHA").substring(6, 8);
                    strDesc = strDesc.replace("[DIA_FAC]", strDia);
                }
                if (strDesc.contains("[MES_FAC]")) {
                    String strMes = this.document.getFieldString(this.strPrefijoMaster + "_FECHA").substring(4, 6);
                    String strNomMes = "";
                    if (strMes.equals("01")) {
                        strNomMes = "ENERO";
                    }
                    if (strMes.equals("02")) {
                        strNomMes = "FEBRERO";
                    }
                    if (strMes.equals("03")) {
                        strNomMes = "MARZO";
                    }
                    if (strMes.equals("04")) {
                        strNomMes = "ABRIL";
                    }
                    if (strMes.equals("05")) {
                        strNomMes = "MAYO";
                    }
                    if (strMes.equals("06")) {
                        strNomMes = "JUNIO";
                    }
                    if (strMes.equals("07")) {
                        strNomMes = "JULIO";
                    }
                    if (strMes.equals("08")) {
                        strNomMes = "AGOSTO";
                    }
                    if (strMes.equals("09")) {
                        strNomMes = "SEPTIEMBRE";
                    }
                    if (strMes.equals("10")) {
                        strNomMes = "OCTUBRE";
                    }
                    if (strMes.equals("11")) {
                        strNomMes = "NOVIEMBRE";
                    }
                    if (strMes.equals("12")) {
                        strNomMes = "DICIEMBRE";
                    }
                    strDesc = strDesc.replace("[MES_FAC]", strNomMes);
                }
                if (strDesc.contains("[ANIO_FAC]")) {
                    String strAnio = this.document.getFieldString(this.strPrefijoMaster + "_FECHA").substring(0, 4);
                    strDesc = strDesc.replace("[ANIO_FAC]", strAnio);
                }
                deta.setFieldString(this.strPrefijoDeta + "_DESCRIPCION", strDesc);
            }
            if (strComentario.contains("[") && strComentario.contains("]")) {
                if (strComentario.contains("[DIA_FAC]")) {
                    String strDia = this.document.getFieldString(this.strPrefijoMaster + "_FECHA").substring(6, 8);
                    strComentario = strComentario.replace("[DIA_FAC]", strDia);
                }
                if (strComentario.contains("[MES_FAC]")) {
                    String strMes = this.document.getFieldString(this.strPrefijoMaster + "_FECHA").substring(4, 6);
                    String strNomMes = "";
                    if (strMes.equals("01")) {
                        strNomMes = "enero";
                    }
                    if (strMes.equals("02")) {
                        strNomMes = "febrero";
                    }
                    if (strMes.equals("03")) {
                        strNomMes = "marzo";
                    }
                    if (strMes.equals("04")) {
                        strNomMes = "abril";
                    }
                    if (strMes.equals("05")) {
                        strNomMes = "mayo";
                    }
                    if (strMes.equals("06")) {
                        strNomMes = "junio";
                    }
                    if (strMes.equals("07")) {
                        strNomMes = "julio";
                    }
                    if (strMes.equals("08")) {
                        strNomMes = "agosto";
                    }
                    if (strMes.equals("09")) {
                        strNomMes = "septiembre";
                    }
                    if (strMes.equals("10")) {
                        strNomMes = "octubre";
                    }
                    if (strMes.equals("11")) {
                        strNomMes = "noviembre";
                    }
                    if (strMes.equals("12")) {
                        strNomMes = "diciembre";
                    }
                    strComentario = strComentario.replace("[MES_FAC]", strNomMes);
                }
                if (strComentario.contains("[ANIO_FAC]")) {
                    String strAnio = this.document.getFieldString(this.strPrefijoMaster + "_FECHA").substring(0, 4);
                    strComentario = strComentario.replace("[ANIO_FAC]", strAnio);
                }
                deta.setFieldString(this.strPrefijoDeta + "_COMENTARIO", strComentario);
            }
        }
    }

    /**
     * Genera los items para las partidas contables cuando el detalle de la
     * venta es de acuerdo a las partidas de los productos
     *
     * @param poli Es la poliza
     * @param bolEsCargo Es el tipo de movimiento(cargo u abono)
     * @param strFolio Es el folio
     */
    protected void getPoliDetaProd(PolizasContables poli, boolean bolEsCargo, String strFolio) {
        //Lista de los cuentas agrupadas
        ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
        //Asignamos los valores de las partidas
        Iterator<TableMaster> it = this.lstMovs.iterator();
        while (it.hasNext()) {
            TableMaster deta = it.next();
            //Consultamos en la tabla de productos a que cuenta pertenecen...
            String strSql = "select PR_CTA_VENTA,PR_CTA_COSTO from vta_producto where PR_ID = " + deta.getFieldInt("PR_ID");
            ResultSet rs3;
            try {
                rs3 = this.oConn.runQuery(strSql);
                while (rs3.next()) {
                    String strPR_CTA_VENTA = rs3.getString("PR_CTA_VENTA");
                    if (strPR_CTA_VENTA.isEmpty()) {
                        strPR_CTA_VENTA = this.strCtaEmpty;
                    }
                    //Validamos si existe
                    boolean bolExiste = false;
                    Iterator<PoliCtas> it2 = lstCuentasAG.iterator();
                    while (it2.hasNext()) {
                        PoliCtas polDeta = it2.next();
                        if (polDeta.getStrCuenta().equals(strPR_CTA_VENTA)) {
                            bolExiste = true;
                            //Ya existe la cuenta solo sumamos el importe
                            polDeta.setDblImporte(polDeta.getDblImporte() + deta.getFieldDouble(this.strPrefijoDeta + "_IMPORTE"));
                        }
                    }
                    //Si no existe lo agregamos
                    if (!bolExiste) {
                        PoliCtas pol = new PoliCtas();
                        pol.setStrCuenta(strPR_CTA_VENTA);
                        pol.setBolEsCargo(bolEsCargo);
                        pol.setDblImporte(deta.getFieldDouble(this.strPrefijoDeta + "_IMPORTE"));
                        pol.setStrFolioRef(strFolio);
                        lstCuentasAG.add(pol);
                    }
                }
                //if(rs3.getStatement() != null )rs3.getStatement().close(); 
                rs3.close();
            } catch (SQLException ex) {
                log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
        }
        //Vaciamos el listado de cuentas en las polizas contables
        poli.setLstCuentasAG(lstCuentasAG);
    }

    /**
     * Envia aviso de que se cancelo la operacion al cliente
     */
    protected void EnvioMailCancel() {
        //Buscamos mail del cliente
        String strCT_EMAIL1 = "";
        String strCT_EMAIL2 = "";
        String strSql = "SELECT CT_EMAIL1,CT_EMAIL2 FROM vta_cliente "
                + " where CT_ID=" + this.document.getFieldInt("CT_ID") + "";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strCT_EMAIL1 = rs.getString("CT_EMAIL1");
                strCT_EMAIL2 = rs.getString("CT_EMAIL2");
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
        }
        //validamos que hallan puesto el mail
        Mail mail = new Mail();
        if (!strCT_EMAIL1.isEmpty() || !strCT_EMAIL2.isEmpty()) {
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCT_EMAIL1)) {
                strLstMail += "," + strCT_EMAIL1;
            }
            if (mail.isEmail(strCT_EMAIL2)) {
                strLstMail += "," + strCT_EMAIL2;
            }
            //Intentamos mandar el mail
            mail.setBolDepuracion(false);
            mail.getTemplate("CANCEL_FAC", oConn);
            mail.getMensaje();
            mail.setReplaceContent(this.getDocument());
            strSql = "SELECT * FROM vta_empresas "
                    + " where EMP_ID=" + this.document.getFieldInt("EMP_ID") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql);
                mail.setReplaceContent(rs);
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            mail.setDestino(strLstMail);
            boolean bol = mail.sendMail();
            if (bol) {
                //strResp = "MAIL ENVIADO.";
            } else {
                //strResp = "FALLO EL ENVIO DEL MAIL.";
            }

        } else {
            //strResp = "ERROR: INGRESE UN MAIL";
        }
    }

    /**
     * Avisa si quedan pocos folios
     *
     * @param intFE_ID Es el id de los folios
     * @param intEMP_AVISOFOLIO Es el numero de folios a partir del cual se van
     * a enviar notificaciones
     * @param intFE_FOLIOFIN Es el numero de folios totales solicitados
     * @param intFolio Es el folio actual
     */
    protected void AvisoFolios(int intFE_ID, int intEMP_AVISOFOLIO, int intFE_FOLIOFIN, int intFolio) {
        this.intFoliosRestantes = (intFE_FOLIOFIN - intFolio);
        //Validamos si quedan pocos folios para terminarlos
        if ((intFE_FOLIOFIN - intFolio) <= intEMP_AVISOFOLIO) {
            this.bolQuedanPocosFolios = true;
            //Enviamos mail de aviso si procede
            if (this.getStrTipoVta().equals(Ticket.FACTURA)) {
                if (this.bolAvisoFolios) {
                    //Validamos si hay alguna nueva serie de folios para ya no enviar el aviso
                    String strSql = "SELECT * FROM vta_foliosempresa WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID")
                            + " AND FE_ID  <>  " + intFE_ID + " AND FE_FOLIOINI > " + intFolio + " AND FE_ESNC = 0 ";
                    try {
                        boolean bolFindFolio = false;
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            bolFindFolio = true;
                        }
                        //if(rs.getStatement() != null )rs.getStatement().close(); 
                        rs.close();
                        if (!bolFindFolio) {
                            EnvioMailAvisoFolios();
                        }
                    } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        ex.fillInStackTrace();
                    }

                }
            }
        }
    }

    /**
     * Envia aviso de que se cancelo la operacion al cliente
     */
    protected void EnvioMailAvisoFolios() {
        Mail mail = new Mail();
        //Obtenemos los usuarios a los que mandaremos el mail
        String strLstMail = "";
        String strSql = "select * from usuarios where BOL_MAIL_FACT = 1";
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
            log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        }
        //Si hay mails para notificar
        if (!strLstMail.isEmpty()) {
            //Intentamos mandar el mail
            mail.setBolDepuracion(false);
            mail.getTemplate("AVISOFOLIO", oConn);
            mail.getMensaje();
            mail.setReplaceContent(this.getDocument());
            strSql = "SELECT * FROM vta_empresas "
                    + " where EMP_ID=" + this.document.getFieldInt("EMP_ID") + "";
            try {
                ResultSet rs = oConn.runQuery(strSql);
                mail.setReplaceContent(rs);
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                this.strResultLast = "ERROR:" + ex.getMessage();
                ex.fillInStackTrace();
            }
            mail.setDestino(strLstMail);
            boolean bol = mail.sendMail();
            if (bol) {
                //strResp = "MAIL ENVIADO.";
            } else {
                //strResp = "FALLO EL ENVIO DEL MAIL.";
            }

        } else {
            //strResp = "ERROR: INGRESE UN MAIL";
        }
    }

    /**
     * Obtiene el password para encriptar
     *
     * @param context Es el contexto del servlet
     */
    public void initMyPass(ServletContext context) {
        try {
            String strPassB64 = context.getInitParameter("SecretWord");
            Opalina opa = new Opalina();
            strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        } catch (NoSuchPaddingException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        } catch (InvalidKeyException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        } catch (IllegalBlockSizeException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        } catch (BadPaddingException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
        }
    }

    /**
     * Registra el surtido de un pedido afectando inventarios y el estatus del
     * pedido
     *
     * @param strFechaSurtido Es la fecha de surtido
     * @param strNotas Son las notas
     * @param strAduana Es la aduana
     * @param strNumLote Es el numero de lote
     * @param lstContenido Es el contenido de la caja, contiene la lista de
     * productos surtidos en cada una
     * @param lstCajas Contiene el listado con la informacion de cada caja
     */
    public void doSurtidoPedido(String strFechaSurtido, String strNotas, String strAduana, String strNumLote,
            ArrayList<vta_pedidos_cajas> lstContenido,
            ArrayList<vta_pedidos_cajas_master> lstCajas) {
        this.strResultLast = "OK";
        // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
        if (this.getDocument().getFieldInt("PD_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir el pedido por surtir";
        }
        //Validamos que hallan registrado entradas
        boolean bolSurtio = false;
        Iterator<TableMaster> it = this.lstMovs.iterator();
        while (it.hasNext()) {
            TableMaster tbn = it.next();
            if (tbn.getFieldDouble("PDD_CANTIDADSURTIDA") > 0) {
                bolSurtio = true;
            }
        }
        //si no surtio nada marcamos error
        if (!bolSurtio) {
            this.strResultLast = "ERROR:Necesita surtir mercancia";
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
        if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
        }
        // </editor-fold>

        //Validamos que no este anulado
        if (this.document.getFieldInt("PD_ANULADA") == 0) {
            //Realizamos la entrada al almacen
            Inventario inv = new Inventario(oConn, varSesiones, request);
            //Inicializamos
            inv.Init();
            //Definimos valores
            inv.setBolTransaccionalidad(false);
            inv.setIntTipoOperacion(Inventario.SALIDA);
            inv.setIntTipoCosteo(this.intSistemaCostos);//SistemaCostos
            inv.getMovProd().setFieldInt("TIN_ID", Inventario.SALIDA);
            //Asignamos los valores master
            inv.getMovProd().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
            inv.getMovProd().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
            inv.getMovProd().setFieldInt("PD_ID", this.document.getFieldInt("PD_ID"));
            inv.getMovProd().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
            inv.getMovProd().setFieldInt("MON_ID", this.document.getFieldInt("PD_MONEDA"));
            inv.getMovProd().setFieldString("MP_FECHA", strFechaSurtido);
            inv.getMovProd().setFieldString("MP_NOTAS", strNotas);
            inv.getMovProd().setFieldString("MP_ORIGENLOTE", strAduana);
            //Asignamos los valores de las partidas
            it = this.lstMovs.iterator();
            while (it.hasNext()) {
                TableMaster deta = it.next();
                if (deta.getFieldDouble("PDD_CANTIDADSURTIDA") > 0) {
                    //Validamos si maneja numeros de serie
                    if (deta.getFieldString("PDD_USASERIE").equals("1")) {
                        String[] lstSeriesI = deta.getFieldString("PDD_SERIES").split(",");
                        //Recorremos cada numero de serie para ingresarlo
                        for (int y = 0; y < lstSeriesI.length; y++) {
                            //Copiamos valores del ticket
                            vta_movproddeta detaInv = new vta_movproddeta();
                            detaInv.setBolUsaSeries(true);
                            detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                            detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                            detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("PDD_ID"));
                            detaInv.setFieldDouble("MPD_SALIDAS", 1);
                            detaInv.setFieldDouble("MPD_COSTO", deta.getFieldDouble("PDD_COSTO"));
                            detaInv.setFieldString("PR_CODIGO", deta.getFieldString("PDD_CVE"));
                            detaInv.setFieldString("PL_NUMLOTE", lstSeriesI[y]);
                            detaInv.setFieldString("MPD_FECHA", strFechaSurtido);
                            inv.AddDetalle(detaInv);

                        }
                    } else {
                        //Copiamos valores del ticket
                        vta_movproddeta detaInv = new vta_movproddeta();
                        detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                        detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("PDD_ID"));
                        detaInv.setFieldDouble("MPD_SALIDAS", deta.getFieldDouble("PDD_CANTIDADSURTIDA"));
                        detaInv.setFieldDouble("MPD_COSTO", deta.getFieldDouble("PDD_COSTO"));
                        detaInv.setFieldString("PR_CODIGO", deta.getFieldString("PDD_CVE"));
                        detaInv.setFieldString("PL_NUMLOTE", strNumLote);
                        detaInv.setFieldString("MPD_FECHA", strFechaSurtido);
                        inv.AddDetalle(detaInv);
                    }

                }
            }
            //Generamos la operacion de inventarios
            inv.doTrx();
            if (!inv.getStrResultLast().equals("OK")) {
                //Fallo algo al guardar el movimiento
                this.strResultLast = inv.getStrResultLast();
            } else {

                // <editor-fold defaultstate="collapsed" desc="Actualizamos las cantidades surtidas por cada item">
                it = this.lstMovs.iterator();
                while (it.hasNext()) {
                    TableMaster deta = it.next();
                    //Obtenemos la cantidad y el id
                    double dblRecibida = deta.getFieldDouble("PDD_CANTIDADSURTIDA");
                    int intIdDeta = deta.getFieldInt("PDD_ID");
                    //Recuperamos objeto
                    vta_pedidosdeta detaTmp = new vta_pedidosdeta();
                    detaTmp.ObtenDatos(intIdDeta, oConn);
                    //Actualizamos
                    detaTmp.setFieldDouble("PDD_CANTIDADSURTIDA", dblRecibida + detaTmp.getFieldDouble("PDD_CANTIDADSURTIDA"));
                    detaTmp.Modifica(oConn);
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Actualizamos la bandera de surtido del pedido">
                vta_pedidosdeta detaTmp2 = new vta_pedidosdeta();
                ArrayList<TableMaster> lstDetalle = detaTmp2.ObtenDatosVarios(" PD_ID = " + this.document.getFieldInt("PD_ID"), oConn);
                Iterator<TableMaster> it2 = lstDetalle.iterator();
                double dblSumFaltantes = 0;
                while (it2.hasNext()) {
                    TableMaster tbn = it2.next();
                    double dblFaltante = tbn.getFieldDouble("PDD_CANTIDAD") - tbn.getFieldDouble("PDD_CANTIDADSURTIDA");
                    if (dblFaltante < 0) {
                        dblFaltante = 0;
                    }
                    dblSumFaltantes += dblFaltante;
                }
                //Si no hay faltantes marcamos la orden de compra como surtida
                if (dblSumFaltantes == 0) {
                    this.document.setFieldInt("PD_ES_SURTIDO", 1);
                    this.document.Modifica(oConn);
                    //Marcamos el estatus como el 3:SURTIDO
                    this.document.setFieldInt("PD_STATUS", 2);
                } else {
                    //Marcamos el estatus como el 3:SURTIDO PARCIALMENTE
                    this.document.setFieldInt("PD_STATUS", 3);
                }
                // </editor-fold>

                String strResp1 = this.document.Modifica(oConn);

                // <editor-fold defaultstate="collapsed" desc="Guardamos las cajas">
                if (lstContenido != null) {
                    //Contenido
                    Iterator<vta_pedidos_cajas> it3 = lstContenido.iterator();
                    while (it3.hasNext()) {
                        vta_pedidos_cajas contenido = it3.next();
                        contenido.setFieldInt("MP_ID", Integer.valueOf(inv.getMovProd().getValorKey()));
                        contenido.Agrega(oConn);
                    }
                    //Cajas
                    Iterator<vta_pedidos_cajas_master> it4 = lstCajas.iterator();
                    while (it4.hasNext()) {
                        vta_pedidos_cajas_master cajas = it4.next();
                        cajas.setFieldInt("MP_ID", Integer.valueOf(inv.getMovProd().getValorKey()));
                        cajas.Agrega(oConn);
                    }
                }
                // </editor-fold>
                if (!strResp1.equals("OK")) {
                    this.strResultLast = strResp1;
                } else {
                    //Enviamos el id de la salida para poderlo imprimir
                    this.strResultLast = "OK." + inv.getMovProd().getValorKey();
                    // <editor-fold defaultstate="collapsed" desc="Bitacora">
                    this.saveBitacora("PEDIDOS", "SURTIDO", this.document.getFieldInt("PD_ID"));
                    // </editor-fold>
                }

            }

        } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
        }

        // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
        if (this.bolTransaccionalidad) {
            if (this.strResultLast.startsWith("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }
        }
        // </editor-fold>
    }

    /**
     * Obtiene la lista de pedidos que estan generando la factura o remisión
     *
     * @param strLstPedidos Es una lista de pedidos
     */
    public void getListPedidos(String strLstPedidos) {
        String[] lstPedidos = strLstPedidos.split(",");
        this.lstPedidosOrigen = new int[lstPedidos.length];
        for (int j = 0; j < lstPedidos.length; j++) {
            try {
                this.lstPedidosOrigen[j] = Integer.valueOf(lstPedidos[j]);
            } catch (NumberFormatException ex) {
                log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
        }
    }

    /**
     * Actualiza el estatus de los numeros de serie de un pedido remisionado o
     * facturado
     *
     * @param bolPrende Indica con true que esta factura y con false lo
     * contrario
     * @param intNumFactura Es el id de factura
     */
    public void ActualizaStatusSeries(boolean bolPrende, int intNumFactura) {
        if (this.lstSeries != null) {
            log.debug("Actualizamos el estatus de las series....");
            Iterator<vta_movproddeta> it = this.lstSeries.iterator();
            while (it.hasNext()) {
                vta_movproddeta objMov = it.next();
                if (bolPrende) {
                    objMov.setFieldInt("MPD_SERIE_VENDIDO", 1);
                    objMov.setFieldInt("MPD_SERIE_FACTURA", intNumFactura);
                } else {
                    objMov.setFieldInt("MPD_SERIE_VENDIDO", 0);
                    objMov.setFieldInt("MPD_SERIE_FACTURA", 0);
                }
                objMov.Modifica(oConn);
            }
        }
    }

    /**
     * Registra el surtido de un pedido afectando inventarios y el estatus del
     * pedido
     *
     * @param strFechaSurtido Es la fecha de surtido
     * @param strNotas Son las notas
     * @param strAduana Es la aduana
     * @param strNumLote Es el numero de lote
     */
    public void doRecepcionPedidoConsignacion(String strFechaSurtido, String strNotas, String strAduana, String strNumLote) {
        this.strResultLast = "OK";
        // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
        if (this.getDocument().getFieldInt("PD_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir el pedido por surtir";
        }
        //Validamos que hallan registrado entradas
        boolean bolSurtio = false;
        Iterator<TableMaster> it = this.lstMovs.iterator();
        while (it.hasNext()) {
            TableMaster tbn = it.next();
            if (tbn.getFieldDouble("PDD_CANTIDADSURTIDA") > 0) {
                bolSurtio = true;
            }
        }
        //si no surtio nada marcamos error
        if (!bolSurtio) {
            this.strResultLast = "ERROR:Necesita surtir mercancia";
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
        if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
        }
        // </editor-fold>

        //Validamos que no este anulado
        if (this.document.getFieldInt("PD_ANULADA") == 0) {
            //Realizamos la entrada al almacen
            Inventario inv = new Inventario(oConn, varSesiones, request);
            //Inicializamos
            inv.Init();
            //Definimos valores
            inv.setBolTransaccionalidad(false);
            inv.setIntTipoOperacion(Inventario.ENTRADA);
            inv.setIntTipoCosteo(this.intSistemaCostos);//SistemaCostos
            inv.getMovProd().setFieldInt("TIN_ID", Inventario.ENTRADA);
            //Asignamos los valores master
            inv.getMovProd().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
            inv.getMovProd().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
            inv.getMovProd().setFieldInt("PD_ID", this.document.getFieldInt("PD_ID"));
            inv.getMovProd().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
            inv.getMovProd().setFieldInt("MON_ID", this.document.getFieldInt("PD_MONEDA"));
            inv.getMovProd().setFieldString("MP_FECHA", strFechaSurtido);
            inv.getMovProd().setFieldString("MP_NOTAS", strNotas);
            inv.getMovProd().setFieldString("MP_ORIGENLOTE", strAduana);
            //Asignamos los valores de las partidas
            it = this.lstMovs.iterator();
            while (it.hasNext()) {
                TableMaster deta = it.next();
                if (deta.getFieldDouble("PDD_CANTIDADSURTIDA") > 0) {
                    //Validamos si maneja numeros de serie
                    if (deta.getFieldString("PDD_USASERIE").equals("1")) {
                        String[] lstSeriesI = deta.getFieldString("PDD_SERIES").split(",");
                        //Recorremos cada numero de serie para ingresarlo
                        for (int y = 0; y < lstSeriesI.length; y++) {
                            //Copiamos valores del ticket
                            vta_movproddeta detaInv = new vta_movproddeta();
                            detaInv.setBolUsaSeries(true);
                            detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                            detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                            detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("PDD_ID"));
                            detaInv.setFieldDouble("MPD_ENTRADAS", 1);
                            detaInv.setFieldDouble("MPD_COSTO", deta.getFieldDouble("PDD_COSTO"));
                            detaInv.setFieldString("PR_CODIGO", deta.getFieldString("PDD_CVE"));
                            detaInv.setFieldString("PL_NUMLOTE", lstSeriesI[y]);
                            detaInv.setFieldString("MPD_FECHA", strFechaSurtido);
                            inv.AddDetalle(detaInv);

                        }
                    } else {
                        //Copiamos valores del ticket
                        vta_movproddeta detaInv = new vta_movproddeta();
                        detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                        detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("PDD_ID"));
                        detaInv.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble("PDD_CANTIDADSURTIDA"));
                        detaInv.setFieldDouble("MPD_COSTO", deta.getFieldDouble("PDD_COSTO"));
                        detaInv.setFieldString("PR_CODIGO", deta.getFieldString("PDD_CVE"));
                        detaInv.setFieldString("PL_NUMLOTE", strNumLote);
                        detaInv.setFieldString("MPD_FECHA", strFechaSurtido);
                        inv.AddDetalle(detaInv);
                    }

                }
            }
            //Generamos la operacion de inventarios
            inv.doTrx();
            if (!inv.getStrResultLast().equals("OK")) {
                //Fallo algo al guardar el movimiento
                this.strResultLast = inv.getStrResultLast();
            } else {

                //Actualizamos las cantidades surtidas por cada item
                it = this.lstMovs.iterator();
                while (it.hasNext()) {
                    TableMaster deta = it.next();
                    //Obtenemos la cantidad y el id
                    double dblRecibida = deta.getFieldDouble("PDD_CANTIDADSURTIDA");
                    int intIdDeta = deta.getFieldInt("PDD_ID");
                    //Recuperamos objeto
                    vta_pedidosdeta detaTmp = new vta_pedidosdeta();
                    detaTmp.ObtenDatos(intIdDeta, oConn);
                    //Actualizamos
                    detaTmp.setFieldDouble("PDD_CANTIDADSURTIDA", dblRecibida - detaTmp.getFieldDouble("PDD_CANTIDADSURTIDA"));
                    detaTmp.Modifica(oConn);
                }

                //Actualizamos la bandera de surtido del pedido
                vta_pedidosdeta detaTmp2 = new vta_pedidosdeta();
                ArrayList<TableMaster> lstDetalle = detaTmp2.ObtenDatosVarios(" PD_ID = " + this.document.getFieldInt("PD_ID"), oConn);
                Iterator<TableMaster> it2 = lstDetalle.iterator();
                double dblSumFaltantes = 0;
                while (it2.hasNext()) {
                    TableMaster tbn = it2.next();
                    double dblFaltante = tbn.getFieldDouble("PDD_CANTIDAD") - tbn.getFieldDouble("PDD_CANTIDADSURTIDA");
                    if (dblFaltante < 0) {
                        dblFaltante = 0;
                    }
                    dblSumFaltantes += dblFaltante;
                }
                //Si no hay faltantes marcamos la orden de compra como surtida
                if (dblSumFaltantes == 0) {
                    this.document.setFieldInt("PD_ES_SURTIDO", 1);
                    this.document.Modifica(oConn);
                    //Marcamos el estatus como el 3:SURTIDO
                    this.document.setFieldInt("PD_STATUS", 2);
                } else {
                    //Marcamos el estatus como el 3:SURTIDO PARCIALMENTE
                    this.document.setFieldInt("PD_STATUS", 3);
                }
                String strResp1 = this.document.Modifica(oConn);

                if (!strResp1.equals("OK")) {
                    this.strResultLast = strResp1;
                } else {
                    //Enviamos el id de la salida para poderlo imprimir
                    this.strResultLast = "OK." + inv.getMovProd().getValorKey();
                    // <editor-fold defaultstate="collapsed" desc="Bitacora">
                    this.saveBitacora("PEDIDOS", "SURTIDO", this.document.getFieldInt("PD_ID"));
                    // </editor-fold>
                }

            }

        } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
        }

        // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
        if (this.bolTransaccionalidad) {
            if (this.strResultLast.startsWith("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }
        }
        // </editor-fold>
    }

    /**
     * Realiza la cancelacion de un surtido
     *
     * @param intMP_ID
     */
    public void doSurtidoPedidoCancel(int intMP_ID) {
        this.strResultLast = "OK";
        // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
        if (intMP_ID == 0) {
            this.strResultLast = "ERROR:Necesita seleccionar un movimiento de almacen";
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
        if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
        }
        // </editor-fold>

        //Realizamos la cancelacion del movimiento en almacen
        Inventario inv = new Inventario(oConn, varSesiones, request);
        inv.getMovProd().setFieldInt("MP_ID", intMP_ID);
        //Inicializamos
        inv.Init();
        //Definimos valores
        inv.setBolTransaccionalidad(false);
        inv.doTrxAnul();
        if (!inv.getStrResultLast().equals("OK")) {
            //Fallo algo al guardar el movimiento
            this.strResultLast = inv.getStrResultLast();
        } else {
            //Recuperamos las partidas del movimiento de almacen
            vta_movproddeta movdeta = new vta_movproddeta();
            ArrayList<TableMaster> lstMovsInv = movdeta.ObtenDatosVarios(" MP_ID = " + intMP_ID, oConn);
            //Recuperamos el id del pedido
            this.document.setFieldInt(this.strPrefijoMaster + "_ID", inv.getMovProd().getFieldInt("PD_ID"));
            if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") != 0) {
                this.document.ObtenDatos(this.document.getFieldInt(this.strPrefijoMaster + "_ID"), oConn);
                //Recuperamos las partidas
                vta_pedidosdeta detaTmp = new vta_pedidosdeta();
                this.lstMovs = detaTmp.ObtenDatosVarios(" PD_ID = " + inv.getMovProd().getFieldInt("PD_ID"), oConn);
            }
            // <editor-fold defaultstate="collapsed" desc="Actualizamos las cantidades surtidas por cada item">
            Iterator<TableMaster> it = this.lstMovs.iterator();
            while (it.hasNext()) {
                TableMaster deta = it.next();
                //Obtenemos la cantidad y el id
                int intIdDeta = deta.getFieldInt("PDD_ID");
                //Recuperamos objeto
                vta_pedidosdeta detaTmp = new vta_pedidosdeta();
                detaTmp.ObtenDatos(intIdDeta, oConn);
                Iterator<TableMaster> itI = lstMovsInv.iterator();
                while (itI.hasNext()) {
                    TableMaster detaInv = itI.next();
                    if (detaInv.getFieldInt("MPD_IDORIGEN") == deta.getFieldInt("PDD_ID")) {
                        //Actualizamos
                        detaTmp.setFieldDouble("PDD_CANTIDADSURTIDA", detaTmp.getFieldDouble("PDD_CANTIDADSURTIDA") - detaInv.getFieldDouble("MPD_SALIDAS"));
                        detaTmp.Modifica(oConn);
                    }
                }

            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Actualizamos la bandera de surtido del pedido">
            vta_pedidosdeta detaTmp2 = new vta_pedidosdeta();
            ArrayList<TableMaster> lstDetalle = detaTmp2.ObtenDatosVarios(" PD_ID = " + this.document.getFieldInt("PD_ID"), oConn);
            Iterator<TableMaster> it2 = lstDetalle.iterator();
            double dblSumFaltantes = 0;
            double dblSumPedidos = 0;
            while (it2.hasNext()) {
                TableMaster tbn = it2.next();
                double dblFaltante = tbn.getFieldDouble("PDD_CANTIDAD") - tbn.getFieldDouble("PDD_CANTIDADSURTIDA");
                if (dblFaltante < 0) {
                    dblFaltante = 0;
                }
                dblSumFaltantes += dblFaltante;
                dblSumPedidos += tbn.getFieldDouble("PDD_CANTIDAD");
            }
            //Si no hay faltantes marcamos la orden de compra como surtida
            if (dblSumFaltantes == dblSumPedidos) {
                this.document.setFieldInt("PD_ES_SURTIDO", 0);
                //Marcamos el estatus como el 3:SURTIDO
                this.document.setFieldInt("PD_STATUS", 8);
            } else {
                //Marcamos el estatus como el 3:SURTIDO PARCIALMENTE
                this.document.setFieldInt("PD_STATUS", 3);
            }
            // </editor-fold>

            String strResp1 = this.document.Modifica(oConn);

            if (!strResp1.equals("OK")) {
                this.strResultLast = strResp1;
            } else {
                //Enviamos el id de la salida para poderlo imprimir
                this.strResultLast = "OK." + inv.getMovProd().getValorKey();
                // <editor-fold defaultstate="collapsed" desc="Bitacora">
                this.saveBitacora("PEDIDOS", "SURTIDO", this.document.getFieldInt("PD_ID"));
                // </editor-fold>
            }

        }

        // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
        if (this.bolTransaccionalidad) {
            if (this.strResultLast.startsWith("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }
        }
        // </editor-fold>
    }
    //Funciones para envio por mail de tickets, pedidos

    /**
     * Funciones
     */
    /**
     * Envia el mail al cliente
     *
     * @param oConn
     * @param inIdTrx Es el id de la operacion por enviar mail
     * @param strTipoDoc Es el tipo de documento
     * @param strPathXML
     * @param strPathFonts
     * @return Regresa OK si fue exitoso el envio del mail
     */
    public String generaMail(Conexion oConn, int inIdTrx, String strTipoDoc, String strPathXML, String strPathFonts, String strCorreo) {
        String strResp = "OK";
        //Nombre de archivo
        String strFolio = "";
        int intIdCliente = 0;
        //Obtenemos datos del smtp
        String strsmtp_server = "";
        String strsmtp_user = "";
        String strsmtp_pass = "";
        String strsmtp_port = "";
        String strsmtp_usaTLS = "";
        String strsmtp_usaSTLS = "";

        // <editor-fold defaultstate="collapsed" desc="Buscamos los datos del SMTP">
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
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
            //Buscamos el folio
            String strNomFolio = "";
            if (strTipoDoc.equals("COTIZA")) {
                strSql = "select COT_FOLIO,CT_ID from vta_cotiza where COT_ID = " + inIdTrx;
                strNomFolio = "COT_FOLIO";
            }
            if (strTipoDoc.equals("PEDIDO")) {
                strSql = "select PD_FOLIO,CT_ID from vta_pedidos where PD_ID = " + inIdTrx;
                strNomFolio = "PD_FOLIO";
            }
            if (strTipoDoc.equals("TICKET")) {
                strSql = "select TKT_FOLIO,CT_ID from vta_tickets where TKT_ID = " + inIdTrx;
                strNomFolio = "TKT_FOLIO";
            }
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strFolio = rs.getString(strNomFolio);
                intIdCliente = rs.getInt("CT_ID");
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Obtenemos los textos para el envio del mail">
        String strNomTemplate = "";
        String strNomFormato = "";
        if (strTipoDoc.equals("COTIZA")) {
            strNomTemplate = "COTIZA_MAI";
            strNomFormato = "COTIZA";
        }
        if (strTipoDoc.equals("PEDIDO")) {
            strNomTemplate = "PEDIDO_MAI";
            strNomFormato = "PEDIDO";
        }
        if (strTipoDoc.equals("TICKET")) {
            strNomTemplate = "TICKET_MAI";
            strNomFormato = "TICKET";
        }
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        // </editor-fold>

        //generamos el formato de impresión
        String strRespPdf = GeneraImpresionPDF(oConn, strPathXML,
                strNomFormato, strFolio, inIdTrx,
                strPathFonts);
        if (strRespPdf.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Si estan llenos todos los datos mandamos el mail">
            if (!strsmtp_server.equals("")
                    && !strsmtp_user.equals("")
                    && !strsmtp_pass.equals("")) {
                //armamos el mail
                Mail mail = new Mail();
                //Activamos envio de acuse de recibo
                mail.setBolAcuseRecibo(true);
                //Obtenemos los usuarios a los que mandaremos el mail
                String strLstMail = "";
                String strMailCte = strCorreo;
//            String strMailCte = getMail(intIdCliente, oConn);
                //Validamos si el mail del cliente es valido
                if (mail.isEmail(strMailCte)) {
                    strLstMail += strMailCte;
                }

                // <editor-fold defaultstate="collapsed" desc="Mandamos mail si hay usuarios">
                if (!strLstMail.equals("")) {
                    String strMsgMail = lstMail[1];
                    strMsgMail = strMsgMail.replace("%folio%", strFolio);
                    //Establecemos parametros
                    mail.setUsuario(strsmtp_user);
                    mail.setContrasenia(strsmtp_pass);
                    mail.setHost(strsmtp_server);
                    mail.setPuerto(strsmtp_port);
                    mail.setAsunto(lstMail[0].replace("%folio%", strFolio));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    //Adjuntamos XML y PDF
                    mail.setFichero(strPathXML + strNomFormato + strFolio + ".pdf");

                    if (strsmtp_usaTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strsmtp_usaSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        System.out.println("se envio el correo");
                    }
                }
                // </editor-fold>
            }
            // </editor-fold>
        }

        return strResp;
    }

    public String getMailProv(Conexion oConn, VariableSession varSesiones, int intNvoKey, String strTipoVta) {
        String strRes = "";
        String strEmail1 = "";
        String strEmail2 = "";
        String strIdProveedor = "";

        String strSqlUsuarios = "";
        if (strTipoVta.equals("1")) {
            strSqlUsuarios = "SELECT vta_proveedor.PV_ID,vta_proveedor.PV_RAZONSOCIAL,vta_proveedor.PV_EMAIL1,vta_proveedor.PV_EMAIL2 "
                    + " FROM vta_facturasdeta INNER JOIN vta_producto ON vta_facturasdeta.PR_ID = vta_producto.PR_ID "
                    + " INNER JOIN vta_proveedor ON vta_producto.PV_ID = vta_proveedor.PV_ID "
                    + " WHERE FAC_ID =" + intNvoKey + " and vta_proveedor.PV_BOL_MAIL = 1 "
                    + " group by vta_proveedor.PV_ID,vta_proveedor.PV_RAZONSOCIAL,vta_proveedor.PV_EMAIL1,vta_proveedor.PV_EMAIL2; ";
        }
        if (strTipoVta.equals("2")) {
            strSqlUsuarios = "SELECT vta_proveedor.PV_ID,vta_proveedor.PV_RAZONSOCIAL,vta_proveedor.PV_EMAIL1,vta_proveedor.PV_EMAIL2 "
                    + " FROM vta_ticketsdeta INNER JOIN vta_producto ON vta_ticketsdeta.PR_ID = vta_producto.PR_ID "
                    + " INNER JOIN vta_proveedor ON vta_producto.PV_ID = vta_proveedor.PV_ID "
                    + " WHERE TKT_ID =" + intNvoKey + " and vta_proveedor.PV_BOL_MAIL = 1 "
                    + " group by vta_proveedor.PV_ID,vta_proveedor.PV_RAZONSOCIAL,vta_proveedor.PV_EMAIL1,vta_proveedor.PV_EMAIL2; ";
        }
        try {
            ResultSet rs = oConn.runQuery(strSqlUsuarios);
            while (rs.next()) {
                strEmail1 = rs.getString("PV_EMAIL1");
                strEmail2 = rs.getString("PV_EMAIL2");
                strIdProveedor = rs.getString("PV_ID");

                Mail mail = new Mail();
                if (!strEmail1.isEmpty() || !strEmail2.isEmpty()) {
                    String strLstMail = "";
                    //Validamos si el mail del cliente es valido
                    if (mail.isEmail(strEmail1)) {
                        strLstMail += "," + strEmail1;
                    }
                    if (mail.isEmail(strEmail2)) {
                        strLstMail += "," + strEmail2;
                    }

                    //Intentamos mandar el mail
                    mail.setBolDepuracion(true);
                    mail.getTemplate("MSG_PEDIDO_PROV", oConn);
                    String strMensaje = mail.getMensaje();
                    //Query para la table de lo que se vendio
                    StringBuilder strTablaHtml = new StringBuilder("<table border=\"0\" cellpadding=\"4\" cellspacing=\"4\">");
                    strTablaHtml.append(
                            "<tr>"
                            + "<td>C&oacute;digo</td>"
                            + "<td>Descripci&oacute;n</td>"
                            + "<td>Cantidad</td>"
                            + "</tr>");

                    String strSqlEmp = "";

                    if (strTipoVta.equals("1")) {
                        strSqlEmp = " SELECT vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION,vta_facturasdeta.FACD_CANTIDAD as Cantidad "
                                + " FROM vta_facturasdeta INNER JOIN vta_producto ON vta_facturasdeta.PR_ID = vta_producto.PR_ID "
                                + " INNER JOIN vta_proveedor ON vta_producto.PV_ID = vta_proveedor.PV_ID  "
                                + " WHERE FAC_ID = " + intNvoKey + " and vta_proveedor.PV_ID =" + strIdProveedor
                                + " group by  vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION,vta_facturasdeta.FACD_CANTIDAD;";
                    }

                    if (strTipoVta.equals("2")) {
                        strSqlEmp = " SELECT vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION,vta_ticketsdeta.TKTD_CANTIDAD as Cantidad "
                                + " FROM vta_ticketsdeta INNER JOIN vta_producto ON vta_ticketsdeta.PR_ID = vta_producto.PR_ID "
                                + " INNER JOIN vta_proveedor ON vta_producto.PV_ID = vta_proveedor.PV_ID "
                                + " WHERE TKT_ID = " + intNvoKey + " and vta_proveedor.PV_ID =" + strIdProveedor
                                + " group by vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION,vta_ticketsdeta.TKTD_CANTIDAD;";
                    }

                    try {
                        ResultSet rs1 = oConn.runQuery(strSqlEmp);
                        while (rs1.next()) {
                            strTablaHtml.append("<tr>" + "<td>").
                                    append(rs1.getDouble("PR_CODIGO")).
                                    append("</td>" + "<td>").
                                    append(rs1.getString("PR_DESCRIPCION")).
                                    append("</td>" + "<td>").
                                    append(rs1.getString("Cantidad")).
                                    append("</td>"
                                            + "</tr>");
                        }
//                  if(rs1.getStatement() != null )rs1.getStatement().close(); 
                        rs1.close();
                        strTablaHtml.append("</table>");
                    } catch (SQLException ex) {
                        log.error(ex.getMessage());
                    }
                    strMensaje = strMensaje.replace("%TABLA_PRODUCTO%", strTablaHtml.toString());
                    mail.setMensaje(strMensaje);

                    if (strTipoVta.equals("1")) {
                        strSqlEmp = " SELECT vta_facturas.FAC_FOLIO as FOLIO, vta_facturas.FAC_FECHA as FECHA, vta_proveedor.PV_RFC,vta_proveedor.PV_RAZONSOCIAL, vta_cliente.CT_RAZONSOCIAL, vta_cliente.CT_RFC,  "
                                + " (select SUM(FACD_CANTIDAD) from vta_facturasdeta where vta_facturasdeta.FAC_ID = vta_facturas.FAC_ID) as cantidad  "
                                + " FROM vta_facturas INNER JOIN vta_facturasdeta ON vta_facturas.FAC_ID = vta_facturasdeta.FAC_ID  "
                                + " INNER JOIN vta_cliente ON vta_facturas.CT_ID = vta_cliente.CT_ID  "
                                + " INNER JOIN vta_producto ON vta_facturasdeta.PR_ID = vta_producto.PR_ID  "
                                + " INNER JOIN vta_proveedor ON vta_producto.PV_ID = vta_proveedor.PV_ID    "
                                + " WHERE vta_facturas.FAC_ID = " + intNvoKey
                                + " group by vta_facturas.FAC_FOLIO , vta_facturas.FAC_FECHA , vta_proveedor.PV_RFC,vta_proveedor.PV_RAZONSOCIAL, vta_cliente.CT_RAZONSOCIAL, vta_cliente.CT_RFC; ";

                    }

                    if (strTipoVta.equals("2")) {
                        strSqlEmp = " SELECT vta_tickets.TKT_FOLIO as FOLIO, vta_tickets.TKT_FECHA as FECHA, vta_proveedor.PV_RFC,vta_proveedor.PV_RAZONSOCIAL, vta_cliente.CT_RAZONSOCIAL, vta_cliente.CT_RFC, "
                                + " (select SUM(TKTD_CANTIDAD) from vta_ticketsdeta where vta_ticketsdeta.TKT_ID = vta_tickets.TKT_ID) as cantidad "
                                + " FROM vta_tickets INNER JOIN vta_ticketsdeta ON vta_tickets.TKT_ID = vta_ticketsdeta.TKT_ID "
                                + " INNER JOIN vta_cliente ON vta_tickets.CT_ID = vta_cliente.CT_ID "
                                + " INNER JOIN vta_producto ON vta_ticketsdeta.PR_ID = vta_producto.PR_ID "
                                + " INNER JOIN vta_proveedor ON vta_producto.PV_ID = vta_proveedor.PV_ID  "
                                + " WHERE vta_tickets.TKT_ID = " + intNvoKey
                                + " group by vta_tickets.TKT_FOLIO, vta_proveedor.PV_RFC, vta_cliente.CT_RAZONSOCIAL, vta_cliente.CT_RFC, vta_tickets.TKT_FECHA, vta_proveedor.PV_RAZONSOCIAL; ";
                    }

                    try {
                        ResultSet rs1 = oConn.runQuery(strSqlEmp);
                        mail.setReplaceContent(rs1);
//                  if(rs1.getStatement() != null )rs1.getStatement().close(); 
                        rs1.close();
                    } catch (SQLException ex) {
                        log.error(ex.getMessage());
                    }

                    mail.setDestino(strLstMail);
                    boolean bol = mail.sendMail();
                    if (bol) {
                        //System.out.println("MAIL ENVIADO.");
                    } else {
                        //System.out.println("FALLO EL ENVIO DEL MAIL.");
                    }

                } else {
                    //strResp = "ERROR: INGRESE UN MAIL";
                }
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR:" + ex.getMessage());
        }

        return "";
    }

    /**
     * Obtenemos los valores del template para el mail
     *
     * @param strNom Es el nombre del template
     * @return Regresa un arreglo con los valores del template
     */
    public String[] getMailTemplate(Conexion oConn, String strNom) {
        String[] listValores = new String[2];
        String strSql = "select MT_ASUNTO,MT_CONTENIDO from mailtemplates where MT_ABRV ='" + strNom + "'";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                listValores[0] = rs.getString("MT_ASUNTO");
                listValores[1] = rs.getString("MT_CONTENIDO");
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listValores;
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
    protected String GeneraImpresionPDF(Conexion oConn, String strPath,
            String strFAC_NOMFORMATO, String strFolio, int intTransaccion,
            String strPATHFonts) {
        String strResp = "OK";
        //Posicion inicial para el numero de pagina
        String strPosX = null;
        String strTitle = strFAC_NOMFORMATO;

        try {
            Document documentPdf = new Document();
            PdfWriter writer = PdfWriter.getInstance(documentPdf, new FileOutputStream(strPath + strFAC_NOMFORMATO + strFolio + ".pdf"));
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
            documentPdf.open();
            Formateador format = new Formateador();
            format.setIntTypeOut(Formateador.FILE);
            format.setStrPath(strPath);
            format.InitFormat(oConn, strFAC_NOMFORMATO);
            String strRes = format.DoFormat(oConn, intTransaccion);
            if (strRes.equals("OK")) {
                CIP_Formato fPDF = new CIP_Formato();
                fPDF.setDocument(documentPdf);
                fPDF.setWriter(writer);
                fPDF.setStrPathFonts(strPATHFonts);
                fPDF.EmiteFormato(format.getFmXML());
            } else {
                strResp = strRes;
            }
            documentPdf.close();
            writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        }
        return strResp;
    }

    /**
     * Obtenemos el mail del cliente
     *
     * @param intIdCliente Es el id del cliente
     * @param oConn Es la conexión a la base de datos
     * @return Regresa el primero correo del cliente
     */
    public String getMail(int intIdCliente, Conexion oConn) {
        String strMail = "";
        String strSql = "select CT_EMAIL1 from vta_cliente where CT_ID = " + intIdCliente;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strMail = rs.getString("CT_EMAIL1");
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return strMail;
    }
    // </editor-fold>

    private void doGeneraEntradaPedidoSurtido(int intDocId) {
        String strSql = "select vta_movproddeta.* from vta_pedidosdeta,vta_facturasdeta,vta_movproddeta "
                + " where vta_pedidosdeta.PDD_ID = vta_facturasdeta.PDD_ID "
                + "AND vta_movproddeta.MPD_IDORIGEN = vta_pedidosdeta.PDD_ID "
                + "AND FAC_ID = " + intDocId;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                //Generamos un movimiento de entrada de la mercancia que se cancelo.
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            log.debug(ex.getMessage());
        }
    }

    /**
     * Obtiene la llave privada
     *
     * @param strPath Es el path del archivo
     * @return Regresa la llave en binario
     */
    public byte[] getPrivateKey(String strPath) {
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

    /**
     * Este metodo borra el folio indicado y regresa el autoincremental al valor
     * anterior
     *
     * @param oConn Es la conexion
     * @param intTipo Es el tipo de folio a generar TICKET FACTURA RECIBO DE
     * COBROS RECIBO DE PAGOS ETC
     * @param intIdDoc Es el id del documento
     * @param intIdEmp Es el id de la sucursal
     * @return Regresa una cadena con el folio nuevo
     */
    public String recoveryIdLost(Conexion oConn, int intTipo, int intIdDoc, int intIdEmp) {
        String strRes = "OK";
        String strNomTablaFolios = "vta_";
        switch (intTipo) {
            case Folios.TICKET:
                strNomTablaFolios += "tickets";
                break;
            case Folios.FACTURA:
                strNomTablaFolios += "facturas";
                break;
            case Folios.PEDIDOS:
                strNomTablaFolios += "pedidos";
                break;
            case Folios.COTIZACIONES:
                strNomTablaFolios += "cotiza";
                break;
        }
        try {
            intIdDoc--;
            //Cambiamos el autoincremental al folio anterior
            String strInit = "alter table " + strNomTablaFolios + " auto_increment = " + intIdDoc;
            oConn.runQueryLMD(strInit);
        } catch (NumberFormatException ex) {
            this.bitacora.GeneraBitacora(ex.getMessage(), oConn.getStrUsuario(), "recoveryId", oConn);
        }
        return strRes;
    }

    // <editor-fold defaultstate="collapsed" desc="Cerramos el pedido">
    public void doCerrar() {  //pedido cerrado 
        this.strResultLast = "OK";
        // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
        if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
        }
        // </editor-fold>
        //Validamos que no este anulado
        if (this.document.getFieldInt("PD_ANULADA") == 0) {

            //Definimos campos
            this.document.setFieldInt("PD_STATUS", 4);
            this.document.setFieldString("PD_HORA", this.fecha.getHoraActual());
            this.document.setFieldString("PD_FECHA", this.fecha.getFechaActual());

            String strResp1 = this.document.Modifica(oConn);

            if (!strResp1.equals("OK")) {
                this.strResultLast = strResp1;

            } else {
                // <editor-fold defaultstate="collapsed" desc="Bitacora">
                this.saveBitacora("PEDIDO ", "CERRAR", this.document.getFieldInt("PD_ID"));
                // </editor-fold>
            }

        } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
        }
        // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
        if (this.bolTransaccionalidad) {
            if (this.strResultLast.startsWith("OK")) {
                this.oConn.runQueryLMD("COMMIT");
                this.strResultLast = "PEDIDO CERRADO";
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }
        }
        // </editor-fold>
    }

    /**
     * Genera el nombre del xml
     *
     * @param mapeo Es el objeto del mapeo
     * @param intTransaccion Es la transaccion
     * @param strNombreReceptor Es el nombre del receptor
     * @param strFechaCFDI Esla fecha del CFDI
     * @param strFolioFiscalUUID Es el UUID del comprobante fisca
     * @return Regresa la cadena formateada
     */
    public String getNombreFileXml(ERP_MapeoFormato mapeo, int intTransaccion, String strNombreReceptor, String strFechaCFDI, String strFolioFiscalUUID) {
        String strNomFileXml = null;
        String strPatronNomXml = mapeo.getStrNomXML("NOMINA");
        if (mapeo.getIntTipoComp() == 8) {
            strPatronNomXml = mapeo.getStrNomXML("NOTACARGO");
        }
        if (mapeo.getIntTipoComp() == 9) {
            strPatronNomXml = mapeo.getStrNomXML("NOTACARGOPROV");
        }
        strNomFileXml = strPatronNomXml.replace("%Transaccion%", intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaCFDI).replace("%UUID%", strFolioFiscalUUID).replace(" ", "_");
        strNomFileXml = strNomFileXml.replace("__", "_");
        return strNomFileXml;
    }
    // </editor-fold>
}
