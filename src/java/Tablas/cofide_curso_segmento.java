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
public class cofide_curso_segmento extends TableMaster {

    public cofide_curso_segmento() {
        super("cofide_curso_segmento", "CAMPOLLAVE", "", "");
        this.Fields.put("CCS_ID", new Integer(0));
        this.Fields.put("CC_CURSO_ID", new Integer(0));
        this.Fields.put("CC_AREA", "");
        this.setBolGetAutonumeric(true);
    }
}
