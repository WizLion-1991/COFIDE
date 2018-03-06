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
public class Hijo {
    
    private int idHijo;
    private int idPapa;
    private String nombre;
    private String telefono1;
    private String telefono2;
    private String email1;
    private int armadodeep;
    private int armadoini;
    private int armadofin;
    private String pPuntos;
    private String pNegocio;
    private String gPuntos;
    private String gNegocio;
    private String comision;
    private String nivelRed;
    
    public int getIdHijo(){
        return idHijo;
    }
    
    public void setIdHijo(int idHijo){
        this.idHijo = idHijo;
    }
    
    public int getIdPapa(){
        return idPapa;
    }
    
    public void setIdPapa(int idPapa){
        this.idPapa = idPapa;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getTelefono1(){
        return telefono1;
    }
    
    public void setTelefono1(String telefono1){
        this.telefono1 = telefono1;
    }
    
    public String getTelefono2(){
        return telefono2;
    }
    
    public void setTelefono2(String telefono2){
        this.telefono2 = telefono2;
    }
    
    public String getEmail(){
        return email1;
    }
    
    public void setEmail(String email1){
        this.email1 = email1;
    }
    
    public int getArmadoDeep(){
        return armadodeep;
    }
    
    public void setArmadoDeep(int armadodeep){
        this.armadodeep = armadodeep;
    }
    
    public int getArmadoIni(){
        return armadoini;
    }
    
    public void setArmadoIni(int armadoini){
        this.armadoini = armadoini;
    }
    
    public int getArmadoFin(){
        return armadofin;
    }
    
    public void setArmadoFin(int armadofin){
        this.armadofin = armadofin;
    }
    
    public String getPpuntos(){
        return pPuntos;
    }
    
    public void setpPuntos(String pPuntos){
        this.pPuntos=pPuntos;
    }
    
    public String getPnegocio(){
        return pNegocio;
    }
    
    public void setPnegocio(String pNegocio){
        this.pNegocio=pNegocio;
    }
    
    public String getGpuntos(){
        return gPuntos;
    }
    
    public void setGpuntos(String gPuntos){
        this.gPuntos=gPuntos;
    }
    
    public String getGnegocio(){
        return gNegocio;
    }
    
    public void setGnegocio(String gNegocio){
        this.gNegocio=gNegocio;
    }
    
    public String getComision(){
        return comision;
    }
    
    public void setComision(String comision){
        this.comision=comision;
    }
    
    public String getNivelRed(){
        return nivelRed;
    }
    
    public void setNivelRed(String nivelRed){
        this.nivelRed=nivelRed;
    }
}
