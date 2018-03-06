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
public class Categorias {
   
   
    private List<CategoriasDeta> lstItems;
    
  
    
    public List<CategoriasDeta> getLstItems(){
        return lstItems;
    }
    
    public void setLstItems(List<CategoriasDeta> lstItems){
        this.lstItems = lstItems;
    }
    
    public Categorias(){
        lstItems = new ArrayList<CategoriasDeta>();
    }

     // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}

