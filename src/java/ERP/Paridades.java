/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import ERP.Monedas;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Corrije la paridad de las operaciones seleccionadas
 *
 * @author ZeusGalindo
 */
public class Paridades {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Corrige la paridad de todas las ventas
    *
    * @param strFechaIni Fecha inicial
    * @param strFechaFin Fecha final
    * @param oConn Es la conexion
    * @param intEmp_Id Es el id de la empresa
    * @param bolTodos Indica que aplica aunque ya tenga paridad
    */
   public void CorrigeVentas(String strFechaIni, String strFechaFin, Conexion oConn, int intEmp_Id, boolean bolTodos) {
      //Clase para obtener la paridad
      Monedas moneda = new Monedas(oConn);
      //Consulta a facturas
      String strConsulta = "select vta_facturas.FAC_ID,FAC_MONEDA,FAC_FECHA,FAC_TASAPESO "
              + "from vta_facturas\n"
              + "WHERE  FAC_MONEDA NOT IN (0,1) AND  vta_facturas.EMP_ID = " + intEmp_Id + ""
              + " and vta_facturas.FAC_FECHA>='" + strFechaIni + "'   "
              + " and vta_facturas.FAC_FECHA<='" + strFechaFin + "'   "
              + " ";
      try {
         ResultSet rs = oConn.runQuery(strConsulta, true);
         while (rs.next()) {
            //Vemos si tienen paridad asignada
            if (rs.getDouble("FAC_TASAPESO") == 1 || bolTodos) {
               double dblFactor = moneda.GetFactorConversion(rs.getString("FAC_FECHA"), 4, rs.getInt("FAC_MONEDA"), 1);
               String strUpdate = "update vta_facturas set FAC_TASAPESO = " + dblFactor + " where FAC_ID = " + rs.getInt("FAC_ID");
               oConn.runQueryLMD(strUpdate);
            }

         }
         rs.close();
         //Corregimos tickets
         strConsulta = "select vta_tickets.TKT_ID,TKT_MONEDA,TKT_FECHA,TKT_TASAPESO "
                 + "from vta_tickets\n"
                 + "WHERE  TKT_MONEDA NOT IN (0,1) AND  vta_tickets.EMP_ID = " + intEmp_Id + ""
                 + " and vta_tickets.TKT_FECHA>='" + strFechaIni + "'   "
                 + " and vta_tickets.TKT_FECHA<='" + strFechaFin + "'   "
                 + " ";
         rs = oConn.runQuery(strConsulta, true);
         while (rs.next()) {
            //Vemos si tienen paridad asignada
            if (rs.getDouble("TKT_TASAPESO") == 1 || bolTodos) {
               double dblFactor = moneda.GetFactorConversion(rs.getString("TKT_FECHA"), 4, rs.getInt("TKT_MONEDA"), 1);
               String strUpdate = "update vta_tickets set TKT_TASAPESO = " + dblFactor + " where TKT_ID = " + rs.getInt("TKT_ID");
               oConn.runQueryLMD(strUpdate);
            }
         }
         rs.close();
         //Corregimos PEDIDOS
         strConsulta = "select vta_pedidos.PD_ID,PD_MONEDA,PD_FECHA,PD_TASAPESO "
                 + "from vta_pedidos\n"
                 + "WHERE  PD_MONEDA NOT IN (0,1) AND  vta_pedidos.EMP_ID = " + intEmp_Id + ""
                 + " and vta_pedidos.PD_FECHA>='" + strFechaIni + "'   "
                 + " and vta_pedidos.PD_FECHA<='" + strFechaFin + "'   "
                 + " ";
         rs = oConn.runQuery(strConsulta, true);
         while (rs.next()) {
            //Vemos si tienen paridad asignada
            if (rs.getDouble("PD_TASAPESO") == 1 || bolTodos) {
               double dblFactor = moneda.GetFactorConversion(rs.getString("PD_FECHA"), 4, rs.getInt("PD_MONEDA"), 1);
               String strUpdate = "update vta_pedidos set PD_TASAPESO = " + dblFactor + " where PD_ID = " + rs.getInt("PD_ID");
               oConn.runQueryLMD(strUpdate);
            }
         }
         rs.close();
         //Corregimos movimientos del cliente
         strConsulta = "select vta_mov_cte.MC_ID,MC_MONEDA,MC_FECHA,MC_TASAPESO "
                 + "from vta_mov_cte\n"
                 + "WHERE  MC_MONEDA NOT IN (0,1) AND  vta_mov_cte.EMP_ID = " + intEmp_Id + ""
                 + " and vta_mov_cte.MC_FECHA>='" + strFechaIni + "'   "
                 + " and vta_mov_cte.MC_FECHA<='" + strFechaFin + "'   "
                 + " ";
         rs = oConn.runQuery(strConsulta, true);
         while (rs.next()) {
            //Vemos si tienen paridad asignada
            if (rs.getDouble("MC_TASAPESO") == 1 || bolTodos) {
               double dblFactor = moneda.GetFactorConversion(rs.getString("MC_FECHA"), 4, rs.getInt("MC_MONEDA"), 1);
               String strUpdate = "update vta_mov_cte set MC_TASAPESO = " + dblFactor + " where MC_ID = " + rs.getInt("MC_ID");
               oConn.runQueryLMD(strUpdate);
            }
         }
         rs.close();
      } catch (SQLException ex) {
         System.out.println(" Error en algo...");
      }

   }

   /**
    * Corrige la paridad de todas las ventas
    *
    * @param strFechaIni Fecha inicial
    * @param strFechaFin Fecha final
    * @param oConn Es la conexion
    * @param intEmp_Id Es el id de la empresa
    * @param bolTodos Indica que aplica aunque ya tenga paridad
    */
   public void CorrigeCXPagar(String strFechaIni, String strFechaFin, Conexion oConn, int intEmp_Id, boolean bolTodos) {
      //Clase para obtener la paridad
      Monedas moneda = new Monedas(oConn);
      //Consulta a cuentas por pagar
      String strConsulta = "SELECT\n"
              + "	vta_cxpagar.CXP_ID,\n"
              + "	vta_cxpagar.CXP_MONEDA,\n"
              + "	vta_cxpagar.CXP_PARIDAD,\n"
              + "	vta_cxpagar.CXP_FECHA\n"
              + "FROM\n"
              + "	vta_cxpagar\n"
              + "WHERE\n"
              + "	CXP_MONEDA NOT IN (0, 1)\n"
              + "AND EMP_ID = " + intEmp_Id + ""
              + " and vta_cxpagar.CXP_FECHA>='" + strFechaIni + "'   "
              + " and vta_cxpagar.CXP_FECHA<='" + strFechaFin + "'   "
              + "";
      try {
         ResultSet rs = oConn.runQuery(strConsulta, true);
         while (rs.next()) {
            //Vemos si tienen paridad asignada
            if (rs.getDouble("CXP_PARIDAD") == 1 || bolTodos) {
               double dblFactor = moneda.GetFactorConversion(rs.getString("CXP_FECHA"), 4, rs.getInt("CXP_MONEDA"), 1);
               String strUpdate = "update vta_cxpagar set CXP_PARIDAD = " + dblFactor + " where CXP_ID = " + rs.getInt("CXP_ID");
               oConn.runQueryLMD(strUpdate);
            }

         }
         rs.close();
         //Corregimos tickets
         strConsulta = "SELECT vta_mov_prov.MP_ID, \n"
                 + "	vta_mov_prov.MP_FECHA, \n"
                 + "	vta_mov_prov.MP_MONEDA, \n"
                 + "	vta_mov_prov.MP_TASAPESO\n"
                 + " FROM vta_mov_prov where EMP_ID =  3 AND vta_mov_prov.MP_MONEDA NOT IN(0,1)"
                 + " and vta_mov_prov.MP_FECHA>='" + strFechaIni + "'   "
                 + " and vta_mov_prov.MP_FECHA<='" + strFechaFin + "'   "
                 + " ";
         rs = oConn.runQuery(strConsulta, true);
         while (rs.next()) {
            //Vemos si tienen paridad asignada
            if (rs.getDouble("MP_TASAPESO") == 1 || bolTodos) {
               double dblFactor = moneda.GetFactorConversion(rs.getString("MP_FECHA"), 4, rs.getInt("MP_MONEDA"), 1);
               String strUpdate = "update vta_mov_prov set MP_TASAPESO = " + dblFactor + " where MP_ID = " + rs.getInt("MP_ID");
               oConn.runQueryLMD(strUpdate);
            }
         }
         rs.close();
      } catch (SQLException ex) {
         System.out.println(" Error en algo...");
      }

   }
   // </editor-fold>
}
