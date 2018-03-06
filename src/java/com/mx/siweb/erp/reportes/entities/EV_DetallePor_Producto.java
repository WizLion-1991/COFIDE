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
public class EV_DetallePor_Producto {

   private String strNum;
   private String strDescripcion;
   private String strCalsif1;
   private String strCalsif2;
   private String strCalsif3;
   private String strCalsif4;
   private String strCalsif5;
   private Double dblSubTotal;
   private Double dblCantidad;

   public Double getDblCantidad() {
      return dblCantidad;
   }

   public void setDblCantidad(Double dblCantidad) {
      this.dblCantidad = dblCantidad;
   }

   public void setStrNum(String strNum) {
      this.strNum = strNum;
   }

   public void setStrDescripcion(String strDescripcion) {
      this.strDescripcion = strDescripcion;
   }

   public void setStrCalsif1(String strCalsif1) {
      this.strCalsif1 = strCalsif1;
   }

   public void setStrCalsif2(String strCalsif2) {
      this.strCalsif2 = strCalsif2;
   }

   public void setStrCalsif3(String strCalsif3) {
      this.strCalsif3 = strCalsif3;
   }

   public void setStrCalsif4(String strCalsif4) {
      this.strCalsif4 = strCalsif4;
   }

   public void setStrCalsif5(String strCalsif5) {
      this.strCalsif5 = strCalsif5;
   }

   public void setDblSubTotal(Double dblSubTotal) {
      this.dblSubTotal = dblSubTotal;
   }

   public String getStrNum() {
      return strNum;
   }

   public String getStrDescripcion() {
      return strDescripcion;
   }

   public String getStrCalsif1() {
      return strCalsif1;
   }

   public String getStrCalsif2() {
      return strCalsif2;
   }

   public String getStrCalsif3() {
      return strCalsif3;
   }

   public String getStrCalsif4() {
      return strCalsif4;
   }

   public String getStrCalsif5() {
      return strCalsif5;
   }

   public Double getDblSubTotal() {
      return dblSubTotal;
   }

}
