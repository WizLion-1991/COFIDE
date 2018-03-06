/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Tablas.repo_master;
import Tablas.repo_params;
import comSIWeb.ContextoApt.Seguridad;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/**
 * Servlet que procesa los reportes de jasper reports
 *
 * @author ZeusGalindo
 */
public class JasperReport extends HttpServlet {

   /**
    * Processes requests for both HTTP
    * <code>GET</code> and
    * <code>POST</code> methods.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      /*Obtenemos las variables de sesion*/
      VariableSession varSesiones = new VariableSession(request);
      varSesiones.getVars();
      //Abrimos la conexion
      Conexion oConn = null;
      try {
         oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      oConn.open();
      //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
      Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
      if (varSesiones.getIntNoUser() != 0 /*&& seg.ValidaURL(request)*/) {
         Fechas fecha = new Fechas();
         //Recuperamos los parametros del reporte
         int intIdRepo = 0;
         String strREP_ID = request.getParameter("REP_ID");
         String strboton_1 = request.getParameter("boton_1");
         try {
            intIdRepo = Integer.valueOf(strREP_ID);
         } catch (NumberFormatException ex) {
            System.out.println("Error al recuperar el id del reporte " + strREP_ID);
         }
         //Consultamos datos del reporte
         repo_master rM = new repo_master();
         rM.ObtenDatos(intIdRepo, oConn);
         repo_params r = new repo_params();
         ArrayList<TableMaster> lstParams = r.ObtenDatosVarios(" REP_ID = " + intIdRepo, oConn);
         //inicializamos reporte jasper
         JasperPrint jasperPrint = null;
         try {
//            String reportFileName = "<REPORT_FILE_NAME>";
//            String reportPath = "<REPORT_FILE_PATH>" + reportFileName;
            String strPathBase = this.getServletContext().getRealPath("/");
            String strSeparator = System.getProperty("file.separator");
            String reportFileName = rM.getFieldString("REP_JRXML");
            String reportPath = "";
            if(strPathBase.endsWith(strSeparator)){
                reportPath = strPathBase + "WEB-INF"
                    + strSeparator + "jreports"
                    + strSeparator + reportFileName;
            }else{
                reportPath = strPathBase + strSeparator + "WEB-INF"
                    + strSeparator + "jreports"
                    + strSeparator + reportFileName;
            }


            String targetFileName = null;
            //Generamos el reporte
            final net.sf.jasperreports.engine.JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            //Definimos los parametros del reporte
            Locale locale = new Locale("es", "MX");
            Map parametersMap = new HashMap();
            //Definicion estandard localidad(MX mientras no implementemos algo en el extranjero)
            parametersMap.put(JRParameter.REPORT_LOCALE, locale);
            //Definicion estandard el path de las imagenes de los reportes
            parametersMap.put("PathBase", strPathBase + "WEB-INF"
                    + strSeparator + "jreports"
                    + strSeparator);
            //Definicion estandard el path BASE
            parametersMap.put("PathBaseWeb", strPathBase);
            //Iteramos por los parametros del reporte
            Iterator<TableMaster> it = lstParams.iterator();
            while (it.hasNext()) {
               TableMaster tbn = it.next();
               String strValor = request.getParameter(tbn.getFieldString("REPP_VARIABLE"));
               //Dependiendo del tipo de control
               if (tbn.getFieldString("REPP_TIPO").equals("PanelCheck")) {
                  if (strValor != null) {
                     String[] lstValue = strValor.split(",");
                     parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), lstValue);
                  }
               } else {
                  if (tbn.getFieldString("REPP_DATO").equals("integer") || tbn.getFieldString("REPP_DATO").equals("double")) {
                     int intValor = 0;
                     if (strValor != null) {
                        try {
                           intValor = Integer.valueOf(strValor);
                        } catch (NumberFormatException ex) {
                           System.out.println("Error al recuperar el parametro(" + tbn.getFieldString("REPP_VARIABLE") + ") del reporte " + strValor);
                        }
                     }
                     parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), intValor);
                  } else {
                     if (strValor != null) {
                        if (tbn.getFieldString("REPP_TIPO").equals("date")) {
                           parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), fecha.FormateaBD(strValor, "/"));
                        } else {
                           parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), strValor);
                        }
                     }
                  }

               }
            }
            //Compilamos el reporte
            jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, oConn.getConexion());
            ServletOutputStream outputstream = response.getOutputStream();
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            //Emitimos el reporte en el formato solicitado
            //PDF
            if (strboton_1.equals("PDF")) {
               targetFileName = reportFileName.replace(".jrxml", ".pdf");
               //PDF
               JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
               //Tags para que identifique el browser el tipo de archivo
               response.setContentType("application/pdf");
               //Limpiamos cache y nombre del archivo
               response.setHeader("Cache-Control", "max-age=0");
               response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
               outputstream.write(byteArrayOutputStream.toByteArray());
            }
            //XLS
            if (strboton_1.equals("XLS")) {
               targetFileName = reportFileName.replace(".jrxml", ".xls");
               JRXlsExporter exporterXLS = new JRXlsExporter();
               exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
               exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
               exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
               exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
               exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
               exporterXLS.exportReport();
               //Tags para que identifique el browser el tipo de archivo
               response.setContentType("application/vnd.ms-excel");
               //Limpiamos cache y nombre del archivo
               response.setHeader("Cache-Control", "max-age=0");
               response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
               outputstream.write(byteArrayOutputStream.toByteArray());
            }

            //DOC
            if (strboton_1.equals("DOC")) {
               targetFileName = reportFileName.replace(".jrxml", ".doc");
               JRRtfExporter exporterRTF = new JRRtfExporter();
               exporterRTF.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
               exporterRTF.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
               exporterRTF.exportReport();
               //Tags para que identifique el browser el tipo de archivo
               response.setContentType("application/msword");
               //Limpiamos cache y nombre del archivo
               response.setHeader("Cache-Control", "max-age=0");
               response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
               outputstream.write(byteArrayOutputStream.toByteArray());
            }

            //HTML
            if (strboton_1.equals("HTML")) {
               targetFileName = reportFileName.replace(".jrxml", ".html");

               // Exportamos el informe a HTML  
               final JRHtmlExporter exporter = new JRHtmlExporter();
               exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
               exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
               exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
               exporter.exportReport();

               //Tags para que identifique el browser el tipo de archivo
               response.setContentType("text/html");
               //Limpiamos cache y nombre del archivo
               response.setHeader("Cache-Control", "max-age=0");
               response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
               outputstream.write(byteArrayOutputStream.toByteArray());
            }

            //TXT
            if (strboton_1.equals("TXT")) {
               targetFileName = reportFileName.replace(".jrxml", ".csv");

               // Exportamos el informe a TXT
               final JRCsvExporter exporter = new JRCsvExporter();
               exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, ",");
               exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
               exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
               exporter.exportReport();

               //Tags para que identifique el browser el tipo de archivo
               response.setContentType("text/csv");
               //Limpiamos cache y nombre del archivo
               response.setHeader("Cache-Control", "max-age=0");
               response.setHeader("Content-Disposition", "attachment; filename=" + targetFileName);
               outputstream.write(byteArrayOutputStream.toByteArray());
            }

            // clear the output stream.
            outputstream.flush();
            outputstream.close();


         } catch (final JRException e) {
            e.printStackTrace();
         }
      }
      oConn.close();
   }

   // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   /**
    * Handles the HTTP
    * <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      processRequest(request, response);
   }

   /**
    * Handles the HTTP
    * <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      processRequest(request, response);
   }

   /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
}
