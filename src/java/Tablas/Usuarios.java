package Tablas;
// Generated 14/02/2010 11:10:21 AM by Hibernate Tools 3.2.1.GA

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla Usuarios
 */
public class Usuarios extends TableMaster {

    public Usuarios() {
        super("usuarios", "id_usuarios", "", "");
        this.Fields.put("id_usuarios", new Integer(0));
        this.Fields.put("nombre_usuario", "");
        this.Fields.put("user", "");
        this.Fields.put("password", "");
        this.Fields.put("UsuarioActivo", new Integer(0));
        this.Fields.put("UsuarioCaptura", new Integer(0));
        this.Fields.put("ctam_id", new Integer(0));
        /*this.Fields.put("LAST_LOGIN_DATE", "");
      this.Fields.put("LAST_LOGIN_HOUR", "");
      this.Fields.put("COUNT_ACCESS", new Integer(0));
      this.Fields.put("COUNT_FAILED", new Integer(0));
      this.Fields.put("LAST_FAIL_DATE", "");
      this.Fields.put("LAST_FAIL_HOUR", "");*/
    }
}
