package com.mx.siweb.erp.reportes.entities;

import java.util.HashMap;

/**
 *Representa un cliente con 
 * @author zeus
 */
public class ClienteAnual {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   private int IdCliente;
   private String strNombre;
   private HashMap props;
   private HashMap map1;
   private HashMap map2;
   private HashMap map3;
   private HashMap map4;
   private HashMap map5;
   private HashMap map6;
   private HashMap map7;
   private HashMap map8;
   private HashMap map9;
   private HashMap map10;
   private HashMap map11;
   private HashMap map12;

   public int getIdCliente() {
      return IdCliente;
   }

   public void setIdCliente(int IdCliente) {
      this.IdCliente = IdCliente;
   }

   /**
    * Obtiene el mapa para enero
    * @return Regresa un hash Map
    */
   public HashMap getMap1() {
      return map1;
   }

   public void setMap1(HashMap map1) {
      this.map1 = map1;
   }

   public HashMap getMap10() {
      return map10;
   }

   public void setMap10(HashMap map10) {
      this.map10 = map10;
   }

   public HashMap getMap11() {
      return map11;
   }

   public void setMap11(HashMap map11) {
      this.map11 = map11;
   }

   public HashMap getMap12() {
      return map12;
   }

   public void setMap12(HashMap map12) {
      this.map12 = map12;
   }

   public HashMap getMap2() {
      return map2;
   }

   public void setMap2(HashMap map2) {
      this.map2 = map2;
   }

   public HashMap getMap3() {
      return map3;
   }

   public void setMap3(HashMap map3) {
      this.map3 = map3;
   }

   public HashMap getMap4() {
      return map4;
   }

   public void setMap4(HashMap map4) {
      this.map4 = map4;
   }

   public HashMap getMap5() {
      return map5;
   }

   public void setMap5(HashMap map5) {
      this.map5 = map5;
   }

   public HashMap getMap6() {
      return map6;
   }

   public void setMap6(HashMap map6) {
      this.map6 = map6;
   }

   public HashMap getMap7() {
      return map7;
   }

   public void setMap7(HashMap map7) {
      this.map7 = map7;
   }

   public HashMap getMap8() {
      return map8;
   }

   public void setMap8(HashMap map8) {
      this.map8 = map8;
   }

   public HashMap getMap9() {
      return map9;
   }

   public void setMap9(HashMap map9) {
      this.map9 = map9;
   }

   public HashMap getProps() {
      return props;
   }

   public void setProps(HashMap props) {
      this.props = props;
   }

   public String getStrNombre() {
      return strNombre;
   }

   public void setStrNombre(String strNombre) {
      this.strNombre = strNombre;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ClienteAnual() {
      IdCliente = 0;
      strNombre = "";
      props = new HashMap();
      map1 = new HashMap();
      map2 = new HashMap();
      map3 = new HashMap();
      map4 = new HashMap();
      map5 = new HashMap();
      map6 = new HashMap();
      map7 = new HashMap();
      map8 = new HashMap();
      map9 = new HashMap();
      map10 = new HashMap();
      map11 = new HashMap();
      map12 = new HashMap();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Obtiene el total por cliente
    * @return Regresa el total
    */
   public double getTotalCte(){
      double dblTotal = 0;
      if(this.map1.get("timporte") != null)dblTotal += Double.valueOf(this.map1.get("timporte").toString());
      if(this.map2.get("timporte") != null)dblTotal += Double.valueOf(this.map2.get("timporte").toString());
      if(this.map3.get("timporte") != null)dblTotal += Double.valueOf(this.map3.get("timporte").toString());
      if(this.map4.get("timporte") != null)dblTotal += Double.valueOf(this.map4.get("timporte").toString());
      if(this.map5.get("timporte") != null)dblTotal += Double.valueOf(this.map5.get("timporte").toString());
      if(this.map6.get("timporte") != null)dblTotal += Double.valueOf(this.map6.get("timporte").toString());
      if(this.map7.get("timporte") != null)dblTotal += Double.valueOf(this.map7.get("timporte").toString());
      if(this.map8.get("timporte") != null)dblTotal += Double.valueOf(this.map8.get("timporte").toString());
      if(this.map9.get("timporte") != null)dblTotal += Double.valueOf(this.map9.get("timporte").toString());
      if(this.map10.get("timporte") != null)dblTotal += Double.valueOf(this.map10.get("timporte").toString());
      if(this.map11.get("timporte") != null)dblTotal += Double.valueOf(this.map11.get("timporte").toString());
      if(this.map12.get("timporte") != null)dblTotal += Double.valueOf(this.map12.get("timporte").toString());
      return dblTotal;
   }
   
   /**
    * Obtiene el importe en el mes especificado
    * @param intMes Es el numero de mes
    * @return Regresa el importe
    */
   public double getImporteMes(int intMes){
      double dblImporte = 0;
      if(intMes == 1 && this.map1.get("timporte") != null)dblImporte = Double.valueOf(this.map1.get("timporte").toString());
      if(intMes == 2 && this.map2.get("timporte") != null)dblImporte = Double.valueOf(this.map2.get("timporte").toString());
      if(intMes == 3 && this.map3.get("timporte") != null)dblImporte = Double.valueOf(this.map3.get("timporte").toString());
      if(intMes == 4 && this.map4.get("timporte") != null)dblImporte = Double.valueOf(this.map4.get("timporte").toString());
      if(intMes == 5 && this.map5.get("timporte") != null)dblImporte = Double.valueOf(this.map5.get("timporte").toString());
      if(intMes == 6 && this.map6.get("timporte") != null)dblImporte = Double.valueOf(this.map6.get("timporte").toString());
      if(intMes == 7 && this.map7.get("timporte") != null)dblImporte = Double.valueOf(this.map7.get("timporte").toString());
      if(intMes == 8 && this.map8.get("timporte") != null)dblImporte = Double.valueOf(this.map8.get("timporte").toString());
      if(intMes == 9 && this.map9.get("timporte") != null)dblImporte = Double.valueOf(this.map9.get("timporte").toString());
      if(intMes == 10 && this.map10.get("timporte") != null)dblImporte = Double.valueOf(this.map10.get("timporte").toString());
      if(intMes == 11 && this.map11.get("timporte") != null)dblImporte = Double.valueOf(this.map11.get("timporte").toString());
      if(intMes == 12 && this.map12.get("timporte") != null)dblImporte = Double.valueOf(this.map12.get("timporte").toString());
      return dblImporte;
   }
// </editor-fold>
}
