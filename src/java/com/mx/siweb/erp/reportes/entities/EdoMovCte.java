/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.reportes.entities;

/**
 *Representa un movimiento del estado de cuenta de clientes
 * @author ZeusGalindo
 */
public class EdoMovCte {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String tipoDocumento;
   private String folio;
   private String fecha;
   private String vencimiento;
   private double dblCargos;
   private double dblAbonos;
   private double dblSaldo;
   private String estatus;
   private String banco;
   
   public String getTipoDocumento() {
      return tipoDocumento;
   }

   public void setTipoDocumento(String tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
   }

   public String getFolio() {
      return folio;
   }

   public void setFolio(String folio) {
      this.folio = folio;
   }

   public String getFecha() {
      return fecha;
   }

   public void setFecha(String fecha) {
      this.fecha = fecha;
   }

   public String getVencimiento() {
      return vencimiento;
   }

   public void setVencimiento(String vencimiento) {
      this.vencimiento = vencimiento;
   }

   public double getDblCargos() {
      return dblCargos;
   }

   public void setDblCargos(double dblCargos) {
      this.dblCargos = dblCargos;
   }

   public double getDblAbonos() {
      return dblAbonos;
   }

   public void setDblAbonos(double dblAbonos) {
      this.dblAbonos = dblAbonos;
   }

   public double getDblSaldo() {
      return dblSaldo;
   }

   public void setDblSaldo(double dblSaldo) {
      this.dblSaldo = dblSaldo;
   }

   public String getEstatus() {
      return estatus;
   }

   public void setEstatus(String estatus) {
      this.estatus = estatus;
   }

   public String getBanco() {
      return banco;
   }

   public void setBanco(String banco) {
      this.banco = banco;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
