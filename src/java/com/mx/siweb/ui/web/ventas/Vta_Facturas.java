/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web.ventas;

import Tablas.vta_facturas;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author siwebmx5
 */
public class Vta_Facturas {
    vta_facturas FAC;

    public Vta_Facturas(VariableSession varSesiones) {
        this.FAC = new vta_facturas();
    }

    public vta_facturas getFAC() {
        return FAC;
    }

    public void setFAC(vta_facturas FAC) {
        this.FAC = FAC;
    }
    public String AltaDatos(Conexion oConn)
    {
        String strRes = "";
        strRes = this.FAC.Agrega(oConn);
        return strRes;
    }
    
    
    public String updateDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.FAC.Modifica(oConn);
        return strRes;
    }
    public String deleteDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.FAC.Borra(oConn);
        return strRes;
    }
    
    public String getDataFacturas(Conexion oConn,String strFOLIO,String strFECHA_INI,String strFECHA_FIN,String strID_C) throws SQLException {
        Fechas cambiaFecha=new Fechas();
        StringBuilder strXMLData = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        strXMLData.append("<rows>");
        
        
        strXMLData.append("<page>1</page>");
        strXMLData.append("<total>3</total>");
        strXMLData.append("<records>22</records>");
        
        //Debes poner el de page
        String strConsulta = "SELECT * FROM vta_facturas"+
                                " Where FAC_FECHA >= '"+strFECHA_INI+"' AND FAC_FECHA <='"+strFECHA_FIN+"' AND CT_ID="+strID_C;
        if(!strFOLIO.equals(""))
        {
            strConsulta += " AND FAC_FOLIO = '"+strFOLIO+"'";
        }
        ResultSet rs = oConn.runQuery(strConsulta);


        while (rs.next()) {

            strXMLData.append("<row id='" + rs.getInt("FAC_ID") + "'> ");

            strXMLData.append("<cell>" + rs.getInt("FAC_ID") + "</cell>");//Usa este formato para todas las columnas
            //deben llevar el miso orden que definiste en el jqgrid
            //strXMLData.append("<cell>" + rs.getString("FAC_ID") + "</cell>");
            strXMLData.append("<cell>" + cambiaFecha.DameFechaenLetra(rs.getString("FAC_FECHA")) + "</cell>");
            strXMLData.append("<cell>" + rs.getString("FAC_FOLIO") + "</cell>");
            strXMLData.append("<cell>" + "FACTURA" + "</cell>");
            strXMLData.append("<cell>" + rs.getString("FAC_IMPORTE") + "</cell>");
            strXMLData.append("<cell>" + rs.getString("FAC_IMPUESTO1") + "</cell>");
            strXMLData.append("<cell>" + rs.getString("FAC_TOTAL") + "</cell>");

            strXMLData.append("</row>");
        }
        //if(rs.getStatement() != null )rs.getStatement().close(); 
        rs.close();
        strXMLData.append("</rows>");
        //System.out.println(strXMLData);
        return strXMLData.toString();
    }
}
