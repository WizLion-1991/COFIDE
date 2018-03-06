/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

/**
 * Representa una campania por aplicar
 *
 * @author ZeusSIWEB
 */
public class CofideCampanias {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private int intIdCampania;
    private int intIdArea;
    private int intIdGiro;
    private int intIdSede;
    private String strArea;
    private String strGiro;
    private String strSede;
    private int intProspecto;
    private int intExParticipante;
    private int intPuntosLealtad;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrGiro() {
        return strGiro;
    }

    public void setStrGiro(String strGiro) {
        this.strGiro = strGiro;
    }

    public String getStrSede() {
        return strSede;
    }

    public void setStrSede(String strSede) {
        this.strSede = strSede;
    }

    public int getIntIdCampania() {
        return intIdCampania;
    }

    public void setIntIdCampania(int intIdCampania) {
        this.intIdCampania = intIdCampania;
    }

    public int getIntIdArea() {
        return intIdArea;
    }

    public void setIntIdArea(int intIdArea) {
        this.intIdArea = intIdArea;
    }

    public int getIntIdGiro() {
        return intIdGiro;
    }

    public void setIntIdGiro(int intIdGiro) {
        this.intIdGiro = intIdGiro;
    }

    public int getIntIdSede() {
        return intIdSede;
    }

    public void setIntIdSede(int intIdSede) {
        this.intIdSede = intIdSede;
    }

    public int getIntProspecto() {
        return intProspecto;
    }

    public void setIntProspecto(int intProspecto) {
        this.intProspecto = intProspecto;
    }

    public int getIntExParticipante() {
        return intExParticipante;
    }

    public void setIntExParticipante(int intExParticipante) {
        this.intExParticipante = intExParticipante;
    }

    public int getIntPuntosLealtad() {
        return intPuntosLealtad;
    }

    public void setIntPuntosLealtad(int intPuntosLealtad) {
        this.intPuntosLealtad = intPuntosLealtad;
    }
    // </editor-fold>
}
