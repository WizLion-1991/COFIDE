package ERP;

import Tablas.vta_facturadeta;
import Tablas.vta_pedidos;
import Tablas.vta_pedidosdeta;
import Tablas.vta_ticketsdeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Realiza las operaciones de facturacion recurrente
 *
 * @author zeus
 */
public class FacRecurrente extends ProcesoMaster implements ProcesoInterfaz {

   protected ArrayList<TableMaster> lstPedidos;
   protected Fechas Fecha;
   /*protected String strFecha1;
    protected String strFecha2;*/
   protected String strFolioGen1;
   protected String strFolioGen2;
   protected String strPathPrivateKeys;
   protected String strPathXML;
   protected String strPathFonts;
   protected int intProcesadas;
   protected int intFacturadas;
   protected ServletContext context;
   protected String strLstIdFacturar = "";
   protected String strPassKeys = "";
   protected boolean bolEsLocal = false;
   protected boolean bolAfectaInv = true;
   protected boolean bolUsaFechUser;
   protected String strFechaUser;
   protected int intSistemaCostos = 0;
   private static final Logger log = LogManager.getLogger(FacRecurrente.class.getName());

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
    * Constructor de factura recurrente
    *
    * @param oConn Es la conexion a la base de datos
    * @param varSesiones Son las variables de sesion
    */
   public FacRecurrente(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.lstPedidos = new ArrayList<TableMaster>();
      this.Fecha = new Fechas();
      this.strFolioGen1 = "";
      this.strFolioGen2 = "";
      this.intProcesadas = 0;
      this.intFacturadas = 0;
      this.strPathPrivateKeys = "";
      this.strPathXML = "";
      this.strPathFonts = "";
      this.strFechaUser = "";
      this.bolUsaFechUser = false;
   }

   /**
    * Constructor de factura recurrente
    *
    * @param oConn Es la conexion a la base de datos
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion web
    * @param context Es la variable de contexto
    */
   public FacRecurrente(Conexion oConn, VariableSession varSesiones, HttpServletRequest request, ServletContext context) {
      super(oConn, varSesiones, request);
      this.lstPedidos = new ArrayList<TableMaster>();
      this.Fecha = new Fechas();
      this.strFolioGen1 = "";
      this.strFolioGen2 = "";
      this.intProcesadas = 0;
      this.intFacturadas = 0;
      this.strPathPrivateKeys = "";
      this.strPathXML = "";
      this.strPathFonts = "";
      this.strFechaUser = "";
      this.context = context;
      this.bolUsaFechUser = false;
   }

   public void Init() {
      this.strFolioGen1 = "";
      this.strFolioGen2 = "";
      this.intProcesadas = 0;
      this.intFacturadas = 0;
   }

   public void doTrx() {
      if (!this.strLstIdFacturar.isEmpty()) {
         //if (!this.strFecha1.equals("") && !this.strFecha2.equals("")) {
         //Obtenemos el listado de pedidoes individuales por procesar
         gettPedidosProcesar();
         //Realiza los tickets de las pedidoes individuales
         doFacturas();
         /*} else {
          this.strResultLast = "ERROR: NO SE ESPECIFICO LA FECHA A FACTURAR";
          }*/
      } else {
         this.strResultLast = "ERROR: NO SE ESPECIFICO UNA LISTA DE PEDIDOS PARA FACTURAR";
      }
   }

   /**
    * Obtiene los tickets por facturar
    */
   private void gettPedidosProcesar() {
      vta_pedidos PedMas = new vta_pedidos();
      this.lstPedidos = PedMas.ObtenDatosVarios(" PD_ID IN (" + this.strLstIdFacturar + ")", this.oConn);
   }

   /**
    * Genera las facturas de los pedidos recurrentes seleccionados
    */
   private void doFacturas() {
      this.strResultLast = "OK";
      //Bandera para detectar errores
      boolean bolError = false;
      //Si hay transaccionalidad comenzamos
      if (this.bolTransaccionalidad) {
         oConn.runQueryLMD("BEGIN");
      }
      //Marcamos si mostramos querys
      boolean bolMostrarQuerys = this.oConn.isBolMostrarQuerys();
      this.oConn.setBolMostrarQuerys(true);
      //Procesamos todas las pedidoes
      int intCount = 0;
      Iterator<TableMaster> it = this.lstPedidos.iterator();
      while (it.hasNext()) {
         TableMaster pedido = it.next();
         intCount++;
         //Calculamos la fecha de la operaion
         String strFechaVta = pedido.getFieldString("PD_VENCI");
         //Nuevo ticket
         Ticket ticket = new Ticket(oConn, varSesiones, request);
         ticket.setBolAfectaInv(false);
         ticket.setIntSistemaCostos(intSistemaCostos);
         ticket.setStrPATHKeys(this.strPathPrivateKeys);
         ticket.setStrPATHXml(this.strPathXML);
         ticket.setStrPATHFonts(this.strPathFonts);
         ticket.setBolSendMailMasivo(true);
         ticket.setBolEsLocal(bolEsLocal);
         if (!this.bolEsLocal) {
            if (this.context != null) {
               ticket.initMyPass(this.context);
            }
         } else {
            ticket.setStrMyPassSecret(this.strPassKeys);
         }
         //Recibimos parametros
         String strPrefijoMaster = "FAC";
         String strPrefijoDeta = "FACD";
         //Validamos si es una factura recurrente o remision
         String strTipoVtaNom = Ticket.FACTURA;
         if (pedido.getFieldInt("TKT_ID") != 0) {
            strTipoVtaNom = Ticket.TICKET;
            strPrefijoMaster = "TKT";
            strPrefijoDeta = "TKTD";
         }
         log.debug("Documento recurrente de tipo " + strTipoVtaNom);
         ticket.setStrTipoVta(strTipoVtaNom);
         ticket.setBolTransaccionalidad(false);
         ticket.setIntEMP_ID(pedido.getFieldInt("EMP_ID"));
         ticket.setBolFolioGlobal(this.bolFolioGlobal);
         ticket.getDocument().setFieldInt("SC_ID", pedido.getFieldInt("SC_ID"));
         ticket.getDocument().setFieldInt("CT_ID", pedido.getFieldInt("CT_ID"));
         //ticket.getDocument().setFieldInt("TKT_ID", pedido.getIntTKT_ID());
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", pedido.getFieldInt("PD_MONEDA"));
         ticket.getDocument().setFieldInt("VE_ID", pedido.getFieldInt("VE_ID"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", pedido.getFieldInt("PD_ESSERV"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESRECU", pedido.getFieldInt("PD_ESRECU"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_PERIODICIDAD", pedido.getFieldInt("PD_PERIODICIDAD"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_DIAPER", pedido.getFieldInt("PD_DIAPER"));
         ticket.getDocument().setFieldInt("PD_RECU_ID", pedido.getFieldInt("PD_ID"));
         ticket.setIntPedidoGenero(pedido.getFieldInt("PD_ID"));
         //Validamos si usamos la fecha definida por el usuario
         if (this.bolUsaFechUser) {
            if (this.strFechaUser != null) {
               ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", this.strFechaUser);
            } else {
               ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", strFechaVta);
            }
         } else {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", strFechaVta);
         }
         ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
         ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", pedido.getFieldString("PD_NOTAS"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", pedido.getFieldString("PD_NOTASPIE"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", pedido.getFieldString("PD_REFERENCIA"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", pedido.getFieldString("PD_CONDPAGO"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", pedido.getFieldDouble("PD_IMPORTE"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", pedido.getFieldDouble("PD_IMPUESTO1"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", pedido.getFieldDouble("PD_IMPUESTO2"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", pedido.getFieldDouble("PD_IMPUESTO3"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", pedido.getFieldDouble("PD_TOTAL"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", pedido.getFieldDouble("PD_TASA1"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", pedido.getFieldDouble("PD_TASA2"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", pedido.getFieldDouble("PD_TASA3"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", pedido.getFieldDouble("PD_TASAPESO"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESARRENDA", pedido.getFieldInt("PD_ESARRENDA"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_NOMFORMATO", pedido.getFieldString("PD_NOMFORMATO"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", pedido.getFieldDouble("PD_RETISR"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", pedido.getFieldDouble("PD_RETIVA"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", pedido.getFieldDouble("PD_NETO"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_USO_IEPS", pedido.getFieldInt("PD_USO_IEPS"));
         ticket.getDocument().setFieldInt(strPrefijoMaster + "_TASA_IEPS", pedido.getFieldInt("PD_TASA_IEPS"));
         ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_IEPS", pedido.getFieldDouble("PD_IMPORTE_IEPS"));
         //Nuevos campos SAT Julio 2012
         ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", pedido.getFieldString("PD_METODODEPAGO"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", pedido.getFieldString("PD_NUMCUENTA"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", pedido.getFieldString("PD_FORMADEPAGO"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_REGIMENFISCAL", pedido.getFieldString("PD_REGIMENFISCAL"));
         vta_pedidosdeta detaMas = new vta_pedidosdeta();
         ArrayList<TableMaster> lstPartidas = detaMas.ObtenDatosVarios(" PD_ID = " + pedido.getFieldInt("PD_ID"), oConn);
         //Iteramos por las partidas
         Iterator<TableMaster> it2 = lstPartidas.iterator();
         while (it2.hasNext()) {
            TableMaster tb = it2.next();
            //Generamos nuevo objeto para la partida
            TableMaster deta = null;
            //Validamos si sera una factura o ticket recurrente
            if (pedido.getFieldInt("TKT_ID") == 0) {
               deta = new vta_facturadeta();
            }else{
               deta = new vta_ticketsdeta();
            }
            deta.setFieldInt("SC_ID", pedido.getFieldInt("SC_ID"));
            deta.setFieldInt("PR_ID", tb.getFieldInt("PR_ID"));
            deta.setFieldString(strPrefijoDeta + "_CVE", tb.getFieldString("PDD_CVE"));
            deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", tb.getFieldString("PDD_DESCRIPCION"));
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", tb.getFieldDouble("PDD_IMPORTE"));
            deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", tb.getFieldDouble("PDD_CANTIDAD"));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", tb.getFieldDouble("PDD_TASAIVA1"));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", tb.getFieldDouble("PDD_TASAIVA2"));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", tb.getFieldDouble("PDD_TASAIVA3"));
            deta.setFieldInt(strPrefijoDeta + "_DESGLOSA1", tb.getFieldInt("PDD_DESGLOSA1"));
            deta.setFieldInt(strPrefijoDeta + "_DESGLOSA2", tb.getFieldInt("PDD_DESGLOSA2"));
            deta.setFieldInt(strPrefijoDeta + "_DESGLOSA3", tb.getFieldInt("PDD_DESGLOSA3"));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", tb.getFieldDouble("PDD_IMPUESTO1"));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", tb.getFieldDouble("PDD_IMPUESTO2"));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", tb.getFieldDouble("PDD_IMPUESTO3"));
            deta.setFieldString(strPrefijoDeta + "_NOSERIE", tb.getFieldString("PDD_NOSERIE"));
            deta.setFieldInt(strPrefijoDeta + "_ESREGALO", tb.getFieldInt("PDD_ESREGALO"));
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", tb.getFieldDouble("PDD_IMPORTEREAL"));
            deta.setFieldDouble(strPrefijoDeta + "_PRECIO", tb.getFieldDouble("PDD_PRECIO"));
            deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", tb.getFieldDouble("PDD_PRECREAL"));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO1", tb.getFieldInt("PDD_EXENTO1"));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO2", tb.getFieldInt("PDD_EXENTO2"));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO3", tb.getFieldInt("PDD_EXENTO3"));
            deta.setFieldInt(strPrefijoDeta + "_EXEN_RET_ISR", tb.getFieldInt("PDD_EXEN_RET_ISR"));
            deta.setFieldInt(strPrefijoDeta + "_EXEN_RET_IVA", tb.getFieldInt("PDD_EXEN_RET_IVA"));
            deta.setFieldDouble(strPrefijoDeta + "_COSTO", tb.getFieldDouble("PDD_COSTO"));
            deta.setFieldDouble(strPrefijoDeta + "_GANANCIA", tb.getFieldDouble("PDD_GANANCIA"));
            deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", tb.getFieldDouble("PDD_DESCUENTO"));
            deta.setFieldDouble(strPrefijoDeta + "_PORDESC", tb.getFieldDouble("PDD_PORDESC"));
            deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", tb.getFieldInt("PDD_PRECFIJO"));
            deta.setFieldString(strPrefijoDeta + "_COMENTARIO", tb.getFieldString("PDD_COMENTARIO"));
            deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", tb.getFieldString("PDD_UNIDAD_MEDIDA"));
            ticket.AddDetalle(deta);
         }

         //Inicializamos objeto
         ticket.Init();
         //Generamos transaccion
         ticket.doTrx();
         if (!ticket.getStrResultLast().equals("OK")) {
            this.strResultLast = ticket.getStrResultLast();
            bolError = true;
            break;
         } else {
            log.debug("Documento generado " + ticket.getDocument().getValorKey());
            //Calculamos la nueva fecha de vencimiento
            String strNvaVenci = this.calculaFechavenci(ticket, strFechaVta, strPrefijoMaster);
            //Marcamos el pedido como facturado
            String strUpdate = "update vta_pedidos set "
                    + "FAC_ID = " + ticket.getDocument().getValorKey() + " "
                    + ",PD_VENCI = '" + strNvaVenci + "' "
                    + "WHERE PD_ID =  " + pedido.getFieldInt("PD_ID");
            //Evaluamos si es un ticket recurrente
            if (pedido.getFieldInt("TKT_ID") != 0) {
               strUpdate = "update vta_pedidos set "
                       + "TKT_ID = " + ticket.getDocument().getValorKey() + " "
                       + ",PD_VENCI = '" + strNvaVenci + "' "
                       + "WHERE PD_ID =  " + pedido.getFieldInt("PD_ID");
            }
            oConn.runQueryLMD(strUpdate);
            this.intFacturadas++;
            this.strFolioGen2 = ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO");
            if (intCount == 1) {
               this.strFolioGen1 = ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO");
            }
         }
      }
      //Marcamos si mostramos querys
      this.oConn.setBolMostrarQuerys(bolMostrarQuerys);
      //Si hay transaccionalidad terminamos
      if (this.bolTransaccionalidad) {
         if (!bolError) {
            oConn.runQueryLMD("COMMIT");
         } else {
            oConn.runQueryLMD("ROLLBACK");
         }
      }
   }

   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * Genera un pedido recurrrente a partir de una factura 
    *
    * @param documentFac Es la factura recurrente
    * @param lstDeta Es el listado de partidas de la venta
    */
   public void NewPedRec(TableMaster documentFac, ArrayList<TableMaster> lstDeta) {
      NewPedRec("FACTURA",  documentFac,  lstDeta);
   }
   /**
    * Genera un pedido recurrrente a partir de una factura o remision
    *
    * @param strTipoVta Es el tipo de venta
    * @param documentFac Es la factura recurrente
    * @param lstDeta Es el listado de partidas de la venta
    */
   public void NewPedRec(String strTipoVta, TableMaster documentFac, ArrayList<TableMaster> lstDeta) {
      String strPrefijoMaster = "PD";
      String strPrefijoDeta = "PDD";
      String strPrefijoMasterFac = "FAC";
      String strPrefijoDetaFac = "FACD";
      log.debug("Tipo de documento Origen " + strTipoVta);
      if (strTipoVta.equals(Ticket.TICKET)) {
         strPrefijoMasterFac = "TKT";
         strPrefijoDetaFac = "TKTD";
      }
      String strTipoVtaNom = Ticket.PEDIDO;
      //Copiamos los datos de la factura en el pedido
      Ticket ticket = new Ticket(oConn, varSesiones, request);
      ticket.setStrTipoVta(strTipoVtaNom);
      ticket.setBolTransaccionalidad(false);
      //Asignamos la empresa seleccionada
      ticket.setIntEMP_ID(documentFac.getFieldInt("EMP_ID"));
      if (!strTipoVta.equals(Ticket.TICKET)) {
         ticket.getDocument().setFieldInt("FAC_ID", Integer.valueOf(documentFac.getValorKey()));
      } else {
         ticket.getDocument().setFieldInt("TKT_ID", Integer.valueOf(documentFac.getValorKey()));
      }
      log.debug("strPrefijoMasterFac:" + strPrefijoMasterFac);
      ticket.getDocument().setFieldInt("SC_ID", documentFac.getFieldInt("SC_ID"));
      ticket.getDocument().setFieldInt("CT_ID", documentFac.getFieldInt("CT_ID"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", documentFac.getFieldInt(strPrefijoMasterFac + "_MONEDA"));
      ticket.getDocument().setFieldInt("VE_ID", documentFac.getFieldInt("VE_ID"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", documentFac.getFieldInt(strPrefijoMasterFac + "_ESSERV"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESRECU", documentFac.getFieldInt(strPrefijoMasterFac + "_ESRECU"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_PERIODICIDAD", documentFac.getFieldInt(strPrefijoMasterFac + "_PERIODICIDAD"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_DIAPER", documentFac.getFieldInt(strPrefijoMasterFac + "_DIAPER"));
      //Cambios para guardar el numero de eventos
      // 19/06/2013
      //Zeus Galindo
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_NO_EVENTOS", documentFac.getFieldInt(strPrefijoMasterFac + "_NO_EVENTOS"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_RECU_FINAL", documentFac.getFieldString(strPrefijoMasterFac + "_RECU_FINAL"));

      ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", documentFac.getFieldString(strPrefijoMasterFac + "_FECHA"));
//      ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", documentFac.getFieldString(strPrefijoMasterFac + "_FOLIO"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", documentFac.getFieldString(strPrefijoMasterFac + "_NOTAS"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", documentFac.getFieldString(strPrefijoMasterFac + "_NOTASPIE"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", documentFac.getFieldString(strPrefijoMasterFac + "_REFERENCIA"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", documentFac.getFieldString(strPrefijoMasterFac + "_CONDPAGO"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", documentFac.getFieldDouble(strPrefijoMasterFac + "_IMPORTE"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", documentFac.getFieldDouble(strPrefijoMasterFac + "_IMPUESTO1"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", documentFac.getFieldDouble(strPrefijoMasterFac + "_IMPUESTO2"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", documentFac.getFieldDouble(strPrefijoMasterFac + "_IMPUESTO3"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", documentFac.getFieldDouble(strPrefijoMasterFac + "_TOTAL"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", documentFac.getFieldDouble(strPrefijoMasterFac + "_TASA1"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", documentFac.getFieldDouble(strPrefijoMasterFac + "_TASA2"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", documentFac.getFieldDouble(strPrefijoMasterFac + "_TASA3"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", documentFac.getFieldDouble(strPrefijoMasterFac + "_TASAPESO"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", documentFac.getFieldDouble(strPrefijoMasterFac + "_RETISR"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", documentFac.getFieldDouble(strPrefijoMasterFac + "_RETIVA"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", documentFac.getFieldDouble(strPrefijoMasterFac + "_NETO"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESARRENDA", documentFac.getFieldInt(strPrefijoMasterFac + "_ESARRENDA"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NOMFORMATO", documentFac.getFieldString(strPrefijoMasterFac + "_NOMFORMATO"));
      ticket.getDocument().setFieldInt("TI_ID", documentFac.getFieldInt("TI_ID"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_USO_IEPS", documentFac.getFieldInt(strPrefijoMasterFac + "_USO_IEPS"));
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_TASA_IEPS", documentFac.getFieldInt(strPrefijoMasterFac + "_TASA_IEPS"));
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_IEPS", documentFac.getFieldDouble(strPrefijoMasterFac + "_IMPORTE_IEPS"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", documentFac.getFieldString(strPrefijoMasterFac + "_METODODEPAGO"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", documentFac.getFieldString(strPrefijoMasterFac + "_NUMCUENTA"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", documentFac.getFieldString(strPrefijoMasterFac + "_FORMADEPAGO"));
      ticket.getDocument().setFieldString(strPrefijoMaster + "_REGIMENFISCAL", documentFac.getFieldString(strPrefijoMasterFac + "_REGIMENFISCAL"));
      //Iteramos por las partidas
      Iterator<TableMaster> it = lstDeta.iterator();
      while (it.hasNext()) {
         TableMaster detaFac = it.next();
         //Nueva partida de pedidos
         vta_pedidosdeta deta = new vta_pedidosdeta();
         deta.setFieldInt("SC_ID", documentFac.getFieldInt("SC_ID"));
         deta.setFieldInt("PR_ID", detaFac.getFieldInt("PR_ID"));
         deta.setFieldInt(strPrefijoDeta + "_EXENTO1", detaFac.getFieldInt(strPrefijoDetaFac + "_EXENTO1"));
         deta.setFieldInt(strPrefijoDeta + "_EXENTO2", detaFac.getFieldInt(strPrefijoDetaFac + "_EXENTO2"));
         deta.setFieldInt(strPrefijoDeta + "_EXENTO3", detaFac.getFieldInt(strPrefijoDetaFac + "_EXENTO3"));
         deta.setFieldInt(strPrefijoDeta + "_EXEN_RET_ISR", detaFac.getFieldInt(strPrefijoDetaFac + "_EXEN_RET_ISR"));
         deta.setFieldInt(strPrefijoDeta + "_EXEN_RET_IVA", detaFac.getFieldInt(strPrefijoDetaFac + "_EXEN_RET_IVA"));         
         deta.setFieldInt(strPrefijoDeta + "_ESREGALO", detaFac.getFieldInt(strPrefijoDetaFac + "_ESREGALO"));
         deta.setFieldString(strPrefijoDeta + "_CVE", detaFac.getFieldString(strPrefijoDetaFac + "_CVE"));
         deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", detaFac.getFieldString(strPrefijoDetaFac + "_DESCRIPCION"));
         deta.setFieldString(strPrefijoDeta + "_NOSERIE", detaFac.getFieldString(strPrefijoDetaFac + "_NOSERIE"));
         deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", detaFac.getFieldDouble(strPrefijoDetaFac + "_IMPORTE"));
         deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", detaFac.getFieldDouble(strPrefijoDetaFac + "_CANTIDAD"));
         deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", detaFac.getFieldDouble(strPrefijoDetaFac + "_TASAIVA1"));
         deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", detaFac.getFieldDouble(strPrefijoDetaFac + "_TASAIVA2"));
         deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", detaFac.getFieldDouble(strPrefijoDetaFac + "_TASAIVA3"));
         deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", detaFac.getFieldDouble(strPrefijoDetaFac + "_IMPUESTO1"));
         deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", detaFac.getFieldDouble(strPrefijoDetaFac + "_IMPUESTO2"));
         deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", detaFac.getFieldDouble(strPrefijoDetaFac + "_IMPUESTO3"));
         deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", detaFac.getFieldDouble(strPrefijoDetaFac + "_IMPORTEREAL"));
         deta.setFieldDouble(strPrefijoDeta + "_PRECIO", detaFac.getFieldDouble(strPrefijoDetaFac + "_PRECIO"));
         deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", detaFac.getFieldDouble(strPrefijoDetaFac + "_DESCUENTO"));
         deta.setFieldDouble(strPrefijoDeta + "_PORDESC", detaFac.getFieldDouble(strPrefijoDetaFac + "_PORDESC"));
         deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", detaFac.getFieldDouble(strPrefijoDetaFac + "_PRECREAL"));
         deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", detaFac.getFieldInt(strPrefijoDetaFac + "_PRECFIJO"));
         deta.setFieldInt(strPrefijoDeta + "_DESC_PREC", detaFac.getFieldInt(strPrefijoDetaFac + "_DESC_PREC"));
         deta.setFieldInt(strPrefijoDeta + "_DESC_PUNTOS", detaFac.getFieldInt(strPrefijoDetaFac + "_DESC_PUNTOS"));
         deta.setFieldInt(strPrefijoDeta + "_DESC_VNEGOCIO", detaFac.getFieldInt(strPrefijoDetaFac + "_DESC_VNEGOCIO"));
         deta.setFieldString(strPrefijoDeta + "_COMENTARIO", detaFac.getFieldString(strPrefijoDetaFac + "_COMENTARIO"));
         deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", detaFac.getFieldString(strPrefijoDetaFac + "_UNIDAD_MEDIDA"));
         ticket.AddDetalle(deta);
      }
      //Obtenemos el mes y anio actual
      log.debug(strPrefijoMaster + "_FECHA:" + ticket.getDocument().getFieldString(strPrefijoMaster + "_FECHA"));
      String strMesAct = ticket.getDocument().getFieldString(strPrefijoMaster + "_FECHA").substring(4, 6);
      String strAnioAct = ticket.getDocument().getFieldString(strPrefijoMaster + "_FECHA").substring(0, 4);
      String strDiaAct = ticket.getDocument().getFieldInt(strPrefijoMaster + "_DIAPER") + "";
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_DIAPER") < 10) {
         strDiaAct = "0" + ticket.getDocument().getFieldInt(strPrefijoMaster + "_DIAPER");
      }
      String strFechaBase = strAnioAct + strMesAct + strDiaAct;
      String strFechaVenci = calculaFechavenci(ticket, strFechaBase, strPrefijoMaster);
      ticket.getDocument().setFieldString("PD_VENCI", strFechaVenci);
      //Cambios para guardar el numero de eventos
      // 19/06/2013
      //Zeus Galindo
      if (documentFac.getFieldInt(strPrefijoMasterFac + "_NO_EVENTOS") > 0) {
         String strFechaFinal = calculaFechaFinal(ticket, strFechaBase, strPrefijoMaster, documentFac.getFieldInt(strPrefijoMasterFac + "_NO_EVENTOS"));
         ticket.getDocument().setFieldString(strPrefijoMaster + "_RECU_FINAL", strFechaFinal);
      }
      //Inicializamos objeto
      ticket.Init();
      //Generamos transaccion
      ticket.doTrx();
      log.debug("Respuesta al generar el pedido " + ticket.getStrResultLast());
      //Si fue ok la respuesta actualizamos el id del pedido
      if(ticket.getStrResultLast().equals("OK")){
         log.debug("id recurrente " + Integer.valueOf(ticket.getDocument().getValorKey()));
      if (!strTipoVta.equals(Ticket.TICKET)) {
         String strUpdate = "update vta_facturas set PD_RECU_ID = " + Integer.valueOf(ticket.getDocument().getValorKey())
                 + " where FAC_ID = " + Integer.valueOf(documentFac.getValorKey());
         oConn.runQueryLMD(strUpdate);
      } else {
         String strUpdate = "update vta_tickets set PD_RECU_ID = " + Integer.valueOf(ticket.getDocument().getValorKey())
                 + " where TKT_ID = " + Integer.valueOf(documentFac.getValorKey());
         oConn.runQueryLMD(strUpdate);
      }
      }
      this.strResultLast = ticket.getStrResultLast();
   }

   /**
    * Calcula la fecha de vencimiento de un pedido recurrente
    *
    * @param ticket Es el ticket
    * @param strFechaBase Es la fecha base
    * @param strPrefijoMaster Es el prefijo
    * @return Regresa la nueva fecha de vencimiento
    */
   public String calculaFechavenci(Ticket ticket, String strFechaBase, String strPrefijoMaster) {
      String strFechaVenci = "";
      //Calculamos el siguiente vencimiento
      //MENSUAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 1) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, 1);
      }
      //BIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 2) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, 2);
      }
      //TRIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 3) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, 3);
      }
      //TRIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 4) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.YEAR, 1);
      }
      return strFechaVenci;
   }
   /**
    * Calcula la fecha de vencimiento de un pedido recurrente cancelado
    *
    * @param ticket Es el ticket
    * @param strFechaBase Es la fecha base
    * @param strPrefijoMaster Es el prefijo
    * @return Regresa la nueva fecha de vencimiento
    */
   public String calculaFechaVenciCancel(Ticket ticket, String strFechaBase, String strPrefijoMaster) {
      String strFechaVenci = "";
      //Calculamos el siguiente vencimiento
      //MENSUAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 1) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, -1);
      }
      //BIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 2) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, -2);
      }
      //TRIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 3) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, -3);
      }
      //TRIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 4) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.YEAR, -1);
      }
      return strFechaVenci;
   }

   /**
    * Calcula la fecha de ultimo movimiento de un pedido recurrente
    *
    * @param ticket Es el ticket
    * @param strFechaBase Es la fecha base
    * @param strPrefijoMaster Es el prefijo
    * @param intNumEventos Es el numero de eventos por sumar
    * @return Regresa la nueva fecha de vencimiento
    */
   public String calculaFechaFinal(Ticket ticket, String strFechaBase, String strPrefijoMaster, int intNumEventos) {
      String strFechaVenci = "";
      //Calculamos el siguiente vencimiento
      //MENSUAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 1) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, intNumEventos);
      }
      //BIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 2) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, 2 * intNumEventos);
      }
      //TRIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 3) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.MONTH, 3 * intNumEventos);
      }
      //TRIMESTRAL
      if (ticket.getDocument().getFieldInt(strPrefijoMaster + "_PERIODICIDAD") == 4) {
         strFechaVenci = this.fecha.addFecha(strFechaBase, Calendar.YEAR, intNumEventos);
      }
      return strFechaVenci;
   }

   /**
    * Nos regresa la lista de pedidos por facturar
    *
    * @return Regresa una lista de id's separados por comas
    */
   public String getStrLstIdFacturar() {
      return strLstIdFacturar;
   }

   /**
    * Definimos la lista de pedidos por facturar
    *
    * @param strLstIdFacturar Define una lista de id's separados por comas
    */
   public void setStrLstIdFacturar(String strLstIdFacturar) {
      this.strLstIdFacturar = strLstIdFacturar;
   }

   /**
    * Regresa el folio inicial generado
    *
    * @return Regresa un numero de folio de la factura
    */
   public String getStrFolioGen1() {
      return strFolioGen1;
   }

   /**
    * Regresa el folio final generado
    *
    * @return Regresa un numero de folio de la factura
    */
   public String getStrFolioGen2() {
      return strFolioGen2;
   }

   /**
    * Regresa el numero de operacion facturadas
    *
    * @return Regresa un contador de numero de operaciones
    */
   public int getIntFacturadas() {
      return intFacturadas;
   }

   /**
    * Regresa el numero de operaciones que se intentaron facturar
    *
    * @return Regresa un contador de numero de operaciones
    */
   public int getIntProcesadas() {
      return intProcesadas;
   }

   /**
    * Regresa el path de las fuentes
    *
    * @return Regresa una cadena
    */
   public String getStrPathFonts() {
      return strPathFonts;
   }

   /**
    * Define el path de las fuentes
    *
    * @param strPathFonts Es una cadena con un path
    */
   public void setStrPathFonts(String strPathFonts) {
      this.strPathFonts = strPathFonts;
   }

   /**
    * Regresa el path de la llave privada
    *
    * @return Regresa una cadena
    */
   public String getStrPathPrivateKeys() {
      return strPathPrivateKeys;
   }

   /**
    * Define el path de la llave privada
    *
    * @param strPathPrivateKeys Es una cadena con un path
    */
   public void setStrPathPrivateKeys(String strPathPrivateKeys) {
      this.strPathPrivateKeys = strPathPrivateKeys;
   }

   /**
    * Regresa el path de los XML
    *
    * @return Regresa una cadena
    */
   public String getStrPathXML() {
      return strPathXML;
   }

   /**
    * Define el path de los XML
    *
    * @param strPathXML Es una cadena con un path
    */
   public void setStrPathXML(String strPathXML) {
      this.strPathXML = strPathXML;
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
    * Ontiene el password para abrir las llaves
    *
    * @return Regresa cadena con password
    */
   public String getStrPassKeys() {
      return strPassKeys;
   }

   /**
    * Define el password para abrir las llaves
    *
    * @param strPassKeys Define cadena con password
    */
   public void setStrPassKeys(String strPassKeys) {
      this.strPassKeys = strPassKeys;
   }

   /**
    * Nos dice si esta Activa el uso de la fecha del usuario
    *
    * @return
    */
   public boolean isBolUsaFechUser() {
      return bolUsaFechUser;
   }

   /**
    * Definimos que la fecha a usar para generar las facturas sera definida por
    * el usuario
    *
    * @param bolUsaFechUser Activa el uso de la fecha del usuario
    */
   public void setBolUsaFechUser(boolean bolUsaFechUser) {
      this.bolUsaFechUser = bolUsaFechUser;
   }

   /**
    * Regresa la fecha definida por el usuario
    *
    * @return Es la fecha definida por el usuario
    */
   public String getStrFechaUser() {
      return strFechaUser;
   }

   /**
    * Es la fecha definida por el usuario
    *
    * @param strFechaUser Es la fecha definida por el usuario
    */
   public void setStrFechaUser(String strFechaUser) {
      this.strFechaUser = strFechaUser;
   }
}
