/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comSIWeb.Operaciones;
import Tablas.Bitacoraquerys;
import comSIWeb.Utilerias.Fechas;
/**
 *Con este objeto generamos la bitacora
 * @author zeus
 */
public class Bitacora {

   /**
    * Genera la bitacora de la instrucción dada
    * @param strSentencia Es la sentencia pór almacenar
    * @param strUsuario Es el usuario
    * @param strAccion Es la accion ejecutada
    * @param oConn Es la conexion a la base de datos
    */
   public void GeneraBitacora(String strSentencia,String strUsuario,String strAccion,Conexion oConn){
      Fechas fecha = new Fechas();
      if(strSentencia == null)strSentencia = "";
      strSentencia = strSentencia.replaceAll("'", "#");
      Bitacoraquerys  bitacora = new Bitacoraquerys();
      bitacora.setFieldString("FECHA", fecha.getFechaActual());
      bitacora.setFieldString("HORA", fecha.getHoraActual());
      bitacora.setFieldString("USUARIO", strUsuario);
      bitacora.setFieldString("SENTENCIA", strSentencia);
      bitacora.setFieldString("ACCION", strAccion);
      bitacora.Agrega(oConn);
      
   }
}
