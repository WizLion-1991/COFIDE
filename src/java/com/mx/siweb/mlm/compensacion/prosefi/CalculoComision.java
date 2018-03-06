/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.prosefi;

import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision_deta;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;

/**
 * Clase que se encarga de generar las comisiones para Prosefi
 *
 * @author ZeusGalindo
 */
public class CalculoComision extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.prosefi.CalculoComision.class.getName());

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
         strSQL += "PRO_GPUNTOS_GEN1  =  0,";
         strSQL += "PRO_GPUNTOS_GEN2  =  0,";
         strSQL += "PRO_GPUNTOS_GEN3  =  0,";
         strSQL += "PRO_PPUNTOS  =  0,";
         strSQL += "PRO_PNEGOCIO  =  0,";
         strSQL += "PRO_HIJOS_GRAN_SENIOR  =  0,";
         strSQL += "PRO_HIJOS_EMPRESARIO  =  0,";
         strSQL += "PRO_FRONTALES  =  0,";
         strSQL += "CT_CONTEO_HIJOS  =  0";
         this.oConn.runQueryLMD(strSQL);
      }
      //Marcamos los clientes de comisiones mensuales
      int intIni = 0;
      int intFin = 0;
      //Buscamos el armado ini y fin del nodo raiz
      String strSqlP = "select CT_ARMADOINI,CT_ARMADOFIN from vta_cliente c where c.CT_ID = 324457;";
      try {
         ResultSet rs = this.oConn.runQuery(strSqlP, true);
         while (rs.next()) {
            intIni = rs.getInt("CT_ARMADOINI");
            intFin = rs.getInt("CT_ARMADOFIN");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error("Error al buscar el periodo en comisiones " + ex.getMessage());
      }
      //Limpiamos las banderas
      String strUpdate1 = "update vta_cliente as a \n"
         + " \n"
         + "set a.PRO_MENSUAL = 0 "
         + " ";
      this.oConn.runQueryLMD(strUpdate1);
      //Activamos la bandera para la parte mensual
      strUpdate1 = "update vta_cliente  as a  \n"
         + "\n"
         + " set a.PRO_MENSUAL = 1   \n"
         + " where a.CT_ARMADONUM>= " + intIni + " and \n"
         + "a.CT_ARMADONUM<= " + intFin + " ";
      this.oConn.runQueryLMD(strUpdate1);
   }

   @Override
   public void doFase2() {
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 2.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

      //Query para marcar los productos que son de prosefi
      //inicializamos valores
      Map mapaClientes = new HashMap();
      this.strResultLast = "OK";

      //Obtenemos las monedas y paridades
      lstTasaCambio lstTasas = new lstTasaCambio();

      String strSql = "SELECT TKT_MONEDA FROM vta_tickets group by TKT_MONEDA";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            if (rs.getInt("TKT_MONEDA") != 1) {
               //Generamos objeto de conversion
               TasaCambio tCambio = new TasaCambio();
               tCambio.setMoneda1(1);
               tCambio.setMoneda2(rs.getInt("TKT_MONEDA"));
               //Buscamos la tasa de cambio de esta moneda
               String strSqlT = "select * from vta_tasacambio where "
                  + " (TC_MONEDA1 = 1 and TC_MONEDA2 = " + tCambio.getMoneda2() + ") || "
                  + " (TC_MONEDA2 = 1 and TC_MONEDA1 = " + tCambio.getMoneda2() + ") "
                  + " order by TC_FECHA DESC LIMIT 0,1";
               ResultSet rs2 = this.oConn.runQuery(strSqlT, true);
               while (rs2.next()) {
                  tCambio.setValor(rs2.getDouble("TC_PARIDAD"));
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
         //---------------------------- CARGA DE DOCUMENTOS DE REMISION NO PROSEFI
         strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
            + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
            + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
            + " FROM vta_tickets,vta_cliente WHERE vta_tickets.CT_ID = vta_cliente.CT_ID and vta_tickets.MPE_ID =  " + this.intPeriodo
            + " and vta_tickets.TKT_ANULADA = 0 "
            + " and vta_tickets.TKT_NO_MLM = 0 "
            + " and vta_cliente.PRO_MENSUAL = 0"
            + " and vta_tickets.PRO_PROSEFI = 0 "
            + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         log.debug(strSql);
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

         //---------------------------- CARGA DE DOCUMENTOS DE REMISION PROSEFI
         strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
            + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
            + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
            + " FROM vta_tickets,vta_cliente WHERE vta_tickets.CT_ID = vta_cliente.CT_ID and vta_tickets.MPE_ID =  " + this.intPeriodo
            + " and vta_tickets.TKT_ANULADA = 0 "
            + " and vta_tickets.TKT_NO_MLM = 0 "
            + " and vta_cliente.PRO_MENSUAL = 0"
            + " and vta_tickets.PRO_PROSEFI = 1 "
            + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         log.debug(strSql);
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
               cteMlm.setDblValorDbl1(cteMlm.getDblValorDbl1() + dblPuntos);
               cteMlm.setDblValorDbl2(cteMlm.getDblValorDbl2() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblValorDbl1(dblPuntos);
               cteMlm.setDblValorDbl2(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();

         //CARGA DE MEMORANDA  NO PROSEFI
         strSql = "SELECT "
            + "	mlm_bono_ind.CT_ID, 	"
            + " SUM(mlm_bono_ind.MBI_PUNTOS) AS TPUNTOS, 	"
            + " SUM(mlm_bono_ind.MBI_NEGOCIO) AS TNEGOCIO  "
            + " FROM mlm_bono_ind WHERE MPE_ID =   " + this.intPeriodo
            + " AND PRO_PROSEFI = 0  "
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

         //CARGA DE MEMORANDA  PROSEFI
         strSql = "SELECT "
            + "	mlm_bono_ind.CT_ID, 	"
            + " SUM(mlm_bono_ind.MBI_PUNTOS) AS TPUNTOS, 	"
            + " SUM(mlm_bono_ind.MBI_NEGOCIO) AS TNEGOCIO  "
            + " FROM mlm_bono_ind WHERE MPE_ID =   " + this.intPeriodo
            + " AND PRO_PROSEFI = 1   "
            + " GROUP BY mlm_bono_ind.CT_ID";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("TPUNTOS");
            dblNegocio = rs.getDouble("TNEGOCIO");
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblValorDbl1(cteMlm.getDblValorDbl1() + dblPuntos);
               cteMlm.setDblValorDbl2(cteMlm.getDblValorDbl2() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblValorDbl1(dblPuntos);
               cteMlm.setDblValorDbl2(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();

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
            if (cteMlm.getDblPuntos() > 0 || cteMlm.getDblNegocio() > 0 || cteMlm.getDblValorDbl1() > 0 || cteMlm.getDblValorDbl2() > 0) {
               String strUpdate = "update vta_cliente set "
                  + " CT_PPUNTOS = CT_PPUNTOS + " + cteMlm.getDblPuntos() + " "
                  + " ,CT_PNEGOCIO = CT_PNEGOCIO + " + cteMlm.getDblNegocio() + " "
                  + " ,PRO_PPUNTOS = PRO_PPUNTOS + " + cteMlm.getDblValorDbl1() + " "
                  + " ,PRO_PNEGOCIO = PRO_PNEGOCIO + " + cteMlm.getDblValorDbl2() + " "
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

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para conteo de hijos">
      log.info("Inicia Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      this.oConn.runQueryLMD("BEGIN");

      //Calculamos el total de hijos por cada nodo
      String strHijos = "select CT_UPLINE, count(CT_ID) AS cuantos "
         + " from vta_cliente where CT_ACTIVO = 1 AND PRO_MENSUAL = 0 "
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
         + ",CT_CONTEO_HIJOS,SC_ID,PRO_NIV_MAX,PRO_ES_TOP_SPONSOR,PRO_MENSUAL"
         + ",PRO_PPUNTOS,PRO_PNEGOCIO "
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
            cte.setDblValorDbl1(rs.getDouble("PRO_PPUNTOS"));
            cte.setDblValorDbl2(rs.getDouble("PRO_PNEGOCIO"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntValor1(rs.getInt("PRO_NIV_MAX"));
            cte.setIntValor2(rs.getInt("PRO_ES_TOP_SPONSOR"));
            cte.setIntValor3(rs.getInt("PRO_MENSUAL"));
            cte.setIntValor6(0);

            lstMlmCliente.add(cte);
            //Para indexarloEMPRESA
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

         // <editor-fold defaultstate="collapsed" desc="Validamos que pertenezca al plan quincenal">
         if (cte.getIntValor3() == 0) {

            //Actualizamos puntos grupales
            // <editor-fold defaultstate="collapsed" desc="Calculamos el nivel de la persona">
            int intNivel = 0;
//         log.debug(":: "+ cte.getIntCliente() + " " + cte.getDblPuntos() + " " + lstParams.get(0).getFieldDouble("CP_PPUNTOS"));
            if (cte.getDblPuntos() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")
               || cte.getDblValorDbl1() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
               intNivel = 1;
               //Recorremos la lista de parametros del plan para definir el nivel
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  // <editor-fold defaultstate="collapsed" desc="Otros niveles ">
                  double dblPuntosGrupales1y2 = cte.getDblValorDbl4() + cte.getDblValorDbl5();
                  //if (dblPuntosGrupales1y2 >= tbn.getFieldDouble("CP_GPUNTOS")) {
                  //Nivel Gran Senior, requerimos que sea top sponsor
                  if (tbn.getFieldInt("CP_NIVEL") == 2 && cte.getIntValor2() == 1) {
                     //Validamos directos con mas de 3 mil puntos
                     if (cte.getIntValor6() >= 1) {
                        intNivel = tbn.getFieldInt("CP_NIVEL");
                     }
                  }
                  //Nivel Empresario
                  if (tbn.getFieldInt("CP_NIVEL") == 3 && cte.getIntValor4() >= tbn.getFieldDouble("CP_HIJOS")) {
                     intNivel = tbn.getFieldInt("CP_NIVEL");
                  }
                  //Nivel Millonario
                  if (tbn.getFieldInt("CP_NIVEL") == 4 && cte.getIntValor5() >= tbn.getFieldDouble("CP_HIJOS")) {
                     intNivel = tbn.getFieldInt("CP_NIVEL");
                  }
                  //}
                  // </editor-fold>
               }
            }
//         log.debug(cte.getIntCliente() + " " + intNivel);
            cte.setIntNivelRed(intNivel);
            // </editor-fold>
            //Guardamos info del nodo
            // <editor-fold defaultstate="collapsed" desc="Subimos info al padre Generacion 1">
            int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
            //Si se encontro el padre
            if (intIdx != -1) {
               mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
               ctePadre.setDblValorDbl4(ctePadre.getDblValorDbl4() + cte.getDblValorDbl1() + cte.getDblPuntos());
               // Si el nivel es gran senior o empresario subimos el contador al padre
               if (cte.getIntNivelRed() == 2) {
                  ctePadre.setIntValor4(ctePadre.getIntValor4() + 1);
               }
               if (cte.getIntNivelRed() == 3) {
                  ctePadre.setIntValor5(ctePadre.getIntValor5() + 1);
               }
               //Conteo para directos con puntos mayores a 3000 mil puntos
               if (cte.getDblPuntos() >= 3000) {
                  ctePadre.setIntValor6(ctePadre.getIntValor6() + 1);
               }
               //Subimos info al padre Generacion 2
               intIdx = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
               //Si se encontro el padre
               if (intIdx != -1) {
                  mlm_cliente ctePadre2 = lstMlmCliente.get(intIdx);
                  ctePadre2.setDblValorDbl5(ctePadre2.getDblValorDbl5() + cte.getDblValorDbl1() + cte.getDblPuntos());
                  //Subimos info al padre Generacion 3
                  //log.debug("papa 2(" + ctePadre2.getIntCliente() + ")....papa---" + ctePadre2.getIntUpline());
                  intIdx = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
                  //Si se encontro el padre
                  if (intIdx != -1) {
                     mlm_cliente ctePadre3 = lstMlmCliente.get(intIdx);
                     ctePadre3.setDblValorDbl6(ctePadre3.getDblValorDbl6() + cte.getDblValorDbl1() + cte.getDblPuntos());
                  }
               }
            }
            // </editor-fold>
         }
         // </editor-fold>

      }

      log.info(
         "Termina Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD(
         "BEGIN");

      //Guardamos informacion de los nodos
      log.info(
         "Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         mlm_cliente cte = lstMlmCliente.get(i);
         // <editor-fold defaultstate="collapsed" desc="Validamos que pertenezca al plan quincenal">
         if (cte.getIntValor3() == 0) {
            //Validamos que solo los nodos con valores se guarden
            if (cte.getDblValorDbl1() != 0
               || cte.getDblGPuntos() != 0
               || cte.getDblGNegocio() != 0
               || cte.getDblPuntos() > 0) {

               String strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " CT_NIVELRED = " + cte.getIntNivelRed() + "";
               strUpdate += ",PRO_HIJOS_GRAN_SENIOR = " + cte.getIntValor4() + "";
               strUpdate += ",PRO_HIJOS_EMPRESARIO = " + cte.getIntValor5() + "";
               strUpdate += ",CT_GPUNTOS = " + cte.getDblGPuntos() + "";
               strUpdate += ",CT_GNEGOCIO = " + cte.getDblGNegocio() + " ";
               strUpdate += ",PRO_FRONTALES = " + cte.getIntValor6() + " ";
               strUpdate += ",PRO_GPUNTOS_GEN1 = " + cte.getDblValorDbl4() + " ";
               strUpdate += ",PRO_GPUNTOS_GEN2 = " + cte.getDblValorDbl5() + " ";
               strUpdate += ",PRO_GPUNTOS_GEN3 = " + cte.getDblValorDbl6() + " ";

               //Condicion para guardado de definitivas
               if (this.isEsCorridaDefinitiva()) {
                  //Actualizamos nivel maximo
                  if (cte.getIntNivelRed() > cte.getIntValor1()) {
                     strUpdate += ",PRO_NIV_MAX = " + cte.getIntNivelRed() + " ";
                  }
               }
               strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
            }
         }
         // </editor-fold>

      }

      this.oConn.runQueryLMD(
         "COMMIT");
      log.info(
         "Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si son definitivas movemos informacion historica">
      if (this.isEsCorridaDefinitiva()) {
         log.info("Inicia Guardado de historicos en clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
         String strUpdate = "UPDATE vta_cliente SET PRO_NIV12=PRO_NIV11 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV11=PRO_NIV10 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV10=PRO_NIV9 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV9=PRO_NIV8 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV8=PRO_NIV7 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV7=PRO_NIV6 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV6=PRO_NIV5 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV5=PRO_NIV4 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV4=PRO_NIV3 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV3=PRO_NIV2 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV2=PRO_NIV1 WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET PRO_NIV1=CT_GPUNTOS WHERE PRO_MENSUAL = 0";
         this.oConn.runQueryLMD(strUpdate);
         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>
   }

   @Override
   public void doFase4() {
      super.doFase4(); //To change body of generated methods, choose Tools | Templates.
      Fechas fecha = new Fechas();
      //Lista para los clientes
      ArrayList<mlm_cliente> lstMlmCliente = new ArrayList<mlm_cliente>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Cargamos en un arreglo la info de los clientes">
      log.info("Inicia Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intConta = -1;
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO"
         + ",CT_CONTEO_HIJOS,SC_ID,PRO_NIV_MAX,PRO_ES_TOP_SPONSOR"
         + ",PRO_PPUNTOS,PRO_PNEGOCIO,CT_NIVELRED,PRO_MENSUAL "
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
            cte.setDblValorDbl1(rs.getDouble("PRO_PPUNTOS"));
            cte.setDblValorDbl2(rs.getDouble("PRO_PNEGOCIO"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntValor1(rs.getInt("PRO_NIV_MAX"));
            cte.setIntValor2(rs.getInt("PRO_ES_TOP_SPONSOR"));
            cte.setIntValor3(rs.getInt("PRO_MENSUAL"));
            cte.setIntNivelRed(rs.getInt("CT_NIVELRED"));
            lstMlmCliente.add(cte);
            //Para indexarloEMPRESA
            ClienteIndice tupla = new ClienteIndice();
            tupla.setIntCte(rs.getInt("CT_ID"));
            tupla.setIntIndice(intConta);
            lstMlmClienteIdx.add(tupla);
         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      log.info("Termina Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Ordenamos tabla hash
      Collections.sort(lstMlmClienteIdx);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

      int intContaProcess = 0;
      int intContaProcessTot = 0;
      int intContaProcessDif = 0;
      int intContaProcessTotDif = 0;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         double dblImporte = cte.getDblNegocio() /*+ cte.getDblValorDbl2()*/;
         // <editor-fold defaultstate="collapsed" desc="Si el importe de negocio es mayor a cero y es quincenal">
         if (dblImporte > 0 && cte.getIntValor3() == 0) {
            intContaProcess++;
            intContaProcessTot++;

            // <editor-fold defaultstate="collapsed" desc="Unilevel">
            log.info("Inicia Calculo de Unilevel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
            int intEscalon = 0;
            int intXPadre = cte.getIntUpline();
            while (intEscalon <= 12) {
               int intIdx = this.BuscaNodoenArreglo(intXPadre, lstMlmClienteIdx, false);
               //Si se encontro el padre
               if (intIdx != -1) {
                  mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
                  int intYPadre = ctePadre.getIntUpline();
                  //Recorremos la lista de parametros del plan obtener los porcentajes
                  Iterator<TableMaster> it = lstParams.iterator();
                  while (it.hasNext()) {
                     TableMaster tbn = it.next();
                     // <editor-fold defaultstate="collapsed" desc="----- N GENERACION --">
                     if (ctePadre.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                        if (ctePadre.getDblPuntos() + ctePadre.getDblValorDbl1() >= tbn.getFieldDouble("CP_PPUNTOS")) {
                           intEscalon++;
                           //Validamos si hay un porcentaje de pago
                           if (tbn.getFieldDouble("CP_UNILEVEL" + intEscalon) > 0) {
                              double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL" + intEscalon) / 100);
                              ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
                              //Entidad para el detalle de las comisiones
                              mlm_comision_deta comisDeta = new mlm_comision_deta();
                              comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
                              comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                              comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                              comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
                              comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL" + intEscalon));
                              comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                              comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                              comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                              comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                              comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                              comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                              comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                              comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                              comisDeta.setFieldString("COMI_CODIGO", "UNI" + intEscalon);
                              comisDeta.Agrega(oConn);
//                     ctePadre.getLstDeta().add(comisDeta);                               
                           }//Cierre factor
                        }//Cierre puntos personales
                     }//Cierre nivel de red
                     intXPadre = intYPadre;
                     // </editor-fold>
                  }//Cierre ciclo
               } else {
                  //No encontro cerramos el ciclo
                  intEscalon = 99;
               }
            }
            log.info("Termina Calculo de Unilevel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Diferenciales">
            log.info("Inicia Calculo de Break.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

//            log.info("Diferenciales " + cte.getIntCliente());
            int intRaiz = 0;
            int intPadre = cte.getIntUpline();
            int intMaxPaga = cte.getIntNivelRed();
            int intStep = 0;
            while (intRaiz == 0) {
               int intIdxD = -1;
//               if(intPadre == 3421){
//                  intIdxD = this.BuscaNodoenArreglo(intPadre, lstMlmClienteIdx,true);
//               }else{
               intIdxD = this.BuscaNodoenArreglo(intPadre, lstMlmClienteIdx, false);
//               }

//               log.info("search node:" +  intPadre + " " + intIdxD );
               //Si se encontro el padre
               if (intIdxD != -1) {
                  intContaProcessDif++;
                  intContaProcessTotDif++;
                  mlm_cliente ctePadre = lstMlmCliente.get(intIdxD);
//                  log.info( "node found :" +  ctePadre.getIntCliente() + " " + ctePadre.getIntUpline() );
                  //Recorremos la lista de parametros del plan obtener los porcentajes
                  Iterator<TableMaster> it = lstParams.iterator();
                  while (it.hasNext()) {
                     TableMaster tbn = it.next();
                     // <editor-fold defaultstate="collapsed" desc="Aplica DIFERENCIALES">
                     if (ctePadre.getIntNivelRed() == tbn.getFieldInt("CP_NIVEL")) {
                        if (ctePadre.getIntNivelRed() > intMaxPaga
                           && ctePadre.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")) {
//                        log.info("Aplica diferencia " + cte.getIntCliente() + " -> " + ctePadre.getIntCliente());
                           int intMaxDif = intMaxPaga - 1;
                           double dblPorcentaje = 0;
                           if (intMaxDif > 0) {
                              dblPorcentaje = tbn.getFieldDouble("CP_DIFERENCIAL1")
                                 - lstParams.get(intMaxDif).getFieldDouble("CP_DIFERENCIAL1");
                           }
//                        //Solo si hay porcentaje de pago para la comision
                           if (dblPorcentaje > 0) {
                              intStep++;
                              double dblComision = dblImporte * (dblPorcentaje / 100);
                              ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
                              //Entidad para el detalle de las comisiones
                              mlm_comision_deta comisDeta = new mlm_comision_deta();
                              comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
                              comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                              comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                              comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
                              comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_DIFERENCIAL1"));
                              comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                              comisDeta.setFieldInt("COMI_NIVEL", ctePadre.getIntNivelRed());
                              comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                              comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                              comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                              comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                              comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                              comisDeta.setFieldString("COMI_CODIGO", "DIF" + intStep);
                              comisDeta.Agrega(oConn);
//                           ctePadre.getLstDeta().add(comisDeta);
                           }
                        }
                     }
                     // </editor-fold>
                  }
                  if (ctePadre.getIntNivelRed() > intMaxPaga) {
                     intMaxPaga = ctePadre.getIntNivelRed();
                  }
                  intPadre = ctePadre.getIntUpline();
//                  log.info("intPadre " + ctePadre.getIntCliente() + " " + intPadre);
               } else {
                  intRaiz = 1;
               }
               if (intContaProcessDif == 1000) {
                  intContaProcessDif = 0;
                  log.info("Llevamos " + intContaProcessTotDif + " registros diferenciales...");
                  System.out.flush();
               }
            }

            log.info("Termina Calculo de break.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
            // </editor-fold>

            if (intContaProcess == 1000) {
               intContaProcess = 0;
               log.info("Llevamos " + intContaProcessTot + " registros unilevel...");
               System.out.flush();
            }

         }
         // </editor-fold>

      }

      log.info("Termina Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden y sean quincenales
         if (cte.getDblComision() != 0 && cte.getIntValor3() == 0) {
            //Realizamos la conversion a la moneda original

            //Actualizamos el importe de comision en clientes
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += " CT_COMISION = " + cte.getDblComision();
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

         }

      }
      // </editor-fold>
      //Terminamos transaccion
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
   }

   @Override
   public void doFaseBonos() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doFaseCierre() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   // </editor-fold>
}
