
package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class ProductosResumen {
   private String codigo;
   private String nombre;
   private double cantidad;
   private double valor;
   
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
