/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes.entities;

import java.util.ArrayList;

/**
 * Representa una serie de un dashboard
 *
 * @author ZeusGalindo
 */
public class DashBoardSerie {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private ArrayList<String> lstNomCategorias;
   private ArrayList<Double> lstValorSeries;
   private ArrayList<Integer> lstValorSeriesInt;
   private int intIdGrafica = 0;
   private String strNomSerie;

   public ArrayList<String> getLstNomCategorias() {
      return lstNomCategorias;
   }

   public void setLstNomCategorias(ArrayList<String> lstNomCategorias) {
      this.lstNomCategorias = lstNomCategorias;
   }

   public ArrayList<Double> getLstValorSeries() {
      return lstValorSeries;
   }

   public void setLstValorSeries(ArrayList<Double> lstValorSeries) {
      this.lstValorSeries = lstValorSeries;
   }

   public ArrayList<Integer> getLstValorSeriesInt() {
      return lstValorSeriesInt;
   }

   public void setLstValorSeriesInt(ArrayList<Integer> lstValorSeriesInt) {
      this.lstValorSeriesInt = lstValorSeriesInt;
   }

   public int getIntIdGrafica() {
      return intIdGrafica;
   }

   public void setIntIdGrafica(int intIdGrafica) {
      this.intIdGrafica = intIdGrafica;
   }

   public String getStrNomSerie() {
      return strNomSerie;
   }

   public void setStrNomSerie(String strNomSerie) {
      this.strNomSerie = strNomSerie;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public DashBoardSerie() {
      lstNomCategorias = new ArrayList<String>();
      lstValorSeries = new ArrayList<Double>();
      lstValorSeriesInt = new ArrayList<Integer>();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
