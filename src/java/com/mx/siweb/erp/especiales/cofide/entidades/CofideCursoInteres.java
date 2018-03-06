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
public class CofideCursoInteres extends TableMaster {

    public CofideCursoInteres() {
        super("cofide_cursos_interes", "CCI_ID", "", "");
        this.Fields.put("CCI_ID", new Integer(0));
        this.Fields.put("CCI_CT_ID", new Integer(0));
        this.Fields.put("CCI_FECHA", "");
        this.Fields.put("CCI_US_ALTA", new Integer(0));
        this.Fields.put("CCI_DESCRIPCION", "");
    }
}
