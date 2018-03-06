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
public class RepoFacTimbFisc_ClienteE {

   private int idCliente;
   private String nombre;
   private int anio;
   private double cuantosAnio;

   public int getIdCliente() {
      return idCliente;
   }

   public void setIdCliente(int idCliente) {
      this.idCliente = idCliente;
   }

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

   public double getCuantosAnio() {
      return cuantosAnio;
   }

   public void setCuantosAnio(double cuantosAnio) {
      this.cuantosAnio = cuantosAnio;
   }

  
}
