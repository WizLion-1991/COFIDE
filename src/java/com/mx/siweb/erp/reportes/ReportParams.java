/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import Tablas.repo_master;
import Tablas.repo_params;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase se encarga de administrar los parametros de los reportes
 *
 * @author ZeusGalindo
 */
public class ReportParams {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ReportParams.class.getName());
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ReportParams(Conexion oConn) {
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Agrega un nuevo parametro
    *
    * @param r Es el objeto parametros
    * @return Regresa OK si fue exitoso
    */
   public String Agrega(repo_params r) {
       String salida=r.Agrega(this.oConn);
       System.out.println("salida: "+salida);
      return salida;
   }

   /**
    * Modifica un parametro
    *
    * @param r Es el objeto parametros
    * @return Regresa OK si fue exitoso
    */
   public String Modifica(repo_params r) {
      return r.Modifica(this.oConn);
   }

   /**
    * Borra un parametro
    *
    * @param r Es el objeto parametros
    * @return Regresa OK si fue exitoso
    */
   public String Borra(repo_params r) {
      return r.Borra(this.oConn);
   }

   /**
    * Consulta los parametros
    *
    * @param strIdRepo Es el id del reporte
    * @return Regresa EL XML con los parametros del reporte
    */
   public String ConsultaXML(String strIdRepo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      strXML.append("<rows>");
      strXML.append("<page>1</page>");
      strXML.append("<total>1</total>");
      strXML.append("<records>1</records>");
      //consultamos los parametros
      String strSql = "select * from repo_params where REP_ID = " + strIdRepo;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<row id=\"").append(rs.getInt("REPP_ID")).append("\">");
            strXML.append("<cell>").append(rs.getString("REPP_ID")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REP_ID")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_NOMBRE")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_VARIABLE")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_TIPO")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_DATO")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_TABLAEXT")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_ENVIO")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_MOSTRAR")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_PRE")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_POST")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("REPP_DEFAULT")).append("</cell>");
            strXML.append("</row>");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      strXML.append("</rows>");
      return strXML.toString();
   }

   /**
    * Obtiene los datos de un parametro
    *
    * @param intKeyRepo Es el id del parametro
    * @return Regresa un cadena con el xml del parametro
    */
   public String GetData(int intKeyRepo) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

      repo_params r = new repo_params();
      r.ObtenDatos(intKeyRepo, oConn);

      strXML.append("<parametros>");
      strXML.append("<parametro ");
      strXML.append(" REPP_ID=\"").append(r.getFieldInt("REPP_ID")).append("\" ");
      strXML.append(" REPP_NOMBRE=\"").append(r.getFieldString("REPP_NOMBRE")).append("\" ");
      strXML.append(" REPP_VARIABLE=\"").append(r.getFieldString("REPP_VARIABLE")).append("\" ");
      strXML.append(" REPP_TIPO=\"").append(r.getFieldString("REPP_TIPO")).append("\" ");
      strXML.append(" REPP_DATO=\"").append(r.getFieldString("REPP_DATO")).append("\" ");
      strXML.append(" REPP_TABLAEXT=\"").append(r.getFieldString("REPP_TABLAEXT")).append("\" ");
      strXML.append(" REPP_ENVIO=\"").append(r.getFieldString("REPP_ENVIO")).append("\" ");
      strXML.append(" REPP_MOSTRAR=\"").append(r.getFieldString("REPP_MOSTRAR")).append("\" ");
      strXML.append(" REPP_POST=\"").append(r.getFieldString("REPP_POST")).append("\" ");
      strXML.append(" REPP_PRE=\"").append(r.getFieldString("REPP_PRE")).append("\" ");
      strXML.append(" REPP_DEFAULT=\"").append(r.getFieldString("REPP_DEFAULT")).append("\" ");
      strXML.append("/>");
      strXML.append("</parametros>");
      return strXML.toString();
   }

   /**
    * Obtiene los datos de un reporte
    *
    * @param intKeyRepo Es el id del parametro
    * @return Regresa un cadena con el xml del parametro
    */
   public String GetDataRepo(int intIdRepo, int intIdUser) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");

      repo_master rM = new repo_master();
      rM.ObtenDatos(intIdRepo, oConn);

      repo_params r = new repo_params();
      ArrayList<TableMaster> lstParams = r.ObtenDatosVarios(" REP_ID = " + intIdRepo, oConn);

      strXML.append("<reportes>");
      strXML.append("<reporte ");
      strXML.append(" REP_ID=\"").append(rM.getFieldInt("REP_ID")).append("\" ");
      strXML.append(" REP_NOMBRE=\"").append(rM.getFieldInt("REP_NOMBRE")).append("\" ");
      strXML.append(" REP_PERMISO=\"").append(rM.getFieldInt("REP_PERMISO")).append("\" ");
      strXML.append(" REP_SECCION=\"").append(rM.getFieldInt("REP_SECCION")).append("\" ");
      strXML.append(" REP_ORDEN=\"").append(rM.getFieldInt("REP_ORDEN")).append("\" ");
      strXML.append(" REP_FZA_VTAS=\"").append(rM.getFieldInt("REP_FZA_VTAS")).append("\" ");
      strXML.append(" REP_ACTIVO=\"").append(rM.getFieldInt("REP_ACTIVO")).append("\" ");
      strXML.append(" REP_XLS=\"").append(rM.getFieldInt("REP_XLS")).append("\" ");
      strXML.append(" REP_PDF=\"").append(rM.getFieldInt("REP_PDF")).append("\" ");
      strXML.append(" REP_TXT=\"").append(rM.getFieldInt("REP_TXT")).append("\" ");
      strXML.append(" REP_HTML=\"").append(rM.getFieldInt("REP_HTML")).append("\" ");
      strXML.append(" REP_DESCRIPCION=\"").append(rM.getFieldInt("REP_DESCRIPCION")).append("\" ");
      strXML.append(" REP_JRXML=\"").append(rM.getFieldInt("REP_JRXML")).append("\" ");
      strXML.append(" /> ");
      strXML.append("<parametros>");
      Iterator<TableMaster> it = lstParams.iterator();
      while (it.hasNext()) {
         TableMaster tbn = it.next();
         strXML.append("<parametro ");
         strXML.append(" REPP_ID=\"").append(tbn.getFieldInt("REPP_ID")).append("\" ");
         strXML.append(" REPP_NOMBRE=\"").append(tbn.getFieldString("REPP_NOMBRE")).append("\" ");
         strXML.append(" REPP_VARIABLE=\"").append(tbn.getFieldString("REPP_VARIABLE")).append("\" ");
         strXML.append(" REPP_TIPO=\"").append(tbn.getFieldString("REPP_TIPO")).append("\" ");
         strXML.append(" REPP_DATO=\"").append(tbn.getFieldString("REPP_DATO")).append("\" ");
         strXML.append(" REPP_TABLAEXT=\"").append(tbn.getFieldString("REPP_TABLAEXT")).append("\" ");
         strXML.append(" REPP_ENVIO=\"").append(tbn.getFieldString("REPP_ENVIO")).append("\" ");
         strXML.append(" REPP_MOSTRAR=\"").append(tbn.getFieldString("REPP_MOSTRAR")).append("\" ");
         strXML.append(" REPP_POST=\"").append(tbn.getFieldString("REPP_POST")).append("\" ");
         strXML.append(" REPP_PRE=\"").append(r.getFieldString("REPP_PRE")).append("\" ");
         strXML.append(" REPP_DEFAULT=\"").append(r.getFieldString("REPP_DEFAULT")).append("\" ");         
         strXML.append("/>");
      }

      strXML.append("</parametros>");
      strXML.append("</reportes>");
      return strXML.toString();
   }
   // </editor-fold>
}
