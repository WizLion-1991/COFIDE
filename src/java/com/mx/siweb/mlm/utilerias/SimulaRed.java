/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.utilerias;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;

/**
 * Genera una red de prueba
 *
 * @author ZeusGalindo
 */
public class SimulaRed {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SimulaRed.class.getName());
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Genera una red de clientes de acuerdo a parametros
    *
    * @param intSucursal Es la sucursal
    * @param intIdEmpresa Es el id de la empresa
    * @param intNodosMax Es el numero maximo de nodos por rama
    * @param intTotPers Es el total de nodos por generar
    * @param strNomLado Campo donde dejamos la marca de I=IZQUIERDA Y D=DERECHA
    * @param oConn Es la conexion
    */
   public void GeneraRed(int intSucursal, int intIdEmpresa, int intNodosMax, int intTotPers,
           String strNomLado,
           Conexion oConn) {
      //Borramos todos los clientes
      log.debug("Borramos datos...");
      oConn.runQueryLMD("DELETE FROM vta_cliente WHERE CT_ID <> 1");
      oConn.runQueryLMD("ALTER TABLE vta_cliente AUTO_INCREMENT = 1");
      //Cola para almacenar
      Vector<Integer> lstCtes = new Vector<Integer>();
      lstCtes.add(1);
      //Ciclo para generar nodos
      int intNumNodosGen = 0;
      boolean bolSalir = false;
      ResultSet rs;
      while (intNumNodosGen < intTotPers || bolSalir == true) {
         int intCte = lstCtes.firstElement();
         lstCtes.remove(0);
         for (int i = 0; i < intNodosMax; i++) {
            int intNvoCte = 0;
            log.debug("Insertamos registros datos...");
            oConn.runQueryLMD("INSERT INTO vta_cliente"
                    + "(CT_UPLINE,CT_RAZONSOCIAL,CT_SPONZOR,SC_ID,EMP_ID)VALUES("
                    + "" + intCte + ",'CLIENTE NUMERO " + intCte + "'," + intCte + "," + intSucursal + "," + intIdEmpresa + ");");
            //Obtenemos ID autonumerico
            String strSql = "select @@identity ";
            try {
               rs = oConn.runQuery(strSql);
               //System.out.println(rs.getFetchSize());
               while (rs.next()) {
                  String strValor = rs.getString("@@identity");
                  try {
                     intNvoCte = Integer.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     log.error(ex.getMessage());
                     bolSalir = true;
                  }
                  /*Marcamos el nodo si es binario*/
                  String strMarcaBinario = "";
                  if (intNodosMax == 2 && !strNomLado.equals("")) {
                     if (i == 0) {
                        strMarcaBinario = "," + strNomLado + " = 'I' ";
                     } else {
                        strMarcaBinario = "," + strNomLado + " = 'D' ";
                     }

                  }

                  oConn.runQueryLMD("UPDATE vta_cliente  "
                          + " SET CT_RAZONSOCIAL = 'CLIENTE NUMERO " + intNvoCte + "' " + strMarcaBinario
                          + " WHERE CT_ID = " + intNvoCte);
                  lstCtes.add(intNvoCte);
                  intNumNodosGen++;
               }
                //if(rs.getStatement() != null )rs.getStatement().close(); 
                rs.close();

            } catch (SQLException ex) {
               log.error(ex.getMessage());
               bolSalir = true;
            }
         }
      }
   }
   // </editor-fold>
}
