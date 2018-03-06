/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author siweb
 */
@XmlRootElement
public class TasaIva {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private int ivaId;
    private double ivaTasa;
    private String descripcionTasa;
   

    public int getIvaId() {
        return ivaId;
    }

    public void setIvaId(int ivaId) {
        this.ivaId = ivaId;
    }

    public double getIvaTasa() {
        return ivaTasa;
    }

    public void setIvaTasa(double ivaTasa) {
        this.ivaTasa = ivaTasa;
    }

    public String getDescripcionTasa() {
        return descripcionTasa;
    }

    public void setDescripcionTasa(String descripcionTasa) {
        this.descripcionTasa = descripcionTasa;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
