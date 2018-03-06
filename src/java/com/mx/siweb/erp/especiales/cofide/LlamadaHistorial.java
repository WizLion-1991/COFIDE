/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import Tablas.CofideLlamadas;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author juliocesar
 */
public class LlamadaHistorial {
// <editor-fold defaultstate="collapsed" desc="Propiedades">
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Constructores">
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Metodos">

    public String GuardaLlamada(Conexion oConn, int intIdCte, String strFecha, String strHr, int intUsr, String strExt, String strDestino) {
        CofideLlamadas cl = new CofideLlamadas();
        cl.setFieldInt("CL_ID_CLIENTE", intIdCte);
        cl.setFieldString("CL_FECHA", strFecha);
        cl.setFieldString("CL_HORA", strHr);
        cl.setFieldInt("CL_USUARIO", intUsr);
        cl.setFieldString("CL_EXT", strExt);
        cl.setFieldString("CL_DESTINO", strDestino);
        cl.setBolGetAutonumeric(true);
        cl.Agrega(oConn);
        String strResultado = cl.getValorKey(); //el id que se inserto
        return strResultado;
    }
}
// </editor-fold>
