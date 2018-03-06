package comSIWeb.Utilerias;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *Esta clase contiene metodo para usar la libreria jfreechart y generar graficas
 * @author zeus
 */
public class BarGraphics {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   /**
    * Constante para el tipo de grafica de barra
    */
   public final int BARRA = 1;
   /**
    * Constante para el tipo de grafica de PIE
    */
   public final int PIE = 2;
   /**
    * Constante para el tipo de grafica XY
    */
   public final int XY = 3;
   /**
    * Constante para el tipo de grafica de linea
    */
   public final int LINE = 4;
   private boolean bolLineaUsaFecha;
   //Lista de las series para las graficas de tiempo
   private ArrayList<TimeSeries> listSeriesTime;

   /**
    * Nos dice si se usan fechas en las graficas de linea
    * @return Es un true si se usan fechas cortas
    */
   public boolean isBolLineaUsaFecha() {
      return bolLineaUsaFecha;
   }

   /**
    * Define si se usan fechas en las graficas de linea
    * @param bolLineaUsaFecha Es un true si se usan fechas cortas
    */
   public void setBolLineaUsaFecha(boolean bolLineaUsaFecha) {
      this.bolLineaUsaFecha = bolLineaUsaFecha;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public BarGraphics() {
      this.listSeriesTime = new ArrayList<TimeSeries>();
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Genera una grafica
    * @param strTitle Es el titulo de la grafica
    * @param lstNom Son los nombres de cada barra
    * @param lstValue Son los valores
    * @param lstNom2 Son los valores de la columna
    * @param strTitleX Es el titulo en X
    * @param strTitleY Es el titulo en Y
    * @param strNomFile Es el nombre del archivo conteniendo la ruta completa
    * @param intTipoGrafica Es el tipo de grafica
    * @param intWidth Es el ancho de al grafica
    * @param intHeight  Es el alto de la grafica
    * @param color Es el color de fondo
    */
   public void DrawGraphic(String strTitle, ArrayList<String> lstNom,
           ArrayList<Integer> lstValue, ArrayList<String> lstNom2, String strTitleX, String strTitleY,
           String strNomFile, int intTipoGrafica, int intWidth, int intHeight, Color color) {
      JFreeChart chart = DrawGraphicChart(strTitle, lstNom,
              lstValue, lstNom2, strTitleX, strTitleY,
              strNomFile, intTipoGrafica, intWidth, intHeight, color, null);
      this.SaveGraphic(chart,
              strNomFile, intWidth, intHeight);
   }

   /**
    * Guarda en un jpg la grafica que se pasa como parametro
    * @param chart Es la grafica
    * @param strNomFile Es el nombre del archivo jpg
    * @param intWidth Es el ancho 
    * @param intHeight Es el alto
    */
   public void SaveGraphic(JFreeChart chart,
           String strNomFile, int intWidth, int intHeight) {
      String strPathUsado = strNomFile;
      try {
         ChartUtilities.saveChartAsJPEG(new File(strPathUsado), chart, intWidth, intHeight);
      } catch (IOException ex) {
         System.out.println(ex.getMessage());
      }
   }

   /**
    * Genera una grafica
    * @param strTitle Es el titulo de la grafica
    * @param lstNom Son los nombres de cada barra
    * @param lstValue Son los valores
    * @param lstNom2 Son los valores de la columna
    * @param strTitleX Es el titulo en X
    * @param strTitleY Es el titulo en Y
    * @param strNomFile Es el nombre del archivo conteniendo la ruta completa
    * @param intTipoGrafica Es el tipo de grafica
    * @param intWidth Es el ancho de al grafica
    * @param intHeight  Es el alto de la grafica
    * @param color Es el color de fondo
    * @param lstValue2 
    * @return Regresa el objeto chart
    */
   public JFreeChart DrawGraphicChart(String strTitle, ArrayList<String> lstNom,
           ArrayList<Integer> lstValue, ArrayList<String> lstNom2, String strTitleX, String strTitleY,
           String strNomFile, int intTipoGrafica, int intWidth, int intHeight, Color color,
           ArrayList<Integer> lstValue2) {
      JFreeChart chart = null;
      //Grafica de barras
      if (intTipoGrafica == this.BARRA) {
         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         //Recorremos los estados
         for (int i = 0; i < lstNom.size(); i++) {
            String strNom = lstNom.get(i);
            int intNum = lstValue.get(i);
            String strNom2 = "";
            if (lstNom2 != null) {
               strNom2 = lstNom2.get(i);
            }
            //Agregamos elementos a la grafica
            dataset.setValue(intNum, strNom, strNom2);
         }
         chart = ChartFactory.createBarChart3D(strTitle, strTitleX,
                 strTitleY, dataset, PlotOrientation.VERTICAL, true,
                 true, false);
      }
      //Grafica de pie
      if (intTipoGrafica == this.PIE) {
         DefaultPieDataset data = new DefaultPieDataset();
         //Recorremos los estados
         for (int i = 0; i < lstNom.size(); i++) {
            String strNom = lstNom.get(i);
            int intNum = lstValue.get(i);
            //Agregamos elementos a la grafica
            data.setValue(strNom, intNum);
         }
         chart = ChartFactory.createPieChart3D(strTitle, data, true, true, false);
      }
      if (intTipoGrafica == this.XY) {
         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         //Recorremos los estados
         for (int i = 0; i < lstNom.size(); i++) {
            String strNom = lstNom.get(i);
            int intNum = lstValue.get(i);
            String strNom2 = "";
            if (lstNom2 != null) {
               strNom2 = lstNom2.get(i);
            }
            //Agregamos elementos a la grafica
            dataset.setValue(intNum, strNom, strNom2);
         }
         chart = ChartFactory.createBarChart(strTitle, strTitleX,
                 strTitleY, dataset, PlotOrientation.VERTICAL, true,
                 true, false);
      }
      //Grafica de Linea
      if (intTipoGrafica == this.LINE) {
         String s1 = strTitle;
         TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
         //Añadimos las series definidas
         Iterator<TimeSeries> it = this.listSeriesTime.iterator();
         while (it.hasNext()){
            TimeSeries time = it.next();
            timeseriescollection.addSeries(time);
         }
         XYDataset xydataset = timeseriescollection;
         chart = ChartFactory.createTimeSeriesChart(s1, strTitleX, strTitleY, xydataset, true, true, false);
         XYPlot xyplot = (XYPlot) chart.getPlot();
         org.jfree.chart.renderer.xy.XYItemRenderer xyitemrenderer = xyplot.getRenderer();
         xyplot.setBackgroundPaint(Color.white);
         if (xyitemrenderer instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyitemrenderer;
            xylineandshaperenderer.setShapesVisible(true);
            xylineandshaperenderer.setShapesFilled(true);
         }
         if (this.bolLineaUsaFecha) {
            DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
            dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
         }
      }
      if (color != null) {
         chart.setBackgroundPaint(color);
      }
      return chart;
   }

   /**
    * Genera una grafica
    * @param strTitle Es el titulo de la grafica
    * @param lstNom Son los nombres de cada barra
    * @param lstValue Son los valores
    * @param lstNom2 Son los valores de la columna
    * @param strTitleX Es el titulo en X
    * @param strTitleY Es el titulo en Y
    * @param strNomFile Es el nombre del archivo conteniendo la ruta completa
    * @param intTipoGrafica Es el tipo de grafica
    * @param intWidth Es el ancho de al grafica
    * @param intHeight  Es el alto de la grafica
    * @param color Es el color de fondo
    * @return Regresa el objeto chart
    */
   public JFreeChart DrawGraphicChartDouble(String strTitle, ArrayList<String> lstNom,
           ArrayList<Double> lstValue, ArrayList<String> lstNom2, String strTitleX, String strTitleY,
           String strNomFile, int intTipoGrafica, int intWidth, int intHeight, Color color) {
      JFreeChart chart = null;
      //Grafica de barras
      if (intTipoGrafica == this.BARRA) {
         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         //Recorremos los estados
         for (int i = 0; i < lstNom.size(); i++) {
            String strNom = lstNom.get(i);
            double intNum = lstValue.get(i);
            String strNom2 = "";
            if (lstNom2 != null) {
               strNom2 = lstNom2.get(i);
            }
            //Agregamos elementos a la grafica
            dataset.setValue(intNum, strNom, strNom2);
         }
         chart = ChartFactory.createBarChart3D(strTitle, strTitleX,
                 strTitleY, dataset, PlotOrientation.VERTICAL, true,
                 true, false);
      }
      //Grafica de pie
      if (intTipoGrafica == this.PIE) {
         DefaultPieDataset data = new DefaultPieDataset();
         //Recorremos los estados
         for (int i = 0; i < lstNom.size(); i++) {
            String strNom = lstNom.get(i);
            double intNum = lstValue.get(i);
            //Agregamos elementos a la grafica
            data.setValue(strNom, intNum);
         }
         chart = ChartFactory.createPieChart3D(strTitle, data, true, true, false);
      }
      //Grafica de XY
      if (intTipoGrafica == this.XY) {
         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
         //Recorremos los estados
         for (int i = 0; i < lstNom.size(); i++) {
            String strNom = lstNom.get(i);
            double intNum = lstValue.get(i);
            String strNom2 = "";
            if (lstNom2 != null) {
               strNom2 = lstNom2.get(i);
            }
            //Agregamos elementos a la grafica
            dataset.setValue(intNum, strNom, strNom2);
         }
         chart = ChartFactory.createBarChart(strTitle, strTitleX,
                 strTitleY, dataset, PlotOrientation.VERTICAL, true,
                 true, false);
      }
      //Grafica de Linea
      if (intTipoGrafica == this.LINE) {
      }
      if (color != null) {
         chart.setBackgroundPaint(color);
      }
      return chart;
   }

   /**
    * Genera una grafica
    * @param strTitle Es el titulo de la grafica
    * @param lstNom Son los nombres de cada barra
    * @param lstValue Son los valores
    * @param lstNom2 Son los valores de la columna
    * @param strTitleX Es el titulo en X
    * @param strTitleY Es el titulo en Y
    * @param strNomFile Es el nombre del archivo conteniendo la ruta completa
    * @param intTipoGrafica Es el tipo de grafica
    * @param intWidth Es el ancho de al grafica
    * @param intHeight  Es el alto de la grafica
    */
   public void DrawGraphic(String strTitle, ArrayList<String> lstNom,
           ArrayList<Integer> lstValue, ArrayList<String> lstNom2, String strTitleX, String strTitleY,
           String strNomFile, int intTipoGrafica, int intWidth, int intHeight) {
      this.DrawGraphic(strTitle, lstNom,
              lstValue, lstNom2, strTitleX, strTitleY,
              strNomFile, intTipoGrafica, intWidth, intHeight, null);
   }

   /**
    * Genera una grafica 
    * @param strTitle Es el titulo de la grafica
    * @param lstNom Son los nombres de cada barra
    * @param lstValue Son los valores
    * @param strTitleX Es el titulo en X 
    * @param strTitleY Es el titulo en Y
    * @param strNomFile Es el nombre del archivo conteniendo la ruta completa
    * @param intTipoGrafica Es el tipo de grafica
    */
   public void DrawGraphic(String strTitle, ArrayList<String> lstNom,
           ArrayList<Integer> lstValue, String strTitleX, String strTitleY,
           String strNomFile, int intTipoGrafica) {
      this.DrawGraphic(strTitle, lstNom,
              lstValue, null, strTitleX, strTitleY,
              strNomFile, intTipoGrafica, 500, 300);
   }
   /**
    * Agrega una nueva serie para los reportes
    * @param serie Es la serie por agregar
    */
   public void AddTimeSerie(TimeSeries serie){
      this.listSeriesTime.add(serie);
   }
   /**
    * Genera una nueva serie de tipo Time
    * @param strTitulo Es el titulo de la serie
    * @return Regresa un objeto Time Series
    */
   public TimeSeries createTimeSeries(String strTitulo) {
      TimeSeries timeseries = new TimeSeries(strTitulo);
      //timeseries.add(new Month(2, 2001), 181.80000000000001D);
      return timeseries;
   }
   // </editor-fold>
}
