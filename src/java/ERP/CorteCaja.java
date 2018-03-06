package ERP;

import Tablas.vta_cortecaja;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 *Esta clase realiza las operaciones de corte de caja
 * @author zeus
 */
public class CorteCaja extends ProcesoMaster implements ProcesoInterfaz {

   protected vta_cortecaja corte;
   protected String strFechaAnul;
   protected String strHoraAnul;

   public CorteCaja(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.corte = new vta_cortecaja();
   }

   public void Init() {
      this.corte.setFieldInt("IDUSUARIO", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.corte.getFieldInt("CCJ_ID") != 0) {
         this.strFechaAnul = this.corte.getFieldString("CCJ_FECHAANUL");
         this.strHoraAnul = this.corte.getFieldString("CCJ_HORAANUL");
         this.corte.ObtenDatos(this.corte.getFieldInt("CCJ_ID"), oConn);
      }
   }

   public void doTrx() {
      this.strResultLast = "OK";
      this.corte.setFieldString("CCJ_FECHAREAL", this.fecha.getFechaActual());
      this.corte.setFieldString("CCJ_HORAREAL", this.fecha.getHoraActual());
      //Validamos La fecha
      if (this.corte.getFieldString("CCJ_FECHA").equals("")) {
         this.strResultLast = "ERROR:FALTA DEFINIR LA FECHA DEL CORTE";
      }
      //La sucursal
      if (this.corte.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:FALTA DEFINIR LA SUCURSAL";
      }
      if (this.strResultLast.equals("OK")) {
         boolean bolExiste = false;
         try {
            //que el corte no exista
            String strSql = "select * from vta_cortecaja "
                    + " where SC_ID = '" + this.corte.getFieldInt("SC_ID") + "'  "
                    + " AND CCJ_FECHA = '" + this.corte.getFieldString("CCJ_FECHA") + "'"
                    + " AND CCJ_TURNO = " + this.corte.getFieldInt("CCJ_TURNO")
                    + " AND CCJ_ANULADO = 0";
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               bolExiste = true;
            }
            rs.close();
         } catch (SQLException ex) {
            Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, ex);
         }
         if (bolExiste) {
            this.strResultLast = "ERROR:EL CORTE DE LA FECHA " + this.fecha.FormateaDDMMAAAA(this.corte.getFieldString("CCJ_FECHA"), "/") + " YA EXISTE";
         }
         //Si todas las validaciones pasan comenzamos el movimiento
         if (this.strResultLast.equals("OK")) {
            //Inicializamos la operacion
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            this.corte.setBolGetAutonumeric(true);
            //Guardamos el corte
            String strRes1 = this.corte.Agrega(oConn);
            if (strRes1.equals("OK")) {
               //Buscamos los importes de los pagos reales en el periodo
               String strSql = "SELECT MCD_FORMAPAGO,SUM(MCD_IMPORTE) AS TPAGO FROM vta_mov_cte,vta_mov_cte_deta "
                       + "where vta_mov_cte.MC_ID = vta_mov_cte_deta.MC_ID AND "
                       + " MC_ESPAGO = 1 AND MC_ANULADO = 0 AND vta_mov_cte.SC_ID = " + this.corte.getFieldInt("SC_ID")
                       + " AND vta_mov_cte.MC_FECHA <='" + this.corte.getFieldString("CCJ_FECHA") + "' "
                       + " GROUP BY MCD_FORMAPAGO";
               ResultSet rs;
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     if (rs.getString("MCD_FORMAPAGO").equals("EFECTIVO")) {
                        this.corte.setFieldDouble("CCJ_EFECTIVOREAL", rs.getDouble("TPAGO"));
                     }
                     if (rs.getString("MCD_FORMAPAGO").equals("CHEQUE")) {
                        this.corte.setFieldDouble("CCJ_CHEQUEREAL", rs.getDouble("TPAGO"));
                     }
                     if (rs.getString("MCD_FORMAPAGO").equals("TCREDITO")) {
                        this.corte.setFieldDouble("CCJ_TCREAL", rs.getDouble("TPAGO"));
                     }
                     if (rs.getString("MCD_FORMAPAGO").equals("SALDOFAVOR")) {
                        this.corte.setFieldDouble("CCJ_SALDOREAL", rs.getDouble("TPAGO"));
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, ex);
               }
               //Marcamos las remisiones del periodo o anteriores con este numero de corte
               String strUpdate = "update vta_tickets SET CCJ_ID = " + this.corte.getFieldInt("CCJ_ID")
                       + " WHERE CCJ_ID = 0 "
                       + " AND SC_ID =  " + this.corte.getFieldInt("SC_ID")
                       + " AND TKT_FECHA<='" + this.corte.getFieldString("CCJ_FECHA") + "'";
               oConn.runQueryLMD(strUpdate);
               //Marcamos las facturas  del periodo o anteriores con este numero de corte
               strUpdate = "update vta_facturas SET CCJ_ID = " + this.corte.getFieldInt("CCJ_ID")
                       + " WHERE CCJ_ID = 0 "
                       + " AND SC_ID =  " + this.corte.getFieldInt("SC_ID")
                       + " AND FAC_FECHA<='" + this.corte.getFieldString("CCJ_FECHA") + "'";
               oConn.runQueryLMD(strUpdate);
               //Marcamos los pagos o cargos del periodo o anteriores con este numero de corte
               strUpdate = "update vta_mov_cte SET CCJ_ID = " + this.corte.getFieldInt("CCJ_ID")
                       + " WHERE CCJ_ID = 0 "
                       + " AND SC_ID =  " + this.corte.getFieldInt("SC_ID")
                       + " AND MC_FECHA<='" + this.corte.getFieldString("CCJ_FECHA") + "'";
               oConn.runQueryLMD(strUpdate);
               //Buscamos los importe totales de ventas para guardarlos en el corte
               //*********TICKETS******
               strSql = "SELECT SUM(TKT_IMPORTE) AS TIMPORTE, "
                       + "SUM(TKT_IMPUESTO1) AS TIMPUESTO1, "
                       + "SUM(TKT_IMPUESTO2) AS TIMPUESTO2, "
                       + "SUM(TKT_IMPUESTO3) AS TIMPUESTO3, "
                       + "SUM(TKT_TOTAL) AS TTOTAL "
                       + " FROM vta_tickets WHERE CCJ_ID = " + this.corte.getFieldInt("CCJ_ID")
                       + " AND SC_ID = " + this.corte.getFieldInt("SC_ID")
                       + " AND (TKT_ANULADA = 0 OR (TKT_ANULADA = 1 AND FAC_ID <> 0) )";
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     this.corte.setFieldDouble("CCJ_TICKETIMPORTE", rs.getDouble("TIMPORTE"));
                     this.corte.setFieldDouble("CCJ_TOTALTICKETS", rs.getDouble("TTOTAL"));
                     this.corte.setFieldDouble("CCJ_TICKETIMPUESTO1", rs.getDouble("TIMPUESTO1"));
                     this.corte.setFieldDouble("CCJ_TICKETIMPUESTO2", rs.getDouble("TIMPUESTO2"));
                     this.corte.setFieldDouble("CCJ_TICKETIMPUESTO3", rs.getDouble("TIMPUESTO3"));
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, ex);
               }
               //*********FACTURAS******
               strSql = "SELECT SUM(FAC_IMPORTE) AS TIMPORTE, "
                       + "SUM(FAC_IMPUESTO1) AS TIMPUESTO1, "
                       + "SUM(FAC_IMPUESTO2) AS TIMPUESTO2, "
                       + "SUM(FAC_IMPUESTO3) AS TIMPUESTO3, "
                       + "SUM(FAC_TOTAL) AS TTOTAL "
                       + " FROM vta_facturas WHERE CCJ_ID = " + this.corte.getFieldInt("CCJ_ID")
                       + " AND SC_ID = " + this.corte.getFieldInt("SC_ID")
                       + " AND FAC_ANULADA = 0 AND FAC_ESMASIVA = 0";
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     this.corte.setFieldDouble("CCJ_FACTURAIMPORTE", rs.getDouble("TIMPORTE"));
                     this.corte.setFieldDouble("CCJ_TOTALFACTURA", rs.getDouble("TTOTAL"));
                     this.corte.setFieldDouble("CCJ_FACTURAIMPUESTO1", rs.getDouble("TIMPUESTO1"));
                     this.corte.setFieldDouble("CCJ_FACTURAIMPUESTO2", rs.getDouble("TIMPUESTO2"));
                     this.corte.setFieldDouble("CCJ_FACTURAIMPUESTO3", rs.getDouble("TIMPUESTO3"));
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, ex);
               }
               //*********PAGOS******
               strSql = "SELECT "
                       + "SUM(MC_IMPUESTO1) AS TIMPUESTO1, "
                       + "SUM(MC_IMPUESTO2) AS TIMPUESTO2, "
                       + "SUM(MC_IMPUESTO3) AS TIMPUESTO3, "
                       + "SUM(MC_ABONO) AS TTOTAL "
                       + " FROM vta_mov_cte WHERE CCJ_ID = " + this.corte.getFieldInt("CCJ_ID")
                       + " AND SC_ID = " + this.corte.getFieldInt("SC_ID")
                       + " AND MC_ANULADO = 0 ";
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     this.corte.setFieldDouble("CCJ_PAGOIMPORTE", (rs.getDouble("TTOTAL") - rs.getDouble("TIMPUESTO1") - rs.getDouble("TIMPUESTO2") - rs.getDouble("TIMPUESTO3")));
                     this.corte.setFieldDouble("CCJ_TOTALPAGOS", rs.getDouble("TTOTAL"));
                     this.corte.setFieldDouble("CCJ_PAGOIMPUESTO1", rs.getDouble("TIMPUESTO1"));
                     this.corte.setFieldDouble("CCJ_PAGOIMPUESTO2", rs.getDouble("TIMPUESTO2"));
                     this.corte.setFieldDouble("CCJ_PAGOIMPUESTO3", rs.getDouble("TIMPUESTO3"));
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(CorteCaja.class.getName()).log(Level.SEVERE, null, ex);
               }
               //Guardamos los importes
               strRes1 = this.corte.Modifica(oConn);
               if (strRes1.equals("OK")) {
                  this.saveBitacora("CORTE CAJA", "NEW", this.corte.getFieldInt("CCJ_ID"));
               } else {
                  this.strResultLast = strRes1;
               }
            } else {
               this.strResultLast = strRes1;
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
   }

   public void doTrxAnul() {
      this.strResultLast = "OK";
      //Al anular Marcamos la operacion
      //Validamos que todos los campos basico se encuentren
      if (this.corte.getFieldInt("CCJ_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Inicializamos la operacion
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      //Validamos que no este anulado
      if (this.corte.getFieldInt("CCJ_ANULADO") == 0) {
         //Quitamos la marca de las operaciones
         //Remisiones o tickets
         String strUpdate = "update vta_tickets SET CCJ_ID = 0 "
                 + " WHERE "
                 + " SC_ID =  " + this.corte.getFieldInt("SC_ID")
                 + " AND CCJ_ID='" + this.corte.getFieldInt("CCJ_ID") + "'";
         oConn.runQueryLMD(strUpdate);
         //Facturas
         strUpdate = "update vta_facturas SET CCJ_ID = 0 "
                 + " WHERE "
                 + " SC_ID =  " + this.corte.getFieldInt("SC_ID")
                 + " AND CCJ_ID='" + this.corte.getFieldInt("CCJ_ID") + "'";
         oConn.runQueryLMD(strUpdate);
         //Pagos o cargos
         strUpdate = "update vta_mov_cte SET CCJ_ID = 0 "
                 + " WHERE "
                 + " SC_ID =  " + this.corte.getFieldInt("SC_ID")
                 + " AND CCJ_ID='" + this.corte.getFieldInt("CCJ_ID") + "'";
         oConn.runQueryLMD(strUpdate);
         //Definimos campos
         this.corte.setFieldInt("IDUSUARIOANULA", this.varSesiones.getIntNoUser());
         this.corte.setFieldInt("CCJ_ANULADO", 1);
         if (strFechaAnul.equals("")) {
            this.corte.setFieldString("CCJ_FECHAANUL", this.fecha.getFechaActual());
            this.corte.setFieldString("CCJ_HORAANUL", this.fecha.getHoraActual());
         } else {
            this.corte.setFieldString("CCJ_FECHAANUL", strFechaAnul);
            this.corte.setFieldString("CCJ_HORAANUL", this.strHoraAnul);
         }
         String strResp1 = this.corte.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         }
         this.saveBitacora("CORTE CAJA", "ANULAR", this.corte.getFieldInt("CCJ_ID"));
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

   /**
    * Regresa el objeto corte de caja con los campos del corte
    * @return Regresa el objeto que representa la tabla
    */
   public vta_cortecaja getCorte() {
      return corte;
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
}
