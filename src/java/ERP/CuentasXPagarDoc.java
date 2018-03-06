/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_cxp_rep;
import comSIWeb.Operaciones.Conexion;

/**
 *
 * @author siwebmx5
 */
public class CuentasXPagarDoc {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private vta_cxp_rep CPR;
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CuentasXPagarDoc() {
        this.CPR = new vta_cxp_rep();
    }
    public vta_cxp_rep getCPR() {
        return CPR;
    }

    public void setCPR(vta_cxp_rep CPR) {
        this.CPR = CPR;
    }
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Sirve para dar de alta un documento de los pedimentos
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String saveCXPDoc(Conexion oConn) {
        String strRes = "";

        try {
            this.CPR.setBolGetAutonumeric(true);
            strRes = this.CPR.Agrega(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
     /**
     * Sirve para eliminar un documento de los pedimentos
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    
    
    public String delCXPDoc(Conexion oConn) {
        String strRes = "";

        try {
            this.CPR.setBolGetAutonumeric(true);
            strRes = this.CPR.Borra(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
    
    /**
     * Sirve para modificar un documento de los pedimentos
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String editCXPDoc(Conexion oConn) {
        String strRes = "";

        try {
            this.CPR.setBolGetAutonumeric(true);
            strRes = this.CPR.Modifica(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }

    // </editor-fold>

    
    
    
}
