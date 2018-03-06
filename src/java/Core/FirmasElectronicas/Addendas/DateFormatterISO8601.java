/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas.Addendas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Representa la fecha con formato ISO 8601
 *
 * @author ZeusGalindo
 */
public class DateFormatterISO8601 extends XmlAdapter<String, Date> {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected DateFormat formatter;
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public DateFormatterISO8601() {
//      formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");//Solución que funciona con el SDK 7
      formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//Solución que funciona con el SDK 6 el detalle es que es manual
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public Date unmarshal(String v) throws Exception {
      return formatter.parse(v);
   }

   @Override
   public String marshal(Date v) throws Exception {
//      DateTime fechaj = new DateTime(v);
//      return formatter.format(v) ;//Solución que funciona con el SDK 7
      String strFechaFormateada = formatter.format(v);
      strFechaFormateada = strFechaFormateada.substring(0, strFechaFormateada.length() - 2) + ":" + strFechaFormateada.substring( strFechaFormateada.length() - 2,strFechaFormateada.length() );
      //strFechaFormateada = strFechaFormateada.replaceAll( "$1",":(\\d\\d)$");
      return strFechaFormateada;//+ "-06:00";
//      return fechaj.toString();
   }
   // </editor-fold>
}
