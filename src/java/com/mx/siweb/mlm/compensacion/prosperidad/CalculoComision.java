/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.prosperidad;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision_deta;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Esta clase genera el calculo de comision de la empresa Firm o Jonfilu
 *
 * @author aleph_79
 */
public class CalculoComision extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculoComision.class.getName());
   private int intIdPeriodoSemanal;

   public int getIntIdPeriodoSemanal() {
      return intIdPeriodoSemanal;
   }

   public void setIntIdPeriodoSemanal(int intIdPeriodoSemanal) {
      this.intIdPeriodoSemanal = intIdPeriodoSemanal;
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
         strSQL += "CT_CONTEO_HIJOS    =  0 ";
         this.oConn.runQueryLMD(strSQL);
      }
   }

   @Override
   public void doFase3() {
      super.doFase3();
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 3.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Lista para los clientes
      ArrayList<MlmClienteProsperidad> lstMlmCliente = new ArrayList<MlmClienteProsperidad>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para conteo de hijos">
//      doDetectaKit();
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para conteo de hijos">
      log.info("Inicia Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      this.oConn.runQueryLMD("BEGIN");

      //Calculamos el total de hijos por cada nodo
      String strHijos = "select CT_UPLINE, count(CT_ID) AS cuantos "
         + " from vta_cliente where CT_ACTIVO = 1 "
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
      //Terminamos transaccion
      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Calculo de cuantos hijos.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Cargamos en un arreglo la info de los clientes">
      log.info("Inicia Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intConta = -1;
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO,CT_CONTEO_HIJOS"
         + " ,PROSP_PER_ACTIVO,CT_ACTIVO,getNumSemanas(" + intIdPeriodoSemanal + ",PROS_SEMANA) as NumSemanas"
         + ",SC_ID"
         + " from vta_cliente order by CT_ARMADONUM";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intConta++;
            MlmClienteProsperidad cte = new MlmClienteProsperidad();
            cte.setIntCliente(rs.getInt("CT_ID"));
            cte.setIntUpline(rs.getInt("CT_UPLINE"));
            cte.setDblPuntos(rs.getDouble("CT_PPUNTOS"));
            cte.setDblNegocio(rs.getDouble("CT_PNEGOCIO"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntNumSemanas(rs.getInt("NumSemanas"));
            cte.setIntActivo(rs.getInt("CT_ACTIVO"));
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

      // <editor-fold defaultstate="collapsed" desc="Recorremos la red desde abajo para el calculo de nivel">
      log.info("Inicia Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteProsperidad cte = lstMlmCliente.get(i);
         //Actualizamos puntos grupales
         log.debug("Calculo de nivel..id:." + cte.getIntCliente());
         log.debug("Hijos:" + cte.getIntTotalHijos());
         log.debug("Semana:" + cte.getIntNumSemanas());
         log.debug("Activo:" + cte.getIntActivo());

//         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblValorDbl1() + cte.getDblValorDbl2() + cte.getDblValorDbl3() + cte.getDblValorDbl4());
//         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblValorDbl5() + cte.getDblValorDbl6() + cte.getDblValorDbl7() + cte.getDblValorDbl8());
         //Calculamos el nivel de la persona
         int intNivel = 0;
         //Debo estar activo y tener 4 hijos activos que hicieron su pago
         if (cte.getIntActivo() == 1 && cte.getIntTotalHijos() >= 4) {
            double dblComision = 0;
            String strNivel = "";
            if (cte.getIntNumSemanas() >= 1 && cte.getIntNumSemanas() <= 10) {
               switch (cte.getIntNumSemanas()) {
                  case 1:
                     dblComision = 180;
                     strNivel = "SEM2";
                     break;
                  case 2:
                     dblComision = 360;
                     strNivel = "SEM3";
                     break;
                  case 3:
                     dblComision = 720;
                     strNivel = "SEM4";
                     break;
                  case 4:
                     dblComision = 1440;
                     strNivel = "SEM5";
                     break;
                  case 5:
                     dblComision = 2880;
                     strNivel = "SEM6";
                     break;
                  case 6:
                     dblComision = 5760;
                     strNivel = "SEM7";
                     break;
                  case 7:
                     dblComision = 11520;
                     strNivel = "SEM8";
                     break;
                  case 8:
                     dblComision = 23040;
                     strNivel = "SEM9";
                     break;
                  case 9:
                     dblComision = 46080;
                     strNivel = "SEM10";
                     break;
               }
            } else //Si llev 10 semanas se calcula mensual 
            {
               if (cte.getIntNumSemanas() >= 10) {
                  dblComision = 2000;
                  strNivel = "MENS";
               }
            }
            log.debug("dblComision:" + dblComision);
            if (dblComision > 0) {
               //Entidad para el detalle de las comisiones
               mlm_comision_deta comisDeta = new mlm_comision_deta();
               comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
               comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
               comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
               comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
               comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
               comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
               comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
               comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
               comisDeta.setFieldInt("COMI_ARMADOINI", 1);
               comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
               comisDeta.setFieldInt("COMI_ARMADONUM", 1);
               comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
               comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
               comisDeta.setFieldString("COMI_CODIGO", strNivel);
               comisDeta.Agrega(oConn);
               cte.setDblComision(dblComision);
            }
         }
         cte.setIntNivelRed(intNivel);
         //Guardamos info del nodo
//
//         //Subimos info al padre
//         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
//         //Si se encontro el padre
//         if (intIdx != -1) {
//            MlmClienteProsperidad ctePadre = lstMlmCliente.get(intIdx);
//            log.debug(cte.getIntUpline() + " intIdx" + intIdx);
//            ctePadre.setDblGNegocio(ctePadre.getDblGNegocio() + cte.getDblGNegocio());
//            ctePadre.setDblGPuntos(ctePadre.getDblGPuntos() + cte.getDblGPuntos());
//            ctePadre.setDblValorDbl1(ctePadre.getDblValorDbl1() + cte.getDblNegocio());
//            ctePadre.setDblValorDbl2(ctePadre.getDblValorDbl2() + cte.getDblValorDbl1());
//            ctePadre.setDblValorDbl3(ctePadre.getDblValorDbl3() + cte.getDblValorDbl2());
//            ctePadre.setDblValorDbl4(ctePadre.getDblValorDbl4() + cte.getDblValorDbl3());
//            ctePadre.setDblValorDbl5(ctePadre.getDblValorDbl5() + cte.getDblPuntos());
//            ctePadre.setDblValorDbl6(ctePadre.getDblValorDbl6() + cte.getDblValorDbl5());
//            ctePadre.setDblValorDbl7(ctePadre.getDblValorDbl7() + cte.getDblValorDbl6());
//            ctePadre.setDblValorDbl8(ctePadre.getDblValorDbl8() + cte.getDblValorDbl7());
//         }

      }
      log.info("Termina Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD("BEGIN");

      //Guardamos informacion de los nodos
//      log.info("Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
//      for (int i = 0; i < lstMlmCliente.size(); i++) {
//         MlmClienteProsperidad cte = lstMlmCliente.get(i);
//         //Validamos que solo los nodos con valores se guarden
//         if (cte.getDblPuntos() != 0
//            || cte.getDblValorDbl2() != 0
//            || cte.getDblValorDbl3() != 0
//            || cte.getDblValorDbl4() != 0
//            || cte.getDblValorDbl5() != 0
//            || cte.getDblValorDbl6() != 0
//            || cte.getDblValorDbl7() != 0
//            || cte.getDblValorDbl8() != 0
//            || cte.getDblGPuntos() != 0
//            || cte.getDblGNegocio() != 0) {
//            //G_NEGOCIO
//            //G_PUNTOS
//            //NIVELRED
//            String strUpdate = "UPDATE vta_cliente SET ";
//            strUpdate += "CT_NIVELRED = " + cte.getIntNivelRed() + ",";
//            strUpdate += "CT_GPUNTOS = " + (cte.getDblGPuntos()) + ",";
//            strUpdate += "CT_GNEGOCIO = " + cte.getDblGNegocio() + " ";
//            strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
//            this.oConn.runQueryLMD(strUpdate);
//         }
//
//      }
//      this.oConn.runQueryLMD("COMMIT");
      log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si son definitivas movemos informacion historica">
      if (this.isEsCorridaDefinitiva()) {
         log.info("Inicia Guardado de historicos en clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
         log.info("Inicia Detectamos los inactivos de todo el mes... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
         for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
            MlmClienteProsperidad cte = lstMlmCliente.get(i);
            if(cte.getIntActivo() == 0){
               //Los movemos hasta abajo de la red
            }
         }
         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteProsperidad cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
         if (cte.getDblComision() != 0) {
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
            comisionM.setFieldDouble("CO_PUNTOS_G", cte.getDblGPuntos() + cte.getDblPuntos());
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
      
      // <editor-fold defaultstate="collapsed" desc="Si son definitivas movemos informacion historica">
      if (this.isEsCorridaDefinitiva()) {
         log.info("Inicia Guardado de historicos en clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
         log.info("Inicia Detectamos los inactivos de todo el mes... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
         for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
            MlmClienteProsperidad cte = lstMlmCliente.get(i);
            if(cte.getIntActivo() == 0){
               //Los movemos hasta abajo de la red
            }
         }
         //Marcamos como inactivos a todos los clientes para la siguiente corrida
         String strUpdate = "Update vta_cliente set CT_ACTIVO = 0;";
         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
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

      Fechas fecha = new Fechas();

      //Consulta para buscar los kits de inscripción de este periodo
      String strSql = "select vta_tickets.CT_ID, vta_ticketsdeta.TKTD_CVE from  vta_tickets, vta_ticketsdeta  \n"
         + " where  "
         + "  vta_tickets.TKT_ID = vta_ticketsdeta.TKT_ID  "
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
                     + " ,PROSP_PER_ACTIVO = " + intPeriodo
                     + " ,PROS_SEMANA = " + intIdPeriodoSemanal
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
}
