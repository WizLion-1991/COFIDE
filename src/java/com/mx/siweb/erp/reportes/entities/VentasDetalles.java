

package com.mx.siweb.erp.reportes.entities;

import java.util.ArrayList;


public class VentasDetalles {
   // <editor-fold defaultstate="collapsed" desc="Propiedades GET&SET">
   private String sucursal;
   private String factura;
   private String fecha;
   private double totalFactura;
   private double impuestos;
   private double subTotal;
   private double descuentos;
   private String notasCredito;
   
   
   
    // </editor-fold>

   public String getSucursal() {
      return sucursal;
   }

   public void setSucursal(String sucursal) {
      this.sucursal = sucursal;
   }

   public String getFactura() {
      return factura;
   }

   public void setFactura(String factura) {
      this.factura = factura;
   }

   public String getFecha() {
      return fecha;
   }

   public void setFecha(String fecha) {
      this.fecha = fecha;
   }

   public double getTotalFactura() {
      return totalFactura;
   }

   public void setTotalFactura(double totalFactura) {
      this.totalFactura = totalFactura;
   }

   public double getImpuestos() {
      return impuestos;
   }

   public void setImpuestos(double impuestos) {
      this.impuestos = impuestos;
   }

   public double getSubTotal() {
      return subTotal;
   }

   public void setSubTotal(double subTotal) {
      this.subTotal = subTotal;
   }

   public double getDescuentos() {
      return descuentos;
   }

   public void setDescuentos(double descuentos) {
      this.descuentos = descuentos;
   }

   public String getNotasCredito() {
      return notasCredito;
   }

   public void setNotasCredito(String notasCredito) {
      this.notasCredito = notasCredito;
   }
}
