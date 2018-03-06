
package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class Pagos {
   private String sucursal;
   private String nPago;
   private String fecha;
   private double valorPago;
   private String formaPago;
   private String nCheque;
   private String referncia;

   public String getSucursal() {
      return sucursal;
   }

   public void setSucursal(String sucursal) {
      this.sucursal = sucursal;
   }

   public String getnPago() {
      return nPago;
   }

   public void setnPago(String nPago) {
      this.nPago = nPago;
   }

   public String getFecha() {
      return fecha;
   }

   public void setFecha(String fecha) {
      this.fecha = fecha;
   }

   public double getValorPago() {
      return valorPago;
   }

   public void setValorPago(double valorPago) {
      this.valorPago = valorPago;
   }

   public String getFormaPago() {
      return formaPago;
   }

   public void setFormaPago(String formaPago) {
      this.formaPago = formaPago;
   }

   public String getnCheque() {
      return nCheque;
   }

   public void setnCheque(String nCheque) {
      this.nCheque = nCheque;
   }

   public String getReferncia() {
      return referncia;
   }

   public void setReferncia(String referncia) {
      this.referncia = referncia;
   }
   
   
   
}
