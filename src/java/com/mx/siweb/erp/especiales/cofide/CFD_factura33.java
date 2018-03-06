/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import ERP.ProcesoInterfaz;
import ERP.ProcesoMaster;
import Tablas.cofide_contactos;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class CFD_factura33 extends ProcesoMaster implements ProcesoInterfaz {

    protected TableMaster document;
    protected ArrayList<TableMaster> lstConceptos;
    protected String strContactoId;
    protected String strFolioFiscal; //factura
    protected String strIDFactura; //factura
    protected String strIdTicket; //ticket
    protected String strFolio; //ticket
    protected String strPATHXml;
    protected String PATHTKT;
    protected String strIdComplemento;
    private static final Logger log = LogManager.getLogger(CFD_factura33.class.getName());
    Fechas fec = new Fechas();

    public TableMaster getDocument() {
        return document;
    }

    public void setDocument(TableMaster document) {
        this.document = document;
    }

    public ArrayList<TableMaster> getLstConceptos() {
        return lstConceptos;
    }

    public void setLstConceptos(ArrayList<TableMaster> lstConceptos) {
        this.lstConceptos = lstConceptos;
    }

    public String getStrFolioFiscal() {
        return strFolioFiscal;
    }

    public void setStrFolioFiscal(String strFolioFiscal) {
        this.strFolioFiscal = strFolioFiscal;
    }

    public String getStrIDFactura() {
        return strIDFactura;
    }

    public void setStrIDFactura(String strIDFactura) {
        this.strIDFactura = strIDFactura;
    }

    public String getStrIdTicket() {
        return strIdTicket;
    }

    public void setStrIdTicket(String strIdTicket) {
        this.strIdTicket = strIdTicket;
    }

    public String getStrIdComplemento() {
        return strIdComplemento;
    }

    public void setStrIdComplemento(String strIdComplemento) {
        this.strIdComplemento = strIdComplemento;
    }

    public String getStrFolio() {
        return strFolio;
    }

    public void setStrFolio(String strFolio) {
        this.strFolio = strFolio;
    }

    public String getStrContactoId() {
        return strContactoId;
    }

    public void setStrContactoId(String strContactoId) {
        this.strContactoId = strContactoId;
    }

    public String getStrPATHXml() {
        return strPATHXml;
    }

    public void setStrPATHXml(String strPATHXml) {
        this.strPATHXml = strPATHXml;
    }

    public String getPATHTKT() {
        return PATHTKT;
    }

    public void setPATHTKT(String PATHTKT) {
        this.PATHTKT = PATHTKT;
    }

    public CFD_factura33(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
        super(oConn, varSesiones, request);
        this.document = new cofide_contactos();
        this.lstConceptos = new ArrayList<TableMaster>();
    }

    public CFD_factura33(Conexion oConn, VariableSession varSesiones) {
        super(oConn, varSesiones);
        this.document = new cofide_contactos();
        this.lstConceptos = new ArrayList<TableMaster>();
    }

    @Override
    public void Init() {
        this.strResultLast = "OK";
        //Si nos pasan el id del movimiento recuperamos todos los datos
        if (this.document.getFieldInt("CONTACTO_ID") != 0) {
            //¿Validamos o asignamos la tabla?
            this.document.ObtenDatosVarios(" CONTACTO_ID = " + this.document.getFieldInt("CONTACTO_ID"), oConn);
        }
    }

    @Override
    public void doTrx() {

        this.strResultLast = "OK";

        if (this.document.getFieldInt("CONTACTO_ID") == 0) {
            this.strResultLast = "NO SE ENCONTRÓ EL CONTACTO";
        }

    }

    @Override
    public void doTrxAnul() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doTrxRevive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doTrxSaldo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doTrxMod() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Envío de factura
     *
     */
    public void doEnvioFactura() {

//        Fechas fec = new Fechas();
        this.strResultLast = "OK";
        //Recuperamos valores para el envio del correo

        String strSql = "select FAC_FECHA, FAC_RAZONSOCIAL, FAC_FOLIO_C, FAC_ID, FAC_FOLIO, EMP_ID, SC_ID, SMTP_US, SMTP, SMTP_PASS, SMTP_USASTLS, SMTP_USATLS, PORT,"
                + "(SELECT DFA_EMAIL FROM vta_cliente_facturacion AS DFA WHERE DFA.DFA_ID = FAC.DFA_ID) AS CORREO , "
                + "(SELECT DFA_EMAI2 FROM vta_cliente_facturacion AS DFA WHERE DFA.DFA_ID = FAC.DFA_ID) AS CC "
                + "from vta_facturas AS FAC, usuarios "
                + "where FAC_US_ALTA = id_usuarios and FAC_ID = " + getStrIDFactura();

        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //información del cliente
                String strCorreoCliente = rs.getString("CORREO");
                String strCorreoClienteCC = rs.getString("CC");
                //información de correo
                String strEjecutivoMail = rs.getString("SMTP_US");
                String strEjecutivoContrasenia = rs.getString("SMTP_PASS");
                String strEjecutivoServer = rs.getString("SMTP");
                String strEjecutivoPort = rs.getString("PORT");
                String strEjecutivoTLS = rs.getString("SMTP_USATLS");
                String strEjecutivoSTLS = rs.getString("SMTP_USASTLS");
                //información de la factura
                String strFolio = rs.getString("FAC_FOLIO_C");
                String strRazonSocial = rs.getString("FAC_RAZONSOCIAL").trim();
                String strFechaEmision = rs.getString("FAC_FECHA");

                //Evaluamos si hay correo donde enviar
                if (!strCorreoCliente.isEmpty() || !strCorreoClienteCC.isEmpty()) {
                    //Objeto de mail
                    Mail mail = new Mail();
                    String strLstMail = "";
                    //Validamos si el mail del cliente es valido
                    if (mail.isEmail(strCorreoCliente)) {
                        strLstMail += "," + strCorreoCliente;
                    }
                    if (mail.isEmail(strCorreoClienteCC)) {
                        strLstMail += "," + strCorreoClienteCC;
                    }
                    if (mail.isEmail(strEjecutivoMail)) {
                        strLstMail += "," + strEjecutivoMail;
                    }
                    if (strLstMail.startsWith(",")) {
                        strLstMail = strLstMail.substring(1, strLstMail.length());
                    }
                    //Intentamos mandar el mail
                    mail.setBolDepuracion(false);
                    mail.getTemplate("FACTURA", oConn);
                    mail.setUsuario(strEjecutivoMail);
                    mail.setContrasenia(strEjecutivoContrasenia);
                    mail.setPuerto(strEjecutivoPort);
                    mail.setHost(strEjecutivoServer);
                    mail.setBolUsaTls(true);
                    mail.setBolUsaStartTls(false);

                    mail.setReplaceContent(this.getDocument());
                    this.document.setFieldString("FOLIO_FISCAL", getStrFolioFiscal());
                    this.document.setFieldString("doc_folio1", strFolio);
                    this.document.setFieldString("doc_folio2", strFolio);
                    this.document.setFieldString("doc_id", getStrIDFactura());
                    mail.setDestino(strLstMail);
                    mail.setAsunto("Factura con FOLIO: " + getStrFolioFiscal());
                    //Adjuntamos los archivos PDF
                    String[] lstParamsName = {"doc_folio1", "doc_folio2", "doc_id"};
                    String[] lstParamsValue = {strFolio, strFolio, getStrIDFactura()};
                    String strNomFormato = "factura_cfdi";

                    //Obtenemos los formatos correspondientes
                    String strNomXML = this.strPATHXml + "CFDI_" + strRazonSocial.replace(" ", "_").replace("  ", "_") + "_" + strFechaEmision + "_" + getStrFolioFiscal().trim() + ".xml";
                    System.out.println(strNomXML);

                    String strNomFile = "";
                    File file = new File(this.strPATHXml + "factura_cfdi_" + getStrFolioFiscal().trim() + ".pdf");
                    if (!file.exists()) {
                        System.out.println("NO EXISTE EL ARCHIVO, VAMOS A CREARLO");
                        strNomFile = this.doGeneraFormatoJasper(0, strNomFormato, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.strPATHXml);
                    } else {
                        System.out.println("EXISTE Y SE VA A ADJUNTAR");
                        strNomFile = this.strPATHXml + "factura_cfdi_" + getStrFolioFiscal().trim() + ".pdf";
                    }

                    if (!strNomFile.isEmpty()) {
                        log.debug(strNomFile);
                        //adjunta el PDF
                        mail.setFichero(strNomFile);
                        //adjunta el XML
                        mail.setFichero(strNomXML);
                    }

                    log.debug("Enviamos el correo....");

                    boolean bol = mail.sendMail();
                    if (bol) {
                        //Se envio el correo
                        strResultLast = "OK";

                    } else {
                        //No se envio...
                        strResultLast = "Hubó un problema al envíar la factura.";
                    }
                } else {
                    strResultLast = "El Correo no es correcto.";
                }

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Envío de TICKET
     *
     */
    public void doEnvioTicket() {

//        Fechas fec = new Fechas();
        this.strResultLast = "OK";
        //Recuperamos valores para el envio del correo

        String strSql = "select TKT_FECHA, TKT_RAZONSOCIAL, TKT_FOLIO, TKT_ID, SMTP_US, SMTP, SMTP_PASS, SMTP_USASTLS, SMTP_USATLS, PORT "
                + "from vta_tickets AS tkt, usuarios "
                + "where TKT_US_ALTA = id_usuarios and TKT_ID = " + getStrIdTicket();

        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //información de correo
                String strEjecutivoMail = rs.getString("SMTP_US");
                String strEjecutivoContrasenia = rs.getString("SMTP_PASS");
                String strEjecutivoServer = rs.getString("SMTP");
                String strEjecutivoPort = rs.getString("PORT");
                String strEjecutivoTLS = rs.getString("SMTP_USATLS");
                String strEjecutivoSTLS = rs.getString("SMTP_USASTLS");
                //información de la factura
                String strFolio = rs.getString("TKT_FOLIO");
                String strRazonSocial = rs.getString("TKT_RAZONSOCIAL");
                String strFechaEmision = rs.getString("TKT_FECHA");

                //Evaluamos si hay correo donde enviar
                if (!strEjecutivoMail.isEmpty()) {
                    //Objeto de mail
                    Mail mail = new Mail();
                    String strLstMail = "";
                    //Validamos si el mail del cliente es valido
                    if (mail.isEmail(strEjecutivoMail)) {
                        strLstMail = strEjecutivoMail;
                    }
                    if (strLstMail.startsWith(",")) {
                        strLstMail = strLstMail.substring(1, strLstMail.length());
                    }
                    //Intentamos mandar el mail
                    mail.setBolDepuracion(false);
                    mail.getTemplate("TICKET_MAI", oConn);
                    mail.setUsuario(strEjecutivoMail);
                    mail.setContrasenia(strEjecutivoContrasenia);
                    mail.setPuerto(strEjecutivoPort);
                    mail.setHost(strEjecutivoServer);
                    mail.setBolUsaTls(true);
                    mail.setBolUsaStartTls(false);

                    mail.setReplaceContent(this.getDocument());
                    this.document.setFieldString("FOLIO", getStrFolio());
                    mail.setDestino(strLstMail);
                    mail.setAsunto("TICKET CON FOLIO: " + getStrFolio());
                    //Adjuntamos los archivos PDF
                    String[] lstParamsName = {"TKT_ID"};
                    String[] lstParamsValue = {getStrIdTicket()};
                    String strNomFormato = "ticket";

                    //Obtenemos los formatos correspondientes
                    String strNomFile = "";
                    File file = new File(this.PATHTKT + "ticket_" + getStrFolio().trim() + ".pdf");
                    System.out.println("archivo: " + file);
                    if (!file.exists()) {
                        System.out.println("NO EXISTE EL ARCHIVO, VAMOS A CREARLO");
                        strNomFile = this.doGeneraFormatoJasper(0, strNomFormato, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.PATHTKT);
                    } else {
                        System.out.println("EXISTE Y SE VA A ADJUNTAR");
                        strNomFile = this.PATHTKT + "ticket_" + getStrFolio().trim() + ".pdf";
                    }

                    if (!strNomFile.isEmpty()) {
                        log.debug(strNomFile);
                        //adjunta el PDF
                        mail.setFichero(strNomFile);
                    }

                    log.debug("Enviamos el correo....");

                    boolean bol = mail.sendMail();
                    if (bol) {
                        //Se envio el correo
                        strResultLast = "OK";

                    } else {
                        //No se envio...
                        strResultLast = "Hubó un problema al envíar el ticket.";
                    }
                } else {
                    strResultLast = "El Correo no es correcto.";
                }

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * Envío de complemento
     *
     */
    public void doEnvioComplemento() {

        this.strResultLast = "OK";
        //Recuperamos valores para el envio del correo
        String strSql = "SELECT FAC_RAZONSOCIAL, MCM_FECHA, MCM_FOLIO, SMTP_US, SMTP, SMTP_PASS, SMTP_USASTLS, SMTP_USATLS, PORT,"
                + "if(c.DFA_ID=0,'',(SELECT DFA_EMAIL FROM vta_cliente_facturacion e WHERE e.DFA_ID=c.DFA_ID)) AS DFA_EMAIL,"
                + "if(c.DFA_ID=0,'',(SELECT DFA_EMAI2 FROM vta_cliente_facturacion e WHERE e.DFA_ID=c.DFA_ID)) AS DFA_EMAI2 "
                + "FROM vta_mov_cte_mas a , vta_mov_cte b, vta_facturas c, usuarios u "
                + "WHERE b.MCM_ID=a.MCM_ID "
                + "AND b.FAC_ID=c.FAC_ID "
                + "AND u.id_usuarios = c.FAC_US_ALTA "
                + "AND a.MCM_ID = " + getStrIdComplemento();

        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //información del cliente
                setStrFolioFiscal(rs.getString("MCM_FOLIO"));
                String strCorreoCliente = rs.getString("DFA_EMAIL");
                String strCorreoClienteCC = rs.getString("DFA_EMAI2");
                //información de correo
                String strEjecutivoMail = rs.getString("SMTP_US");
                String strEjecutivoContrasenia = rs.getString("SMTP_PASS");
                String strEjecutivoServer = rs.getString("SMTP");
                String strEjecutivoPort = rs.getString("PORT");
                String strEjecutivoTLS = rs.getString("SMTP_USATLS");
                String strEjecutivoSTLS = rs.getString("SMTP_USASTLS");
                //información de la factura
//                String strFolio = rs.getString("FAC_FOLIO_C");
                String strRazonSocial = rs.getString("FAC_RAZONSOCIAL").trim();
                String strFechaEmision = rs.getString("MCM_FECHA");

                //Evaluamos si hay correo donde enviar
                if (!strCorreoCliente.isEmpty() || !strCorreoClienteCC.isEmpty()) {
                    //Objeto de mail
                    Mail mail = new Mail();
                    String strLstMail = "";
                    //Validamos si el mail del cliente es valido
                    if (mail.isEmail(strCorreoCliente)) {
                        strLstMail += "," + strCorreoCliente;
                    }
                    if (mail.isEmail(strCorreoClienteCC)) {
                        strLstMail += "," + strCorreoClienteCC;
                    }
                    if (mail.isEmail(strEjecutivoMail)) {
                        strLstMail += "," + strEjecutivoMail;
                    }
                    if (strLstMail.startsWith(",")) {
                        strLstMail = strLstMail.substring(1, strLstMail.length());
                    }
                    //Intentamos mandar el mail
                    mail.setBolDepuracion(false);
                    mail.getTemplate("COMPLEMENTO", oConn);
                    mail.setUsuario(strEjecutivoMail);
                    mail.setContrasenia(strEjecutivoContrasenia);
                    mail.setPuerto(strEjecutivoPort);
                    mail.setHost(strEjecutivoServer);
                    mail.setBolUsaTls(true);
                    mail.setBolUsaStartTls(false);

                    mail.setReplaceContent(this.getDocument());
                    this.document.setFieldString("FOLIO", getStrFolioFiscal());
                    this.document.setFieldString("doc_id", getStrIdComplemento());
                    mail.setDestino(strLstMail);
                    mail.setAsunto("Complemento con FOLIO: " + getStrFolioFiscal());
                    //Adjuntamos los archivos PDF
                    String[] lstParamsName = {"doc_id"};
                    String[] lstParamsValue = {getStrIdComplemento()};
                    String strNomFormato = "complemento_cfdi";

                    //Obtenemos los formatos correspondientes
                    String strNomXML = this.strPATHXml + "CFDI_" + strRazonSocial.replace(" ", "_").replace("  ", "_") + "_" + strFechaEmision + "_" + getStrFolioFiscal().trim() + ".xml";
                    System.out.println(strNomXML);

                    String strNomFile = "";
                    File file = new File(this.strPATHXml + "complemento_cfdi_" + getStrFolioFiscal().trim() + ".pdf");
                    if (!file.exists()) {
                        System.out.println("NO EXISTE EL ARCHIVO, VAMOS A CREARLO");
                        strNomFile = this.doGeneraFormatoJasper(0, strNomFormato, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.strPATHXml);
                    } else {
                        System.out.println("EXISTE Y SE VA A ADJUNTAR");
                        strNomFile = this.strPATHXml + "complemento_cfdi_" + getStrFolioFiscal().trim() + ".pdf";
                    }

                    if (!strNomFile.isEmpty()) {
                        log.debug(strNomFile);
                        //adjunta el PDF
                        mail.setFichero(strNomFile);
                        //adjunta el XML
                        mail.setFichero(strNomXML);
                    }

                    log.debug("Enviamos el correo....");

                    boolean bol = mail.sendMail();
                    if (bol) {
                        //Se envio el correo
                        strResultLast = "OK";

                    } else {
                        //No se envio...
                        strResultLast = "Hubó un problema al envíar el complemento.";
                    }
                } else {
                    strResultLast = "El Correo no es correcto.";
                }

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

}
