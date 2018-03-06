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
public class CatSede extends TableMaster {
// <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    public CatSede() {
        super("cat_sedes", "", "", "");
        this.Fields.put("id_sede", 0);
        this.Fields.put("nombre", "");
        this.Fields.put("tel", "");
        this.Fields.put("calle_num", "");
        this.Fields.put("col", "");
        this.Fields.put("id_mun_del", 0);
        this.Fields.put("id_estado", 0);
        this.Fields.put("cp", 0);
        this.Fields.put("pais", "");
        this.Fields.put("id_status", 0);
        this.Fields.put("url_map", "");
        this.Fields.put("alias", "");
        this.bolGetAutonumeric = true;
    }
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
