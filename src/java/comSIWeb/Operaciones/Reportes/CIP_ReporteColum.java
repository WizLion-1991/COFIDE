package comSIWeb.Operaciones.Reportes;

import java.util.ArrayList;

/**
 *Representa una columna dentro del reporte
 * @author zeus
 */
public class CIP_ReporteColum {

   /**
    * Es el titulo de la columna
    */
   public String strTitulo;
   /**
    * Es el ancho de la columna
    */
   public int intAncho;
   /**
    * Es el alto de la columna
    */
   public int intAlto;
   /**
    * Es la alineacion de la columna
    */
   public String strAlign;
   /**
    * Es la lista de valores
    */
   public ArrayList<CIP_ReporteValor> lstValores;
   /**
    * Llena el cuadro del reporte
    */
   public boolean bolFill;
   /**
    * Es el color de la letra de toda la columna
    */
   public CIP_ReporteRGB RGBColorLetra;
   /**
    * Es el color de fondo de toda la columna
    */
   public CIP_ReporteRGB RGBColorFondo;
   private int intContador;
   /**
    * Es el tamanio de la fuente por default es  8
    */
   public int intFontSize;
   /**
    * Es el tamanio de la fuente del titulo por default es  10
    */
   public int intFontSizeTitle;
   /**
    * Indica si usaremos NOWRAP
    */
   public boolean bolNoWrap;
   /**
    * Indica si vamos a usar numeros en esta celda
    */
   public boolean bolFormatoNumber = false;
   /**
    * Construye la columna
    * @param strTitulo Es el titulo de la columna
    */
   public CIP_ReporteColum(String strTitulo) {
      this.strTitulo = strTitulo;
      this.intAncho = 0;
      this.intAlto = 0;
      this.strAlign = "left";
      this.lstValores = new ArrayList<CIP_ReporteValor>();
      this.bolFill = false;
      this.RGBColorLetra = new CIP_ReporteRGB();
      this.RGBColorFondo = new CIP_ReporteRGB();
      this.intContador = 0;
      this.intFontSize = 8;
      this.intFontSizeTitle = 10;
      this.bolNoWrap = false;
   }

   /**
    * Agrega un nuevo valor al arreglo de la columna
    * @param strValor Es el valor a pintar
    * @param strAlign Es la alineacion del valor a pintar
    * @param intColspan Es el valor del colspan(juntar varias filas)
    * @param RGBColorLetra Es el color RGB de la letra
    * @param RGBColorFondo Es el color RGB del fondo
    */
   public void AddValor(String strValor,String strAlign,int intColspan,CIP_ReporteRGB RGBColorLetra,CIP_ReporteRGB RGBColorFondo){
      this.intContador++;
      CIP_ReporteValor objValor = new CIP_ReporteValor();
      objValor.strValor = strValor;
      objValor.strAlign = strAlign;
      objValor.intColspan = intColspan;
      if(RGBColorLetra != null)objValor.RGBColorLetra = RGBColorLetra;
      if(RGBColorFondo != null)objValor.RGBColorFondo = RGBColorFondo;
      this.lstValores.add(objValor);
   }
   /**
    * Agrega un nuevo valor al arreglo de la columna
    * @param strValor Es el valor a pintar
    * @param strAlign Es la alineacion del valor a pintar
    * @param intColspan Es el valor del colspan(juntar varias filas)
    * @param intFontSize Tamanio de la fuente
    * @param intFontStyle  Estilo de la fuente
    * @param RGBColorLetra Es el color RGB de la letra
    * @param strNameFont Nombre de la fuente
    * @param RGBColorFondo Es el color RGB del fondo
    */
   public void AddValor(String strValor,String strAlign,int intColspan,int intFontSize,int intFontStyle,String strNameFont,
           CIP_ReporteRGB RGBColorLetra,CIP_ReporteRGB RGBColorFondo){
      this.intContador++;
      CIP_ReporteValor objValor = new CIP_ReporteValor();
      objValor.strValor = strValor;
      objValor.strAlign = strAlign;
      objValor.intColspan = intColspan;
      objValor.intFontSize = intFontSize;
      objValor.intFontStyle= intFontStyle;
      objValor.strNameFont= strNameFont;
      if(RGBColorLetra != null)objValor.RGBColorLetra = RGBColorLetra;
      if(RGBColorFondo != null)objValor.RGBColorFondo = RGBColorFondo;
      this.lstValores.add(objValor);
   }
   /**
    * Agrega un nuevo valor al arreglo de la columna
    * @param strValor Es el valor a pintar
    * @param strAlign Es la alineacion del valor a pintar
    * @param intColspan Es el valor del colspan(juntar varias filas)
    * @param intFontSize Tamanio de la fuente
    * @param intFontStyle  Estilo de la fuente
    * @param RGBColorLetra Es el color RGB de la letra
    * @param strNameFont Nombre de la fuente
    * @param RGBColorFondo Es el color RGB del fondo
    * @param strURLImg Es la URL
    * @param intWidthImg Es el ancho de la imagen
    * @param intHeightImg  Es el alto de la imagen
    * @param intPercent Es el porcentaje de la imagen
    * @param intBorder Es el borde de la celda
    * @param intBorderWidth  Es el ancho del borde
    * @param intHeightCell Define el alto de la celda
    */
   public void AddValor(String strValor,String strAlign,int intColspan,int intFontSize,int intFontStyle,String strNameFont,
           CIP_ReporteRGB RGBColorLetra,CIP_ReporteRGB RGBColorFondo,String strURLImg,
           int intWidthImg,int intHeightImg,
           int intPercent,int intBorder,float intBorderWidth,float intHeightCell){
      this.intContador++;
      CIP_ReporteValor objValor = new CIP_ReporteValor();
      objValor.strValor = strValor;
      objValor.strAlign = strAlign;
      objValor.intColspan = intColspan;
      objValor.intFontSize = intFontSize;
      objValor.intFontStyle= intFontStyle;
      objValor.strNameFont= strNameFont;
      objValor.strUrlImg = strURLImg;
      objValor.intPercent = intPercent;
      objValor.intWidth = intWidthImg;
      objValor.intHeight = intHeightImg;
      objValor.setIntBorder(intBorder);
      objValor.setIntBorderWidth(intBorderWidth);
      objValor.setIntHeightCell(intHeightCell);
      if(RGBColorLetra != null)objValor.RGBColorLetra = RGBColorLetra;
      if(RGBColorFondo != null)objValor.RGBColorFondo = RGBColorFondo;
      this.lstValores.add(objValor);
   }
   /**Nos regresa el numero de elementos del arreglo
    * @return Nos regresa el valor del contador
    */
   public int getNumRows(){
      return this.intContador;
   }
   /**Nos regresa el elemento en la posicion indicada
    * @param intIdx Es el indice del elemento a recuperar
    * @return  Nos regresa el objeto CIP_ReporteValor con el valor a pintar en la columna
    */
   public CIP_ReporteValor getData(int intIdx){
      return lstValores.get(intIdx);
   }
}
