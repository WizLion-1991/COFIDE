/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.klensy;

import ERP.Impuestos;
import ERP.Ticket;
import Tablas.MlmMovComis;
import Tablas.vta_pedidosdeta;
import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.Periodos;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision_deta;
import com.mx.siweb.mlm.compensacion.wenow.ActivaBinario;
import com.mx.siweb.mlm.utilerias.Redes;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.CIP_Tabla;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Esta clase genera el calculo de comision de la empresa Firm o Jonfilu
 *
 * @author aleph_79
 */
public class CalculoComision extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.klensy.CalculoComision.class.getName());
   private VariableSession varSesiones;

   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CalculoComision(Conexion oConn, int intPeriodo, boolean EsCorridaDefinitiva) {
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
      String strSqlP = "Select * from mlm_comision_bitacora where MPE_ID = " + this.intPeriodo;
      try {
         ResultSet rs = this.oConn.runQuery(strSqlP, true);
         while (rs.next()) {
            int intDefinitivas = rs.getInt("CCO_DEFINITIVAS");
            if (intDefinitivas == 1) {
               bolPasa = false;
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error("Error al buscar el periodo en comisiones " + ex.getMessage());
      }

      //Validamos si ya se corrieron definitivas
      if (bolPasa) {
         //Borramos comisiones del periodo
         String strDel1 = "delete from mlm_comision where MPE_ID = " + this.intPeriodo;
         this.oConn.runQueryLMD(strDel1);

         String strDel2 = "delete from mlm_comision_deta where MPE_ID = " + this.intPeriodo;
         this.oConn.runQueryLMD(strDel2);

         // <editor-fold defaultstate="collapsed" desc="Detectamos kits de inscripción confirmados con tickets o facturas">
         doDetectaKit();
         // </editor-fold>

         //Calculamos el arbol
         Redes red = new Redes();
         boolean bolArmo = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
         String strRes = "Se ha generado correctamente la red.";
         if (bolArmo) {
            strRes = "ERROR:" + red.getStrError();
         }

         //SI SON DEFINITIVAS marcamos el proceso
         if (this.isEsCorridaDefinitiva()) {

         }
      } else {
         this.strResultLast = "ERROR:";
      }
      //Si todo es ok limpiamos banderas
      if (this.strResultLast.equals("OK")) {
         String strSQL = "UPDATE vta_cliente SET ";
         strSQL += "CT_PPUNTOS    =  0,";
         strSQL += "CT_GPUNTOS    =  0,";
         strSQL += "CT_PNEGOCIO   =  0,";
         strSQL += "CT_GNEGOCIO   =  0,";
         strSQL += "CT_COMISION    =  0,";
         strSQL += "CT_NIVELRED    =  0,";
         strSQL += "KL_NUM_HIJOS    =  0,";
         strSQL += "KL_NUM_NIETOS    =  0,";
         strSQL += "KL_NUM_BISNIETOS    =  0,";
         strSQL += "KL_NUM_TATA    =  0";
         strSQL += ",KL_HIJOS_CICLO1    =  0";
         strSQL += ",KL_NIETOS_CICLO1    =  0";
         strSQL += ",KL_BISNIETOS_CICLO1    =  0 ";
         strSQL += ",KL_TATA_CICLO1    =  0 ";
         strSQL += ",KL_HIJOS_CICLO2    =  0";
         strSQL += ",KL_NIETOS_CICLO2    =  0";
         strSQL += ",KL_BISNIETOS_CICLO2    =  0 ";
         strSQL += ",KL_TATA_CICLO2    =  0 ";
         strSQL += ",KL_HIJOS_CICLO3    =  0";
         strSQL += ",KL_NIETOS_CICLO3    =  0";
         strSQL += ",KL_BISNIETOS_CICLO3    =  0 ";
         strSQL += ",KL_TATA_CICLO3    =  0 ";
         strSQL += ",KL_HIJOS_CICLO4    =  0";
         strSQL += ",KL_NIETOS_CICLO4    =  0";
         strSQL += ",KL_BISNIETOS_CICLO4    =  0 ";
         strSQL += ",KL_TATA_CICLO4    =  0 ";
         strSQL += ",KL_HIJOS_CICLO5    =  0";
         strSQL += ",KL_NIETOS_CICLO5    =  0";
         strSQL += ",KL_BISNIETOS_CICLO5    =  0 ";
         strSQL += ",KL_TATA_CICLO5    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO1    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO2    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO3    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO4    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO5    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO6    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO7    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO8    =  0 ";
         strSQL += ",KL_PUNTOS_CICLO9    =  0 ";
         this.oConn.runQueryLMD(strSQL);
      }
   }

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
         //---------------------------- CARGA DE DOCUMENTOS DE FACTURA
         strSql = "SELECT KL_CICLO,vta_facturas.CT_ID,vta_facturas.FAC_MONEDA,"
                 + " sum(vta_facturas.FAC_IMPORTE_PUNTOS) AS PPUNTOS, "
                 + " sum(vta_facturas.FAC_IMPORTE_NEGOCIO) AS PNEGOCIO "
                 + " FROM vta_facturas WHERE "
                 + " vta_facturas.FAC_ANULADA = 0 "
                 + " and vta_facturas.FAC_NO_MLM = 0 "
                 + " GROUP BY KL_CICLO,vta_facturas.CT_ID,vta_facturas.FAC_MONEDA";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("PPUNTOS");
            dblNegocio = rs.getDouble("PNEGOCIO");
            int intKL_CICLO = rs.getInt("KL_CICLO");
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
               MlmClienteKlensy cteMlm = (MlmClienteKlensy) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
               if (intKL_CICLO == 1) {
                  cteMlm.setDblPuntosCiclo1(dblPuntos);
               }
               if (intKL_CICLO == 2) {
                  cteMlm.setDblPuntosCiclo2(dblPuntos);
               }
               if (intKL_CICLO == 3) {
                  cteMlm.setDblPuntosCiclo3(dblPuntos);
               }
               if (intKL_CICLO == 4) {
                  cteMlm.setDblPuntosCiclo4(dblPuntos);
               }
               if (intKL_CICLO == 5) {
                  cteMlm.setDblPuntosCiclo5(dblPuntos);
               }
            } else {
               MlmClienteKlensy cteMlm = new MlmClienteKlensy();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               if (intKL_CICLO == 1) {
                  cteMlm.setDblPuntosCiclo1(dblPuntos);
               }
               if (intKL_CICLO == 2) {
                  cteMlm.setDblPuntosCiclo2(dblPuntos);
               }
               if (intKL_CICLO == 3) {
                  cteMlm.setDblPuntosCiclo3(dblPuntos);
               }
               if (intKL_CICLO == 4) {
                  cteMlm.setDblPuntosCiclo4(dblPuntos);
               }
               if (intKL_CICLO == 5) {
                  cteMlm.setDblPuntosCiclo5(dblPuntos);
               }
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //CARGA DE DOCUMENTOS DE TICKETS
         strSql = "SELECT KL_CICLO,vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
                 + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
                 + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
                 + " FROM vta_tickets WHERE "
                 + " vta_tickets.TKT_ANULADA = 0 "
                 + " and vta_tickets.TKT_NO_MLM = 0 "
                 + " GROUP BY KL_CICLO,vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("PPUNTOS");
            dblNegocio = rs.getDouble("PNEGOCIO");
            int intKL_CICLO = rs.getInt("KL_CICLO");
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
               MlmClienteKlensy cteMlm = (MlmClienteKlensy) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
               if (intKL_CICLO == 1) {
                  cteMlm.setDblPuntosCiclo1(dblPuntos);
               }
               if (intKL_CICLO == 2) {
                  cteMlm.setDblPuntosCiclo2(dblPuntos);
               }
               if (intKL_CICLO == 3) {
                  cteMlm.setDblPuntosCiclo3(dblPuntos);
               }
               if (intKL_CICLO == 4) {
                  cteMlm.setDblPuntosCiclo4(dblPuntos);
               }
               if (intKL_CICLO == 5) {
                  cteMlm.setDblPuntosCiclo5(dblPuntos);
               }
            } else {
               MlmClienteKlensy cteMlm = new MlmClienteKlensy();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               if (intKL_CICLO == 1) {
                  cteMlm.setDblPuntosCiclo1(dblPuntos);
               }
               if (intKL_CICLO == 2) {
                  cteMlm.setDblPuntosCiclo2(dblPuntos);
               }
               if (intKL_CICLO == 3) {
                  cteMlm.setDblPuntosCiclo3(dblPuntos);
               }
               if (intKL_CICLO == 4) {
                  cteMlm.setDblPuntosCiclo4(dblPuntos);
               }
               if (intKL_CICLO == 5) {
                  cteMlm.setDblPuntosCiclo5(dblPuntos);
               }
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //CARGA DE MEMORANDA
         strSql = "SELECT "
                 + "	mlm_bono_ind.CT_ID, 	"
                 + " SUM(mlm_bono_ind.MBI_PUNTOS) AS TPUNTOS, 	"
                 + " SUM(mlm_bono_ind.MBI_NEGOCIO) AS TNEGOCIO  "
                 + " FROM mlm_bono_ind WHERE MPE_ID =   " + this.intPeriodo
                 + " GROUP BY mlm_bono_ind.CT_ID";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("TPUNTOS");
            dblNegocio = rs.getDouble("TNEGOCIO");
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               MlmClienteKlensy cteMlm = (MlmClienteKlensy) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               MlmClienteKlensy cteMlm = new MlmClienteKlensy();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
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
            MlmClienteKlensy cteMlm = (MlmClienteKlensy) e.getValue();
            //Guardamos info
            if (cteMlm.getDblPuntos() > 0 || cteMlm.getDblNegocio() > 0) {
               String strUpdate = "update vta_cliente set "
                       + " CT_PPUNTOS = CT_PPUNTOS + " + cteMlm.getDblPuntos()
                       + " ,KL_PUNTOS_CICLO1 = KL_PUNTOS_CICLO1 + " + cteMlm.getDblPuntosCiclo1()
                       + " ,KL_PUNTOS_CICLO2 = KL_PUNTOS_CICLO2 + " + cteMlm.getDblPuntosCiclo2()
                       + " ,KL_PUNTOS_CICLO3 = KL_PUNTOS_CICLO3 + " + cteMlm.getDblPuntosCiclo3()
                       + " ,KL_PUNTOS_CICLO4 = KL_PUNTOS_CICLO4 + " + cteMlm.getDblPuntosCiclo4()
                       + " ,CT_PNEGOCIO = CT_PNEGOCIO + " + cteMlm.getDblNegocio()
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
      ArrayList<MlmClienteKlensy> lstMlmCliente = new ArrayList<MlmClienteKlensy>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para conteo de hijos">
      log.info("Inicia Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      this.oConn.runQueryLMD("BEGIN");
      //Calculamos el total de hijos por cada nodo
      String strHijos = "select CT_SPONZOR, count(CT_ID) AS cuantos "
              + " from vta_cliente where CT_ACTIVO = 1 "
              + " GROUP BY CT_SPONZOR";
      try {
         ResultSet rs = this.oConn.runQuery(strHijos, true);
         while (rs.next()) {
            String strUpdate = "UPDATE vta_cliente SET KL_REFERENCIADOS =  " + rs.getInt("cuantos")
                    + " where CT_ID = " + rs.getInt("CT_SPONZOR");
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
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,"
              + "CT_PPUNTOS,CT_PNEGOCIO,CT_CONTEO_HIJOS,KL_REFERENCIADOS,SC_ID"
              + ",KL_PLAN_ORO,KL_CICLO_ACTUAL,KL_CICLO_CERRADO,KL_NIVEL_MAX"
              + ",KL_CONSUME_PTOS_CICLO1,KL_CONSUME_PTOS_CICLO2,KL_CONSUME_PTOS_CICLO3,KL_CONSUME_PTOS_CICLO4 "
              + " from vta_cliente order by CT_ARMADONUM";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intConta++;
            MlmClienteKlensy cte = new MlmClienteKlensy();
            cte.setIntCliente(rs.getInt("CT_ID"));
            cte.setIntUpline(rs.getInt("CT_UPLINE"));
            cte.setDblPuntos(rs.getDouble("CT_PPUNTOS"));
            cte.setDblNegocio(rs.getDouble("CT_PNEGOCIO"));
            cte.setIntReferenciados(rs.getInt("KL_REFERENCIADOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntPlanOro(rs.getInt("KL_PLAN_ORO"));//Bandera para detectar plan oro o premier
            cte.setIntCicloActual(rs.getInt("KL_CICLO_ACTUAL"));
            cte.setIntCicloCerrado(rs.getInt("KL_CICLO_CERRADO"));
            cte.setIntNivelRedAnterior(rs.getInt("KL_NIVEL_MAX"));
            cte.setIntConsumePuntos1(rs.getInt("KL_CONSUME_PTOS_CICLO1"));
            cte.setIntConsumePuntos2(rs.getInt("KL_CONSUME_PTOS_CICLO2"));
            cte.setIntConsumePuntos3(rs.getInt("KL_CONSUME_PTOS_CICLO3"));
            cte.setIntConsumePuntos4(rs.getInt("KL_CONSUME_PTOS_CICLO4"));
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
         MlmClienteKlensy cte = lstMlmCliente.get(i);
         //Actualizamos puntos grupales
         log.debug("Calculo de nivel..." + cte.getIntCliente());
         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblNegocio());
         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblPuntos());

         //Calculamos el nivel de la persona
         int intNivel = 0;
         // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan para definir el nivel">
         Iterator<TableMaster> it = lstParams.iterator();
         while (it.hasNext()) {
            TableMaster tbn = it.next();
            log.debug("Nivel red:" + tbn.getFieldInt("CP_NIVEL"));
            log.debug(cte.getIntHijos() + " " + tbn.getFieldInt("CP_HIJOS"));
            log.debug(cte.getIntNietos() + " " + tbn.getFieldInt("CP_NIETOS"));
            log.debug(cte.getIntBisnietos() + " " + tbn.getFieldInt("CP_BISNIETOS"));
            log.debug(cte.getIntTataranietos() + " " + tbn.getFieldInt("CP_TATARANIETOS"));
            log.debug(cte.getIntPlanOro() + " " + tbn.getFieldInt("CP_PREMIER"));
            log.debug("Nivel anterior: " + cte.getIntCicloCerrado());
            if (cte.getIntReferenciados() >= 2
                    /**
                     * Para que se active debemos de referenciar a 2 personas
                     */
                    && cte.getIntHijos() >= tbn.getFieldInt("CP_HIJOS")
                    && cte.getIntNietos() >= tbn.getFieldInt("CP_NIETOS")
                    && cte.getIntBisnietos() >= tbn.getFieldInt("CP_BISNIETOS")
                    && cte.getIntTataranietos() >= tbn.getFieldInt("CP_TATARANIETOS")
                    /*Ciclo1*/
                    && cte.getIntHijosCiclo1() >= tbn.getFieldInt("CP_HIJOS_CICLO1")
                    && cte.getIntNietosCiclo1() >= tbn.getFieldInt("CP_NIETOS_CICLO1")
                    && cte.getIntBisnietosCiclo1() >= tbn.getFieldInt("CP_BISNIETOS_CICLO1")
                    && cte.getIntTataranietosCiclo1() >= tbn.getFieldInt("CP_TATARANIETOS_CICLO1")
                    && cte.getIntConsumePuntos1() >= tbn.getFieldInt("CP_CONSUME_CICLO1")
                    /*Ciclo1*/
                    /*Ciclo2*/
                    && cte.getIntHijosCiclo2() >= tbn.getFieldInt("CP_HIJOS_CICLO2")
                    && cte.getIntNietosCiclo2() >= tbn.getFieldInt("CP_NIETOS_CICLO2")
                    && cte.getIntBisnietosCiclo2() >= tbn.getFieldInt("CP_BISNIETOS_CICLO2")
                    && cte.getIntTataranietosCiclo2() >= tbn.getFieldInt("CP_TATARANIETOS_CICLO2")
                    && cte.getIntConsumePuntos2() >= tbn.getFieldInt("CP_CONSUME_CICLO2")
                    /*Ciclo2*/
                    /*Ciclo3*/
                    && cte.getIntHijosCiclo3() >= tbn.getFieldInt("CP_HIJOS_CICLO3")
                    && cte.getIntNietosCiclo3() >= tbn.getFieldInt("CP_NIETOS_CICLO3")
                    && cte.getIntBisnietosCiclo3() >= tbn.getFieldInt("CP_BISNIETOS_CICLO3")
                    && cte.getIntTataranietosCiclo3() >= tbn.getFieldInt("CP_TATARANIETOS_CICLO3")
                    && cte.getIntConsumePuntos2() >= tbn.getFieldInt("CP_CONSUME_CICLO3")
                    /*Ciclo3*/
                    /*Ciclo4*/
                    && cte.getIntHijosCiclo4() >= tbn.getFieldInt("CP_HIJOS_CICLO4")
                    && cte.getIntNietosCiclo4() >= tbn.getFieldInt("CP_NIETOS_CICLO4")
                    && cte.getIntBisnietosCiclo4() >= tbn.getFieldInt("CP_BISNIETOS_CICLO4")
                    && cte.getIntTataranietosCiclo4() >= tbn.getFieldInt("CP_TATARANIETOS_CICLO4")
                    && cte.getIntConsumePuntos4() >= tbn.getFieldInt("CP_CONSUME_CICLO4")
                    /*Ciclo4*/
                    && cte.getIntPlanOro() == tbn.getFieldInt("CP_PREMIER") /*Validamos que halla usado los puntos anteriores...*/) {
               if (cte.getIntNivelRedAnterior() < tbn.getFieldInt("CP_NIVEL")) {
                  intNivel = tbn.getFieldInt("CP_NIVEL");
                  cte.setIntNivelRed(intNivel);
                  cte.setBolCierraCiclo(true);
                  cte.setIntCicloActual(tbn.getFieldInt("CP_CICLO_ACTUAL"));
                  log.debug("Si cumple nivel " + intNivel + " y cierra");
                  break;
               }

            }
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Asignamos el nivel anterior si aun no se cierra el ciclo...">
         if (!cte.isBolCierraCiclo()) {
            cte.setIntNivelRed(cte.getIntNivelRedAnterior());
            if (cte.getIntNivelRedAnterior() == 0) {
               cte.setIntCicloActual(1);
               cte.setIntNivelRed(1);
               //El primer nivel del plan oro es el 5
               if (cte.getIntPlanOro() == 1) {
                  cte.setIntNivelRed(5);
               }
            }
            if (cte.getIntNivelRedAnterior() == 1) {
               cte.setIntCicloActual(2);
            }
            if (cte.getIntNivelRedAnterior() == 2) {
               cte.setIntCicloActual(3);
            }
            if (cte.getIntNivelRedAnterior() == 3) {
               cte.setIntCicloActual(4);
            }
            if (cte.getIntNivelRedAnterior() == 4) {
               cte.setIntCicloActual(1);
            }
            if (cte.getIntNivelRedAnterior() == 5) {
               cte.setIntCicloActual(2);
            }
            if (cte.getIntNivelRedAnterior() == 6) {
               cte.setIntCicloActual(3);
            }
            if (cte.getIntNivelRedAnterior() == 7) {
               cte.setIntCicloActual(4);
            }
            if (cte.getIntNivelRedAnterior() == 8) {
               cte.setIntCicloActual(5);
            }
            if (cte.getIntNivelRedAnterior() == 9) {
               cte.setIntCicloActual(5);
            }

         }
         // </editor-fold>

         //Guardamos info del nodo
         //Subimos info al padre
//         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
//         //Si se encontro el padre
//         if (intIdx != -1) {
//            MlmClienteKlensy ctePadre = lstMlmCliente.get(intIdx);
//            log.debug(cte.getIntUpline() + " intIdx" + intIdx);
//         }
         //----- 1 GENERACION ---
         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
         if (intIdx != -1) {

            MlmClienteKlensy ctePadre = lstMlmCliente.get(intIdx);
            ctePadre.setDblGNegocio(ctePadre.getDblGNegocio() + cte.getDblGNegocio());
            ctePadre.setDblGPuntos(ctePadre.getDblGPuntos() + cte.getDblGPuntos());
            ctePadre.setDblValorDbl1(ctePadre.getDblValorDbl1() + cte.getDblNegocio());
            ctePadre.setDblValorDbl5(ctePadre.getDblValorDbl5() + cte.getDblPuntos());
            ctePadre.setIntHijos(ctePadre.getIntHijos() + 1);
            //Conteos de acuerdo al nivel
            if ((cte.getIntCicloActual() == 1 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 1) {
               ctePadre.setIntHijosCiclo1(ctePadre.getIntHijosCiclo1() + 1);
            }
            if ((cte.getIntCicloActual() == 2 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 2) {
               ctePadre.setIntHijosCiclo2(ctePadre.getIntHijosCiclo2() + 1);
            }
            if ((cte.getIntCicloActual() == 3 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 3) {
               ctePadre.setIntHijosCiclo3(ctePadre.getIntHijosCiclo3() + 1);
            }
            if ((cte.getIntCicloActual() == 4 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 4) {
               ctePadre.setIntHijosCiclo4(ctePadre.getIntHijosCiclo4() + 1);
            }
            if ((cte.getIntCicloActual() == 5 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 5) {
               ctePadre.setIntHijosCiclo5(ctePadre.getIntHijosCiclo5() + 1);
            }

            int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
            //----- 2 GENERACION ---
            //Si se encontro el abuelo
            if (intIdx2 != -1) {
               MlmClienteKlensy ctePadre2 = lstMlmCliente.get(intIdx2);
               ctePadre2.setIntNietos(ctePadre2.getIntNietos() + 1);
               //Conteos de acuerdo al nivel
               if ((cte.getIntCicloActual() == 1 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 1) {
                  ctePadre2.setIntNietosCiclo1(ctePadre2.getIntNietosCiclo1() + 1);
               }
               if ((cte.getIntCicloActual() == 2 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 2) {
                  ctePadre2.setIntNietosCiclo2(ctePadre2.getIntNietosCiclo2() + 1);
               }
               if ((cte.getIntCicloActual() == 3 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 3) {
                  ctePadre2.setIntNietosCiclo3(ctePadre2.getIntNietosCiclo3() + 1);
               }
               if ((cte.getIntCicloActual() == 4 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 4) {
                  ctePadre2.setIntNietosCiclo4(ctePadre2.getIntNietosCiclo4() + 1);
               }
               if ((cte.getIntCicloActual() == 5 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 5) {
                  ctePadre2.setIntNietosCiclo5(ctePadre2.getIntNietosCiclo5() + 1);
               }
               //----- 3 GENERACION ---
               int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
               //Si se encontro el abuelo
               if (intIdx3 != -1) {
                  MlmClienteKlensy ctePadre3 = lstMlmCliente.get(intIdx3);
                  ctePadre3.setIntBisnietos(ctePadre3.getIntBisnietos() + 1);
                  //Conteos de acuerdo al nivel
                  if ((cte.getIntCicloActual() == 1 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 1) {
                     ctePadre3.setIntBisnietosCiclo1(ctePadre3.getIntBisnietosCiclo1() + 1);
                  }
                  if ((cte.getIntCicloActual() == 2 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 2) {
                     ctePadre3.setIntBisnietosCiclo2(ctePadre3.getIntBisnietosCiclo2() + 1);
                  }
                  if ((cte.getIntCicloActual() == 3 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 3) {
                     ctePadre3.setIntBisnietosCiclo3(ctePadre3.getIntBisnietosCiclo3() + 1);
                  }
                  if ((cte.getIntCicloActual() == 4 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 4) {
                     ctePadre3.setIntBisnietosCiclo4(ctePadre3.getIntBisnietosCiclo4() + 1);
                  }
                  if ((cte.getIntCicloActual() == 5 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 5) {
                     ctePadre3.setIntBisnietosCiclo5(ctePadre3.getIntBisnietosCiclo5() + 1);
                  }
                  //----- 4 GENERACION ---
                  int intIdx4 = this.BuscaNodoenArreglo(ctePadre3.getIntUpline(), lstMlmClienteIdx, false);
                  //Si se encontro el abuelo
                  if (intIdx4 != -1) {
                     MlmClienteKlensy ctePadre4 = lstMlmCliente.get(intIdx4);
                     ctePadre4.setIntTataranietos(ctePadre4.getIntTataranietos() + 1);
                     //Conteos de acuerdo al nivel
                     if ((cte.getIntCicloActual() == 1 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 1) {
                        ctePadre4.setIntTataranietosCiclo1(ctePadre4.getIntTataranietosCiclo1() + 1);
                     }
                     if ((cte.getIntCicloActual() == 2 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 2) {
                        ctePadre4.setIntTataranietosCiclo2(ctePadre4.getIntTataranietosCiclo2() + 1);
                     }
                     if ((cte.getIntCicloActual() == 3 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 3) {
                        ctePadre4.setIntTataranietosCiclo3(ctePadre4.getIntTataranietosCiclo3() + 1);
                     }
                     if ((cte.getIntCicloActual() == 4 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 4) {
                        ctePadre4.setIntTataranietosCiclo4(ctePadre4.getIntTataranietosCiclo4() + 1);
                     }
                     if ((cte.getIntCicloActual() == 5 && cte.isBolCierraCiclo()) || cte.getIntCicloCerrado() == 5) {
                        ctePadre4.setIntTataranietosCiclo5(ctePadre4.getIntTataranietosCiclo5() + 1);
                     }
                  }
               }
            }
         }

      }
      log.info("Termina Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de Unilevel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intContaProcess = 0;
      int intContaProcessTot = 0;

      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteKlensy cte = lstMlmCliente.get(i);
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

               MlmClienteKlensy ctePadre = lstMlmCliente.get(intIdx);
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
                     comisDeta.Agrega(oConn);
//                     ctePadre.getLstDeta().add(comisDeta);
                  }
                  // </editor-fold>
               }//Cierre busqueda de nivel para aplicar el factor de unilevel
               int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
               //----- 2 GENERACION ---
               //Si se encontro el abuelo
               if (intIdx2 != -1) {
                  MlmClienteKlensy ctePadre2 = lstMlmCliente.get(intIdx2);
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
                        comisDeta.Agrega(oConn);
                        //ctePadre.getLstDeta().add(comisDeta);
                     }
                     // </editor-fold>
                  }//Cierre busqueda de nivel para aplicar el factor de unilevel
                  //----- 3 GENERACION ---
                  int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
                  //Si se encontro el abuelo
                  if (intIdx3 != -1) {
                     MlmClienteKlensy ctePadre3 = lstMlmCliente.get(intIdx3);
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
                     //----- 4 GENERACION ---
                     int intIdx4 = this.BuscaNodoenArreglo(ctePadre3.getIntUpline(), lstMlmClienteIdx, false);
                     //Si se encontro el abuelo
                     if (intIdx4 != -1) {
                        MlmClienteKlensy ctePadre4 = lstMlmCliente.get(intIdx4);
                        //Recorremos la lista de parametros del plan obtener los porcentajes
                        it = lstParams.iterator();
                        while (it.hasNext()) {
                           TableMaster tbn = it.next();
                           // <editor-fold defaultstate="collapsed" desc="----- 4 GENERACION ---">
                           if (ctePadre4.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                   && ctePadre4.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                              double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL4") / 100);
                              ctePadre4.setDblComision(ctePadre4.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/

                              //Entidad para el detalle de las comisiones

                              mlm_comision_deta comisDeta = new mlm_comision_deta();
                              comisDeta.setFieldInt("CT_ID", ctePadre4.getIntCliente());
                              comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                              comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                              comisDeta.setFieldInt("COMI_DESTINO", ctePadre4.getIntCliente());
                              comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                              comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                              comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                              comisDeta.setFieldInt("COMI_NIVEL", ctePadre4.getIntNivelRed());
                              comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                              comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                              comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                              comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                              comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                              comisDeta.setFieldString("COMI_CODIGO", "UNI4");
                              comisDeta.Agrega(oConn);
//                              ctePadre.getLstDeta().add(comisDeta);
                           }
                           // </editor-fold>
                        }//Cierre busqueda de nivel para aplicar el factor de unilevel
                     }
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
      log.info("Termina Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD("BEGIN");

      //Guardamos informacion de los nodos
      log.info("Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteKlensy cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
         if (cte.getDblPuntos() != 0
                 || cte.getDblValorDbl2() != 0
                 || cte.getDblValorDbl3() != 0
                 || cte.getDblValorDbl4() != 0
                 || cte.getDblValorDbl5() != 0
                 || cte.getDblValorDbl6() != 0
                 || cte.getDblValorDbl7() != 0
                 || cte.getDblValorDbl8() != 0
                 || cte.getDblGPuntos() != 0
                 || cte.getDblGNegocio() != 0
                 || cte.getIntCicloActual() != 0
                 || cte.getIntNivelRed() != 0) {
            //G_NEGOCIO
            //G_PUNTOS
            //NIVELRED
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "KL_CICLO_ACTUAL = " + cte.getIntCicloActual() + ",";
            strUpdate += "CT_NIVELRED = " + cte.getIntNivelRed() + ",";
            strUpdate += "CT_GPUNTOS = " + (cte.getDblGPuntos()) + ",";
            strUpdate += "CT_GNEGOCIO = " + cte.getDblGNegocio() + " ";
            strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
            this.oConn.runQueryLMD(strUpdate);

            //Guardado dependiendo del ciclo
            if (cte.getIntCicloActual() == 1 && cte.isBolCierraCiclo()) {
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += "KL_CICLO_CERRADO = 1,";
               strUpdate += "KL_CICLO1_FECHA = " + fecha.getFechaActual() + ",";
               strUpdate += "KL_CICLO1_PER = " + this.intPeriodo + ",";
               strUpdate += "KL_CICLO1_COMIS = " + cte.getDblComision() + ",";
               strUpdate += "KL_CICLO1_SALDO = " + cte.getDblComision() + "";
               strUpdate += ",KL_NIVEL_MAX = " + cte.getIntNivelRed() + "";
               strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
               //Detalle de comisiones
               MlmMovComis nMov = new MlmMovComis();
               nMov.setFieldString("MMC_FECHA", fecha.getFechaActual());
               nMov.setFieldInt("CT_ID", cte.getIntCliente());
               nMov.setFieldDouble("MMC_CARGO", 0.0);
               nMov.setFieldDouble("MMC_ABONO", cte.getDblComision());
               nMov.setFieldString("MMC_NOTAS", "CIERRE CICLO 1");
               nMov.setFieldInt("MMC_USUARIO", 0);
               nMov.setFieldInt("FAC_ID", 0);
               nMov.setFieldInt("MC_ID", 0);
               nMov.setFieldInt("NC_ID", 0);
               nMov.setFieldInt("MPE_ID", this.intPeriodo);
               nMov.Agrega(oConn);
            }
            if (cte.getIntCicloActual() == 2 && cte.isBolCierraCiclo()) {
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += "KL_CICLO_CERRADO = 2,";
               strUpdate += "KL_CICLO2_FECHA = " + fecha.getFechaActual() + ",";
               strUpdate += "KL_CICLO2_PER = " + this.intPeriodo + ",";
               strUpdate += "KL_CICLO2_COMIS = " + cte.getDblComision() + ",";
               strUpdate += "KL_CICLO2_SALDO = " + cte.getDblComision() + "";
               strUpdate += ",KL_NIVEL_MAX = " + cte.getIntNivelRed() + "";
               strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
               //Detalle de comisiones
               MlmMovComis nMov = new MlmMovComis();
               nMov.setFieldString("MMC_FECHA", fecha.getFechaActual());
               nMov.setFieldInt("CT_ID", cte.getIntCliente());
               nMov.setFieldDouble("MMC_CARGO", 0.0);
               nMov.setFieldDouble("MMC_ABONO", cte.getDblComision());
               nMov.setFieldString("MMC_NOTAS", "CIERRE CICLO 2");
               nMov.setFieldInt("MMC_USUARIO", 0);
               nMov.setFieldInt("FAC_ID", 0);
               nMov.setFieldInt("MC_ID", 0);
               nMov.setFieldInt("NC_ID", 0);
               nMov.setFieldInt("MPE_ID", this.intPeriodo);
               nMov.Agrega(oConn);
            }
            if (cte.getIntCicloActual() == 3 && cte.isBolCierraCiclo()) {
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += "KL_CICLO_CERRADO = 3,";
               strUpdate += "KL_CICLO3_FECHA = " + fecha.getFechaActual() + ",";
               strUpdate += "KL_CICLO3_PER = " + this.intPeriodo + ", ";
               strUpdate += "KL_CICLO3_COMIS = " + cte.getDblComision() + ", ";
               strUpdate += "KL_CICLO3_SALDO = " + cte.getDblComision() + " ";
               strUpdate += ",KL_NIVEL_MAX = " + cte.getIntNivelRed() + "";
               strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
               //Detalle de comisiones
               MlmMovComis nMov = new MlmMovComis();
               nMov.setFieldString("MMC_FECHA", fecha.getFechaActual());
               nMov.setFieldInt("CT_ID", cte.getIntCliente());
               nMov.setFieldDouble("MMC_CARGO", 0.0);
               nMov.setFieldDouble("MMC_ABONO", cte.getDblComision());
               nMov.setFieldString("MMC_NOTAS", "CIERRE CICLO 3");
               nMov.setFieldInt("MMC_USUARIO", 0);
               nMov.setFieldInt("FAC_ID", 0);
               nMov.setFieldInt("MC_ID", 0);
               nMov.setFieldInt("NC_ID", 0);
               nMov.setFieldInt("MPE_ID", this.intPeriodo);
               nMov.Agrega(oConn);
            }
            if (cte.getIntCicloActual() == 4 && cte.isBolCierraCiclo()) {
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += "KL_CICLO_CERRADO = 4,";
               strUpdate += "KL_CICLO4_FECHA = " + fecha.getFechaActual() + ",";
               strUpdate += "KL_CICLO4_PER = " + this.intPeriodo + ", ";
               strUpdate += "KL_CICLO4_COMIS = " + cte.getDblComision() + ", ";
               strUpdate += "KL_CICLO4_SALDO = " + cte.getDblComision() + " ";
               strUpdate += ",KL_NIVEL_MAX = " + cte.getIntNivelRed() + "";
               strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);

               //Detalle de comisiones
               MlmMovComis nMov = new MlmMovComis();
               nMov.setFieldString("MMC_FECHA", fecha.getFechaActual());
               nMov.setFieldInt("CT_ID", cte.getIntCliente());
               nMov.setFieldDouble("MMC_CARGO", 0.0);
               nMov.setFieldDouble("MMC_ABONO", cte.getDblComision());
               nMov.setFieldString("MMC_NOTAS", "CIERRE CICLO 4");
               nMov.setFieldInt("MMC_USUARIO", 0);
               nMov.setFieldInt("FAC_ID", 0);
               nMov.setFieldInt("MC_ID", 0);
               nMov.setFieldInt("NC_ID", 0);
               nMov.setFieldInt("MPE_ID", this.intPeriodo);
               nMov.Agrega(oConn);
               //Validamos si es el ciclo normal para que generemos el centro
               //de negocios adicional
               if (cte.getIntPlanOro() == 0) {
                  //Generamos un centro de negocios adicional...
                  GeneraCentroOro(cte.getIntCliente());
               }
            }
            if (cte.getIntCicloActual() == 5 && cte.isBolCierraCiclo()) {
               strUpdate = "UPDATE vta_cliente SET ";
               strUpdate += "KL_CICLO_CERRADO = 5,";
               strUpdate += "KL_CICLO5_FECHA = " + fecha.getFechaActual() + ",";
               strUpdate += "KL_CICLO5_PER = " + this.intPeriodo + ", ";
               strUpdate += "KL_CICLO5_COMIS = " + cte.getDblComision() + ", ";
               strUpdate += "KL_CICLO5_SALDO = " + cte.getDblComision() + " ";
               strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);

               //Detalle de comisiones
               MlmMovComis nMov = new MlmMovComis();
               nMov.setFieldString("MMC_FECHA", fecha.getFechaActual());
               nMov.setFieldInt("CT_ID", cte.getIntCliente());
               nMov.setFieldDouble("MMC_CARGO", 0.0);
               nMov.setFieldDouble("MMC_ABONO", cte.getDblComision());
               nMov.setFieldString("MMC_NOTAS", "CIERRE CICLO 5");
               nMov.setFieldInt("MMC_USUARIO", 0);
               nMov.setFieldInt("FAC_ID", 0);
               nMov.setFieldInt("MC_ID", 0);
               nMov.setFieldInt("NC_ID", 0);
               nMov.setFieldInt("MPE_ID", this.intPeriodo);
               nMov.Agrega(oConn);
            }
         }

      }
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si son definitivas movemos informacion historica">
      if (this.isEsCorridaDefinitiva()) {
         log.info("Inicia Guardado de historicos en clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
//         String strUpdate = "UPDATE vta_cliente SET XOCO_MES_15=XOCO_MES_14";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_14=XOCO_MES_13";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_13=XOCO_MES_12";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_12=XOCO_MES_11";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_11=XOCO_MES_10";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_10=XOCO_MES_9";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_9=XOCO_MES_8";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_8=XOCO_MES_7";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_7=XOCO_MES_6";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_6=XOCO_MES_5";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_5=XOCO_MES_4";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_4=XOCO_MES_3";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_3=XOCO_MES_2";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_2=XOCO_MES_1";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_MES_1=CT_GPUNTOS";
//         this.oConn.runQueryLMD(strUpdate);
//         //Guardamos el periodo del nombramiento
//         strUpdate = "UPDATE vta_cliente SET XOCO_SE_NOMBRO_2=" + this.getIntPeriodo() + " where CT_NIVELRED =2 AND XOCO_SE_NOMBRO_2 = 0";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_SE_NOMBRO_3=" + this.getIntPeriodo() + " where CT_NIVELRED =3 AND XOCO_SE_NOMBRO_3 = 0";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_SE_NOMBRO_4=" + this.getIntPeriodo() + " where CT_NIVELRED =4 AND XOCO_SE_NOMBRO_4 = 0";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_SE_NOMBRO_5=" + this.getIntPeriodo() + " where CT_NIVELRED =5 AND XOCO_SE_NOMBRO_5 = 0";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_SE_NOMBRO_6=" + this.getIntPeriodo() + " where CT_NIVELRED =6 AND XOCO_SE_NOMBRO_6 = 0";
//         this.oConn.runQueryLMD(strUpdate);
//         strUpdate = "UPDATE vta_cliente SET XOCO_SE_NOMBRO_7=" + this.getIntPeriodo() + " where CT_NIVELRED =7 AND XOCO_SE_NOMBRO_7 = 0";
//         this.oConn.runQueryLMD(strUpdate);

         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteKlensy cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
//         if (cte.getDblPuntos() != 0) {
         //Realizamos la conversion a la moneda original

         //Actualizamos el importe de comision en clientes
//         if (cte.getIntPlanOro() == 0) {
         //Ciclos normales
         String strUpdate = "UPDATE vta_cliente SET ";
         strUpdate += " CT_COMISION = " + cte.getDblComision();
         strUpdate += " ,KL_NUM_HIJOS = " + cte.getIntHijos();
         strUpdate += " ,KL_NUM_NIETOS = " + cte.getIntNietos();
         strUpdate += " ,KL_NUM_BISNIETOS = " + cte.getIntBisnietos();
         strUpdate += " ,KL_NUM_TATA = " + cte.getIntTataranietos();

         strUpdate += " ,KL_HIJOS_CICLO1 = " + cte.getIntHijosCiclo1();
         strUpdate += " ,KL_NIETOS_CICLO1 = " + cte.getIntNietosCiclo1();
         strUpdate += " ,KL_BISNIETOS_CICLO1 = " + cte.getIntBisnietosCiclo1();
         strUpdate += " ,KL_TATA_CICLO1 = " + cte.getIntTataranietosCiclo1();

         strUpdate += " ,KL_HIJOS_CICLO2 = " + cte.getIntHijosCiclo2();
         strUpdate += " ,KL_NIETOS_CICLO2 = " + cte.getIntNietosCiclo2();
         strUpdate += " ,KL_BISNIETOS_CICLO2 = " + cte.getIntBisnietosCiclo2();
         strUpdate += " ,KL_TATA_CICLO2 = " + cte.getIntTataranietosCiclo2();

         strUpdate += " ,KL_HIJOS_CICLO3 = " + cte.getIntHijosCiclo3();
         strUpdate += " ,KL_NIETOS_CICLO3 = " + cte.getIntNietosCiclo3();
         strUpdate += " ,KL_BISNIETOS_CICLO3 = " + cte.getIntBisnietosCiclo3();
         strUpdate += " ,KL_TATA_CICLO3 = " + cte.getIntTataranietosCiclo3();

         strUpdate += " ,KL_HIJOS_CICLO4 = " + cte.getIntHijosCiclo4();
         strUpdate += " ,KL_NIETOS_CICLO4 = " + cte.getIntNietosCiclo4();
         strUpdate += " ,KL_BISNIETOS_CICLO4 = " + cte.getIntBisnietosCiclo4();
         strUpdate += " ,KL_TATA_CICLO4 = " + cte.getIntTataranietosCiclo4();

         strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
         this.oConn.runQueryLMD(strUpdate);

//         } else {
//            //Ciclo premier
//            String strUpdate = "UPDATE vta_cliente SET ";
//            strUpdate += " CT_COMISION = " + cte.getDblComision();
//            strUpdate += " ,KL_NUM_HIJOS_ORO = " + cte.getIntValor1();
//            strUpdate += " ,KL_NUM_NIETOS_ORO = " + cte.getIntValor2();
//            strUpdate += " ,KL_NUM_BISNIETOS_ORO = " + cte.getIntValor3();
//            strUpdate += " WHERE CT_ID = " + cte.getIntCliente();
//            this.oConn.runQueryLMD(strUpdate);
//         }
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
         comisionM.setFieldDouble("CO_PUNTOS_G", cte.getDblGPuntos() + cte.getDblPuntos());
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
//         }
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

   }

   @Override
   public void doFaseBonos() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doFaseCierre() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private void doDetectaKit() {
      //Consulta para buscar los kits de inscripción de este periodo
//      String strSql = "select vta_tickets.CT_ID from vta_pedidos inner join vta_tickets\n"
//              + "on vta_tickets.TKT_ID = vta_pedidoS.TKT_ID\n"
//              + " where vta_pedidos.KL_ES_KIT1 = 1  "
//              + " AND vta_tickets.MPE_ID = " + this.intPeriodo;
        String strSql = "select vta_tickets.CT_ID, vta_ticketsdeta.TKTD_CVE from  vta_tickets, vta_ticketsdeta  \n"
              + " where  "
              + "  vta_tickets.TKT_ID = vta_ticketsdeta.TKT_ID  "
                + "  and vta_ticketsdeta.TKTD_CVE in ('PQ001' ,'PQ002','PQ003') "
              + " AND vta_tickets.MPE_ID = " + this.intPeriodo;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intCte = rs.getInt("CT_ID");
            //Validamos si ya esta en la red activo
            String strSqlCte = "select CT_UPLINE,CT_ACTIVO,CT_SPONZOR from vta_cliente where CT_ID = " + intCte;
            ResultSet rs2 = oConn.runQuery(strSqlCte, true);
            while (rs2.next()) {
               if (rs2.getInt("CT_UPLINE") == 4 && rs2.getInt("CT_ACTIVO") == 0) {
                  //Buscamos si su papa esta activo
                  int intActivoPadre = 0;
                  strSqlCte = "select CT_ACTIVO from vta_cliente where CT_ID = " + rs2.getInt("CT_SPONZOR");
                  ResultSet rs3 = oConn.runQuery(strSqlCte, true);
                  while (rs3.next()) {
                     intActivoPadre = rs3.getInt("CT_ACTIVO");
                  }
                  rs3.close();
                  if (intActivoPadre == 1) {
                     //Posicionamos en la red global de su papa 
                     ActivaBinario activador = new ActivaBinario(this.oConn);
                     activador.setIntUplineInicial(rs2.getInt("CT_SPONZOR"));
                     activador.setIntUplineTemporal(4);
                     activador.activarDistribuidor(intCte);
                  } else {
                     //Posicionamos en la red global a partir del nodo raiz
                     ActivaBinario activador = new ActivaBinario(this.oConn);
                     activador.setIntUplineInicial(1000);
                     activador.setIntUplineTemporal(4);
                     activador.activarDistribuidor(intCte);
                  }

               }
            }
//            if(rs2.getStatement() != null )rs2.getStatement().close(); 
               rs2.close();
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

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
    * Genera un nuevo centro Premier
    *
    * @param intIdCliente Es el id del cliente
    */
   public void GeneraCentroOro(int intIdCliente) {
      //Consultamos datos del cliente 
      Periodos periodo = new Periodos();
      Fechas fecha = new Fechas();
      String strKit_ingreso = "";
      int intSC_ID = 0;
      int intEMP_ID = 0;
      int intMON_ID = 0;
      int intTI_ID = 0;
      int intPr_Id = 0;
      String strKey = "";
      String strDescripcion = "";
      double dblPrecio = 0;
      double dblPuntos = 0;
      double dblNegocio = 0;
      String strRegimenFiscal = "";
      String strCodigo = "";
      int intExento1 = 0;
      int intExento2 = 0;
      int intExento3 = 0;
      int intUnidadMedida = 0;
      String strUnidadMedida = "";

      CIP_Tabla objTablaAct = new CIP_Tabla("", "", "", "", varSesiones);
      objTablaAct.Init("CLIENTES", true, true, false, oConn);
      objTablaAct.setBolGetAutonumeric(true);
      objTablaAct.ObtenDatos(intIdCliente, oConn);
      intSC_ID = objTablaAct.getFieldInt("SC_ID");
      intEMP_ID = objTablaAct.getFieldInt("EMP_ID");
      intMON_ID = objTablaAct.getFieldInt("EMP_ID");
      intTI_ID = objTablaAct.getFieldInt("TI_ID");
//      String strEmail1 = objTablaAct.getFieldString("CT_EMAIL1");
//      String strEmail2 = objTablaAct.getFieldString("CT_EMAIL2");

      //Obtenemos la sucursal del clientex
      double dblTasa1 = 0;
      double dblTasa2 = 0;
      double dblTasa3 = 0;
      int intIdTasa1 = 0;
      int intIdTasa2 = 0;
      int intIdTasa3 = 0;
      int intSImp1_2 = 0;
      int intSImp1_3 = 0;
      int intSImp2_3 = 0;
      int intSucDefOfertas = 0;
      int intLPrecios = 0;
      int intCT_DIASCREDITO = 0;
      double dblDescuento = 0;
      //Obtenemos el nombre de la sucursal default
      String strSql = "select vta_sucursal.SC_ID,SC_CLAVE,SC_NOMBRE,"
              + "SC_TASA1,SC_TASA2,SC_TASA3,SC_SOBRIMP1_2,SC_SOBRIMP1_3,SC_SOBRIMP2_3,SC_DIVISA,"
              + "vta_sucursal.TI_ID,vta_sucursal.TI_ID2,vta_sucursal.TI_ID3,SC_ACTIVA_OFERTA,CT_DESCUENTO,CT_LPRECIOS "
              + ",vta_cliente.MON_ID,CT_DIASCREDITO "
              + " from vta_sucursal,vta_cliente "
              + " where vta_sucursal.SC_ID = vta_cliente.SC_ID"
              + " AND vta_cliente.CT_ID = " + intIdCliente;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblTasa1 = rs.getDouble("SC_TASA1");
            dblTasa2 = rs.getDouble("SC_TASA2");
            dblTasa3 = rs.getDouble("SC_TASA3");
            intIdTasa1 = rs.getInt("TI_ID");
            intIdTasa2 = rs.getInt("TI_ID2");
            intIdTasa3 = rs.getInt("TI_ID3");
            intSImp1_2 = rs.getInt("SC_SOBRIMP1_2");
            intSImp1_3 = rs.getInt("SC_SOBRIMP1_3");
            intSImp2_3 = rs.getInt("SC_SOBRIMP2_3");
            intSucDefOfertas = rs.getInt("SC_ACTIVA_OFERTA");
            intLPrecios = rs.getInt("CT_LPRECIOS");
            dblDescuento = rs.getDouble("CT_DESCUENTO");
            intCT_DIASCREDITO = rs.getInt("CT_DIASCREDITO");
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }

      //Obtenemos el id del kit de ingreso
      try {
         intPr_Id = Integer.valueOf(strKit_ingreso);
      } catch (NumberFormatException ex) {
         System.out.println("Error al convertir intPr_Id...");
      }
      //consultamos el precio del articulo
      strSql = "select PR_ID,PP_PRECIO,PP_APDESC,PP_PTOSLEAL,PP_PTOSLEALCAM "
              + ",PP_PRECIO_USD,PP_PUNTOS,PP_NEGOCIO,PP_PPUBLICO,PP_APDESC,PP_APDESCPTO,PP_APDESCNEGO,PP_PUTILIDAD "
              + " from vta_prodprecios where PR_ID = " + intPr_Id + " AND LP_ID= 1";
      ResultSet rs2;
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            dblPrecio = rs2.getDouble("PP_PRECIO");
            dblPuntos = rs2.getDouble("PP_PUNTOS");
            dblNegocio = rs2.getDouble("PP_NEGOCIO");
         }
//         if(rs2.getStatement() != null )rs2.getStatement().close(); 
         rs2.close();
      } catch (SQLException ex) {
         System.out.println("ERROR:" + ex.getMessage());
      }
      //Consultamos datos del producto
      strSql = "select PR_CODIGO,PR_DESCRIPCION,PR_EXENTO1,PR_EXENTO2,PR_EXENTO3,PR_UNIDADMEDIDA "
              + " from vta_producto where PR_ID = " + intPr_Id + " ";
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            strCodigo = rs2.getString("PR_CODIGO");
            intExento1 = rs2.getInt("PR_EXENTO1");
            intExento2 = rs2.getInt("PR_EXENTO2");
            intExento3 = rs2.getInt("PR_EXENTO3");
            intUnidadMedida = rs2.getInt("PR_UNIDADMEDIDA");
            strDescripcion = rs2.getString("PR_DESCRIPCION");
         }
//         if(rs2.getStatement() != null )rs2.getStatement().close(); 
         rs2.close();
      } catch (SQLException ex) {
         System.out.println("ERROR:" + ex.getMessage());
      }
      //obtenemos la unidad de medida
      strSql = "select ME_DESCRIPCION from "
              + " vta_unidadmedida "
              + " where ME_ID = " + intUnidadMedida + " ";
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            strUnidadMedida = rs2.getString("ME_DESCRIPCION");
         }
//         if(rs2.getStatement() != null )rs2.getStatement().close(); 
         rs2.close();
      } catch (SQLException ex) {
         System.out.println("ERROR:" + ex.getMessage());
      }
      //obtenemos el regimen fiscal
      strSql = "select REGF_DESCRIPCION from "
              + " vta_empregfiscal,vta_regimenfiscal "
              + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID"
              + " AND EMP_ID = " + intEMP_ID + " ";
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            strRegimenFiscal = rs2.getString("REGF_DESCRIPCION");
         }
//         if(rs2.getStatement() != null )rs2.getStatement().close(); 
         rs2.close();
      } catch (SQLException ex) {
         System.out.println("ERROR:" + ex.getMessage());
      }

      //Llamamos objeto para guardar los datos de la tabla
      CIP_Tabla objTabla = new CIP_Tabla("", "", "", "", varSesiones);
      objTabla.Init("CLIENTES", true, true, false, oConn);
      objTabla.setBolGetAutonumeric(true);
      //atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML7
      //objTabla.ObtenParams(true, true, false, false, request, oConn);
      objTabla.setFieldInt("SC_ID", intSC_ID);
      objTabla.setFieldInt("EMP_ID", intEMP_ID);
      objTabla.setFieldInt("CT_UPLINE", 4);
      objTabla.setFieldInt("CT_ACTIVO", 0);
      objTabla.setFieldInt("CT_LPRECIOS", 1);
      objTabla.setFieldInt("MON_ID", intMON_ID);
      objTabla.setFieldInt("CT_SPONZOR", objTablaAct.getFieldInt("CT_SPONZOR"));
      objTabla.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
      objTabla.setFieldInt("KL_ID_MASTER", intIdCliente);
      objTabla.setFieldInt("KL_PLAN_ORO", 1);

      objTabla.setFieldString("CT_RAZONSOCIAL", objTablaAct.getFieldString("CT_RAZONSOCIAL"));
      objTabla.setFieldString("CT_NOMBRE", objTablaAct.getFieldString("CT_NOMBRE"));
      objTabla.setFieldString("CT_APATERNO", objTablaAct.getFieldString("CT_APATERNO"));
      objTabla.setFieldString("CT_AMATERNO", objTablaAct.getFieldString("CT_AMATERNO"));
      objTabla.setFieldString("CT_RFC", objTablaAct.getFieldString("CT_RFC"));
      objTabla.setFieldString("CT_CRED_ELECTOR", objTablaAct.getFieldString("CT_CRED_ELECTOR"));
      objTabla.setFieldString("CT_CALLE", objTablaAct.getFieldString("CT_CALLE"));
      objTabla.setFieldString("CT_NUMERO", objTablaAct.getFieldString("CT_NUMERO"));
      objTabla.setFieldString("CT_NUMINT", objTablaAct.getFieldString("CT_NUMINT"));
      objTabla.setFieldString("CT_COLONIA", objTablaAct.getFieldString("CT_COLONIA"));
      objTabla.setFieldString("CT_MUNICIPIO", objTablaAct.getFieldString("CT_MUNICIPIO"));
      objTabla.setFieldString("CT_LOCALIDAD", objTablaAct.getFieldString("CT_LOCALIDAD"));
      objTabla.setFieldString("CT_ESTADO", objTablaAct.getFieldString("CT_ESTADO"));
      objTabla.setFieldString("CT_CP", objTablaAct.getFieldString("CT_CP"));
      objTabla.setFieldString("CT_TELEFONO1", objTablaAct.getFieldString("CT_TELEFONO1"));
      objTabla.setFieldString("CT_TELEFONO2", objTablaAct.getFieldString("CT_TELEFONO2"));
      objTabla.setFieldString("CT_CONTACTO1", objTablaAct.getFieldString("CT_CONTACTO1"));
      objTabla.setFieldString("CT_EMAIL1", objTablaAct.getFieldString("CT_EMAIL1"));
      objTabla.setFieldString("CT_EMAIL2", objTablaAct.getFieldString("CT_EMAIL2"));
      objTabla.setFieldString("CT_CTABANCO1", objTablaAct.getFieldString("CT_CTABANCO1"));
      objTabla.setFieldString("CT_CTABANCO2", objTablaAct.getFieldString("CT_CTABANCO2"));
      objTabla.setFieldString("CT_CTA_BANCO1", objTablaAct.getFieldString("CT_CTA_BANCO1"));
      objTabla.setFieldString("CT_CTA_BANCO2", objTablaAct.getFieldString("CT_CTA_BANCO2"));
      objTabla.setFieldString("CT_CTA_SUCURSAL1", objTablaAct.getFieldString("CT_CTA_SUCURSAL1"));
      objTabla.setFieldString("CT_CTA_SUCURSAL2", objTablaAct.getFieldString("CT_CTA_SUCURSAL2"));
      objTabla.setFieldString("CT_CTA_CLABE1", objTablaAct.getFieldString("CT_CTA_CLABE1"));
      objTabla.setFieldString("CT_CTA_CLABE2", objTablaAct.getFieldString("CT_CTA_CLABE2"));
      objTabla.setFieldString("CT_FECHAREG", fecha.getFechaActual());
      objTabla.setFieldString("CT_FECHA_NAC", objTablaAct.getFieldString("CT_FECHA_NAC"));
      objTabla.setFieldString("CT_NOTAS", strDescripcion);
      if (intPr_Id == 141) {
         objTabla.setFieldInt("CT_CATEGORIA1", 1);
      } else {
         objTabla.setFieldInt("CT_CATEGORIA1", 2);
      }

      /**
       * Generamos un password aleatorio
       */
      objTabla.setFieldString("CT_PASSWORD", "");

      //Generamos una alta
      strKey = objTabla.getValorKey();

      int intNvoKey = 0;
      try {
         intNvoKey = Integer.valueOf(strKey);
      } catch (NumberFormatException ex) {
      }
      String strfolio_GLOBAL = "SI";
      /**
       * Generamos el pedido de ingreso
       */
      String strPedido = GeneraPedido(oConn, varSesiones,
              intSC_ID, intNvoKey, intMON_ID, intTI_ID, fecha,
              dblPrecio, dblTasa1, dblTasa2, dblTasa3,
              intSImp1_2, intSImp1_3, intSImp2_3,
              dblPuntos, dblNegocio, intCT_DIASCREDITO,
              strRegimenFiscal,
              intPr_Id, strDescripcion, strCodigo,
              intExento1, intExento2, intExento3, strUnidadMedida, strfolio_GLOBAL);
   }

   /**
    * Genera un pedido inicial para cuando se genera el plan oro
    *
    * @param oConn
    * @param varSesiones
    * @param intSC_ID
    * @param intCT_ID
    * @param intMON_ID
    * @param intTI_ID
    * @param fecha
    * @param dblPrecio
    * @param dblTasa1
    * @param dblTasa2
    * @param dblTasa3
    * @param intSImp1_2
    * @param intSImp1_3
    * @param intSImp2_3
    * @param dblPuntos
    * @param dblNegocio
    * @param intCT_DIASCREDITO
    * @param strRegimenFiscal
    * @param intPR_ID
    * @param strDescripcion
    * @param strCodigo
    * @param intExento1
    * @param intExento2
    * @param intExento3
    * @param strUnidadMedida
    * @param strfolio_GLOBAL
    * @return
    */
   public String GeneraPedido(Conexion oConn, VariableSession varSesiones,
           int intSC_ID, int intCT_ID, int intMON_ID, int intTI_ID, Fechas fecha,
           double dblPrecio, double dblTasa1, double dblTasa2, double dblTasa3,
           int intSImp1_2, int intSImp1_3, int intSImp2_3,
           double dblPuntos, double dblNegocio, int intCT_DIASCREDITO,
           String strRegimenFiscal,
           int intPR_ID, String strDescripcion, String strCodigo,
           int intExento1, int intExento2, int intExento3,
           String strUnidadMedida, String strfolio_GLOBAL) {
      //Instanciamos el objeto que generara la venta
      Ticket ticket = new Ticket(oConn, varSesiones, null);
      //Recibimos parametros
      String strPrefijoMaster = "PD";
      String strPrefijoDeta = "PDD";
      String strTipoVtaNom = Ticket.PEDIDO;
      ticket.setStrTipoVta(strTipoVtaNom);
      //Validamos si tenemos un empresa seleccionada
      if (varSesiones.getIntIdEmpresa() != 0) {
         //Asignamos la empresa seleccionada
         ticket.setIntEMP_ID(varSesiones.getIntIdEmpresa());
      }
      //Validamos si usaremos un folio global
      if (strfolio_GLOBAL.equals("NO")) {
         ticket.setBolFolioGlobal(false);
      }
      ticket.getDocument().setFieldInt("SC_ID", intSC_ID);
      ticket.getDocument().setFieldInt("CT_ID", intCT_ID);
      //Bandera para el tipo de kit
      if (intPR_ID == 141) {
         ticket.getDocument().setFieldInt("KL_ES_KIT1", 1);
      } else {
         ticket.getDocument().setFieldInt("KL_ES_KIT2", 1);
      }
      ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", intMON_ID);
      //Clave de vendedor
      int intVE_ID = 0;

      //Tarifas de IVA
      int intTI_ID2 = 0;
      int intTI_ID3 = 0;
      //Tipo de comprobante
      int intFAC_TIPOCOMP = 1;
      //Asignamos los valores al objeto
      ticket.getDocument().setFieldInt("VE_ID", intVE_ID);
      ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
      ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
      ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
      ticket.setIntFAC_TIPOCOMP(intFAC_TIPOCOMP);
      ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fecha.getFechaActual());
      ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", "Kit de ingreso");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", "");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", "");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", "");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", "NO IDENTIFICADO");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", "");
      ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", "En una sola Exhibicion");
      //ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", "En una sola Exhibicion");
      //Calculo de los importes
      double dblImporte = dblPrecio;
      double dblImpuesto1 = 0;
      double dblImpuesto2 = 0;
      double dblImpuesto3 = 0;
      double dblTotal = 0;
      Impuestos impuesto = new Impuestos(dblTasa1, dblTasa2, dblTasa3,
              intSImp1_2, intSImp1_3, intSImp2_3);
      impuesto.CalculaImpuestoMas(dblImporte, dblImporte, dblImporte);
      dblImpuesto1 = impuesto.getDblImpuesto1();
      dblTotal = dblImporte + dblImpuesto1 + dblImpuesto2 + dblImpuesto3;

      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", dblImporte);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", dblImpuesto1);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", dblImpuesto2);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", dblImpuesto3);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", dblTotal);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", dblTasa1);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", dblTasa2);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", dblTasa3);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_PUNTOS", dblPuntos);
      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_NEGOCIO", dblNegocio);

      ticket.getDocument().setFieldDouble(strPrefijoMaster + "_DIASCREDITO", intCT_DIASCREDITO);
      ticket.getDocument().setFieldString(strPrefijoMaster + "_REGIMENFISCAL", strRegimenFiscal);

      TableMaster deta = new vta_pedidosdeta();

      deta.setFieldInt("SC_ID", intSC_ID);
      deta.setFieldInt("PR_ID", intPR_ID);
      deta.setFieldInt(strPrefijoDeta + "_EXENTO1", intExento1);
      deta.setFieldInt(strPrefijoDeta + "_EXENTO2", intExento2);
      deta.setFieldInt(strPrefijoDeta + "_EXENTO3", intExento3);
      deta.setFieldInt(strPrefijoDeta + "_ESREGALO", 0);
      deta.setFieldString(strPrefijoDeta + "_CVE", strCodigo);
      deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", strDescripcion);
      deta.setFieldString(strPrefijoDeta + "_NOSERIE", "");
      deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", dblImporte);
      deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", 1);
      deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", dblTasa1);
      deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", dblTasa2);
      deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", dblTasa3);
      deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", dblImpuesto1);
      deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", dblImpuesto2);
      deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", dblImpuesto3);
      deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", dblImporte);
      deta.setFieldDouble(strPrefijoDeta + "_PRECIO", dblImporte);
      deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", 0);
      deta.setFieldDouble(strPrefijoDeta + "_PORDESC", 0);
      deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", dblImporte);
      deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", 0);
      deta.setFieldInt(strPrefijoDeta + "_ESREGALO", 0);
      deta.setFieldString(strPrefijoDeta + "_COMENTARIO", "");
      //UNIDAD DE MEDIDA UNIDAD_MEDIDA
      deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", strUnidadMedida);

      //MLM
      deta.setFieldDouble(strPrefijoDeta + "_PUNTOS", dblPuntos);
      deta.setFieldDouble(strPrefijoDeta + "_VNEGOCIO", dblNegocio);
      deta.setFieldDouble(strPrefijoDeta + "_IMP_PUNTOS", dblPuntos);
      deta.setFieldDouble(strPrefijoDeta + "_IMP_VNEGOCIO", dblNegocio);
      deta.setFieldDouble(strPrefijoDeta + "_DESC_ORI", 0);
      deta.setFieldInt(strPrefijoDeta + "_DESC_PREC", 0);
      deta.setFieldInt(strPrefijoDeta + "_DESC_PUNTOS", 0);
      deta.setFieldInt(strPrefijoDeta + "_DESC_VNEGOCIO", 0);
      deta.setFieldInt(strPrefijoDeta + "_REGALO", 0);
      deta.setFieldInt(strPrefijoDeta + "_ID_PROMO", 0);
      ticket.AddDetalle(deta);

      //Guardamos el pedido
      ticket.Init();
      //Generamos transaccion
      ticket.doTrx();

      String strRes = "";
      if (ticket.getStrResultLast().equals("OK")) {
         strRes = "OK." + ticket.getDocument().getValorKey();
      } else {
         strRes = ticket.getStrResultLast();
      }
      return strRes;
   }
   // </editor-fold>

}
