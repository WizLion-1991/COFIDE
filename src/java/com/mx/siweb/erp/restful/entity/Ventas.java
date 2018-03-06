/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author siweb
 */
public class Ventas {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

   //private String Tipo_Doc;
   private List<VentasDeta> VentaItem;
   private String codigo;
   private int CT_ID;

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public int getCT_ID() {
      return CT_ID;
   }

   public void setCT_ID(int CT_ID) {
      this.CT_ID = CT_ID;
   }

   public List<VentasDeta> getVentaItem() {
      return VentaItem;
   }

   public void setVentaItem(List<VentasDeta> VentaItem) {
      this.VentaItem = VentaItem;
   }

   public Ventas() {
      VentaItem = new ArrayList<VentasDeta>();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}



