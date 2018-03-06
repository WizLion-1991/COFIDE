/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cierre de periodos, realizar las operaciones necesarias para este proceso
 *
 * @author ZeusGalindo
 */
public class CierrePeriodos {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   Conexion oConn;

   /**
    * Regresa la conexion
    *
    * @return Es el objeto Conexion
    */
   public Conexion getoConn() {
      return oConn;
   }

   /**
    * Define la conexion
    *
    * @param oConn Es el objeto de conexion
    */
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor con conexion
    *
    * @param oConn Es la conexion a usar
    */
   public CierrePeriodos(Conexion oConn) {
      this.oConn = oConn;
   }

   /**
    * Constructor default
    */
   public CierrePeriodos() {
   }
   // </editor-fold>

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    *Evalua si la fecha es valida respecto a los periodos cerrados
    * @param strFecha Es la fecha por evaluar
    * @param intEMP_ID Es el id de la empresa
    * @param intSC_ID Es el id de la sucursal
    * @return Regresa true si es valido
    */
   public boolean esValido(String strFecha, int intEMP_ID, int intSC_ID) {
      boolean bolValido = true;
      //Extraemos la fecha
      String strAnio = strFecha.substring(0, 4);
      String strMes = strFecha.substring(4, 6);

      String strSql = "SELECT CP_ID FROM vta_cierre_periodo "
              + " WHERE CP_MES = '" + strMes + "' "
              + " AND CP_ANIO= '" + strAnio + "' "
              + " AND EMP_ID = " + intEMP_ID
              + " and SC_ID = " + intSC_ID;
      ResultSet rs;
      try {
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            bolValido = false;
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(CierrePeriodos.class.getName()).log(Level.SEVERE, null, ex);
      }
      return bolValido;
   }
   // </editor-fold>
}
