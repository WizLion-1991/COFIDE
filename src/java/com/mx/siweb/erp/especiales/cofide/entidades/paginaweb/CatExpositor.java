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
public class CatExpositor extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CatExpositor() {
        super("cat_expositores", "", "", "");
        this.Fields.put("id_expositor", 0);
        this.Fields.put("titulo", "");
        this.Fields.put("nombre", "");
        this.Fields.put("apellidos", "");
        this.Fields.put("cv", "");
        this.Fields.put("id_status", 0);
        this.Fields.put("foto", "");
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
