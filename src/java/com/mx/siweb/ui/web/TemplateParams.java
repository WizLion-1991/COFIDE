/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.ui.web;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Representa los parametros de un template
 * @author aleph_79
 */
public class TemplateParams {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private boolean IsHTML5 = false;
   private String sitetitle;
   private String sitedescription;
   public String logo;
   private Conexion oConn;
   private Hashtable lstTextos;
   private static final  Logger log = LogManager.getLogger(TemplateParams.class.getName());
   private int intTemplateId = 0;
   private String strLang;
   private String Color;
   private String BackgroundColor;
   private String Position;
   private String googleFontName;
   private boolean googleFont;

   public boolean isIsHTML5() {
      return IsHTML5;
   }

   public void setIsHTML5(boolean IsHTML5) {
      this.IsHTML5 = IsHTML5;
   }
   public String Nombre;
   public String Abrev;

   public String getNombre() {
      return Nombre;
   }

   public void setNombre(String Nombre) {
      this.Nombre = Nombre;
   }

   public String getSitedescription() {
      return sitedescription;
   }

   public void setSitedescription(String sitedescription) {
      this.sitedescription = sitedescription;
   }

   public String getSitetitle() {
      return sitetitle;
   }

   public void setSitetitle(String sitetitle) {
      this.sitetitle = sitetitle;
   }

   public String getLogo() {
      return logo;
   }

   public void setLogo(String logo) {
      this.logo = logo;
   }

   public String getAbrev() {
      return Abrev;
   }

   public void setAbrev(String Abrev) {
      this.Abrev = Abrev;
   }

   public int getIntTemplateId() {
      return intTemplateId;
   }

   public void setIntTemplateId(int intTemplateId) {
      this.intTemplateId = intTemplateId;
   }

   public String getStrLang() {
      return strLang;
   }

   public void setStrLang(String strLang) {
      this.strLang = strLang;
   }

   public String getColor() {
      return Color;
   }

   public void setColor(String Color) {
      this.Color = Color;
   }

   public String getPosition() {
      return Position;
   }

   public void setPosition(String Position) {
      this.Position = Position;
   }

   public boolean isGoogleFont() {
      return googleFont;
   }

   public void setGoogleFont(boolean googleFont) {
      this.googleFont = googleFont;
   }

   public String getGoogleFontName() {
      return googleFontName;
   }

   public void setGoogleFontName(String googleFontName) {
      this.googleFontName = googleFontName;
   }

   public String getBackgroundColor() {
      return BackgroundColor;
   }

   public void setBackgroundColor(String BackgroundColor) {
      this.BackgroundColor = BackgroundColor;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public TemplateParams(Conexion oConn, String Abrev,String strLang) {
      this.oConn = oConn;
      this.Abrev = Abrev;
      this.sitedescription = "";
      this.sitetitle = "";
      lstTextos = new Hashtable();
      this.strLang = strLang;
      initTemplate();
   }

   public TemplateParams(String Abrev,String strLang) {
      this.Abrev = Abrev;
      this.IsHTML5 = false;
      this.sitedescription = "";
      this.sitetitle = "";
      lstTextos = new Hashtable();
      this.strLang = strLang;
      initTemplate();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   private void initTemplate() {
      String strSql = "select * from ecom_template";
      if (this.Abrev != null) {
         strSql += " where TEM_ABRV = '" + this.Abrev + "' ";
      }
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            this.intTemplateId = rs.getInt("TEM_ID");
            this.Nombre = rs.getString("TEM_NOMBRE");
            this.sitetitle = rs.getString("TEM_SITETITLE");
            this.sitedescription = rs.getString("TEM_SITEDESCRIPTION");
            this.logo = rs.getString("TEM_LOGO");
            this.Color= rs.getString("TEM_COLOR");
            this.BackgroundColor= rs.getString("TEM_BACKGROUNDCOLOR");
            this.Position= rs.getString("TEM_NAVPOSITION");
            this.googleFontName= rs.getString("TEM_GOOGLEFONTNAME");
            if(rs.getInt("TEM_ISHTML5") == 1) this.IsHTML5 = true;
            if(rs.getInt("TEM_GOOGLEFONTS") == 1) this.googleFont = true;
            break;
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
         //Cargamos los textos hash
         String strSql2 = "select * from ecom_tittles where TEM_ID = " + this.intTemplateId + " and STR_LANG = '" + this.strLang + "'";
         rs = this.oConn.runQuery(strSql2, true);
         while (rs.next()) {
            this.lstTextos.put(rs.getString("STR_NOMBRE"), rs.getString("STR_VALOR"));
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
   }

   /**
    * Regresa los textos usados en el template
    * @param strNombre Es el nombre de la llave
    * @return Es el valor del texto
    */
   public String getJText(String strNombre) {
      String strValue = null;
      if (this.lstTextos.containsKey(strNombre)) {
         strValue = (String) this.lstTextos.get(strNombre);
      }
      return strValue;
   }
   // </editor-fold>
}
