/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import ERP.BusinessEntities.PercepcionesDeduccionesE;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class PercepcionesDeducciones {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PercepcionesDeducciones.class.getName());
   private Conexion oConn;
   private final int emp_id;

   public PercepcionesDeducciones(Conexion oConn, int emp_id) {
      this.oConn = oConn;
      this.emp_id = emp_id;
   }

   public Map ObtenPercepciones() {

      /*Creamos un ArrayList con todos los campos de Percepciones que se encuentran en la pantalla de empresas*/
      ArrayList<String> arrEmpPercepciones = new ArrayList<String>();

      arrEmpPercepciones.add("EMP_PERC_SUE_SAL_RAY_JORN");
      arrEmpPercepciones.add("EMP_PERC_GRAT_ANUAL");
      arrEmpPercepciones.add("EMP_PERC_PTU");
      arrEmpPercepciones.add("EMP_PERC_REEM_MEDI");
      arrEmpPercepciones.add("EMP_PERC_FONDO_AHORRO");
      arrEmpPercepciones.add("EMP_PERC_CAJA_AHORRO");
      arrEmpPercepciones.add("EMP_PERC_VALES");
      arrEmpPercepciones.add("EMP_PERC_AYUDAS");
      arrEmpPercepciones.add("EMP_PERC_CONTR_PATRON");
      arrEmpPercepciones.add("EMP_PERC_VACACIONES");
      arrEmpPercepciones.add("EMP_PERC_PRIMA_SEGURO_VIDA");
      arrEmpPercepciones.add("EMP_PERC_GASTOS_MED_MAYORES");
      arrEmpPercepciones.add("EMP_PERC_COUT_SIND_PATRON");
      arrEmpPercepciones.add("EMP_PERC_SUBSI_INCAPACIDAD");
      arrEmpPercepciones.add("EMP_PERC_BECAS");
      arrEmpPercepciones.add("EMP_PERC_OTROS");
      arrEmpPercepciones.add("EMP_PERC_SUB_EMPLEO");
      arrEmpPercepciones.add("EMP_PERC_FOM_PRIMER_EMPL");
      arrEmpPercepciones.add("EMP_PERC_HORAS_EXTRA");
      arrEmpPercepciones.add("EMP_PERC_PRIMA_DOMINICAL");
      arrEmpPercepciones.add("EMP_PERC_PRIMA_VACACIONAL");
      arrEmpPercepciones.add("EMP_PERC_PRIMA_ANTIGUEDAD");
      arrEmpPercepciones.add("EMP_PERC_PAGO_SEPARACION");
      arrEmpPercepciones.add("EMP_PERC_SEGURO_RETIRO");
      arrEmpPercepciones.add("EMP_PERC_INDEMINIZACIONES");
      arrEmpPercepciones.add("EMP_PERC_REEMB_FUNERAL");
      arrEmpPercepciones.add("EMP_PERC_CUOTA_SEGU_SOCI");
      arrEmpPercepciones.add("EMP_PERC_COMISIONES");
      arrEmpPercepciones.add("EMP_PERC_ANT_UTILIDADES");
      arrEmpPercepciones.add("EMP_PERC_VACACIONES1");
      arrEmpPercepciones.add("EMP_PERC_PREMIO_PUNTUALIDAD");
      arrEmpPercepciones.add("EMP_PERC_PREMIO_ASISTENCIA");
      arrEmpPercepciones.add("EMP_PERC_BONO_GRATIFICACIONES");
      arrEmpPercepciones.add("EMP_PERC_DIA_FESTIVO");
      arrEmpPercepciones.add("EMP_PERC_BONO_CUMPLIMIENTO");
      arrEmpPercepciones.add("EMP_PERC_RETROACTIVO");

      Map<String, PercepcionesDeduccionesE> HashMapPercepciones = new LinkedHashMap<String, PercepcionesDeduccionesE>();

      log.debug("OBTENEMOS EL NUMERO DE CADA PERCEPCION ");
      String strSql = "select * from vta_empresas where emp_id =" + emp_id;

      ResultSet rs1;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {

            /*Obtenemos el valor de cada campo de Percepciones*/
            for (int i = 0; i < arrEmpPercepciones.size(); i++) {

               int intIdPercepcion = rs.getInt(arrEmpPercepciones.get(i));

               strSql = "select * from rhh_percepciones where PERC_ID =" + intIdPercepcion;
               try {
                  rs1 = oConn.runQuery(strSql, true);
                  while (rs1.next()) {
                     String strDescripcion = rs1.getString("PERC_DESCRIPCION");
                     int intIdTP = rs1.getInt("TP_ID");
                     HashMapPercepciones.put(Integer.toString(intIdTP), new PercepcionesDeduccionesE(intIdPercepcion, intIdTP));
                     log.debug(arrEmpPercepciones.get(i) + " , " + Integer.toString(intIdTP) + " , " + intIdPercepcion + " , " + intIdTP);
                  }
                  rs1.close();
               } catch (SQLException ex) {
                  log.error(ex);
               }
            }
         }

         rs.close();

      } catch (SQLException ex) {
         log.error(ex);

      }
      log.debug(" FIN OBTENEMOS EL NUMERO DE CADA PERCEPCION ");
      return HashMapPercepciones;
   }

   public Map ObtenDeducciones() {

      /*Creamos un ArrayList con todos los campos de deducciones que se encuentran en la pantalla de empresas*/
      ArrayList<String> arrEmpDeducciones = new ArrayList<String>();

      arrEmpDeducciones.add("EMP_DEDU_SEGURIDAD_SOC");
      arrEmpDeducciones.add("EMP_DEDU_ISR");
      arrEmpDeducciones.add("EMP_DEDU_APORT_VEJEZ");
      arrEmpDeducciones.add("EMP_DEDU_OTROS");
      arrEmpDeducciones.add("EMP_DEDU_FONDO_VIVIENDA");
      arrEmpDeducciones.add("EMP_DEDU_DESC_INCAPACIDAD");
      arrEmpDeducciones.add("EMP_DEDU_PENS_ALIMENTICIA");
      arrEmpDeducciones.add("EMP_DEDU_RENTA");
      arrEmpDeducciones.add("EMP_DEDU_PREST_VIVIENDA");
      arrEmpDeducciones.add("EMP_DEDU_PAGO_CRED_VIVIENDA");
      arrEmpDeducciones.add("EMP_DEDU_PAGO_INFONACOT");
      arrEmpDeducciones.add("EMP_DEDU_ANT_SALARIOS");
      arrEmpDeducciones.add("EMP_DEDU_PAGO_EXCESO_TRABAJ");
      arrEmpDeducciones.add("EMP_DEDU_ERRORES");
      arrEmpDeducciones.add("EMP_DEDU_PÉRDIDAS");
      arrEmpDeducciones.add("EMP_DEDU_AVERIAS");
      arrEmpDeducciones.add("EMP_DEDU_ADQUISICION_ART_PROD");
      arrEmpDeducciones.add("EMP_DEDU_FOM_CAJA_A");
      arrEmpDeducciones.add("EMP_DEDU_CUOTAS_SINDICALES");
      arrEmpDeducciones.add("EMP_DEDU_AUSENCIA");
      arrEmpDeducciones.add("EMP_DEDU_CUOTAS_OBREROS");
      arrEmpDeducciones.add("EMP_DEDU_OTROS_DESCUENTOS");
      arrEmpDeducciones.add("EMP_DEDU_OTROS_ANTICIPOS");
      arrEmpDeducciones.add("EMP_DEDU_FONDO_AHORRO");

      Map<String, PercepcionesDeduccionesE> HashMapDeducciones = new LinkedHashMap<String, PercepcionesDeduccionesE>();

      log.debug("OBTENEMOS EL NUMERO DE CADA DEDUCCION ");
      String strSql = "select * from vta_empresas where emp_id =" + emp_id;

      ResultSet rs1;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {

            /*Obtenemos el valor de cada campo de Percepciones*/
            for (int i = 0; i < arrEmpDeducciones.size(); i++) {

               int intIdDeduccion = rs.getInt(arrEmpDeducciones.get(i));

               strSql = "select * from rhh_deducciones where DEDU_ID =" + intIdDeduccion;
               try {
                  rs1 = oConn.runQuery(strSql, true);
                  while (rs1.next()) {
                     String strDescripcion = rs1.getString("DEDU_DESCRIPCION");
                     int intIdTD = rs1.getInt("TD_ID");
                     HashMapDeducciones.put(Integer.toString(intIdTD), new PercepcionesDeduccionesE(intIdDeduccion, intIdTD));
                     log.debug(arrEmpDeducciones.get(i) + " , " + Integer.toString(intIdTD) + " , " + intIdDeduccion + " , " + intIdTD);
                  }
                  rs1.close();
               } catch (SQLException ex) {
                  log.error(ex);
               }
            }
         }

         rs.close();

      } catch (SQLException ex) {
         log.error(ex);

      }
      log.debug(" FIN OBTENEMOS EL NUMERO DE CADA DEDUCCION ");
      return HashMapDeducciones;

   }

}
