/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.restful.entidades;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean que representa los participantes de cada curso
 *
 * @author juliochavez
 */
@XmlRootElement
public class Participantes {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private int intIdCurso;
    private String strNombre;
    private String strApPaterno;
    private String strApMaterno;
    private String strTitulo;
    private String strNumSocio;
    private String strAsociacion;
    private String strCorreo;
    private int intCT_ID;
    private int intReqMaterial;
    private int intIdFac;
    private int intIdTkt;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public int getIntIdFac() {
        return intIdFac;
    }

    public void setIntIdFac(int intIdFac) {
        this.intIdFac = intIdFac;
    }

    public int getIntIdTkt() {
        return intIdTkt;
    }

    public void setIntIdTkt(int intIdTkt) {
        this.intIdTkt = intIdTkt;
    }

    public int getIntIdCurso() {
        return intIdCurso;
    }

    public void setIntIdCurso(int intIdCurso) {
        this.intIdCurso = intIdCurso;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrApPaterno() {
        return strApPaterno;
    }

    public void setStrApPaterno(String strApPaterno) {
        this.strApPaterno = strApPaterno;
    }

    public String getStrApMaterno() {
        return strApMaterno;
    }

    public void setStrApMaterno(String strApMaterno) {
        this.strApMaterno = strApMaterno;
    }

    public String getStrTitulo() {
        return strTitulo;
    }

    public void setStrTitulo(String strTitulo) {
        this.strTitulo = strTitulo;
    }

    public String getStrNumSocio() {
        return strNumSocio;
    }

    public void setStrNumSocio(String strNumSocio) {
        this.strNumSocio = strNumSocio;
    }

    public String getStrAsociacion() {
        return strAsociacion;
    }

    public void setStrAsociacion(String strAsociacion) {
        this.strAsociacion = strAsociacion;
    }

    public String getStrCorreo() {
        return strCorreo;
    }

    public void setStrCorreo(String strCorreo) {
        this.strCorreo = strCorreo;
    }

    public int getIntCT_ID() {
        return intCT_ID;
    }

    public void setIntCT_ID(int intCT_ID) {
        this.intCT_ID = intCT_ID;
    }

    public int getIntReqMaterial() {
        return intReqMaterial;
    }

    public void setIntReqMaterial(int intReqMaterial) {
        this.intReqMaterial = intReqMaterial;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>

}
