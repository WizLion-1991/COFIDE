/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.casajosefa;

import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision_deta;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase genera el calculo de comision de la empresa Firm o Jonfilu
 *
 * @author aleph_79
 */
public class CalculoComision extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculoComision.class.getName());
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public CalculoComision(Conexion oConn, int intPeriodo, boolean EsCorridaDefinitiva) {
      super(oConn, intPeriodo, EsCorridaDefinitiva);
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void doFase1() {
      super.doFase1();
      //Si todo es ok limpiamos banderas
      if (this.strResultLast.equals("OK")) {
         String strSQL = "UPDATE vta_cliente SET ";
         strSQL += "CT_PPUNTOS    =  0,";
         strSQL += "CT_GPUNTOS    =  0,";
         strSQL += "CT_PNEGOCIO   =  0,";
         strSQL += "CT_GNEGOCIO   =  0,";
         strSQL += "CT_COMISION    =  0,";
         strSQL += "CT_NIVELRED    =  0,";
         strSQL += "CT_CONTEO_HIJOS    =  0,";
         strSQL += "CT_VENTA_REFERENCIADO    =  0,";
         strSQL += "CJ_ST1_N  =  0,";
         strSQL += "CJ_ST2_N  =  0,";
         strSQL += "CJ_ST3_N  =  0,";
         strSQL += "CJ_ST4_N  =  0,";
         strSQL += "CJ_ST1_P  =  0,";
         strSQL += "CJ_ST2_P  =  0,";
         strSQL += "CJ_ST3_P  =  0,";
         strSQL += "CJ_ST4_P  =  0 ";
         this.oConn.runQueryLMD(strSQL);
      }
   }

   /**
    * Calculo de documentos facturas, remisiones y memorandas Puntos y valor
    * negocio, por cliente
    */
   @Override
   public void doFase2() {
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 2.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

      //inicializamos valores
      Map mapaClientes = new HashMap();
      this.strResultLast = "OK";

      //Obtenemos las monedas y paridades
      lstTasaCambio lstTasas = new lstTasaCambio();

      String strSql = "SELECT FAC_MONEDA FROM vta_facturas group by FAC_MONEDA";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            if (rs.getInt("FAC_MONEDA") != 1) {
               //Generamos objeto de conversion
               TasaCambio tCambio = new TasaCambio();
               tCambio.setMoneda1(1);
               tCambio.setMoneda2(rs.getInt("FAC_MONEDA"));
               //Buscamos la tasa de cambio de esta moneda
               String strSqlT = "select * from vta_tasacambio where "
                       + " (TC_MONEDA1 = 1 and TC_MONEDA2 = " + tCambio.getMoneda2() + ") || "
                       + " (TC_MONEDA2 = 1 and TC_MONEDA1 = " + tCambio.getMoneda2() + ") "
                       + " order by TC_FECHA DESC LIMIT 0,1";
               ResultSet rs2 = this.oConn.runQuery(strSqlT, true);
               while (rs2.next()) {
                  tCambio.setValor(rs.getDouble("TC_PARIDAD"));
               }
//               if(rs2.getStatement() != null )rs2.getStatement().close(); 
               rs2.close();
               lstTasas.Agrega(tCambio);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         //Iniciamos transaccion
         this.oConn.runQueryLMD("BEGIN");
         double dblPuntos = 0;
         double dblNegocio = 0;
         //---------------------------- CARGA DE DOCUMENTOS DE FACTURA(SOLO MLM)
         strSql = "SELECT vta_facturas.CT_ID,vta_facturas.FAC_MONEDA,"
                 + " sum(vta_facturas.FAC_IMPORTE_PUNTOS) AS PPUNTOS, "
                 + " sum(vta_facturas.FAC_IMPORTE_NEGOCIO) AS PNEGOCIO "
                 + " FROM vta_cliente,vta_facturas WHERE vta_facturas.MPE_ID =  " + this.intPeriodo
                 + " and vta_facturas.CT_ID = vta_cliente.CT_ID "
                 + " and vta_cliente.CT_CATEGORIA1 = 1 "
                 + " and vta_facturas.FAC_ANULADA = 0 "
                 + " and vta_facturas.FAC_NO_MLM = 0 "
                 //Usamos bandera para restringir los clientes preferentes
                 + " GROUP BY vta_facturas.CT_ID,vta_facturas.FAC_MONEDA";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("PPUNTOS");
            dblNegocio = rs.getDouble("PNEGOCIO");
            //Realizamos la conversion de las monedas
            if (rs.getInt("FAC_MONEDA") != 1) {
               Iterator<TasaCambio> it = lstTasas.getLista().iterator();
               while (it.hasNext()) {
                  TasaCambio tasaCambio = it.next();
                  if (tasaCambio.getMoneda2() == rs.getInt("FAC_MONEDA")) {
                     dblNegocio = dblNegocio * tasaCambio.getValor();
                  }
               }
            }
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //CARGA DE DOCUMENTOS DE TICKETS(SOLO MLM)
         strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
                 + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
                 + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
                 + " FROM vta_cliente,vta_tickets WHERE vta_tickets.MPE_ID =  " + this.intPeriodo
                 + " and vta_tickets.CT_ID = vta_cliente.CT_ID "
                 + " and vta_cliente.CT_CATEGORIA1 = 1 "
                 + " and vta_tickets.TKT_ANULADA = 0 "
                 + " and vta_tickets.TKT_NO_MLM = 0 "
                 + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("PPUNTOS");
            dblNegocio = rs.getDouble("PNEGOCIO");
            //Realizamos la conversion de las monedas
            if (rs.getInt("TKT_MONEDA") != 1) {
               Iterator<TasaCambio> it = lstTasas.getLista().iterator();
               while (it.hasNext()) {
                  TasaCambio tasaCambio = it.next();
                  if (tasaCambio.getMoneda2() == rs.getInt("TKT_MONEDA")) {
                     dblNegocio = dblNegocio * tasaCambio.getValor();
                  }
               }
            }
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //CARGA DE MEMORANDA(SOLO MLM)
         strSql = "SELECT "
                 + "	mlm_bono_ind.CT_ID, 	"
                 + " SUM(mlm_bono_ind.MBI_PUNTOS) AS TPUNTOS, 	"
                 + " SUM(mlm_bono_ind.MBI_NEGOCIO) AS TNEGOCIO  "
                 + " FROM vta_cliente,mlm_bono_ind WHERE mlm_bono_ind.MPE_ID =   " + this.intPeriodo
                 + " and mlm_bono_ind.CT_ID = mlm_bono_ind.CT_ID "
                 + " and vta_cliente.CT_CATEGORIA1 = 1 "
                 + " GROUP BY mlm_bono_ind.CT_ID";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("TPUNTOS");
            dblNegocio = rs.getDouble("TNEGOCIO");
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         try {
            //---------------------------- CARGA DE DOCUMENTOS DE FACTURA(REFERENCIADOS)
            strSql = "SELECT vta_facturas.CT_ID,vta_facturas.FAC_MONEDA,"
                    + " sum(vta_facturas.FAC_IMPORTE) AS PIMPORTE, "
                    + " sum(vta_facturas.FAC_IMPORTE_PUNTOS) AS PPUNTOS, "
                    + " sum(vta_facturas.FAC_IMPORTE_NEGOCIO) AS PNEGOCIO "
                    + " FROM vta_cliente,vta_facturas WHERE vta_facturas.MPE_ID =  " + this.intPeriodo
                    + " and vta_facturas.CT_ID = vta_cliente.CT_ID "
                    + " and vta_cliente.CT_CATEGORIA1 <> 1 "
                    + " and vta_facturas.FAC_ANULADA = 0 "
                    + " and vta_facturas.FAC_NO_MLM = 0 "
                    //Usamos bandera para restringir los clientes preferentes
                    + " GROUP BY vta_facturas.CT_ID,vta_facturas.FAC_MONEDA";
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
               double dblImporte = rs.getDouble("PIMPORTE");
               //Realizamos la conversion de las monedas
               if (rs.getInt("FAC_MONEDA") != 1) {
                  Iterator<TasaCambio> it = lstTasas.getLista().iterator();
                  while (it.hasNext()) {
                     TasaCambio tasaCambio = it.next();
                     if (tasaCambio.getMoneda2() == rs.getInt("FAC_MONEDA")) {
                        dblImporte = dblImporte * tasaCambio.getValor();
                     }
                  }
               }
               //Evaluamos si ya existe el objeto en el mapa
               if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
                  mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
                  cteMlm.setDblValorDbl1(cteMlm.getDblValorDbl1() + dblImporte);
               } else {
                  mlm_cliente cteMlm = new mlm_cliente();
                  cteMlm.setIntCliente(rs.getInt("CT_ID"));
                  cteMlm.setDblValorDbl1(dblImporte);
                  mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
               }
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();

            //CARGA DE DOCUMENTOS DE TICKETS(REFERENCIADOS)
            strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
                    + " sum(vta_tickets.TKT_IMPORTE) AS PIMPORTE, "
                    + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
                    + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
                    + " FROM vta_cliente,vta_tickets WHERE vta_tickets.MPE_ID =  " + this.intPeriodo
                    + " and vta_tickets.CT_ID = vta_cliente.CT_ID "
                    + " and vta_cliente.CT_CATEGORIA1 <> 1 "
                    + " and vta_tickets.TKT_ANULADA = 0 "
                    + " and vta_tickets.TKT_NO_MLM = 0 "
                    + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
               double dblImporte = rs.getDouble("PIMPORTE");
               //Realizamos la conversion de las monedas
               if (rs.getInt("TKT_MONEDA") != 1) {
                  Iterator<TasaCambio> it = lstTasas.getLista().iterator();
                  while (it.hasNext()) {
                     TasaCambio tasaCambio = it.next();
                     if (tasaCambio.getMoneda2() == rs.getInt("TKT_MONEDA")) {
                        dblImporte = dblImporte * tasaCambio.getValor();
                     }
                  }
               }
               //
               //Evaluamos si ya existe el objeto en el mapa
               if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
                  mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
                  cteMlm.setDblValorDbl1(cteMlm.getDblValorDbl1() + dblImporte);
               } else {
                  mlm_cliente cteMlm = new mlm_cliente();
                  cteMlm.setIntCliente(rs.getInt("CT_ID"));
                  cteMlm.setDblValorDbl1(dblImporte);
                  mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
               }

            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }

         //Guardamos los puntos y valor negocio totales
         int intContaProcess = 0;
         int intContaProcessTot = 0;
         Iterator it = mapaClientes.entrySet().iterator();
         while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            intContaProcess++;
            intContaProcessTot++;
            mlm_cliente cteMlm = (mlm_cliente) e.getValue();
            //Guardamos info
            if (cteMlm.getDblPuntos() > 0 || cteMlm.getDblNegocio() > 0 || cteMlm.getDblValorDbl1() > 0) {
               String strUpdate = "update vta_cliente set "
                       + " CT_PPUNTOS = CT_PPUNTOS + " + cteMlm.getDblPuntos() + ", "
                       + " CT_PNEGOCIO = CT_PNEGOCIO + " + cteMlm.getDblNegocio() + ", "
                       + " CT_VENTA_REFERENCIADO = CT_VENTA_REFERENCIADO + " + cteMlm.getDblValorDbl1() + " "
                       + " WHERE CT_ID = " + cteMlm.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
            }
            if (intContaProcess == 1000) {
               intContaProcess = 0;
               log.info("Llevamos " + intContaProcessTot + " registros guardando puntos y vnegocio...");
               System.out.flush();
            }
         }

         //Terminamos transaccion
         if (this.strResultLast.equals("OK")) {
            this.oConn.runQueryLMD("COMMIT");
         } else {
            this.oConn.runQueryLMD("ROLLBACK");
         }

      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      log.info("Termina Fase 2.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

   }

   @Override
   public void doFase3() {
      super.doFase3();
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 3.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Lista para los clientes
      ArrayList<mlm_cliente> lstMlmCliente = new ArrayList<mlm_cliente>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Bonos por ingreso">
      /*double dblBonoRecluta = 180;
       String strIngreso = "select CT_UPLINE,CT_SPONZOR, CT_ID,CT_PPUNTOS,SC_ID "
       + " from vta_cliente where MPE_ID = " + this.getIntPeriodo()
       + " ORDER BY CT_UPLINE";
       try {
       ResultSet rs = this.oConn.runQuery(strIngreso, true);
       while (rs.next()) {
       if (rs.getDouble("CT_PPUNTOS") >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
       //Actualizamos el importe de comision en clientes
       String strUpdate = "UPDATE vta_cliente SET ";
       strUpdate += " CT_COMISION = CT_COMISION + " + dblBonoRecluta;
       strUpdate += " WHERE CT_ID = " + rs.getInt("CT_SPONZOR");
       this.oConn.runQueryLMD(strUpdate);


       //Entidad para el detalle de las comisiones
       mlm_comision_deta comisDeta = new mlm_comision_deta();
       comisDeta.setFieldInt("CT_ID", rs.getInt("CT_SPONZOR"));
       comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
       comisDeta.setFieldInt("COMI_FUENTE", rs.getInt("CT_ID"));
       comisDeta.setFieldInt("COMI_DESTINO", rs.getInt("CT_SPONZOR"));
       comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
       comisDeta.setFieldDouble("COMI_IMPORTE", dblBonoRecluta);
       comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblBonoRecluta);
       comisDeta.setFieldInt("COMI_NIVEL", 1);
       comisDeta.setFieldInt("COMI_ARMADOINI", 1);
       comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
       comisDeta.setFieldInt("COMI_ARMADONUM", 1);
       comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
       comisDeta.setFieldInt("SC_ID", rs.getInt("SC_ID"));
       comisDeta.setFieldString("COMI_CODIGO", "INS");
       comisDeta.Agrega(oConn);

       }

       }
        //if(rs.getStatement() != null )rs.getStatement().close(); 
      rs.close();
       } catch (SQLException ex) {
       this.strResultLast = "ERROR:" + ex.getMessage();
       log.error(ex.getMessage() + " " + ex.getSQLState());
       }*/
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para conteo de hijos">
      log.info("Inicia Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      this.oConn.runQueryLMD("BEGIN");

      //Calculamos el total de hijos por cada nodo
      String strHijos = "select CT_UPLINE, count(CT_ID) AS cuantos "
              + " from vta_cliente where CT_ACTIVO = 1 AND CT_PPUNTOS >= " + lstParams.get(0).getFieldDouble("CP_PPUNTOS")
              + " GROUP BY CT_UPLINE";
      try {
         ResultSet rs = this.oConn.runQuery(strHijos, true);
         while (rs.next()) {
            String strUpdate = "UPDATE vta_cliente SET CT_CONTEO_HIJOS =  " + rs.getInt("cuantos")
                    + " where CT_ID = " + rs.getInt("CT_UPLINE");
            this.oConn.runQueryLMD(strUpdate);
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      //Terminamos transaccion
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Cargamos en un arreglo la info de los clientes">
      log.info("Inicia Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intConta = -1;
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO"
              + ",CJ_ST1_N,CJ_ST2_N,CJ_ST3_N,CJ_ST4_N"
              + ",CJ_ST1_P,CJ_ST2_P,CJ_ST3_P,CJ_ST4_P "
              + ",CJ_MES_1,CT_CONTEO_HIJOS,SC_ID,CT_COMISION"
              + " from vta_cliente order by CT_ARMADONUM";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intConta++;
            mlm_cliente cte = new mlm_cliente();
            cte.setIntCliente(rs.getInt("CT_ID"));
            cte.setIntUpline(rs.getInt("CT_UPLINE"));
            cte.setDblPuntos(rs.getDouble("CT_PPUNTOS"));
            cte.setDblNegocio(rs.getDouble("CT_PNEGOCIO"));
            cte.setDblValorDbl1(rs.getDouble("CJ_ST1_N"));
            cte.setDblValorDbl2(rs.getDouble("CJ_ST2_N"));
            cte.setDblValorDbl3(rs.getDouble("CJ_ST3_N"));
            cte.setDblValorDbl4(rs.getDouble("CJ_ST4_N"));
            cte.setDblValorDbl5(rs.getDouble("CJ_ST1_P"));
            cte.setDblValorDbl6(rs.getDouble("CJ_ST2_P"));
            cte.setDblValorDbl7(rs.getDouble("CJ_ST3_P"));
            cte.setDblValorDbl8(rs.getDouble("CJ_ST4_P"));
            cte.setDblValorDbl9(rs.getDouble("CJ_MES_1"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setDblComision(rs.getDouble("CT_COMISION"));
            lstMlmCliente.add(cte);
            //Para indexarlo
            ClienteIndice tupla = new ClienteIndice();
            tupla.setIntCte(rs.getInt("CT_ID"));
            tupla.setIntIndice(intConta);
            lstMlmClienteIdx.add(tupla);
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      log.info("Termina Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Ordenamos tabla hash
      Collections.sort(lstMlmClienteIdx);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Recorremos la red desde abajo para el calculo de nivel">
      log.info("Inicia Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //Actualizamos puntos grupales
         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblValorDbl1() + cte.getDblValorDbl2() + cte.getDblValorDbl3() + cte.getDblValorDbl4());
         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblValorDbl5() + cte.getDblValorDbl6() + cte.getDblValorDbl7() + cte.getDblValorDbl8());
         //Calculamos el nivel de la persona
         int intNivel = 0;
         if (cte.getDblPuntos() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
            //Recorremos la lista de parametros del plan para definir el nivel
            Iterator<TableMaster> it = lstParams.iterator();
            while (it.hasNext()) {
               TableMaster tbn = it.next();
               if (cte.getDblGPuntos() >= tbn.getFieldDouble("CP_GPUNTOS")
                       /*&& cte.getDblValorDbl9() >= tbn.getFieldDouble("CP_HIST1")*/
                       && cte.getIntTotalHijos() >= tbn.getFieldDouble("CP_HIJOS")) {
                  intNivel = tbn.getFieldInt("CP_NIVEL");
               }
            }
         }
         cte.setIntNivelRed(intNivel);
         //Guardamos info del nodo

         //Subimos info al padre
         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
         //Si se encontro el padre
         if (intIdx != -1) {
            mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
            ctePadre.setDblValorDbl1(ctePadre.getDblValorDbl1() + cte.getDblNegocio());
            ctePadre.setDblValorDbl2(ctePadre.getDblValorDbl2() + cte.getDblValorDbl1());
            ctePadre.setDblValorDbl3(ctePadre.getDblValorDbl3() + cte.getDblValorDbl2());
            ctePadre.setDblValorDbl4(ctePadre.getDblValorDbl4() + cte.getDblValorDbl3());
            ctePadre.setDblValorDbl5(ctePadre.getDblValorDbl5() + cte.getDblPuntos());
            ctePadre.setDblValorDbl6(ctePadre.getDblValorDbl6() + cte.getDblValorDbl5());
            ctePadre.setDblValorDbl7(ctePadre.getDblValorDbl7() + cte.getDblValorDbl6());
            ctePadre.setDblValorDbl8(ctePadre.getDblValorDbl8() + cte.getDblValorDbl7());
         }

      }
      log.info("Termina Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD("BEGIN");

      //Guardamos informacion de los nodos
      log.info("Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
         if (cte.getDblPuntos() != 0/*
                  || cte.getDblValorDbl2() != 0
                  || cte.getDblValorDbl3() != 0
                  || cte.getDblValorDbl4() != 0
                  || cte.getDblValorDbl5() != 0
                  || cte.getDblValorDbl6() != 0
                  || cte.getDblValorDbl7() != 0
                  || cte.getDblValorDbl8() != 0*/
                 || cte.getDblGPuntos() != 0
                 || cte.getDblGNegocio() != 0) {
            //G_NEGOCIO
            //G_PUNTOS
            //NIVELRED
            //CJ_ST1_N ... 4
            //CJ_ST1_P ... 4
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "CJ_ST1_N = " + cte.getDblValorDbl1() + ",";
            strUpdate += "CJ_ST2_N = " + cte.getDblValorDbl2() + ",";
            strUpdate += "CJ_ST3_N = " + cte.getDblValorDbl3() + ",";
            strUpdate += "CJ_ST4_N = " + cte.getDblValorDbl4() + ",";
            strUpdate += "CJ_ST1_P = " + cte.getDblValorDbl5() + ",";
            strUpdate += "CJ_ST2_P = " + cte.getDblValorDbl6() + ",";
            strUpdate += "CJ_ST3_P = " + cte.getDblValorDbl7() + ",";
            strUpdate += "CJ_ST4_P = " + cte.getDblValorDbl8() + ",";
            strUpdate += "CT_NIVELRED = " + cte.getIntNivelRed() + ",";
            strUpdate += "CT_GPUNTOS = " + (cte.getDblGPuntos() + cte.getDblPuntos()) + ",";
            strUpdate += "CT_GNEGOCIO = " + cte.getDblGNegocio() + " ";
            strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
            this.oConn.runQueryLMD(strUpdate);
         }

      }
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si son definitivas movemos informacion historica">
      if (this.isEsCorridaDefinitiva()) {
         log.info("Inicia Guardado de historicos en clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
         String strUpdate = "UPDATE vta_cliente SET CJ_MES_15=CJ_MES_14";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_14=CJ_MES_13";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_13=CJ_MES_12";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_12=CJ_MES_11";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_11=CJ_MES_10";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_10=CJ_MES_9";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_9=CJ_MES_8";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_8=CJ_MES_7";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_7=CJ_MES_6";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_6=CJ_MES_5";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_5=CJ_MES_4";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_4=CJ_MES_3";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_3=CJ_MES_2";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_2=CJ_MES_1";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET CJ_MES_1=CT_GPUNTOS";
         this.oConn.runQueryLMD(strUpdate);
         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>


      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de Unilevel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intContaProcess = 0;
      int intContaProcessTot = 0;
      int intContaProcessDif = 0;
      int intContaProcessTotDif = 0;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         double dblImporte = cte.getDblNegocio();
         // <editor-fold defaultstate="collapsed" desc="Si el importe de negocio es mayor a cero">
         if (dblImporte > 0) {
            intContaProcess++;
            intContaProcessTot++;
            // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones UNILEVEL">
            //----- 1 GENERACION ---
            //Subimos info al padre
            int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
            //Si se encontro el padre
            if (intIdx != -1) {

               mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
               //Recorremos la lista de parametros del plan obtener los porcentajes
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  // <editor-fold defaultstate="collapsed" desc="----- 1 GENERACION --">
                  if (ctePadre.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                          && ctePadre.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                     double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL1") / 100);
                     ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
                     //Entidad para el detalle de las comisiones
                     mlm_comision_deta comisDeta = new mlm_comision_deta();
                     comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
                     comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                     comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                     comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
                     comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL1"));
                     comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                     comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                     comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                     comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                     comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                     comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                     comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                     comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                     comisDeta.setFieldString("COMI_CODIGO", "UNI1");
                     //comisDeta.Agrega(oConn); //NO GENERA COMISION PRIMER NIVEL
//                     ctePadre.getLstDeta().add(comisDeta);
                  }
                  // </editor-fold>
               }//Cierre busqueda de nivel para aplicar el factor de unilevel
               int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
               //----- 2 GENERACION ---
               //Si se encontro el abuelo
               if (intIdx2 != -1) {
                  mlm_cliente ctePadre2 = lstMlmCliente.get(intIdx2);
                  //Recorremos la lista de parametros del plan obtener los porcentajes
                  it = lstParams.iterator();
                  while (it.hasNext()) {
                     TableMaster tbn = it.next();
                     // <editor-fold defaultstate="collapsed" desc="----- 2 GENERACION ---">
                     if (ctePadre2.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                             && ctePadre2.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                        double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL2") / 100);
                        ctePadre2.setDblComision(ctePadre2.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                        //Entidad para el detalle de las comisiones
                        mlm_comision_deta comisDeta = new mlm_comision_deta();
                        comisDeta.setFieldInt("CT_ID", ctePadre2.getIntCliente());
                        comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                        comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                        comisDeta.setFieldInt("COMI_DESTINO", ctePadre2.getIntCliente());
                        comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL2"));
                        comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                        comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                        comisDeta.setFieldInt("COMI_NIVEL", ctePadre2.getIntNivelRed());
                        comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                        comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                        comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                        comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                        comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                        comisDeta.setFieldString("COMI_CODIGO", "UNI2");
                        //comisDeta.Agrega(oConn);//NO GENERA COMISION SEGUNDO NIVEL
                        //ctePadre.getLstDeta().add(comisDeta);
                     }
                     // </editor-fold>
                  }//Cierre busqueda de nivel para aplicar el factor de unilevel
                  //----- 3 GENERACION ---
                  int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
                  //Si se encontro el abuelo
                  if (intIdx3 != -1) {
                     mlm_cliente ctePadre3 = lstMlmCliente.get(intIdx3);
                     //Recorremos la lista de parametros del plan obtener los porcentajes
                     it = lstParams.iterator();
                     while (it.hasNext()) {
                        TableMaster tbn = it.next();
                        // <editor-fold defaultstate="collapsed" desc="----- 3 GENERACION ---">
                        if (ctePadre3.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                && ctePadre3.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                           double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL3") / 100);
                           ctePadre3.setDblComision(ctePadre3.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                           //Entidad para el detalle de las comisiones
                           mlm_comision_deta comisDeta = new mlm_comision_deta();
                           comisDeta.setFieldInt("CT_ID", ctePadre3.getIntCliente());
                           comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                           comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                           comisDeta.setFieldInt("COMI_DESTINO", ctePadre3.getIntCliente());
                           comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL3"));
                           comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                           comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                           comisDeta.setFieldInt("COMI_NIVEL", ctePadre3.getIntNivelRed());
                           comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                           comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                           comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                           comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                           comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                           comisDeta.setFieldString("COMI_CODIGO", "UNI3");
                           comisDeta.Agrega(oConn);
//                           ctePadre.getLstDeta().add(comisDeta);
                        }
                        // </editor-fold>
                     }//Cierre busqueda de nivel para aplicar el factor de unilevel

                  }
               }
               if (intContaProcess == 1000) {
                  intContaProcess = 0;
                  log.info("Llevamos " + intContaProcessTot + " registros unilevel...");
                  System.out.flush();
               }
            }
            // </editor-fold>

         }
         // </editor-fold>
      }

      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones de clientes preferentes">
      try {
         String strSqlRef = "select CT_ID,CT_UPLINE,CT_VENTA_REFERENCIADO,SC_ID from vta_cliente where CT_VENTA_REFERENCIADO <> 0";
         ResultSet rs = oConn.runQuery(strSqlRef, true);
         while (rs.next()) {
            int intCT_ID = rs.getInt("CT_ID");
            int intCT_UPLINE = rs.getInt("CT_UPLINE");
            int intSC_ID = rs.getInt("SC_ID");
            double dblVentaRef = rs.getDouble("CT_VENTA_REFERENCIADO");
            int intIdxRef = this.BuscaNodoenArreglo(intCT_UPLINE, lstMlmClienteIdx, false);
            //Si se encontro el abuelo
            if (intIdxRef != -1) {
               mlm_cliente ctePadreReferenciado = lstMlmCliente.get(intIdxRef);
               double dblComision = dblVentaRef * (lstParams.get(0).getFieldDouble("CP_DIFERENCIAL1") / 100);
               ctePadreReferenciado.setDblComision(ctePadreReferenciado.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
               //Entidad para el detalle de las comisiones
               mlm_comision_deta comisDeta = new mlm_comision_deta();
               comisDeta.setFieldInt("CT_ID", ctePadreReferenciado.getIntCliente());
               comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
               comisDeta.setFieldInt("COMI_FUENTE", intCT_ID);
               comisDeta.setFieldInt("COMI_DESTINO", ctePadreReferenciado.getIntCliente());
               comisDeta.setFieldDouble("COMI_PORCENTAJE", lstParams.get(0).getFieldDouble("CP_DIFERENCIAL1"));
               comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
               comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblVentaRef);
               comisDeta.setFieldInt("COMI_NIVEL", ctePadreReferenciado.getIntNivelRed());
               comisDeta.setFieldInt("COMI_ARMADOINI", 1);
               comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
               comisDeta.setFieldInt("COMI_ARMADONUM", 1);
               comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
               comisDeta.setFieldInt("SC_ID", intSC_ID);
               comisDeta.setFieldString("COMI_CODIGO", "REF");
               comisDeta.Agrega(oConn);
            }
            //Si yo hice mas de 10,000 gano de mi mismo
            if (dblVentaRef > 10000) {
               intIdxRef = this.BuscaNodoenArreglo(intCT_ID, lstMlmClienteIdx, false);
               //Si se encontro el abuelo
               if (intIdxRef != -1) {
                  mlm_cliente ctePadreReferenciado = lstMlmCliente.get(intIdxRef);
                  double dblComision = dblVentaRef * (lstParams.get(0).getFieldDouble("CP_DIFERENCIAL2") / 100);
                  ctePadreReferenciado.setDblComision(ctePadreReferenciado.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                  //Entidad para el detalle de las comisiones
                  mlm_comision_deta comisDeta = new mlm_comision_deta();
                  comisDeta.setFieldInt("CT_ID", ctePadreReferenciado.getIntCliente());
                  comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                  comisDeta.setFieldInt("COMI_FUENTE", intCT_ID);
                  comisDeta.setFieldInt("COMI_DESTINO", ctePadreReferenciado.getIntCliente());
                  comisDeta.setFieldDouble("COMI_PORCENTAJE", lstParams.get(0).getFieldDouble("CP_DIFERENCIAL2"));
                  comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                  comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblVentaRef);
                  comisDeta.setFieldInt("COMI_NIVEL", ctePadreReferenciado.getIntNivelRed());
                  comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                  comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                  comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                  comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                  comisDeta.setFieldInt("SC_ID", intSC_ID);
                  comisDeta.setFieldString("COMI_CODIGO", "REF2");
                  comisDeta.Agrega(oConn);
               }
            }

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>

      log.info("Termina Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>


      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
         if (cte.getDblComision() != 0) {
            //Realizamos la conversion a la moneda original

            //Actualizamos el importe de comision en clientes
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += " CT_COMISION =  " + cte.getDblComision();
            strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
            this.oConn.runQueryLMD(strUpdate);

            //Cabezal
            mlm_comision comisionM = new mlm_comision();
            comisionM.setFieldInt("CT_ID", cte.getIntCliente());
            comisionM.setFieldInt("MPE_ID", this.intPeriodo);
            comisionM.setFieldDouble("CO_IMPORTE", cte.getDblComision());
            comisionM.setFieldDouble("CO_IMPUESTO1", 0);
            comisionM.setFieldDouble("CO_IMPUESTO2", 0);
            comisionM.setFieldDouble("CO_IMPUESTO3", 0);
            comisionM.setFieldDouble("CO_RET1", 0);
            comisionM.setFieldDouble("CO_RET2", 0);
            comisionM.setFieldDouble("CO_RET3", 0);
            comisionM.setFieldDouble("CO_CHEQUE", cte.getDblComision());
            comisionM.setFieldDouble("CO_PUNTOS_P", cte.getDblPuntos());
            comisionM.setFieldDouble("CO_PUNTOS_G", cte.getDblGPuntos());
            comisionM.setFieldDouble("CO_NEGOCIO_P", cte.getDblNegocio());
            comisionM.setFieldDouble("CO_NEGOCIO_G", cte.getDblGNegocio());
            comisionM.setFieldInt("CO_NIVEL", cte.getIntNivelRed());
            comisionM.setFieldInt("CT_NIVELRED", cte.getIntNivelRed());
            //Calculo de impuestos
            CalculoImpuestos(cte, comisionM);
            comisionM.Agrega(oConn);

//            //Detalle
//            Iterator<mlm_comision_deta> it3 = cte.getLstDeta().iterator();
//            while (it3.hasNext()) {
//               mlm_comision_deta detaComis = it3.next();
//               detaComis.Agrega(oConn);
//            }
         }

      }
      // </editor-fold>
      //Terminamos transaccion
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

      log.info("Termina Fase 3.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
   }

   @Override
   public void doFase4() {
      super.doFase4();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();
      if (this.isEsCorridaDefinitiva()) {
         try {
            //Clientes que no cumplieron el minimo les movemos la lista de precios
            String strSql = "select CT_ID,CT_RAZONSOCIAL from vta_cliente where CT_CATEGORIA1 = 1"
                    + " AND CT_CATEGORIA2 = 0"
                    + " AND CT_PPUNTOS < " + lstParams.get(0).getFieldDouble("CP_PPUNTOS");
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               int intCT_ID = rs.getInt("CT_ID");
               String strUpdate = "update vta_cliente set CT_LPRECIOS = 2 where CT_ID = " + intCT_ID;
               oConn.runQueryLMD(strUpdate);
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
            //Clientes Referenciados que  cumplieron el minimo les movemos la lista de precios
            strSql = "select CT_ID,CT_RAZONSOCIAL from vta_cliente where CT_CATEGORIA1 <> 1"
                    + " AND CT_CATEGORIA2 = 0"
                    + " AND CT_PPUNTOS >= " + lstParams.get(0).getFieldDouble("CP_PPUNTOS");
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               int intCT_ID = rs.getInt("CT_ID");
               String strUpdate = "update vta_cliente set CT_LPRECIOS = 1 where CT_ID = " + intCT_ID;
               oConn.runQueryLMD(strUpdate);
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
            //Consultamos clientes tentativos por eliminar...
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
   }

   @Override
   public void doFaseBonos() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doFaseCierre() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
   // </editor-fold>
}
