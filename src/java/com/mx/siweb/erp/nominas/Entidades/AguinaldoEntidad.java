/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas.Entidades;

/**
 *
 * @author siweb
 */
public class AguinaldoEntidad {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private int intEmpNum;
    private String strEmpNombre;
    private String strFecIngreso;
    private String strPeriodicidadPago;
    private int intDiasTrab;
    private double dblSueldoDia;
    private double dblAguinaldo;
    private double dblImpuesto;
    private double dblImporteNeto;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">

    public AguinaldoEntidad() {
    }
    // </editor-fold>
    
    public void setStrFecIngreso(String strFecIngreso) {    
        this.strFecIngreso = strFecIngreso;
    }

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void setStrPeriodicidadPago(String strPeriodicidadPago) {    
        this.strPeriodicidadPago = strPeriodicidadPago;
    }

    public void setIntEmpNum(int intEmpNum) {
        this.intEmpNum = intEmpNum;
    }

    public void setStrEmpNombre(String strEmpNombre) {
        this.strEmpNombre = strEmpNombre;
    }

    public void setIntDiasTrab(int intDiasTrab) {
        this.intDiasTrab = intDiasTrab;
    }

    public void setDblSueldoDia(double dblSueldoDia) {
        this.dblSueldoDia = dblSueldoDia;
    }

    public void setDblAguinaldo(double dblAguinaldo) {
        this.dblAguinaldo = dblAguinaldo;
    }

    public void setDblImpuesto(double dblImpuesto) {
        this.dblImpuesto = dblImpuesto;
    }

    public void setDblImporteNeto(double dblImporteNeto) {
        this.dblImporteNeto = dblImporteNeto;
    }

    public int getIntEmpNum() {
        return intEmpNum;
    }

    public String getStrFecIngreso() {
        return strFecIngreso;
    }

    public String getStrPeriodicidadPago() {
        return strPeriodicidadPago;
    }

    public String getStrEmpNombre() {
        return strEmpNombre;
    }

    public int getIntDiasTrab() {
        return intDiasTrab;
    }

    public double getDblSueldoDia() {
        return dblSueldoDia;
    }

    public double getDblAguinaldo() {
        return dblAguinaldo;
    }

    public double getDblImpuesto() {
        return dblImpuesto;
    }

    public double getDblImporteNeto() {
        return dblImporteNeto;
    }
    // </editor-fold>
    
}
