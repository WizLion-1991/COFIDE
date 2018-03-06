package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de pagos masivos
 * @author zeus
 */
public class vta_mov_cte_mas extends TableMaster {

   public vta_mov_cte_mas() {
      super("vta_mov_cte_mas", "MCM_ID", "", "");
      this.Fields.put("MCM_ID", new Integer(0));
      this.Fields.put("MCM_FECHA", "");
      this.Fields.put("MCM_FECHACREATE", "");
      this.Fields.put("MCM_HORA", "");
      this.Fields.put("MCM_FECHAANUL", "");
      this.Fields.put("MCM_HORAANUL", "");
      this.Fields.put("MCM_IDUSER", new Integer(0));
      this.Fields.put("MCM_ANULADO", new Integer(0));
      this.Fields.put("MCM_IDUSERANUL", new Integer(0));
      this.Fields.put("MCM_TOTAL", new Double(0));
      this.Fields.put("MCM_TOTOPER", new Integer(0));
   }
}
