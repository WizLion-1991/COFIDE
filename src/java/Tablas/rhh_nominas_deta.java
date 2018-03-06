/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa el detalle de una nomina
 *
 * @author ZeusGalindo
 */
public class rhh_nominas_deta extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public rhh_nominas_deta() {
      super("rhh_nominas_deta", "NOMD_ID", "", "");
      this.Fields.put("NOMD_ID", new Integer(0));
      this.Fields.put("NOM_ID", new Integer(0));
      this.Fields.put("TP_ID", new Integer(0));
      this.Fields.put("TI_ID", new Integer(0));
      this.Fields.put("TD_ID", new Integer(0));
      this.Fields.put("PERC_ID", new Integer(0));
      this.Fields.put("DEDU_ID", new Integer(0));
      this.Fields.put("NOMD_CANTIDAD", new Integer(0));
      this.Fields.put("NOMD_UNITARIO", new Double(0));
      this.Fields.put("NOMD_IMPORTE_GRAVADO", new Double(0));
      this.Fields.put("NOMD_IMPORTE_EXENTO", new Double(0));
      this.Fields.put("NOMD_GRAVADO", new Integer(0));
      this.Fields.put("NOMD_NOTAS", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
