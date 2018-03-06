/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful.entity;

import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author siweb
 */
@XmlRootElement
public class CategoriasDeta {
    
    private int id;
    private String descripcion;
   
    
    public String getDescripcion() {
      return descripcion;
    }

    public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
    }

    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    
}

