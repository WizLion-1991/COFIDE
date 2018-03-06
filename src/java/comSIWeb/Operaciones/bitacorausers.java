package comSIWeb.Operaciones;

/**
 *Es la bitacora de actividades de los usuarios
 * @author zeus
 */
public class bitacorausers extends TableMaster {

   public bitacorausers() {
      super("bitacorausers", "BTU_ID", "", "");
      this.Fields.put("BTU_ID", new Integer(0));
      this.Fields.put("BTU_FECHA", "");
      this.Fields.put("BTU_HORA", "");
      this.Fields.put("BTU_IDUSER", new Integer(0));
      this.Fields.put("BTU_IDOPER", new Integer(0));
      this.Fields.put("BTU_NOMUSER", "");
      this.Fields.put("BTU_NOMMOD", "");
      this.Fields.put("BTU_NOMACTION", "");
      this.setBolStrict(true);
   }
}
