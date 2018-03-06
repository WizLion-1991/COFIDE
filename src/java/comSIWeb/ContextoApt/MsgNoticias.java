package comSIWeb.ContextoApt;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.CIP_UtilCharset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Genera las noticias de la administración del producto
 * @author zeus
 */
public class MsgNoticias {

   /**
    * Regresa un div con las noticias por publicar
    * @param oConn Es la conexion a la base de datos
    * @param strFechaActual Es la fecha actual
    * @return Nos regresa una cadena con el texto del div de los enlaces de las noticias
    */
   public String MuestraNoticias( Conexion oConn, String strFechaActual) {
      CIP_UtilCharset util = new CIP_UtilCharset();
      String strMsgNoticias = "";


      System.out.println("OBTENEMOS LAS NOTICIAS....");

      /*Validamos si el usuario existe en la base de datos*/
      String SQL_QUERY = "select * from Noticias where  " + strFechaActual + "  between fecha_ini "
              + " and fecha_fin  ";
      ResultSet rs;
      try {
         rs = oConn.runQuery(SQL_QUERY, true);
         while (rs.next()) {
            String strCadena = "<a href='" + rs.getString("link") + "' class='ui-menu'>" + rs.getString("texto") + "</a><br>";
            String strValor = util.Sustituye(strCadena);
            strMsgNoticias += strValor;
         }
         //Cerrar resultset
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(MsgNoticias.class.getName()).log(Level.SEVERE, null, ex);
      }
      return strMsgNoticias;
   }
}
