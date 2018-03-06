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
public class crm_envio_masivo extends TableMaster {

    public crm_envio_masivo() {
        super("crm_envio_masivo", "CRM_ID", "", "");
        this.Fields.put("CRM_ID", 0);
        this.Fields.put("CRM_FECHA", "");
        this.Fields.put("CRM_HORA", "");
        this.Fields.put("CRM_USUARIO", "");
        this.Fields.put("CRM_FECHAFIN", "");
        this.Fields.put("CRM_HORAFIN", "");
        this.Fields.put("CRM_TEMPLATE", "");
        this.Fields.put("CRM_ASUNTO", "");
        this.Fields.put("CRM_CURSO", "");
        this.Fields.put("CRM_MENSUAL", 0);
        this.Fields.put("CRM_GIRO", "");
        this.Fields.put("CRM_SEDE", "");
        this.Fields.put("CRM_AREA", "");
        this.Fields.put("CRM_TIPO", 0);
    }
}
