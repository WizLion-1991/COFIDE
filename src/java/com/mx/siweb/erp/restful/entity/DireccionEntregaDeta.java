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
public class DireccionEntregaDeta {
    
    private int direccion_Id;
    private String AcuDireccion;
    private String calle;
    private String colonia;
    private String localidad;
    private String municipio;
    private String estado;
    private String cp;
    private String numeroExterior;
    private String numeroInterior;
    private String telefono1;
    private String google;
    private String EMP_ID;
    private String descripcion;
    private String CT_ID;
    private String nombre;
    private String email1;
    private String pais;
    private String codigo;
    
    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }
    
    public int getDireccionId(){
        return direccion_Id;
    }
    
    public void setDireccionId(int direccion_Id){
        this.direccion_Id = direccion_Id;
    }
    
    public String getAcuDir(){
        return AcuDireccion;
    }
    
    public void setAcuDir(String AcuDireccion){
        this.AcuDireccion = AcuDireccion;
    }
    
    public String getCalle(){
        return calle;
    }
    
    public void setCalle(String calle){
        this.calle = calle;
    }
    
    public String getColonia(){
        return colonia;
    }
    
    public void setColonia(String colonia){
        this.colonia = colonia;
    }
    
    public String getLocalidad(){
        return localidad;
    }
    
    public void setLocalidad(String localidad){
        this.localidad = localidad;
    }
    
    public String getMunicipio(){
        return municipio;
    }
    
    public void setMunicipio(String municipio){
        this.municipio = municipio;
    }
    
    public String getEstado(){
        return estado;
    }
    
    public void setEstado(String estado){
        this.estado = estado;
    }
    
    public String getCP(){
        return cp;
    }
    
    public void setCP(String cp){
        this.cp = cp;
    }
    
    public String getNumeroExterior(){
        return numeroExterior;
    }
    
    public void setNumeroExterior(String numeroExterior){
        this.numeroExterior = numeroExterior;
    }
    
    public String getNumeroInterior(){
        return numeroInterior;
    }
    
    public void setNumeroInterior(String numeroInterior){
        this.numeroInterior = numeroInterior;
    }
    
    public String getTelefono1(){
        return telefono1;
    }
    
    public void setTelefono1(String telefono1){
        this.telefono1 = telefono1;
    }
    
    public String getGoogle(){
        return google;
    }
    
    public void setGoogle(String google){
        this.google = google;
    }
    
    public String getEmp_Id(){
        return EMP_ID;
    }
    
    public void setEmp_Id(String EMP_ID){
        this.EMP_ID = EMP_ID;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    
    public String getCt_Id(){
        return CT_ID;
    }
    
    public void setCt_Id(String CT_ID){
        this.CT_ID = CT_ID;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getPais(){
        return pais;
    }
    
    public void setPais(String pais){
        this.pais = pais;
    }
    
    public String getEmail1(){
        return email1;
    }
    
    public void setEmail1(String email1){
        this.email1 = email1;
    }
}
