/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author siweb
 */
public class Periodo {
    private List<PeriodoDeta>PeriodoItem;
    private String codigo;
    
    public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public List<PeriodoDeta> getPeriodoItem() {
      return PeriodoItem;
   }

   public void setPeriodoItem(List<PeriodoDeta> PeriodoItem) {
      this.PeriodoItem = PeriodoItem;
   }

   public Periodo() {
      PeriodoItem = new ArrayList<PeriodoDeta>();
   }
    
    
    
}
