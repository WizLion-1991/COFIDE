/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa los movimientos de los puntos de lealtad
 *
 * @author ZeusGalindo
 */
public class Mlm_ptos_lealtad_deta extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Mlm_ptos_lealtad_deta() {
      super("mlm_ptos_lealtad_deta", "MLM2_ID", "", "");
      this.Fields.put("MLM2_ID", 0);
      this.Fields.put("MLM1_ID", 0);
      this.Fields.put("MLM2_FECHA", "");
      this.Fields.put("MLM2_HORA", "");
      this.Fields.put("MLM2_USER_ALTA", 0);
      this.Fields.put("MLM2_CARGO", 0.0);
      this.Fields.put("MLM2_ABONO", 0.0);
      this.Fields.put("MLM2_CONCEPTO", "");
      this.Fields.put("MC_ID", 0);
      this.Fields.put("CT_ID", 0);
      this.Fields.put("MLM2_ANULADO", 0);
      this.Fields.put("MLM2_USER_ANULA", 0);
      this.Fields.put("MLM2_HORA_ANULA", "");
      this.Fields.put("MLM2_FECHA_ANULA", "");
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("SC_ID", 0);
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
