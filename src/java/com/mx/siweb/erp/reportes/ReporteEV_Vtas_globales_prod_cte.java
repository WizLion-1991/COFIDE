/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.EV_Vtas_globales_prod_cte;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author siweb
 */
public class ReporteEV_Vtas_globales_prod_cte {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String sourceFileName;
   private String targetFileName;
   private int intMoneda;
   private int intConvertido;
   private int intEmpId;
   private int intScId;
   private String strFechaIni;
   private String strFechaFin;
   private int intCategoria;
   private Conexion oConn;
   private ArrayList<EV_Vtas_globales_prod_cte> lstCteCategoria = null;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteEV_Vtas_globales_prod_cte.class.getName());
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructor">
   public ReporteEV_Vtas_globales_prod_cte() {
      lstCteCategoria = new ArrayList<EV_Vtas_globales_prod_cte>();
   }
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void Consume_EV(int intMoneda, int intConvertido, int intEmpresa, int intSucursal, String strFechaIni, String strFechaFin, int intCategoria) throws SQLException {
      CallableStatement cStmt;
      try {
         cStmt = this.oConn.getConexion().prepareCall("{call sp_getVentas_globales_prod_cte(?,?,?,?,?,?,?)}");
         cStmt.setInt(1, intMoneda);
         cStmt.setInt(2, intConvertido);
         cStmt.setInt(3, intEmpresa);
         cStmt.setInt(4, intSucursal);
         cStmt.setString(5, strFechaIni);
         cStmt.setString(6, strFechaFin);
         cStmt.setInt(7, intCategoria);

         boolean hadResults = cStmt.execute();
         while (hadResults) {
            // process result set
            ResultSet rs = cStmt.getResultSet();
            // process result set
            while (rs.next()) {
               String strCalif = "";
               EV_Vtas_globales_prod_cte newCte = new EV_Vtas_globales_prod_cte();
               newCte.setIntCT_IT(rs.getInt("CT_ID"));
               newCte.setStrRazonSocial(rs.getString("CT_RAZONSOCIAL"));
               if (rs.getString("CLASIFICACION") != null) {
                  newCte.setStrClasif(rs.getString("CLASIFICACION"));
               } else {
                  newCte.setStrClasif(strCalif);
               }
               newCte.setDblTSubtotal(rs.getDouble("TSUBTOTAL"));
               newCte.setDblTSubtotal_Porc(rs.getDouble("TSUBTOTAL_PORC"));
               newCte.setDblCantidad(rs.getDouble("CANTIDAD"));
               this.lstCteCategoria.add(newCte);
            }
            rs.close();
            hadResults = cStmt.getMoreResults();
         }
      }//Fin try
      catch (SQLException ex) {
         java.util.logging.Logger.getLogger(ReporteEV_Vtas_globales_prod_cte.class.getName()).log(Level.SEVERE, null, ex);
      }
   }//Fin consume EV

   public String generaXML() {
      Iterator<EV_Vtas_globales_prod_cte> itCte = this.lstCteCategoria.iterator();
      UtilXml util = new UtilXml();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      strXML.append("<VentaMayor>");
      while (itCte.hasNext()) {
         EV_Vtas_globales_prod_cte venta = itCte.next();
         strXML.append("<Detalle");
         strXML.append(" CT_ID= \"").append(venta.getIntCT_IT()).append("\" ");
         strXML.append(" CT_RAZONSOCIAL= \"").append(util.Sustituye(venta.getStrRazonSocial())).append("\" ");
         strXML.append(" CATEGORIA= \"").append(util.Sustituye(venta.getStrClasif())).append("\" ");
         strXML.append(" IMPORTE= \"").append(venta.getDblTSubtotal()).append("\" ");
         strXML.append(" PORCENTAJE= \"").append(venta.getDblTSubtotal_Porc()).append("\" ");
         strXML.append(" CANTIDAD= \"").append(venta.getDblCantidad()).append("\" ");
         strXML.append("/>");
      }
      strXML.append("</VentaMayor>");
      return strXML.toString();
   }//Fin generaXML

   public void setSourceFileName(String sourceFileName) {
      this.sourceFileName = sourceFileName;
   }

   public void setTargetFileName(String targetFileName) {
      this.targetFileName = targetFileName;
   }

   public void setIntMoneda(int intMoneda) {
      this.intMoneda = intMoneda;
   }

   public void setIntConvertido(int intConvertido) {
      this.intConvertido = intConvertido;
   }

   public void setIntEmpId(int intEmpId) {
      this.intEmpId = intEmpId;
   }

   public void setIntScId(int intScId) {
      this.intScId = intScId;
   }

   public void setStrFechaIni(String strFechaIni) {
      this.strFechaIni = strFechaIni;
   }

   public void setStrFechaFin(String strFechaFin) {
      this.strFechaFin = strFechaFin;
   }

   public void setIntCategoria(int intCategoria) {
      this.intCategoria = intCategoria;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public void setLstCteCategoria(ArrayList<EV_Vtas_globales_prod_cte> lstCteCategoria) {
      this.lstCteCategoria = lstCteCategoria;
   }

   public String getSourceFileName() {
      return sourceFileName;
   }

   public String getTargetFileName() {
      return targetFileName;
   }

   public int getIntMoneda() {
      return intMoneda;
   }

   public int getIntConvertido() {
      return intConvertido;
   }

   public int getIntEmpId() {
      return intEmpId;
   }

   public int getIntScId() {
      return intScId;
   }

   public String getStrFechaIni() {
      return strFechaIni;
   }

   public String getStrFechaFin() {
      return strFechaFin;
   }

   public int getIntCategoria() {
      return intCategoria;
   }

   public Conexion getoConn() {
      return oConn;
   }

   public ArrayList<EV_Vtas_globales_prod_cte> getLstCteCategoria() {
      return lstCteCategoria;
   }

   public static Logger getLog() {
      return log;
   }

   public void ReportaVtas_globales_prod_cte(String sourceFileName, String targetFileName, VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream, int frmt,
           String Moneda, String strBodega, String strFecIni, String strFecFin, String strCategoria) {
      //Generamos el pdf
      InputStream reportStream = null;
      try {
         //Parametros
         Map parametersMap = new HashMap();
         parametersMap.put("MONEDA", Moneda);
         parametersMap.put("FEC_INI", strFecIni);
         parametersMap.put("FEC_FIN", strFecFin);
         parametersMap.put("BODEGA", strBodega);
         agregaParametrosCategorias(parametersMap, strCategoria);
         Locale locale = new Locale("es", "MX");
         //Definicion estandard localidad(MX mientras no implementemos algo en el extranjero)
         parametersMap.put(JRParameter.REPORT_LOCALE, locale);
         reportStream = new FileInputStream(sourceFileName);
         // Bing the datasource with the collection
         JRDataSource datasource = new JRBeanCollectionDataSource(this.lstCteCategoria, true);
         // Compile and print the jasper report
         JasperReport report = JasperCompileManager.compileReport(reportStream);
         //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
         JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);

         reportStream.close();

         switch (frmt) {
            case 1:
               if (byteArrayOutputStream == null) {
                  JasperExportManager.exportReportToPdfFile(print, targetFileName);
               } else {
                  JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
               }
               break;
            case 2:
               JRXlsExporter exporterXLS = new JRXlsExporter();
               exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
               exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
               exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
               exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
               exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
               exporterXLS.exportReport();
               break;
         }

      } catch (FileNotFoundException ex) {
         log.error(ex.getLocalizedMessage());
      } catch (JRException ex) {
         log.error(ex.getLocalizedMessage());
      } catch (IOException ex) {
         log.error(ex.getLocalizedMessage());
      }
   }//Fin ReportaDetallePor_Producto

   public String getMoneda(int MON_ID) throws SQLException {
      String strQueryMoneda = "select MON_DESCRIPCION from vta_monedas where MON_ID = '" + MON_ID + "';";
      ResultSet rs = oConn.runQuery(strQueryMoneda);
      String strMoneda = "";
      while (rs.next()) {
         strMoneda = rs.getString("MON_DESCRIPCION");
      }
      rs.close();
      return strMoneda;
   }//Fin getMoneda

   public String getSucursal(int SC_ID) {
      String nombreEmpresa = "";
      String strEmpresa = "select SC_NOMBRE from vta_sucursal where SC_ID = '" + SC_ID + "';";
      try {
         ResultSet rs = oConn.runQuery(strEmpresa, true);
         while (rs.next()) {
            nombreEmpresa = rs.getString("SC_NOMBRE");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex);
      }
      return nombreEmpresa;
   }//Fin GetEmpresa

   /**
    * Agrega los nombres de las categoria de la base de datos
    *
    * @param parametersMap Es el mapa de parametros
    * @param strCategoria Es el nombre de la categoria
    */
   public void agregaParametrosCategorias(Map parametersMap, String strCategoria) {
      String strSql = "select CAT_DESCRIPCION from vta_nom_cat where  CAT_ID = " + strCategoria;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            parametersMap.put("CATEGORIA", rs.getString("CAT_DESCRIPCION"));
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }
   // </editor-fold>
}
