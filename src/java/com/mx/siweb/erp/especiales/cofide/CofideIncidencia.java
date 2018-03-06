/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import java.util.ArrayList;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class CofideIncidencia {

    String strIdUsuario = "";
    String strUsuario = "";
    String strNombre = "";
    String strPassword = "";
    String strPerfil = "";
    ArrayList<String> arrMenuBtn = new ArrayList<String>();

    public ArrayList<String> getArrMenuBtn() {
        return arrMenuBtn;
    }

    public void setArrMenuBtn(ArrayList<String> arrMenuBtn) {
        this.arrMenuBtn = arrMenuBtn;
    }

    public String getStrIdUsuario() {
        return strIdUsuario;
    }

    public void setStrIdUsuario(String strIdUsuario) {
        this.strIdUsuario = strIdUsuario;
    }

    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public String getStrNombre() {
        return strNombre;
    }

    public void setStrNombre(String strNombre) {
        this.strNombre = strNombre;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public String getStrPerfil() {
        return strPerfil;
    }

    public void setStrPerfil(String strPerfil) {
        this.strPerfil = strPerfil;
    }

}
