/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web.ventas;

import Tablas.vta_pedidos;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author siwebmx5
 */
public class Vta_Pedidos {

    vta_pedidos PD;

    public Vta_Pedidos(VariableSession varSesiones) {
        this.PD = new vta_pedidos();
    }

    public vta_pedidos getPD() {
        return PD;
    }

    public void setPD(vta_pedidos PD) {
        this.PD = PD;
    }

    public String AltaDatos(Conexion oConn) {
        String strRes = "";
        strRes = this.PD.Agrega(oConn);
        return strRes;
    }

    public String updateDatos(Conexion oConn) {
        String strRes = "";
        strRes = this.PD.Modifica(oConn);
        return strRes;
    }

    public String deleteDatos(Conexion oConn) {
        String strRes = "";
        strRes = this.PD.Borra(oConn);
        return strRes;
    }

    public String getDataPedidos(Conexion oConn, String strFOLIO, String strFECHA_INI, String strFECHA_FIN, String strID_C) throws SQLException {
        Fechas cambiaFecha=new Fechas();
        StringBuilder strXMLData = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXMLData.append("<rows>");

        strXMLData.append("<page>1</page>");
        strXMLData.append("<total>3</total>");
        strXMLData.append("<records>22</records>");

        String strConsulta = "SELECT * FROM vta_pedidos"
                + " Where PD_FECHA >= '" + strFECHA_INI + "' AND PD_FECHA <= '" + strFECHA_FIN + "' AND CT_ID=" + strID_C;
        if (!strFOLIO.equals("")) {
            strConsulta += " AND PD_FOLIO = '" + strFOLIO + "'";
        }
        ResultSet rs = oConn.runQuery(strConsulta);

        while (rs.next()) {

            strXMLData.append("<row id='" + rs.getInt("PD_ID") + "'> ");

            strXMLData.append("<cell>" + rs.getString("PD_ID") + "</cell>");
            strXMLData.append("<cell>" + cambiaFecha.DameFechaenLetra(rs.getString("PD_FECHA"))+ "</cell>");
            strXMLData.append("<cell>" + rs.getString("PD_FOLIO") + "</cell>");
            strXMLData.append("<cell>" + "PEDIDO" + "</cell>");
            strXMLData.append("<cell>" + rs.getString("PD_IMPORTE") + "</cell>");
            strXMLData.append("<cell>" + rs.getString("PD_IMPUESTO1") + "</cell>");
            strXMLData.append("<cell>" + rs.getString("PD_TOTAL") + "</cell>");

            strXMLData.append("</row>");
        }
        //if(rs.getStatement() != null )rs.getStatement().close(); 
        rs.close();
        strXMLData.append("</rows>");
        //System.out.println(strXMLData);
        return strXMLData.toString();
    }
}
