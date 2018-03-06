/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.prosefi;

import Tablas.MlmComisionBitacora;
import Tablas.MlmComisionDetMensual;
import Tablas.MlmComisionMensual;
import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;
import com.mx.siweb.mlm.utilerias.Redes;
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
 * Cálculo de comisiones mensuales
 *
 * @author ZeusSIWEB
 */
public class CalculaComisionMensual extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.prosefi.CalculaComisionMensual.class.getName());

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CalculaComisionMensual(Conexion oConn, int intPeriodo, boolean EsCorridaDefinitiva) {
      super(oConn, intPeriodo, EsCorridaDefinitiva);
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void doFase1() {
      //Inicializamos valores
      boolean bolPasa = true;
      this.strResultLast = "OK";

      //Validamos si el periodo existe
      String strSqlP = "Select * from mlm_comision_bitacora where MPEM_ID = " + this.intPeriodo;
      try {
         ResultSet rs = this.oConn.runQuery(strSqlP, true);
         while (rs.next()) {
            int intDefinitivas = rs.getInt("CCO_DEFINITIVAS");
            if (intDefinitivas == 1) {
               bolPasa = false;
            }
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error("Error al buscar el periodo en comisiones " + ex.getMessage());
      }

      //Validamos si ya se corrieron definitivas
      if (bolPasa) {
         //Borramos comisiones del periodo
         String strDel1 = "delete from mlm_comision_mensual where MPEM_ID = " + this.intPeriodo;
         this.oConn.runQueryLMD(strDel1);

         String strDel2 = "delete from mlm_comision_deta_mensual where MPEM_ID = " + this.intPeriodo;
         this.oConn.runQueryLMD(strDel2);

         //Calculamos el arbol
         Redes red = new Redes();
         boolean bolArmo = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
         String strRes = "Se ha generado correctamente la red.";
         if (bolArmo) {
            strRes = "ERROR:" + red.getStrError();
         }

         //SI SON DEFINITIVAS marcamos el proceso
         if (this.isEsCorridaDefinitiva()) {
            Fechas fecha = new Fechas();
            MlmComisionBitacora bitacoraComis = new MlmComisionBitacora();
            bitacoraComis.setFieldString("CCO_FECHA", fecha.getFechaActual());
            bitacoraComis.setFieldString("CCO_HORA", fecha.getHoraActual());
            bitacoraComis.setFieldInt("CCO_USUARIO", 1);
            bitacoraComis.setFieldInt("MPE_ID", 0);
            bitacoraComis.setFieldInt("MPEM_ID", intPeriodo);
            bitacoraComis.setFieldInt("CCO_DEFINITIVAS", 1);
            bitacoraComis.Agrega(oConn);

         }
      } else {
         this.strResultLast = "ERROR:";
      }
      //Si todo es ok limpiamos banderas
      if (this.strResultLast.equals("OK")) {
         int intIni = 0;
         int intFin = 0;
         //Buscamos el armado ini y fin del nodo raiz
         strSqlP = "select CT_ARMADOINI,CT_ARMADOFIN from vta_cliente c where c.CT_ID = 324457;";
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
            + "set a.PRO_MENSUAL = 1   \n"
            + ",a.PRO_MENSUAL_VENTAS = 0 "
            + ",a.PRO_MENSUAL_ACTIVOS = 0 "
            + ",a.PRO_MENSUAL_COMISION = 0 "
            + ",a.CT_PPUNTOS = 0 "
            + ",a.CT_GPUNTOS = 0 "
            + ",a.CT_PNEGOCIO = 0 "
            + ",a.CT_ACTIVO = 0 "
            + ",a.PRO_MENSUAL_BONO1_ACT = 0 "
            + ",a.PRO_MENSUAL_BONO2_ACT = 0 "
            + ",a.PRO_MENSUAL_BONO3_ACT = 0 "
            + " where a.CT_ARMADONUM>= " + intIni + " and \n"
            + "a.CT_ARMADONUM<= " + intFin + " ";
         this.oConn.runQueryLMD(strUpdate1);

      }
   }

   @Override
   public void doFase2() {
      Fechas fecha = new Fechas();
      String strFechaIniPer = "";
      String strFechaFinPer = "";
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
//               if (rs2.getStatement() != null) {
//                  rs2.getStatement().close();
//               }
               rs2.close();
               lstTasas.Agrega(tCambio);
            }
         }
         //Buscamos las fechas iniciales y finales del periodo+
         String strSqlPer = "select MPEM_FECHAINICIAL,MPEM_FECHAFINAL from mlm_periodos_mensual where MPEM_ID = " + this.intPeriodo;
         rs = this.oConn.runQuery(strSqlPer, true);
         while (rs.next()) {
            strFechaIniPer = rs.getString("MPEM_FECHAINICIAL");
            strFechaFinPer = rs.getString("MPEM_FECHAFINAL");
         }
         rs.close();
         //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
         //Iniciamos transaccion
         this.oConn.runQueryLMD("BEGIN");
         double dblPuntos = 0;
         double dblNegocio = 0;
         //---------------------------- CARGA DE DOCUMENTOS DE REMISION NO PROSEFI
         strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
            + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
            + " sum(vta_tickets.TKT_IMPORTE) AS PNEGOCIO "
            + " FROM vta_tickets,vta_cliente WHERE vta_tickets.CT_ID = vta_cliente.CT_ID "
            + " and vta_tickets.TKT_FECHA>=  '" + strFechaIniPer + "' "
            + " and vta_tickets.TKT_FECHA<=  '" + strFechaFinPer + "' "
            + " and vta_cliente.PRO_MENSUAL = 1"
            + " and vta_tickets.TKT_ANULADA = 0 "
            + " and vta_tickets.TKT_NO_MLM = 0 "
            + " and vta_tickets.PRO_PROSEFI = 0 "
            + " and vta_tickets.TKT_IMPORTE_PUNTOS > 0  "
            + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         log.debug("Consultamos tickets..." + strSql);
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
            + " sum(vta_tickets.TKT_IMPORTE) AS PNEGOCIO "
            + " FROM vta_tickets,vta_cliente WHERE vta_tickets.CT_ID = vta_cliente.CT_ID "
            + " and vta_tickets.TKT_FECHA>=  '" + strFechaIniPer + "' "
            + " and vta_tickets.TKT_FECHA<=  '" + strFechaFinPer + "' "
            + " and vta_cliente.PRO_MENSUAL = 1"
            + " and vta_tickets.TKT_ANULADA = 0 "
            + " and vta_tickets.TKT_NO_MLM = 0 "
            + " and vta_tickets.PRO_PROSEFI = 1 "
            + " and vta_tickets.TKT_IMPORTE_PUNTOS > 0  "
            + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         log.debug("Consultamos tickets..." + strSql);
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
            + " FROM mlm_bono_ind,vta_cliente WHERE mlm_bono_ind.CT_ID = vta_cliente.CT_ID and MPEM_ID =   " + this.intPeriodo
            + " and vta_cliente.PRO_MENSUAL = 1"
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
            + " FROM mlm_bono_ind,vta_cliente WHERE mlm_bono_ind.CT_ID = vta_cliente.CT_ID and MPEM_ID =   " + this.intPeriodo
            + " and vta_cliente.PRO_MENSUAL = 1"
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
                  + " ,PRO_MENSUAL_VENTAS = PRO_MENSUAL_VENTAS + " + cteMlm.getDblNegocio() + " "
                  + " ,PRO_PPUNTOS = PRO_PPUNTOS + " + cteMlm.getDblPuntos() + " "
                  + " ,PRO_PNEGOCIO = PRO_PNEGOCIO + " + cteMlm.getDblNegocio() + " "
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

      //Marcamos los clientes activos
      String strUpdate = "UPDATE vta_cliente SET CT_ACTIVO = 1 "
         + " where PRO_MENSUAL = 1 and CT_PNEGOCIO >=" + lstParams.get(0).getFieldDouble("CP_PPUNTOS");
      this.oConn.runQueryLMD(strUpdate);

      //Calculamos el total de hijos activos por cada nodo
      String strHijos = "select CT_UPLINE, count(CT_ID) AS cuantos "
         + " from vta_cliente where CT_ACTIVO = 1 "
         + " GROUP BY CT_UPLINE";
      try {
         ResultSet rs = this.oConn.runQuery(strHijos, true);
         while (rs.next()) {
            strUpdate = "UPDATE vta_cliente SET CT_CONTEO_HIJOS =  " + rs.getInt("cuantos")
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
         + ",CT_CONTEO_HIJOS,SC_ID,PRO_MENSUAL"
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
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntValor1(rs.getInt("PRO_MENSUAL"));

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
         //Solo procesamos con el mensual
         if (cte.getIntValor1() == 1) {
            //Actualizamos puntos grupales
            cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblNegocio());
            cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblPuntos());
            //cte.setDblValorDbl1(cte.getDblValorDbl1() + cte.getDblPuntos());
            //Calculamos el nivel de la persona
            int intNivel = 0;
            // <editor-fold defaultstate="collapsed" desc="Validamos el nivel">
            if (cte.getDblNegocio() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
//               log.debug(":: id:" + cte.getIntCliente() + " p.monto:" + cte.getDblNegocio() + " gmonto:" + cte.getDblGNegocio() + " minimo:" + lstParams.get(0).getFieldDouble("CP_PPUNTOS"));
               //Recorremos la lista de parametros del plan para definir el nivel
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  //Puntos 
                  if (cte.getDblGNegocio() >= tbn.getFieldDouble("CP_GPUNTOS")) {
                     if (cte.getIntTotalHijos() >= tbn.getFieldInt("CP_HIJOS")) {
                        intNivel = tbn.getFieldInt("CP_NIVEL");
                     }

                  }
               }
            }
            // </editor-fold>
//            log.debug(cte.getIntCliente() + " " + intNivel);
            cte.setIntNivelRed(intNivel);
            // <editor-fold defaultstate="collapsed" desc="Guardamos info del nodo">
            //Subimos info al padre
            int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
            //Si se encontro el padre
            if (intIdx != -1) {
               mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
               ctePadre.setDblGPuntos(ctePadre.getDblGPuntos() + cte.getDblPuntos());
               ctePadre.setDblGNegocio(ctePadre.getDblGNegocio() + cte.getDblGNegocio());
            }
            // </editor-fold>
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
         //Solo procesamos con el mensual
         if (cte.getIntValor1() == 1) {
            // <editor-fold defaultstate="collapsed" desc="Validamos que solo los nodos con valores se guarden">
            if (cte.getDblNegocio() > 0) {

               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " CT_NIVELRED = " + cte.getIntNivelRed() + "";
               strUpdate += ",CT_GPUNTOS = " + cte.getDblGPuntos() + "";
               strUpdate += ",CT_GNEGOCIO = " + cte.getDblGNegocio() + " ";
               strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
            }
            // </editor-fold>
         }

      }
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si son definitivas movemos informacion historica">
      if (this.isEsCorridaDefinitiva()) {
         log.info("Inicia Guardado de historicos en clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV12=PRO_NIV11";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV11=PRO_NIV10";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV10=PRO_NIV9";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV9=PRO_NIV8";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV8=PRO_NIV7";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV7=PRO_NIV6";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV6=PRO_NIV5";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV5=PRO_NIV4";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV4=PRO_NIV3";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV3=PRO_NIV2";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV2=PRO_NIV1";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET PRO_NIV1=CT_GPUNTOS";
//         this.oConn.runQueryLMD(strUpdate);
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
         + ",CT_CONTEO_HIJOS,SC_ID,PRO_MENSUAL"
         + ",PRO_PPUNTOS,PRO_PNEGOCIO,CT_NIVELRED,CT_GNEGOCIO,PRO_MENSUAL_BONO1_CIERRE"
         + ",PRO_MENSUAL_BONO2_CIERRE,PRO_MENSUAL_BONO3_CIERRE "
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
            cte.setDblValorDbl3(rs.getDouble("CT_GNEGOCIO"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntValor1(rs.getInt("PRO_MENSUAL"));
            cte.setIntValor3(rs.getInt("PRO_MENSUAL_BONO1_CIERRE"));
            cte.setIntValor4(rs.getInt("PRO_MENSUAL_BONO2_CIERRE"));
            cte.setIntValor5(rs.getInt("PRO_MENSUAL_BONO3_CIERRE"));
            cte.setIntNivelRed(rs.getInt("CT_NIVELRED"));
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

      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

      int intContaProcess = 0;
      int intContaProcessTot = 0;
      int intContaProcessDif = 0;
      int intContaProcessTotDif = 0;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         double dblImporte = cte.getDblNegocio() /*+ cte.getDblValorDbl2()*/;
         // <editor-fold defaultstate="collapsed" desc="Si el importe de negocio es mayor a cero y es un cliente del mensual">
         if (dblImporte > 0 && cte.getIntValor1() == 1) {
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
                        if (ctePadre.getDblNegocio() >= tbn.getFieldDouble("CP_PPUNTOS")) {
                           intEscalon++;
                           // <editor-fold defaultstate="collapsed" desc="Validamos si hay un porcentaje de pago">
                           if (tbn.getFieldDouble("CP_UNILEVEL" + intEscalon) > 0) {
                              double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL" + intEscalon) / 100);
                              ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
                              //Entidad para el detalle de las comisiones
                              MlmComisionDetMensual comisDeta = new MlmComisionDetMensual();
                              comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
                              comisDeta.setFieldInt("MPEM_ID", this.intPeriodo);
                              comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                              comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
                              comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL" + intEscalon));
                              comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                              comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                              comisDeta.setFieldInt("COMI_NIVEL", intEscalon);
                              comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                              comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                              comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                              comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                              comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                              comisDeta.setFieldString("COMI_CODIGO", "UNI" + intEscalon);
                              comisDeta.Agrega(oConn);
                           }
                           // </editor-fold>
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

            if (intContaProcess == 1000) {
               intContaProcess = 0;
               log.info("Llevamos " + intContaProcessTot + " registros unilevel...");
               System.out.flush();
            }

         }
         // </editor-fold>

      }

      //Ciclo para los bonos
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         // <editor-fold defaultstate="collapsed" desc="Si  es un cliente del mensual y tiene rango lo validamos, esta activo">
         if (cte.getIntValor1() == 1 && cte.getIntNivelRed() > 0) {
            log.debug("Posible bono: " + cte.getIntCliente() + " grupal " + cte.getDblValorDbl3() + " hijos:" + cte.getIntTotalHijos() );
            // <editor-fold defaultstate="collapsed" desc="Bono lealtad 1">
            if (cte.getDblValorDbl3() >= 21726  && cte.getIntValor3()  == 0) {
               if (cte.getIntTotalHijos() >= 6 ) {
                  //Califica
                  log.debug("Califia bono 1 " + cte.getStrNombre());
                  cte.setIntValor2(1);
                  double dblComision = 8000;
                  cte.setDblComision(cte.getDblComision() + dblComision);
                  // <editor-fold defaultstate="collapsed" desc="Entidad para el detalle de las comisiones">
                  MlmComisionDetMensual comisDeta = new MlmComisionDetMensual();
                  comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
                  comisDeta.setFieldInt("MPEM_ID", this.intPeriodo);
                  comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                  comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
                  comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                  comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                  comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                  comisDeta.setFieldInt("COMI_NIVEL", 1);
                  comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                  comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                  comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                  comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                  comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                  comisDeta.setFieldString("COMI_CODIGO", "LEALTAD1");
                  comisDeta.Agrega(oConn);
                  // </editor-fold>
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Bono lealtad 2">
            if (cte.getDblValorDbl3() >= 36210   && cte.getIntValor4()  == 0) {
               if (cte.getIntTotalHijos() >= 10 ) {
                  //Califica
                  log.debug("Califia bono 2 " + cte.getStrNombre());
                  cte.setIntValor2(2);
                  double dblComision = 10000;
                  cte.setDblComision(cte.getDblComision() + dblComision);
                  // <editor-fold defaultstate="collapsed" desc="Entidad para el detalle de las comisiones">
                  MlmComisionDetMensual comisDeta = new MlmComisionDetMensual();
                  comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
                  comisDeta.setFieldInt("MPEM_ID", this.intPeriodo);
                  comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                  comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
                  comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                  comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                  comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                  comisDeta.setFieldInt("COMI_NIVEL", 1);
                  comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                  comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                  comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                  comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                  comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                  comisDeta.setFieldString("COMI_CODIGO", "LEALTAD2");
                  comisDeta.Agrega(oConn);
                  // </editor-fold>
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Bono lealtad 3">
            if (cte.getDblValorDbl3() >= 54315  && cte.getIntValor5()  == 0) {
               if (cte.getIntTotalHijos() >= 15) {
                  //Califica
                  log.debug("Califia bono 3 " + cte.getStrNombre());
                  cte.setIntValor2(3);
                  double dblComision = 13000;
                  cte.setDblComision(cte.getDblComision() + dblComision);
                  // <editor-fold defaultstate="collapsed" desc="Entidad para el detalle de las comisiones">
                  MlmComisionDetMensual comisDeta = new MlmComisionDetMensual();
                  comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
                  comisDeta.setFieldInt("MPEM_ID", this.intPeriodo);
                  comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                  comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
                  comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                  comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                  comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                  comisDeta.setFieldInt("COMI_NIVEL", 1);
                  comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                  comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                  comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                  comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                  comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                  comisDeta.setFieldString("COMI_CODIGO", "LEALTAD3");
                  comisDeta.Agrega(oConn);
                  // </editor-fold>
               }
            }
            // </editor-fold>
         }
         // </editor-fold>
      }

      log.info("Termina Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
         if (cte.getDblComision() != 0) {
            //Realizamos la conversion a la moneda original

            // <editor-fold defaultstate="collapsed" desc="Actualizamos el importe de comision en clientes">
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += " CT_COMISION = " + cte.getDblComision();
            if (cte.getIntValor2() == 1) {
               strUpdate += " ,PRO_MENSUAL_BONO1_ACT = 1 ";
            }
            if (cte.getIntValor2() == 2) {
               strUpdate += " ,PRO_MENSUAL_BONO2_ACT = 1 ";
            }
            if (cte.getIntValor2() == 3) {
               strUpdate += " ,PRO_MENSUAL_BONO3_ACT = 1 ";
            }
            strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
            this.oConn.runQueryLMD(strUpdate);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cabezal">
            MlmComisionMensual comisionM = new MlmComisionMensual();
            comisionM.setFieldInt("CT_ID", cte.getIntCliente());
            comisionM.setFieldInt("MPEM_ID", this.intPeriodo);
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
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Definitivas">
            if (this.isEsCorridaDefinitiva()) {
               // <editor-fold defaultstate="collapsed" desc="Identicamos los nuevos">
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO1 =1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO1_ACT = 1 and PRO_MENSUAL_BONO1 =0 ";
               this.oConn.runQueryLMD(strUpdate);
               //Marcamos los nuevos como activos
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO2 =1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO2_ACT = 1 and PRO_MENSUAL_BONO2 =0 ";
               this.oConn.runQueryLMD(strUpdate);
               //Marcamos los nuevos como activos
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO3 =1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO3_ACT = 1 and PRO_MENSUAL_BONO3 =0 ";
               this.oConn.runQueryLMD(strUpdate);
               //</editor-fold>
               
               // <editor-fold defaultstate="collapsed" desc="Marcamos los que aun estan vigentes y no cumplieron como cerrados">
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO1_CIERRE = 1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO1_ACT = 0 and PRO_MENSUAL_BONO1 =1 ";
               this.oConn.runQueryLMD(strUpdate);
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO2_CIERRE = 1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO2_ACT = 0 and PRO_MENSUAL_BONO2 =1 ";
               this.oConn.runQueryLMD(strUpdate);
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO3_CIERRE = 1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO3_ACT = 0 and PRO_MENSUAL_BONO3 =1 ";
               this.oConn.runQueryLMD(strUpdate);
               //</editor-fold>
               
               // <editor-fold defaultstate="collapsed" desc="Aumentamos contador de los vigentes">
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO1_NUM = PRO_MENSUAL_BONO1_NUM +  1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO1_ACT = 1 and PRO_MENSUAL_BONO1 =1 ";
               this.oConn.runQueryLMD(strUpdate);
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO2_NUM = PRO_MENSUAL_BONO2_NUM +  1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO2_ACT = 1 and PRO_MENSUAL_BONO2 =1 ";
               this.oConn.runQueryLMD(strUpdate);
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO3_NUM = PRO_MENSUAL_BONO3_NUM +  1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO3_ACT = 1 and PRO_MENSUAL_BONO3 =1 ";
               this.oConn.runQueryLMD(strUpdate);
               //</editor-fold>
               
               // <editor-fold defaultstate="collapsed" desc="Marcamos como cerrados los que se pagaron por completo">
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO1_CIERRE = 1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO1_ACT = 1 and PRO_MENSUAL_BONO1 =1 and PRO_MENSUAL_BONO1_NUM = 25";
               this.oConn.runQueryLMD(strUpdate);
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO2_CIERRE = 1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO2_ACT = 1 and PRO_MENSUAL_BONO2 =1 and PRO_MENSUAL_BONO2_NUM = 30";
               this.oConn.runQueryLMD(strUpdate);
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += " PRO_MENSUAL_BONO3_CIERRE = 1 ";
               strUpdate += " WHERE PRO_MENSUAL_BONO3_ACT = 1 and PRO_MENSUAL_BONO3 =1 and PRO_MENSUAL_BONO3_NUM = 38";
               this.oConn.runQueryLMD(strUpdate);
               //</editor-fold>
            }
            //</editor-fold>

         }

      }
      //</editor-fold>

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

   /**
    * Regresa la lista de parametros del plan de comisiones
    *
    * @return Regresa una lista con objetos de la entidad mlm_comis_param
    */
   @Override
   public ArrayList<TableMaster> getParameters() {
      ArrayList<TableMaster> lstParametros = new ArrayList<TableMaster>();
      ParametrosComis paramsTmp = new ParametrosComis();
      //Obtenemos los datos de la base de datos
      lstParametros = paramsTmp.ObtenDatosVarios(" 1=1 ORDER BY CP_ORDEN", oConn);
      return lstParametros;
   }

   /**
    * Calculo el impuesto correspondiente
    *
    * @param cliente Es el cliente
    * @param comision Es el registro de comision maestro
    */
   public void CalculoImpuestos(mlm_cliente cliente, MlmComisionMensual comision) {
   }
   // </editor-fold>
}
