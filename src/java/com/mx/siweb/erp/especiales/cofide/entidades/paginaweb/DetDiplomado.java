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
public class DetDiplomado extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public DetDiplomado() {
        super("det_diplomado", "", "", "");
        this.Fields.put("id_diplomado", 0);
        this.Fields.put("duracion", 0);
        this.Fields.put("precio", 0.0);
        this.Fields.put("p_online", 0.0);
        this.Fields.put("p_video", 0.0);
        this.Fields.put("codigo_pay", "");
        this.Fields.put("id_sucursal", 0);
        this.Fields.put("id_sede", 0);
        this.Fields.put("objetivo", "");
        this.Fields.put("dirigido", "");
        this.Fields.put("temario", "");
        this.Fields.put("id_paquete", 0);
        this.Fields.put("id_alimento", 0);
        this.Fields.put("hra_alimento", "");
        this.Fields.put("requisitos", "");
        this.Fields.put("incluye", "");
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>

}
