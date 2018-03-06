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
public class Sugerencias {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    
    private int ct_id;
    private String Codigo;
    private List<SugerenciasDeta> SugerenciasItem;
    
    public int getCt_id(){
        return ct_id;
    }
    
    public void setCt_id(int Ct_id){
        this.ct_id = Ct_id;
    }
    
    public String getCodigo() {
      return Codigo;
    }

    public void setCodigo(String Codigo) {
      this.Codigo = Codigo;
    }
    
    public List<SugerenciasDeta> getSugerenciasItem(){
        return SugerenciasItem;
    }
    
    public void setVentaItem(List<SugerenciasDeta> SugerenciasItem){
        this.SugerenciasItem = SugerenciasItem;
    }
    
    public Sugerencias(){
        SugerenciasItem = new ArrayList<SugerenciasDeta>();
    }

     // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}