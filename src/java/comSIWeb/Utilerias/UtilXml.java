/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;

/**
 *Clase para validar y hacer operaciones con XML
 * @author zeus
 */
public class UtilXml {

   /**
    * Sustituye los caracteres prohibidos del XML por un caracter que lo acepte
    * @param strCadena Es la cadena a procesar
    * @return Nos regresa la cadena con loa valores sustituidos
    */
   public String Sustituye(String strCadena) {
      String strValor = new String(strCadena);
      strValor = strValor.replace( "&", "&amp;");
      strValor = strValor.replace(">", "&gt;");
      strValor = strValor.replace("<", "&lt;");
      //strValor = strValor.replace("'", "&#39");
      strValor = strValor.replace("\"", "&quot;");
      strValor = strValor.replace("\n", "&#xD;");
      return strValor;
   }
}
