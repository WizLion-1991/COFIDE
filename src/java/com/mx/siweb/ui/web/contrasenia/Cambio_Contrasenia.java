/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web.contrasenia;

import comSIWeb.Operaciones.Conexion;

/**
 *
 * @author siwebmx5
 */
public class Cambio_Contrasenia {

    public String CambiaContrasenia(Conexion oConn, Integer intID, String strPSW) {
        String strUpdate = "Update vta_cliente Set CT_PASSWORD = '" + strPSW + "' "
                + "Where CT_ID=" + intID;
        oConn.runQueryLMD(strUpdate);
        return "OK";
    }

    public String CambiaContraseniaProv(Conexion oConn, Integer intID, String strPSW) {
        String strUpdate = "Update vta_proveedor Set PV_PASSWORD = '" + strPSW + "' "
                + "Where PV_ID=" + intID;
        oConn.runQueryLMD(strUpdate);
        return "OK";
}
}
