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
public class VtaCteTMP extends TableMaster {

    public VtaCteTMP() {
        super("vta_cliente_tmp", "CT_ID_T", "", "");
        this.Fields.put("CT_ID_T", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CT_RAZONSOCIAL", "");
        this.Fields.put("CT_RFC", "");
        this.Fields.put("CT_CALLE", "");
        this.Fields.put("CT_COLONIA", "");
        this.Fields.put("CT_LOCALIDAD", "");
        this.Fields.put("CT_MUNICIPIO", "");
        this.Fields.put("CT_ESTADO", "");
        this.Fields.put("CT_CP", "");
        this.Fields.put("CT_TELEFONO1", "");
        this.Fields.put("CT_TELEFONO2", "");
        this.Fields.put("CT_CONTACTO1", "");
        this.Fields.put("CT_CONTACTO2", "");
        this.Fields.put("CT_EMAIL1", "");
        this.Fields.put("CT_EMAIL2", "");
        this.Fields.put("CT_NUMERO", "");
        this.Fields.put("CT_NUMINT", "");
        this.Fields.put("CT_ES_PROSPECTO", 0);
        this.Fields.put("COFIDE_BASE", 0);
        this.Fields.put("CT_SEDE", "");
        this.Fields.put("CT_CLAVE_DDBB", "");
        this.Fields.put("CT_GIRO", "");
        this.Fields.put("CT_AREA", "");
        this.Fields.put("CT_MAILMES", 0);
        this.Fields.put("CT_CONMUTADOR", "");
        this.Fields.put("CT_FECHA_GUARDADO", "");
        this.Fields.put("CT_HORA_GUARDADO", "");
        this.Fields.put("CT_CLAVE_DDBB", "");
        this.Fields.put("CT_COMENTARIO", "");
        this.Fields.put("CT_FECHA", "");
        this.Fields.put("CT_HORA", "");
        this.Fields.put("CT_NOMBRE_INVITO", "");
    }
}
