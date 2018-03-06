/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author siweb
 */
public class vta_ciclico_deta extends TableMaster {

    public vta_ciclico_deta() {
        super("vta_ciclico_deta", "CCD_ID", "", "");
        this.Fields.put("CCD_ID", new Integer(0));
         this.Fields.put("CC_ID", new Integer(0));
        this.Fields.put("PR_CODIGO", "");
        this.Fields.put("PR_DESCRIPCION", "");
        //this.Fields.put("CCD_CANT_VEND", new Double(0));
        this.Fields.put("CCD_CANT_EXIST", new Double(0));
        this.Fields.put("CCD_CONTEO1", new Integer(0));
        this.Fields.put("CCD_CONTEO2", new Integer(0));
        this.Fields.put("CCD_ADERIBLE", new Integer(0));
       // this.Fields.put("CC_STATUS", new Integer(0));

    }

}
