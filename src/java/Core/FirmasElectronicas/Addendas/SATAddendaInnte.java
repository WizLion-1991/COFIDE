/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas.Addendas;

import Core.FirmasElectronicas.SAT22.Comprobante;
import Core.FirmasElectronicas.SAT22.Comprobante.Addenda;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import Core.FirmasElectronicas.Addendas.innte.*;
/**
 *Genera el XML de la addenda de Innte
 * @author zeus
 * ALTER TABLE vta_facturas ADD IN_ODC VARCHAR(15) NOT NULL DEFAULT '';
 */
public class SATAddendaInnte extends SATAddenda {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   @Override
   public void FillAddenda(Addenda addenda, Comprobante objComp,
           String strPath, int intTransaccion, Conexion oConn) {
      //Obtenemos el id de la orden de compra
      String strIN_ODC = "";
      String strNOTAS = "";
      /*Consultamos datos del docto*/
      String strSql = "SELECT IN_ODC,FAC_NOTAS "
              + " FROM vta_facturas WHERE FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            strIN_ODC = rs.getString("IN_ODC");
            strNOTAS = rs.getString("FAC_NOTAS");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      //Instanciamos constructor de INNTE
      ObjectFactory objFac = new ObjectFactory();
      Inte inte  = objFac.createInte();
      inte.setOrdenDeCompra(strIN_ODC);
      
      addenda.getAny().add(inte);
   }

   @Override
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
   }
   // </editor-fold>
}
