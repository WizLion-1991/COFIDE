package comSIWeb.Operaciones;

/**
 *Representa los permisos del sistema
 * @author zeus
 */
public class permisos_sistema extends TableMaster {

   public permisos_sistema() {
      super("permisos_sistema", "PS_ID", "", "");
      this.Fields.put("PS_ID", new Integer(0));
      this.Fields.put("PS_DESCRIPCION", "");
      this.Fields.put("PS_BITACORA", new Integer(0));
      this.Fields.put("PS_SECCION", new Integer(0));
      this.Fields.put("PS_SUBSECCION", new Integer(0));
      this.Fields.put("PS_ORDEN", new Integer(0));
      this.Fields.put("PS_ESMENU", new Integer(0));
      this.Fields.put("PS_ESPERMISO", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("PS_LINK", "");
      this.Fields.put("PS_IMAGEN", "");
      this.Fields.put("PS_ALTERNO", "");
   }
}
