

package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class Devoluciones {
   
 private String sucursal;
 private String codigo;
 private String nombre;
 private String fecha;
 private String ajuste;
 private double cantidad;
 private double valor;

   public String getSucursal() {
      return sucursal;
   }

   public void setSucursal(String sucursal) {
      this.sucursal = sucursal;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public String getFecha() {
      return fecha;
   }

   public void setFecha(String fecha) {
      this.fecha = fecha;
   }

   public String getAjuste() {
      return ajuste;
   }

   public void setAjuste(String ajuste) {
      this.ajuste = ajuste;
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
 
 
   
}
