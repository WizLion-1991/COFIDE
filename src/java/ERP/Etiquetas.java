/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.NumberString;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class Etiquetas {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Etiquetas.class.getName());

   public String doEtiquetaProducto(String strCodigoProd, int Cantidad, int Etiqueta, Conexion oConn) {
      StringBuilder strCadenaFinal = new StringBuilder("");
      String strSQL = "Select * From vta_etiquetas Where ET_ID =" + Etiqueta;
      String strEtiqueta = "";
      try {
         ResultSet rs = oConn.runQuery(strSQL, true);
         while (rs.next()) {
            strEtiqueta = rs.getString("ET_CONTENIDO");
         }
         String strSQL2 = "Select "
                 + "(Select PC_DESCRIPCION From vta_prodcat1 Where PC_ID = vta_producto.PR_CATEGORIA1) as Marca,"
                 + "(Select PC2_DESCRIPCION From vta_prodcat2 Where PC2_ID = vta_producto.PR_CATEGORIA2) as Pais,"
                 + "(Select PC3_DESCRIPCION From vta_prodcat3 Where PC3_ID = vta_producto.PR_CATEGORIA3) as Color,"
                 + "(Select PC4_DESCRIPCION From vta_prodcat4 Where PC4_ID = vta_producto.PR_CATEGORIA4) as Talla,"
                 + "(Select PC5_DESCRIPCION From vta_prodcat5 Where PC5_ID = vta_producto.PR_CATEGORIA5) as Comp,"
                 + "(Select PC6_DESCRIPCION From vta_prodcat6 Where PC6_ID = vta_producto.PR_CATEGORIA6) as Norma,"
                 + "(Select PC7_DESCRIPCION From vta_prodcat7 Where PC7_ID = vta_producto.PR_CATEGORIA7) as Cat7,"
                 + "(Select PC8_DESCRIPCION From vta_prodcat8 Where PC8_ID = vta_producto.PR_CATEGORIA8) as ListaPrec,"
                 + "(Select PC9_DESCRIPCION From vta_prodcat9 Where PC9_ID = vta_producto.PR_CATEGORIA9) as Grupo,"
                 + "(Select PC10_DESCRIPCION From vta_prodcat10 Where PC10_ID = vta_producto.PR_CATEGORIA10) as SubGpo,"
                 + "(Select ME_DESCRIPCION From vta_unidadmedida Where ME_ID = vta_producto.PR_UNIDADMEDIDA_COMPRA) as UM,"
                 + "vta_producto.* "
                 + " From vta_producto "
                 + " Join vta_prodcat1 on vta_prodcat1.PC_ID = vta_producto.PR_CATEGORIA1 "
                 + " Where vta_producto.PR_CODIGO =  '" + strCodigoProd + "'";
         ResultSet rs2 = oConn.runQuery(strSQL2, true);
         while (rs2.next()) {

            if (strEtiqueta.contains("[PP_PRECIO1]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=1";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO1]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO1]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO2]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=2";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO2]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO2]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO3]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=3";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO3]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO3]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO4]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=4";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO4]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO4]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO5]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=5";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO5]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO5]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD1]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "1";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD1]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD1]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD2]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "2";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD2]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD2]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD3]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "3";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD3]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD3]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD4]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "4";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD4]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD4]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD5]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "5";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD5]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD5]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PR_CODIGO]")) {
               strEtiqueta = strEtiqueta.replace("[PR_CODIGO]", strCodigoProd);
            }
            if (strEtiqueta.contains("[PR_DESCRIPCIONCORTA]")) {
               if (rs2.getString("PR_DESCRIPCIONCORTA") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCIONCORTA]", rs2.getString("PR_DESCRIPCIONCORTA"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCIONCORTA]", "");
               }
            }
            if (strEtiqueta.contains("[PR_DESCRIPCION]")) {

               if (rs2.getString("PR_DESCRIPCION") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCION]", rs2.getString("PR_DESCRIPCION"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCION]", "");
               }
            }
            //Parte izquierda
            if (strEtiqueta.contains("[PR_DESCRIPCION_LEFT")) {
               String strDesc = rs2.getString("PR_DESCRIPCION");
               int intpos = strEtiqueta.indexOf("[PR_DESCRIPCION_LEFT");
               int intpos2 = strEtiqueta.indexOf("]", intpos);
               String strAncho = strEtiqueta.substring(intpos, intpos2).replace("[PR_DESCRIPCION_LEFT", "");
               try {
                  int intAncho = Integer.valueOf(strAncho);
                  strEtiqueta = strEtiqueta.replace(strEtiqueta.substring(intpos, intpos2) + "]", strDesc.substring(0, intAncho));
               } catch (NumberFormatException ex) {
               }

            }
            //Parte derecha
            if (strEtiqueta.contains("[PR_DESCRIPCION_RIGHT")) {
               String strDesc = rs2.getString("PR_DESCRIPCION");
               int intpos = strEtiqueta.indexOf("[PR_DESCRIPCION_RIGHT");
               int intpos2 = strEtiqueta.indexOf("]", intpos);
               String strAncho = strEtiqueta.substring(intpos, intpos2).replace("[PR_DESCRIPCION_RIGHT", "");
               try {
                  int intAncho = Integer.valueOf(strAncho);
                  strEtiqueta = strEtiqueta.replace(strEtiqueta.substring(intpos, intpos2) + "]", strDesc.substring(strDesc.length() - intAncho, strDesc.length()));
               } catch (NumberFormatException ex) {
               }

            }
            if (strEtiqueta.contains("[PR_UNIDADMEDIDA_COMPRA]")) {

               if (rs2.getString("UM") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_UNIDADMEDIDA_COMPRA]", rs2.getString("UM"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_UNIDADMEDIDA_COMPRA]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO1]")) {
               if (rs2.getString("PR_CUIDADO1") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO1]", rs2.getString("PR_CUIDADO1"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO1]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO2]")) {
               if (rs2.getString("PR_CUIDADO2") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO2]", rs2.getString("PR_CUIDADO2"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO2]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO3]")) {
               if (rs2.getString("PR_CUIDADO3") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO3]", rs2.getString("PR_CUIDADO3"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO3]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO4]")) {
               if (rs2.getString("PR_CUIDADO4") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO4]", rs2.getString("PR_CUIDADO4"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO4]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO5]")) {
               if (rs2.getString("PR_CUIDADO5") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO5]", rs2.getString("PR_CUIDADO5"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO5]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO6]")) {
               if (rs2.getString("PR_CUIDADO6") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO6]", rs2.getString("PR_CUIDADO6"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO6]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO7]")) {
               if (rs2.getString("PR_CUIDADO7") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO7]", rs2.getString("PR_CUIDADO7"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO7]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO8]")) {
               if (rs2.getString("PR_CUIDADO8") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO8]", rs2.getString("PR_CUIDADO8"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO8]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO9]")) {
               if (rs2.getString("PR_CUIDADO9") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO9]", rs2.getString("PR_CUIDADO9"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO9]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO10]")) {
               if (rs2.getString("PR_CUIDADO10") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO10]", rs2.getString("PR_CUIDADO10"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO10]", "");
               }
            }

            if (strEtiqueta.contains("[PR_CATEGORIA1]")) {
               if (rs2.getString("Marca") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA1]", rs2.getString("Marca"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA1]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA2]")) {
               if (rs2.getString("Pais") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA2]", rs2.getString("Pais"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA2]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA3]")) {
               if (rs2.getString("Color") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA3]", rs2.getString("Color"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA3]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA4]")) {
               if (rs2.getString("Talla") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA4]", rs2.getString("Talla"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA4]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA5]")) {
               if (rs2.getString("PR_DESCRIPCIONCORTA") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA5]", rs2.getString("Comp"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA5]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA6]")) {
               if (rs2.getString("Norma") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA6]", rs2.getString("Norma"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA6]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA7]")) {
               if (rs2.getString("Cat7") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA7]", rs2.getString("Cat7"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA7]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA8]")) {
               if (rs2.getString("ListaPrec") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA8]", rs2.getString("ListaPrec"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA8]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA9]")) {
               if (rs2.getString("Grupo") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA9]", rs2.getString("Grupo"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA9]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA10]")) {
               if (rs2.getString("SubGpo") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA10]", rs2.getString("SubGpo"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA10]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CODBARRAS]")) {
               if (rs2.getString("PR_CODBARRAS") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CODBARRAS]", rs2.getString("PR_CODBARRAS"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CODBARRAS]", "");
               }
            }

         }

         for (int i = 0; i < Cantidad; i++) {
            strCadenaFinal.append(strEtiqueta).append("\n");
         }

      } catch (SQLException ex) {
      }
      return strCadenaFinal.toString();
   }

   public String doEtiquetaCompra(int intIdCompra, int Etiqueta, Conexion oConn) {
      StringBuilder strCadenaFinal = new StringBuilder("");
      String strSQL = "Select * From vta_etiquetas Where ET_ID = " + Etiqueta;

      String strEtiqueta = "";
      try {
         ResultSet rs = oConn.runQuery(strSQL, true);
         while (rs.next()) {
            strEtiqueta = rs.getString("ET_CONTENIDO");
         }
         String strSQL2 = "Select "
                 + "vta_compra.COM_STATUS, "
                 + "(Select PC_DESCRIPCION From vta_prodcat1 Where PC_ID = vta_producto.PR_CATEGORIA1) as Marca,"
                 + "(Select PC2_DESCRIPCION From vta_prodcat2 Where PC2_ID = vta_producto.PR_CATEGORIA2) as Pais,"
                 + "(Select PC3_DESCRIPCION From vta_prodcat3 Where PC3_ID = vta_producto.PR_CATEGORIA3) as Color,"
                 + "(Select PC4_DESCRIPCION From vta_prodcat4 Where PC4_ID = vta_producto.PR_CATEGORIA4) as Talla,"
                 + "(Select PC5_DESCRIPCION From vta_prodcat5 Where PC5_ID = vta_producto.PR_CATEGORIA5) as Comp,"
                 + "(Select PC6_DESCRIPCION From vta_prodcat6 Where PC6_ID = vta_producto.PR_CATEGORIA6) as Norma,"
                 + "(Select PC7_DESCRIPCION From vta_prodcat7 Where PC7_ID = vta_producto.PR_CATEGORIA7) as Cat7,"
                 + "(Select PC8_DESCRIPCION From vta_prodcat8 Where PC8_ID = vta_producto.PR_CATEGORIA8) as ListaPrec,"
                 + "(Select PC9_DESCRIPCION From vta_prodcat9 Where PC9_ID = vta_producto.PR_CATEGORIA9) as Grupo,"
                 + "(Select PC10_DESCRIPCION From vta_prodcat10 Where PC10_ID = vta_producto.PR_CATEGORIA10) as SubGpo,"
                 + "(Select ME_DESCRIPCION From vta_unidadmedida Where ME_ID = vta_producto.PR_UNIDADMEDIDA_COMPRA) as UM,"
                 + "vta_producto.* "
                 + " From vta_compra "
                 + " Join vta_compradeta on vta_compradeta.COM_ID = vta_compra.COM_ID"
                 + " Join vta_producto on vta_compradeta.PR_ID = vta_producto.PR_ID "
                 + " Where vta_compra.COM_ID =" + intIdCompra;
         ResultSet rs2 = oConn.runQuery(strSQL2, true);
         String strEtiqueta2 = "";

         while (rs2.next()) {
            strEtiqueta2 = strEtiqueta;

            if (strEtiqueta2.contains("[PR_CODIGO]")) {
               if (rs2.getString("PR_CODIGO") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CODIGO]", rs2.getString("PR_CODIGO"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CODIGO]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_DESCRIPCIONCORTA]")) {
               if (rs2.getString("PR_DESCRIPCIONCORTA") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_DESCRIPCIONCORTA]", rs2.getString("PR_DESCRIPCIONCORTA"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_DESCRIPCIONCORTA]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_DESCRIPCION]")) {
               if (rs2.getString("PR_DESCRIPCION") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_DESCRIPCION]", rs2.getString("PR_DESCRIPCION"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_DESCRIPCION]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_UNIDADMEDIDA_COMPRA]")) {
               if (rs2.getString("UM") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_UNIDADMEDIDA_COMPRA]", rs2.getString("UM"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_UNIDADMEDIDA_COMPRA]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO1]")) {
               if (rs2.getString("PR_CUIDADO1") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO1]", rs2.getString("PR_CUIDADO1"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO1]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO2]")) {
               if (rs2.getString("PR_CUIDADO2") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO2]", rs2.getString("PR_CUIDADO2"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO2]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO3]")) {
               if (rs2.getString("PR_CUIDADO3") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO3]", rs2.getString("PR_CUIDADO3"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO3]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO4]")) {
               if (rs2.getString("PR_CUIDADO4") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO4]", rs2.getString("PR_CUIDADO4"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO4]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO5]")) {
               if (rs2.getString("PR_CUIDADO5") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO5]", rs2.getString("PR_CUIDADO5"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO5]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO6]")) {
               if (rs2.getString("PR_CUIDADO6") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO6]", rs2.getString("PR_CUIDADO6"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO6]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO7]")) {
               if (rs2.getString("PR_CUIDADO7") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO7]", rs2.getString("PR_CUIDADO7"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO7]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO8]")) {
               if (rs2.getString("PR_CUIDADO8") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO8]", rs2.getString("PR_CUIDADO8"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO8]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO9]")) {
               if (rs2.getString("PR_CUIDADO9") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO9]", rs2.getString("PR_CUIDADO9"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO9]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CUIDADO10]")) {
               if (rs2.getString("PR_CUIDADO10") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO10]", rs2.getString("PR_CUIDADO10"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CUIDADO10]", "");
               }
            }

            if (strEtiqueta2.contains("[PR_CATEGORIA1]")) {
               if (rs2.getString("Marca") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA1]", rs2.getString("Marca"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA1]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA2]")) {
               if (rs2.getString("Pais") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA2]", rs2.getString("Pais"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA2]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA3]")) {
               if (rs2.getString("Color") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA3]", rs2.getString("Color"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA3]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA4]")) {
               if (rs2.getString("Talla") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA4]", rs2.getString("Talla"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA4]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA5]")) {
               if (rs2.getString("Comp") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA5]", rs2.getString("Comp"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA5]", "");
               }

            }
            if (strEtiqueta2.contains("[PR_CATEGORIA6]")) {
               if (rs2.getString("Norma") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA6]", rs2.getString("Norma"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA6]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA7]")) {
               if (rs2.getString("Cat7") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA7]", rs2.getString("Cat7"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA7]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA8]")) {
               if (rs2.getString("ListaPrec") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA8]", rs2.getString("ListaPrec"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA8]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA9]")) {
               if (rs2.getString("Grupo") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA9]", rs2.getString("Grupo"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA9]", "");
               }
            }
            if (strEtiqueta2.contains("[PR_CATEGORIA10]")) {
               if (rs2.getString("SubGpo") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA10]", rs2.getString("SubGpo"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CATEGORIA10]", "");
               }
            }

            if (strEtiqueta2.contains("[PR_CODBARRAS]")) {
               if (rs2.getString("PR_CODBARRAS") != null) {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CODBARRAS]", rs2.getString("PR_CODBARRAS"));
               } else {
                  strEtiqueta2 = strEtiqueta2.replace("[PR_CODBARRAS]", "");
               }
            }
            strCadenaFinal.append(strEtiqueta2).append("\n");

         }
      } catch (SQLException ex) {
      }

      return strCadenaFinal.toString();
   }

   public boolean ValidaODC(int intIdCompra, Conexion oConn) {
      boolean boolEsValido = false;
      String strSQL = "Select COM_ID,COM_STATUS From vta_compra Where COM_ID = " + intIdCompra;
      int intStatus = 0;
      try {
         ResultSet rs = oConn.runQuery(strSQL, true);
         while (rs.next()) {
            intStatus = rs.getInt("COM_STATUS");
         }
         switch (intStatus) {
            case 3:
            case 4:
            case 5:
            case 6:
               boolEsValido = true;
               break;
            default:
               boolEsValido = false;
         }

      } catch (SQLException ex) {
      }
      return boolEsValido;
   }

   public String doEtiquetaODC(int Etiqueta, int IdEmpresa, int strIdSucursal, int strNumRecepcion, Conexion oConn) {
      StringBuilder strCadenaFinal = new StringBuilder("");
      String strSQL = "Select * From vta_etiquetas Where ET_ID =" + Etiqueta;
      String strEtiqueta = "";
      int intClasifica = 0;
      try {
         ResultSet rs = oConn.runQuery(strSQL, true);
         while (rs.next()) {
            strEtiqueta = rs.getString("ET_CONTENIDO");
            intClasifica = rs.getInt("ET_CLASIFICACION_PROD");
         }
         rs.close();
         String strSQL2 = "Select "
                 + "(Select PC_DESCRIPCION From vta_prodcat1 Where PC_ID = vta_producto.PR_CATEGORIA1) as Marca,"
                 + "(Select PC2_DESCRIPCION From vta_prodcat2 Where PC2_ID = vta_producto.PR_CATEGORIA2) as Pais,"
                 + "(Select PC3_DESCRIPCION From vta_prodcat3 Where PC3_ID = vta_producto.PR_CATEGORIA3) as Color,"
                 + "(Select PC4_DESCRIPCION From vta_prodcat4 Where PC4_ID = vta_producto.PR_CATEGORIA4) as Talla,"
                 + "(Select PC5_DESCRIPCION From vta_prodcat5 Where PC5_ID = vta_producto.PR_CATEGORIA5) as Comp,"
                 + "(Select PC6_DESCRIPCION From vta_prodcat6 Where PC6_ID = vta_producto.PR_CATEGORIA6) as Norma,"
                 + "(Select PC7_DESCRIPCION From vta_prodcat7 Where PC7_ID = vta_producto.PR_CATEGORIA7) as Cat7,"
                 + "(Select PC8_DESCRIPCION From vta_prodcat8 Where PC8_ID = vta_producto.PR_CATEGORIA8) as ListaPrec,"
                 + "(Select PC9_DESCRIPCION From vta_prodcat9 Where PC9_ID = vta_producto.PR_CATEGORIA9) as Grupo,"
                 + "(Select PC10_DESCRIPCION From vta_prodcat10 Where PC10_ID = vta_producto.PR_CATEGORIA10) as SubGpo,"
                 + "(Select ME_DESCRIPCION From vta_unidadmedida Where ME_ID = vta_producto.PR_UNIDADMEDIDA_COMPRA) as UM,"
                 + "vta_producto.*, EMP_RAZONSOCIAL,EMP_RFC,EMP_CALLE,EMP_NUMERO,EMP_NUMINT,EMP_COLONIA,EMP_MUNICIPIO,EMP_CP,EMP_LOCALIDAD,EMP_ESTADO"
                 + " From vta_movprod,vta_movproddeta, vta_producto , vta_empresas, vta_tmp_series "
                 + " Where vta_movprod.MP_ID = vta_movproddeta.MP_ID AND vta_movproddeta.PR_ID = vta_producto.PR_ID AND vta_producto.EMP_ID = vta_empresas.EMP_ID  "
                 + " AND vta_producto.EMP_ID = " + IdEmpresa + "  and vta_movprod.SC_ID = " + strIdSucursal + " and vta_tmp_series.ID_SERIE <= 1 "
                 + " AND vta_movprod.MP_ID = " + strNumRecepcion + " ";
         if (intClasifica != 0) {
            strSQL2 += " AND vta_producto.PR_CATEGORIA7 = " + intClasifica;
         }
         ResultSet rs2 = oConn.runQuery(strSQL2, true);
         while (rs2.next()) {

            String strEtiquetaTYmp = new String(strEtiqueta);
            if (strEtiquetaTYmp.contains("[PP_PRECIO1]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=1";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO1]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO1]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO2]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=2";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO2]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO2]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO3]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=3";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO3]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO3]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO4]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=4";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO4]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO4]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO5]")) {
               String queryPrecio = "SELECT PP_PRECIO FROM vta_prodprecios WHERE PR_ID= '" + rs2.getString("PR_ID") + "' AND LP_ID=5";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO5]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO5]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD1]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "1";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD1]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD1]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD2]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "2";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD2]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD2]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD3]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "3";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD3]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD3]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD4]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "4";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD4]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD4]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PP_PRECIO_USD5]")) {
               String queryPrecio = "SELECT PP_PRECIO_USD FROM vta_prodprecios WHERE PR_ID=" + rs2.getString("PR_ID") + "' AND LP_ID=" + "5";
               ResultSet rPrecio = oConn.runQuery(queryPrecio, true);
               if (rPrecio.next()) {
                  double precio = rPrecio.getDouble("PP_PRECIO_USD");
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD5]", "" + NumberString.FormatearDecimal(precio, 2));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PP_PRECIO_USD5]", "Sin precio");
               }

            }

            if (strEtiqueta.contains("[PR_CODIGO]")) {
               strEtiqueta = strEtiqueta.replace("[PR_CODIGO]", rs2.getString("PR_CODIGO"));
            }

            if (strEtiqueta.contains("[PR_DESCRIPCIONCORTA]")) {
               if (rs2.getString("PR_DESCRIPCIONCORTA") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCIONCORTA]", rs2.getString("PR_DESCRIPCIONCORTA"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCIONCORTA]", "");
               }
            }

            if (strEtiqueta.contains("[PR_DESCRIPCION]")) {

               if (rs2.getString("PR_DESCRIPCION") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCION]", rs2.getString("PR_DESCRIPCION"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_DESCRIPCION]", "");
               }
            }
            //Parte izquierda
            if (strEtiqueta.contains("[PR_DESCRIPCION_LEFT")) {
               String strDesc = rs2.getString("PR_DESCRIPCION");
               int intpos = strEtiqueta.indexOf("[PR_DESCRIPCION_LEFT");
               int intpos2 = strEtiqueta.indexOf("]", intpos);
               String strAncho = strEtiqueta.substring(intpos, intpos2).replace("[PR_DESCRIPCION_LEFT", "");
               try {
                  int intAncho = Integer.valueOf(strAncho);
                  strEtiqueta = strEtiqueta.replace(strEtiqueta.substring(intpos, intpos2) + "]", strDesc.substring(0, intAncho));
               } catch (NumberFormatException ex) {
               }

            }

            //Parte derecha
            if (strEtiqueta.contains("[PR_DESCRIPCION_RIGHT")) {
               String strDesc = rs2.getString("PR_DESCRIPCION");
               int intpos = strEtiqueta.indexOf("[PR_DESCRIPCION_RIGHT");
               int intpos2 = strEtiqueta.indexOf("]", intpos);
               String strAncho = strEtiqueta.substring(intpos, intpos2).replace("[PR_DESCRIPCION_RIGHT", "");
               try {
                  int intAncho = Integer.valueOf(strAncho);
                  strEtiqueta = strEtiqueta.replace(strEtiqueta.substring(intpos, intpos2) + "]", strDesc.substring(strDesc.length() - intAncho, strDesc.length()));
               } catch (NumberFormatException ex) {
               }

            }

            if (strEtiqueta.contains("[PR_UNIDADMEDIDA_COMPRA]")) {

               if (rs2.getString("UM") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_UNIDADMEDIDA_COMPRA]", rs2.getString("UM"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_UNIDADMEDIDA_COMPRA]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO1]")) {
               if (rs2.getString("PR_CUIDADO1") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO1]", rs2.getString("PR_CUIDADO1"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO1]", "");
               }
            }

            if (strEtiqueta.contains("[PR_CUIDADO2]")) {
               if (rs2.getString("PR_CUIDADO2") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO2]", rs2.getString("PR_CUIDADO2"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO2]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO3]")) {
               if (rs2.getString("PR_CUIDADO3") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO3]", rs2.getString("PR_CUIDADO3"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO3]", "");
               }
            }

            if (strEtiqueta.contains("[PR_CUIDADO4]")) {
               if (rs2.getString("PR_CUIDADO4") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO4]", rs2.getString("PR_CUIDADO4"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO4]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO5]")) {
               if (rs2.getString("PR_CUIDADO5") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO5]", rs2.getString("PR_CUIDADO5"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO5]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO6]")) {
               if (rs2.getString("PR_CUIDADO6") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO6]", rs2.getString("PR_CUIDADO6"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO6]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO7]")) {
               if (rs2.getString("PR_CUIDADO7") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO7]", rs2.getString("PR_CUIDADO7"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO7]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO8]")) {
               if (rs2.getString("PR_CUIDADO8") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO8]", rs2.getString("PR_CUIDADO8"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO8]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO9]")) {
               if (rs2.getString("PR_CUIDADO9") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO9]", rs2.getString("PR_CUIDADO9"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO9]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CUIDADO10]")) {
               if (rs2.getString("PR_CUIDADO10") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO10]", rs2.getString("PR_CUIDADO10"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CUIDADO10]", "");
               }
            }

            if (strEtiqueta.contains("[PR_CATEGORIA1]")) {
               if (rs2.getString("Marca") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA1]", rs2.getString("Marca"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA1]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA2]")) {
               if (rs2.getString("Pais") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA2]", rs2.getString("Pais"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA2]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA3]")) {
               if (rs2.getString("Color") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA3]", rs2.getString("Color"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA3]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA4]")) {
               if (rs2.getString("Talla") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA4]", rs2.getString("Talla"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA4]", "");
               }
            }

            if (strEtiqueta.contains("[PR_CATEGORIA5]")) {
               if (rs2.getString("PR_DESCRIPCIONCORTA") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA5]", rs2.getString("Comp"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA5]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA6]")) {
               if (rs2.getString("Norma") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA6]", rs2.getString("Norma"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA6]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA7]")) {
               if (rs2.getString("Cat7") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA7]", rs2.getString("Cat7"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA7]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA8]")) {
               if (rs2.getString("ListaPrec") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA8]", rs2.getString("ListaPrec"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA8]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA9]")) {
               if (rs2.getString("Grupo") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA9]", rs2.getString("Grupo"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA9]", "");
               }
            }
            if (strEtiqueta.contains("[PR_CATEGORIA10]")) {
               if (rs2.getString("SubGpo") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA10]", rs2.getString("SubGpo"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CATEGORIA10]", "");
               }
            }

            if (strEtiqueta.contains("[PR_CODBARRAS]")) {
               if (rs2.getString("PR_CODBARRAS") != null) {
                  strEtiqueta = strEtiqueta.replace("[PR_CODBARRAS]", rs2.getString("PR_CODBARRAS"));
               } else {
                  strEtiqueta = strEtiqueta.replace("[PR_CODBARRAS]", "");
               }
            }

            strCadenaFinal.append(strEtiquetaTYmp).append("\n");

         }
         rs2.close();

//         for (int i = 0; i < Cantidad; i++) {
         //strCadenaFinal.append(strEtiqueta).append("\n");
//         }
      } catch (SQLException ex) {
      }

      return strCadenaFinal.toString();

   }
}
