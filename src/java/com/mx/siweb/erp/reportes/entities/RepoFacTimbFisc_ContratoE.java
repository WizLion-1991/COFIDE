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
public class RepoFacTimbFisc_ContratoE {

   private int idContrato;
   private String contratoArrendamiento;
   private String strFechaInicio;
   private String strFechaCierre;
   private double cuantosDesdeFecha;
   private int idCliente;
   private String nombreCte;
   private int anio;
   private double cuantosAnio;
   private double cuantosContrato;

   public double getCuantosContrato() {
      return cuantosContrato;
   }

   public void setCuantosContrato(double cuantosContrato) {
      this.cuantosContrato = cuantosContrato;
   }

   public String getStrFechaInicio() {
      return strFechaInicio;
   }

   public void setStrFechaInicio(String strFechaInicio) {
      this.strFechaInicio = strFechaInicio;
   }

   public String getStrFechaCierre() {
      return strFechaCierre;
   }

   public void setStrFechaCierre(String strFechaCierre) {
      this.strFechaCierre = strFechaCierre;
   }

   public double getCuantosDesdeFecha() {
      return cuantosDesdeFecha;
   }

   public void setCuantosDesdeFecha(double cuantosDesdeFecha) {
      this.cuantosDesdeFecha = cuantosDesdeFecha;
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
