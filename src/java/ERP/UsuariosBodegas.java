/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.usuarios_bodegas;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author N4v1d4d3s
 */
public class UsuariosBodegas {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    protected usuarios_bodegas USU_BO;

    public usuarios_bodegas getUSU_BO() {
        return USU_BO;
    }

    public void setUSU_BO(usuarios_bodegas USU_BO) {
        this.USU_BO = USU_BO;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * *
     * Constructor de UsuariosBodegas
     *
     * @param varSesiones variables de sesion
     */
    public UsuariosBodegas(VariableSession varSesiones) {
        this.USU_BO = new usuarios_bodegas();
    }
   //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Sirve para dar de alta un registro en usuarios_bodegas
     *
     * @param oConn Es la conexion
     * @return
     */
    public String doAltaUserPerfil(Conexion oConn) {
        String strRes = "";
        try {
            this.USU_BO.setBolGetAutonumeric(true);
            strRes = this.USU_BO.Agrega(oConn);

        } catch (Exception ex) {
            strRes = ex.getMessage();
        }

        return strRes;
    }

    
    /**
     * Sirve para borrar un registro de usuarios_bodegas
     * @param oConn Es la conexion
     * @return 
     */
    public String deleteUserPerfil(Conexion oConn) {
        String strRes = "";
        try {
            this.USU_BO.setBolGetAutonumeric(true);
            strRes = this.USU_BO.Borra(oConn);

        } catch (Exception ex) {
            strRes = ex.getMessage();
        }

        return strRes;
    }

   // </editor-fold>
}
