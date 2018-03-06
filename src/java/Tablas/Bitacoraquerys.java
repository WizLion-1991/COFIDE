package Tablas;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la bitacora de querys
 */
public class Bitacoraquerys extends TableMaster {

   public Bitacoraquerys() {
      super("bitacoraquerys", "idbitacoraQuerys","","");
      this.Fields.put("idbitacoraQuerys", new Integer(0));
      this.Fields.put("FECHA", "");
      this.Fields.put("HORA", "");
      this.Fields.put("USUARIO", "");
      this.Fields.put("SENTENCIA", "");
      this.Fields.put("ACCION", "");
   }
 /**
    * Inserta el registro en la base de datos
    * @param oConn Es la conexion
    * @return Nos regresa OK si todo fue satisfactorio
    */
   @Override
   public String Agrega(Conexion oConn) {
      oConn.setBolCleanError(false);
      String strRes = "OK";
      String strSql = this.getSQLInsert();
      oConn.runQueryLMD(strSql);
      oConn.setBolCleanError(true);
      return strRes;
   }
}


