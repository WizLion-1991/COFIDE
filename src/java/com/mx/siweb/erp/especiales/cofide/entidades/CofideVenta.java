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
public class CofideVenta extends TableMaster {

    public CofideVenta() {
        super("cofide_venta", "CV_ID", "", "");
        this.Fields.put("CV_ID", 0);
        this.Fields.put("CV_ID_M", 0);
        this.Fields.put("CV_CURSO", "");
        this.Fields.put("CV_ID_CURSO", 0);
        this.Fields.put("CV_PRECIO", 0.0);
        this.Fields.put("CV_DESC", 0.0);
        this.Fields.put("CV_DESC_POR", 0);
        this.Fields.put("CV_TOTAL", 0.0);
        this.Fields.put("CV_IVA", 0.0);
        this.Fields.put("CV_TIPO_CURSO", 0);
        this.Fields.put("CV_LUGARES", 0);
        this.Fields.put("CV_FECHA_INI", "");
    }
}
