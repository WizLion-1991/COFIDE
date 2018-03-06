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
 *Esta clase realizara diversas funciones para emitir informacion con la cuenta contratada como espacio en disco
 * @author aleph_79
 */
public class InformesCuenta {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Obtiene el espacio en disco
    * @param oConn Es la conexion
    * @param strNomDB Es el nombre de la base de datos
    * @return
    */
   public String getEspacioDisco(Conexion oConn,String strNomDB) {
      String strEspacioDisco = "";
      String strSql = "SELECT table_schema 'Data_Base_Name', sum( data_length + index_length ) / 1024 / 1024 'Data_Base_Size_in_MB'"
              + "FROM information_schema.TABLES GROUP BY table_schema;";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql);
         while (rs.next()) {
            if(rs.getString("Data_Base_Name").equals(strNomDB)){
               strEspacioDisco = rs.getString("Data_Base_Size_in_MB");
            }
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(InformesCuenta.class.getName()).log(Level.SEVERE, null, ex);
      }

      return strEspacioDisco;
   }
   // </editor-fold>
}
