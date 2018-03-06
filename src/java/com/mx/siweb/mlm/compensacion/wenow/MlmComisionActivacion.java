/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.wenow;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author ZeusGalindo
 */
public class MlmComisionActivacion extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public MlmComisionActivacion() {
      super("mlm_comision_activacion", "CI_ID", "", "");
      this.Fields.put("CI_ID", 0);
      this.Fields.put("CI_FUENTE", 0);
      this.Fields.put("CI_DESTINO", 0);
      this.Fields.put("CI_IMPORTE", 0.0);
      this.Fields.put("CI_FECHA", "");
      this.Fields.put("CI_NIVEL", "");
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
