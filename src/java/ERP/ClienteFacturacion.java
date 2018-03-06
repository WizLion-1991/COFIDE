/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_cliente_facturacion;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author siwebmx5
 */
public class ClienteFacturacion {

    protected vta_cliente_facturacion CF;

    public vta_cliente_facturacion getCF() {
        return CF;
    }

    public void setCF(vta_cliente_facturacion CF) {
        this.CF = CF;
    }

    public ClienteFacturacion(VariableSession varSesiones) {
        this.CF = new vta_cliente_facturacion();
    }

    /**
     * Sirve para dar de alta una nueva ClienteFacturacion
     *
     * @param oConn Es la conexion
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String saveClienteFacturacion(Conexion oConn) {
        String strRes = "";

        try {
            this.CF.setBolGetAutonumeric(true);
            strRes = this.CF.Agrega(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }

    public String saveClienteFacturacionPed(Conexion oConn) {
        String strRes = "";

        try {
            this.CF.setBolGetAutonumeric(true);
            this.CF.Agrega(oConn);
            strRes = this.CF.getValorKey();
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }

    /**
     * Sirve para obtener las direcciones de entrega del cliente selecionado
     *
     * @param intCtId int Es el id del cliente seleccionado
     * @param oConn Es la conexion
     * @return Es el xml con los datos de la direccion
     */
    public String getDirecciones(int intCtId, Conexion oConn) {
        String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        UtilXml util = new UtilXml();
        //Armamos el xml
        try {

            String strConsulta = "SELECT *,concat(DFA_CALLE,' ',DFA_COLONIA,' ') AS dir_cliente"
                    + " FROM vta_cliente_facturacion WHERE CT_ID = " + intCtId;

            ResultSet rs = oConn.runQuery(strConsulta);

            strXMLData += "<Direcciones>";
            while (rs.next()) {
                strXMLData += "<direcciones";
                strXMLData += " id_dir=\'" + rs.getInt("DFA_ID") + "\'";
                strXMLData += " dir_cliente=\'" + util.Sustituye(rs.getString("dir_cliente")) + "\'";
                strXMLData += "/>";
                System.out.println(strXMLData);
            }

            strXMLData += "</Direcciones>";
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return strXMLData;
    }

    public String getNombres(int intCtId, Conexion oConn) {
        String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        UtilXml util = new UtilXml();
        //Armamos el xml
        try {

            String strConsulta = "SELECT *,concat(DFA_CALLE,' ',DFA_COLONIA,' ') AS dir_cliente"
                    + " FROM vta_cliente_facturacion WHERE CT_ID = " + intCtId + " Or DFA_VISIBLE = 1";

            ResultSet rs = oConn.runQuery(strConsulta);

            strXMLData += "<Direcciones>";
            while (rs.next()) {
                strXMLData += "<direcciones";
                strXMLData += " id_dir=\'" + rs.getInt("DFA_ID") + "\'";
                strXMLData += " dir_cliente=\'" + util.Sustituye(rs.getString("DFA_RAZONSOCIAL")) + "\'";
                strXMLData += "/>";
                System.out.println(strXMLData);
            }

            strXMLData += "</Direcciones>";
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return strXMLData;
    }

    /**
     * Sirve para eliminar ClienteFacturacion
     *
     * @param oConn Es la conexion
     * @return Es el estatus del insert OK= Alta exitosa
     */

    public String delClienteFacturacion(Conexion oConn) {
        String strRes = "";

        try {
            this.CF.setBolGetAutonumeric(true);
            strRes = this.CF.Borra(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }

    /**
     * Sirve para modificar ClienteFacturacion
     *
     * @param oConn Es la conexion
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String editClienteFacturacion(Conexion oConn) {
        String strRes = "";

        try {
            this.CF.setBolGetAutonumeric(true);
            strRes = this.CF.Modifica(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
}
