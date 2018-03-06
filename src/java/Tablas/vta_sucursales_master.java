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
public class vta_sucursales_master extends TableMaster {

    public vta_sucursales_master() {
        super("vta_sucursales_master", "SM_ID", "", "");
        this.Fields.put("SM_ID", new Integer(0));
        this.Fields.put("SM_CODIGO", "");
        this.Fields.put("SM_NOMBRE", "");
        this.Fields.put("SM_DIRECCION", "");
        this.Fields.put("SM_CP", "");
        this.Fields.put("SM_CIUDAD", "");
        this.Fields.put("SM_MUNICIPIO", "");
        this.Fields.put("PA_ID", new Integer(0));
        this.Fields.put("ESP_ID", new Integer(0));
        this.Fields.put("SM_TELEFONO", "");
        this.Fields.put("SM_ENCARGADO", "");
        this.Fields.put("TI_ID", new Integer(0));
        
    }
}
