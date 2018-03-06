/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siweb.test;

/**
 *
 * @author ZeusGalindo
 */
public class GoogleUtils {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   double logrados;
   double lominutos;
   double losegundos;
   String loposicion = "S";
   double lagrados;
   double laminutos;
   double lasegundos;
   String laposicion = "N";
   double lodecimales;
   double loradianes;
   double ladecimales;
   double laradianes;
   double X, Y;
   double dLatitud;
   double dLongitu;

   public double getLogrados() {
      return logrados;
   }

   public void setLogrados(double logrados) {
      this.logrados = logrados;
   }

   public double getLominutos() {
      return lominutos;
   }

   public void setLominutos(double lominutos) {
      this.lominutos = lominutos;
   }

   public double getLosegundos() {
      return losegundos;
   }

   public void setLosegundos(double losegundos) {
      this.losegundos = losegundos;
   }

   public String getLoposicion() {
      return loposicion;
   }

   public void setLoposicion(String loposicion) {
      this.loposicion = loposicion;
   }

   public double getLagrados() {
      return lagrados;
   }

   public void setLagrados(double lagrados) {
      this.lagrados = lagrados;
   }

   public double getLaminutos() {
      return laminutos;
   }

   public void setLaminutos(double laminutos) {
      this.laminutos = laminutos;
   }

   public double getLasegundos() {
      return lasegundos;
   }

   public void setLasegundos(double lasegundos) {
      this.lasegundos = lasegundos;
   }

   public String getLaposicion() {
      return laposicion;
   }

   public void setLaposicion(String laposicion) {
      this.laposicion = laposicion;
   }

   public double getLodecimales() {
      return lodecimales;
   }

   public double getLoradianes() {
      return loradianes;
   }

   public double getLadecimales() {
      return ladecimales;
   }

   public double getLaradianes() {
      return laradianes;
   }

   public double getX() {
      return X;
   }

   public double getY() {
      return Y;
   }

   public double getdLatitud() {
      return dLatitud;
   }

   public void setdLatitud(double dLatitud) {
      this.dLatitud = dLatitud;
   }

   public double getdLongitu() {
      return dLongitu;
   }

   public void setdLongitu(double dLongitu) {
      this.dLongitu = dLongitu;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public String convertirGradosMinutosSegundos(String strCoordenada) {
      System.out.println("strCoordenada:" + strCoordenada);
      String strPosGoogle = "";
      //N 30°53'15.84"  W 108°11'28.73"
      int intIdx1 = strCoordenada.indexOf("W");
      //Latitud
      String strLat = strCoordenada.substring(0, intIdx1);
      strLat = strLat.replace("N", "");
      int intIdx2 = strLat.indexOf("°");
      String strGrado = strLat.substring(0, intIdx2).replace(" ", "").replace(" ", "");
      int intIdx3 = strLat.indexOf("'");
      String strMinuto = strLat.substring(intIdx2 + 1, intIdx3).replace(" ", "");
      String strSegundo = strLat.substring(intIdx3).replace("'", "").replace(" ", "").replace(" ", "");
      //Longitud
      String strLong = strCoordenada.substring(intIdx1, strCoordenada.length());
      strLong = strLong.replace("W", "");
      intIdx2 = strLong.indexOf("°");
      String strGrado2 = strLong.substring(0, intIdx2).replace(" ", "");
      intIdx3 = strLong.indexOf("'");
      String strMinuto2 = strLong.substring(intIdx2 + 1, intIdx3).replace(" ", "");
      String strSegundo2 = strLong.substring(intIdx3).replace("'", "").replace(" ", "");
      //Longitud

      System.out.println("Latitud:" + strLat + " grado:" + strGrado + " Minuto:" + strMinuto + " Segundo:" + strSegundo);
      System.out.println("Longitud" + strLong + " grado:" + strGrado2 + " Minuto:" + strMinuto2 + " Segundo:" + strSegundo2);
      try {
         int intGrado1 = Integer.valueOf(strGrado);
         int intMinuto1 = Integer.valueOf(strMinuto);
         double dblSegundo1 = Double.valueOf(strSegundo);
         int intGrado2 = Integer.valueOf(strGrado2);
         int intMinuto2 = Integer.valueOf(strMinuto2);
         double dblSegundo2 = Double.valueOf(strSegundo2);
         this.setLagrados(intGrado1);
         this.setLaminutos(intMinuto1);
         this.setLasegundos(dblSegundo1);
         this.setLaposicion("N");

         this.setLogrados(intGrado2);
         this.setLominutos(intMinuto2);
         this.setLosegundos(dblSegundo2);
         this.setLoposicion("W");

         this.calculo();
          strPosGoogle = this.getLadecimales() + "," + this.getLodecimales();
         System.out.println("gOOGLE pOS " + strPosGoogle);
      } catch (NumberFormatException ex) {
         System.out.println("********Fallo en la conversion****** " + ex.getMessage());
      } catch (Exception ex) {

      }
      return strPosGoogle;
   }

   public void calculo() {

      dLatitud = (((this.laminutos * 60) + this.lasegundos) / 3600.2621) + this.lagrados;
      dLongitu = (((this.lominutos * 60) + this.losegundos) / 3600.2621) + this.logrados;
      if (loposicion.equals("W")) {
         dLongitu = dLongitu * -1;
      }
      System.out.println("dLatitud->" + dLatitud);
      System.out.println("dLongitu->" + dLongitu);

//      jcoordenadax.setText(String.valueOf(formatter.format(X)));
//      jcoordenaday.setText(String.valueOf(formatter.format(Y)));
   }
   // </editor-fold>
}
