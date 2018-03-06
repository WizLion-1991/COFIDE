/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa el detalle de comisiones de un cliente
 * @author aleph_79
 */
public class mlm_comision_deta extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public mlm_comision_deta() {
      super("mlm_comision_deta", "COMI_ID", "", "");
      this.Fields.put("COMI_ID", new Integer(0));
      this.Fields.put("MPE_ID", new Integer(0));
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("COMI_FUENTE", new Integer(0));
      this.Fields.put("COMI_UPLINE", new Integer(0));
      this.Fields.put("COMI_DESTINO", new Integer(0));
      this.Fields.put("COMI_PORCENTAJE", new Double(0));
      this.Fields.put("COMI_IMPORTE", new Double(0));
      this.Fields.put("COMI_IMPORTE_ORIGEN", new Double(0));
      this.Fields.put("COMI_NIVEL", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("COMI_ARMADOINI", new Integer(0));
      this.Fields.put("COMI_ARMADOFIN", new Integer(0));
      this.Fields.put("COMI_ARMADONUM", new Integer(0));
      this.Fields.put("COMI_ARMADODEEP", new Integer(0));
      this.Fields.put("COMI_CODIGO", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
