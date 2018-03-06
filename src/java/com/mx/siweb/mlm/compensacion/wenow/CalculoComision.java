/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.wenow;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import com.mx.siweb.mlm.compensacion.Comision_MLM;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision_deta;
import com.mx.siweb.mlm.utilerias.Redes;
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
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.wenow.CalculoComision.class.getName());

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
         strSQL += "CT_GENERACION1  =  0,";
         strSQL += "CT_GENERACION2  =  0,";
         strSQL += "CT_GENERACION3  =  0 ";
         this.oConn.runQueryLMD(strSQL);
      }
   }

   @Override
   public void doFase3() {
      super.doFase3();
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 3.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Lista para los clientes
      ArrayList<MlmClienteWenow> lstMlmCliente = new ArrayList<MlmClienteWenow>();
      //Lista para los indices
      ArrayList<ClienteIndice> lstMlmClienteIdx = new ArrayList<ClienteIndice>();
      //Parametros de comisiones
      ArrayList<TableMaster> lstParams = getParameters();

      // <editor-fold defaultstate="collapsed" desc="Deteccion automatica de nodos">
      log.debug("Comienza deteccion de nuevos....");
      detectaNvos();
      log.debug("Termina deteccion de nuevos....");
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
          //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
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
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO,CT_CONTEO_HIJOS,SC_ID,CT_FECHA_ACTIVA"
              + ",WN_AFILIADO"
              + ",WN_ACTIVO_AFILIADO"
              + ",WN_ID_MASTER"
              + ",WN_TRAINING"
              + ",CT_ACTIVO"
              + ",WN_GLOBAL"
              + ",WN_ACTIVO_GLOBAL"
              + ",MPE_ID"
              + ",WN_RECOMPRA"
              + ",CT_SPONZOR,WN_EXCEDENTE"
              + " "
              + " from vta_cliente order by CT_ARMADONUM";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            intConta++;
            MlmClienteWenow cte = new MlmClienteWenow();
            cte.setIntCliente(rs.getInt("CT_ID"));
            cte.setIntUpline(rs.getInt("CT_UPLINE"));
            cte.setDblPuntos(rs.getDouble("CT_PPUNTOS"));
            cte.setDblNegocio(rs.getDouble("CT_PNEGOCIO"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntPeriodo(rs.getInt("MPE_ID"));
            cte.setIntSponsor(rs.getInt("CT_SPONZOR"));
            //Es training

            if (rs.getInt("WN_TRAINING") == 1 && rs.getInt("CT_ACTIVO") == 1) {
               cte.setBolTraining(true);
            }
            //Es afiliado
            if (rs.getInt("WN_AFILIADO") == 1 && rs.getInt("WN_ACTIVO_AFILIADO") == 1) {
               cte.setBolAfiliado(true);
            }

            //Es global
            if (rs.getInt("WN_ID_MASTER") != 0 && rs.getInt("WN_GLOBAL") == 1 && rs.getInt("WN_ACTIVO_GLOBAL") == 1) {
               cte.setBolGlobal(true);
            }
            if (rs.getInt("WN_RECOMPRA") == 1) {
               cte.setBolRecompra(true);
            } else {
               cte.setBolRecompra(false);
            }
            cte.setDblValorDbl8(rs.getDouble("WN_EXCEDENTE"));
            lstMlmCliente.add(cte);
            //Para indexarlo
            ClienteIndice tupla = new ClienteIndice();
            tupla.setIntCte(rs.getInt("CT_ID"));
            tupla.setIntIndice(intConta);
            lstMlmClienteIdx.add(tupla);
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
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
         MlmClienteWenow cte = lstMlmCliente.get(i);
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
                       && cte.getDblValorDbl10() >= tbn.getFieldDouble("CP_GPUNTOS")) {
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
            MlmClienteWenow ctePadre = lstMlmCliente.get(intIdx);
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
//            ctePadre.setDblValorDbl8(ctePadre.getDblValorDbl8() + cte.getDblValorDbl7());
         }

      }
      log.info("Termina Calculo de nivel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion para guardado de datos grupales">
      this.oConn.runQueryLMD("BEGIN");

      //Guardamos informacion de los nodos
      log.info("Inicia Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteWenow cte = lstMlmCliente.get(i);
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
         String strUpdate = "UPDATE vta_cliente SET WN_RECOMPRA=0 ,WN_EXCEDENTE = 0";
         this.oConn.runQueryLMD(strUpdate);

         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Calculo de estructuras">
      log.info("Inicia Calculo de estructuras Binario Global.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteWenow cte = lstMlmCliente.get(i);
         //----- 1 GENERACION ---
         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
         if (intIdx != -1) {
            MlmClienteWenow ctePadre = lstMlmCliente.get(intIdx);
            ctePadre.setIntValor1(ctePadre.getIntValor1() + 1);
            int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
            //----- 2 GENERACION ---
            //Si se encontro el abuelo
            if (intIdx2 != -1) {
               MlmClienteWenow ctePadre2 = lstMlmCliente.get(intIdx2);
               ctePadre2.setIntValor2(ctePadre2.getIntValor2() + 1);
               //----- 3 GENERACION ---
               int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
               //Si se encontro el abuelo
               if (intIdx3 != -1) {
                  MlmClienteWenow ctePadre3 = lstMlmCliente.get(intIdx3);
                  ctePadre3.setIntValor3(ctePadre3.getIntValor3() + 1);
               }
            }
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Calculo de nuevos ingresos 2, 3 y 4 generacion">
      double dblComision = 100;
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         MlmClienteWenow cte = lstMlmCliente.get(i);

         //Revisamos el periodo de ingreso y que sea afiliado
         if (cte.isBolAfiliado() && cte.getIntPeriodo() == this.intPeriodo) {
            log.debug("Calculocando otros niveles para el cte:" + cte.getStrNombre() + " " + cte.getIntCliente());
            // <editor-fold defaultstate="collapsed" desc="Bono de ingreso 2,3 y 4">
            //----- 1 GENERACION ---
            int intIdx = this.BuscaNodoenArreglo(cte.getIntSponsor(), lstMlmClienteIdx, false);
            if (intIdx != -1) {
               MlmClienteWenow ctePadre = lstMlmCliente.get(intIdx);
               int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntSponsor(), lstMlmClienteIdx, false);
               //----- 2 GENERACION ---
               //Si se encontro el abuelo
               if (intIdx2 != -1) {
                  MlmClienteWenow ctePadre2 = lstMlmCliente.get(intIdx2);
                  if (ctePadre2.isBolAfiliado()) {
                     AgregaComision(cte, ctePadre2, dblComision, "ING2");
                  }

                  //----- 3 GENERACION ---
                  int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntSponsor(), lstMlmClienteIdx, false);
                  //Si se encontro el bisabuelo
                  if (intIdx3 != -1) {
                     MlmClienteWenow ctePadre3 = lstMlmCliente.get(intIdx3);
                     if (ctePadre3.isBolAfiliado()) {
                        AgregaComision(cte, ctePadre3, dblComision, "ING3");
                     }
                     //----- 4 GENERACION ---
                     int intIdx4 = this.BuscaNodoenArreglo(ctePadre3.getIntSponsor(), lstMlmClienteIdx, false);
                     //Si se encontro el tatarebuelo
                     if (intIdx4 != -1) {
                        MlmClienteWenow ctePadre4 = lstMlmCliente.get(intIdx4);
                        if (ctePadre4.isBolAfiliado()) {
                           AgregaComision(cte, ctePadre4, dblComision, "ING4");
                        }

                     }
                  }
               }
            }
            // </editor-fold>
         } else {

         }
         // <editor-fold defaultstate="collapsed" desc="Recompra">
         if (cte.isBolAfiliado() && cte.isBolRecompra()) {
            log.debug("Recompra de este cliente...." + cte.getIntCliente() + " " + cte.getStrNombre());
            double dblComision1 = 100;
            double dblComision2 = 1000;
            double dblComision3 = 50;
            double dblComision4 = 500;
            //----- 1 GENERACION ---
            int intIdx = this.BuscaNodoenArreglo(cte.getIntSponsor(), lstMlmClienteIdx, false);
            if (intIdx != -1) {
               MlmClienteWenow ctePadre = lstMlmCliente.get(intIdx);
               if (ctePadre.isBolRecompra()) {
                  AgregaComisionAfiliado(cte, ctePadre, dblComision1, "REC1");
               }
               int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntSponsor(), lstMlmClienteIdx, false);
               //----- 2 GENERACION ---
               //Si se encontro el abuelo
               if (intIdx2 != -1) {
                  MlmClienteWenow ctePadre2 = lstMlmCliente.get(intIdx2);
                  if (ctePadre2.isBolRecompra()) {
                     AgregaComisionAfiliado(cte, ctePadre2, dblComision2, "REC2");
                  }
                  //----- 3 GENERACION ---
                  int intIdx3 = this.BuscaNodoenArreglo(ctePadre2.getIntSponsor(), lstMlmClienteIdx, false);
                  //Si se encontro el bisabuelo
                  if (intIdx3 != -1) {
                     MlmClienteWenow ctePadre3 = lstMlmCliente.get(intIdx3);
                     if (ctePadre3.isBolRecompra()) {
                        AgregaComisionAfiliado(cte, ctePadre3, dblComision3, "REC3");
                     }
                     //----- 4 GENERACION ---
                     int intIdx4 = this.BuscaNodoenArreglo(ctePadre3.getIntSponsor(), lstMlmClienteIdx, false);
                     //Si se encontro el tatarebuelo
                     if (intIdx4 != -1) {
                        MlmClienteWenow ctePadre4 = lstMlmCliente.get(intIdx4);
                        if (ctePadre4.isBolRecompra()) {
                           AgregaComisionAfiliado(cte, ctePadre4, dblComision4, "REC4");
                        }

                     }
                  }
               }
            }
         }
         // </editor-fold>
         
         // <editor-fold defaultstate="collapsed" desc="Excedente">
         if(cte.getDblValorDbl8() > 0){
            double dblComisionExcedente = cte.getDblValorDbl8() * 25;
            //----- 1 GENERACION ---
            int intIdx = this.BuscaNodoenArreglo(cte.getIntSponsor(), lstMlmClienteIdx, false);
            if (intIdx != -1) {
               MlmClienteWenow ctePadre = lstMlmCliente.get(intIdx);
               if (ctePadre.isBolRecompra()) {
                  AgregaComisionAfiliado(cte, ctePadre, dblComisionExcedente, "EXC1");
               }
               int intIdx2 = this.BuscaNodoenArreglo(ctePadre.getIntSponsor(), lstMlmClienteIdx, false);
               //----- 2 GENERACION ---
               //Si se encontro el abuelo
               if (intIdx2 != -1) {
                  MlmClienteWenow ctePadre2 = lstMlmCliente.get(intIdx2);
                  if (ctePadre2.isBolRecompra()) {
                     AgregaComisionAfiliado(cte, ctePadre2, dblComisionExcedente, "EXC2");
                  }
               }
            }
         }
         // </editor-fold>
         
         // </editor-fold>
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Vaciado de las comisiones en la base de datos">
      log.info("Inicia Guardado de comisiones.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      for (int i = 0; i < lstMlmCliente.size(); i++) {
         MlmClienteWenow cte = lstMlmCliente.get(i);
         //Validamos que solo los nodos con valores se guarden
//         if (cte.getDblPuntos() != 0) {
         //Realizamos la conversion a la moneda original

         //Actualizamos el importe de comision en clientes
         String strUpdate = "UPDATE vta_cliente SET ";
         strUpdate += " CT_COMISION = " + cte.getDblComision();
         strUpdate += " ,CT_GENERACION1 = " + cte.getIntValor1();
         strUpdate += " ,CT_GENERACION2 = " + cte.getIntValor2();
         strUpdate += " ,CT_GENERACION3 = " + cte.getIntValor3();
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

   /**
    * Detecta los nuevos ingresos activos
    */
   public void detectaNvos() {
      Fechas fecha = new Fechas();
      ActivaBinario activador = new ActivaBinario(this.oConn);
      activador.setIntUplineInicial(10001);
      activador.setIntUplineTemporal(3);

      // <editor-fold defaultstate="collapsed" desc="Detectamos Training">
      String strSqlNvos = "select CT_ID,CT_SPONZOR from vta_cliente WHERE CT_ACTIVO = 1 AND CT_UPLINE = 3 AND WN_TRAINING = 1;";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSqlNvos, true);
         while (rs.next()) {
            int intCte = rs.getInt("CT_ID");
            int intSponzor = rs.getInt("CT_SPONZOR");
            activador.setIntUplineInicial(intSponzor);
            String strRes1 = activador.activarDistribuidor(intCte);

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Detectamos Afiliado">
      activador.setIntUplineInicial(4);
      strSqlNvos = "select CT_ID,CT_SPONZOR from vta_cliente WHERE WN_ACTIVO_AFILIADO = 1  and WN_AFILIADO = 1 AND WN_ID_AFILIADO = 0 AND WN_ID_MASTER = 0;";
      try {
         rs = oConn.runQuery(strSqlNvos, true);
         while (rs.next()) {
            int intCte = rs.getInt("CT_ID");
//            int intNvo = activador.CopiaCliente(intCte, true, oConn);
            int intSponzor = rs.getInt("CT_SPONZOR");
//            String strRes1 = activador.activarDistribuidor(intNvo);
            //Actualizamos el id en el nodo maestro
//            String strUpdate = "update vta_cliente set WN_ID_AFILIADO = " + intNvo + " WHERE CT_ID = " + intCte;
            String strUpdate = "update vta_cliente set WN_ID_AFILIADO = 1 WHERE CT_ID = " + intCte;
            oConn.runQueryLMD(strUpdate);
            //Calculamos bono 1,150 para el sponzor
            MlmComisionActivacion nvoActivacion = new MlmComisionActivacion();
            nvoActivacion.setFieldInt("CI_FUENTE", intCte);
            nvoActivacion.setFieldInt("CI_DESTINO", intSponzor);
            nvoActivacion.setFieldString("CI_FECHA", fecha.getFechaActual());
            nvoActivacion.setFieldDouble("CI_IMPORTE", 1150);
            nvoActivacion.setFieldString("CI_NIVEL", "RAP");
            nvoActivacion.Agrega(oConn);

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Detectamos Global">
      activador.setIntUplineInicial(5);
      strSqlNvos = "select CT_ID from vta_cliente WHERE WN_ACTIVO_GLOBAL = 1  and WN_GLOBAL = 1 AND WN_ID_GLOBAL = 0 AND WN_ID_MASTER = 0;";
      try {
         rs = oConn.runQuery(strSqlNvos, true);
         while (rs.next()) {
            int intCte = rs.getInt("CT_ID");
//            int intNvo = activador.CopiaCliente(intCte, false, oConn);
//            String strRes1 = activador.activarDistribuidor(intNvo);
            //Actualizamos el id en el nodo maestro
//            String strUpdate = "update vta_cliente set WN_ID_GLOBAL = " + intNvo + " WHERE CT_ID = " + intCte;
            String strUpdate = "update vta_cliente set WN_ID_GLOBAL = 1 WHERE CT_ID = " + intCte;
            oConn.runQueryLMD(strUpdate);

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Terminamos de armar la red">
      Redes red = new Redes();
      boolean bolArmo = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
      // </editor-fold>

   }

   private void AgregaComision(MlmClienteWenow cte, MlmClienteWenow ctePadre, double dblComision, String strNivel) {
      ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
      //Entidad para el detalle de las comisiones
      mlm_comision_deta comisDeta = new mlm_comision_deta();
      comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
      comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
      comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
      comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
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
   }

   private void AgregaComisionAfiliado(MlmClienteWenow cte, MlmClienteWenow ctePadre, double dblComision, String strNivel) {
      boolean bolCumplePara25 = false;
      if (ctePadre.getIntValor1() == 2 && ctePadre.getIntValor2() == 4) {
         bolCumplePara25 = true;
      }
      ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
      //Entidad para el detalle de las comisiones
      mlm_comision_deta comisDeta = new mlm_comision_deta();
      comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
      comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
      comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
      comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
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
      //25 % adicional
      if (bolCumplePara25) {
         //BISABUELO
         if (strNivel.equals("REC3")) {
            dblComision = 25;
            ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
            //Entidad para el detalle de las comisiones
            comisDeta = new mlm_comision_deta();
            comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
            comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
            comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
            comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
            comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
            comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
            comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
            comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
            comisDeta.setFieldInt("COMI_ARMADOINI", 1);
            comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
            comisDeta.setFieldInt("COMI_ARMADONUM", 1);
            comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
            comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
            comisDeta.setFieldString("COMI_CODIGO", "RECAD1");
            comisDeta.Agrega(oConn);
         }
         //TATARABUELO
         if (strNivel.equals("REC4")) {
            dblComision = 250;
            ctePadre.setDblComision(ctePadre.getDblComision() + dblComision);
            //Entidad para el detalle de las comisiones
            comisDeta = new mlm_comision_deta();
            comisDeta.setFieldInt("CT_ID", ctePadre.getIntCliente());
            comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
            comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
            comisDeta.setFieldInt("COMI_DESTINO", ctePadre.getIntCliente());
            comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
            comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
            comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
            comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
            comisDeta.setFieldInt("COMI_ARMADOINI", 1);
            comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
            comisDeta.setFieldInt("COMI_ARMADONUM", 1);
            comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
            comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
            comisDeta.setFieldString("COMI_CODIGO", "RECAD2");
            comisDeta.Agrega(oConn);
         }
      }
   }
   // </editor-fold>
}
