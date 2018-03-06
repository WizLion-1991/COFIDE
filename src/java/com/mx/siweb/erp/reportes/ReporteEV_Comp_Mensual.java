/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;


import com.mx.siweb.erp.reportes.entities.EV_Comp_Mensual;
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
public class ReporteEV_Comp_Mensual {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String sourceFileName;
    private String targetFileName;
    private int intMoneda;
    private int intConvertido;
    private int intEmpId;
    private int intScId;
    private String Anio;
    
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReporteEV_Comp_Mensual.class.getName());
    
    Conexion oConn;
    public ArrayList<EV_Comp_Mensual> lst_Comp_Mensual;

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

    public String getAnio() {
        return Anio;
    }

    public void setAnio(String Anio) {
        this.Anio = Anio;
    }

    public ArrayList<EV_Comp_Mensual> getEV_Comp_Mensual() {
        return lst_Comp_Mensual;
    }

    public void setEV_Comp_Mensual(ArrayList<EV_Comp_Mensual> EV_Comp_Mensual) {
        this.lst_Comp_Mensual = EV_Comp_Mensual;
    }
    
    
    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

    public ReporteEV_Comp_Mensual()
    {
        lst_Comp_Mensual = new ArrayList<EV_Comp_Mensual>();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void Consume_EV(int intMoneda, int intConvertido, int intEmpresa, int intSucursal, int strFechaIni) {
        CallableStatement cStmt;
        try {
            log.debug(intMoneda+":"+intConvertido+":"+intEmpresa+":"+intSucursal+":"+strFechaIni);
            cStmt = this.oConn.getConexion().prepareCall("{call sp_getVentas_globales_comparativo_mensual(?,?,?,?,?)}");
            cStmt.setInt(1, intMoneda);
            cStmt.setInt(2, intConvertido);
            cStmt.setInt(3, intEmpresa);
            cStmt.setInt(4, intSucursal);
            cStmt.setInt(5, strFechaIni);            
            
            boolean hadResults = cStmt.execute();
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();

                // process result set
                while (rs.next()) {
                    EV_Comp_Mensual newEV_CM = new EV_Comp_Mensual();
                    String srtEntrada ="";
                    newEV_CM.setNum(rs.getInt("NUM"));                    
                    newEV_CM.setNOMBRE(rs.getString("NOMBRE"));
                    if(rs.getString("CATEGORIA1") != null)
                    {
                        srtEntrada = rs.getString("CATEGORIA1");
                    }
                    newEV_CM.setTipo(srtEntrada);
                                        
                    srtEntrada ="";
                    if(rs.getString("CATEGORIA2") != null)
                    {
                        srtEntrada = rs.getString("CATEGORIA2");
                    }
                    newEV_CM.setZona(srtEntrada);                    
                    log.debug("Tipo:"+newEV_CM.getTipo()+" Zona:"+newEV_CM.getZona());
                    newEV_CM.setENERO(rs.getDouble("ENERO"));
                    newEV_CM.setFEBRERO(rs.getDouble("FEBRERO"));
                    newEV_CM.setMARZO(rs.getDouble("MARZO"));
                    newEV_CM.setABRIL(rs.getDouble("ABRIL"));
                    newEV_CM.setMAYO(rs.getDouble("MAYO"));
                    newEV_CM.setJUNIO(rs.getDouble("JUNIO"));
                    newEV_CM.setJULIO(rs.getDouble("JULIO"));
                    newEV_CM.setAGOSTO(rs.getDouble("AGOSTO"));
                    newEV_CM.setSEPTIEMBRE(rs.getDouble("SEPTIEMBRE"));
                    newEV_CM.setOCTUBRE(rs.getDouble("OCTUBRE"));
                    newEV_CM.setNOVIEMBRE(rs.getDouble("NOVIEMBRE"));
                    newEV_CM.setDICIEMBRE(rs.getDouble("DICIEMBRE"));
                    newEV_CM.setTTOTAL(rs.getDouble("TTOTAL"));
                    newEV_CM.setENERO_PORC(rs.getDouble("ENERO_PORC"));
                    newEV_CM.setFEBRERO_PORC(rs.getDouble("FEBRERO_PORC"));
                    newEV_CM.setMARZO_PORC(rs.getDouble("MARZO_PORC"));
                    newEV_CM.setABRIL_PORC(rs.getDouble("ABRIL_PORC"));
                    newEV_CM.setMAYO_PORC(rs.getDouble("MAYO_PORC"));
                    newEV_CM.setJUNIO_PORC(rs.getDouble("JUNIO_PORC"));
                    newEV_CM.setJULIO_PORC(rs.getDouble("JULIO_PORC"));
                    newEV_CM.setAGOSTO_PORC(rs.getDouble("AGOSTO_PORC"));
                    newEV_CM.setSEPTIEMBRE_PORC(rs.getDouble("SEPTIEMBRE_PORC"));
                    newEV_CM.setOCTUBRE_PORC(rs.getDouble("OCTUBRE_PORC"));
                    newEV_CM.setNOVIEMBRE_PORC(rs.getDouble("NOVIEMBRE_PORC"));
                    newEV_CM.setDICIEMBRE_PORC(rs.getDouble("DICIEMBRE_PORC"));
                    newEV_CM.setTTOTAL_PORC(rs.getDouble("TTOTAL_PORC"));
                    this.lst_Comp_Mensual.add(newEV_CM);
                }
                rs.close();

                hadResults = cStmt.getMoreResults();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String generarXML() {
        Iterator<EV_Comp_Mensual> itDetFac = lst_Comp_Mensual.iterator();
        UtilXml Util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXML.append("<ComparacionMensuales>");

        while (itDetFac.hasNext()) {
            EV_Comp_Mensual detalle = itDetFac.next();
            strXML.append("<ComparacionMensual");
            strXML.append(" NUM= \"").append(detalle.getNum()).append("\" ");
            strXML.append(" NOMBRE= \"").append(Util.Sustituye(detalle.getNOMBRE())).append("\" ");
            strXML.append(" ENERO= \"").append(detalle.getENERO()).append("\" ");
            strXML.append(" Tipo= \"").append(detalle.getTipo()).append("\" ");
            strXML.append(" Zona= \"").append(detalle.getZona()).append("\" ");
            strXML.append(" FEBRERO= \"").append(detalle.getFEBRERO()).append("\" ");
            strXML.append(" MARZO= \"").append(detalle.getMARZO()).append("\" ");
            strXML.append(" ABRIL= \"").append(detalle.getABRIL()).append("\" ");
            strXML.append(" MAYO= \"").append(detalle.getMAYO()).append("\" ");
            strXML.append(" JUNIO= \"").append(detalle.getJUNIO()).append("\" ");
            strXML.append(" JULIO= \"").append(detalle.getJULIO()).append("\" ");
            strXML.append(" AGOSTO= \"").append(detalle.getAGOSTO()).append("\" ");
            strXML.append(" SEPTIEMBRE= \"").append(detalle.getSEPTIEMBRE()).append("\" ");
            strXML.append(" OCTUBRE= \"").append(detalle.getOCTUBRE()).append("\" ");
            strXML.append(" NOVIEMBRE= \"").append(detalle.getNOVIEMBRE()).append("\" ");
            strXML.append(" DICIEMBRE= \"").append(detalle.getDICIEMBRE()).append("\" ");
            strXML.append(" TTOTAL= \"").append(detalle.getTTOTAL()).append("\" ");
            strXML.append(" ENERO_PORC= \"").append(detalle.getENERO_PORC()).append("\" ");
            strXML.append(" FEBRERO_PORC= \"").append(detalle.getFEBRERO_PORC()).append("\" ");
            strXML.append(" MARZO_PORC= \"").append(detalle.getMARZO_PORC()).append("\" ");
            strXML.append(" ABRIL_PORC= \"").append(detalle.getABRIL_PORC()).append("\" ");
            strXML.append(" MAYO_PORC= \"").append(detalle.getMAYO_PORC()).append("\" ");
            strXML.append(" JUNIO_PORC= \"").append(detalle.getJUNIO_PORC()).append("\" ");
            strXML.append(" JULIO_PORC= \"").append(detalle.getJULIO_PORC()).append("\" ");
            strXML.append(" AGOSTO_PORC= \"").append(detalle.getAGOSTO_PORC()).append("\" ");
            strXML.append(" SEPTIEMBRE_PORC= \"").append(detalle.getSEPTIEMBRE_PORC()).append("\" ");
            strXML.append(" OCTUBRE_PORC= \"").append(detalle.getOCTUBRE_PORC()).append("\" ");
            strXML.append(" NOVIEMBRE_PORC= \"").append(detalle.getNOVIEMBRE_PORC()).append("\" ");
            strXML.append(" DICIEMBRE_PORC= \"").append(detalle.getDICIEMBRE_PORC()).append("\" ");            
            strXML.append(" TTOTAL_PORC= \"").append(detalle.getTTOTAL_PORC()).append("\" ");
            
            strXML.append("/>");
        }
        strXML.append("</ComparacionMensuales>");
        return strXML.toString();
    }
    
    public void GeneraPDF(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {
        log.debug(this.intMoneda+":"+ this.intConvertido+":"+ this.intEmpId+":"+ this.intScId+":"+ Integer.valueOf(this.Anio));
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, Integer.valueOf(this.Anio));
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
         Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
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
            parametersMap.put("strAnio", this.Anio);
            
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.lst_Comp_Mensual, true);
            // Compile and print the jasper report
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);
            
//            log.debug("Salida de reporte " + targetFileName);

            //Dependiendo del parametro lo guardamos localmente o lo enviamos en un hilo
            if (byteArrayOutputStream == null) {
                JasperExportManager.exportReportToPdfFile(print, targetFileName);
            } else {
                JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
            }
//            log.debug("Termina generacion de reporte ");

        } catch (FileNotFoundException ex) {
            log.error("(1)" + ex.getMessage());
        } catch (JRException ex) {
            log.error("(2)" + ex.getMessage());
        } catch (Exception ex) {
            log.error("(3)" + ex.getMessage());
        } finally {
            try {
                reportStream.close();
            } catch (IOException ex) {
                log.error("(4)" + ex.getMessage());
            }
        }
    }
    
    public void GeneraXLS(VariableSession varSesiones, ByteArrayOutputStream byteArrayOutputStream) {
        log.debug(this.intMoneda+":"+ this.intConvertido+":"+ this.intEmpId+":"+ this.intScId+":"+ Integer.valueOf(this.Anio));
        this.Consume_EV(this.intMoneda, this.intConvertido, this.intEmpId, this.intScId, Integer.valueOf(this.Anio));
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
//         Logger.getLogger(EstadoCuentaCliente.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
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
         Logger.getLogger(ReporteEV_Comp_Mensual.class.getName()).log(Level.SEVERE, null, ex);
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
            parametersMap.put("strAnio", this.Anio);
            
//            log.debug("Llenado de jasper " + this.sourceFileName);

            reportStream = new FileInputStream(this.sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.lst_Comp_Mensual, true);
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
