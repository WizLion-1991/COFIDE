/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author N4v1d4d3s
 */
public class vta_sugerencias extends TableMaster {

    public vta_sugerencias() {
        super("vta_sugerencias", "SUG_ID", "", "");
        this.Fields.put("SUG_ID", new Integer(0));
        this.Fields.put("SUG_FECHA", "");
        this.Fields.put("SUG_DIRIGIDO", "");
        this.Fields.put("SUG_CORREO", "");
        this.Fields.put("SUG_COMENTARIO", "");
        this.Fields.put("CT_ID", new Integer(0));
        this.Fields.put("SUG_ESTATUS", "ABIERTO");
        setBolGetAutonumeric(true);
    }
}
