package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa el detalle de movimientos de cargos y abono de un cliente
 * @author zeus
 */
public class vta_mov_cte_deta extends TableMaster {

   public vta_mov_cte_deta() {
      super("vta_mov_cte_deta", "MCD_ID", "", "");
      this.Fields.put("MCD_ID", new Integer(0));
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("MCD_IMPORTE", new Double(0));
      this.Fields.put("MCD_FOLIO", "");
      this.Fields.put("MCD_FORMAPAGO", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("MCD_IMPUESTO1", new Double(0));
      this.Fields.put("MCD_IMPUESTO2", new Double(0));
      this.Fields.put("MCD_IMPUESTO3", new Double(0));
      this.Fields.put("MCD_TASAIMPUESTO1", new Double(0));
      this.Fields.put("MCD_TASAIMPUESTO2", new Double(0));
      this.Fields.put("MCD_TASAIMPUESTO3", new Double(0));
      this.Fields.put("MCD_NOCHEQUE", "");
      this.Fields.put("MCD_BANCO", "");
      this.Fields.put("MCD_NOTARJETA", "");
      this.Fields.put("MCD_TIPOTARJETA", "");
      this.Fields.put("MC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("NC_ID", new Integer(0));
      this.Fields.put("MCD_MONEDA", new Integer(0));
      this.Fields.put("MCD_TASAPESO", new Double(0));
      this.Fields.put("MCD_CAMBIO", new Double(0));
      this.Fields.put("MCM_ID", new Integer(0));
   }
}
