/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.siweb.entities;

/**
 *Representa el contrato por facturar
 * @author ZeusGalindo
 */
public class ContratoporFacturar {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   
   String strDescripcion;
   String strFecha;
   double dblImporte;
   String strTipoSistema;
   String strPeriodicidad;

   public String getStrDescripcion() {
      return strDescripcion;
   }

   public void setStrDescripcion(String strDescripcion) {
      this.strDescripcion = strDescripcion;
   }

   public String getStrFecha() {
      return strFecha;
   }

   public void setStrFecha(String strFecha) {
      this.strFecha = strFecha;
   }

   public double getDblImporte() {
      return dblImporte;
   }

   public void setDblImporte(double dblImporte) {
      this.dblImporte = dblImporte;
   }

   public String getStrTipoSistema() {
      return strTipoSistema;
   }

   public void setStrTipoSistema(String strTipoSistema) {
      this.strTipoSistema = strTipoSistema;
   }

   public String getStrPeriodicidad() {
      return strPeriodicidad;
   }

   public void setStrPeriodicidad(String strPeriodicidad) {
      this.strPeriodicidad = strPeriodicidad;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
