/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author siwebmx5
 */
public class formatos_tickets extends TableMaster{
    public formatos_tickets()
    {
        super("formatos_tickets", "FT_ID", "", "");
        this.Fields.put("FT_ID", new Integer(0));
        this.Fields.put("FT_ABRV", "");
        this.Fields.put("FT_NOMBRE", "");
        this.Fields.put("FT_TXT_ENCABEZADO", "");
        this.Fields.put("FT_TXT_CUERPO", "");
        this.Fields.put("FT_TXT_PIE", "");
    }
}
