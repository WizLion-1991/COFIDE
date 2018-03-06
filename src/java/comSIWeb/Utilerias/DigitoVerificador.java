/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *Esta clase contiene los algoritmos para calculo de digito verificador de distintas bases
 * 
 * @author aleph_79
 */
public class DigitoVerificador {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(DigitoVerificador.class.getName());;

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public DigitoVerificador() {
   }
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Calcula el digito verificador en base 12
    * @param strBase Es la base para el calculo
    * @param bolParIni Con true indica que se comienza con el digito 3, 
    * en caso contrario comienza con el 1
    * @return Es el digito verificador
    */
   public static String CalculoDigitoVerificador12(String strBase, boolean bolParIni) {
      int intSumatoria = 0;
      boolean esPar = bolParIni;
      for (int i = 0; i < strBase.length(); i++) {
         char c = strBase.charAt(i);
//         System.out.println("c " + c);
         int intValorTmp = 0;
         //si es par multiplicmos por 3 o 1 en caso contrario
         if (esPar) {
            intValorTmp = Integer.valueOf(String.valueOf(c)) * 3;
         } else {
            intValorTmp = Integer.valueOf(String.valueOf(c)) * 1;
         }
         intSumatoria += intValorTmp;
         //Cambiamos bandera de par
         if (esPar) {
            esPar = false;
         } else {
            esPar = true;
         }
      }
      //La suma la restamos de la decena inmediata superior
//      System.out.println("intSumatoria:" + intSumatoria);
      int decenas = intSumatoria / 10;
//      System.out.println("decenas:" + decenas);
      int decenaSuperio = (decenas * 10) + 10;
//      System.out.println("decenaSuperio:" + decenaSuperio);
      int intDigito = decenaSuperio - intSumatoria;
      if (intDigito > 9) {
         intDigito = 0;
      }
      return intDigito + "";
   }
   /**Calculo del digito verificado base 10
    * @param strReferencia Es la referencia
    * @param IsAlgoritmp36 Indica que las letras seran reemplazadas en base al algoritmo 36
    * @return   Es el digito
    */
   public static int CalculaModulo10(String strReferencia, boolean IsAlgoritmp36) {
      int intresult = 0;
      String strRef = strReferencia;
      //Debe de ser menor de 39 digitos la referencia
      if (strRef.length() <= 39) {
         //sustituye valores de caracteres por letras
         if(IsAlgoritmp36){
            strRef = SustituyeValoreAlgoritmo36(strRef);
         }else{
            strRef = SustituyeVal(strRef);
         }
         
         //System.out.println("strRef " + strRef);

         int intMultplicador = 2;
         //inciamos hasta aqui bien

         //'Iniciando de derecha a izquierda, tomamos los dgitos que conforman el nmero
         for (int intContador = strRef.length() - 1; intContador >= 0; intContador--) {
            // System.out.println("Contador"+intContador+"ddddd"+String.valueOf(strRef.charAt(intContador))+"x"+intMultplicador);
            //y los iremos multiplicando secuencialmente por 2 y por 1, respectivamente, hasta tomar el ltimo dgito
            try {
               int intvalida = Integer.valueOf(String.valueOf(strRef.charAt(intContador)));
            } catch (NumberFormatException ex) {
               log.error("caracter no numerico" + String.valueOf(strRef.charAt(intContador)));
               return -1;
            }
            int intTempsum = Integer.valueOf(String.valueOf(strRef.charAt(intContador))) * intMultplicador;
            //'En caso de que algunos de los productos estn integrados por dos dgitos,
            int into = 0;
            //while (!(intTempsum>9)){
            while (intTempsum > 9) {
               into++;
               //  System.out.println("intTempsum"+intTempsum+"FFF"+into);
               //se separarn los los dos dgitos y se suma cada uno de ellos individualmente
               String strDigitos = String.valueOf(intTempsum);
               intTempsum = Integer.valueOf(String.valueOf(strDigitos.charAt(0))) + Integer.valueOf(String.valueOf(strDigitos.charAt(1)));
            }
            if (intMultplicador == 2) {
               intMultplicador = 1;
            } else {
               intMultplicador = 2;
            }
            //System.out.println("Result"+intTempsum);
            intresult += intTempsum;
            //System.out.println("Resultado fin "+intresult);
         }
         //Con el resultado de la suma de productos , lo dividimos entre 10
         //resto
         int intResiduo = intresult % 10;
         if (intResiduo == 0) {
            intresult = 0;
         } else {
            //    Se resta de 10, el Remanente de la divisin.
            intresult = 10 - intResiduo;
         }

      } else {
         //la longitud es mayor a 39 no se aplica el algoritmo
         log.error("La longitud del codigo es mayor a 39 no aplica el algoritmo");
         intresult = -1;
      }
      return intresult;
   }

   /**
    * Sustituye los valores de texto por numeros
    * @param strReferenciaalfa Es le referencia alfanumerica
    * @return Regresa la cadena con los valores sustituidos
    */
   public static String SustituyeVal(String strReferenciaalfa) {
      String strResult = "";
      for (int intcontador = 0; intcontador < strReferenciaalfa.length(); intcontador++) {
         String strtemp = String.valueOf(strReferenciaalfa.charAt(intcontador));
         boolean bolConsiderado = false;
         if (strtemp.equalsIgnoreCase("A") || strtemp.equalsIgnoreCase("B") || strtemp.equalsIgnoreCase("C")) {
            strResult += "2";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("D") || strtemp.equalsIgnoreCase("E") || strtemp.equalsIgnoreCase("F")) {
            strResult += "3";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("G") || strtemp.equalsIgnoreCase("H") || strtemp.equalsIgnoreCase("I")) {
            strResult += "4";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("J") || strtemp.equalsIgnoreCase("K") || strtemp.equalsIgnoreCase("L")) {
            strResult += "5";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("M") || strtemp.equalsIgnoreCase("N") || strtemp.equalsIgnoreCase("O")) {
            strResult += "6";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("P") || strtemp.equalsIgnoreCase("Q") || strtemp.equalsIgnoreCase("R")) {
            strResult += "7";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("S") || strtemp.equalsIgnoreCase("T") || strtemp.equalsIgnoreCase("U")) {
            strResult += "8";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("V") || strtemp.equalsIgnoreCase("W") || strtemp.equalsIgnoreCase("X")) {
            strResult += "9";
            bolConsiderado = true;
         }
         if (strtemp.equalsIgnoreCase("Y") || strtemp.equalsIgnoreCase("Z") || strtemp.equalsIgnoreCase("0")) {
            strResult += "0";
            bolConsiderado = true;
         }
         if (bolConsiderado == false) {
            strResult += strtemp;
         }
      }
      //System.out.println("El resultado de sustituir es Mod 10:"+strResult+" de "+strReferenciaalfa);
      return strResult;
   }

   /**
    * Calcula el digito verificador de la base 97
    * @param strSucursal Es la sucursal
    * @param strCuenta Es el numero de cuenta
    * @param strReferencia Es la referencia
    * @return Es el digito verificador
    */
   public int CalculaModulo97(String strSucursal, String strCuenta, String strReferencia) {
      int intresult = 0;
      int intresultCuenta = 0;
      int intresultSucursal = 0;
      String strRef = String.valueOf(strReferencia);
      //formato
      //SSSS                    4       INDICA LA SUCURSAL (DATO FIJO)
      //CCCCCCC                 7       PARA EL NUMERO DE CUENTA (FIJA PARA CADA EMPRESA
      //RRRRRRRRRRRRRRRRRR      18      DIGITOS MAXIMOS PARA LA REFEREANCIA ALFANUMERICA
      //DD                      2       DIGITOS VERIFICADORES

      if ((strRef.length() <= 18) && (strSucursal.length() <= 4) && (strCuenta.length() <= 7)) {

         //sustituye valores de caracteres por letras
         while (strRef.length() <= 17) {
            strRef = "0" + strRef;
         }
         while (strSucursal.length() <= 3) {
            strSucursal = "0" + strSucursal;
         }
         while (strCuenta.length() <= 6) {
            strCuenta = "0" + strCuenta;
         }
         strRef = SustituyeValoreSenseCase(strRef);
         strSucursal = SustituyeValoreSenseCase(strSucursal);
         strCuenta = SustituyeValoreSenseCase(strCuenta);
         log.debug("strRef:" + strRef);
         log.debug("strSucursal:" + strSucursal);
         log.debug("strCuenta:" + strCuenta);

         //int intMultplicador= 2;
         int[] intMultplicador = {37, 31, 29, 23, 19, 17, 13, 11, 7, 5, 3, 2, 1};
         int intContadorArray = 0;
         //inciamos hasta aqui bien
         //'Iniciando de derecha a izquierda, tomamos los dgitos que conforman el nmero
         for (int intContador = strRef.length() - 1; intContador >= 0; intContador--) {

            //System.out.println("ContadorREF" + intContador + "ddddd" + String.valueOf(strRef.charAt(intContador)) + "x" + intMultplicador[intContadorArray]);
            try {
               int intvalida = Integer.valueOf(String.valueOf(strRef.charAt(intContador)));
            } catch (NumberFormatException ex) {
               log.error("caracter no numerico" + String.valueOf(strRef.charAt(intContador)));
               return -1;
            }
            int intTempsum = Integer.valueOf(String.valueOf(strRef.charAt(intContador))) * intMultplicador[intContadorArray];
            log.debug(String.valueOf(strRef.charAt(intContador)) + " " + intMultplicador[intContadorArray] + "=" + intTempsum);
            //'En caso de que algunos de los productos estn integrados por dos dgitos,
            //System.out.println("Result"+intTempsum);
            intresult += intTempsum;
            intContadorArray++;
            if (intContadorArray >= 13) {
               intContadorArray = 0;
            }
         }
         log.debug("intTempsum:" + intresult);
         //terminamos con la referencia
         //Seguimos con la cuenta
         //Iniciamos en 37
         intContadorArray = 0;
         for (int intContador = strCuenta.length() - 1; intContador >= 0; intContador--) {

            //System.out.println("ContadorCUENTA" + intContador + "ddddd" + String.valueOf(strCuenta.charAt(intContador)) + "x" + intMultplicador[intContadorArray]);
            try {
               int intvalida = Integer.valueOf(String.valueOf(strCuenta.charAt(intContador)));
            } catch (NumberFormatException ex) {
               log.error("caracter no numerico" + String.valueOf(strCuenta.charAt(intContador)));
               return -1;
            }
            int intTempsum = Integer.valueOf(String.valueOf(strCuenta.charAt(intContador))) * intMultplicador[intContadorArray];
            //'En caso de que algunos de los productos estn integrados por dos dgitos,
            log.debug(String.valueOf(strCuenta.charAt(intContador)) + " " + intMultplicador[intContadorArray] + "=" + intTempsum);
            intresultCuenta += intTempsum;
            intContadorArray++;
            if (intContadorArray >= 11) {
               intContadorArray = 0;
            }

            //System.out.println("Resultado fin "+intresult);
         }
         log.debug("intTempsum:" + intresultCuenta);
         //terminamos con la cuenta
         //Seguimos con la sucursal
         //Iniciamos en 37
         intContadorArray = 0;
         for (int intContador = strSucursal.length() - 1; intContador >= 0; intContador--) {

            //System.out.println("ContadorSUCURSAL" + intContador + "ddddd" + String.valueOf(strSucursal.charAt(intContador)) + "x" + intMultplicador[intContadorArray]);
            try {
               int intvalida = Integer.valueOf(String.valueOf(strSucursal.charAt(intContador)));
            } catch (NumberFormatException ex) {
               log.error("caracter no numerico" + String.valueOf(strSucursal.charAt(intContador)));
               return -1;
            }
            int intTempsum = Integer.valueOf(String.valueOf(strSucursal.charAt(intContador))) * intMultplicador[intContadorArray];
            log.debug(String.valueOf(strSucursal.charAt(intContador)) + " " + intMultplicador[intContadorArray] + "=" + intTempsum);
            //'En caso de que algunos de los productos estn integrados por dos dgitos,
            //System.out.println("Result"+intTempsum);
            intresultSucursal += intTempsum;
            intContadorArray++;
            if (intContadorArray >= 11) {
               intContadorArray = 0;
            }
            //System.out.println("Resultado fin "+intresult);
         }
         log.debug("intTempsum:" + intresultSucursal);
         //terminamos con la cuenta
//Sumanos todos los resultados
         //System.out.println("suma resultSucursal:" + intresultSucursal + "+intresultCuenta:" + intresultCuenta + "+intresultReferencia:" + intresult + "=" + (intresultSucursal + intresult + intresultCuenta));
         intresult = (intresultSucursal + intresult + intresultCuenta);
         log.debug("intresult:" + intresult);
         //Con el resultado de la suma de productos , lo dividimos entre 10
         //resto
         int intResiduo = intresult % 97;

         //    Se resta de 10, el Remanente de la divisin.
         intresult = 99 - intResiduo;

      } else {
         //la longitud es mayor a 39 no se aplica el algoritmo
         log.error("La longitud del codigo es incorrecto siga el formato SSSS CCCCCCC R(18)");
         intresult = -1;
      }
      //Regresa el o los digitos verificadores
      return intresult;
   }

   /**
    * Sustituye los valores de texto por numeros
    * @param strReferenciaalfa Es la referencia alfanumerica
    * @return Es la cadena con los valores sustituidos
    */
   public static String SustituyeValoreSenseCase(String strReferenciaalfa) {
      String strResult = "";
      for (int intcontador = 0; intcontador < strReferenciaalfa.length(); intcontador++) {
         String strtemp = String.valueOf(strReferenciaalfa.charAt(intcontador));
         boolean bolConsiderado = false;
         if (strtemp.equals("A") || strtemp.equals("B") || strtemp.equals("C")) {
            strResult += "2";
            bolConsiderado = true;
         }
         if (strtemp.equals("D") || strtemp.equals("E") || strtemp.equals("F")) {
            strResult += "3";
            bolConsiderado = true;
         }
         if (strtemp.equals("G") || strtemp.equals("H") || strtemp.equals("I")) {
            strResult += "4";
            bolConsiderado = true;
         }
         if (strtemp.equals("J") || strtemp.equals("K") || strtemp.equals("L")) {
            strResult += "5";
            bolConsiderado = true;
         }
         if (strtemp.equals("M") || strtemp.equals("N") || strtemp.equals("O")) {
            strResult += "6";
            bolConsiderado = true;
         }
         if (strtemp.equals("P") || strtemp.equals("Q") || strtemp.equals("R")) {
            strResult += "7";
            bolConsiderado = true;
         }
         if (strtemp.equals("S") || strtemp.equals("T") || strtemp.equals("U")) {
            strResult += "8";
            bolConsiderado = true;
         }
         if (strtemp.equals("V") || strtemp.equals("W") || strtemp.equals("X")) {
            strResult += "9";
            bolConsiderado = true;
         }
         if (strtemp.equals("Y") || strtemp.equals("Z") || strtemp.equals("0")) {
            strResult += "0";
            bolConsiderado = true;
         }
         if (bolConsiderado == false) {
            strResult += strtemp;
         }
      }
      //System.out.println("El resultado de sustituir es Mod 10:"+strResult+" de "+strReferenciaalfa);
      return strResult;
   }
   /**
    * Sustituye los valores de texto por numeros
    * @param strReferenciaalfa Es la referencia alfanumerica
    * @return Es la cadena con los valores sustituidos
    */
   public static String SustituyeValoreAlgoritmo36(String strReferenciaalfa) {
      String strResult = "";
      for (int intcontador = 0; intcontador < strReferenciaalfa.length(); intcontador++) {
         String strtemp = String.valueOf(strReferenciaalfa.charAt(intcontador));
         boolean bolConsiderado = false;
         if (strtemp.equals("A") || strtemp.equals("J") || strtemp.equals("S")) {
            strResult += "1";
            bolConsiderado = true;
         }
         if (strtemp.equals("B") || strtemp.equals("K") || strtemp.equals("T")) {
            strResult += "2";
            bolConsiderado = true;
         }
         if (strtemp.equals("C") || strtemp.equals("L") || strtemp.equals("U")) {
            strResult += "3";
            bolConsiderado = true;
         }
         if (strtemp.equals("D") || strtemp.equals("M") || strtemp.equals("V")) {
            strResult += "4";
            bolConsiderado = true;
         }
         if (strtemp.equals("E") || strtemp.equals("N") || strtemp.equals("W")) {
            strResult += "5";
            bolConsiderado = true;
         }
         if (strtemp.equals("F") || strtemp.equals("O") || strtemp.equals("X")) {
            strResult += "6";
            bolConsiderado = true;
         }
         if (strtemp.equals("G") || strtemp.equals("P") || strtemp.equals("Y")) {
            strResult += "7";
            bolConsiderado = true;
         }
         if (strtemp.equals("H") || strtemp.equals("Q") || strtemp.equals("Z")) {
            strResult += "8";
            bolConsiderado = true;
         }
         if (strtemp.equals("I") || strtemp.equals("R") ) {
            strResult += "9";
            bolConsiderado = true;
         }
         if (bolConsiderado == false) {
            strResult += strtemp;
         }
      }
      //System.out.println("El resultado de sustituir es Mod 10:"+strResult+" de "+strReferenciaalfa);
      return strResult;
   }
   // </editor-fold>
}
