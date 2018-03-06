/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author siweb
 */
@XmlRootElement
public class ProductosEcomm {

   
    private List<ProductosEcommDeta> lstItems;

    public List<ProductosEcommDeta> getLstItems() {
        return lstItems;
    }

    public void setLstItems(List<ProductosEcommDeta> lstItems) {
        this.lstItems = lstItems;
    }

  

    public ProductosEcomm() {
        lstItems = new ArrayList<ProductosEcommDeta>();
    }
   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
    // </editor-fold>
}
