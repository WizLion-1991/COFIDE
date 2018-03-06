package ERP;

import Tablas.repo_master;
import Tablas.repo_params;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Operaciones.bitacorausers;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representa un proceso y contiene las utilidades generales de todos los
 * procesos
 *
 * @author zeus
 */
public class ProcesoMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected boolean bolTransaccionalidad = true;
   protected Conexion oConn;
   protected VariableSession varSesiones;
   protected HttpServletRequest request;
   protected UtilXml utilXML;
   protected Bitacora bitacora;
   protected Fechas fecha;
   protected String strResultLast;
   protected boolean bolFolioGlobal = true;
   protected String strPATHBase;
   protected String strNomFileJasper;
   private static final Logger log = LogManager.getLogger(ProcesoMaster.class.getName());

   public String getStrNomFileJasper() {
      return strNomFileJasper;
   }

   public void setStrNomFileJasper(String strNomFileJasper) {
      this.strNomFileJasper = strNomFileJasper;
   }

   /**
    * Nos dice si el proceso iniciara la transaccionalidad o esta sera llamada
    * desde otro proceso
    *
    * @return true/false
    */
   public boolean isBolTransaccionalidad() {
      return bolTransaccionalidad;
   }

   /**
    * Definimos si el proceso iniciara la transaccionalidad o esta sera llamada
    * desde otro proceso
    *
    * @param bolTransaccionalidad true/false
    */
   public void setBolTransaccionalidad(boolean bolTransaccionalidad) {
      this.bolTransaccionalidad = bolTransaccionalidad;
   }

   /**
    * Nos regresa la respuesta del ultimo proceso ejecutado
    *
    * @return Nos regresa una cadena con OK en caso de ser exitoso el ultimo
    * proceso, con ERROR en caso de haber algun error o vacio sino se ejecuto el
    * proceso
    */
   public String getStrResultLast() {
      return strResultLast;
   }

   /**
    * Nos dice si se maneja un folio global
    *
    * @return Nos regresa true/false dependiendo si regresa un folio global
    */
   public boolean isBolFolioGlobal() {
      return bolFolioGlobal;
   }

   /**
    * Definimos si se maneja un folio global
    *
    * @param bolFolioGlobal Enviamos true/false dependiendo si regresa un folio
    * global
    */
   public void setBolFolioGlobal(boolean bolFolioGlobal) {
      this.bolFolioGlobal = bolFolioGlobal;
   }

   /**
    * Regresa el path base del programa
    *
    * @return
    */
   public String getStrPATHBase() {
      return strPATHBase;
   }

   /**
    * Path base del programa
    *
    * @param strPATHBase
    */
   public void setStrPATHBase(String strPATHBase) {
      this.strPATHBase = strPATHBase;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructor">
   /**
    * Inicializa un proceso con los parametros basicos
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion
    */
   public ProcesoMaster(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      this.oConn = oConn;
      this.varSesiones = varSesiones;
      this.request = request;
      this.utilXML = new UtilXml();
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.strResultLast = "";
   }

   /**
    * Inicializa un proceso con los parametros basicos
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    */
   public ProcesoMaster(Conexion oConn, VariableSession varSesiones) {
      this.oConn = oConn;
      this.varSesiones = varSesiones;
      this.utilXML = new UtilXml();
      this.bitacora = new Bitacora();
      this.fecha = new Fechas();
      this.strResultLast = "";
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Guarda la bitacora de usuario del proceso
    *
    * @param strModulo Es el nombre del modulo
    * @param strAction Es la accion a realizar
    * @param intIdOper Es el id de la operacion afectada
    */
   protected void saveBitacora(String strModulo, String strAction, int intIdOper) {
      String strRes = "OK";
      bitacorausers logUser = new bitacorausers();
      logUser.setFieldString("BTU_FECHA", this.fecha.getFechaActual());
      logUser.setFieldString("BTU_HORA", this.fecha.getHoraActual());
      logUser.setFieldString("BTU_NOMMOD", strModulo);
      logUser.setFieldString("BTU_NOMACTION", strAction);
      logUser.setFieldInt("BTU_IDOPER", intIdOper);
      logUser.setFieldInt("BTU_IDUSER", this.varSesiones.getIntNoUser());
      logUser.setFieldString("BTU_NOMUSER", this.varSesiones.getStrUser());
      strRes = logUser.Agrega(oConn);
      if (!strRes.equals("OK")) {
         this.strResultLast = strRes;
      }
   }

   /**
    * Evalua si la fecha es valida respecto a los periodos cerrados
    *
    * @param strFecha Es la fecha por evaluar
    * @param intEMP_ID Es el id de la empresa
    * @param intSC_ID Es el id de la sucursal
    * @return Regresa true si es valido
    */
   protected boolean evaluaFechaCierre(String strFecha, int intEMP_ID, int intSC_ID) {
      CierrePeriodos cierre = new CierrePeriodos(oConn);
      boolean bolValido = cierre.esValido(strFecha, intEMP_ID, intSC_ID);
      return bolValido;
   }

   /**
    * Genera un formato en jaspersoft
    *
    * @param intIdRepo Es el id del reporte en jaspersoft
    * @param strNomFormato Es el nombre o abreviatura del formato
    * @param strTipoFormat Es el tipo de formato PDF XLS HTML DOC TXT
    * @param tbnDocument Es el documento que contiene la información basica
    * @param strParamsName Son los nombres de los parametros del reporte
    * @param strParamsValue Son los valores de los parametros del reporte
    * @param strPATHXml Es el path donde se guardaran temporalmente los pdfs
    * @return Regresa el path del nombre del archivo generado
    */
   public String doGeneraFormatoJasper(int intIdRepo, String strNomFormato, String strTipoFormat, TableMaster tbnDocument,
      String[] strParamsName,
      String[] strParamsValue, String strPATHXml) {
      String strRes = "";

      // <editor-fold defaultstate="collapsed" desc="Si no envian el id buscamos por la abreviatura">
      if (intIdRepo == 0) {
         String strSql = "SELECT REP_ID from repo_master where REP_ABRV = '" + strNomFormato + "'";
         try {
            ResultSet rs2 = oConn.runQuery(strSql, true);
            while (rs2.next()) {
               intIdRepo = rs2.getInt("REP_ID");
            }
            rs2.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
      }
      // </editor-fold>
      //Consultamos datos del reporte
      repo_master rM = new repo_master();
      rM.ObtenDatos(intIdRepo, oConn);
      repo_params r = new repo_params();
      ArrayList<TableMaster> lstParams = r.ObtenDatosVarios(" REP_ID = " + intIdRepo, oConn);
      //inicializamos reporte jasper
      JasperPrint jasperPrint = null;
      try {
         //Declaramos valores para los paths
         String strSeparator = System.getProperty("file.separator");
         String reportFileName = rM.getFieldString("REP_JRXML");
         String reportPath = this.strPATHBase + "WEB-INF"
            + strSeparator + "jreports"
            + strSeparator + reportFileName;
         //Nombre del archivo de salida
         String targetFileName = rM.getFieldString("REP_NOM_FILE");
         //Si viene vacio el nombre usamos el del jasper
         if (this.strNomFileJasper == null) {
            // <editor-fold defaultstate="collapsed" desc="Reemplazamos valores en el nombre del archivo para personalizarlo">
            if (tbnDocument != null) {
               Iterator<String> itJ = tbnDocument.getNomFields().iterator();
               while (itJ.hasNext()) {
                  String strNom = itJ.next();
                  String strValor = tbnDocument.getFieldString(strNom);
                   System.out.println("strValor = " + strValor);
                   System.out.println("strNom = " + strNom);
                  if (targetFileName.contains("%" + strNom + "%")) {
                     targetFileName = targetFileName.replace("%" + strNom + "%", strValor);
                  }
               }
            }
            // </editor-fold>
         } else {
            targetFileName = strNomFileJasper;
         }
          System.out.println("NOMBRE DEL ARCHIVO : " + targetFileName);
         //Anadimos el path
         targetFileName = strPATHXml + targetFileName;
         //Generamos el reporte
         final net.sf.jasperreports.engine.JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
         // <editor-fold defaultstate="collapsed" desc="Definimos los parametros del reporte">
         Locale locale = new Locale("es", "MX");
         Map parametersMap = new HashMap();
         //Definicion estandard localidad(MX mientras no implementemos algo en el extranjero)
         parametersMap.put(JRParameter.REPORT_LOCALE, locale);
         //Definicion estandard el path de las imagenes de los reportes
         parametersMap.put("PathBase", this.strPATHBase + "WEB-INF"
            + strSeparator + "jreports"
            + strSeparator);
         //Definicion estandard el path BASE
         parametersMap.put("PathBaseWeb", this.strPATHBase);
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Iteramos por los parametros del reporte">
         log.debug("Mapeamos las variables...");
         Iterator<TableMaster> it = lstParams.iterator();
         while (it.hasNext()) {
            TableMaster tbn = it.next();
            String strValor = "";//request.getParameter(tbn.getFieldString("REPP_VARIABLE"));
            //Recorremos el arreglo para obtener los valores
            for (int iC1 = 0; iC1 < strParamsName.length; iC1++) {
               if (strParamsName[iC1].equals(tbn.getFieldString("REPP_VARIABLE"))) {
                  strValor = strParamsValue[iC1];
               }
            }
            log.debug(tbn.getFieldString("REPP_VARIABLE") + " " + strValor);

            //Dependiendo del tipo de control
            if (tbn.getFieldString("REPP_TIPO").equals("PanelCheck")) {
               if (strValor != null) {
                  String[] lstValue = strValor.split(",");
                  parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), lstValue);
               }
            } else if (tbn.getFieldString("REPP_DATO").equals("integer") || tbn.getFieldString("REPP_DATO").equals("double")) {
               int intValor = 0;
               if (strValor != null) {
                  if (!strValor.isEmpty()) {
                     try {
                        intValor = Integer.valueOf(strValor);
                     } catch (NumberFormatException ex) {
                        log.error("Error al recuperar el parametro(" + tbn.getFieldString("REPP_VARIABLE") + ") del reporte " + strValor + " " + ex.getMessage());
                     }
                  }
               }
               parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), intValor);
            } else if (strValor != null) {
               if (tbn.getFieldString("REPP_TIPO").equals("date")) {
                  parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), fecha.FormateaBD(strValor, "/"));
               } else {
                  parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), strValor);
               }
            }
         }
         // </editor-fold>
         //Compilamos el reporte
         jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, oConn.getConexion());

         //Emitimos el reporte en el formato solicitado
         // <editor-fold defaultstate="collapsed" desc="PDF">
         if (strTipoFormat.equals("PDF")) {
            //PDF
            if(targetFileName.endsWith(".jrxml")){
                targetFileName = targetFileName.replace(".jrxml", ".pdf");
            }
            JasperExportManager.exportReportToPdfFile(jasperPrint, targetFileName);
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="XLS">
         if (strTipoFormat.equals("XLS")) {
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
               targetFileName);
            exporterXLS.exportReport();
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="DOC">
         if (strTipoFormat.equals("DOC")) {
            JRRtfExporter exporterRTF = new JRRtfExporter();
            exporterRTF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporterRTF.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, targetFileName);
            exporterRTF.exportReport();
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="HTML">
         if (strTipoFormat.equals("HTML")) {

            // Exportamos el informe a HTML  
            final JRHtmlExporter exporter = new JRHtmlExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, targetFileName);
            exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
            exporter.exportReport();
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="TXT">
         if (strTipoFormat.equals("TXT")) {

            // Exportamos el informe a TXT
            final JRCsvExporter exporter = new JRCsvExporter();
            exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, targetFileName);
            exporter.exportReport();
         }
         // </editor-fold>
         strRes = targetFileName;
      } catch (final JRException e) {
         log.error(e.getMessage());
      }
      return strRes;
   }
   // </editor-fold>
}
