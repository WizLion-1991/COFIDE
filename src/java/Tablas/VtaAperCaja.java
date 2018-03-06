/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;
import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author Fernando
 */
public class VtaAperCaja extends TableMaster {

    public VtaAperCaja() {
        super("vta_aper_caja", "APC_ID", "", "");
        this.Fields.put("APC_ID", 0);
        this.Fields.put("SC_ID", 0);
        this.Fields.put("EMP_ID", 0);
        this.Fields.put("APC_FECHA_APER", "");
        this.Fields.put("APC_HORA_APER", "");
        this.Fields.put("id_usuario", 0);
        this.Fields.put("APC_ESTATUS", 0);
        this.Fields.put("APC_TOTAL_PESOS", 0.);
        this.Fields.put("APC_TOTAL_DOLARES", 0.0);
        this.Fields.put("APC_FECHA_CIERRE", "");
        this.Fields.put("APC_HORA_CIERRE", "");
        this.setBolGetAutonumeric(true);
    }
}