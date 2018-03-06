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
public class Estados {
    
    private String codigo;
    private List<EstadosDeta> estadosItem;
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public List<EstadosDeta> getEstadosItem() {
      return estadosItem;
   }

   public void setEstadosItem(List<EstadosDeta> estadosItem) {
      this.estadosItem = estadosItem;
   }

   public Estados() {
      estadosItem = new ArrayList<EstadosDeta>();
   }
    
}
