/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Tablas.repo_master;
import Tablas.repo_params;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author ZeusGalindo
 */
@Path("Reportes")
public class Reports {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Reports.class.getName());

   /**
    * Creates a new instance of Reports
    */
   public Reports() {

   }

   @GET
   @Path("/Test")
   @Produces("application/pdf")
   public File getFile() {
      File samplePDF = new File("/Users/ZeusGalindo/Downloads/ECuenta_Banco.pdf");
      return samplePDF;
   }

   @GET
   @Path("/fileis")
   @Produces("application/pdf")
   public InputStream getFileInputStream(
      @DefaultValue("") @QueryParam("Codigo") String strCodigo,
      @DefaultValue("") @QueryParam("Report") String strReport) throws Exception {

      FileInputStream fileIs = new FileInputStream("/Users/ZeusGalindo/Downloads/ECuenta_Banco.pdf");
      return fileIs;
   }

   @GET
   @Path("/JasperReportsPDF")
   @Produces("application/pdf")
   public StreamingOutput getFileStreamingOutput(
      @DefaultValue("") @QueryParam("Code") String strCodigo,
      @DefaultValue("") @QueryParam("ReportId") final String strReportId,
      @DefaultValue("") @QueryParam("ParamNames") final String strReportParamNames,
      @DefaultValue("") @QueryParam("ParamValues") final String strReportParamValues
   ) throws Exception {

      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSesiones = new VariableSession(servletRequest);
      varSesiones.getVars();
      try {
         //Abrimos la conexion
         final Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();

         //Validamos si tenemos acceso
         EvalSesion eval = new EvalSesion();
         if (eval.evaluaSesion(strCodigo, oConn)) {
            return new StreamingOutput() {
               @Override
               public void write(OutputStream outputStream) throws IOException,
                  WebApplicationException {

                  try {

                     // <editor-fold defaultstate="collapsed" desc="Recuperamos los parametros del reporte">
                     System.out.println("Aqui estamos....");
                     Fechas fecha = new Fechas();
                     int intIdRepo = 0;
                     String strREP_ID = strReportId;
                     try {
                        intIdRepo = Integer.valueOf(strREP_ID);
                     } catch (NumberFormatException ex) {
                        System.out.println("Error al recuperar el id del reporte " + strREP_ID);
                     }
                     String lstParamNames[] =  strReportParamNames.split(",", -1); 
                     String lstParamValues[] =  strReportParamValues.split(",", -1); 
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Consultamos datos del reporte">
                     repo_master rM = new repo_master();
                     rM.ObtenDatos(intIdRepo, oConn);
                     repo_params r = new repo_params();
                     ArrayList<TableMaster> lstParams = r.ObtenDatosVarios(" REP_ID = " + intIdRepo, oConn);
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="inicializamos variables para reporte jasper">
                     JasperPrint jasperPrint = null;
                     String strPathBase = servletContext.getRealPath("/");
                     String strSeparator = System.getProperty("file.separator");
                     String reportFileName = rM.getFieldString("REP_JRXML");
                     String reportPath = "";
                     if (strPathBase.endsWith(strSeparator)) {
                        reportPath = strPathBase + "WEB-INF"
                           + strSeparator + "jreports"
                           + strSeparator + reportFileName;
                     } else {
                        reportPath = strPathBase + strSeparator + "WEB-INF"
                           + strSeparator + "jreports"
                           + strSeparator + reportFileName;
                     }
                     String targetFileName = null;
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Compilamos el reporte">
                     final net.sf.jasperreports.engine.JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Definimos los parametros del reporte">
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
                        String strValor = "";//request.getParameter(tbn.getFieldString("REPP_VARIABLE"));
                        //Iteramos por los parametros que se enviaron
                        for(int i=0; i<lstParamNames.length;i++){
                           String strNameParam = lstParamNames[i];
                           if(strNameParam.equals(tbn.getFieldString("REPP_VARIABLE"))){
                              strValor = lstParamValues[i];
                           }
                        }
                        System.out.println("Parametro:" + tbn.getFieldString("REPP_VARIABLE") +  " valor:" + strValor);
                        //Dependiendo del tipo de control
                        if (tbn.getFieldString("REPP_TIPO").equals("PanelCheck")) {
                           if (strValor != null) {
                              String[] lstValue = strValor.split(",");
                              parametersMap.put(tbn.getFieldString("REPP_VARIABLE"), lstValue);
                           }
                        } else if (tbn.getFieldString("REPP_DATO").equals("integer") || tbn.getFieldString("REPP_DATO").equals("double")) {
                           int intValor = 0;
                           if (strValor != null) {
                              try {
                                 intValor = Integer.valueOf(strValor);
                              } catch (NumberFormatException ex) {
                                 System.out.println("Error al recuperar el parametro(" + tbn.getFieldString("REPP_VARIABLE") + ") del reporte " + strValor);
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
                     // <editor-fold defaultstate="collapsed" desc="Poblamos el reporte">
                     jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, oConn.getConexion());
                     // </editor-fold>

                     // <editor-fold defaultstate="collapsed" desc="Exportamos a PDF">
                     final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     //Caso de pdf
                     targetFileName = reportFileName.replace(".jrxml", ".pdf");
                     try {
                        //PDF
                        
                        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
                        //Tags para que identifique el browser el tipo de archivo
                     } catch (JRException ex) {
                        log.error(ex.getMessage());
                     }
                     outputStream.write(byteArrayOutputStream.toByteArray());
                     // </editor-fold>
                     oConn.close();
                  } catch (JRException ex) {
                     log.error(ex.getMessage());
                  }
               }
            };
         } else {
            return null;
         }
//         oConn.close();

      } catch (Exception ex) {
         log.error("Mobil Web Polizas" + ex.getMessage() + " " + ex.getLocalizedMessage());
      } finally {
      }
      return null;
   }

   @GET
   @Path("/Formatos")
   @Produces("application/pdf")
   public StreamingOutput getFormatoStreamingOutput(
      @DefaultValue("") @QueryParam("Codigo") String strCodigo,
      @DefaultValue("") @QueryParam("Tipo") final String strTipo,
      @DefaultValue("") @QueryParam("NomForm") final String strNomForm,
      @DefaultValue("") @QueryParam("Id") final String strId) throws Exception {

      //Accedemos a la base de datos para ver si tenemos acceso
      VariableSession varSesiones = new VariableSession(servletRequest);
      varSesiones.getVars();
      try {
         //Abrimos la conexion
         final Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
         oConn.open();

         //Validamos si tenemos acceso
         EvalSesion eval = new EvalSesion();
         if (eval.evaluaSesion(strCodigo, oConn)) {
            return new StreamingOutput() {
               @Override
               public void write(OutputStream outputStream) throws IOException,
                  WebApplicationException {

                  //Instanciamos el objeto Formateador
                  Formateador format = new Formateador();
                  //Objeto que construye un formato PDF en base al XML
                  CIP_Formato fPDF = new CIP_Formato();

                  //Path para las fuentes personalizadas...
                  String strPathFonts = servletContext.getRealPath("/") + System.getProperty("file.separator") + "fonts";
                  fPDF.setStrPathFonts(strPathFonts);
                  format.InitFormat(oConn, strNomForm);
                  //Recuperamos el ID del campo llave
                  String strLlave = servletRequest.getParameter(format.getStrNomLlave());
                  if (strLlave == null) {
                     strLlave = "0";
                  }
                  String strMASK_FOLIO = "";

                  int intLlave = 0;//Es el id del campo a recuperar
                  try {
                     intLlave = Integer.valueOf(strLlave);
                  } catch (NumberFormatException ex) {
                     log.error(ex.getMessage());
                  }
                  //Posicion inicial para el numero de pagina
                  String strPosX = "";
                  String strTitle = "";
                  strPosX = servletContext.getInitParameter("PosXTitle");
                  strTitle = servletContext.getInitParameter("TitleApp");
                  //Generamos el formato XML
                  format.setIntTypeOut(Formateador.OBJECT);
                  format.setStrPath(servletContext.getRealPath("/"));
                  format.InitParamsSql(servletRequest);//inicializamos parametros adicionales
                  String strRes = format.DoFormat(oConn, intLlave);
                  if (strRes.equals("OK")) {

                     //Documento PDF
                     Document document = new Document();
                     try {
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        PdfWriter writer = PdfWriter.getInstance(document,
                           byteArrayOutputStream);//Instanciamos el objeto write

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
                        //Emite el formato
                        fPDF.setIntTipoReporte(CIP_Formato.PDF);
                        fPDF.EmiteFormato(format.getFmXML());

                        document.close();
                        writer.close();
                        document = null;

                        outputStream.write(byteArrayOutputStream.toByteArray());
                     } catch (DocumentException e) {
                        log.error(e.getMessage());
                     }

                  } else {
                     final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     outputStream.write(byteArrayOutputStream.toByteArray());
                  }

                  oConn.close();
               }
            };
         } else {
            return null;
         }
//         oConn.close();

      } catch (Exception ex) {
         log.error("Mobil Web Formatos" + ex.getMessage() + " " + ex.getLocalizedMessage());
      } finally {
         log.error("Final");
      }
      return null;
   }
}
