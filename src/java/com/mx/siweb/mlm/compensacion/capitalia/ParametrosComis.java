/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.mlm.compensacion.capitalia;

import com.mx.siweb.mlm.compensacion.entidades.mlm_comis_param;

/**
 *Parametros especiales del plan
 * @author ZeusGalindo
 */
public class ParametrosComis extends mlm_comis_param{

  

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ParametrosComis() {
      super();
      this.Fields.put("CP_EXTRA_LEVEL1", 0.0);
      this.Fields.put("CP_EXTRA_LEVEL2", 0.0);
      this.Fields.put("CP_EXTRA_LEVEL3", 0.0);
      this.Fields.put("CP_EXTRA_LEVEL4", 0.0);
      this.Fields.put("CP_EXTRA_LEVEL5", 0.0);

   }   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>


}
