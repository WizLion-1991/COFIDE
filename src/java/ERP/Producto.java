package ERP;

import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.DigitoVerificador;
import comSIWeb.Utilerias.Parser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Realiza operaciones con productos como cambio de precios y nos regresa las
 * existencia actuales
 *
 * @author zeus
 */
public class Producto {

   /**
    * Guarda los precios para determinado producto
    *
    * @param oConn Es la conexion a la base de datos
    * @param strCodigo Es el codigo o clave del producto
    * @param lstSuc Es la lista de codigos o claves de productos
    * @param lstList Es un arreglo con los numeros de lista de precios
    * @param lstPrec Es un arreglo con los precios
    * @param lstDesc Es un arreglo con los descuentos
    * @param lstLealtad Es un arreglo con los puntos de lealtad
    * @param lstLealtadCk Es un arreglo con los puntos de lealtad que se usaran
    * para cambiar los puntos
    * @param strLstPrecUSD Es un arreglo con los precios en dolares
    * @param strLstPto Es un arreglo con los puntos MLM
    * @param strLstNego Es un arreglo con el valor negocio MLM
    * @param strLstPub Es un arreglo con los precios publicos
    * @param strLstApDPto Es un arreglo con la bandera de descuento en puntos
    * @param strLstApDNego Es un arreglo con la bandera de descuento en negocio
    * @param strLstPUtilidad Es un arreglo con el porcentaje de utilidad
    * @return Regresa Ok en caso de que todo sea satisfactorio
    */
   public String GuardaPrecios(Conexion oConn, String strCodigo, String[] lstSuc, String[] lstList, String[] lstPrec,
           String[] lstDesc, String[] lstLealtad, String[] lstLealtadCk,
           String[] strLstPrecUSD, String[] strLstPto, String[] strLstNego, String[] strLstPub,
           String[] strLstApDPto, String[] strLstApDNego, String[] strLstPUtilidad) {
      String strResult = "OK";
      Bitacora bitacora = new Bitacora();
      //Recorremos las sucursales
      for (int j = 0; j < lstSuc.length; j++) {
         String strPrId = "";
         String strSql = "select PR_ID from vta_producto where PR_CODIGO = '" + strCodigo + "' AND SC_ID = " + lstSuc[j];
         ResultSet Rs;
         try {
            Rs = oConn.runQuery(strSql, true);
            while (Rs.next()) {
               strPrId = Rs.getString("PR_ID");
            }
            Rs.close();
         } catch (SQLException ex) {
            ex.fillInStackTrace();
            bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRECIOS", oConn);
         }
         //Si encontramos el producto lo almacenamos
         if (!strPrId.equals("")) {
            //Borramos los precios actuales del producto
            strSql = "delete from vta_prodprecios where PR_ID = " + strPrId;
            oConn.runQueryLMD(strSql);
            //Insertamos los nuevos precios
            for (int i = 0; i < lstList.length; i++) {
               strSql = "insert into vta_prodprecios (PR_ID,LP_ID,PP_PRECIO,PP_APDESC,PP_PTOSLEAL,PP_PTOSLEALCAM,"
                       + "PP_PRECIO_USD,PP_PUNTOS,PP_NEGOCIO,PP_PPUBLICO,PP_APDESCPTO,PP_APDESCNEGO,PP_PUTILIDAD)"
                       + "VALUES"
                       + "("
                       + strPrId + "," + lstList[i] + "," + lstPrec[i] + "," + lstDesc[i] + "," + lstLealtad[i] + "," + lstLealtadCk[i] + ""
                       + "," + strLstPrecUSD[i] + "," + strLstPto[i] + "," + strLstNego[i] + "," + strLstPub[i] + "," + strLstApDPto[i] + "," + strLstApDNego[i]
                       + "," + strLstPUtilidad[i]
                       + ")";
               oConn.runQueryLMD(strSql);
            }
         }
      }
      return strResult;
   }

   /**
    * Nos regresa la existencia de un producto
    *
    * @param strPr_Id Es el id del producto
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa la existencia de un producto en particular
    */
   public double RegresaExistencia(String strPr_Id, Conexion oConn) {
      double dblExistencia = 0;
      Bitacora bitacora = new Bitacora();
      String strSql = "select sum(MPD_ENTRADAS - MPD_SALIDAS) as texist "
              + "from vta_movproddeta,vta_movprod "
              + "WHERE vta_movproddeta.MP_ID = vta_movprod.MP_ID and "
              + " vta_movproddeta.PR_ID='" + strPr_Id + "' and MP_ANULADO = 0";
      ResultSet Rs;
      try {
         Rs = oConn.runQuery(strSql, true);
         while (Rs.next()) {
            dblExistencia = Rs.getDouble("texist");
         }
         Rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "REGRESA EXISTENCIA", oConn);
      }
      return dblExistencia;
   }

   /**
    * Calcula el codigo EAN 13 de un producto
    *
    * @param strPR_ID Es el id del producto
    * @param oConn Es la conexion
    * @return Regresa Error en caso de un percance o el mensaje de que fue
    * exitoso
    */
   public String GeneraCodigoEAN13(String strPR_ID, Conexion oConn) {
      String strResult = "Código de barras EAN 13 registrado exitosamente";
      String strPR_CODIGO = "";
      String strPR_CODBARRAS = "";
      int intSC_ID = 0;
      //Obtenemos datos de la sucursal
      String strSql = "select PR_CODIGO,SC_ID,PR_CODBARRAS from vta_producto where PR_ID = '" + strPR_ID + "' ";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strPR_CODIGO = rs.getString("PR_CODIGO");
            strPR_CODBARRAS = rs.getString("PR_CODBARRAS");
            intSC_ID = rs.getInt("SC_ID");
         }
         rs.close();
         if (!strPR_CODIGO.isEmpty()) {
            String strSC_EAN13_BASE = "";
            int intSC_EAN13_SEMILLA = 0;
            //Validamos si ya tiene un codigo de barras asignado
            if (strPR_CODBARRAS.trim().isEmpty() || strPR_CODBARRAS.equals("0")) {
               //Consultamos los datos de la sucursal
               strSql = "select SC_EAN13_BASE,SC_EAN13_SEMILLA from vta_sucursal where SC_ID = " + intSC_ID + " ";
               rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  strSC_EAN13_BASE = rs.getString("SC_EAN13_BASE");
                  intSC_EAN13_SEMILLA = rs.getInt("SC_EAN13_SEMILLA");
                  if (!strSC_EAN13_BASE.isEmpty()) {
                     intSC_EAN13_SEMILLA++;
                  } else {
                     strResult = "ERROR:Falta definir la base de EAN13 en la sucursal";
                  }
               }
               rs.close();
               if (!strResult.startsWith("ERROR")) {
                  //Calculamos el codigo de barras...
                  StringBuilder strCodBar = new StringBuilder();
                  System.out.println("intSC_EAN13_SEMILLA " + intSC_EAN13_SEMILLA);
                  strCodBar.append(strSC_EAN13_BASE);
                  strCodBar.append(Parser.fillLeft(intSC_EAN13_SEMILLA + "", 4));
                  //Calculamos el digito verificador
                  String strDigito = DigitoVerificador.CalculoDigitoVerificador12(strCodBar.toString(), false);
                  strCodBar.append(strDigito);
                  //Buscamos si otro producto tiene este codigo de barras para enviar error
                  boolean bolEncontroDupli = false;
                  String strSearchCB = "select PR_ID from vta_producto where PR_CODBARRAS = '" + strCodBar + "' ";
                  rs = oConn.runQuery(strSearchCB, true);
                  while (rs.next()) {
                     bolEncontroDupli = true;
                  }
                  rs.close();
                  if (!bolEncontroDupli) {
                     //Actualizamos el producto
                     String strUpdate = "update vta_producto set PR_CODBARRAS = '" + strCodBar + "' where PR_ID =  '" + strPR_ID + "'";
                     oConn.runQueryLMD(strUpdate);
                     //Actualizamos la sucursal
                     strUpdate = "update vta_sucursal set SC_EAN13_SEMILLA = " + intSC_EAN13_SEMILLA + " where SC_ID = " + intSC_ID + " ";
                     oConn.runQueryLMD(strUpdate);
                  } else {
                     strResult = "ERROR:El codigo de barras generado(" + strCodBar + ") ya existe en otro item, verifique la semilla del codigo de barras.";
                  }
               }

            } else {
               strResult = "ERROR:Ya existe un codigo de barras asignado";
            }
         } else {
            strResult = "ERROR:No existe el producto indicado";
         }

      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      return strResult;
   }

   /**
    * Copia la informacion de los productos de una bodega a otra
    *
    * @param oConn Es la conexion
    * @param intSC_IDOrigen Es la sucursal origen
    * @param intSC_IDDestino Es la sucursal destino
    * @return Regresa OK si todo sale bien
    */
   public String CopiaProductosSucursal(Conexion oConn, int intSC_IDOrigen, int intSC_IDDestino) throws SQLException {
      String strRes = "OK";
      //Objeto que contendra la estructura de la tabla de productos
      TableMaster tbn = new TableMaster("vta_producto", "PR_ID", "", "");
      tbn.setBolGetAutonumeric(true);
      tbn.ObtenEstructura(oConn);
      //Objeto que contendra la estructura de la tabla de precios
      TableMaster tbn1 = new TableMaster("vta_prodprecios", "PP_ID", "", "");
      tbn1.ObtenEstructura(oConn);
      //Objeto que contendra la estructura de la tabla de paquetes
      TableMaster tbn2 = new TableMaster("vta_paquetes", "PAQ_ID", "", "");
      tbn2.ObtenEstructura(oConn);
      //Buscamos la empresa de la sucursal destino
      String strSql3 = "select EMP_ID from vta_sucursal where SC_ID = " + intSC_IDDestino ;
      int intIdEmpDestino = 0 ;
      ResultSet rs3 = oConn.runQuery(strSql3, true);
      while (rs3.next()) {
        intIdEmpDestino = rs3.getInt("EMP_ID");
      }
      rs3.close();
      //Buscamos todos los productos de la sucursal origen
      String strSql = "select PR_ID,PR_CODIGO from vta_producto where SC_ID = " + intSC_IDOrigen;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intIdProd = rs.getInt("PR_ID");
            String strCodigo = rs.getString("PR_CODIGO");
            boolean bolExiste = false;
            //Buscamos si existe en la sucursal destino
            strSql = "select PR_ID from vta_producto where SC_ID = " + intSC_IDDestino + " and PR_CODIGO = '" + strCodigo + "'";
            ResultSet rs2 = oConn.runQuery(strSql, true);
            while (rs2.next()) {
               bolExiste = true;
            }
            rs2.close();
            //No existe lo damos de alta
            if (!bolExiste) {
               //Copiamos los datos y asignamos valores para la nueva sucursal
               tbn.ObtenDatos(intIdProd, oConn);
               tbn.setFieldInt("PR_ID", 0);
               tbn.setFieldInt("PR_EXISTENCIA", 0);
               tbn.setFieldInt("SC_ID", intSC_IDDestino);
               tbn.setFieldInt("EMP_ID", intIdEmpDestino);
               String strResp1 = tbn.Agrega(oConn);
               if (!strResp1.equals("OK")) {
                  strRes += strResp1;
               } else {
                  //Copiamos los precios
                  strSql = "select PP_ID from vta_prodprecios where PR_ID = " + intIdProd;
                  rs2 = oConn.runQuery(strSql, true);
                  while (rs2.next()) {
                     int intPP_ID = rs2.getInt("PP_ID");
                     tbn1.ObtenDatos(intPP_ID, oConn);
                     tbn1.setFieldInt("PR_ID", Integer.valueOf(tbn.getValorKey()));
                     String strResp2 = tbn1.Agrega(oConn);
                     if (!strResp2.equals("OK")) {
                        strRes += strResp2;
                     }
                  }
                  rs2.close();
                  //Copiamos los paquetes
                  strSql = "select PAQ_ID from vta_paquetes where PR_ID = " + intIdProd;
                  rs2 = oConn.runQuery(strSql, true);
                  while (rs2.next()) {
                     int intPAQ_ID = rs2.getInt("PAQ_ID");
                     tbn2.ObtenDatos(intPAQ_ID, oConn);
                     tbn2.setFieldInt("PR_ID", Integer.valueOf(tbn.getValorKey()));
                     String strResp3 = tbn2.Agrega(oConn);
                     if (!strResp3.equals("OK")) {
                        strRes += strResp3;
                     }
                  }
                  rs2.close();
               }
            }
         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
      }


      return strRes;
   }
}
