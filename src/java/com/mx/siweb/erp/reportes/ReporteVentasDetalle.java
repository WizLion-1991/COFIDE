package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.VentasDetalles;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
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
public class ReporteVentasDetalle {

   private ArrayList<VentasDetalles> vtaDetalle;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteVentasDetalle.class.getName());

   public ReporteVentasDetalle() {
      vtaDetalle = new ArrayList();

   }

   public ArrayList<VentasDetalles> getVtaDetalle() {
      return vtaDetalle;
   }

   public void setVtaDetalle(ArrayList<VentasDetalles> vtaDetalle) {
      this.vtaDetalle = vtaDetalle;
   }

   //(intCtId int, intMoneda int, intConvertido int, intScId int, strFechaIni varchar(8),strFechaFin varchar(8))
   public void llamarSP(Conexion oConn, int intCtid, int intMoneda, int intConvertido, int intScId, String strFechaIni, String strFechaFin) {

      try {
         CallableStatement cStmt = oConn.getConexion().prepareCall("{call sp_getClienteFacturasDetalle(?,?,?,?,?,?)}");

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
               VentasDetalles vtaDeta = new VentasDetalles();
               vtaDeta.setSucursal(rs.getString("SC_NOMBRE"));
               vtaDeta.setFactura(rs.getString("FAC_FOLIO_C"));
               vtaDeta.setFecha(rs.getString("FECHA"));
               vtaDeta.setTotalFactura(rs.getDouble("TTOTAL"));
               vtaDeta.setImpuestos(rs.getDouble("TIMPUESTO1"));
               vtaDeta.setSubTotal(rs.getDouble("TIMPORTE"));
               vtaDeta.setDescuentos(rs.getDouble("TDESCUENTO"));
               vtaDeta.setNotasCredito(rs.getString("NOTAS_CREDITO"));

               vtaDetalle.add(vtaDeta);

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

      Iterator<VentasDetalles> itVta = vtaDetalle.iterator();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<Reporte_Ventas_Detalles>");

      while (itVta.hasNext()) {
         VentasDetalles detalle = itVta.next();
         strXML.append(" <Docs ");
         strXML.append(" SUCURSAL= \"").append(detalle.getSucursal()).append("\" ");
         strXML.append(" FACTURA= \"").append(detalle.getFactura()).append("\" ");
         strXML.append(" FECHA= \"").append(detalle.getFecha()).append("\" ");
         strXML.append(" TOTAL_FACTURA= \"").append(detalle.getTotalFactura()).append("\" ");
         strXML.append(" IMPUESTOS= \"").append(detalle.getImpuestos()).append("\" ");
         strXML.append(" SUBTOTAL= \"").append(detalle.getSubTotal()).append("\" ");
         strXML.append(" DESCUENTOS= \"").append(detalle.getDescuentos()).append("\" ");
         strXML.append(" NOTAS_DE_CREDITO= \"").append(detalle.getNotasCredito()).append("\" ");
         strXML.append("/>");
      }
      strXML.append("</Reporte_Ventas_Detalles>");
      return strXML.toString();

   }

   public void generarReporte(String pathBase, VariableSession varSesiones, String sourceFile, String targetFileName, Conexion oConn,
           int intCtid, int intMoneda, int intConvertido, int intScId, String strFechaIni,
           String strFechaFin, ByteArrayOutputStream byteArrayOutputStream) {

      String strSql2 = "select EMP_PATHIMG,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      String strMoneda = "SELECT MON_DESCRIPCION FROM vta_monedas WHERE MON_ID=" + intMoneda;
      String strBodega = "SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID=" + intScId;
      String strCliente = "SELECT CT_RAZONSOCIAL FROM vta_cliente WHERE CT_ID=" + intCtid;
      this.llamarSP(oConn, intCtid, intMoneda, intConvertido, intScId, strFechaIni, strFechaFin);

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
            JRDataSource datasource = new JRBeanCollectionDataSource(vtaDetalle, true);
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
           int intCtid, int intMoneda, int intConvertido, int intScId, String strFechaIni,
           String strFechaFin, ByteArrayOutputStream byteArrayOutputStream) {

      String strSql2 = "select EMP_PATHIMG,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      String strMoneda = "SELECT MON_DESCRIPCION FROM vta_monedas WHERE MON_ID=" + intMoneda;
      String strBodega = "SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID=" + intScId;
      String strCliente = "SELECT CT_RAZONSOCIAL FROM vta_cliente WHERE CT_ID=" + intCtid;
      this.llamarSP(oConn, intCtid, intMoneda, intConvertido, intScId, strFechaIni, strFechaFin);

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
            JRDataSource datasource = new JRBeanCollectionDataSource(vtaDetalle, true);
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
