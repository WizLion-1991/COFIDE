/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web.ventas;

import Tablas.vta_tickets;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author siwebmx5
 */
public class Vta_Tickets {
    vta_tickets TKT;

    public Vta_Tickets(VariableSession varSesiones) {
        this.TKT = new vta_tickets();
    }

    public vta_tickets getTKT() {
        return TKT;
    }

    public void setTKT(vta_tickets TKT) {
        this.TKT = TKT;
    }
    
    public String AltaDatos(Conexion oConn)
    {
        String strRes = "";
        strRes = this.TKT.Agrega(oConn);
        return strRes;
    }
    
    
    public String updateDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.TKT.Modifica(oConn);
        return strRes;
    }
    public String deleteDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.TKT.Borra(oConn);
        return strRes;
    }
    
    public String getDataTickets(Conexion oConn,String strFOLIO,String strFECHA_INI,String strFECHA_FIN,String strID_C) throws SQLException {
        Fechas cambiaFecha=new Fechas();
        StringBuilder strXMLData = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXMLData.append("<rows>");
        
        strXMLData.append("<page>1</page>");
        strXMLData.append("<total>3</total>");
        strXMLData.append("<records>22</records>");
        
        String strConsulta = "SELECT * FROM vta_tickets"+
                                " Where TKT_FECHA >= '"+strFECHA_INI+"' AND TKT_FECHA <='"+strFECHA_FIN+"' AND CT_ID="+strID_C;
        if(!strFOLIO.equals(""))
        {
            strConsulta += " AND TKT_FOLIO = '"+strFOLIO+"'";
        }
        ResultSet rs = oConn.runQuery(strConsulta);

        while (rs.next()) {

            strXMLData.append("<row id='" + rs.getInt("TKT_ID") + "'> ");

            strXMLData.append("<cell>" + rs.getString("TKT_ID") + "</cell>");
            strXMLData.append("<cell>" + cambiaFecha.DameFechaenLetra(rs.getString("TKT_FECHA"))+ "</cell>");
            strXMLData.append("<cell>" + rs.getString("TKT_FOLIO") + "</cell>");
            strXMLData.append("<cell>" + "REMISION" + "</cell>");
            strXMLData.append("<cell>" + rs.getString("TKT_IMPORTE") + "</cell>");
            strXMLData.append("<cell>" + rs.getString("TKT_IMPUESTO1") + "</cell>");
            strXMLData.append("<cell>" + rs.getString("TKT_TOTAL") + "</cell>");

            strXMLData.append("</row>");
        }
        //if(rs.getStatement() != null )rs.getStatement().close(); 
        rs.close();
        strXMLData.append("</rows>");
        //System.out.println(strXMLData);
        return strXMLData.toString();
    }
}
