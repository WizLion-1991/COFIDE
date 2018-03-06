/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.webaccount.ui.entities;

/**
 *Representa una hoja de estilos
 * @author aleph_79
 */
public class StyleSheet {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strPath;
   private String strTipo;
   private String strMedio;

   public String getStrMedio() {
      return strMedio;
   }

   public void setStrMedio(String strMedio) {
      this.strMedio = strMedio;
   }

   public String getStrPath() {
      return strPath;
   }

   public void setStrPath(String strPath) {
      this.strPath = strPath;
   }

   public String getStrTipo() {
      return strTipo;
   }

   public void setStrTipo(String strTipo) {
      this.strTipo = strTipo;
   }
   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public StyleSheet(String strPath, String strTipo, String strMedio) {
      this.strPath = strPath;
      this.strTipo = strTipo;
      this.strMedio = strMedio;
   }

   public StyleSheet() {
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
