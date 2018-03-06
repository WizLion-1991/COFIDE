/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de detalle de compras
 * @author SIWEB
 */
public class vta_compradeta extends TableMaster {

    public vta_compradeta() {
        super("vta_compradeta", "COMD_ID", "", "");
        this.Fields.put("COMD_ID", new Integer(0));
        this.Fields.put("COM_ID", new Integer(0));
        this.Fields.put("PR_ID", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("COMD_CVE", "");
        this.Fields.put("COMD_CANTIDAD", new Double(0));
        this.Fields.put("COMD_PORDESC", new Double(0));
        this.Fields.put("COMD_DESCUENTO", new Double(0));
        this.Fields.put("COMD_CANTIDADSURTIDA", new Double(0));
        this.Fields.put("COMD_DESCRIPCION", "");
        this.Fields.put("COMD_COSTO",new Double(0));
        this.Fields.put("COMD_IMPORTE",new Double(0));
        this.Fields.put("COMD_PARIDAD",new Double(0));
        this.Fields.put("COMD_IMPUESTO1",new Double(0));
        this.Fields.put("COMD_IMPUESTO2",new Double(0));
        this.Fields.put("COMD_IMPUESTO3",new Double(0));
        this.Fields.put("COMD_EXENTO1", new Integer(0));
        this.Fields.put("COMD_EXENTO2", new Integer(0));
        this.Fields.put("COMD_EXENTO3", new Integer(0));
        this.Fields.put("COMD_TASAIVA1",new Double(0));
        this.Fields.put("COMD_TASAIVA2",new Double(0));
        this.Fields.put("COMD_TASAIVA3",new Double(0));
        this.Fields.put("COMD_NOTAS", "");
        this.Fields.put("COMD_NOSERIE", "");
        this.Fields.put("COMD_IMPORTEREAL",new Double(0));
        this.Fields.put("COMD_COSTOREAL",new Double(0));
        this.Fields.put("COMD_UNIDAD_MEDIDA", "");
        this.Fields.put("COMD_MONEDA", new Integer(0));
    }
}