package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de productos lote
 * @author zeus
 */
public class vta_prodlote extends TableMaster {

   /**
    * Construtor de producto lote
    */
   public vta_prodlote() {
      super("vta_prodlote", "PL_ID", "", "");
      this.Fields.put("PL_ID", new Integer(0));
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("PL_EXISTENCIA", new Double(0));
      this.Fields.put("PL_NUMLOTE", "");
      this.Fields.put("PL_ORIGEN", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("PL_COSTO", new Double(0));
      this.Fields.put("PL_AUTO", new Integer(0));
      this.Fields.put("PED_ID", new Integer(0));
      this.Fields.put("PL_FECHA", "");
      this.Fields.put("PL_PEDIMENTO", "");
      this.Fields.put("PL_CADFECHA", "");
   }
}
