/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

/**
 *
 * @author N4v1d4d3s
 */
public class DetalleInventario {

   private String strCodigo;
   private String strDescripcion;
   private String strFamilia;
   private String strUnidad;
   private String strSucursal;
   private double dbInicial;
   private double dbEntradas;
   private double dbSalidas;
   private double dbFinal;
   private int intMovimientos;
   private double dbCantidadVendida;
   private double dbCostoUnitario;
   private double dbPrecioVenta;
   private double dbCostoTotal;
   private double dblVentaTotal;
    private double dbInicialC;
    private double dbEntradasC;
    private double dbSalidasC;
    private double dbFinalC;

    public double getDbInicialC() {
        return dbInicialC;
    }

    public void setDbInicialC(double dbInicialC) {
        this.dbInicialC = dbInicialC;
    }

    public double getDbEntradasC() {
        return dbEntradasC;
    }

    public void setDbEntradasC(double dbEntradasC) {
        this.dbEntradasC = dbEntradasC;
    }

    public double getDbSalidasC() {
        return dbSalidasC;
    }

    public void setDbSalidasC(double dbSalidasC) {
        this.dbSalidasC = dbSalidasC;
    }

    public double getDbFinalC() {
        return dbFinalC;
    }

    public void setDbFinalC(double dbFinalC) {
        this.dbFinalC = dbFinalC;
    }

   public String getStrCodigo() {
      return strCodigo;
   }

   public String getStrSucursal() {
      return strSucursal;
   }

   public void setStrSucursal(String strSucursal) {
      this.strSucursal = strSucursal;
   }

   public void setStrCodigo(String strCodigo) {
      this.strCodigo = strCodigo;
   }

   public String getStrDescripcion() {
      return strDescripcion;
   }

   public void setStrDescripcion(String strDescripcion) {
      this.strDescripcion = strDescripcion;
   }

   public String getStrFamilia() {
      return strFamilia;
   }

   public void setStrFamilia(String strFamilia) {
      this.strFamilia = strFamilia;
   }

   public String getStrUnidad() {
      return strUnidad;
   }

   public void setStrUnidad(String strUnidad) {
      this.strUnidad = strUnidad;
   }

   public double getDbInicial() {
      return dbInicial;
   }

   public void setDbInicial(double dbInicial) {
      this.dbInicial = dbInicial;
   }

   public double getDbEntradas() {
      return dbEntradas;
   }

   public void setDbEntradas(double dbEntradas) {
      this.dbEntradas = dbEntradas;
   }

   public double getDbSalidas() {
      return dbSalidas;
   }

   public void setDbSalidas(double dbSalidas) {
      this.dbSalidas = dbSalidas;
   }

   public double getDbFinal() {
      return dbFinal;
   }

   public void setDbFinal(double dbFinal) {
      this.dbFinal = dbFinal;
   }

   public int getIntMovimientos() {
      return intMovimientos;
   }

   public void setIntMovimientos(int intMovimientos) {
      this.intMovimientos = intMovimientos;
   }

   public double getDbCantidadVendida() {
      return dbCantidadVendida;
   }

   public void setDbCantidadVendida(double dbCantidadVendida) {
      this.dbCantidadVendida = dbCantidadVendida;
   }

   public double getDbCostoUnitario() {
      return dbCostoUnitario;
   }

   public void setDbCostoUnitario(double dbCostoUnitario) {
      this.dbCostoUnitario = dbCostoUnitario;
   }

   public double getDbPrecioVenta() {
      return dbPrecioVenta;
   }

   public void setDbPrecioVenta(double dbPrecioVenta) {
      this.dbPrecioVenta = dbPrecioVenta;
   }

   public double getDbCostoTotal() {
      return dbCostoTotal;
   }

   public void setDbCostoTotal(double dbCostoTotal) {
      this.dbCostoTotal = dbCostoTotal;
   }

   public double getDblVentaTotal() {
      return dblVentaTotal;
   }

   public void setDblVentaTotal(double dblVentaTotal) {
      this.dblVentaTotal = dblVentaTotal;
   }

}