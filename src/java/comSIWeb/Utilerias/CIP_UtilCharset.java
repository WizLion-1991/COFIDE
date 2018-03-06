/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comSIWeb.Utilerias;

/**
 *Sustituye los acentos y ? en una cadena
 * @author zeus
 */
public class CIP_UtilCharset {
   /**Sustituye los acentos y ? en una cadena
    * @param strCadena es la cadena por procesar
    * @return nos regresa la cadena formateada
    */
   public String Sustituye(String strCadena){
      String strValor = new String(strCadena);
      strValor = strValor.replace( "&", "&amp;");
      strValor = strValor.replace("á","&aacute;");
      strValor = strValor.replace("é","&eacute;");
      strValor = strValor.replace("í","&iacute;");
      strValor = strValor.replace("ó","&oacute;");
      strValor = strValor.replace("ú","&uacute;");
      strValor = strValor.replace("¿","&iquest;");
      return strValor;
   }
}
