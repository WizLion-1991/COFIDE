package comSIWeb.Operaciones.Reportes;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import generated.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Genera el formato
 *
 * @author zeus
 */
public class CIP_Formato extends CIP_ReportPDF {

   private Header head;
   private Body body;
   private Footer foot;
   private Config cnf;
   private Title tit;
   private TypePage tpage;
   private Orientation ori;
   private LeftMargin left;
   private RightMargin right;
   private TopMargin top;
   private BottomMargin bot;
   private Width width;
   private Height height;
   private TotalPages tot;
   private PdfWriter writer;
   private String strPathFonts;
   private int intXMIN;
   private int intYMIN;
   private int intLastPY;
   protected boolean bolDosxHoja;
   protected boolean bolCuatroxHoja;
   protected boolean bolSeisxHoja;
   private float fltWidthTable;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CIP_Formato.class.getName());

   /**
    * Constructor del objeto que genera formatos en PDF a partir del XML
    */
   public CIP_Formato() {
      super("horizontal", "letter");
      this.setIntTipoReporte(CIP_Formato.PDF);
      this.strPathFonts = "";
      this.intXMIN = 99999;
      this.intYMIN = 99999;
      this.intLastPY = 0;
      this.bolDosxHoja = false;
      this.bolCuatroxHoja = false;
      this.bolSeisxHoja = false;
      this.fltWidthTable = 523;
   }

   /**
    * Este metodo lee el objeto formatoXML y con el genera una impresion en PDF
    *
    * @param fmXML Es el objeto del formato
    * @return Nos regresa OK en caso de que todo halla sido exitoso
    * @throws DocumentException Exception en el documento
    */
   public String EmiteFormato(Formato fmXML) throws DocumentException {
      String strRes = "OK";
      //Parseamos el XML
      parseaXML(fmXML);
      //En base al XML generamos el formato
      //TXT
      if (this.getIntTipoReporte() == CIP_Formato.TXT) {
      }
      //HTML
      if (this.getIntTipoReporte() == CIP_Formato.HTML) {
      }
      //EXCEL
      if (this.getIntTipoReporte() == CIP_Formato.EXCEL) {
      }
      //PDF
      if (this.getIntTipoReporte() == CIP_Formato.PDF) {
         if (this.getDocument() != null && this.writer != null) {
            Document doc = this.getDocument();
            //Parametros generales del documento
            doc.addCreationDate();
            doc.addTitle(this.tit.getvalue());
            doc.addCreator(this.tit.getvalue());
            doc.addAuthor(this.tit.getvalue());
            doc.resetPageCount();
            PdfPTable table = new PdfPTable(1);
            table.setTotalWidth(doc.right() - doc.left());
            table.setHorizontalAlignment(100);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            //Definimos el tamanio de la pagina
            this.setPageSize(doc);
            doc.newPage();
            //Objeto para pintar en el canvas el formato
            PdfContentByte canvas = writer.getDirectContent();
            //Recorremos los encabezados
            Iterator<HP> ihp = this.head.getHP().iterator();
            while (ihp.hasNext()) {
               HP hp = ihp.next();
               DrawHp(hp, canvas, table);

            }
            //Recorremos el detalle
            Iterator<Page> itPage = this.body.getPage().iterator();
            int intCountPage = -1;
            while (itPage.hasNext()) {
               intCountPage++;
               Page page = itPage.next();
               ihp = page.getHP().iterator();
               while (ihp.hasNext()) {
                  HP hp = ihp.next();
                  DrawHp(hp, canvas, table);
               }
               //Solo si son mas de 1 pagina
               if (this.body.getPage().size() > 1) {
                  //Ultima Hoja
                  if (intCountPage + 1 == this.body.getPage().size()) {
                  } else {
                     //Intermedias
                     //Pintamos el pie
                     //Recorremos el pie de pagina
                     ihp = this.foot.getHP().iterator();
                     doc.newPage();
                     this.intYMIN = 99999;
                     //Pintamos los encabezados
                     //Recorremos los encabezados
                     Iterator<HP> ihpH = this.head.getHP().iterator();
                     while (ihpH.hasNext()) {
                        HP hp = ihpH.next();
                        DrawHp(hp, canvas, table);
                     }
                  }
               }

            }
            //Recorremos el pie de pagina
            ihp = this.foot.getHP().iterator();

            while (ihp.hasNext()) {
               HP hp = ihp.next();
               DrawHp(hp, canvas, table);
            }
            this.intYMIN -= 5;
            //Agregamos la tabla
            if (!table.getRows().isEmpty()) {
               table.completeRow();
               table.writeSelectedRows(0, table.getRows().size(), doc.getPageSize().getWidth() / 10, this.intYMIN, canvas);
            }
         }
      }
      return strRes;
   }

   /**
    * Este metodo lee el objeto formatoXML y con el genera una impresion en PDF
    *
    * @param fmXML Es el objeto del formato
    * @return Nos regresa OK en caso de que todo halla sido exitoso
    * @throws DocumentException Exception en el documento
    */
   public String EmiteFormatoMasivo(Formato fmXML) throws DocumentException {
      String strRes = "OK";

      //En base al XML generamos el formato
      //TXT
      if (this.getIntTipoReporte() == CIP_Formato.TXT) {
      }
      //HTML
      if (this.getIntTipoReporte() == CIP_Formato.HTML) {
      }
      //EXCEL
      if (this.getIntTipoReporte() == CIP_Formato.EXCEL) {
      }
      //PDF
      if (this.getIntTipoReporte() == CIP_Formato.PDF) {
         if (this.getDocument() != null && this.writer != null) {
            //Contador para las hojas o documentos por imprimir
            int intContaDocs = 0;
            //Recuperamos la configuracion
            Iterator itN = fmXML.getFooterOrBodyOrHeaderOrConfig().listIterator();
            while (itN.hasNext()) {
               Object obj = itN.next();
               if (obj.getClass().getCanonicalName().equals("generated.Config")) {
                  cnf = (Config) obj;
                  ParseaConfig(cnf);
               }
               if (obj.getClass().getCanonicalName().equals("generated.Footer")) {
                  intContaDocs++;
               }
            }
            //Documento para el PDF
            Document doc = this.getDocument();
            //Parametros generales del documento
            doc.addCreationDate();
            doc.addTitle(this.tit.getvalue());
            doc.addCreator(this.tit.getvalue());
            doc.addAuthor(this.tit.getvalue());
            doc.resetPageCount();
            //Tabla para la parte inferior
            PdfPTable table = new PdfPTable(1);
            table.setTotalWidth(doc.right() - doc.left());
            table.setHorizontalAlignment(100);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setTotalWidth(this.fltWidthTable);
            //Definimos el tamanio de la pagina
            this.setPageSize(doc);
            doc.newPage();
            //Objeto para pintar en el canvas el formato
            PdfContentByte canvas = writer.getDirectContent();

            //Parseamos el XML
            //Recuperamos los objetos del XML
            int intCont = 0;
            boolean bolFoot = false;
            itN = fmXML.getFooterOrBodyOrHeaderOrConfig().listIterator();
            int intPar = -1;
            while (itN.hasNext()) {
               Object obj = itN.next();
               if (obj.getClass().getCanonicalName().equals("generated.Header")) {
                  head = (Header) obj;
                  intCont++;
                  //Si es la segunda hoja creamos nuevas tablas
                  if (intCont > 1) {
                     table = new PdfPTable(1);
                     table.setTotalWidth(doc.right() - doc.left());
                     table.setHorizontalAlignment(100);
                     table.setHorizontalAlignment(Element.ALIGN_CENTER);
                     table.setTotalWidth(this.fltWidthTable);
                     this.intYMIN = 99999;
                  }
                  bolFoot = false;
               }
               if (obj.getClass().getCanonicalName().equals("generated.Body")) {
                  body = (Body) obj;
               }
               if (obj.getClass().getCanonicalName().equals("generated.Footer")) {
                  foot = (Footer) obj;
                  bolFoot = true;
               }
               if (obj.getClass().getCanonicalName().equals("generated.Config")) {
                  bolFoot = false;
               }
               //Si es un foot generamos parte del formato
               if (bolFoot) {
                  intPar++;
                  if (this.bolDosxHoja) {
                     if (intPar == 1 && this.bolDosxHoja) {
                        //
                     } else {
                        //Anadimos una pagina por cada formato
                        if (intCont > 1 && intCont <= intContaDocs) {
                           doc.newPage();
                        }
                     }
                  } else {
                     if (this.bolCuatroxHoja || this.bolSeisxHoja) {
                     } else {
                        //Anadimos una pagina por cada formato
                        if (intCont > 1 && intCont <= intContaDocs) {
                           doc.newPage();
                        }
                     }
                  }
                  //************Comenzamos a generar el formato
                  //Recorremos los encabezados
                  Iterator<HP> ihp = this.head.getHP().iterator();
                  while (ihp.hasNext()) {
                     HP hp = ihp.next();
                     DrawHp(hp, canvas, table);

                  }
                  //Recorremos el detalle
                  int intContaPages = 0;
                  Iterator<Page> itPage = this.body.getPage().iterator();
                  while (itPage.hasNext()) {
                     Page page = itPage.next();
                     intContaPages++;
                     if (intContaPages > 1) {
                        doc.newPage();
                     }

                     ihp = page.getHP().iterator();
                     while (ihp.hasNext()) {
                        HP hp = ihp.next();
                        DrawHp(hp, canvas, table);
                     }
                  }
                  //Recorremos el pie de pagina
                  ihp = this.foot.getHP().iterator();
                  while (ihp.hasNext()) {
                     HP hp = ihp.next();
                     DrawHp(hp, canvas, table);
                  }
                  this.intYMIN -= intLastPY;
                  //Agregamos la tabla
                  if (!table.getRows().isEmpty()) {
                     table.completeRow();
                     if (this.intYMIN < 0) {
                        this.intYMIN = Integer.valueOf(top.getvalue());
                     }
                     table.writeSelectedRows(0, table.getRows().size(), doc.getPageSize().getWidth() / 10, this.intYMIN, canvas);
                  }
                  this.head.getHP().clear();
                  this.body.getPage().clear();
                  this.foot.getHP().clear();
                  //*****Terminamos de generar formato
                  // <editor-fold defaultstate="collapsed" desc="Dos por hoja">
                  if (this.bolDosxHoja) {
                     if (intPar == 1) {
                        intPar = -1;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Cuatro por hoja">
                  if (this.bolCuatroxHoja) {
                     if (intPar < 3) {
                        //
                     } else {
                        if (intPar == 3) {
                           doc.newPage();
                        } else {
                           //Anadimos una pagina por cada formato
                           if (intCont > 1 && intCont <= intContaDocs) {
                              doc.newPage();
                           }
                        }

                     }
                     if (intPar == 3) {
                        intPar = -1;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Seis por hoja">
                  if (this.bolSeisxHoja) {
                     if (intPar < 5) {
                        //
                     } else {
                        if (intPar == 5) {
                           doc.newPage();
                        } else {
                           //Anadimos una pagina por cada formato
                           if (intCont > 1 && intCont <= intContaDocs) {
                              doc.newPage();
                           }
                        }

                     }
                     if (intPar == 5) {
                        intPar = -1;
                     }
                  }
                  // </editor-fold>
               }
            }


         }
      }
      return strRes;
   }

   /**
    * Define el tamanio de la hoja
    */
   private void setPageSize(Document doc) {
      if (this.tpage.getvalue().equals("letter")) {
         //Landscape
         if (this.ori.getvalue().equals("horizontal")) {
            doc.setPageSize(PageSize.LETTER);
         } else {
            doc.setPageSize(PageSize.LETTER.rotate());
         }
      }
      if (this.tpage.getvalue().equals("A4")) {
         //Landscape
         if (this.ori.getvalue().equals("horizontal")) {
            doc.setPageSize(PageSize.A4);
         } else {
            doc.setPageSize(PageSize.A4.rotate());
         }
      }
      if (this.tpage.getvalue().equals("A6")) {
         //Landscape
         if (this.ori.getvalue().equals("horizontal")) {
            doc.setPageSize(PageSize.A6);
         } else {
            doc.setPageSize(PageSize.A6.rotate());
         }
      }
      if (this.tpage.getvalue().equals("EXECUTIVE")) {
         //Landscape
         if (this.ori.getvalue().equals("horizontal")) {
            doc.setPageSize(PageSize.EXECUTIVE);
         } else {
            doc.setPageSize(PageSize.EXECUTIVE.rotate());
         }
      }
      if (this.tpage.getvalue().equals("HALFLETTER")) {
         //Landscape
         if (this.ori.getvalue().equals("horizontal")) {
            doc.setPageSize(PageSize.HALFLETTER);
         } else {
            doc.setPageSize(PageSize.HALFLETTER.rotate());
         }
      }
      if (this.tpage.getvalue().equals("NOTE")) {
         //Landscape
         if (this.ori.getvalue().equals("horizontal")) {
            doc.setPageSize(PageSize.NOTE);
         } else {
            doc.setPageSize(PageSize.NOTE.rotate());
         }
      }
      if (this.tpage.getvalue().contains(",")) {
         String[] strValues = this.tpage.getvalue().split(",");
         if (strValues.length == 2) {
            int intValue1 = 0;
            int intValue2 = 0;
            try {
               intValue1 = Integer.valueOf(strValues[0]);
               intValue2 = Integer.valueOf(strValues[1]);
               Rectangle PAGESIZEUSER = new RectangleReadOnly(intValue1, intValue2);
               //Landscape
               if (this.ori.getvalue().equals("horizontal")) {
                  doc.setPageSize(PAGESIZEUSER);
               } else {
                  doc.setPageSize(PAGESIZEUSER.rotate());
               }
            } catch (NumberFormatException ex) {
               ex.printStackTrace();
            }
         }
      }
   }

   /**
    * Dibuja en el pdf los datos del XML
    */
   private void DrawHp(HP hp, PdfContentByte canvas, PdfPTable table) {
      //Validamos el color
      BaseColor colorDraw = new BaseColor(0, 0, 0);
      if (hp.getPcolor().contains("{") && hp.getPcolor().contains("}") && hp.getPcolor().contains(",")) {
         String strColorRGB = new String(hp.getPcolor().replace("{", ""));
         strColorRGB = strColorRGB.replace("}", "");
         String[] lstColor = strColorRGB.split(",");
         if (lstColor.length == 3) {
            try {
               int intR = Integer.valueOf(lstColor[0]);
               int intG = Integer.valueOf(lstColor[1]);
               int intB = Integer.valueOf(lstColor[2]);
               colorDraw = new BaseColor(intR, intG, intB);
            } catch (NumberFormatException ex) {
               System.out.println(ex.getMessage());
            }
         }
      }
      canvas.setColorFill(colorDraw);
      // <editor-fold defaultstate="collapsed" desc="Alineación">
      int intAlign = 0;
      if (hp.getPalign().equals("0")) {
         intAlign = Element.ALIGN_LEFT;
      }
      if (hp.getPalign().equals("2")) {
         intAlign = Element.ALIGN_RIGHT;
      }
      if (hp.getPalign().equals("1")) {
         intAlign = Element.ALIGN_CENTER;
      }
      if (hp.getPalign().equals("3")) {
         intAlign = Element.ALIGN_JUSTIFIED;
      }
      // </editor-fold>
      if (hp.getUrlimg().equals("")) {
         //BaseFont m_BaseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
         // <editor-fold defaultstate="collapsed" desc="Calculo de la fuente">
         BaseFont m_BaseFont = null;
         int intFontSize = 10;
         String strFont = BaseFont.HELVETICA;
         if (Integer.valueOf(hp.getPsize()) != 0) {
            intFontSize = Integer.valueOf(hp.getPsize());
         }
         try {
            if (hp.getPfont().equals("Times")) {
               strFont = BaseFont.TIMES_ROMAN;
               //Oblicua
               if (hp.getPstyle().equals("1")) {
                  strFont = BaseFont.TIMES_ITALIC;
               }
               //Negritas
               if (hp.getPstyle().equals("2")) {
                  strFont = BaseFont.TIMES_BOLD;
               }
               //Oblicua y negritas
               if (hp.getPstyle().equals("3")) {
                  strFont = BaseFont.TIMES_BOLDITALIC;
               }
            } else {
               if (hp.getPfont().equals("Courier")) {
                  strFont = BaseFont.COURIER;
                  //Oblicua
                  if (hp.getPstyle().equals("1")) {
                     strFont = BaseFont.COURIER_OBLIQUE;
                  }
                  //Negritas
                  if (hp.getPstyle().equals("2")) {
                     strFont = BaseFont.COURIER_BOLD;
                  }
                  //Oblicua y negritas
                  if (hp.getPstyle().equals("3")) {
                     strFont = BaseFont.COURIER_BOLDOBLIQUE;
                  }
               } else {
                  if (hp.getPfont().equals("Helvetica")) {
                     strFont = BaseFont.HELVETICA;
                     //Oblicua
                     if (hp.getPstyle().equals("1")) {
                        strFont = BaseFont.HELVETICA_OBLIQUE;
                     }
                     //Negritas
                     if (hp.getPstyle().equals("2")) {
                        strFont = BaseFont.HELVETICA_BOLD;
                     }
                     //Oblicua y negritas
                     if (hp.getPstyle().equals("3")) {
                        strFont = BaseFont.HELVETICA_BOLDOBLIQUE;
                     }
                  } else {
                     if (hp.getPfont().startsWith("USR:")) {
                        String strNameFontUser = this.getStrPathFonts() + System.getProperty("file.separator") + hp.getPfont().replace("USR:", "") + ".ttf";
                        File fileFonts = new File(strNameFontUser);
                        if (fileFonts.exists()) {
                           m_BaseFont = BaseFont.createFont(strNameFontUser, BaseFont.CP1252, BaseFont.EMBEDDED);
                        }
                     } else {
                        //En caso de que no defina la fuente
                        strFont = BaseFont.HELVETICA;
                        //Oblicua
                        if (hp.getPstyle().equals("1")) {
                           strFont = BaseFont.HELVETICA_OBLIQUE;
                        }
                        //Negritas
                        if (hp.getPstyle().equals("2")) {
                           strFont = BaseFont.HELVETICA_BOLD;
                        }
                        //Oblicua y negritas
                        if (hp.getPstyle().equals("3")) {
                           strFont = BaseFont.HELVETICA_BOLDOBLIQUE;
                        }
                     }
                  }
               }
            }
            //Definimos la fuente
            if (m_BaseFont == null) {
               m_BaseFont = BaseFont.createFont(strFont, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            }
            canvas.setFontAndSize(m_BaseFont, intFontSize);
         } catch (DocumentException ex) {
            log.error(ex.getMessage());
         } catch (IOException ex) {
            log.error(ex.getMessage());
         }
         // </editor-fold>
         /*(BaseFont)FontFactory.getFont("Arial", 11, Font.BOLD,new BaseColor(0, 0, 0));*/
         //Pintamos el texto
         String strValor = hp.getValor();

         if (hp.getPfont().equals("USR:3OF9")) {
            strValor = "*" + strValor + "*";
         }
         //Validamos si hay algun formato especial como el autoajuste(usado para imprimir toda la firma electronica
         boolean bolPintar = true;
         //Formato para pintarlo como tabla al final
         if (hp.getPformato().endsWith("AutoAjust") || hp.getPformato().equals("AutoParagraph")) {
            bolPintar = false;
            int intStringSize = Integer.valueOf(hp.getPwidth());
            // <editor-fold defaultstate="collapsed" desc="Estilo de fuente">
            int intFontStyle = Font.NORMAL;
            //Oblicua
            if (hp.getPstyle().equals("1")) {
               intFontStyle = Font.ITALIC;
            }
            //Negritas
            if (hp.getPstyle().equals("2")) {
               intFontStyle = Font.BOLD;
            }
            //Oblicua y negritas
            if (hp.getPstyle().equals("3")) {
               intFontStyle = Font.BOLDITALIC;
            }
            //STRIKETHRU
            if (hp.getPstyle().equals("3")) {
               intFontStyle = Font.STRIKETHRU;
            }
            //UNDERLINE
            if (hp.getPstyle().equals("3")) {
               intFontStyle = Font.UNDERLINE;
            }
            if (hp.getPfont().equals("Times")) {
               strFont = BaseFont.TIMES_ROMAN;
            } else {
               if (hp.getPfont().equals("Courier")) {
                  strFont = BaseFont.COURIER;
               } else {
                  if (hp.getPfont().equals("Helvetica")) {
                     strFont = BaseFont.HELVETICA;
                  } else {
                     //En caso de que no defina la fuente
                     strFont = BaseFont.HELVETICA;
                  }
               }
            }
            // </editor-fold >
            //Fuente
            Font bsfont = FontFactory.getFont(strFont, intFontSize, intFontStyle, colorDraw);
            // <editor-fold defaultstate="collapsed" desc="Escribimos las frases">
            String[] lstFrases;
            if (intStringSize != 0) {
               if (strValor.length() > intStringSize) {
                  int intPartes = strValor.length() / intStringSize;
                  lstFrases = new String[intPartes + 1];
                  for (int bq = 0; bq <= intPartes; bq++) {
                     if (((bq * intStringSize) + intStringSize) > strValor.length()) {
                        lstFrases[bq] = strValor.substring(bq * intStringSize, strValor.length());
                     } else {
                        lstFrases[bq] = strValor.substring(bq * intStringSize, (bq * intStringSize) + intStringSize);
                     }
                  }
               } else {
                  lstFrases = new String[1];
                  lstFrases[0] = strValor;
               }
            } else {
               lstFrases = new String[1];
               lstFrases[0] = strValor;
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Recorremos cada frase">
            for (int yh = 0; yh < lstFrases.length; yh++) {
               if (lstFrases[yh] != null) {
                  int intHeightTmp = 0;
                  try {
                     intHeightTmp = Integer.valueOf(hp.getPheight());
                  } catch (NumberFormatException ex) {
                  }
                  Phrase phrase = new Phrase(lstFrases[yh], bsfont);
                  PdfPCell cell0 = new PdfPCell();
                  if (intHeightTmp != 0) {
                     cell0.setMinimumHeight(intHeightTmp);
                  }
                  cell0.setColspan(1);
                  cell0.setBorderWidth(0);
                  cell0.setHorizontalAlignment(intAlign);
                  //Imprimimos la frase
                  cell0.setPhrase(phrase);
                  if (hp.getPformato().endsWith("AutoAjust")) {
                     cell0.setNoWrap(true);
                  } else {
                     cell0.setNoWrap(false);
                  }
                  table.addCell(cell0);
               }
            }
            // </editor-fold>
         } else {
            //Si el formato es una linea
            if (hp.getPformato().equals("Line")) {
               // <editor-fold defaultstate="collapsed" desc="Linea">
               bolPintar = false;
               float intPosX = Float.valueOf(hp.getPX());
               float intPosY = Float.valueOf(hp.getPY());
               float intPosX2 = Float.valueOf(hp.getPwidth());
               float intPosY2 = Float.valueOf(hp.getPheight());
               PdfContentByte cb = this.writer.getDirectContent();
               cb.setColorStroke(colorDraw);
               cb.setColorFill(colorDraw);
               cb.setLineWidth(1.0f);	 // Make a bit thicker than 1.0 default
               cb.moveTo(intPosX, intPosY);
               cb.lineTo(intPosX2, intPosY2);
               cb.stroke();
               // </editor-fold>
            } else {
               // <editor-fold defaultstate="collapsed" desc="Cortamos el texto si no es texto auto">
               if (!hp.getPwidth().equals("0") && !hp.getPformato().startsWith("textAuto")) {
                  int intWidthTmp = 0;
                  try {
                     intWidthTmp = Integer.valueOf(hp.getPwidth());
                     if (strValor.length() > intWidthTmp) {
                        strValor = strValor.substring(0, intWidthTmp);
                     }
                  } catch (NumberFormatException ex) {
                  }
               }
               // </editor-fold>
            }
         }
         //Si no se interrumpe la impresion proseguimos
         if (bolPintar) {
            // <editor-fold defaultstate="collapsed" desc="Calculo de posiciones en x y y">
            int intPosX = Integer.valueOf(hp.getPX());
            int intPosY = Integer.valueOf(hp.getPY());
            if (intPosX < this.intXMIN) {
               this.intXMIN = intPosX;
            }
            if (intPosY < this.intYMIN) {
               this.intLastPY = this.intYMIN - intPosY;
               this.intYMIN = intPosY;
            }
            // </editor-fold>
            //Si es un texto en un parrafo
            if (hp.getPformato().equals("text_par") /*|| hp.getPformato().equals("textParag")*/) {
               // <editor-fold defaultstate="collapsed" desc="texto en parrafo">
               Paragraph paragraph = new Paragraph(strValor);
               paragraph.setAlignment(intAlign);
               Font bsfont = FontFactory.getFont(strFont, intFontSize, Font.NORMAL, new BaseColor(0, 0, 0));
               paragraph.setFont(bsfont);
               try {
                  canvas.moveTo(intPosX, intPosY);
                  this.getDocument().add(paragraph);
               } catch (DocumentException ex) {
                  Logger.getLogger(CIP_Formato.class.getName()).log(Level.SEVERE, null, ex);
               }
               // </editor-fold>
            } else {
               canvas.beginText();
               //Detectamos si es texto ajustado
               if (hp.getPformato().startsWith("textAuto")) {
                  // <editor-fold defaultstate="collapsed" desc="texto ajustado">
                  float fltAnchoMax = 0;
                  if (hp.getPwidthCanvas() != null) {
                     fltAnchoMax = (float) Float.valueOf(hp.getPwidthCanvas());
                  }
                  //Detectamos si tiene un ancho en canvas
                  if (fltAnchoMax > 0) {
                     float fltAncho = canvas.getEffectiveStringWidth(strValor, true);
                     //Detectamos si el ancho de la frase es menor al canvas
                     if (fltAncho < fltAnchoMax && fltAncho > (fltAnchoMax / 3)) {
                        //Ajustamos el espaciado de tal manera que se ajuste al canvas esperado(Justificar la letra)
                        float fltSpacingDefa = canvas.getCharacterSpacing();
                        float fltDiff = (fltAnchoMax - fltAncho);
                        float fltNewSpacing = fltDiff / (float) strValor.length();
                        canvas.setCharacterSpacing(fltNewSpacing);
                        canvas.showTextAligned(intAlign, strValor, intPosX, intPosY, 0);
                        canvas.setCharacterSpacing(fltSpacingDefa);
                     } else {
                        canvas.showTextAligned(intAlign, strValor, intPosX, intPosY, 0);
                     }
                  } else {
                     canvas.showTextAligned(intAlign, strValor, intPosX, intPosY, 0);
                  }
                  // </editor-fold>
               } else {
                  canvas.showTextAligned(intAlign, strValor, intPosX, intPosY, 0);
               }
               canvas.endText();
            }
         }
      } else {
         // <editor-fold defaultstate="collapsed" desc="Imagenes">
         try {
            //Validamos si la imagen existe
            File fileImg = new File(hp.getUrlimg());
            if (fileImg.exists()) {
               Image img = Image.getInstance(hp.getUrlimg());
               img.setAbsolutePosition(Integer.valueOf(hp.getPX()), Integer.valueOf(hp.getPY()));
               //img.scaleAbsolute(Integer.valueOf(hp.getPwidth()), Integer.valueOf(hp.getPheight()));
               img.scaleToFit(Integer.valueOf(hp.getPwidth()), Integer.valueOf(hp.getPheight()));
               img.setAlignment(intAlign);
               intPercent = Integer.valueOf(hp.getPsize());
               if (intPercent != 0) {
                  img.scalePercent(intPercent);
               }
               img.setAlt(hp.getValor());
               try {
                  canvas.addImage(img);
               } catch (DocumentException ex) {
                  Logger.getLogger(CIP_Formato.class.getName()).log(Level.SEVERE, null, ex);
               }
            } else {
               System.out.println("La imagen no existe " + hp.getUrlimg());
            }
         } catch (BadElementException ex) {
            System.out.println("ERROR EN CIP_FORMATO(1)" + ex.getMessage());
         } catch (MalformedURLException ex) {
            System.out.println("ERROR EN CIP_FORMATO(2)" + ex.getMessage());
         } catch (IOException ex) {
            System.out.println("ERROR EN CIP_FORMATO(3)" + ex.getMessage());
         }
         // </editor-fold>
      }
   }

   /**
    * Recupera los objeto del XML
    */
   private void parseaXML(Formato fmXML) {
      //Recuperamos los objetos del XML
      Iterator itN = fmXML.getFooterOrBodyOrHeaderOrConfig().listIterator();
      while (itN.hasNext()) {
         Object obj = itN.next();
         if (obj.getClass().getCanonicalName().equals("generated.Header")) {
            head = (Header) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.Body")) {
            body = (Body) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.Footer")) {
            foot = (Footer) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.Config")) {
            cnf = (Config) obj;
            ParseaConfig(cnf);
         }
      }
   }

   /**
    * Parsea los objetos config
    */
   private void ParseaConfig(Config cnf) {
      Iterator it = cnf.getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().iterator();
      while (it.hasNext()) {
         Object obj = it.next();
         if (obj.getClass().getCanonicalName().equals("generated.Title")) {
            tit = (Title) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.TypePage")) {
            tpage = (TypePage) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.LeftMargin")) {
            left = (LeftMargin) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.RightMargin")) {
            right = (RightMargin) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.Orientation")) {
            ori = (Orientation) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.TopMargin")) {
            top = (TopMargin) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.BottomMargin")) {
            bot = (BottomMargin) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.Width")) {
            width = (Width) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.Height")) {
            height = (Height) obj;
         }
         if (obj.getClass().getCanonicalName().equals("generated.TotalPages")) {
            tot = (TotalPages) obj;
         }
      }
   }

   /**
    * Obtenemos el objeto write del PDF
    *
    * @return Es el objeto writer
    */
   @Override
   public PdfWriter getWriter() {
      return writer;
   }

   /**
    * Definimos el objeto write del PDF
    *
    * @param writer Es el objeto writer
    */
   @Override
   public void setWriter(PdfWriter writer) {
      this.writer = writer;
   }

   /**
    * Regresa el path donde estara la fuente
    *
    * @return es una cadena con un path
    */
   public String getStrPathFonts() {
      return strPathFonts;
   }

   /**
    * Define el path donde estara la fuente
    *
    * @param strPathFonts es una cadena con un path
    */
   public void setStrPathFonts(String strPathFonts) {
      this.strPathFonts = strPathFonts;
   }

   /**
    * Nos Indica que se imprimiran dos formatos por hoja
    *
    * @return Es un valor boolean
    */
   public boolean isBolDosxHoja() {
      return bolDosxHoja;
   }

   /**
    * Indica que se imprimiran dos formatos por hoja
    *
    * @param bolDosxHoja Es un valor boolean
    */
   public void setBolDosxHoja(boolean bolDosxHoja) {
      this.bolDosxHoja = bolDosxHoja;
   }

   /**
    * Nos Indica que se imprimiran cuatro formatos por hoja
    *
    * @return Es un valor boolean
    */
   public boolean isBolCuatroxHoja() {
      return bolCuatroxHoja;
   }

   /**
    * Indica que se imprimiran cuatro formatos por hoja
    *
    * @param bolCuatroxHoja Es un valor boolean
    */
   public void setBolCuatroxHoja(boolean bolCuatroxHoja) {
      this.bolCuatroxHoja = bolCuatroxHoja;
   }

   /**
    * Nos Indica que se imprimiran seis formatos por hoja
    *
    * @return Es un valor boolean
    */
   public boolean isBolSeisxHoja() {
      return bolSeisxHoja;
   }

   /**
    * Indica que se imprimiran seis formatos por hoja
    *
    * @param bolSeisxHoja Es un valor boolean
    */
   public void setBolSeisxHoja(boolean bolSeisxHoja) {
      this.bolSeisxHoja = bolSeisxHoja;
   }

   /**
    * Regresa el ancho de la tabla
    *
    * @return Regresa el ancho de tipo float
    */
   public float getFltWidthTable() {
      return fltWidthTable;
   }

   /**
    * Define el ancho de la tabla
    *
    * @param fltWidthTable Define el ancho de tipo float
    */
   public void setFltWidthTable(float fltWidthTable) {
      this.fltWidthTable = fltWidthTable;
   }
}
