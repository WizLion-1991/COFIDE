
package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.CobroNoIdentificadoE;
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

public class CobrosNoIdentificados {
private int mon_id;
    private int convertido;
    private String fechaI;
    private String fechaF;
    private int sc_id;
    private int emp_id;
    private ArrayList<CobroNoIdentificadoE> entidades;
    private Conexion oConn;
    private boolean bolImpreso;
    private Object strPathLogoWeb;
    private ResultSet rs2;
    private Object emp_nom;
    private String sc_nombre;
    private String mon_nom;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CobrosNoIdentificados.class.getName());

    public CobrosNoIdentificados(int mon_id, int convertido, String fechaI, String fechaF, int sc_id, int emp_id, Conexion oConn) {
        this.mon_id = mon_id;
        this.convertido = convertido;
        this.fechaI = fechaI;
        this.fechaF = fechaF;
        this.sc_id = sc_id;
        this.emp_id = emp_id;
        this.oConn = oConn;
        entidades=new ArrayList<CobroNoIdentificadoE>();
    }

    public void HacerReporte(){
        try {
            CallableStatement cStmt;
            String stmt="{call sp_getCobranzaNoIdentificada( "
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
                    CobroNoIdentificadoE e=new CobroNoIdentificadoE();
                    e.setBanco(rs.getString("BANCO"));
                    e.setBeneficiario(rs.getString("BENEFICIARIO"));
                    e.setConcepto(rs.getString("CONCEPTO"));
                    e.setDeposito(rs.getDouble("DEPOSITO"));
                    e.setFecha(rs.getString("FECHA"));
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
        Iterator<CobroNoIdentificadoE> it= entidades.iterator();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<CobroNoIdentificado>");
        while (it.hasNext()) {
            CobroNoIdentificadoE entidad = it.next();
            strXML.append(" <Pago");
            strXML.append(" Banco= \"").append(xml.Sustituye(entidad.getBanco())).append("\" ");
            strXML.append(" Beneficiario= \"").append(xml.Sustituye(entidad.getBeneficiario())).append("\" ");
            strXML.append(" Concepto= \"").append(xml.Sustituye(entidad.getConcepto())).append("\" ");
            strXML.append(" Fecha= \"").append(entidad.getFecha()).append("\" ");
            strXML.append(" Deposito= \"").append(entidad.getDeposito()).append("\" ");
            strXML.append("/>");
      }
      strXML.append("</CobroNoIdentificado>");
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
            log.error(ex.getLocalizedMessage());
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
      strSql2 = "select vta_monedas.MON_DESCRIPCION from vta_monedas where vta_monedas.MON_ID  =" + mon_id;
      ResultSet rs2;
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            mon_nom = rs2.getString("MON_DESCRIPCION");
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
        log.error(ex.getLocalizedMessage());
    } catch (JRException ex) {
        log.error(ex.getLocalizedMessage());
    } catch (IOException ex) {
        log.error(ex.getLocalizedMessage());
    }
   }    
}
