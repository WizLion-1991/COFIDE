/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Es una lista de bodegas
 *
 * @author ZeusSIWEB
 */
public class BodegaList {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private List<Bodega> bodegaItem;

   public List<Bodega> getBodegaItem() {
      return bodegaItem;
   }

   public void setBodegaItem(List<Bodega> bodegaItem) {
      this.bodegaItem = bodegaItem;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public BodegaList() {
      bodegaItem = new ArrayList<Bodega>();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
