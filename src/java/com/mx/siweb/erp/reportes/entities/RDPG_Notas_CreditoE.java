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
public class RDPG_Notas_CreditoE {

    private int id;
    private String folio;
    private String fecha;
    private double importe;
    private String moneda;
    private String polizaGenerada;
    private String nombre;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
