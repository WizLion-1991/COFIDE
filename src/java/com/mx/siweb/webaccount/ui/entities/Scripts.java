/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.webaccount.ui.entities;

/**
 *Representa un script por pintar en pantalla
 * @author aleph_79
 */
public class Scripts {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strNomScript;
   private String strTipoScript;

   public String getStrNomScript() {
      return strNomScript;
   }

   public void setStrNomScript(String strNomScript) {
      this.strNomScript = strNomScript;
   }

   public String getStrTipoScript() {
      return strTipoScript;
   }

   public void setStrTipoScript(String strTipoScript) {
      this.strTipoScript = strTipoScript;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Scripts() {
   }

   public Scripts(String strNomScript, String strTipoScript) {
      this.strNomScript = strNomScript;
      this.strTipoScript = strTipoScript;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
