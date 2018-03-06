package Tablas;
import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla PermisosUsuario
 */
public class PermisosUsuario extends TableMaster {

   public PermisosUsuario() {
      super("permisos_usuario", "ID_PERMISOS_USUARIO", "", "");
      this.Fields.put("ID_PERMISOS_USUARIO", new Integer(0));
      this.Fields.put("ID_USUARIOS", new Integer(0));
      this.Fields.put("ID_PERMISOS_SISTEMA", new Integer(0));
   }
}
