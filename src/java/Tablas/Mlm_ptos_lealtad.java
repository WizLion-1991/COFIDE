/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa los puntos de lealtad
 * @author ZeusGalindo
 */
public class Mlm_ptos_lealtad extends TableMaster{



   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
      public Mlm_ptos_lealtad() {
      super("mlm_ptos_lealtad", "MLM1_ID", "", "");
      this.Fields.put("MLM1_ID", 0);
      this.Fields.put("MLM1_FECHA", "");
      this.Fields.put("MLM1_HORA", "");
      this.Fields.put("MLM1_USER_ALTA", 0);
      this.Fields.put("MLM1_CONCEPTO", "");
      this.Fields.put("MLM1_IMPORTE", 0.0);
      this.Fields.put("MLM1_SALDO", 0.0);
      this.Fields.put("CO_ID", 0);
      this.Fields.put("CT_ID", 0);
      this.Fields.put("MLM1_ANULADO", 0);
      this.Fields.put("MLM1_USER_ANULA", 0);
      this.Fields.put("MLM1_FECHA_ANULA", "");
      this.Fields.put("MLM1_CICLO", 0);
      this.Fields.put("MC_ID", 0);
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("SC_ID", 0);

   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
