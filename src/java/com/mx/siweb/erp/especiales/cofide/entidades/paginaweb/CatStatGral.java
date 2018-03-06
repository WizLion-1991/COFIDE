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
public class CatStatGral extends TableMaster {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    CatStatGral() {
        super("cat_status_gral", "id_status", "", "");
        this.Fields.put("id_status", 0);
        this.Fields.put("status", "");
    }

// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
