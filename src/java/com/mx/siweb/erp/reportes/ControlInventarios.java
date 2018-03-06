/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.ConsultaInventarioE;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class ControlInventarios {
    private Conexion oConn;
    private int id=0;
    private String codigo;
    private String descripcion;
    private int sc_id;
    private int emp_id;
    private int pv_id;
    private int categoria1;
    private int categoria2;
    private int categoria3;
    private int categoria4;
    private int categoria5;
    private int categoria6;
    private int categoria7;
    private int categoria8;
    private int categoria9;
    private int categoria10;
    private ArrayList<ConsultaInventarioE> entidades;
    private boolean bolImpreso;
    private String strSqllogo;
    private ResultSet rslogo;
    private String nomProveedor;
    private int activo;
    private int apartados;
    private int existencia;
    private String strSql2;
    private ResultSet rs2;
    private String emp_nom;
    private String sc_nombre;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ControlInventarios.class.getName());
    

    public ControlInventarios(Conexion oConn, int id, String codigo, String descripcion, int sc_id, int emp_id, int pv_id, int categoria1, int categoria2, int categoria3, int categoria4, int categoria5, int categoria6, int categoria7, int categoria8, int categoria9, int categoria10) {
        this.oConn = oConn;
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.sc_id = sc_id;
        this.emp_id = emp_id;
        this.pv_id = pv_id;
        this.categoria1 = categoria1;
        this.categoria2 = categoria2;
        this.categoria3 = categoria3;
        this.categoria4 = categoria4;
        this.categoria5 = categoria5;
        this.categoria6 = categoria6;
        this.categoria7 = categoria7;
        this.categoria8 = categoria8;
        this.categoria9 = categoria9;
        this.categoria10 = categoria10;
        this.entidades=new ArrayList<ConsultaInventarioE>();
    }

    
    
    public void HacerReporte(){
        try {
            CallableStatement cStmt;
            String stmt="{call sp_getProdControlInventario1("
                    + id+","
                    + " '"+codigo+"',"
                    + "'"+descripcion+"',"
                    + sc_id+","
                    + emp_id+","
                    + pv_id+","
                    + categoria1+","
                    + categoria2+","
                    + categoria3+","
                    + categoria4+","
                    + categoria5+","
                    + categoria6+","
                    + categoria7+","
                    + categoria8+","
                    + categoria9+","
                    + categoria10+")}";
            
            cStmt = oConn.getConexion().prepareCall(stmt);
            boolean hadResults = cStmt.execute();
           
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();
                while(rs.next()){
                    ConsultaInventarioE e=new ConsultaInventarioE();
                    e.setPr_id(rs.getInt("PR_ID"));
                    this.nomProveedor=rs.getString("NomProveedor");
                    this.activo=(rs.getInt("PR_ACTIVO"));
                    this.apartados=(rs.getInt("PR_APARTADOS"));
                    e.setPr_categoria1(rs.getString("PR_CATEGORIA1"));
                    e.setPr_categoria10(rs.getString("PR_CATEGORIA10"));
                    e.setPr_categoria2(rs.getString("PR_CATEGORIA2"));
                    e.setPr_categoria3(rs.getString("PR_CATEGORIA3"));
                    e.setPr_categoria4(rs.getString("PR_CATEGORIA4"));
                    e.setPr_categoria5(rs.getString("PR_CATEGORIA5"));
                    e.setPr_categoria6(rs.getString("PR_CATEGORIA6"));
                    e.setPr_categoria7(rs.getString("PR_CATEGORIA7"));
                    e.setPr_categoria8(rs.getString("PR_CATEGORIA8"));
                    e.setPr_categoria9(rs.getString("PR_CATEGORIA9"));
                    e.setPr_codigo(rs.getString("PR_CODIGO"));
                    e.setPr_descripcion(rs.getString("PR_DESCRIPCION"));
                    this.existencia=(rs.getInt("PR_EXISTENCIA"));
                    e.setPrecio1(rs.getDouble("precio1"));
                    e.setPrecio2(rs.getDouble("precio2"));
                    e.setPrecio3(rs.getDouble("precio3"));
                    e.setPrecio4(rs.getDouble("precio4"));
                    e.setTcompras(rs.getInt("TCOMPRAS"));
                    e.setTexistencia(rs.getInt("TEXISTENCIA"));
                    e.setTpedidos(rs.getInt("TPEDIDOS"));
                    e.setDisponibles(this.existencia-e.getTpedidos());
                    e.setEglobales(rs.getInt("EGLOBALES"));
                    entidades.add(e);
                }
                rs.close();
                
                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }    
    
    public String GeneraXml(){
        UtilXml xml = new UtilXml();
        Iterator<ConsultaInventarioE> it= entidades.iterator();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<ControlInventarios>");
        while (it.hasNext()) {
            ConsultaInventarioE entidad = it.next();
            strXML.append(" <Producto");
            strXML.append(" PR_ID= \"").append(entidad.getPr_id()).append("\" ");
            strXML.append(" Codigo= \"").append(entidad.getPr_codigo()).append("\" ");
            strXML.append(" Descripcion= \"").append(xml.Sustituye(entidad.getPr_descripcion())).append("\" ");
            strXML.append(" Disponible= \"").append(entidad.getDisponibles()).append("\" ");
            strXML.append(" Pedidos= \"").append(entidad.getTpedidos()).append("\" ");
            strXML.append(" OrdenC= \"").append(entidad.getTcompras()).append("\" ");
            strXML.append(" EGlobal= \"").append(entidad.getEglobales()).append("\" ");
            strXML.append(" Existencia= \"").append(entidad.getTexistencia()).append("\" ");
            strXML.append(" Prec1= \"").append(entidad.getPrecio1()).append("\" ");
            strXML.append(" Prec2= \"").append(entidad.getPrecio2()).append("\" ");
            strXML.append(" Prec3= \"").append(entidad.getPrecio3()).append("\" ");
            strXML.append(" Prec4= \"").append(entidad.getPrecio4()).append("\" ");
            strXML.append(" Cat1= \"").append(entidad.getPr_categoria1()).append("\" ");
            strXML.append(" Cat2= \"").append(entidad.getPr_categoria2()).append("\" ");
            strXML.append(" Cat3= \"").append(entidad.getPr_categoria3()).append("\" ");
            strXML.append(" Cat4= \"").append(entidad.getPr_categoria4()).append("\" ");
            strXML.append(" Cat5= \"").append(entidad.getPr_categoria5()).append("\" ");
            strXML.append(" Cat6= \"").append(entidad.getPr_categoria6()).append("\" ");
            strXML.append(" Cat7= \"").append(entidad.getPr_categoria7()).append("\" ");
            strXML.append(" Cat8= \"").append(entidad.getPr_categoria8()).append("\" ");
            strXML.append(" Cat9= \"").append(entidad.getPr_categoria9()).append("\" ");
            strXML.append(" Cat10= \"").append(entidad.getPr_categoria10()).append("\" ");
            strXML.append("/>");
      }
      strXML.append("</ControlInventarios>");
      return strXML.toString();
    }
    public void getReportPrint(String sourceFileName, String targetFileName,VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream,int frmt,String strPathLogoWeb) {
      this.bolImpreso = true;
      
      
      //Logo de la empresa
      strSql2 = "select EMP_PATHIMG from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            strPathLogoWeb += System.getProperty("file.separator")+rs2.getString("EMP_PATHIMG");
         }
         rs2.close();
      } catch (SQLException ex) {
            log.error(ex.getLocalizedMessage());
        }
     String strSql2 = "select EMP_PATHIMGFORM,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
     try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            emp_nom = rs2.getString("EMP_RAZONSOCIAL");
         }
         rs2.close();
      } catch (SQLException ex) {
            log.error(ex.getLocalizedMessage());
        }
     String strBodega="SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID="+sc_id;
     try {
         rs2 = this.oConn.runQuery(strBodega, true);
         while (rs2.next()) {
            sc_nombre = rs2.getString("SC_NOMBRE");
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
         parametersMap.put("proveedor",nomProveedor);
         parametersMap.put("bodega",sc_nombre);
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
         //Dependiendo del parametro lo guardamos localmente o lo enviamos en un hilo

      } catch (FileNotFoundException ex) {
          log.error(ex.getLocalizedMessage());
      } catch (JRException ex) {
          log.error(ex.getLocalizedMessage());
      } catch (Exception ex) {
          log.error(ex.getLocalizedMessage());
      } 
      
   }
    //Getters y Setters
    public ArrayList<ConsultaInventarioE> getEntidades() {
        return entidades;
    }

    public void setEntidades(ArrayList<ConsultaInventarioE> entidades) {
        this.entidades = entidades;
    }
    
    
}
