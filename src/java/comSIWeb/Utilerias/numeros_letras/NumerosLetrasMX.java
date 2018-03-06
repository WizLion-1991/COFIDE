/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias.numeros_letras;

/**
 *Son los numero en localidad MX espanol mexico
 * @author aleph_79
 */
public class NumerosLetrasMX extends NumerosLetras {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private final String[] numeros = {"CERO", "UN", "DOS", "tres ", "cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve"};
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public NumerosLetrasMX() {
      strNombre = "MX";

   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   @Override
   public String[] getNumeros() {
      return this.numeros;
   }
   // </editor-fold>
}
