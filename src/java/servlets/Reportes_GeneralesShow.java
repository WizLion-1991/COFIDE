/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.Seguridad;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Reportes.CIP_ReportPDF;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import comSIWeb.Scripting.scriptReport;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.*;

/**
 *Este servlet emite los reportes solicitados
 * @author zeus
 */
public class Reportes_GeneralesShow extends HttpServlet {

   /**
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
         Logger.getLogger(Reportes_GeneralesShow.class.getName()).log(Level.SEVERE, null, ex);
      }
      oConn.open();
      //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
      Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
      if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
         String strTipo = request.getParameter("report");
         if (strTipo == null) {
            strTipo = "HTML";
         }

         //Objeto que emitira el reporte
         CIP_ReportPDF reportPdf = new CIP_ReportPDF("", "");
         reportPdf.setVarSesionesServlet(varSesiones);

         //Inicializamos clase de reportes scripts
         scriptReport sRep = new scriptReport(request, response, reportPdf, oConn, null);
         sRep.setContext(this.getServletContext());
         //Dependiendo del tipo de reporte emitira el reporte
         //Imprimimos un reporte de tipo TXT
         if (strTipo.equals("TXT")) {
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            reportPdf.setOut(out);
            reportPdf.setIntTipoReporte(CIP_ReportPDF.TXT);
            //Creamos el reporte
            sRep.Execute();
            //Emitimos el reporte
            reportPdf.EmiteReportePDF(oConn);
         }
         //Imprimimos un reporte de tipo HTML
         if (strTipo.equals("HTML")) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            reportPdf.setOut(out);
            reportPdf.setIntTipoReporte(CIP_ReportPDF.HTML);
            //Creamos el reporte
            sRep.Execute();
            //Emitimos el reporte
            reportPdf.EmiteReportePDF(oConn);
         }
         //Imprimimos un reporte de tipo XLS
         if (strTipo.equals("XLS")) {
            //Define salida del archivo
            OutputStream out = response.getOutputStream();
            reportPdf.setOutStream(out);
            reportPdf.setIntTipoReporte(CIP_ReportPDF.EXCEL);
            //Creamos el reporte
            sRep.Execute();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-disposition", "attachment; filename=" + reportPdf.strTitulo.replace(" ","_") + ".xls");
            response.setHeader("cache-control", "no-cache");
            //Emitimos el reporte
            reportPdf.EmiteReportePDF(oConn);
         }
         //Imprimimos un reporte de tipo PDF
         if (strTipo.equals("PDF")) {

            //Obtenemos el titulo de los reportes de la aplicacion
            String strTitle = "";
            String strPosX = "";
            strTitle = this.getServletContext().getInitParameter("TitleApp");
            strPosX = this.getServletContext().getInitParameter("PosXTitle");
            if (strTitle == null) {
               strTitle = "";
            }
            reportPdf.setIntTipoReporte(CIP_ReportPDF.PDF);
            //Documento PDF
            Document document = new Document();
            try {
               PdfWriter writer = PdfWriter.getInstance(document,
                       response.getOutputStream()); // Code 2
               //Objeto que dibuja el numero de paginas
               PDFEventPage pdfEvent = new PDFEventPage();
               pdfEvent.setStrTitleApp(strTitle);
               //Colocamos el numero donde comienza X por medio del parametro del web Xml por si necesitamos algun ajuste
               if (strPosX != null) {
                  try {
                     int intPosX = Integer.valueOf(strPosX);
                     pdfEvent.setIntXPageNum(intPosX);
                  } catch (NumberFormatException ex) {
                  }
               }
               //Anexamos el evento
               writer.setPageEvent(pdfEvent);
               document.open();
               //Asignamos el documento el reporte
               reportPdf.setDocument(document);
               reportPdf.setWriter(writer);
               //Creamos el reporte
               sRep.Execute();
               response.setContentType("application/pdf");
               response.setHeader("content-disposition", "attachment; filename=" + reportPdf.strTitulo.replace(" ", "_") + ".pdf");
               response.setHeader("cache-control", "no-cache");
               //Emitimos el reporte
               reportPdf.EmiteReportePDF(oConn);

               document.close();
               writer.close();
            } catch (DocumentException e) {
               e.printStackTrace();
            }
            sRep.setDocument(null);
         }

      }

      oConn.close();
   }

   // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   /**
    * Handles the HTTP <code>GET</code> method.
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
    * Handles the HTTP <code>POST</code> method.
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
    * @return a String containing servlet description
    */
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
}
