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
public class Paises {
     // <editor-fold defaultstate="collapsed" desc="Propiedades">

    private String codigo;
    private List<PaisesDeta> paisesItem;
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public List<PaisesDeta> getPaisesItem() {
      return paisesItem;
   }

   public void setPaisesItem(List<PaisesDeta> paisesItem) {
      this.paisesItem = paisesItem;
   }

   public Paises() {
      paisesItem = new ArrayList<PaisesDeta>();
   }
// </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
