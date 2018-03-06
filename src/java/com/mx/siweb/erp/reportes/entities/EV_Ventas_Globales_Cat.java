/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class EV_Ventas_Globales_Cat {

   private String strCalsif;
   private Double dblSubTotal;
   private Double dblCantidad;

   public Double getDblCantidad() {
      return dblCantidad;
   }

   public void setDblCantidad(Double dblCantidad) {
      this.dblCantidad = dblCantidad;
   }

   public void setStrCalsif(String strCalsif) {
      this.strCalsif = strCalsif;
   }

   public void setDblSubTotal(Double dblSubTotal) {
      this.dblSubTotal = dblSubTotal;
   }

   public String getStrCalsif() {
      return strCalsif;
   }

   public Double getDblSubTotal() {
      return dblSubTotal;
   }

}
