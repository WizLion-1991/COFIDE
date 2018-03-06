/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean que representa el detalle de los pedidos
 *
 * @author ZeusGalindo
 */
@XmlRootElement
public class DirEntrega {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String calle_nw;
    private String colonia_nw;
    private String municipio_nw;
    private String estado_nw;
    private String cp_fact_nw;
    private String numero_nw;
    private String numeroInt_nw;
    private String nombre_nw;
    private String email_nw;
    private String strpais;
    private String strDescripcion;
    private String idDireccion;
    private String strTelefono1;

   
    public void setTelefono1(String strTelefono1){
    	this.strTelefono1 = strTelefono1;
    }
    
    public String getTelefono1(){
    	return strTelefono1;
    }
    
    public void setId(String id){
    	this.idDireccion = id;
    }
    
    public String getID(){
    	return idDireccion;
    }

    public void setPais(String strpais){
    	this.strpais = strpais;
    }
    
    public String getPais(){
    	return strpais;
    }
    
    public void setDescripcion(String strDescripcion){
    	this.strDescripcion = strDescripcion;
    }
    
    public String getDescripcion(){
    	return strDescripcion;
    }
    
    public String getEmail_nw() {
        return email_nw;
    }

    public void setEmail_nw(String email_nw) {
        this.email_nw = email_nw;
    }

    public String getNombre_nw() {
        return nombre_nw;
    }

    public void setNombre_nw(String nombre_nw) {
        this.nombre_nw = nombre_nw;
    }

    public String getNumero_nw() {
        return numero_nw;
    }

    public void setNumero_nw(String numero_nw) {
        this.numero_nw = numero_nw;
    }

    public String getCp_fact_nw() {
        return cp_fact_nw;
    }

    public void setCp_fact_nw(String cp_fact_nw) {
        this.cp_fact_nw = cp_fact_nw;
    }

    public String getEstado_nw() {
        return estado_nw;
    }

    public void setEstado_nw(String estado_nw) {
        this.estado_nw = estado_nw;
    }

    public String getMunicipio_nw() {
        return municipio_nw;
    }

    public void setMunicipio_nw(String municipio_nw) {
        this.municipio_nw = municipio_nw;
    }

    public String getColonia_nw() {
        return colonia_nw;
    }

    public void setColonia_nw(String colonia_nw) {
        this.colonia_nw = colonia_nw;
    }

    public String getCalle_nw() {
        return calle_nw;
    }

    public void setCalle_nw(String calle_nw) {
        this.calle_nw = calle_nw;
    }

    public String getNumeroInt_nw() {
        return numeroInt_nw;
    }

    public void setNumeroInt_nw(String numeroInt_nw) {
        this.numeroInt_nw = numeroInt_nw;
    }

}
