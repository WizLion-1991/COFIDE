/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author casajosefa
 */
public class VtaComboDeta extends TableMaster {

    public VtaComboDeta() {
        super("vta_combo_deta", "CMBD_ID", "", "");
        this.Fields.put("CMBD_ID", 0);
        this.Fields.put("CMB_ID", 0);
        this.Fields.put("CMBD_PR_ID", 0);
        this.Fields.put("CMBD_PR_CODIGO", "");
        this.Fields.put("CMBD_PR_DESCRIPCION", "");
        this.Fields.put("CMBD_PR_CANTIDAD", 0.0);
        this.Fields.put("CMBD_PR_NUM", 0);
        this.Fields.put("CMBD_PR_PRECIO", 0.0);
        this.Fields.put("CMBD_CONTADOR", 0.0);
    }
}