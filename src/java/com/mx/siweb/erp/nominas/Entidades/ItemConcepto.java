/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas.Entidades;

/**
 * Representa cualquier percepcion o deduccion configurada
 *
 * @author ZeusGalindo
 */
public class ItemConcepto {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intId;
   private int intTipoId;
   private String strCve;
   private String strFormula;
   private String strResultEjecucion;
   private boolean bolEsPercepcion;
   private boolean bolSeEjecutoOK;
   private double dblImporte = 0;
   
   public int getIntId() {
      return intId;
   }

   public void setIntId(int intId) {
      this.intId = intId;
   }

   public int getIntTipoId() {
      return intTipoId;
   }

   public void setIntTipoId(int intTipoId) {
      this.intTipoId = intTipoId;
   }

   public String getStrCve() {
      return strCve;
   }

   public void setStrCve(String strCve) {
      this.strCve = strCve;
   }

   public String getStrFormula() {
      return strFormula;
   }

   public void setStrFormula(String strFormula) {
      this.strFormula = strFormula;
   }

   public boolean isBolEsPercepcion() {
      return bolEsPercepcion;
   }

   public void setBolEsPercepcion(boolean bolEsPercepcion) {
      this.bolEsPercepcion = bolEsPercepcion;
   }

   public double getDblImporte() {
      return dblImporte;
   }

   public void setDblImporte(double dblImporte) {
      this.dblImporte = dblImporte;
   }

   public String getStrResultEjecucion() {
      return strResultEjecucion;
   }

   public void setStrResultEjecucion(String strResultEjecucion) {
      this.strResultEjecucion = strResultEjecucion;
   }

   public boolean isBolSeEjecutoOK() {
      return bolSeEjecutoOK;
   }

   public void setBolSeEjecutoOK(boolean bolSeEjecutoOK) {
      this.bolSeEjecutoOK = bolSeEjecutoOK;
   }
   
   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ItemConcepto() {
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
