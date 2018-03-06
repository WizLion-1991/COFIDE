/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author N4v1d4d3s
 */
public class RepoVentas {

   private String strFechaInicial;
   private String strFechaFinal;
   private String strCodigo;
   private int intFamilia;
   private int intSeleccion;
   private int intSucursal;
   private int intEmpresa;
   private Conexion oConn;
   private ArrayList<DetalleInventario> detalle;

   public ArrayList<DetalleInventario> getDetalle() {
      return detalle;
   }

   public int getIntEmpresa() {
      return intEmpresa;
   }

   public void setIntEmpresa(int intEmpresa) {
      this.intEmpresa = intEmpresa;
   }

   public void setDetalle(ArrayList<DetalleInventario> detalle) {
      this.detalle = detalle;
   }

   public String getStrFechaInicial() {
      return strFechaInicial;
   }

   public void setStrFechaInicial(String strFechaInicial) {
      this.strFechaInicial = strFechaInicial;
   }

   public String getStrFechaFinal() {
      return strFechaFinal;
   }

   public void setStrFechaFinal(String strFechaFinal) {
      this.strFechaFinal = strFechaFinal;
   }

   public String getStrCodigo() {
      return strCodigo;
   }

   public void setStrCodigo(String strCodigo) {
      this.strCodigo = strCodigo;
   }

   public int getIntFamilia() {
      return intFamilia;
   }

   public void setIntFamilia(int intFamilia) {
      this.intFamilia = intFamilia;
   }

   public int getIntSeleccion() {
      return intSeleccion;
   }

   public void setIntSeleccion(int intSeleccion) {
      this.intSeleccion = intSeleccion;
   }

   public int getIntSucursal() {
      return intSucursal;
   }

   public void setIntSucursal(int intSucursal) {
      this.intSucursal = intSucursal;
   }

   public RepoVentas(String strFechaInicial, String strFechaFinal, String strCodigo, int intFamilia, int intSeleccion, int intSucursal, int intEmpresa, Conexion oConn) {
      this.strFechaInicial = strFechaInicial;
      this.strFechaFinal = strFechaFinal;
      this.strCodigo = strCodigo;
      this.intFamilia = intFamilia;
      this.intSeleccion = intSeleccion;
      this.intSucursal = intSucursal;
      this.intEmpresa = intEmpresa;
      this.oConn = oConn;
      detalle = new ArrayList<DetalleInventario>();
   }

   public void CalcularReporte() {
      String strSql = "";
      String strFiltroSucursal = "";
      String strFiltroFamilia = "";
      String strFiltroCodigo = "";
      if (this.intSucursal != 0) {
         strFiltroSucursal += " AND  f.SC_ID  ='" + this.intSucursal + "' ";
      }
      if (this.intFamilia != 0) {
         strFiltroFamilia += " AND  p.PR_CATEGORIA1 ='" + this.intFamilia + "' ";
      }
      if (this.strCodigo != "") {
         strFiltroCodigo += " AND  p.PR_CODIGO ='" + this.strCodigo + "' ";
      }
      if (intSeleccion == 1) { //es agrupar por familia

         strSql = " Select "
                 + " (SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = p.PR_CATEGORIA1) AS FAMILIA "
                 + " ,(select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = p.PR_UNIDADMEDIDA) as unidad "
                 + " ,d.FACD_PRECIO as precio_venta,p.PR_COSTOPROM as costo_unitario "
                 + " ,sum(d.FACD_CANTIDAD) as cantidad_vendida"
                 + " ,sum(d.FACD_CANTIDAD * p.PR_COSTOPROM) as costo_total"
                 + " ,count(f.FAC_ID) as venta_total"
                 + " from vta_facturas f, vta_facturasdeta d, vta_producto p where"
                 + " f.FAC_ID = d.FAC_ID AND d.PR_ID = p.PR_ID"
                 + " and f.FAC_ANULADA = 0"
                 + " AND f.FAC_FECHA>='" + strFechaInicial + "' AND f.FAC_FECHA<='" + strFechaFinal + "' "
                 + " AND f.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " (SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = p.PR_CATEGORIA1) "
                 + ",(select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = p.PR_UNIDADMEDIDA) "
                 + ",d.FACD_PRECIO ,p.PR_COSTOPROM ;";
      } else if (intSeleccion == 2) {//selecciono agrupar por almacen

         strSql = "Select s.SC_NOMBRE "
                 + ",(select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = p.PR_UNIDADMEDIDA) as unidad "
                 + ",d.FACD_PRECIO as precio_venta,p.PR_COSTOPROM as costo_unitario "
                 + ",sum(d.FACD_CANTIDAD) as cantidad_vendida "
                 + ",sum(d.FACD_CANTIDAD * p.PR_COSTOPROM) as costo_total "
                 + ",count(f.FAC_ID) as venta_total "
                 + "from vta_sucursal s,vta_facturas f, vta_facturasdeta d, vta_producto p where "
                 + "s.SC_ID = f.SC_ID and "
                 + "f.FAC_ID = d.FAC_ID AND d.PR_ID = p.PR_ID "
                 + "  and f.FAC_ANULADA = 0"
                 + " AND f.FAC_FECHA>='" + strFechaInicial + "' AND f.FAC_FECHA<='" + strFechaFinal + "' "
                 + " AND f.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " s.SC_NOMBRE "
                 + " ,(select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = p.PR_UNIDADMEDIDA) "
                 + ",d.FACD_PRECIO ,p.PR_COSTOPROM ; ";

      } else { //mostrar detalle si seleccion es igual a 3

         strSql = "Select p.PR_CODIGO as codigo,p.PR_DESCRIPCION as descripcion "
                 + " ,(SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = p.PR_CATEGORIA1) AS FAMILIA "
                 + ",(select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = p.PR_UNIDADMEDIDA) as unidad "
                 + " ,d.FACD_PRECIO as precio_venta,p.PR_COSTOPROM as costo_unitario "
                 + ",sum(d.FACD_CANTIDAD) as cantidad_vendida "
                 + ",sum(d.FACD_CANTIDAD * p.PR_COSTOPROM) as costo_total "
                 + " ,count(f.FAC_ID) as venta_total"
                 + "  from vta_facturas f, vta_facturasdeta d, vta_producto p where "
                 + " f.FAC_ID = d.FAC_ID AND d.PR_ID = p.PR_ID "
                 + " and f.FAC_ANULADA = 0 "
                 + " AND f.FAC_FECHA>='" + strFechaInicial + "' AND f.FAC_FECHA<='" + strFechaFinal + "' "
                 + " AND f.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " p.PR_CODIGO ,p.PR_DESCRIPCION "
                 + ",(SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = p.PR_CATEGORIA1) "
                 + ",(select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = p.PR_UNIDADMEDIDA) "
                 + ",d.FACD_PRECIO ,p.PR_COSTOPROM ;";
      }

      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            DetalleInventario deta = new DetalleInventario();
            //  deta.setCodigo(rs.getString("ddddd"));
            if (intSeleccion == 1) {
               deta.setStrFamilia(rs.getString("FAMILIA"));

            } else if (intSeleccion == 2) {

               deta.setStrSucursal(rs.getString("SC_NOMBRE"));
            } else {
               deta.setStrCodigo(rs.getString("codigo"));
               deta.setStrDescripcion(rs.getString("descripcion"));
               deta.setStrFamilia(rs.getString("FAMILIA"));

            }
            deta.setStrUnidad(rs.getString("unidad"));
            deta.setDbCantidadVendida(rs.getDouble("cantidad_vendida"));
            deta.setDbCostoUnitario(rs.getDouble("costo_unitario"));
            deta.setDbPrecioVenta(rs.getDouble("precio_venta"));
            deta.setDbCostoTotal(rs.getDouble("costo_total"));
            deta.setDblVentaTotal(rs.getInt("venta_total"));

            //
            detalle.add(deta);

         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }

   }
   public void CalcularReporteInventario() {
      String strSql="";
      String strFiltroSucursal = "";
      String strFiltroFamilia = "";
      String strFiltroCodigo = "";
      if (this.intSucursal != 0) {
         strFiltroSucursal += " AND  vta_movprod.SC_ID ='" + this.intSucursal + "' ";
      }
      if (this.intFamilia != 0) {
         strFiltroFamilia += " AND  vta_producto.PR_CATEGORIA1 ='" + this.intFamilia + "' ";
      }
      if (this.strCodigo != "") {
         strFiltroCodigo += " AND  vta_producto.PR_CODIGO ='" + this.strCodigo + "' ";
      }
      if (intSeleccion == 1) { //es agrupar por familia

         strSql = "select"
                 +" (SELECT vta_prodprecios.PP_PRECIO"
                 +" FROM   vta_prodprecios , vta_lprecios" 
                 +" WHERE vta_producto.PR_ID = vta_prodprecios.PR_ID and  vta_prodprecios.LP_ID = vta_lprecios.LP_ID" 
                 +" AND LP_DESCRIPCION = '" + strFechaInicial.substring(0, 6) + "' limit 0,1) AS tprecio,"
                 + " (SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = vta_producto.PR_CATEGORIA1) AS FAMILIA, "
                 + " (select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = PR_UNIDADMEDIDA) as unidad, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS - vta_movproddeta.MPD_SALIDAS,0)) as inicial, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS,0)) as entradas, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_SALIDAS,0)) as salidas, "
                 + " sum(vta_movproddeta.MPD_ENTRADAS- vta_movproddeta.MPD_SALIDAS) as final, "
                 + " count(vta_movprod.MP_ID) as num_mov,"
                 + " vta_movproddeta.MPD_COSTO as costo_unitario"
                 + " from vta_movprod, vta_movproddeta, vta_producto where "
                 + " vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_producto.PR_ID = vta_movproddeta.PR_ID AND "
                 + " vta_movprod.MP_ANULADO = 0 "
                 + " AND vta_movprod.MP_FECHA<='" + strFechaFinal + "' AND vta_movprod.TMP_ID = 1 "
                 + " AND vta_producto.PR_INVENTARIO = 1 AND vta_producto.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " vta_producto.PR_CATEGORIA1,PR_UNIDADMEDIDA;";

      } else if (intSeleccion == 2) {//selecciono agrupar por almacen

         strSql = "select vta_sucursal.SC_NOMBRE, "
                 +" (SELECT vta_prodprecios.PP_PRECIO"
                 +" FROM   vta_prodprecios , vta_lprecios" 
                 +" WHERE vta_producto.PR_ID = vta_prodprecios.PR_ID and  vta_prodprecios.LP_ID = vta_lprecios.LP_ID" 
                 +" AND LP_DESCRIPCION = '" + strFechaInicial.substring(0, 6) + "' limit 0,1) AS tprecio,"                 
                 + " (select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = PR_UNIDADMEDIDA) as unidad, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS - vta_movproddeta.MPD_SALIDAS,0)) as inicial, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS,0)) as entradas, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_SALIDAS,0)) as salidas, "
                 + " sum(vta_movproddeta.MPD_ENTRADAS- vta_movproddeta.MPD_SALIDAS) as final, "
                 + " count(vta_movprod.MP_ID) as num_mov,"
                 + " vta_movproddeta.MPD_COSTO as costo_unitario"
                 + " from vta_movprod, vta_movproddeta, vta_producto, vta_sucursal where "
                 + " vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_producto.PR_ID = vta_movproddeta.PR_ID AND "
                 + " vta_movprod.MP_ANULADO = 0 "
                 + " AND vta_sucursal.SC_ID = vta_movprod.SC_ID AND vta_movprod.TMP_ID = 1 "
                 + " AND vta_movprod.MP_FECHA<='" + strFechaFinal + "' "
                 + " AND vta_producto.PR_INVENTARIO = 1 AND vta_producto.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " vta_sucursal.SC_NOMBRE,PR_UNIDADMEDIDA; ";

      } else { //mostrar detalle si seleccion es igual a 3
        

         strSql = "select vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION, "
                 +" (SELECT vta_prodprecios.PP_PRECIO"
                 +" FROM   vta_prodprecios , vta_lprecios" 
                 +" WHERE vta_producto.PR_ID = vta_prodprecios.PR_ID and  vta_prodprecios.LP_ID = vta_lprecios.LP_ID" 
                 +" AND LP_DESCRIPCION = '" + strFechaInicial.substring(0, 6) + "' limit 0,1) AS tprecio,"
                 + " (SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = vta_producto.PR_CATEGORIA1) AS FAMILIA, "
                 + " (select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = PR_UNIDADMEDIDA) as unidad, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS - vta_movproddeta.MPD_SALIDAS,0)) as inicial, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS,0)) as entradas, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_SALIDAS,0)) as salidas, "
                 + " sum(vta_movproddeta.MPD_ENTRADAS- vta_movproddeta.MPD_SALIDAS) as final, "
                 + " count(vta_movprod.MP_ID) as num_mov, "
                 + " vta_movproddeta.MPD_COSTO as costo_unitario"
                 + " from vta_movprod, vta_movproddeta, vta_producto where "
                 + " vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_producto.PR_ID = vta_movproddeta.PR_ID AND "
                 + " vta_movprod.MP_ANULADO = 0 "
                 + " AND vta_movprod.MP_FECHA<='" + strFechaFinal + "' AND vta_movprod.TMP_ID = 1 "
                 + " AND vta_producto.PR_INVENTARIO = 1 AND vta_producto.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION, "
                 + " vta_producto.PR_CATEGORIA1,PR_UNIDADMEDIDA;";
         
      }
      
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            DetalleInventario deta = new DetalleInventario();
            //  deta.setCodigo(rs.getString("ddddd"));
            if (intSeleccion == 1) {
               deta.setStrFamilia(rs.getString("FAMILIA"));

            } else if (intSeleccion == 2) {

               deta.setStrSucursal(rs.getString("SC_NOMBRE"));
            } else {
               deta.setStrCodigo(rs.getString("PR_CODIGO"));
               deta.setStrDescripcion(rs.getString("PR_DESCRIPCION"));
               deta.setStrFamilia(rs.getString("FAMILIA"));

            }
            deta.setStrUnidad(rs.getString("unidad"));
            deta.setDbInicial(rs.getDouble("inicial"));
            deta.setDbPrecioVenta(rs.getDouble("tprecio"));
            deta.setDbEntradas(rs.getDouble("entradas"));
            deta.setDbCantidadVendida(rs.getDouble("salidas"));
            deta.setDbFinal(rs.getDouble("final"));
            deta.setIntMovimientos(rs.getInt("num_mov"));
            deta.setDbCostoUnitario(rs.getDouble("costo_unitario"));
            
            deta.setDbCostoTotal(rs.getDouble("costo_unitario")*rs.getDouble("salidas"));
            deta.setDblVentaTotal((int) (rs.getDouble("tprecio")*rs.getDouble("salidas")));

            // System.out.println("hola");
            detalle.add(deta);

         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }

   }

   public String GeneraXml() {
      Iterator<DetalleInventario> it = detalle.iterator();

      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<Reporte_Ventas>");

      while (it.hasNext()) {
         DetalleInventario deta = it.next();
         strXML.append(" <Docs");
         if (intSeleccion == 1) {

            strXML.append(" Descripcion=\"").append(deta.getStrFamilia()).append("\" ");

         } else if (intSeleccion == 2) {

            strXML.append(" Descripcion= \"").append(deta.getStrSucursal()).append("\" ");

         } else {
            UtilXml algo = new UtilXml();

            strXML.append(" Codigo= \"").append(deta.getStrCodigo()).append("\" ");
            String cadena = algo.Sustituye(deta.getStrDescripcion());

            //System.out.println(cadena);
            strXML.append(" Descripcion=\"").append(cadena).append("\" ");
            strXML.append(" Familia= \"").append(deta.getStrFamilia()).append("\" ");
         }

         strXML.append(" Unidad=\"").append(deta.getStrUnidad()).append("\" ");
         strXML.append(" cantidad_vendida=\"").append(deta.getDbCantidadVendida()).append("\" ");
         strXML.append(" costo_unitario=\"").append(deta.getDbCostoUnitario()).append("\" ");
         strXML.append("precio_venta= \"").append(deta.getDbPrecioVenta()).append("\" ");
         strXML.append("costo_total=\"").append(deta.getDbCostoTotal()).append("\" ");
         strXML.append("venta_total=\"").append(deta.getDblVentaTotal()).append("\" ");

         strXML.append("/>");
      }
      strXML.append("</Reporte_Ventas>");
      return strXML.toString();

   }

}
