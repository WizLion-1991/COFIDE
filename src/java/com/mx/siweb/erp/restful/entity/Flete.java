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
public class Flete {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">

      private List<FleteDeta> lstItems;

    public List<FleteDeta> getLstItems() {
        return lstItems;
    }

    public void setLstItems(List<FleteDeta> lstItems) {
        this.lstItems = lstItems;
    }
  
    

     // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
     public Flete() {
        lstItems = new ArrayList<FleteDeta>();
       
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
