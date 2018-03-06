package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla a detalle de las cotizaciones
 *
 * @author zeus
 */
public class vta_cotizadeta extends TableMaster {

   /**
    * Constructor
    */
   public vta_cotizadeta() {
      super("vta_cotizadeta", "CAMPOLLAVE", "", "");
      this.Fields.put("COTD_ID", new Integer(0));
      this.Fields.put("COT_ID", new Integer(0));
      this.Fields.put("COTD_CVE", "");
      this.Fields.put("COTD_DESCRIPCION", "");
      this.Fields.put("COTD_IMPORTE", new Double(0));
      this.Fields.put("COTD_CANTIDAD", new Double(0));
      this.Fields.put("COTD_TASAIVA1", new Double(0));
      this.Fields.put("COTD_TASAIVA2", new Double(0));
      this.Fields.put("COTD_TASAIVA3", new Double(0));
      this.Fields.put("COTD_DESGLOSA1", new Integer(0));
      this.Fields.put("COTD_DESGLOSA2", new Integer(0));
      this.Fields.put("COTD_DESGLOSA3", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("COTD_IMPUESTO1", new Double(0));
      this.Fields.put("COTD_IMPUESTO2", new Double(0));
      this.Fields.put("COTD_IMPUESTO3", new Double(0));
      this.Fields.put("COTD_NOSERIE", "");
      this.Fields.put("COTD_ESREGALO", new Integer(0));
      this.Fields.put("COTD_IMPORTEREAL", new Double(0));
      this.Fields.put("COTD_PRECIO", new Double(0));
      this.Fields.put("COTD_EXENTO1", new Integer(0));
      this.Fields.put("COTD_EXENTO2", new Integer(0));
      this.Fields.put("COTD_EXENTO3", new Integer(0));
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("COTD_COSTO", new Double(0));
      this.Fields.put("COTD_GANANCIA", new Double(0));
      this.Fields.put("COTD_DESCUENTO", new Double(0));
      this.Fields.put("COTD_PORDESC", new Double(0));
      this.Fields.put("COTD_PRECFIJO", new Integer(0));
      this.Fields.put("COTD_PRECREAL", new Double(0));
      this.Fields.put("COTD_COMENTARIO", "");
      this.Fields.put("COTD_PEDINUM", "");
      this.Fields.put("COTD_PEDIFECHA", "");
      this.Fields.put("COTD_PEDIADUANA", "");
      this.Fields.put("COTD_RET_ISR", new Integer(0));
      this.Fields.put("COTD_RET_IVA", new Integer(0));
      this.Fields.put("COTD_RET_FLETE", new Integer(0));
      this.Fields.put("COTD_PUNTOS", new Double(0));
      this.Fields.put("COTD_VNEGOCIO", new Double(0));
      this.Fields.put("COTD_IMP_PUNTOS", new Double(0));
      this.Fields.put("COTD_IMP_VNEGOCIO", new Double(0));
      this.Fields.put("COTD_DESC_PREC", new Integer(0));
      this.Fields.put("COTD_DESC_PUNTOS", new Integer(0));
      this.Fields.put("COTD_DESC_VNEGOCIO", new Integer(0));
      this.Fields.put("COTD_DESC_ORI", new Double(0));
      this.Fields.put("COTD_REGALO", new Integer(0));
      this.Fields.put("COTD_ID_PROMO", new Integer(0));
      this.Fields.put("COTD_EXEN_RET_ISR", new Integer(0));
      this.Fields.put("COTD_EXEN_RET_IVA", new Integer(0));
      this.Fields.put("CF_ID", new Integer(0));
      this.Fields.put("COTD_UNIDAD_MEDIDA", "");
   }
}
