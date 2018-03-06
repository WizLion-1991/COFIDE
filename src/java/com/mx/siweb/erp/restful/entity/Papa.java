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
public class Papa {

    private int idPapa;
    private String nombrePapa;
    private String codigo;
    private List<Hijo> hijosItem;

    public int getIdPapa(){
        return idPapa;
    }
    
    public void setIdPapa(int idPapa){
        this.idPapa = idPapa;
    }
    
    public void setNombrePapa(String codigo) {
        this.nombrePapa = codigo;
    }

    public String getNombrePapa() {
        return nombrePapa;
    }

    public List<Hijo> getHijoItem() {
        return hijosItem;
    }

    public void setHijoItem(List<Hijo> hijosItem) {
        this.hijosItem = hijosItem;
    }

    public Papa() {
        hijosItem = new ArrayList<Hijo>();
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

}
