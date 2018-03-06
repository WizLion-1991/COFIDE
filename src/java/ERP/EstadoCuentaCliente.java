/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import com.mx.siweb.erp.reportes.entities.EdoMovCte;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
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
 * Esta clase realiza las operaciones del estado de cuenta de clientes
 *
 * @author ZeusGalindo
 */
public class EstadoCuentaCliente {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EstadoCuentaCliente.class.getName());
   private String targetFileName;
   private String sourceFileName;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }
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
   private String strIdCte;
   private String strRazonSocial;
   private double dblMontoCredito;
   private int intDiasCredito;
   private String strMoneda;
   private double dblSaldoinicial = 0;
   private double dblCargos = 0;
   private double dblAbonos = 0;
   private double dblSaldoFinal = 0;
   private ArrayList<EdoMovCte> lstData;
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public EstadoCuentaCliente() {
      bolImpreso = false;
   }

   public EstadoCuentaCliente(Conexion oConn) {
      this.oConn = oConn;
      bolImpreso = false;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public String getInfoGral(int intIdCliente) {

      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      if (!this.bolImpreso) {
         strXML.append("<clientes>\n");
      }

      String strSql = "select CT_ID,CT_RAZONSOCIAL,CT_MONTOCRED,CT_DIASCREDITO,MON_ID "
              + " from vta_cliente where CT_ID = " + intIdCliente;
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            //Validamos la generaci��n del xml
            if (!this.bolImpreso) {
               strXML.append("<cliente ");
               strXML.append(" id = \"").append(rs.getInt("CT_ID")).append("\" ");
               strXML.append(" nombre = \"").append(rs.getString("CT_RAZONSOCIAL")).append("\" ");
               strXML.append(" monto_credito = \"").append(rs.getDouble("CT_MONTOCRED")).append("\" ");
               strXML.append(" dias_credito = \"").append(rs.getDouble("CT_DIASCREDITO")).append("\" ");
               strXML.append(" moneda = \"").append(rs.getInt("MON_ID")).append("\" ");
               strXML.append(" >\n");
               strXML.append("<monedas>");
               //Buscamos que monedas usa el cliente
               String strSql2 = "select distinct vta_mov_cte.MC_MONEDA,vta_monedas.MON_DESCRIPCION from vta_mov_cte,vta_monedas where  "
                       + "vta_mov_cte.MC_MONEDA = vta_monedas.MON_ID and CT_ID =" + rs.getInt("CT_ID");
               ResultSet rs2 = this.oConn.runQuery(strSql2, true);
               while (rs2.next()) {
                  strXML.append("<moneda ");
                  strXML.append(" id = \"").append(rs2.getInt("MC_MONEDA")).append("\" ");
                  strXML.append(" desc = \"").append(rs2.getString("MON_DESCRIPCION")).append("\" ");
                  strXML.append(" />\n");
               }
               rs2.close();
               strXML.append("</monedas>");
               strXML.append("</cliente>");
            } else {
               this.strIdCte = rs.getString("CT_ID");
               this.strRazonSocial = rs.getString("CT_RAZONSOCIAL");
               this.dblMontoCredito = rs.getDouble("CT_MONTOCRED");
               this.intDiasCredito = rs.getInt("CT_DIASCREDITO");
            }
         }
         rs.close();
      } catch (SQLException ex) {
         this.log.error(ex.getMessage());
      }
      if (!this.bolImpreso) {
         strXML.append("</clientes>\n");
      }

      return strXML.toString();
   }

   /**
    * Obtiene la informaci��n de los saldos del cliente y en el periodo
    * determinado
    *
    * @param intIdCliente Es el id del cliente
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo a buscar
    * @return Regresa un xml
    */
   public String getInfoSaldos(int intIdCliente, int intMoneda, String strPeriodo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");

      //SALDO INICIAL
      String strSql = "SELECT sum(MC_CARGO-MC_ABONO),if(sum(MC_CARGO-MC_ABONO) is null,0,\n"
              + "sum(\n"
              + "(MC_CARGO-MC_ABONO)*\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1,\n"
              + "if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 or (vta_mov_cte.MC_MONEDA = 2 and $P{Moneda} = 3 ),1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID),4,MC_FECHA) )"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + ")\n"
              + ")  as  SALDO_INICIAL\n"
              + "FROM vta_mov_cte,vta_cliente WHERE\n"
              + "vta_mov_cte.CT_ID = vta_cliente.CT_ID and \n"
              + "vta_cliente.CT_ID= $P{Cliente} AND LEFT(MC_FECHA,6) < $P{Periodo} AND MC_ANULADO = 0 "
              + " and if($P{SoloMoneda}= 1,\n"
              + "if(FAC_ID = 0,\n"
              + "if(NC_ID = 0 ,\n"
              + "if(TKT_ID = 0 ,vta_mov_cte.MC_MONEDA = $P{Moneda},(SELECT t.TKT_MONEDA from vta_tickets t WHERE t.TKT_ID =  vta_mov_cte.TKT_ID) = $P{Moneda})\n"
              + ",(SELECT n.NC_MONEDA from vta_ncredito n WHERE n.NC_ID =  vta_mov_cte.NC_ID) = $P{Moneda})\n"
              + ",(SELECT f.FAC_MONEDA from vta_facturas f WHERE f.FAC_ID =  vta_mov_cte.FAC_ID) = $P{Moneda})\n"
              + ",1=1)"
              + " ;";//and vta_mov_cte.MC_MONEDA = $P{Moneda};
      strSql = strSql.replace("$P{SoloMoneda}", 1 + "");
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
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
      strSql = "SELECT if(sum(MC_CARGO) is null,0,SUM(MC_CARGO\n"
              + "*\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1,\n"
              + "if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 or (vta_mov_cte.MC_MONEDA = 2 and $P{Moneda} = 3 ),1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID),4,MC_FECHA) )"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + ")\n"
              + ") AS TCARGO FROM vta_mov_cte,vta_cliente WHERE\n"
              + "vta_mov_cte.CT_ID = vta_cliente.CT_ID and  vta_cliente.CT_ID = $P{Cliente} "
              + " AND LEFT(vta_mov_cte.MC_FECHA,6) >= $P{Periodo} "
              + " AND LEFT(vta_mov_cte.MC_FECHA,6) <= $P{Periodo} AND MC_ANULADO = 0"
              + " and if($P{SoloMoneda} = 1,\n"
              + "if(FAC_ID = 0,\n"
              + "if(NC_ID = 0 ,\n"
              + "if(TKT_ID = 0 ,vta_mov_cte.MC_MONEDA = $P{Moneda},(SELECT t.TKT_MONEDA from vta_tickets t WHERE t.TKT_ID =  vta_mov_cte.TKT_ID) = $P{Moneda})\n"
              + ",(SELECT n.NC_MONEDA from vta_ncredito n WHERE n.NC_ID =  vta_mov_cte.NC_ID) = $P{Moneda})\n"
              + ",(SELECT f.FAC_MONEDA from vta_facturas f WHERE f.FAC_ID =  vta_mov_cte.FAC_ID) = $P{Moneda})\n"
              + ", 1 = 1)"
              + " ;";//and vta_mov_cte.MC_MONEDA = $P{Moneda};

      strSql = strSql.replace("$P{SoloMoneda}", 1 + "");
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
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
      strSql = "SELECT if(sum(MC_ABONO) is null,0,SUM(MC_ABONO\n"
              + "*\n"
              + "/*obtenemos la paridad*/\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1,\n"
              + "if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 or (vta_mov_cte.MC_MONEDA = 2 and $P{Moneda} = 3 ),1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID),4,MC_FECHA) )"
              + ")\n"
              + "/*obtenemos la paridad*/\n"
              + ")\n"
              + ") AS TABONO FROM vta_mov_cte,vta_cliente WHERE\n"
              + "vta_mov_cte.CT_ID = vta_cliente.CT_ID and  vta_cliente.CT_ID = $P{Cliente} "
              + " AND LEFT(vta_mov_cte.MC_FECHA,6) >= $P{Periodo} "
              + " AND LEFT(vta_mov_cte.MC_FECHA,6) <= $P{Periodo} AND MC_ANULADO = 0"
              + " and if($P{SoloMoneda} = 1, \n"
              + "if(FAC_ID = 0,\n"
              + "if(NC_ID = 0 ,\n"
              + "if(TKT_ID = 0 ,vta_mov_cte.MC_MONEDA = $P{Moneda},(SELECT t.TKT_MONEDA from vta_tickets t WHERE t.TKT_ID =  vta_mov_cte.TKT_ID) = $P{Moneda})\n"
              + ",(SELECT n.NC_MONEDA from vta_ncredito n WHERE n.NC_ID =  vta_mov_cte.NC_ID) = $P{Moneda})\n"
              + ",(SELECT f.FAC_MONEDA from vta_facturas f WHERE f.FAC_ID =  vta_mov_cte.FAC_ID) = $P{Moneda})\n"
              + ",1 = 1)"
              + " ;";//and vta_mov_cte.MC_MONEDA = $P{Moneda};
      strSql = strSql.replace("$P{SoloMoneda}", 1 + "");
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
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
      String strSql = "select DISTINCT LEFT(FAC_FECHA,6) as tnom from vta_facturas order by LEFT(FAC_FECHA,6) desc";
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
         //Listamos PAGOS
         strSql = "select DISTINCT LEFT(MC_FECHA,6) as tnom from vta_mov_cte order by LEFT(MC_FECHA,6) desc";
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
               log.debug("tnom: " + rs.getString("tnom") + " " + !rs.getString("tnom").isEmpty());
               if (!rs.getString("tnom").isEmpty()) {
                  lstPeriodos.add(rs.getString("tnom"));
               }

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
    * @param intIdCliente Es el id del cliente
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo
    * @return Regresa una cadena con un xml
    */
   public String getSaldosPeriodos(int intIdCliente, int intMoneda, String strPeriodo, int intSoloConSaldo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      if (!this.bolImpreso) {
         strXML.append("<docs>");
      }
      // <editor-fold defaultstate="collapsed" desc="Facturas">
      String strSql = "SELECT FAC_ID, DATE_FORMAT(STR_TO_DATE(FAC_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,vta_cliente.CT_ID,FAC_FOLIO_C,"
              + "FAC_RAZONSOCIAL,FAC_TOTAL,FAC_SALDO,FAC_IMPORTE,FAC_IMPUESTO1,FAC_RAZONSOCIAL, FAC_DIASCREDITO,\n"
              + " DATEDIFF(CURDATE(),DATE_ADD(STR_TO_DATE(FAC_FECHA,'%Y%m%d'), INTERVAL FAC_DIASCREDITO DAY)) as dias   \n"
              + ",DATE_FORMAT(DATE_ADD(STR_TO_DATE(FAC_FECHA,'%Y%m%d'), INTERVAL FAC_DIASCREDITO DAY),'%d/%m/%Y') AS VENCIMIENTO\n"
              + ",FAC_MONEDA,(select MON_DESCRIPCION from vta_monedas where MON_ID=FAC_MONEDA  ) as NOM_MONEDA,vta_cliente.CT_DIASCREDITO\n"
              + ",1 as tasa\n"
              + ",if(FAC_DIASCREDITO = 0,if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(FAC_FECHA,'%Y%m%d'), INTERVAL FAC_DIASCREDITO DAY)) > 0,'VENCIDO','CONTADO'),if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(FAC_FECHA,'%Y%m%d'), INTERVAL FAC_DIASCREDITO DAY)) > 0, 'VENCIDO', 'VIGENTE')) AS ESTATUS \n"
              + ",vta_empresas.EMP_RAZONSOCIAL as EMPRESA\n"
              + ",(\n"
              + "select GROUP_CONCAT(DISTINCT vta_bcos.BC_DESCRIPCION) from  vta_mov_cta_bcos_rela,vta_mov_cte,vta_mov_cta_bcos, vta_bcos where \n"
              + " vta_mov_cte.MC_ID = vta_mov_cta_bcos_rela.MC_ID and \n"
              + " vta_mov_cta_bcos_rela.MCB_ID = vta_mov_cta_bcos.MCB_ID and  \n"
              + " vta_facturas.FAC_ID = vta_mov_cte.FAC_ID and \n"
              + " vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and\n"
              + "  vta_mov_cte.MC_ANULADO = 0 and vta_mov_cte.MC_ABONO> 0 "
              + ") as TBANCO"
              + " FROM vta_facturas,vta_cliente, vta_empresas\n"
              + " where vta_facturas.CT_ID = vta_cliente.CT_ID AND FAC_ANULADA = 0 \n"
              + " AND vta_facturas.EMP_ID = vta_empresas.EMP_ID \n"
              + " AND vta_cliente.CT_ID = $P{Cliente} and vta_facturas.FAC_MONEDA = $P{Moneda}  \n";
      if (intSoloConSaldo == 1) {
         strSql += " AND ("
                 + "(LEFT(vta_facturas.FAC_FECHA,6) <= $P{Periodo} )  "
                 + " OR (select COUNT(vta_mov_cte.FAC_ID) from  vta_mov_cte where \n"
                 + "vta_facturas.FAC_ID = vta_mov_cte.FAC_ID AND \n"
                 + "LEFT(vta_mov_cte.MC_FECHA,6) = $P{Periodo} \n"
                 + "AND LEFT(vta_facturas.FAC_FECHA,6) < $P{Periodo} \n"
                 + "AND MC_ESPAGO = 1 AND MC_ANULADO = 0 ) )";
      } else {
         strSql += " AND ("
                 + "(LEFT(vta_facturas.FAC_FECHA,6) <= $P{Periodo}  ) OR "
                 + "(LEFT(vta_facturas.FAC_FECHA,6) = $P{Periodo} ) "
                 + " OR (select COUNT(vta_mov_cte.FAC_ID) from  vta_mov_cte where \n"
                 + "vta_facturas.FAC_ID = vta_mov_cte.FAC_ID AND \n"
                 + "LEFT(vta_mov_cte.MC_FECHA,6) = $P{Periodo} \n"
                 + "AND LEFT(vta_facturas.FAC_FECHA,6) <= $P{Periodo} \n"
                 + "AND MC_ESPAGO = 1 AND MC_ANULADO = 0 ) )";
      }
      strSql += " order by DATEDIFF(CURDATE(),STR_TO_DATE(FAC_FECHA,'%Y%m%d')) asc,FAC_FOLIO";
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {

            //Analizamos el saldo
            double dblSaldoFac = 0;
            strSql = "SELECT "
                    + "  if(SUM(MC_CARGO - MC_ABONO) is null, 0 , "
                    + " SUM( "
                    + " (MC_CARGO - MC_ABONO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if(if(" + intMoneda + " <> 0," + intMoneda + ",vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1, "
                    + " if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 or (vta_mov_cte.MC_MONEDA = 2 and " + intMoneda + " = 3 ),1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if(" + intMoneda + " <> 0,1,vta_cliente.MON_ID),4,MC_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_cliente,vta_mov_cte WHERE vta_mov_cte.CT_ID=vta_cliente.CT_ID and vta_cliente.CT_ID = " + intIdCliente
                    + " AND LEFT(vta_mov_cte.MC_FECHA,6) <=" + strPeriodo + " AND (FAC_ID = " + rs.getInt("FAC_ID") + " ) AND MC_ANULADO = 0 ";
            ResultSet rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblSaldoFac = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();
            //Filtro para solo con saldo
            boolean bolPasa = true;
            if (intSoloConSaldo == 1) {
               if (dblSaldoFac <= 1) {
                  bolPasa = false;
               }
            }
            //Solo si pasa validaciones para mostrarse
            if (bolPasa) {
               //Validamos si se genera el xml o no
               if (!this.bolImpreso) {
                  strXML.append("<doc");
                  strXML.append(" id=\"").append(rs.getInt("FAC_ID")).append("\" ");
                  strXML.append(" esPago=\"0\" ");
                  strXML.append(" tipo=\"").append("FACTURA").append("\" ");
                  strXML.append(" folio=\"").append(rs.getString("FAC_FOLIO_C")).append("\" ");
                  strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
                  strXML.append(" vencimiento=\"").append(rs.getString("VENCIMIENTO")).append("\" ");
                  strXML.append(" cargos=\"").append(rs.getDouble("FAC_TOTAL")).append("\" ");
                  strXML.append(" abonos=\"").append(rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_SALDO")).append("\" ");
                  strXML.append(" saldo=\"").append(dblSaldoFac).append("\" ");
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
                  edo.setTipoDocumento("FACTURA");
                  edo.setFolio(rs.getString("FAC_FOLIO_C"));
                  edo.setFecha(rs.getString("FECHA"));
                  edo.setVencimiento(rs.getString("VENCIMIENTO"));
                  edo.setDblCargos(rs.getDouble("FAC_TOTAL"));
                  edo.setDblAbonos(rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_SALDO"));
                  edo.setDblSaldo(dblSaldoFac);
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
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tickets">
      strSql = "SELECT TKT_ID, DATE_FORMAT(STR_TO_DATE(TKT_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,vta_cliente.CT_ID,TKT_FOLIO,"
              + "TKT_RAZONSOCIAL,TKT_TOTAL,TKT_SALDO,TKT_IMPORTE,TKT_IMPUESTO1,TKT_RAZONSOCIAL, TKT_DIASCREDITO,\n"
              + " DATEDIFF(CURDATE(),DATE_ADD(STR_TO_DATE(TKT_FECHA,'%Y%m%d'), INTERVAL TKT_DIASCREDITO DAY)) as dias   \n"
              + ",DATE_FORMAT(DATE_ADD(STR_TO_DATE(TKT_FECHA,'%Y%m%d'), INTERVAL TKT_DIASCREDITO DAY),'%d/%m/%Y') AS VENCIMIENTO\n"
              + ",TKT_MONEDA,(select MON_DESCRIPCION from vta_monedas where MON_ID=TKT_MONEDA  ) as NOM_MONEDA,vta_cliente.CT_DIASCREDITO\n"
              + ",1 as tasa\n"
              + ",if(TKT_DIASCREDITO = 0,if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(TKT_FECHA,'%Y%m%d'), INTERVAL TKT_DIASCREDITO DAY)) > 0,'VENCIDO','CONTADO'),if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(TKT_FECHA,'%Y%m%d'), INTERVAL TKT_DIASCREDITO DAY)) > 0, 'VENCIDO', 'VIGENTE')) AS ESTATUS \n"
              + ",vta_empresas.EMP_RAZONSOCIAL as EMPRESA\n"
              + ",(\n"
              + "select GROUP_CONCAT(DISTINCT vta_bcos.BC_DESCRIPCION) from  vta_mov_cta_bcos_rela,vta_mov_cte,vta_mov_cta_bcos, vta_bcos where \n"
              + " vta_mov_cte.MC_ID = vta_mov_cta_bcos_rela.MC_ID and \n"
              + " vta_mov_cta_bcos_rela.MCB_ID = vta_mov_cta_bcos.MCB_ID and  \n"
              + " vta_tickets.TKT_ID = vta_mov_cte.TKT_ID and \n"
              + " vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and\n"
              + "  vta_mov_cte.MC_ANULADO = 0 and vta_mov_cte.MC_ABONO> 0 "
              + ") as TBANCO"
              + " FROM vta_tickets,vta_cliente, vta_empresas\n"
              + " where vta_tickets.CT_ID = vta_cliente.CT_ID AND TKT_ANULADA = 0 \n"
              + " AND vta_tickets.EMP_ID = vta_empresas.EMP_ID \n"
              + " AND vta_cliente.CT_ID = $P{Cliente} and vta_tickets.TKT_MONEDA = $P{Moneda}  \n";
      if (intSoloConSaldo == 1) {
         strSql += " AND ("
                 + "(LEFT(vta_tickets.TKT_FECHA,6) <= $P{Periodo} )  "
                 + " OR (select COUNT(vta_mov_cte.TKT_ID) from  vta_mov_cte where \n"
                 + "vta_tickets.TKT_ID = vta_mov_cte.TKT_ID AND \n"
                 + "LEFT(vta_mov_cte.MC_FECHA,6) = $P{Periodo} \n"
                 + "AND LEFT(vta_tickets.TKT_FECHA,6) < $P{Periodo} \n"
                 + "AND MC_ESPAGO = 1 AND MC_ANULADO = 0 ) )";
      } else {
         strSql += " AND ("
                 + "(LEFT(vta_tickets.TKT_FECHA,6) <= $P{Periodo}  ) OR "
                 + "(LEFT(vta_tickets.TKT_FECHA,6) = $P{Periodo} ) "
                 + " OR (select COUNT(vta_mov_cte.TKT_ID) from  vta_mov_cte where \n"
                 + "vta_tickets.TKT_ID = vta_mov_cte.TKT_ID AND \n"
                 + "LEFT(vta_mov_cte.MC_FECHA,6) = $P{Periodo} \n"
                 + "AND LEFT(vta_tickets.TKT_FECHA,6) <= $P{Periodo} \n"
                 + "AND MC_ESPAGO = 1 AND MC_ANULADO = 0 ) )";
      }
      strSql += " order by DATEDIFF(CURDATE(),STR_TO_DATE(TKT_FECHA,'%Y%m%d')) asc,TKT_FOLIO";
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {

            //Analizamos el saldo
            double dblSaldoFac = 0;
            strSql = "SELECT "
                    + "  if(SUM(MC_CARGO - MC_ABONO) is null, 0 , "
                    + " SUM( "
                    + " (MC_CARGO - MC_ABONO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if(if(" + intMoneda + " <> 0," + intMoneda + ",vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1, "
                    + " if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 or (vta_mov_cte.MC_MONEDA = 2 and " + intMoneda + " = 3 ),1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if(" + intMoneda + " <> 0,1,vta_cliente.MON_ID),4,MC_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_cliente,vta_mov_cte WHERE vta_mov_cte.CT_ID=vta_cliente.CT_ID and vta_cliente.CT_ID = " + intIdCliente
                    + " AND LEFT(vta_mov_cte.MC_FECHA,6) <=" + strPeriodo + " AND (TKT_ID = " + rs.getInt("TKT_ID") + " ) AND MC_ANULADO = 0 ";
            ResultSet rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblSaldoFac = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();
            //Filtro para solo con saldo
            boolean bolPasa = true;
            if (intSoloConSaldo == 1) {
               if (dblSaldoFac <= 1) {
                  bolPasa = false;
               }
            }
            //Solo si pasa validaciones para mostrarse
            if (bolPasa) {
               //Validamos si se genera el xml o no
               if (!this.bolImpreso) {
                  strXML.append("<doc");
                  strXML.append(" id=\"").append(rs.getInt("TKT_ID")).append("\" ");
                  strXML.append(" esPago=\"0\" ");
                  strXML.append(" tipo=\"").append("TICKET").append("\" ");
                  strXML.append(" folio=\"").append(rs.getString("TKT_FOLIO")).append("\" ");
                  strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
                  strXML.append(" vencimiento=\"").append(rs.getString("VENCIMIENTO")).append("\" ");
                  strXML.append(" cargos=\"").append(rs.getDouble("TKT_TOTAL")).append("\" ");
                  strXML.append(" abonos=\"").append(rs.getDouble("TKT_TOTAL") - rs.getDouble("TKT_SALDO")).append("\" ");
                  strXML.append(" saldo=\"").append(dblSaldoFac).append("\" ");
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
                  edo.setTipoDocumento("TICKET");
                  edo.setFolio(rs.getString("TKT_FOLIO_C"));
                  edo.setFecha(rs.getString("FECHA"));
                  edo.setVencimiento(rs.getString("VENCIMIENTO"));
                  edo.setDblCargos(rs.getDouble("TKT_TOTAL"));
                  edo.setDblAbonos(rs.getDouble("TKT_TOTAL") - rs.getDouble("TKT_SALDO"));
                  edo.setDblSaldo(dblSaldoFac);
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
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Pagos">
      //Obtenemos los pagos en el periodo
      strSql = "SELECT MC_ID,MCM_ID, DATE_FORMAT(STR_TO_DATE(MC_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,vta_cliente.CT_ID,MC_FOLIO,"
              + " MC_ABONO,MC_SALDO_ANTICIPO,MC_ANTICIPO,\n"
              + "MC_MONEDA,(select MON_DESCRIPCION from vta_monedas where MON_ID=MC_MONEDA  ) as NOM_MONEDA,vta_cliente.CT_DIASCREDITO\n"
              //              + ",if(FAC_DIASCREDITO = 0,if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(FAC_FECHA,'%Y%m%d'), INTERVAL FAC_DIASCREDITO DAY)) > 0,'VENCIDO','CONTADO'),if(DATEDIFF(CURDATE(), DATE_ADD(STR_TO_DATE(FAC_FECHA,'%Y%m%d'), INTERVAL FAC_DIASCREDITO DAY)) > 0, 'VENCIDO', 'VIGENTE')) AS ESTATUS \n"
              + ",vta_empresas.EMP_RAZONSOCIAL as EMPRESA\n"
              + ",(\n"
              + "select GROUP_CONCAT(DISTINCT vta_bcos.BC_DESCRIPCION) from  vta_mov_cta_bcos_rela,vta_mov_cta_bcos, vta_bcos where \n"
              + " vta_mov_cte.MC_ID = vta_mov_cta_bcos_rela.MC_ID and \n"
              + " vta_mov_cta_bcos_rela.MCB_ID = vta_mov_cta_bcos.MCB_ID and  \n"
              + " vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID and\n"
              + " vta_mov_cte.MC_ANULADO = 0 and vta_mov_cte.MC_ABONO> 0 "
              + ") as TBANCO,MC_ESPAGO,NC_ID"
              + ",(select NC_FOLIO FROM vta_ncredito WHERE vta_ncredito.NC_ID = vta_mov_cte.NC_ID) as NC_FOLIO"
              + " FROM vta_mov_cte,vta_cliente, vta_empresas\n"
              + " where vta_mov_cte.CT_ID = vta_cliente.CT_ID AND MC_ANULADO = 0 AND MC_ABONO > 0\n"
              + " AND vta_mov_cte.EMP_ID = vta_empresas.EMP_ID \n"
              + " AND vta_cliente.CT_ID = $P{Cliente} and vta_mov_cte.MC_MONEDA = $P{Moneda}  \n";
      strSql += " AND ("
              + "(LEFT(vta_mov_cte.MC_FECHA,6) = $P{Periodo} ) "
              + " or (MC_ANTICIPO = 1 and MC_SALDO_ANTICIPO> 0.9 AND LEFT(vta_mov_cte.MC_FECHA,6) <= $P{Periodo}) "//Anadimos la opcion de mostrar anticipos no aplicados
              + " ) ";
      strSql += " order by DATEDIFF(CURDATE(),STR_TO_DATE(MC_FECHA,'%Y%m%d')) asc,MC_FOLIO";
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
      strSql = strSql.replace("$P{Periodo}", strPeriodo);
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {

            //Analisis del saldo
            double dblSaldoAnticipo = 0;
            strSql = "SELECT "
                    + "  if(SUM(MC_ABONO) is null, 0 , "
                    + " SUM( "
                    + " (MC_ABONO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if(if(" + intMoneda + " <> 0," + intMoneda + ",vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1, "
                    + " if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 or (vta_mov_cte.MC_MONEDA = 2 and " + intMoneda + " = 3 ),1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if(" + intMoneda + " <> 0," + intMoneda + ",vta_cliente.MON_ID),4,MC_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_cliente,vta_mov_cte WHERE vta_mov_cte.CT_ID=vta_cliente.CT_ID and vta_cliente.CT_ID = " + intIdCliente
                    + " AND LEFT(vta_mov_cte.MC_FECHA,6) <=" + strPeriodo + " AND (MC_ANTI_ID = " + rs.getInt("MC_ID") + " ) AND MC_ANULADO = 0  and vta_mov_cte.MC_MONEDA =  " + intMoneda + " AND MC_ESPAGO= 1";
            ResultSet rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblSaldoAnticipo = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();
            //Cargo del anticipo
            double dblCargoAnticipo = 0;
            strSql = "SELECT "
                    + "  if(SUM(MC_CARGO) is null, 0 , "
                    + " SUM( "
                    + " (MC_CARGO) *  "
                    + " /*obtenemos la paridad*/ "
                    + " if(if(" + intMoneda + " <> 0," + intMoneda + ",vta_cliente.MON_ID) = vta_mov_cte.MC_MONEDA , 1, "
                    + " if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,if(vta_mov_cte.MC_MONEDA = 1 ,1/MC_TASAPESO,MC_TASAPESO),GetParidadAuto(vta_mov_cte.MC_MONEDA,if(" + intMoneda + " <> 0," + intMoneda + ",vta_cliente.MON_ID),4,MC_FECHA) ) "
                    + " ) "
                    + " /*obtenemos la paridad*/ "
                    + " ) "
                    + " ) AS SALDO_INICIAL "
                    + " FROM vta_cliente,vta_mov_cte WHERE vta_mov_cte.CT_ID=vta_cliente.CT_ID and vta_cliente.CT_ID = " + intIdCliente
                    + " AND MC_FECHA <='" + strPeriodo + "' AND (MC_ANTI_ID = " + rs.getInt("MC_ID") + " ) "
                    + " AND ( MC_USA_ANTICIPO = 0 AND MC_CARGO>0) "
                    + " AND MC_ANULADO = 0  and vta_mov_cte.MC_MONEDA =  " + intMoneda + " ";
            rs6 = oConn.runQuery(strSql, true);
            while (rs6.next()) {
               dblCargoAnticipo = rs6.getDouble("SALDO_INICIAL");
            }
            rs6.close();

            dblSaldoAnticipo = rs.getDouble("MC_ABONO") - dblSaldoAnticipo - dblCargoAnticipo;
            boolean bolMuestra = true;

            //Filtro para solo con saldo
            if (intSoloConSaldo == 1) {
               if (dblSaldoAnticipo <= 1) {
                  bolMuestra = false;
               }
            }
            //Solo si pasa validaciones para mostrarse
            if (bolMuestra) {

               //Validamos si se genera el xml o no
               if (!this.bolImpreso) {
                  strXML.append("<doc");
                  //Evaluamos el tipo de registro
                  if (rs.getInt("MC_ESPAGO") == 1) {
                     strXML.append(" id=\"").append(rs.getInt("MC_ID")).append("\" ");
                     strXML.append(" idMasivo=\"").append(rs.getInt("MCM_ID")).append("\" ");
                     strXML.append(" esPago=\"1\" ");
                     strXML.append(" tipo=\"").append("COBRO").append("\" ");
                     strXML.append(" folio=\"").append(rs.getString("MC_FOLIO")).append("\" ");
                  } else {
                     if (rs.getInt("NC_ID") != 0) {
                        strXML.append(" id=\"").append(rs.getInt("NC_ID")).append("\" ");
                        strXML.append(" tipo=\"").append("NCREDITO").append("\" ");
                        strXML.append(" folio=\"").append(rs.getString("NC_FOLIO_C")).append("\" ");
                     } else {
                        strXML.append(" id=\"").append(rs.getInt("MC_ID")).append("\" ");
                        strXML.append(" tipo=\"").append("OTRO").append("\" ");
                        strXML.append(" folio=\"").append(rs.getString("MC_FOLIO")).append("\" ");
                     }
                     strXML.append(" esPago=\"0\" ");
                  }

                  strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
                  strXML.append(" vencimiento=\"").append("").append("\" ");
                  strXML.append(" cargos=\"").append(0).append("\" ");
                  strXML.append(" abonos=\"").append(rs.getDouble("MC_ABONO")).append("\" ");
                  //Anadimos la opcion de mostrar anticipos no aplicados
                  if (rs.getInt("MC_ANTICIPO") == 1) {
                     strXML.append(" saldo=\"").append(dblSaldoAnticipo).append("\" ");
                  } else {
                     strXML.append(" saldo=\"").append(0).append("\" ");
                  }
                  strXML.append(" anticipo=\"").append(rs.getInt("MC_ANTICIPO")).append("\" ");
                  strXML.append(" estatus=\"").append("Saldado").append("\" ");

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
                  if (rs.getInt("MC_ESPAGO") == 1) {
                     edo.setTipoDocumento("COBRO");
                     edo.setFolio(rs.getString("MC_FOLIO"));
                  } else {
                     if (rs.getInt("NC_ID") != 0) {
                        edo.setTipoDocumento("NCREDITO");
                        edo.setFolio(rs.getString("NC_FOLIO_C"));
                     } else {
                        edo.setTipoDocumento("OTRO");
                        edo.setFolio(rs.getString("MC_FOLIO"));
                     }
                  }

                  edo.setFecha(rs.getString("FECHA"));
                  edo.setVencimiento("");
                  edo.setDblCargos(0.0);
                  edo.setDblAbonos(rs.getDouble("MC_ABONO"));

                  //Anadimos la opcion de mostrar anticipos no aplicados
                  if (rs.getInt("MC_ANTICIPO") == 1) {
                     edo.setDblSaldo(dblSaldoAnticipo);
                  } else {
                     edo.setDblSaldo(0);
                  }
                  //strXML.append(" anticipo=\"").append(rs.getInt("MC_ANTICIPO")).append("\" ");
                  edo.setEstatus("Saldado");
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
      // </editor-fold>
      if (!this.bolImpreso) {
         strXML.append("</docs>");
      }

      return strXML.toString();
   }

   /**
    * Obtiene el historial de movimientos de una factura
    *
    * @param intIdCliente Es el id del cliente
    * @param intMoneda Es el id del moneda
    * @param intFactura Es el id de la factura
    * @return Regresa el xml con la informaci��n
    */
   public String getHistorialFactura(int intIdCliente, int intMoneda, int intFactura) {
      String banco;
      ResultSet rBanco;
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<docs>");
      String strSql = "SELECT *, DATE_FORMAT(STR_TO_DATE(MC_FECHA,'%Y%m%d'),'%d/%m/%Y') AS FECHA,\n"
              + "(SELECT FAC_FOLIO FROM vta_facturas where vta_facturas.FAC_ID =vta_mov_cte.FAC_ID ) AS FOLIO_FAC,\n"
              + "(SELECT NC_FOLIO FROM vta_ncredito where vta_ncredito.NC_ID =vta_mov_cte.NC_ID ) AS FOLIO_NC,\n"
              + "if(MC_ESPAGO = 1, 'PAGO',if(FAC_ID <> 0,'FACTURA',if(NC_ID <> 0,'N.CREDITO',if(TKT_ID <> 0,'TICKET','---')))) AS CONCEPTO,\n"
              + "(SELECT TKT_FOLIO FROM vta_tickets where vta_tickets.TKT_ID =vta_mov_cte.TKT_ID )AS FOLIO_TKT,\n"
              + "(SELECT MON_DESCRIPCION FROM vta_monedas where vta_monedas.MON_ID =vta_mov_cte.MC_MONEDA )AS NOM_MONEDA,\n"
              + "( CT_RAZONSOCIAL ) as CLIENTE,\n"
              + "(EMP_RAZONSOCIAL ) as EMPRESA,\n"
              + "if(if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID)  = vta_mov_cte.MC_MONEDA , 1,\n"
              + "if(MC_TASAPESO <> 1 AND MC_TASAPESO <> 0 ,MC_TASAPESO,getParidad(vta_mov_cte.MC_MONEDA,if($P{Moneda} <> 0,$P{Moneda},vta_cliente.MON_ID),4,MC_FECHA) )\n"
              + "\n"
              + ") as tasa\n"
              + ",vta_empresas.EMP_PATHIMG as LOGO\n"
              + "FROM vta_mov_cte,vta_cliente,vta_empresas WHERE \n"
              + "vta_cliente.EMP_ID=vta_empresas.EMP_ID AND \n"
              + "vta_mov_cte.CT_ID = vta_cliente.CT_ID and vta_cliente.CT_ID = $P{Cliente}\n"
              + " AND vta_mov_cte.FAC_ID = $P{FacturaId} AND vta_mov_cte.MC_ANULADO = 0 \n"
              + "ORDER BY MC_FECHA;";
      strSql = strSql.replace("$P{Cliente}", intIdCliente + "");
      strSql = strSql.replace("$P{FacturaId}", intFactura + "");
      strSql = strSql.replace("$P{Moneda}", intMoneda + "");
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<doc");
            strXML.append(" fecha=\"").append(rs.getString("FECHA")).append("\" ");
            strXML.append(" folio=\"").append(rs.getString("FOLIO_FAC")).append("\" ");
            strXML.append(" concepto=\"").append(rs.getString("CONCEPTO")).append("\" ");
            strXML.append(" cargos=\"").append(rs.getDouble("MC_CARGO")).append("\" ");
            strXML.append(" abonos=\"").append(rs.getDouble("MC_ABONO")).append("\" ");
            strXML.append(" paridad=\"").append(rs.getDouble("tasa")).append("\" ");
            strXML.append(" moneda=\"").append(rs.getString("MC_MONEDA")).append("\" ");
            strXML.append(" id=\"").append(rs.getString("MC_ID")).append("\" ");
            strXML.append(" id_Masivo=\"").append(rs.getString("MCM_ID")).append("\" ");
            strXML.append(" id_Fac=\"").append(rs.getString("FAC_ID")).append("\" ");
            strXML.append(" mc_folio=\"").append(rs.getString("MC_FOLIO")).append("\" ");
            strXML.append(" nc_folio=\"").append(rs.getString("FOLIO_NC")).append("\" ");
            if (rs.getString("MCM_ID").equals("0")) {
               banco = "SELECT vta_bcos.BC_DESCRIPCION  FROM vta_mov_cta_bcos , vta_bcos WHERE vta_mov_cta_bcos.BC_ID = vta_bcos.BC_ID AND MC_ID = '" + rs.getString("MC_ID") + "'";
               rBanco = this.oConn.runQuery(banco, true);
               if (rBanco.next()) {
                  strXML.append(" banco=\"").append(rBanco.getString("BC_DESCRIPCION")).append("\" ");
               } else {
                  strXML.append(" banco=\"").append(" ").append("\" ");
               }
            } else {
               banco = "select c.BC_DESCRIPCION  from vta_mov_cta_bcos a, vta_mov_cta_bcos_rela b , vta_bcos c where a.MCB_ID = b.MCB_ID and a.BC_ID = c.BC_ID  and b.MC_ID ='" + rs.getString("MC_ID") + "'";
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
    * @param intIdCliente Es el id del cliente
    * @param intMoneda Es el id de la moneda
    * @param strPeriodo Es el periodo
    * @param intSoloConSaldo Indica si es solo con saldo
    * @param varSesiones Es la sesion
    * @param byteArrayOutputStream Es el stream de salida
    */
   public void getReportPrint(int intIdCliente, int intMoneda, String strPeriodo,
           int intSoloConSaldo, VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream) {
      this.bolImpreso = true;
      lstData = new ArrayList<EdoMovCte>();
      //Hacemos los calculos para el reporte
      log.debug("Comenzamos los calculos");
      //Datos generales
      getInfoGral(intIdCliente);
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
      getInfoSaldos(intIdCliente, intMoneda, strPeriodo);
      //Saldos a detalle
      getSaldosPeriodos(intIdCliente, intMoneda, strPeriodo, intSoloConSaldo);
      //Generamos el pdf
      InputStream reportStream = null;
      try {
         log.debug("Comenzamos a generar el reporte jrxml");
         //Parametros
         Map parametersMap = new HashMap();
         log.debug("Parametros");
         parametersMap.put("NumCliente", this.strIdCte);
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
