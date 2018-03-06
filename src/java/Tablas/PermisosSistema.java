package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla PermisosSistema
 */
public class PermisosSistema extends TableMaster {

   public PermisosSistema() {
      super("permisos_sistema", "ID_PERMISOS_SISTEMA", "", "");
      this.Fields.put("ID_PERMISOS_SISTEMA", new Integer(0));
      this.Fields.put("DESCRIPCION_PERMISO", "");
      this.Fields.put("GENERABITACORA", new Integer(0));
      this.Fields.put("SECCION", new Integer(0));
      this.Fields.put("ORDEN", new Integer(0));
      this.Fields.put("ID_MENU", new Integer(0));
   }
}

