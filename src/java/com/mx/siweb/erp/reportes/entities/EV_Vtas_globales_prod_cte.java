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
public class EV_Vtas_globales_prod_cte {

   private int intCT_IT;
   private String strRazonSocial;
   private String strClasif;
   private double dblTSubtotal;
   private double dblTSubtotal_Porc;
   private double dblCantidad;

   public double getDblCantidad() {
      return dblCantidad;
   }

   public void setDblCantidad(double dblCantidad) {
      this.dblCantidad = dblCantidad;
   }

   public void setIntCT_IT(int intCT_IT) {
      this.intCT_IT = intCT_IT;
   }

   public void setStrRazonSocial(String strRazonSocial) {
      this.strRazonSocial = strRazonSocial;
   }

   public void setStrClasif(String strClasif) {
      this.strClasif = strClasif;
   }

   public void setDblTSubtotal(double dblTSubtotal) {
      this.dblTSubtotal = dblTSubtotal;
   }

   public void setDblTSubtotal_Porc(double dblTSubtotal_Porc) {
      this.dblTSubtotal_Porc = dblTSubtotal_Porc;
   }

   public int getIntCT_IT() {
      return intCT_IT;
   }

   public String getStrRazonSocial() {
      return strRazonSocial;
   }

   public String getStrClasif() {
      return strClasif;
   }

   public double getDblTSubtotal() {
      return dblTSubtotal;
   }

   public double getDblTSubtotal_Porc() {
      return dblTSubtotal_Porc;
   }

}
