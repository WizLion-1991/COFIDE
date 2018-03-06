/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa el detalle de la tabla de notas de cargo
 * @author ZeusSIWEB
 */
public class VtaNotasCargosDeta extends TableMaster {

   public VtaNotasCargosDeta() {
      super("vta_notas_cargosdeta", "NCAD_ID", "", "");
      this.Fields.put("NCAD_ID", 0);
      this.Fields.put("NCA_ID", 0);
      this.Fields.put("NCAD_CVE", "");
      this.Fields.put("NCAD_DESCRIPCION", "");
      this.Fields.put("NCAD_IMPORTE", 0.0);
      this.Fields.put("NCAD_CANTIDAD", 0.0);
      this.Fields.put("NCAD_TASAIVA1", 0.0);
      this.Fields.put("NCAD_TASAIVA2", 0.0);
      this.Fields.put("NCAD_TASAIVA3", 0.0);
      this.Fields.put("NCAD_DESGLOSA1", 0);
      this.Fields.put("NCAD_DESGLOSA2", 0);
      this.Fields.put("NCAD_DESGLOSA3", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("NCAD_IMPUESTO1", 0.0);
      this.Fields.put("NCAD_IMPUESTO2", 0.0);
      this.Fields.put("NCAD_IMPUESTO3", 0.0);
      this.Fields.put("NCAD_NOSERIE", "");
      this.Fields.put("NCAD_ESREGALO", 0);
      this.Fields.put("NCAD_IMPORTEREAL", 0.0);
      this.Fields.put("NCAD_PRECIO", 0.0);
      this.Fields.put("NCAD_EXENTO1", 0);
      this.Fields.put("NCAD_EXENTO2", 0);
      this.Fields.put("NCAD_EXENTO3", 0);
      this.Fields.put("PR_ID", 0);
      this.Fields.put("NCAD_COSTO", 0.0);
      this.Fields.put("NCAD_GANANCIA", 0.0);
      this.Fields.put("NCAD_DESCUENTO", 0.0);
      this.Fields.put("NCAD_PORDESC", 0.0);
      this.Fields.put("NCAD_PRECFIJO", 0);
      this.Fields.put("NCAD_PRECREAL", 0.0);
      this.Fields.put("NCAD_ESDEVO", 0);
      this.Fields.put("NCAD_COMENTARIO", "");
      this.Fields.put("NCAD_PEDINUM", "");
      this.Fields.put("NCAD_PEDIFECHA", "");
      this.Fields.put("NCAD_PEDIADUANA", "");
      this.Fields.put("NCAD_UNIDAD_MEDIDA", "");
      this.Fields.put("NCAD_RET_ISR", 0);
      this.Fields.put("NCAD_RET_IVA", 0);
      this.Fields.put("NCAD_RET_FLETE", 0);
      this.Fields.put("PDD_ID", 0);
      this.Fields.put("TKTD_ID", 0);
      this.Fields.put("COTD_ID", 0);
      this.Fields.put("NCAD_PUNTOS", 0.0);
      this.Fields.put("NCAD_VNEGOCIO", 0.0);
      this.Fields.put("NCAD_IMP_PUNTOS", 0.0);
      this.Fields.put("NCAD_IMP_VNEGOCIO", 0.0);
      this.Fields.put("NCAD_DESC_PREC", 0);
      this.Fields.put("NCAD_DESC_PUNTOS", 0);
      this.Fields.put("NCAD_DESC_VNEGOCIO", 0);
      this.Fields.put("NCAD_DESC_ORI", 0.0);
      this.Fields.put("NCAD_REGALO", 0);
      this.Fields.put("NCAD_ID_PROMO", 0);
      this.Fields.put("NCAD_EXEN_RET_ISR", 0);
      this.Fields.put("NCAD_EXEN_RET_IVA", 0);
      this.Fields.put("NCAD_CAN_DEV", 0);
      this.Fields.put("NCAD_SERIES_DEV", "");
      this.Fields.put("CF_ID", 0);
   }

}
