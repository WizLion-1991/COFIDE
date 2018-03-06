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
public class vta_mov_prov_deta extends TableMaster {

    public vta_mov_prov_deta() {
        super("vta_mov_prov_deta", "MPD_ID", "", "");
        this.Fields.put("MPD_ID", new Integer(0));
        this.Fields.put("PV_ID", new Integer(0));
        this.Fields.put("MPD_IMPORTE", new Double(0));
        this.Fields.put("MPD_FOLIO", "");
        this.Fields.put("MPD_FORMAPAGO", "");
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("MPD_IMPUESTO1", new Double(0));
        this.Fields.put("MPD_IMPUESTO2", new Double(0));
        this.Fields.put("MPD_IMPUESTO3", new Double(0));
        this.Fields.put("MPD_TASAIMPUESTO1", new Double(0));
        this.Fields.put("MPD_TASAIMPUESTO2", new Double(0));
        this.Fields.put("MPD_TASAIMPUESTO3", new Double(0));
        this.Fields.put("MPD_NOCHEQUE", "");
        this.Fields.put("MPD_BANCO", "");
        this.Fields.put("MPD_NOTARJETA", "");
        this.Fields.put("MPD_TIPOTARJETA", "");
        this.Fields.put("MP_ID", new Integer(0));
        this.Fields.put("MPD_MONEDA", new Integer(0));
        this.Fields.put("MPD_TASAPESO", new Double(0));
        this.Fields.put("MPD_CAMBIO", new Double(0));
        this.Fields.put("EMP_ID", new Integer(0));
        this.Fields.put("MPD_RFC_TERCEROS", "");
        this.Fields.put("MPD_BANCO_DEST","");
        this.Fields.put("MPD_CUENTA_DEST", "");

    }
}
