/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.EV_Cliente_MVenta;
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
public class ReporteEV_Cliente_MVenta {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    private String sourceFileName;
    private String targetFileName;
    private int intMoneda;
    private int intConvertido;
    private int intEmpId;
    private int intScId;
    private int intCuantos;
    private String strFechaIni;
    private String strFechaFin;
    private Conexion oConn;
    private ArrayList<EV_Cliente_MVenta> EV_Cliente_MVenta;
    
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteEV_Cliente_MVenta.class.getName());
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

    public int getIntCuantos() {
        return intCuantos;
    }

    public void setIntCuantos(int intCuantos) {
        this.intCuantos = intCuantos;
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

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    public ReporteEV_Cliente_MVenta() {
        EV_Cliente_MVenta = new ArrayList<EV_Cliente_MVenta>();
    }

    public void Consume_EV(int intMoneda, int intConvertido, int intEmpresa, int intSucursal, String strFechaIni, String strFechaFin, int intNumeroClientes) {
        CallableStatement cStmt;
        try {

            cStmt = this.oConn.getConexion().prepareCall("{call sp_getVentas_globales_mayor_venta(?,?,?,?,?,?,?)}");
            cStmt.setInt(1, intMoneda);
            cStmt.setInt(2, intConvertido);
            cStmt.setInt(3, intEmpresa);
            cStmt.setInt(4, intSucursal);
            cStmt.setString(5, strFechaIni);
            cStmt.setString(6, strFechaFin);
            cStmt.setInt(7, intNumeroClientes);

            boolean hadResults = cStmt.execute();
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();

                // process result set
                while (rs.next()) {
                    EV_Cliente_MVenta newEV_MV = new EV_Cliente_MVenta();
                    newEV_MV.setNUM(rs.getInt("NUM"));
                    newEV_MV.setNOMBRE(rs.getString("NOMBRE"));
                    newEV_MV.setTSUBTOTAL(rs.getDouble("TSUBTOTAL"));
                    newEV_MV.setTSUBTOTAL_PORC(rs.getDouble("TSUBTOTAL_PORC"));

                    this.EV_Cliente_MVenta.add(newEV_MV);
                }
                rs.close();

                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String generarXML() {
        Iterator<EV_Cliente_MVenta> itDetFac = EV_Cliente_MVenta.iterator();
        UtilXml Util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXML.append("<ClienteMVentas>");

        while (itDetFac.hasNext()) {
            EV_Cliente_MVenta detalle = itDetFac.next();
            strXML.append("<ClienteMVenta");
            strXML.append(" NUM= \"").append(detalle.getNUM()).append("\" ");
            strXML.append(" NOMBRE= \"").append(Util.Sustituye(detalle.getNOMBRE())).append("\" ");
            strXML.append(" TSUBTOTAL= \"").append(detalle.getTSUBTOTAL()).append("\" ");
            strXML.append(" TSUBTOTAL_PORC= \"").append(detalle.getTSUBTOTAL_PORC()).append("\" ");
            strXML.append("/>");
        }
        strXML.append("</ClienteMVentas>");
        return strXML.toString();
    }

    public void GeneraPDF(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, this.strFechaIni, this.strFechaFin, this.intCuantos);
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
         Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
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
            parametersMap.put("intCuantos", this.intCuantos);
            System.out.println("Clase:" + intMoneda + ":" + intConvertido + ":" + intEmpId + ":" + intEmpId + ":" + intScId + ":" + strFechaIni + ":" + strFechaFin);
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.EV_Cliente_MVenta, true);
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
    
    public void GeneraXLS(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, this.strFechaIni, this.strFechaFin, this.intCuantos);
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
         Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Cliente_MVenta.class.getName()).log(Level.SEVERE, null, ex);
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
            parametersMap.put("intCuantos", this.intCuantos);
            System.out.println("Clase:" + intMoneda + ":" + intConvertido + ":" + intEmpId + ":" + intEmpId + ":" + intScId + ":" + strFechaIni + ":" + strFechaFin);
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.EV_Cliente_MVenta, true);
            // Compile and print the jasper report
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);
            System.out.println("Binario: " + print);
//            log.debug("Salida de reporte " + targetFileName);

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
