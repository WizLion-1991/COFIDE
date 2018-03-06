/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el listado de etiquetas
 *
 * @author ZeusSIWEB
 */
public class EtiquetasList {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   private List<Etiquetas> etiquetasItem;


   public List<Etiquetas> getEtiquetasItem() {
      return etiquetasItem;
   }

   public void setEtiquetasItem(List<Etiquetas> etiquetasItem) {
      this.etiquetasItem = etiquetasItem;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public EtiquetasList() {
      etiquetasItem = new ArrayList<Etiquetas>();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
