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
public class EstadosDeta {
    
    private int id;
    private String estado;

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdEstado(int id) {
        this.id = id;
    }

    public int getIdEstado() {
        return id;
    }
    
}
