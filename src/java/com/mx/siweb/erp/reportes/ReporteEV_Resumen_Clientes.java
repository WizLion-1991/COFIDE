/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;


import com.mx.siweb.erp.reportes.entities.EV_Resumen_Clientes;
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
 * @author siwebmx5
 */
public class ReporteEV_Resumen_Clientes {
// <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String sourceFileName;
    private String targetFileName;
    private int intMoneda;
    private int intConvertido;
    private int intEmpId;
    private int intScId;
    private String strFechaIni;
    private String strFechaFin;
    
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteEV_Resumen_Clientes.class.getName());
    
    Conexion oConn;
    private ArrayList<EV_Resumen_Clientes> EV_Resumen_Clientes;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public int getIntMoneda() {
        return intMoneda;
    }

    public void setIntMoneda(int intMoneda) {
        this.intMoneda = intMoneda;
    }

    public int getIntConvertido() {
        return intConvertido;
    }

    public void setIntConvertido(int intConvertido) {
        this.intConvertido = intConvertido;
    }

    public int getIntEmpId() {
        return intEmpId;
    }

    public void setIntEmpId(int intEmpId) {
        this.intEmpId = intEmpId;
    }

    public int getIntScId() {
        return intScId;
    }

    public void setIntScId(int intScId) {
        this.intScId = intScId;
    }

    public String getStrFechaIni() {
        return strFechaIni;
    }

    public void setStrFechaIni(String strFechaIni) {
        this.strFechaIni = strFechaIni;
    }

    public String getStrFechaFin() {
        return strFechaFin;
    }

    public void setStrFechaFin(String strFechaFin) {
        this.strFechaFin = strFechaFin;
    }

    public ArrayList<EV_Resumen_Clientes> getEV_Resumen_Clientes() {
        return EV_Resumen_Clientes;
    }

    public void setEV_Resumen_Clientes(ArrayList<EV_Resumen_Clientes> EV_Resumen_Clientes) {
        this.EV_Resumen_Clientes = EV_Resumen_Clientes;
    }
    
    
    public ReporteEV_Resumen_Clientes() {
        EV_Resumen_Clientes = new ArrayList<EV_Resumen_Clientes>();
    }

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }
// </editor-fold>
     // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void Consume_EV(int intMoneda, int intConvertido, int intEmpresa, int intSucursal, String strFechaIni, String strFechaFin) {
        CallableStatement cStmt;
        try {

            cStmt = this.oConn.getConexion().prepareCall("{call sp_getVentas_globales_resumen_clientes(?,?,?,?,?,?)}");
            cStmt.setInt(1, intMoneda);
            cStmt.setInt(2, intConvertido);
            cStmt.setInt(3, intEmpresa);
            cStmt.setInt(4, intSucursal);
            cStmt.setString(5, strFechaIni);
            cStmt.setString(6, strFechaFin);

            boolean hadResults = cStmt.execute();
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();

                // process result set
                while (rs.next()) {
                    EV_Resumen_Clientes newEV_RC = new EV_Resumen_Clientes();
                    newEV_RC.setNUM(rs.getInt("NUM"));
                    newEV_RC.setNOMBRE(rs.getString("NOMBRE"));
                    newEV_RC.setVENDEDOR(rs.getString("VENDEDOR"));
                    newEV_RC.setSC_NUM(rs.getInt("SC_NUM"));
                    newEV_RC.setSC_CLAVE(rs.getString("SC_CLAVE"));
                    newEV_RC.setSC_NOMBRE(rs.getString("SC_NOMBRE"));

                    newEV_RC.setTSUBTOTAL(rs.getDouble("TSUBTOTAL"));
                    newEV_RC.setTIMPUESTOS(rs.getDouble("TIMPUESTOS"));
                    newEV_RC.setTTOTAL(rs.getDouble("TTOTAL"));
                    newEV_RC.setTCOBROS(rs.getDouble("TCOBROS"));
                    newEV_RC.setTNOTASCREDITO(rs.getDouble("TNOTASCREDITO"));
                    newEV_RC.setTRETENCIONES(rs.getDouble("TRETENCIONES"));
                    newEV_RC.setTSALDO(rs.getDouble("TSALDO"));

                    newEV_RC.setTSUBTOTAL_PORC(rs.getDouble("TSUBTOTAL_PORC"));
                    newEV_RC.setTIMPUESTOS_PORC(rs.getDouble("TIMPUESTOSL_PORC"));
                    newEV_RC.setTTOTAL_PORC(rs.getDouble("TTOTAL_PORC"));
                    newEV_RC.setTCOBROS_PORC(rs.getDouble("TCOBROS_PORC"));
                    newEV_RC.setTNOTASCREDITO_PORC(rs.getDouble("TNOTASCREDITO_PORC"));
                    newEV_RC.setTRETENCIONES_PORC(rs.getDouble("TRETENCIONES_PORC"));
                    newEV_RC.setTSALDO_PORC(rs.getDouble("TSALDO_PORC"));

                    this.EV_Resumen_Clientes.add(newEV_RC);
                }
                rs.close();

                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String generarXML() {
        Iterator<EV_Resumen_Clientes> itDetFac = EV_Resumen_Clientes.iterator();
        UtilXml Util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXML.append("<ResumenClientes>");

        while (itDetFac.hasNext()) {
            EV_Resumen_Clientes detalle = itDetFac.next();
            strXML.append("<ResumenCliente");
            strXML.append(" NUM= \"").append(detalle.getNUM()).append("\" ");
            strXML.append(" NOMBRE= \"").append(Util.Sustituye(detalle.getNOMBRE())).append("\" ");
            strXML.append(" VENDEDOR= \"").append(detalle.getVENDEDOR()).append("\" ");
            strXML.append(" SC_NUM= \"").append(detalle.getSC_NUM()).append("\" ");
            strXML.append(" SC_CLAVE= \"").append(detalle.getSC_CLAVE()).append("\" ");
            strXML.append(" SC_NOMBRE= \"").append(detalle.getSC_NOMBRE()).append("\" ");

            strXML.append(" TSUBTOTAL= \"").append(detalle.getTSUBTOTAL()).append("\" ");
            strXML.append(" TIMPUESTOS= \"").append(detalle.getTIMPUESTOS()).append("\" ");
            strXML.append(" TTOTAL= \"").append(detalle.getTTOTAL()).append("\" ");
            strXML.append(" TCOBROS= \"").append(detalle.getTCOBROS()).append("\" ");
            strXML.append(" TNOTASCREDITO= \"").append(detalle.getTNOTASCREDITO()).append("\" ");
            strXML.append(" TRETENCIONES= \"").append(detalle.getTRETENCIONES()).append("\" ");
            strXML.append(" TSALDO= \"").append(detalle.getTSALDO()).append("\" ");

            strXML.append(" TSUBTOTAL_PORC= \"").append(detalle.getTSUBTOTAL_PORC()).append("\" ");
            strXML.append(" TIMPUESTOS_PORC= \"").append(detalle.getTIMPUESTOS_PORC()).append("\" ");
            strXML.append(" TTOTAL_PORC= \"").append(detalle.getTTOTAL_PORC()).append("\" ");
            strXML.append(" TCOBROS_PORC= \"").append(detalle.getTCOBROS_PORC()).append("\" ");
            strXML.append(" TNOTASCREDITO_PORC= \"").append(detalle.getTNOTASCREDITO_PORC()).append("\" ");
            strXML.append(" TRETENCIONES_PORC= \"").append(detalle.getTRETENCIONES_PORC()).append("\" ");
            strXML.append(" TSALDO_PORC= \"").append(detalle.getTSALDO_PORC()).append("\" ");
            strXML.append("/>");
        }
        strXML.append("</ResumenClientes>");
        return strXML.toString();
    }
    
    public void GeneraPDF(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, this.strFechaIni, this.strFechaFin);
        //Generamos el pdf
        InputStream reportStream = null;
        String strMoneda = "";
        String strSql2 = "select vta_monedas.MON_DESCRIPCION from vta_monedas where vta_monedas.MON_ID  =" + this.intMoneda;
        ResultSet rs2;
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strMoneda = rs2.getString("MON_DESCRIPCION");
            }
            rs2.close();
        } catch (SQLException ex) {
         Logger.getLogger(ReporteEV_Resumen_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        String strConvertido="";
        if(this.intConvertido == 1)
        {
            strConvertido ="SI";
        }
        else
        {
            strConvertido ="NO";
        }
        strSql2 = "select EMP_RAZONSOCIAL from vta_empresas where vta_empresas.EMP_ID  =" + this.intEmpId;
        String strEMP="";
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strEMP = rs2.getString("EMP_RAZONSOCIAL");
            }
            rs2.close();
        } catch (SQLException ex) {
         Logger.getLogger(ReporteEV_Resumen_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        strSql2 = "select SC_NOMBRE from vta_sucursal where vta_sucursal.SC_ID  =" + this.intScId;
        String strSC="";
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strSC = rs2.getString("SC_NOMBRE");
            }
            rs2.close();
        } catch (SQLException ex) {
         Logger.getLogger(ReporteEV_Resumen_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
//            log.debug("Comenzamos a generar el reporte jrxml");


            //Parametros
            Map parametersMap = new HashMap();
//            log.debug("Parametros");
            parametersMap.put("strMoneda", strMoneda);
            parametersMap.put("strConvertido", strConvertido);
            parametersMap.put("strEmp", strEMP);
            parametersMap.put("strSc", strSC);
            parametersMap.put("strFechaIni", this.strFechaIni);
            parametersMap.put("strFechaFin", this.strFechaFin);
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.EV_Resumen_Clientes, true);
            // Compile and print the jasper report
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);
            System.out.println("Binario: " + print);
//            log.debug("Salida de reporte " + targetFileName);

            //Dependiendo del parametro lo guardamos localmente o lo enviamos en un hilo
            if (byteArrayOutputStream == null) {
                JasperExportManager.exportReportToPdfFile(print, targetFileName);
            } else {
                JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
            }
            log.debug("Termina generacion de reporte ");

        } catch (FileNotFoundException ex) {
           log.error(ex.getMessage());
        } catch (JRException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
           log.error(ex.getMessage());
        } finally {
            try {
                reportStream.close();
            } catch (IOException ex) {
              log.error(ex.getMessage());
            }
        }
    }
    
    public void GeneraXLS(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, this.strFechaIni, this.strFechaFin);
        //Generamos el pdf
        InputStream reportStream = null;
        String strMoneda = "";
        String strSql2 = "select vta_monedas.MON_DESCRIPCION from vta_monedas where vta_monedas.MON_ID  =" + this.intMoneda;
        ResultSet rs2;
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strMoneda = rs2.getString("MON_DESCRIPCION");
            }
            rs2.close();
        } catch (SQLException ex) {
         Logger.getLogger(ReporteEV_Resumen_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        String strConvertido="";
        if(this.intConvertido == 1)
        {
            strConvertido ="SI";
        }
        else
        {
            strConvertido ="NO";
        }
        strSql2 = "select EMP_RAZONSOCIAL from vta_empresas where vta_empresas.EMP_ID  =" + this.intEmpId;
        String strEMP="";
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strEMP = rs2.getString("EMP_RAZONSOCIAL");
            }
            rs2.close();
        } catch (SQLException ex) {
         Logger.getLogger(ReporteEV_Resumen_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        strSql2 = "select SC_NOMBRE from vta_sucursal where vta_sucursal.SC_ID  =" + this.intScId;
        String strSC="";
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strSC = rs2.getString("SC_NOMBRE");
            }
            rs2.close();
        } catch (SQLException ex) {
         Logger.getLogger(ReporteEV_Resumen_Clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
//            log.debug("Comenzamos a generar el reporte jrxml");


            //Parametros
            Map parametersMap = new HashMap();
//            log.debug("Parametros");
            parametersMap.put("strMoneda", strMoneda);
            parametersMap.put("strConvertido", strConvertido);
            parametersMap.put("strEmp", strEMP);
            parametersMap.put("strSc", strSC);
            parametersMap.put("strFechaIni", this.strFechaIni);
            parametersMap.put("strFechaFin", this.strFechaFin);
            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.EV_Resumen_Clientes, true);
            // Compile and print the jasper report
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);
            System.out.println("Binario: " + print);
           log.debug("Salida de reporte " + targetFileName);

            //Dependiendo del parametro lo guardamos localmente o lo enviamos en un hilo
            
                JRXlsExporter exporterXLS = new JRXlsExporter();
               exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
               exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
               exporterXLS.exportReport();
                
           
//            log.debug("Termina generacion de reporte ");

        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
        } catch (JRException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                reportStream.close();
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
    }
    // </editor-fold>
}
