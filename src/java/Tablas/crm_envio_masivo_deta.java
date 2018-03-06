/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author juliocesar
 */
public class crm_envio_masivo_deta extends TableMaster {

    public crm_envio_masivo_deta() {
        super("crm_envio_masivo_deta", "CAMPOLLAVE", "", "");
        this.Fields.put("CRMD_ID", 0);
        this.Fields.put("CRM_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CRMD_EMAIL", "");
        this.Fields.put("CRMD_ESTATUS_ENVIO", 0);
        this.Fields.put("CRMD_ESTATUS", "");
    }
}
