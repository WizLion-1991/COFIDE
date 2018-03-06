/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class GeneraFolios {

   Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(GeneraFolios.class.getName());
   boolean bolFolioGlobal;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public GeneraFolios() {

      this.bolFolioGlobal = false;
   }

   public GeneraFolios(Conexion oConn) {
      this.oConn = oConn;
      this.bolFolioGlobal = false;
   }

   public void UpdateFolio(int intEMP_ID) {
      Folios folio = new Folios();
      //Determinamos el tipo de ticket
      int intTipoFolio = Folios.FACTURA;


      String strSQL = "select FAC_ID from vta_facturas where fac_sellotimbre is null";
      try {
         ResultSet rs = this.oConn.runQuery(strSQL, true);
         while (rs.next()) {
            String strUPDATE = "UPDATE vta_facturas SET  FAC_ES_CFD = 1 Where FAC_ID=" + rs.getInt("FAC_ID");
            this.oConn.runQueryLMD(strUPDATE);
         }
         String strSQL2 = "Select FAC_ID From vta_facturas Where FAC_ES_CFD=0 And EMP_ID = " + intEMP_ID;
         ResultSet rs2 = this.oConn.runQuery(strSQL2, true);
         while (rs2.next()) {
            String strFolio = "";
            strFolio = folio.doFolio(this.oConn, intTipoFolio, this.bolFolioGlobal, intEMP_ID);
            String strUPDATE2 = "UPDATE vta_facturas SET  FAC_FOLIO_C ='" + strFolio + "' Where FAC_ID=" + rs2.getInt("FAC_ID");
            this.oConn.runQueryLMD(strUPDATE2);

         }
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }

   }
}
