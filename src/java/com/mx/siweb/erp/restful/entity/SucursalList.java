/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Es una lista de sucursales
 *
 * @author ZeusSIWEB
 */
public class SucursalList {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private List<Sucursal> sucursalItem;

   public List<Sucursal> getSucursalItem() {
      return sucursalItem;
   }

   public void setSucursalItem(List<Sucursal> sucursalItem) {
      this.sucursalItem = sucursalItem;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public SucursalList() {
      sucursalItem = new ArrayList<Sucursal>();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
