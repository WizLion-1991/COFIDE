package ERP;

import ERP.BusinessEntities.PoliCtas;
import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.UtilJson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import org.apache.commons.io.IOUtils;
import org.apache.commons.ssl.Base64;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase realiza las peticiones al sistema contable para actualizar y
 * generar polizas
 *
 * @author zeus
 */
public class PolizasContables {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   protected Conexion oConn;
   protected VariableSession varSesiones;
   protected HttpServletRequest request;
   protected String strResultLast;
   protected String strURLServicio;
   protected ArrayList<PoliCtas> lstCuentasAG;
   private ArrayList<ArchivoEnvio> lstFiles;
   protected boolean bolEsAnticipo = false;
   protected boolean bolUsaAnticipo = false;
   private boolean bolUsaCheque;
   private boolean bolUsaComplementoAjuste;
   private double dblComplementoAjuste;
   private String strNumCheque;
   private String strRfcDIOT;
   private String strPV_NOMBREDIOT;
   private double dblTmp1 = 0;
   private double dblTmp2 = 0;
   private double dblTmp3 = 0;
   private double dblTmp4 = 0;
   private double dblTmp5 = 0;
   private double dblTmp6 = 0;
   private double dblTmp7 = 0;
   private double dblTmp8 = 0;
   private double dblTmp9 = 0;
   private double dblTmp10 = 0;
   public boolean bolEsTicket = false;
   //Objetos para pagos masivos
   private PagosMasivos pagosMasivos;
   private PagosMasivosCtas pagosMasivosCtas;
   //Objeto json para la migracion contable
   private JSONObject objJson;
   private JSONArray listPolizas;
   private JSONArray listArchivos;
   //Muestra el banco origen
   private String strBancoOrigen;
   private String strCuentaOrigen;
   private String strBancoDestino;
   private String strRfcTerceros;
   private String strCuentaDestino;
   private String strRFCProveedor;
   private String strCXP_UUID;
   private UtilJson json;
   private int intIdPoliMasivo;

   public double getDblTmp4() {
      return dblTmp4;
   }

   public void setDblTmp4(double dblTmp4) {
      this.dblTmp4 = dblTmp4;
   }

   public double getDblTmp5() {
      return dblTmp5;
   }

   public void setDblTmp5(double dblTmp5) {
      this.dblTmp5 = dblTmp5;
   }

   public double getDblTmp6() {
      return dblTmp6;
   }

   public void setDblTmp6(double dblTmp6) {
      this.dblTmp6 = dblTmp6;
   }

   public double getDblTmp7() {
      return dblTmp7;
   }

   public void setDblTmp7(double dblTmp7) {
      this.dblTmp7 = dblTmp7;
   }

   public double getDblTmp8() {
      return dblTmp8;
   }

   public void setDblTmp8(double dblTmp8) {
      this.dblTmp8 = dblTmp8;
   }

   public double getDblTmp9() {
      return dblTmp9;
   }

   public void setDblTmp9(double dblTmp9) {
      this.dblTmp9 = dblTmp9;
   }

   public double getDblTmp10() {
      return dblTmp10;
   }

   public void setDblTmp10(double dblTmp10) {
      this.dblTmp10 = dblTmp10;
   }

   /**
    * Regresa el UUID a usar en las cxp
    *
    * @return Es el UUID
    */
   public String getStrCXP_UUID() {
      return strCXP_UUID;
   }

   /**
    * Define el UUID a usar en las cxp
    *
    * @param strCXP_UUID Es el UUID
    */
   public void setStrCXP_UUID(String strCXP_UUID) {
      this.strCXP_UUID = strCXP_UUID;
   }

   /**
    * Regresa el rfc del proveedor
    *
    * @return Es un texto con el rfc del proveedor
    */
   public String getStrRFCProveedor() {
      return strRFCProveedor;
   }

   /**
    * Define el rfc del proveedor
    *
    * @param strRFCProveedor Es un texto con el rfc del proveedor
    */
   public void setStrRFCProveedor(String strRFCProveedor) {
      this.strRFCProveedor = strRFCProveedor;
   }

   public int getIntIdPoliMasivo() {
      return intIdPoliMasivo;
   }

   public void setIntIdPoliMasivo(int intIdPoliMasivo) {
      this.intIdPoliMasivo = intIdPoliMasivo;
   }

   public String getStrBancoOrigen() {
      return strBancoOrigen;
   }

   public void setStrBancoOrigen(String strBancoOrigen) {
      this.strBancoOrigen = strBancoOrigen;
   }

   public String getStrCuentaOrigen() {
      return strCuentaOrigen;
   }

   public void setStrCuentaOrigen(String strCuentaOrigen) {
      this.strCuentaOrigen = strCuentaOrigen;
   }

   public String getStrBancoDestino() {
      return strBancoDestino;
   }

   public void setStrBancoDestino(String strBancoDestino) {
      this.strBancoDestino = strBancoDestino;
   }

   public String getStrCuentaDestino() {
      return strCuentaDestino;
   }

   public void setStrCuentaDestino(String strCuentaDestino) {
      this.strCuentaDestino = strCuentaDestino;
   }

   public PagosMasivos getPagosMasivos() {
      return pagosMasivos;
   }

   /**
    * Objeto para obtener los pagos masivos de los cobros
    *
    * @param pagosMasivos
    */
   public void setPagosMasivos(PagosMasivos pagosMasivos) {
      this.pagosMasivos = pagosMasivos;
   }

   public PagosMasivosCtas getPagosMasivosCtas() {
      return pagosMasivosCtas;
   }

   /**
    * Objetos para pagos masivos de pagos de cuentas por pagar
    *
    * @param pagosMasivosCtas
    */
   public void setPagosMasivosCtas(PagosMasivosCtas pagosMasivosCtas) {
      this.pagosMasivosCtas = pagosMasivosCtas;
   }

   /**
    * Regresa el rfc usado para la diot
    *
    * @return
    */
   public String getStrRfcDIOT() {
      return strRfcDIOT;
   }

   /**
    * Define el rfc a usar para la diot
    *
    * @param strRfcDIOT
    */
   public void setStrRfcDIOT(String strRfcDIOT) {
      this.strRfcDIOT = strRfcDIOT;
   }

   public double getDblTmp2() {
      return dblTmp2;
   }

   public double getDblTmp1() {
      return dblTmp1;
   }

   public void setDblTmp1(double dblTmp1) {
      this.dblTmp1 = dblTmp1;
   }

   /**
    * Importe tempooral 2
    *
    * @param dblTmp2
    */
   public void setDblTmp2(double dblTmp2) {
      this.dblTmp2 = dblTmp2;
   }

   public double getDblTmp3() {
      return dblTmp3;
   }

   /**
    * Importe temporal 3
    *
    * @param dblTmp3
    */
   public void setDblTmp3(double dblTmp3) {
      this.dblTmp3 = dblTmp3;
   }

   public String getStrNumCheque() {
      return strNumCheque;
   }

   public void setStrNumCheque(String strNumCheque) {
      this.strNumCheque = strNumCheque;
   }

   public boolean isBolUsaCheque() {
      return bolUsaCheque;
   }

   public void setBolUsaCheque(boolean bolUsaCheque) {
      this.bolUsaCheque = bolUsaCheque;
   }

   public boolean isBolUsaComplementoAjuste() {
      return bolUsaComplementoAjuste;
   }

   public void setBolUsaComplementoAjuste(boolean bolUsaComplementoAjuste) {
      this.bolUsaComplementoAjuste = bolUsaComplementoAjuste;
   }

   public double getDblComplementoAjuste() {
      return dblComplementoAjuste;
   }

   public void setDblComplementoAjuste(double dblComplementoAjuste) {
      this.dblComplementoAjuste = dblComplementoAjuste;
   }
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PolizasContables.class.getName());
   /**
    * Indica si las facturas se llevan a detalle
    */
   protected boolean bolVTA_DETA = false;
   /**
    * Constante para identificar que el tipo de operacion es un ticket
    */
   public static final int TICKET = 0;
   /**
    * Constante para identificar que el tipo de operacion es una factura
    */
   public static final int FACTURA = 1;
   /**
    * Constante para identificar que el tipo de operacion es un recibo
    */
   public static final int RECIBOS = 2;
   /**
    * Constante para identificar que el tipo de operacion es un recibo de pago a
    * proveedores
    */
   public static final int RECIBOS_PROV = 3;
   /**
    * Constante para identificar que el tipo de operacion es una orden de compra
    */
   public static final int OCOMPRA = 4;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento al
    * inventario
    */
   public static final int INVENTARIO = 5;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento de
    * nota de credito
    */
   public static final int NCREDITO = 6;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento de
    * bancos
    */
   public static final int BANCOS = 7;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento de
    * cuentas por pagar
    */
   public static final int CUENTASXPAGAR = 8;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento de
    * nominas
    */
   public static final int NOMINAS = 9;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento de
    * nominas
    */
   public static final int NOTAS_CARGO = 10;
   protected String strOper;
   protected String strUserCte;
   protected String strPassCte;
   protected TableMaster document;
   protected ArrayList<String> lstCuentas;

   public boolean isBolEsAnticipo() {
      return bolEsAnticipo;
   }

   /**
    * Indica que es un anticipo
    *
    * @param bolEsAnticipo true/false
    */
   public void setBolEsAnticipo(boolean bolEsAnticipo) {
      this.bolEsAnticipo = bolEsAnticipo;
   }

   public boolean isBolUsaAnticipo() {
      return bolUsaAnticipo;
   }

   /**
    * Indica que se usa un anticipo
    *
    * @param bolUsaAnticipo true/false
    */
   public void setBolUsaAnticipo(boolean bolUsaAnticipo) {
      this.bolUsaAnticipo = bolUsaAnticipo;
   }

   /**
    * Obtiene el documento maestro
    *
    * @param document Es el documento maestro
    */
   public void setDocumentMaster(TableMaster document) {
      this.document = document;
   }

   /**
    * Regresa el resultado de la ultima operacion
    *
    * @return Regresa Ok o Error
    */
   public String getStrResultLast() {
      return strResultLast;
   }

   /**
    * Regresa la operacion por realizar
    *
    * @return Es el nombre de la operacion por realizar
    */
   public String getStrOper() {
      return strOper;
   }

   /**
    * Establece la operacion por realizar
    *
    * @param strOper Es el nombre de la operacion por realizar
    */
   public void setStrOper(String strOper) {
      this.strOper = strOper;
   }

   /**
    * Obtiene la lista de las cuentas a usar
    *
    * @return Regresa un array list con las cuentas
    */
   public ArrayList<String> getLstCuentas() {
      return lstCuentas;
   }

   /**
    * Regresa el password del cliente contable
    *
    * @return Es el password
    */
   public String getStrPassCte() {
      return strPassCte;
   }

   /**
    * Define el password del cliente contable
    *
    * @param strPassCte Es el password
    */
   public void setStrPassCte(String strPassCte) {
      this.strPassCte = strPassCte;
   }

   /**
    * Regresa el usuario para el sistema contable
    *
    * @return Es el id del usuario
    */
   public String getStrUserCte() {
      return strUserCte;
   }

   /**
    * Define el usuario para el sistema contable
    *
    * @param strUserCte Es el id del usuario
    */
   public void setStrUserCte(String strUserCte) {
      this.strUserCte = strUserCte;
   }

   /**
    * Regresa la lista de cuentas especiales
    *
    * @return Es el listado de cuentas especiales
    */
   public ArrayList<PoliCtas> getLstCuentasAG() {
      return lstCuentasAG;
   }

   /**
    * Define la lista de cuentas especiales
    *
    * @param lstCuentasAG Es el listado de cuentas especiales
    */
   public void setLstCuentasAG(ArrayList<PoliCtas> lstCuentasAG) {
      this.lstCuentasAG = lstCuentasAG;
   }

   /**
    * Nos dice si las facturas se llevan a detalle en ventas
    *
    * @return un valor boolean
    */
   public boolean isBolVTA_DETA() {
      return bolVTA_DETA;
   }

   /**
    * Define si las facturas se llevan a detalle en ventas
    *
    * @param bolVTA_DETA un valor boolean
    */
   public void setBolVTA_DETA(boolean bolVTA_DETA) {
      this.bolVTA_DETA = bolVTA_DETA;
   }

   /**
    * Regresa la URL del servicio web
    *
    * @return Es una cadena con la url del servicio
    */
   public String getStrURLServicio() {
      return strURLServicio;
   }

   /**
    * Define la URL del servicio web
    *
    * @param strURLServicio Es una cadena con la url del servicio
    */
   public void setStrURLServicio(String strURLServicio) {
      this.strURLServicio = strURLServicio;
   }

   /**
    *Obtiene el RFC a terceros
    * @return
    */
   public String getStrRfcTerceros() {
      return strRfcTerceros;
   }

   /**
    *Define el rfc a terceros
    * @param strRfcTerceros
    */
   public void setStrRfcTerceros(String strRfcTerceros) {
      this.strRfcTerceros = strRfcTerceros;
   }

   public String getStrPV_NOMBREDIOT() {
      return strPV_NOMBREDIOT;
   }

   /**
    *Define el nombre del proveedor de la DIOT
    * @param strPV_NOMBREDIOT Es el nombre del proveedor
    */
   public void setStrPV_NOMBREDIOT(String strPV_NOMBREDIOT) {
      this.strPV_NOMBREDIOT = strPV_NOMBREDIOT;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor
    *
    * @param oConn Es la conexion a la bd
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion HTTP
    */
   public PolizasContables(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      System.setProperty("https.protocols", "TLSv1");
      this.oConn = oConn;
      this.varSesiones = varSesiones;
      this.request = request;
      this.strOper = "";
      this.strUserCte = "";
      this.strPassCte = "";
      this.lstCuentas = new ArrayList<String>();
      this.lstCuentasAG = new ArrayList<PoliCtas>();
      this.lstFiles = new ArrayList<ArchivoEnvio>();
      this.objJson = new JSONObject();
      this.listPolizas = new JSONArray();
      this.listArchivos = new JSONArray();
      try {
         this.objJson.put("detalle", listPolizas);
         this.objJson.put("archivos", listArchivos);
      } catch (JSONException ex) {
         log.exit(ex.getMessage());
      }
      this.json = new UtilJson();
      this.intIdPoliMasivo = 0;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Genera el XML para enviar los datos y que se genera la info contable
    *
    * @param intId Es el id de la operacion
    * @param intTipoOpera Es el tipo de operacion
    */
   public void callRemote(int intId, int intTipoOpera) {
      //Validamos si tiene un usuario y password
      if (!this.strUserCte.equals("") && !this.strPassCte.equals("")) {
         if (intTipoOpera == PolizasContables.FACTURA) {
            if (strOper.equals("NEW")) {
               doPolizaFactura(intId);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaFacturaCancel(intId);
            }
         }
         if (intTipoOpera == PolizasContables.RECIBOS) {
            if (strOper.equals("NEW")) {
               doPolizaRecibos(intId, "", "");
            }
            if (strOper.equals("CANCEL")) {
               doPolizaRecibosCancel(intId, "", "");
            }
         }
         if (intTipoOpera == PolizasContables.CUENTASXPAGAR) {
            if (strOper.equals("NEW")) {
               doPolizaCXP(intId);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaCXPCancel(intId);
            }
         }
         if (intTipoOpera == PolizasContables.RECIBOS_PROV) {
            if (strOper.equals("NEW")) {
               doPolizaPagos(intId, "", "");
            }
            if (strOper.equals("CANCEL")) {
               doPolizaPagosCancel(intId, "", "");
            }
         }
         if (intTipoOpera == PolizasContables.BANCOS) {
            if (strOper.equals("NEW")) {
               doPolizaBcos(intId, "", "");
            }
            if (strOper.equals("CANCEL")) {
               doPolizaBcosCancel(intId, "", "");
            }
         }
         if (intTipoOpera == PolizasContables.NOMINAS) {
            if (strOper.equals("NEW")) {
               doPolizaNominas(intId, "", "");
            }
            if (strOper.equals("CANCEL")) {
               doPolizaNominasCancel(intId, "", "");
            }
         }
         if (intTipoOpera == PolizasContables.NCREDITO) {
            if (strOper.equals("NEW")) {
               doPolizaNCredito(intId);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaNCreditoCancel(intId);
            }
         }
         if (intTipoOpera == PolizasContables.NOTAS_CARGO) {
            if (strOper.equals("NEW")) {
               doPolizaNotasCargo(intId);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaNotasCargoCancel(intId);
            }
         }
      } else {
         this.strResultLast = "ERROR:NO CONTIENE UN USUARIO Y PASSWORD LLENO";
      }
   }

   /**
    * Genera el XML para enviar los datos y que se genera la info contable
    *
    * @param intId Es el id de la operacion
    * @param strTipo Es el tipo de poliza DIARIO/INGRESOS/EGRESOS
    * @param strFecha Es la fecha
    * @param strHora Es la hora
    * @param strConcepto Es el concepto
    * @param dblTasaPeso Es la tasa del peso
    * @param intMoneda Es el id de la moneda
    */
   public void callRemote(int intId, String strTipo, String strFecha, String strHora, String strConcepto, int intMoneda, double dblTasaPeso) {
      //Validamos si tiene un usuario y password
      if (!this.strUserCte.equals("") && !this.strPassCte.equals("")) {
         this.doPolizaManual(intId, strTipo, strFecha, strHora, strConcepto, intMoneda, dblTasaPeso);
      } else {
         this.strResultLast = "ERROR:NO CONTIENE UN USUARIO Y PASSWORD LLENO";
      }
   }

   /**
    * Genera el XML para enviar los datos y que se genera la info contable
    *
    * @param intId Es el id de la operacion
    * @param intTipoOpera Es el tipo de operacion
    * @param strRazonSocial Es la razon social del cliente
    * @param strFolio Es el folio del pago
    */
   public void callRemote(int intId, int intTipoOpera, String strRazonSocial, String strFolio) {
      //Validamos si tiene un usuario y password
      if (!this.strUserCte.equals("") && !this.strPassCte.equals("")) {
         if (intTipoOpera == PolizasContables.FACTURA) {
            if (strOper.equals("NEW")) {
               doPolizaFactura(intId);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaFacturaCancel(intId);
            }
         }
         if (intTipoOpera == PolizasContables.RECIBOS) {
            if (strOper.equals("NEW")) {
               doPolizaRecibos(intId, strRazonSocial, strFolio);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaRecibosCancel(intId, strRazonSocial, strFolio);
            }
         }
         if (intTipoOpera == PolizasContables.CUENTASXPAGAR) {
            if (strOper.equals("NEW")) {
               doPolizaCXP(intId, strRazonSocial, strFolio);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaCXPCancel(intId);
            }
         }
         if (intTipoOpera == PolizasContables.RECIBOS_PROV) {
            if (strOper.equals("NEW")) {
               doPolizaPagos(intId, strRazonSocial, strFolio);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaPagosCancel(intId, strRazonSocial, strFolio);
            }
         }
         if (intTipoOpera == PolizasContables.BANCOS) {
            if (strOper.equals("NEW")) {
               doPolizaBcos(intId, strRazonSocial, strFolio);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaBcosCancel(intId, strRazonSocial, strFolio);
            }
         }
         if (intTipoOpera == PolizasContables.NOMINAS) {
            if (strOper.equals("NEW")) {
               doPolizaNominas(intId, strRazonSocial, strFolio);
            }
            if (strOper.equals("CANCEL")) {
               doPolizaNominasCancel(intId, strRazonSocial, strFolio);
            }
         }
      } else {
         this.strResultLast = "ERROR:NO CONTIENE UN USUARIO Y PASSWORD LLENO";
      }
   }

   // <editor-fold defaultstate="collapsed" desc="Ventas">
   /**
    * Genera la poliza de una factura
    *
    * @param intId Es el id de la factura
    */
   protected void doPolizaFactura(int intId) {
      this.strResultLast = "OK";
      String strPrefijo = "FAC_";
      if (bolEsTicket) {
         strPrefijo = "TKT_";
      }
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_CTE = this.lstCuentas.get(2);
         String strCTA_CTE_RET_ISR = this.lstCuentas.get(3);
         String strCTA_CTE_RET_IVA = this.lstCuentas.get(4);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString("FAC_FECHA") + "|"
             + "DIARIO|"
             + this.document.getFieldString("FAC_HORA").substring(0, 5) + "|"
             + this.document.getFieldString("FAC_RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt(strPrefijo + "EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString(strPrefijo + "FECHA"));
               objJson.put("PO_HORA", this.document.getFieldString(strPrefijo + "HORA").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse(this.document.getFieldString(strPrefijo + "RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt(strPrefijo + "MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble(strPrefijo + "TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }

            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;
            /*strCTA_VTA + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "IMPORTE") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| ";*/
            //Si la cuenta de ventas es a detalle no mandamos la venta global
            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "IMPORTE"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "IMPUESTO1") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| ";*/

            if (this.document.getFieldDouble(strPrefijo + "IMPUESTO1") != 0) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "IMPUESTO1"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem3 = null;/*strCTA_CTE + "|"
             + this.document.getFieldDouble(strPrefijo + "TOTAL") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| "*/;
            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               //Evaluamos si tiene retenciones
               if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "NETO"));
               } else {
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "TOTAL"));
               }
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //RETENCIONES
            if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
               if (document.getFieldDouble("FAC_RETISR") > 0) {
                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE_RET_ISR);
                     objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("FAC_RETISR"));
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("FAC_FOLIO_C"));
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               if (document.getFieldDouble("FAC_RETIVA") > 0) {

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE_RET_IVA);
                     objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("FAC_RETIVA"));
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("FAC_FOLIO_C"));
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR VENTAS";
      }
   }

   /**
    * Aplica la regla de negocio para generar la poliza contable de cancelacion
    * de facturas
    *
    * @param intId Es el id de la operacion cancelada
    */
   protected void doPolizaFacturaCancel(int intId) {
      this.strResultLast = "OK";
      String strPrefijo = "FAC_";
      if (bolEsTicket) {
         strPrefijo = "TKT_";
      }
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_CTE = this.lstCuentas.get(2);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString(strPrefijo + "FECHAANUL") + "|"
             + "DIARIO|"
             + this.document.getFieldString(strPrefijo + "HORANUL").substring(0, 5) + "|"
             + "CANCELACION " + this.document.getFieldString(strPrefijo + "RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt(strPrefijo + "EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString(strPrefijo + "FECHAANUL"));
               objJson.put("PO_HORA", this.document.getFieldString(strPrefijo + "HORANUL").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + this.document.getFieldString(strPrefijo + "RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt(strPrefijo + "MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble(strPrefijo + "TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;/*strCTA_VTA + "|"
             + this.document.getFieldDouble(strPrefijo + "IMPORTE") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";*/

            //Si la cuenta de ventas es a detalle no mandamos la venta global

            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "IMPORTE"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + this.document.getFieldDouble(strPrefijo + "IMPUESTO1") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
               objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "IMPUESTO1"));
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            String strItem3 = null;/*strCTA_CTE + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "TOTAL") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";
             * */

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "TOTAL"));
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR VENTAS";
      }
   }

   /**
    * Genera la poliza de un cobro realizado
    *
    * @param intId Es el id del pago
    * @param strRazonSocial Es la razon social del cliente
    * @param strFolio Es el folio del pago
    */
   protected void doPolizaRecibos(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";
      int intNumOblig = 3;
      if (this.bolEsAnticipo || this.bolUsaAnticipo) {
         intNumOblig = 2;
      }
      log.debug("Campos obligatorios ... " + intNumOblig);
      //Validamos que el arrayList tenga 4 elementos(Bancos,Cte,IVA,IVA COBRADO)
      if (this.lstCuentas.size() >= intNumOblig) {
         String strCTA_BCO = this.lstCuentas.get(0);
         String strCTA_CTE = this.lstCuentas.get(1);
         String strCTA_IVA_TRAS = "";
         String strCTA_IVA_COB = "";
         double dblImportePagoDoc = document.getFieldDouble("MC_ABONO");
         //Asignamos importe de pago global
         if (this.dblTmp2 != 0) {
            dblImportePagoDoc = this.dblTmp2;
         }
         // <editor-fold defaultstate="collapsed" desc="Los anticipos no tienen iva en caso de usar ya se aplicaron manualmente">
         if (!this.bolEsAnticipo && !this.bolUsaAnticipo) {
            strCTA_IVA_TRAS = this.lstCuentas.get(2);
            strCTA_IVA_COB = this.lstCuentas.get(3);
         } else {
            if (this.bolUsaAnticipo) {
               strCTA_IVA_TRAS = this.lstCuentas.get(2);
               strCTA_IVA_COB = this.lstCuentas.get(3);
            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Id de la poliza generada anteriormente">
         int intIdPoliza = this.document.getFieldInt("MC_EXEC_INTER_CP");
         if (this.pagosMasivos != null) {
            intIdPoliza = this.intIdPoliMasivo;
         }
         // </editor-fold>
         log.debug("strCTA_BCO:" + strCTA_BCO);
         log.debug("strCTA_CTE:" + strCTA_CTE);
         log.debug("strCTA_IVA_TRAS:" + strCTA_IVA_TRAS);
         log.debug("strCTA_IVA_COB:" + strCTA_IVA_COB);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/

            /*
             * this.document.getFieldString("MC_FECHA") + "|"
             + "INGRESOS|"
             + this.document.getFieldString("MC_HORA").substring(0, 5) + "|"
             + strRazonSocial + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "INGRESOS");
               objJson.put("PO_POLIZA", intIdPoliza);
               objJson.put("PO_FECHA", this.document.getFieldString("MC_FECHA"));
               objJson.put("PO_HORA", this.document.getFieldString("MC_HORA").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt("MC_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("MC_TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            if (!this.bolEsAnticipo && !this.bolUsaAnticipo) {
               // <editor-fold defaultstate="collapsed" desc="Pago normal">
               String strItem1 = null;/*strCTA_BCO + "|"
                + dblImportePagoDoc + "|"
                + "0.0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                  objJsonPoliDeta.put("PD_DEBE", dblImportePagoDoc);
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
               String strItem2 = null;/*strCTA_CTE + "|"
                + "0.0|"
                + dblImportePagoDoc + "|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               //Si es un pago masivo no enviamos el movimiento de clientes
               if (this.pagosMasivos != null) {
                  strItem2 = "";
                  if (this.lstCuentasAG == null) {
                     this.lstCuentasAG = new ArrayList<PoliCtas>();
                  }
                  Iterator<movCliente> it = this.pagosMasivos.lstPagos.iterator();
                  while (it.hasNext()) {
                     movCliente deta = it.next();
                     //Obtenemos el folio de referencia de la factura cobrada
                     String strFolioRef = this.getFolioReferenciaCobro(deta);
                     if (strFolioRef == null) {
                        strFolioRef = strFolio;
                     }
                     //Movimiento de clientes por documento
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strCTA_CTE);
                     pol.setBolEsCargo(false);
                     pol.setDblImporte(deta.getCta_clie().getFieldDouble("MC_ABONO"));
                     pol.setStrFolioRef(strFolioRef);
                     lstCuentasAG.add(pol);
                  }
               } else {
                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", dblImportePagoDoc);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               //Aplicamos ajuste cuando proceda
               if (this.bolUsaComplementoAjuste) {
                  strItem2 = null;/*strCTA_CTE + "|"
                   + "0.0|"
                   + this.dblComplementoAjuste + "|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + strFolio + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", this.dblComplementoAjuste);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               double dblImpuestoAplica = this.document.getFieldDouble("MC_IMPUESTO1");
               //Impuesto global
               if (this.dblTmp3 > 0) {
                  dblImpuestoAplica = this.dblTmp3;
               }
               String strItem3 = null; /*strCTA_IVA_TRAS + "|"
                + this.document.getFieldDouble("MC_IMPUESTO1") + "|"
                + "0.0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_TRAS);
                  objJsonPoliDeta.put("PD_DEBE", dblImpuestoAplica);
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
               String strItem4 = null; /*strCTA_IVA_COB + "|"
                + "0.0|"
                + this.document.getFieldDouble("MC_IMPUESTO1") + "|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_COB);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", dblImpuestoAplica);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
               //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
               this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, strItem4, "", "", "", "", "", "");
               // </editor-fold>

            } else {
               if (this.bolEsAnticipo) {
                  // <editor-fold defaultstate="collapsed" desc="Anticipo">
                  String strItem1 = strCTA_BCO + "|"
                          + dblImportePagoDoc + "|"
                          + "0.0|"
                          + "0|"
                          + "0|"
                          + "0|"
                          + "0|"
                          + "0|"
                          + "0|"
                          + strFolio + "| ";
                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                     objJsonPoliDeta.put("PD_DEBE", dblImportePagoDoc);
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
                  String strItem2 = null;/*strCTA_CTE + "|"
                   + "0.0|"
                   + dblImportePagoDoc + "|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + strFolio + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", dblImportePagoDoc);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
                  //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
                  this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, "", "", "", "", "", "", "", "");
                  // </editor-fold>
               } else {
                  if (this.bolUsaAnticipo) {
                     // <editor-fold defaultstate="collapsed" desc="Usa anticipo">
                     /*
                      this.document.getFieldString("MC_FECHA") + "|"
                      + "DIARIO|"
                      + this.document.getFieldString("MC_HORA").substring(0, 5) + "|"
                      + strRazonSocial + "|"
                      + "1|"
                      + "1|";
                      * */
                     try {
                        objJson.put("PO_TIPO", "DIARIO");
                        objJson.put("PO_POLIZA", intIdPoliza);
                        objJson.put("PO_FECHA", this.document.getFieldString("MC_FECHA"));
                        objJson.put("PO_HORA", this.document.getFieldString("MC_HORA").substring(0, 5));
                        objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                        objJson.put("PO_USUARIO", 1);
                        objJson.put("PO_APLIC", 1);
                        objJson.put("PO_CHEQUE", "");
                        objJson.put("PO_MONTO", 0);
                        objJson.put("PO_BANCO_ORIGEN", "");
                        objJson.put("PO_BANCO_DESTINO", "");
                        objJson.put("PO_RFC_TERCEROS", "");
                        objJson.put("PO_CUENTA_ORIGEN", "");
                        objJson.put("PO_CUENTA_DESTINO", "");
                        objJson.put("PO_BENEFICIARIO", "");
                        objJson.put("PO_RFC", "");
                        objJson.put("PO_MONEDA", this.document.getFieldInt("MC_MONEDA"));
                        objJson.put("PO_PARIDAD", this.document.getFieldDouble("MC_TASAPESO"));
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     String strItem1 = null;/*strCTA_BCO + "|"
                      + dblImportePagoDoc + "|"
                      + "0.0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + strFolio + "| ";*/

                     try {
                        JSONObject objJsonPoliDeta = new JSONObject();
                        objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                        objJsonPoliDeta.put("PD_DEBE", dblImportePagoDoc);
                        objJsonPoliDeta.put("PD_HABER", 0);
                        objJsonPoliDeta.put("PD_APLICAIETU", 0);
                        objJsonPoliDeta.put("PD_APLICAIVA", 0);
                        objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                        objJsonPoliDeta.put("PD_TASA", 0);
                        objJsonPoliDeta.put("PD_RUBRO", 0);
                        objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                        objJsonPoliDeta.put("PD_FOLIO", strFolio);
                        objJsonPoliDeta.put("PD_NOTAS", "");
                        objJsonPoliDeta.put("RFCDiot", "");
                        objJsonPoliDeta.put("Base", 0);
                        objJsonPoliDeta.put("Retencion", 0);
                        objJsonPoliDeta.put("PD_RFC", "");
                        objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                        objJsonPoliDeta.put("PD_MONTO", 0);
                        objJsonPoliDeta.put("PD_UUID", "");
                        listPolizas.put(objJsonPoliDeta);
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     String strItem2 = null;/*strCTA_CTE + "|"
                      + "0.0|"
                      + dblImportePagoDoc + "|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + strFolio + "| ";*/

                     try {
                        JSONObject objJsonPoliDeta = new JSONObject();
                        objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                        objJsonPoliDeta.put("PD_DEBE", 0);
                        objJsonPoliDeta.put("PD_HABER", dblImportePagoDoc);
                        objJsonPoliDeta.put("PD_APLICAIETU", 0);
                        objJsonPoliDeta.put("PD_APLICAIVA", 0);
                        objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                        objJsonPoliDeta.put("PD_TASA", 0);
                        objJsonPoliDeta.put("PD_RUBRO", 0);
                        objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                        objJsonPoliDeta.put("PD_FOLIO", strFolio);
                        objJsonPoliDeta.put("PD_NOTAS", "");
                        objJsonPoliDeta.put("RFCDiot", "");
                        objJsonPoliDeta.put("Base", 0);
                        objJsonPoliDeta.put("Retencion", 0);
                        objJsonPoliDeta.put("PD_RFC", "");
                        objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                        objJsonPoliDeta.put("PD_MONTO", 0);
                        objJsonPoliDeta.put("PD_UUID", "");
                        listPolizas.put(objJsonPoliDeta);
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     //Si es un pago masivo no enviamos el movimiento de clientes
                     if (this.pagosMasivos != null) {
                        strItem2 = "";
                        if (this.lstCuentasAG == null) {
                           this.lstCuentasAG = new ArrayList<PoliCtas>();
                        }
                        Iterator<movCliente> it = this.pagosMasivos.lstPagos.iterator();
                        while (it.hasNext()) {
                           movCliente deta = it.next();
                           //Obtenemos el folio de referencia de la factura cobrada
                           String strFolioRef = this.getFolioReferenciaCobro(deta);
                           if (strFolioRef == null) {
                              strFolioRef = strFolio;
                           }
                           //Movimiento de clientes por documento
                           PoliCtas pol = new PoliCtas();
                           pol.setStrCuenta(strCTA_CTE);
                           pol.setBolEsCargo(false);
                           pol.setDblImporte(deta.getCta_clie().getFieldDouble("MC_ABONO"));
                           pol.setStrFolioRef(strFolioRef);
                           lstCuentasAG.add(pol);
                        }
                     }
                     String strItem3 = null;/*strCTA_IVA_TRAS + "|"
                      + this.document.getFieldDouble("MC_IMPUESTO1") + "|"
                      + "0.0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + strFolio + "| ";*/

                     try {
                        JSONObject objJsonPoliDeta = new JSONObject();
                        objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_TRAS);
                        objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("MC_IMPUESTO1"));
                        objJsonPoliDeta.put("PD_HABER", 0);
                        objJsonPoliDeta.put("PD_APLICAIETU", 0);
                        objJsonPoliDeta.put("PD_APLICAIVA", 0);
                        objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                        objJsonPoliDeta.put("PD_TASA", 0);
                        objJsonPoliDeta.put("PD_RUBRO", 0);
                        objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                        objJsonPoliDeta.put("PD_FOLIO", strFolio);
                        objJsonPoliDeta.put("PD_NOTAS", "");
                        objJsonPoliDeta.put("RFCDiot", "");
                        objJsonPoliDeta.put("Base", 0);
                        objJsonPoliDeta.put("Retencion", 0);
                        objJsonPoliDeta.put("PD_RFC", "");
                        objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                        objJsonPoliDeta.put("PD_MONTO", 0);
                        objJsonPoliDeta.put("PD_UUID", "");
                        listPolizas.put(objJsonPoliDeta);
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     String strItem4 = null;/*strCTA_IVA_COB + "|"
                      + "0.0|"
                      + this.document.getFieldDouble("MC_IMPUESTO1") + "|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + strFolio + "| ";*/

                     try {
                        JSONObject objJsonPoliDeta = new JSONObject();
                        objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_COB);
                        objJsonPoliDeta.put("PD_DEBE", 0);
                        objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("MC_IMPUESTO1"));
                        objJsonPoliDeta.put("PD_APLICAIETU", 0);
                        objJsonPoliDeta.put("PD_APLICAIVA", 0);
                        objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                        objJsonPoliDeta.put("PD_TASA", 0);
                        objJsonPoliDeta.put("PD_RUBRO", 0);
                        objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                        objJsonPoliDeta.put("PD_FOLIO", strFolio);
                        objJsonPoliDeta.put("PD_NOTAS", "");
                        objJsonPoliDeta.put("RFCDiot", "");
                        objJsonPoliDeta.put("Base", 0);
                        objJsonPoliDeta.put("Retencion", 0);
                        objJsonPoliDeta.put("PD_RFC", "");
                        objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                        objJsonPoliDeta.put("PD_MONTO", 0);
                        objJsonPoliDeta.put("PD_UUID", "");
                        listPolizas.put(objJsonPoliDeta);
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, strItem4, "", "", "", "", "", "");
                     // </editor-fold>
                  }
               }
            }

         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR RECIBOS DE COBROS";
      }
   }

   /**
    * Genera la poliza para cancelar un cobro realizado
    *
    * @param intId Es el id del pago
    * @param strRazonSocial Es la razon social del cliente
    * @param strFolio Es el folio del pago
    */
   protected void doPolizaRecibosCancel(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 4 elementos(Bancos,Cte,IVA,IVA COBRADO)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_BCO = this.lstCuentas.get(0);
         String strCTA_CTE = this.lstCuentas.get(1);
         String strCTA_IVA_TRAS = this.lstCuentas.get(2);
         String strCTA_IVA_COB = this.lstCuentas.get(3);
         double dblImportePagoDoc = document.getFieldDouble("MC_ABONO");
         //Asignamos importe de pago global
         if (this.dblTmp2 != 0) {
            dblImportePagoDoc = this.dblTmp2;
         }
         // <editor-fold defaultstate="collapsed" desc="Id de la poliza generada anteriormente">
         int intIdPoliza = this.document.getFieldInt("MC_EXEC_INTER_CP");
         if (this.pagosMasivos != null) {
            intIdPoliza = this.intIdPoliMasivo;
         }
         // </editor-fold>
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/

            /*
             this.document.getFieldString("MC_FECHAANUL") + "|"
             + "INGRESOS|"
             + this.document.getFieldString("MC_HORAANUL").substring(0, 5) + "|"
             + "CANCELACION " + strRazonSocial + "|"
             + "1|"
             + "1|";
             * */
            try {
               objJson.put("PO_TIPO", "INGRESOS");
               objJson.put("PO_POLIZA", intIdPoliza);
               objJson.put("PO_FECHA", this.document.getFieldString("MC_FECHAANUL"));
               objJson.put("PO_HORA", this.document.getFieldString("MC_HORAANUL").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + strRazonSocial));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt("MC_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("MC_TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;/*strCTA_BCO + "|"
             + "0.0|"
             + dblImportePagoDoc + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + strFolio + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", dblImportePagoDoc);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", strFolio);
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            String strItem2 = null;/*strCTA_CTE + "|"
             + dblImportePagoDoc + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + strFolio + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               objJsonPoliDeta.put("PD_DEBE", dblImportePagoDoc);
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", strFolio);
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }

            String strItem3 = null;/* strCTA_IVA_TRAS + "|"
             + "0.0|"
             + this.document.getFieldDouble("MC_IMPUESTO1") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + strFolio + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_TRAS);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("MC_IMPUESTO1"));
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", strFolio);
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            String strItem4 = null;/*strCTA_IVA_COB + "|"
             + this.document.getFieldDouble("MC_IMPUESTO1") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + strFolio + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_COB);
               objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("MC_IMPUESTO1"));
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", strFolio);
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, strItem4, "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR RECIBOS DE COBROS";
      }
   }
   // </editor-fold>
   
   // <editor-fold defaultstate="collapsed" desc="Notas de cargo">
   /**
    * Genera la poliza de una factura
    *
    * @param intId Es el id de la factura
    */
   protected void doPolizaNotasCargo(int intId) {
      this.strResultLast = "OK";
      String strPrefijo = "NCA_";
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_CTE = this.lstCuentas.get(2);
         String strCTA_CTE_RET_ISR = this.lstCuentas.get(3);
         String strCTA_CTE_RET_IVA = this.lstCuentas.get(4);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString("FAC_FECHA") + "|"
             + "DIARIO|"
             + this.document.getFieldString("FAC_HORA").substring(0, 5) + "|"
             + this.document.getFieldString("FAC_RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt(strPrefijo + "EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString(strPrefijo + "FECHA"));
               objJson.put("PO_HORA", this.document.getFieldString(strPrefijo + "HORA").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse(this.document.getFieldString(strPrefijo + "RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt(strPrefijo + "MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble(strPrefijo + "TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }

            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;
            /*strCTA_VTA + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "IMPORTE") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| ";*/
            //Si la cuenta de ventas es a detalle no mandamos la venta global
            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "IMPORTE"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "IMPUESTO1") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| ";*/

            if (this.document.getFieldDouble(strPrefijo + "IMPUESTO1") != 0) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "IMPUESTO1"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem3 = null;/*strCTA_CTE + "|"
             + this.document.getFieldDouble(strPrefijo + "TOTAL") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| "*/;
            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               //Evaluamos si tiene retenciones
               if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "NETO"));
               } else {
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "TOTAL"));
               }
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //RETENCIONES
            if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
               if (document.getFieldDouble("FAC_RETISR") > 0) {
                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE_RET_ISR);
                     objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("FAC_RETISR"));
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("FAC_FOLIO_C"));
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               if (document.getFieldDouble("FAC_RETIVA") > 0) {

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE_RET_IVA);
                     objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("FAC_RETIVA"));
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("FAC_FOLIO_C"));
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR VENTAS";
      }
   }

   /**
    * Aplica la regla de negocio para generar la poliza contable de cancelacion
    * de facturas
    *
    * @param intId Es el id de la operacion cancelada
    */
   protected void doPolizaNotasCargoCancel(int intId) {
      this.strResultLast = "OK";
      String strPrefijo = "NCA_";
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_CTE = this.lstCuentas.get(2);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString(strPrefijo + "FECHAANUL") + "|"
             + "DIARIO|"
             + this.document.getFieldString(strPrefijo + "HORANUL").substring(0, 5) + "|"
             + "CANCELACION " + this.document.getFieldString(strPrefijo + "RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt(strPrefijo + "EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString(strPrefijo + "FECHAANUL"));
               objJson.put("PO_HORA", this.document.getFieldString(strPrefijo + "HORANUL").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + this.document.getFieldString(strPrefijo + "RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt(strPrefijo + "MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble(strPrefijo + "TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;/*strCTA_VTA + "|"
             + this.document.getFieldDouble(strPrefijo + "IMPORTE") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";*/

            //Si la cuenta de ventas es a detalle no mandamos la venta global

            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "IMPORTE"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + this.document.getFieldDouble(strPrefijo + "IMPUESTO1") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
               objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "IMPUESTO1"));
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            String strItem3 = null;/*strCTA_CTE + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "TOTAL") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";
             * */

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "TOTAL"));
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR VENTAS";
      }
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Cuentas por pagar">

   /**
    * Genera la poliza de una cuenta por pagar
    *
    * @param intId Es el id de la factura
    */
   protected void doPolizaCXP(int intId) {
      doPolizaCXP(intId, "", "");
   }
   protected void doPolizaCXP(int intId,String strRazonSocial,String strFolio) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_PROV = this.lstCuentas.get(2);
         String strCTA_PROV_RET_ISR = this.lstCuentas.get(3);
         String strCTA_PROV_RET_IVA = this.lstCuentas.get(4);
         if(strFolio.isEmpty()){
            strFolio = this.document.getFieldString("CXP_FOLIO");
         }
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/


            /*this.document.getFieldString("CXP_FECHA_PROVISION") + "|"
             + "DIARIO|"
             + this.document.getFieldString("CXP_HORA").substring(0, 5) + "|"
             + this.document.getFieldString("CXP_RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt("CXP_EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString("CXP_FECHA_PROVISION"));
               objJson.put("PO_HORA", this.document.getFieldString("CXP_HORA").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse(this.document.getFieldString("CXP_RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt("CXP_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("CXP_PARIDAD"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */

            String strItem1 = null;/*strCTA_VTA + "|"
             + this.document.getFieldDouble("CXP_IMPORTE") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString("CXP_FOLIO") + "| ";*/

            //Si la cuenta de ventas es a detalle no mandamos la venta global
            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("CXP_IMPORTE"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", (this.strRFCProveedor == null ? "" : this.strRFCProveedor));
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT == null ? "" : this.strPV_NOMBREDIOT));
                  objJsonPoliDeta.put("PD_MONTO", this.document.getFieldDouble("CXP_TOTAL"));
                  objJsonPoliDeta.put("PD_UUID", this.document.getFieldString("CXP_UUID"));
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + this.document.getFieldDouble("CXP_IMPUESTO1") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString("CXP_FOLIO") + "| ";*/

            if (this.document.getFieldDouble("CXP_IMPUESTO1") != 0) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("CXP_IMPUESTO1"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", (this.strRFCProveedor == null ? "" : this.strRFCProveedor));
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT == null ? "" : this.strPV_NOMBREDIOT));
                  objJsonPoliDeta.put("PD_MONTO", this.document.getFieldDouble("CXP_TOTAL"));
                  objJsonPoliDeta.put("PD_UUID", this.document.getFieldString("CXP_UUID"));
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            //Evaluamos retenciones
            String strItem3 = "";
            if (document.getFieldDouble("CXP_RETISR") > 0 || document.getFieldDouble("CXP_RETIVA") > 0) {
               strItem3 = null;/*strCTA_PROV + "|"
                + "0.0|"
                + this.document.getFieldDouble("CXP_NETO") + "|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + this.document.getFieldString("CXP_FOLIO") + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_PROV);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("CXP_NETO"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", (this.strRFCProveedor == null ? "" : this.strRFCProveedor));
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT == null ? "" : this.strPV_NOMBREDIOT));
                  objJsonPoliDeta.put("PD_MONTO", this.document.getFieldDouble("CXP_TOTAL"));
                  objJsonPoliDeta.put("PD_UUID", this.document.getFieldString("CXP_UUID"));
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }

            } else {
               strItem3 = null;/*strCTA_PROV + "|"
                + "0.0|"
                + this.document.getFieldDouble("CXP_TOTAL") + "|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + this.document.getFieldString("CXP_FOLIO") + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_PROV);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("CXP_TOTAL"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", (this.strRFCProveedor == null ? "" : this.strRFCProveedor));
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT == null ? "" : this.strPV_NOMBREDIOT));
                  objJsonPoliDeta.put("PD_MONTO", this.document.getFieldDouble("CXP_TOTAL"));
                  objJsonPoliDeta.put("PD_UUID", this.document.getFieldString("CXP_UUID"));
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            log.debug(" " + strItem1 + " " + strItem2 + " " + strItem3 + " " + " ");

            String strItem4 = "";
            String strItem5 = "";
            if (document.getFieldDouble("CXP_RETISR") > 0 || document.getFieldDouble("CXP_RETIVA") > 0) {
               if (document.getFieldDouble("CXP_RETISR") > 0) {
                  strItem4 = null;/*strCTA_PROV_RET_ISR + "|"
                   + "0.0|"
                   + this.document.getFieldDouble("CXP_RETISR") + "|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + this.document.getFieldString("CXP_FOLIO") + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_PROV_RET_ISR);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("CXP_RETISR"));
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", (this.strRFCProveedor == null ? "" : this.strRFCProveedor));
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT == null ? "" : this.strPV_NOMBREDIOT));
                     objJsonPoliDeta.put("PD_MONTO", this.document.getFieldDouble("CXP_TOTAL"));
                     objJsonPoliDeta.put("PD_UUID", this.document.getFieldString("CXP_UUID"));
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               if (document.getFieldDouble("CXP_RETIVA") > 0) {
                  strItem5 = null;/*strCTA_PROV_RET_IVA + "|"
                   + "0.0|"
                   + this.document.getFieldDouble("CXP_RETIVA") + "|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + this.document.getFieldString("CXP_FOLIO") + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_PROV_RET_IVA);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("CXP_RETIVA"));
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", (this.strRFCProveedor == null ? "" : this.strRFCProveedor));
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT == null ? "" : this.strPV_NOMBREDIOT));
                     objJsonPoliDeta.put("PD_MONTO", this.document.getFieldDouble("CXP_TOTAL"));
                     objJsonPoliDeta.put("PD_UUID", this.document.getFieldString("CXP_UUID"));
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, strItem4, strItem5, "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR CUENTAS POR PAGAR";
      }
   }

   /**
    * Aplica la regla de negocio para generar la poliza contable de cancelacion
    * de facturas
    *
    * @param intId Es el id de la operacion cancelada
    */
   protected void doPolizaCXPCancel(int intId) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_PROV = this.lstCuentas.get(2);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString("CXP_FECHAANUL") + "|"
             + "DIARIO|"
             + this.document.getFieldString("CXP_HORANUL") + "|"
             + "CANCELACION " + this.document.getFieldString("CXP_RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt("CXP_EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString("CXP_FECHAANUL"));
               objJson.put("PO_HORA", this.document.getFieldString("CXP_HORANUL").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + this.document.getFieldString("CXP_RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt("CXP_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("CXP_PARIDAD"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;/*strCTA_VTA + "|"
             + this.document.getFieldDouble("CXP_IMPORTE") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString("CXP_FOLIO") + "| ";*/

            //Si la cuenta de ventas es a detalle no mandamos la venta global

            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("CXP_IMPORTE"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("CXP_FOLIO"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + this.document.getFieldDouble("CXP_IMPUESTO1") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString("CXP_FOLIO") + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
               objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("CXP_IMPUESTO1"));
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("CXP_FOLIO"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            String strItem3 = null;/*strCTA_PROV + "|"
             + "0.0|"
             + this.document.getFieldDouble("CXP_TOTAL") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString("CXP_FOLIO") + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_PROV);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("CXP_TOTAL"));
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("CXP_FOLIO"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR CUENTAS POR PAGAR";
      }
   }

   /**
    * Genera la poliza de un pago realizado a proveedores o acreedores
    *
    * @param intId Es el id del pago
    * @param strRazonSocial Es la razon social del proveedor
    * @param strFolio Es el folio del pago
    */
   protected void doPolizaPagos(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 4 elementos(Bancos,Cte,IVA,IVA COBRADO)
      int intNumOblig = 3;
      if (this.bolEsAnticipo || this.bolUsaAnticipo) {
         intNumOblig = 2;
      }
      //Importe del pago
      double dblImportePagoDoc = document.getFieldDouble("MP_ABONO");
      //Asignamos importe de pago global
      if (this.dblTmp2 != 0) {
         dblImportePagoDoc = this.dblTmp2;
      }
      log.debug("Campos obligatorios ... " + intNumOblig);
      log.debug("Cuantos campos son? ... " + this.lstCuentas.size());

      // <editor-fold defaultstate="collapsed" desc="Obtenemos el id de la poliza contable">
      int intIdPoliza = this.document.getFieldInt("MP_EXEC_INTER_CP");
      if (this.pagosMasivosCtas != null) {
         intIdPoliza = this.intIdPoliMasivo;
      }
      // </editor-fold>

      if (this.lstCuentas.size() >= intNumOblig) {
         String strCTA_BCO = this.lstCuentas.get(0);
         String strCTA_CTE = this.lstCuentas.get(1);
         String strCTA_IVA_TRAS = "";
         String strCTA_IVA_COB = "";
         // <editor-fold defaultstate="collapsed" desc="Los anticipos no tienen iva">
         if (!this.bolEsAnticipo && !this.bolUsaAnticipo) {
            strCTA_IVA_TRAS = this.lstCuentas.get(2);
            strCTA_IVA_COB = this.lstCuentas.get(3);
         } else {

            if (this.bolUsaAnticipo) {
               strCTA_IVA_TRAS = this.lstCuentas.get(2);
               strCTA_IVA_COB = this.lstCuentas.get(3);
            }

         }
         // </editor-fold>
         log.debug("strCTA_BCO:" + strCTA_BCO);
         log.debug("strCTA_CTE:" + strCTA_CTE);
         log.debug("strCTA_IVA_TRAS:" + strCTA_IVA_TRAS);
         log.debug("strCTA_IVA_COB:" + strCTA_IVA_COB);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/


            /*this.document.getFieldString("MP_FECHA") + "|"
             + "EGRESOS|"
             + this.document.getFieldString("MP_HORA").substring(0, 5) + "|"
             + strRazonSocial + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "EGRESOS");
               objJson.put("PO_POLIZA", intIdPoliza);
               objJson.put("PO_FECHA", this.document.getFieldString("MP_FECHA"));
               objJson.put("PO_HORA", this.document.getFieldString("MP_HORA").substring(0, 5));
               if(!this.document.getFieldString("MP_NOTAS").isEmpty()){
                  objJson.put("PO_CONCEPTO", this.json.parse(this.document.getFieldString("MP_NOTAS")));
               }else{
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
               }
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", dblImportePagoDoc);
               objJson.put("PO_BANCO_ORIGEN", (this.strBancoOrigen != null ? this.strBancoOrigen : ""));
               objJson.put("PO_BANCO_DESTINO", (this.strBancoDestino != null ? this.strBancoDestino : ""));
               objJson.put("PO_RFC_TERCEROS", (this.strRfcTerceros != null ? this.strRfcTerceros : ""));
               objJson.put("PO_CUENTA_ORIGEN", (this.strCuentaOrigen != null ? this.strCuentaOrigen : ""));
               objJson.put("PO_CUENTA_DESTINO", (this.strCuentaDestino != null ? this.strCuentaDestino : ""));
               objJson.put("PO_BENEFICIARIO", this.json.parse(strRazonSocial));
               objJson.put("PO_RFC", (this.strRfcDIOT != null ? this.json.parse(this.strRfcDIOT) : ""));
               objJson.put("PO_MONEDA", this.document.getFieldInt("MP_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("MP_TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //Solo si pagaron con cheque
            if (this.bolUsaCheque) {
               /*this.document.getFieldString("MP_FECHA") + "|"
                + "CHEQUE|"
                + this.document.getFieldString("MP_HORA").substring(0, 5) + "|"
                + strRazonSocial + "|"
                + "1|"
                + "1|";*/
               //+ "1|" + this.strNumCheque + "|";
               try {
                  objJson.put("PO_TIPO", "CHEQUES");
                  objJson.put("PO_POLIZA", intIdPoliza);
                  objJson.put("PO_FECHA", this.document.getFieldString("MP_FECHA"));
                  objJson.put("PO_HORA", this.document.getFieldString("MP_HORA").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", this.strNumCheque);
                  objJson.put("PO_MONTO", dblImportePagoDoc);
                  objJson.put("PO_BANCO_ORIGEN", (this.strBancoOrigen != null ? this.strBancoOrigen : ""));
                  objJson.put("PO_BANCO_DESTINO", "");
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", (this.strCuentaOrigen != null ? this.strCuentaOrigen : ""));
                  objJson.put("PO_CUENTA_DESTINO", "");
                  objJson.put("PO_BENEFICIARIO", this.json.parse(strRazonSocial));
                  objJson.put("PO_RFC", (this.strRfcDIOT != null ? this.json.parse(this.strRfcDIOT) : ""));
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MP_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MP_TASAPESO"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            if (!this.bolEsAnticipo && !this.bolUsaAnticipo) {
               // <editor-fold defaultstate="collapsed" desc="Pago normal">
               String strItem1 = null;/*strCTA_BCO + "|"
                + "0.0|"
                + dblImportePagoDoc + "|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  double dblImporteBancos = dblImportePagoDoc;
                  log.debug("dblImporteBancos sin comision " + dblImporteBancos);
                  if(this.dblTmp4 > 0 ){
                     dblImporteBancos += this.dblTmp4;
                  }
                  log.debug("dblTmp4 " + dblTmp4);
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", dblImporteBancos );
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                  objJsonPoliDeta.put("PD_MONTO", dblImportePagoDoc);
                  objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
               String strItem2 = null;/*strCTA_CTE + "|"
                + dblImportePagoDoc + "|"
                + "0.0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               //Si es un pago masivo no enviamos el movimiento de proveedores

               log.debug("this.pagosMasivosCtas:" + this.pagosMasivosCtas);
               if (this.pagosMasivosCtas != null) {
                  strItem2 = "";
                  if (this.lstCuentasAG == null) {
                     this.lstCuentasAG = new ArrayList<PoliCtas>();
                  }
                  Iterator<MovProveedor> it = this.pagosMasivosCtas.lstPagos.iterator();
                  while (it.hasNext()) {
                     MovProveedor deta = it.next();
                     //Obtenemos el folio de referencia de la factura cobrada
                     DatosPagos datosPago = this.getFolioReferenciaPago(deta);
                     String strFolioRef = datosPago.strFolio;
                     if (datosPago.strFolio == null) {
                        strFolioRef = strFolio;
                     }
                     double dblAplicaGob = deta.getCta_prov().getFieldDouble("MP_ABONO");
                     if (this.bolUsaComplementoAjuste) {
                        dblAplicaGob = this.pagosMasivosCtas.getDblFactorConv() * dblAplicaGob;
                        log.debug("Usa complemento ajuste " + dblComplementoAjuste);
                     }

                     //Movimiento de proveedores por documento
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strCTA_CTE);
                     pol.setBolEsCargo(true);
                     pol.setDblImporte(dblAplicaGob);
                     pol.setStrFolioRef(strFolioRef);
                     pol.setStrRFC((this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                     pol.setDblMonto(deta.getCta_prov().getFieldDouble("MP_ABONO"));
                     pol.setStrUUID(datosPago.getStrUUID());
                     if (datosPago.getStrUUID() != null) {
                        if (datosPago.getStrUUID().length() > 0) {
                           pol.setStrFolioRef(datosPago.getStrUUID().substring(datosPago.getStrUUID().length() - 4, datosPago.getStrUUID().length()));
                        }
                     }

                     lstCuentasAG.add(pol);
                  }
               } else {

                  try {
                     double dblAplicaInd = dblImportePagoDoc;
                     if (this.bolUsaComplementoAjuste) {
                        dblAplicaInd = dblComplementoAjuste;
                        log.debug("Usa complemento ajuste " + dblComplementoAjuste);
                     }

                     log.debug("this.individual:");
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                     objJsonPoliDeta.put("PD_DEBE", dblAplicaInd);
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                     objJsonPoliDeta.put("PD_MONTO", dblImportePagoDoc);
                     objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               //Solo si hay impuestos
               if (this.document.getFieldDouble("MP_IMPUESTO1") != 0) {
                  double dblImpuestoAplica = this.document.getFieldDouble("MP_IMPUESTO1");
                  //Impuesto global
                  if (this.dblTmp3 > 0) {
                     dblImpuestoAplica = this.dblTmp3;
                  }
                  String strItem3 = null;/*strCTA_IVA_TRAS + "|"
                   + "0.0|"
                   + this.document.getFieldDouble("MP_IMPUESTO1") + "|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + strFolio + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_TRAS);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", dblImpuestoAplica);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                     objJsonPoliDeta.put("PD_MONTO", document.getFieldDouble("MP_ABONO"));
                     objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
                  double dblBaseIVA = dblImportePagoDoc / (1 + (this.document.getFieldDouble("MP_TASAIMPUESTO1") / 100));
                  String strItem4 = null;/*strCTA_IVA_COB + "|"
                   + this.document.getFieldDouble("MP_IMPUESTO1") + "|"
                   + "0.0|"
                   + "0|"
                   + (this.document.getFieldDouble("MP_IMPUESTO1") == 0 ? 0 : 1) + "|"//APLICA IVA
                   + "0|"
                   + (this.document.getFieldDouble("MP_IMPUESTO1") == 0 ? 0 : this.document.getFieldInt("MP_TASAIMPUESTO1")) + "|"//TASA IVA
                   + "0|"
                   + "1|"//ES PROV
                   + strFolio + "|"
                   + " |" //nOTAS
                   + (this.strRfcDIOT != null ? this.strRfcDIOT : "") + "|" //RFC
                   + dblBaseIVA + "|" //strImporteBase
                   + 0 + "|" //strRetencion
                   ;*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_COB);
                     objJsonPoliDeta.put("PD_DEBE", dblImpuestoAplica);
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", (dblImpuestoAplica == 0 ? 0 : 1));
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", (dblImpuestoAplica == 0 ? 0 : this.document.getFieldDouble("MP_TASAIMPUESTO1")));
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 1);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                     objJsonPoliDeta.put("Base", dblBaseIVA);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                     objJsonPoliDeta.put("PD_MONTO", dblImportePagoDoc);
                     objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }

               //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
               this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, "", "", "", "", "", "", "", "");
               // </editor-fold>
            } else {
               // <editor-fold defaultstate="collapsed" desc="Anticipo">
               //Reglas para anticipos
               if (this.bolEsAnticipo) {
                  String strItem1 = null;/*strCTA_BCO + "|"
                   + "0.0|"
                   + dblImportePagoDoc + "|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + strFolio + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     double dblImporteBancos = dblImportePagoDoc;
                     log.debug("dblImporteBancos sin comision " + dblImporteBancos);
                     if(this.dblTmp4 > 0 ){
                        dblImporteBancos += this.dblTmp4;
                     }
                     log.debug("dblTmp4 " + dblTmp4);
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", dblImporteBancos);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
                  String strItem2 = null;/*strCTA_CTE + "|"
                   + dblImportePagoDoc + "|"
                   + "0.0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + "0|"
                   + strFolio + "| ";*/

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                     //Evaluamos el importe esta en moneda EUROS o DLLS de otro pais
                     if (this.dblTmp1 != 0) {
                        objJsonPoliDeta.put("PD_DEBE", this.dblTmp1);
                     } else {
                        objJsonPoliDeta.put("PD_DEBE", dblImportePagoDoc);
                     }
                     objJsonPoliDeta.put("PD_HABER", 0);
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", strFolio);
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }

                  //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
                  this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, "", "", "", "", "", "", "", "");
                  // </editor-fold>
               } else {

                  //Reglas para uso de anticipos
                  if (this.bolUsaAnticipo) {
                     // <editor-fold defaultstate="collapsed" desc="Uso de anticipos">
                     /*
                      this.document.getFieldString("MP_FECHA") + "|"
                      + "DIARIO|"
                      + this.document.getFieldString("MP_HORA").substring(0, 5) + "|"
                      + strRazonSocial + "|"
                      + "1|"
                      + "1|";
                      * */
                     try {
                        objJson.put("PO_TIPO", "DIARIO");
                        objJson.put("PO_POLIZA", intIdPoliza);
                        objJson.put("PO_FECHA", this.document.getFieldString("MP_FECHA"));
                        objJson.put("PO_HORA", this.document.getFieldString("MP_HORA").substring(0, 5));
                        objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                        objJson.put("PO_USUARIO", 1);
                        objJson.put("PO_APLIC", 1);
                        objJson.put("PO_CHEQUE", "");
                        objJson.put("PO_MONTO", 0);
                        objJson.put("PO_BANCO_ORIGEN", "");
                        objJson.put("PO_BANCO_DESTINO", "");
                        objJson.put("PO_RFC_TERCEROS", "");
                        objJson.put("PO_CUENTA_ORIGEN", "");
                        objJson.put("PO_CUENTA_DESTINO", "");
                        objJson.put("PO_BENEFICIARIO", "");
                        objJson.put("PO_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                        objJson.put("PO_MONEDA", this.document.getFieldInt("MP_MONEDA"));
                        objJson.put("PO_PARIDAD", this.document.getFieldDouble("MP_TASAPESO"));
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     String strItem1 = null;/*strCTA_BCO + "|"
                      + "0.0|"
                      + dblImportePagoDoc + "|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + "0|"
                      + strFolio + "| ";*/

                     try {
                        JSONObject objJsonPoliDeta = new JSONObject();
                        objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                        objJsonPoliDeta.put("PD_DEBE", 0);
                        objJsonPoliDeta.put("PD_HABER", dblImportePagoDoc);
                        objJsonPoliDeta.put("PD_APLICAIETU", 0);
                        objJsonPoliDeta.put("PD_APLICAIVA", 0);
                        objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                        objJsonPoliDeta.put("PD_TASA", 0);
                        objJsonPoliDeta.put("PD_RUBRO", 0);
                        objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                        objJsonPoliDeta.put("PD_FOLIO", strFolio);
                        objJsonPoliDeta.put("PD_NOTAS", "");
                        objJsonPoliDeta.put("RFCDiot", "");
                        objJsonPoliDeta.put("Base", 0);
                        objJsonPoliDeta.put("Retencion", 0);
                        objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                        objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                        objJsonPoliDeta.put("PD_MONTO", document.getFieldDouble("MP_ABONO"));
                        objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                        listPolizas.put(objJsonPoliDeta);
                     } catch (JSONException ex) {
                        log.error(ex.getMessage());
                     }
                     //Si es un pago masivo no enviamos el movimiento de proveedores
                     if (this.pagosMasivosCtas != null) {
                        if (this.lstCuentasAG == null) {
                           this.lstCuentasAG = new ArrayList<PoliCtas>();
                        }
                        Iterator<MovProveedor> it = this.pagosMasivosCtas.lstPagos.iterator();
                        while (it.hasNext()) {
                           MovProveedor deta = it.next();
                           //Obtenemos el folio de referencia de la factura cobrada
                           DatosPagos datosPago = this.getFolioReferenciaPago(deta);
                           String strFolioRef = datosPago.strFolio;
                           if (datosPago.strFolio == null) {
                              strFolioRef = strFolio;
                           }
                           //Movimiento de proveedores por documento
                           PoliCtas pol = new PoliCtas();
                           pol.setStrCuenta(strCTA_CTE);
                           pol.setBolEsCargo(true);
                           pol.setDblImporte(deta.getCta_prov().getFieldDouble("MP_ABONO"));
                           pol.setStrFolioRef(strFolioRef);
                           pol.setStrRFC((this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                           pol.setDblMonto(deta.getCta_prov().getFieldDouble("MP_ABONO"));
                           pol.setStrUUID(datosPago.getStrUUID());
                           lstCuentasAG.add(pol);
                        }
                     } else {
                        //No es masivo
                        /*strCTA_CTE + "|"
                         + dblImportePagoDoc + "|"
                         + "0.0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + strFolio + "| ";*/
                        try {
                           JSONObject objJsonPoliDeta = new JSONObject();
                           objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
                           objJsonPoliDeta.put("PD_DEBE", dblImportePagoDoc);
                           objJsonPoliDeta.put("PD_HABER", 0);
                           objJsonPoliDeta.put("PD_APLICAIETU", 0);
                           objJsonPoliDeta.put("PD_APLICAIVA", 0);
                           objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                           objJsonPoliDeta.put("PD_TASA", 0);
                           objJsonPoliDeta.put("PD_RUBRO", 0);
                           objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                           objJsonPoliDeta.put("PD_FOLIO", strFolio);
                           objJsonPoliDeta.put("PD_NOTAS", "");
                           objJsonPoliDeta.put("RFCDiot", "");
                           objJsonPoliDeta.put("Base", 0);
                           objJsonPoliDeta.put("Retencion", 0);
                           objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                           objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                           objJsonPoliDeta.put("PD_MONTO", document.getFieldDouble("MP_ABONO"));
                           objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                           listPolizas.put(objJsonPoliDeta);
                        } catch (JSONException ex) {
                           log.error(ex.getMessage());
                        }
                     }
                     if (this.document.getFieldDouble("MP_IMPUESTO1") != 0) {
                        String strItem3 = null;/*strCTA_IVA_TRAS + "|"
                         + "0.0|"
                         + this.document.getFieldDouble("MP_IMPUESTO1") + "|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + "0|"
                         + strFolio + "| ";*/

                        try {
                           JSONObject objJsonPoliDeta = new JSONObject();
                           objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_TRAS);
                           objJsonPoliDeta.put("PD_DEBE", 0);
                           objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("MP_IMPUESTO1"));
                           objJsonPoliDeta.put("PD_APLICAIETU", 0);
                           objJsonPoliDeta.put("PD_APLICAIVA", 0);
                           objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                           objJsonPoliDeta.put("PD_TASA", 0);
                           objJsonPoliDeta.put("PD_RUBRO", 0);
                           objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                           objJsonPoliDeta.put("PD_FOLIO", strFolio);
                           objJsonPoliDeta.put("PD_NOTAS", "");
                           objJsonPoliDeta.put("RFCDiot", "");
                           objJsonPoliDeta.put("Base", 0);
                           objJsonPoliDeta.put("Retencion", 0);
                           objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                           objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                           objJsonPoliDeta.put("PD_MONTO", document.getFieldDouble("MP_ABONO"));
                           objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                           listPolizas.put(objJsonPoliDeta);
                        } catch (JSONException ex) {
                           log.error("strCTA_IVA_TRAS:" + ex.getMessage());
                        }
                        double dblBaseIVA = this.document.getFieldDouble("MP_IMPUESTO1") / ( (this.document.getFieldDouble("MP_TASAIMPUESTO1") / 100));
                        String strItem4 = null;/*strCTA_IVA_COB + "|"
                         + this.document.getFieldDouble("MP_IMPUESTO1") + "|"
                         + "0.0|"
                         + "0|"
                         + (this.document.getFieldDouble("MP_IMPUESTO1") == 0 ? 0 : 1) + "|"//APLICA IVA
                         + "0|"
                         + (this.document.getFieldDouble("MP_IMPUESTO1") == 0 ? 0 : this.document.getFieldInt("MP_TASAIMPUESTO1")) + "|"//TASA IVA
                         + "0|"
                         + "1|"//ES PROV
                         + strFolio + "|"
                         + " |" //nOTAS
                         + (this.strRfcDIOT != null ? this.strRfcDIOT : "") + "|" //RFC
                         + dblBaseIVA + "|" //strImporteBase
                         + 0 + "|" //strRetencion
                         ;*/

                        try {
                           JSONObject objJsonPoliDeta = new JSONObject();
                           objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA_COB);
                           objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("MP_IMPUESTO1"));
                           objJsonPoliDeta.put("PD_HABER", 0);
                           objJsonPoliDeta.put("PD_APLICAIETU", 0);
                           objJsonPoliDeta.put("PD_APLICAIVA", (this.document.getFieldDouble("MP_IMPUESTO1") == 0 ? 0 : 1));
                           objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                           objJsonPoliDeta.put("PD_TASA", (this.document.getFieldDouble("MP_IMPUESTO1") == 0 ? 0 : this.document.getFieldDouble("MP_TASAIMPUESTO1")));
                           objJsonPoliDeta.put("PD_RUBRO", 0);
                           objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 1);
                           objJsonPoliDeta.put("PD_FOLIO", strFolio);
                           objJsonPoliDeta.put("PD_NOTAS", "");
                           objJsonPoliDeta.put("RFCDiot", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                           objJsonPoliDeta.put("Base", dblBaseIVA);
                           objJsonPoliDeta.put("Retencion", 0);
                           objJsonPoliDeta.put("PD_RFC", (this.strRfcDIOT != null ? this.strRfcDIOT : ""));
                           objJsonPoliDeta.put("PD_NOMBRE_PROV", (this.strPV_NOMBREDIOT != null ? this.strPV_NOMBREDIOT : ""));
                           objJsonPoliDeta.put("PD_MONTO", document.getFieldDouble("MP_ABONO"));
                           objJsonPoliDeta.put("PD_UUID", (this.strCXP_UUID == null ? "" : this.strCXP_UUID));
                           listPolizas.put(objJsonPoliDeta);
                        } catch (JSONException ex) {
                           log.error("strCTA_IVA_COB " + ex.getMessage());
                           ex.printStackTrace();
                        }

                     }

                     //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
                     this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, "", "", "", "", "", "", "", "", "");
                     // </editor-fold>
                  }
               }
            }

         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR RECIBOS DE COBROS";
      }
   }

   /**
    * Genera la poliza para cancelar un cobro realizado
    *
    * @param intId Es el id del pago
    * @param strRazonSocial Es la razon social del cliente
    * @param strFolio Es el folio del pago
    */
   protected void doPolizaPagosCancel(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 4 elementos(Bancos,Cte,IVA,IVA COBRADO)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_BCO = this.lstCuentas.get(0);
         String strCTA_CTE = this.lstCuentas.get(1);
         String strCTA_IVA_TRAS = this.lstCuentas.get(2);
         String strCTA_IVA_COB = this.lstCuentas.get(3);
         //Importe del pago
         double dblImportePagoDoc = document.getFieldDouble("MP_ABONO");
         //Asignamos importe de pago global
         if (this.dblTmp2 != 0) {
            dblImportePagoDoc = this.dblTmp2;
         }
         // <editor-fold defaultstate="collapsed" desc="Obtenemos el id de la poliza contable">
         int intIdPoliza = this.document.getFieldInt("MP_EXEC_INTER_CP");
         if (this.pagosMasivosCtas != null) {
            intIdPoliza = this.intIdPoliMasivo;
         }
         // </editor-fold>
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/


            /*this.document.getFieldString("MP_FECHAANUL") + "|"
             + "EGRESOS|"
             + this.document.getFieldString("MP_HORAANUL").substring(0, 5) + "|"
             + "CANCELACION " + strRazonSocial + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "EGRESOS");
               objJson.put("PO_POLIZA", intIdPoliza);
               objJson.put("PO_FECHA", this.document.getFieldString("MP_FECHAANUL"));
               objJson.put("PO_HORA", this.document.getFieldString("MP_HORAANUL").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + strRazonSocial));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt("MP_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("MP_TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            if (this.bolUsaCheque) {
               /*
                this.document.getFieldString("MP_FECHAANUL") + "|"
                + "CHEQUE|"
                + this.document.getFieldString("MP_HORAANUL").substring(0, 5) + "|"
                + "CANCELACION " + strRazonSocial + "|"
                + "1|"
                + "1|";
                * */
               try {
                  objJson.put("PO_TIPO", "CHEQUES");
                  objJson.put("PO_POLIZA", intIdPoliza);
                  objJson.put("PO_FECHA", this.document.getFieldString("MP_FECHAANUL"));
                  objJson.put("PO_HORA", this.document.getFieldString("MP_HORAANUL").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", "");
                  objJson.put("PO_MONTO", 0);
                  objJson.put("PO_BANCO_ORIGEN", "");
                  objJson.put("PO_BANCO_DESTINO", "");
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", "");
                  objJson.put("PO_CUENTA_DESTINO", "");
                  objJson.put("PO_BENEFICIARIO", "");
                  objJson.put("PO_RFC", "");
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MP_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MP_TASAPESO"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = strCTA_BCO + "|"
                    + "0.0|"
                    + dblImportePagoDoc + "|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + strFolio + "| ";

            String strItem2 = strCTA_CTE + "|"
                    + dblImportePagoDoc + "|"
                    + "0.0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + strFolio + "| ";
            String strItem3 = strCTA_IVA_TRAS + "|"
                    + "0.0|"
                    + this.document.getFieldDouble("MP_IMPUESTO1") + "|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + strFolio + "| ";
            String strItem4 = strCTA_IVA_COB + "|"
                    + this.document.getFieldDouble("MP_IMPUESTO1") + "|"
                    + "0.0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + "0|"
                    + strFolio + "| ";
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, strItem4, "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR RECIBOS DE COBROS";
      }
   }
   // </editor-fold>

   private void doPolizaBcos(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 2 elementos(Bancos,centro de gastos)
      if (this.lstCuentas.size() >= 1) {
         String strCTA_BCO = this.lstCuentas.get(0);
//         String strCTA_GASTO = this.lstCuentas.get(1);
//         String strCTA_IVA_TRAS = this.lstCuentas.get(2);
//         String strCTA_IVA_COB = this.lstCuentas.get(3);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            if (strRazonSocial.isEmpty()) {
               strRazonSocial = strFolio;
            }
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/

            /*this.document.getFieldString("MCB_FECHA") + "|"
             + "EGRESOS|"
             + this.document.getFieldString("MCB_HORA").substring(0, 5) + "|"
             + strRazonSocial + "|"
             + "1|"
             + "1|";*/
            if (document.getFieldDouble("MCB_RETIRO") > 0 && !this.bolUsaCheque) {
               try {
                  objJson.put("PO_TIPO", "EGRESOS");
                  objJson.put("PO_POLIZA", this.document.getFieldInt("MCB_EXEC_INTER_CP"));
                  objJson.put("PO_FECHA", this.document.getFieldString("MCB_FECHA"));
                  objJson.put("PO_HORA", this.document.getFieldString("MCB_HORA").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", "");
                  objJson.put("PO_MONTO", document.getFieldDouble("MCB_RETIRO"));
                  objJson.put("PO_BANCO_ORIGEN", this.strBancoOrigen);
                  objJson.put("PO_BANCO_DESTINO", this.strBancoDestino);
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", this.strCuentaOrigen);
                  objJson.put("PO_CUENTA_DESTINO", this.strCuentaDestino);
                  objJson.put("PO_BENEFICIARIO", this.json.parse(this.document.getFieldString("MCB_BENEFICIARIO")));
                  objJson.put("PO_RFC", (this.strRfcDIOT != null ? this.json.parse(this.strRfcDIOT) : ""));
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MCB_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MCB_PARIDAD"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            if (document.getFieldDouble("MCB_DEPOSITO") > 0) {
               /*this.document.getFieldString("MCB_FECHA") + "|"
                + "INGRESOS|"
                + this.document.getFieldString("MCB_HORA").substring(0, 5) + "|"
                + strRazonSocial + "|"
                + "1|"
                + "1|";*/
               try {
                  objJson.put("PO_TIPO", "INGRESOS");
                  objJson.put("PO_POLIZA", this.document.getFieldInt("MCB_EXEC_INTER_CP"));
                  objJson.put("PO_FECHA", this.document.getFieldString("MCB_FECHA"));
                  objJson.put("PO_HORA", this.document.getFieldString("MCB_HORA").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", "");
                  objJson.put("PO_MONTO", 0);
                  objJson.put("PO_BANCO_ORIGEN", "");
                  objJson.put("PO_BANCO_DESTINO", "");
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", "");
                  objJson.put("PO_CUENTA_DESTINO", "");
                  objJson.put("PO_BENEFICIARIO", "");
                  objJson.put("PO_RFC", "");
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MCB_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MCB_PARIDAD"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            if (document.getFieldDouble("MCB_RETIRO") > 0 && this.bolUsaCheque) {
               /* this.document.getFieldString("MCB_FECHA") + "|"
                + "CHEQUE|"
                + this.document.getFieldString("MCB_HORA").substring(0, 5) + "|"
                + strRazonSocial + "|"
                + "1|"
                + "1|";
                * */
               try {
                  objJson.put("PO_TIPO", "CHEQUES");
                  objJson.put("PO_POLIZA", this.document.getFieldInt("MCB_EXEC_INTER_CP"));
                  objJson.put("PO_FECHA", this.document.getFieldString("MCB_FECHA"));
                  objJson.put("PO_HORA", this.document.getFieldString("MCB_HORA").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", this.strNumCheque);
                  objJson.put("PO_MONTO", document.getFieldDouble("MCB_RETIRO"));
                  objJson.put("PO_BANCO_ORIGEN", this.strBancoOrigen);
                  objJson.put("PO_BANCO_DESTINO", "");
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", this.strCuentaOrigen);
                  objJson.put("PO_CUENTA_DESTINO", "");
                  objJson.put("PO_BENEFICIARIO", this.json.parse(this.document.getFieldString("MCB_BENEFICIARIO")));
                  objJson.put("PO_RFC", (this.strRfcDIOT != null ? this.json.parse(this.strRfcDIOT) : ""));
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MCB_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MCB_PARIDAD"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;
            if (document.getFieldDouble("MCB_DEPOSITO") > 0) {
               strItem1 = null;/*strCTA_BCO + "|"
                + this.document.getFieldDouble("MCB_DEPOSITO") + "|"
                + "0.0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("MCB_DEPOSITO"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", "");
                  objJsonPoliDeta.put("PD_NOTAS", strFolio);
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            } else {
               /*strCTA_BCO + "|"
                + "0.0|"
                + this.document.getFieldDouble("MCB_RETIRO") + "|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| ";*/
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("MCB_RETIRO"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", "");
                  objJsonPoliDeta.put("PD_NOTAS", strFolio);
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, "", "", "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR RECIBOS DE COBROS";
      }
   }

   private void doPolizaBcosCancel(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";
      //Validamos que el arrayList tenga 4 elementos(Bancos,Cte,IVA,IVA COBRADO)
      if (this.lstCuentas.size() >= 1) {
         String strCTA_BCO = this.lstCuentas.get(0);
//         String strCTA_CTE = this.lstCuentas.get(1);
//         String strCTA_IVA_TRAS = this.lstCuentas.get(2);
//         String strCTA_IVA_COB = this.lstCuentas.get(3);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/


            /*this.document.getFieldString("MP_FECHA") + "|"
             + "EGRESOS|"
             + this.document.getFieldString("MP_HORA").substring(0, 5) + "|"
             + strRazonSocial + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "EGRESOS");
               objJson.put("PO_POLIZA", this.document.getFieldInt("MCB_EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString("MCB_FECHA"));
               objJson.put("PO_HORA", this.document.getFieldString("MCB_HORA").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt("MCB_MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble("MCB_PARIDAD"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            if (document.getFieldDouble("MCB_DEPOSITO") > 0) {
               /*this.document.getFieldString("MCB_FECHA") + "|"
                + "INGRESOS|"
                + this.document.getFieldString("MCB_HORA").substring(0, 5) + "|"
                + strRazonSocial + "|"
                + "1|"
                + "1|";*/
               try {
                  objJson.put("PO_TIPO", "INGRESOS");
                  objJson.put("PO_POLIZA", this.document.getFieldInt("MCB_EXEC_INTER_CP"));
                  objJson.put("PO_FECHA", this.document.getFieldString("MCB_FECHA"));
                  objJson.put("PO_HORA", this.document.getFieldString("MCB_HORA").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", "");
                  objJson.put("PO_MONTO", 0);
                  objJson.put("PO_BANCO_ORIGEN", "");
                  objJson.put("PO_BANCO_DESTINO", "");
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", "");
                  objJson.put("PO_CUENTA_DESTINO", "");
                  objJson.put("PO_BENEFICIARIO", "");
                  objJson.put("PO_RFC", "");
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MCB_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MCB_PARIDAD"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            if (this.bolUsaCheque && document.getFieldDouble("MCB_DEPOSITO") == 0) {

               /*this.document.getFieldString("MCB_FECHA") + "|"
                + "CHEQUE|"
                + this.document.getFieldString("MCB_HORA").substring(0, 5) + "|"
                + strRazonSocial + "|"
                + "1|"
                + "1|";*/
               try {
                  objJson.put("PO_TIPO", "CHEQUES");
                  objJson.put("PO_POLIZA", this.document.getFieldInt("MCB_EXEC_INTER_CP"));
                  objJson.put("PO_FECHA", this.document.getFieldString("MCB_FECHA"));
                  objJson.put("PO_HORA", this.document.getFieldString("MCB_HORA").substring(0, 5));
                  objJson.put("PO_CONCEPTO", this.json.parse(strRazonSocial));
                  objJson.put("PO_USUARIO", 1);
                  objJson.put("PO_APLIC", 1);
                  objJson.put("PO_CHEQUE", "");
                  objJson.put("PO_MONTO", 0);
                  objJson.put("PO_BANCO_ORIGEN", "");
                  objJson.put("PO_BANCO_DESTINO", "");
                  objJson.put("PO_RFC_TERCEROS", "");
                  objJson.put("PO_CUENTA_ORIGEN", "");
                  objJson.put("PO_CUENTA_DESTINO", "");
                  objJson.put("PO_BENEFICIARIO", "");
                  objJson.put("PO_RFC", "");
                  objJson.put("PO_MONEDA", this.document.getFieldInt("MCB_MONEDA"));
                  objJson.put("PO_PARIDAD", this.document.getFieldDouble("MCB_PARIDAD"));
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;/*strCTA_BCO + "|"
             + "0.0|"
             + this.document.getFieldDouble("MCB_RETIRO") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + strFolio + "| "*/;
            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("MCB_RETIRO"));
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", strFolio);
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            if (document.getFieldDouble("MCB_DEPOSITO") > 0) {
               strItem1 = null;/*strCTA_BCO + "|"
                + this.document.getFieldDouble("MCB_DEPOSITO") + "|"
                + "0.0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + "0|"
                + strFolio + "| "*/

               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_BCO);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble("MCB_DEPOSITO"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", strFolio);
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, "", "", "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR RECIBOS DE COBROS";
      }
   }

   /**
    * Regresa el folio del movimiento de cobro realizado
    */
   private String getFolioReferenciaCobro(movCliente deta) {
      //Buscamos el folio del documento
      String strFolioRef = null;
      String strSqlFolio = "select f.FAC_FOLIO_C from vta_facturas f where f.FAC_ID = " + deta.getCta_clie().getFieldInt("FAC_ID");
      if (deta.getCta_clie().getFieldInt("TKT_ID") != 0) {
         strSqlFolio = "select t.TKT_FOLIO_C from vta_tickets t where t.TKT_ID = " + deta.getCta_clie().getFieldInt("TKT_ID");
      }
      try {
         ResultSet rs = this.oConn.runQuery(strSqlFolio, true);
         while (rs.next()) {
            if (deta.getCta_clie().getFieldInt("TKT_ID") != 0) {
               strFolioRef = rs.getString("TKT_FOLIO_C");
            } else {
               strFolioRef = rs.getString("FAC_FOLIO_C");
            }
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return strFolioRef;
   }

   /**
    * Regresa el folio del movimiento de pago realizado
    */
   private DatosPagos getFolioReferenciaPago(MovProveedor deta) {
      //Buscamos el folio del documento
//      String strFolioRef = null;
      DatosPagos datosPago = new DatosPagos();
      String strSqlFolio = "select f.CXP_FOLIO,f.CXP_UUID from vta_cxpagar f where f.CXP_ID = " + deta.getCta_prov().getFieldInt("CXP_ID");

      try {
         ResultSet rs = this.oConn.runQuery(strSqlFolio, true);
         while (rs.next()) {
            datosPago.setStrFolio(rs.getString("CXP_FOLIO"));
            datosPago.setStrUUID(rs.getString("CXP_UUID"));
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return datosPago;
   }

   private void doPolizaNominas(int intId, String strRazonSocial, String strFolio) {
      this.strResultLast = "OK";

      /* PO_FECHA Es la fecha de la operacion
       * PO_TIPO Es el tipo de poliza
       * PO_HORA Es la hora de la poliza
       * PO_CONCEPTO Es el concepto
       * PO_USUARIO Es el usuario
       * PO_APLIC Indica si esta aplicada la poliza*/

      /*this.document.getFieldString("MCB_FECHA") + "|"
       + "EGRESOS|"
       + this.document.getFieldString("MCB_HORA").substring(0, 5) + "|"
       + strRazonSocial + "|"
       + "1|"
       + "1|";*/
      try {
         objJson.put("PO_TIPO", "DIARIO");
         objJson.put("PO_POLIZA", this.document.getFieldInt("NOM_EXEC_INTER_CP"));
         objJson.put("PO_FECHA", this.document.getFieldString("NOM_FECHA"));
         objJson.put("PO_HORA", this.document.getFieldString("NOM_HORA").substring(0, 5));
         objJson.put("PO_CONCEPTO", this.json.parse(document.getFieldString("NOM_CONCEPTO")));
         objJson.put("PO_USUARIO", 1);
         objJson.put("PO_APLIC", 1);
         objJson.put("PO_CHEQUE", "");
         objJson.put("PO_MONTO", document.getFieldDouble("NOM_PERCEPCION_TOTAL"));
         objJson.put("PO_BANCO_ORIGEN", "");
         objJson.put("PO_BANCO_DESTINO", "");
         objJson.put("PO_RFC_TERCEROS", "");
         objJson.put("PO_CUENTA_ORIGEN", "");
         objJson.put("PO_CUENTA_DESTINO", "");
         objJson.put("PO_BENEFICIARIO", this.json.parse(this.document.getFieldString("NOM_RAZONSOCIAL")));
         objJson.put("PO_RFC", this.json.parse(this.document.getFieldString("NOM_RFC")));
         objJson.put("PO_MONEDA", this.document.getFieldInt("NOM_MONEDA"));
         objJson.put("PO_PARIDAD", this.document.getFieldDouble("NOM_TASAPESO"));
      } catch (JSONException ex) {
         log.error(ex.getMessage());
      }

      //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
      this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, "", "", "", "", "", "", "", "", "", "");
   }

   private void doPolizaNominasCancel(int intId, String strRazonSocial, String strFolio) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   private void doPolizaNCredito(int intId) {
      this.strResultLast = "OK";
      String strPrefijo = "NC_";
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_CTE = this.lstCuentas.get(2);
         String strCTA_CTE_RET_ISR = this.lstCuentas.get(3);
         String strCTA_CTE_RET_IVA = this.lstCuentas.get(4);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString("FAC_FECHA") + "|"
             + "DIARIO|"
             + this.document.getFieldString("FAC_HORA").substring(0, 5) + "|"
             + this.document.getFieldString("FAC_RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt(strPrefijo + "EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString(strPrefijo + "FECHA"));
               objJson.put("PO_HORA", this.document.getFieldString(strPrefijo + "HORA").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse(this.document.getFieldString(strPrefijo + "RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt(strPrefijo + "MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble(strPrefijo + "TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }

            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;
            /*strCTA_VTA + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "IMPORTE") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| ";*/
            //Si la cuenta de ventas es a detalle no mandamos la venta global
            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "IMPORTE"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "IMPUESTO1") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| ";*/

            if (this.document.getFieldDouble(strPrefijo + "IMPUESTO1") != 0) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
                  objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "IMPUESTO1"));
                  objJsonPoliDeta.put("PD_HABER", 0);
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem3 = null;/*strCTA_CTE + "|"
             + this.document.getFieldDouble(strPrefijo + "TOTAL") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO_C") + "| "*/;
            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               //Evaluamos si tiene retenciones
               if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "NETO"));
               } else {
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "TOTAL"));
               }
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //RETENCIONES
            if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
               if (document.getFieldDouble("FAC_RETISR") > 0) {
                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE_RET_ISR);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("FAC_RETISR"));
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("FAC_FOLIO_C"));
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
               if (document.getFieldDouble("FAC_RETIVA") > 0) {

                  try {
                     JSONObject objJsonPoliDeta = new JSONObject();
                     objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE_RET_IVA);
                     objJsonPoliDeta.put("PD_DEBE", 0);
                     objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble("FAC_RETIVA"));
                     objJsonPoliDeta.put("PD_APLICAIETU", 0);
                     objJsonPoliDeta.put("PD_APLICAIVA", 0);
                     objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                     objJsonPoliDeta.put("PD_TASA", 0);
                     objJsonPoliDeta.put("PD_RUBRO", 0);
                     objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                     objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString("FAC_FOLIO_C"));
                     objJsonPoliDeta.put("PD_NOTAS", "");
                     objJsonPoliDeta.put("RFCDiot", "");
                     objJsonPoliDeta.put("Base", 0);
                     objJsonPoliDeta.put("Retencion", 0);
                     objJsonPoliDeta.put("PD_RFC", "");
                     objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                     objJsonPoliDeta.put("PD_MONTO", 0);
                     objJsonPoliDeta.put("PD_UUID", "");
                     listPolizas.put(objJsonPoliDeta);
                  } catch (JSONException ex) {
                     log.error(ex.getMessage());
                  }
               }
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR VENTAS";
      }
   }

   private void doPolizaNCreditoCancel(int intId) {
      this.strResultLast = "OK";
      String strPrefijo = "NC_";
      //Validamos que el arrayList tenga 3 elementos(CTA_VTA,CT_IVA_CTA_CTE)
      if (this.lstCuentas.size() >= 3) {
         String strCTA_VTA = this.lstCuentas.get(0);
         String strCTA_IVA = this.lstCuentas.get(1);
         String strCTA_CTE = this.lstCuentas.get(2);
         //Obtenemos los campos del documento
         if (this.strResultLast.equals("OK")) {
            /* PO_FECHA Es la fecha de la operacion
             * PO_TIPO Es el tipo de poliza
             * PO_HORA Es la hora de la poliza
             * PO_CONCEPTO Es el concepto
             * PO_USUARIO Es el usuario
             * PO_APLIC Indica si esta aplicada la poliza*/
            /*this.document.getFieldString(strPrefijo + "FECHAANUL") + "|"
             + "DIARIO|"
             + this.document.getFieldString(strPrefijo + "HORANUL").substring(0, 5) + "|"
             + "CANCELACION " + this.document.getFieldString(strPrefijo + "RAZONSOCIAL") + "|"
             + "1|"
             + "1|";*/
            try {
               objJson.put("PO_TIPO", "DIARIO");
               objJson.put("PO_POLIZA", this.document.getFieldInt(strPrefijo + "EXEC_INTER_CP"));
               objJson.put("PO_FECHA", this.document.getFieldString(strPrefijo + "FECHAANUL"));
               objJson.put("PO_HORA", this.document.getFieldString(strPrefijo + "HORANUL").substring(0, 5));
               objJson.put("PO_CONCEPTO", this.json.parse("CANCELACION " + this.document.getFieldString(strPrefijo + "RAZONSOCIAL")));
               objJson.put("PO_USUARIO", 1);
               objJson.put("PO_APLIC", 1);
               objJson.put("PO_CHEQUE", "");
               objJson.put("PO_MONTO", 0);
               objJson.put("PO_BANCO_ORIGEN", "");
               objJson.put("PO_BANCO_DESTINO", "");
               objJson.put("PO_RFC_TERCEROS", "");
               objJson.put("PO_CUENTA_ORIGEN", "");
               objJson.put("PO_CUENTA_DESTINO", "");
               objJson.put("PO_BENEFICIARIO", "");
               objJson.put("PO_RFC", "");
               objJson.put("PO_MONEDA", this.document.getFieldInt(strPrefijo + "MONEDA"));
               objJson.put("PO_PARIDAD", this.document.getFieldDouble(strPrefijo + "TASAPESO"));
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            /*
             * strCuenta Numero de cuenta contable
             * dblDebe Importe en debe
             * dblHaber Importe en haber
             * intAplicaIETU Aplica IETU 0/1
             * intAplicaIVA Aplica IVA 0/1
             * intAplicaIEPS Aplica IEPS 0/1
             * intTasa Es la tasa del iva
             * intRubro Es el id del rubro
             *    1, 'COMPRAS'
             *    2, 'GASTOS'
             *    3, 'INVERSIONES'
             *    4, 'CLIENTES'
             * intClieoProv Indica si es cliente o proveedor 0/1 respectivamente
             * strFolio Es el folio en particular del movimiento
             * strNotas Son las notas de la partida
             */
            String strItem1 = null;/*strCTA_VTA + "|"
             + this.document.getFieldDouble(strPrefijo + "IMPORTE") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";*/

            //Si la cuenta de ventas es a detalle no mandamos la venta global

            if (!this.bolVTA_DETA) {
               try {
                  JSONObject objJsonPoliDeta = new JSONObject();
                  objJsonPoliDeta.put("CC_NUMERO", strCTA_VTA);
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "IMPORTE"));
                  objJsonPoliDeta.put("PD_APLICAIETU", 0);
                  objJsonPoliDeta.put("PD_APLICAIVA", 0);
                  objJsonPoliDeta.put("PD_APLICAIEPS", 0);
                  objJsonPoliDeta.put("PD_TASA", 0);
                  objJsonPoliDeta.put("PD_RUBRO", 0);
                  objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
                  objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
                  objJsonPoliDeta.put("PD_NOTAS", "");
                  objJsonPoliDeta.put("RFCDiot", "");
                  objJsonPoliDeta.put("Base", 0);
                  objJsonPoliDeta.put("Retencion", 0);
                  objJsonPoliDeta.put("PD_RFC", "");
                  objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
                  objJsonPoliDeta.put("PD_MONTO", 0);
                  objJsonPoliDeta.put("PD_UUID", "");
                  listPolizas.put(objJsonPoliDeta);
               } catch (JSONException ex) {
                  log.error(ex.getMessage());
               }
            }
            String strItem2 = null;/*strCTA_IVA + "|"
             + this.document.getFieldDouble(strPrefijo + "IMPUESTO1") + "|"
             + "0.0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";*/

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_IVA);
               objJsonPoliDeta.put("PD_DEBE", 0);
               objJsonPoliDeta.put("PD_HABER", this.document.getFieldDouble(strPrefijo + "IMPUESTO1"));
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            String strItem3 = null;/*strCTA_CTE + "|"
             + "0.0|"
             + this.document.getFieldDouble(strPrefijo + "TOTAL") + "|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + "0|"
             + this.document.getFieldString(strPrefijo + "FOLIO") + "| ";
             * */

            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", strCTA_CTE);
               objJsonPoliDeta.put("PD_DEBE", this.document.getFieldDouble(strPrefijo + "TOTAL"));
               objJsonPoliDeta.put("PD_HABER", 0);
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", this.document.getFieldString(strPrefijo + "FOLIO_C"));
               objJsonPoliDeta.put("PD_NOTAS", "");
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", "");
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", 0);
               objJsonPoliDeta.put("PD_UUID", "");
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
            //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
            this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, strItem1, strItem2, strItem3, "", "", "", "", "", "", "");
         }
      } else {
         this.strResultLast = "ERROR: FALTAN CUENTAS PARA TRANSPORTAR VENTAS";
      }
   }

   /**
    * Genera una poliza manual
    */
   private void doPolizaManual(int intId, String strTipo, String strFecha, String strHora, String strConcepto, int intMoneda, double dblTasaPeso) {
      try {
         objJson.put("PO_TIPO", strTipo);
         objJson.put("PO_POLIZA", intId);
         objJson.put("PO_FECHA", strFecha);
         objJson.put("PO_HORA", strHora);
         objJson.put("PO_CONCEPTO", this.json.parse(strConcepto));
         objJson.put("PO_USUARIO", 1);
         objJson.put("PO_APLIC", 1);
         objJson.put("PO_CHEQUE", "");
         objJson.put("PO_MONTO", 0);
         objJson.put("PO_BANCO_ORIGEN", "");
         objJson.put("PO_BANCO_DESTINO", "");
         objJson.put("PO_RFC_TERCEROS", "");
         objJson.put("PO_CUENTA_ORIGEN", "");
         objJson.put("PO_CUENTA_DESTINO", "");
         objJson.put("PO_BENEFICIARIO", "");
         objJson.put("PO_RFC", "");
         objJson.put("PO_MONEDA", intMoneda);
         objJson.put("PO_PARIDAD", dblTasaPeso);
      } catch (JSONException ex) {
         log.error(ex.getMessage());
      }
      //intCliente, pass, item1, item2, item3, item4, item5, headers, item6, item7, item8, item9, item10
      this.strResultLast = this.doPolizaAuto(this.strUserCte, this.strPassCte, "", "", "", "", "", "", "", "", "", "");
   }

   /**
    * Representa los datos de pagos que regresan
    */
   class DatosPagos {

      private String strFolio;
      private String strUUID;

      public DatosPagos() {
      }

      public String getStrFolio() {
         return strFolio;
      }

      public void setStrFolio(String strFolio) {
         this.strFolio = strFolio;
      }

      public String getStrUUID() {
         return strUUID;
      }

      public void setStrUUID(String strUUID) {
         this.strUUID = strUUID;
      }
   }

   /**
    * Agrega un archivo para enviarse en la contabilidad
    *
    * @param strNombre Es el nombre del archivo
    * @param strPathFile Es el path del archivo
    * @param intTamanio Es el tamaño del archivo
    */
   public void AgregaArchivo(String strNombre, String strPathFile, int intTamanio) {
      try {
         File file = new File(strPathFile);
         //Abrimos el archivo y lo comprimimos
         byte[] fileByte = org.apache.commons.io.FileUtils.readFileToByteArray(file);
         String strCompBase64 = compressString(fileByte);
         if (strCompBase64 != null) {
            ArchivoEnvio archivo = new ArchivoEnvio(strNombre, strCompBase64, intTamanio);
            this.lstFiles.add(archivo);
         }
      } catch (IOException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * At server side, use ZipOutputStream to zip text to byte array, then
    * convert byte array to base64 string, so it can be trasnfered via http
    * request.
    *
    * @param fileByte es el archivo por comprimir
    * @return Regresa Ok si fue exitoso
    * @throws java.io.IOException Es la excepcion
    */
   public static String compressString(byte[] fileByte)
           throws IOException {
      ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
      GZIPOutputStream zos = new GZIPOutputStream(rstBao);
      zos.write(fileByte);
      IOUtils.closeQuietly(zos);

      byte[] bytes = rstBao.toByteArray();
      String strContent = null;
      // In my solr project, I use org.apache.solr.co mmon.util.Base64.
      // return = org.apache.solr.common.util.Base64.byteArrayToBase64(bytes, 0,
      // bytes.length);
      byte[] b64Enc;
      try {
         b64Enc = Base64.encodeBase64(bytes);
         strContent = new String(b64Enc);
      } catch (Exception ex) {
         log.error("ERROR:CertificateException " + ex.getMessage());
      }
      return strContent;
   }

   /**
    * Manda a llamara al webservice
    */
   private String doPolizaAuto(String strCliente, java.lang.String pass, java.lang.String item1, java.lang.String item2, java.lang.String item3, java.lang.String item4, java.lang.String item5,
           java.lang.String item6, java.lang.String item7, java.lang.String item8, java.lang.String item9, java.lang.String item10) {
      //Mostramos parametros si estamos en modo debug
      log.debug("Inicio envio contabilidad...");
      Interfaz.ContabilidadPlus.MySIWEBContaService service = null;
      if (this.strURLServicio != null) {

         //Acomodamos las cuentas especiales o adicionales
         //Recorremos todas las partidas y las acomodamos
         Iterator<PoliCtas> it2 = lstCuentasAG.iterator();
         while (it2.hasNext()) {
            PoliCtas polDeta = it2.next();
            try {
               JSONObject objJsonPoliDeta = new JSONObject();
               objJsonPoliDeta.put("CC_NUMERO", polDeta.getStrCuenta());
               if (polDeta.isBolEsCargo()) {
                  objJsonPoliDeta.put("PD_DEBE", polDeta.getDblImporte());
                  objJsonPoliDeta.put("PD_HABER", 0);

               } else {
                  objJsonPoliDeta.put("PD_DEBE", 0);
                  objJsonPoliDeta.put("PD_HABER", polDeta.getDblImporte());
               }
               objJsonPoliDeta.put("PD_APLICAIETU", 0);
               objJsonPoliDeta.put("PD_APLICAIVA", 0);
               objJsonPoliDeta.put("PD_APLICAIEPS", 0);
               objJsonPoliDeta.put("PD_TASA", 0);
               objJsonPoliDeta.put("PD_RUBRO", 0);
               objJsonPoliDeta.put("PD_CLIE_PROV_EXP", 0);
               objJsonPoliDeta.put("PD_FOLIO", polDeta.getStrFolioRef());
               objJsonPoliDeta.put("PD_NOTAS", polDeta.getStrNotas());
               objJsonPoliDeta.put("RFCDiot", "");
               objJsonPoliDeta.put("Base", 0);
               objJsonPoliDeta.put("Retencion", 0);
               objJsonPoliDeta.put("PD_RFC", (polDeta.getStrRFC() != null ? polDeta.getStrRFC() : ""));
               objJsonPoliDeta.put("PD_NOMBRE_PROV", "");
               objJsonPoliDeta.put("PD_MONTO", polDeta.getDblMonto());
               objJsonPoliDeta.put("PD_UUID", (polDeta.getStrUUID() != null ? polDeta.getStrUUID() : ""));
               listPolizas.put(objJsonPoliDeta);
            } catch (JSONException ex) {
               log.error(ex.getMessage());
            }
         }
         //Iteramos por los archivos para enviarlos por Json
         log.debug("Cuantos archivos tienen " + this.lstFiles.size());
         Iterator<ArchivoEnvio> itFiles = this.lstFiles.iterator();
         while (itFiles.hasNext()) {
            ArchivoEnvio archivo = itFiles.next();
            try {
               JSONObject objJsonArchivo = new JSONObject();
               objJsonArchivo.put("PD_NOMBRE", archivo.getStrNombre());
               objJsonArchivo.put("PD_ARCHIVO", archivo.getStrContenido());
               objJsonArchivo.put("PD_TAMANIO", archivo.getIntTamanio());
               objJsonArchivo.put("PD_EXTENSION", archivo.getStrNombre().substring(archivo.getStrNombre().lastIndexOf("."), archivo.getStrNombre().length()));
               listArchivos.put(objJsonArchivo);
            } catch (JSONException ex) {
               Logger.getLogger(PolizasContables.class.getName()).log(Level.SEVERE, null, ex);
            }
         }

         log.debug("BEGIN");
         log.debug(objJson.toString());

         if (!this.strURLServicio.trim().equals("")) {
            //Qname
            QName qName = new QName("http://ws.contabilidad.siweb.mx.com/", "MySIWEBContaService");
            //Url
            URL url = null;
            try {
               url = new URL(this.strURLServicio);
               log.debug("Nueva URL Polizas......." + this.strURLServicio);
               //Validacion para detectar si es https quitar la restriccion del ssl
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               if (conn instanceof HttpsURLConnection) {
                  log.debug("Es conexion por https...");
                  HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();
                  log.debug("Is here " + conn1.toString());
                  conn1.setHostnameVerifier(new HostnameVerifier() {
                     public boolean verify(String hostname, SSLSession session) {
                        log.debug("Its true " + hostname);
                        return true;
                     }
                  });
                  log.debug("Is here (2)");
                  conn1.setRequestMethod("GET");
                  log.debug("Is here (3)");
                  conn1.setRequestProperty("Accept", "application/xml");
                  log.debug("Is here (4)");

                  if (conn1.getResponseCode() != 200) {
                     log.debug("Failed : HTTP error code :");
                     throw new RuntimeException("Failed : HTTP error code : "
                             + conn1.getResponseCode());
                  }
               }

               service = new Interfaz.ContabilidadPlus.MySIWEBContaService(url, qName);
               log.debug("Custom Contabilidad....");
            } catch (MalformedURLException ex) {
               log.error("Error 1: " + ex.getMessage());
               service = new Interfaz.ContabilidadPlus.MySIWEBContaService();
               ex.printStackTrace();
            } catch (Exception ex) {
               log.error("Error 2: " + ex.getMessage());
               ex.printStackTrace();
               return null;
            }

         } else {
            service = new Interfaz.ContabilidadPlus.MySIWEBContaService();
         }
      } else {
         service = new Interfaz.ContabilidadPlus.MySIWEBContaService();
      }
      Interfaz.ContabilidadPlus.MySIWEBConta port = service.getMySIWEBContaPort();

      log.debug("END");
      log.debug("Fin envio contabilidad...");
      return port.doPolizaAuto(strCliente, pass, "", "", "", "", "", objJson.toString(), "", "", "", "", "",
              "", "", "", "", "", "", "", "", "", "",
              "", "", "", "", "", "", "", "", "", "", strNumCheque);
   }

   /**
    * Limpia las polizas de cierto periodo
    *
    * @param strCliente Es el id de cliente
    * @param pass Es el password
    * @param strPeriodo Es el periodo
    * @return Regreso la respuesta del servicio
    */
   public String doCleanPolizaAuto(String strCliente, java.lang.String pass, String strPeriodo) {
      //Mostramos parametros si estamos en modo debug
      log.debug("Inicio envio limpia contabilidad...");
      Interfaz.ContabilidadPlus.MySIWEBContaService service = null;
      if (this.strURLServicio != null) {
         if (!this.strURLServicio.trim().equals("")) {
            //Qname
            QName qName = new QName("http://ws.contabilidad.siweb.mx.com/", "MySIWEBContaService");
            //Url
            URL url = null;
            try {
               url = new URL(this.strURLServicio);

               //Validacion para detectar si es https quitar la restriccion del ssl
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
               if (conn instanceof HttpsURLConnection) {

                  HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();

                  conn1.setHostnameVerifier(new HostnameVerifier() {
                     public boolean verify(String hostname, SSLSession session) {
                        return true;
                     }
                  });
                  conn1.setRequestMethod("GET");
                  conn1.setRequestProperty("Accept", "application/xml");

                  if (conn1.getResponseCode() != 200) {
                     throw new RuntimeException("Failed : HTTP error code : "
                             + conn1.getResponseCode());
                  }
               }

               service = new Interfaz.ContabilidadPlus.MySIWEBContaService(url, qName);
               log.debug("Custom Contabilidad....");
            } catch (MalformedURLException ex) {
               log.error("Error 3: " + ex.getMessage());
               service = new Interfaz.ContabilidadPlus.MySIWEBContaService();
            } catch (Exception ex) {
               log.error("Error 4: " + ex.getMessage());
               return null;
            }

         } else {
            service = new Interfaz.ContabilidadPlus.MySIWEBContaService();
         }
      } else {
         service = new Interfaz.ContabilidadPlus.MySIWEBContaService();
      }
      Interfaz.ContabilidadPlus.MySIWEBConta port = service.getMySIWEBContaPort();

      log.debug("Fin envio limpia contabilidad...");
      return port.doCleanPeriodo(strPeriodo, strCliente, pass);
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Objetos adicionales">
   /**
    * Representa un elemento por enviar
    */
   class ArchivoEnvio {

      private String strNombre;
      private String strContenido;
      private int intTamanio;

      public ArchivoEnvio(String strNombre, String strContenido, int intTamanio) {
         this.strNombre = strNombre;
         this.intTamanio = intTamanio;
         this.strContenido = strContenido;
      }

      public ArchivoEnvio() {
      }

      public String getStrNombre() {
         return strNombre;
      }

      public void setStrNombre(String strNombre) {
         this.strNombre = strNombre;
      }

      public String getStrContenido() {
         return strContenido;
      }

      public void setStrContenido(String strContenido) {
         this.strContenido = strContenido;
      }

      public int getIntTamanio() {
         return intTamanio;
      }

      public void setIntTamanio(int intTamanio) {
         this.intTamanio = intTamanio;
      }
   }

   // </editor-fold>
}
