/*
* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.reportes.entities;

import com.mx.siweb.erp.reportes.*;
import com.mx.siweb.erp.reportes.entities.*;

/**
 *
 * @author siweb
 */
public class RDPG_BancosE {
    
    private int id;
    private String concepto;
    private String fecha;
    private double deposito;
    private double retiro;
    private String moneda;
    private String polizaGenerada;
    private String banco;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getPolizaGenerada() {
        return polizaGenerada;
    }

    public void setPolizaGenerada(String polizaGenerada) {
        this.polizaGenerada = polizaGenerada;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getDeposito() {
        return deposito;
    }

    public void setDeposito(double deposito) {
        this.deposito = deposito;
    }

    public double getRetiro() {
        return retiro;
    }

    public void setRetiro(double retiro) {
        this.retiro = retiro;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
    
    
}
