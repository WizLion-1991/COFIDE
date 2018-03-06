/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Clase que relaciona los pagos con el movimiento del banco
 * @author ZeusGalindo
 */
public class vta_mov_cta_bcos_rela extends TableMaster {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public vta_mov_cta_bcos_rela() {
      super("vta_mov_cta_bcos_rela", "MCBD_ID", "", "");
      this.Fields.put("MCBD_ID", new Integer(0));
      this.Fields.put("MC_ID", new Integer(0));
      this.Fields.put("MCB_ID", new Integer(0));
      this.Fields.put("MP_ID", new Integer(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
