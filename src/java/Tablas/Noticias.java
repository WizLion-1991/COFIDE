package Tablas;
import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla Noticias
 */
public class Noticias extends TableMaster {

   public Noticias() {
      super("noticias", "idnoticias", "", "");
      this.Fields.put("idnoticias", new Integer(0));
      this.Fields.put("activo", new Integer(0));
      this.Fields.put("fecha_ini", new Integer(0));
      this.Fields.put("fecha_fin", new Integer(0));
      this.Fields.put("texto", "");
      this.Fields.put("link", "");
   }
}
