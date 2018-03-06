package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa el detalle de las facturas
 *
 * @author zeus
 */
public class vta_facturadeta extends TableMaster {

    /**
     * Constructor
     */
    public vta_facturadeta() {
        super("vta_facturasdeta", "FACD_ID", "", "");
        this.Fields.put("FACD_ID", new Integer(0));
        this.Fields.put("FAC_ID", new Integer(0));
        this.Fields.put("FACD_CVE", "");
        this.Fields.put("FACD_DESCRIPCION", "");
        this.Fields.put("FACD_IMPORTE", new Double(0));
        this.Fields.put("FACD_CANTIDAD", new Integer(0));
        this.Fields.put("FACD_TASAIVA1", new Double(0));
        this.Fields.put("FACD_TASAIVA2", new Double(0));
        this.Fields.put("FACD_TASAIVA3", new Double(0));
        this.Fields.put("FACD_DESGLOSA1", new Integer(0));
        this.Fields.put("FACD_DESGLOSA2", new Integer(0));
        this.Fields.put("FACD_DESGLOSA3", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("FACD_IMPUESTO1", new Double(0));
        this.Fields.put("FACD_IMPUESTO2", new Double(0));
        this.Fields.put("FACD_IMPUESTO3", new Double(0));
        this.Fields.put("FACD_NOSERIE", "");
        this.Fields.put("FACD_ESREGALO", new Integer(0));
        this.Fields.put("FACD_IMPORTEREAL", new Double(0));
        this.Fields.put("FACD_PRECIO", new Double(0));
        this.Fields.put("FACD_EXENTO1", new Integer(0));
        this.Fields.put("FACD_EXENTO2", new Integer(0));
        this.Fields.put("FACD_EXENTO3", new Integer(0));
        this.Fields.put("PR_ID", new Integer(0));
        this.Fields.put("FACD_COSTO", new Double(0));
        this.Fields.put("FACD_GANANCIA", new Double(0));
        this.Fields.put("FACD_DESCUENTO", new Double(0));
        this.Fields.put("FACD_PORDESC", new Double(0));
        this.Fields.put("FACD_PRECFIJO", new Integer(0));
        this.Fields.put("FACD_PRECREAL", new Double(0));
        this.Fields.put("FACD_ESDEVO", new Integer(0));
        this.Fields.put("FACD_COMENTARIO", "");
        this.Fields.put("FACD_PEDINUM", "");
        this.Fields.put("FACD_PEDIFECHA", "");
        this.Fields.put("FACD_PEDIADUANA", "");
        this.Fields.put("FACD_UNIDAD_MEDIDA", "");
        this.Fields.put("FACD_RET_ISR", new Integer(0));
        this.Fields.put("FACD_RET_IVA", new Integer(0));
        this.Fields.put("FACD_RET_FLETE", new Integer(0));
        this.Fields.put("PDD_ID", new Integer(0));
        this.Fields.put("TKTD_ID", new Integer(0));
        this.Fields.put("COTD_ID", new Integer(0));
        this.Fields.put("FACD_PUNTOS", new Double(0));
        this.Fields.put("FACD_VNEGOCIO", new Double(0));
        this.Fields.put("FACD_IMP_PUNTOS", new Double(0));
        this.Fields.put("FACD_IMP_VNEGOCIO", new Double(0));
        this.Fields.put("FACD_DESC_PREC", new Integer(0));
        this.Fields.put("FACD_DESC_PUNTOS", new Integer(0));
        this.Fields.put("FACD_DESC_VNEGOCIO", new Integer(0));
        this.Fields.put("FACD_DESC_ORI", new Double(0));
        this.Fields.put("FACD_REGALO", new Integer(0));
        this.Fields.put("FACD_ID_PROMO", new Integer(0));
        this.Fields.put("FACD_EXEN_RET_ISR", new Integer(0));
        this.Fields.put("FACD_EXEN_RET_IVA", new Integer(0));
        this.Fields.put("FACD_CAN_DEV", new Integer(0));
        this.Fields.put("FACD_SERIES_DEV", "");
        this.Fields.put("CF_ID", new Integer(0));
        this.Fields.put("FACD_TIPO_CURSO", new Integer(0));
        this.Fields.put("FACD_CANCEL", new Integer(0));
        this.Fields.put("FACD_ID_CURSO", new Integer(0));
        this.Fields.put("FACD_CANTIDAD_IMPRIMIR", new Integer(0));
        this.Fields.put("FACD_IMPORTE_COMISION", new Integer(0));
        this.Fields.put("FACD_FORMULA", "");
        this.Fields.put("FACD_DETALLE_COMISION", "");
        this.Fields.put("FACD_CVE_PRODSERV", "");
        this.Fields.put("FACD_CVE_UNIDAD", "");
        this.Fields.put("FACD_CANTTOT_BACKORDER", new Double(0));
        this.Fields.put("FACD_CANT_CORTESIAS", new Integer(0));
        this.Fields.put("FACD_CANT_MULTIPLOS", new Double(0));
        this.Fields.put("FACD_ES_COMPONENTE", new Integer(0));
        this.Fields.put("FACD_ES_PAQUETE", new Integer(0));
        this.Fields.put("FACD_ES_VIRTUAL", new Integer(0));
        this.Fields.put("FACD_MULTIPLO", new Integer(0));
        this.Fields.put("FACD_PRECIO_MULTIPLO", new Double(0));
        this.Fields.put("FACD_PR_PAQUETE", new Integer(0));
    }
}
