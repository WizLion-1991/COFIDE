package Core.FirmasElectronicas;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 *Clase que coloca un prefijo personalizado cuando se usan addendas
 * @author zeus
 */
public class MyNamespacePrefixMapper extends NamespacePrefixMapper {

   public String getPreferredPrefix(String namespaceUri,
           String suggestion,
           boolean requirePrefix) {
      if (requirePrefix) {
         if ("http://recepcionfe.mabempresa.com/cfd/addenda/v1".equals(namespaceUri)) {
            return "mabe";
         }
         if ("http://www.itcomplements.com/printweb".equals(namespaceUri)) {
            return "Sonoco";
         }
         return suggestion;
      } else {
         return "";
      }
   }
}
