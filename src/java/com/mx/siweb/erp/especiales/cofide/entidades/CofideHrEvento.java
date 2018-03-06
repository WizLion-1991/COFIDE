/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author Administrador
 */
public class CofideHrEvento extends TableMaster {

    public CofideHrEvento() {
        super("horario_eventos", "CAMPOLLAVE", "", "");
        this.Fields.put("id_evento", new Integer(0));
        this.Fields.put("fch_ini", "");
        this.Fields.put("fch_fin", "");
    }

}
