/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de consecuentes de las promociones(oferta)
 * @author aleph_79
 */
public class vta_promo_consecuente extends TableMaster {

   public vta_promo_consecuente() {
      super("vta_promo_consecuente", "PCO_ID", "", "");
      this.Fields.put("PCO_ID", new Integer(0));
      this.Fields.put("PCO_TIPO", new Integer(0));
      this.Fields.put("PCO_DESCRIPCION", "");
      this.Fields.put("PCO_DESC", new Double(0));
      this.Fields.put("PCO_AP_DESC_PTO", new Integer(0));
      this.Fields.put("PCO_AP_DESC_VN", new Integer(0));
      this.Fields.put("PCO_AP_DESC_IMPORTE", new Integer(0));
      this.Fields.put("PCO_LST_REGALO", "");
      this.Fields.put("PCO_NUMCLAS_REGALO", new Integer(0));
      this.Fields.put("PCO_CLAS_REGALO", new Integer(0));
      this.Fields.put("PCO_PREC", new Double(0));
      this.Fields.put("PCO_PREC_PTO", new Double(0));
      this.Fields.put("PCO_PREC_VN", new Double(0));
      this.Fields.put("PCO_PREC_LST", "");
      this.Fields.put("PCO_PREC_NUMCLAS", new Integer(0));
      this.Fields.put("PCO_PREC_CLAS", new Integer(0));
      this.Fields.put("PCO_PREC_LEAL", new Double(0));
      this.Fields.put("PCO_AP_DESC_LEAL", new Integer(0));
      this.Fields.put("PCO_SCRIPT", "");
      this.Fields.put("PCO_MSG", "");
      this.Fields.put("PCO_STOP", new Integer(0));
      this.Fields.put("PCO_LIBSCRIPT", "");
      this.Fields.put("PCO_LPRECIOS", new Integer(0));
      this.Fields.put("PCO_REGA_CANTIDAD", new Integer(0));
   }
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
