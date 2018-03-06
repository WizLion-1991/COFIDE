/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author CasaJosefa
 */
public class cofide_gtrabajo extends TableMaster {

    public cofide_gtrabajo() {
        super("cofide_gtrabajo", "CGD_ID", "", "");
        this.Fields.put("CGD_ID", new Integer(0));
        this.Fields.put("CGD_MES", "");
        this.Fields.put("CGD_ANIO", "");
        this.Fields.put("CGD_IMPVENTA",0.0);
        this.Fields.put("CGD_IMPVENTA_COBRO", 0.0);
        this.Fields.put("CGD_IMPMETA", 0.0);
        this.Fields.put("CG_ID", 0.0);

    }
}
