package comSIWeb.Operaciones.Reportes;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

/**
 * Representa un reporte que se puede emitir en pdf en excel o en pantalla
 *
 * @author zeus
 */
public class CIP_ReportPDF {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CIP_ReportPDF.class.getName());
   protected Bitacora bitacora;
   private PdfWriter writer;
   /**
    * Es la lista de Encabezados
    */
   public ArrayList<CIP_ReporteValor> lstTitulos;
   /**
    * Es la fecha de emision
    */
   public String strFechaEmision;
   /**
    * Es la hora de emision
    */
   public String strHoraEmision;
   /**
    * Indica si va a tener margen el reporte
    */
   public boolean bolConMargen;
   /**
    * Es la lista de columnas del reporte
    */
   public ArrayList<CIP_SubReport> lstSubReports;
   /**
    * Es el titulo principal del reporte
    */
   public String strTitulo;
   /**
    * Es el autor del reporte
    */
   public String strAutor;
   /**
    * Es el contador de columnas
    */
   private int intContaTitulos;
   private String strOrientation;
   private String strTamHoja;
   private int intTipoReporte;
   public static final int TXT = 0;
   public static final int HTML = 1;
   public static final int EXCEL = 2;
   public static final int PDF = 3;
   private Document document;
   private int intBorderWidth = 0;
   private BufferedWriter outFile;
   private PrintWriter out;
   private Conexion oConn;
   private boolean bolEmision;
   private boolean bolAnchoFijo;
   protected OutputStream outStream;
   protected int intNumSubReports;
   protected int intFontSizeTitle = 11;
   protected int intFontSizeTitle2 = 10;
   /**
    * Es la url de la imagen a dibujar
    */
   public String strUrlImg = "";
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
    * Es el colspan de la imagen
    */
   public int intColspanImg = 0;
   /**
    * Es la posicion X de la imagen del encabezado
    */
   public int intPosXImg = 0;
   /**
    * Es la posicion X de la imagen del encabezado
    */
   public int intPosYImg = 0;
   /**
    * Es la alineacion de la imagen del encabezado
    */
   public int intAlignImg = 0;
   /**
    * Es el titulo de la imagen del encabezado
    */
   public String strTitleImg = null;
   protected VariableSession varSesionesServlet;

   /**
    * Regresa el objeto de las variables de sesion
    *
    * @return Es un objeto con las variables de sesion
    */
   public VariableSession getVarSesionesServlet() {
      return varSesionesServlet;
   }

   /**
    * Define el objeto de las variables de sesion
    *
    * @param varSesionesServlet Es un objeto con las variables de sesion
    */
   public void setVarSesionesServlet(VariableSession varSesionesServlet) {
      this.varSesionesServlet = varSesionesServlet;
   }

   /**
    * Nos regresa el numero de columnas del reporte
    *
    * @return Regresa un entero con el numero de columnas
    */
   public int getIntContaCol() {
      return this.lstSubReports.get(0).getIntContaCol();
   }

   /**
    * Regresa la orientacion del documento(aplica solo en pdf)
    *
    * @return Regresa una cadena con el tipo de orientacion Horizontal o
    * Vertical
    */
   public String getStrOrientation() {
      return strOrientation;
   }

   /**
    * Define la orientacion del documento(aplica solo en pdf)
    *
    * @param strOrientation Es una cadena con el tipo de orientacion Horizontal
    * o Vertical
    */
   public void setStrOrientation(String strOrientation) {
      this.strOrientation = strOrientation;
   }

   /**
    * Regresa el tamanio de hoja del documento(aplica solo en pdf)
    *
    * @return Regresa el tamanio de la hoja
    */
   public String getStrTamHoja() {
      return strTamHoja;
   }

   /**
    * Define el tamanio de la hoja
    *
    * @param strTamHoja Es el tamanio de la hoja
    */
   public void setStrTamHoja(String strTamHoja) {
      this.strTamHoja = strTamHoja;
   }

   /**
    * Nos regresa el documento de iText que imprimira el reporte
    *
    * @return Es el documento pdf
    */
   public Document getDocument() {
      return document;
   }

   /**
    * Define el documento de iText que imprimira el reporte
    *
    * @param document Es el documento pdf
    */
   public void setDocument(Document document) {
      this.document = document;
   }

   /**
    * Nos dice el tipo de reporte TXT HTML EXCEL PDF MAIL
    *
    * @return Es un valor numerico con el tipo de reporte
    */
   public int getIntTipoReporte() {
      return intTipoReporte;
   }

   /**
    * Define el tipo de reporte TXT HTML EXCEL PDF MAIL
    *
    * @param intTipoReporte es el tipo de reporte
    */
   public void setIntTipoReporte(int intTipoReporte) {
      this.intTipoReporte = intTipoReporte;
   }

   /**
    * Nos regresa el ancho del borde de las celdas
    *
    * @return Nos regresa el ancho
    */
   public int getIntBorderWidth() {
      return intBorderWidth;
   }

   /**
    * Establece el ancho del borde las celdas
    *
    * @param intBorderWidth Es el ancho
    */
   public void setIntBorderWidth(int intBorderWidth) {
      this.intBorderWidth = intBorderWidth;
   }

   /**
    * Obtiene la salida para escribir un archivo
    *
    * @return Es el buffer
    */
   public BufferedWriter getOutFile() {
      return outFile;
   }

   /**
    * Define la salida para escribir un archivo
    *
    * @param out Es el buffer
    */
   public void setOutFile(BufferedWriter out) {
      this.outFile = out;
   }

   /**
    * Obtiene la salida para escribir en una pagina web
    *
    * @return Es la salida al browser
    */
   public PrintWriter getOut() {
      return out;
   }

   /**
    * Define la salida para escribir en una pagina web
    *
    * @param out Es la salida al browser
    */
   public void setOut(PrintWriter out) {
      this.out = out;
   }

   /**
    * Nos dice si el reporte mostrara la fecha y hora de emision
    *
    * @return Regresa true o false
    */
   public boolean isBolEmision() {
      return bolEmision;
   }

   /**
    * Definimos si el reporte mostrara la fecha y hora de emision
    *
    * @param bolEmision Indica si mostramos el momento de emision
    */
   public void setBolEmision(boolean bolEmision) {
      this.bolEmision = bolEmision;
   }

   /**
    * Nos dice si las columnas van a tener un ancho fijo
    *
    * @return Nos regresa true/false
    */
   public boolean isBolAnchoFijo() {
      return bolAnchoFijo;
   }

   /**
    * Definimos si las columnas van a tener un ancho fijo
    *
    * @param bolAnchoFijo Definimos con true/false
    */
   public void setBolAnchoFijo(boolean bolAnchoFijo) {
      this.bolAnchoFijo = bolAnchoFijo;
   }

   /**
    * Obtenemos el objeto write del PDF
    *
    * @return Es el objeto writer
    */
   public PdfWriter getWriter() {
      return writer;
   }

   /**
    * Definimos el objeto write del PDF
    *
    * @param writer Es el objeto writer
    */
   public void setWriter(PdfWriter writer) {
      this.writer = writer;
   }

   /**
    * Regresa el objeto out stream si no esta definido regresa null
    *
    * @return Regresa un objeto
    */
   public OutputStream getOutStream() {
      return outStream;
   }

   /**
    * Define el objeto out stream
    *
    * @param outStream Es el objeto
    */
   public void setOutStream(OutputStream outStream) {
      this.outStream = outStream;
   }

   /**
    * Regresa el tamanio de la fuente de los titulos
    *
    * @return Es el tamanio de la fuente(el default es 10)
    */
   public int getIntFontSizeTitle() {
      return intFontSizeTitle;
   }

   /**
    * Define el tamanio de la fuente de los titulos
    *
    * @param intFontSizeTitle Es el tamanio de la fuente(el default es 10)
    */
   public void setIntFontSizeTitle(int intFontSizeTitle) {
      this.intFontSizeTitle = intFontSizeTitle;
   }

   /**
    * Regresa el tamanio de la fuente de los titulos
    *
    * @return Es el tamanio de la fuente(el default es 10)
    */
   public int getIntFontSizeTitle2() {
      return intFontSizeTitle2;
   }

   /**
    * Define el tamanio de la fuente de los titulos
    *
    * @param intFontSizeTitle2 Es el tamanio de la fuente(el default es 10)
    */
   public void setIntFontSizeTitle2(int intFontSizeTitle2) {
      this.intFontSizeTitle2 = intFontSizeTitle2;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   /**
    * Inicializa el reporte
    *
    * @param strOrientation Es la orientacion
    * @param strTamHoja Es el tamanio de hoja
    */
   public CIP_ReportPDF(String strOrientation, String strTamHoja) {
      this.strOrientation = strOrientation;
      this.strTamHoja = strTamHoja;
      this.bolConMargen = true;
      this.strOrientation = "p";
      this.strTamHoja = "letter";
      this.intTipoReporte = CIP_ReportPDF.TXT;
      this.lstTitulos = new ArrayList<CIP_ReporteValor>();
      this.lstSubReports = new ArrayList<CIP_SubReport>();
      //Nuevo subreporte por default
      CIP_SubReport subReport = new CIP_SubReport();
      this.lstSubReports.add(subReport);
      intNumSubReports = 0;
      this.strTitulo = "";
      this.strAutor = "Automatic...";
      this.bitacora = new Bitacora();
      this.bolEmision = true;
      this.bolAnchoFijo = true;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Agrega una nueva columna
    *
    * @param objCol Es el objeto columna
    */
   public void AddValor(CIP_ReporteColum objCol) {
      if (this.lstSubReports != null) {
         this.lstSubReports.get(0).AddValor(objCol);
      } else {
         log.error("No hay lista de reportes especificada....");
      }

   }

   /**
    * Agrega una nueva columna al subreporte especificado
    *
    * @param intIdxSubReporte Es el indice del subreporte
    * @param objCol Es el objeto columna
    */
   public void AddValor(int intIdxSubReporte, CIP_ReporteColum objCol) {
      this.lstSubReports.get(intIdxSubReporte).AddValor(objCol);
   }

   /**
    * Agrega un nuevo subreporte
    *
    * @param strTitulo Es el titulo del subreporte
    * @return Regresa el indice del sub reporte generado
    */
   public int AddSubReporte(String strTitulo) {
      intNumSubReports++;
      CIP_SubReport subReport = new CIP_SubReport();
      subReport.setStrTitulo(strTitulo);
      this.lstSubReports.add(subReport);
      return intNumSubReports;
   }

   /**
    * Define el subtitulo del reporte
    *
    * @param intIdxSubReporte Es el indice del subreporte
    * @param strSubtitulo Es el subtitulo
    */
   public void setTituloSubReporte(int intIdxSubReporte, String strSubtitulo) {
      this.lstSubReports.get(intIdxSubReporte).setStrTitulo(strSubtitulo);
   }

   /**
    * Agrega un titulo al reporte
    *
    * @param objTitulo Es el objeto con los valores del titulo
    */
   public void AddTitulo(CIP_ReporteValor objTitulo) {
      intContaTitulos++;
      this.lstTitulos.add(objTitulo);
   }

   /**
    * Emite los encabezados del reporte
    */
   private void EmiteEncabezadosTXT(CIP_SubReport report) {
      char cTab9 = 9;
      //Imprimimos el titulo del reporte
      if (this.outFile == null) {
         for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
            System.out.println(cTab9);
         }
         System.out.println(this.strTitulo);
         for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
            System.out.println(cTab9);
         }

      } else {
         try {
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               this.outFile.write(cTab9);
            }
            this.outFile.write(this.strTitulo);
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               this.outFile.write(cTab9);
            }
         } catch (IOException ex) {
            ex.printStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
         }
      }
      //Imprimimos los encabezados definidos
      for (int i = 0; i < this.intContaTitulos; i++) {
         CIP_ReporteValor objValor = this.lstTitulos.get(i);
         if (this.outFile == null) {
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               System.out.println(cTab9);
            }
            System.out.println(objValor.strValor);
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               System.out.println(cTab9);
            }

         } else {
            try {
               for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
                  this.outFile.write(cTab9);
               }
               this.outFile.write(objValor.strValor);
               for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
                  this.outFile.write(cTab9);
               }
            } catch (IOException ex) {
               ex.printStackTrace();
               this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
            }
         }
      }
      //Aplica solo si queremos mostrar la fecha y hora de emision
      if (bolEmision) {
         Fechas fecha = new Fechas();
         if (this.outFile == null) {
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               System.out.println(cTab9);
            }
            System.out.println("Fecha Emision: " + fecha.getFechaActualDDMMAAAADiagonal());
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               System.out.println(cTab9);
            }
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               System.out.println(cTab9);
            }
            System.out.println("Hora Emision: " + fecha.getHoraActual());
            for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
               System.out.println(cTab9);
            }
         } else {
            try {
               for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
                  this.outFile.write(cTab9);
               }
               this.outFile.write("Fecha Emision: " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActual());
               for (int h = 1; h <= report.getIntContaCol() / 2; h++) {
                  this.outFile.write(cTab9);
               }
            } catch (IOException ex) {
               ex.printStackTrace();
               this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
            }
         }

      }
   }

   /**
    * Emite los encabezados del reporte
    */
   private String EmiteEncabezadosHTML(CIP_SubReport report) {
      char chr10 = 10;
      String strHtml = "<table border=\"" + this.intBorderWidth + "\" width=\"80%\" >" + chr10;
      if (this.intTipoReporte == CIP_ReportPDF.HTML) {
         strHtml = "<table border=\"" + this.intBorderWidth + "\" width=\"80%\" class=\"tabla\">" + chr10;
      }

      //Imprimimos el titulo del reporte
      strHtml += "<tr>" + chr10;
      if (this.intTipoReporte == CIP_ReportPDF.HTML) {
         strHtml += "<td colspan=\"" + report.getIntContaCol() + "\"  class=\"ui-Title2\" align=\"center\" >";
      } else {
         strHtml += "<td colspan=\"" + report.getIntContaCol() + "\" align=\"center\" >";
      }
      strHtml += this.strTitulo;
      strHtml += "</td>" + chr10;
      strHtml += "</tr>" + chr10;

      //Imprimimos los encabezados definidos      
      for (int i = 0; i < this.intContaTitulos; i++) {
         CIP_ReporteValor objValor = this.lstTitulos.get(i);
         strHtml += "<tr>" + chr10;
         if (this.intTipoReporte == CIP_ReportPDF.HTML) {
            strHtml += "<td colspan=\"" + report.getIntContaCol() + "\" class=\"ui-Title3\" align=\"center\" >";
         } else {
            strHtml += "<td colspan=\"" + report.getIntContaCol() + "\" align=\"center\" >";
         }
         strHtml += objValor.strValor;
         strHtml += "</td>" + chr10;
         strHtml += "</tr>" + chr10;
      }
      //Aplica solo si queremos mostrar la fecha y hora de emision
      if (bolEmision) {
         Fechas fecha = new Fechas();

         strHtml += "<tr>" + chr10;
         strHtml += "<td colspan=\"" + report.getIntContaCol() + "\" align=\"center\" >";
         strHtml += "Fecha Emision: " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActual();
         strHtml += "</td>" + chr10;
         strHtml += "</tr>" + chr10;

      }
      return strHtml;
   }

   /**
    * Emite los encabezados del reporte
    */
   private void EmiteEncabezadosPDF(CIP_SubReport report, int intConta) {
      //Definimos tabla del reporte en pdf
      PdfPTable table = new PdfPTable(report.getIntContaCol());
      table.setWidthPercentage(100);
      report.setTable(table);
      //Obtenemos el objeto canvas
      PdfContentByte canvas = writer.getDirectContent();

      int intConTitle = 0;
      int intColsPan = report.getIntContaCol();
      if (intConta == 0) {
         //Imprimimos la imagen del reporte
         if (!this.strUrlImg.equals("")) {
//            try {

            // <editor-fold defaultstate="collapsed" desc="Imagenes">
            try {
               //Validamos si la imagen existe
               File fileImg = new File(this.strUrlImg);
               if (fileImg.exists()) {
                  Image img = Image.getInstance(this.strUrlImg);
                  img.setAbsolutePosition(this.intPosXImg, this.intPosYImg);
                  //Mostramos las posiciones de la imagen principal
                  log.debug(" intPercent: " + intPercent);
                  log.debug(" intWidth: " + intWidth);
                  log.debug(" intHeight: " + intHeight);
                  log.debug(" intPosXImg: " + intPosXImg);
                  log.debug(" intPosYImg: " + intPosYImg);
                  log.debug(" intPosYImg: " + this.intAlignImg);
                  log.debug(" strTitleImg: " + this.strTitleImg);
                  if (intPercent != 0) {
                     img.scalePercent(intPercent);
                  } else {
                     img.scaleToFit(intWidth, intHeight);
                  }
                  img.setAlignment(this.intAlignImg);
                  if (strTitleImg != null) {
                     img.setAlt(strTitleImg);
                  }
                  try {
                     canvas.addImage(img);
                  } catch (DocumentException ex) {
                     log.error("Error al anexar la imagen " + ex.getMessage());
                  }
               } else {
                  log.error("La imagen no existe " + this.strUrlImg);
               }
            } catch (BadElementException ex) {
               log.error("ERROR EN CIP_FORMATO(1)" + ex.getMessage());
            } catch (MalformedURLException ex) {
               log.error("ERROR EN CIP_FORMATO(2)" + ex.getMessage());
            } catch (IOException ex) {
               log.error("ERROR EN CIP_FORMATO(3)" + ex.getMessage());
            }
            // </editor-fold>

//               PdfPCell cell0 = new PdfPCell(img, false);
//               cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
//               cell0.setBorder(this.intBorderWidth);
//               //Calculamos el rowspan
//               int intRowSpan = this.intContaTitulos;
//               if (this.bolEmision) {
//                  intRowSpan += 2;
//               }
//               cell0.setRowspan(intRowSpan);
//               //Calculamos el colSpan
//               if (intColspanImg != 0) {
//                  cell0.setColspan(intColspanImg);
//                  intColsPan -= intColspanImg;
//               } else {
//                  intColsPan--;
//               }
//               table.addCell(cell0);
//               intConTitle++;
//            } catch (BadElementException ex) {
//               Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (MalformedURLException ex) {
//               Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//               Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
//            }
         }
         //Imprimimos el titulo del reporte
         PdfPCell cell0 = new PdfPCell();
         cell0.setColspan(intColsPan);
         cell0.setBorderWidth(this.intBorderWidth);
         cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
         //Imprimimos la frase
         Phrase phrase0 = new Phrase(this.strTitulo,
                 FontFactory.getFont("Arial", this.intFontSizeTitle, Font.BOLD,
                         new BaseColor(0, 0, 0)));
         cell0.setPhrase(phrase0);
         cell0.setNoWrap(true);
         table.addCell(cell0);
         intConTitle++;

         //Imprimimos los encabezados definidos
         for (int i = 0; i < this.intContaTitulos; i++) {
            CIP_ReporteValor objValor = this.lstTitulos.get(i);
            //Validamos si es una imagen la que hay que dibujar
            if (objValor.strUrlImg.equals("")) {
               PdfPCell cell = new PdfPCell();
               cell.setColspan(intColsPan);
               cell.setBorderWidth(this.intBorderWidth);

               cell.setHorizontalAlignment(Element.ALIGN_CENTER);
               //Imprimimos la frase
               Phrase phrase = new Phrase(objValor.strValor,
                       FontFactory.getFont("Arial", this.intFontSizeTitle2, Font.BOLD,
                               new BaseColor(0, 0, 0)));
               cell.setPhrase(phrase);
               cell.setNoWrap(true);
               table.addCell(cell);
               intConTitle++;
            } else {
               //Dibujamos la imagen
               try {
                  Image img = Image.getInstance(objValor.strUrlImg);
                  img.setAbsolutePosition(objValor.intX, objValor.intY);
                  img.scaleToFit(objValor.intWidth, objValor.intHeight);
                  int intAlign = 0;
                  if (objValor.strAlign.toLowerCase().equals("left")) {
                     intAlign = Element.ALIGN_LEFT;
                  }
                  if (objValor.strAlign.toLowerCase().equals("right")) {
                     intAlign = Element.ALIGN_RIGHT;
                  }
                  if (objValor.strAlign.toLowerCase().equals("center")) {
                     intAlign = Element.ALIGN_CENTER;
                  }
                  img.setAlignment(intAlign);
                  if (objValor.intPercent != 0) {
                     img.scalePercent(objValor.intPercent);
                  }
                  img.setAlt(objValor.strValor);
                  try {
                     canvas.addImage(img);
                  } catch (DocumentException ex) {
                     log.error("ex.getMessage() " + ex.getMessage());
                  }
               } catch (BadElementException ex) {
                  Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
               } catch (MalformedURLException ex) {
                  Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
               } catch (IOException ex) {
                  Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
               }

            }
         }
         //Aplica solo si queremos mostrar la fecha y hora de emision
         if (bolEmision) {
            Fechas fecha = new Fechas();

            PdfPCell cell = new PdfPCell();
            cell.setColspan(intColsPan);
            cell.setBorderWidth(this.intBorderWidth);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Imprimimos la frase
            Phrase phrase = new Phrase("Fecha Emision: " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActual(),
                    FontFactory.getFont("Arial", this.intFontSizeTitle2, Font.BOLD,
                            new BaseColor(0, 0, 0)));
            cell.setPhrase(phrase);
            cell.setNoWrap(true);
            table.addCell(cell);
            intConTitle++;
         }
         //intConTitle++;//Esta es por la columna de los titulos de los campos
         table.setHeaderRows(intConTitle);
      }

   }
   //Emite el contenido del reporte

   private void EmiteContenidoTXT(CIP_SubReport report) {
      char cTab9 = 9;
      //Imprimimos el titulo de las celdas
      String strEncabezados = "";
      for (int i = 0; i < report.getLstColumn().size(); i++) {
         CIP_ReporteColum col = report.getLstColumn().get(i);
         strEncabezados += col.strTitulo + cTab9;
      }
      //Imprimimos los encabezados
      if (this.outFile == null) {
         log.info(strEncabezados);
      } else {
         try {
            this.outFile.write(strEncabezados);
         } catch (IOException ex) {
            this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
         }
      }
      //Imprimimos el contenido de las celdas
      if (report.getIntContaCol() > 0) {
         for (int i = 0; i < report.getLstColumn().get(0).getNumRows(); i++) {
            String strRow = "";
            for (int j = 0; j < report.getLstColumn().size(); j++) {
               boolean bolShow = true;
               int intAncho = report.getLstColumn().get(j).intAncho;
               String strValor = report.getLstColumn().get(j).getData(i).strValor;
               String strAlinea = report.getLstColumn().get(j).getData(i).strAlign;
               //Validacion para alineacion especial o global
               if (strAlinea.equals("")) {
                  strAlinea = report.getLstColumn().get(j).strAlign;
               }
               if (strAlinea.equals("")) {
                  strAlinea = "Left";
               }
               if (strAlinea.toUpperCase().equals("R")) {
                  strAlinea = "right";
               }
               if (strAlinea.toUpperCase().equals("L")) {
                  strAlinea = "left";
               }
               if (strAlinea.toUpperCase().equals("C")) {
                  strAlinea = "center";
               }
               //Validacion para mostrarse
               if (strValor.trim().equals("")) {
                  if (j > 1) {
                     if (report.getLstColumn().get(j - 1).getData(i).intColspan != 0) {
                        bolShow = false;
                     }
                  }
               }
               //Si esta activado para mostrarse
               if (bolShow) {
                  //pdf.Cell intAncho,lstColumn(j).intAlto,strValor,strMargen,0,strAlinea,bolFill
                  strRow += strValor + cTab9;
               }
            }
            if (this.outFile == null) {
               log.error(strRow);
            } else {
               try {
                  this.outFile.write(strRow);
               } catch (IOException ex) {
                  this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
               }
            }
         }
      }
   }
   //Emite el contenido del reporte

   private String EmiteContenidoHTML(CIP_SubReport report) {
      String strHtml = "";
      char chr10 = 10;
      //Imprimimos el titulo de las celdas
      strHtml += "<tr>" + chr10;
      for (int i = 0; i < report.getLstColumn().size(); i++) {
         CIP_ReporteColum col = report.getLstColumn().get(i);
         strHtml += "<td align=\"" + col.strAlign + " \" bgcolor=\"#808080\">";
         strHtml += col.strTitulo;
         strHtml += "</td>" + chr10;
      }
      strHtml += "</tr>" + chr10;
      //Imprimimos el contenido de las celdas
      if (report.getIntContaCol() > 0) {
         for (int i = 0; i < report.getLstColumn().get(0).getNumRows(); i++) {
            strHtml += "<tr>";
            for (int j = 0; j < report.getLstColumn().size(); j++) {
               boolean bolShow = true;
               int intAncho = report.getLstColumn().get(j).intAncho;
               String strValor = report.getLstColumn().get(j).getData(i).strValor;
               String strAlinea = report.getLstColumn().get(j).getData(i).strAlign;
               //Validacion para alineacion especial o global
               if (strAlinea.equals("")) {
                  strAlinea = report.getLstColumn().get(j).strAlign;
               }
               if (strAlinea.equals("")) {
                  strAlinea = "Left";
               }
               if (strAlinea.toUpperCase().equals("R")) {
                  strAlinea = "right";
               }
               if (strAlinea.toUpperCase().equals("L")) {
                  strAlinea = "left";
               }
               if (strAlinea.toUpperCase().equals("C")) {
                  strAlinea = "center";
               }
               String strBGCOLOR = "";
               CIP_ReporteValor objVal = report.getLstColumn().get(j).getData(i);
               if (objVal.RGBColorFondo != null) {
                  if (objVal.RGBColorFondo.intR == 255 && objVal.RGBColorFondo.intG == 255 && objVal.RGBColorFondo.intB == 255) {
                  } else {
                     String hexstr1 = InttoHex(objVal.RGBColorFondo.intR);
                     String hexstr2 = InttoHex(objVal.RGBColorFondo.intG);
                     String hexstr3 = InttoHex(objVal.RGBColorFondo.intB);
                     strBGCOLOR = " bgcolor =\"#" + hexstr1 + hexstr2 + hexstr3 + "\" ";
                  }
               }
               //Validacion para mostrarse
               if (strValor.trim().equals("")) {
                  if (j > 1) {
                     if (report.getLstColumn().get(j - 1).getData(i).intColspan != 0) {
                        bolShow = false;
                     }
                  }
               }
               //Validacion para el colspan
               String strColspan = "";
               if (report.getLstColumn().get(j).getData(i).intColspan != 0) {
                  strColspan = " colspan='" + report.getLstColumn().get(j).getData(i).intColspan + "' ";
               }
               //Si esta activado para mostrarse
               if (bolShow) {
                  //Color Letra
                  String strColorFont1 = "";
                  String strColorFont2 = "";
                  if (objVal.RGBColorLetra != null) {
                     if (objVal.RGBColorLetra.intR == 0 && objVal.RGBColorLetra.intG == 0 && objVal.RGBColorLetra.intB == 0) {
                     } else {
                        String hexstr1 = InttoHex(objVal.RGBColorLetra.intR);
                        String hexstr2 = InttoHex(objVal.RGBColorLetra.intG);
                        String hexstr3 = InttoHex(objVal.RGBColorLetra.intB);
                        String strCOLOR = "#" + hexstr1 + hexstr2 + hexstr3 + "";
                        strColorFont1 = "<font color=\"" + strCOLOR + "\">";
                        strColorFont2 = "</font>";
                     }
                  }
                  strHtml += "<td align='" + strAlinea + "' " + strColspan + " " + strBGCOLOR + " NOWRAP>" + strColorFont1 + strValor + strColorFont2 + "</td>" + chr10;
               }
            }
            strHtml += "</tr>" + chr10;
         }
      }
      strHtml += "</table>" + chr10;
      return strHtml;
   }

   private void EmiteTitulosColsPDF(CIP_SubReport report) {
      //Imprimimos el subtitulo
      if (report.getStrTitulo() != null) {
         int intColsPan = report.getIntContaCol();
         PdfPCell cell = new PdfPCell();
         cell.setColspan(intColsPan);
         cell.setBorderWidth(this.intBorderWidth);
         cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         //Imprimimos la frase
         Phrase phrase = new Phrase(report.getStrTitulo(),
                 FontFactory.getFont("Arial", 10, Font.BOLD,
                         new BaseColor(0, 0, 0)));
         cell.setPhrase(phrase);
         cell.setNoWrap(true);
         report.getTable().addCell(cell);
      }
      //Imprimimos los titulos de las columnas
      int[] intColsWitdth = new int[report.getLstColumn().size()];
      //Imprimimos los encabezados de las celdas
      for (int i = 0; i < report.getLstColumn().size(); i++) {
         CIP_ReporteColum col = report.getLstColumn().get(i);
         PdfPCell cell = new PdfPCell();
         cell.setBorderWidth(this.intBorderWidth);
         if (col.strAlign.toLowerCase().equals("left")) {
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
         }
         if (col.strAlign.toLowerCase().equals("right")) {
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
         }
         if (col.strAlign.toLowerCase().equals("center")) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
         }
         //Imprimimos la frase
         Phrase phrase = new Phrase(col.strTitulo,
                 FontFactory.getFont(BaseFont.TIMES_ROMAN, col.intFontSizeTitle, Font.BOLD,
                         new BaseColor(0, 0, 0)));
         cell.setPhrase(phrase);
         //cell.setBackgroundColor(new BaseColor(objValor.RGBColorFondo.intR, objValor.RGBColorFondo.intG, objValor.RGBColorFondo.intB));
         cell.setBackgroundColor(new BaseColor(223, 223, 223));
         report.getTable().addCell(cell);
         intColsWitdth[i] = col.intAncho;
      }
      //Si esta definido el reporte para mostrar las columnas con un ancho fijo se hace
      if (this.bolAnchoFijo) {
         try {
            report.getTable().setWidths(intColsWitdth);
         } catch (DocumentException ex) {
            log.error(" " + ex.getMessage());
         }
      }
      if (report.getIntContaCol() > 0) {
         //Determinamos si hay datos para marcar el titulo de las columnas como header
         if (report.getLstColumn().get(0).getNumRows() > 0) {
            report.getTable().setHeaderRows(report.getTable().getHeaderRows() + 1);
         } else {
            report.getTable().setHeaderRows(0);
            /*Notificamos que no hubo informacion*/
            getMessageNoData(report);
         }
      }
   }
   //Emite el contenido del reporte

   private void EmiteContenidoPDF(CIP_SubReport report) {
      EmiteTitulosColsPDF(report);
      //Imprimimos el contenido de las celdas
      if (report.getIntContaCol() > 0) {
         for (int i = 0; i < report.getLstColumn().get(0).getNumRows(); i++) {
            int intColSpanRest = 0;
            for (int j = 0; j < report.getLstColumn().size(); j++) {
               boolean bolShow = true;
               int intAncho = report.getLstColumn().get(j).intAncho;
               String strValor = null;
               try {
                  strValor = report.getLstColumn().get(j).getData(i).strValor;
               } catch (java.lang.IndexOutOfBoundsException ex) {
                  log.error("Fallo el recuperar el dato..." + report.getLstColumn().get(j).strTitulo + " contador: " + i);
                  strValor = "";
               }
               //Validacion para nueva pagina
               if (!strValor.equals("[NEW_PAGE]")) {
                  //Url de la imagen
                  String strURLImgImg = report.getLstColumn().get(j).getData(i).strUrlImg;
                  int intXImg = report.getLstColumn().get(j).getData(i).intX;
                  int intYImg = report.getLstColumn().get(j).getData(i).intY;
                  int intWidthImg = report.getLstColumn().get(j).getData(i).intWidth;
                  int intHeightImg = report.getLstColumn().get(j).getData(i).intHeight;
                  int intPercentImg = report.getLstColumn().get(j).getData(i).intPercent;
                  int intBorderCell = report.getLstColumn().get(j).getData(i).getIntBorder();
                  float intBorderWidthCell = report.getLstColumn().get(j).getData(i).getIntBorderWidth();
                  float intHeightCell = report.getLstColumn().get(j).getData(i).getIntHeightCell();
                  //Definimos colores
                  CIP_ReporteRGB colorFondo = report.getLstColumn().get(j).getData(i).RGBColorFondo;
                  CIP_ReporteRGB colorLetra = report.getLstColumn().get(j).getData(i).RGBColorLetra;
                  int intFontSize = report.getLstColumn().get(j).intFontSize;
                  if (intFontSize != report.getLstColumn().get(j).getData(i).intFontSize) {
                     intFontSize = report.getLstColumn().get(j).getData(i).intFontSize;
                  }
                  int intFontStyle = report.getLstColumn().get(j).getData(i).intFontStyle;
                  String strAlinea = report.getLstColumn().get(j).getData(i).strAlign;
                  //Validacion para alineacion especial o global
                  if (strAlinea.equals("")) {
                     strAlinea = report.getLstColumn().get(j).strAlign;
                  }
                  if (strAlinea.equals("")) {
                     strAlinea = "Left";
                  }
                  if (strAlinea.toUpperCase().equals("R")) {
                     strAlinea = "right";
                  }
                  if (strAlinea.toUpperCase().equals("L")) {
                     strAlinea = "left";
                  }
                  if (strAlinea.toUpperCase().equals("C")) {
                     strAlinea = "center";
                  }
                  //Validacion para mostrarse
//                  if (strValor.trim().equals("")) {
//                     if (j > 1) {
//                        if (report.getLstColumn().get(j - 1).getData(i).intColspan != 0) {
//                           bolShow = false;
//                        }
//                     }
//                  }
                  //Validamos si se muestra la columna en base al colspan
                  if (intColSpanRest > 0) {
                     bolShow = false;
                     intColSpanRest--;//Restamos colspan
                  }
                  //Validacion para el colspan
                  int intColspan = 1;
                  if (report.getLstColumn().get(j).getData(i).intColspan != 0) {
                     intColspan = report.getLstColumn().get(j).getData(i).intColspan;
                     if (intColspan > 1) {
                        intColSpanRest = intColspan;
                     }
                  }
                  //Si esta activado para mostrarse
                  if (bolShow) {
                     PdfPCell cell = new PdfPCell();
                     if (intBorderCell != 0) {
                        cell.setBorderWidth(intBorderWidthCell);
                        cell.setBorder(intBorderCell);
                     } else {
                        cell.setBorderWidth(this.intBorderWidth);
                     }
                     if (intHeightCell != 0) {
                        cell.setFixedHeight(intHeightCell);
                     }
                     if (strAlinea.toLowerCase().equals("left")) {
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                     }
                     if (strAlinea.toLowerCase().equals("right")) {
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                     }
                     if (strAlinea.toLowerCase().equals("center")) {
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                     }
                     cell.setColspan(intColspan);
                     intColSpanRest--;//Restamos colspan
                     cell.setNoWrap(report.getLstColumn().get(j).bolNoWrap);
                     //Solo es texto
                     if (strURLImgImg.trim().equals("")) {
                        Phrase phrase = new Phrase(strValor,
                                FontFactory.getFont(BaseFont.TIMES_ROMAN, intFontSize, intFontStyle,
                                        new BaseColor(colorLetra.intR, colorLetra.intG, colorLetra.intB)));
                        cell.setPhrase(phrase);
                     } else {
                        try {
                           //Es una imagen
                           Image img = Image.getInstance(strURLImgImg);
                           img.setAbsolutePosition(intXImg, intYImg);
                           img.scaleToFit(intWidthImg, intHeightImg);
                           if (intPercentImg != 0) {
                              img.scalePercent(intPercentImg);
                           }
                           img.setAlt(strValor);
                           cell.setImage(img);
                        } catch (BadElementException ex) {
                           Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (MalformedURLException ex) {
                           Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                           Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }
                     cell.setBackgroundColor(new BaseColor(colorFondo.intR, colorFondo.intG, colorFondo.intB));
                     report.getTable().addCell(cell);
                  }
               } else {
                  //Agregamos la tabla
                  try {
                     this.document.add(report.getTable());
                  } catch (DocumentException ex) {
                     this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
                  }
                  //Nueva pagina
                  this.document.newPage();
                  EmiteEncabezadosPDF(report, 0);
                  EmiteTitulosColsPDF(report);
               }
            }
         }
      } else {
         //No hubo datos que mostrar quitamos los cabezeros para que por lo menos los imprima
         report.getTable().setHeaderRows(0);
      }
   }

   private void getMessageNoData(CIP_SubReport report) {
      PdfPCell cell = new PdfPCell();
      cell.setBorderWidth(this.intBorderWidth);
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setColspan(report.getIntContaCol());
      cell.setNoWrap(true);
      //Solo es texto
      Phrase phrase = new Phrase("NO SE ENCONTRO INFORMACION",
              FontFactory.getFont(BaseFont.TIMES_ROMAN, 15, 1,
                      new BaseColor(0, 0, 0)));
      cell.setPhrase(phrase);
      report.getTable().addCell(cell);
   }

   /**
    * Emite el reporte
    *
    * @param oConn Es la conexion a la bd
    */
   public void EmiteReportePDF(Conexion oConn) {
      this.oConn = oConn;
      //Validamos que siempre halla por lo menos una columna
      /*if (this.intContaCol == 0) {
       this.intContaCol = 1;
       }*/
      //Emite los encabezados del reporte
      //Reporte en TXT
      if (this.intTipoReporte == CIP_ReportPDF.TXT) {
         Iterator<CIP_SubReport> it = this.lstSubReports.iterator();
         while (it.hasNext()) {
            CIP_SubReport report = it.next();
            EmiteEncabezadosTXT(report);
            EmiteContenidoTXT(report);
         }
      }
      //Reporte en HTML
      if (this.intTipoReporte == CIP_ReportPDF.HTML) {
         String strHtml = "";
         //Iteramos por todos los reportes
         Iterator<CIP_SubReport> it = this.lstSubReports.iterator();
         while (it.hasNext()) {
            CIP_SubReport report = it.next();
            strHtml += EmiteEncabezadosHTML(report);
            strHtml += EmiteContenidoHTML(report);
         }
         //Valida si es nula la salida a la web sino lo manda solo a la consola
         if (this.out == null) {
            System.out.println(strHtml);
         } else {
            this.out.println(strHtml);
         }
      }
      //Reporte en XLS
      if (this.intTipoReporte == CIP_ReportPDF.EXCEL) {
         HSSFWorkbook wb = new HSSFWorkbook();
         HSSFSheet sheet = wb.createSheet("S" + this.strTitulo.replace(" ", ""));
         int intNumRow = 0;
         //Iteramos por todos los reportes
         Iterator<CIP_SubReport> it = this.lstSubReports.iterator();
         boolean bolMaster = true;
         while (it.hasNext()) {
            CIP_SubReport report = it.next();
            intNumRow = EmiteEncabezadosXLS(report, sheet, intNumRow, bolMaster);
            intNumRow = EmiteContenidoXLS(report, wb, sheet, intNumRow);
            if (bolMaster) {
               bolMaster = false;
            }
            intNumRow++;
         }
         if (this.outStream != null) {
            try {
               wb.write(this.outStream);
            } catch (IOException ex) {
               Logger.getLogger(CIP_ReportPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      }
      //Reporte en PDF
      if (this.intTipoReporte == CIP_ReportPDF.PDF) {
         if (this.document != null) {
            //Parametros generales del documento
            this.document.addCreationDate();
            this.document.addTitle(this.strTitulo);
            this.document.addCreator(this.strTitulo);
            this.document.addAuthor(this.strTitulo);
            this.document.resetPageCount();
            if (this.strTamHoja.equals("letter")) {
               //Landscape
               if (this.strOrientation.equals("horizontal")) {
                  this.document.setPageSize(PageSize.LETTER);
               } else {
                  this.document.setPageSize(PageSize.LETTER.rotate());
               }
            }
            if (this.strTamHoja.equals("A4")) {
               //Landscape
               if (this.strOrientation.equals("horizontal")) {
                  this.document.setPageSize(PageSize.A4);
               } else {
                  this.document.setPageSize(PageSize.A4.rotate());
               }
            }
            if (this.strTamHoja.equals("LEGAL")) {
               //Landscape
               if (this.strOrientation.equals("horizontal")) {
                  this.document.setPageSize(PageSize.LEGAL);
               } else {
                  this.document.setPageSize(PageSize.LEGAL.rotate());
               }
            }
            this.document.newPage();
            //Iteramos por todos los reportes
            Iterator<CIP_SubReport> it = this.lstSubReports.iterator();
            int intConta = -1;
            while (it.hasNext()) {
               CIP_SubReport report = it.next();
               intConta++;
               //Imprimimos el reporte
               EmiteEncabezadosPDF(report, intConta);
               EmiteContenidoPDF(report);
               //Agregamos la tabla al documento
               try {
                  this.document.add(report.getTable());
               } catch (DocumentException ex) {
                  this.bitacora.GeneraBitacora(ex.getMessage(), "System", "ReportPDF", null);
               }
            }
         }
      }
      //Reporte en WORD
      //Reporte por mail en PDF
   }

   /**
    * Convierte un numero RGB(Decimal) a Hexadecimal
    */
   private static String InttoHex(int intValue) {
      int i = intValue;
      return Integer.toHexString(0x100 | i).substring(1).toUpperCase();
   }

   /**
    * Emite el encabezado del reporte en un archivo XLS por medio de POI
    *
    * @param report Es el reporte por imprimir
    * @param sheet Es el sheet
    * @param intNumRow Es el numero de row
    * @param bolMaster Indica si es la hoja maestra
    * @return Regresa el contador de filas
    */
   public int EmiteEncabezadosXLS(CIP_SubReport report, HSSFSheet sheet, int intNumRow, boolean bolMaster) {
      // Create a row and put some cells in it. Rows are 0 based.

      HSSFRow row = sheet.createRow(intNumRow);
      //Imprimimos los encabezados definidos
      if (bolMaster) {
         row.createCell(1).setCellValue(this.strTitulo);
      } else {
         //Titulos adicionales
         if (report.getStrTitulo() != null) {
            row.createCell(1).setCellValue(report.getStrTitulo());
         }
      }
      if (bolMaster) {
         for (int i = 0; i < this.intContaTitulos; i++) {
            CIP_ReporteValor objValor = this.lstTitulos.get(i);
            //Create new row
            intNumRow++;
            HSSFRow rowTit = sheet.createRow(intNumRow);
            rowTit.createCell(1).setCellValue(objValor.strValor);
         }
      }
      //Aplica solo si queremos mostrar la fecha y hora de emision
      if (bolMaster) {
         if (bolEmision) {
            Fechas fecha = new Fechas();
            //Create new row
            intNumRow++;
            HSSFRow rowTit = sheet.createRow(intNumRow);
            rowTit.createCell(1).setCellValue("Fecha Emision: " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActual());
         }
      }
      return intNumRow;
   }

   /**
    * Emite el contenido del reporte en un archivo XLS por medio de POI
    *
    * @param report Es el reporte por imprimir
    * @param wb Es el workbook
    * @param sheet Es el sheet
    * @param intNumRow Es el numero de row
    * @return Regresa el contador de filas
    */
   public int EmiteContenidoXLS(CIP_SubReport report, HSSFWorkbook wb, HSSFSheet sheet, int intNumRow) {
      // Create a row 
      intNumRow++;
      HSSFRow row = sheet.createRow(intNumRow);
      //Imprimimos el titulo de las celdas
      for (int i = 0; i < report.getLstColumn().size(); i++) {
         CIP_ReporteColum col = report.getLstColumn().get(i);
         short sAlign = 0;
         short sDegree = 0;
         short sVAlign = CellStyle.ALIGN_GENERAL;
         if (col.strAlign.equals("left")) {
            sAlign = CellStyle.ALIGN_LEFT;
         }
         if (col.strAlign.equals("right")) {
            sAlign = CellStyle.ALIGN_RIGHT;
         }
         if (col.strAlign.equals("center")) {
            sAlign = CellStyle.ALIGN_CENTER;
         }
         if (col.strAlign.equals("justify")) {
            sAlign = CellStyle.ALIGN_JUSTIFY;
         }
         if (col.strAlign.equals("inverse")) {
            sAlign = CellStyle.ALIGN_LEFT;
            sVAlign = CellStyle.VERTICAL_TOP;
            sDegree = 90;
         }
         short sColumn = (short) i;
         Cell cell = this.createCell(wb, row, sColumn, sAlign, sVAlign, sDegree, new CIP_ReporteRGB(223, 223, 223), new CIP_ReporteRGB(0, 0, 0), col.strTitulo, false);
      }

      //Imprimimos el contenido de las celdas
      if (report.getIntContaCol() > 0) {
         for (int i = 0; i < report.getLstColumn().get(0).getNumRows(); i++) {
            // Create a row
            intNumRow++;
            HSSFRow rowD = sheet.createRow(intNumRow);
            int intColSpanRest = 0;
            for (int j = 0; j < report.getLstColumn().size(); j++) {
               boolean bolShow = true;
               boolean bolFormatoNumber = report.getLstColumn().get(j).bolFormatoNumber;
               String strValor = report.getLstColumn().get(j).getData(i).strValor;
               String strAlinea = report.getLstColumn().get(j).getData(i).strAlign;
               String strAlineaGlobal = report.getLstColumn().get(j).getData(i).strAlign;
               //Validacion para alineacion especial o global
               if (strAlinea.equals("")) {
                  strAlinea = report.getLstColumn().get(j).strAlign;
               }
               if (strAlinea.equals("")) {
                  strAlinea = "left";
               }
               if (strAlinea.toUpperCase().equals("R")) {
                  strAlinea = "right";
               }
               if (strAlinea.toUpperCase().equals("L")) {
                  strAlinea = "left";
               }
               if (strAlinea.toUpperCase().equals("C")) {
                  strAlinea = "center";
               }
               CIP_ReporteValor objVal = report.getLstColumn().get(j).getData(i);
               //Validamos si se muestra la columna en base al colspan
               if (intColSpanRest > 0) {
                  bolShow = false;
                  intColSpanRest--;//Restamos colspan
               }
               //Validacion para mostrarse
//               if (strValor.trim().equals("")) {
//                  if (j > 1) {
//                     if (report.getLstColumn().get(j - 1).getData(i).intColspan != 0) {
//                        bolShow = false;
//                     }
//                  }
//               }
               //Validacion para el colspan
               int intColspan = 1;
               if (report.getLstColumn().get(j).getData(i).intColspan != 0) {
                  intColspan = report.getLstColumn().get(j).getData(i).intColspan;
                  if (intColspan > 1) {
                     intColSpanRest = intColspan;
                  }
               }

               //Si esta activado para mostrarse
               if (bolShow) {
                  intColSpanRest--;//Restamos colspan
                  short sAlign = 0;
                  short sDegree = 0;
                  short sVAlign = CellStyle.ALIGN_GENERAL;
                  if (strAlinea.equals("left")) {
                     sAlign = CellStyle.ALIGN_LEFT;
                  }
                  if (strAlinea.equals("right")) {
                     sAlign = CellStyle.ALIGN_RIGHT;
                  }
                  if (strAlinea.equals("center")) {
                     sAlign = CellStyle.ALIGN_CENTER;
                  }
                  if (strAlinea.equals("justify")) {
                     sAlign = CellStyle.ALIGN_JUSTIFY;
                  }
                  if (strAlinea.equals("inverse")) {
                     sAlign = CellStyle.ALIGN_LEFT;
                     sVAlign = CellStyle.VERTICAL_TOP;
                     sDegree = 90;
                  }
                  short sColumn = (short) j;
                  //Validamos si usamos el formato default
                  boolean bolFormatoDefault = true;
                  //Color Letra
                  if (objVal.RGBColorLetra != null) {
                     if (objVal.RGBColorLetra.intR == 0 && objVal.RGBColorLetra.intG == 0 && objVal.RGBColorLetra.intB == 0) {
                     } else {
                        bolFormatoDefault = false;
                     }
                  }
                  if (objVal.RGBColorFondo != null) {
                     if (objVal.RGBColorFondo.intR == 255 && objVal.RGBColorFondo.intG == 255 && objVal.RGBColorFondo.intB == 255) {
                     } else {
                        //Color de fondo diferente
                        bolFormatoDefault = false;
                     }
                  }
                  if (!strAlineaGlobal.equals(strAlinea)) {
                     bolFormatoDefault = false;
                  }
                  //Mostramos la celda
                  if (!bolFormatoDefault) {
                     //Si hay un colo ponemos un estilo diferente
                     Cell cell = this.createCell(wb, rowD, sColumn, sAlign, sVAlign, sDegree,
                             objVal.RGBColorFondo, objVal.RGBColorLetra, strValor, bolFormatoNumber);
                  } else {
                     //Alineacion
                     HSSFCell cell = rowD.createCell(j);
                     CellStyle cellStyle = wb.createCellStyle();
                     cellStyle.setAlignment(sAlign);
                     cellStyle.setVerticalAlignment(sVAlign);
                     cell.setCellStyle(cellStyle);
                     //Formato de numeros
                     if (bolFormatoNumber) {
                        double dblNumber = 0;
                        try {
                           dblNumber = Double.valueOf(strValor);
                           cell.setCellValue(dblNumber);
                        } catch (NumberFormatException ex) {
                           cell.setCellValue(strValor);
                        }
                     } else {
                        cell.setCellValue(strValor);
                     }
                  }
                  //Create new rows for colspan
//                  for (int hk = sColumn; hk < (sColumn + intColspan); hk++) {
//                     short sCol2 = (short) hk;
//                     rowD.createCell(sCol2).setCellValue("");
//                  }
               }
            }
         }
      }
      return intNumRow;
   }

   /**
    * Creates a cell and aligns it a certain way.
    *
    * @param wb the workbook
    * @param row the row to create the cell in
    * @param column the column number to create the cell in
    * @param halign the horizontal alignment for the cell.
    * @param valign Es la alineacion vertical
    * @param sdegree Es la rotacion del texto
    * @param RGBColorFondo Es el color de fondo
    * @param RGBColorLetra Es el color de la letra
    * @param strValor Es el valor a mostrar
    * @param bolFormatNumber Parametro para indicar si se formateara como numero
    * @return Regresa una celda con estilo
    */
   protected Cell createCell(HSSFWorkbook wb, Row row, short column, short halign, short valign, short sdegree,
           CIP_ReporteRGB RGBColorFondo, CIP_ReporteRGB RGBColorLetra, String strValor, boolean bolFormatNumber) {
      Cell cell = row.createCell(column);
      CellStyle cellStyle = wb.createCellStyle();
      cellStyle.setAlignment(halign);
      cellStyle.setVerticalAlignment(valign);
      if (sdegree != 0) {
         cellStyle.setRotation(sdegree);
      }
      cell.setCellStyle(cellStyle);
      //Si el color no es nulo proseguimos
      if (RGBColorFondo != null) {
         Color color = new Color(RGBColorFondo.intR, RGBColorFondo.intG, RGBColorFondo.intB);
         HSSFPalette palette = wb.getCustomPalette();
         HSSFColor hssfColor = palette.findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
         if (hssfColor == null) {
            palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte) color.getRed(), (byte) color.getGreen(),
                    (byte) color.getBlue());
            hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
         }
         //Si se pudo crear el color proseguimos
         if (hssfColor != null) {
            cellStyle.setFillForegroundColor(hssfColor.getIndex());
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
         } else {
            if (RGBColorFondo.intR == 0 && RGBColorFondo.intG == 255 && RGBColorFondo.intB == 0) {
               cellStyle.setFillForegroundColor(new HSSFColor.GREEN().getIndex());
               cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            if (RGBColorFondo.intR == 255 && RGBColorFondo.intG == 0 && RGBColorFondo.intB == 0) {
               cellStyle.setFillForegroundColor(new HSSFColor.RED().getIndex());
               cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            if (RGBColorFondo.intR == 255 && RGBColorFondo.intG == 255 && RGBColorFondo.intB == 0) {
               cellStyle.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());
               cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
            if (RGBColorFondo.intR == 128 && RGBColorFondo.intG == 128 && RGBColorFondo.intB == 128) {
               cellStyle.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());
               cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            }
         }
      }
      if (RGBColorLetra != null) {
         Color color = new Color(RGBColorLetra.intR, RGBColorLetra.intG, RGBColorLetra.intB);
         //Color de letra
         HSSFPalette palette = wb.getCustomPalette();
         HSSFColor hssfColor = palette.findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
         if (hssfColor == null) {
            palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte) color.getRed(), (byte) color.getGreen(),
                    (byte) color.getBlue());
            hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
         }
         //Si se pudo crear el color proseguimos
         if (hssfColor != null) {

            //cellStyle.setFillBackgroundColor(hssfColor.getIndex());
            //cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 12);
            font.setColor(hssfColor.getIndex());
            cellStyle.setFont(font);
         } else {
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 12);
            if (RGBColorLetra.intR == 0 && RGBColorLetra.intG == 255 && RGBColorLetra.intB == 0) {
               font.setColor(new HSSFColor.LIGHT_GREEN().getIndex());
            }
            if (RGBColorLetra.intR == 255 && RGBColorLetra.intG == 0 && RGBColorLetra.intB == 0) {
               font.setColor(new HSSFColor.RED().getIndex());
            }
            if (RGBColorLetra.intR == 255 && RGBColorLetra.intG == 255 && RGBColorLetra.intB == 0) {
               font.setColor(new HSSFColor.YELLOW().getIndex());
            }
            if (RGBColorLetra.intR == 128 && RGBColorLetra.intG == 128 && RGBColorLetra.intB == 128) {
               font.setColor(new HSSFColor.GREY_25_PERCENT().getIndex());
            }
            if (RGBColorLetra.intR == 255 && RGBColorLetra.intG == 255 && RGBColorLetra.intB == 255) {
               font.setColor(new HSSFColor.WHITE().getIndex());
            }
            cellStyle.setFont(font);
         }
      }
      if (bolFormatNumber) {
         double dblNumber = 0;
         try {
            dblNumber = Double.valueOf(strValor);
            cell.setCellValue(dblNumber);
         } catch (NumberFormatException ex) {
            cell.setCellValue(strValor);
         }
      } else {
         cell.setCellValue(strValor);
      }
      return cell;
   }
   // </editor-fold>
}
