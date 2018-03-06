/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa los participantes
 *
 * @author ZeusSIWEB
 */
public class CofideParticipanteVta extends TableMaster {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CofideParticipanteVta() {
        super("cofide_participantes_vta", "CP_ID", "", "");
        this.Fields.put("CP_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CP_ID_CURSO", 0);
        this.Fields.put("CP_NOMBRE", "");
        this.Fields.put("CP_APPAT", "");
        this.Fields.put("CP_APMAT", "");
        this.Fields.put("CP_FAC_ID", 0);
        this.Fields.put("CP_TKT_ID", 0);
        this.Fields.put("CP_TITULO", "");
        this.Fields.put("CCO_NOSOCIO", 0);
        this.Fields.put("CP_ASCOC", "");
        this.Fields.put("CP_CORREO", "");
        this.Fields.put("CP_COMENT", "");
        this.Fields.put("CP_ASISTENCIA", 0);
        this.Fields.put("CP_REQFAC", 0);
        this.Fields.put("CP_PAGO", 0);
        this.Fields.put("CP_OBSERVACIONES", "");
        this.Fields.put("CP_USUARIO_ALTA", 0);
        this.Fields.put("CP_MATERIAL_IMPRESO", 0);
        this.Fields.put("CP_TIPO_CURSO", 0);
        this.Fields.put("CP_FECHA_PAGO", "");
        this.Fields.put("CP_DIGITO", "");
        this.Fields.put("CP_NOM_COMPROBANTE", "");
        this.Fields.put("CP_ESTATUS", 1);
        this.Fields.put("CONTACTO_ID", 0);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>

}
