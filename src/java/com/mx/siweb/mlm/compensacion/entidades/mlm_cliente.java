/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.entidades;

import java.util.ArrayList;

/**
 * Entidad que representa un cliente para el calculo de comisiones
 *
 * @author aleph_79
 */
public class mlm_cliente {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intCliente = 0;
   private double dblPuntos = 0;
   private double dblNegocio = 0;
   private double dblGPuntos = 0;
   private double dblGNegocio = 0;
   private int intNivelRed = 0;
   private int intNivelRedAnterior = 0;
   private int intUpline = 0;
   private double dblValorDbl1 = 0;
   private double dblValorDbl2 = 0;
   private double dblValorDbl3 = 0;
   private double dblValorDbl4 = 0;
   private double dblValorDbl5 = 0;
   private double dblValorDbl6 = 0;
   private double dblValorDbl7 = 0;
   private double dblValorDbl8 = 0;
   private double dblValorDbl9 = 0;
   private double dblValorDbl10 = 0;
   private int intSponsor = 0;
   private int intTotalHijos = 0;
   private int intTotalHijosActivos = 0;
   private String strNombre;
   private double dblComision = 0;
   private int intSC_ID = 0;
   private int intTipoPago = 0;
   private int intValor1 = 0;
   private int intValor2 = 0;
   private int intValor3 = 0;
   private int intValor4 = 0;
   private int intValor5 = 0;
   private int intValor6 = 0;
   private int intValor7 = 0;
   private int intValor8 = 0;
   private int intValor9 = 0;
   private int intValor10 = 0;
   private int intArmadoIni = 0;
   private int intArmadoFin = 0;
   private int intArmadoNum = 0;
   private int intArmadoDeep = 0;
   private int intPeriodoIngreso = 0;
   ArrayList<mlm_comision_deta> lstDeta = null;

   /**
    *Regresa el listado de las comisiones a detalle
    * @return
    */
   public ArrayList<mlm_comision_deta> getLstDeta() {
      if (lstDeta == null) {
         lstDeta = new ArrayList<mlm_comision_deta>();
      }
      return lstDeta;
   }

   /**
    *Define el listado de comisiones a detalle
    * @param lstDeta Es un arraylist con objetos de tipo comision detalle
    */
   public void setLstDeta(ArrayList<mlm_comision_deta> lstDeta) {
      this.lstDeta = lstDeta;
   }

   /**
    *Regresa el valor negocio
    * @return Es un valor de tipo double
    */
   public double getDblNegocio() {
      return dblNegocio;
   }

   /**
    *Define el valor negocio
    * @param dblNegocio Define un valor de tipo double
    */
   public void setDblNegocio(double dblNegocio) {
      this.dblNegocio = dblNegocio;
   }

   public double getDblPuntos() {
      return dblPuntos;
   }

   public void setDblPuntos(double dblPuntos) {
      this.dblPuntos = dblPuntos;
   }

   public int getIntCliente() {
      return intCliente;
   }

   public void setIntCliente(int intCliente) {
      this.intCliente = intCliente;
   }

   public double getDblGNegocio() {
      return dblGNegocio;
   }

   public void setDblGNegocio(double dblGNegocio) {
      this.dblGNegocio = dblGNegocio;
   }

   public double getDblGPuntos() {
      return dblGPuntos;
   }

   public void setDblGPuntos(double dblGPuntos) {
      this.dblGPuntos = dblGPuntos;
   }

   public double getDblValorDbl1() {
      return dblValorDbl1;
   }

   public void setDblValorDbl1(double dblValorDbl1) {
      this.dblValorDbl1 = dblValorDbl1;
   }

   public double getDblValorDbl10() {
      return dblValorDbl10;
   }

   public void setDblValorDbl10(double dblValorDbl10) {
      this.dblValorDbl10 = dblValorDbl10;
   }

   public double getDblValorDbl2() {
      return dblValorDbl2;
   }

   public void setDblValorDbl2(double dblValorDbl2) {
      this.dblValorDbl2 = dblValorDbl2;
   }

   public double getDblValorDbl3() {
      return dblValorDbl3;
   }

   public void setDblValorDbl3(double dblValorDbl3) {
      this.dblValorDbl3 = dblValorDbl3;
   }

   public double getDblValorDbl4() {
      return dblValorDbl4;
   }

   public void setDblValorDbl4(double dblValorDbl4) {
      this.dblValorDbl4 = dblValorDbl4;
   }

   public double getDblValorDbl5() {
      return dblValorDbl5;
   }

   public void setDblValorDbl5(double dblValorDbl5) {
      this.dblValorDbl5 = dblValorDbl5;
   }

   public double getDblValorDbl6() {
      return dblValorDbl6;
   }

   public void setDblValorDbl6(double dblValorDbl6) {
      this.dblValorDbl6 = dblValorDbl6;
   }

   public double getDblValorDbl7() {
      return dblValorDbl7;
   }

   public void setDblValorDbl7(double dblValorDbl7) {
      this.dblValorDbl7 = dblValorDbl7;
   }

   public double getDblValorDbl8() {
      return dblValorDbl8;
   }

   public void setDblValorDbl8(double dblValorDbl8) {
      this.dblValorDbl8 = dblValorDbl8;
   }

   public double getDblValorDbl9() {
      return dblValorDbl9;
   }

   public void setDblValorDbl9(double dblValorDbl9) {
      this.dblValorDbl9 = dblValorDbl9;
   }

   public int getIntNivelRed() {
      return intNivelRed;
   }

   public void setIntNivelRed(int intNivelRed) {
      this.intNivelRed = intNivelRed;
   }

   public int getIntUpline() {
      return intUpline;
   }

   public void setIntUpline(int intUpline) {
      this.intUpline = intUpline;
   }

   public int getIntSponsor() {
      return intSponsor;
   }

   public void setIntSponsor(int intSponsor) {
      this.intSponsor = intSponsor;
   }

   public int getIntTotalHijos() {
      return intTotalHijos;
   }

   public void setIntTotalHijos(int intTotalHijos) {
      this.intTotalHijos = intTotalHijos;
   }

   public int getIntTotalHijosActivos() {
      return intTotalHijosActivos;
   }

   public void setIntTotalHijosActivos(int intTotalHijosActivos) {
      this.intTotalHijosActivos = intTotalHijosActivos;
   }

   public double getDblComision() {
      return dblComision;
   }

   public void setDblComision(double dblComision) {
      this.dblComision = dblComision;
   }

   public String getStrNombre() {
      return strNombre;
   }

   public void setStrNombre(String strNombre) {
      this.strNombre = strNombre;
   }

   public int getIntSC_ID() {
      return intSC_ID;
   }

   public void setIntSC_ID(int intSC_ID) {
      this.intSC_ID = intSC_ID;
   }

   public int getIntTipoPago() {
      return intTipoPago;
   }

   public void setIntTipoPago(int intTipoPago) {
      this.intTipoPago = intTipoPago;
   }

   public int getIntValor1() {
      return intValor1;
   }

   public void setIntValor1(int intValor1) {
      this.intValor1 = intValor1;
   }

   public int getIntValor2() {
      return intValor2;
   }

   public void setIntValor2(int intValor2) {
      this.intValor2 = intValor2;
   }

   public int getIntValor3() {
      return intValor3;
   }

   public void setIntValor3(int intValor3) {
      this.intValor3 = intValor3;
   }

   public int getIntValor4() {
      return intValor4;
   }

   public void setIntValor4(int intValor4) {
      this.intValor4 = intValor4;
   }

   public int getIntValor5() {
      return intValor5;
   }

   public void setIntValor5(int intValor5) {
      this.intValor5 = intValor5;
   }

   public int getIntValor6() {
      return intValor6;
   }

   public void setIntValor6(int intValor6) {
      this.intValor6 = intValor6;
   }

   public int getIntValor7() {
      return intValor7;
   }

   public void setIntValor7(int intValor7) {
      this.intValor7 = intValor7;
   }

   public int getIntValor8() {
      return intValor8;
   }

   public void setIntValor8(int intValor8) {
      this.intValor8 = intValor8;
   }

   public int getIntValor9() {
      return intValor9;
   }

   public void setIntValor9(int intValor9) {
      this.intValor9 = intValor9;
   }

   public int getIntValor10() {
      return intValor10;
   }

   public void setIntValor10(int intValor10) {
      this.intValor10 = intValor10;
   }

   public int getIntNivelRedAnterior() {
      return intNivelRedAnterior;
   }

   public void setIntNivelRedAnterior(int intNivelRedAnterior) {
      this.intNivelRedAnterior = intNivelRedAnterior;
   }

   public int getIntArmadoIni() {
      return intArmadoIni;
   }

   public void setIntArmadoIni(int intArmadoIni) {
      this.intArmadoIni = intArmadoIni;
   }

   public int getIntArmadoFin() {
      return intArmadoFin;
   }

   public void setIntArmadoFin(int intArmadoFin) {
      this.intArmadoFin = intArmadoFin;
   }

   public int getIntArmadoNum() {
      return intArmadoNum;
   }

   public void setIntArmadoNum(int intArmadoNum) {
      this.intArmadoNum = intArmadoNum;
   }

   public int getIntArmadoDeep() {
      return intArmadoDeep;
   }

   public void setIntArmadoDeep(int intArmadoDeep) {
      this.intArmadoDeep = intArmadoDeep;
   }

   /**
    * Regresa el id del periodo de ingreso
    *
    * @return Es un entero con el id del periodo de ingreso
    */
   public int getIntPeriodoIngreso() {
      return intPeriodoIngreso;
   }

   /**
    * Es el id del periodo de ingreso
    *
    * @param intPeriodoIngreso Es un entero con el id del periodo de ingreso
    */
   public void setIntPeriodoIngreso(int intPeriodoIngreso) {
      this.intPeriodoIngreso = intPeriodoIngreso;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
