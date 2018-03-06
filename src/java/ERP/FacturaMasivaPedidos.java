/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_facturadeta;
import Tablas.vta_ticketsdeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Genera la facturacion masiva de los pedidos
 *
 * @author aleph_79
 */
public class FacturaMasivaPedidos extends FacturaMasiva {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(FacturaMasivaPedidos.class.getName());
   private String strTipoVta;
   String strFiltro = "";
   int intPedidosPagados = 0;
   double dblClientesSaldoMenor = 0;
   String strFechaFactura = "";
   private int intContadorExitosos;
   private int intContadorFallidos;

   public int getIntContadorExitosos() {
      return intContadorExitosos;
   }

   public void setIntContadorExitosos(int intContadorExitosos) {
      this.intContadorExitosos = intContadorExitosos;
   }

   public int getIntContadorFallidos() {
      return intContadorFallidos;
   }

   public void setIntContadorFallidos(int intContadorFallidos) {
      this.intContadorFallidos = intContadorFallidos;
   }

   public String getStrTipoVta() {
      return strTipoVta;
   }

   public void setStrTipoVta(String strTipoVta) {
      this.strTipoVta = strTipoVta;
   }

   public int getIntPedidosPagados() {
      return intPedidosPagados;
   }

   public void setIntPedidosPagados(int intPedidosPagados) {
      this.intPedidosPagados = intPedidosPagados;
   }

   public double getDblClientesSaldoMenor() {
      return dblClientesSaldoMenor;
   }

   public void setDblClientesSaldoMenor(double dblClientesSaldoMenor) {
      this.dblClientesSaldoMenor = dblClientesSaldoMenor;
   }

   public String getStrFechaFactura() {
      return strFechaFactura;
   }

   public void setStrFechaFactura(String strFechaFactura) {
      this.strFechaFactura = strFechaFactura;
   }

   public String getStrFiltro() {
      return strFiltro;
   }

   public void setStrFiltro(String strFiltro) {
      this.strFiltro = strFiltro;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public FacturaMasivaPedidos(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.strTipoVta = Ticket.TICKET;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   @Override
   public void Init() {

      //Recuperamos todos los pedidos con el filtro correspondiente
      String strSql = "SELECT PD_ID FROM vta_pedidos WHERE TKT_ID = 0 AND FAC_ID = 0 " + strFiltro;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intPD_ID = rs.getInt("PD_ID");
            Ticket mov = new Ticket(oConn, varSesiones, request);
            mov.setStrTipoVta(Ticket.PEDIDO);
            mov.getDocument().setFieldInt("PD_ID", intPD_ID);
            mov.Init();
            this.lstTickets.add(mov);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error("SQLException " + ex.getMessage());
      }
   }

   @Override
   public void doTrx() {
      //Generamos una FACTURA de PEDIDOS O
      this.strResultLast = "OK";
      intContadorExitosos = 0;
      this.intContadorFallidos = 0;
      String strPrefijoMaster = "FAC_";
      String strPrefijoDeta = "FACD_";
      if (this.strTipoVta.equals(Ticket.TICKET)) {
         strPrefijoMaster = "TKT_";
         strPrefijoDeta = "TKTD_";
      }

      Iterator<Ticket> it = this.lstTickets.iterator();
      while (it.hasNext()) {
         Ticket pedido = it.next();

         String strResult0 = "OK";
         boolean bolGeneraVenta = true;
         // <editor-fold defaultstate="collapsed" desc="Validamos si se genera la venta">
         //Validamos que halla un saldo a favor que sustente el pedido
         if (this.intPedidosPagados == 1 || this.dblClientesSaldoMenor > 0) {
            bolGeneraVenta = false;
            double dblSaldo = 0;

            //Buscamos el saldo del cliente
            String strSql = "SELECT "
                    + " CT_SALDO "
                    + " FROM vta_cliente WHERE CT_ID = " + pedido.getDocument().getFieldInt("CT_ID")
                    + " ";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  dblSaldo = rs.getDouble("CT_SALDO");
               }
               rs.close();
            } catch (SQLException ex) {
               log.error("SQLException " + ex.getMessage());
            }
            //Validacion saldo pedido
            if (this.intPedidosPagados == 1) {
               if (dblSaldo >= pedido.getDocument().getFieldDouble("PD_TOTAL")) {
                  bolGeneraVenta = true;
               } else {
                  strResult0 = "El pedido " + pedido.getDocument().getFieldString("PD_FOLIO")
                          + "(" + pedido.getDocument().getFieldString("PD_RAZONSOCIAL")
                          + ") no tiene un saldo para pagarlo\n";
               }
            }
            //Validacion deudas(creditos)
            if (this.dblClientesSaldoMenor > 0) {
               if (dblSaldo <= this.dblClientesSaldoMenor) {
                  bolGeneraVenta = true;
               } else {
                  strResult0 = "El cliente " + pedido.getDocument().getFieldString("PD_RAZONSOCIAL") + " no cumple con el saldo menor\n";
               }
            }
         }
         // </editor-fold>

         if (bolGeneraVenta) {
            Ticket venta = new Ticket(oConn, varSesiones, request);
            // <editor-fold defaultstate="collapsed" desc="Asignamos valores globales">
            venta.setStrTipoVta(this.strTipoVta);
            venta.Init();

            venta.getDocument().setFieldString(strPrefijoMaster + "RAZONSOCIAL", pedido.getDocument().getFieldString("PD_RAZONSOCIAL"));
            venta.getDocument().setFieldString(strPrefijoMaster + "RFC", pedido.getDocument().getFieldString("PD_RFC"));
            venta.getDocument().setFieldString(strPrefijoMaster + "CALLE", pedido.getDocument().getFieldString("PD_CALLE"));
            venta.getDocument().setFieldString(strPrefijoMaster + "COLONIA", pedido.getDocument().getFieldString("PD_COLONIA"));
            venta.getDocument().setFieldString(strPrefijoMaster + "LOCALIDAD", pedido.getDocument().getFieldString("PD_LOCALIDAD"));
            venta.getDocument().setFieldString(strPrefijoMaster + "MUNICIPIO", pedido.getDocument().getFieldString("PD_MUNICIPIO"));
            venta.getDocument().setFieldString(strPrefijoMaster + "ESTADO", pedido.getDocument().getFieldString("PD_ESTADO"));
            venta.getDocument().setFieldString(strPrefijoMaster + "CP", pedido.getDocument().getFieldString("PD_CP"));
            venta.getDocument().setFieldString(strPrefijoMaster + "NOTAS", pedido.getDocument().getFieldString("PD_NOTAS"));
            venta.getDocument().setFieldString(strPrefijoMaster + "HORANUL", pedido.getDocument().getFieldString("PD_HORANUL"));
            venta.getDocument().setFieldString(strPrefijoMaster + "NOTASPIE", pedido.getDocument().getFieldString("PD_NOTASPIE"));
            venta.getDocument().setFieldString(strPrefijoMaster + "REFERENCIA", pedido.getDocument().getFieldString("PD_REFERENCIA"));
            venta.getDocument().setFieldString(strPrefijoMaster + "CONDPAGO", pedido.getDocument().getFieldString("PD_CONDPAGO"));
//            venta.getDocument().setFieldString(strPrefijoMaster + "VENCI", pedido.getDocument().getFieldString("PD_VENCI"));
            venta.getDocument().setFieldString(strPrefijoMaster + "NOMFORMATO", pedido.getDocument().getFieldString("PD_NOMFORMATO"));
            venta.getDocument().setFieldString(strPrefijoMaster + "NUMPEDI", pedido.getDocument().getFieldString("PD_NUMPEDI"));
            venta.getDocument().setFieldString(strPrefijoMaster + "FECHAPEDI", pedido.getDocument().getFieldString("PD_FECHAPEDI"));
            venta.getDocument().setFieldString(strPrefijoMaster + "ADUANA", pedido.getDocument().getFieldString("PD_ADUANA"));
            venta.getDocument().setFieldString(strPrefijoMaster + "FORMADEPAGO", pedido.getDocument().getFieldString("PD_FORMADEPAGO"));
            venta.getDocument().setFieldString(strPrefijoMaster + "NUMCUENTA", pedido.getDocument().getFieldString("PD_NUMCUENTA"));
            venta.getDocument().setFieldString(strPrefijoMaster + "REGIMENFISCAL", pedido.getDocument().getFieldString("PD_REGIMENFISCAL"));
            venta.getDocument().setFieldString(strPrefijoMaster + "NUM_GUIA", pedido.getDocument().getFieldString("PD_NUM_GUIA"));
            venta.getDocument().setFieldString(strPrefijoMaster + "RECU_FINAL", pedido.getDocument().getFieldString("PD_RECU_FINAL"));

            venta.getDocument().setFieldDouble(strPrefijoMaster + "TASA1", pedido.getDocument().getFieldDouble("PD_TASA1"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "TASA2", pedido.getDocument().getFieldDouble("PD_TASA2"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "TASA3", pedido.getDocument().getFieldDouble("PD_TASA3"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "GANANCIA", pedido.getDocument().getFieldDouble("PD_GANANCIA"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "TASAPESO", pedido.getDocument().getFieldDouble("PD_TASAPESO"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "COSTO", pedido.getDocument().getFieldDouble("PD_COSTO"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "DESCUENTO", pedido.getDocument().getFieldDouble("PD_DESCUENTO"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "RETISR", pedido.getDocument().getFieldDouble("PD_RETISR"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "RETIVA", pedido.getDocument().getFieldDouble("PD_RETIVA"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "NETO", pedido.getDocument().getFieldDouble("PD_NETO"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPORTE_IEPS", pedido.getDocument().getFieldDouble("PD_IMPORTE_IEPS"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPORTE_FLETE", pedido.getDocument().getFieldDouble("PD_IMPORTE_FLETE"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPORTE_PUNTOS", pedido.getDocument().getFieldDouble("PD_IMPORTE_PUNTOS"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPORTE_NEGOCIO", pedido.getDocument().getFieldDouble("PD_IMPORTE_NEGOCIO"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "POR_DESCUENTO", pedido.getDocument().getFieldDouble("PD_POR_DESCUENTO"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPORTE", pedido.getDocument().getFieldDouble("PD_IMPORTE"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPUESTO1", pedido.getDocument().getFieldDouble("PD_IMPUESTO1"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPUESTO2", pedido.getDocument().getFieldDouble("PD_IMPUESTO2"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "IMPUESTO3", pedido.getDocument().getFieldDouble("PD_IMPUESTO3"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "TOTAL", pedido.getDocument().getFieldDouble("PD_TOTAL"));
            venta.getDocument().setFieldDouble(strPrefijoMaster + "SALDO", pedido.getDocument().getFieldDouble("PD_SALDO"));

            venta.getDocument().setFieldInt("CT_ID", pedido.getDocument().getFieldInt("CT_ID"));
            venta.getDocument().setFieldInt("SC_ID", pedido.getDocument().getFieldInt("SC_ID"));
            venta.getDocument().setFieldInt("EMP_ID", pedido.getDocument().getFieldInt("EMP_ID"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "O_REM", pedido.getDocument().getFieldInt("PD_O_REM"));
//            venta.getDocument().setFieldInt(strPrefijoMaster + "ES_ID", pedido.getDocument().getFieldInt("ES_ID"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "ANULADA", pedido.getDocument().getFieldInt("PD_ANULADA"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "DIASCREDITO", pedido.getDocument().getFieldInt("PD_DIASCREDITO"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "LPRECIOS", pedido.getDocument().getFieldInt("PD_LPRECIOS"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "IDNC", pedido.getDocument().getFieldInt("PD_IDNC"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "ES_SURTIDO", pedido.getDocument().getFieldInt("PD_ES_SURTIDO"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "US_ALTA", pedido.getDocument().getFieldInt("PD_US_ALTA"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "US_ANUL", pedido.getDocument().getFieldInt("PD_US_ANUL"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "US_MOD", pedido.getDocument().getFieldInt("PD_US_MOD"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "MONEDA", pedido.getDocument().getFieldInt("PD_MONEDA"));
            venta.getDocument().setFieldInt("VE_ID", pedido.getDocument().getFieldInt("VE_ID"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "ESSERV", pedido.getDocument().getFieldInt("PD_ESSERV"));
            venta.getDocument().setFieldInt("TKT_ID", 0 );
            venta.getDocument().setFieldInt("FAC_ID", 0);
            venta.getDocument().setFieldInt(strPrefijoMaster + "ESRECU", pedido.getDocument().getFieldInt("PD_ESRECU"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "PERIODICIDAD", pedido.getDocument().getFieldInt("PD_PERIODICIDAD"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "DIAPER", pedido.getDocument().getFieldInt("PD_DIAPER"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "ESARRENDA", pedido.getDocument().getFieldInt("PD_ESARRENDA"));
            venta.getDocument().setFieldInt("TI_ID", pedido.getDocument().getFieldInt("TI_ID"));
            venta.getDocument().setFieldInt("TI_ID2", pedido.getDocument().getFieldInt("TI_ID2"));
            venta.getDocument().setFieldInt("TI_ID3", pedido.getDocument().getFieldInt("TI_ID3"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "USO_IEPS", pedido.getDocument().getFieldInt("PD_USO_IEPS"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "TASA_IEPS", pedido.getDocument().getFieldInt("PD_TASA_IEPS"));
//            venta.getDocument().setFieldInt(strPrefijoMaster + "STATUS", pedido.getDocument().getFieldInt("PD_STATUS"));
//            venta.getDocument().setFieldInt(strPrefijoMaster + "APARTADO", pedido.getDocument().getFieldInt("PD_APARTADO"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "TIPO_FLETE", pedido.getDocument().getFieldInt("PD_TIPO_FLETE"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "NO_MLM", pedido.getDocument().getFieldInt("PD_NO_MLM"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "CONSIGNACION", pedido.getDocument().getFieldInt("PD_CONSIGNACION"));
            venta.getDocument().setFieldInt("CDE_ID", pedido.getDocument().getFieldInt("CDE_ID"));
            venta.getDocument().setFieldInt("DFA_ID", pedido.getDocument().getFieldInt("DFA_ID"));
            venta.getDocument().setFieldInt("MPE_ID", pedido.getDocument().getFieldInt("MPE_ID"));
            venta.getDocument().setFieldInt(strPrefijoMaster + "NO_EVENTOS", pedido.getDocument().getFieldInt("PD_NO_EVENTOS"));
            venta.getDocument().setFieldInt("TR_ID", pedido.getDocument().getFieldInt("TR_ID"));
            venta.getDocument().setFieldInt("ME_ID", pedido.getDocument().getFieldInt("ME_ID"));
            venta.getDocument().setFieldInt("TF_ID", pedido.getDocument().getFieldInt("TF_ID"));
            venta.getDocument().setFieldInt("COT_ID", pedido.getDocument().getFieldInt("COT_ID"));

            venta.getDocument().setFieldString(strPrefijoMaster + "FECHACREATE", this.fecha.getFechaActual());
            venta.getDocument().setFieldString(strPrefijoMaster + "HORA", this.fecha.getHoraActual());
            venta.getDocument().setFieldString(strPrefijoMaster + "FECHA", this.strFechaFactura);
            //Validamos que todos los campos basico se encuentren
            if (venta.getDocument().getFieldString(strPrefijoMaster + "FECHA").equals("")) {
               strResult0 = "ERROR:Falta definir la fecha de la operacion para el pedido con id " + pedido.getDocument().getFieldInt("PD_ID") + " \n";
            }
            if (venta.getDocument().getFieldString("CT_ID").equals("0")) {
               strResult0 = "ERROR:Falta definir un cliente para el pedido con id " + pedido.getDocument().getFieldInt("PD_ID") + " \n";
            }
            if (venta.getDocument().getFieldString("SC_ID").equals("0")) {
               strResult0 = "ERROR:Falta definir una sucursal para el pedido con id " + pedido.getDocument().getFieldInt("PD_ID") + " \n";
            }
            log.debug("Pedido por generar venta "
                    + pedido.getDocument().getFieldInt("PD_ID") + " "
                    + venta.getDocument().getFieldDouble(strPrefijoMaster + "IMPORTE_PUNTOS")
                    + venta.getDocument().getFieldDouble(strPrefijoMaster + "IMPORTE_NEGOCIO")
            );
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Obtenemos la lista de los productos de estos documentos">
            String strSql = "SELECT "
                    + " * "
                    + " FROM vta_pedidosdeta WHERE vta_pedidosdeta.PD_ID IN(" + pedido.getDocument().getFieldInt("PD_ID") + ")"
                    + " ";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  //Anadimos partidas a la factura
                  TableMaster deta = new vta_facturadeta();
                  if (this.strTipoVta.equals(Ticket.TICKET)) {
                     deta = new vta_ticketsdeta();
                  }
                  deta.setFieldInt("SC_ID", rs.getInt("SC_ID"));
                  deta.setFieldInt("PR_ID", rs.getInt("PR_ID"));
                  deta.setFieldInt(strPrefijoDeta + "EXENTO1", rs.getInt("PDD_EXENTO1"));
                  deta.setFieldInt(strPrefijoDeta + "EXENTO2", rs.getInt("PDD_EXENTO2"));
                  deta.setFieldInt(strPrefijoDeta + "EXENTO3", rs.getInt("PDD_EXENTO3"));
                  deta.setFieldInt(strPrefijoDeta + "ESREGALO", rs.getInt("PDD_ESREGALO"));
                  deta.setFieldString(strPrefijoDeta + "CVE", rs.getString("PDD_CVE"));
                  deta.setFieldString(strPrefijoDeta + "COMENTARIO", rs.getString("PDD_COMENTARIO"));
                  deta.setFieldString(strPrefijoDeta + "DESCRIPCION", rs.getString("PDD_DESCRIPCION"));
                  deta.setFieldDouble(strPrefijoDeta + "TASAIVA1", rs.getDouble("PDD_TASAIVA1"));
                  deta.setFieldDouble(strPrefijoDeta + "TASAIVA2", rs.getDouble("PDD_TASAIVA2"));
                  deta.setFieldDouble(strPrefijoDeta + "TASAIVA3", rs.getDouble("PDD_TASAIVA3"));
                  deta.setFieldString(strPrefijoDeta + "NOSERIE", rs.getString("PDD_NOSERIE"));
                  deta.setFieldDouble(strPrefijoDeta + "PRECIO", rs.getDouble("PDD_PRECIO"));
                  deta.setFieldDouble(strPrefijoDeta + "PORDESC", rs.getDouble("PDD_PORDESC"));
                  deta.setFieldDouble(strPrefijoDeta + "PRECREAL", rs.getDouble("PDD_PRECREAL"));

                  deta.setFieldInt(strPrefijoDeta + "PRECFIJO", rs.getInt("PDD_PRECFIJO"));
                  deta.setFieldInt(strPrefijoDeta + "ESREGALO", rs.getInt("PDD_ESREGALO"));
                  deta.setFieldDouble(strPrefijoDeta + "DESCUENTO", rs.getDouble("PDD_DESCUENTO"));
                  deta.setFieldDouble(strPrefijoDeta + "IMPORTE", rs.getDouble("PDD_IMPORTE"));
                  deta.setFieldDouble(strPrefijoDeta + "CANTIDAD", rs.getDouble("PDD_CANTIDAD"));
                  deta.setFieldDouble(strPrefijoDeta + "IMPUESTO1", rs.getDouble("PDD_IMPUESTO1"));
                  deta.setFieldDouble(strPrefijoDeta + "IMPUESTO2", rs.getDouble("PDD_IMPUESTO2"));
                  deta.setFieldDouble(strPrefijoDeta + "IMPUESTO3", rs.getDouble("PDD_IMPUESTO3"));
                  deta.setFieldDouble(strPrefijoDeta + "IMPORTEREAL", rs.getDouble("PDD_IMPORTEREAL"));
                  deta.setFieldDouble(strPrefijoDeta + "PUNTOS", rs.getDouble("PDD_PUNTOS"));
                  deta.setFieldDouble(strPrefijoDeta + "VNEGOCIO", rs.getDouble("PDD_VNEGOCIO"));
                  deta.setFieldDouble(strPrefijoDeta + "IMP_PUNTOS", rs.getDouble("PDD_IMP_PUNTOS"));
                  deta.setFieldDouble(strPrefijoDeta + "IMP_VNEGOCIO", rs.getDouble("PDD_IMP_VNEGOCIO"));
                  deta.setFieldInt(strPrefijoDeta + "DESC_PREC", rs.getInt("PDD_DESC_PREC"));
                  deta.setFieldInt(strPrefijoDeta + "DESC_PUNTOS", rs.getInt("PDD_DESC_PUNTOS"));
                  deta.setFieldInt(strPrefijoDeta + "DESC_VNEGOCIO", rs.getInt("PDD_DESC_VNEGOCIO"));
                  deta.setFieldDouble(strPrefijoDeta + "DESC_ORI", rs.getDouble("PDD_DESC_ORI"));
                  deta.setFieldInt(strPrefijoDeta + "REGALO", rs.getInt("PDD_REGALO"));
                  deta.setFieldInt(strPrefijoDeta + "ID_PROMO", rs.getInt("PDD_ID_PROMO"));
                  deta.setFieldInt(strPrefijoDeta + "EXEN_RET_ISR", rs.getInt("PDD_EXEN_RET_ISR"));
                  deta.setFieldInt(strPrefijoDeta + "EXEN_RET_IVA", rs.getInt("PDD_EXEN_RET_IVA"));
                  deta.setFieldInt("CF_ID", rs.getInt("CF_ID"));
                  venta.AddDetalle(deta);
               }
               rs.close();
            } catch (SQLException ex) {
               log.error("SQLException " + ex.getMessage());
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Guardado de ventas">
            //Validamos si pasamos las validaciones
            if (strResult0.equals("OK")) {
               //Iniciar transaccion
               if (this.bolTransaccionalidad) {
                  this.oConn.runQueryLMD("BEGIN");
               }
               //Guardamos la factura
               venta.setBolTransaccionalidad(false);
               venta.doTrx();
               String strResult1 = venta.strResultLast;
               if (strResult1.equals("OK")) {
                  String strUpdatePedido = "update vta_pedidos set FAC_ID =  " + venta.getDocument().getValorKey()
                          + " WHERE PD_ID =  " + pedido.getDocument().getFieldInt("PD_ID");
                  if (this.strTipoVta.equals(Ticket.TICKET)) {
                     strUpdatePedido = "update vta_pedidos set TKT_ID =  " + venta.getDocument().getValorKey()
                             + " WHERE PD_ID =  " + pedido.getDocument().getFieldInt("PD_ID");
                  }
                  oConn.runQueryLMD(strUpdatePedido);
               } else {
                  this.strResultLast += strResult1;
               }
               //Terminamos la operacion
               if (this.bolTransaccionalidad) {
                  if (strResult1.equals("OK")) {
                     this.oConn.runQueryLMD("COMMIT");
                     intContadorExitosos++;
                  } else {
                     this.oConn.runQueryLMD("ROLLBACK");
                     this.intContadorFallidos++;
                  }
               }
            } else {
               this.strResultLast += strResult0;
               this.intContadorFallidos++;
            }
            // </editor-fold>
         } else {
            this.strResultLast += strResult0;
            this.intContadorFallidos++;
         }

      }

   }
   // </editor-fold>
}
