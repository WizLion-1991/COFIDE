package com.mx.siweb.erp.reportes.conversores;

import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;

/**
 *Realiza la tarea de generar un objeto TimeSeries en base  a una lista de parametros que le dan
 * @author zeus
 */
public class AcumuladosporPeriodo {
// <editor-fold defaultstate="collapsed" desc="Propiedades">
   private TimeSeries timeseries;

   /**
    * Regresa la serie
    * @return Es un objeto TimeSerie
    */
   public TimeSeries getTimeseries() {
      return timeseries;
   }

   /**
    * Define una serie
    * @param timeseries Es un objeto TimeSerie
    */
   public void setTimeseries(TimeSeries timeseries) {
      this.timeseries = timeseries;
   }
   
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor default
    * @param strTitulo Es el titulo
    */
   public AcumuladosporPeriodo(String strTitulo) {
      timeseries = new TimeSeries(strTitulo);
   }   
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Agrega un periodo con un importe
    * @param dblTotal Es el total
    * @param intMes Es el mes
    * @param intAnio Es el año
    */
   public void AgregaPeriodo(double dblTotal, int intMes, int intAnio){
      timeseries.add(new Month(intMes, intAnio), dblTotal);
   }
// </editor-fold>


}
