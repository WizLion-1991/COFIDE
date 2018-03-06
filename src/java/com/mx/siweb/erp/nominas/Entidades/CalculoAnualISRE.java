/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas.Entidades;

/**
 *
 * @author CasaJosefa
 */
public class CalculoAnualISRE {

    double totalRemuneraciones = 0.0;
    double totalPercepcionesExentas = 0.0;
    double totalPercepcionesGravadas = 0.0;
    double impuestoCalculado = 0.0;
    double impuestoRetenidoAnio = 0.0;
    double diferencia = 0.0;
    int idEmpleado = 0;
    String nombreEmpleado = "";

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public double getTotalPercepcionesGravadas() {
        return totalPercepcionesGravadas;
    }

    public void setTotalPercepcionesGravadas(double totalPercepcionesGravadas) {
        this.totalPercepcionesGravadas = totalPercepcionesGravadas;
    }

    public double getTotalRemuneraciones() {
        return totalRemuneraciones;
    }

    public void setTotalRemuneraciones(double totalRemuneraciones) {
        this.totalRemuneraciones = totalRemuneraciones;
    }

    public double getTotalPercepcionesExentas() {
        return totalPercepcionesExentas;
    }

    public void setTotalPercepcionesExentas(double totalPercepcionesExentas) {
        this.totalPercepcionesExentas = totalPercepcionesExentas;
    }

    public double getImpuestoCalculado() {
        return impuestoCalculado;
    }

    public void setImpuestoCalculado(double impuestoCalculado) {
        this.impuestoCalculado = impuestoCalculado;
    }

    public double getImpuestoRetenidoAnio() {
        return impuestoRetenidoAnio;
    }

    public void setImpuestoRetenidoAnio(double impuestoRetenidoAnio) {
        this.impuestoRetenidoAnio = impuestoRetenidoAnio;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

}
