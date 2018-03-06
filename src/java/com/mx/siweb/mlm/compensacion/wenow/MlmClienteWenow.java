/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.wenow;

import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;

/**
 *
 * @author ZeusGalindo
 */
public class MlmClienteWenow extends mlm_cliente {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private boolean bolTraining;
   private boolean bolAfiliado;
   private boolean bolGlobal;
   private int intPeriodo;
   private boolean bolRecompra;

   public boolean isBolRecompra() {
      return bolRecompra;
   }

   public void setBolRecompra(boolean bolRecompra) {
      this.bolRecompra = bolRecompra;
   }

   public int getIntPeriodo() {
      return intPeriodo;
   }

   public void setIntPeriodo(int intPeriodo) {
      this.intPeriodo = intPeriodo;
   }

   public boolean isBolTraining() {
      return bolTraining;
   }

   public void setBolTraining(boolean bolTraining) {
      this.bolTraining = bolTraining;
   }

   public boolean isBolAfiliado() {
      return bolAfiliado;
   }

   public void setBolAfiliado(boolean bolAfiliado) {
      this.bolAfiliado = bolAfiliado;
   }

   public boolean isBolGlobal() {
      return bolGlobal;
   }

   public void setBolGlobal(boolean bolGlobal) {
      this.bolGlobal = bolGlobal;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
