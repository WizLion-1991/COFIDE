/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class CofideIncidenciaEnt extends TableMaster {

    public CofideIncidenciaEnt() {
        super("cfd_incidencia_incidencia", "CII_ID", "", "");
        this.Fields.put("CII_ID", 0);
        this.Fields.put("CCI_FECHA_ALTA", "");
        this.Fields.put("CCI_HORA_ALTA", "");
        this.Fields.put("CCI_CLIENTE", "");
        this.Fields.put("CCI_CORREO", "");
        this.Fields.put("CCI_CURSO_ID", 0);
        this.Fields.put("CCI_TIPO_PROBLEMA", 0);
        this.Fields.put("CCI_MODULO_CRM", "");
        this.Fields.put("CCI_ORIGEN_PROBLEMA", "");
        this.Fields.put("CCI_ESTATUS", 0);
        this.Fields.put("CCI_COMENTARIO", "");
        this.Fields.put("CCI_OBSERVACION", "");
        this.Fields.put("CCI_FECHA_TERMINO", "");
        this.Fields.put("CCI_HORA_TERMINO", "");
        this.Fields.put("CCI_TOTAL_TIEMPO", "");
        this.Fields.put("CCI_US_ALTA", 0);
    }
}
