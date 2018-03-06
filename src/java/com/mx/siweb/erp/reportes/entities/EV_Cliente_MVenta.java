/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siwebmx5
 */
public class EV_Cliente_MVenta {
    private int NUM;
    private String NOMBRE;
    private Double TSUBTOTAL;
    private Double TSUBTOTAL_PORC;

    public int getNUM() {
        return NUM;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public Double getTSUBTOTAL() {
        return TSUBTOTAL;
    }

    public void setTSUBTOTAL(Double TSUBTOTAL) {
        this.TSUBTOTAL = TSUBTOTAL;
    }

    public Double getTSUBTOTAL_PORC() {
        return TSUBTOTAL_PORC;
    }

    public void setTSUBTOTAL_PORC(Double TSUBTOTAL_PORC) {
        this.TSUBTOTAL_PORC = TSUBTOTAL_PORC;
    }
    
    
}
