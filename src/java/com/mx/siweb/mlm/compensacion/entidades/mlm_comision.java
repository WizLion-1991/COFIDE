/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa las comisiones de un cliente
 * @author aleph_79
 */
public class mlm_comision extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public mlm_comision() {
      super("mlm_comision", "CO_ID", "", "");
      this.Fields.put("CO_ID", new Integer(0));
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("MPE_ID", new Integer(0));
      this.Fields.put("CO_IMPORTE", new Double(0));
      this.Fields.put("CO_IMPUESTO1", new Double(0));
      this.Fields.put("CO_IMPUESTO2", new Double(0));
      this.Fields.put("CO_IMPUESTO3", new Double(0));
      this.Fields.put("CO_RET1", new Double(0));
      this.Fields.put("CO_RET2", new Double(0));
      this.Fields.put("CO_RET3", new Double(0));
      this.Fields.put("CO_PUNTOS_P", new Double(0));
      this.Fields.put("CO_PUNTOS_G", new Double(0));
      this.Fields.put("CO_NEGOCIO_P", new Double(0));
      this.Fields.put("CO_NEGOCIO_G", new Double(0));
      this.Fields.put("CO_CHEQUE", new Double(0));
      this.Fields.put("CO_NIVEL", new Integer(0));
      this.Fields.put("CT_NIVELRED", new Integer(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
