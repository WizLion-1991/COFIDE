/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package comSIWeb.Utilerias;

/**
 *Realiza validaciones de los caracteres especiales para Json
\b  Backspace (ascii code 08)
\f  Form feed (ascii code 0C)
\n  New line
\r  Carriage return
\t  Tab
\v  Vertical tab
\'  Apostrophe or single quote (only valid in single quoted json strings)
\"  Double quote (only valid in double quoted json strings)
\\  Backslash caracter
 * @author ZeusGalindo
 */
public class UtilJson {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
    public String parse(String strCadena) {
      strCadena = strCadena.replace( "'", "\'");
      strCadena = strCadena.replace("\"", "\\\"");
      strCadena = strCadena.replace("\\", "\\\\");
      //strValor = strValor.replace("'", "&#39");
      strCadena = strCadena.replace("\n", "\\n");
      return strCadena;
    }
   // </editor-fold>
}
