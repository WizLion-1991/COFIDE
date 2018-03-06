/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_cliente_facturacion;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;

/**
 *
 * @author N4v1d4d3s
 */
public class SubclientesFacturacion {

    protected vta_cliente_facturacion SUB;

    public vta_cliente_facturacion getSUB() {
        return SUB;
    }

    public void setSUB(vta_cliente_facturacion SUB) {
        this.SUB = SUB;
    }

    public SubclientesFacturacion(VariableSession varSesiones) {
        this.SUB = new vta_cliente_facturacion();
    }

    /**
     * Sirve para dar de alta una nueva direccion de entrega
     *
     * @param oConn Es la conexion
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String save_subClienteFacturacion(Conexion oConn) {
        String strRes = "";
        try {
            this.SUB.setBolGetAutonumeric(true);
            strRes = this.SUB.Agrega(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }

}
