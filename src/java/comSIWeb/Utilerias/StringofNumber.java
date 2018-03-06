package comSIWeb.Utilerias;

import comSIWeb.Utilerias.numeros_letras.NumerosLetras;
import comSIWeb.Utilerias.numeros_letras.NumerosLetrasMX;

/**
 * Nos genera la representacion de un numero en letra
 *
 * @author zeus
 */
public class StringofNumber {

   private Integer counter = 0;
   private String value;
   private String nombreDeMoneda;
   private String strLocate;
   private String strSimbolo;
   private NumerosLetras letras;

   /**
    * Constructor
    */
   public StringofNumber() {
      nombreDeMoneda = "PESOS";
      letras = new NumerosLetrasMX();
      value = "";
      nombreDeMoneda = "";
      strLocate = "";
      strSimbolo = "";
   }

   /**
    * Regresa la representacion en letra del numero
    *
    * @param $num Es el numero
    * @return Nos regresa una cadena
    */
   public String getStringOfNumber(Integer $num) {
      this.counter = $num;
      return doThings($num);
   }

   /**
    * Establece el nombre de la moneda
    *
    * @param nombre
    */
   public void setNombreMoneda(String nombre) {
      nombreDeMoneda = nombre;
   }

   public String getStrSimbolo() {
      return strSimbolo;
   }

   public void setStrSimbolo(String strSimbolo) {
      this.strSimbolo = strSimbolo;
   }

   /**
    * Con formato centavos/100MN
    *
    *
    * @param $num Es el numero
    * @return Regresa el numero en letras con decimales
    */
   public String getStringOfNumber(double $num) {
// this.counter = $num; 
      $num = Math.abs($num);
      int _counter = (int) $num;
      double resto = $num - _counter; //Almaceno la parte decimal
//Redondeo y convierto a entero puedo tener problemas
      long fraccion = Math.round(resto * 100);
      String strFraccion = fraccion + "";
      if (strFraccion.length() == 1) {
         strFraccion = "0" + strFraccion;
      }
      String strSimboloPost = this.strSimbolo;
      if (nombreDeMoneda.equals("PESOS")) {
         strSimboloPost = " M.N.";
         return doThings(_counter) + " " + nombreDeMoneda + " " + strFraccion + "/100 " + strSimboloPost;
      } else {
         //if (strFraccion.equals("00")) {
         //   return doThings(_counter) + " " + nombreDeMoneda + " " + strSimboloPost;
         //} else {
            return doThings(_counter) + " " + nombreDeMoneda + " " + strFraccion + "/100 " + strSimboloPost;
         //}
      }

   }

   private String doThings(Integer _counter) {
//Limite
      if (_counter > 100000000) {
         return "CIEN MILLONES";
      }

      switch (_counter) {
         case 0:
            return "CERO";
         case 1:
            return "UN"; //UNO
         case 2:
            return "DOS";
         case 3:
            return "TRES";
         case 4:
            return "CUATRO";
         case 5:
            return "CINCO";
         case 6:
            return "SEIS";
         case 7:
            return "SIETE";
         case 8:
            return "OCHO";
         case 9:
            return "NUEVE";
         case 10:
            return "DIEZ";
         case 11:
            return "ONCE";
         case 12:
            return "DOCE";
         case 13:
            return "TRECE";
         case 14:
            return "CATORCE";
         case 15:
            return "QUINCE";
         case 20:
            return "VEINTE";
         case 30:
            return "TREINTA";
         case 40:
            return "CUARENTA";
         case 50:
            return "CINCUENTA";
         case 60:
            return "SESENTA";
         case 70:
            return "SETENTA";
         case 80:
            return "OCHENTA";
         case 90:
            return "NOVENTA";
         case 100:
            return "CIEN";

         case 200:
            return "DOSCIENTOS";
         case 300:
            return "TRESCIENTOS";
         case 400:
            return "CUATROCIENTOS";
         case 500:
            return "QUINIENTOS";
         case 600:
            return "SEISCIENTOS";
         case 700:
            return "SETECIENTOS";
         case 800:
            return "OCHOCIENTOS";
         case 900:
            return "NOVECIENTOS";

         case 1000:
            return "MIL";

         case 1000000:
            return "UN MILLON";
         case 2000000:
            return "DOS MILLONES";
         case 3000000:
            return "TRES MILLONES";
         case 4000000:
            return "CUATRO MILLONES";
         case 5000000:
            return "CINCO MILLONES";
         case 6000000:
            return "SEIS MILLONES";
         case 7000000:
            return "SIETE MILLONES";
         case 8000000:
            return "OCHO MILLONES";
         case 9000000:
            return "NUEVE MILLONES";
         case 10000000:
            return "DIEZ MILLONES";
         case 20000000:
            return "VEINTE MILLONES";
         case 30000000:
            return "TREINTA MILLONES";
         case 40000000:
            return "CUARENTA MILLONES";
         case 50000000:
            return "CINCUENTA MILLONES";
         case 60000000:
            return "SESENTA MILLONES";
         case 70000000:
            return "SETENTA MILLONES";
         case 80000000:
            return "OCHENTA MILLONES";
         case 90000000:
            return "NOVENTA MILLONES";
         case 100000000:
            return "CIEN MILLONES";
      }
      if (_counter < 20) {
//System.out.println(">15");
         return "DIECI" + doThings(_counter - 10);
      }
      if (_counter < 30) {
//System.out.println(">20");
         return "VEINTI" + doThings(_counter - 20);
      }
      if (_counter < 100) {
//System.out.println("<100"); 
         return doThings((int) (_counter / 10) * 10) + " Y " + doThings(_counter % 10);
      }
      if (_counter < 200) {
//System.out.println("<200"); 
         return "CIENTO " + doThings(_counter - 100);
      }
      if (_counter < 1000) {
//System.out.println("<1000");
         return doThings((int) (_counter / 100) * 100) + " " + doThings(_counter % 100);
      }
      if (_counter < 2000) {
//System.out.println("<2000");
         return "MIL " + doThings(_counter % 1000);
      }
      if (_counter < 1000000) {
         String var = "";
//System.out.println("<1000000");
         var = doThings((int) (_counter / 1000)) + " MIL";
         if (_counter % 1000 != 0) {
//System.out.println(var);
            var += " " + doThings(_counter % 1000);
         }
         return var;
      }
      if (_counter < 2000000) {
         return "UN MILLON " + doThings(_counter % 1000000);
      }
      if (_counter < 10000000) {
         String var = "";
         var = doThings((int) (_counter / 1000000)) + " MILLONES";
         if (_counter % 1000000 != 0) {
            var += " " + doThings(_counter % 1000000);
         }
         return var;
      }

      if (_counter < 100000000) {
         String var = "";
         var = doThings((int) (_counter / 1000000)) + " MILLONES";
         if (_counter % 10000000 != 0) {
            var += " " + doThings(_counter % 1000000);
         }
         return var;
      }
      return "";
   }
}
