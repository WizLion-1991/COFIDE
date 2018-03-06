/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa los datos de un pedimento
 * @author aleph_79
 */
public class vta_pedimentos extends TableMaster {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public vta_pedimentos() {
      super("vta_pedimentos", "PED_ID", "", "");
      this.Fields.put("PED_ID", new Integer(0));
      this.Fields.put("PED_COD", "");
      this.Fields.put("PED_DESC", "");
      this.Fields.put("PED_FECHA_ENTRA", "");
      this.Fields.put("PED_FECHA_CRUCE", "");
      this.Fields.put("AD_ID", new Integer(0));
      this.Fields.put("MON_ID", new Integer(0));
      this.Fields.put("PED_PARIDAD", new Double(0));
      this.Fields.put("PD_DOLAR", new Double(0));
      this.Fields.put("PED_ADUANA_16", new Double(0));
      this.Fields.put("PED_ADUANA_11", new Double(0));
      this.Fields.put("PED_IVA_16", new Double(0));
      this.Fields.put("PED_IVA_11", new Double(0));
      this.Fields.put("PED_DTA", new Double(0));
      this.Fields.put("PED_IGI_IGE", new Double(0));
      this.Fields.put("PED_APLICADO", new Integer(0));
      this.Fields.put("PED_USR_APLIC", new Integer(0));
      this.Fields.put("PED_FECHA_APLIC", "");
      this.Fields.put("PED_HORA_APLIC", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
