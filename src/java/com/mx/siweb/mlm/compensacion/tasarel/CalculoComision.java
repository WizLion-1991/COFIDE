package com.mx.siweb.mlm.compensacion.tasarel;

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
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;

/**
 * Clase que se encarga de generar las comisiones para Tasarel
 *
 * @author aleph_79
 */
public class CalculoComision extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.tasarel.CalculoComision.class.getName());
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
         strSQL += "TA_PPGRUPO  =  0,";
         strSQL += "CT_CONTEO_HIJOS  =  0";
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
      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_UPLINE,CT_PPUNTOS,CT_PNEGOCIO"
              + ",TA_PPGRUPO,CT_CONTEO_HIJOS,SC_ID,TA_NIVEL_MAX"
              + ",CT_ARMADOINI "
              + ",CT_ARMADOFIN "
              + ",CT_ARMADONUM "
              + ",CT_ARMADODEEP "
              + ",MPE_ID "
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
            cte.setDblValorDbl1(rs.getDouble("TA_PPGRUPO"));
            cte.setIntTotalHijos(rs.getInt("CT_CONTEO_HIJOS"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            cte.setIntSC_ID(rs.getInt("SC_ID"));
            cte.setIntNivelRedAnterior(rs.getInt("TA_NIVEL_MAX"));
            cte.setIntArmadoIni(rs.getInt("CT_ARMADOINI"));
            cte.setIntArmadoFin(rs.getInt("CT_ARMADOFIN"));
            cte.setIntArmadoNum(rs.getInt("CT_ARMADONUM"));
            cte.setIntArmadoDeep(rs.getInt("CT_ARMADODEEP"));
            cte.setIntPeriodoIngreso(rs.getInt("MPE_ID"));
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
         mlm_cliente cte = lstMlmCliente.get(i);
         //Actualizamos puntos grupales
         cte.setDblGNegocio(cte.getDblGNegocio() + cte.getDblNegocio());
         cte.setDblGPuntos(cte.getDblGPuntos() + cte.getDblPuntos());
         cte.setDblValorDbl1(cte.getDblValorDbl1() + cte.getDblPuntos());
         //Calculamos el nivel de la persona
         int intNivel = 0;
         boolean bolCumplePuntos = false;
         if (cte.getDblPuntos() >= lstParams.get(0).getFieldDouble("CP_PPUNTOS")) {
            //Recorremos la lista de parametros del plan para definir el nivel
            Iterator<TableMaster> it = lstParams.iterator();
            bolCumplePuntos = true;
            while (it.hasNext()) {
               TableMaster tbn = it.next();
               if (cte.getDblGPuntos() >= tbn.getFieldDouble("CP_GPUNTOS")
                       && cte.getDblValorDbl1() >= tbn.getFieldDouble("CP_GPPUNTOS")
                       && cte.getIntTotalHijos() >= tbn.getFieldDouble("CP_HIJOS")) {
                  intNivel = tbn.getFieldInt("CP_NIVEL");
               }
            }
         }
         log.debug("Calculando nivel nodo " + cte.getIntCliente() + " " + cte.getIntNivelRedAnterior() + " " + intNivel);
         //Validamos si el nivel es mayor al anterior
         if (intNivel > cte.getIntNivelRedAnterior()) {
            cte.setIntNivelRed(intNivel);
         }else{
            if(bolCumplePuntos){
               cte.setIntNivelRed(cte.getIntNivelRedAnterior());
            }
         }

         //Guardamos info del nodo

         //Subimos info al padre
         int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
         //Si se encontro el padre
         if (intIdx != -1) {
            mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
            ctePadre.setDblGPuntos(ctePadre.getDblGPuntos() + cte.getDblGPuntos());
            ctePadre.setDblGNegocio(ctePadre.getDblGNegocio() + cte.getDblGNegocio());
            ctePadre.setDblValorDbl1(ctePadre.getDblValorDbl1() + cte.getDblPuntos());
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
         if (cte.getDblValorDbl1() != 0
                 || cte.getDblGPuntos() != 0
                 || cte.getDblGNegocio() != 0) {
            //G_NEGOCIO
            //G_PUNTOS
            //NIVELRED
            //FIRM_ST1_N ... 4
            //FIRM_ST1_P ... 4
            String strUpdate = "UPDATE vta_cliente SET ";
            strUpdate += "TA_PPGRUPO = " + cte.getDblValorDbl1() + ",";
            strUpdate += "CT_NIVELRED = " + cte.getIntNivelRed() + ",";
            strUpdate += "CT_GPUNTOS = " + cte.getDblGPuntos() + ",";
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
         log.info("Termina Guardado de nivel y datos grupales.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      }
      // </editor-fold>


      // <editor-fold defaultstate="collapsed" desc="Calculo de comisiones">
      log.info("Inicia Calculo de Unilevel.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      int intContaProcess = 0;
      int intContaProcessTot = 0;
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
                                 comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                                 comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                 comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                 comisDeta.setFieldInt("COMI_NIVEL", ctePadre5.getIntNivelRed());
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

                           //----- 6 GENERACION ---
                           int intIdx6 = this.BuscaNodoenArreglo(ctePadre5.getIntUpline(), lstMlmClienteIdx, false);
                           //Si se encontro el abuelo
                           if (intIdx6 != -1) {
                              mlm_cliente ctePadre6 = lstMlmCliente.get(intIdx6);
                              //Recorremos la lista de parametros del plan obtener los porcentajes
                              it = lstParams.iterator();
                              while (it.hasNext()) {
                                 TableMaster tbn = it.next();
                                 // <editor-fold defaultstate="collapsed" desc="----- 6 GENERACION ---">
                                 if (ctePadre6.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                         && ctePadre6.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                    double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL6") / 100);
                                    ctePadre6.setDblComision(ctePadre6.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                                    //Entidad para el detalle de las comisiones
                                    mlm_comision_deta comisDeta = new mlm_comision_deta();
                                    comisDeta.setFieldInt("CT_ID", ctePadre6.getIntCliente());
                                    comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                    comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                    comisDeta.setFieldInt("COMI_DESTINO", ctePadre6.getIntCliente());
                                    comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                                    comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                    comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                    comisDeta.setFieldInt("COMI_NIVEL", ctePadre6.getIntNivelRed());
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

                              //----- 7 GENERACION ---
                              int intIdx7 = this.BuscaNodoenArreglo(ctePadre6.getIntUpline(), lstMlmClienteIdx, false);
                              //Si se encontro el abuelo
                              if (intIdx7 != -1) {
                                 mlm_cliente ctePadre7 = lstMlmCliente.get(intIdx7);
                                 //Recorremos la lista de parametros del plan obtener los porcentajes
                                 it = lstParams.iterator();
                                 while (it.hasNext()) {
                                    TableMaster tbn = it.next();
                                    // <editor-fold defaultstate="collapsed" desc="----- 7 GENERACION ---">
                                    if (ctePadre7.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                            && ctePadre7.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                       double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL7") / 100);
                                       ctePadre7.setDblComision(ctePadre7.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                                       //Entidad para el detalle de las comisiones
                                       mlm_comision_deta comisDeta = new mlm_comision_deta();
                                       comisDeta.setFieldInt("CT_ID", ctePadre7.getIntCliente());
                                       comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                       comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                       comisDeta.setFieldInt("COMI_DESTINO", ctePadre7.getIntCliente());
                                       comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                                       comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                       comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                       comisDeta.setFieldInt("COMI_NIVEL", ctePadre7.getIntNivelRed());
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

                                 //----- 8 GENERACION ---
                                 int intIdx8 = this.BuscaNodoenArreglo(ctePadre7.getIntUpline(), lstMlmClienteIdx, false);
                                 //Si se encontro el abuelo
                                 if (intIdx8 != -1) {
                                    mlm_cliente ctePadre8 = lstMlmCliente.get(intIdx8);
                                    //Recorremos la lista de parametros del plan obtener los porcentajes
                                    it = lstParams.iterator();
                                    while (it.hasNext()) {
                                       TableMaster tbn = it.next();
                                       // <editor-fold defaultstate="collapsed" desc="----- 8 GENERACION ---">
                                       if (ctePadre8.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                               && ctePadre8.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                          double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL8") / 100);
                                          ctePadre8.setDblComision(ctePadre8.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                                          //Entidad para el detalle de las comisiones
                                          mlm_comision_deta comisDeta = new mlm_comision_deta();
                                          comisDeta.setFieldInt("CT_ID", ctePadre8.getIntCliente());
                                          comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                          comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                          comisDeta.setFieldInt("COMI_DESTINO", ctePadre8.getIntCliente());
                                          comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                                          comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                          comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                          comisDeta.setFieldInt("COMI_NIVEL", ctePadre8.getIntNivelRed());
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

                                    //----- 9 GENERACION ---
                                    int intIdx9 = this.BuscaNodoenArreglo(ctePadre8.getIntUpline(), lstMlmClienteIdx, false);
                                    //Si se encontro el abuelo
                                    if (intIdx9 != -1) {
                                       mlm_cliente ctePadre9 = lstMlmCliente.get(intIdx9);
                                       //Recorremos la lista de parametros del plan obtener los porcentajes
                                       it = lstParams.iterator();
                                       while (it.hasNext()) {
                                          TableMaster tbn = it.next();
                                          // <editor-fold defaultstate="collapsed" desc="----- 9 GENERACION ---">
                                          if (ctePadre9.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                                  && ctePadre9.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                             double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL9") / 100);
                                             ctePadre9.setDblComision(ctePadre9.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                                             //Entidad para el detalle de las comisiones
                                             mlm_comision_deta comisDeta = new mlm_comision_deta();
                                             comisDeta.setFieldInt("CT_ID", ctePadre9.getIntCliente());
                                             comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                             comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                             comisDeta.setFieldInt("COMI_DESTINO", ctePadre9.getIntCliente());
                                             comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                                             comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                             comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                             comisDeta.setFieldInt("COMI_NIVEL", ctePadre9.getIntNivelRed());
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

                                       //----- 10 GENERACION ---
                                       int intIdx10 = this.BuscaNodoenArreglo(ctePadre9.getIntUpline(), lstMlmClienteIdx, false);
                                       //Si se encontro el abuelo
                                       if (intIdx10 != -1) {
                                          mlm_cliente ctePadre10 = lstMlmCliente.get(intIdx10);
                                          //Recorremos la lista de parametros del plan obtener los porcentajes
                                          it = lstParams.iterator();
                                          while (it.hasNext()) {
                                             TableMaster tbn = it.next();
                                             // <editor-fold defaultstate="collapsed" desc="----- 10 GENERACION ---">
                                             if (ctePadre10.getDblPuntos() >= tbn.getFieldDouble("CP_PPUNTOS")
                                                     && ctePadre10.getIntNivelRed() == tbn.getFieldDouble("CP_NIVEL")) {
                                                double dblComision = dblImporte * (tbn.getFieldDouble("CP_UNILEVEL10") / 100);
                                                ctePadre10.setDblComision(ctePadre10.getDblComision() + dblComision);/*A=REGISTRO(WFUENTE,WPADRE1,ASTEP1,WCOMISION,WIMPORTE,-1,WPAIS,WNOMBRE)*/
                                                //Entidad para el detalle de las comisiones
                                                mlm_comision_deta comisDeta = new mlm_comision_deta();
                                                comisDeta.setFieldInt("CT_ID", ctePadre10.getIntCliente());
                                                comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                                comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                                comisDeta.setFieldInt("COMI_DESTINO", ctePadre10.getIntCliente());
                                                comisDeta.setFieldDouble("COMI_PORCENTAJE", tbn.getFieldDouble("CP_UNILEVEL4"));
                                                comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                                comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporte);
                                                comisDeta.setFieldInt("COMI_NIVEL", ctePadre10.getIntNivelRed());
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
                                       //----- 10 GENERACION ---

                                    }
                                    //----- 9 GENERACION ---

                                 }
                                 //----- 8 GENERACION ---

                              }
                              //----- 7 GENERACION ---

                           }
                           //----- 6 GENERACION ---

                        }
                        //----- 5 GENERACION ---
                     }
                     //----- 4GENERACION ---
                  }
                  //----- 3GENERACION ---
               }
               //----- 2GENERACION ---
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

      // <editor-fold defaultstate="collapsed" desc="Calculamos bonos por nombramiento">
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //Estos bonos unicamente aplican a los calificados
         if (cte.getIntNivelRed() >= 1) {

            // <editor-fold defaultstate="collapsed" desc="Bono por calificar o nombrarse para este nivel">
            if (cte.getIntNivelRed() > cte.getIntNivelRedAnterior()) {


               double dblImporteBono = lstParams.get(cte.getIntNivelRed() - 1).getFieldDouble("CP_BONO_NOMBRARSE");
               if (dblImporteBono > 0) {
                  //Asignamos el bono
                  cte.setDblComision(cte.getDblComision() + dblImporteBono);
                  //Entidad para el detalle de las comisiones
                  mlm_comision_deta comisDeta = new mlm_comision_deta();
                  comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
                  comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                  comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                  comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
                  comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                  comisDeta.setFieldDouble("COMI_IMPORTE", dblImporteBono);
                  comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporteBono);
                  comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                  comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                  comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                  comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                  comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                  comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                  comisDeta.setFieldString("COMI_CODIGO", "BONO_NOM_" + cte.getIntNivelRed());
                  comisDeta.Agrega(oConn);

                  // <editor-fold defaultstate="collapsed" desc="Esta calificando por lo que hay que darle el bono a su padre">
                  //Subimos info al padre
                  int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
                  //Si se encontro el padre
                  if (intIdx != -1) {
                     mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
                     //Solo le damos comisiones al padre si esta calificado
                     if (ctePadre.getIntNivelRed() >= 1) {
                        double dblComision = lstParams.get(ctePadre.getIntNivelRed() - 1).getFieldDouble("CP_BONO_NOMBRAR" + cte.getIntNivelRed());
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
                        comisDeta.setFieldString("COMI_CODIGO", "NOMBRA_" + cte.getIntNivelRed());
                        comisDeta.Agrega(oConn);
                     }

                  }
                  // </editor-fold>
               }


               //Guardamos el dato historico en caso de definitivas
               if (this.isEsCorridaDefinitiva()) {
                  String strUpdate = "UPDATE vta_cliente SET ";
                  strUpdate += "TA_NIVEL_MAX = " + cte.getIntNivelRed() + " ";
                  strUpdate += "WHERE CT_ID = " + cte.getIntCliente();
                  this.oConn.runQueryLMD(strUpdate);
               }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Bonos por apoyo tiempo aire">
            System.out.println("Apoyo economico " + cte.getIntNivelRed() + " " + lstParams.get(cte.getIntNivelRed() - 1).getFieldDouble("CP_APOYO_ECONOMICO"));
            double dblImporteBonoTiempoAire = lstParams.get(cte.getIntNivelRed() - 1).getFieldDouble("CP_APOYO_ECONOMICO");
            if (dblImporteBonoTiempoAire > 0) {
               //Asignamos el bono
               cte.setDblComision(cte.getDblComision() + dblImporteBonoTiempoAire);
               //Entidad para el detalle de las comisiones
               mlm_comision_deta comisDeta = new mlm_comision_deta();
               comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
               comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
               comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
               comisDeta.setFieldInt("COMI_DESTINO", cte.getIntCliente());
               comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
               comisDeta.setFieldDouble("COMI_IMPORTE", dblImporteBonoTiempoAire);
               comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblImporteBonoTiempoAire);
               comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
               comisDeta.setFieldInt("COMI_ARMADOINI", 1);
               comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
               comisDeta.setFieldInt("COMI_ARMADONUM", 1);
               comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
               comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
               comisDeta.setFieldString("COMI_CODIGO", "BONO_T_AIRE_" + cte.getIntNivelRed());
               comisDeta.Agrega(oConn);
            }
            // </editor-fold>
         }
         //Recorremos los parametros de comisiones
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Calculamos bonos por ingreso">
      for (int i = lstMlmCliente.size() - 1; i >= 0; i--) {
         mlm_cliente cte = lstMlmCliente.get(i);
         //si el ingreso es del mismo periodo
         if (cte.getIntPeriodoIngreso() == this.intPeriodo) {
            //Otorgamos bono al padre aunque no tenga nivel
            String strTipoKit = "";
            //Buscamos el tipo de kit de ingreso
            String strSqlK = "select vta_cliente.CT_ID,PR_ESKITINC,PR_DESCRIPCION from vta_cliente,vta_pedidos,vta_pedidosdeta, vta_producto\n"
                    + " WHERE vta_cliente.CT_ID =  vta_pedidos.CT_ID AND  vta_pedidos.PD_ID = Vta_pedidosdeta.PD_ID \n"
                    + "and vta_producto.PR_ID = Vta_pedidosdeta.PR_ID and PR_ESKITINC = 1\n"
                    + " and pd_anulada = 0 and vta_cliente.CT_ID = " + cte.getIntCliente();
            try {
               ResultSet rs = this.oConn.runQuery(strSqlK, true);
               while (rs.next()) {
                  strTipoKit = rs.getString("PR_DESCRIPCION");
               }
                //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage() + " " + ex.getSQLState());
            }
            //Solamente los kits generan comision
            if (strTipoKit.equals("BASICO")
                    || strTipoKit.equals("EJECUTIVO")
                    || strTipoKit.equals("EMPRESARIAL")) {
               // <editor-fold defaultstate="collapsed" desc="Subimos comision al nivel 1">
               int intIdx = this.BuscaNodoenArreglo(cte.getIntUpline(), lstMlmClienteIdx, false);
               //Si se encontro el padre
               if (intIdx != -1) {

                  mlm_cliente ctePadre = lstMlmCliente.get(intIdx);
                  double dblComision = 0;
                  if (strTipoKit.equals("BASICO")) {
                     dblComision = 100;
                  }
                  if (strTipoKit.equals("EJECUTIVO")) {
                     dblComision = 300;
                  }
                  if (strTipoKit.equals("EMPRESARIAL")) {
                     dblComision = 800;
                  }
                  //Solo si aplica la comision
                  if (dblComision > 0) {
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
                     comisDeta.setFieldString("COMI_CODIGO", "ING_1");
                     comisDeta.Agrega(oConn);

                     // <editor-fold defaultstate="collapsed" desc="Subimos comision al nivel 2">
                     intIdx = this.BuscaNodoenArreglo(ctePadre.getIntUpline(), lstMlmClienteIdx, false);
                     //Si se encontro el abuelo
                     if (intIdx != -1) {

                        mlm_cliente ctePadre2 = lstMlmCliente.get(intIdx);
                        dblComision = 0;
                        if (strTipoKit.equals("EJECUTIVO")) {
                           dblComision = 100;
                        }
                        if (strTipoKit.equals("EMPRESARIAL")) {
                           dblComision = 100;
                        }
                        //Solo si aplica la comision
                        if (dblComision > 0) {
                           ctePadre2.setDblComision(ctePadre2.getDblComision() + dblComision);
                           //Entidad para el detalle de las comisiones
                           comisDeta = new mlm_comision_deta();
                           comisDeta.setFieldInt("CT_ID", ctePadre2.getIntCliente());
                           comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                           comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                           comisDeta.setFieldInt("COMI_DESTINO", ctePadre2.getIntCliente());
                           comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                           comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                           comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                           comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                           comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                           comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                           comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                           comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                           comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                           comisDeta.setFieldString("COMI_CODIGO", "ING_2");
                           comisDeta.Agrega(oConn);

                           // <editor-fold defaultstate="collapsed" desc="Subimos comision al nivel 3">
                           intIdx = this.BuscaNodoenArreglo(ctePadre2.getIntUpline(), lstMlmClienteIdx, false);
                           //Si se encontro el bisa-abuelo
                           if (intIdx != -1) {

                              mlm_cliente ctePadre3 = lstMlmCliente.get(intIdx);
                              dblComision = 0;
                              if (strTipoKit.equals("EMPRESARIAL")) {
                                 dblComision = 50;
                              }

                              //Solo si aplica la comision
                              if (dblComision > 0) {
                                 ctePadre3.setDblComision(ctePadre3.getDblComision() + dblComision);
                                 //Entidad para el detalle de las comisiones
                                 comisDeta = new mlm_comision_deta();
                                 comisDeta.setFieldInt("CT_ID", ctePadre3.getIntCliente());
                                 comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                 comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                 comisDeta.setFieldInt("COMI_DESTINO", ctePadre3.getIntCliente());
                                 comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                                 comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                 comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                                 comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                                 comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                                 comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                                 comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                                 comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                                 comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                                 comisDeta.setFieldString("COMI_CODIGO", "ING_3");
                                 comisDeta.Agrega(oConn);

                                 // <editor-fold defaultstate="collapsed" desc="Subimos comision al nivel 4">
                                 intIdx = this.BuscaNodoenArreglo(ctePadre3.getIntUpline(), lstMlmClienteIdx, false);
                                 //Si se encontro el bisa-abuelo
                                 if (intIdx != -1) {

                                    mlm_cliente ctePadre4 = lstMlmCliente.get(intIdx);
                                    dblComision = 0;
                                    if (strTipoKit.equals("EMPRESARIAL")) {
                                       dblComision = 25;
                                    }
                                    //Solo si aplica la comision
                                    if (dblComision > 0) {
                                       ctePadre4.setDblComision(ctePadre4.getDblComision() + dblComision);
                                       //Entidad para el detalle de las comisiones
                                       comisDeta = new mlm_comision_deta();
                                       comisDeta.setFieldInt("CT_ID", ctePadre4.getIntCliente());
                                       comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                       comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                       comisDeta.setFieldInt("COMI_DESTINO", ctePadre4.getIntCliente());
                                       comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                                       comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                       comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                                       comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                                       comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                                       comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                                       comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                                       comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                                       comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                                       comisDeta.setFieldString("COMI_CODIGO", "ING_4");
                                       comisDeta.Agrega(oConn);

                                       // <editor-fold defaultstate="collapsed" desc="Subimos comision al nivel 5">
                                       intIdx = this.BuscaNodoenArreglo(ctePadre4.getIntUpline(), lstMlmClienteIdx, false);
                                       //Si se encontro el bisa-abuelo
                                       if (intIdx != -1) {

                                          mlm_cliente ctePadre5 = lstMlmCliente.get(intIdx);
                                          dblComision = 0;
                                          if (strTipoKit.equals("EMPRESARIAL")) {
                                             dblComision = 25;
                                          }
                                          
                                          // <editor-fold defaultstate="collapsed" desc="Generamos bono solo si el aplica con el tipo de kit">
                                          if (dblComision > 0) {
                                             ctePadre5.setDblComision(ctePadre5.getDblComision() + dblComision);
                                             //Entidad para el detalle de las comisiones
                                             comisDeta = new mlm_comision_deta();
                                             comisDeta.setFieldInt("CT_ID", ctePadre5.getIntCliente());
                                             comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
                                             comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
                                             comisDeta.setFieldInt("COMI_DESTINO", ctePadre5.getIntCliente());
                                             comisDeta.setFieldDouble("COMI_PORCENTAJE", 100);
                                             comisDeta.setFieldDouble("COMI_IMPORTE", dblComision);
                                             comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", dblComision);
                                             comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
                                             comisDeta.setFieldInt("COMI_ARMADOINI", 1);
                                             comisDeta.setFieldInt("COMI_ARMADOFIN", 1);
                                             comisDeta.setFieldInt("COMI_ARMADONUM", 1);
                                             comisDeta.setFieldInt("COMI_ARMADODEEP", 1);
                                             comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
                                             comisDeta.setFieldString("COMI_CODIGO", "ING_5");
                                             comisDeta.Agrega(oConn);
                                          }
                                          // </editor-fold>

                                       }
                                       // </editor-fold>
                                    }

                                 }
                                 // </editor-fold>
                              }

                           }
                           // </editor-fold>
                        }

                     }
                     // </editor-fold>
                  }


               }
               // </editor-fold>
            }

         }
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
         //Guardamos la estructura de la red
         if (this.isEsCorridaDefinitiva()) {
            mlm_comision_deta comisDeta = new mlm_comision_deta();
            comisDeta.setFieldInt("CT_ID", cte.getIntCliente());
            comisDeta.setFieldInt("MPE_ID", this.intPeriodo);
            comisDeta.setFieldInt("COMI_FUENTE", cte.getIntCliente());
            comisDeta.setFieldInt("COMI_DESTINO", 0);
            comisDeta.setFieldInt("COMI_UPLINE", cte.getIntUpline());
            comisDeta.setFieldDouble("COMI_PORCENTAJE", 0);
            comisDeta.setFieldDouble("COMI_IMPORTE", 0);
            comisDeta.setFieldDouble("COMI_IMPORTE_ORIGEN", 0);
            comisDeta.setFieldInt("COMI_NIVEL", cte.getIntNivelRed());
            comisDeta.setFieldInt("COMI_ARMADOINI", cte.getIntArmadoIni());
            comisDeta.setFieldInt("COMI_ARMADOFIN", cte.getIntArmadoFin());
            comisDeta.setFieldInt("COMI_ARMADONUM", cte.getIntArmadoNum());
            comisDeta.setFieldInt("COMI_ARMADODEEP", cte.getIntArmadoDeep());
            comisDeta.setFieldInt("SC_ID", cte.getIntSC_ID());
            comisDeta.setFieldString("COMI_CODIGO", "EST_" + cte.getIntNivelRed());
            comisDeta.Agrega(oConn);
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
   // </editor-fold>
}
