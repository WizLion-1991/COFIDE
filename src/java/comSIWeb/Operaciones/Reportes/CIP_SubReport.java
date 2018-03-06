package comSIWeb.Operaciones.Reportes;

import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;

/**
 *Representa un subreporte del programa de reportes
 * @author zeus
 */
public class CIP_SubReport {
// <editor-fold defaultstate="collapsed" desc="Propiedades">

   private String strTitulo;
   private PdfPTable table;
   private int intContaCol;//Contador de la columna
   /**
    * Es la lista de columnas del reporte
    */
   private ArrayList<CIP_ReporteColum> lstColumn;

   /**
    * Regresa el subtitulo del reporte
    * @return Es el titulo
    */
   public String getStrTitulo() {
      return strTitulo;
   }

   /**
    * Define el titulo del subreporte
    * @param strTitulo Es el titulo
    */
   public void setStrTitulo(String strTitulo) {
      this.strTitulo = strTitulo;
   }

   /**
    * Nos regresa el numero de columnas del reporte
    * @return Regresa un entero con el numero de columnas
    */
   public int getIntContaCol() {
      return this.intContaCol;
   }

   /**
    * Obtiene la tabla de itext para este reporte
    * @return Es el objeto PdfPTable
    */
   public PdfPTable getTable() {
      return table;
   }

   /**
    * Define la tabla de itext para este reporte
    * @param table Es el objeto PdfPTable
    */
   public void setTable(PdfPTable table) {
      this.table = table;
   }

   /**
    * Nos regresa la lista de columnas del reporte
    * @return Es una lista de columnas
    */
   public ArrayList<CIP_ReporteColum> getLstColumn() {
      return lstColumn;
   }

   /**
    * Definimos la lista de columnas del reporte
    * @param lstColumn Es una lista de columnas
    */
   public void setLstColumn(ArrayList<CIP_ReporteColum> lstColumn) {
      this.lstColumn = lstColumn;
   }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor default
    */
   public CIP_SubReport() {
      this.intContaCol = 0;
      lstColumn = new ArrayList<CIP_ReporteColum>();
   }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Metodos">

   /**Agrega una nueva columna
    * @param objCol Es el objeto columna
    */
   public void AddValor(CIP_ReporteColum objCol) {
      this.intContaCol++;
      this.lstColumn.add(objCol);
   }
// </editor-fold>
}
