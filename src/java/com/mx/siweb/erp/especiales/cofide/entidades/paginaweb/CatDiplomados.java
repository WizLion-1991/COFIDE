/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades.paginaweb;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa los diplomados
 *
 * @author ZeusSIWEB
 */
public class CatDiplomados extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CatDiplomados() {
        super("cat_diplomados", "", "", "");
        this.Fields.put("id_diplomado", 0);
        this.Fields.put("nombre", "");
        this.Fields.put("fch_ini", "");
        this.Fields.put("id_status", 0);
        this.Fields.put("fch_alta", "");
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>

}
