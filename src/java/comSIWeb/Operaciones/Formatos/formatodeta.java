package comSIWeb.Operaciones.Formatos;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa el detalle del formato
 * @author zeus
 */
public class formatodeta extends TableMaster {
   private String strValPrint ;
   public formatodeta() {
      super("formatodeta", "CAMPOLLAVE", "", "");
      this.Fields.put("FMD_ID", new Integer(0));
      this.Fields.put("FM_ID", new Integer(0));
      this.Fields.put("FMD_NOM", "");
      this.Fields.put("FMD_TITULO", "");
      this.Fields.put("FMD_TIPODATO", "");
      this.Fields.put("FMD_FORMATO", "");
      this.Fields.put("FMD_POSX", new Integer(0));
      this.Fields.put("FMD_POSY", new Integer(0));
      this.Fields.put("FMD_ALTO", new Integer(0));
      this.Fields.put("FMD_ANCHO", new Integer(0));
      this.Fields.put("FMD_ESTILO", new Integer(0));
      this.Fields.put("FMD_ALIGN", new Integer(0));
      this.Fields.put("FMD_WIDTH_CANVAS", new Integer(0));
      this.Fields.put("FMD_LETRA", "");
      this.Fields.put("FMD_SIZEFONT", new Integer(0));
      this.Fields.put("FMD_COLOR", "");
      this.Fields.put("FMD_COLORFONDO", "");
      this.Fields.put("FMD_VALCONST", "");
      this.Fields.put("FMD_HEAD_FOOT_BOD", new Integer(0));
      this.Fields.put("FS_ID", new Integer(0));
      this.strValPrint = "";
   }

   /**
    *
    * @return
    */
   public String getStrValPrint() {
      return strValPrint;
   }

   /**
    * Define el valor a imprimir en el formato
    * @param strValPrint
    */
   public void setStrValPrint(String strValPrint) {
      this.strValPrint = strValPrint;
   }

}
