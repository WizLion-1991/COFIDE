/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;

/**
 *Esta clase se usa para el surtido de mercancia
 * @author aleph_79
 */
public class Surtido extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Surtido(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
   }

   public Surtido(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   public void Init() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrx() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
   // </editor-fold>
}
