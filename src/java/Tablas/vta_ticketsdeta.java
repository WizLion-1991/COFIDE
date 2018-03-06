package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa el detalle de los tickets
 *
 * @author zeus
 */
public class vta_ticketsdeta extends TableMaster {

    /**
     * Constructor
     */
    public vta_ticketsdeta() {
        super("vta_ticketsdeta", "TKTD_ID", "", "");
        this.Fields.put("TKTD_ID", new Integer(0));
        this.Fields.put("TKT_ID", new Integer(0));
        this.Fields.put("TKTD_CVE", "");
        this.Fields.put("TKTD_DESCRIPCION", "");
        this.Fields.put("TKTD_IMPORTE", new Double(0));
        this.Fields.put("TKTD_CANTIDAD", new Integer(0));
        this.Fields.put("TKTD_TASAIVA1", new Double(0));
        this.Fields.put("TKTD_TASAIVA2", new Double(0));
        this.Fields.put("TKTD_TASAIVA3", new Double(0));
        this.Fields.put("TKTD_DESGLOSA1", new Integer(0));
        this.Fields.put("TKTD_DESGLOSA2", new Integer(0));
        this.Fields.put("TKTD_DESGLOSA3", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("TKTD_IMPUESTO1", new Double(0));
        this.Fields.put("TKTD_IMPUESTO2", new Double(0));
        this.Fields.put("TKTD_IMPUESTO3", new Double(0));
        this.Fields.put("TKTD_NOSERIE", "");
        this.Fields.put("TKTD_ESREGALO", new Integer(0));
        this.Fields.put("TKTD_IMPORTEREAL", new Double(0));
        this.Fields.put("TKTD_PRECIO", new Double(0));
        this.Fields.put("TKTD_EXENTO1", new Integer(0));
        this.Fields.put("TKTD_EXENTO2", new Integer(0));
        this.Fields.put("TKTD_EXENTO3", new Integer(0));
        this.Fields.put("PR_ID", new Integer(0));
        this.Fields.put("TKTD_COSTO", new Double(0));
        this.Fields.put("TKTD_GANANCIA", new Double(0));
        this.Fields.put("TKTD_DESCUENTO", new Double(0));
        this.Fields.put("TKTD_PORDESC", new Double(0));
        this.Fields.put("TKTD_PRECFIJO", new Integer(0));
        this.Fields.put("TKTD_PRECREAL", new Double(0));
        this.Fields.put("TKTD_ESDEVO", new Integer(0));
        this.Fields.put("TKTD_COMENTARIO", "");
        this.Fields.put("TKT_PEDINUM", "");
        this.Fields.put("TKT_PEDIFECHA", "");
        this.Fields.put("TKT_PEDIADUANA", "");
        this.Fields.put("TKTD_RET_ISR", new Integer(0));
        this.Fields.put("TKTD_RET_IVA", new Integer(0));
        this.Fields.put("TKTD_RET_FLETE", new Integer(0));
        this.Fields.put("TKTD_UNIDAD_MEDIDA", "");
        this.Fields.put("PDD_ID", new Integer(0));
        this.Fields.put("COTD_ID", new Integer(0));
        this.Fields.put("TKTD_PUNTOS", new Double(0));
        this.Fields.put("TKTD_VNEGOCIO", new Double(0));
        this.Fields.put("TKTD_IMP_PUNTOS", new Double(0));
        this.Fields.put("TKTD_IMP_VNEGOCIO", new Double(0));
        this.Fields.put("TKTD_DESC_PREC", new Integer(0));
        this.Fields.put("TKTD_DESC_PUNTOS", new Integer(0));
        this.Fields.put("TKTD_DESC_VNEGOCIO", new Integer(0));
        this.Fields.put("TKTD_DESC_ORI", new Double(0));
        this.Fields.put("TKTD_REGALO", new Integer(0));
        this.Fields.put("TKTD_ID_PROMO", new Integer(0));
        this.Fields.put("TKTD_EXEN_RET_ISR", new Integer(0));
        this.Fields.put("TKTD_EXEN_RET_IVA", new Integer(0));
        this.Fields.put("CF_ID", new Integer(0));
        this.Fields.put("TKTD_CAN_DEV", new Double(0));
        this.Fields.put("TKTD_SERIES_DEV", "");
        this.Fields.put("TKTD_TIPO_CURSO", new Integer(0));
        this.Fields.put("TKTD_ID_CURSO", new Integer(0));
        this.Fields.put("TKTD_CVE_PRODSERV", "");
        this.setBolGetAutonumeric(false);
    }
}
