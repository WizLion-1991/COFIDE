/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.crm.mailing.entidades;

/**
 * Representa una tabla adicional de donde se tomaran datos para generar el mail
 *
 * @author ZeusSIWEB
 */
public class TablaAdicional {

   private String strNombreTabla;
   private String strNombreCampoLink;

   public String getStrNombreTabla() {
      return strNombreTabla;
   }

   public void setStrNombreTabla(String strNombreTabla) {
      this.strNombreTabla = strNombreTabla;
   }

   public String getStrNombreCampoLink() {
      return strNombreCampoLink;
   }

   public void setStrNombreCampoLink(String strNombreCampoLink) {
      this.strNombreCampoLink = strNombreCampoLink;
   }

}
