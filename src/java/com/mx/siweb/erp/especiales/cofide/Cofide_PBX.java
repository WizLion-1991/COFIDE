/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author juliocesar
 */
public class Cofide_PBX {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    Conexion oConn;
    Conexion oConnCOFIDE_PBX;
    boolean bolLocal = false;
    private String strUrl;
    private String strUsuario;
    private String strPassword;

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public boolean isBolLocal() {
        return bolLocal;
    }

    /**
     * Define si lo ejecutamos en local
     *
     * @param bolLocal
     */
    public void setBolLocal(boolean bolLocal) {
        this.bolLocal = bolLocal;
    }
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(Cofide_PBX.class.getName());

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public Cofide_PBX(Conexion oConn) {
        this.oConn = oConn;
        oConnCOFIDE_PBX = new Conexion();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Realiza la conexion a la base de COFIDE
     */
    private void connCOFIDE_PBX() {
        oConnCOFIDE_PBX.setStrNomPool("jdbc/COFIDE_PBX"); //comentar para hacer el testclases
        if (this.bolLocal) {
            String[] ConexionURL = new String[4];

            ConexionURL[0] = this.strUrl;
            ConexionURL[1] = this.strUsuario;
            ConexionURL[2] = this.strPassword;
            ConexionURL[3] = "mysql";

            try {
                oConnCOFIDE_PBX = new Conexion(ConexionURL, null);
            } catch (Exception ex) {
                LOG.error(ex.getMessage());
            }
            oConnCOFIDE_PBX.open();
        } else {
            oConnCOFIDE_PBX.open();
        }
    }

    public void ObtenerDatosCall(String strPbxId) throws SQLException {
        connCOFIDE_PBX();
        String strUniqueId = "";
        String strEstatus = "";
        String strFacturacion = "";
        int intStatusEx = 0;
        int intStatusDe = 0;
        String strSqlPBX = "select * from crm where uniqueid = '" + strPbxId + "';";
        ResultSet rs = oConnCOFIDE_PBX.runQuery(strSqlPBX, true);
        while (rs.next()) {
            strUniqueId = rs.getString("uniqueid");
            strFacturacion = rs.getString("facturacion");
            strEstatus = rs.getString("estatus");
        }
        if (strEstatus.equals("ANSWERED")) {
            intStatusEx = 1;
        } else {
            intStatusDe = 1;
        }
        rs.close();
        String strUpdateCallSql = "Update cofide_llamada set "
                + "CL_TIEMPO_LLAMADA = '" + strFacturacion + "', CL_EXITOSO = '" + intStatusEx + "', CL_DESCARTADO = '" + intStatusDe + "' "
                + "where uniqueid = '" + strPbxId + "'";
        oConn.runQueryLMD(strUpdateCallSql);
    }
   // </editor-fold>
}
