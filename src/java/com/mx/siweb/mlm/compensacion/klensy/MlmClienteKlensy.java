/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.klensy;

import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;

/**
 *
 * @author ZeusGalindo
 */
public class MlmClienteKlensy extends mlm_cliente {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intHijos;
   private int intNietos;
   private int intBisnietos;
   private int intTataranietos;
   private int intHijosCiclo1;
   private int intNietosCiclo1;
   private int intBisnietosCiclo1;
   private int intTataranietosCiclo1;
   private int intHijosCiclo2;
   private int intNietosCiclo2;
   private int intBisnietosCiclo2;
   private int intTataranietosCiclo2;
   private int intHijosCiclo3;
   private int intNietosCiclo3;
   private int intBisnietosCiclo3;
   private int intTataranietosCiclo3;
   private int intHijosCiclo4;
   private int intNietosCiclo4;
   private int intBisnietosCiclo4;
   private int intTataranietosCiclo4;
   private int intHijosCiclo5;
   private int intNietosCiclo5;
   private int intBisnietosCiclo5;
   private int intTataranietosCiclo5;
   private int intPlanOro;
   private int intCicloActual;
   private int intCicloCerrado;
   private boolean bolCierraCiclo;
   private int intConsumePuntos1;
   private int intConsumePuntos2;
   private int intConsumePuntos3;
   private int intConsumePuntos4;
   private double dblPuntosCiclo1;
   private double dblPuntosCiclo2;
   private double dblPuntosCiclo3;
   private double dblPuntosCiclo4;
   private double dblPuntosCiclo5;
   private int intReferenciados;

   public int getIntReferenciados() {
      return intReferenciados;
   }

   public void setIntReferenciados(int intReferenciados) {
      this.intReferenciados = intReferenciados;
   }

   public double getDblPuntosCiclo5() {
      return dblPuntosCiclo5;
   }

   public void setDblPuntosCiclo5(double dblPuntosCiclo5) {
      this.dblPuntosCiclo5 = dblPuntosCiclo5;
   }

   public double getDblPuntosCiclo1() {
      return dblPuntosCiclo1;
   }

   public void setDblPuntosCiclo1(double dblPuntosCiclo1) {
      this.dblPuntosCiclo1 = dblPuntosCiclo1;
   }

   public double getDblPuntosCiclo2() {
      return dblPuntosCiclo2;
   }

   public void setDblPuntosCiclo2(double dblPuntosCiclo2) {
      this.dblPuntosCiclo2 = dblPuntosCiclo2;
   }

   public double getDblPuntosCiclo3() {
      return dblPuntosCiclo3;
   }

   public void setDblPuntosCiclo3(double dblPuntosCiclo3) {
      this.dblPuntosCiclo3 = dblPuntosCiclo3;
   }

   public double getDblPuntosCiclo4() {
      return dblPuntosCiclo4;
   }

   public void setDblPuntosCiclo4(double dblPuntosCiclo4) {
      this.dblPuntosCiclo4 = dblPuntosCiclo4;
   }

   

   public int getIntConsumePuntos1() {
      return intConsumePuntos1;
   }

   public void setIntConsumePuntos1(int intConsumePuntos1) {
      this.intConsumePuntos1 = intConsumePuntos1;
   }

   public int getIntConsumePuntos2() {
      return intConsumePuntos2;
   }

   public void setIntConsumePuntos2(int intConsumePuntos2) {
      this.intConsumePuntos2 = intConsumePuntos2;
   }

   public int getIntConsumePuntos3() {
      return intConsumePuntos3;
   }

   public void setIntConsumePuntos3(int intConsumePuntos3) {
      this.intConsumePuntos3 = intConsumePuntos3;
   }

   public int getIntConsumePuntos4() {
      return intConsumePuntos4;
   }

   public void setIntConsumePuntos4(int intConsumePuntos4) {
      this.intConsumePuntos4 = intConsumePuntos4;
   }

   public int getIntHijos() {
      return intHijos;
   }

   public void setIntHijos(int intHijos) {
      this.intHijos = intHijos;
   }

   public int getIntNietos() {
      return intNietos;
   }

   public void setIntNietos(int intNietos) {
      this.intNietos = intNietos;
   }

   public int getIntBisnietos() {
      return intBisnietos;
   }

   public void setIntBisnietos(int intBisnietos) {
      this.intBisnietos = intBisnietos;
   }

   public int getIntTataranietos() {
      return intTataranietos;
   }

   public void setIntTataranietos(int intTataranietos) {
      this.intTataranietos = intTataranietos;
   }

   public int getIntHijosCiclo1() {
      return intHijosCiclo1;
   }

   public void setIntHijosCiclo1(int intHijosCiclo1) {
      this.intHijosCiclo1 = intHijosCiclo1;
   }

   public int getIntNietosCiclo1() {
      return intNietosCiclo1;
   }

   public void setIntNietosCiclo1(int intNietosCiclo1) {
      this.intNietosCiclo1 = intNietosCiclo1;
   }

   public int getIntBisnietosCiclo1() {
      return intBisnietosCiclo1;
   }

   public void setIntBisnietosCiclo1(int intBisnietosCiclo1) {
      this.intBisnietosCiclo1 = intBisnietosCiclo1;
   }

   public int getIntTataranietosCiclo1() {
      return intTataranietosCiclo1;
   }

   public void setIntTataranietosCiclo1(int intTataranietosCiclo1) {
      this.intTataranietosCiclo1 = intTataranietosCiclo1;
   }

   public int getIntHijosCiclo2() {
      return intHijosCiclo2;
   }

   public void setIntHijosCiclo2(int intHijosCiclo2) {
      this.intHijosCiclo2 = intHijosCiclo2;
   }

   public int getIntNietosCiclo2() {
      return intNietosCiclo2;
   }

   public void setIntNietosCiclo2(int intNietosCiclo2) {
      this.intNietosCiclo2 = intNietosCiclo2;
   }

   public int getIntBisnietosCiclo2() {
      return intBisnietosCiclo2;
   }

   public void setIntBisnietosCiclo2(int intBisnietosCiclo2) {
      this.intBisnietosCiclo2 = intBisnietosCiclo2;
   }

   public int getIntTataranietosCiclo2() {
      return intTataranietosCiclo2;
   }

   public void setIntTataranietosCiclo2(int intTataranietosCiclo2) {
      this.intTataranietosCiclo2 = intTataranietosCiclo2;
   }

   public int getIntHijosCiclo3() {
      return intHijosCiclo3;
   }

   public void setIntHijosCiclo3(int intHijosCiclo3) {
      this.intHijosCiclo3 = intHijosCiclo3;
   }

   public int getIntNietosCiclo3() {
      return intNietosCiclo3;
   }

   public void setIntNietosCiclo3(int intNietosCiclo3) {
      this.intNietosCiclo3 = intNietosCiclo3;
   }

   public int getIntBisnietosCiclo3() {
      return intBisnietosCiclo3;
   }

   public void setIntBisnietosCiclo3(int intBisnietosCiclo3) {
      this.intBisnietosCiclo3 = intBisnietosCiclo3;
   }

   public int getIntTataranietosCiclo3() {
      return intTataranietosCiclo3;
   }

   public void setIntTataranietosCiclo3(int intTataranietosCiclo3) {
      this.intTataranietosCiclo3 = intTataranietosCiclo3;
   }

   public int getIntHijosCiclo4() {
      return intHijosCiclo4;
   }

   public void setIntHijosCiclo4(int intHijosCiclo4) {
      this.intHijosCiclo4 = intHijosCiclo4;
   }

   public int getIntNietosCiclo4() {
      return intNietosCiclo4;
   }

   public void setIntNietosCiclo4(int intNietosCiclo4) {
      this.intNietosCiclo4 = intNietosCiclo4;
   }

   public int getIntBisnietosCiclo4() {
      return intBisnietosCiclo4;
   }

   public void setIntBisnietosCiclo4(int intBisnietosCiclo4) {
      this.intBisnietosCiclo4 = intBisnietosCiclo4;
   }

   public int getIntTataranietosCiclo4() {
      return intTataranietosCiclo4;
   }

   public void setIntTataranietosCiclo4(int intTataranietosCiclo4) {
      this.intTataranietosCiclo4 = intTataranietosCiclo4;
   }

   public int getIntHijosCiclo5() {
      return intHijosCiclo5;
   }

   public void setIntHijosCiclo5(int intHijosCiclo5) {
      this.intHijosCiclo5 = intHijosCiclo5;
   }

   public int getIntNietosCiclo5() {
      return intNietosCiclo5;
   }

   public void setIntNietosCiclo5(int intNietosCiclo5) {
      this.intNietosCiclo5 = intNietosCiclo5;
   }

   public int getIntBisnietosCiclo5() {
      return intBisnietosCiclo5;
   }

   public void setIntBisnietosCiclo5(int intBisnietosCiclo5) {
      this.intBisnietosCiclo5 = intBisnietosCiclo5;
   }

   public int getIntTataranietosCiclo5() {
      return intTataranietosCiclo5;
   }

   public void setIntTataranietosCiclo5(int intTataranietosCiclo5) {
      this.intTataranietosCiclo5 = intTataranietosCiclo5;
   }

   public int getIntPlanOro() {
      return intPlanOro;
   }

   public void setIntPlanOro(int intPlanOro) {
      this.intPlanOro = intPlanOro;
   }

   public int getIntCicloActual() {
      return intCicloActual;
   }

   public void setIntCicloActual(int intCicloActual) {
      this.intCicloActual = intCicloActual;
   }

   public int getIntCicloCerrado() {
      return intCicloCerrado;
   }

   public void setIntCicloCerrado(int intCicloCerrado) {
      this.intCicloCerrado = intCicloCerrado;
   }

   public boolean isBolCierraCiclo() {
      return bolCierraCiclo;
   }

   public void setBolCierraCiclo(boolean bolCierraCiclo) {
      this.bolCierraCiclo = bolCierraCiclo;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
