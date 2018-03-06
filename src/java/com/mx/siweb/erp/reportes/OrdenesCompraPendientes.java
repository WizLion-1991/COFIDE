/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.OrdenenCompraPendienteE;
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
 *
 * @author siweb
 */
public class OrdenesCompraPendientes {
    
    private Conexion oConn;
    private int codigo;
    private ArrayList<OrdenenCompraPendienteE> entidades;
    private boolean bolImpreso;
    private String strPathLogoWeb;
    private ResultSet rs2;
    private String emp_nom;
    private VariableSession varSesiones;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(OrdenesCompraPendientes.class.getName());

    public OrdenesCompraPendientes(Conexion oConn, int codigo,VariableSession varSesiones) {
        this.oConn = oConn;
        this.codigo = codigo;
        this.entidades=new ArrayList<OrdenenCompraPendienteE>();
        this.varSesiones = varSesiones;
    }
    
    
    
    public void HacerReporte(){
        try {
            CallableStatement cStmt;
            String stmt="{call sp_getProdComprasPendientes("
                    +codigo+"," + varSesiones.getIntIdEmpresa() + ")};";
            
            cStmt = oConn.getConexion().prepareCall(stmt);
            boolean hadResults = cStmt.execute();
           
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();
                while(rs.next()){
                    OrdenenCompraPendienteE e=new OrdenenCompraPendienteE();
                    e.setCodigo(rs.getString("PR_CODIGO"));
                    e.setAlmacen(rs.getString("SC_NOMBRE"));
                    e.setProveedor(rs.getString("PV_RAZONSOCIAL"));
                    e.setFolio(rs.getString("COM_FOLIO"));
                    e.setFecha(rs.getString("FECHA"));
                    e.setFentrega(rs.getString("FENTREGA"));
                    e.setCantidad(rs.getInt("CantidadTotal"));
                    entidades.add(e);
                }
                rs.close();
                
                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            log.debug(ex);
        }
    }    
    
    public String GeneraXml(){
        UtilXml xml = new UtilXml();
        Iterator<OrdenenCompraPendienteE> it= entidades.iterator();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<OrdenesCompraPendientes>");
        while (it.hasNext()) {
            OrdenenCompraPendienteE entidad = it.next();
            strXML.append(" <Producto");
            strXML.append(" Codigo= \"").append(entidad.getCodigo()).append("\" ");
            strXML.append(" Almacen= \"").append(entidad.getAlmacen()).append("\" ");
            strXML.append(" Proveedor= \"").append(xml.Sustituye(entidad.getProveedor())).append("\" ");
            strXML.append(" Folio= \"").append(entidad.getFolio()).append("\" ");
            strXML.append(" Fecha= \"").append(entidad.getFecha()).append("\" ");
            strXML.append(" Fentrega= \"").append(entidad.getFentrega()).append("\" ");
            strXML.append(" Cantidad= \"").append(entidad.getCantidad()).append("\" ");
            strXML.append("/>");
      }
      strXML.append("</OrdenesCompraPendientes>");
      return strXML.toString();
    }
    public void getReportPrint(String sourceFileName, String targetFileName,VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream,String descripcion,int frmt,String strPathLogoWeb) {
      this.bolImpreso = true;
      
      
      //Logo de la empresa
      String strSql2 = "select EMP_PATHIMG from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            strPathLogoWeb += System.getProperty("file.separator")+rs2.getString("EMP_PATHIMG");
            }
            rs2.close();
        } catch (SQLException ex) {
            log.debug(ex);
        }         
      
       strSql2 = "select EMP_PATHIMGFORM,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
     try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            emp_nom = rs2.getString("EMP_RAZONSOCIAL");
         }
         rs2.close();
      } catch (SQLException ex) {
            log.error(ex.getLocalizedMessage());
        }
      //Generamos el pdf
      InputStream reportStream = null;
      try {
         //Parametros
         Map parametersMap = new HashMap();
         
         parametersMap.put("PathLogoWeb", strPathLogoWeb);
         parametersMap.put("empresa",emp_nom);
         parametersMap.put("codigo",codigo);
         parametersMap.put("descripcion",descripcion);

         reportStream = new FileInputStream(sourceFileName);
         // Bing the datasource with the collection
         JRDataSource datasource = new JRBeanCollectionDataSource(this.entidades, true);
         // Compile and print the jasper report
         JasperReport report = JasperCompileManager.compileReport(reportStream);
         //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
         JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);
         
         reportStream.close();

         
         switch(frmt){
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

      } catch (JRException ex) { 
            log.debug(ex);
        } catch (FileNotFoundException ex) {
            log.debug(ex);
        } catch (IOException ex) {
            log.debug(ex);
        } 
   }
}
