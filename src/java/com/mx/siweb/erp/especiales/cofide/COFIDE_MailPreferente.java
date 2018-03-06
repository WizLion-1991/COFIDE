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
import comSIWeb.Utilerias.Mail;
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
public class COFIDE_MailPreferente extends ProcesoMaster implements ProcesoInterfaz {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    protected TableMaster document;
    protected ArrayList<TableMaster> lstConceptos;
    protected String strContactoId;
    protected String strPATHXml;
//    protected Conexion oConn;
//    protected VariableSession varSesiones;
    private static final Logger log = LogManager.getLogger(COFIDE_MailPreferente.class.getName());
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
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

    public COFIDE_MailPreferente(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
        super(oConn, varSesiones, request);
        this.document = new cofide_contactos();
        this.lstConceptos = new ArrayList<TableMaster>();
    }

    public COFIDE_MailPreferente(Conexion oConn, VariableSession varSesiones) {
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
        // <editor-fold defaultstate="collapsed" desc="Inicializamos valores ">
        this.strResultLast = "OK";

        if (this.document.getFieldInt("CONTACTO_ID") == 0) {
            this.strResultLast = "NO SE ENCONTRÓ EL CONTACTO";
        }
        // </editor-fold>
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

    public void doEnvioCtPreferente(String strContactoId) {
        this.strResultLast = "OK";
        //Recuperamos valores para el envio del correo

        String strSql = "SELECT CCO_NOMBRE,CCO_CORREO,CCO_CORREO2 FROM cofide_contactos where CONTACTO_ID = " + strContactoId;
        ResultSet rs;
        try {
            rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                //Buscamos mail del empleado
                String strCP_EMAIL1 = "";
                String strCP_EMAIL2 = "";

                strCP_EMAIL1 = rs.getString("CCO_CORREO");
                strCP_EMAIL2 = rs.getString("CCO_CORREO2");

                //Evaluamos si hay correo donde enviar
                if (!strCP_EMAIL1.isEmpty() || !strCP_EMAIL1.isEmpty()) {
                    this.document.ObtenDatosVarios(" CONTACTO_ID = " + strContactoId, oConn);
                    //Objeto de mail
                    Mail mail = new Mail();
                    String strLstMail = "";
                    //Validamos si el mail del cliente es valido
                    if (mail.isEmail(strCP_EMAIL1)) {
                        strLstMail += "," + strCP_EMAIL1;
                    }
                    if (mail.isEmail(strCP_EMAIL2)) {
                        strLstMail += "," + strCP_EMAIL2;
                    }
                    if (strLstMail.startsWith(",")) {
                        strLstMail = strLstMail.substring(1, strLstMail.length());
                    }
                    //Intentamos mandar el mail
                    mail.setBolDepuracion(false);
                    mail.getTemplate("COFIDE_PREFERENTE", oConn);
                    mail.getMensaje();
                    mail.setReplaceContent(this.getDocument());
                    this.document.setFieldInt("CONTACTO_ID", Integer.parseInt(strContactoId));
                    mail.setDestino(strLstMail);
                    //Adjuntamos los archivos PDF
                    String[] lstParamsName = {"intContactoId"};
                    String[] lstParamsValue = {strContactoId};
                    String strNomFormato = "ClientePreferente";

                    //Obtenemos los formatos correspondientes
                    String strNomFile = this.doGeneraFormatoJasper(0, strNomFormato, "PDF", this.getDocument(), lstParamsName, lstParamsValue, this.strPATHXml);
                    if (!strNomFile.isEmpty()) {
                        log.debug(strNomFile);
                        mail.setFichero(strNomFile);
                    }

                    log.debug("Enviamos el correo....");

                    boolean bol = mail.sendMail();
                    if (bol) {
                        //Se envio el correo
                        siEnvioCtPreferente(oConn, strContactoId);
                    } else {
                        //No se envio...
                    }
                }

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }//Fin doEnvioCtPreferente

    public void siEnvioCtPreferente(Conexion oConn, String strContactoId) {
        String strSql = "INSERT INTO cofide_envio_preferente (CONTACTO_ID) VALUES ('" + strContactoId + "')";
        oConn.runQueryLMD(strSql);
    }//Fin siEnvioCtPreferente

}
