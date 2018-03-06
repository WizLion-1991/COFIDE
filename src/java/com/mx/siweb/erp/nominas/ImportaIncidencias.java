/*
 * Esta clase se encarga de importar las incidencias
 */
package com.mx.siweb.erp.nominas;

import Tablas.RhhFaltasDetalle;
import Tablas.RhhIncapacidadesDeta;
import Tablas.RhhIncidencias;
import Tablas.rhh_empleados;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Importaos con un excel las incidencias
 *
 * @author ZeusSIWEB
 */
public class ImportaIncidencias {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ImportaIncidencias.class.getName());
   private String strResultLast;
   private boolean bolLimpiarInfo = false;
   private VariableSession varSesiones;
   private int intIdNomina;
   private SimpleDateFormat format;
   private SimpleDateFormat formatToSave;
   private String strNomNomina;

   public int getIntIdNomina() {
      return intIdNomina;
   }

   public void setIntIdNomina(int intIdNomina) {
      this.intIdNomina = intIdNomina;
   }

   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

   public String getStrResultLast() {
      return strResultLast;
   }

   public void setStrResultLast(String strResultLast) {
      this.strResultLast = strResultLast;
   }

   public boolean isBolLimpiarInfo() {
      return bolLimpiarInfo;
   }

   public void setBolLimpiarInfo(boolean bolLimpiarInfo) {
      this.bolLimpiarInfo = bolLimpiarInfo;
   }

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ImportaIncidencias(Conexion oConn, VariableSession varSesiones) {
      this.oConn = oConn;
      this.strResultLast = "";
      this.bolLimpiarInfo = true;
      this.varSesiones = varSesiones;
      format = new SimpleDateFormat("dd/MM/yyyy");
      formatToSave = new SimpleDateFormat("yyyyMMdd");
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void importaIncidencias(int intIdNomina, String strPathExcel) {
      this.strResultLast = "OK";
      this.intIdNomina = intIdNomina;
      if (validaExcel(strPathExcel)) {
         limpiaInformacion();
         procesaArchivo(strPathExcel);
      } else {
         log.debug("No hice nada");
      }
   }
   // </editor-fold>

   private boolean validaExcel(String strPathExcel) {
      boolean boolValido = true;
      try {
         InputStream myxls = null;
         myxls = new FileInputStream(strPathExcel);
         HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);
         // <editor-fold defaultstate="collapsed" desc="MetodosDeclaramos bandera para identificar errores">
         boolean boolDetalleTiempoExtra;
         boolean boolDetalleFaltasJustificadas;
         boolean boolDetalleFaltasInjustificadas;
         boolean boolDetalleFaltasSancion;
         boolean boolDetalleIncapacidadEnfermedad;
         boolean boolDetalleIncapacidadMaterna;
         boolean boolDetalleIncapacidadAccidenteTrabajo;
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Iteramos por los datos">
         Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
         it.next();
         it.next();
         while (it.hasNext()) {
            Row row = it.next();

            // <editor-fold defaultstate="collapsed" desc="Recuperamos las celdas">
            Cell cellClave = row.getCell(CamposExcelIncidencias.CLAVE.getPosicion());
            Cell celTiempoExtra = row.getCell(CamposExcelIncidencias.HORAS_EXTRAS.getPosicion());
            Cell cellFaltasJustificadas = row.getCell(CamposExcelIncidencias.FALTAS_JUSTIFICADAS.getPosicion());
            Cell cellFaltasInjustificadas = row.getCell(CamposExcelIncidencias.FALTAS_INJUSTIFICADAS.getPosicion());
            Cell cellFaltasSancion = row.getCell(CamposExcelIncidencias.FALTAS_SANCION.getPosicion());
            Cell cellIncapacidadEnfermedad = row.getCell(CamposExcelIncidencias.INCAPACIDAD_ENFERMEDAD.getPosicion());
            Cell cellIncapacidadMaterna = row.getCell(CamposExcelIncidencias.INCAPACIDAD_MATERNA.getPosicion());
            Cell cellIncapacidadAccidenteTrabajo = row.getCell(CamposExcelIncidencias.ACCIDENTE_TRABAJO.getPosicion());
            Cell celDetalleTiempoExtra = row.getCell(CamposExcelIncidencias.DETALLE_HORAS_EXTRAS.getPosicion());
            Cell celDetallelFaltasJustificadas = row.getCell(CamposExcelIncidencias.DETALLE_FALTAS_JUSTIFICADAS.getPosicion());
            Cell cellDetalleFaltasInjustificadas = row.getCell(CamposExcelIncidencias.DETALLE_FALTAS_INJUSTIFICADAS.getPosicion());
            Cell cellDetalleFaltasSancion = row.getCell(CamposExcelIncidencias.DETALLE_FALTAS_SANCION.getPosicion());
            Cell cellDetalleIncapacidadEnfermedad = row.getCell(CamposExcelIncidencias.DETALLE_INCAPACIDAD_ENFERMEDAD.getPosicion());
            Cell cellDetalleIncapacidadMaterna = row.getCell(CamposExcelIncidencias.DETALLE_INCAPACIDAD_MATERNA.getPosicion());
            Cell cellDetalleIncapacidadAccidenteTrabajo = row.getCell(CamposExcelIncidencias.DETALLE_ACCIDENTE_TRABAJO.getPosicion());
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Comenzamos las validaciones">
            if (cellClave != null) {
               // <editor-fold defaultstate="collapsed" desc="Buscamos si la clave existe en la base">
               String strCve;
               if (cellClave.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                  BigDecimal big = new BigDecimal(cellClave.getNumericCellValue());
                  strCve = big.toString();
               } else {
                  strCve = cellClave.getStringCellValue();
               }
               if (!buscaCveEmpleado(strCve)) {
                  log.error("ERROR: La clave no existe en el renglon " + (row.getRowNum() + 1));
                  this.strResultLast = "ERROR: La clave no existe  en el renglon " + (row.getRowNum() + 1);
                  boolValido = false;
                  break;
               } else {
                  // <editor-fold defaultstate="collapsed" desc="Tiempo extra">
                  int intNumTmp = getNumeroCeldaInt(celTiempoExtra);
                  if (intNumTmp != 0) {
                     boolDetalleTiempoExtra = validaDetalleTiempoExtra(this.getStringCeldaStr(celDetalleTiempoExtra), intNumTmp);
                     if (!boolDetalleTiempoExtra) {
                        log.error("ERROR: Faltan fechas para el tiempo extra, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para el tiempo extra, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        break;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Faltas justificadas">
                  intNumTmp = getNumeroCeldaInt(cellFaltasJustificadas);
                  log.debug("cellFaltasJustificadas:" + intNumTmp);
                  if (intNumTmp != 0) {
                     boolDetalleFaltasJustificadas = this.validaFaltasJustificadas(this.getStringCeldaStr(celDetallelFaltasJustificadas), intNumTmp);
                     if (!boolDetalleFaltasJustificadas) {
                        log.error("ERROR: Faltan fechas para las faltas justificadas, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para las faltas justificadas, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        boolValido = false;
                        break;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Faltas injustificadas">
                  intNumTmp = getNumeroCeldaInt(cellFaltasInjustificadas);
                  if (intNumTmp != 0) {
                     boolDetalleFaltasInjustificadas = this.validaFaltasInjustificadas(this.getStringCeldaStr(cellDetalleFaltasInjustificadas), intNumTmp);
                     if (!boolDetalleFaltasInjustificadas) {
                        log.error("ERROR: Faltan fechas para las faltas injustificadas, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para las faltas injustificadas, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        boolValido = false;
                        break;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Faltas sancion">
                  intNumTmp = getNumeroCeldaInt(cellFaltasSancion);
                  if (intNumTmp != 0) {
                     log.debug("Faltas por sancion..." + intNumTmp);
                     boolDetalleFaltasSancion = this.validaFaltasSancion(this.getStringCeldaStr(cellDetalleFaltasSancion), intNumTmp);
                     if (!boolDetalleFaltasSancion) {
                        log.error("ERROR: Faltan fechas para las faltas por sancion, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para las faltas por sancion, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        boolValido = false;
                        break;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Incapacidad por enfermedad">
                  intNumTmp = getNumeroCeldaInt(cellIncapacidadEnfermedad);
                  if (intNumTmp != 0) {
                     boolDetalleIncapacidadEnfermedad = this.validaIncapacidadEnfermedad(this.getStringCeldaStr(cellDetalleIncapacidadEnfermedad), intNumTmp);
                     if (!boolDetalleIncapacidadEnfermedad) {
                        log.error("ERROR: Faltan fechas para la incapacidad por enfermedad, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para la incapacidad por enfermedad, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        boolValido = false;
                        break;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Incapacidad materna">
                  intNumTmp = getNumeroCeldaInt(cellIncapacidadMaterna);
                  if (intNumTmp != 0) {
                     boolDetalleIncapacidadMaterna = this.validaIncapacidadMaterna(this.getStringCeldaStr(cellDetalleIncapacidadMaterna), intNumTmp);
                     if (!boolDetalleIncapacidadMaterna) {
                        log.error("ERROR: Faltan fechas para la incapacidad materna, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para la incapacidad materna, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        boolValido = false;
                        break;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Incapacidad accidente de trabajo">
                  intNumTmp = getNumeroCeldaInt(cellIncapacidadAccidenteTrabajo);
                  if (intNumTmp != 0) {
                     boolDetalleIncapacidadAccidenteTrabajo = this.validaIncapacidadAccidente(this.getStringCeldaStr(cellDetalleIncapacidadAccidenteTrabajo), intNumTmp);
                     if (!boolDetalleIncapacidadAccidenteTrabajo) {
                        log.error("ERROR: Faltan fechas para la incapacidad por accidente de trabajo, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1));
                        this.strResultLast = "ERROR: Faltan fechas para la incapacidad por accidente de trabajo, favor de agregarlas en formato:dd/mm/aaaa separados por comas " + (row.getRowNum() + 1);
                        boolValido = false;
                        break;
                     }
                  }
                  // </editor-fold>
               }
               // </editor-fold>
            } else {
//               log.error("ERROR: La clave esta vacia en el renglon " + (row.getRowNum() + 1));
//               this.strResultLast = "ERROR: La clave esta vacia en el renglon " + (row.getRowNum() + 1);
//               break;
            }
            // </editor-fold>
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Validamos la nómina">
         if (!existeLaNomina()) {
            log.error("Error el id de nomina no existe");
            this.strResultLast = "Error el id de nomina no existe";
            boolValido = false;
         }
         // </editor-fold>

      } catch (IOException ex) {
         log.error("Error(3):" + ex.getMessage());
         this.strResultLast = "ERROR: " + ex.getMessage();
         boolValido = false;
      } catch (Exception ex) {
         log.error("Error(4):" + ex.getMessage());
         this.strResultLast = "ERROR: " + ex.getMessage();
         boolValido = false;
      }
      return boolValido;
   }

   /*Realiza el proceso de guardado del archivo de excel*/
   private void procesaArchivo(String strPathExcel) {
      ArrayList<RhhIncidencias> listado = new ArrayList<RhhIncidencias>();
      try {
         InputStream myxls = null;
         myxls = new FileInputStream(strPathExcel);
         HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);
         Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
         it.next();
         it.next();
         while (it.hasNext()) {
            Row row = it.next();

            // <editor-fold defaultstate="collapsed" desc="Recuperamos las celdas">
            Cell cellClave = row.getCell(CamposExcelIncidencias.CLAVE.getPosicion());
            Cell cellDiasTrabajados = row.getCell(CamposExcelIncidencias.DIAS_TRABAJADOS.getPosicion());
            Cell cellDiasDomingo = row.getCell(CamposExcelIncidencias.DIAS_DOMINGO.getPosicion());
            Cell cellDiasVacaciones = row.getCell(CamposExcelIncidencias.DIAS_DE_VACACIONES.getPosicion());
            Cell cellPrimaVacacional = row.getCell(CamposExcelIncidencias.PRIMA_VACACIONAL.getPosicion());
            Cell celTiempoExtra = row.getCell(CamposExcelIncidencias.HORAS_EXTRAS.getPosicion());
            Cell cellRetroactivo = row.getCell(CamposExcelIncidencias.RETROACTIVO_DIAS.getPosicion());
            Cell cellRetardos = row.getCell(CamposExcelIncidencias.NUMERO_RETARDOS.getPosicion());
            Cell cellCajaAhorro = row.getCell(CamposExcelIncidencias.CAJA_DE_AHORROS.getPosicion());
            Cell cellDiasFestivos = row.getCell(CamposExcelIncidencias.DIA_FESTIVO.getPosicion());
            Cell celBonoCumplimiento = row.getCell(CamposExcelIncidencias.BONO_CUMPLIMIENTO.getPosicion());
            Cell cellFaltasJustificadas = row.getCell(CamposExcelIncidencias.FALTAS_JUSTIFICADAS.getPosicion());
            Cell cellFaltasInjustificadas = row.getCell(CamposExcelIncidencias.FALTAS_INJUSTIFICADAS.getPosicion());
            Cell cellFaltasSancion = row.getCell(CamposExcelIncidencias.FALTAS_SANCION.getPosicion());
            Cell cellIncapacidadEnfermedad = row.getCell(CamposExcelIncidencias.INCAPACIDAD_ENFERMEDAD.getPosicion());
            Cell cellIncapacidadMaterna = row.getCell(CamposExcelIncidencias.INCAPACIDAD_MATERNA.getPosicion());
            Cell cellIncapacidadAccidenteTrabajo = row.getCell(CamposExcelIncidencias.ACCIDENTE_TRABAJO.getPosicion());
            Cell celDetalleTiempoExtra = row.getCell(CamposExcelIncidencias.DETALLE_HORAS_EXTRAS.getPosicion());
            Cell celDetallelFaltasJustificadas = row.getCell(CamposExcelIncidencias.DETALLE_FALTAS_JUSTIFICADAS.getPosicion());
            Cell cellDetalleFaltasInjustificadas = row.getCell(CamposExcelIncidencias.DETALLE_FALTAS_INJUSTIFICADAS.getPosicion());
            Cell cellDetalleFaltasSancion = row.getCell(CamposExcelIncidencias.DETALLE_FALTAS_SANCION.getPosicion());
            Cell cellDetalleIncapacidadEnfermedad = row.getCell(CamposExcelIncidencias.DETALLE_INCAPACIDAD_ENFERMEDAD.getPosicion());
            Cell cellDetalleIncapacidadMaterna = row.getCell(CamposExcelIncidencias.DETALLE_INCAPACIDAD_MATERNA.getPosicion());
            Cell cellDetalleIncapacidadAccidenteTrabajo = row.getCell(CamposExcelIncidencias.DETALLE_ACCIDENTE_TRABAJO.getPosicion());
            Cell cellBonoPuntualidad = row.getCell(CamposExcelIncidencias.BONO_PUNTUALIDAD.getPosicion());
            Cell cellBonoAsistencia = row.getCell(CamposExcelIncidencias.BONO_ASISTENCIA.getPosicion());
            Cell cellTipoDictamen = row.getCell(CamposExcelIncidencias.TIPO_DICTAMEN.getPosicion());
            Cell cellTipoCaso = row.getCell(CamposExcelIncidencias.TIPO_CASO.getPosicion());
            // </editor-fold>
            
             // <editor-fold defaultstate="collapsed" desc="Comenzamos a generar los objetos">
            if (cellClave != null) {
               // <editor-fold defaultstate="collapsed" desc="Obtenemos la clave">
               String strCve;
               if (cellClave.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                  BigDecimal big = new BigDecimal(cellClave.getNumericCellValue());
                  strCve = big.toString();
               } else {
                  strCve = cellClave.getStringCellValue();
               }
               rhh_empleados empleado = getEmpleado( strCve) ;
               // </editor-fold>
               RhhIncidencias incidencia  = new RhhIncidencias();
               incidencia.setBolGetAutonumeric(true);
               incidencia.setFieldInt("RHN_ID", intIdNomina);
               incidencia.setFieldString("RHN_DESCRIPCION", strNomNomina);
               incidencia.setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
               incidencia.setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
               incidencia.setFieldInt("EMP_NUM", empleado.getFieldInt("EMP_NUM"));
               incidencia.setFieldString("EMP_NOMBRE", empleado.getFieldString("EMP_NOMBRE"));
               
               // <editor-fold defaultstate="collapsed" desc="Faltas">
               incidencia.setFieldInt("RHIN_NUM_FALTAS", this.getNumeroCeldaInt(cellFaltasJustificadas));
               incidencia.setFieldInt("RHIN_FALTAS_SANCION", this.getNumeroCeldaInt(cellFaltasSancion));
               incidencia.setFieldInt("RHIN_NUM_FALTAS_INJUSTIFICADAS", this.getNumeroCeldaInt(cellFaltasInjustificadas));
               if(incidencia.getFieldInt("RHIN_NUM_FALTAS")  > 0){
                  generaFaltas(  this.getStringCeldaStr(celDetallelFaltasJustificadas),  1,incidencia.getListFaltas());
               }
               if(incidencia.getFieldInt("RHIN_NUM_FALTAS_INJUSTIFICADAS") > 0){
                  generaFaltas(  this.getStringCeldaStr(cellDetalleFaltasInjustificadas),  2,incidencia.getListFaltas());
               }
               if(incidencia.getFieldInt("RHIN_FALTAS_SANCION")> 0){
                  generaFaltas(  this.getStringCeldaStr(cellDetalleFaltasSancion),  3,incidencia.getListFaltas());
               }
               // </editor-fold>

               incidencia.setFieldInt("RHIN_RETARDOS", this.getNumeroCeldaInt(cellRetardos));
               incidencia.setFieldInt("RHIN_DIAS_RETROACTIVO", this.getNumeroCeldaInt(cellRetroactivo));
               
               // <editor-fold defaultstate="collapsed" desc="Incapacidades">
               incidencia.setFieldInt("RHIN_DIAS_INCAPACIDAD", this.getNumeroCeldaInt(cellIncapacidadEnfermedad)
               +this.getNumeroCeldaInt(cellIncapacidadMaterna) + this.getNumeroCeldaInt(cellIncapacidadAccidenteTrabajo));
               incidencia.setFieldString("RHIN_CERTIFICADO_INCAPACIDAD", "");//??
                if(this.getNumeroCeldaInt(cellIncapacidadAccidenteTrabajo)> 0){
                     generaIncapacidades(this.getStringCeldaStr(cellDetalleIncapacidadEnfermedad), this.getNumeroCeldaInt(cellIncapacidadEnfermedad),incidencia.getListIncapacidades());
                }
                if(this.getNumeroCeldaInt(cellIncapacidadAccidenteTrabajo)> 0){
                     generaIncapacidades(this.getStringCeldaStr(cellDetalleIncapacidadMaterna),  this.getNumeroCeldaInt(cellIncapacidadMaterna),incidencia.getListIncapacidades());
                }
                if(this.getNumeroCeldaInt(cellIncapacidadAccidenteTrabajo)> 0){
                     generaIncapacidades(this.getStringCeldaStr(cellDetalleIncapacidadAccidenteTrabajo),  this.getNumeroCeldaInt(cellIncapacidadAccidenteTrabajo),incidencia.getListIncapacidades());
                }
               // </editor-fold>
               //Prima dominical
               if(this.getNumeroCeldaInt(cellDiasDomingo)> 0){
                  incidencia.setFieldInt("RHIN_APLICA_PRIMA_DOMINICAL", 1);
                  //agregaos las partidas
               }
               //Prima vacacional
               if(this.getNumeroCeldaInt(cellDiasDomingo)> 0){
                  incidencia.setFieldInt("RHIN_APLICA_PRIMA_DOMINICAL", 1);
                  //agregaos las partidas
               }
               /*
      this.Fields.put("RTF_ID", 0);
      this.Fields.put("RTI_ID", 0);
      this.Fields.put("RTR_ID", 0);
      this.Fields.put("RTD_ID", 0);
      this.Fields.put("RTC_ID", 0);
      this.Fields.put("RHIN_APLICA_PREMIO_ASISTENCIA", 0);
      this.Fields.put("RHIN_APLICA_PREMIO_PUNTUALIDAD", 0);
      this.Fields.put("RHIN_APLICA_PREMIO_PRODUCTIVIDAD", 0);
      this.Fields.put("", 0);
      this.Fields.put("RHIN_APLICA_VACACIONES", 0);
      this.Fields.put("RHIN_APLICA_BONOS_GRATIFICACIONES", 0);
      this.Fields.put("RHIN_PREM_ASIS_PORC", 0);
      this.Fields.put("RHIN_PREM_PUNT_PORC", 0);
      this.Fields.put("RHIN_PREM_PROD_PORC", 0);
      this.Fields.put("RHIN_PREM_BONOYGRAT_PORC", 0);
               */
            }
            // </editor-fold>
         }
      } catch (IOException ex) {
         log.error("Error(3):" + ex.getMessage());
         this.strResultLast = "ERROR: " + ex.getMessage();
      } catch (Exception ex) {
         log.error("Error(4):" + ex.getMessage());
         this.strResultLast = "ERROR: " + ex.getMessage();
      }
   }

   private void limpiaInformacion() {
      //Si tiene activa la bandera de limpiar que borre la info
      if (this.bolLimpiarInfo) {

         String strSql = "delete from rhh_incidencias where EMP_ID = " + varSesiones.getIntIdEmpresa()
            + " and RHN_ID = " + intIdNomina;
         oConn.runQueryLMD(strSql);

      }
   }

   private boolean buscaCveEmpleado(String strCve) {
      boolean bolEncontro = false;
      if (!strCve.isEmpty()) {
         String strSql = "select EMP_NUM from rhh_empleados where EMP_CLAVE='" + strCve + "' "
            + " and EMP_ACTIVO = 1  and EMP_ID = " + this.varSesiones.getIntIdEmpresa();
         try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
               bolEncontro = true;
            }
            rs.getStatement().close();
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      return bolEncontro;
   }
   private rhh_empleados getEmpleado(String strCve) {
      rhh_empleados empleado = new rhh_empleados();
      if (!strCve.isEmpty()) {
         String strSql = "select EMP_NUM from rhh_empleados where EMP_CLAVE='" + strCve + "' "
            + " and EMP_ACTIVO = 1  and EMP_ID = " + this.varSesiones.getIntIdEmpresa();
         try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
               int intEmpleado = rs.getInt("EMP_NUM");
               empleado.ObtenDatos(intEmpleado, oConn);
            }
            rs.getStatement().close();
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      return empleado;
   }

   private boolean existeLaNomina() {
      boolean bolEncontro = false;
      String strSql = "select RHN_ID,RHN_DESCRIPCION from rhh_nominas_master where RHN_ID=" + this.intIdNomina + " "
         + " and EMP_ID = " + this.varSesiones.getIntIdEmpresa();
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            bolEncontro = true;
            strNomNomina = rs.getString("RHN_DESCRIPCION");
         }
         rs.getStatement().close();
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return bolEncontro;
   }

   private boolean validaDetalleTiempoExtra(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Horas extras..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }
      return bolValido;
   }

   private boolean validaFaltasJustificadas(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Faltas  justificadas..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }
      return bolValido;
   }

   private boolean validaFaltasInjustificadas(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Faltas  injustificadas..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }
      return bolValido;
   }

   private boolean validaFaltasSancion(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Faltas por sancion..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }
      return bolValido;
   }

   private boolean validaIncapacidadEnfermedad(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Incapacidad enfermedad..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }
      return bolValido;
   }

   private boolean validaIncapacidadMaterna(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Incapacidad materna..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }

      return bolValido;
   }

   private boolean validaIncapacidadAccidente(String strFechasDetalle, int intNumEventos) {
      boolean bolValido = false;
      String[] lstEvt = strFechasDetalle.split(",");
      log.debug("Incapacidad accidente..." + lstEvt.length + " " + intNumEventos);
      if (lstEvt.length == intNumEventos) {
         bolValido = bolValidaFechas(lstEvt);
      }
      return bolValido;
   }

   private int getNumeroCeldaInt(Cell celda) {
      int intNumero = 0;
      if (celda != null) {
         if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            BigDecimal big = new BigDecimal(celda.getNumericCellValue());
            intNumero = big.intValue();
         }
      }
      return intNumero;
   }

   private double getNumeroCeldaDbl(Cell celda) {
      double dblNumero = 0;
      if (celda != null) {
         if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            BigDecimal big = new BigDecimal(celda.getNumericCellValue());
            dblNumero = big.doubleValue();
         }
      }
      return dblNumero;
   }

   private String getStringCeldaStr(Cell celda) {
      String strValor = "";
      if (celda != null) {
         if (celda.getCellType() != Cell.CELL_TYPE_NUMERIC) {
            strValor = celda.getStringCellValue();
         }
      }
      return strValor;
   }

   private boolean bolValidaFechas(String[] lstEvt) {
      boolean bolValido = true;
      for (String strFecha : lstEvt) {
         try {
            log.debug("parse fecha:" + strFecha);
            format.parse(strFecha);
         } catch (ParseException ex) {
            log.error(ex.getMessage());
            bolValido = false;
         }
      }
      return bolValido;
   }
   /**Genera el detalle de la falta*/
   private void generaFaltas(String strFechasDetalle, int intRTF_ID,ArrayList<RhhFaltasDetalle> listFaltas) {
      String[] lstEvt = strFechasDetalle.split(",");
      for (String strFecha : lstEvt) {
         RhhFaltasDetalle detalleFalta = new RhhFaltasDetalle();
         detalleFalta.setFieldInt("RTF_ID", intRTF_ID);
         try {
            Date dateFecha = format.parse(strFecha);
            detalleFalta.setFieldString("RFD_FECHA", formatToSave.format(dateFecha));
            listFaltas.add(detalleFalta);
         } catch (ParseException ex) {
            log.error(ex.getMessage());
         }
      }
   }
   /**Genera el detalle de la falta*/
   private void generaIncapacidades(String strFechasDetalle, int intDias,ArrayList<RhhIncapacidadesDeta> listIncapacidad) {
      String[] lstEvt = strFechasDetalle.split(",");
      for (String strFecha : lstEvt) {
         RhhIncapacidadesDeta detalleIncapacidad = new RhhIncapacidadesDeta();
         detalleIncapacidad.setFieldInt("RID_NUM_DIAS", intDias);
         try {
            Date dateFecha = format.parse(strFecha);
            detalleIncapacidad.setFieldString("RID_FECHA", formatToSave.format(dateFecha));
            listIncapacidad.add(detalleIncapacidad);
         } catch (ParseException ex) {
            log.error(ex.getMessage());
         }
      }
   }
}
