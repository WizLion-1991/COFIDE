/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author SIWEB
 */
public class vta_mov_prov_mas extends TableMaster {

    public vta_mov_prov_mas() {
        super("vta_mov_prov_mas", "MPM_ID", "", "");
        this.Fields.put("MPM_ID", new Integer(0));
        this.Fields.put("MPM_FECHA", "");
        this.Fields.put("MPM_HORA", "");
        this.Fields.put("MPM_IDUSER", new Integer(0));
        this.Fields.put("MPM_ANULADO", new Integer(0));
        this.Fields.put("MPM_IDUSERANUL", new Integer(0));
        this.Fields.put("MPM_TOTAL", new Double(0));
        this.Fields.put("MPM_TOTOPER", new Integer(0));
        this.Fields.put("MPM_FECHACREATE", "");
        this.Fields.put("MPM_FECHAANUL", "");
        this.Fields.put("MPM_HORAANUL", "");
    }
}
