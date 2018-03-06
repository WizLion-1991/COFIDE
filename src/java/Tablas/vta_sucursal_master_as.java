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
public class vta_sucursal_master_as extends TableMaster{
    public vta_sucursal_master_as() {
        super("vta_sucursal_master_as", "SMA_ID", "", "");
        this.Fields.put("SMA_ID", new Integer(0));
        this.Fields.put("SM_ID", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
    }
    
}
