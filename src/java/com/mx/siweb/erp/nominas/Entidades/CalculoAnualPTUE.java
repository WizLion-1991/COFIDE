/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas.Entidades;

/**
 *
 * @author CasaJosefa
 */
public class CalculoAnualPTUE {

   int numero = 0;
   String nombre = "";
   double ptuDias = 0.0;
   double ptuSalario = 0.0;
   double ptuTotal = 0.0;
   double diasLaborados = 0;
   double sueldoPrecibido = 0;

   public double getDiasLaborados() {
      return diasLaborados;
   }

   public void setDiasLaborados(double diasLaborados) {
      this.diasLaborados = diasLaborados;
   }

   public double getSueldoPrecibido() {
      return sueldoPrecibido;
   }

   public void setSueldoPrecibido(double sueldoPrecibido) {
      this.sueldoPrecibido = sueldoPrecibido;
   }

   public int getNumero() {
      return numero;
   }

   public void setNumero(int numero) {
      this.numero = numero;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public double getPtuDias() {
      return ptuDias;
   }

   public void setPtuDias(double ptuDias) {
      this.ptuDias = ptuDias;
   }

   public double getPtuSalario() {
      return ptuSalario;
   }

   public void setPtuSalario(double ptuSalario) {
      this.ptuSalario = ptuSalario;
   }

   public double getPtuTotal() {
      return ptuTotal;
   }

   public void setPtuTotal(double ptuTotal) {
      this.ptuTotal = ptuTotal;
   }

}
