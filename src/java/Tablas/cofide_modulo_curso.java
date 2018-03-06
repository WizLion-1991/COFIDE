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
public class cofide_modulo_curso extends TableMaster {

    public cofide_modulo_curso() {
        super("cofide_modulo_curso", "CAMPOLLAVE", "", "");
        this.Fields.put("CM_ID", new Integer(0));
        this.Fields.put("CC_CURSO_ID", new Integer(0));
        this.Fields.put("CC_NOMBRE_CURSO", "");
        this.setBolGetAutonumeric(true);
    }
}
