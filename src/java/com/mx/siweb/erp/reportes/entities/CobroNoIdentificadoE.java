/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class CobroNoIdentificadoE {
    
    
    private String fecha;
    private String concepto;
    private String beneficiario;
    private String banco;
    private double deposito;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String formaDePago) {
        this.concepto = formaDePago;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String documento) {
        this.beneficiario = documento;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public double getDeposito() {
        return deposito;
    }

    public void setDeposito(double monto) {
        this.deposito = monto;
    }
}
