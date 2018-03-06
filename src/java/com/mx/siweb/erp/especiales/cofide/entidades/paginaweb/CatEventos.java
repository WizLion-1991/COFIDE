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
public class CatEventos extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CatEventos() {
        super("cat_eventos", "", "", "");
        this.Fields.put("id_evento", new Integer(0));
        this.Fields.put("id_tipo_evento", new Integer(0));
        this.Fields.put("fch_ini", "");
        this.Fields.put("id_status", new Integer(0));
        this.Fields.put("prioridad", new Integer(0));
        this.Fields.put("agotado", new Integer(0));
        this.Fields.put("id_mod", new Integer(0));
        this.Fields.put("id_area", new Integer(0));
        this.Fields.put("retransmision", new Integer(0));
        this.Fields.put("retrans_status_w", new Integer(0));
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>

}
