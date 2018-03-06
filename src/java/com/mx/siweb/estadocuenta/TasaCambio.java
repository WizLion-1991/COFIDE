/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.estadocuenta;

/**
 *Entidad que representa una tasa de cambio
 * @author siwebmx5
 */
public class TasaCambio {
    private Integer moneda1;
    private Integer moneda2;
    private String fecha;
    private Double valor;

    public TasaCambio()
    {
        this.moneda1 = 1;
        this.moneda2 = 2;
        this.fecha = "";
        this.valor = 1.0;
    }
    public TasaCambio(Integer moneda1, Integer moneda2, String fecha, Double valor) {
        this.moneda1 = moneda1;
        this.moneda2 = moneda2;
        this.fecha = fecha;
        this.valor = valor;
    }

    public Integer getMoneda1() {
        return moneda1;
    }
    public void setMoneda1(Integer moneda1) {
        this.moneda1 = moneda1;
    }
    public Integer getMoneda2() {
        return moneda2;
    }
    public void setMoneda2(Integer moneda2) {
        this.moneda2 = moneda2;
    }
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public Double getValor() {
        return valor;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public boolean equals(TasaCambio obj)
    {
        boolean regresa = false;
        if((this.moneda1 == obj.moneda1) &&(this.moneda2 == obj.moneda2) && (this.fecha.equals(obj.fecha)))
        {
            regresa = true;
        }
        return regresa;
    }
}
