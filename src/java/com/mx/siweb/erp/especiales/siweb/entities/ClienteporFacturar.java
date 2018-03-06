/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.siweb.entities;

/**
 * Representa los clientes por facturar
 *
 * @author ZeusGalindo
 */
public class ClienteporFacturar {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   int intNum = 0;
   int intNumContrato = 0;
   int intMonedaId = 0;
   int intConsecutivoFactura = 0;
    String strMoneda;
   String strNombre;
    String strArrenda;
   String strContrato;
   String strResultado;
    String strMes;
    double mes1;
    double mes2;
    double mes3;
    double mes4;
    double mes5;
    double mes6;
    double mes7;
    double mes8;
    double mes9;
    double mes10;
    double mes11;
    double mes12;
    double mto_arrendamiento;

   ContratoporFacturar[] lstContratosAnual;

    public String getStrArrenda() {
        return strArrenda;
    }

    public void setStrArrenda(String strArrenda) {
        this.strArrenda = strArrenda;
    }
    
    public String getStrMes() {
        return strMes;
    }

    public void setStrMes(String strMes) {
        this.strMes = strMes;
    }
    
    public String getStrMoneda() {
        return strMoneda;
    }

    public void setStrMoneda(String strMoneda) {
        this.strMoneda = strMoneda;
    }

   public String getStrContrato() {
      return strContrato;
   }

   public void setStrContrato(String strContrato) {
      this.strContrato = strContrato;
   }

   public int getIntNum() {
      return intNum;
   }

   public void setIntNum(int intNum) {
      this.intNum = intNum;
   }

   public String getStrNombre() {
      return strNombre;
   }

   public void setStrNombre(String strNombre) {
      this.strNombre = strNombre;
   }

   public ContratoporFacturar[] getLstContratosAnual() {
      return lstContratosAnual;
   }

   public void setLstContratosAnual(ContratoporFacturar[] lstContratosAnual) {
      this.lstContratosAnual = lstContratosAnual;
   }

   public int getIntNumContrato() {
      return intNumContrato;
   }

   public void setIntNumContrato(int intNumContrato) {
      this.intNumContrato = intNumContrato;
   }

   public String getStrResultado() {
      return strResultado;
   }

   public void setStrResultado(String strResultado) {
      this.strResultado = strResultado;
   }

   public int getIntMonedaId() {
      return intMonedaId;
   }

   public void setIntMonedaId(int intMonedaId) {
      this.intMonedaId = intMonedaId;
   }

   public int getIntConsecutivoFactura() {
      return intConsecutivoFactura;
   }

   public void setIntConsecutivoFactura(int intConsecutivoFactura) {
      this.intConsecutivoFactura = intConsecutivoFactura;
   }

    public void Setmot_Arrendamiento(double mto_arrendamiento) {
        this.mto_arrendamiento = mto_arrendamiento;
    }

    public double getMot_Arrendamiento() {
        return mto_arrendamiento;
    }
    
    public void setMes1(double mto_arrendamiento){
        this.mes1 = mto_arrendamiento;        
    }
    
    public double getMes1(){
        return mes1;
    }
    
    public void setMes2(double mto_arrendamiento){
        this.mes2 = mto_arrendamiento;        
    }
    
    public double getMes2(){
        return mes2;
    }
    
    public void setMes3(double mto_arrendamiento){
        this.mes3 = mto_arrendamiento;        
    }
    
    public double getMes3(){
        return mes3;
    }
    
    public void setMes4(double mto_arrendamiento){
        this.mes4 = mto_arrendamiento;        
    }
    
    public double getMes4(){
        return mes4;
    }
    
    public void setMes5(double mto_arrendamiento){
        this.mes5 = mto_arrendamiento;        
    }
    
    public double getMes5(){
        return mes5;
    }
    
    public void setMes6(double mto_arrendamiento){
        this.mes6 = mto_arrendamiento;        
    }
    
    public double getMes6(){
        return mes6;
    }
    
    public void setMes7(double mto_arrendamiento){
        this.mes7 = mto_arrendamiento;        
    }
    
    public double getMes7(){
        return mes7;
    }
    
    public void setMes8(double mto_arrendamiento){
        this.mes8 = mto_arrendamiento;        
    }
    
    public double getMes8(){
        return mes8;
    }
    
    public void setMes9(double mto_arrendamiento){
        this.mes9 = mto_arrendamiento;        
    }
    
    public double getMes9(){
        return mes9;
    }
    
    public void setMes10(double mto_arrendamiento){
        this.mes10 = mto_arrendamiento;        
    }
    
    public double getMes10(){
        return mes10;
    }
    
    public void setMes11(double mto_arrendamiento){
        this.mes11 = mto_arrendamiento;        
    }
    
    public double getMes11(){
        return mes11;
    }
    
    public void setMes12(double mto_arrendamiento){
        this.mes12 = mto_arrendamiento;        
    }
    
    public double getMes12(){
        return mes12;
    }
    

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ClienteporFacturar() {
      lstContratosAnual = new ContratoporFacturar[12];
      for (int i = 0; i < 12; i++) {
         ContratoporFacturar contrato = new ContratoporFacturar();
         contrato.dblImporte = 0;
         lstContratosAnual[i] = contrato;
      }
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
