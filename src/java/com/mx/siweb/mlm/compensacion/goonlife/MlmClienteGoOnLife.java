/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.goonlife;

import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;

/**
 *Representa un cliente del plan de GoOnLife
 * @author ZeusSIWEB
 */
public class MlmClienteGoOnLife extends mlm_cliente {
   public int intNivelMax;
   public int intPeriodoActivo;
   public int intActivo;
   public double dblGananciaDuoLevel1;
   public double dblGananciaDuoLevel2;

   public int getIntNivelMax() {
      return intNivelMax;
   }

   public void setIntNivelMax(int intNivelMax) {
      this.intNivelMax = intNivelMax;
   }

   public double getDblGananciaDuoLevel1() {
      return dblGananciaDuoLevel1;
   }

   public void setDblGananciaDuoLevel1(double dblGananciaDuoLevel1) {
      this.dblGananciaDuoLevel1 = dblGananciaDuoLevel1;
   }

   public double getDblGananciaDuoLevel2() {
      return dblGananciaDuoLevel2;
   }

   public void setDblGananciaDuoLevel2(double dblGananciaDuoLevel2) {
      this.dblGananciaDuoLevel2 = dblGananciaDuoLevel2;
   }

   public int getIntPeriodoActivo() {
      return intPeriodoActivo;
   }

   public void setIntPeriodoActivo(int intPeriodoActivo) {
      this.intPeriodoActivo = intPeriodoActivo;
   }

   public int getIntActivo() {
      return intActivo;
   }

   public void setIntActivo(int intActivo) {
      this.intActivo = intActivo;
   }
   
}
