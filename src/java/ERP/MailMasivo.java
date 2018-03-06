/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 *
 * @author SIWEB
 */
public class MailMasivo {

   private String sord;
   private long page;
   private int rows;
   private String sidx;
   private String NumFolio;
   Bitacora bitacora = new Bitacora();

   /**
    * Constructor
    */
   public MailMasivo() {
      this.page = 0;
      this.rows = 0;

   }

   /**
    *obtiene el numero de paginas
    * @return regresa el numero de pagina
    */
   public long getPage() {
      return page;
   }

   /**
    * define el numero de paginas
    * @param page contiene el numero de paginas por mostrar
    */
   public void setPage(long page) {
      this.page = page;
   }

   /**
    * obtiene las filas por mostrar
    * @return regresa las filas por mostrar
    */
   public int getRows() {
      return rows;
   }

   /**
    * define las filas por mostrar
    * @param rows contiene la cadena con las filas por mostrar
    */
   public void setRows(int rows) {
      this.rows = rows;
   }

   protected double round(double d, int decimalPlace) {
      // see the Javadoc about why we use a String in the constructor
      // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
      BigDecimal bd = new BigDecimal(Double.toString(d));
      bd = bd.setScale(decimalPlace, BigDecimal.ROUND_CEILING);
      return bd.doubleValue();
   }

   /***
    * 
    * @param oConn Conexion
    * @param idCliente id cliente 
    * @param strEstado estado del cliente
    * @param idSucursal id de la sucursal
    * @param idCat1 id de la categoria 1
    * @param idCat2 id de la categoria 2
    * @param idCat3 id de la categoria 3
    * @param idCat4 id de la categoria 4
    * @param idCat5 id de la categoria 5
    * @return 
    */
   public String generaXML(Conexion oConn, int idCliente, String strEstado, int idSucursal, int idCat1, int idCat2, int idCat3, int idCat4, int idCat5) {


      String strSqlCount = "";
      double dblCount = 0;
      long inttotal_pages = 0;
      String strXML = "";
      //Consultamos la tabla de cliente en base al cliente 
      String strFiltro = "";
      if (idCliente != 0) {
         strFiltro += " AND vta_cliente.CT_ID=" + idCliente;
      }
      if (strEstado != "") {
         strFiltro += " AND CT_ESTADO ='" + strEstado + "'";
      }
      if (idCat1 != 0) {
         strFiltro += " AND CT_CATEGORIA1=" + idCat1;
      }
      if (idCat2 != 0) {
         strFiltro += " AND CT_CATEGORIA2=" + idCat2;
      }
      if (idCat3 != 0) {
         strFiltro += " AND CT_CATEGORIA3=" + idCat3;
      }
      if (idCat4 != 0) {
         strFiltro += " AND CT_CATEGORIA4=" + idCat4;
      }

      String strSql = "select vta_cliente.CT_ID,CT_RAZONSOCIAL,vta_cliente.SC_ID,CT_EMAIL1,CT_ESTADO,CT_CATEGORIA1,CT_CATEGORIA2,CT_CATEGORIA3,CT_CATEGORIA4,CT_CATEGORIA5, vta_sucursal.SC_NOMBRE  "
              + "  from vta_cliente,vta_sucursal where vta_sucursal.SC_ID= "+ idSucursal + " " + strFiltro;

      if (this.rows == 0) {
         this.rows = 1;
      }
      strSqlCount = "Select count(*) as cnt from (" + strSql + ") as tbl";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSqlCount, true);
         while (rs.next()) {
            dblCount = rs.getDouble("cnt");
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         //bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
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
      int start = (int) (this.rows * this.page - this.rows);
      /*if for some reasons start position is negative set it to 0
      'typical case is that the user type 0 for the requested page*/
      if (start < 0) {
         start = 0;
      }
      /*************************PAGINACION***************/
      /*************************ENCABEZADO***************/
      strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
      strXML += "<rows>" + "";
      strXML += "<page>" + page + "</page>";
      strXML += "<total>" + inttotal_pages + "</total>";
      strXML += "<records>" + (int) dblCount + "</records>";

      //ejecutamos el query
      try {
         rs = oConn.runQuery(strSql);
         while (rs.next()) {

            strXML += " <row id='" + rs.getString("CT_ID") + "'>";
            strXML += "<cell>" + rs.getString("CT_ID") + "</cell>";
            strXML += "<cell>" + rs.getString("CT_RAZONSOCIAL") + "</cell>";
            strXML += "<cell>" + rs.getString("CT_ESTADO") + "</cell>";
            strXML += "<cell>" + rs.getString("SC_NOMBRE") + "</cell>";
            strXML += "<cell>" + rs.getString("CT_EMAIL1") + "</cell>";
            strXML += "<cell>" + rs.getInt("CT_CATEGORIA1") + "</cell>";
            strXML += "<cell>" + rs.getInt("CT_CATEGORIA2") + "</cell>";
            strXML += "<cell>" + rs.getInt("CT_CATEGORIA3") + "</cell>";
            strXML += "<cell>" + rs.getInt("CT_CATEGORIA4") + "</cell>";
            strXML += "<cell>" + rs.getInt("CT_CATEGORIA5") + "</cell>";
            strXML += " </row>";


         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      strXML += "</rows>";
      return strXML;
   }
}
