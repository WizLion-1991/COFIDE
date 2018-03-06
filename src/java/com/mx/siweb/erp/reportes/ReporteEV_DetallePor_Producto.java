/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.EV_DetallePor_Producto;
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
public class ReporteEV_DetallePor_Producto {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String sourceFileName;
   private String targetFileName;
   private int intMoneda;
   private int intConvertido;
   private int intEmpId;
   private int intScId;
   private String strFechaIni;
   private String strFechaFin;
   private Conexion oConn;
   private ArrayList<EV_DetallePor_Producto> lstDetaPR = null;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteEV_DetallePor_Producto.class.getName());
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructor">
   public ReporteEV_DetallePor_Producto() {
      lstDetaPR = new ArrayList<EV_DetallePor_Producto>();
   }
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
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

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
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

   public Conexion getoConn() {
      return oConn;
   }

   public static Logger getLog() {
      return log;
   }

   public void Consume_EV(int intMoneda, int intConvertido, int intEmpresa, int intSucursal, String strFechaIni, String strFechaFin) {
      CallableStatement cStmt;
      try {
         cStmt = this.oConn.getConexion().prepareCall("{call sp_getVentas_globales_detalle_prod(?,?,?,?,?,?)}");
         cStmt.setInt(1, intMoneda);
         cStmt.setInt(2, intConvertido);
         cStmt.setInt(3, intEmpresa);
         cStmt.setInt(4, intSucursal);
         cStmt.setString(5, strFechaIni);
         cStmt.setString(6, strFechaFin);

         boolean hadResults = cStmt.execute();
         while (hadResults) {
            // process result set
            ResultSet rs = cStmt.getResultSet();
            // process result set
            while (rs.next()) {
               String strCalif1 = "";
               String strCalif2 = "";
               String strCalif3 = "";
               String strCalif4 = "";
               String strCalif5 = "";
               EV_DetallePor_Producto newEV_DetaProd = new EV_DetallePor_Producto();
               newEV_DetaProd.setStrNum(rs.getString("NUM"));
               newEV_DetaProd.setStrDescripcion(rs.getString("NOMBRE"));
               if (rs.getString("CLASIFICACION1") != null) {
                  newEV_DetaProd.setStrCalsif1(rs.getString("CLASIFICACION1"));
               } else {
                  newEV_DetaProd.setStrCalsif1(strCalif1);
               }
               if (rs.getString("CLASIFICACION2") != null) {
                  newEV_DetaProd.setStrCalsif2(rs.getString("CLASIFICACION2"));
               } else {
                  newEV_DetaProd.setStrCalsif2(strCalif2);
               }
               if (rs.getString("CLASIFICACION3") != null) {
                  newEV_DetaProd.setStrCalsif3(rs.getString("CLASIFICACION3"));
               } else {
                  newEV_DetaProd.setStrCalsif3(strCalif3);
               }
               if (rs.getString("CLASIFICACION4") != null) {
                  newEV_DetaProd.setStrCalsif4(rs.getString("CLASIFICACION4"));
               } else {
                  newEV_DetaProd.setStrCalsif4(strCalif4);
               }
               if (rs.getString("CLASIFICACION5") != null) {
                  newEV_DetaProd.setStrCalsif5(rs.getString("CLASIFICACION5"));
               } else {
                  newEV_DetaProd.setStrCalsif5(strCalif5);
               }
               newEV_DetaProd.setDblSubTotal(rs.getDouble("TSUBTOTAL"));
               newEV_DetaProd.setDblCantidad(rs.getDouble("CANTIDAD"));
               this.lstDetaPR.add(newEV_DetaProd);
            }//Fin rs
            rs.close();
            hadResults = cStmt.getMoreResults();
         }//Fin WHILE            
      }//Fin try
      catch (SQLException ex) {
         java.util.logging.Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
      }
   }//Fin Consume_EV

   public String generarXML() {
      Iterator<EV_DetallePor_Producto> itDetaPr = this.lstDetaPR.iterator();
      UtilXml util = new UtilXml();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      strXML.append("<DetalleProd>");
      while (itDetaPr.hasNext()) {
         EV_DetallePor_Producto prod = itDetaPr.next();
         strXML.append("<Producto");
         strXML.append(" PR_CODIGO= \"").append(util.Sustituye(prod.getStrNum())).append("\" ");
         strXML.append(" PR_DESCRIPCION= \"").append(util.Sustituye(prod.getStrDescripcion())).append("\" ");
         strXML.append(" CLASIFICACION_1= \"").append(util.Sustituye(prod.getStrCalsif1())).append("\" ");
         strXML.append(" CLASIFICACION_2= \"").append(util.Sustituye(prod.getStrCalsif2())).append("\" ");
         strXML.append(" CLASIFICACION_3= \"").append(util.Sustituye(prod.getStrCalsif3())).append("\" ");
         strXML.append(" CLASIFICACION_4= \"").append(util.Sustituye(prod.getStrCalsif4())).append("\" ");
         strXML.append(" CLASIFICACION_5= \"").append(util.Sustituye(prod.getStrCalsif5())).append("\" ");
         strXML.append(" IMPORTE_VETA= \"").append(prod.getDblSubTotal()).append("\" ");
         strXML.append(" CANTIDAD= \"").append(prod.getDblCantidad()).append("\" ");
         strXML.append("/>");
      }
      strXML.append("</DetalleProd>");
      return strXML.toString();
   }//Fin genera XML

   public void ReportaDetallePor_Producto(String sourceFileName, String targetFileName, VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream, int frmt,
           String Moneda, String strBodega, String strFecIni, String strFecFin) {
      //Generamos el pdf
      InputStream reportStream = null;
      try {
         //Parametros
         Map parametersMap = new HashMap();
         parametersMap.put("MONEDA", Moneda);
         parametersMap.put("FEC_INI", strFecIni);
         parametersMap.put("FEC_FIN", strFecFin);
         parametersMap.put("BODEGA", strBodega);
         //Agregamos los nombres de la base
         agregaParametrosCategorias( parametersMap);
         Locale locale = new Locale("es", "MX");
         //Definicion estandard localidad(MX mientras no implementemos algo en el extranjero)
         parametersMap.put(JRParameter.REPORT_LOCALE, locale);
         reportStream = new FileInputStream(sourceFileName);
         // Bing the datasource with the collection
         JRDataSource datasource = new JRBeanCollectionDataSource(this.lstDetaPR, true);
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
         log.error(ex.getLocalizedMessage() + "(1)");
      } catch (JRException ex) {
         log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getMessageKey() + " " + ex.toString() +  "(2)");
         
      } catch (IOException ex) {
         log.error(ex.getLocalizedMessage() + "(3)");
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
    *Agrega los nombres de las categoria de la base de datos
    * @param parametersMap
    */
   public void agregaParametrosCategorias(Map parametersMap) {
      String strSql = "select *  from vta_nom_cat";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
               parametersMap.put("NomCategoria" + rs.getInt("CAT_ID"), rs.getString("CAT_DESCRIPCION"));
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

   }
    // </editor-fold>
}
