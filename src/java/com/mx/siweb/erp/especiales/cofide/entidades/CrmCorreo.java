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
public class CrmCorreo extends TableMaster {

    public CrmCorreo() {
        super("crm_correos", "CRC_ID", "", "");
        this.Fields.put("CRC_ID", new Integer(0));
        this.Fields.put("CRC_ID_PLANTILLA", new Integer(0));
    }
}
