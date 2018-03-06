
package comSIWeb.Operaciones.Formatos;

/**
 *Representa un comodin en un formato
 * @author zeus
 */
public class formatoComodin {
   private String strNomComodin;
   private String strValorComodin;

   /**
    * Construimos un objeto comodin
    * @param strNomComodin Definimos el nombre del comodin
    * @param strValorComodin Es el texto con el valor a reemplazar en el comodin
    */
   public formatoComodin(String strNomComodin, String strValorComodin) {
      this.strNomComodin = strNomComodin;
      this.strValorComodin = strValorComodin;
   }

   /**
    * Regresa el nombre del comodin
    * @return Es el texto del comodin encerrado entre [corchetes]
    */
   public String getStrNomComodin() {
      return strNomComodin;
   }

   /**
    * Definimos el nombre del comodin
    * @param strNomComodin Es el texto del comodin encerrado entre [corchetes]
    */
   public void setStrNomComodin(String strNomComodin) {
      this.strNomComodin = strNomComodin;
   }

   /**
    * Regresa el valor del comodin
    * @return Es el texto con el valor a reemplazar en el comodin
    */
   public String getStrValorComodin() {
      return strValorComodin;
   }

   /**
    * Definimos el valor del comodin
    * @param strValorComodin Es el texto con el valor a reemplazar en el comodin
    */
   public void setStrValorComodin(String strValorComodin) {
      this.strValorComodin = strValorComodin;
   }

}
