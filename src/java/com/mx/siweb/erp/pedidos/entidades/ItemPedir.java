/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.pedidos.entidades;

/**
 * Representa un codigo para generar pedido interno o requisicion
 *
 * @author ZeusSIWEB
 */
public class ItemPedir implements Comparable {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   String strCodigo;
   double dblCantidad;
   int intBodega;

   public String getStrCodigo() {
      return strCodigo;
   }

   public void setStrCodigo(String strCodigo) {
      this.strCodigo = strCodigo;
   }

   public double getDblCantidad() {
      return dblCantidad;
   }

   public void setDblCantidad(double dblCantidad) {
      this.dblCantidad = dblCantidad;
   }

   public int getIntBodega() {
      return intBodega;
   }

   public void setIntBodega(int intBodega) {
      this.intBodega = intBodega;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

   @Override
   public int compareTo(Object o) {
      ItemPedir ct2 = (ItemPedir) o;
      if (this.getIntBodega() < ct2.getIntBodega()) {
         return -1;
      } else if (this.getIntBodega() == ct2.getIntBodega()) {
         return 0;
      } else {
         return 1;
      }
   }

}
