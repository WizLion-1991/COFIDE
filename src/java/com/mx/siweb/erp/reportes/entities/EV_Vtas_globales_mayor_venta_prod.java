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
public class EV_Vtas_globales_mayor_venta_prod {

   private String strNumeroPr;
   private String strDescripcion;
   private String strCategoria1;
   private String strCategoria2;
   private String strCategoria3;
   private String strCategoria4;
   private String strCategoria5;
   private double dblTSubtotal;
   private double dblTSubtotalPorc;
   private Double dblCantidad;

   public String getStrCategoria1() {
      return strCategoria1;
   }

   public void setStrCategoria1(String strCategoria1) {
      this.strCategoria1 = strCategoria1;
   }

   public String getStrCategoria2() {
      return strCategoria2;
   }

   public void setStrCategoria2(String strCategoria2) {
      this.strCategoria2 = strCategoria2;
   }

   public String getStrCategoria3() {
      return strCategoria3;
   }

   public void setStrCategoria3(String strCategoria3) {
      this.strCategoria3 = strCategoria3;
   }

   public String getStrCategoria4() {
      return strCategoria4;
   }

   public void setStrCategoria4(String strCategoria4) {
      this.strCategoria4 = strCategoria4;
   }

   public String getStrCategoria5() {
      return strCategoria5;
   }

   public void setStrCategoria5(String strCategoria5) {
      this.strCategoria5 = strCategoria5;
   }

   public Double getDblCantidad() {
      return dblCantidad;
   }

   public void setDblCantidad(Double dblCantidad) {
      this.dblCantidad = dblCantidad;
   }

   public String getStrNumeroPr() {
      return strNumeroPr;
   }

   public String getStrDescripcion() {
      return strDescripcion;
   }

   public double getDblTSubtotal() {
      return dblTSubtotal;
   }

   public double getDblTSubtotalPorc() {
      return dblTSubtotalPorc;
   }

   public void setStrNumeroPr(String strNumeroPr) {
      this.strNumeroPr = strNumeroPr;
   }

   public void setStrDescripcion(String strDescripcion) {
      this.strDescripcion = strDescripcion;
   }

   public void setDblTSubtotal(double dblTSubtotal) {
      this.dblTSubtotal = dblTSubtotal;
   }

   public void setDblTSubtotalPorc(double dblTSubtotalPorc) {
      this.dblTSubtotalPorc = dblTSubtotalPorc;
   }

}
