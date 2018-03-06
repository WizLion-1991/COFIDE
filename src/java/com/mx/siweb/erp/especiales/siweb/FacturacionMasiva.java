/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.siweb;

import ERP.ProcesoInterfaz;
import ERP.ProcesoMaster;
import ERP.Ticket;
import Tablas.vta_facturadeta;
import com.mx.siweb.erp.especiales.siweb.entities.ClienteporFacturar;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
import org.apache.struts.util.MessageResources;

/**
 * Calcula la facturacion que debe generarse
 *
 * @author ZeusGalindo
 */
public class FacturacionMasiva extends ProcesoMaster implements ProcesoInterfaz {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    String strAnio;
    String strMes;
    int intMes = 0;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(FacturacionMasiva.class.getName());
    ArrayList<ClienteporFacturar> lstClientes;
    protected int intEMP_ID = 0;
    protected int intSC_ID = 0;
    protected ServletContext context;
    protected String strPathPrivateKeys;
    protected String strPathXML;
    protected String strPathFonts;
    protected String strMyPassSecret;
    private MessageResources msgRes;
    private boolean bolImpreso;
    private ResultSet rs2;
    private String emp_nom;
    private String periodo;
    private int sc_id;
    private String sc_nombre;

    public String getStrAnio() {
        return strAnio;
    }

    public void setStrAnio(String strAnio) {
        this.strAnio = strAnio;
    }

    public String getStrMes() {
        return strMes;
    }

    public void setStrMes(String strMes) {
        this.strMes = strMes;
    }

    public int getIntMes() {
        return intMes;
    }

    public void setIntMes(int intMes) {
        this.intMes = intMes;
    }

    public ArrayList<ClienteporFacturar> getLstClientes() {
        return lstClientes;
    }

    public int getIntEMP_ID() {
        return intEMP_ID;
    }

    public void setIntEMP_ID(int intEMP_ID) {
        this.intEMP_ID = intEMP_ID;
    }

    public int getIntSC_ID() {
        return intSC_ID;
    }

    public void setIntSC_ID(int intSC_ID) {
        this.intSC_ID = intSC_ID;
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public String getStrPathPrivateKeys() {
        return strPathPrivateKeys;
    }

    public void setStrPathPrivateKeys(String strPathPrivateKeys) {
        this.strPathPrivateKeys = strPathPrivateKeys;
    }

    public String getStrPathXML() {
        return strPathXML;
    }

    public void setStrPathXML(String strPathXML) {
        this.strPathXML = strPathXML;
    }

    public String getStrPathFonts() {
        return strPathFonts;
    }

    public void setStrPathFonts(String strPathFonts) {
        this.strPathFonts = strPathFonts;
    }

    public String getStrMyPassSecret() {
        return strMyPassSecret;
    }

    public void setStrMyPassSecret(String strMyPassSecret) {
        this.strMyPassSecret = strMyPassSecret;
    }

    public MessageResources getMsgRes() {
        return msgRes;
    }

    public void setMsgRes(MessageResources msgRes) {
        this.msgRes = msgRes;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public FacturacionMasiva(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
        super(oConn, varSesiones, request);
        lstClientes = new ArrayList<ClienteporFacturar>();
    }

    public FacturacionMasiva(Conexion oConn, VariableSession varSesiones) {
        super(oConn, varSesiones);
        lstClientes = new ArrayList<ClienteporFacturar>();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void Init() {
    }

    public void doTrx() {
        this.strResultLast = "OK";
        int intMesFiltroIni = this.intMes;
        int intMesFiltroFin = this.intMes;
        //Validamos si buscaremos la informacion de un solo mes
        if (intMesFiltroIni == 0) {
            intMesFiltroIni = 1;
        }
        if (intMesFiltroFin == 0) {
            intMesFiltroFin = 12;
        }
        //Recorremos todos los meses del anio
        for (int i = intMesFiltroIni; i <= intMesFiltroFin; i++) {
            String strMes = "0" + i;
            if (i >= 10) {
                strMes = "" + i;
            }
            //Obtenemos las facturas mensuales         
            String strSql1 = "SELECT vta_contrato_arrend.CTOA_ID, \n"
               + "	vta_contrato_arrend.CTE_ID, \n"
               + "	vta_contrato_arrend.CTOA_MONEDA, \n"
               + "	vta_contrato_arrend.CTE_NOMBRE, 	\n"
               + "	vta_contrato_arrend.CTOA_MENSUALIDAD_ACTUAL, 	\n"
               + "	vta_contrato_arrend.CTOA_ARRENDAMIENTO, \n"
               + "	vta_contrato_arrend.CTOA_VENCIMIENTO, 	\n"
               + "	vta_contrato_arrend.CTOA_MTO_ARRENDAMIENTO, \n"
               + "	vta_contrato_arrend.CTOA_MONEDA, \n"
               + "	vta_contrato_arrend.CTOA_FOLIO, \n"
               + "	vta_contrato_arrend.CTOA_INICIO, \n"
               + "	vta_contrato_arrend.CTOA_MENSUALIDAD_ACTUAL, \n"
               + "	vta_contrato_arrend.CTOA_PERIODICIDAD, \n"
               + "	vta_contrato_arrend.CTOA_TIPO_SISTEMA\n"
               + "FROM vta_contrato_arrend where vta_contrato_arrend.CTOA_ACTIVO = 1 AND vta_contrato_arrend.CTOA_PERIODICIDAD = 1\n"
               + " AND '" + this.strAnio + strMes + "'>=CTOA_INICIO"
               + " AND '" + this.strAnio + strMes + "'<=CTOA_VENCIMIENTO";
            try {
                ResultSet rs = this.oConn.runQuery(strSql1, true);
                while (rs.next()) {
                    int intNumCte = rs.getInt("CTE_ID");
                    String strFolio = rs.getString("CTOA_FOLIO");
                    boolean bolFound = false;
                    ClienteporFacturar porLlenar = null;
                    for (ClienteporFacturar d : lstClientes) {
                        if (d.getStrContrato().equals(strFolio)) {
                            bolFound = true;
                            porLlenar = d;
                        }

                    }
                    //Si no lo encuentra que lo de alta
                    if (!bolFound) {
                        ClienteporFacturar cte = new ClienteporFacturar();
                        cte.setIntNum(intNumCte);
                        cte.setIntNumContrato(rs.getInt("CTOA_ID"));
                        cte.setStrNombre(rs.getString("CTE_NOMBRE"));
                        cte.setIntMonedaId(rs.getInt("CTOA_MONEDA"));
                        cte.setStrArrenda(rs.getString("CTOA_ARRENDAMIENTO"));
                        cte.setStrMoneda(getMoneda(rs.getInt("CTOA_MONEDA")));
                        cte.setStrMes(getMes());
                        cte.setIntConsecutivoFactura(rs.getInt("CTOA_MENSUALIDAD_ACTUAL"));
                        cte.setStrContrato(strFolio);
                        porLlenar = cte;
                        this.lstClientes.add(cte);
                    }
                    //Validamos que halla objeto por llenar
                    if (porLlenar != null) {
                        String strFecha = this.strAnio + strMes + "01";
                        porLlenar.getLstContratosAnual()[i - 1].setStrDescripcion(rs.getString("CTOA_ARRENDAMIENTO"));
                        porLlenar.getLstContratosAnual()[i - 1].setDblImporte(rs.getDouble("CTOA_MTO_ARRENDAMIENTO"));
                        porLlenar.getLstContratosAnual()[i - 1].setStrFecha(strFecha);
                    }
                }
                rs.close();

            } catch (SQLException ex) {
                log.error(ex);
            }

            //Obtenemos las facturas anuales
            String strSql2 = "SELECT vta_contrato_arrend.CTOA_ID, \n"
               + "	vta_contrato_arrend.CTE_ID, \n"
               + "	vta_contrato_arrend.CTOA_MONEDA, \n"
               + "	vta_contrato_arrend.CTE_NOMBRE, 	\n"
               + "	vta_contrato_arrend.CTOA_MENSUALIDAD_ACTUAL, 	\n"
               + "	vta_contrato_arrend.CTOA_ARRENDAMIENTO, \n"
               + "	vta_contrato_arrend.CTOA_VENCIMIENTO, 	\n"
               + "	vta_contrato_arrend.CTOA_MTO_ARRENDAMIENTO, \n"
               + "	vta_contrato_arrend.CTOA_MONEDA, \n"
               + "	vta_contrato_arrend.CTOA_FOLIO, \n"
               + "	vta_contrato_arrend.CTOA_INICIO, \n"
               + "	vta_contrato_arrend.CTOA_MENSUALIDAD_ACTUAL, \n"
               + "	vta_contrato_arrend.CTOA_PERIODICIDAD, \n"
               + "	vta_contrato_arrend.CTOA_TIPO_SISTEMA\n"
               + "FROM vta_contrato_arrend where vta_contrato_arrend.CTOA_ACTIVO = 1 AND vta_contrato_arrend.CTOA_PERIODICIDAD = 2\n"
               + " AND MID(CTOA_INICIO,5,2)='" + strMes + "'";
            try {
                ResultSet rs = this.oConn.runQuery(strSql2, true);
                while (rs.next()) {
                    int intNumCte = rs.getInt("CTE_ID");
                    String strFolio = rs.getString("CTOA_FOLIO");
                    boolean bolFound = false;
                    ClienteporFacturar porLlenar = null;
                    for (ClienteporFacturar d : lstClientes) {
                        if (d.getStrContrato().equals(strFolio)) {
                            bolFound = true;
                            porLlenar = d;
                        }

                    }
                    //Si no lo encuentra que lo de alta
                    if (!bolFound) {
                        ClienteporFacturar cte = new ClienteporFacturar();
                        cte.setIntNum(intNumCte);
                        cte.setIntNumContrato(rs.getInt("CTOA_ID"));
                        cte.setStrNombre(rs.getString("CTE_NOMBRE"));
                        cte.setIntMonedaId(rs.getInt("CTOA_MONEDA"));
                        cte.setStrMoneda(getMoneda(rs.getInt("CTOA_MONEDA")));
                        cte.setStrArrenda(rs.getString("CTOA_ARRENDAMIENTO"));
                        cte.setStrMes(getMes());
                        cte.setIntConsecutivoFactura(rs.getInt("CTOA_MENSUALIDAD_ACTUAL"));
                        cte.setStrContrato(strFolio);
                        porLlenar = cte;
                        this.lstClientes.add(cte);
                    }
                    //Validamos que halla objeto por llenar
                    if (porLlenar != null) {

                        String strFecha = this.strAnio + strMes + "01";
                        porLlenar.getLstContratosAnual()[i - 1].setStrDescripcion(rs.getString("CTOA_ARRENDAMIENTO"));//En el 0 siempre ponemos la desc
                        porLlenar.getLstContratosAnual()[i - 1].setStrDescripcion(rs.getString("CTOA_ARRENDAMIENTO"));
                        porLlenar.getLstContratosAnual()[i - 1].setDblImporte(rs.getDouble("CTOA_MTO_ARRENDAMIENTO"));
                        porLlenar.getLstContratosAnual()[i - 1].setStrFecha(strFecha);

                    }
                }
                rs.close();

            } catch (SQLException ex) {
                log.error(ex);
            }
        }

//      //
//      log.debug("Cuantos elementos encontrados " + this.lstClientes.size());
//      log.debug("********");
//      log.debug("Numero|Nombre|Descripcion|Enero|Feb|Mar|Abr|May|Jun|Jul|Agos|Sep|Oct|Nov|Dic");
//      for (ClienteporFacturar d : lstClientes) {
//         log.debug(d.getIntNum() + "|" + d.getStrNombre() + "|" + d.getLstContratosAnual()[0].getStrDescripcion() + "|" + d.getLstContratosAnual()[0].getDblImporte() + "|" + d.getLstContratosAnual()[1].getDblImporte()
//                 + "|" + d.getLstContratosAnual()[2].getDblImporte() + "|" + d.getLstContratosAnual()[3].getDblImporte()
//                 + "|" + d.getLstContratosAnual()[4].getDblImporte()
//                 + "|" + d.getLstContratosAnual()[5].getDblImporte() + "|" + d.getLstContratosAnual()[6].getDblImporte() + "|" + d.getLstContratosAnual()[7].getDblImporte() + "|"
//                 + d.getLstContratosAnual()[8].getDblImporte() + "|" + d.getLstContratosAnual()[9].getDblImporte() + "|" + d.getLstContratosAnual()[10].getDblImporte() + "|"
//                 + d.getLstContratosAnual()[11].getDblImporte() + "");
//      }
//      log.debug("********");
    }

    public void doTrxAnul() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void doTrxRevive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void doTrxSaldo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void doTrxMod() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        StringBuilder strAll = new StringBuilder();
        strAll.append("Numero|Nombre|Descripcion|Estatus|Enero|Feb|Mar|Abr|May|Jun|Jul|Agos|Sep|Oct|Nov|Dic\n");
        for (ClienteporFacturar d : lstClientes) {

            String strDescripcion = d.getLstContratosAnual()[0].getStrDescripcion();
            if (this.intMes != 0) {
                strDescripcion = d.getLstContratosAnual()[this.intMes - 1].getStrDescripcion();
            }
            strAll.append(d.getIntNum() + "|" + d.getStrNombre() + "|" + strDescripcion + "|" + d.getStrResultado() + "|"
               + d.getLstContratosAnual()[0].getDblImporte() + "|" + d.getLstContratosAnual()[1].getDblImporte()
               + "|" + d.getLstContratosAnual()[2].getDblImporte() + "|" + d.getLstContratosAnual()[3].getDblImporte()
               + "|" + d.getLstContratosAnual()[4].getDblImporte()
               + "|" + d.getLstContratosAnual()[5].getDblImporte() + "|" + d.getLstContratosAnual()[6].getDblImporte() + "|" + d.getLstContratosAnual()[7].getDblImporte() + "|"
               + d.getLstContratosAnual()[8].getDblImporte() + "|" + d.getLstContratosAnual()[9].getDblImporte() + "|" + d.getLstContratosAnual()[10].getDblImporte() + "|"
               + d.getLstContratosAnual()[11].getDblImporte() + "\n");
        }
        strAll.append("********\n");
        return strAll.toString();
    }

    /**
     * *Realiza las facturas del periodo seleccionado
     */
    public void doInvoices() {

        //Unicamente si seleccionaron un mes procede
        if (this.intMes != 0) {

            for (ClienteporFacturar d : lstClientes) {
                boolean bolError = false;
                //Si hay transaccionalidad comenzamos
                if (this.bolTransaccionalidad) {
                    oConn.runQueryLMD("BEGIN");
                }
                String strFecha = d.getLstContratosAnual()[this.intMes - 1].getStrFecha();
                int intIdContrato = d.getIntNumContrato();
                d.setStrResultado("OK");

                //Evaluamos si hay una factura en este periodo relacionado con el contrato
                int intCuantos = 0;
                String strSqlHay = "select count(FAC_ID) as cuantos from vta_facturas "
                   + " where CTOA_ID = " + intIdContrato + ""
                   + "  and left(FAC_FECHA,6) = '" + strFecha.substring(0, 6) + "' "
                   + "  AND FAC_ANULADA = 0";
                ResultSet rs2;
                try {
                    rs2 = oConn.runQuery(strSqlHay);
                    while (rs2.next()) {
                        intCuantos = rs2.getInt("cuantos");
                    }
                    rs2.close();

                    //Si no existe facturamos
                    if (intCuantos == 0) {
                        doInvoiceMake(d);
                        log.debug("Resultado:" + d.getStrResultado());
                        if (!d.getStrResultado().equals("OK")) {
                            bolError = true;
                        }
                    } else {
                        d.setStrResultado("ERROR:Ya se facturo el contrato.");
                        bolError = true;
                    }
                } catch (SQLException ex) {
                    log.exit(ex.getMessage());
                }
                //Si hay transaccionalidad terminamos
                if (this.bolTransaccionalidad) {
                    if (!bolError) {
                        oConn.runQueryLMD("COMMIT");
                    } else {
                        oConn.runQueryLMD("ROLLBACK");
                    }
                }
            }
        }
    }

    /**
     * Realiza la factura de un contrato
     */
    private void doInvoiceMake(ClienteporFacturar d) {
        //Recuperamos valores del contrato
        String strDescripcion = d.getLstContratosAnual()[this.intMes - 1].getStrDescripcion();
        double dblImporteFacturar = d.getLstContratosAnual()[this.intMes - 1].getDblImporte();
        int intIdContrato = d.getIntNumContrato();
        double dblTI_TASA = 0;
        int dblTI_ID = 0;
        String strCT_METODODEPAGO = "";
        String strCT_FORMADEPAGO = "";
        String strCT_CTABANCO1 = "";

        //Asignamos valores default
        String strFechaFactura = this.fecha.getFechaActual();

        //Obtenemos la tasa de IVA actual
        //buscamos la tasa del iva
        try {
            String strSql = "select vta_tasaiva.TI_ID,vta_tasaiva.TI_TASA "
               + " from vta_cliente, vta_tasaiva"
               + " where vta_cliente.TI_ID =vta_tasaiva.TI_ID AND vta_cliente.CT_ID = " + d.getIntNum();
            ResultSet rs = oConn.runQuery(strSql);
            while (rs.next()) {
                dblTI_TASA = rs.getDouble("TI_TASA");
                dblTI_ID = rs.getInt("TI_ID");
            }
            rs.close();
            strSql = "select CT_METODODEPAGO,CT_FORMADEPAGO,CT_CTABANCO1 "
               + " from vta_cliente"
               + " where vta_cliente.CT_ID = " + d.getIntNum();
            rs = oConn.runQuery(strSql);
            while (rs.next()) {
                strCT_METODODEPAGO = rs.getString("CT_METODODEPAGO");//CT_METODODEPAGO
                strCT_FORMADEPAGO = rs.getString("CT_FORMADEPAGO");//CT_FORMADEPAGO
                strCT_CTABANCO1 = rs.getString("CT_CTABANCO1");//CT_CTABANCO1
                log.debug("CT_METODODEPAGO: " + rs.getString("CT_METODODEPAGO"));
                log.debug("CT_CTABANCO1: " + rs.getString("CT_CTABANCO1"));
                log.debug("CT_FORMADEPAGO: " + rs.getString("CT_FORMADEPAGO"));
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex);
        }
        double dblSubTotal = dblImporteFacturar;
        double dblImpuesto = (dblTI_TASA / 100) * dblSubTotal;
        double dblTotal = dblImpuesto + dblSubTotal;
        //Parsear comentario...
        strDescripcion = parserComments(strDescripcion, d.getIntConsecutivoFactura());

        log.debug("*************");
        log.debug("Nva factura " + d.getStrContrato());
        log.debug("dblSubTotal:" + dblSubTotal);
        log.debug("dblImpuesto:" + dblImpuesto);
        log.debug("dblTotal:" + dblTotal);
        log.debug("strDescripcion:" + strDescripcion);
        log.debug("*************");
        //Nuevo ticket
        Ticket ticket = new Ticket(oConn, varSesiones, request);
        ticket.setBolAfectaInv(false);
        ticket.setStrPATHKeys(this.strPathPrivateKeys);
        ticket.setStrPATHXml(this.strPathXML);
        ticket.setStrPATHFonts(this.strPathFonts);
        //ticket.setBolUsoLugarExpEmp(true);
        if (this.context != null) {
            ticket.initMyPass(this.context);
        } else {
            //El password viene parametrizado
            ticket.setStrMyPassSecret(strMyPassSecret);
        }
        //Recibimos parametros
        String strPrefijoMaster = "FAC";
        String strPrefijoDeta = "FACD";
        String strTipoVtaNom = Ticket.FACTURA;
        ticket.setStrTipoVta(strTipoVtaNom);
        ticket.setBolTransaccionalidad(!this.bolTransaccionalidad);
        ticket.setIntEMP_ID(this.intEMP_ID);
        ticket.setBolFolioGlobal(false);
        ticket.getDocument().setFieldInt("SC_ID", this.intSC_ID);
        ticket.getDocument().setFieldInt("CT_ID", d.getIntNum());
        //ticket.getDocument().setFieldInt("TKT_ID", obj.getIntTKT_ID());
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", d.getIntMonedaId());
        ticket.getDocument().setFieldInt("VE_ID", 0);
        ticket.getDocument().setFieldInt("TI_ID", dblTI_ID);
        ticket.getDocument().setFieldInt("CTOA_ID", intIdContrato);
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", 1);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", strFechaFactura);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", "CONTRATO:" + d.getStrContrato());
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", "");
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", dblSubTotal);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", dblImpuesto);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", 0);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", 0);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", dblTotal);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", dblTI_TASA);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", 0);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", 0);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NUM_MENS", d.getIntConsecutivoFactura());
        log.debug("CT_METODODEPAGO: " + strCT_METODODEPAGO);
        log.debug("CT_CTABANCO1: " + strCT_CTABANCO1.trim());
        log.debug("CT_FORMADEPAGO: " + strCT_FORMADEPAGO);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", strCT_METODODEPAGO);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", strCT_CTABANCO1.trim());
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", strCT_FORMADEPAGO);

        //Insertamos el detalle (RENTA)
        vta_facturadeta deta = new vta_facturadeta();
        double dblImporteRentamasIVA = dblImporteFacturar * (1 + (dblTI_TASA / 100));
        double dblImporteRentaIVA = dblImporteFacturar * ((dblTI_TASA / 100));
        deta.setFieldInt("SC_ID", this.intSC_ID);
        deta.setFieldInt("PR_ID", 0);
        deta.setFieldString(strPrefijoDeta + "_CVE", "...");
        deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", "SERVICIO");
        deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", dblImporteRentamasIVA);
        deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", 1);
        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", dblTI_TASA);
        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", dblImporteRentaIVA);
        deta.setFieldDouble(strPrefijoDeta + "_PRECIO", dblImporteRentamasIVA);
        deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", dblImporteRentamasIVA);
        String strComentario = strDescripcion;
        deta.setFieldString(strPrefijoDeta + "_COMENTARIO", strComentario);
        ticket.AddDetalle(deta);

        //Inicializamos objeto
        ticket.Init();
        //Generamos transaccion
        ticket.doTrx();
        if (!ticket.getStrResultLast().equals("OK")) {
            if (this.strResultLast.equals("OK")) {
                this.strResultLast = "";
            }
            this.strResultLast += " " + ticket.getStrResultLast() + " CONTRATO:" + d.getStrContrato() + "<br>";
            d.setStrResultado(ticket.getStrResultLast());
            log.debug(ticket.getStrResultLast() + " CONTRATO:" + d.getStrContrato());
        } else {
            //Si funciono....
            this.strResultLast += " " + ticket.getStrResultLast() + " CONTRATO:" + d.getStrContrato() + "<br>";
            //Actualizamos la mensualidad
            int intNewMens = d.getIntConsecutivoFactura();
            intNewMens++;
            String strUpdateContrato = "update vta_contrato_arrend set CTOA_MENSUALIDAD_ACTUAL = " + intNewMens
               + " WHERE CTOA_ID = " + d.getIntNumContrato();
            oConn.runQueryLMD(strUpdateContrato);
        }
    }

    private String parserComments(String strComments, int intNumPago) {
        if (strComments.contains("[ANIO]")) {
            strComments = strComments.replace("[ANIO]", this.strAnio);
        }
        if (strComments.contains("[MES]")) {
            //Mes
            if (this.msgRes != null) {
                fecha.setMsgRes(msgRes);
            } else {
                fecha.setMsgRes(null);
            }
            String strNomMes = fecha.DameMesenLetra(this.intMes);
            strComments = strComments.replace("[MES]", strNomMes);
        }
        if (strComments.contains("[ANIO+1]")) {
            try {
                strComments = strComments.replace("[ANIO+1]", (Integer.valueOf(this.strAnio) + 1) + "");
            } catch (NumberFormatException ex) {

            }
        }
        if (strComments.contains("[NO_PAGO]")) {
            try {
                strComments = strComments.replace("[NO_PAGO]", intNumPago + "");
            } catch (NumberFormatException ex) {

            }
        }
        return strComments;
    }
    // </editor-fold>

    public String RepAnio_GeneraXml() {
        Iterator<ClienteporFacturar> it = lstClientes.iterator();
        int intMoneda;
        String strMoneda;
        for (ClienteporFacturar d : lstClientes) {
            d.setMes1(d.getLstContratosAnual()[0].getDblImporte());
            d.setMes2(d.getLstContratosAnual()[1].getDblImporte());
            d.setMes3(d.getLstContratosAnual()[2].getDblImporte());
            d.setMes4(d.getLstContratosAnual()[3].getDblImporte());
            d.setMes5(d.getLstContratosAnual()[4].getDblImporte());
            d.setMes6(d.getLstContratosAnual()[5].getDblImporte());
            d.setMes7(d.getLstContratosAnual()[6].getDblImporte());
            d.setMes8(d.getLstContratosAnual()[7].getDblImporte());
            d.setMes9(d.getLstContratosAnual()[8].getDblImporte());
            d.setMes10(d.getLstContratosAnual()[9].getDblImporte());
            d.setMes11(d.getLstContratosAnual()[10].getDblImporte());
            d.setMes12(d.getLstContratosAnual()[11].getDblImporte());
        }

        UtilXml utilXml = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<Total_por_Anio>");
        while (it.hasNext()) {
            ClienteporFacturar lstClientes1 = it.next();
            strXML.append(" <ReporteAnual");
            strXML.append(" NumeroContrato= \"").append(lstClientes1.getIntNumContrato()).append("\" ");
            strXML.append(" ARRENDAMIENTO= \"").append(lstClientes1.getStrArrenda()).append("\" ");
            strXML.append(" MES1= \"").append(lstClientes1.getMes1()).append("\" ");
            strXML.append(" MES2= \"").append(lstClientes1.getMes2()).append("\" ");
            strXML.append(" MES3= \"").append(lstClientes1.getMes3()).append("\" ");
            strXML.append(" MES4= \"").append(lstClientes1.getMes4()).append("\" ");
            strXML.append(" MES5= \"").append(lstClientes1.getMes5()).append("\" ");
            strXML.append(" MES6= \"").append(lstClientes1.getMes6()).append("\" ");
            strXML.append(" MES7= \"").append(lstClientes1.getMes7()).append("\" ");
            strXML.append(" MES8= \"").append(lstClientes1.getMes8()).append("\" ");
            strXML.append(" MES9= \"").append(lstClientes1.getMes9()).append("\" ");
            strXML.append(" MES10= \"").append(lstClientes1.getMes10()).append("\" ");
            strXML.append(" MES11= \"").append(lstClientes1.getMes11()).append("\" ");
            strXML.append(" MES12= \"").append(lstClientes1.getMes12()).append("\" ");
            intMoneda = lstClientes1.getIntMonedaId();
            strMoneda = getMoneda(intMoneda);
            strXML.append(" Moneda= \"").append(strMoneda).append("\" ");
            strXML.append(" Nombre= \"").append(utilXml.Sustituye(lstClientes1.getStrNombre())).append("\" ");
            strXML.append(" Contrato= \"").append(lstClientes1.getStrContrato()).append("\" ");
            strXML.append(" Importe= \"").append(lstClientes1.getMot_Arrendamiento()).append("\" ");
            strMes = getMes();
            strXML.append(" Mes= \"").append(strMes).append("\" ");
            strXML.append(" TOTAL_CONTRATO= \"").append(
               lstClientes1.getLstContratosAnual()[0].getDblImporte()
               + lstClientes1.getLstContratosAnual()[1].getDblImporte()
               + lstClientes1.getLstContratosAnual()[2].getDblImporte()
               + lstClientes1.getLstContratosAnual()[3].getDblImporte()
               + lstClientes1.getLstContratosAnual()[4].getDblImporte()
               + lstClientes1.getLstContratosAnual()[5].getDblImporte()
               + lstClientes1.getLstContratosAnual()[6].getDblImporte()
               + lstClientes1.getLstContratosAnual()[7].getDblImporte()
               + lstClientes1.getLstContratosAnual()[8].getDblImporte()
               + lstClientes1.getLstContratosAnual()[9].getDblImporte()
               + lstClientes1.getLstContratosAnual()[10].getDblImporte()
               + lstClientes1.getLstContratosAnual()[11].getDblImporte()
            ).append("\" ");
            strXML.append("/>");
        }
        strXML.append("</Total_por_Anio>");
        log.debug(strXML.toString());
        return strXML.toString();
    }//Fin GENERA XML

    public void GeneraImporte() {
        int mes = getIntMes();
        for (ClienteporFacturar d : lstClientes) {
            d.setMes1(d.getLstContratosAnual()[mes - 1].getDblImporte());
        }
    }

    public String RepAnio_GeneraXml_MES() {
        Iterator<ClienteporFacturar> it = lstClientes.iterator();
        int intMoneda;
        String strMoneda;
        int mes = getIntMes();
        UtilXml utilXml = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<Total_por_Mes>");
        while (it.hasNext()) {
            ClienteporFacturar lstClientes1 = it.next();
            strXML.append(" <ReporteMensual");
            strXML.append(" ARRENDAMIENTO= \"").append(lstClientes1.getStrArrenda()).append("\" ");
            strXML.append(" MES1= \"").append(lstClientes1.getMes1()).append("\" ");
            intMoneda = lstClientes1.getIntMonedaId();
            strMoneda = getMoneda(intMoneda);
            strXML.append(" Moneda= \"").append(strMoneda).append("\" ");
            strXML.append(" Nombre= \"").append(utilXml.Sustituye(lstClientes1.getStrNombre())).append("\" ");
            strXML.append(" Contrato= \"").append(lstClientes1.getStrContrato()).append("\" ");
            strMes = getMes();
            strXML.append(" Mes= \"").append(strMes).append("\" ");
            strXML.append("/>");
        }

        strXML.append("</Total_por_Mes>");
        log.debug(strXML.toString());
        return strXML.toString();
    }//Fin GENERA XML_MES

    public void RepAnio_getReportPrint(String sourceFileName, String targetFileName, VariableSession varSesiones,
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
//      strSql2 = "select vta_monedas.MON_DESCRIPCION from vta_monedas where vta_monedas.MON_ID  =" + mon_id;
//      ResultSet rs2;
//      try {
//         rs2 = this.oConn.runQuery(strSql2, true);
//         while (rs2.next()) {
//            mon_nom = rs2.getString("MON_DESCRIPCION");
//         }
//         rs2.close();
//      } catch (SQLException ex) {
//         log.error(ex);
//      }
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
            JRDataSource datasource = new JRBeanCollectionDataSource(this.lstClientes, true);
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

    public String getMoneda(int MON_ID) {
        String MON_DESC = "";
        String query = "select MON_DESCRIPCION from vta_monedas where MON_ID = " + MON_ID;
        try {
            ResultSet rs = this.oConn.runQuery(query, true);
            while (rs.next()) {
                MON_DESC = rs.getString("MON_DESCRIPCION");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionMasiva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return MON_DESC;
    }//Fin método getMoneda

    public String getMes() {
        int idMes = getIntMes();
        String strMesDesc = "";
        String MesQuery = "select GM_DESCRIPCION from gs_meses where GM_ID =" + idMes + ";";
        try {
            ResultSet rs = this.oConn.runQuery(MesQuery, true);
            while (rs.next()) {
                strMesDesc = rs.getString("GM_DESCRIPCION");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FacturacionMasiva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strMesDesc;
    }
}//FIN CLASE

