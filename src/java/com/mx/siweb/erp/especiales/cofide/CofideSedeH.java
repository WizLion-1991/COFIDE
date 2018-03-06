/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author juliochavez
 */
public class CofideSedeH extends TableMaster {

    public CofideSedeH() {
        super("cofide_sede_hotel", "CSH_ID", "", "");
        this.Fields.put("CSH_ID", 0);
        this.Fields.put("CSH_SEDE", "");
        this.Fields.put("CSH_ESTADOS", "");
        this.Fields.put("CSH_COLOR", "");
        this.Fields.put("CSH_COLOR_D", "");
        this.Fields.put("CSH_TELEFONO", "");
        this.Fields.put("CSH_CALLE", "");
        this.Fields.put("CSH_NUM_EXT", "");
        this.Fields.put("CSH_COLONIA", "");
        this.Fields.put("CSH_ESTADO", "");
        this.Fields.put("CSH_ID_ESTADO", 0);
        this.Fields.put("CSH_CP", "");
        this.Fields.put("CSH_URLMAP", "");
        this.Fields.put("CSH_NUM_INT", 0);
        this.Fields.put("CSH_ALIAS", "");
        this.Fields.put("CSH_ESTATUS", 0);
    }
}