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
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Reportes.CIP_ReportPDF;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Este servlet despacha los formatos generados por el usuario
 *
 * @author zeus
 */
public class Formatos extends HttpServlet {

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
         String strTipo = request.getParameter("report");
         if (strTipo == null) {
            strTipo = "PDF";
         }
         //Obtenemos el nombre del Formato
         String strNomForm = request.getParameter("NomForm");
         if (strNomForm == null) {
            strNomForm = "";
         }
         //Instanciamos el objeto Formateador
         Formateador format = new Formateador();
         //Objeto que construye un formato PDF en base al XML
         CIP_Formato fPDF = new CIP_Formato();

         //Path para las fuentes personalizadas...
         String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";
         fPDF.setStrPathFonts(strPathFonts);
         format.InitFormat(oConn, strNomForm);
         //Recuperamos el ID del campo llave
         String strLlave = request.getParameter(format.getStrNomLlave());
         if (strLlave == null) {
            strLlave = "0";
         }
         String strMASK_FOLIO = request.getParameter("MASK_FOLIO");
         if (strMASK_FOLIO == null) {
            strMASK_FOLIO = "";
         }
         int intLlave = 0;//Es el id del campo a recuperar
         try {
            intLlave = Integer.valueOf(strLlave);
         } catch (NumberFormatException ex) {
            ex.printStackTrace();
         }
         //Posicion inicial para el numero de pagina
         String strPosX = "";
         String strTitle = "";
         strPosX = this.getServletContext().getInitParameter("PosXTitle");
         strTitle = this.getServletContext().getInitParameter("TitleApp");
         //Generamos el formato XML
         format.setIntTypeOut(Formateador.OBJECT);
         format.setStrPath(this.getServletContext().getRealPath("/"));
         format.InitParamsSql(request);//inicializamos parametros adicionales
         String strRes = format.DoFormat(oConn, intLlave);
         if (strRes.equals("OK")) {
            //Dependiendo del tipo de reporte emitira el reporte
            //Imprimimos un reporte de tipo TXT
            if (strTipo.equals("TXT")) {
               response.setContentType("text/plain");
               PrintWriter out = response.getWriter();
               fPDF.setOut(out);
               fPDF.setIntTipoReporte(CIP_ReportPDF.TXT);

            }
            //Imprimimos un reporte de tipo HTML
            if (strTipo.equals("HTML")) {
               response.setContentType("text/html;charset=UTF-8");
               PrintWriter out = response.getWriter();
               fPDF.setOut(out);
               fPDF.setIntTipoReporte(CIP_ReportPDF.HTML);
               //Creamos el reporte
               //Emitimos el reporte
               //fPDF.EmiteFormato(format);
            }
            //Imprimimos un reporte de tipo XLS
            if (strTipo.equals("XLS")) {
               PrintWriter out = response.getWriter();
               fPDF.setOut(out);
               fPDF.setIntTipoReporte(CIP_ReportPDF.EXCEL);
               response.setContentType("application/vnd.ms-excel");
               if (!strMASK_FOLIO.equals("")) {
                  response.setHeader("content-disposition", "attachment; filename=" + fPDF.strTitulo.replace(" ", "_") + strMASK_FOLIO + ".xls");
               } else {
                  response.setHeader("content-disposition", "attachment; filename=" + fPDF.strTitulo.replace(" ", "_") + strLlave + ".xls");
               }
               response.setHeader("cache-control", "no-cache");
               //Creamos el reporte
               //Emitimos el reporte
               //fPDF.EmiteFormato(format);
            }
            //Imprimimos un reporte de tipo PDF
            if (strTipo.equals("PDF")) {
               //Documento PDF
               Document document = new Document();
               try {
                  PdfWriter writer = PdfWriter.getInstance(document,
                          response.getOutputStream());//Instanciamos el objeto write
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
                  } else {
                     pdfEvent.setIntXPageNum(300);
                     pdfEvent.setIntXPageNumRight(50);
                     pdfEvent.setIntXPageTemplate(252.3f);
                  }
                  //Anexamos el evento
                  writer.setPageEvent(pdfEvent);

                  document.open();
                  //Asignamos los objeto al formato
                  fPDF.setDocument(document);
                  fPDF.setWriter(writer);
                  response.setContentType("application/pdf");
                  if (!strMASK_FOLIO.equals("")) {
                     response.setHeader("content-disposition", "attachment; filename=" + format.getStrTitulo().replace(" ", "_") + strMASK_FOLIO + ".pdf");
                  } else {
                     response.setHeader("content-disposition", "attachment; filename=" + format.getStrTitulo().replace(" ", "_") + strLlave + ".pdf");
                  }
                  response.setHeader("cache-control", "no-cache");
                  //Emite el formato
                  fPDF.EmiteFormato(format.getFmXML());

                  document.close();
                  writer.close();
                  document = null;
               } catch (DocumentException e) {
                  e.printStackTrace();
               }

            }
         } else {
            PrintWriter out = response.getWriter();
            out.println("Error al generar el formato " + strRes);
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
