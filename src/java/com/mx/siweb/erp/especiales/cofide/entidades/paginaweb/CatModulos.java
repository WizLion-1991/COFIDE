/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.entidades.paginaweb;

import comSIWeb.Operaciones.TableMaster;

/**
 *Son los modulos
 * @author ZeusSIWEB
 */
public class CatModulos extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CatModulos() {
      super("cat_modulos", "", "", "");
      this.Fields.put("id_curso", 0);
      this.Fields.put("id_dip_sem", 0);
      this.Fields.put("id_tipo_evento", 0);
      this.Fields.put("modulo", 0);
      this.Fields.put("descripcion", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
