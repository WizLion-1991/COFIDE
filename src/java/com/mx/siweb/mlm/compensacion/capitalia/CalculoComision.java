/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.capitalia;

import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision_deta;
import com.mx.siweb.mlm.utilerias.Redes;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;

/**
 * Plan de comisiones de capitalia
 *
 * @author ZeusGalindo
 */
public class CalculoComision extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.capitalia.CalculoComision.class.getName());

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
//         doDetectaKit();
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
         strSQL += "CAP_NUM_LEVEL1    =  0,";
         strSQL += "CAP_NUM_LEVEL2    =  0,";
         strSQL += "CAP_NUM_LEVEL3    =  0,";
         strSQL += "CAP_NUM_LEVEL4    =  0,";
         strSQL += "CAP_NUM_LEVEL5    =  0";
         this.oConn.runQueryLMD(strSQL);
      }
   }

   @Override
   public void doFase3() {
      super.doFase3();
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 3.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Lista para los clientes
      ArrayList<MLMClienteCapitalia> lstMlmCliente = new ArrayList<MLMClienteCapitalia>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Cargamos en un arreglo la info de los clientes">
      log.info("Inicia Carga de clientes.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intConta = -1;
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO,CAP_NUM_LEVEL1,SC_ID "
              + " from vta_cliente order by CT_ARMADONUM";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intConta++;
            MLMClienteCapitalia cte = new MLMClienteCapitalia();
            cte.setIntCliente(rs.getInt("CT_ID"));
            cte.setIntUpline(rs.getInt("CT_UPLINE"));
            cte.setDblPuntos(rs.getDouble("CT_PPUNTOS"));
            cte.setDblNegocio(rs.getDouble("CT_PNEGOCIO"));
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
         log.debug("Calculo de nivel..." + cte.getIntCliente());
         log.debug(cte.getDblGNegocio() + " " + cte.getDblNegocio());
         log.debug(cte.getDblGPuntos() + " " + cte.getDblPuntos());
         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblNegocio());
         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblPuntos());

//         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblValorDbl1() + cte.getDblValorDbl2() + cte.getDblValorDbl3() + cte.getDblValorDbl4());
//         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblValorDbl5() + cte.getDblValorDbl6() + cte.getDblValorDbl7() + cte.getDblValorDbl8());
         //Calculamos el nivel de la persona
         int intNivel = 1;
         if (cte.getDblPuntos() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
            //Recorremos la lista de parametros del plan para definir el nivel
            Iterator<TableMaster> it = lstParams.iterator();
            while (it.hasNext()) {
               TableMaster tbn = it.next();
//               if (cte.getDblGPuntos() >= tbn.getFieldDouble("CP_GPUNTOS")
//                       && cte.getDblValorDbl9() >= tbn.getFieldDouble("CP_GPUNTOS")
//                       && cte.getDblValorDbl10() >= tbn.getFieldDouble("CP_GPUNTOS")) {
               intNivel = tbn.getFieldInt("CP_NIVEL");
//               }
            }
         }
         cte.setIntNivelRed(intNivel);
         //Guardamos info del nodo

         //Subimos info al padre
         //----- 1 GENERACION ---
         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
         if (intIdx != -1) {
            MLMClienteCapitalia ctePadre = lstMlmCliente.get(intIdx);
            ctePadre.setIntLevel1(ctePadre.getIntLevel1() + 1);
            //----- 2 GENERACION ---
            int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
            //Si se encontro el abuelo
            if (intIdx2 != -1) {
               MLMClienteCapitalia ctePadre2 = lstMlmCliente.get(intIdx2);
               ctePadre2.setIntLevel2(ctePadre2.getIntLevel2() + 1);
               //----- 3 GENERACION ---
               int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
               //Si se encontro el abuelo
               if (intIdx3 != -1) {
                  MLMClienteCapitalia ctePadre3 = lstMlmCliente.get(intIdx3);
                  ctePadre3.setIntLevel3(ctePadre3.getIntLevel3() + 1);

                  //----- 4 GENERACION ---
                  int intIdx4 = this.BuscaNodoenArreglo(ctePadre3.getIntUpline(), lstMlmClienteIdx, false);
                  //Si se encontro el abuelo
                  if (intIdx4 != -1) {
                     MLMClienteCapitalia ctePadre4 = lstMlmCliente.get(intIdx4);
                     ctePadre4.setIntLevel4(ctePadre4.getIntLevel4() + 1);
                     //----- 5 GENERACION ---
                     int intIdx5 = this.BuscaNodoenArreglo(ctePadre4.getIntUpline(), lstMlmClienteIdx, false);
                     //Si se encontro el abuelo
                     if (intIdx5 != -1) {
                        MLMClienteCapitalia ctePadre5 = lstMlmCliente.get(intIdx5);
                        ctePadre5.setIntLevel5(ctePadre5.getIntLevel5() + 1);
                     }
                  }
               }
            }
         }

      }
      log.info("Termina Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD("BEGIN");

      //Guardamos informacion de los nodos
      log.info("Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (MLMClienteCapitalia cte : lstMlmCliente) {
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
            //XOCO_ST1_N ... 4
            //XOCO_ST1_P ... 4
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "CAP_NUM_LEVEL1 = " + cte.getIntLevel1() + ",";
            strUpdate += "CAP_NUM_LEVEL2 = " + cte.getIntLevel2() + ",";
            strUpdate += "CAP_NUM_LEVEL3 = " + cte.getIntLevel3() + ",";
            strUpdate += "CAP_NUM_LEVEL4 = " + cte.getIntLevel4() + ",";
            strUpdate += "CAP_NUM_LEVEL5 = " + cte.getIntLevel5() + ",";
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

      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de Unilevel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intContaProcess = 0;
      int intContaProcessTot = 0;

      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MLMClienteCapitalia cte = lstMlmCliente.get(i);
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
                           }
                           // </editor-fold>
                        }//Cierre busqueda de nivel para aplicar el factor de unilevel
                        //----- 5 GENERACION ---
                        int intIdx5 = this.BuscaNodoenArreglo(ctePadre4.getIntUpline(), lstMlmClienteIdx, false);
                        //Si se encontro el abuelo
                        if (intIdx5 != -1) {
                           mlm_cliente ctePadre5 = lstMlmCliente.get(intIdx5);
                           //Recorremos la lista de parametros del plan obtener los porcentajes
                           it = lstParams.iterator();
                           while (it.hasNext()) {
                              TableMaster tbn = it.next();
                              // <editor-fold defaultstate="collapsed" desc="----- 5 GENERACION ---">
                              if (ctePadre5.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                      && ctePadre5.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                 double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL5") / 100);
                                 ctePadre5.setDblComision(ctePadre5.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/

                                 //Entidad para el detalle de las comisiones

                                 mlm_comision_deta comisDeta = new mlm_comision_deta();
                                 comisDeta.setFieldInt("CT_ID", ctePadre5.getIntCliente());
                                 comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                 comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                 comisDeta.setFieldInt("COMI_DESTINO", ctePadre5.getIntCliente());
                                 comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL5"));
                                 comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                 comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                 comisDeta.setFieldInt("COMI_NIVEL", ctePadre5.getIntNivelRed());
                                 comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                                 comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                                 comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                                 comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                                 comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                                 comisDeta.setFieldString("COMI_CODIGO", "UNI5");
                                 comisDeta.Agrega(oConn);

                              }
                              // </editor-fold>
                           }//Cierre busqueda de nivel para aplicar el factor de unilevel
                        }
                        //-----CIERRA 5 GENERACION ---
                     }
                     //-----CIERRA 4 GENERACION --
                  }//-----CIERRA 3 GENERACION --
               }//-----CIERRA 2 GENERACION --
               if (intContaProcess == 1000) {
                  intContaProcess = 0;
                  log.info("Llevamos " + intContaProcessTot + " registros unilevel...");
                  System.out.flush();
               }
            }//-----CIERRA 1 GENERACION --
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Ganancias extras">
            if (cte.getIntLevel1() == 6) {
               //Recorremos la lista de parametros del plan obtener los porcentajes
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  if (cte.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                          && cte.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                     double dblComisPer = (tbn.getFieldDouble("CP_EXTRA_LEVEL1"));
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
                     comisDeta.setFieldString("COMI_CODIGO", "EXTRA_LEVEL1");
                     cte.setDblComision(cte.getDblComision() + dblComisPer);
                     comisDeta.Agrega(oConn);
                  }
               }

            }
            if (cte.getIntLevel2() == 36) {
               //Recorremos la lista de parametros del plan obtener los porcentajes
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  if (cte.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                          && cte.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                     double dblComisPer = (tbn.getFieldDouble("CP_EXTRA_LEVEL2"));
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
                     comisDeta.setFieldString("COMI_CODIGO", "EXTRA_LEVEL2");
                     cte.setDblComision(cte.getDblComision() + dblComisPer);
                     comisDeta.Agrega(oConn);
                  }
               }
            }
            if (cte.getIntLevel3() == 216) {
               //Recorremos la lista de parametros del plan obtener los porcentajes
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  if (cte.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                          && cte.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                     double dblComisPer = (tbn.getFieldDouble("CP_EXTRA_LEVEL3"));
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
                     comisDeta.setFieldString("COMI_CODIGO", "EXTRA_LEVEL3");
                     cte.setDblComision(cte.getDblComision() + dblComisPer);
                     comisDeta.Agrega(oConn);
                  }
               }
            }
            if (cte.getIntLevel4() == 1296) {
               //Recorremos la lista de parametros del plan obtener los porcentajes
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  if (cte.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                          && cte.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                     double dblComisPer = (tbn.getFieldDouble("CP_EXTRA_LEVEL4"));
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
                     comisDeta.setFieldString("COMI_CODIGO", "EXTRA_LEVEL4");
                     cte.setDblComision(cte.getDblComision() + dblComisPer);
                     comisDeta.Agrega(oConn);
                  }
               }
            }
            if (cte.getIntLevel5() == 7776) {
               //Recorremos la lista de parametros del plan obtener los porcentajes
               Iterator<TableMaster> it = lstParams.iterator();
               while (it.hasNext()) {
                  TableMaster tbn = it.next();
                  if (cte.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                          && cte.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                     double dblComisPer = (tbn.getFieldDouble("CP_EXTRA_LEVEL5"));
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
                     comisDeta.setFieldString("COMI_CODIGO", "EXTRA_LEVEL5");
                     cte.setDblComision(cte.getDblComision() + dblComisPer);
                     comisDeta.Agrega(oConn);
                  }
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Calculo de bono de VIAJE">
            {
               if (cte.getIntNivelRed() >= 0 && cte.getIntLevel3() == 216) {
                  double dblComisPer = 5000;
                  if (cte.getIntLevel4() == 1296) {
                     dblComisPer = 10000;
                  }
                  if (cte.getIntLevel5() == 7776) {
                     dblComisPer = 15000;
                  }
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
                  comisDeta.setFieldString("COMI_CODIGO", "BONO_VIAJE");
                  cte.setDblComision(cte.getDblComision() + dblComisPer);
                  comisDeta.Agrega(oConn);
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
      com.mx.siweb.mlm.compensacion.capitalia.ParametrosComis paramsTmp = new com.mx.siweb.mlm.compensacion.capitalia.ParametrosComis();
      //Obtenemos los datos de la base de datos
      ArrayList<TableMaster> lstParametros = paramsTmp.ObtenDatosVarios(" 1=1 ORDER BY CP_ORDEN", oConn);
      return lstParametros;
   }
   // </editor-fold>
}
