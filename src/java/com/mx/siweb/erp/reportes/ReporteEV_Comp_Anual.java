/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.EV_Comp_Anual;
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
public class ReporteEV_Comp_Anual {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String sourceFileName;
    private String targetFileName;
    private int intMoneda;
    private int intConvertido;
    private int intEmpId;
    private int intScId;
    private String strAnioIni;
    private String strAnioFin;
    
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteEV_Comp_Anual.class.getName());
    
    Conexion oConn;
    private ArrayList<EV_Comp_Anual> EV_Comp_Anual;

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

    public String getStrAnioIni() {
        return strAnioIni;
    }

    public void setStrAnioIni(String strAnioIni) {
        this.strAnioIni = strAnioIni;
    }

    public String getStrAnioFin() {
        return strAnioFin;
    }

    public void setStrAnioFin(String strAnioFin) {
        this.strAnioFin = strAnioFin;
    }

    public ArrayList<EV_Comp_Anual> getEV_Comp_Anual() {
        return EV_Comp_Anual;
    }

    public void setEV_Comp_Anual(ArrayList<EV_Comp_Anual> EV_Comp_Anual) {
        this.EV_Comp_Anual = EV_Comp_Anual;
    }
    
    
    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }
    public ReporteEV_Comp_Anual()
    {
        EV_Comp_Anual = new ArrayList<EV_Comp_Anual>();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void Consume_EV(int intMoneda, int intConvertido, int intEmpresa, int intSucursal, int strFechaIni, int strFechaFin) {
        CallableStatement cStmt;
        log.debug(intMoneda+":"+intConvertido+":"+intEmpresa+":"+intSucursal+":"+strFechaIni+":"+strFechaFin);
        
        try {
 
            cStmt = this.oConn.getConexion().prepareCall("{call sp_getVentas_globales_comparativo_anual(?,?,?,?,?,?)}");
            cStmt.setInt(1, intMoneda);
            cStmt.setInt(2, intConvertido);
            cStmt.setInt(3, intEmpresa);
            cStmt.setInt(4, intSucursal);
            cStmt.setInt(5, strFechaIni);
            cStmt.setInt(6, strFechaFin);

            boolean hadResults = cStmt.execute();
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();

                // process result set
                while (rs.next()) {
                    EV_Comp_Anual newEV_CA = new EV_Comp_Anual();                    
                    newEV_CA.setNum(rs.getInt("Num"));
                    newEV_CA.setNombre(rs.getString("Nombre"));
                    newEV_CA.setTipo(rs.getString("Tipo"));
                    newEV_CA.setZona(rs.getString("Zona"));
                    newEV_CA.setAnio1(rs.getDouble("Anio1"));
                    newEV_CA.setAnio2(rs.getDouble("Anio2"));
                    newEV_CA.setAnio3(rs.getDouble("Anio3"));
                    newEV_CA.setAnio4(rs.getDouble("Anio4"));
                    newEV_CA.setAnio5(rs.getDouble("Anio5"));
                    newEV_CA.setAnio6(rs.getDouble("Anio6"));
                    newEV_CA.setAnio7(rs.getDouble("Anio7"));
                    newEV_CA.setAnio8(rs.getDouble("Anio8"));
                    newEV_CA.setAnio9(rs.getDouble("Anio9"));
                    newEV_CA.setAnio10(rs.getDouble("Anio10"));
                    newEV_CA.setAnioTot(rs.getDouble("AnioTot"));
                    newEV_CA.setAnioP1(rs.getDouble("AnioP1"));
                    newEV_CA.setAnioP2(rs.getDouble("AnioP2"));
                    newEV_CA.setAnioP3(rs.getDouble("AnioP3"));
                    newEV_CA.setAnioP4(rs.getDouble("AnioP4"));
                    newEV_CA.setAnioP5(rs.getDouble("AnioP5"));
                    newEV_CA.setAnioP6(rs.getDouble("AnioP6"));
                    newEV_CA.setAnioP7(rs.getDouble("AnioP7"));
                    newEV_CA.setAnioP8(rs.getDouble("AnioP8"));
                    newEV_CA.setAnioP9(rs.getDouble("AnioP9"));
                    newEV_CA.setAnioP10(rs.getDouble("AnioP10"));
                    newEV_CA.setAnioPTot(rs.getDouble("AnioPTot"));
                    this.EV_Comp_Anual.add(newEV_CA);
                }
                rs.close();

                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String generarXML() {
        Iterator<EV_Comp_Anual> itDetFac = EV_Comp_Anual.iterator();
        UtilXml Util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXML.append("<ComparacionAnuales>");

        while (itDetFac.hasNext()) {
            EV_Comp_Anual detalle = itDetFac.next();
            strXML.append("<ComparacionAnual");
            strXML.append(" Num= \"").append(detalle.getNum()).append("\" ");
            strXML.append(" Nombre= \"").append(Util.Sustituye(detalle.getNombre())).append("\" ");
            strXML.append(" Tipo= \"").append(detalle.getTipo()).append("\" ");
            strXML.append(" Zona= \"").append(detalle.getZona()).append("\" ");
            
            strXML.append(" Anio1= \"").append(detalle.getAnio1()).append("\" ");
            strXML.append(" Anio2= \"").append(detalle.getAnio2()).append("\" ");
            strXML.append(" Anio3= \"").append(detalle.getAnio3()).append("\" ");
            strXML.append(" Anio4= \"").append(detalle.getAnio4()).append("\" ");
            strXML.append(" Anio5= \"").append(detalle.getAnio5()).append("\" ");
            strXML.append(" Anio6= \"").append(detalle.getAnio6()).append("\" ");
            strXML.append(" Anio7= \"").append(detalle.getAnio7()).append("\" ");
            strXML.append(" Anio8= \"").append(detalle.getAnio8()).append("\" ");
            strXML.append(" Anio9= \"").append(detalle.getAnio9()).append("\" ");
            strXML.append(" Anio10= \"").append(detalle.getAnio10()).append("\" ");
            strXML.append(" AnioTot= \"").append(detalle.getAnioTot()).append("\" ");
            
            strXML.append(" AnioP1= \"").append(detalle.getAnioP1()).append("\" ");
            strXML.append(" AnioP2= \"").append(detalle.getAnioP2()).append("\" ");
            strXML.append(" AnioP3= \"").append(detalle.getAnioP3()).append("\" ");
            strXML.append(" AnioP4= \"").append(detalle.getAnioP4()).append("\" ");
            strXML.append(" AnioP5= \"").append(detalle.getAnioP5()).append("\" ");
            strXML.append(" AnioP6= \"").append(detalle.getAnioP6()).append("\" ");
            strXML.append(" AnioP7= \"").append(detalle.getAnioP7()).append("\" ");
            strXML.append(" AnioP8= \"").append(detalle.getAnioP8()).append("\" ");
            strXML.append(" AnioP9= \"").append(detalle.getAnioP9()).append("\" ");
            strXML.append(" AnioP10= \"").append(detalle.getAnioP10()).append("\" ");
            strXML.append(" AnioPTot= \"").append(detalle.getAnioPTot()).append("\" ");
            
            strXML.append("/>");
        }
        strXML.append("</ComparacionAnuales>");
        return strXML.toString();
    }
    
    public void GeneraPDF(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {        
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, Integer.valueOf(this.strAnioIni),Integer.valueOf(this.strAnioFin));
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
         Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
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
            parametersMap.put("strAnioIni", this.strAnioIni);
            parametersMap.put("strAnioFin", this.strAnioFin);
            
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.EV_Comp_Anual, true);
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
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, Integer.valueOf(this.strAnioIni),Integer.valueOf(this.strAnioFin));
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
         Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
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
            parametersMap.put("strAnioIni", this.strAnioIni);
            parametersMap.put("strAnioFin", this.strAnioFin);
            
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.EV_Comp_Anual, true);
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
