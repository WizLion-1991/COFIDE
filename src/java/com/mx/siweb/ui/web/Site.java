/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Representa los parametros del sitio por mostrarse
 * @author aleph_79
 */
public class Site {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strLanguage;
   private String strDirection;
   private String nomSite;
   private String urlSite;
   private String CharSet;
   private String strAbrev;
   private Conexion oConn;
   private int intEMP_ID;
   private int intMON_ID;
   private int intSC_ID;
   private int intLPrecios;
   private int intCT_ID = 0;
   private static final Logger log = LogManager.getLogger(Site.class.getName());
   
   public String getStrDirection() {
      return strDirection;
   }

   public void setStrDirection(String strDirection) {
      this.strDirection = strDirection;
   }

   public String getStrLanguage() {
      return strLanguage;
   }

   public void setStrLanguage(String strLanguage) {
      this.strLanguage = strLanguage;
   }

   public String getNomSite() {
      return nomSite;
   }

   public void setNomSite(String nomSite) {
      this.nomSite = nomSite;
   }

   public String getUrlSite() {
      return urlSite;
   }

   public void setUrlSite(String urlSite) {
      this.urlSite = urlSite;
   }

   public String getCharSet() {
      return CharSet;
   }

   public void setCharSet(String CharSet) {
      this.CharSet = CharSet;
   }

   public String getStrAbrev() {
      return strAbrev;
   }

   public void setStrAbrev(String strAbrev) {
      this.strAbrev = strAbrev;
   }

   public int getIntEMP_ID() {
      return intEMP_ID;
   }

   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   public int getIntMON_ID() {
      return intMON_ID;
   }

   public void setIntMON_ID(int intMON_ID) {
      this.intMON_ID = intMON_ID;
   }

   public int getIntSC_ID() {
      return intSC_ID;
   }

   public void setIntSC_ID(int intSC_ID) {
      this.intSC_ID = intSC_ID;
   }

   public int getIntLPrecios() {
      return intLPrecios;
   }

   public void setIntLPrecios(int intLPrecios) {
      this.intLPrecios = intLPrecios;
   }

   public int getIntWP_CT_ID() {
      return intCT_ID;
   }

   public void setIntWP_CT_ID(int intWP_CT_ID) {
      this.intCT_ID = intWP_CT_ID;
   }
   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor vacio
    * @param oConn Es la conexión
    */
   public Site(Conexion oConn) {
      this( oConn,"");
   }
   
   public Site(Conexion oConn,String strAbrev) {
      this.oConn = oConn;
      strLanguage = "";
      strDirection = "";
      this.nomSite = "";
      this.urlSite = "";
      this.strAbrev = strAbrev;
      this.intEMP_ID = 0;
      initSite();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   private void initSite(){
      String strSql = "select * from ecom_site";
      if(this.strAbrev != null && !this.strAbrev.isEmpty())strSql += " where WP_ABREV = '" + this.strAbrev + "' ";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while(rs.next()){
            this.nomSite = rs.getString("WP_NOMSITE");
            this.strLanguage = rs.getString("WP_LANGUAGE");
            this.strDirection = rs.getString("WP_DIRECTION");
            this.urlSite = rs.getString("WP_URLSITE");
            this.CharSet = rs.getString("WP_CHARSET");
            this.intEMP_ID = rs.getInt("WP_EMP_ID");
            this.intMON_ID = rs.getInt("WP_MON_ID");
            this.intLPrecios = rs.getInt("WP_LPRECIOS");
            this.intSC_ID = rs.getInt("WP_SC_ID");
            this.intCT_ID= rs.getInt("WP_CT_ID");
            break;
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
        log.error(ex.getMessage() + " " + ex.getSQLState());
      }
   }
   public String getHeader(TemplateParams templateparams){
      StringBuilder strHTML = new StringBuilder();
      strHTML.append("<base href=\"").append(this.urlSite).append("\" />");
      strHTML.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=").append(this.CharSet).append("\" />");
      strHTML.append("<title>").append(this.nomSite).append("</title>");
//      strHTML.append("<link href=\"templates/").append(templateparams.getNombre()).append("/favicon.ico\" rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" />");
      strHTML.append("<link href=\"templates/").append(templateparams.getNombre()).append("/cofide.ico\" rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" />");
      return strHTML.toString();
   }
   // </editor-fold>
}
