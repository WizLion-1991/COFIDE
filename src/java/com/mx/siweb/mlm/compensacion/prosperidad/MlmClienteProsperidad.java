/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.prosperidad;

import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;

/**
 * Clase para las entidades de prosperidad
 *
 * @author ZeusSIWEB
 */
public class MlmClienteProsperidad extends mlm_cliente {

   private int intNumSemanas;
   private int intActivo;

   public int getIntNumSemanas() {
      return intNumSemanas;
   }

   public void setIntNumSemanas(int intNumSemanas) {
      this.intNumSemanas = intNumSemanas;
   }

   public int getIntActivo() {
      return intActivo;
   }

   public void setIntActivo(int intActivo) {
      this.intActivo = intActivo;
   }

}
