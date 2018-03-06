/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades.paginaweb;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author ZeusSIWEB
 */
public class CatCursos extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CatCursos() {
        super("cat_cursos", "", "", "");
        this.Fields.put("id_curso", new Integer(0));
        this.Fields.put("nombre", "");
        this.Fields.put("fch_ini", "");
        this.Fields.put("alias_fch_ini", "");
        this.Fields.put("hra_ini", "");
        this.Fields.put("hra_fin", "");
        this.Fields.put("id_status", new Integer(0));
        this.Fields.put("fch_alta", "");
        this.Fields.put("nombre_retrans", "");
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>

}
