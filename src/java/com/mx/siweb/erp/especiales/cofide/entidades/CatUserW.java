/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author casajosefa
 */
public class CatUserW extends TableMaster {
    public CatUserW() {
        super("cat_usr_w", "id_usr_w", "", "");
        this.Fields.put("id_usr_w", 0);
        this.Fields.put("id_usr_crm", 0);
        this.Fields.put("usr", "");
        this.Fields.put("psw", "");
        this.Fields.put("nombre", "");
        this.Fields.put("apellidos", "");
        this.Fields.put("nombre_doc", "");
        this.Fields.put("tel", "");
        this.Fields.put("id_status", 0);
        this.Fields.put("fch_alta","");
        this.Fields.put("fch_baja","");
        this.setBolGetAutonumeric(true);
    }
}
