package comSIWeb.Utilerias;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Esta clase nos ayuda a formatear numeros en texto
 *
 * @author zeus
 */
public class NumberString {

   /**
    * En este metodo formatea un numero con los decimales especificados
    *
    * @param dblNumero Numero a formatear
    * @param intNumDecimales Numero de decimales
    * @return Retorna el campo formateado
    */
   public static String FormatearDecimal(double dblNumero, int intNumDecimales) {
      //Obtenemos la cadena con el numero de posiciones
      String strPosiciones = "";
      //Validamos si el numero de decimales es mayor a cero
      if (intNumDecimales > 0) {
         strPosiciones = ".";
         for (int i = 0; i < intNumDecimales; i++) {
            strPosiciones += "0";
         }
      }
      //Instanciamos el objeto de decimal format
      Locale loc = new Locale("es", "MX");
      DecimalFormatSymbols symbol = new DecimalFormatSymbols(loc);
      DecimalFormat df1 = new DecimalFormat("#,###,###,##0" + strPosiciones, symbol);
      return df1.format(dblNumero);
   }
   /**
    * En este metodo formatea un numero con los decimales especificados
    *
    * @param dblNumero Numero a formatear
    * @param intNumDecimales Numero de decimales
    * @return Retorna el campo formateado
    */
   public  String FormatearDecimaln(double dblNumero, int intNumDecimales) {
      //Obtenemos la cadena con el numero de posiciones
      String strPosiciones = "";
      //Validamos si el numero de decimales es mayor a cero
      if (intNumDecimales > 0) {
         strPosiciones = ".";
         for (int i = 0; i < intNumDecimales; i++) {
            strPosiciones += "0";
         }
      }
      //Instanciamos el objeto de decimal format
      Locale loc = new Locale("es", "MX");
      DecimalFormatSymbols symbol = new DecimalFormatSymbols(loc);
      DecimalFormat df1 = new DecimalFormat("#,###,###,##0" + strPosiciones, symbol);
      return df1.format(dblNumero);
   }

   /**
    * Nos regresa el numero en letras
    *
    * @param dblNumber Es el numero
    * @param strMoneda Es la moneda
    * @return Nos regresa una cadena con el numero expresado en letras
    */
   public static String NumeroenTexto(double dblNumber, String strMoneda) {
      String strLetras = "";
      StringofNumber toStringNumber = new StringofNumber();
      toStringNumber.setNombreMoneda(strMoneda);
      strLetras = toStringNumber.getStringOfNumber(dblNumber);
      return strLetras;
   }

   /**
    *Redondea los decimales de acuerdo al estandard de los dolares, menor de 5 pasa a 0,
    * mayor a 5 para al siguiente numero, 5 se queda
    * @param dblNumero Es el numero a redondear
    * @return Regresa el numero redondeado
    */
   public double RedondeoDolares(double dblNumero) {
      double dblRedondeo = 0;
      if (dblNumero >= 10.0 && dblNumero <= 50.0) {
         double result = dblNumero;
         String[] numeros = String.valueOf(result).split("\\.");
         int intEntero = 0;
         long lngDecimal = 0;
         try {
            intEntero = Integer.valueOf(numeros[0]);
            lngDecimal = Integer.valueOf(numeros[1].substring(0, 2));
         } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Fuera del indice ...." + ex.getMessage());
         } catch (NumberFormatException ex) {
            System.out.println("Formato de numero ...." + ex.getMessage());
         }
         if (lngDecimal <= 24) {
            lngDecimal = 0;
         } else {
            if (lngDecimal >= 25 && lngDecimal <= 74) {
               lngDecimal = 50;
            }else{
               if (lngDecimal >= 75) {
                  lngDecimal = 0;
                  intEntero++;
               }
            }
         }
         result = Double.valueOf(String.valueOf(intEntero + "." + lngDecimal));

         dblRedondeo = result;
      }
      if(dblNumero <10.0)
      {
          dblRedondeo = dblNumero;
      }
      if(dblNumero > 50.0)
      {
          dblRedondeo = RedondeoPesos(dblNumero);
      }
      return dblRedondeo;
   }
   /**
    *Redondea los decimales de acuerdo al estandard de los pesos mexicanos, menor de 5 pasa a 0,
    * mayor a 5 para al siguiente numero, 5 se queda
    * @param dblNumero Es el numero a redondear
    * @return Regresa el numero redondeado
    */
   public double RedondeoPesos(double dblNumero) {
      double dblRedondeo = 0;
      if (dblNumero != 0) {
         double result = dblNumero;
         String[] numeros = String.valueOf(result).split("\\.");
         int intEntero = 0;
         long lngDecimal = 0;
         try {
            intEntero = Integer.valueOf(numeros[0]);
            lngDecimal = Integer.valueOf(numeros[1].substring(0, 2));
         } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Fuera del indice ...." + ex.getMessage());
         } catch (NumberFormatException ex) {
            System.out.println("Formato de numero ...." + ex.getMessage());
         }
         if (lngDecimal < 50) {
            lngDecimal = 0;
         } else {
            if (lngDecimal >= 51) {
               lngDecimal = 0;
               intEntero++;
            }
         }
         result = Double.valueOf(String.valueOf(intEntero + "." + lngDecimal));

         dblRedondeo = result;
      }
      return dblRedondeo;
   }

}
