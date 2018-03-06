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
public class VtaCombo extends TableMaster {

    public VtaCombo() {
        super("vta_combo", "CMB_ID", "", "");
        this.Fields.put("CMB_ID", 0);
        this.Fields.put("CMB_CODIGO", "");
        this.Fields.put("CMB_DESCRIPCION", "");
        this.Fields.put("CMB_ESTATUS", 0);
        this.Fields.put("CMB_MON_FIJA", 0);
        this.Fields.put("CMB_PREC_TIENDA", 0.0);
        this.Fields.put("MON_ID_PREC_TIENDA", 0);
        this.Fields.put("CMB_PREC_MAYOREO", 0.0);
        this.Fields.put("MON_ID_PREC_MAYOREO", 0);
        this.Fields.put("CMB_PREC_DISTRIBUIDOR", 0.0);
        this.Fields.put("MON_ID_PREC_DISTRIBUIDOR", 0);
        this.Fields.put("EMP_ID", 0);
    }
}