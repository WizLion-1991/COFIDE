package Core.FirmasElectronicas.Addendas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *Formato para las fechas
 * @author zeus
 */
public class DateFormatter extends XmlAdapter<String, Date> {

   protected DateFormat formatter;

   /**
    * Constructor
    */
   public DateFormatter() {
      formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
   }

   /**
    * Transform a date in a long to a GregorianCalendar
    *
    * @param date es la fecha
    * @return regresa el objeto xml gregorian
    */
   public static XMLGregorianCalendar long2Gregorian(long date) {
      DatatypeFactory dataTypeFactory;
      try {
         dataTypeFactory = DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException e) {
         throw new RuntimeException(e);
      }
      GregorianCalendar gc = new GregorianCalendar();
      gc.setTimeInMillis(date);
      return dataTypeFactory.newXMLGregorianCalendar(gc);
   }

   /**
    * Transform a date in Date to XMLGregorianCalendar
    * @param date Es la fecha
    * @return Regresa el objeto xml gregorian
    */
   public static XMLGregorianCalendar date2Gregorian(Date date) {
      return long2Gregorian(date.getTime());
   }

   @Override
   public Date unmarshal(String v) throws Exception {
      return formatter.parse(v);
   }

   @Override
   public String marshal(Date v) throws Exception {
      return formatter.format(v);
   }
}
