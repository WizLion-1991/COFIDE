/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.goonlife;

import com.mx.siweb.mlm.compensacion.entidades.mlm_comis_param;

/**
 *Contiene los campos especiales para GoOnLife
 * @author ZeusSIWEB
 */
public class ParametrosComis extends mlm_comis_param{
   
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ParametrosComis() {
      super();
      this.Fields.put("CP_VENTAS_1", 0.0);
      this.Fields.put("CP_VENTAS_2", 0.0);
      this.Fields.put("CP_BONO_RECOMPRA", 0.0);
      
   }   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
