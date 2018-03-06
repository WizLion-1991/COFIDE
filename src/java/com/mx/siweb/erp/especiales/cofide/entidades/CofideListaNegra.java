/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *Es la lista negra del CRM
 * @author ZeusSIWEB
 */
public class CofideListaNegra extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CofideListaNegra() {
      super("cofide_lista_negra", "CLN_ID", "", "");
      this.Fields.put("CLN_ID", 0);
      this.Fields.put("CB_CT_CORREO", "");
      this.Fields.put("CLN_EMP_ID", 0);
      this.Fields.put("CLN_SC_ID", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
