
package Core.FirmasElectronicas.Addendas;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;
/**
 *Clase que formatea la fecha del xml de jaxb en formato AAAAMMDD
 * @author zeus
 */
public class ShortDateFormatter extends XmlAdapter<String, Date> {
   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

   @Override
   public Date unmarshal(String v) throws Exception {
      return formatter.parse(v);
   }

   @Override
   public String marshal(Date v) throws Exception {
      return formatter.format(v);
   }

}
