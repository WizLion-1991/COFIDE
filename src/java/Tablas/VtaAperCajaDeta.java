/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author Fernando
 */
public class VtaAperCajaDeta extends TableMaster {

    public VtaAperCajaDeta() {
        super("vta_aper_caja_deta", "APCD_ID", "", "");
        this.Fields.put("APCD_ID", 0);
        this.Fields.put("APC_ID", 0);
        this.Fields.put("APCD_VALOR", 0.0);
        this.Fields.put("APCD_PESOS", 0.0);
        this.Fields.put("APCD_DOLARES", 0.0);
    }
}