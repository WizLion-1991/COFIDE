package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa una partida de una nota de credito
 * @author zeus
 */
public class vta_ncreditodeta_prov extends TableMaster {
   
   /**
    * Constructor
    */
   public vta_ncreditodeta_prov() {
      super("vta_ncreditodeta_prov", "NCD_ID", "", "");
      this.Fields.put("NCD_ID", new Integer(0));
      this.Fields.put("NC_ID", new Integer(0));
      this.Fields.put("NCD_CVE", "");
      this.Fields.put("NCD_DESCRIPCION", "");
      this.Fields.put("NCD_IMPORTE", new Double(0));
      this.Fields.put("NCD_CANTIDAD", new Double(0));
      this.Fields.put("NCD_TASAIVA1", new Double(0));
      this.Fields.put("NCD_TASAIVA2", new Double(0));
      this.Fields.put("NCD_TASAIVA3", new Double(0));
      this.Fields.put("NCD_DESGLOSA1", new Integer(0));
      this.Fields.put("NCD_DESGLOSA2", new Integer(0));
      this.Fields.put("NCD_DESGLOSA3", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("NCD_IMPUESTO1", new Double(0));
      this.Fields.put("NCD_IMPUESTO2", new Double(0));
      this.Fields.put("NCD_IMPUESTO3", new Double(0));
      this.Fields.put("NCD_NOSERIE", "");
      this.Fields.put("NCD_ESREGALO", new Integer(0));
      this.Fields.put("NCD_IMPORTEREAL", new Double(0));
      this.Fields.put("NCD_PRECIO", new Double(0));
      this.Fields.put("NCD_EXENTO1", new Integer(0));
      this.Fields.put("NCD_EXENTO2", new Integer(0));
      this.Fields.put("NCD_EXENTO3", new Integer(0));
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("NCD_COSTO", new Double(0));
      this.Fields.put("NCD_GANANCIA", new Double(0));
      this.Fields.put("NCD_DESCUENTO", new Double(0));
      this.Fields.put("NCD_PORDESC", new Double(0));
      this.Fields.put("NCD_PRECFIJO", new Integer(0));
      this.Fields.put("NCD_PRECREAL", new Double(0));
      this.Fields.put("NCD_ESDEVO", new Integer(0));
      this.Fields.put("NCD_COMENTARIO", "");
      this.Fields.put("NCD_PEDINUM", "");
      this.Fields.put("NCD_PEDIFECHA", "");
      this.Fields.put("NCD_PEDIADUANA", "");
      this.Fields.put("NCD_UNIDAD_MEDIDA", "");
      this.Fields.put("FACD_ID", new Integer(0));
   }
}
