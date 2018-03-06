/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;

/**
 * Realiza las operaciones de la pantalla de estado de cuenta de bancos
 *
 * @author ZeusGalindo
 */
public class EstadoCuentaBanco {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EstadoCuentaProveedor.class.getName());

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public EstadoCuentaBanco() {
   }

   public EstadoCuentaBanco(Conexion oConn) {
      this.oConn = oConn;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Obtiene los datos generales del banco
    *
    * @param intIdBco Es el id del banco
    * @return Regresa un xml con los datos
    */
   public String getInfoGral(int intIdBco) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<bancos>\n");
      String strSql = "select BC_ID,BC_DESCRIPCION,BC_MONEDA,BC_CTA_BANC,BC_MONEDA "
              + " from vta_bcos where BC_ID = " + intIdBco;
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<banco ");
            strXML.append(" id = \"").append(rs.getInt("BC_ID")).append("\" ");
            strXML.append(" nombre = \"").append(rs.getString("BC_DESCRIPCION")).append("\" ");
            strXML.append(" moneda = \"").append(rs.getInt("BC_MONEDA")).append("\" ");
            strXML.append(" cuenta = \"").append(rs.getString("BC_CTA_BANC")).append("\" ");
            strXML.append(" />\n");
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      strXML.append("</bancos>\n");
      return strXML.toString();
   }

   /**
    * Obtiene la información de los saldos del banco y en el periodo determinado
    *
    * @param intIdBco Es el id del banco
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo a buscar
    * @return Regresa un xml
    */
   public String getInfoSaldos(int intIdBco, int intMoneda, String strPeriodo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");

      double dblSaldoinicial = 0;
      double dblCargos = 0;
      double dblAbonos = 0;
      double dblSaldoFinal = 0;
      //SALDO INICIAL
      String strSql = "SELECT sum(MCB_DEPOSITO-MCB_RETIRO),if(sum(MCB_DEPOSITO-MCB_RETIRO) is null,0,\n"
              + "sum(\n"
              + "(MCB_DEPOSITO-MCB_RETIRO)*\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = vta_mov_cta_bcos.MCB_MONEDA , 1,\n"
              + "if(MCB_PARIDAD <> 1 AND MCB_PARIDAD <> 0 ,if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = 1,MCB_PARIDAD,1/MCB_PARIDAD),GetParidadAuto(vta_mov_cta_bcos.MCB_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA),4,MCB_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + ")\n"
              + ")  as  SALDO_INICIAL\n"
              + "FROM vta_bcos,vta_mov_cta_bcos WHERE\n"
              + "vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and \n"
              + "vta_bcos.BC_ID= $P{Banco} AND LEFT(MCB_FECHA,6) < $P{Periodo} AND MCB_ANULADO = 0 AND MCB_CONCILIADO = 1;";
      strSql = strSql.replace("$P{Banco}", intIdBco + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblSaldoinicial = rs.getDouble("SALDO_INICIAL");
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      //CARGOS
      strSql = "SELECT if(sum(MCB_DEPOSITO) is null,0,SUM(MCB_DEPOSITO\n"
              + "*\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = vta_mov_cta_bcos.MCB_MONEDA , 1,\n"
              + "if(MCB_PARIDAD <> 1 AND MCB_PARIDAD <> 0 ,if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = 1,MCB_PARIDAD,1/MCB_PARIDAD),GetParidadAuto(vta_mov_cta_bcos.MCB_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA),4,MCB_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + ")\n"
              + ") AS TCARGO FROM vta_mov_cta_bcos,vta_bcos WHERE\n"
              + "vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and  vta_bcos.BC_ID = $P{Banco} AND LEFT(vta_mov_cta_bcos.MCB_FECHA,6) >= $P{Periodo} "
              + " AND LEFT(vta_mov_cta_bcos.MCB_FECHA,6) <= $P{Periodo} AND vta_mov_cta_bcos.MCB_ANULADO = 0 AND MCB_CONCILIADO = 1;";
      strSql = strSql.replace("$P{Banco}", intIdBco + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblCargos = rs.getDouble("TCARGO");
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      //ABONOS
      strSql = "SELECT if(sum(MCB_RETIRO) is null,0,SUM(MCB_RETIRO\n"
              + "*\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = vta_mov_cta_bcos.MCB_MONEDA , 1,\n"
              + "if(MCB_PARIDAD <> 1 AND MCB_PARIDAD <> 0 ,if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = 1,MCB_PARIDAD,1/MCB_PARIDAD),GetParidadAuto(vta_mov_cta_bcos.MCB_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA),4,MCB_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + ")\n"
              + ") AS TABONO FROM vta_mov_cta_bcos,vta_bcos WHERE\n"
              + "vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and  vta_bcos.BC_ID = $P{Banco} AND LEFT(vta_mov_cta_bcos.MCB_FECHA,6) >= $P{Periodo}"
              + " AND LEFT(vta_mov_cta_bcos.MCB_FECHA,6) <= $P{Periodo} AND vta_mov_cta_bcos.MCB_ANULADO = 0 AND MCB_CONCILIADO = 1;";
      strSql = strSql.replace("$P{Banco}", intIdBco + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblAbonos = rs.getDouble("TABONO");
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      dblSaldoFinal = dblSaldoinicial + dblCargos - dblAbonos;
      strXML.append("<saldos ");
      strXML.append(" inicial = \"").append(dblSaldoinicial).append("\" ");
      strXML.append(" cargos = \"").append(dblCargos).append("\" ");
      strXML.append(" abonos = \"").append(dblAbonos).append("\" ");
      strXML.append(" final = \"").append(dblSaldoFinal).append("\" ");
      strXML.append(" />");
      return strXML.toString();
   }

   /**
    * Obtiene el listado de periodos
    *
    * @return Regresa un xml con el listado de periodos
    */
   public String getPeriodos() {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<periodos>");
      ArrayList<String> lstPeriodos = new ArrayList<String>();
      //Listamos movimientos del banco
      String strSql = "select DISTINCT LEFT(MCB_FECHA,6) as tnom from vta_mov_cta_bcos order by LEFT(MCB_FECHA,6) desc";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            boolean bolExiste = false;
            Iterator<String> it = lstPeriodos.iterator();
            while (it.hasNext()) {
               String strNomFecha = it.next();
               if (strNomFecha.equals(rs.getString("tnom"))) {
                  bolExiste = true;
               }
            }
            if (!bolExiste) {
               lstPeriodos.add(rs.getString("tnom"));
            }

         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      //Ordenamos el arreglo
//      Collections.sort(lstPeriodos);
      //Generamos el xml
      Iterator<String> it = lstPeriodos.iterator();
      while (it.hasNext()) {
         String strNomFecha = it.next();
         strXML.append("<periodo");
         strXML.append(" valor=\"").append(strNomFecha.substring(0, 4) + "/" + strNomFecha.substring(4, 6)).append("\" ");
         strXML.append("/>\n");
      }
      strXML.append("</periodos>");
      return strXML.toString();
   }

   /**
    * Obtiene los movimientos del periodo y banco seleccionados
    *
    * @param intIdBco Es el id del banco
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo definido
    * @return Regresa el xml con la info
    */
   public String getSaldosPeriodos(int intIdBco, int intMoneda, String strPeriodo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<docs>");
      //Consulta
      String strSql = "SELECT *, DATE_FORMAT(STR_TO_DATE(MCB_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA\n"
              + ",(SELECT FAC_FOLIO FROM vta_facturas where vta_facturas.FAC_ID =vta_mov_cta_bcos.FAC_ID ) AS FOLIO_FAC\n"
              + ",(SELECT FAC_RAZONSOCIAL FROM vta_facturas where vta_facturas.FAC_ID =vta_mov_cta_bcos.FAC_ID ) AS BENEFICIARIO_FAC\n"
              + ",if(MCB_DEPOSITO > 0, 'DEPOSITO','RETIRO') AS CONCEPTO,\n"
              + "(SELECT TKT_FOLIO FROM vta_tickets where vta_tickets.TKT_ID =vta_mov_cta_bcos.TKT_ID )AS FOLIO_TKT,\n"
              + "(SELECT TKT_RAZONSOCIAL FROM vta_tickets where vta_tickets.TKT_ID =vta_mov_cta_bcos.TKT_ID )AS BENEFICIARIO_TKT,\n"
              + "(SELECT CXP_FOLIO FROM vta_cxpagar where vta_cxpagar.CXP_ID =vta_mov_cta_bcos.CXP_ID )AS FOLIO_CXP,\n"
              + "(SELECT CXP_RAZONSOCIAL FROM vta_cxpagar where vta_cxpagar.CXP_ID =vta_mov_cta_bcos.CXP_ID )AS BENEFICIARIO_CXP,\n"
              + "(select vta_cxpagar.CXP_FOLIO from vta_mov_prov,vta_cxpagar where vta_mov_prov.CXP_ID  = vta_cxpagar.CXP_ID AND MP_ID = vta_mov_cta_bcos.MP_ID)AS FOLIO_CXP2,"
              + "(select vta_cxpagar.CXP_RAZONSOCIAL from vta_mov_prov,vta_cxpagar where vta_mov_prov.CXP_ID  = vta_cxpagar.CXP_ID AND MP_ID = vta_mov_cta_bcos.MP_ID)AS BENEFICIARIO_CXP2,"
              + "(SELECT MON_DESCRIPCION FROM vta_monedas where vta_monedas.MON_ID =vta_mov_cta_bcos.MCB_MONEDA )AS NOM_MONEDA,\n"
              + "(SELECT vta_empresas.EMP_RAZONSOCIAL FROM vta_sucursal,vta_empresas where vta_sucursal.EMP_ID= vta_empresas.EMP_ID AND \n"
              + "vta_mov_cta_bcos.SC_ID=vta_sucursal.SC_ID) as EMPRESA,\n"
              + "(SELECT vta_empresas.EMP_PATHIMG FROM vta_sucursal,vta_empresas where vta_sucursal.EMP_ID= vta_empresas.EMP_ID AND \n"
              + "vta_mov_cta_bcos.SC_ID=vta_sucursal.SC_ID) as LOGO,BC_DESCRIPCION,\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA) = vta_mov_cta_bcos.MCB_MONEDA , 1,\n"
              + "if(MCB_PARIDAD <> 1 AND MCB_PARIDAD <> 0 ,MCB_PARIDAD,GetParidadAuto(vta_mov_cta_bcos.MCB_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_bcos.BC_MONEDA),4,MCB_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/ AS PARIDAD\n"
              + "FROM vta_bcos left join vta_mov_cta_bcos on vta_mov_cta_bcos.BC_ID=vta_bcos.BC_ID WHERE\n"
              + "vta_bcos.BC_ID = $P{Banco} AND MCB_ANULADO = 0 \n"
              + "AND left(MCB_FECHA,6) = $P{Periodo} \n"
              + "ORDER BY MCB_FECHA;";
      strSql = strSql.replace("$P{Banco}", intIdBco + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<doc");
            strXML.append(" id=\"").append(rs.getInt("MCB_ID")).append("\" ");
            strXML.append(" tipo=\"").append("Bancos").append("\" ");
            strXML.append(" folio_fac=\"").append(rs.getString("FOLIO_FAC")).append("\" ");
            strXML.append(" folio_tkt=\"").append(rs.getString("FOLIO_TKT")).append("\" ");
            strXML.append(" folio_cxp=\"").append(rs.getString("FOLIO_CXP")).append("\" ");
            strXML.append(" folio_cxp2=\"").append(rs.getString("FOLIO_CXP2")).append("\" ");
            strXML.append(" beneficiario_fac=\"").append(rs.getString("BENEFICIARIO_FAC")).append("\" ");
            strXML.append(" beneficiario_tkt=\"").append(rs.getString("BENEFICIARIO_TKT")).append("\" ");
            strXML.append(" beneficiario_cxp=\"").append(rs.getString("BENEFICIARIO_CXP")).append("\" ");
            strXML.append(" beneficiario_cxp2=\"").append(rs.getString("BENEFICIARIO_CXP2")).append("\" ");
            strXML.append(" FAC_ID=\"").append(rs.getString("FAC_ID")).append("\" ");
            strXML.append(" TKT_ID=\"").append(rs.getString("TKT_ID")).append("\" ");
            strXML.append(" CXP_ID=\"").append(rs.getString("CXP_ID")).append("\" ");
            strXML.append(" MP_ID=\"").append(rs.getString("MP_ID")).append("\" ");
            strXML.append(" moneda=\"").append(rs.getString("NOM_MONEDA")).append("\" ");
            strXML.append(" nom_moneda=\"").append(rs.getString("MCB_MONEDA")).append("\" ");
            strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
            strXML.append(" concepto=\"").append(rs.getString("MCB_CONCEPTO")).append("\" ");
            strXML.append(" beneficiario=\"").append(rs.getString("MCB_BENEFICIARIO")).append("\" ");
            strXML.append(" cargos=\"").append(rs.getDouble("MCB_DEPOSITO")).append("\" ");
            strXML.append(" abonos=\"").append(rs.getDouble("MCB_RETIRO")).append("\" ");
            strXML.append(" paridad=\"").append(rs.getDouble("PARIDAD")).append("\" ");
            if (rs.getInt("MCB_CONCILIADO") == 1) {
               strXML.append(" estatus=\"CONCILIADO\" ");
            } else {
               strXML.append(" estatus=\"POR CONCILIAR\" ");
            }
            strXML.append("/>\n");
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      strXML.append("</docs>");
      return strXML.toString();
   }
//   /**
//    * Regresa el historial del movimiento de bancos
//    *
//    * @param intIdBco Es el id del banco
//    * @param intMoneda Es el id de la moneda
//    * @param intIdMov Es el id del movimiento
//    * @return Regresa un xml con los datos solicitados
//    */
//   public String getHistorialMovimiento(int intIdBco, int intMoneda, int intIdMov) {
//      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
//      strXML.append("<docs>");
//      strXML.append("</docs>");
//
//      return strXML.toString();
//   }
   // </editor-fold>
}
