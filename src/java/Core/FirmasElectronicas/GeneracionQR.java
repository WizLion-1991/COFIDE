package Core.FirmasElectronicas;

import java.io.File;
import java.io.IOException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.logging.log4j.LogManager;

/**
 *Clase que genera el codigo de barras para las facturas
 * @author zeus
 */
public class GeneracionQR {
   
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(GeneracionQR.class.getName());

   /**
    * Genera el QR de un CFDI
    * @param rfcEmpresaEmisor RFC del emisor del cfdi
    * @param rfcClienteReceptor RFC del cliente del cfdi
    * @param importeTotalFactura Importe del cfdi
    * @param folioFiscalUUID Folio fiscal del cfdi
    * @param strPath Path donde se guardara
    * @param intDoc_Id Numero de documento
    * @param intTipoDoc Es el tipo de documento, 
    * 1. es factura 
    * 2. es Nota de credito 
    * 3. es nominas 
    * 4. es recibos de retencion 
    * 5. es notas de cargo
    * @return Regresa OK{PATHQR} si se genero el QR o ERROR:{MSG}
    */
   public static String generaQR(String rfcEmpresaEmisor,
           String rfcClienteReceptor,
           String importeTotalFactura,
           String folioFiscalUUID, String strPath, int intDoc_Id, int intTipoDoc) {
      String strResult = "OK";
      QRCodeWriter qrWriter = new QRCodeWriter();
      try {
         //Hashtable params = new Hashtable();
         String strNomTipo = "_FAC_";
         if(intTipoDoc == 2){
            strNomTipo = "_NC_";
         }
         if(intTipoDoc == 3){
            strNomTipo = "_NOM_";
         }
         if(intTipoDoc == 4){
            strNomTipo = "_RET_";
         }
         if(intTipoDoc == 5){
            strNomTipo = "_NCARGO_";
         }
         if(intTipoDoc == 6){
            strNomTipo = "_NCARGOPROV_";
         }
         //params.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
         File fileTemp = new File(strPath + "qrCFDI" + strNomTipo + intDoc_Id + ".png");
         String dato = "?re=" + rfcEmpresaEmisor + "&rr=" + rfcClienteReceptor + "&tt=" + importeTotalFactura + "&id=" + folioFiscalUUID;
         strResult += strPath + "qrCFDI" + strNomTipo + intDoc_Id + ".png";
         log.debug("strResult: " + strResult);
         MatrixToImageWriter.writeToFile(qrWriter.encode(dato, BarcodeFormat.QR_CODE, 625, 625), "png", fileTemp);
      } catch (WriterException e) {
         log.error(e.getMessage());
         strResult = "ERROR:" + e.getMessage();
      } catch (IOException e) {
         log.error(e.getMessage());
         strResult = "ERROR:" + e.getMessage();
      }
      return strResult;
   }
}
