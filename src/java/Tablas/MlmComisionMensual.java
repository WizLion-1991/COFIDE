/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa las comisiones mensuales
 * @author ZeusSIWEB
 */
public class MlmComisionMensual extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public MlmComisionMensual() {
      super("mlm_comision_mensual", "CO_ID", "", "");
      this.Fields.put("CO_ID", 0);
      this.Fields.put("CT_ID", 0);
      this.Fields.put("MPEM_ID", 0);
      this.Fields.put("CO_IMPORTE", 0.0);
      this.Fields.put("CO_IMPUESTO1", 0.0);
      this.Fields.put("CO_IMPUESTO2", 0.0);
      this.Fields.put("CO_IMPUESTO3", 0.0);
      this.Fields.put("CO_RET1", 0.0);
      this.Fields.put("CO_RET2", 0.0);
      this.Fields.put("CO_RET3", 0.0);
      this.Fields.put("CO_CHEQUE", 0.0);
      this.Fields.put("CO_NIVEL", 0);
      this.Fields.put("CT_NIVELRED", 0);
      this.Fields.put("CO_PUNTOS_P", 0.0);
      this.Fields.put("CO_PUNTOS_G", 0.0);
      this.Fields.put("CO_NEGOCIO_P", 0.0);
      this.Fields.put("CO_NEGOCIO_G", 0.0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
