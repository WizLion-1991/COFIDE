/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author juliochavez
 */
public class VtaRazonsocialTMP extends TableMaster {

    public VtaRazonsocialTMP() {
        super("cofide_razonsocial_tmp", "CR_ID", "", "");
        this.Fields.put("CR_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CR_RAZONSOCIAL", "");
    }
}
