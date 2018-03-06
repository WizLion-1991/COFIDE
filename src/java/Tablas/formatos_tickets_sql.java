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
public class formatos_tickets_sql extends TableMaster{
    public formatos_tickets_sql()
    {
        super("formatos_tickets_sql", "FTD_ID", "", "");
        this.Fields.put("FTD_ID", new Integer(0));
        this.Fields.put("FT_ID", new Integer(0));
        this.Fields.put("FTD_TXT _SQL", "");
        this.Fields.put("FTD_ES_ENCABEZADO", new Integer(0));
    }
}
