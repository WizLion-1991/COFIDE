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
public class cofide_contactos extends TableMaster {

    public cofide_contactos() {
        super("cofide_contactos", "CAMPOLLAVE", "", "");
        this.Fields.put("CCO_ID", new Integer(0));
        this.Fields.put("CCO_NOMBRE", "");
        this.Fields.put("CCO_APPATERNO", "");
        this.Fields.put("CCO_APMATERNO", "");
        this.Fields.put("CCO_TITULO", "");
        this.Fields.put("CCO_NOSOCIO", new Integer(0));
        this.Fields.put("CCO_AREA", "");
        this.Fields.put("CCO_ASOCIACION", "");
        this.Fields.put("CCO_CORREO", "");
        this.Fields.put("CCO_TELEFONO", "");
        this.Fields.put("CCO_EXTENCION", "");
        this.Fields.put("CCO_ALTERNO", "");
        this.Fields.put("CONTACTO_ID", new Integer(0));
        this.Fields.put("CT_ID", new Integer(0));
    }
}
