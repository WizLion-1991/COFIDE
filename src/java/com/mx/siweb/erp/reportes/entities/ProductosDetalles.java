

package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class ProductosDetalles {
   
   private String codigo;
   private String sucursal;
   private String nombreProducto;
   private String und;
   private String nFactura;
   private String fecha;
   private double cantidad;
   private double valor;
   private double precioUnit;

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public String getSucursal() {
      return sucursal;
   }

   public void setSucursal(String sucursal) {
      this.sucursal = sucursal;
   }

   public String getNombreProducto() {
      return nombreProducto;
   }

   public void setNombreProducto(String nombreProducto) {
      this.nombreProducto = nombreProducto;
   }

   public String getUnd() {
      return und;
   }

   public void setUnd(String und) {
      this.und = und;
   }

   public String getnFactura() {
      return nFactura;
   }

   public void setnFactura(String nFctura) {
      this.nFactura = nFctura;
   }

   public String getFecha() {
      return fecha;
   }

   public void setFecha(String fecha) {
      this.fecha = fecha;
   }

   public double getCantidad() {
      return cantidad;
   }

   public void setCantidad(double cantidad) {
      this.cantidad = cantidad;
   }

   public double getValor() {
      return valor;
   }

   public void setValor(double valor) {
      this.valor = valor;
   }

   public double getPrecioUnit() {
      return precioUnit;
   }

   public void setPrecioUnit(double precioUnit) {
      this.precioUnit = precioUnit;
   }
   
   
}
