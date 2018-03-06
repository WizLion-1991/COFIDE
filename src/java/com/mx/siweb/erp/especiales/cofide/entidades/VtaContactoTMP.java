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
public class VtaContactoTMP extends TableMaster {

    public VtaContactoTMP() {
        super("cofide_contactos_tmp", "CCO_ID", "", "");
        this.Fields.put("CCO_ID", 0);
        this.Fields.put("CCO_NOMBRE", "");
        this.Fields.put("CCO_APPATERNO", "");
        this.Fields.put("CCO_APMATERNO", "");
        this.Fields.put("CCO_TITULO", "");
        this.Fields.put("CCO_NOSOCIO", "");
        this.Fields.put("CCO_AREA", "");
        this.Fields.put("CCO_ASOCIACION", "");
        this.Fields.put("CCO_CORREO", "");
        this.Fields.put("CCO_TELEFONO", "");
        this.Fields.put("CCO_EXTENCION", "");
        this.Fields.put("CCO_ALTERNO", "");
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CT_MAILMES", "");
        this.Fields.put("CCO_CORREO2", "");
        this.Fields.put("CONTACTO_ID", 0);
    }
}
