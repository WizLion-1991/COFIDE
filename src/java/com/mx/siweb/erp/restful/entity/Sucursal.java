/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

/**
 * Representa una sucursal
 *
 * @author ZeusSIWEB
 */
public class Sucursal {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int id_sucursal;
   private String nombre_sucursal;
   private String codigo_sucursal;
   private int id_empresa;

   public String getCodigo_sucursal() {
      return codigo_sucursal;
   }

   public void setCodigo_sucursal(String codigo_sucursal) {
      this.codigo_sucursal = codigo_sucursal;
   }

   public int getId_sucursal() {
      return id_sucursal;
   }

   public void setId_sucursal(int id_sucursal) {
      this.id_sucursal = id_sucursal;
   }

   public String getNombre_sucursal() {
      return nombre_sucursal;
   }

   public void setNombre_sucursal(String nombre_sucursal) {
      this.nombre_sucursal = nombre_sucursal;
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
