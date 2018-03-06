/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

/**
 *
 * @author siweb
 */
public class VentasDeta {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String Fecha;
   private String Folio;
   private double Importe;
   private double Impuesto1;
   private double Total;
   private double Saldo;
   private double Importe_Puntos;
   private double Importe_Negocios;
   private String tipoDoc;
   private int id;

   public String getTipoDoc() {
      return tipoDoc;
   }

   public void setTipoDoc(String tipoDoc) {
      this.tipoDoc = tipoDoc;
   }

   public String getFecha() {
      return Fecha;
   }

   public void setFecha(String Fecha) {
      this.Fecha = Fecha;
   }

   public String getFolio() {
      return Folio;
   }

   public void setFolio(String Folio) {
      this.Folio = Folio;
   }

   public double getImporte() {
      return Importe;
   }

   public void setImporte(double Importe) {
      this.Importe = Importe;
   }

   public double getImpuesto1() {
      return Impuesto1;
   }

   public void setImpuesto1(double Impuesto1) {
      this.Impuesto1 = Impuesto1;
   }

   public double getTotal() {
      return Total;
   }

   public void setTotal(double Total) {
      this.Total = Total;
   }

   public double getSaldo() {
      return Saldo;
   }

   public void setSaldo(double Saldo) {
      this.Saldo = Saldo;
   }

   public double getImporte_Puntos() {
      return Importe_Puntos;
   }

   public void setImporte_Puntos(double Importe_Puntos) {
      this.Importe_Puntos = Importe_Puntos;
   }

   public double getImporte_Negocios() {
      return Importe_Negocios;
   }

   public void setImporte_Negocios(double Importe_Negocios) {
      this.Importe_Negocios = Importe_Negocios;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }
    // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>   

}
