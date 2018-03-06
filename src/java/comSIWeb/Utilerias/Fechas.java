/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.struts.util.MessageResources;

/**
 *Utilerias para el manejo de fechas
 * @author zeus
 */
public class Fechas {

   private String[] lstNomMeses = {"fechas.Mes1", "fechas.Mes2", "fechas.Mes3", "fechas.Mes4", "fechas.Mes5", "fechas.Mes6", "fechas.Mes7", "fechas.Mes8", "fechas.Mes9", "fechas.Mes10", "fechas.Mes11", "fechas.Mes12"};
   private String[] lstNomMesesESP = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
   private Integer[] lstDiaMeses = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
   private MessageResources msgRes = null;

   public Fechas() {
   }

   /**
    * Nos regresa el mes en letras
    * @param intMes Es el numero de Mes
    * @return Nos regresa la llave para obtener el nombre del mes
    */
   public String DameMesenLetra(int intMes) {
      String strNomMes = lstNomMesesESP[intMes - 1];
      if (this.msgRes != null) {
         strNomMes = this.lstNomMeses[intMes - 1];
         strNomMes = msgRes.getMessage(strNomMes);
      }
      return strNomMes;
   }

   /**
    * Nos regresa el numero de dias que tiene el mes solicitado
    * @param intMes Es el mes Solicitado
    * @param intAnio Es el anio
    * @return Nos regresa el numero de dias del mes
    */
   public int DameDiasporMes(int intMes, int intAnio) {
      if ((intAnio % 4) == 0 && intMes == 2) {
         return 29;
      } else {
         return lstDiaMeses[intMes - 1];
      }
   }

   /**
    * Nos regresa el anio actual
    * @return Nos regresa un entero con el anio actual
    */
   public int getAnioActual() {
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      return dateFecha.get(Calendar.YEAR);
   }

   /**
    * Nos regresa el mes actual
    * @return Nos regresa un entero con el anio actual
    */
   public int getMesActual() {
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      return dateFecha.get(Calendar.MONTH);
   }

   /**
    * Nos regresa el dia de la semana actual
    * @return Nos regresa un entero con el dia de la semana
    */
   public int getDayWeek() {
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      return dateFecha.get(Calendar.DAY_OF_WEEK);
   }

   /**
    * Nos regresa la fecha actual
    * @return Regresa la fecha actual en formato AAAAMMDD
    */
   public String getFechaActual() {
      String strFecha = "";
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      strDia = "0" + String.valueOf(dateFecha.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(dateFecha.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(dateFecha.get(Calendar.YEAR));
      strFecha = strAnio + strMes + strDia;
      return strFecha;
   }
   /**
    * Nos regresa la fecha actual
    * @param dateParam Es la fecha en tipo de dato Date
    * @return Regresa la fecha actual en formato AAAAMMDD
    */
   public String getFechaDate(Date dateParam) {
      String strFecha = "";
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      dateFecha.setTime(dateParam);
      strDia = "0" + String.valueOf(dateFecha.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(dateFecha.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(dateFecha.get(Calendar.YEAR));
      strFecha = strAnio + strMes + strDia;
      return strFecha;
   }

   /**
    * Nos regresa la fecha del calendario
    * @param dateFecha  Es el calendario
    * @return Regresa la fecha del calendario en formato AAAAMMDD
    */
   public String getFechaCalendar(java.util.GregorianCalendar dateFecha) {
      String strFecha = "";
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      strDia = "0" + String.valueOf(dateFecha.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(dateFecha.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(dateFecha.get(Calendar.YEAR));
      strFecha = strAnio + strMes + strDia;
      return strFecha;
   }

   /**
    * Nos regresa la fecha actual
    * @return Regresa la fecha actual en formato DD-MM-AAAA
    */
   public String getFechaActualDDMMAAAAguion() {
      String strFecha = "";
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      strDia = "0" + String.valueOf(dateFecha.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(dateFecha.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(dateFecha.get(Calendar.YEAR));
      strFecha = strDia + "-" + strMes + "-" + strAnio;
      return strFecha;
   }

   /**
    * Nos regresa la fecha actual
    * @return Regresa la fecha actual en formato AAAA-MM-DD
    */
   public String getFechaActualAAAAMMDDguion() {
      String strFecha = "";
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      strDia = "0" + String.valueOf(dateFecha.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(dateFecha.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(dateFecha.get(Calendar.YEAR));
      strFecha = strAnio + "-" + strMes + "-" + strDia;
      return strFecha;
   }

   /**
    * Nos regresa la fecha actual
    * @return Regresa la fecha actual en formato DD/MM/AAAA
    */
   public String getFechaActualDDMMAAAADiagonal() {
      String strFecha = "";
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      java.util.GregorianCalendar dateFecha = new java.util.GregorianCalendar();
      strDia = "0" + String.valueOf(dateFecha.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(dateFecha.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(dateFecha.get(Calendar.YEAR));
      strFecha = strDia + "/" + strMes + "/" + strAnio;
      return strFecha;
   }

   /**
    * Regresa la hora actual HH:MM
    * @return Regresa una cadena con la fecha actual
    */
   public String getHoraActual() {
      String strHorasMin = "";
      String strHora = "00";
      String strMinuto = "00";
      //Objeto calendario
      java.util.Calendar dateHora = java.util.Calendar.getInstance();
      //java.util.GregorianCalendar dateHora = new java.util.GregorianCalendar( );
      //Obtenemos Hora y Minutos
      strHora = "0" + String.valueOf(dateHora.get(Calendar.HOUR_OF_DAY));
      strMinuto = "0" + String.valueOf(dateHora.get(Calendar.MINUTE));
      if (strHora.length() == 3) {
         strHora = strHora.substring(1, 3);
      }
      if (strMinuto.length() == 3) {
         strMinuto = strMinuto.substring(1, 3);
      }
      strHorasMin = strHora + ":" + strMinuto;
      return strHorasMin;
   }

   /**
    * Regresa la hora del calendario HH:MM
    * @param dateHora Es el objeto calendario con la hora
    * @return Regresa una cadena con la hora del calendario
    */
   public String getHoraCalendar(java.util.Calendar dateHora) {
      String strHorasMin = "";
      String strHora = "00";
      String strMinuto = "00";
      //Obtenemos Hora y Minutos
      strHora = "0" + String.valueOf(dateHora.get(Calendar.HOUR_OF_DAY));
      strMinuto = "0" + String.valueOf(dateHora.get(Calendar.MINUTE));
      if (strHora.length() == 3) {
         strHora = strHora.substring(1, 3);
      }
      if (strMinuto.length() == 3) {
         strMinuto = strMinuto.substring(1, 3);
      }
      strHorasMin = strHora + ":" + strMinuto;
      return strHorasMin;
   }

   /**
    * Regresa la hora actual HH:MM:SS
    * @return Regresa una cadena con la hora actual
    */
   public String getHoraActualHHMMSS() {
      String strHorasMin = "";
      String strHora = "00";
      String strMinuto = "00";
      String strSegundo = "00";
      //Objeto calendario
      java.util.Calendar dateHora = java.util.Calendar.getInstance();
      //Obtenemos Hora y Minutos
      strHora = "0" + String.valueOf(dateHora.get(Calendar.HOUR_OF_DAY));
      strMinuto = "0" + String.valueOf(dateHora.get(Calendar.MINUTE));
      strSegundo = "0" + String.valueOf(dateHora.get(Calendar.SECOND));
      if (strHora.length() == 3) {
         strHora = strHora.substring(1, 3);
      }
      if (strMinuto.length() == 3) {
         strMinuto = strMinuto.substring(1, 3);
      }
      if (strSegundo.length() == 3) {
         strSegundo = strSegundo.substring(1, 3);
      }
      strHorasMin = strHora + ":" + strMinuto + ":" + strSegundo;
      return strHorasMin;
   }

   /**
    * Regresa la hora del objeto calendario HH:MM:SS
    * @param dateHora Es el objeto calendario con la hora
    * @return Regresa una cadena con la hora del calendario
    */
   public String getHoraCalendarHHMMSS(java.util.Calendar dateHora) {
      String strHorasMin = "";
      String strHora = "00";
      String strMinuto = "00";
      String strSegundo = "00";
      //java.util.GregorianCalendar dateHora = new java.util.GregorianCalendar( );
      //Obtenemos Hora y Minutos
      strHora = "0" + String.valueOf(dateHora.get(Calendar.HOUR_OF_DAY));
      strMinuto = "0" + String.valueOf(dateHora.get(Calendar.MINUTE));
      strSegundo = "0" + String.valueOf(dateHora.get(Calendar.SECOND));
      if (strHora.length() == 3) {
         strHora = strHora.substring(1, 3);
      }
      if (strMinuto.length() == 3) {
         strMinuto = strMinuto.substring(1, 3);
      }
      if (strSegundo.length() == 3) {
         strSegundo = strSegundo.substring(1, 3);
      }
      strHorasMin = strHora + ":" + strMinuto + ":" + strSegundo;
      return strHorasMin;
   }

   /**
    * Formatea una fecha guardada en la base de datos a una formato DD MM AAAA con el caracter separado que queramos
    * @param strFechaAAAAMMDD Es la fecha
    * @param strSeparador Es el separador
    * @return Nos regresa una cadena con la fecha formateada
    */
   public String Formatea(String strFechaAAAAMMDD, String strSeparador) {
      return strFechaAAAAMMDD.substring(6, 8) + strSeparador + strFechaAAAAMMDD.substring(4, 6) + strSeparador + strFechaAAAAMMDD.substring(0, 4);
   }

   /**
    * Formatea una fecha guardada en la base de datos a una formato DD MM AAAA con el caracter separado que queramos
    * @param strFechaAAAAMMDD Es la fecha
    * @param strSeparador Es el separador
    * @return Nos regresa una cadena con la fecha formateada
    */
   public String FormateaAAAAMMDD(String strFechaAAAAMMDD, String strSeparador) {
      return strFechaAAAAMMDD.substring(0, 4) + strSeparador + strFechaAAAAMMDD.substring(4, 6) + strSeparador + strFechaAAAAMMDD.substring(6, 8);
   }

   /**
    * Formatea una fecha guardada en la base de datos a una formato DD MM AAAA con el caracter separado que queramos
    * @param strFechaAAAAMMDD Es la fecha
    * @param strSeparador Es el separador
    * @return Nos regresa una cadena con la fecha formateada
    */
   public String FormateaDDMMAAAA(String strFechaAAAAMMDD, String strSeparador) {
      return strFechaAAAAMMDD.substring(6, 8) + strSeparador + strFechaAAAAMMDD.substring(4, 6) + strSeparador + strFechaAAAAMMDD.substring(0, 4);
   }

   /**
    * Formatea una fecha capturada por el picker para sera guardada en la base de datos
    * @param strFechaDDMMAAAA Es la fecha capturada
    * @param strSeparador Es el separador
    * @return Nos regresa una cadena con la fecha formateada
    */
   public String FormateaBD(String strFechaDDMMAAAA, String strSeparador) {
      if (!strFechaDDMMAAAA.equals("")) {
         strFechaDDMMAAAA = strFechaDDMMAAAA.replace(strSeparador, "");
         strFechaDDMMAAAA = strFechaDDMMAAAA.replace("_", "");
         strFechaDDMMAAAA = strFechaDDMMAAAA.substring(4, 8) + strFechaDDMMAAAA.substring(2, 4) + strFechaDDMMAAAA.substring(0, 2);
      }
      return strFechaDDMMAAAA;
   }

   /**
    * Compara si una hora es mayor o igual
    * @param strHoraComparar La hora que necesitamos saber si esta en un rango.El formato debe de ser (hh:mm)
    * @param strHoraComparar2 La hora inicial del rango a comparar.El formato debe de ser (hh:mm)
    * @return Regresa true si la <B>hora</B> esta en el rango de horas
    */
   public boolean theHourIsPlus(String strHoraComparar, String strHoraComparar2) {
      Calendar cat1 = Calendar.getInstance();
      Calendar cat2 = Calendar.getInstance();

      //Validamos si las cadenas son iguales
      if (strHoraComparar.equals(strHoraComparar2)) {
         return true;
      } else {
         //Hora y minuto de los parametros
         int intHora = Integer.parseInt(strHoraComparar.substring(0, 2));
         int intMinuto = Integer.parseInt(strHoraComparar.substring(3, 5));
         cat1.set(Calendar.HOUR_OF_DAY, intHora);
         cat1.set(Calendar.MINUTE, intMinuto);

         intHora = Integer.parseInt(strHoraComparar2.substring(0, 2));
         intMinuto = Integer.parseInt(strHoraComparar2.substring(3, 5));
         cat2.set(Calendar.HOUR_OF_DAY, intHora);
         cat2.set(Calendar.MINUTE, intMinuto);

         //Validamos si la hora esta en el rango especificado
         if (cat1.after(cat2) == true) {
            return true;
         } else {
            return false;
         }
      }
   }

   /**
    * Compara si una hora es menor o igual
    * @param strHoraComparar La hora que necesitamos saber si esta en un rango.El formato debe de ser (hh:mm)
    * @param strHoraComparar2 La hora inicial del rango a comparar.El formato debe de ser (hh:mm)
    * @return Regresa true si la <B>hora</B> esta en el rango de horas
    */
   public boolean theHourIsLess(String strHoraComparar, String strHoraComparar2) {
      Calendar cat1 = Calendar.getInstance();
      Calendar cat2 = Calendar.getInstance();

      //Validamos si las cadenas son iguales
      if (strHoraComparar.equals(strHoraComparar2)) {
         return true;
      } else {
         //Hora y minuto de los parametros
         int intHora = Integer.parseInt(strHoraComparar.substring(0, 2));
         int intMinuto = Integer.parseInt(strHoraComparar.substring(3, 5));
         cat1.set(Calendar.HOUR_OF_DAY, intHora);
         cat1.set(Calendar.MINUTE, intMinuto);

         intHora = Integer.parseInt(strHoraComparar2.substring(0, 2));
         intMinuto = Integer.parseInt(strHoraComparar2.substring(3, 5));
         cat2.set(Calendar.HOUR_OF_DAY, intHora);
         cat2.set(Calendar.MINUTE, intMinuto);

         //Validamos si la hora esta en el rango especificado
         if (cat1.before(cat2) == true) {
            return true;
         } else {
            return false;
         }
      }
   }

   /**
    * Convierte una fecha String AAAAMMDD en una fecha de tipo XML Gregorian
    * @param strFecha Es la fecha a convertir
    * @param strHora Es la hora a convertir
    * @return Nos regresa una fecha en XMLGregorianCalendar
    */
   public XMLGregorianCalendar RegresaXMLGregorianCalendar(String strFecha, String strHora) {
      int intAnio = 0;
      int intMes = 0;
      int intDia = 0;
      int intHora = 0;
      int intMinuto = 0;
      XMLGregorianCalendar fecha = null;
      DatatypeFactory factory;

      try {
         intAnio = Integer.valueOf(strFecha.substring(0, 4));
         intMes = Integer.valueOf(strFecha.substring(4, 6));
         intDia = Integer.valueOf(strFecha.substring(6, 8));
         intHora = Integer.valueOf(strHora.substring(0, 2));
         intMinuto = Integer.valueOf(strHora.substring(3, 5));
      } catch (NumberFormatException ex) {
         ex.fillInStackTrace();
      }
      try {
         factory = DatatypeFactory.newInstance();
         //TimeZone tm = TimeZone.getDefault();
         fecha = factory.newXMLGregorianCalendar(intAnio, intMes, intDia, intHora, intMinuto, 0, 0, 0);
      } catch (DatatypeConfigurationException ex) {
         ex.fillInStackTrace();
      }
      return fecha;
   }

   /**
    * Suma un periodo a la fecha de los parametros
    * @param intDia Es el dia
    * @param intMes Es el mes
    * @param intAnio Es el anio
    * @param intPeriod Es el periodo(dias, mensual,anual,bimestral)
    * @param intCantSuma Es la cantidad a sumar o restar
    * @return Regresa la fecha con el periodo 
    */
   public Calendar addFecha(int intDia, int intMes, int intAnio, int intPeriod, int intCantSuma) {
      Calendar cal = Calendar.getInstance();
      cal.set(intAnio, intMes - 1, intDia);
      cal.add(intPeriod, intCantSuma);
      return cal;
   }

   /**
    * Suma un periodo a la fecha de los parametros
    * @param strFecha Es la fecha base
    * @param intPeriod Es el periodo(dias, mensual,anual,bimestral)
    * @param intCantSuma Es la cantidad a sumar o restar
    * @return Regresa la fecha con el periodo
    */
   public String addFecha(String strFecha, int intPeriod, int intCantSuma) {
      int intDia = Integer.valueOf(strFecha.substring(6, 8));
      int intMes = Integer.valueOf(strFecha.substring(4, 6));
      int intAnio = Integer.valueOf(strFecha.substring(0, 4));
      String strDia = "1";
      String strMes = "1";
      String strAnio = "2000";
      //Obtenemos la nueva fecha
      Calendar cal = this.addFecha(intDia, intMes, intAnio, intPeriod, intCantSuma);
      strDia = "0" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
      if (strDia.length() == 3) {
         strDia = strDia.substring(1, 3);
      }
      strMes = "0" + String.valueOf(cal.get(Calendar.MONTH) + 1);
      if (strMes.length() == 3) {
         strMes = strMes.substring(1, 3);
      }
      strAnio = String.valueOf(cal.get(Calendar.YEAR));
      strFecha = strAnio + strMes + strDia;
      return strFecha;
   }

   /**
    * Calcula la diferencia en dias
    * @param Y1 Anio
    * @param M1 Mes
    * @param D1 Dia
    * @param Y2 Anio
    * @param M2 Mes
    * @param D2 Dia
    * @return regresa el numero de dias de diferencia
    */
   public static long difDiasEntre2fechas(int Y1, int M1, int D1, int Y2, int M2, int D2) {
      final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día
      java.util.GregorianCalendar date = new java.util.GregorianCalendar(Y1, M1, D1);
      java.util.GregorianCalendar date2 = new java.util.GregorianCalendar(Y2, M2, D2);
      long difms = (date2.getTimeInMillis() - date.getTimeInMillis()) / MILLSECS_PER_DAY;
      return difms;
   }

   /**
    * Calcula la diferencia en dias
    * @param strFecha1 Es la fecha inicial en formato AAAAMMDD
    * @param strFecha2 Es la fecha final en formato AAAAMMDD
    * @return regresa el numero de dias de diferencia
    */
   public static long difDiasEntre2fechasStr(String strFecha1, String strFecha2) {
      int intDia = Integer.valueOf(strFecha1.substring(6, 8));
      int intMes = Integer.valueOf(strFecha1.substring(4, 6));
      int intAnio = Integer.valueOf(strFecha1.substring(0, 4));
      int intDia2 = Integer.valueOf(strFecha2.substring(6, 8));
      int intMes2 = Integer.valueOf(strFecha2.substring(4, 6));
      int intAnio2 = Integer.valueOf(strFecha2.substring(0, 4));
      return difDiasEntre2fechas(intAnio, intMes, intDia, intAnio2, intMes2, intDia2);
   }

   /**
    * Calcula la diferencia en meses
    * @param Y1 Anio
    * @param M1 Mes
    * @param D1 Dia
    * @param Y2 Anio
    * @param M2 Mes
    * @param D2 Dia
    * @return regresa el numero de dias de diferencia
    */
   public static int difDiasEntre2fechasMes(int Y1, int M1, int D1, int Y2, int M2, int D2) {
      java.util.GregorianCalendar g1 = new java.util.GregorianCalendar(Y1, M1 -1, D1);
      java.util.GregorianCalendar g2 = new java.util.GregorianCalendar(Y2, M2 - 1, D2);
      java.util.GregorianCalendar aux = new java.util.GregorianCalendar();
      int elapsed = 0; // Por defecto estaba en 0 y siempre asi no haya pasado un mes contaba 1)
      GregorianCalendar gc1, gc2;
      if (g2.after(g1)) {
         gc2 = (GregorianCalendar) g2.clone();
         gc1 = (GregorianCalendar) g1.clone();
      } else {
         gc2 = (GregorianCalendar) g1.clone();
         gc1 = (GregorianCalendar) g2.clone();
      }
      //Ciclo para contar meses
      while (gc1.before(gc2)) {
         gc1.add(Calendar.MONTH, 1);
         elapsed++;
      }
      return elapsed;
   }

   /**
    * Calcula la diferencia en meses
    * @param strFecha1 Es la fecha inicial en formato AAAAMMDD
    * @param strFecha2 Es la fecha final en formato AAAAMMDD
    * @return regresa el numero de dias de diferencia
    */
   public static int difDiasEntre2fechasMesStr(String strFecha1, String strFecha2) {
      int intDia = Integer.valueOf(strFecha1.substring(6, 8));
      int intMes = Integer.valueOf(strFecha1.substring(4, 6));
      int intAnio = Integer.valueOf(strFecha1.substring(0, 4));
      int intDia2 = Integer.valueOf(strFecha2.substring(6, 8));
      int intMes2 = Integer.valueOf(strFecha2.substring(4, 6));
      int intAnio2 = Integer.valueOf(strFecha2.substring(0, 4));
      return difDiasEntre2fechasMes(intAnio, intMes, intDia, intAnio2, intMes2, intDia2);
   }

   /**
    * Nos regresa la fecha en letras [DIA] DE [MES] DEL [ANIO]
    * @param strFechaAAAAMMDD Es la fecha en formato AAAAMMDD
    * @return Nos regresa la llave para obtener el nombre del mes
    */
   public String DameFechaenLetra(String strFechaAAAAMMDD) {
      String strFechaSet = "";
      String strDiaIni = "";
      String strMesIni = "";
      String strAnioIni = "";
      if (!strFechaAAAAMMDD.isEmpty()) {
         strDiaIni = strFechaAAAAMMDD.substring(6, 8);
         strMesIni = strFechaAAAAMMDD.substring(4, 6);
         strAnioIni = strFechaAAAAMMDD.substring(0, 4);
         int intMes = 0;
         try {
            intMes = Integer.valueOf(strMesIni);
         } catch (NumberFormatException ex) {
         }
         String strNomMes = "";
         if (this.msgRes != null) {
            strNomMes = this.lstNomMeses[intMes - 1];
            strNomMes = msgRes.getMessage(strNomMes);
         } else {
            strNomMes = lstNomMesesESP[intMes - 1];
         }
         strFechaSet = strDiaIni + " DE " + strNomMes + " DE " + strAnioIni;
      } else {
      }
      return strFechaSet;
   }

   /**
    * Regresa el repositorio de mensajes de struts o nulo sino ha sido inicializado
    * @return Regresa el repositorio
    */
   public MessageResources getMsgRes() {
      return msgRes;
   }

   /**
    * Define el repositorio de mensajes de struts
    * @param msgRes Envia el repositorio
    */
   public void setMsgRes(MessageResources msgRes) {
      this.msgRes = msgRes;
   }
}
