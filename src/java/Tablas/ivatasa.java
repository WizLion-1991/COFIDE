package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author zeus
 */
public class ivatasa extends TableMaster {

   public ivatasa() {
      super("ivatasa", "IVA_ID", "", "");
      this.Fields.put("IVA_ID", new Integer(0));
      this.Fields.put("IVA_TASA", new Double(0));
      this.Fields.put("IVA_DESCRIPCION", "");
   }
}
