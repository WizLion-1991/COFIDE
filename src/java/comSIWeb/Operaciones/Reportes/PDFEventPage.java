package comSIWeb.Operaciones.Reportes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author zeus
 */
public class PDFEventPage extends PdfPageEventHelper {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private final static String reportTitle = "pdfText.pdf";
    private PdfTemplate tpl;
    private BaseFont helv;
    private BaseFont m_BaseFont;
    private PdfContentByte m_Cb;
    private String strTitleApp;
    private int intXPageNum;
    private int intXPageNumRight;
    private float intXPageTemplate;
    private static final Logger log = LogManager.getLogger(PDFEventPage.class.getName());;

    /**
     * Nos regresa el titulo de la aplicacion
     *
     * @return Es el titulo
     */
    public String getStrTitleApp() {
        return strTitleApp;
    }

    /**
     * Definimos el titulo de la aplicacion
     *
     * @param strTitleApp Es el titulo
     */
    public void setStrTitleApp(String strTitleApp) {
        this.strTitleApp = strTitleApp;
    }

    /**
     * Regresa la posicion en x desde donde se pondra el titulo y el numero de
     * pagina
     *
     * @return Es un valor entero
     */
    public int getIntXPageNum() {
        return intXPageNum;
    }

    /**
     * Define la posicion en x desde donde se pondra el titulo y el numero de
     * pagina
     *
     * @param intXPageNum Es un valor entero
     */
    public void setIntXPageNum(int intXPageNum) {
        this.intXPageNum = intXPageNum;
    }

    public int getIntXPageNumRight() {
        return intXPageNumRight;
    }

    public void setIntXPageNumRight(int intXPageNumRight) {
        this.intXPageNumRight = intXPageNumRight;
    }

    public float getIntXPageTemplate() {
        return intXPageTemplate;
    }

    public void setIntXPageTemplate(float intXPageTemplate) {
        this.intXPageTemplate = intXPageTemplate;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Inicializa el objeto que coloca el numero de paginas
     */
    public PDFEventPage() {
        this.strTitleApp = "";
        this.intXPageNum = 412;
        this.intXPageNumRight = 65;
        this.intXPageTemplate = 352.3f;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        try {
            // initialization of the template
            tpl = writer.getDirectContent().createTemplate(85, 140);

            helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);

        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {

            final int page = writer.getPageNumber();
            String text = this.strTitleApp + " Página " + (page + 1);
            m_BaseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            m_Cb = writer.getDirectContent();

            if (m_Cb != null) {
                m_Cb.beginText();
                m_Cb.setFontAndSize(m_BaseFont, 6);
                m_Cb.setColorFill(BaseColor.GRAY);
                m_Cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, text, this.intXPageNum, 20, 0);
                m_Cb.endText();
                m_Cb.addTemplate(this.tpl, this.intXPageTemplate, 20);
            }
        } catch (DocumentException e) {
            log.error("Error DocumentException en PDF " + e.getMessage());
        } catch (IOException e) {
            log.error("Error IOException en PDF " + e.getMessage());
        }
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        try {
            // arg0.reorderPages(null);
            m_BaseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            
            tpl.beginText();
            tpl.setFontAndSize(m_BaseFont, 6);
            tpl.setColorFill(BaseColor.GRAY);
            String text = "de " + (writer.getPageNumber());
            tpl.showTextAligned(PdfContentByte.ALIGN_LEFT, text, intXPageNumRight, 0, 0);
            tpl.endText();
        } catch (DocumentException e) {
            log.error("Error DocumentException en PDF " + e.getMessage());
        } catch (IOException e) {
            log.error("Error IOException en PDF " + e.getMessage());
        }

    }
    // </editor-fold>
}
