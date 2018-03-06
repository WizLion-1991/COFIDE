package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.ProductosResumen;
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
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.logging.log4j.LogManager;
/**
 * system
 * @author siweb
 */
public class RepoProductosResumen {

   ArrayList<ProductosResumen> pResumen;
     private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(RepoProductosResumen.class.getName());

   public RepoProductosResumen() {
      pResumen = new ArrayList();
   }

   public ArrayList<ProductosResumen> getpResumen() {
      return pResumen;
   }

   public void setpResumen(ArrayList<ProductosResumen> pResumen) {
      this.pResumen = pResumen;
   }

   public void llamarSp(Conexion oConn, int intCtid, int intMoneda, int intConvertido, int intScId, String strFechaIni, String strFechaFin) {

      try {
         CallableStatement cStmt = oConn.getConexion().prepareCall("{call sp_getClienteFacturasProductosResumen(?,?,?,?,?,?)}");

         cStmt.setInt(1, intCtid);
         cStmt.setInt(2, intMoneda);
         cStmt.setInt(3, intConvertido);
         cStmt.setInt(4, intScId);
         cStmt.setString(5, strFechaIni);
         cStmt.setString(6, strFechaFin);

         boolean hadResults = cStmt.execute();
         while (hadResults) {
            ResultSet rs = cStmt.getResultSet();

            // process result set
            while (rs.next()) {
               ProductosResumen producto = new ProductosResumen();
               producto.setCodigo(rs.getString("CODIGO"));
               producto.setNombre(rs.getString("NOMBRE"));
               producto.setCantidad(rs.getDouble("CANTIDAD"));
               producto.setValor(rs.getDouble("VALOR"));

               pResumen.add(producto);

            }
            rs.close();
            hadResults = cStmt.getMoreResults();
         }
      } catch (SQLException ex) {
         log.debug("ERROR SQL>>> " + ex.getMessage());
         
      } catch (Exception exc) {
         log.debug("Otro error>>> " + exc.getMessage());
         
      }
   }

   public String generarXML() {

      Iterator<ProductosResumen> itRes = pResumen.iterator();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      UtilXml util = new UtilXml();
      strXML.append("<Reporte_productos_resumen>");
      while (itRes.hasNext()) {
         ProductosResumen prodR = itRes.next();
         strXML.append("<Docs ");
         strXML.append(" CODIGO= \"").append(prodR.getCodigo()).append("\"");
         strXML.append(" NOMBRE= \"").append(util.Sustituye(prodR.getNombre())).append("\"");
         strXML.append(" CANTIDAD= \"").append(prodR.getCantidad()).append("\"");
         strXML.append(" VALOR= \"").append(prodR.getValor()).append("\"");
         strXML.append("/>");

      }
      strXML.append("</Reporte_productos_resumen>");
      return strXML.toString();

   }

   public void generarReporte(String pathBase, VariableSession varSesiones, String sourceFile, String targetFileName, Conexion oConn,
           int intCtid, int intMoneda, int intConvertido, int intScId, String strFechaIni, String strFechaFin,
           ByteArrayOutputStream byteArrayOutputStream) {
      String strSql2 = "select EMP_PATHIMG,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      String strMoneda = "SELECT MON_DESCRIPCION FROM vta_monedas WHERE MON_ID=" + intMoneda;
      String strBodega = "SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID=" + intScId;
      String strCliente = "SELECT CT_RAZONSOCIAL FROM vta_cliente WHERE CT_ID=" + intCtid;
      this.llamarSp(oConn, intCtid, intMoneda, intConvertido, intScId, strFechaIni, strFechaFin);

      InputStream reportStream = null;
      try {
         ResultSet rs2 = oConn.runQuery(strSql2, true);
         ResultSet rMoneda = oConn.runQuery(strMoneda);
         ResultSet rBodega = oConn.runQuery(strBodega);
         ResultSet rCliente = oConn.runQuery(strCliente);
         while (rMoneda.next() & rBodega.next() & rCliente.next() & rs2.next()) {
            //Parametros
            String strPathLogoWeb = pathBase + rs2.getString("EMP_PATHIMG");
            Map parametersMap = new HashMap();
            parametersMap.put("logo", strPathLogoWeb);
            parametersMap.put("empresa", rs2.getString("EMP_RAZONSOCIAL"));
            parametersMap.put("Cliente", rCliente.getString("CT_RAZONSOCIAL"));
            parametersMap.put("Moneda", rMoneda.getString("MON_DESCRIPCION"));
            parametersMap.put("Bodega", rBodega.getString("SC_NOMBRE"));
            parametersMap.put("Convertido", intConvertido);
            parametersMap.put("fechaIni", strFechaIni);
            parametersMap.put("fechaFin", strFechaFin);

            reportStream = new FileInputStream(sourceFile);
            JRDataSource datasource = new JRBeanCollectionDataSource(pResumen, true);
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);

            if (byteArrayOutputStream == null) {
               JasperExportManager.exportReportToPdfFile(print, targetFileName);
            } else {
               JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
            }

         }

      } catch (FileNotFoundException ex) {
         log.debug(ex.getMessage());
      } catch (JRException ex) {
         log.debug(ex.getMessage());
      } catch (Exception ex) {
         log.debug(ex.getMessage());
      } finally {
         try {
            reportStream.close();
         } catch (IOException ex) {
            log.debug(ex.getMessage());
         }
      }
   }

   public void generarExcel(String pathBase, VariableSession varSesiones, String sourceFile, String targetFileName, Conexion oConn,
           int intCtid, int intMoneda, int intConvertido, int intScId, String strFechaIni, String strFechaFin,
           ByteArrayOutputStream byteArrayOutputStream) {

      String strSql2 = "select EMP_PATHIMG,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      String strMoneda = "SELECT MON_DESCRIPCION FROM vta_monedas WHERE MON_ID=" + intMoneda;
      String strBodega = "SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID=" + intScId;
      String strCliente = "SELECT CT_RAZONSOCIAL FROM vta_cliente WHERE CT_ID=" + intCtid;
      this.llamarSp(oConn, intCtid, intMoneda, intConvertido, intScId, strFechaIni, strFechaFin);

      InputStream reportStream = null;
      try {
         ResultSet rs2 = oConn.runQuery(strSql2, true);
         ResultSet rMoneda = oConn.runQuery(strMoneda);
         ResultSet rBodega = oConn.runQuery(strBodega);
         ResultSet rCliente = oConn.runQuery(strCliente);
         while (rMoneda.next() & rBodega.next() & rCliente.next() & rs2.next()) {
            //Parametros
            String strPathLogoWeb = pathBase + rs2.getString("EMP_PATHIMG");
            Map parametersMap = new HashMap();
            parametersMap.put("logo", strPathLogoWeb);
            parametersMap.put("empresa", rs2.getString("EMP_RAZONSOCIAL"));
            parametersMap.put("Cliente", rCliente.getString("CT_RAZONSOCIAL"));
            parametersMap.put("Moneda", rMoneda.getString("MON_DESCRIPCION"));
            parametersMap.put("Bodega", rBodega.getString("SC_NOMBRE"));
            parametersMap.put("Convertido", intConvertido);
            parametersMap.put("fechaIni", strFechaIni);
            parametersMap.put("fechaFin", strFechaFin);

            reportStream = new FileInputStream(sourceFile);
            JRDataSource datasource = new JRBeanCollectionDataSource(pResumen, true);
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);

            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporterXLS.exportReport();

         }

      } catch (FileNotFoundException ex) {
         log.debug(ex.getMessage());
      } catch (JRException ex) {
         log.debug(ex.getMessage());
      } catch (Exception ex) {
         log.debug(ex.getMessage());
      } finally {
         try {
            reportStream.close();
         } catch (IOException ex) {
            log.debug(ex.getMessage());
         }
      }
   }

}
