package com.mx.siweb.erp.especiales.arrendadoramak;

import comSIWeb.Operaciones.Conexion;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Realiza las operaciones de preparacion de pedidos
 *
 * @author ZeusSIWEB
 */
public class PreparacionPedidos {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(PreparacionPedidos.class.getName());
   private Conexion oConn;
   private int intEmpId;
   private int intScId;
   private int intMonId;
   private int intTipo;
   protected String sidx, sord, _search;
   protected int page, rows;

   public String getSidx() {
      return sidx;
   }

   public void setSidx(String sidx) {
      this.sidx = sidx;
   }

   public String getSord() {
      return sord;
   }

   public void setSord(String sord) {
      this.sord = sord;
   }

   public String getSearch() {
      return _search;
   }

   public void setSearch(String _search) {
      this._search = _search;
   }

   public int getPage() {
      return page;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getRows() {
      return rows;
   }

   public void setRows(int rows) {
      this.rows = rows;
   }

   public int getIntMonId() {
      return intMonId;
   }

   public void setIntMonId(int intMonId) {
      this.intMonId = intMonId;
   }

   public int getIntTipo() {
      return intTipo;
   }

   public void setIntTipo(int intTipo) {
      this.intTipo = intTipo;
   }

   public int getIntEmpId() {
      return intEmpId;
   }

   public void setIntEmpId(int intEmpId) {
      this.intEmpId = intEmpId;
   }

   public int getIntScId() {
      return intScId;
   }

   public void setIntScId(int intScId) {
      this.intScId = intScId;
   }

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public PreparacionPedidos(Conexion oConn) {
      this.oConn = oConn;
      this.rows = 1;
      this.sidx = " PD_ID ";
      this.sord = " desc ";
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Genera el listado de pedidos
    *
    * @param intTipo Es el tipo de pedidos
    * @param intEmpId Es la empresa
    * @param intScId Es la bodega
    * @param intMoneda Es la moneda
    * @return Regresa el listado
    */
   public String getListPedidos(int intTipo, int intEmpId, int intScId, int intMoneda) {
      StringBuilder strXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      this.intEmpId = intEmpId;
      this.intScId = intScId;
      // <editor-fold defaultstate="collapsed" desc="PAGINACION">
      int intLimInf = 0;
      int intLimSup = 0;
      double dblCount = contarRegistros();
      long inttotal_pages = 0;
      if (this.rows == 0) {
         this.rows = 1;
      }
      if (dblCount > 0) {
         inttotal_pages = (long) this.round(dblCount / rows, 0);
      } else {
         inttotal_pages = 0;
      }
      /*if for some reasons the requested page is greater than the total
       set the requested page to total page*/
      if (this.page > inttotal_pages) {
         page = (int) inttotal_pages;
      }
      //Calculate the starting position of the rows
      int start = this.rows * this.page - this.rows;
      /*if for some reasons start position is negative set it to 0
       'typical case is that the user type 0 for the requested page*/
      if (start < 0) {
         start = 0;
      }
      // </editor-fold>

      strXml.append("<rows>" + "");
      strXml.append("<page>").append(page).append("</page>");
      strXml.append("<total>").append(inttotal_pages).append("</total>");
      strXml.append("<records>").append((int) dblCount).append("</records>");

      //Generamos el reporte
      hacerReporte(strXml, start, this.rows);
      
      strXml.append("</rows>");

      return strXml.toString();
   }

   public String getListPedidosDeta(int intIdPedido) {
      StringBuilder strXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      return strXml.toString();
   }

   public void hacerReporte(StringBuilder strXml, int intLimiteInf, int intLimiteSup) {
      try {
         CallableStatement cStmt;
         String stmt = "{call sp_getPreparaPedidos("
            + this.intEmpId + ","
            + this.intScId + ","
            + this.intMonId + ","
            + this.intTipo + ","
            + intLimiteInf + ","
            + intLimiteSup + ",'"
            + this.sidx + "','"
            + this.sord + "'"
            + ")};";
         log.debug(stmt);
         cStmt = oConn.getConexion().prepareCall(stmt);
         boolean hadResults = cStmt.execute();
         while (hadResults) {

            log.debug("hubo resultados...");
            ResultSet rs = cStmt.getResultSet();
            while (rs.next()) {
               log.debug("pintamos datos...");
               strXml.append("<row id='").append(rs.getInt("PD_ID")).append("'>");

//               strXml.append("<cell>").append(rs.getInt("EMP_ID")).append("</cell>\n");
//               strXml.append("<cell>").append(rs.getInt("SC_ID")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("EMPRESA")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("BODEGA")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("SUCURSAL")).append("</cell>\n");
//               strXml.append("<cell>").append(rs.getInt("PD_ID")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("PD_FOLIO")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("PD_FECHA_SURTIDO")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("PD_FECHA_ENTREGA")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("PD_FECHA")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getInt("dias")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getInt("TR_ID")).append("</cell>\n");

               if (rs.getString("TRANSPORTISTA") != null) {
                  strXml.append("<cell>").append(rs.getString("TRANSPORTISTA")).append("</cell>\n");
               } else {
                  strXml.append("<cell>").append("").append("</cell>\n");
               }

               strXml.append("<cell>").append(rs.getString("PD_IMPRESO")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("NIVEL_INVENTARIO")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("BAK_ORDER")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("MENSAJE")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("STOCK_INVENTARIO")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("TOTAL_UNIDADES")).append("</cell>\n");

               if (rs.getString("TOTAL_CAJAS") != null) {
                  strXml.append("<cell>").append(rs.getString("TOTAL_CAJAS")).append("</cell>\n");
               } else {
                  strXml.append("<cell>").append(0).append("</cell>\n");
               }
               strXml.append("<cell>").append(rs.getString("USUARIO_CAPTURA")).append("</cell>\n");

               if (rs.getString("VENDEDOR") != null) {
                  strXml.append("<cell>").append(rs.getString("VENDEDOR")).append("</cell>\n");
               } else {
                  strXml.append("<cell>").append(0).append("</cell>\n");
               }
               strXml.append("<cell>").append(rs.getString("ESTATUS_SURTIDO")).append("</cell>\n");
               strXml.append("<cell>").append(rs.getString("REMISIONES_SURTIDO")).append("</cell>\n");
               strXml.append(" </row>\n");
            }
            hadResults = cStmt.getMoreResults();
            rs.close();

         }

      } catch (SQLException ex) {
         log.error(ex);
      }
   }

   public double contarRegistros() {
      double dblCuantos = 0;
      try {
         CallableStatement cStmt;
         String stmt = "{call sp_getPreparaPedidosTot("
            + this.intEmpId + ","
            + this.intScId + ","
            + this.intMonId + ","
            + this.intTipo + ""
            + ")};";
         log.debug(stmt);
         cStmt = oConn.getConexion().prepareCall(stmt);
         boolean hadResults = cStmt.execute();
         while (hadResults) {

            log.debug("hubo resultados...tot");
            ResultSet rs = cStmt.getResultSet();
            while (rs.next()) {
               log.debug("obtenemos datos...");
               dblCuantos = rs.getDouble("tot");

            }
            hadResults = cStmt.getMoreResults();
            rs.close();

         }

      } catch (SQLException ex) {
         log.error(ex);
      }
      return dblCuantos;
   }

   protected double round(double d, int decimalPlace) {
      // see the Javadoc about why we use a String in the constructor
      // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
      BigDecimal bd = new BigDecimal(Double.toString(d));
      bd = bd.setScale(decimalPlace, BigDecimal.ROUND_CEILING);
      return bd.doubleValue();
   }
   // </editor-fold>

}
