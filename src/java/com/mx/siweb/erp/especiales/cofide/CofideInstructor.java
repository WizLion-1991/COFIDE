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
public class CofideInstructor extends TableMaster {

    public CofideInstructor() {
        super("cofide_instructor", "CI_INSTRUCTOR_ID", "", "");
        this.Fields.put("CI_INSTRUCTOR_ID", 0);
        this.Fields.put("CI_INSTRUCTOR", "");
        this.Fields.put("CI_RAZONSOCIAL", "");
        this.Fields.put("CI_COPROBANTE", "");
        this.Fields.put("CI_COSTO_HR1", 0.0);
        this.Fields.put("CI_COSTO_HR2", 0.0);
        this.Fields.put("CI_COSTO_HR3", 0.0);
        this.Fields.put("CI_COSTO_HR4", 0.0);
        this.Fields.put("CI_COSTO_QRO_PBLA", 0.0);
        this.Fields.put("CI_OBSERVACIONES", "");
        this.Fields.put("CI_FOTO", "");
        this.Fields.put("CI_TITULO", 0);
        this.Fields.put("CI_CV", "");
        this.Fields.put("CI_ESTATUS", 0);
        this.Fields.put("CI_NOMBRE", "");
        this.Fields.put("CI_APELLIDO", "");
    }
}
