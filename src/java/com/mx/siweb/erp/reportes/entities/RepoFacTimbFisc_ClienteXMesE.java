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
public class RepoFacTimbFisc_ClienteXMesE {

   private String nombre;
   private int anio;
   private int mes;
   private double cuantos_anio;

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public int getAnio() {
      return anio;
   }

   public void setAnio(int anio) {
      this.anio = anio;
   }

   public int getMes() {
      return mes;
   }

   public void setMes(int mes) {
      this.mes = mes;
   }

   public double getCuantos_anio() {
      return cuantos_anio;
   }

   public void setCuantos_anio(double cuantos_anio) {
      this.cuantos_anio = cuantos_anio;
   }
}
