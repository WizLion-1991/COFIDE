package comSIWeb.Operaciones.Reportes;

import com.itextpdf.text.Font;

/**
 *Representa el valor a mostrar en una celda
 * @author zeus
 */
public class CIP_ReporteValor {
   /**
    * Es el valor a pintar
    */
   public String strValor;
   /**
    * Es la alineacion RIGHT LEFT CENTER
    */
   public String strAlign;
   /**
    * Es el colspan
    */
   public int intColspan;
   /**
    * Es el color de la letra
    */
   public CIP_ReporteRGB RGBColorLetra;
   /**
    * Es el color de fondo
    */
   public CIP_ReporteRGB RGBColorFondo;
   /**
    * Es el tipo de fuente NORMAL UNDERLINE etc definidmos en el iText
    */
   public int intFontStyle = Font.NORMAL;
   /**
    * Es el tamanio de la fuente
    */
   public int intFontSize = 8;
   /**
    * Es la url de la imagen a dibujar
    */
   public String strUrlImg = "";
   /**
    * Es el punto X donde dibujar la imagen
    */
   public int intX = 0;
   /**
    * Es el punto Y donde dibujar la imagen
    */
   public int intY = 0;
   /**
    * Es el ancho de la imagen
    */
   public int intWidth = 0;
   /**
    * Es el alto de la imagen
    */
   public int intHeight = 0;
   /**
    * Es el porcentaje de la imagen
    */
   public int intPercent = 100;
   /**
    * Es el nombre de la fuente
    */
   public String strNameFont = "Arial";
   private float intBorderWidth = 0;
   private int intBorder = 0;
   private float intHeightCell = 0;
   public CIP_ReporteValor() {
      this.strValor = "";
      this.strAlign = "";
      this.intColspan = 0;
      this.RGBColorLetra = new CIP_ReporteRGB();
      this.RGBColorLetra.intR = 0;
      this.RGBColorLetra.intG = 0;
      this.RGBColorLetra.intB = 0;
      this.RGBColorFondo = new CIP_ReporteRGB();
      this.RGBColorFondo.intR = 255;
      this.RGBColorFondo.intG = 255;
      this.RGBColorFondo.intB = 255;
   }
   /**
    * Nos regresa el ancho del borde de las celdas
    * @return Nos regresa el ancho
    */
   public float getIntBorderWidth() {
      return intBorderWidth;
   }

   /**
    * Establece el ancho del borde las celdas
    * @param intBorderWidth Es el ancho
    */
   public void setIntBorderWidth(float intBorderWidth) {
      this.intBorderWidth = intBorderWidth;
   }
   /**
    * Nos regresa el borde de la celda
    * @return Nos regresa el borde
    */
   public int getIntBorder() {
      return intBorder;
   }

   /**
    * Establece si tienen bordes las celdas
    * @param intBorder Es el borde
    */
   public void setIntBorder(int intBorder) {
      this.intBorder = intBorder;
   }

   /**
    * Regresa el alto de la celda
    * @return Regresa el ancho
    */
   public float getIntHeightCell() {
      return intHeightCell;
   }

   /**
    * Define el alto de la celda
    * @param intHeightCell Define el ancho
    */
   public void setIntHeightCell(float intHeightCell) {
      this.intHeightCell = intHeightCell;
   }

}
