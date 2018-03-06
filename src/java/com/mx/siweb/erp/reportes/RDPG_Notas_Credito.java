/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.*;
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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author siweb
 */
public class RDPG_Notas_Credito {

    private String periodo;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(RDPG_Notas_Credito.class.getName());
    private ArrayList<RDPG_Notas_CreditoE> entidades;
    private Conexion oConn;
    private boolean bolImpreso;
    private ResultSet rs2;
    private String emp_nom;
    private String sc_id;
    private String mon_id;
    private String sc_nombre;
    private String mon_nom;
    private int convertido;
    private int emp_id;
    private org.w3c.dom.Document doc;

    public RDPG_Notas_Credito(String periodo, Conexion oConn, VariableSession varSesiones) {
        this.emp_id = varSesiones.getIntIdEmpresa();
        this.periodo = periodo;
        this.oConn = oConn;
        this.entidades = new ArrayList<RDPG_Notas_CreditoE>();
        log.debug(emp_id + " " + periodo);

    }

    public void loadXMLFrom(java.io.InputStream is)
            throws org.xml.sax.SAXException, java.io.IOException {
        javax.xml.parsers.DocumentBuilderFactory factory
                = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        javax.xml.parsers.DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException ex) {
            log.error(ex.getLocalizedMessage());
        }
        org.w3c.dom.Document doc = builder.parse(is);
        doc.setStrictErrorChecking(false);
        is.close();
        this.doc = doc;
    }

    private String getIdPoliza(String idPol) {

        String idPoliza = new String();
        /////////////////////////////////Se queda en esta instruccion
        NodeList nList = doc.getDocumentElement().getChildNodes();
        
        if (nList != null) {
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                                Node nNode = nList.item(temp);
                if (nNode.getNodeName().equals("row")) {
                    if (Integer.parseInt(nNode.getAttributes().getNamedItem("id").getTextContent()) == Integer.parseInt(idPol)) {
                        idPoliza = nNode.getChildNodes().item(2).getTextContent() + " " + nNode.getChildNodes().item(1).getTextContent();
                    }
                }
            }
        }
       
        return idPoliza;
    }

    public void HacerReporte() {
        try {
            CallableStatement cStmt;

            String stmt = "{call sp_getContaNotasCredito(" + emp_id + "," + periodo + ")};";
            log.debug("{call sp_getContaNotasCredito(" + emp_id + "," + periodo + ")};");
           
            cStmt = oConn.getConexion().prepareCall(stmt);
            
            boolean hadResults = cStmt.execute();
           
            while (hadResults) {
              
                ResultSet rs = cStmt.getResultSet();
              
                while (rs.next()) {
                  
                    RDPG_Notas_CreditoE e = new RDPG_Notas_CreditoE();
                    
                    e.setId(rs.getInt("id"));                    
                    e.setFolio(rs.getString("folio"));                    
                    e.setFecha(rs.getString("fecha"));                  
                    e.setNombre(rs.getString("razon_social"));                   
                    e.setImporte(rs.getDouble("subtotal"));
                    e.setMoneda(rs.getString("Moneda"));                
//Se queda en esta instruccion esta comentariada 
                    e.setPolizaGenerada(this.getIdPoliza(rs.getString("idPoliza")));                
         
                    entidades.add(e);

                }

                hadResults = cStmt.getMoreResults();
                rs.close();

            }
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    public String GeneraXml() {
        Iterator<RDPG_Notas_CreditoE> it = entidades.iterator();
        UtilXml util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<Notas_Credito>");
        while (it.hasNext()) {
            RDPG_Notas_CreditoE entidad = it.next();

            strXML.append(" <Notas_Credito");
            strXML.append(" Id= \"").append(entidad.getId()).append("\" ");
            strXML.append(" Folio= \"").append(util.Sustituye(entidad.getFolio())).append("\" ");
            strXML.append(" Fecha= \"").append(entidad.getFecha()).append("\" ");
            strXML.append(" Importe= \"").append(entidad.getImporte()).append("\" ");
            strXML.append(" Moneda= \"").append(entidad.getMoneda()).append("\" ");
            strXML.append(" Nombre= \"").append(util.Sustituye(entidad.getNombre())).append("\" ");
            strXML.append(" PolizaGenerada= \"").append(entidad.getPolizaGenerada()).append("\" ");
            strXML.append("/>");
        }
        strXML.append("</Notas_Credito>");
        log.debug(strXML.toString());

        return strXML.toString();
    }

    public void getReportPrint(String sourceFileName, String targetFileName, VariableSession varSesiones,
            ByteArrayOutputStream byteArrayOutputStream, int frmt, String strPathLogoWeb) {
        this.bolImpreso = true;

        //Logo de la empresa
        String strSql2 = "select EMP_PATHIMG from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strPathLogoWeb += System.getProperty("file.separator") + rs2.getString("EMP_PATHIMG");
            }
            rs2.close();
        } catch (SQLException ex) {
            log.error(ex);
        }

        strSql2 = "select EMP_PATHIMGFORM,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                emp_nom = rs2.getString("EMP_RAZONSOCIAL");
            }
            rs2.close();
        } catch (SQLException ex) {
            log.error(ex);
        }
        String strBodega = "SELECT SC_NOMBRE FROM vta_sucursal WHERE SC_ID=" + sc_id;
        try {
            rs2 = this.oConn.runQuery(strBodega, true);
            while (rs2.next()) {
                sc_nombre = rs2.getString("SC_NOMBRE");
            }
            rs2.close();
        } catch (SQLException ex) {
            log.error(ex);
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
            log.error(ex);
        }
        //Generamos el pdf
        InputStream reportStream = null;
        try {
            //Parametros
            Map parametersMap = new HashMap();

            parametersMap.put("PathLogoWeb", strPathLogoWeb);
            parametersMap.put("empresa", emp_nom);
            parametersMap.put("periodo", periodo);

            reportStream = new FileInputStream(sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.entidades, true);
            // Compile and print the jasper report
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);

            reportStream.close();

            switch (frmt) {
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
