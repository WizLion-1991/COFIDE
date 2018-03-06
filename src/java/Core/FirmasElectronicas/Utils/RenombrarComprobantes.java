/*
 * Renombra en una carpeta todos los xml en base al id de vta_factura
 */
package Core.FirmasElectronicas.Utils;

import Core.FirmasElectronicas.GeneracionQR;
import ERP.MovProveedor;
import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import org.apache.logging.log4j.LogManager;

/**
 * Renombra en una carpeta todos los xml en base al id de vta_factura
 *
 * @author ZeusGalindo
 */
public class RenombrarComprobantes {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(RenombrarComprobantes.class.getName());
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Renombra los xml de los comprobantes para que coincidan con el id de
    * vta_factura
    *
    * @param oConn Es la conexion a la base de datos
    * @param strPath Es el path
    * @param strRFCEmisor Es el RFC del emisor
    */
   public void RenombrarXmlCFDI(Conexion oConn, String strPath, String strRFCEmisor) {
      try {
         //Path de los archivos
         /*String strPath = "/Users/ZeusGalindo/Documents/Zeus/dbs/RespaldoXml20141017/";
          String strRFCEmisor = "ALI790531PK6";*/
         String strSql = "select * from vta_facturas where FAC_ES_CFD = 0";
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intId = rs.getInt("FAC_ID");
            String strUUIDFind = rs.getString("FAC_FOLIO");
//            String strFolioC = rs.getString("FAC_FOLIO_C");
            String strNomFileSupuesto = "XmlSAT" + intId + " .xml";
            double dblTotal = rs.getDouble("FAC_TOTAL");
            String strRFCReceptor = rs.getString("FAC_RFC");

            //Buscamos en todos los xml...
            log.debug("Buscamos el archivo " + intId);
            String files;
            File folder = new File(strPath);
            File[] listOfFiles = folder.listFiles();
            boolean bolFoundXml = false;
            for (int i = 0; i < listOfFiles.length; i++) {
               if (listOfFiles[i].isFile()) {
                  files = listOfFiles[i].getName();
                  if (files.toUpperCase().startsWith("XMLSAT") && files.toUpperCase().endsWith(".XML")) {
                     MovProveedor mov = new MovProveedor(oConn, null, null);
                     mov.CargaDatosXML(listOfFiles[i].getAbsolutePath());
                     if (mov.getStrUUID().equals(strUUIDFind)) {
                        if (strNomFileSupuesto.equals(files)) {
                           log.debug("\u001B[32m Encontramos el archivo xml..." + files);
                        } else {
                           log.debug("\u001B[31m Renombramos el archivo xml..." + files);
                        }
                        //Copiamos el archivo
                        String strPathNuevo = strPath + "Modificados/" + strNomFileSupuesto;
                        File fileDestino = new File(strPathNuevo);
                        listOfFiles[i].renameTo(fileDestino);
                        //Marcamos que ya lo encontramos y cerramos el ciclo...
                        bolFoundXml = true;
                        break;

                     }
                  }
               }
            }
            //Generamos el QR de nuevo
            GeneracionQR.generaQR(strRFCEmisor, strRFCReceptor, new DecimalFormat("0000000000.000000").format(dblTotal), strUUIDFind, strPath + "Modificados/", intId, 1);

            //Si no encontro lo indicamos
            if (!bolFoundXml) {
               log.debug("\u001B[36m No encontro el archivo..." + strNomFileSupuesto);
            }
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }
   // </editor-fold>
}
