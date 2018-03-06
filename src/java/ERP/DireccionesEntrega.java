/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_cliente_dir_entrega;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author N4v1d4d3s
 */
public class DireccionesEntrega {

    protected vta_cliente_dir_entrega DIR;

    /*Obtiene los valores del objeto*/
    public vta_cliente_dir_entrega getDIR() {
        return DIR;
    }

    /*Define los valores del objeto*/
    public void setDIR(vta_cliente_dir_entrega DIR) {
        this.DIR = DIR;
    }

    public DireccionesEntrega(VariableSession varSesiones) {
        this.DIR = new vta_cliente_dir_entrega();
    }

    /**
     * Sirve para obtener las direcciones de entrega del cliente selecionado
     * @param intCtId int Es el id del cliente seleccionado
     * @param oConn Es la conexion
     * @return Es el xml con los datos de la direccion
     */
    public String getDirecciones(int intCtId, Conexion oConn) {
        String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        UtilXml util = new UtilXml();
        //Armamos el xml
        try {

            String strConsulta = "SELECT *,concat(CDE_CALLE,' ',CDE_COLONIA,' ',CDE_DESCRIPCION) AS dir_cliente"
                    + " FROM vta_cliente_dir_entrega WHERE CT_ID = " + intCtId;

            ResultSet rs = oConn.runQuery(strConsulta);

            strXMLData += "<Direcciones>";
            while (rs.next()) {
                strXMLData += "<direcciones";
                strXMLData += " id_dir=\'" + rs.getInt("CDE_ID") + "\'";
                strXMLData += " dir_cliente=\'" + util.Sustituye(rs.getString("dir_cliente")) + "\'";
                strXMLData += "/>";
            }

            strXMLData += "</Direcciones>";
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return strXMLData;
    }

    /**
     * Sirve para dar de alta una nueva direccion de entrega
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String saveDireccionEntrega(Conexion oConn) {
        String strRes = "";

        try {
            this.DIR.setBolGetAutonumeric(true);
            strRes = this.DIR.Agrega(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
    
     /**
     * Sirve para eliminar direccion de entrega
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String delDireccionEntrega(Conexion oConn) {
        String strRes = "";

        try {
            this.DIR.setBolGetAutonumeric(true);
            strRes = this.DIR.Borra(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
    
    /**
     * Sirve para modificar direccion de entrega
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String editDireccionEntrega(Conexion oConn) {
        String strRes = "";

        try {
            this.DIR.setBolGetAutonumeric(true);
            strRes = this.DIR.Modifica(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
}
