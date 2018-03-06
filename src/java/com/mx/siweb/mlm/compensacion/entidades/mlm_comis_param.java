/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa los parametros de comisiones
 * @author aleph_79
 */
public class mlm_comis_param extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public mlm_comis_param() {
      super("mlm_comis_param", "CP_ID", "", "");
      this.Fields.put("CP_ID", new Integer(0));
      this.Fields.put("CP_NOMBRE_NIVEL", "");
      this.Fields.put("CP_PPUNTOS", new Double(0));
      this.Fields.put("CP_PNEGOCIO", new Double(0));
      this.Fields.put("CP_GPPUNTOS", new Double(0));
      this.Fields.put("CP_GPUNTOS", new Double(0));
      this.Fields.put("CP_GNEGOCIO", new Double(0));
      this.Fields.put("CP_HIST1", new Double(0));
      this.Fields.put("CP_HIJOS", new Integer(0));
      this.Fields.put("CP_UNILEVEL1", new Double(0));
      this.Fields.put("CP_UNILEVEL2", new Double(0));
      this.Fields.put("CP_UNILEVEL3", new Double(0));
      this.Fields.put("CP_UNILEVEL4", new Double(0));
      this.Fields.put("CP_UNILEVEL5", new Double(0));
      this.Fields.put("CP_UNILEVEL6", new Double(0));
      this.Fields.put("CP_UNILEVEL7", new Double(0));
      this.Fields.put("CP_UNILEVEL8", new Double(0));
      this.Fields.put("CP_UNILEVEL9", new Double(0));
      this.Fields.put("CP_UNILEVEL10", new Double(0));
      this.Fields.put("CP_DIFERENCIAL1", new Double(0));
      this.Fields.put("CP_DIFERENCIAL2", new Double(0));
      this.Fields.put("CP_DIFERENCIAL3", new Double(0));
      this.Fields.put("CP_DIFERENCIAL4", new Double(0));
      this.Fields.put("CP_DIFERENCIAL5", new Double(0));
      this.Fields.put("CP_APOYO_ECONOMICO", new Double(0));
      this.Fields.put("CP_BONO_NOMBRARSE", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR1", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR2", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR3", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR4", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR5", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR6", new Double(0));
      this.Fields.put("CP_BONO_NOMBRAR7", new Double(0));
      this.Fields.put("CP_ORDEN", new Integer(0));
      this.Fields.put("CP_NIVEL", new Integer(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
