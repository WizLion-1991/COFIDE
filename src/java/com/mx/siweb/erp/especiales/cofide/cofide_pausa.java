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
public class cofide_pausa extends TableMaster {

    public cofide_pausa() {
        super("cofide_pausa", "CP_ID", "", "");
        this.Fields.put("CP_ID", 0);
        this.Fields.put("CP_USUARIO", 0);
        this.Fields.put("CP_HORA_INICIO", "");
        this.Fields.put("CP_HORA_FIN", "");
        this.Fields.put("CP_FECHA", "");
        this.Fields.put("CP_TIPO", "");
    }
}
