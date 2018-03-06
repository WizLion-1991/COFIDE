/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades.paginaweb;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author juliochavez
 */
public class CatModalidad extends TableMaster {
// <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    CatModalidad() {
        super("cat_modalidad", "", "", "");
        this.Fields.put("id_mod", 0);
        this.Fields.put("modalidad", "");
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
