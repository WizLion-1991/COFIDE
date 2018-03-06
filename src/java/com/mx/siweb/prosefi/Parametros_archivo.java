/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.prosefi;

import Tablas.vta_datos_archivo;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author SIWEB
 */
public class Parametros_archivo {
    vta_datos_archivo PA;
    
    
    public Parametros_archivo(VariableSession varSesiones) {
       this.PA = new vta_datos_archivo();
    }
    
    public vta_datos_archivo getPA() {
        return PA;
    }

    public void setPA(vta_datos_archivo PA) {
        this.PA = PA;
    }
    public String AltaDatos(Conexion oConn)
    {
        String strRes = "";
        strRes = this.PA.Agrega(oConn);
        
        return strRes;
    }    
    

    public String getDatos(Conexion oConn) throws SQLException
    {
        String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";

        String strConsulta = "SELECT * FROM vta_datos_archivo";
        ResultSet rs = oConn.runQuery(strConsulta);

        strXMLData += "<ParametrosArchivo>";
        while (rs.next()) {

            strXMLData += "<Parchivo ";

            strXMLData += "CPMD_ID =\"" + rs.getInt("CPMD_ID") + "\" ";
            strXMLData += "CPM_ID =\"" + rs.getString("CPM_ID") + "\" ";
            strXMLData += "CPMD_NOMBRE_CAMPO =\"" + rs.getString("CPMD_NOMBRE_CAMPO") + "\" ";
            strXMLData += "CPMD_TIPO =\"" + rs.getString("CPMD_TIPO") + "\" ";
            strXMLData += "CPMD_EXP_REG =\"" + rs.getString("CPMD_EXP_REG") + "\" ";
            strXMLData += "CPMD_ORDEN =\"" + rs.getString("CPMD_ORDEN") + "\" ";

            strXMLData += ">";
            strXMLData += "</Parchivo>";
        }
        //if(rs.getStatement() != null )rs.getStatement().close(); 
        rs.close();
        strXMLData += "</ParametrosArchivo>";
        return strXMLData;
    }    
    
    public String updateDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.PA.Modifica(oConn);
        return strRes;
    }    
    
    public String deleteDatos(Conexion oConn)
    {
        String strRes="";
        strRes=this.PA.Borra(oConn);
        return strRes;
    }
    
}
