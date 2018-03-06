package comSIWeb.Scripting;

import com.itextpdf.text.Document;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Reportes.CIP_ReportPDF;
import comSIWeb.Operaciones.Reportes.CIP_ReporteColum;
import comSIWeb.Operaciones.Reportes.CIP_ReporteRGB;
import comSIWeb.Operaciones.Reportes.CIP_ReporteValor;
import comSIWeb.Utilerias.BarGraphics;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.NumberString;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Es el script para generar los reportes
 *
 * @author zeus
 */
public class scriptReport extends scriptBase {

    protected HttpServletResponse response;
    protected CIP_ReportPDF report;
    protected Conexion oConn;
    protected Document document;
    String strAbrev;
    protected ServletContext context;

    /**
     * Construye el objeto reporte por scripting
     *
     * @param request Es la peticion
     * @param response Es la respuesta al browser
     * @param report Es el objeto report
     * @param oConn Es la conexion
     * @param document Es el documento de impresion del pdf
     */
    public scriptReport(HttpServletRequest request, HttpServletResponse response,
            CIP_ReportPDF report, Conexion oConn, Document document) {
        this.request = request;
        this.response = response;
        this.report = report;
        this.oConn = oConn;
        this.document = document;
    }

    /**
     * Ejecuta el reporte
     */
    public void Execute() {
        if (strAbrev == null && request != null) {
            strAbrev = request.getParameter("Abrev");
        }
        if (strAbrev != null) {
            String strSql = "select * from reportbase where RP_ABREV = '" + strAbrev + "'";
            try {
                ResultSet rs = this.oConn.runQuery(strSql, true);
                while (rs.next()) {
                    this.report.strTitulo = rs.getString("RP_NOMBRE");
                    this.EjecutaSentencia(rs.getString("RP_SCRIPT"));
                }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Regresa el nombre abreviado del reporte
     *
     * @return Es una cadena de texto
     */
    public String getStrAbrev() {
        return strAbrev;
    }

    /**
     * Define el nombre abreviado del reporte
     *
     * @param strAbrev Es una cadena de texto
     */
    public void setStrAbrev(String strAbrev) {
        this.strAbrev = strAbrev;
    }

    /**
     * Este metodo ejecuta la sentencia a evaluar
     *
     * @param strSentencia Es la sentencia por ejecutar
     * @return Regresa true si se ejecuto bien la sentencia
     */
    protected boolean EjecutaSentencia(String strSentencia) {
        //Nos indica si la ejecucion de la sentencia fue exitosa
        boolean bolResulSentencia = false;
        //Objeto del recordset
        ResultSet Rs = null;
        //Objeto para fechas
        Fechas Fecha = new Fechas();
        //Objeto para numeros
        NumberString Numbers = new NumberString();
        //Objeto para graficas
        BarGraphics bars = new BarGraphics();
        //Objetos para la ejecucion de scripting usamos el SCRIPT default
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        //Anadimos objetos para que los tome en cuenta el scripting
        jsEngine.put("Fecha", Fecha);
        jsEngine.put("Numbers", Numbers);
        jsEngine.put("request", request);
        jsEngine.put("response", this.response);
        jsEngine.put("Rs", Rs);
        jsEngine.put("oConn", oConn);
        jsEngine.put("report", this.report);
        jsEngine.put("document", this.document);
        jsEngine.put("bars", bars);
        jsEngine.put("obj", this);
        jsEngine.put("contextServlet", this.context);
        jsEngine.put("bolResulSentencia", bolResulSentencia);
        if (oConn.isBolMostrarQuerys() == true) {
            System.out.println("**Program to execute**");
            System.out.println(strSentencia);
            System.out.println("**End Program to execute**");
        }
        try {
            jsEngine.eval(strSentencia + ";");
        } catch (ScriptException ex) {
            ex.printStackTrace();
            this.bitacora.GeneraBitacora(ex.getMessage(), "System", "scriptReport", this.oConn);
        }
        //Recuperamos el valor booleano que se halla parseado
        String strValorBoolean = jsEngine.get("bolResulSentencia").toString();
        if (strValorBoolean.equals("true")) {
            bolResulSentencia = true;
        } else {
            bolResulSentencia = false;
        }
        if (oConn.isBolMostrarQuerys() == true) {
            System.out.println("**Script Solution**" + bolResulSentencia);
        }
        return bolResulSentencia;
    }

    /**
     * Genera un nuevo control Valor
     *
     * @return Nos regresa un objeto de tipo CIP_ReporteValor
     */
    public CIP_ReporteValor newTitle() {
        CIP_ReporteValor objTitle = new CIP_ReporteValor();
        return objTitle;
    }

    /**
     * Genera un nuevo control Valor
     *
     * @param strTitle Es el titulo de la columna
     * @return Nos regresa un objeto de tipo CIP_ReporteValor
     */
    public CIP_ReporteColum newCol(String strTitle) {
        CIP_ReporteColum col1 = new CIP_ReporteColum(strTitle);
        return col1;
    }

    /**
     * Genera un nuevo color
     *
     * @return Nos regresa un objeto de tipo CIP_ReporteValor
     */
    public CIP_ReporteRGB newColor() {
        CIP_ReporteRGB color = new CIP_ReporteRGB();
        return color;
    }

    /**
     * Define el documento PDF
     *
     * @param document Es el documento PDF del iText
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /*
     *Regresa el objeto de contexto del servlet
     */
    public ServletContext getContext() {
        return context;
    }
    /*
     *Define el objeto de contexto del servlet
     */
    public void setContext(ServletContext context) {
        this.context = context;
    }
}
