/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_pedimento_rep;
import comSIWeb.Operaciones.Conexion;

/**
 *
 * @author siwebmx5
 */
public class PedimentosDoc {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private vta_pedimento_rep PR;
   // </editor-fold>
       
// <editor-fold defaultstate="collapsed" desc="Constructores">
    public PedimentosDoc() {
        this.PR = new vta_pedimento_rep();
    }
    public vta_pedimento_rep getPR() {
        return this.PR;
    }

    public void setPR(vta_pedimento_rep PR) {
        this.PR = PR;
    }
   // </editor-fold>
   
// <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
     * Sirve para dar de alta un documento de los pedimentos
     * @param oConn Es la conexion 
     * @return Es el estatus del insert OK= Alta exitosa
     */
    public String savePedimentosDoc(Conexion oConn) {
        String strRes = "";

        try {
            this.PR.setBolGetAutonumeric(true);
            strRes = this.PR.Agrega(oConn);
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
    
    
    public String delPedimentosDoc(Conexion oConn) {
        String strRes = "";

        try {
            this.PR.setBolGetAutonumeric(true);
            strRes = this.PR.Borra(oConn);
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
    public String editPedimentosDoc(Conexion oConn) {
        String strRes = "";

        try {
            this.PR.setBolGetAutonumeric(true);
            strRes = this.PR.Modifica(oConn);
        } catch (Exception e) {
            strRes = e.getMessage();
        }
        return strRes;
    }
    // </editor-fold>

    
}
