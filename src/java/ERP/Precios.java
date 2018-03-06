package ERP;

import ERP.BusinessEntities.ListaPrecios;
import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.NumberString;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Esta clase genera un xml con los precios de un determinado producto
 * <Precios>
 * <Precio num="1" pr_id="0" precio="0.0" descuento="0.0" lealtad="0.0"
 * lealtadCA = "0.0">
 * <Precios>
 *
 * @author zeus
 */
public class Precios {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   protected final String strXMLHEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
   private static final Logger log = LogManager.getLogger(Precios.class.getName());
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor
    */
   public Precios() {
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Nos regresa el xml de los precios del producto indicado
    * <Precios>
    * <Precio num="1" pr_id="0" precio="0.0" descuento="0.0" lealtad="0.0"
    * lealtadCA = "0.0">
    * <Precios>
    *
    * @param strPr_id Es el id del producto, en caso de ser cero nos trae los
    * valores en ceros(excepto la lista)
    * @param oConn Es la conexion a la base de datos
    * @param bolUsaPromos Indicamos si validamos las promociones
    * @param dblCantidad Es la cantidad de articulos solicitados
    * @return Regresa una cadena con el xml
    */
   public String getXML(String strPr_id, Conexion oConn, boolean bolUsaPromos, double dblCantidad) {
      Bitacora bitacora = new Bitacora();
      StringBuilder strXML = new StringBuilder(strXMLHEAD);
      strXML.append("<Precios>");

      //Consultamos El numero de listas de precios
      String strSql = "select * from vta_lprecios";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<Precio num=\"").append(rs.getInt("LP_ID")).append("\" ");
            if (strPr_id.equals("0")) {
               strXML.append(" pr_id=\"0\" precio=\"0.0\" descuento=\"1\" lealtad=\"0.0\" lealtadCA = \"0.0\" "
                       + " publico= \"0.0\" desc_pto=\"1\" desc_nego=\"1\" puntos=\"0\" negocio=\"0\" precioUsar=\"0\" precio_usd=\"0\" putilidad=\"0\" ");
            } else {
               //Consultamos los precios de la tabla de precios
               boolean bolExiste = false;
               strSql = "select PP_PRECIO,PP_APDESC,PP_PTOSLEAL,PP_PTOSLEALCAM,"
                       + "PP_PUNTOS,PP_NEGOCIO,PP_APDESCPTO,PP_APDESCNEGO,PP_PPUBLICO,PP_PRECIO_USD,PP_PUTILIDAD "
                       + " from vta_prodprecios where PR_ID = " + strPr_id + " AND LP_ID= " + rs.getInt("LP_ID");
               ResultSet rs2;
               try {
                  rs2 = oConn.runQuery(strSql, true);
                  while (rs2.next()) {
                     bolExiste = true;
                     strXML.append(" pr_id=\"").append(strPr_id).
                             append("\" " + "nombre=\"").append(rs.getString("LP_DESCRIPCION")).
                             append("\" " + "precio=\"").append(rs2.getDouble("PP_PRECIO")).
                             append("\" " + "precio_usd=\"").append(rs2.getDouble("PP_PRECIO_USD")).
                             append("\" " + "putilidad=\"").append(rs2.getDouble("PP_PUTILIDAD")).
                             append("\" " + "descuento=\"").append(rs2.getInt("PP_APDESC")).
                             append("\" " + "puntos=\"").append(rs2.getDouble("PP_PUNTOS")).
                             append("\" " + "negocio=\"").append(rs2.getDouble("PP_NEGOCIO")).
                             append("\" " + "desc_pto=\"").append(rs2.getInt("PP_APDESCPTO")).
                             append("\" " + "desc_nego=\"").append(rs2.getInt("PP_APDESCNEGO")).
                             append("\" " + "publico=\"").append(rs2.getDouble("PP_PPUBLICO")).
                             append("\" " + "lealtad=\"").append(rs2.getDouble("PP_PTOSLEAL")).
                             append("\" " + "lealtadCA = \"").append(rs2.getDouble("PP_PTOSLEALCAM")).
                             append("\" ");
                     //Aqui validariamos promociones
                     if (bolUsaPromos) {
                     }
                  }
                  rs2.close();
               } catch (SQLException ex) {
                  log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                  bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRECIOS", oConn);
               }
               //Si no existe el precio lo generamos en ceros
               if (bolExiste == false) {
                  strXML.append(" pr_id=\"0\" precio=\"0.0\" descuento=\"1\" lealtad=\"0.0\" lealtadCA = \"0.0\" "
                          + " publico= \"0.0\" desc_pto=\"1\" desc_nego=\"1\" puntos=\"0\" negocio=\"0\" precioUsar=\"0\" precio_usd=\"0\"  putilidad=\"0\" ");
               }
            }
            strXML.append(" />");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRECIOS", oConn);
      }

      strXML.append("</Precios>");
      return strXML.toString();
   }

   /**
    * Nos regresa el xml de los precios del producto indicado
    * <Precios>
    * <Precio num="1" pr_id="0" precio="0.0" descuento="0.0" lealtad="0.0"
    * lealtadCA = "0.0">
    * <Precios>
    *
    * @param strPr_id Es el codigo del producto, en caso de ser cero nos trae
    * los valores en ceros(excepto la lista)
    * @param oConn Es la conexion a la base de datos
    * @param bolUsaPromos Indicamos si validamos las promociones
    * @param dblCantidad Es la cantidad de articulos solicitados
    * @param strCt_Id Es el id del cliente al que queremos buscar el precio
    * @return Regresa una cadena con el xml
    */
   public String getXML(String strPr_id, Conexion oConn, boolean bolUsaPromos, double dblCantidad, String strCt_Id) {
      Bitacora bitacora = new Bitacora();
      StringBuilder strXML = new StringBuilder(strXMLHEAD);
      strXML.append("<Precios>");

      //Consultamos la lista de precios del cliente
      String strSql = "select CT_LPRECIOS from vta_cliente where CT_ID = " + strCt_Id;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intLprecios = rs.getInt("CT_LPRECIOS");
            if (intLprecios == 0) {
               intLprecios = 1;
            }
            strXML.append("<Precio num=\"").append(intLprecios).append("\" ");
            if (strPr_id.equals("0")) {
               strXML.append(" pr_id=\"0\" precio=\"0.0\" descuento=\"1\" lealtad=\"0.0\" lealtadCA = \"0.0\" "
                       + " publico= \"0.0\" desc_pto=\"1\" desc_nego=\"1\" puntos=\"0\" negocio=\"0\" precioUsar=\"0\" precio_usd=\"0\"  putilidad=\"0\" ");
            } else {
               //Consultamos los precios de la tabla de precios
               boolean bolExiste = false;
               strSql = "select PR_ID,PP_PRECIO,PP_APDESC,PP_PTOSLEAL,PP_PTOSLEALCAM"
                       + ",PP_PRECIO_USD,PP_PUNTOS,PP_NEGOCIO,PP_PPUBLICO,PP_APDESC,PP_APDESCPTO,PP_APDESCNEGO,PP_PUTILIDAD "
                       + " from vta_prodprecios where PR_ID = " + strPr_id + " AND LP_ID= " + intLprecios;
               ResultSet rs2;
               try {
                  rs2 = oConn.runQuery(strSql, true);
                  while (rs2.next()) {
                     bolExiste = true;
                     strXML.append(" pr_id=\"").append(rs2.getInt("PR_ID")).
                             append("\" " + "precio=\"").append(rs2.getDouble("PP_PRECIO")).
                             append("\" " + "precio_usd=\"").append(rs2.getDouble("PP_PRECIO_USD")).
                             append("\" " + "putilidad=\"").append(rs2.getDouble("PP_PUTILIDAD")).
                             append("\" " + "puntos=\"").append(rs2.getDouble("PP_PUNTOS")).
                             append("\" " + "negocio=\"").append(rs2.getDouble("PP_NEGOCIO")).
                             append("\" " + "publico=\"").append(rs2.getDouble("PP_PPUBLICO")).
                             append("\" " + "descuento=\"").append(rs2.getDouble("PP_APDESC")).
                             append("\" " + "desc_pto=\"").append(rs2.getDouble("PP_APDESCPTO")).
                             append("\" " + "desc_nego=\"").append(rs2.getDouble("PP_APDESCNEGO")).
                             append("\" " + "lealtad=\"").append(rs2.getDouble("PP_PTOSLEAL")).
                             append("\" " + "lealtadCA = \"").append(rs2.getDouble("PP_PTOSLEALCAM")).
                             append("\" ");
                     //Aqui validariamos promociones
                     if (bolUsaPromos) {
                     }
                  }
                  rs2.close();
               } catch (SQLException ex) {
                  log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
                  bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRECIOS", oConn);
               }
               //Si no existe el precio lo generamos en ceros
               if (bolExiste == false) {
                  strXML.append(" pr_id=\"0\" precio=\"0.0\" descuento=\"1\" lealtad=\"0.0\" lealtadCA = \"0.0\" "
                          + " publico= \"0.0\" desc_pto=\"1\" desc_nego=\"1\" puntos=\"0\" negocio=\"0\" precioUsar=\"0\" precio_usd=\"0\"  putilidad=\"0\" ");
               }
            }
            strXML.append(" />");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRECIOS", oConn);
      }

      strXML.append("</Precios>");
      return strXML.toString();
   }

   /**
    * Nos regresa el xml de los precios del producto indicado y una lista de
    * precios en particular
    * <Precios>
    * <Precio num="1" pr_id="0" precio="0.0" descuento="0.0" lealtad="0.0"
    * lealtadCA = "0.0">
    * <Precios>
    *
    * @param strPr_id Es el codigo del producto, en caso de ser cero nos trae
    * los valores en ceros(excepto la lista)
    * @param oConn Es la conexion a la base de datos
    * @param bolUsaPromos Indicamos si validamos las promociones
    * @param dblCantidad Es la cantidad de articulos solicitados
    * @param intLprecios Es la lista de precios
    * @param intMonedaVta Es la moneda con la que se hace la venta
    * @param intTipoCambio Es el tipo de cambio del cliente
    * @return Regresa una cadena con el xml
    */
   public String getXMLPrec(String strPr_id, Conexion oConn, boolean bolUsaPromos,
           double dblCantidad, int intLprecios, int intMonedaVta, int intTipoCambio) {
      Bitacora bitacora = new Bitacora();
      StringBuilder strXML = new StringBuilder("");
      strXML.append("<Precios>");

      //Consultamos la lista de precios del cliente
      if (intLprecios == 0) {
         intLprecios = 1;
      }
      strXML.append("<Precio num=\"").append(intLprecios).append("\" ");
      if (strPr_id.equals("0")) {
         strXML.append(" pr_id=\"0\" precio=\"0.0\" descuento=\"0.0\" lealtad=\"0.0\" lealtadCA = \"0.0\" "
                 + " publico= \"0.0\" desc_pto=\"1\" desc_nego=\"1\" puntos=\"0\" negocio=\"0\" precioUsar=\"0\" precio_usd=\"0\"  putilidad=\"0\" ");
      } else {
         //Consultamos los precios de la tabla de precios
         boolean bolExiste = false;
         String strSql = "select PR_ID,PP_PRECIO,PP_APDESC,PP_PTOSLEAL,PP_PTOSLEALCAM "
                 + ",PP_PRECIO_USD,PP_PUNTOS,PP_NEGOCIO,PP_PPUBLICO,PP_APDESC,PP_APDESCPTO,PP_APDESCNEGO,PP_PUTILIDAD "
                 + " from vta_prodprecios where PR_ID = " + strPr_id + " AND LP_ID= " + intLprecios;
         ResultSet rs2;
         try {
            rs2 = oConn.runQuery(strSql, true);
            while (rs2.next()) {
               bolExiste = true;
               //Calculamos el precio en la moneda de venta
               double dblPrecioUsar = GetPrecioUsar(oConn, bolUsaPromos,
                       dblCantidad, intLprecios, intMonedaVta, intTipoCambio,
                       rs2.getDouble("PP_PRECIO"), rs2.getDouble("PP_PRECIO_USD"));
               strXML.append(" pr_id=\"").append(rs2.getInt("PR_ID")).
                       append("\" " + "precioUsar=\"").append(dblPrecioUsar).
                       append("\" " + "precio=\"").append(rs2.getDouble("PP_PRECIO")).
                       append("\" " + "precio_usd=\"").append(rs2.getDouble("PP_PRECIO_USD")).
                       append("\" " + "putilidad=\"").append(rs2.getDouble("PP_PUTILIDAD")).
                       append("\" " + "puntos=\"").append(rs2.getDouble("PP_PUNTOS")).
                       append("\" " + "negocio=\"").append(rs2.getDouble("PP_NEGOCIO")).
                       append("\" " + "publico=\"").append(rs2.getDouble("PP_PPUBLICO")).
                       append("\" " + "descuento=\"").append(rs2.getDouble("PP_APDESC")).
                       append("\" " + "desc_pto=\"").append(rs2.getDouble("PP_APDESCPTO")).
                       append("\" " + "desc_nego=\"").append(rs2.getDouble("PP_APDESCNEGO")).
                       append("\" " + "lealtad=\"").append(rs2.getDouble("PP_PTOSLEAL")).
                       append("\" " + "lealtadCA = \"").append(rs2.getDouble("PP_PTOSLEALCAM")).
                       append("\" ");
               //Aqui validariamos promociones
               if (bolUsaPromos) {
               }
            }
            rs2.close();
         } catch (SQLException ex) {
            log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRECIOS", oConn);
         }
         //Si no existe el precio lo generamos en ceros
         if (bolExiste == false) {
            strXML.append(" pr_id=\"0\" precio=\"0.0\" descuento=\"0.0\" lealtad=\"0.0\" lealtadCA = \"0.0\" "
                    + "publico= \"0.0\" desc_pto=\"1\" desc_nego=\"1\" puntos=\"0\" negocio=\"0\" precioUsar=\"0\" precio_usd=\"0\"  putilidad=\"0\" ");
         }
         strXML.append(" />");
      }

      strXML.append("</Precios>");
      return strXML.toString();
   }

   /**
    * Nos regresa el xml de los precios del producto indicado y una lista de
    * precios en particular
    * <Precios>
    * <Precio num="1" pr_id="0" precio="0.0" descuento="0.0" lealtad="0.0"
    * lealtadCA = "0.0">
    * <Precios>
    *
    * @param strPr_id Es el codigo del producto, en caso de ser cero nos trae
    * los valores en ceros(excepto la lista)
    * @param oConn Es la conexion a la base de datos
    * @param bolUsaPromos Indicamos si validamos las promociones
    * @param dblCantidad Es la cantidad de articulos solicitados
    * @param intLprecios Es la lista de precios
    * @param intMonedaVta Es la moneda con la que se hace la venta
    * @param intTipoCambio Es el tipo de cambio del cliente
    * @return Regresa una cadena con el xml
    */
   public String getXML(String strPr_id, Conexion oConn, boolean bolUsaPromos,
           double dblCantidad, int intLprecios, int intMonedaVta, int intTipoCambio) {

      StringBuilder strXML = new StringBuilder(strXMLHEAD);

      strXML.append(getXMLPrec(strPr_id, oConn, bolUsaPromos,
              dblCantidad, intLprecios, intMonedaVta, intTipoCambio));

      return strXML.toString();
   }

   /**
    * Obtiene el precio a usar para el producto
    *
    * @param oConn Es la conexion
    * @param bolUsaPromos Indica si usaremos promociones
    * @param dblCantidad Es la cantidad pedida
    * @param intLprecios Es la lista de precios
    * @param intMonedaVta Es el id de la moneda de venta
    * @param intTipoCambio Es el id del tipo de cambio
    * @param dblPrecio Es el precio en MN
    * @param dblPrecioUSD Es el precio en USD
    * @return Regresa el precio de acuerdo a la moneda
    */
   public double GetPrecioUsar(Conexion oConn, boolean bolUsaPromos,
           double dblCantidad, int intLprecios, int intMonedaVta, int intTipoCambio,
           double dblPrecio, double dblPrecioUSD) {
      double dblPrecioUsar = 0;

      //Casos de monedas
      //Moneda base
      if (intMonedaVta == 1) {
         //Caso 1 existe el precio en la moneda base
         if (dblPrecio != 0) {
            dblPrecioUsar = dblPrecio;
         }
         //Caso 2 no existe el precio en la moneda base y si en la USD
         if (dblPrecio == 0 && dblPrecioUSD != 0) {
            //Hay que hacer la conversion a la moneda base
            dblPrecioUsar = dblPrecioUSD;
            Monedas moneda = new Monedas(oConn);
            double dblFactor = moneda.GetFactorConversion(intTipoCambio, 2, intMonedaVta);
            log.debug("Caso 1:");
            log.debug("dblPrecioUsar USD:" + dblPrecioUsar);
            log.debug("dblFactor:" + dblFactor);
            if (dblFactor != 0) {
               dblPrecioUsar = dblPrecioUsar * dblFactor;
            } else {
               AvisoEmergencia(oConn, "AVISO: ERROR AL CONVERTIR DE DOLARES A PESOS, FALTA FACTOR DE CONVERSION");
            }
         }
      }
      //Moneda USD
      if (intMonedaVta == 2) {
         //Caso 1 existe el precio en la moneda base
         if (dblPrecioUSD == 0 && dblPrecio != 0) {
            dblPrecioUsar = dblPrecio;
            //Hay que hacer la conversion a la moneda base
            Monedas moneda = new Monedas(oConn);
            double dblFactor = moneda.GetFactorConversion(intTipoCambio, 1, intMonedaVta);
            log.debug("Caso 2:");
            log.debug("dblPrecioUsar USD:" + dblPrecioUsar);
            log.debug("dblFactor:" + dblFactor);
            if (dblFactor != 0) {
               dblPrecioUsar = dblPrecioUsar * dblFactor;
            } else {
               AvisoEmergencia(oConn, "AVISO: ERROR AL CONVERTIR DE PESOS A DOLARES, FALTA FACTOR DE CONVERSION");
            }
         }
         //Caso 2 no existe el precio en la moneda base y si en la USD
         if (dblPrecioUSD != 0) {
            dblPrecioUsar = dblPrecioUSD;
         }
      }
      //Otras monedas
      if (intMonedaVta > 2) {
         Monedas moneda = new Monedas(oConn);
         if (dblPrecioUSD != 0) {
            dblPrecioUsar = dblPrecioUSD;
            double dblFactor = moneda.GetFactorConversion(intTipoCambio, 2, intMonedaVta);
            log.debug("Caso 3:");
            log.debug("dblPrecioUsar USD:" + dblPrecioUsar);
            log.debug("dblFactor:" + dblFactor);
            if (dblFactor == 0) {
               //Si el factor de conversion es ceros
               //si el precio esta en USD pero no tenemos conversion
               //lo convertimos primero en pesos  
               dblFactor = moneda.GetFactorConversion(intTipoCambio, 2, 1);
               log.debug("Caso 3(A):");
               log.debug("dblPrecioUsar USD:" + dblPrecioUsar);
               log.debug("dblFactor:" + dblFactor);
               if (dblFactor != 0) {
                  //conversion en Pesos
                  dblPrecioUsar = dblPrecioUsar * dblFactor;
                  //Ahora hacemos la conversion de pesos a la moneda
                  dblFactor = moneda.GetFactorConversion(intTipoCambio, 1, intMonedaVta);
                  log.debug("Caso 3(B):");
                  log.debug("dblPrecioUsar MN:" + dblPrecioUsar);
                  log.debug("dblFactor:" + dblFactor);
                  if (dblFactor != 0) {
                     dblPrecioUsar = dblPrecioUsar * dblFactor;
                  } else {
                     AvisoEmergencia(oConn, "(1)AVISO: ERROR AL CONVERTIR DE DOLARES A " + intMonedaVta + ", FALTA FACTOR DE CONVERSION");
                  }
               } else {
                  AvisoEmergencia(oConn, "(2)AVISO: ERROR AL CONVERTIR DE DOLARES A PESOS, FALTA FACTOR DE CONVERSION");
               }
            } else {
               dblPrecioUsar = dblPrecioUsar * dblFactor;
            }
         } else {
            dblPrecioUsar = dblPrecio;
            double dblFactor = moneda.GetFactorConversion(intTipoCambio, 1, intMonedaVta);
            log.debug("Caso 4:");
            log.debug("dblPrecioUsar MN:" + dblPrecioUsar);
            log.debug("dblFactor:" + dblFactor);
            if (dblFactor == 0) {
               //Si el factor de conversion es ceros
               //si el precio esta en M.N. pero no tenemos conversion
               //lo convertimos primero en USD  
               dblFactor = moneda.GetFactorConversion(intTipoCambio, 1, 2);
               log.debug("Caso 4(A):");
               log.debug("dblPrecioUsar MN:" + dblPrecioUsar);
               log.debug("dblFactor:" + dblFactor);
               if (dblFactor != 0) {
                  //conversion en USD
                  dblPrecioUsar = dblPrecioUsar * dblFactor;
                  //Ahora hacemos la conversion de USD a la moneda
                  dblFactor = moneda.GetFactorConversion(intTipoCambio, 2, intMonedaVta);
                  log.debug("Caso 4(B):");
                  log.debug("dblPrecioUsar USD:" + dblPrecioUsar);
                  log.debug("dblFactor:" + dblFactor);
                  if (dblFactor != 0) {
                     dblPrecioUsar = dblPrecioUsar * dblFactor;
                  } else {
                     AvisoEmergencia(oConn, "(3)AVISO: ERROR AL CONVERTIR DE PESOS A " + intMonedaVta + ", FALTA FACTOR DE CONVERSION");
                  }
               } else {
                  AvisoEmergencia(oConn, "(4)AVISO: ERROR AL CONVERTIR DE PESOS A DOLARES, FALTA FACTOR DE CONVERSION");
               }

            } else {
               dblPrecioUsar = dblPrecioUsar * dblFactor;
            }
         }
      }
      return dblPrecioUsar;
   }

   /**
    * Generamos mensaje de emergencia por anomalías en el precio de venta
    *
    * @param oConn Es la conexion
    * @param strMsg Es el mensaje de emergencia
    */
   public void AvisoEmergencia(Conexion oConn, String strMsg) {
      Mail mail = new Mail();
      String strLstMail = "";
      //Buscamos mail de usuarios
      String strSql = "select * from usuarios where BOL_MAIL_PRECIOS = 1";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            if (mail.isEmail(rs.getString("EMAIL"))) {
               strLstMail += rs.getString("EMAIL") + ",";
            }
         }
         rs.close();
         if (strLstMail.endsWith(",")) {
            strLstMail = strLstMail.substring(0, strLstMail.length() - 1);
         }
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
      //validamos que hallan puesto el mail
      if (!strLstMail.isEmpty()) {

         //Intentamos mandar el mail
         mail.setBolDepuracion(false);
         mail.getTemplate("PRECIOS_CO", oConn);
         mail.getMensaje();
         mail.setMensaje(strMsg);
         mail.setDestino(strLstMail);
         boolean bol = mail.sendMail();
         if (bol) {
            log.debug("MAIL ENVIADO...");
         } else {
            log.error("Fallo el envio de mail:" + mail.getErrMsg());
         }

      } else {
         log.debug("No hay usuarios asignados a estos mensajes....");
      }
   }

   /**
    * Metodo para cálculo de precios masivo
    *
    * @param oConn
    * @param strCodigo
    * @param intGpoPrec
    * @param bolCambiarPorcUtilidad
    * @param dblPorUtilidad
    * @param intSC_ID
    * @param intEMP_ID
    * @param intPR_ID1 Es el id del producto
    * @return Regresa OK si fue exitosa la operación
    */
   public String CambioPreciosMasivo(Conexion oConn,
           String strCodigo,
           int intGpoPrec,
           boolean bolCambiarPorcUtilidad,
           double dblPorUtilidad,
           int intSC_ID,
           int intEMP_ID,
           int intPR_ID1) {
      String strResp = "OK";
      NumberString numbers = new NumberString();
      //Armamos el filtro de los productos por aplicar el cambio
      String strFiltro = "";
      //Si es por el id del producto solo usamos ese filtro
      if (intPR_ID1 != 0) {
         strFiltro += " PR_ID = " + intPR_ID1;
      } else {
         strFiltro += " EMP_ID = " + intEMP_ID;
         strFiltro += " AND SC_ID = " + intSC_ID;
         if (!strCodigo.isEmpty()) {
            //Si no esta vacio el código
            strFiltro += " AND PR_CODIGO='" + strCodigo + "' ";
         } else {
            if (intGpoPrec != 0) {
               strFiltro += " AND PR_CATEGORIA1 =" + intGpoPrec + " ";
            }
            
         }
      }

      //Obtenemos las listas de precios
      ArrayList<ListaPrecios> lstPrecios = new ArrayList<ListaPrecios>();
      String strSql = "select * from vta_lprecios order by LP_ORDEN";
      ResultSet rs3;
      try {
         rs3 = oConn.runQuery(strSql, true);
         while (rs3.next()) {
            int intLP_ID = rs3.getInt("LP_ID");
            //Validamos si hay que cambiar el porcentaje de utilidad
            double dblPorcUtil = rs3.getDouble("LP_PORC_UTIL");
            ListaPrecios precios = new ListaPrecios();
            precios.setIntNumLista(intLP_ID);
            precios.setDblPorcentaje(dblPorcUtil);
            precios.setStrFormula(rs3.getString("LP_FORMULA"));
            precios.setIntLp_ConvMoneda(rs3.getInt("LP_CONV_MONEDA"));
            precios.setIntLp_RedondeoCalculo(rs3.getInt("LP_REDONDEO_CALCULO"));
            precios.setIntLp_TipoRedondeo(rs3.getInt("LP_TIPO_REDONDEO"));
            lstPrecios.add(precios);
         }
         rs3.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
      //Ejecutamos el query
      strSql = "select PR_ID,PR_COSTOCOMPRA,PR_COSTOREPOSICION,PR_PORC_UTILIDAD"
              + ",PR_FLETE,PR_VALORES,PR_DTA,PR_MADUANAL,PR_DPROVEEDOR,PR_MONEDA_VTA,PR_TCAMBIO_TDA,PR_TCAMBIO_PROV "
              + " from vta_producto where " + strFiltro;
      ResultSet rs2;
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            int intPR_ID = rs2.getInt("PR_ID");
            //Validamos si hay que cambiar el porcentaje de utilidad
            double dblPorcUtil = rs2.getDouble("PR_PORC_UTILIDAD");
            if (bolCambiarPorcUtilidad) {
               dblPorcUtil = dblPorUtilidad;
            }
            //Calculamos nuevos precios
            double dblCostoProm = rs2.getDouble("PR_COSTOREPOSICION");
            double $costo_repo = rs2.getDouble("PR_COSTOREPOSICION");
            double $flete = rs2.getDouble("PR_FLETE");
            double $add_valores = rs2.getDouble("PR_VALORES");
            double $dta = rs2.getDouble("PR_DTA");
            double $manejo_aduanal = rs2.getDouble("PR_MADUANAL");
            double $descuento_prov = rs2.getDouble("PR_DPROVEEDOR");
            double dblTCambioTda = rs2.getDouble("PR_TCAMBIO_TDA");
            double dblTCambioProv = rs2.getDouble("PR_TCAMBIO_PROV");
            double intMonedaProv = 0;
            int intMonedaVta = rs2.getInt("PR_MONEDA_VTA");

            /*CAMBIOS 20130620
             * Hay que obtener el resto de valores para el calculo de precios
             */
            //Query al proveedor para obtener su moneda

            if (dblCostoProm != 0) {
               // <editor-fold defaultstate="collapsed" desc="Iteramos por las listas de precio">
               Iterator<ListaPrecios> it = lstPrecios.iterator();
               while (it.hasNext()) {
                  ListaPrecios lPrecios = it.next();
                  lPrecios.setDblPrecioNvo(0);
                  double $porc_utilidad = 0;

                  // <editor-fold defaultstate="collapsed" desc="Buscamos el porcentaje de utilidad">
                  String strSql3 = "select PP_PUTILIDAD from vta_prodprecios where PR_ID = " + intPR_ID + " AND LP_ID = " + lPrecios.getIntNumLista();
                  rs3 = oConn.runQuery(strSql3, true);
                  while (rs3.next()) {
                     $porc_utilidad = rs3.getDouble("PP_PUTILIDAD");
                  }
                  rs3.close();
                  // </editor-fold>

                  double dblNvoPrec = 0;

                  // <editor-fold defaultstate="collapsed" desc="Sustituimos las variables en la formula">
                  String strFormula = "dblNvoPrec = " + new String(lPrecios.getStrFormula() + ";");
                  log.debug("strFormula:" + strFormula);

                  strFormula = strFormula.replace("$costo_repo", String.valueOf($costo_repo));
                  strFormula = strFormula.replace("$flete", String.valueOf($flete));
                  strFormula = strFormula.replace("$add_valores", String.valueOf($add_valores));
                  strFormula = strFormula.replace("$dta", String.valueOf($dta));
                  strFormula = strFormula.replace("$manejo_aduanal", String.valueOf($manejo_aduanal));
                  strFormula = strFormula.replace("$descuento_prov", String.valueOf($descuento_prov));
                  strFormula = strFormula.replace("$camb_proov", String.valueOf(dblTCambioProv));

                  // <editor-fold defaultstate="collapsed" desc="Buscamos los precios anteriores">
                  Iterator<ListaPrecios> it2 = lstPrecios.iterator();
                  while (it2.hasNext()) {
                     ListaPrecios lPrecios2 = it2.next();
                     double $precioX = lPrecios2.getDblPrecioNvo();
                     if (lPrecios.getIntLp_RedondeoCalculo() == 1) {
                        //Aqui redondeamos
                        if (lPrecios.getIntLp_TipoRedondeo() == 1) {
                           $precioX = numbers.RedondeoDolares($precioX);
                        }
                        if (lPrecios.getIntLp_TipoRedondeo() == 2) {
                           $precioX = numbers.RedondeoDolares($precioX);
                        }
                     }
                     //Faltan validaciones de la moneda
                     strFormula = strFormula.replace("$precio" + lPrecios2.getIntNumLista(), String.valueOf($precioX));
                  }
                  // </editor-fold>
                  strFormula = strFormula.replace("$porc_utilidad", String.valueOf($porc_utilidad));
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="Ejecutamos el script con eval">
                  //Objetos para la ejecucion de scripting usamos el SCRIPT default
                  ScriptEngineManager mgr = new ScriptEngineManager();
                  ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
                  //Anadimos objetos para que los tome en cuenta el scripting
                  jsEngine.put("dblNvoPrec", dblNvoPrec);
                     log.debug("**Program to execute**" + strFormula);
                  try {
                     jsEngine.eval(strFormula + ";");
                  } catch (ScriptException ex) {
                     log.error("Error al calcular el precio " + ex.getMessage() + " " + ex.getColumnNumber() + " " + ex.getLineNumber());
                  }
                  //Recuperamos el valor booleano que se halla parseado
                  String strStringPrecio = jsEngine.get("dblNvoPrec").toString();
                  try {
                     dblNvoPrec = Double.valueOf(strStringPrecio);
                  } catch (NumberFormatException ex) {
                     log.error("Error " + ex.toString());
                  }
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="Evaluamos las monedas para el tipo de cambio">
                  if (lPrecios.getIntNumLista() == 1) {
                     dblNvoPrec = dblNvoPrec * dblTCambioTda;
                  } else {
                     dblNvoPrec = dblNvoPrec * dblTCambioProv;
                  }
                  // </editor-fold>
                  //Asignamos el nuevo valor para el calculo de los otros precios
                  lPrecios.setDblPrecioNvo(dblNvoPrec);
                  log.debug(lPrecios.getIntNumLista() + " dblNvoPrec:" + dblNvoPrec);
                  //Aqui aplicaria el redondeo

               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Iteramos por las listas de precio para actualizar el precio">
               it = lstPrecios.iterator();
               while (it.hasNext()) {
                  ListaPrecios lPrecios = it.next();

                  double dblPrecioAplica = lPrecios.getDblPrecioNvo();
                  //Redondeo
                  log.debug(lPrecios.getIntNumLista() + " " + lPrecios.getIntLp_TipoRedondeo() + "  dblNvoPrec SIN redondeo:" + dblPrecioAplica);
                  if (lPrecios.getIntLp_TipoRedondeo() == 1) {
                     dblPrecioAplica = numbers.RedondeoPesos(dblPrecioAplica);
                  }
                  if (lPrecios.getIntLp_TipoRedondeo() == 2) {
                     dblPrecioAplica = numbers.RedondeoDolares(dblPrecioAplica);
                  }
                  log.debug("  dblNvoPrec con redondeo:" + dblPrecioAplica);
                  //Actualizamos la informacion
                  String strUpdatePrec = null;
                  if(intMonedaVta == 1 || lPrecios.getIntLp_ConvMoneda() == 0){
                     strUpdatePrec = "UPDATE vta_prodprecios set PP_PRECIO = " + dblPrecioAplica + ",PP_PRECIO_USD=0 "
                          + "   where PR_ID = " + intPR_ID + " and LP_ID = " + lPrecios.getIntNumLista();
                  }  else{
                     strUpdatePrec = "UPDATE vta_prodprecios set PP_PRECIO_USD = " + dblPrecioAplica + ",PP_PRECIO=0 "
                          + "   where PR_ID = " + intPR_ID + " and LP_ID = " + lPrecios.getIntNumLista();                     
                  }
                  oConn.runQueryLMD(strUpdatePrec);
                  lPrecios.setDblPrecioNvo(0);
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Si se cambia el porcentaje actualizamos el producto">
               if (bolCambiarPorcUtilidad) {
                  String strUpdate = "UPDATE vta_producto SET PR_PORC_UTILIDAD = " + dblPorcUtil
                          + " WHERE PR_ID =  " + intPR_ID;
                  //oConn.runQueryLMD(strUpdate);
               }
               // </editor-fold>
            }
         }
         rs2.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         strResp = ex.getMessage();
      }



      return strResp;
   }

   /**
    * Regresa en un xml las formulas de las listas de precio
    *
    * @param oConn Es la conexion a la base de datos
    * @return regresa un xml con las formulas para el calculo de precios
    * <formulas>
    * <formula lp_id="1" sentencia="$costo_reposicion * $utilidad" />
    * <formula lp_id="2" sentencia="$costo_reposicion * $utilidad" />
    * </formulas>
    * En la sentencia o formula se siguen las siguientes variables: $costo_repo
    * Es el costo de reposicion del productos\n $flete Es el % del gasto del
    * flete\n $add_valores Es el % de valores adicionales por incrementar\n $dta
    * % de gasto de dta\n $manejo_aduanal % de gasto de manejo aduanal\n
    * $descuento_prov % de descuento del proveedor\n $porc_utilidad % de
    * utilidad a ganar al producto\n
    */
   public String RegresaFormulas(Conexion oConn) {
      //Clase para sustituir caracteres no permitidos en el xml
      comSIWeb.Utilerias.UtilXml utilXml = new comSIWeb.Utilerias.UtilXml();
      //Cadena a devolver
      StringBuilder strXML = new StringBuilder(strXMLHEAD);
      strXML.append("<formulas>");
      String strSql = "select LP_ID,LP_FORMULA,LP_CONV_MONEDA,LP_TIPO_REDONDEO,LP_REDONDEO_CALCULO "
              + " from vta_lprecios ORDER BY LP_ORDEN";
      ResultSet rs2;
      try {
         rs2 = oConn.runQuery(strSql, true);
         while (rs2.next()) {
            strXML.append("<formula lp_id=\"").append(rs2.getInt("LP_ID")).append("\" ");
            strXML.append("sentencia = \"").append(utilXml.Sustituye(rs2.getString("LP_FORMULA"))).append("\" ");
            strXML.append("redondeo = \"").append(rs2.getInt("LP_TIPO_REDONDEO")).append("\" ");
            strXML.append("redondeo_calculo = \"").append(rs2.getInt("LP_REDONDEO_CALCULO")).append("\" ");
            strXML.append("conversion = \"").append(rs2.getInt("LP_CONV_MONEDA")).append("\" />");
         }
         rs2.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
      strXML.append("</formulas>");
      return strXML.toString();
   }
   // </editor-fold>
}
