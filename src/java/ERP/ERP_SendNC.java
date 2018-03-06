/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.Seguridad;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Formatos.FormateadorMasivo;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Utilerias.Fechas;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *Etse servlet nos ayuda a imprimir el formato de nota de credito
 * @author zeus
 */
public class ERP_SendNC extends HttpServlet {

   /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      try {
         /*Obtenemos las variables de sesion*/
         VariableSession varSesiones = new VariableSession(request);
         varSesiones.getVars();
         //Abrimos la conexion
         Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
         oConn.open();
         String strRes = "OK";//Respuesta de la aplicacion OK es que todo salio correcto
         //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
         Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
         if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
            String id = request.getParameter("id");
            if (id == null) {
               id = "0";
            }
            //Tipo de impresion
            String strOper = request.getParameter("Oper");
            if (strOper == null) {
               strOper = "NC_1";
            }
            if (strOper.equals("")) {
               strOper = "NC_1";
            }
            //Si es solo 1
            if (strOper.equals("NC_1")) {
               //Obtenemos el folio
               String strNumFolio = "";
               int intEMP_TIPOCOMP = 0;
               int intEMP_ID = 0;
               String strNC_NOMFORMATO = "";
               //Recuperamos el numero de folio que queremos imprimir
               String strSql = "select NC_FOLIO,NC_TIPOCOMP,NC_NOMFORMATO,EMP_ID from vta_ncredito where NC_ID = " + id;
               ResultSet rs = oConn.runQuery(strSql, true);
               //Buscamos el nombre del archivo
               while (rs.next()) {
                  strNumFolio = rs.getString("NC_FOLIO");
                  intEMP_TIPOCOMP = rs.getInt("NC_TIPOCOMP");
                  intEMP_ID = rs.getInt("EMP_ID");
                  strNC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
               }
               rs.close();
               //Buscamos si la empresa usa CBB
               int intEMP_USACODBARR = 0;
               int intEMP_CFD_CFDI = 0;
               strSql = "select EMP_USACODBARR,EMP_CFD_CFDI from vta_empresas where EMP_ID = " + intEMP_ID;
               rs = oConn.runQuery(strSql, true);
               //Buscamos el nombre del archivo
               while (rs.next()) {
                  intEMP_USACODBARR = rs.getInt("EMP_USACODBARR");
                  intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
               }
               rs.close();
               ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
               String strNomFormato = mapeo.getStrNomFormato();
               if (intEMP_USACODBARR == 1) {
                  strNomFormato += "_CBB";
               }
               if(intEMP_CFD_CFDI == 1){
                  strNomFormato += "_cfdi";
               }
               //No existe redireccionamos
               String redirectURL = "Formatos?NomForm=" + strNomFormato + "&NC_ID=" + request.getParameter("NC_ID") + "&MASK_FOLIO=" + strNumFolio;
               if (!strNC_NOMFORMATO.equals("")) {
                  redirectURL = "Formatos?NomForm=" + strNC_NOMFORMATO + "&NC_ID=" + request.getParameter("NC_ID") + "&MASK_FOLIO=" + strNumFolio;
               }
               response.sendRedirect(redirectURL);
            }
            //Si es masiva
            if (strOper.equals("NC_N")) {
               Fechas fecha = new Fechas();
               //Path para el documento
               String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";
               /*Definimos parametros de la aplicacion*/
               String strNC_FOLIO1 = request.getParameter("NC_FOLIO1");
               if (strNC_FOLIO1 == null) {
                  strNC_FOLIO1 = "";
               }
               String strNC_FOLIO2 = request.getParameter("NC_FOLIO2");
               if (strNC_FOLIO2 == null) {
                  strNC_FOLIO2 = "";
               }
               String strNC_FECHA1 = request.getParameter("NC_FECHA1");
               if (strNC_FECHA1 == null) {
                  strNC_FECHA1 = "";
               } else {
                  strNC_FECHA1 = fecha.FormateaBD(strNC_FECHA1, "/");
               }
               String strNC_FECHA2 = request.getParameter("NC_FECHA2");
               if (strNC_FECHA2 == null) {
                  strNC_FECHA2 = "";
               } else {
                  strNC_FECHA2 = fecha.FormateaBD(strNC_FECHA2, "/");
               }
               int intEMP_ID = 0;
               int intSC_ID = 0;
               int intTipo_Comp = 0;
               if (request.getParameter("EMP_ID") != null) {
                  try {
                     intEMP_ID = Integer.valueOf(request.getParameter("EMP_ID"));
                  } catch (NumberFormatException ex) {
                     System.out.println("ERROR:" + ex.getMessage());
                  }
               }
               if (request.getParameter("SC_ID") != null) {
                  try {
                     intSC_ID = Integer.valueOf(request.getParameter("SC_ID"));
                  } catch (NumberFormatException ex) {
                     System.out.println("ERROR:" + ex.getMessage());
                  }
               }
               if (request.getParameter("NC_TIPOCOMP") != null) {
                  try {
                     intTipo_Comp = Integer.valueOf(request.getParameter("NC_TIPOCOMP"));
                  } catch (NumberFormatException ex) {
                     System.out.println("ERROR:" + ex.getMessage());
                  }
               }
               //Filtro para el reporte
               String strFiltro = " AND vta_facturas.EMP_ID = " + intEMP_ID;
               if (!strNC_FOLIO1.equals("") && !strNC_FOLIO2.equals("")) {
                  strFiltro += " AND NC_FOLIO>='" + strNC_FOLIO1 + "' AND NC_FOLIO<='" + strNC_FOLIO2 + "' ";
               } else {
                  if (!strNC_FECHA1.equals("") && !strNC_FECHA2.equals("")) {
                     strFiltro += " AND NC_FECHA>='" + strNC_FECHA1 + "' AND NC_FECHA<='" + strNC_FECHA2 + "' ";
                  }
               }
               if (intSC_ID != 0) {
                  strFiltro += " AND vta_facturas.SC_ID = " + intSC_ID;
               }
               //Buscamos si la empresa usa CBB
               int intEMP_USACODBARR = 0;
               String strSql = "select EMP_USACODBARR from vta_empresas where EMP_ID = " + intEMP_ID;
               ResultSet rs = oConn.runQuery(strSql, true);
               //Buscamos el nombre del archivo
               while (rs.next()) {
                  intEMP_USACODBARR = rs.getInt("EMP_USACODBARR");
               }
               rs.close();
               /**Formateador Masivo*/
               FormateadorMasivo format = new FormateadorMasivo();
               format.setIntTypeOut(Formateador.OBJECT);
               format.setStrPath(this.getServletContext().getRealPath("/"));
               //Seleccionamos el tipo de comprobante
               ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intTipo_Comp);
               String strNomFormato = mapeo.getStrNomFormato();
               if (intEMP_USACODBARR == 1) {
                  strNomFormato += "_CBB";
               }
               format.InitFormat(oConn, strNomFormato);
               strRes = format.DoFormat(oConn, strFiltro);
               if (strRes.equals("OK") && format.getIntNumRows() > 0) {
                  //Documento donde guardaremos el formato
                  Document document = new Document();
                  PdfWriter writer = PdfWriter.getInstance(document,
                          response.getOutputStream());
                  document.open();
                  //Definimos parametros para que el cliente sepa que es un PDF
                  response.setContentType("application/pdf");
                  response.setHeader("content-disposition", "attachment; filename=" + format.getStrTitulo().replace(" ", "_") + "Masivo.pdf");
                  response.setHeader("cache-control", "no-cache");
                  CIP_Formato fPDF = new CIP_Formato();
                  fPDF.setDocument(document);
                  fPDF.setWriter(writer);
                  fPDF.setStrPathFonts(strPathFonts);
                  fPDF.EmiteFormatoMasivo(format.getFmXML());
                  document.close();
                  writer.close();
               } else {
                  PrintWriter out = response.getWriter();
                  response.setContentType("text/plain");
                  response.setHeader("content-disposition", "attachment; filename=" + format.getStrTitulo().replace(" ", "_") + ".txt");
                  response.setHeader("cache-control", "no-cache");
                  if (!strRes.equals("OK")) {
                     out.println(strRes);
                  } else {
                     out.println("NO HAY DOCUMENTOS POR IMPRIMIR...");
                  }
               }
            }
         } else {
            System.out.println("No Found....");
         }
         oConn.close();
      } catch (Exception ex) {
         Logger.getLogger(ERP_SendInvoice.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
      }
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
