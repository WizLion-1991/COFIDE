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
public class DireccionEntrega {
    private String ct_id;
    private String codigo;
    private List<DireccionEntregaDeta> DireccionEntregaDetaItem;
    
    public String getCt_id(){
        return ct_id;
    }
    
    public void setCt_id(String ct_id){
        this.ct_id = ct_id;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public List<DireccionEntregaDeta> getDireccionEntregaItem(){
        return DireccionEntregaDetaItem;
    }
    
    public void setDireccionEntregaItem(List<DireccionEntregaDeta> DireccionEntregaDetaItem){
        this.DireccionEntregaDetaItem = DireccionEntregaDetaItem;
    }
    
    public DireccionEntrega(){
        DireccionEntregaDetaItem = new ArrayList<DireccionEntregaDeta>();
    }
       
}
