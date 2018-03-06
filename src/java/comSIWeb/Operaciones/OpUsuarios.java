package comSIWeb.Operaciones;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Esta clase implementa la logica de negocios para un usuario
 *
 * @author zeus
 */
public class OpUsuarios {

   /**
    * Guarda la fecha de la ultima actividad del usuario
    */
   public void SaveLastActivity(int intUser, int intIdCliente, Conexion oConn) {
      //Obtenemos la fecha y el momento actual
      Date today = (Date) Calendar.getInstance().getTime();
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      StringBuilder strUpdate = new StringBuilder();

      if (intIdCliente == 0) {
            strUpdate.append("UPDATE usuarios set " + "LAST_ACT='").append(dateFormat.format(today)).append("',LAST_TIME='").append(today.getTime()).append("'  " + "where id_usuarios=").append(intUser);
      } else {
            strUpdate.append("UPDATE vta_cliente set " + "CT_LAST_ACT='").append(dateFormat.format(today)).append("',CT_LAST_TIME='").append(today.getTime()).append("' " + "where CT_ID=").append(intIdCliente)  ;
      }

      oConn.runQueryLMD(strUpdate.toString());
   }
}
