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
public class cofide_metas_usuario extends TableMaster {

    public cofide_metas_usuario() {
        super("cofide_metas_usuario", "CMU_ID", "", "");
        this.Fields.put("CMU_ID", 0);
        this.Fields.put("id_usuarios", 0);
        this.Fields.put("CMU_IMPORTE", 0.0);
        this.Fields.put("CMU_NUEVO_CTE", 0);
        this.Fields.put("CMU_FECHA", "");
    }
}
