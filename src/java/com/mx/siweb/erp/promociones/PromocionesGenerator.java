/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.promociones;

import Tablas.vta_promo_variables;
import com.mx.siweb.erp.promociones.entidades.Promocion;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Esta clase realiza las operaciones de generación de promociones
 * @author aleph_79
 */
public class PromocionesGenerator {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private ArrayList<Promocion> lstPromociones;
   private ArrayList<vta_promo_variables> lstVariables;
   Fechas fecha = null;
   private static final Logger log = LogManager.getLogger(PromocionesGenerator.class.getName());
   private String strPeriodoActual;
   private int intMPE_ID;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public ArrayList<Promocion> getLstPromociones() {
      return lstPromociones;
   }

   public void setLstPromociones(ArrayList<Promocion> lstPromociones) {
      this.lstPromociones = lstPromociones;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public PromocionesGenerator(Conexion oConn) {
      this.oConn = oConn;
      this.lstPromociones = new ArrayList<Promocion>();
      this.lstVariables = new ArrayList<vta_promo_variables>();
      this.fecha = new Fechas();
      strPeriodoActual = "";
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Carga las promociones de la fecha actual
    */
   public void cargaPromociones() {
      cargaPromociones(fecha.getFechaActual());
   }

   /**
    * Carga las variables de las promociones
    */
   public void cargaVariables() {
      //Cargamos las variables
      String strSqlVar = "SELECT PVAR_ID,PVAR_VARIABLE FROM vta_promo_variables";
      try {
         ResultSet rs = this.oConn.runQuery(strSqlVar, true);
         while (rs.next()) {
            vta_promo_variables variable = new vta_promo_variables();
            variable.ObtenDatos(rs.getInt("PVAR_ID"), oConn);
            this.lstVariables.add(variable);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
   }

   /**
    * Carga las promociones de  una fecha dada
    * @param strFecha Es la fecha de la venta
    */
   public void cargaPromociones(String strFecha) {
      cargaVariables();
      //Consulta de promociones
      String strSql = "select PROM_ID FROM vta_promociones WHERE PROM_ACTIVO = 1  "
              + " AND '" + strFecha + "' >= PROM_FECHA_INI "
              + " AND '" + strFecha + "' <= PROM_FECHA_FIN ";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intPromoId = rs.getInt("PROM_ID");
            Promocion promo = new Promocion(oConn, intPromoId);
            promo.cargaPromocion(this.lstVariables);
            this.lstPromociones.add(promo);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
   }

   /**
    * Regresa un xml con todas las promociones
    * @return Regresa el xml con todas las promociones activas
    */
   public String getXml() {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<promociones>\n");
      for (Promocion promo : this.lstPromociones) {
         strXML.append(promo.getXML());
      }
      strXML.append("</promociones>\n");
      return strXML.toString();
   }
   
   // <editor-fold defaultstate="collapsed" desc="VARIABLE DEL CLIENTE">

   /**
    * Calcula las variables del cliente
    * @param strCT_ID Es el id del cliente
    * @param strSC_ID Es el id de la sucursal
    * @param strLstVarsId Es la lista de id de variables por calcular
    * @param strLstVars Es la lista de nombres de variables por calcular
    * @param strLstVarsPromoId Es la lista de id de promociones
    * @return Regresa un xml con las variables y su valor
    */
   public String calculaVarsCte(String strCT_ID, String strSC_ID, String strLstVarsId, String strLstVars, String strLstVarsPromoId) {
      //Inicializamos variables
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      TableMaster tbnCte = null;
      UtilXml utilXML = new UtilXml();
      cargaVariables();
      //Generamos arreglos de cadenas
      String[] lstVarsId = strLstVarsId.split(",");
      String[] lstVars = strLstVars.split(",");
      String[] lstVarsPromoId = strLstVarsPromoId.split(",");
      log.debug("variables por calcular....");
      //Iteramos para ver si necesitamos datos del cliente
      boolean bolDatosCte = false;
      for (int i = 0; i < lstVars.length; i++) {
         if (lstVars[i].startsWith("$clienteAtributos") || lstVars[i].startsWith("$cumpleClas2Cte")) {
            bolDatosCte = true;
         }
      }
      //Si solicitan datos del cliente los buscamos
      if (bolDatosCte) {
         tbnCte = getDataCte(strCT_ID);
      }
      //Variables
      strXML.append("<variables>\n");
      //Calculamos las variables
      for (int i = 0; i < lstVars.length; i++) {
         log.debug(lstVarsId[i] + " " + lstVars[i] + " " + lstVarsPromoId[i]);
         //Obtenemos el valor de la variable
         String strValor = parseVars(lstVarsId[i], lstVars[i], strCT_ID, strSC_ID, lstVarsPromoId[i], tbnCte);
         strXML.append("<variable ");
         strXML.append(" promo_id=\"").append(lstVarsPromoId[i]).
                 append("\" var_id=\"").append(lstVarsId[i]).append("\" var_nom_var=\"").
                 append(utilXML.Sustituye(lstVars[i])).append("\" var_valor=\"").
                 append(utilXML.Sustituye(strValor)).append("\" ");
         strXML.append("/>\n");
      }
      strXML.append("</variables>\n");
      return strXML.toString();
   }

   /***Obtiene los campos del cliente*/
   private TableMaster getDataCte(String strIdCte) {
      int intCT_ID = 0;
      try {
         intCT_ID = Integer.valueOf(strIdCte);
      } catch (NumberFormatException ex) {
         log.error("Al convertir id de cliente a numerico: " + ex.getMessage());
      }
      //Cargamos entidad por programacion con todos los atributos del cliente
      TableMaster tbn = new TableMaster("vta_cliente", "CT_ID", "", "");
      tbn.ObtenEstructura(oConn);
      tbn.ObtenDatos(intCT_ID, oConn);
      return tbn;
   }


   /**Parsea las variables*/
   private String parseVars(String strIdVar, String strNomVar, String strCT_ID, String strSC_ID, String strPromoId, TableMaster tbnCte) {
      String strValor = null;
      // <editor-fold defaultstate="collapsed" desc="Variables de fecha">
      //Fecha actual
      if (strNomVar.equals("$Hoy")) {
         strValor = fecha.getFechaActual();
         return strValor;
      }
      //Año actual
      if (strNomVar.equals("$Mes")) {
         strValor = fecha.getMesActual() + "";
         return strValor;
      }
      //Mes actual
      if (strNomVar.equals("$Anio")) {
         strValor = fecha.getAnioActual() + "";
         return strValor;
      }
      //Mes actual
      if (strNomVar.equals("$Semana")) {
         strValor = fecha.getDayWeek() + "";
         return strValor;
      }
      if (strNomVar.equals("$Hora")) {
         strValor = fecha.getHoraActual() + "";
         return strValor;
      }
      if (strNomVar.equals("$cliente")) {
         strValor = strCT_ID;
         return strValor;
      }
      if (strNomVar.equals("$sucursal")) {
         strValor = strSC_ID;
         return strValor;
      }
      if (strNomVar.contains("$periodoId")) {
         getPeriodoActual();
         strValor = this.intMPE_ID + "";
         return strValor;
      }
      if (strNomVar.equals("$periodo")) {
         getPeriodoActual();
         strValor = this.strPeriodoActual;
         return strValor;
      }
      // </editor-fold>
      //Campos del cliente
      if (tbnCte != null) {
         // <editor-fold defaultstate="collapsed" desc="Clasificaciones">
         if (strNomVar.startsWith("$cumpleClas")) {
            strValor = "false";
            strNomVar = strNomVar.replace("$cumpleClas", "");
            // <editor-fold defaultstate="collapsed" desc="Clasifica 1">
            if (strNomVar.startsWith("1Cte(")) {
               strNomVar = strNomVar.replace("1Cte(", "");
               strNomVar = strNomVar.replace(")", "");
               try {
                  int intParam1 = Integer.valueOf(strNomVar);
                  log.debug("Categoria de cliente 1 " + tbnCte.getFieldInt("CT_CATEGORIA1"));
                  if (tbnCte.getFieldInt("CT_CATEGORIA1") == intParam1) {
                     strValor = "true";
                  }
               } catch (NumberFormatException ex) {
                  log.error("Error al convertir clasificacion de clientes  "
                          + ex.getMessage() + " (Variable " + strIdVar
                          + ")(Promocion " + strPromoId + ")");
               }
               return strValor;
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Clasifica 2">
            if (strNomVar.startsWith("2Cte(")) {
               strNomVar = strNomVar.replace("2Cte(", "");
               strNomVar = strNomVar.replace(")", "");
               try {
                  int intParam1 = Integer.valueOf(strNomVar);
                  if (tbnCte.getFieldInt("CT_CATEGORIA2") == intParam1) {
                     strValor = "true";
                  }
               } catch (NumberFormatException ex) {
                  log.error("Error al convertir clasificacion de clientes  "
                          + ex.getMessage() + " (Variable " + strIdVar
                          + ")(Promocion " + strPromoId + ")");
               }
               return strValor;
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Clasifica 3">
            if (strNomVar.startsWith("3Cte(")) {
               strNomVar = strNomVar.replace("3Cte(", "");
               strNomVar = strNomVar.replace(")", "");
               try {
                  int intParam1 = Integer.valueOf(strNomVar);
                  if (tbnCte.getFieldInt("CT_CATEGORIA3") == intParam1) {
                     strValor = "true";
                  }
               } catch (NumberFormatException ex) {
                  log.error("Error al convertir clasificacion de clientes  "
                          + ex.getMessage() + " (Variable " + strIdVar
                          + ")(Promocion " + strPromoId + ")");
               }
               return strValor;
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Clasifica 4">
            if (strNomVar.startsWith("4Cte(")) {
               strNomVar = strNomVar.replace("4Cte(", "");
               strNomVar = strNomVar.replace(")", "");
               try {
                  int intParam1 = Integer.valueOf(strNomVar);
                  if (tbnCte.getFieldInt("CT_CATEGORIA4") == intParam1) {
                     strValor = "true";
                  }
               } catch (NumberFormatException ex) {
                  log.error("Error al convertir clasificacion de clientes  "
                          + ex.getMessage() + " (Variable " + strIdVar
                          + ")(Promocion " + strPromoId + ")");
               }
               return strValor;
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Clasifica 5">
            if (strNomVar.startsWith("5Cte(")) {
               strNomVar = strNomVar.replace("5Cte(", "");
               strNomVar = strNomVar.replace(")", "");
               try {
                  int intParam1 = Integer.valueOf(strNomVar);
                  if (tbnCte.getFieldInt("CT_CATEGORIA5") == intParam1) {
                     strValor = "true";
                  }
               } catch (NumberFormatException ex) {
                  log.error("Error al convertir clasificacion de clientes  "
                          + ex.getMessage() + " (Variable " + strIdVar
                          + ")(Promocion " + strPromoId + ")");
               }
               return strValor;
            }
            // </editor-fold>
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Atributos del cliente">
         if (strNomVar.startsWith("$clienteAtributos")) {
            strValor = this.getAtributosCte(strIdVar, strNomVar, strCT_ID, strSC_ID, strPromoId, tbnCte);
            return strValor;
         }
         // </editor-fold>
      }
      //Consultas SQL
      strValor = this.consultasSQL(strIdVar, strNomVar, strCT_ID,"0", strSC_ID, strPromoId, tbnCte);
      if (strValor != null) {
         return strValor;
      }
      //No se encontro la variable
      log.error("La variable no se encontro " + strNomVar + ", revise la promocion..." + strPromoId);
      strValor = "";
      return strValor;
   }

   /**
    * Regresa atributos del cliente
    * @param strIdVar Es el id de la variable
    * @param strNomVar Es el nombre de la variable
    * @param strCT_ID Es el id del cliente
    * @param strSC_ID Es el id de la sucursal
    * @param strPromoId Es el id de la promocion
    * @param tbnCte Son los atributos del cliente
    * @return Regresa el atributo del producto requerido
    */
   public String getAtributosCte(String strIdVar, String strNomVar, String strCT_ID, String strSC_ID, String strPromoId, TableMaster tbnCte) {
      String strValor = "";
      strNomVar = strNomVar.replace("$clienteAtributos", "");
      // <editor-fold defaultstate="collapsed" desc="Tipo Entero">
      if (strNomVar.startsWith("Entero(")) {
         strNomVar = strNomVar.replace("Entero(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnCte.getFieldInt(strNomVar) + "";
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tipo texto">
      if (strNomVar.startsWith("Texto(")) {
         strNomVar = strNomVar.replace("Texto(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnCte.getFieldString(strNomVar);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tipo Fecha">
      if (strNomVar.startsWith("Fecha(")) {
         strNomVar = strNomVar.replace("Fecha(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnCte.getFieldString(strNomVar);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tipo Decimal">
      if (strNomVar.startsWith("Decimal(")) {
         strNomVar = strNomVar.replace("Decimal(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnCte.getFieldDouble(strNomVar) + "";
      }
      // </editor-fold>
      log.debug("Atributo:" + strNomVar + " " + strValor);
      return strValor;
   }
   /**Genera una consulta sql*/
   private String consultasSQL(String strIdVar, String strNomVar, String strCT_ID,String strPR_ID, String strSC_ID, String strPromoId, TableMaster tbnCte) {
      String strValorSql = null;
      int intVar_id = 0;
      try {
         intVar_id = Integer.valueOf(strIdVar);
      } catch (NumberFormatException ex) {
         log.error("Error al convertir el id de la promocion en sql " + strIdVar + " " + ex.getMessage());
      }
      //Evaluamos si la promocion es con una consulta SQL
      for (vta_promo_variables variable : this.lstVariables) {
         //variable.getFieldPar()
         if (variable.getFieldInt("PVAR_ID") == intVar_id) {
            if (variable.getFieldInt("PVAR_TIPO") == 1) {
               String strSqlVar = variable.getFieldString("PVAR_SQL");
               if (strSqlVar != null) {
                  if (!strSqlVar.isEmpty()) {
                     //Reemplazamos comodines
                     if (strSqlVar.contains("$cliente")) {
                        strSqlVar = strSqlVar.replace("$cliente", strCT_ID);
                     }
                     if (strSqlVar.contains("$producto")) {
                        strSqlVar = strSqlVar.replace("$producto", strPR_ID);
                     }
                     if (strSqlVar.contains("$sucursal")) {
                        strSqlVar = strSqlVar.replace("$sucursal", strSC_ID);
                     }
                     if (strSqlVar.contains("$periodoId")) {
                        getPeriodoActual();
                        strSqlVar = strSqlVar.replace("$periodoId", this.intMPE_ID + "");
                     }
                     if (strSqlVar.contains("$periodo")) {
                        getPeriodoActual();
                        strSqlVar = strSqlVar.replace("$periodo", this.strPeriodoActual);
                     }
                     if (strSqlVar.contains("$hoy")) {
                        strSqlVar = strSqlVar.replace("$hoy", this.fecha.getFechaActual());
                     }
                     if (strSqlVar.contains("$clienteAtributos")) {
                        int index = strSqlVar.indexOf("$clienteAtributos");
                        while (index >= 0) {
                           int indexFin = strSqlVar.indexOf(")", index + 1);
                           String strFuncNva = strSqlVar.substring(index, indexFin + 1);
                           log.debug("Variable " + strFuncNva + " encontrada....");
                           String strValor2 = this.getAtributosCte(strIdVar, strFuncNva, strCT_ID, strSC_ID, strPromoId, tbnCte);
                           strSqlVar = strSqlVar.replace(strFuncNva, strValor2);
                           //Buscamos en el resto de la cadena
                           index = strSqlVar.indexOf("$clienteAtributos", index + 1);
                        }
                     }
                     //Tipo de dato a obtener
                     String strVarDato = variable.getFieldString("PVAR_DATO");
                     try {
                        //Ejecutamos la consulta
                        ResultSet rsPromo = this.oConn.runQuery(strSqlVar, true);
                        while (rsPromo.next()) {
                           strValorSql = rsPromo.getString(strNomVar);
                           if (strValorSql == null) {
                              strValorSql = "";
                           }
                           if (strVarDato.equals("integer") && strValorSql.isEmpty()) {
                              strValorSql = "0";
                           }
                           if (strVarDato.equals("double") && strValorSql.isEmpty()) {
                              strValorSql = "0.0";
                           }
                        }
                        rsPromo.close();
                     } catch (SQLException ex) {
                        log.error("Error en promocion(" + strPromoId + ") de sentencia sql con la variable(" + strIdVar + ") "
                                + " " + ex.getMessage() + " " + ex.getSQLState());
                     }

                  } else {
                     log.error("Error en la consulta sql, el valor es vacio" + strIdVar + " IdPromocion:" + strPromoId);
                  }
               } else {
                  log.error("Error en la consulta sql, el valor es nulo" + strIdVar + " IdPromocion:" + strPromoId);
               }
            }
         }
      }
      return strValorSql;
   }
   
   // </editor-fold>
   
   /**Obtiene los valores del periodo actual*/
   private void getPeriodoActual() {
      if (this.strPeriodoActual.isEmpty()) {
         String strFechaHoy = this.fecha.getFechaActual();
         String strSql = "select MPE_ID,MPE_ABRV from mlm_periodos where "
                 + "'" + strFechaHoy + "'>= MPE_FECHAINICIAL "
                 + "AND '" + strFechaHoy + "'<= MPE_FECHAFINAL";
         try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
               this.strPeriodoActual = rs.getString("MPE_ABRV");
               this.intMPE_ID = rs.getInt("MPE_ID");
            }
            rs.close();
         } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(PromocionesGenerator.class.getName()).log(Level.SEVERE, null, ex);
         }

      }
   }
   
   // <editor-fold defaultstate="collapsed" desc="VARIABLES DEL PRODUCTO">
   /**
    * Calcula las variables del producto
    * @param strCT_ID Es el id del cliente
    * @param strSC_ID Es el id de la sucursal
    * @param strPR_ID Es el id del producto
    * @param strLstVarsId Es la lista de id de variables por calcular
    * @param strLstVars Es la lista de nombres de variables por calcular
    * @param strLstVarsPromoId Es la lista de id de promociones
    * @return Regresa un xml con las variables y su valor
    */
   public String calculaVarsProd(String strCT_ID, String strSC_ID,String strPR_ID, String strLstVarsId, String strLstVars, String strLstVarsPromoId) {
      //Inicializamos variables
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      TableMaster tbnProd = null;
      UtilXml utilXML = new UtilXml();
      cargaVariables();
      //Generamos arreglos de cadenas
      String[] lstVarsId = strLstVarsId.split(",");
      String[] lstVars = strLstVars.split(",");
      String[] lstVarsPromoId = strLstVarsPromoId.split(",");
      log.debug("variables por calcular....");
      //Iteramos para ver si necesitamos datos del cliente
      boolean bolDatosProd = false;
      for (int i = 0; i < lstVars.length; i++) {
         if (lstVars[i].startsWith("$productoAtributos") ) {
            bolDatosProd = true;
         }
      }
      //Si solicitan datos del cliente los buscamos
      if (bolDatosProd) {
         tbnProd = getDataProd(strPR_ID);
      }
      //Variables
      strXML.append("<variables>\n");
      //Calculamos las variables
      for (int i = 0; i < lstVars.length; i++) {
         log.debug(lstVarsId[i] + " " + lstVars[i] + " " + lstVarsPromoId[i]);
         //Obtenemos el valor de la variable
         String strValor = parseVarsProd(lstVarsId[i], lstVars[i], strCT_ID, strSC_ID,strPR_ID, lstVarsPromoId[i], tbnProd);
         strXML.append("<variable ");
         strXML.append(" promo_id=\"").append(lstVarsPromoId[i]).
                 append("\" var_id=\"").append(lstVarsId[i]).append("\" var_nom_var=\"").
                 append(utilXML.Sustituye(lstVars[i])).append("\" var_valor=\"").
                 append(utilXML.Sustituye(strValor)).append("\" ");
         strXML.append("/>\n");
      }
      strXML.append("</variables>\n");
      return strXML.toString();
   }
   
   /***Obtiene los campos del producto*/
   private TableMaster getDataProd(String strIdProd) {
      int intPR_ID = 0;
      try {
         intPR_ID = Integer.valueOf(strIdProd);
      } catch (NumberFormatException ex) {
         log.error("Al convertir id de producto a numerico: " + ex.getMessage());
      }
      //Cargamos entidad por programacion con todos los atributos del cliente
      TableMaster tbn = new TableMaster("vta_producto", "PR_ID", "", "");
      tbn.ObtenEstructura(oConn);
      tbn.ObtenDatos(intPR_ID, oConn);
      return tbn;
   }
   /**Parsea las variables*/
   private String parseVarsProd(String strIdVar, String strNomVar, String strCT_ID, String strSC_ID,String strPR_ID, String strPromoId, TableMaster tbnProd) {
      String strValor = null;

      //Campos del producto
      if (tbnProd != null) {
         // <editor-fold defaultstate="collapsed" desc="Atributos del cliente">
         if (strNomVar.startsWith("$productoAtributos")) {
            strValor = this.getAtributosProd(strIdVar, strNomVar, strCT_ID, strSC_ID,strPR_ID, strPromoId, tbnProd);
            return strValor;
         }
         // </editor-fold>
      }
      //Consultas SQL
      strValor = this.consultasSQL(strIdVar, strNomVar, strCT_ID,strPR_ID, strSC_ID, strPromoId, tbnProd);
      if (strValor != null) {
         return strValor;
      }
      //No se encontro la variable
      log.error("La variable no se encontro " + strNomVar + ", revise la promocion..." + strPromoId);
      strValor = "";
      return strValor;
   }
 /**
    * Regresa atributos del producto
    * @param strIdVar Es el id de la variable
    * @param strNomVar Es el nombre de la variable
    * @param strCT_ID Es el id del cliente
    * @param strPR_ID  Es el id del producto
    * @param strSC_ID Es el id de la sucursal
    * @param strPromoId Es el id de la promocion
    * @param tbnProd Son los atributos del producto
    * @return Regresa el atributo del producto requerido
    */
   public String getAtributosProd(String strIdVar, String strNomVar, String strCT_ID,String strSC_ID,String strPR_ID, 
           String strPromoId, TableMaster tbnProd) {
      String strValor = "";
      strNomVar = strNomVar.replace("$productoAtributos", "");
      // <editor-fold defaultstate="collapsed" desc="Tipo Entero">
      if (strNomVar.startsWith("Entero(")) {
         strNomVar = strNomVar.replace("Entero(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnProd.getFieldInt(strNomVar) + "";
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tipo texto">
      if (strNomVar.startsWith("Texto(")) {
         strNomVar = strNomVar.replace("Texto(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnProd.getFieldString(strNomVar);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tipo Fecha">
      if (strNomVar.startsWith("Fecha(")) {
         strNomVar = strNomVar.replace("Fecha(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnProd.getFieldString(strNomVar);
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Tipo Decimal">
      if (strNomVar.startsWith("Decimal(")) {
         strNomVar = strNomVar.replace("Decimal(", "");
         strNomVar = strNomVar.replace(")", "");
         strNomVar = strNomVar.replace("\"", "");
         strValor = tbnProd.getFieldDouble(strNomVar) + "";
      }
      // </editor-fold>
      log.debug("Atributo:" + strNomVar + " " + strValor);
      return strValor;
   }
   // </editor-fold>
   
   // </editor-fold>
}
