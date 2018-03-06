package com.mx.siweb.mlm.compensacion;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase realiza la operacion de calculo sobre periodos
 *
 * @author aleph_79
 */
public class Periodos {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Fechas fecha = null;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Periodos.class.getName());

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Periodos() {
      this.fecha = new Fechas();
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Regresa el id del periodo actual
    *
    * @param oConn Es la conexiona la base de datos
    * @return Regresa el id del periodo actual
    */
   public int getPeriodoActual(Conexion oConn) {
      int intMPE_ID = 0;
      String strFechaHoy = this.fecha.getFechaActual();
      String strSql = "select MPE_ID,MPE_ABRV from mlm_periodos where "
         + "'" + strFechaHoy + "'>= MPE_FECHAINICIAL "
         + "AND '" + strFechaHoy + "'<= MPE_FECHAFINAL";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intMPE_ID = rs.getInt("MPE_ID");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

      return intMPE_ID;
   }

   /**
    * Regresa el nombre del periodo actual
    *
    * @param oConn Es la conexiona la base de datos
    * @return Regresa el nombre del periodo actual
    */
   public String getPeriodoActualNom(Conexion oConn) {
      String strNomPeriodo = "";
      String strFechaHoy = this.fecha.getFechaActual();
      String strSql = "select MPE_ID,MPE_NOMBRE from mlm_periodos where "
         + "'" + strFechaHoy + "'>= MPE_FECHAINICIAL "
         + "AND '" + strFechaHoy + "'<= MPE_FECHAFINAL";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strNomPeriodo = rs.getString("MPE_NOMBRE");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

      return strNomPeriodo;
   }

   /**
    * Calcula las fechas de las semanas por periodo
    *
    * @param oConn Es la conexion
    * @param intAnio Es el anio
    * @param bolLimpiar Indica si limpiamos la tabla de periodos semanal
    * @param primerDiaSemana Es el primero dia del anio
    */
   public  void calculaSemanasAnio(Conexion oConn, int intAnio, boolean bolLimpiar, int primerDiaSemana) {
      //Limpiamos
      if (bolLimpiar) {
         oConn.runQueryLMD("truncate mlm_periodos_semanal;");
      }
      //Inicializamos parametros
      Calendar calendar = Calendar.getInstance();
      calendar.setFirstDayOfWeek(primerDiaSemana);
      Date date = null;
      //Obtenemos la semanas del anio
      int intNumSemanas = calendar.getActualMaximum(Calendar.WEEK_OF_YEAR);
      log.debug("Numero de semanas... " + intNumSemanas);
      //Recorremos todas las semanas para calcularlas
      for (int i = 1; i <= intNumSemanas; i++) {
         calendar.setWeekDate(intAnio, i, 7);
         int numberWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
         log.debug("*    *   *  numberWeekOfYear:" + numberWeekOfYear + " *  *  *");
         date = calendar.getTime();
         SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
         String strFechaInicial = format.format(date);
         log.debug("Fecha: " + strFechaInicial);
         calendar.setWeekDate(intAnio, i, 6);
         date = calendar.getTime();
         String strFechaFinal = format.format(date);
         log.debug("Fecha: " + strFechaFinal);
         //Insertamos el periodo
         String strInsert = "insert into mlm_periodos_semanal(MSE_NOMBRE,MSE_FECHAINICIAL,MSE_FECHAFINAL,MSE_ABRV,MSE_ANIO,MSE_SEMANA)"
            + "values('Semana " + i + " " + intAnio + "','" + strFechaInicial + "','" + strFechaFinal + "','" + intAnio + "" + (i <10? ("0" + i): i) + "'," + intAnio + "," + i + ")";
         //log.debug("strInsert:" + strInsert);
         oConn.runQueryLMD(strInsert);
      }

   }

   /**
    * Regresa el nombre del periodo semana actual
    *
    * @param oConn Es la conexiona la base de datos
    * @return Regresa el nombre del periodo actual
    */
   public String getPeriodoSemanalActualNom(Conexion oConn) {
      String strNomPeriodo = "";
      String strFechaHoy = this.fecha.getFechaActual();
      String strSql = "select MSE_ID,MSE_NOMBRE from mlm_periodos_semanal where "
         + "'" + strFechaHoy + "'>= MSE_FECHAINICIAL "
         + "AND '" + strFechaHoy + "'<= MSE_FECHAFINAL";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strNomPeriodo = rs.getString("MSE_NOMBRE");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

      return strNomPeriodo;
   }

   /**
    * Regresa el id del periodo semanal actual
    *
    * @param oConn Es la conexiona la base de datos
    * @return Regresa el id del periodo actual
    */
   public int getPeriodoSemanalActual(Conexion oConn) {
      int intMPE_ID = 0;
      String strFechaHoy = this.fecha.getFechaActual();
      String strSql = "select MSE_ID,MSE_NOMBRE from mlm_periodos_semanal where "
         + "'" + strFechaHoy + "'>= MSE_FECHAINICIAL "
         + "AND '" + strFechaHoy + "'<= MSE_FECHAFINAL";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intMPE_ID = rs.getInt("MSE_ID");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return intMPE_ID;
   }

   /**
    * Regresa el id del periodo semanal solicitado
    *
    * @param strFecha Es la fecha por calcular
    * @param oConn Es la conexiona la base de datos
    * @return Regresa el id del periodo actual
    */
   public int getPeriodoSemanal(String strFecha, Conexion oConn) {
      int intMPE_ID = 0;
      String strSql = "select MSE_ID,MSE_NOMBRE from mlm_periodos_semanal where "
         + "'" + strFecha + "'>= MSE_FECHAINICIAL "
         + "AND '" + strFecha + "'<= MSE_FECHAFINAL";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intMPE_ID = rs.getInt("MSE_ID");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return intMPE_ID;
   }

   /**
    * Calcula la semana de inscripcion de cada cliente Requiere los campos
    * CT_ID, CT_FECHAREG Y MSE_ID
    *
    * @param oConn
    */
   public void recalculaSemanaClientes(Conexion oConn) {
      try {
         String strSql = "select CT_ID,CT_FECHAREG from vta_cliente order by CT_ID";
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            int intIdCliente = rs.getInt("CT_ID");
            String strFechaReg = rs.getString("CT_FECHAREG");
            if (!strFechaReg.isEmpty()) {
               int intNumSemana = getPeriodoSemanal(strFechaReg, oConn);
               String strUpdate = "update vta_cliente set MSE_ID = " + intNumSemana + " where CT_ID = " + intIdCliente;
               oConn.runQueryLMD(strUpdate);
            }
         }
         //rs.getStatement().close();
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }
   // </editor-fold>
}
