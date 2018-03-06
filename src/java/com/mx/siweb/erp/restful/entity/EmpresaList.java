/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Es una lista de empresas
 *
 * @author ZeusSIWEB
 */
public class EmpresaList {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private List<Empresa> empresaItem;

   public List<Empresa> getEmpresaItem() {
      return empresaItem;
   }

   public void setEmpresaItem(List<Empresa> empresaItem) {
      this.empresaItem = empresaItem;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public EmpresaList() {
      empresaItem = new ArrayList<Empresa>();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
