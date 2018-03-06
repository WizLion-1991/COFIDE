package com.mx.siweb.mlm.compensacion.goonlife;

import Tablas.MlmComisionSemanal;
import Tablas.MlmComisionSemanalDeta;
import Tablas.MlmMovComis;
import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.Periodos;
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

/**
 * Esta clase genera el calculo de comision de la empresa Go On Life
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
         strSQL += "CT_CONTEO_HIJOS    =  0";
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
//               if (rs2.getStatement() != null) {
//                  rs2.getStatement().close();
//               }
               rs2.close();
               lstTasas.Agrega(tCambio);
            }
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
         //Iniciamos transaccion
         this.oConn.runQueryLMD("BEGIN");
         double dblPuntos = 0;
         double dblNegocio = 0;
         //---------------------------- CARGA DE DOCUMENTOS DE FACTURA
         strSql = "SELECT vta_facturas.CT_ID,vta_facturas.FAC_MONEDA,"
            + " sum(vta_facturas.FAC_IMPORTE_PUNTOS) AS PPUNTOS, "
            + " sum(vta_facturas.FAC_IMPORTE_NEGOCIO) AS PNEGOCIO "
            + " FROM vta_facturas WHERE vta_facturas.MPE_ID =  " + this.intPeriodo
            + " and vta_facturas.FAC_ANULADA = 0 "
            + " and vta_facturas.FAC_NO_MLM = 0 "
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
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();

         //CARGA DE DOCUMENTOS DE TICKETS
         strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
            + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
            + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
            + " FROM vta_tickets WHERE vta_tickets.MPE_ID =  " + this.intPeriodo
            + " and vta_tickets.TKT_ANULADA = 0 "
            + " and vta_tickets.TKT_NO_MLM = 0 AND GO_ES_KIT = 0"
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
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
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
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
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
            if (cteMlm.getDblPuntos() > 0 || cteMlm.getDblNegocio() > 0) {
               String strUpdate = "update vta_cliente set "
                  + " CT_PPUNTOS = CT_PPUNTOS + " + cteMlm.getDblPuntos() + ", "
                  + " CT_PNEGOCIO = CT_PNEGOCIO + " + cteMlm.getDblNegocio() + " "
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
      ArrayList<MlmClienteGoOnLife> lstMlmCliente = new ArrayList<MlmClienteGoOnLife>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para conteo de hijos">
      log.info("Inicia Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      this.oConn.runQueryLMD("BEGIN");

      //Calculamos el total de hijos por cada nodo
      String strHijos = "select CT_UPLINE, count(CT_ID) AS cuantos "
         + " from vta_cliente  "
         + " GROUP BY CT_UPLINE";
      try {
         ResultSet rs = this.oConn.runQuery(strHijos, true);
         while (rs.next()) {
            String strUpdate = "UPDATE vta_cliente SET CT_CONTEO_HIJOS =  " + rs.getInt("cuantos")
               + " where CT_ID = " + rs.getInt("CT_UPLINE");
            this.oConn.runQueryLMD(strUpdate);
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      strHijos = "select CT_UPLINE, count(CT_ID) AS cuantos "
         + " from vta_cliente where CT_ACTIVO = 1 "
         + " GROUP BY CT_UPLINE";
      try {
         ResultSet rs = this.oConn.runQuery(strHijos, true);
         while (rs.next()) {
            String strUpdate = "UPDATE vta_cliente SET CT_CONTEO_HIJOS_ACTIVOS =  " + rs.getInt("cuantos")
               + " where CT_ID = " + rs.getInt("CT_UPLINE");
            this.oConn.runQueryLMD(strUpdate);
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      //Terminamos transaccion
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Activamos nuevos ingresos">
      doDetectaKit();
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Cargamos en un arreglo la info de los clientes">
      log.info("Inicia Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intConta = -1;
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO,"
         + "GO_NIVEL_MAX,CT_CONTEO_HIJOS_ACTIVOS,SC_ID,GO_PER_ACTIVO,CT_ACTIVO "
         + " from vta_cliente order by CT_ARMADONUM";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intConta++;
            MlmClienteGoOnLife cte = new MlmClienteGoOnLife();
            cte.setIntCliente(rs.getInt("CT_ID"));
            cte.setIntUpline(rs.getInt("CT_UPLINE"));
            cte.setDblPuntos(rs.getDouble("CT_PPUNTOS"));
            cte.setDblNegocio(rs.getDouble("CT_PNEGOCIO"));
            cte.setIntNivelMax(rs.getInt("GO_NIVEL_MAX"));
            cte.setDblGananciaDuoLevel1(0);
            cte.setDblGananciaDuoLevel2(0);
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS_ACTIVOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntPeriodoActivo(rs.getInt("GO_PER_ACTIVO"));
            if (cte.getDblPuntos() >= lstParams.get(1).getFieldDouble("CP_PPUNTOS")) {
               cte.setIntActivo(1);
            }
            lstMlmCliente.add(cte);
            //Para indexarlo
            ClienteIndice tupla = new ClienteIndice();
            tupla.setIntCte(rs.getInt("CT_ID"));
            tupla.setIntIndice(intConta);
            lstMlmClienteIdx.add(tupla);
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
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

      // <editor-fold defaultstate="collapsed" desc="Calculo DUO-LEVEL">
      log.debug("Calculo de (Duo Level)....");
      int intContaProcess = 0;
      int intContaProcessTot = 0;
      int intContaProcessDif = 0;
      int intContaProcessTotDif = 0;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteGoOnLife cte = lstMlmCliente.get(i);
         double dblImporte = cte.getDblNegocio();
         // <editor-fold defaultstate="collapsed" desc="Si el importe de negocio es mayor a cero y estamos en un periodo posterior al ingreso">
         if (dblImporte > 0) {
            intContaProcess++;
            intContaProcessTot++;
            // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones DUO-LEVEL">
            calculaDuoLevel(cte, cte,
               lstMlmClienteIdx,
               lstMlmCliente,
               lstParams,
               0);
            // </editor-fold>
         }
         // </editor-fold>
      }
      log.debug("Termina calculo(Duo Level)....");
      log.debug("intContaProcess(Duo Level):" + intContaProcess);
      log.debug("intContaProcessTot(Duo Level):" + intContaProcessTot);
      log.debug(" ");
      log.debug(" ");
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Ganancias residuales">
      log.debug("Calculo de residuales....");
      intContaProcess = 0;
      intContaProcessTot = 0;
      intContaProcessDif = 0;
      intContaProcessTotDif = 0;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteGoOnLife cte = lstMlmCliente.get(i);
         double dblImporte = cte.getDblGananciaDuoLevel1() + cte.getDblGananciaDuoLevel2();
         // <editor-fold defaultstate="collapsed" desc="Si el importe de negocio es mayor a cero y estamos en un periodo posterior al ingreso">
         if (dblImporte > 0) {
            log.debug("Residuales..." + cte.getIntCliente() + " " + dblImporte);
            intContaProcess++;
            intContaProcessTot++;
            // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones Residuales">
            calculaResidual(cte, cte,
               lstMlmClienteIdx,
               lstMlmCliente,
               lstParams,
               0);
            // </editor-fold>
         }
         // </editor-fold>
      }
      log.debug("Termina calculo(Residuales)....");
      log.debug("intContaProcess(Residuales):" + intContaProcess);
      log.debug("intContaProcessTot(Residuales):" + intContaProcessTot);
      log.debug(" ");
      log.debug(" ");
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Recorremos la red desde abajo para el calculo de nivel">
      log.info("Inicia Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteGoOnLife cte = lstMlmCliente.get(i);
         //Actualizamos puntos grupales
         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblNegocio());
         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblPuntos());
         //Calculamos el nivel de la persona
         int intNivel = 1;
         if (cte.getDblPuntos() >= lstParams.get(1).getFieldDouble("CP_PPUNTOS")) {

            log.debug("Calculo de nivel..." + cte.getIntCliente());
            //log.debug(cte.getDblGNegocio() + " " + cte.getDblNegocio());
            log.debug("Puntos personales: " + cte.getDblPuntos());
            log.debug("DuLevel 1: " + cte.getDblGananciaDuoLevel1());
            log.debug("DuLevel 2: " + cte.getDblGananciaDuoLevel2());
            intNivel = 2;
            //Recorremos la lista de parametros del plan para definir el nivel
            Iterator<TableMaster> it = lstParams.iterator();
            while (it.hasNext()) {
               TableMaster tbn = it.next();
               double dblGananciasDuoLevel = cte.getDblGananciaDuoLevel1() + cte.getDblGananciaDuoLevel2();
               if (dblGananciasDuoLevel >= tbn.getFieldDouble("CP_VENTAS_1")
                  && dblGananciasDuoLevel <= tbn.getFieldDouble("CP_VENTAS_2")) {
                  intNivel = tbn.getFieldInt("CP_NIVEL");
               }
            }
            //El nivel logrado no se pierde
            if (intNivel < cte.getIntNivelMax()) {
               cte.setIntNivelRed(cte.getIntNivelMax());
            } else {
               cte.setIntNivelRed(intNivel);
            }
            log.debug("Nivel calculado.." + intNivel);
            log.debug("Nivel maximo.." + cte.getIntNivelMax());
            log.debug("Nivel que se quedo.." + cte.getIntNivelRed());
         }

         //Guardamos info del nodo
         //Subimos info al padre
         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
         //Si se encontro el padre
         if (intIdx != -1) {
            MlmClienteGoOnLife ctePadre = lstMlmCliente.get(intIdx);
//            log.debug(cte.getIntUpline() + " intIdx" + intIdx);
            ctePadre.setDblGNegocio(ctePadre.getDblGNegocio() + cte.getDblGNegocio());
            ctePadre.setDblGPuntos(ctePadre.getDblGPuntos() + cte.getDblGPuntos());
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
      log.debug(" ");
      log.debug(" ");
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD("BEGIN");

      //Guardamos informacion de los nodos
      log.info("Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteGoOnLife cte = lstMlmCliente.get(i);
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
            || cte.getDblGNegocio() != 0) {
            //G_NEGOCIO
            //G_PUNTOS
            //NIVELRED
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "CT_NIVELRED = " + cte.getIntNivelRed() + "";
            strUpdate += ",CT_GPUNTOS = " + (cte.getDblGPuntos()) + "";
            strUpdate += ",CT_GNEGOCIO = " + cte.getDblGNegocio() + " ";
            strUpdate += ",CT_ACTIVO= " + cte.getIntActivo() + " ";
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
         //Limpiamos los puntos no usados y generamos los nuevos puntos....
         doGeneraPuntosyLimpia();
         //Actualizamos el maximo nivel alcanzado
         String strUpdate = "UPDATE vta_cliente SET GO_NIVEL_MAX = CT_NIVELRED where CT_NIVELRED >GO_NIVEL_MAX ";
         this.oConn.runQueryLMD(strUpdate);
         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de Diferenciales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      intContaProcess = 0;
      intContaProcessTot = 0;
      intContaProcessDif = 0;
      intContaProcessTotDif = 0;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteGoOnLife cte = lstMlmCliente.get(i);
         double dblImporte = cte.getDblNegocio();
         // <editor-fold defaultstate="collapsed" desc="Si el importe de negocio es mayor a cero">
         if (dblImporte > 0) {
            intContaProcess++;
            intContaProcessTot++;

            // <editor-fold defaultstate="collapsed" desc="Calculo de diferenciales">
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
                           log.info("Aplica diferencia " + cte.getIntCliente() + " -> " + ctePadre.getIntCliente());
                           log.info("intMaxPaga " + intMaxPaga);
                           if (intMaxPaga > 0) {
                              double dblPorcentaje = tbn.getFieldDouble("CP_DIFERENCIAL1") - lstParams.get(intMaxPaga - 1).getFieldDouble("CP_DIFERENCIAL1");
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
            // </editor-fold>

         }
         // </editor-fold>
      }
      log.info("Termina Calculo de COMISIONES.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteGoOnLife cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
         if (cte.getDblPuntos() != 0) {
            //Realizamos la conversion a la moneda original

            //Actualizamos el importe de comision en clientes
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += " CT_COMISION = " + cte.getDblComision();
            strUpdate += " ,CT_ACTIVO = 1";
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

   }

   @Override
   public void doFaseBonos() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doFaseCierre() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * Calcula las comisiones semanales por afiliacion
    *
    * @param intIdPeriodoSemanal Es el numero de semana en que se afiliaran
    */
   public void doComisionSemanal(int intIdPeriodoSemanal) {
      this.strResultLast = "OK";
      //Borramos comisiones del periodo
      String strDel1 = "delete from mlm_comision_semanal where MSE_ID = " + intIdPeriodoSemanal;
      this.oConn.runQueryLMD(strDel1);

      String strDel2 = "delete from mlm_comision_semanal_deta where MSE_ID = " + intIdPeriodoSemanal;
      this.oConn.runQueryLMD(strDel2);

      //Calculamos la semana inicial de cada cliente
      String strUpdateSemanas = "update mlm_periodos_semanal ,\n"
         + "vta_cliente\n"
         + " set GO_SEM_INI=MSE_SEMANA ,GO_ANIO_INI = MSE_ANIO\n"
         + "where \n"
         + "mlm_periodos_semanal.MSE_ID  = vta_cliente.MSE_ID and GO_SEM_INI = 0";
      oConn.runQueryLMD(strUpdateSemanas);

      //Buscar clientes inscritos en la semana
      try {

         String strSql = "select CT_ID,CT_UPLINE,CT_CATEGORIA1 from vta_cliente where MSE_ID = " + intIdPeriodoSemanal;
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intIdCliente = rs.getInt("CT_ID");
            int intIdSponzor = rs.getInt("CT_UPLINE");
            int intIdCategoria1Cte = rs.getInt("CT_CATEGORIA1");
            if (intIdSponzor != 0) {
               pagaComisionSemanalSponsor(intIdSponzor, intIdPeriodoSemanal, 0, intIdCliente, intIdCategoria1Cte);
            }
         }
         //rs.getStatement().close();
         rs.close();
         //Sumamos totales para el maestro
         strSql = "SELECT\n"
            + "MSE_ID,\n"
            + "CT_ID,\n"
            + "sum(COMI_IMPORTE) AS TOT\n"
            + "FROM\n"
            + "mlm_comision_semanal_deta \n"
            + " where MSE_ID = " + intIdPeriodoSemanal
            + " GROUP BY\n"
            + " MSE_ID,\n"
            + " CT_ID;";
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intIdCliente = rs.getInt("CT_ID");
            double dblComisTot = rs.getDouble("TOT");
            MlmComisionSemanal comisionM = new MlmComisionSemanal();
            comisionM.setFieldInt("CT_ID", intIdCliente);
            comisionM.setFieldInt("MSE_ID", intIdPeriodoSemanal);
            comisionM.setFieldDouble("CO_IMPORTE", dblComisTot);
            comisionM.setFieldDouble("CO_IMPUESTO1", 0);
            comisionM.setFieldDouble("CO_IMPUESTO2", 0);
            comisionM.setFieldDouble("CO_IMPUESTO3", 0);
            comisionM.setFieldDouble("CO_RET1", 0);
            comisionM.setFieldDouble("CO_RET2", 0);
            comisionM.setFieldDouble("CO_RET3", 0);
            comisionM.setFieldDouble("CO_CHEQUE", dblComisTot);
            comisionM.setFieldDouble("CO_PUNTOS_P", 0);
            comisionM.setFieldDouble("CO_PUNTOS_G", 0);
            comisionM.setFieldDouble("CO_NEGOCIO_P", 0);
            comisionM.setFieldDouble("CO_NEGOCIO_G", 0);
            comisionM.setFieldInt("CO_NIVEL", 0);
            comisionM.setFieldInt("CT_NIVELRED", 0);
            //Calculo de impuestos
//            CalculoImpuestos(cte, comisionM);
            comisionM.Agrega(oConn);
         }
         //rs.getStatement().close();
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la comision DuoLevel
    *
    * @param cte Es el cliente que esta generando la comision
    * @param cteRed Es el cliente nodo actual que se esta recorriendo
    * @param lstMlmClienteIdx Indices
    * @param lstMlmCliente Lista de clientes
    * @param lstParams Lista de parametros de comision
    * @param intStep Es el paso, con este controlamos la compresión dinámica
    */
   protected void calculaDuoLevel(MlmClienteGoOnLife cte, MlmClienteGoOnLife cteRed,
      ArrayList<ClienteIndice> lstMlmClienteIdx,
      ArrayList<MlmClienteGoOnLife> lstMlmCliente,
      ArrayList<TableMaster> lstParams,
      int intStep) {
      log.debug("Step " + intStep);
      log.debug("Cte: " + cte.getIntCliente());
      log.debug("Cte Red: " + cteRed.getIntCliente() + " " + cteRed.getIntUpline());
      int intIdx = this.BuscaNodoenArreglo(cteRed.getIntUpline(), lstMlmClienteIdx, false);
      //Si se encontro el padre
      if (intIdx != -1) {
         MlmClienteGoOnLife ctePadre = lstMlmCliente.get(intIdx);
         log.debug("ctePadre: " + ctePadre.getIntCliente());
         // <editor-fold defaultstate="collapsed" desc="Si esta activo da comisiones">
         if (ctePadre.getIntActivo() == 1) {
            log.debug("Activo:Si ");
            intStep++;
            double dblImporte = cte.getDblNegocio();
            double dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL1");
            if (intStep == 2) {
               dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL2");
            }
            double dblComision = dblImporte * (dblPorc / 100);
            ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
            if (intStep == 1) {
               ctePadre.setDblGananciaDuoLevel1(ctePadre.getDblGananciaDuoLevel1() + dblComision);
            }
            if (intStep == 2) {
               ctePadre.setDblGananciaDuoLevel2(ctePadre.getDblGananciaDuoLevel2() + dblComision);
            }
            //Entidad para el detalle de las comisiones
            mlm_comision_deta comisDeta = new mlm_comision_deta();
            comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
            comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
            comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
            comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
            comisDeta.setFieldDouble("COMI_PORCENTAJE", dblPorc);
            comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
            comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
            comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
            comisDeta.setFieldInt("COMI_ARMADOINI", 1);
            comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
            comisDeta.setFieldInt("COMI_ARMADONUM", 1);
            comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
            comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
            comisDeta.setFieldString("COMI_CODIGO", "DUO" + intStep);
            comisDeta.Agrega(oConn);
         }
         // </editor-fold>
         //Solo aplica a los dos primeros nodos activos
         if (intStep < 2) {
            calculaDuoLevel(cte, ctePadre, lstMlmClienteIdx, lstMlmCliente, lstParams, intStep);
         } else {
            calculaBonoRecompra(cte, ctePadre, lstMlmClienteIdx, lstMlmCliente, lstParams, intStep);
         }
      }
   }

   /**
    * Calcula la comision Recompra
    *
    * @param cte Es el cliente que esta generando la comision
    * @param cteRed Es el cliente nodo actual que se esta recorriendo
    * @param lstMlmClienteIdx Indices
    * @param lstMlmCliente Lista de clientes
    * @param lstParams Lista de parametros de comision
    * @param intStep Es el paso, con este controlamos la compresión dinámica
    */
   protected void calculaBonoRecompra(MlmClienteGoOnLife cte, MlmClienteGoOnLife cteRed,
      ArrayList<ClienteIndice> lstMlmClienteIdx,
      ArrayList<MlmClienteGoOnLife> lstMlmCliente,
      ArrayList<TableMaster> lstParams,
      int intStep) {
      log.debug("RECOMPRA Step " + intStep);
      log.debug("Cte: " + cte.getIntCliente());
      log.debug("Cte Red: " + cteRed.getIntCliente() + " " + cteRed.getIntUpline());
      int intIdx = this.BuscaNodoenArreglo(cteRed.getIntUpline(), lstMlmClienteIdx, false);
      //Si se encontro el padre
      if (intIdx != -1) {
         MlmClienteGoOnLife ctePadre = lstMlmCliente.get(intIdx);
         log.debug("ctePadre: " + ctePadre.getIntCliente());
         // <editor-fold defaultstate="collapsed" desc="Si esta activo da comisiones">
         if (ctePadre.getIntActivo() == 1) {
            log.debug("Activo:Si ");
            intStep++;
            double dblImporte = cte.getDblNegocio();
            double dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL1");
            double dblComision = dblImporte * (dblPorc / 100);
            //Entidad para el detalle de las comisiones
            mlm_comision_deta comisDeta = new mlm_comision_deta();
            comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
            comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
            comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
            comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
            comisDeta.setFieldDouble("COMI_PORCENTAJE", dblPorc);
            comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
            comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
            comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
            comisDeta.setFieldInt("COMI_ARMADOINI", 1);
            comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
            comisDeta.setFieldInt("COMI_ARMADONUM", 1);
            comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
            comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
            comisDeta.setFieldString("COMI_CODIGO", "REC" + intStep);
            comisDeta.Agrega(oConn);
         }
         // </editor-fold>
         //Solo aplica a los dos primeros nodos activos
         if (intStep < 3) {
            calculaBonoRecompra(cte, ctePadre, lstMlmClienteIdx, lstMlmCliente, lstParams, intStep);
         }
      }
   }

   /**
    * Calcula la comision Residual
    *
    * @param cte Es el cliente que esta generando la comision
    * @param cteRed Es el cliente nodo actual que se esta recorriendo
    * @param lstMlmClienteIdx Indices
    * @param lstMlmCliente Lista de clientes
    * @param lstParams Lista de parametros de comision
    * @param intStep Es el paso, con este controlamos la compresión dinámica
    */
   protected void calculaResidual(MlmClienteGoOnLife cte, MlmClienteGoOnLife cteRed,
      ArrayList<ClienteIndice> lstMlmClienteIdx,
      ArrayList<MlmClienteGoOnLife> lstMlmCliente,
      ArrayList<TableMaster> lstParams,
      int intStep) {
      log.debug("Step " + intStep);
      log.debug("Cte: " + cte.getIntCliente());
      log.debug("Cte Red: " + cteRed.getIntCliente() + " " + cteRed.getIntUpline());
      int intIdx = this.BuscaNodoenArreglo(cteRed.getIntUpline(), lstMlmClienteIdx, false);
      //Si se encontro el padre
      if (intIdx != -1) {
         MlmClienteGoOnLife ctePadre = lstMlmCliente.get(intIdx);
         // <editor-fold defaultstate="collapsed" desc="Si esta activo da comisiones">
         if (ctePadre.getIntActivo() == 1) {
            log.debug("Activo:Si ");
            intStep++;
            //Solo aplica a partir del nivel 3
            if (intStep >= 3) {
               log.debug("intStep 3:Si " + intStep);
               double dblImporte = cte.getDblGananciaDuoLevel1() + cte.getDblGananciaDuoLevel2();
               double dblPorc = dblPorcResidual(intStep, lstParams);
               double dblComision = dblImporte * (dblPorc / 100);
               ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
               //Entidad para el detalle de las comisiones
               mlm_comision_deta comisDeta = new mlm_comision_deta();
               comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
               comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
               comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
               comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
               comisDeta.setFieldDouble("COMI_PORCENTAJE", dblPorc);
               comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
               comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
               comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
               comisDeta.setFieldInt("COMI_ARMADOINI", 1);
               comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
               comisDeta.setFieldInt("COMI_ARMADONUM", 1);
               comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
               comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
               comisDeta.setFieldString("COMI_CODIGO", "UNI" + intStep);
               comisDeta.Agrega(oConn);
            }

         }
         // </editor-fold>
         //Solo aplica a los dos primeros nodos activos
         if (intStep < 6) {
            calculaResidual(cte, ctePadre, lstMlmClienteIdx, lstMlmCliente, lstParams, intStep);
         }
      }
   }

   private double dblPorcResidual(int intStep, ArrayList<TableMaster> lstParams) {
      double dblPorc = 0;
      if (intStep == 3) {
         dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL3");
      }
      if (intStep == 4) {
         dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL4");
      }
      if (intStep == 5) {
         dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL5");
      }
      if (intStep == 6) {
         dblPorc = lstParams.get(1).getFieldDouble("CP_UNILEVEL6");
      }
      return dblPorc;
   }

   private void pagaComisionSemanalSponsor(int intIdSponzor, int intIdPeriodoSemanal, int intStep, int intIdCliente, int intIdCategoria1Cte) {
      double dblPuntos = 0;
      double dblPorcComis = 0;
      double dblFactorPtos = 20;

      //Depende del paquete de inscripcion del cliente son los puntos que tiene
      if (intIdCategoria1Cte == 1) {
         dblPuntos = 150;
      } else {
         dblPuntos = 75;
      }

      try {
         //Validamos si esta en el periodo de semanas que le corresponde
         String strSql = "select CT_ID,CT_UPLINE"
            + ",getNumSemanas(" + intIdPeriodoSemanal + ",MSE_ID) as NumSemanas,CT_CATEGORIA1,CT_ACTIVO from vta_cliente where CT_ID = " + intIdSponzor;
         ResultSet rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            int intNumSemanas = rs2.getInt("NumSemanas");
            //Validamos si esta activo
            if (rs2.getInt("CT_ACTIVO") == 1) {
               intStep++;
               //Depende de si es hijo o nieto el porcentaje
               if (intStep == 1) {
                  dblPorcComis = 50;
               } else {
                  dblPorcComis = 6.667;
               }
               log.debug("Numero semanas:" + intNumSemanas);
               boolean bolCobraComis = false;
               if (rs2.getInt("CT_CATEGORIA1") == 1) {
                  //Tiene 4 semanas
                  if (intNumSemanas > 0 && intNumSemanas <= 4) {
                     bolCobraComis = true;
                  }
               } else //Tiene 2 semanas
               {
                  if (intNumSemanas > 0 && intNumSemanas <= 2) {
                     bolCobraComis = true;
                  }
               }
               if (bolCobraComis) {
                  //Cobra comisiones semanales....
                  double dblImporteComis = (dblPorcComis / 100) * (dblPuntos * dblFactorPtos);
                  log.debug(intStep + " Comisiones cte " + intIdSponzor + " " + dblImporteComis);
                  MlmComisionSemanalDeta comisDeta = new MlmComisionSemanalDeta();
                  comisDeta.setFieldInt("CT_ID", intIdSponzor);
                  comisDeta.setFieldInt("MSE_ID", intIdPeriodoSemanal);
                  comisDeta.setFieldInt("COMI_FUENTE", intIdCliente);
                  comisDeta.setFieldInt("COMI_DESTINO", intIdSponzor);
                  comisDeta.setFieldDouble("COMI_PORCENTAJE", dblPorcComis);
                  comisDeta.setFieldDouble("COMI_IMPORTE", dblImporteComis);
                  comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", (dblPuntos * dblFactorPtos));
                  comisDeta.setFieldInt("COMI_NIVEL", intStep);
                  comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                  comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                  comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                  comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                  comisDeta.setFieldInt("SC_ID", 1);
                  comisDeta.setFieldString("COMI_CODIGO", "ING" + intStep);
                  comisDeta.Agrega(oConn);
               } else {
                  log.debug("No cobra comisiones ...activo:" + rs2.getInt("CT_CATEGORIA1") + " intNumSemanas:" + intNumSemanas);
               }
            } else {
               log.debug("No esta activo....");
            }
            //Buscamos los datos del abuelo

            if (intStep < 2) {
               pagaComisionSemanalSponsor(rs2.getInt("CT_UPLINE"), intIdPeriodoSemanal, intStep, intIdCliente, intIdCategoria1Cte);
            }
         }
//         rs2.getStatement().close();
         rs2.close();
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
      ArrayList<TableMaster> lstParametros;
      ParametrosComis paramsTmp = new ParametrosComis();
      //Obtenemos los datos de la base de datos
      lstParametros = paramsTmp.ObtenDatosVarios(" 1=1 ORDER BY CP_ORDEN", oConn);
      return lstParametros;
   }

   private void doDetectaKit() {

      Periodos periodo = new Periodos();
      int intPeriodoSemanal = periodo.getPeriodoSemanalActual(oConn);
      //Consulta para buscar los kits de inscripción de este periodo
      String strSql = "select vta_tickets.CT_ID, vta_ticketsdeta.TKTD_CVE from  vta_tickets, vta_ticketsdeta  \n"
         + " where  "
         + "  vta_tickets.TKT_ID = vta_ticketsdeta.TKT_ID  "
         + "  and vta_ticketsdeta.TKTD_CVE in ('KIT1' ,'KIT2') "
         + " AND vta_tickets.MPE_ID = " + this.intPeriodo;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intCte = rs.getInt("CT_ID");
            //Validamos si ya esta en la red activo
            String strSqlCte = "select CT_UPLINE,CT_ACTIVO,CT_SPONZOR,CT_ID from vta_cliente where CT_ID = " + intCte;
            ResultSet rs2 = oConn.runQuery(strSqlCte, true);
            while (rs2.next()) {
               if (rs2.getInt("CT_ACTIVO") == 0) {
                  //Actualizamos el upline
                  String strUpdate = "update vta_cliente set"
                     + " CT_ACTIVO = 1 "
                     + " ,GO_PER_ACTIVO = " + intPeriodo
                     + " ,MSE_ID = " + intPeriodoSemanal
                     + "WHERE CT_ID = " + intCte;
                  log.debug(strUpdate);
                  oConn.runQueryLMD(strUpdate);
               }
            }
//            if (rs2.getStatement() != null) {
//               rs2.getStatement().close();
//            }
            rs2.close();
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }
   // </editor-fold>

   private void doGeneraPuntosyLimpia() {
      Fechas fecha = new Fechas();
      //Consultamos todos los puntos disponibles para limpiarlos
      String strSqlLimpia = "select CT_ID,SUM(MMC_ABONO- MMC_CARGO) AS restante from mlm_mov_comis\n"
         + "group by CT_ID;";
      try {
         ResultSet rs2 = oConn.runQuery(strSqlLimpia, true);
         while (rs2.next()) {
            int intCte = rs2.getInt("CT_ID");
            double dblPuntosRestantes = rs2.getDouble("restante");
            MlmMovComis movs = new MlmMovComis();
            movs.setFieldString("MMC_FECHA", fecha.getFechaActual());
            movs.setFieldInt("CT_ID", intCte);
            movs.setFieldInt("MPE_ID", this.intPeriodo);
            movs.setFieldInt("MMC_USUARIO", 1);
            movs.setFieldDouble("MMC_CARGO", dblPuntosRestantes);
            movs.setFieldString("MMC_NOTAS", "Cancela Bono recompra");
            movs.Agrega(oConn);
         }
//         if (rs2.getStatement() != null) {
//            rs2.getStatement().close();
//         }
         rs2.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Generamos los nuevos puntos a partir de las comisiones
      String strSql = "select CT_ID,COMI_IMPORTE FROM\n"
         + " mlm_comision_deta where MPE_ID = " + this.intPeriodo + " and COMI_CODIGO = 'REC3'";
      try {
         ResultSet rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            int intCte = rs2.getInt("CT_ID");
            double dblPuntos = rs2.getDouble("COMI_IMPORTE");
            MlmMovComis movs = new MlmMovComis();
            movs.setFieldString("MMC_FECHA", fecha.getFechaActual());
            movs.setFieldInt("CT_ID", intCte);
            movs.setFieldInt("MPE_ID", this.intPeriodo);
            movs.setFieldInt("MMC_USUARIO", 1);
            movs.setFieldDouble("MMC_ABONO", dblPuntos);
            movs.setFieldString("MMC_NOTAS", "Bono recompra");
            movs.Agrega(oConn);
         }
//         if (rs2.getStatement() != null) {
//            rs2.getStatement().close();
//         }
         rs2.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

   }
}
