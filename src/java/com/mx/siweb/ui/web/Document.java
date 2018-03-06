/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web;

import com.mx.siweb.webaccount.ui.entities.Scripts;
import com.mx.siweb.webaccount.ui.entities.StyleSheet;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Representa el documento de la pagina web por mostrar
 * @author aleph_79
 */
public class Document {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private ArrayList<StyleSheet> lstStyleSheet;
   private ArrayList<Scripts> lstJavaScripts;
   private Conexion oConn;
   private static final  Logger log = LogManager.getLogger(Document.class.getName());
   Hashtable lstCountModules;

   public Hashtable getLstCountModules() {
      return lstCountModules;
   }

   public void setLstCountModules(Hashtable lstCountModules) {
      this.lstCountModules = lstCountModules;
   }

   public ArrayList<StyleSheet> getLstStyleSheet() {
      return lstStyleSheet;
   }

   public void setLstStyleSheet(ArrayList<StyleSheet> lstStyleSheet) {
      this.lstStyleSheet = lstStyleSheet;
   }

   public ArrayList<Scripts> getLstJavaScripts() {
      return lstJavaScripts;
   }

   public void setLstJavaScripts(ArrayList<Scripts> lstJavaScripts) {
      this.lstJavaScripts = lstJavaScripts;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Document(Conexion oConn) {
      lstStyleSheet = new ArrayList<StyleSheet>();
      lstJavaScripts = new ArrayList<Scripts>();
      this.oConn = oConn;
      lstCountModules = new Hashtable();
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void addStyleSheet(String strNomStyleSheet, String strTipo, String strMedio) {
      StyleSheet style = new StyleSheet(strNomStyleSheet, strTipo, strMedio);
      if (style.getStrTipo().isEmpty()) {
         style.setStrTipo("text/css");
      }

      this.lstStyleSheet.add(style);
   }

   public void addJavaScript(String strNom, String strTipo) {
      Scripts script = new Scripts(strNom, strTipo);
      if (script.getStrTipoScript().isEmpty()) {
         script.setStrTipoScript("text/javascript");
      }
      this.lstJavaScripts.add(script);
   }

   /**
    * Genera el codigo HTML para los estilos
    * @return  <link rel="stylesheet" href="/templates/beez_20/css/position.css" type="text/css" media="screen,projection"  />
    */
   public String writeHtmlStyles() {
      StringBuilder strHTML = new StringBuilder();
      Iterator<StyleSheet> it = this.lstStyleSheet.iterator();
      while (it.hasNext()) {
         StyleSheet style = it.next();
         strHTML.append("<link rel=\"stylesheet\" href=\"").append(style.getStrPath()).append("\" type=\"").append(style.getStrTipo()).append("\" ");
         if (!style.getStrMedio().isEmpty()) {
            strHTML.append(" media=\"").append(style.getStrMedio()).append("\"  />\n");
         }else{
            strHTML.append("  />\n");
         }
      }
      return strHTML.toString();
   }

   /**
    * Genera el codigo HTML para el javascript
    * @return  <script src="/media/system/js/mootools-core.js" type="text/javascript"></script>
    */
   public String writeHtmlJavaScript() {
      StringBuilder strHTML = new StringBuilder();
      Iterator<Scripts> it = this.lstJavaScripts.iterator();
      while (it.hasNext()) {
         Scripts script = it.next();
         strHTML.append("<script src=\"").append(script.getStrNomScript()).append("\" type=\"").append(script.getStrTipoScript()).append("\"></script>\n");
      }
      return strHTML.toString();
   }

   /**
    * Carga información de los modulos
    */
   public void getInfoModules() {
      String strSql = "select  MOD_POSITION,count(*) as cuantos  from ecom_modules group by MOD_POSITION;";

      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            this.lstCountModules.put(rs.getString("MOD_POSITION"), rs.getInt("cuantos"));
            break;
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();

      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
   }
   //type="modules" name="position-9" style="beezDivision"  headerlevel="3" 
   /**
    * Regresa el html de un modulo
    * @param strNomModulo Es el nombre del modulo
    * @param strPosition Es la position del o los modulos
    * @param strStyle Es el estilo
    * @param strHeaderLevel Es el nivel
    * @return Regresa codigo HTML
    */
   public String getModule(String strNomModulo,String strPosition,String strStyle,String strHeaderLevel){
      StringBuilder strHTML = new StringBuilder();
      return strHTML.toString();
   }
   public String getMessage(String strNomMsg){
      StringBuilder strHTML = new StringBuilder();
      return strHTML.toString();
   }
   public String getComponent(String strNomMsg){
      StringBuilder strHTML = new StringBuilder();
      return strHTML.toString();
   }
   // </editor-fold>
}
