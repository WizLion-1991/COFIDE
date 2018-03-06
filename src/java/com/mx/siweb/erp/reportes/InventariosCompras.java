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
public class InventariosCompras {

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

   public InventariosCompras(String strFechaInicial, String strFechaFinal, String strCodigo, int intFamilia, int intSeleccion, int intSucursal, int intEmpresa, Conexion oConn) {
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
                 + " (SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = vta_producto.PR_CATEGORIA1) AS FAMILIA, "
                 + " (select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = PR_UNIDADMEDIDA) as unidad, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS - vta_movproddeta.MPD_SALIDAS,0)) as inicial, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS,0)) as entradas, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_SALIDAS,0)) as salidas, "
                 + " sum(vta_movproddeta.MPD_ENTRADAS- vta_movproddeta.MPD_SALIDAS) as final, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',(vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO) - (vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO),0)) as inicialc, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',(vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO),0)) as entradasc, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',(vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO),0)) as salidasc, "
                 + " sum((vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO) - (vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO)) as finalc, "
                 + " count(vta_movprod.MP_ID) as num_mov"
                 + " from vta_movprod, vta_movproddeta, vta_producto where "
                 + " vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_producto.PR_ID = vta_movproddeta.PR_ID AND "
                 + " vta_movprod.MP_ANULADO = 0 "
                 + " AND vta_movprod.MP_FECHA<='" + strFechaFinal + "' "
                 + " AND vta_producto.PR_INVENTARIO = 1 AND vta_producto.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " vta_producto.PR_CATEGORIA1,PR_UNIDADMEDIDA;";

      } else if (intSeleccion == 2) {//selecciono agrupar por almacen

         strSql = "select vta_sucursal.SC_NOMBRE, "
                 + " (select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = PR_UNIDADMEDIDA) as unidad, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS - vta_movproddeta.MPD_SALIDAS,0)) as inicial, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS,0)) as entradas, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_SALIDAS,0)) as salidas, "
                 + " sum(vta_movproddeta.MPD_ENTRADAS- vta_movproddeta.MPD_SALIDAS) as final, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',(vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO) - (vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO),0)) as inicialc, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',(vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO),0)) as entradasc, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',(vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO),0)) as salidasc, "
                 + " sum((vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO) - (vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO)) as finalc, "
                 + " count(vta_movprod.MP_ID) as num_mov  "
                 + " from vta_movprod, vta_movproddeta, vta_producto, vta_sucursal where "
                 + " vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_producto.PR_ID = vta_movproddeta.PR_ID AND "
                 + " vta_movprod.MP_ANULADO = 0 "
                 + " AND vta_sucursal.SC_ID = vta_movprod.SC_ID "
                 + " AND vta_movprod.MP_FECHA<='" + strFechaFinal + "' "
                 + " AND vta_producto.PR_INVENTARIO = 1 AND vta_producto.EMP_ID = '" + intEmpresa + "'"
                 + strFiltroSucursal
                 + strFiltroFamilia
                 + strFiltroCodigo
                 + " GROUP BY "
                 + " vta_sucursal.SC_NOMBRE,PR_UNIDADMEDIDA; ";

      } else { //mostrar detalle si seleccion es igual a 3
        

         strSql = "select vta_producto.PR_CODIGO,vta_producto.PR_DESCRIPCION, "
                 + " (SELECT p1.PC_DESCRIPCION FRom vta_prodcat1 p1 where p1.PC_ID = vta_producto.PR_CATEGORIA1) AS FAMILIA, "
                 + " (select u.ME_DESCRIPCION FROM vta_unidadmedida u where u.ME_ID = PR_UNIDADMEDIDA) as unidad, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS - vta_movproddeta.MPD_SALIDAS,0)) as inicial, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_ENTRADAS,0)) as entradas, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',vta_movproddeta.MPD_SALIDAS,0)) as salidas, "
                 + " sum(vta_movproddeta.MPD_ENTRADAS- vta_movproddeta.MPD_SALIDAS) as final, "
                 + " sum(if(vta_movprod.MP_FECHA<'" + strFechaInicial + "',(vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO) - (vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO),0)) as inicialc, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',(vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO),0)) as entradasc, "
                 + " sum(if(vta_movprod.MP_FECHA>='" + strFechaInicial + "',(vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO),0)) as salidasc, "
                 + " sum((vta_movproddeta.MPD_ENTRADAS*vta_movproddeta.MPD_COSTO) - (vta_movproddeta.MPD_SALIDAS*vta_movproddeta.MPD_COSTO)) as finalc, "
                 + " count(vta_movprod.MP_ID) as num_mov  "
                 + " from vta_movprod, vta_movproddeta, vta_producto where "
                 + " vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_producto.PR_ID = vta_movproddeta.PR_ID AND "
                 + " vta_movprod.MP_ANULADO = 0 "
                 + " AND vta_movprod.MP_FECHA<='" + strFechaFinal + "' "
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
            deta.setDbEntradas(rs.getDouble("entradas"));
            deta.setDbSalidas(rs.getDouble("salidas"));
            deta.setDbFinal(rs.getDouble("final"));
            deta.setIntMovimientos(rs.getInt("num_mov"));
            deta.setDbInicialC(rs.getDouble("inicialc"));
            deta.setDbEntradasC(rs.getDouble("entradasc"));
            deta.setDbSalidasC(rs.getDouble("salidasc"));
            deta.setDbFinalC(rs.getDouble("finalc"));
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
      strXML.append("<Reporte_Inventario>");

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
         strXML.append(" Inicial=\"").append(deta.getDbInicial()).append("\" ");
         strXML.append(" Entradas=\"").append(deta.getDbEntradas()).append("\" ");
         strXML.append("Salidas= \"").append(deta.getDbSalidas()).append("\" ");
         strXML.append("Final=\"").append(deta.getDbFinal()).append("\" ");
         strXML.append("Movimientos=\"").append(deta.getIntMovimientos()).append("\" ");
         strXML.append(" InicialC=\"").append(deta.getDbInicialC()).append("\" ");
         strXML.append(" EntradasC=\"").append(deta.getDbEntradasC()).append("\" ");
         strXML.append("SalidasC= \"").append(deta.getDbSalidasC()).append("\" ");
         strXML.append("FinalC=\"").append(deta.getDbFinalC()).append("\" ");

         strXML.append("/>");
      }
      strXML.append("</Reporte_Inventario>");
      return strXML.toString();

   }

}
