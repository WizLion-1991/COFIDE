/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.jonfilu;

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
import java.util.Iterator;

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
         strSQL += "FIRM_ST1_N  =  0,";
         strSQL += "FIRM_ST2_N  =  0,";
         strSQL += "FIRM_ST3_N  =  0,";
         strSQL += "FIRM_ST4_N  =  0,";
         strSQL += "FIRM_ST1_P  =  0,";
         strSQL += "FIRM_ST2_P  =  0,";
         strSQL += "FIRM_ST3_P  =  0,";
         strSQL += "FIRM_ST4_P  =  0 ";
         this.oConn.runQueryLMD(strSQL);
      }
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
              + " from vta_cliente where CT_ACTIVO = 1 "
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
              + ",FIRM_ST1_N,FIRM_ST2_N,FIRM_ST3_N,FIRM_ST4_N"
              + ",FIRM_ST1_P,FIRM_ST2_P,FIRM_ST3_P,FIRM_ST4_P "
              + ",FIRM_MES_1,CT_CONTEO_HIJOS,SC_ID"
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
            cte.setDblValorDbl1(rs.getDouble("FIRM_ST1_N"));
            cte.setDblValorDbl2(rs.getDouble("FIRM_ST2_N"));
            cte.setDblValorDbl3(rs.getDouble("FIRM_ST3_N"));
            cte.setDblValorDbl4(rs.getDouble("FIRM_ST4_N"));
            cte.setDblValorDbl5(rs.getDouble("FIRM_ST1_P"));
            cte.setDblValorDbl6(rs.getDouble("FIRM_ST2_P"));
            cte.setDblValorDbl7(rs.getDouble("FIRM_ST3_P"));
            cte.setDblValorDbl8(rs.getDouble("FIRM_ST4_P"));
            cte.setDblValorDbl9(rs.getDouble("FIRM_MES_1"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
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
               if ((cte.getDblGPuntos()+cte.getDblPuntos()) >= tbn.getFieldDouble("CP_GPUNTOS")
                       && cte.getDblValorDbl9() >= tbn.getFieldDouble("CP_HIST1")
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
            //FIRM_ST1_N ... 4
            //FIRM_ST1_P ... 4
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "FIRM_ST1_N = " + cte.getDblValorDbl1() + ",";
            strUpdate += "FIRM_ST2_N = " + cte.getDblValorDbl2() + ",";
            strUpdate += "FIRM_ST3_N = " + cte.getDblValorDbl3() + ",";
            strUpdate += "FIRM_ST4_N = " + cte.getDblValorDbl4() + ",";
            strUpdate += "FIRM_ST1_P = " + cte.getDblValorDbl5() + ",";
            strUpdate += "FIRM_ST2_P = " + cte.getDblValorDbl6() + ",";
            strUpdate += "FIRM_ST3_P = " + cte.getDblValorDbl7() + ",";
            strUpdate += "FIRM_ST4_P = " + cte.getDblValorDbl8() + ",";
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
         String strUpdate = "UPDATE vta_cliente SET FIRM_MES_15=FIRM_MES_14";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_14=FIRM_MES_13";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_13=FIRM_MES_12";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_12=FIRM_MES_11";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_11=FIRM_MES_10";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_10=FIRM_MES_9";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_9=FIRM_MES_8";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_8=FIRM_MES_7";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_7=FIRM_MES_6";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_6=FIRM_MES_5";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_5=FIRM_MES_4";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_4=FIRM_MES_3";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_3=FIRM_MES_2";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_2=FIRM_MES_1";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_1=CT_GPUNTOS";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET FIRM_MES_PP_1=CT_PPUNTOS";
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
                     comisDeta.Agrega(oConn);
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
                        comisDeta.Agrega(oConn);
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
                     //----- 4 GENERACION ---
                     int intIdx4 = this.BuscaNodoenArreglo(ctePadre3.getIntUpline(), lstMlmClienteIdx, false);
                     //Si se encontro el abuelo
                     if (intIdx4 != -1) {
                        mlm_cliente ctePadre4 = lstMlmCliente.get(intIdx4);
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

            // <editor-fold defaultstate="collapsed" desc="Calculo de venta personal">
            if (cte.getIntNivelRed() >= 0) {
               double dblComisPer = dblImporte * (lstParams.get(cte.getIntNivelRed()).getFieldDouble("CP_DIFERENCIAL1") / 100);
               //Entidad para el detalle de las comisiones
               mlm_comision_deta comisDeta = new mlm_comision_deta();
               comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
               comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
               comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
               comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
               comisDeta.setFieldDouble("COMI_PORCENTAJE", lstParams.get(cte.getIntNivelRed()).getFieldDouble("CP_DIFERENCIAL1"));
               comisDeta.setFieldDouble("COMI_IMPORTE", dblComisPer);
               comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
               comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
               comisDeta.setFieldInt("COMI_ARMADOINI", 1);
               comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
               comisDeta.setFieldInt("COMI_ARMADONUM", 1);
               comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
               comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
               comisDeta.setFieldString("COMI_CODIGO", "PER");
               cte.setDblComision(cte.getDblComision() + dblComisPer);
               comisDeta.Agrega(oConn);
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
         if (cte.getDblPuntos() != 0) {
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
   // </editor-fold>
}
