/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias.numeros_letras;

/**
 *Contiene la defincion de los textos de los numero en espanol
 * @author aleph_79
 */
public abstract class NumerosLetras {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   public String strNombre = null;
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Regresa la lista de numeros
    * @return Regresa un arreglo con los textos del numero en la localidad especificada
    */
   public abstract String[] getNumeros();
   // </editor-fold>

}
