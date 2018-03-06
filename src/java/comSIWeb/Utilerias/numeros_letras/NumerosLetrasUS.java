/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias.numeros_letras;

/**
 *Son los numero en localidad MX Ingles USA
 * @author aleph_79
 */
public class NumerosLetrasUS extends NumerosLetras{
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   private final String[] numeros = {"ZERO", "ONE", "TWO", "THREE ", "FOUR ", "FIVE ", "SIX ", "SEVEN ", "EIGHT ", "NINE"};
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   @Override
   public String[] getNumeros() {
      return this.numeros;
   }
   // </editor-fold>
}
