/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author N4v1d4d3s
 */
public class usuarios_bodegas extends TableMaster {

    public usuarios_bodegas() {
        super("usuarios_bodegas", "PUS_ID", "", "");
        this.Fields.put("PUS_ID", new Integer(0));
        this.Fields.put("SC1_ID", new Integer(0));
        this.Fields.put("id_usuarios", new Integer(0));
        this.Fields.put("PF_ID", new Integer(0));

    }

}
