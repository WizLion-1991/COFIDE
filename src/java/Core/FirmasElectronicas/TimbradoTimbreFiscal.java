package Core.FirmasElectronicas;

/**
 *Clase que realiza las operaciones de timbrado del PAC Timbre Fiscal
 * @author zeus
 */
public class TimbradoTimbreFiscal extends Timbrado_Pacs{

   public TimbradoTimbreFiscal(String strPathProperties) {
      super(strPathProperties);
   }

   @Override
   public String timbra_Factura(String strArchivo) {
      return "";
   }

   @Override
   public String timbra_Recibo(String strArchivo) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

}
