/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful.entity;

/**
 *
 * @author siweb
 */
public class PeriodoDeta {
    
    private int idPeriodo;
    private String nombre;
    private String fecInicial;
    private String fecFinal;
    private String abrv;
 
    public int getIdPeriodo(){
        return idPeriodo; 
    }
     
    public void setIdPeriodo(int idPeriodo){
        this.idPeriodo = idPeriodo;
    }
    
    public String getNombre(){
        return nombre; 
    }
     
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getFecInicial(){
        return fecInicial; 
    }
     
    public void setFecInicial(String fecInicial){
        this.fecInicial = fecInicial;
    }
    
    public String getFecFinal(){
        return fecFinal; 
    }
     
    public void setFecFinal( String fecFinal){
        this.fecFinal = fecFinal;
    }
    
    public String getAbrv(){
        return abrv; 
    }
     
    public void setAbrv(String abrv){
        this.abrv = abrv;
    }
    
    
}
