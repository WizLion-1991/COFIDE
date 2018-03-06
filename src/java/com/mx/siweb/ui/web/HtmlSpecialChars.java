/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web;

/**
 *parsea los caracteres especiales del html
 * @author aleph_79
 */
public class HtmlSpecialChars {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public static String Parser(String strContenido){
      StringBuilder newContent = new StringBuilder(strContenido);
      return newContent.toString();
   }
   // </editor-fold>
}
