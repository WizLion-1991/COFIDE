/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa una llamada de la campaña
 * @author ZeusSIWEB
 */
public class CofideCampaniasLlamadas extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CofideCampaniasLlamadas() {
      super("cofide_campanias_llamadas", "CAMR_ID", "", "");
      this.Fields.put("CAMR_ID", 0);
      this.Fields.put("CAMR_FECHA", "");
      this.Fields.put("CAMR_HORA", "");
      this.Fields.put("CAMR_USER", 0);
      this.Fields.put("CT_ID", 0);
      this.Fields.put("CAMP_ID", 0);
      this.Fields.put("CAMP_REALIZADO", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
