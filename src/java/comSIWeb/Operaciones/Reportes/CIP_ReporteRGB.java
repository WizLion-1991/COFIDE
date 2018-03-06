package comSIWeb.Operaciones.Reportes;

/**
 *Representa un Color RGB
 * @author zeus
 */
public class CIP_ReporteRGB {

   /**
    * Color Rojo
    */
   public int intR;
   /**
    * Color Verde
    */
   public int intG;
   /**
    * Color Azul
    */
   public int intB;

   /**
    * Constructor de color RGB
    */
   public CIP_ReporteRGB() {
      this.intR = 0;
      this.intG = 0;
      this.intB = 0;
   }

   /**
    * Constructor
    * @param intR Color R
    * @param intG Color G
    * @param intB Color B
    */
   public CIP_ReporteRGB(int intR, int intG, int intB) {
      this.intR = intR;
      this.intG = intG;
      this.intB = intB;
   }

}
