package ERP;

import Tablas.vta_facturadeta;
import Tablas.vta_mov_cte_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Clase que realiza las operaciones de facturacion masiva
 *
 * @author zeus
 */
public class FacturaMasiva extends ProcesoMaster implements ProcesoInterfaz {

   protected Ticket miFactura;
   protected ArrayList<Ticket> lstTickets;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FacturaMasiva.class.getName());

   public FacturaMasiva(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.miFactura = new Ticket(oConn, varSesiones, request);
      this.miFactura.setStrTipoVta(Ticket.FACTURA);
      this.miFactura.setBolAfectaInv(false);
      this.lstTickets = new ArrayList<Ticket>();
   }

   @Override
   public void Init() {
      this.miFactura.getDocument().setFieldInt("FAC_US_ALTA", varSesiones.getIntNoUser());
      this.miFactura.getDocument().setFieldInt("FAC_ESMASIVA", 1);//Indicamos que es una factura de tickets
      this.miFactura.setBolTransaccionalidad(false);
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.miFactura.getDocument().getFieldInt("FAC_ID") != 0) {
         this.miFactura.getDocument().ObtenDatos(this.miFactura.getDocument().getFieldInt("FAC_ID"), oConn);
         //Recuperamos todas las operaciones de pagos
         String strSql = "SELECT TKT_ID FROM vta_tickets where FAC_ID = " + this.miFactura.getDocument().getFieldInt("FAC_ID");
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               int intTKT_ID = rs.getInt("TKT_ID");
               Ticket mov = new Ticket(oConn, varSesiones, request);
               mov.setStrTipoVta(Ticket.TICKET);
               mov.getDocument().setFieldInt("TKT_ID", intTKT_ID);
               mov.Init();
               mov.setBolTransaccionalidad(false);
               mov.setBolAfectaInv(false);
               this.lstTickets.add(mov);
            }
            rs.close();
         } catch (SQLException ex) {
            Logger.getLogger(PagosMasivos.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   @Override
   public void doTrx() {
      //Generamos una FACTURA de TICKETS
      this.strResultLast = "OK";
      this.miFactura.getDocument().setFieldString("FAC_FECHACREATE", this.fecha.getFechaActual());
      this.miFactura.getDocument().setFieldString("FAC_HORA", this.fecha.getHoraActual());
      //Validamos que todos los campos basico se encuentren
      if (this.miFactura.getDocument().getFieldString("FAC_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.miFactura.getDocument().getFieldString("CT_ID").equals("0")) {
         this.strResultLast = "ERROR:Falta definir un cliente";
      }
      if (this.miFactura.getDocument().getFieldString("SC_ID").equals("0")) {
         this.strResultLast = "ERROR:Falta definir una sucursal";
      }
      //Validamos que halla por lo menos un ticket
      if (this.lstTickets.isEmpty()) {
         this.strResultLast = "ERROR:NECESITA CAPTURAR UN TICKET PARA FACTURAR";
      }
      //Calculamos el importe, impuestos, pagos y obtenemos la lista de productos
      //que apareceran en la factura
      double dblImporte = 0;
      double dblImpuesto1 = 0;
      double dblImpuesto2 = 0;
      double dblImpuesto3 = 0;
      double dblPuntos = 0;
      double dblNegocio = 0;
      double dblTotal = 0;
      double dblTasa1 = 0;
      double dblTasa2 = 0;
      double dblTasa3 = 0;
      int intMpeId = 0;
      String lstTks = "";
      Iterator<Ticket> it = this.lstTickets.iterator();
      while (it.hasNext()) {
         Ticket tk = it.next();
         dblPuntos += tk.getDocument().getFieldDouble("TKT_IMPORTE_PUNTOS");
         dblNegocio += tk.getDocument().getFieldDouble("TKT_IMPORTE_NEGOCIO");
         dblImporte += tk.getDocument().getFieldDouble("TKT_IMPORTE");
         dblImpuesto1 += tk.getDocument().getFieldDouble("TKT_IMPUESTO1");
         dblImpuesto2 += tk.getDocument().getFieldDouble("TKT_IMPUESTO2");
         dblImpuesto3 += tk.getDocument().getFieldDouble("TKT_IMPUESTO3");
         dblTotal += tk.getDocument().getFieldDouble("TKT_TOTAL");
         dblTasa1 = tk.getDocument().getFieldDouble("TKT_TASA1");
         dblTasa2 = tk.getDocument().getFieldDouble("TKT_TASA2");
         dblTasa3 = tk.getDocument().getFieldDouble("TKT_TASA3");
         lstTks += " " + tk.getDocument().getFieldInt("TKT_ID") + ",";
         intMpeId = tk.getDocument().getFieldInt("MPE_ID");
      }
      if (lstTks.endsWith(",")) {
         lstTks = lstTks.substring(0, lstTks.length() - 1);
      }
      this.miFactura.getDocument().setFieldDouble("FAC_IMPORTE", dblImporte);
      this.miFactura.getDocument().setFieldDouble("FAC_IMPUESTO1", dblImpuesto1);
      this.miFactura.getDocument().setFieldDouble("FAC_IMPUESTO2", dblImpuesto2);
      this.miFactura.getDocument().setFieldDouble("FAC_IMPUESTO3", dblImpuesto3);
      this.miFactura.getDocument().setFieldDouble("FAC_TASA1", dblTasa1);
      this.miFactura.getDocument().setFieldDouble("FAC_TASA2", dblTasa2);
      this.miFactura.getDocument().setFieldDouble("FAC_TASA3", dblTasa3);
      this.miFactura.getDocument().setFieldDouble("FAC_IMPORTE_PUNTOS", 0);
      this.miFactura.getDocument().setFieldDouble("FAC_IMPORTE_NEGOCIO", 0);
      this.miFactura.getDocument().setFieldInt("MPE_ID", intMpeId);
      log.debug(dblTotal + " " + this.miFactura.getDocument().getFieldDouble("FAC_TOTAL"));
      if (Math.round(dblTotal) != Math.round(this.miFactura.getDocument().getFieldDouble("FAC_TOTAL"))) {
         this.strResultLast = "ERROR:EL TOTAL DE LA OPERACION NO ES IGUAL AL TOTAL DE LOS TICKETS ";
      }
      //Obtenemos la suma de pagos vivos de estas operaciones para incluirlos en la factura
      String strSql = "SELECT SUM(MCD_IMPORTE) AS TIMPORTE,SUM(MCD_CAMBIO) AS TCAMBIO "
              + ",MCD_FORMAPAGO,MCD_MONEDA,MCD_TASAPESO,                                      "
              + "MCD_NOCHEQUE,MCD_BANCO,MCD_NOTARJETA,MCD_TIPOTARJETA                         "
              + " FROM vta_tickets ,vta_mov_cte,vta_mov_cte_deta where                        "
              + "vta_tickets.TKT_ID = vta_mov_cte.TKT_ID AND                                  "
              + "vta_mov_cte.MC_ID = vta_mov_cte_deta.MC_ID AND vta_tickets.TKT_ID IN ( " + lstTks + ")"
              + " AND MC_ANULADO = 0 GROUP BY                                                 "
              + " MCD_FORMAPAGO,MCD_MONEDA,MCD_TASAPESO,                                      "
              + "MCD_NOCHEQUE,MCD_BANCO,MCD_NOTARJETA,MCD_TIPOTARJETA;                        ";
      ResultSet rs = null;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            vta_mov_cte_deta detaPago = new vta_mov_cte_deta();
            detaPago.setFieldInt("CT_ID", this.miFactura.getDocument().getFieldInt("CT_ID"));
            detaPago.setFieldInt("SC_ID", this.miFactura.getDocument().getFieldInt("SC_ID"));
            detaPago.setFieldInt("MCD_MONEDA", rs.getInt("MCD_MONEDA"));
            detaPago.setFieldString("MCD_FOLIO", "");
            detaPago.setFieldString("MCD_FORMAPAGO", rs.getString("MCD_FORMAPAGO"));
            detaPago.setFieldString("MCD_NOCHEQUE", rs.getString("MCD_NOCHEQUE"));
            detaPago.setFieldString("MCD_BANCO", rs.getString("MCD_BANCO"));
            detaPago.setFieldString("MCD_NOTARJETA", rs.getString("MCD_NOTARJETA"));
            detaPago.setFieldString("MCD_TIPOTARJETA", rs.getString("MCD_TIPOTARJETA"));
            detaPago.setFieldDouble("MCD_IMPORTE", rs.getDouble("TIMPORTE"));
            detaPago.setFieldDouble("MCD_TASAPESO", rs.getDouble("MCD_TASAPESO"));
            detaPago.setFieldDouble("MCD_CAMBIO", rs.getDouble("TCAMBIO"));
            this.miFactura.AddDetalle(detaPago);
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(FacturaMasiva.class.getName()).log(Level.SEVERE, null, ex);
      }
      //Obtenemos la lista de los productos de estos documentos
      strSql = "SELECT "
              + " SUM(TKTD_IMPORTE) AS TIMPORTE,SUM(TKTD_CANTIDAD) AS TCANTIDAD,"
              + " SUM(TKTD_IMPUESTO1) AS TIMPUESTO1,SUM(TKTD_IMPUESTO2) AS TIMPUESTO2,SUM(TKTD_IMPUESTO3) AS TIMPUESTO3,"
              + " SUM(TKTD_IMPORTEREAL) AS TIMPORTEREAL,SUM(TKTD_COSTO) AS TCOSTO,"
              + " SUM(TKTD_GANANCIA) AS TGANANCIA,SUM(TKTD_DESCUENTO) AS TDESC,"
              + " PR_ID,TKTD_CVE,TKTD_DESCRIPCION,TKTD_TASAIVA1,TKTD_TASAIVA2,TKTD_TASAIVA3,"
              + " TKTD_PRECIO,TKTD_ESREGALO,"
              + " TKTD_EXENTO1,TKTD_EXENTO2,TKTD_EXENTO3,TKTD_PORDESC,"
              + " TKTD_PRECFIJO,TKTD_PRECREAL,TKTD_ESDEVO,TKTD_COMENTARIO"
              + " FROM vta_ticketsdeta WHERE vta_ticketsdeta.TKT_ID IN(" + lstTks + ")"
              + " GROUP BY PR_ID,TKTD_CVE,TKTD_DESCRIPCION,TKTD_TASAIVA1,TKTD_TASAIVA2,TKTD_TASAIVA3,"
              + " TKTD_PRECIO,TKTD_ESREGALO,                          "
              + " TKTD_EXENTO1,TKTD_EXENTO2,TKTD_EXENTO3,TKTD_PORDESC,"
              + " TKTD_PRECFIJO,TKTD_PRECREAL,TKTD_ESDEVO,TKTD_COMENTARIO;";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            //Anadimos partidas a la factura
            vta_facturadeta deta = new vta_facturadeta();
            deta.setFieldInt("SC_ID", this.miFactura.getDocument().getFieldInt("SC_ID"));
            deta.setFieldInt("PR_ID", rs.getInt("PR_ID"));
            deta.setFieldInt("FACD_EXENTO1", rs.getInt("TKTD_EXENTO1"));
            deta.setFieldInt("FACD_EXENTO2", rs.getInt("TKTD_EXENTO2"));
            deta.setFieldInt("FACD_EXENTO3", rs.getInt("TKTD_EXENTO3"));
            deta.setFieldInt("FACD_ESREGALO", rs.getInt("TKTD_ESREGALO"));
            deta.setFieldString("FACD_CVE", rs.getString("TKTD_CVE"));
            deta.setFieldString("FACD_COMENTARIO", rs.getString("TKTD_COMENTARIO"));
            deta.setFieldString("FACD_DESCRIPCION", rs.getString("TKTD_DESCRIPCION"));
            deta.setFieldDouble("FACD_TASAIVA1", rs.getDouble("TKTD_TASAIVA1"));
            deta.setFieldDouble("FACD_TASAIVA2", rs.getDouble("TKTD_TASAIVA2"));
            deta.setFieldDouble("FACD_TASAIVA3", rs.getDouble("TKTD_TASAIVA3"));
            deta.setFieldString("FACD_NOSERIE", "");
            deta.setFieldDouble("FACD_PRECIO", rs.getDouble("TKTD_PRECIO"));
            deta.setFieldDouble("FACD_PORDESC", rs.getDouble("TKTD_PORDESC"));
            deta.setFieldDouble("FACD_PRECREAL", rs.getDouble("TKTD_PRECREAL"));
            deta.setFieldInt("FACD_ESDEVO", rs.getInt("TKTD_ESDEVO"));
            deta.setFieldInt("FACD_PRECFIJO", rs.getInt("TKTD_PRECFIJO"));
            deta.setFieldInt("FACD_ESREGALO", rs.getInt("TKTD_ESREGALO"));
            deta.setFieldDouble("FACD_DESCUENTO", rs.getDouble("TDESC"));
            deta.setFieldDouble("FACD_IMPORTE", rs.getDouble("TIMPORTE"));
            deta.setFieldDouble("FACD_CANTIDAD", rs.getDouble("TCANTIDAD"));
            deta.setFieldDouble("FACD_IMPUESTO1", rs.getDouble("TIMPUESTO1"));
            deta.setFieldDouble("FACD_IMPUESTO2", rs.getDouble("TIMPUESTO2"));
            deta.setFieldDouble("FACD_IMPUESTO3", rs.getDouble("TIMPUESTO3"));
            deta.setFieldDouble("FACD_IMPORTEREAL", rs.getDouble("TIMPORTEREAL"));
            this.miFactura.AddDetalle(deta);
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(FacturaMasiva.class.getName()).log(Level.SEVERE, null, ex);
      }
      //this.strResultLast = "ERROR:ESTAMOS PROBANOD LA INICIADA DE LOS TICKETS....";
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Iniciar transaccion
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         //Guardamos la factura
         this.miFactura.setBolTransaccionalidad(false);
         miFactura.setBolAfectaInv(false);//Nodebe afectar inventarios
         miFactura.setBolAfectoCargosAbonos(false);//Nodebe afectar cargos/abonos
         this.miFactura.doTrx();
         if (this.miFactura.strResultLast.equals("OK")) {
            //Anulamos el ticket estableciendo el parametro
//            de que no afecte inventarios
            it = this.lstTickets.iterator();
            while (it.hasNext()) {
               Ticket tk = it.next();
//               tk.setBolAfectaInv(false);
//               tk.getDocument().setFieldInt("FAC_ID", Integer.valueOf(this.miFactura.getDocument().getValorKey()));
//               tk.setBolFacturaTicket(true);
//               tk.doTrxAnul();
//               tk.doTrxMod();
               String strUpdate = "update vta_tickets set FAC_ID = " + this.miFactura.getDocument().getValorKey() + " where TKT_ID = " + tk.getDocument().getFieldInt("TKT_ID");
               oConn.runQueryLMD(strUpdate);
//               if (!tk.getStrResultLast().equals("OK")) {
//                  this.strResultLast = this.miFactura.strResultLast;
//                  break;
//               }
            }
         } else {
            this.strResultLast = this.miFactura.strResultLast;
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
   }

   @Override
   public void doTrxAnul() {
      //Anulamos factura masiva y liberamos los tickets
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.miFactura.getDocument().getFieldInt("FAC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Inicializamos la operacion
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      //Validamos que no este anulado
      if (this.miFactura.getDocument().getFieldInt("FAC_ANULADA") == 0) {
         miFactura.setBolAfectaInv(false);//Nodebe afectar inventarios
         miFactura.setBolAfectoCargosAbonos(false);//Nodebe afectar cargos/abonos
         //Revivir Tickets
         Iterator<Ticket> it = this.lstTickets.iterator();
         while (it.hasNext()) {
            Ticket tk = it.next();
//            tk.setBolFacturaTicket(true);
//            tk.doTrxRevive();
//            if (!tk.getStrResultLast().equals("OK")) {
//               this.strResultLast = tk.getStrResultLast();
//            }
            String strUpdate = "update vta_tickets set FAC_ID = 0 where TKT_ID = " + tk.getDocument().getFieldInt("TKT_ID");
            oConn.runQueryLMD(strUpdate);
         }
         //Termina revivir tickets
         if (this.strResultLast.equals("OK")) {
            //Definimos campos
            this.miFactura.doTrxAnul();
            if (!this.miFactura.getStrResultLast().equals("OK")) {
               this.strResultLast = this.miFactura.getStrResultLast();
            }
         }
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

   @Override
   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * Regresa el objeto que representa la factura masiva
    *
    * @return Regresa un objeto de tipo Ticket
    */
   public Ticket getMiFactura() {
      return miFactura;
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(Ticket deta) {
      deta.Init();
      deta.setBolTransaccionalidad(false);
      deta.setBolAfectaInv(false);
      this.lstTickets.add(deta);
   }
}
