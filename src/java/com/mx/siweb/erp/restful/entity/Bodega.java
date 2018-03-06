/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

/**
 * Representa una bodega
 *
 * @author ZeusSIWEB
 */
public class Bodega {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int id_bodega;
   private String nombre_bodega;
   private int id_sucursal;
   private int id_empresa;
   private String codigo_bodega;

   public String getCodigo_bodega() {
      return codigo_bodega;
   }

   public void setCodigo_bodega(String codigo_bodega) {
      this.codigo_bodega = codigo_bodega;
   }

   public int getId_bodega() {
      return id_bodega;
   }

   public void setId_bodega(int id_bodega) {
      this.id_bodega = id_bodega;
   }

   public String getNombre_bodega() {
      return nombre_bodega;
   }

   public void setNombre_bodega(String nombre_bodega) {
      this.nombre_bodega = nombre_bodega;
   }

   public int getId_sucursal() {
      return id_sucursal;
   }

   public void setId_sucursal(int id_sucursal) {
      this.id_sucursal = id_sucursal;
   }

   public int getId_empresa() {
      return id_empresa;
   }

   public void setId_empresa(int id_empresa) {
      this.id_empresa = id_empresa;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
