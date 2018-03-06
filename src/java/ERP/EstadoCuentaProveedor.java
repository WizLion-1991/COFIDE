/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import com.mx.siweb.erp.reportes.entities.EdoMovCte;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.logging.log4j.LogManager;

/**
 * Realiza las operaciones de la pantalla de estado de cuenta de proveedores
 *
 * @author ZeusGalindo
 */
public class EstadoCuentaProveedor {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EstadoCuentaProveedor.class.getName());
   private String targetFileName;
   private String sourceFileName;
   boolean bolImpreso = false;

   public boolean isBolImpreso() {
      return bolImpreso;
   }

   public void setBolImpreso(boolean bolImpreso) {
      this.bolImpreso = bolImpreso;
   }

   public String getTargetFileName() {
      return targetFileName;
   }

   public void setTargetFileName(String targetFileName) {
      this.targetFileName = targetFileName;
   }

   public String getSourceFileName() {
      return sourceFileName;
   }

   public void setSourceFileName(String sourceFileName) {
      this.sourceFileName = sourceFileName;
   }
   private String strRazonSocial;
   private double dblMontoCredito;
   private int intDiasCredito;
   private String strMoneda;
   private double dblSaldoinicial = 0;
   private double dblCargos = 0;
   private double dblAbonos = 0;
   private double dblSaldoFinal = 0;
   private ArrayList<EdoMovCte> lstData;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public EstadoCuentaProveedor() {
      bolImpreso = false;
   }

   public EstadoCuentaProveedor(Conexion oConn) {
      bolImpreso = false;
      this.oConn = oConn;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Obtiene la informaci��n general del proveedor
    *
    * @param intIdProveedor Es el id del proveedor
    * @return Regresa el xml con informacion del proveedor
    */
   public String getInfoGral(int intIdProveedor) {
      UtilXml util = new UtilXml();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      if (!this.bolImpreso) {
         strXML.append("<proveedores>\n");
      }
      String strSql = "select PV_ID,PV_RAZONSOCIAL,PV_MONTOCRED,PV_DIASCREDITO,MON_ID "
              + " from vta_proveedor where PV_ID = " + intIdProveedor;
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {

            if (!this.bolImpreso) {

               strXML.append("<proveedor ");
               strXML.append(" id = \"").append(rs.getInt("PV_ID")).append("\" ");
               strXML.append(" nombre = \"").append(util.Sustituye(rs.getString("PV_RAZONSOCIAL"))).append("\" ");
               strXML.append(" monto_credito = \"").append(rs.getDouble("PV_MONTOCRED")).append("\" ");
               strXML.append(" dias_credito = \"").append(rs.getDouble("PV_DIASCREDITO")).append("\" ");
               strXML.append(" moneda = \"").append(rs.getInt("MON_ID")).append("\" ");
               strXML.append(" >\n");
               strXML.append("<monedas>");
               //Buscamos que monedas usa el proveedor
               String strSql2 = "select distinct vta_mov_prov.MP_MONEDA,vta_monedas.MON_DESCRIPCION from vta_mov_prov,vta_monedas where  "
                       + "vta_mov_prov.MP_MONEDA = vta_monedas.MON_ID and PV_ID =" + rs.getInt("PV_ID");
               ResultSet rs2 = this.oConn.runQuery(strSql2, true);
               while (rs2.next()) {
                  strXML.append("<moneda ");
                  strXML.append(" id = \"").append(rs2.getInt("MP_MONEDA")).append("\" ");
                  strXML.append(" desc = \"").append(rs2.getString("MON_DESCRIPCION")).append("\" ");
                  strXML.append(" />\n");
               }
               rs2.close();
               strXML.append("</monedas>");
               strXML.append("</proveedor>");
            } else {
               this.strRazonSocial = rs.getString("PV_RAZONSOCIAL");
               this.dblMontoCredito = rs.getDouble("PV_MONTOCRED");
               this.intDiasCredito = rs.getInt("PV_DIASCREDITO");
            }
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      if (!this.bolImpreso) {
         strXML.append("</proveedores>\n");
      }
      return strXML.toString();
   }

   /**
    * Obtiene la informaci��n de los saldos del proveedor y en el periodo
    * determinado
    *
    * @param intIdProveedor Es el id del cliente
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo a buscar
    * @return Regresa un xml
    */
   public String getInfoSaldos(int intIdProveedor, int intMoneda, String strPeriodo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");

      //SALDO INICIAL
      String strSql = "SELECT \n"
              + " if(SUM(MP_CARGO-MP_ABONO) is null, 0 ,\n"
              + "SUM(\n"
              + "(MP_CARGO-MP_ABONO) * \n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID) = vta_mov_prov.MP_MONEDA , 1,\n"
              + "if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,if(vta_mov_prov.MP_MONEDA = 1 or (vta_mov_prov.MP_MONEDA = 2 and $P{Moneda} = 3 ),1/MP_TASAPESO,MP_TASAPESO),GetParidadAuto(vta_mov_prov.MP_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID),4,MP_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + "\n"
              + ")\n"
              + ") AS SALDO_INICIAL\n"
              + "FROM vta_proveedor,vta_mov_prov WHERE vta_mov_prov.PV_ID=vta_proveedor.PV_ID and vta_proveedor.PV_ID=  $P{Proveedor} \n"
              + "AND LEFT(MP_FECHA,6) < '$P{Periodo}' AND MP_ANULADO = 0 "
              + " and if($P{SoloMonedaOrig} = 1,if(vta_mov_prov.CXP_ID = 0,vta_mov_prov.MP_MONEDA = $P{Moneda},(SELECT CXP_MONEDA FROM vta_cxpagar WHERE vta_cxpagar.CXP_ID=vta_mov_prov.CXP_ID) =$P{Moneda} ),1=1 )";
      strSql = strSql.replace("$P{Proveedor}", intIdProveedor + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      strSql = strSql.replace("$P{SoloMonedaOrig}", 0 + "");
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
      strSql = "SELECT \n"
              + " if(SUM(MP_CARGO) is null, 0 ,\n"
              + "SUM(\n"
              + "(MP_CARGO) * \n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID) = vta_mov_prov.MP_MONEDA , 1,\n"
              + "if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,if(vta_mov_prov.MP_MONEDA = 1 or (vta_mov_prov.MP_MONEDA = 2 and $P{Moneda} = 3 ),1/MP_TASAPESO,MP_TASAPESO),GetParidadAuto(vta_mov_prov.MP_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID),4,MP_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + "\n"
              + ")\n"
              + ") AS TCARGO\n"
              + "FROM vta_proveedor,vta_mov_prov WHERE vta_mov_prov.PV_ID=vta_proveedor.PV_ID and vta_proveedor.PV_ID=  $P{Proveedor} \n"
              + " AND LEFT(vta_mov_prov.MP_FECHA,6) >= '$P{Periodo}' "
              + " AND LEFT(vta_mov_prov.MP_FECHA,6) <= '$P{Periodo}'  AND MP_ANULADO = 0 "
              + " and if($P{SoloMonedaOrig} = 1,if(vta_mov_prov.CXP_ID = 0,vta_mov_prov.MP_MONEDA = $P{Moneda},(SELECT CXP_MONEDA FROM vta_cxpagar WHERE vta_cxpagar.CXP_ID=vta_mov_prov.CXP_ID) =$P{Moneda} ),1=1 )";
      strSql = strSql.replace("$P{Proveedor}", intIdProveedor + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      strSql = strSql.replace("$P{SoloMonedaOrig}", 0 + "");
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
      strSql = "SELECT \n"
              + " if(SUM(MP_ABONO) is null, 0 ,\n"
              + "SUM(\n"
              + "(MP_ABONO) * \n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID) = vta_mov_prov.MP_MONEDA , 1,\n"
              + "if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,if(vta_mov_prov.MP_MONEDA = 1 or (vta_mov_prov.MP_MONEDA = 2 and $P{Moneda} = 3 ),1/MP_TASAPESO,MP_TASAPESO),GetParidadAuto(vta_mov_prov.MP_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID),4,MP_FECHA) )\n"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + "\n"
              + ")\n"
              + ") AS TABONO\n"
              + "FROM vta_proveedor,vta_mov_prov WHERE vta_mov_prov.PV_ID=vta_proveedor.PV_ID and vta_proveedor.PV_ID=  $P{Proveedor} \n"
              + " AND LEFT(vta_mov_prov.MP_FECHA,6) >= '$P{Periodo}' "
              + " AND LEFT(vta_mov_prov.MP_FECHA,6) <= '$P{Periodo}'  AND MP_ANULADO = 0 "
              + " and if($P{SoloMonedaOrig} = 1,if(vta_mov_prov.CXP_ID = 0,vta_mov_prov.MP_MONEDA = $P{Moneda},(SELECT CXP_MONEDA FROM vta_cxpagar WHERE vta_cxpagar.CXP_ID=vta_mov_prov.CXP_ID) =$P{Moneda} ),1=1 )";
      strSql = strSql.replace("$P{Proveedor}", intIdProveedor + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      strSql = strSql.replace("$P{SoloMonedaOrig}", 0 + "");
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
      //Validamos si se imprime el xml
      if (!this.bolImpreso) {
         strXML.append("<saldos ");
         strXML.append(" inicial = \"").append(dblSaldoinicial).append("\" ");
         strXML.append(" cargos = \"").append(dblCargos).append("\" ");
         strXML.append(" abonos = \"").append(dblAbonos).append("\" ");
         strXML.append(" final = \"").append(dblSaldoFinal).append("\" ");
         strXML.append(" />");
      }
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
      //Listamos facturas
      String strSql = "select DISTINCT LEFT(CXP_FECHA,6) as tnom from vta_cxpagar order by LEFT(CXP_FECHA,6) desc";
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
         //Listamos tickets
         strSql = "select DISTINCT LEFT(TKT_FECHA,6) as tnom from vta_tickets order by LEFT(TKT_FECHA,6) desc";
         rs = this.oConn.runQuery(strSql, true);
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
         //Listamos tickets
         strSql = "select DISTINCT LEFT(MP_FECHA,6) as tnom from vta_mov_prov order by LEFT(MP_FECHA,6) desc";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            boolean bolExiste = false;
            Iterator<String> it = lstPeriodos.iterator();
            while (it.hasNext()) {
               String strNomFecha = it.next();
               if (strNomFecha.equals(rs.getString("tnom"))) {
                  bolExiste = true;
               }
            }
            if (!bolExiste && !rs.getString("tnom").isEmpty()) {
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
    * Regresa el saldo del periodo
    *
    * @param intIdProveedor Es el id del proveedor
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo
    * @return Regresa una cadena con un xml
    */
   public String getSaldosPeriodos(int intIdProveedor, int intMoneda, String strPeriodo, int intSoloConSaldo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      if (!this.bolImpreso) {
         strXML.append("<docs>");
      }

      String strSql = "SELECT CXP_ID, DATE_FORMAT(STR_TO_DATE(CXP_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,\n"
              + "vta_proveedor.PV_ID,CXP_FOLIO,CXP_RAZONSOCIAL,CXP_TOTAL,CXP_SALDO,CXP_IMPORTE,CXP_IMPUESTO1,CXP_RAZONSOCIAL, CXP_DIASCREDITO,\n"
              + " DATEDIFF(CURDATE(),DATE_ADD(STR_TO_DATE(CXP_FECHA,'%Y%m%d'), INTERVAL CXP_DIASCREDITO DAY)) as dias   \n"
              + ",DATE_FORMAT(DATE_ADD(STR_TO_DATE(CXP_FECHA,'%Y%m%d'), INTERVAL CXP_DIASCREDITO DAY),'%d/%m/%Y') AS VENCIMIENTO\n"
              + ",CXP_MONEDA,(select MON_DESCRIPCION from vta_monedas where MON_ID=CXP_MONEDA  ) as NOM_MONEDA,vta_proveedor.PV_DIASCREDITO\n"
              + ",1 as tasa\n"
              + ",if(CXP_DIASCREDITO = 0,if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(CXP_FECHA,'%Y%m%d'), INTERVAL CXP_DIASCREDITO DAY)) > 0,'VENCIDO','CONTADO'),if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(CXP_FECHA,'%Y%m%d'), INTERVAL CXP_DIASCREDITO DAY)) > 0, 'VENCIDO', 'VIGENTE')) AS ESTATUS \n"
              + ",vta_empresas.EMP_RAZONSOCIAL as EMPRESA\n"
              + ",(\n"
              + "select GROUP_CONCAT(DISTINCT vta_bcos.BC_DESCRIPCION) from  vta_mov_cta_bcos_rela,vta_mov_prov,vta_mov_cta_bcos, vta_bcos where \n"
              + " vta_mov_prov.MP_ID = vta_mov_cta_bcos_rela.MP_ID and \n"
              + " vta_mov_cta_bcos_rela.MCB_ID = vta_mov_cta_bcos.MCB_ID and  \n"
              + "vta_cxpagar.CXP_ID = vta_mov_prov.CXP_ID and \n"
              + "vta_mov_cta_bcos.MCB_FECHA<= $P{Periodo} and\n"
              + "vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and\n"
              + "vta_mov_prov.MP_ANULADO = 0 and vta_mov_prov.MP_ABONO> 0 ) as TBANCO\n"
              + "FROM vta_cxpagar,vta_proveedor, vta_empresas\n"
              + " where vta_cxpagar.PV_ID = vta_proveedor.PV_ID AND CXP_ANULADO = 0 \n"
              + " AND vta_cxpagar.EMP_ID = vta_empresas.EMP_ID \n"
              + " AND vta_proveedor.PV_ID = $P{Proveedor} and vta_cxpagar.CXP_MONEDA = $P{Moneda}  \n";
      if (intSoloConSaldo == 1) {
         strSql += " AND ("
                 + "(LEFT(vta_cxpagar.CXP_FECHA,6) <= $P{Periodo} AND vta_cxpagar.CXP_SALDO >= 1)  "
                 + " OR (select COUNT(vta_mov_prov.CXP_ID) from  vta_mov_prov where \n"
                 + "vta_cxpagar.CXP_ID = vta_mov_prov.CXP_ID AND \n"
                 + "LEFT(vta_mov_prov.MP_FECHA,6) = $P{Periodo} \n"
                 + "AND LEFT(vta_cxpagar.CXP_FECHA,6) < $P{Periodo} \n"
                 + "AND MP_ESPAGO = 1 AND MP_ANULADO = 0 ) )";
      } else {
         strSql += " AND ("
                 + "(LEFT(vta_cxpagar.CXP_FECHA,6) = $P{Periodo} ) "
                 + " OR (LEFT(vta_cxpagar.CXP_FECHA,6) <= $P{Periodo} AND vta_cxpagar.CXP_SALDO >= 1 ) "
                 + " OR (select COUNT(vta_mov_prov.CXP_ID) from  vta_mov_prov where \n"
                 + "vta_cxpagar.CXP_ID = vta_mov_prov.CXP_ID AND \n"
                 + "LEFT(vta_mov_prov.MP_FECHA,6) = $P{Periodo} \n"
                 + "AND LEFT(vta_cxpagar.CXP_FECHA,6) <= $P{Periodo} \n"
                 + "AND MP_ESPAGO = 1 AND MP_ANULADO = 0 ) ) ";
      }
      strSql += " order by DATEDIFF(CURDATE(),STR_TO_DATE(CXP_FECHA,'%Y%m%d')) asc";
      strSql = strSql.replace("$P{Proveedor}", intIdProveedor + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intMonedaCXP = rs.getInt("CXP_MONEDA");
            double dblSaldoCXP = 0;
            //Analizamos el saldo
            strSql = "SELECT "
                    + "  if(SUM(MP_CARGO - MP_ABONO) is null, 0 , "
                    + " SUM( "
                    + " (MP_CARGO - MP_ABONO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if(if(" + intMonedaCXP + " <> 0," + intMonedaCXP + ",vta_proveedor.MON_ID) = vta_mov_prov.MP_MONEDA , 1, "
                    + " if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,if(vta_mov_prov.MP_MONEDA = 1 or (vta_mov_prov.MP_MONEDA = 2 and " + intMonedaCXP + " = 3 ),1/MP_TASAPESO,MP_TASAPESO),GetParidadAuto(vta_mov_prov.MP_MONEDA,if(" + intMonedaCXP + " <> 0,1,vta_proveedor.MON_ID),4,MP_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_proveedor,vta_mov_prov WHERE vta_mov_prov.PV_ID=vta_proveedor.PV_ID and vta_proveedor.PV_ID = " + intIdProveedor
                    + " AND LEFT(vta_mov_prov.MP_FECHA,6) <=" + strPeriodo + " AND (CXP_ID = " + rs.getInt("CXP_ID") + " ) AND MP_ANULADO = 0 ";
            ResultSet rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblSaldoCXP = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();
            //Filtro para solo con saldo
            boolean bolPasa = true;
            if (intSoloConSaldo == 1) {
               if (dblSaldoCXP <= 1) {
                  bolPasa = false;
               }
            }
            //Solo si pasa validaciones para mostrarse
            if (bolPasa) {

               if (!this.bolImpreso) {
                  strXML.append("<doc");
                  strXML.append(" id=\"").append(rs.getInt("CXP_ID")).append("\" ");
                  strXML.append(" tipo=\"").append("CXPAGAR").append("\" ");
                  strXML.append(" folio=\"").append(rs.getString("CXP_FOLIO")).append("\" ");
                  strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
                  strXML.append(" vencimiento=\"").append(rs.getString("VENCIMIENTO")).append("\" ");
                  strXML.append(" cargos=\"").append(rs.getDouble("CXP_TOTAL")).append("\" ");
                  strXML.append(" abonos=\"").append(rs.getDouble("CXP_TOTAL") - dblSaldoCXP).append("\" ");
                  strXML.append(" saldo=\"").append(dblSaldoCXP).append("\" ");
                  strXML.append(" estatus=\"").append(rs.getString("ESTATUS")).append("\" ");
                  if (rs.getString("TBANCO") == null) {
                     strXML.append(" banco=\"\" ");
                  } else {
                     strXML.append(" banco=\"").append(rs.getString("TBANCO")).append("\" ");
                  }
                  strXML.append("/>\n");
               } else {
                  //Objeto para impresion
                  EdoMovCte edo = new EdoMovCte();
                  edo.setTipoDocumento("CXPAGAR");
                  edo.setFolio(rs.getString("CXP_FOLIO"));
                  edo.setFecha(rs.getString("FECHA"));
                  edo.setVencimiento(rs.getString("VENCIMIENTO"));
                  edo.setDblCargos(rs.getDouble("CXP_TOTAL"));
                  edo.setDblAbonos(rs.getDouble("CXP_TOTAL") - dblSaldoCXP);
                  edo.setDblSaldo(dblSaldoCXP);
                  edo.setEstatus(rs.getString("ESTATUS"));
                  if (rs.getString("TBANCO") == null) {
                     edo.setBanco("");
                  } else {
                     edo.setBanco(rs.getString("TBANCO"));
                  }
                  this.lstData.add(edo);
               }

            }
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      //Obtenemos los anticipos en el periodo
      strSql = "SELECT MP_ID, LEFT(vta_mov_prov.MP_FECHA,6) as periodo,DATE_FORMAT(STR_TO_DATE(MP_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,vta_proveedor.PV_ID,MP_FOLIO,"
              + " MP_ABONO,MP_SALDO_ANTICIPO,MP_ANTICIPO,\n"
              + "MP_MONEDA,(select MON_DESCRIPCION from vta_monedas where MON_ID=MP_MONEDA  ) as NOM_MONEDA,vta_proveedor.PV_DIASCREDITO\n"
              + ",vta_empresas.EMP_RAZONSOCIAL as EMPRESA\n"
              + ",(\n"
              + "select GROUP_CONCAT(DISTINCT vta_bcos.BC_DESCRIPCION) from  vta_mov_cta_bcos_rela,vta_mov_cta_bcos, vta_bcos where \n"
              + " vta_mov_prov.MP_ID = vta_mov_cta_bcos_rela.MP_ID and \n"
              + " vta_mov_cta_bcos_rela.MCB_ID = vta_mov_cta_bcos.MCB_ID and  \n"
              + " vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and\n"
              + " vta_mov_prov.MP_ANULADO = 0 and vta_mov_prov.MP_ABONO> 0 "
              + ") as TBANCO,MP_ESPAGO,MP_USA_ANTICIPO "
              + " FROM vta_mov_prov,vta_proveedor, vta_empresas\n"
              + " where vta_mov_prov.PV_ID = vta_proveedor.PV_ID AND MP_ANULADO = 0 AND MP_ABONO > 0\n"
              + " AND vta_mov_prov.EMP_ID = vta_empresas.EMP_ID \n"
              + " AND vta_proveedor.PV_ID = $P{Proveedor} and vta_mov_prov.MP_MONEDA = $P{Moneda}  \n";
      strSql += " AND ("
              + "(LEFT(vta_mov_prov.MP_FECHA,6) = $P{Periodo} ) "
              + " or (MP_ANTICIPO = 1 AND LEFT(vta_mov_prov.MP_FECHA,6) <= $P{Periodo}) "//Anadimos la opcion de mostrar anticipos no aplicados
              + " ) ";
      strSql += " order by DATEDIFF(CURDATE(),STR_TO_DATE(MP_FECHA,'%Y%m%d')) asc,MP_FOLIO";
      strSql = strSql.replace("$P{Proveedor}", intIdProveedor + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            //Analisis del saldo
            double dblSaldoAnticipo = 0;
            strSql = "SELECT "
                    + "  if(SUM(MP_ABONO) is null, 0 , "
                    + " SUM( "
                    + " (MP_ABONO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if(if(" + intMoneda + " <> 0," + intMoneda + ",vta_proveedor.MON_ID) = vta_mov_prov.MP_MONEDA , 1, "
                    + " if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,if(vta_mov_prov.MP_MONEDA = 1 or (vta_mov_prov.MP_MONEDA = 2 and " + intMoneda + " = 3 ),1/MP_TASAPESO,MP_TASAPESO),GetParidadAuto(vta_mov_prov.MP_MONEDA,if(" + intMoneda + " <> 0," + intMoneda + ",vta_proveedor.MON_ID),4,MP_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_proveedor,vta_mov_prov WHERE vta_mov_prov.PV_ID=vta_proveedor.PV_ID and vta_proveedor.PV_ID = " + intIdProveedor
                    + " AND LEFT(vta_mov_prov.MP_FECHA,6) <=" + strPeriodo + " AND (MP_ANTI_ID = " + rs.getInt("MP_ID") + " ) AND MP_ANULADO = 0  and vta_mov_prov.MP_MONEDA =  " + intMoneda + " AND MP_ESPAGO= 1";
            ResultSet rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblSaldoAnticipo = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();

            double dblCargoAnticipo = 0;
            strSql = "SELECT "
                    + "  if(SUM(MP_CARGO) is null, 0 , "
                    + " SUM( "
                    + " (MP_CARGO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if((" + intMoneda + ") = vta_mov_prov.MP_MONEDA , 1, "
                    + " if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,if(vta_mov_prov.MP_MONEDA = 1 or (vta_mov_prov.MP_MONEDA = 2 and " + intMoneda + " = 3 ),1/MP_TASAPESO,MP_TASAPESO),GetParidadAuto(vta_mov_prov.MP_MONEDA,(" + intMoneda + "),4,MP_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_mov_prov WHERE "
                    + "  MP_FECHA <='" + strPeriodo + "' AND (MP_ANTI_ID = " + rs.getInt("MP_ID") + " ) "
                    + " AND ( MP_USA_ANTICIPO = 0 AND MP_CARGO>0) "
                    + " AND MP_ANULADO = 0  ";
//07/04/2015 Lo quitamos porque hay movimientos en varias monedas and vta_mov_prov.MP_MONEDA =  " + lstMonedasId[hk] + "
            rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblCargoAnticipo = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();
            log.debug("MP_ABONO " + rs.getDouble("MP_ABONO"));
            log.debug("dblSaldoAnticipo " + dblSaldoAnticipo);
            log.debug("dblCargoAnticipo " + dblCargoAnticipo);
            
            dblSaldoAnticipo = rs.getDouble("MP_ABONO") - dblSaldoAnticipo - dblCargoAnticipo;
            System.out.println("Saldo anticipo: " + rs.getInt("MP_ID") + " " + dblSaldoAnticipo);
            boolean bolMuestra = true;

            //Filtro para solo con saldo
            log.debug("dblSaldoAnticipo " + dblSaldoAnticipo);
            if (intSoloConSaldo == 1) {

               if (dblSaldoAnticipo <= 1) {
                  bolMuestra = false;
               }
            } else {
               //Si no es solo con saldo y es anticipo mostramos solo los activos 
               //y los del periodo actual
               if (rs.getString("periodo").equals(strPeriodo)) {
                  //No validamos nada
               } else {
                  if (dblSaldoAnticipo <= 1) {
                     bolMuestra = false;
                  }
               }
            }
            //Solo si pasa validaciones para mostrarse
            if (bolMuestra) {

               if (!this.bolImpreso) {

                  strXML.append("<doc");
                  //Evaluamos el tipo de registro
                  if (rs.getInt("MP_ESPAGO") == 1) {
                     strXML.append(" id=\"").append(rs.getInt("MP_ID")).append("\" ");
                     strXML.append(" tipo=\"").append("COBRO").append("\" ");
                     strXML.append(" folio=\"").append(rs.getString("MP_FOLIO")).append("\" ");
                  } else {
                     strXML.append(" id=\"").append(rs.getInt("MP_ID")).append("\" ");
                     strXML.append(" tipo=\"").append("OTRO").append("\" ");
                     strXML.append(" folio=\"").append(rs.getString("MP_FOLIO")).append("\" ");
                  }

                  strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
                  strXML.append(" vencimiento=\"").append("").append("\" ");
                  strXML.append(" cargos=\"").append(0).append("\" ");
                  strXML.append(" abonos=\"").append(rs.getDouble("MP_ABONO")).append("\" ");
                  //Anadimos la opcion de mostrar anticipos no aplicados
                  if (rs.getInt("MP_ANTICIPO") == 1) {
                     strXML.append(" saldo=\"").append(dblSaldoAnticipo).append("\" ");
                  } else {
                     strXML.append(" saldo=\"").append(0).append("\" ");
                  }
                  strXML.append(" anticipo=\"").append(rs.getInt("MP_ANTICIPO")).append("\" ");
                  //Estatus
                  if (rs.getInt("MP_USA_ANTICIPO") == 1) {
                     strXML.append(" estatus=\"").append("Uso anticipo").append("\" ");
                  } else {
                     if (rs.getInt("MP_ANTICIPO") == 1) {
                        strXML.append(" estatus=\"").append("Anticipo").append("\" ");
                     } else {
                        strXML.append(" estatus=\"").append("Saldado").append("\" ");
                     }

                  }
                  if (rs.getString("TBANCO") == null) {
                     strXML.append(" banco=\"\" ");
                  } else {
                     strXML.append(" banco=\"").append(rs.getString("TBANCO")).append("\" ");
                  }
                  strXML.append("/>\n");
               } else {
                  //Objeto para impresion
                  EdoMovCte edo = new EdoMovCte();
                  //Evaluamos el tipo de registro
                  if (rs.getInt("MP_ESPAGO") == 1) {
                     edo.setTipoDocumento("COBRO");
                     edo.setFolio(rs.getString("MP_FOLIO"));
                  } else {
                     //if (rs.getInt("NC_ID") != 0) {
                     //   edo.setTipoDocumento("NCREDITO");
                     //   edo.setFolio(rs.getString("NC_FOLIO_C"));
                     //} else {
                     edo.setTipoDocumento("OTRO");
                     edo.setFolio(rs.getString("MP_FOLIO"));
                     // }
                  }

                  edo.setFecha(rs.getString("FECHA"));
                  edo.setVencimiento("");
                  edo.setDblCargos(0.0);
                  edo.setDblAbonos(rs.getDouble("MP_ABONO"));

                  //Anadimos la opcion de mostrar anticipos no aplicados
                  if (rs.getInt("MP_ANTICIPO") == 1) {
                     edo.setDblSaldo(dblSaldoAnticipo);
                  } else {
                     edo.setDblSaldo(0);
                  }
                  //strXML.append(" anticipo=\"").append(rs.getInt("MC_ANTICIPO")).append("\" ");
                  //Estatus
                  if (rs.getInt("MP_USA_ANTICIPO") == 1) {
                     edo.setEstatus("Uso anticipo");
                  } else {
                     if (rs.getInt("MP_ANTICIPO") == 1) {
                        edo.setEstatus("Anticipo");
                     } else {
                        edo.setEstatus("Saldado");
                     }
                  }
                  if (rs.getString("TBANCO") == null) {
                     edo.setBanco("");
                  } else {
                     edo.setBanco(rs.getString("TBANCO"));
                  }
                  this.lstData.add(edo);
               }
            }
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      if (!this.bolImpreso) {
         strXML.append("</docs>");
      }

      return strXML.toString();
   }

   /**
    * Obtiene el historial de movimientos de una cuenta por pagar
    *
    * @param intIdProveedor Es el id del proveedor
    * @param intMoneda Es el id del moneda
    * @param intIdCxp Es el id de la cuenta por pagar
    * @return Regresa el xml con la informaci��n
    */
   public String getHistorialCxp(int intIdProveedor, int intMoneda, int intIdCxp) {
      String banco;
      ResultSet rBanco;
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<docs>");
      String strSql = "SELECT *, DATE_FORMAT(STR_TO_DATE(MP_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,"
              + "(SELECT CXP_FOLIO FROM vta_cxpagar where vta_cxpagar.CXP_ID =vta_mov_prov.CXP_ID ) AS FOLIO_CXP,\n"
              + "if(MP_ESPAGO = 1, 'PAGO',if(CXP_ID <> 0,'C.PAGAR','---')) AS CONCEPTO,\n"
              + "(SELECT MON_DESCRIPCION FROM vta_monedas where vta_monedas.MON_ID =vta_mov_prov.MP_MONEDA )AS NOM_MONEDA,\n"
              + "( PV_RAZONSOCIAL ) as PROVEEDOR,\n"
              + "(EMP_RAZONSOCIAL ) as EMPRESA,\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID)  = vta_mov_prov.MP_MONEDA , 1,\n"
              + "if(MP_TASAPESO <> 1 AND MP_TASAPESO <> 0 ,MP_TASAPESO,getParidad(vta_mov_prov.MP_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_proveedor.MON_ID),4,MP_FECHA) )\n"
              + "\n"
              + ") as tasa\n"
              + ",vta_empresas.EMP_PATHIMG as LOGO\n"
              + "FROM vta_mov_prov,vta_proveedor,vta_empresas WHERE  \n"
              + "vta_mov_prov.EMP_ID=vta_empresas.EMP_ID AND  \n"
              + "vta_mov_prov.PV_ID = vta_proveedor.PV_ID and vta_proveedor.PV_ID =  $P{Cliente}\n"
              + "  AND vta_mov_prov.CXP_ID = $P{FacturaId} AND vta_mov_prov.MP_ANULADO = 0 \n"
              + "ORDER BY MP_FECHA;";
      strSql = strSql.replace("$P{Cliente}", intIdProveedor + "");
      strSql = strSql.replace("$P{FacturaId}", intIdCxp + "");
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");

      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<doc");
            strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
            strXML.append(" folio=\"").append(rs.getString("FOLIO_CXP")).append("\" ");
            strXML.append(" concepto=\"").append(rs.getString("CONCEPTO")).append("\" ");
            strXML.append(" cargos=\"").append(rs.getDouble("MP_CARGO")).append("\" ");
            strXML.append(" abonos=\"").append(rs.getDouble("MP_ABONO")).append("\" ");
            strXML.append(" paridad=\"").append(rs.getDouble("tasa")).append("\" ");
            strXML.append(" moneda=\"").append(rs.getString("MP_MONEDA")).append("\" ");
            strXML.append(" id=\"").append(rs.getString("MP_ID")).append("\" ");
            strXML.append(" id_Masivo=\"").append(rs.getString("MPM_ID")).append("\" ");
            strXML.append(" id_Cxp=\"").append(rs.getString("CXP_ID")).append("\" ");
            strXML.append(" mc_folio=\"").append(rs.getString("MP_FOLIO")).append("\" ");
            if (rs.getString("MPM_ID").equals("0")) {
               banco = "SELECT BC_DESCRIPCION FROM vta_bcos where BC_ID=(SELECT BC_ID FROM vta_mov_cta_bcos WHERE MP_ID = '" + rs.getString("MP_ID") + "')";
               rBanco = this.oConn.runQuery(banco, true);
               if (rBanco.next()) {
                  strXML.append(" banco=\"").append(rBanco.getString("BC_DESCRIPCION")).append("\" ");
               } else {
                  strXML.append(" banco=\"").append(" ").append("\" ");
               }
            } else {
               banco = "SELECT BC_DESCRIPCION FROM vta_bcos where BC_ID=( SELECT a.BC_ID FROM vta_mov_cta_bcos a, vta_mov_cta_bcos_rela b WHERE a.MCB_ID = b.MCB_ID and b.MP_ID = '" + rs.getString("MP_ID") + "')";
               rBanco = this.oConn.runQuery(banco, true);
               if (rBanco.next()) {
                  strXML.append(" banco=\"").append(rBanco.getString("BC_DESCRIPCION")).append("\" ");
               } else {
                  strXML.append(" banco=\"").append(" ").append("\" ");
               }
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

   /**
    * Genera el reporte en pdf
    *
    * @param intIdProveedor Es el id del cliente
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo
    * @param intSoloConSaldo Indica si es solo con saldo
    * @param varSesiones Es la sesion
    * @param byteArrayOutputStream Es el stream de salida
    */
   public void getReportPrint(int intIdProveedor, int intMoneda, String strPeriodo,
           int intSoloConSaldo, VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream) {
      this.bolImpreso = true;
      lstData = new ArrayList<EdoMovCte>();
      //Hacemos los calculos para el reporte
      log.debug("Comenzamos los calculos");
      //Datos generales
      getInfoGral(intIdProveedor);
      String strSql2 = "select vta_monedas.MON_DESCRIPCION from vta_monedas where vta_monedas.MON_ID  =" + intMoneda;
      ResultSet rs2;
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            this.strMoneda = rs2.getString("MON_DESCRIPCION");
         }
         rs2.close();
      } catch (SQLException ex) {
         Logger.getLogger(EstadoCuentaCliente.class.getName()).log(Level.SEVERE, null, ex);
      }
      //Logo de la empresa
      String strPathLogoWeb = "";
      strSql2 = "select EMP_PATHIMGFORM from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            strPathLogoWeb = rs2.getString("EMP_PATHIMGFORM");
         }
         rs2.close();
      } catch (SQLException ex) {
         Logger.getLogger(EstadoCuentaCliente.class.getName()).log(Level.SEVERE, null, ex);
      }

      //Saldos Globales
      getInfoSaldos(intIdProveedor, intMoneda, strPeriodo);
      //Saldos a detalle
      getSaldosPeriodos(intIdProveedor, intMoneda, strPeriodo, intSoloConSaldo);
      //Generamos el pdf
      InputStream reportStream = null;
      try {
         log.debug("Comenzamos a generar el reporte jrxml");
         //Parametros
         Map parametersMap = new HashMap();
         log.debug("Parametros");
         parametersMap.put("NumProveedor", intIdProveedor + "");
         parametersMap.put("NombreCliente", this.strRazonSocial);
         parametersMap.put("SaldoInicial", this.dblSaldoinicial);
         parametersMap.put("Cargos", this.dblCargos);
         parametersMap.put("Abonos", this.dblAbonos);
         parametersMap.put("SaldoFinal", this.dblSaldoFinal);
         parametersMap.put("Moneda", this.strMoneda);
         parametersMap.put("Periodo", strPeriodo);
         parametersMap.put("MontoCredito", this.dblMontoCredito);
         parametersMap.put("DiasCredito", this.intDiasCredito);
         if (intSoloConSaldo == 1) {
            parametersMap.put("SoloConSaldo", "SI");
         } else {
            parametersMap.put("SoloConSaldo", "NO");
         }
         parametersMap.put("PathLogoWeb", strPathLogoWeb);
         log.debug("Llenado de jasper " + this.sourceFileName);

         reportStream = new FileInputStream(this.sourceFileName);
         // Bing the datasource with the collection
         JRDataSource datasource = new JRBeanCollectionDataSource(lstData, true);
         // Compile and print the jasper report
         JasperReport report = JasperCompileManager.compileReport(reportStream);
         //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
         JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);

         log.debug("Salida de reporte " + targetFileName);

         //Dependiendo del parametro lo guardamos localmente o lo enviamos en un hilo
         if (byteArrayOutputStream == null) {
            JasperExportManager.exportReportToPdfFile(print, targetFileName);
         } else {
            JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
         }
         log.debug("Termina generacion de reporte ");

      } catch (FileNotFoundException ex) {
         log.error(ex.getMessage());
      } catch (JRException ex) {
         log.error(ex.getMessage());
      } catch (Exception ex) {
         log.error(ex.getMessage());
      } finally {
         try {
            reportStream.close();
         } catch (IOException ex) {
            log.error(ex.getMessage());
         }
      }
   }
   // </editor-fold>
}
