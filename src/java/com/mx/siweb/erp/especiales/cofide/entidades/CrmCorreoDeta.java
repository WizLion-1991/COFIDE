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
public class CrmCorreoDeta extends TableMaster {

    public CrmCorreoDeta() {
        super("crm_correos_deta", "CRCD_ID", "", "");
        this.Fields.put("CRCD_ID", new Integer(0));
        this.Fields.put("CRC_ID", new Integer(0));
        this.Fields.put("CRCD_ID_CURSO", new Integer(0));
        this.Fields.put("CRCD_TIPO", new Integer(0));
        this.Fields.put("CRCD_CONFIRMA", new Integer(0));
        this.Fields.put("CRCD_ESTATUS", new Integer(0));
        this.Fields.put("CRCD_ASUNTO", "");
    }
}
