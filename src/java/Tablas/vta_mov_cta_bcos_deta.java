package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa un movimiento a detalle de CAJA/BANCOS
 * @author zeus
 */
public class vta_mov_cta_bcos_deta extends TableMaster {

   /**
    * Constructor del movimiento de bancos a detalle
    */
   public vta_mov_cta_bcos_deta( ) {
      super("vta_mov_cta_bcos_deta", "MCBD_ID", "", "");
      this.Fields.put("MCBD_ID", new Integer(0));
      this.Fields.put("MCB_ID", new Integer(0));
      this.Fields.put("MCBD_IMPORTE", new Double(0));
      this.Fields.put("MCBD_CONCEPTO", "");
      this.Fields.put("GT_ID", new Integer(0));
      this.Fields.put("CC_ID", new Integer(0));
   }
}
