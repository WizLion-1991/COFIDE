/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades.paginaweb;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de la interfase para el material extra
 * @author ZeusSIWEB
 */
public class CatCrmMat extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
      public CatCrmMat() {
      super("interfase_crm_mat", "id_inter_crm_mat", "", "");
      this.Fields.put("id_inter_crm_mat", 0);
      this.Fields.put("id_venta", 0);
      this.Fields.put("cant_reserv", 0);
      this.Fields.put("id_evento", 0);
      this.Fields.put("id_tipo_evento", 0);
      this.Fields.put("email", "");
      this.Fields.put("process", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>



}
