/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.ConsultaInventarioE;
import com.mx.siweb.erp.reportes.entities.DetalleCobranzaE;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DetalleCobranza {
    private int mon_id;
    private int convertido;
    private String fechaI;
    private String fechaF;
    private int sc_id;
    private int emp_id;
    private ArrayList<DetalleCobranzaE> entidades;
    private Conexion oConn;
    private boolean bolImpreso;
    private Object strPathLogoWeb;
    private ResultSet rs2;
    private String emp_nom;
    private String sc_nombre;
    private String mon_nom;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DetalleCobranza.class.getName());

    public DetalleCobranza(int mon_id, int convertido, String fechaI, String fechaF, int sc_id, int emp_id, Conexion oConn) {
        this.mon_id = mon_id;
        this.convertido = convertido;
        this.fechaI = fechaI;
        this.fechaF = fechaF;
        this.sc_id = sc_id;
        this.emp_id = emp_id;
        this.oConn = oConn;
        entidades=new ArrayList<DetalleCobranzaE>();
    }

    public void HacerReporte(){
        try {
            CallableStatement cStmt;
            String stmt="{call sp_getCobranzaDetalle( "
                    + mon_id+","
                    + convertido+ ","
                    + emp_id+","
                    + sc_id+","
                    + "'"+fechaI+"',"
                    + "'"+fechaF+"')};";
            
            cStmt = oConn.getConexion().prepareCall(stmt);
            boolean hadResults = cStmt.execute();
           
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();
                while(rs.next()){
                    DetalleCobranzaE e=new DetalleCobranzaE();
                    e.setBanco(rs.getString("NOM_BANCO"));
                    e.setCliente(rs.getString("CLIENTE"));
                    e.setCobro(rs.getInt("NPAGO"));
                    e.setDocumento(rs.getString("DOCUMENTO"));
                    e.setFormaDePago(rs.getString("FORMAPAGO"));
                    e.setMonto(rs.getDouble("VALORPAGO"));
                    e.setFecha(rs.getString("FECHA"));
                    entidades.add(e);
                }
                rs.close();
                
                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControlInventarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public String GeneraXml(){
        Iterator<DetalleCobranzaE> it= entidades.iterator();
        UtilXml util = new UtilXml();  
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<DetalleCobranza>");
        while (it.hasNext()) {
            DetalleCobranzaE entidad = it.next();
            strXML.append(" <Pago");
            strXML.append(" Banco= \"").append(entidad.getBanco()).append("\" ");
            strXML.append(" Cliente= \"").append(util.Sustituye(entidad.getCliente())).append("\" ");
            strXML.append(" Documento= \"").append(entidad.getDocumento()).append("\" ");
            strXML.append(" FormadePago= \"").append(entidad.getFormaDePago()).append("\" ");
            strXML.append(" Monto= \"").append(entidad.getMonto()).append("\" ");
            strXML.append(" Fecha= \"").append(entidad.getFecha()).append("\" ");
            strXML.append(" Pago= \"").append(entidad.getCobro()).append("\" ");
            strXML.append("/>");
      }
      strXML.append("</DetalleCobranza>");
      return strXML.toString();
    }
    public void getReportPrint(String sourceFileName, String targetFileName,VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream,int frmt,String strPathLogoWeb) {
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
            Logger.getLogger(DetalleCobranza.class.getName()).log(Level.SEVERE, null, ex);
        }         
      
       strSql2 = "select EMP_PATHIMGFORM,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
     try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            emp_nom = rs2.getString("EMP_RAZONSOCIAL");
         }
         rs2.close();
      } catch (SQLException ex) {
            Logger.getLogger(ControlInventarios.class.getName()).log(Level.SEVERE, null, ex);
        }
     String strBodega="SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID="+sc_id;
     try {
         rs2 = this.oConn.runQuery(strBodega, true);
         while (rs2.next()) {
            sc_nombre = rs2.getString("SC_NOMBRE");
         }
         rs2.close();
      } catch (SQLException ex) {
            Logger.getLogger(ControlInventarios.class.getName()).log(Level.SEVERE, null, ex);
        }
      strSql2 = "select vta_monedas.MON_DESCRIPCION from vta_monedas where vta_monedas.MON_ID  =" + mon_id;
      ResultSet rs2;
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            mon_nom = rs2.getString("MON_DESCRIPCION");
         }
         rs2.close();
      } catch (SQLException ex) {
            Logger.getLogger(DetalleCobranza.class.getName()).log(Level.SEVERE, null, ex);
        }
      //Generamos el pdf
      InputStream reportStream = null;
      try {
         //Parametros
         Map parametersMap = new HashMap();
         
         parametersMap.put("PathLogoWeb", strPathLogoWeb);
         parametersMap.put("empresa",emp_nom );
         parametersMap.put("bodega",sc_nombre );
         parametersMap.put("moneda", mon_nom);
         parametersMap.put("finicial",fechaI );
         parametersMap.put("ffinal",fechaF );
         if(convertido==1)
            parametersMap.put("convertido","si");
         else
            parametersMap.put("convertido","no");

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

      } catch (FileNotFoundException ex) {
            Logger.getLogger(DetalleCobranza.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(DetalleCobranza.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DetalleCobranza.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
