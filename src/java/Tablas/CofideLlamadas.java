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
public class CofideLlamadas extends TableMaster {

    public CofideLlamadas() {
        super("cofide_llamada", "CL_ID", "", "");
        this.Fields.put("CL_ID", 0);
        this.Fields.put("CL_FECHA", "");
        this.Fields.put("CL_HORA", "");
        this.Fields.put("CL_USUARIO", "");
        this.Fields.put("CL_ID_CLIENTE", 0);
        this.Fields.put("CL_ID_BASE", 0);
        this.Fields.put("CL_EXITOSO", 0);
        this.Fields.put("CL_DESCARTADO", 0);
        this.Fields.put("CL_COMENTARIO", "");
        this.Fields.put("CL_CONTACTO", "");
        this.Fields.put("CL_TIEMPO_LLAMADA", 0.0);
        this.Fields.put("CL_FIN_LLAMADA", "");
        this.Fields.put("CL_PAUSA_CAPACITA", 0);
        this.Fields.put("CL_PAUSA_ADMIN", 0);
        this.Fields.put("CL_PAUSA_SANIT", 0);
        this.Fields.put("CL_PAUSA_COMIDA", 0);
        this.Fields.put("CL_EXT", "");
        this.Fields.put("uniqueid", "");
        this.Fields.put("CL_DESTINO", "");
    }
}
