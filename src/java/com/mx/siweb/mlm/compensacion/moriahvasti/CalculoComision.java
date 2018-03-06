/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.moriahvasti;

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
         strSQL += "MV_ST1_N  =  0,";
         strSQL += "MV_ST2_N  =  0,";
         strSQL += "MV_ST3_N  =  0,";
         strSQL += "MV_ST4_N  =  0,";
         strSQL += "MV_ST1_P  =  0,";
         strSQL += "MV_ST2_P  =  0,";
         strSQL += "MV_ST3_P  =  0,";
         strSQL += "MV_ST4_P  =  0 ";
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
              + ",MV_ST1_N,MV_ST2_N,MV_ST3_N,MV_ST4_N"
              + ",MV_ST1_P,MV_ST2_P,MV_ST3_P,MV_ST4_P "
              + ",MV_MES_1,CT_CONTEO_HIJOS,SC_ID,MV_MES_2"
              + ",MV_SE_NOMBRO_2,MV_SE_NOMBRO_3,MV_SE_NOMBRO_4,MV_SE_NOMBRO_5"
              + ",MV_SE_NOMBRO_6,MV_SE_NOMBRO_7,MV_COMPRO_PAQ_MOR "
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
            cte.setDblValorDbl1(rs.getDouble("MV_ST1_N"));
            cte.setDblValorDbl2(rs.getDouble("MV_ST2_N"));
            cte.setDblValorDbl3(rs.getDouble("MV_ST3_N"));
            cte.setDblValorDbl4(rs.getDouble("MV_ST4_N"));
            cte.setDblValorDbl5(rs.getDouble("MV_ST1_P"));
            cte.setDblValorDbl6(rs.getDouble("MV_ST2_P"));
            cte.setDblValorDbl7(rs.getDouble("MV_ST3_P"));
            cte.setDblValorDbl8(rs.getDouble("MV_ST4_P"));
            cte.setDblValorDbl9(rs.getDouble("MV_MES_1"));
            cte.setDblValorDbl10(rs.getDouble("MV_MES_2"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntValor2(rs.getInt("MV_SE_NOMBRO_2"));
            cte.setIntValor3(rs.getInt("MV_SE_NOMBRO_3"));
            cte.setIntValor4(rs.getInt("MV_SE_NOMBRO_4"));
            cte.setIntValor5(rs.getInt("MV_SE_NOMBRO_5"));
            cte.setIntValor6(rs.getInt("MV_SE_NOMBRO_6"));
            cte.setIntValor7(rs.getInt("MV_SE_NOMBRO_7"));
            cte.setIntValor8(rs.getInt("MV_COMPRO_PAQ_MOR"));
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
         log.debug("Calculo de nivel..." + cte.getIntCliente());
         log.debug(cte.getDblGNegocio() + " " + cte.getDblNegocio());
         log.debug(cte.getDblGPuntos() + " " + cte.getDblPuntos());
         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblNegocio());
         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblPuntos());

//         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblValorDbl1() + cte.getDblValorDbl2() + cte.getDblValorDbl3() + cte.getDblValorDbl4());
//         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblValorDbl5() + cte.getDblValorDbl6() + cte.getDblValorDbl7() + cte.getDblValorDbl8());
         //Calculamos el nivel de la persona
         int intNivel = 0;
         if (cte.getDblPuntos() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
            //Recorremos la lista de parametros del plan para definir el nivel
            Iterator<TableMaster> it = lstParams.iterator();
            while (it.hasNext()) {
               TableMaster tbn = it.next();
               if (cte.getDblGPuntos() >= tbn.getFieldDouble("CP_GPUNTOS")
                       && cte.getDblValorDbl9() >= tbn.getFieldDouble("CP_GPUNTOS")
                       && cte.getDblValorDbl10() >= tbn.getFieldDouble("CP_GPUNTOS")
                       && cte.getIntValor8()>= tbn.getFieldDouble("CP_DIFERENCIAL1")
                       && cte.getIntTotalHijos()>=tbn.getFieldInt("CP_HIJOS")
                       ) {
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
            log.debug(cte.getIntUpline() + " intIdx" + intIdx);
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
            //MV_ST1_N ... 4
            //MV_ST1_P ... 4
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "MV_ST1_N = " + cte.getDblValorDbl1() + ",";
            strUpdate += "MV_ST2_N = " + cte.getDblValorDbl2() + ",";
            strUpdate += "MV_ST3_N = " + cte.getDblValorDbl3() + ",";
            strUpdate += "MV_ST4_N = " + cte.getDblValorDbl4() + ",";
            strUpdate += "MV_ST1_P = " + cte.getDblValorDbl5() + ",";
            strUpdate += "MV_ST2_P = " + cte.getDblValorDbl6() + ",";
            strUpdate += "MV_ST3_P = " + cte.getDblValorDbl7() + ",";
            strUpdate += "MV_ST4_P = " + cte.getDblValorDbl8() + ",";
            strUpdate += "CT_NIVELRED = " + cte.getIntNivelRed() + ",";
            strUpdate += "CT_GPUNTOS = " + (cte.getDblGPuntos()) + ",";
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
         String strUpdate = "UPDATE vta_cliente SET MV_MES_15=MV_MES_14";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_14=MV_MES_13";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_13=MV_MES_12";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_12=MV_MES_11";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_11=MV_MES_10";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_10=MV_MES_9";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_9=MV_MES_8";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_8=MV_MES_7";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_7=MV_MES_6";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_6=MV_MES_5";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_5=MV_MES_4";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_4=MV_MES_3";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_3=MV_MES_2";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_2=MV_MES_1";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_MES_1=CT_GPUNTOS";
         this.oConn.runQueryLMD(strUpdate);
         //Guardamos el periodo del nombramiento
         strUpdate = "UPDATE vta_cliente SET MV_SE_NOMBRO_2=" + this.getIntPeriodo() + " where CT_NIVELRED =2 AND MV_SE_NOMBRO_2 = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_SE_NOMBRO_3=" + this.getIntPeriodo() + " where CT_NIVELRED =3 AND MV_SE_NOMBRO_3 = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_SE_NOMBRO_4=" + this.getIntPeriodo() + " where CT_NIVELRED =4 AND MV_SE_NOMBRO_4 = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_SE_NOMBRO_5=" + this.getIntPeriodo() + " where CT_NIVELRED =5 AND MV_SE_NOMBRO_5 = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_SE_NOMBRO_6=" + this.getIntPeriodo() + " where CT_NIVELRED =6 AND MV_SE_NOMBRO_6 = 0";
         this.oConn.runQueryLMD(strUpdate);
         strUpdate = "UPDATE vta_cliente SET MV_SE_NOMBRO_7=" + this.getIntPeriodo() + " where CT_NIVELRED =7 AND MV_SE_NOMBRO_7 = 0";
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
                        // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
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
                        }
                        // </editor-fold>
                        //Cierre busqueda de nivel para aplicar el factor de unilevel
                        //----- 5 GENERACION ---
                        int intIdx5 = this.BuscaNodoenArreglo(ctePadre4.getIntUpline(), lstMlmClienteIdx, false);
                        if (intIdx5 != -1) {
                           mlm_cliente ctePadre5 = lstMlmCliente.get(intIdx5);
                           // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
                           it = lstParams.iterator();
                           while (it.hasNext()) {
                              TableMaster tbn = it.next();
                              // <editor-fold defaultstate="collapsed" desc="----- 5 GENERACION ---">
                              if (ctePadre5.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                      && ctePadre5.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                 double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL5") / 100);
                                 ctePadre5.setDblComision(ctePadre5.getDblComision() + dblComision);
                                 //Entidad para el detalle de las comisiones
                                 InsertaDetalle(ctePadre5,  cte,  dblComision,  dblImporte,  tbn,  "5");
                              }
                              // </editor-fold>
                           }
                           // </editor-fold>
                           //Cierre busqueda de nivel para aplicar el factor de unilevel
                           //----- 6 GENERACION ---
                           int intIdx6 = this.BuscaNodoenArreglo(ctePadre5.getIntUpline(), lstMlmClienteIdx, false);
                           if (intIdx6 != -1) {
                              mlm_cliente ctePadre6 = lstMlmCliente.get(intIdx6);
                              // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
                              it = lstParams.iterator();
                              while (it.hasNext()) {
                                 TableMaster tbn = it.next();
                                 // <editor-fold defaultstate="collapsed" desc="----- 6 GENERACION ---">
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
                              }
                              // </editor-fold>
                              //Cierre busqueda de nivel para aplicar el factor de unilevel
                              //----- 7 GENERACION ---
                              int intIdx7 = this.BuscaNodoenArreglo(ctePadre6.getIntUpline(), lstMlmClienteIdx, false);
                              if (intIdx7 != -1) {
                                 mlm_cliente ctePadre7 = lstMlmCliente.get(intIdx7);
                                 // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
                                 it = lstParams.iterator();
                                 while (it.hasNext()) {
                                    TableMaster tbn = it.next();
                                    // <editor-fold defaultstate="collapsed" desc="----- 7 GENERACION ---">
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
                                 }
                                 // </editor-fold>
                                 //Cierre busqueda de nivel para aplicar el factor de unilevel
                                 //----- 8 GENERACION ---
                                 int intIdx8 = this.BuscaNodoenArreglo(ctePadre7.getIntUpline(), lstMlmClienteIdx, false);
                                 if (intIdx8 != -1) {
                                    mlm_cliente ctePadre8 = lstMlmCliente.get(intIdx8);
                                    // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
                                    it = lstParams.iterator();
                                    while (it.hasNext()) {
                                       TableMaster tbn = it.next();
                                       // <editor-fold defaultstate="collapsed" desc="----- 8 GENERACION ---">
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
                                    }
                                    // </editor-fold>
                                    //Cierre busqueda de nivel para aplicar el factor de unilevel
                                    //----- 9 GENERACION ---
                                    int intIdx9 = this.BuscaNodoenArreglo(ctePadre8.getIntUpline(), lstMlmClienteIdx, false);
                                    if (intIdx9 != -1) {
                                       mlm_cliente ctePadre9 = lstMlmCliente.get(intIdx9);
                                       // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
                                       it = lstParams.iterator();
                                       while (it.hasNext()) {
                                          TableMaster tbn = it.next();
                                          // <editor-fold defaultstate="collapsed" desc="----- 9 GENERACION ---">
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
                                       }
                                       // </editor-fold>
                                       //Cierre busqueda de nivel para aplicar el factor de unilevel
                                       //----- 10 GENERACION ---
                                       int intIdx10 = this.BuscaNodoenArreglo(ctePadre9.getIntUpline(), lstMlmClienteIdx, false);
                                       if (intIdx10 != -1) {
                                          mlm_cliente ctePadre10 = lstMlmCliente.get(intIdx10);
                                          // <editor-fold defaultstate="collapsed" desc="Recorremos la lista de parametros del plan obtener los porcentajes">
                                          it = lstParams.iterator();
                                          while (it.hasNext()) {
                                             TableMaster tbn = it.next();
                                             // <editor-fold defaultstate="collapsed" desc="----- 10 GENERACION ---">
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
                                          }
                                          // </editor-fold>
                                       }

                                    }

                                 }

                              }
                           }
                        }

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

            // <editor-fold defaultstate="collapsed" desc="Calculo de bono de gastos">
            /*if (cte.getIntNivelRed() >= 0 && cte.getDblPuntos() >= 30000) {
             double dblComisPer = 2000;
             //Entidad para el detalle de las comisiones
             mlm_comision_deta comisDeta = new mlm_comision_deta();
             comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
             comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
             comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
             comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
             comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
             comisDeta.setFieldDouble("COMI_IMPORTE", dblComisPer);
             comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
             comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
             comisDeta.setFieldInt("COMI_ARMADOINI", 1);
             comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
             comisDeta.setFieldInt("COMI_ARMADONUM", 1);
             comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
             comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
             comisDeta.setFieldString("COMI_CODIGO", "BONO_GASTO");
             cte.setDblComision(cte.getDblComision() + dblComisPer);
             comisDeta.Agrega(oConn);
             }*/
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Calculo de bono de nombramiento(Deprecated)">
            log.debug("Bonos por nombramiento...");
            /*if (
             (cte.getIntNivelRed() == 2 && cte.getIntValor2() == 0) ||
             (cte.getIntNivelRed() == 3 && cte.getIntValor3() == 0) ||
             (cte.getIntNivelRed() == 4 && cte.getIntValor4() == 0) ||
             (cte.getIntNivelRed() == 5 && cte.getIntValor5() == 0) ||
             (cte.getIntNivelRed() == 6 && cte.getIntValor6() == 0) ||
             (cte.getIntNivelRed() == 7 && cte.getIntValor7() == 0) 
             ) {
               
             double dblImporteNombramiento = lstParams.get(cte.getIntNivelRed() -1).getFieldDouble("CP_BONO_NOMBRARSE");
             log.debug(cte.getIntCliente() +  " " + dblImporteNombramiento);
             //Entidad para el detalle de las comisiones
             mlm_comision_deta comisDeta = new mlm_comision_deta();
             comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
             comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
             comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
             comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
             comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
             comisDeta.setFieldDouble("COMI_IMPORTE", dblImporteNombramiento);
             comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
             comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
             comisDeta.setFieldInt("COMI_ARMADOINI", 1);
             comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
             comisDeta.setFieldInt("COMI_ARMADONUM", 1);
             comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
             comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
             comisDeta.setFieldString("COMI_CODIGO", "BONO_NOM" + cte.getIntNivelRed());
             //Solo se suma en el caso del 3 y 7
             if(cte.getIntNivelRed() == 3 || cte.getIntNivelRed() == 7){
             cte.setDblComision(cte.getDblComision() + dblImporteNombramiento);
             }
               
             comisDeta.Agrega(oConn);
             }*/
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calculo de bono de inscripcion">
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

   private void InsertaDetalle(mlm_cliente ctePadre, mlm_cliente cte, double dblComision, double dblImporte, TableMaster tbn, String strNivel) {
      mlm_comision_deta comisDeta = new mlm_comision_deta();
      comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
      comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
      comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
      comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
      comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL" + strNivel));
      comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
      comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
      comisDeta.setFieldInt("COMI_NIVEL", ctePadre.getIntNivelRed());
      comisDeta.setFieldInt("COMI_ARMADOINI", 1);
      comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
      comisDeta.setFieldInt("COMI_ARMADONUM", 1);
      comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
      comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
      comisDeta.setFieldString("COMI_CODIGO", "UNI" + strNivel);
      comisDeta.Agrega(oConn);
   }
   // </editor-fold>
}
