/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas.Addendas;

import Core.FirmasElectronicas.Addendas.santander.ObjectFactory;
import Core.FirmasElectronicas.Addendas.santander.TAddendaSantanderV1;
import Core.FirmasElectronicas.Addendas.santander.TInformacionPago;
import Core.FirmasElectronicas.SAT22.Comprobante;
import Core.FirmasElectronicas.SAT22.Comprobante.Addenda;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *Genera el XML de la addenda de Santander
 * @author zeus
 * ALTER TABLE vta_facturas ADD SAN_USADDENDA INT NOT NULL DEFAULT 0;
 * ALTER TABLE vta_facturas ADD SAN_CODIGOPROV VARCHAR(30) NOT NULL DEFAULT'';
 * ALTER TABLE vta_facturas ADD SAN_ORDENCOMPRA VARCHAR(10) NOT NULL DEFAULT'';
 */
public class SATAddendaSantander extends SATAddenda {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   @Override
   public void FillAddenda(Addenda addenda, Comprobante objComp,
           String strPath, int intTransaccion, Conexion oConn) {
      String strSAN_CODIGOPROV = "";
      String strSAN_ORDENCOMPRA = "";
      String strEmailEmp = "";
      int intUserId = 0;
      /*Consultamos datos del docto*/
      String strSql = "SELECT SAN_CODIGOPROV,FAC_US_ALTA,SAN_ORDENCOMPRA "
              + " FROM vta_facturas WHERE FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            strSAN_CODIGOPROV = rs.getString("SAN_CODIGOPROV");
            strSAN_ORDENCOMPRA = rs.getString("SAN_ORDENCOMPRA");
            intUserId = rs.getInt("FAC_US_ALTA");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
         //Obtenemos el email del usuario que genera la factura
         strSql = "SELECT EMAIL "
                 + " FROM usuarios WHERE id_usuarios = " + intUserId;
         rs = oConn.runQuery(strSql);
         while (rs.next()) {
            strEmailEmp = rs.getString("EMAIL");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
         //Si el mail esta vacio tomamos el mail del primer usuario
         if(strEmailEmp.isEmpty()){
            
         }
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      //Instanciamos constructor de SANTANDER
      ObjectFactory objFac = new ObjectFactory();
      
      //Objeto maestro de SANTANDER
      TAddendaSantanderV1 objSantander = objFac.createTAddendaSantanderV1(); //Aqui llenamos los atributos que necesitamos
      TInformacionPago objPago = objFac.createTInformacionPago();
      objPago.setEmail(strEmailEmp);
      objPago.setNumProveedor(strSAN_CODIGOPROV);
      objPago.setOrdenCompra(strSAN_ORDENCOMPRA);
      objSantander.getInformacionPago().add(objPago);
      
      addenda.getAny().add(objFac.createAddendaSantanderV1(objSantander));
   }

   @Override
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
   }
   // </editor-fold>
}
