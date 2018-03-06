/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siweb
 */
public class RepoFacTimbFisc_ContratoXMesE {

   private int idContrato;
   private String contratoArrendamiento;
   private int idCliente;
   private String nombreCte;
   private int anio;
   private double cuantosAnio;
   private int mes;

   public int getMes() {
      return mes;
   }

   public void setMes(int mes) {
      this.mes = mes;
   }

   public int getIdContrato() {
      return idContrato;
   }

   public void setIdContrato(int idContrato) {
      this.idContrato = idContrato;
   }

   public String getContratoArrendamiento() {
      return contratoArrendamiento;
   }

   public void setContratoArrendamiento(String contratoArrendamiento) {
      this.contratoArrendamiento = contratoArrendamiento;
   }

   public int getIdCliente() {
      return idCliente;
   }

   public void setIdCliente(int idCliente) {
      this.idCliente = idCliente;
   }

   public String getNombreCte() {
      return nombreCte;
   }

   public void setNombreCte(String nombreCte) {
      this.nombreCte = nombreCte;
   }

   public int getAnio() {
      return anio;
   }

   public void setAnio(int anio) {
      this.anio = anio;
   }

   public double getCuantosAnio() {
      return cuantosAnio;
   }

   public void setCuantosAnio(double cuantosAnio) {
      this.cuantosAnio = cuantosAnio;
   }


 
}
