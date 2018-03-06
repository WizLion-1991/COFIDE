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
public class IntCrmExpositor extends TableMaster {
// <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public IntCrmExpositor() {
        super("interface_expositores", "", "", "");
        this.Fields.put("id_evento", 0);
        this.Fields.put("id_tipo_evento", 0);
        this.Fields.put("id_expositor", 0);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
