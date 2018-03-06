package com.mx.siweb.erp.promociones.entidades;

import Tablas.vta_promo_consecuente;
import Tablas.vta_promo_sucursales;
import Tablas.vta_promo_variables;
import Tablas.vta_promociones;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Representa una promocion
 * @author aleph_79
 */
public final class Promocion {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private vta_promociones promocion;
   private ArrayList<vta_promo_consecuente> lstConsecuentes;
   private ArrayList<TableMaster> lstSucursales;
   private ArrayList<vta_promo_variables> lstVariables;
   private Conexion oConn;
   private int intPromoId;
   private static final Logger log = LogManager.getLogger(Promocion.class.getName());

   /**
    * Regresa la lista de consecuentes
    * @return Es una lista de objetos entidad (see the {@link Tablas.vta_promo_consecuente} class)
    */
   public ArrayList<vta_promo_consecuente> getLstConsecuentes() {
      return lstConsecuentes;
   }

   /**
    * Define la lista de consecuentes
    * @param lstConsecuentes Es una lista de objetos entidad (see the {@link Tablas.vta_promo_consecuente} class)
    */
   public void setLstConsecuentes(ArrayList<vta_promo_consecuente> lstConsecuentes) {
      this.lstConsecuentes = lstConsecuentes;
   }

   /**
    * Regresa la lista de sucursales donde se aplica la promocion
    * @return Es una lista de entidades sucursal (see the {@link Tablas.vta_promo_sucursales} class)
    */
   public ArrayList<TableMaster> getLstSucursales() {
      return lstSucursales;
   }

   /**
    * Define la lista de sucursales donde se aplica la promocion
    * @param lstSucursales Es una lista de entidades sucursal (see the {@link Tablas.vta_promo_sucursales} class)
    */
   public void setLstSucursales(ArrayList<TableMaster> lstSucursales) {
      this.lstSucursales = lstSucursales;
   }

   /**
    * Regresa la conexion a la base de datos
    * @return Es un objeto de tipo  (see the {@link comSIWeb.Operaciones.Conexion} class)
    */
   public Conexion getoConn() {
      return oConn;
   }

   /**
    * Define la conexion  a la base de datos
    * @param oConn Es un objeto de tipo (see the {@link comSIWeb.Operaciones.Conexion} class)
    */
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   /**
    * Regresa el objeto de la promocion
    * @return Es un objeto de tipo vta_promociones(see the {@link Tablas.vta_promociones} class)
    */
   public vta_promociones getPromocion() {
      return promocion;
   }

   /**
    * Define el objeto de la promocion
    * @param promocion Es un objeto de tipo vta_promociones(see the {@link Tablas.vta_promociones} class)
    */
   public void setPromocion(vta_promociones promocion) {
      this.promocion = promocion;
   }

   /**
    * Regresa el id de la promocion
    * @return Es el id de la promocion
    */
   public int getIntPromoId() {
      return intPromoId;
   }

   /**
    * Define el id de la promocion
    * @param intPromoId Es el id de la promocion
    */
   public void setIntPromoId(int intPromoId) {
      this.intPromoId = intPromoId;
   }

   /**
    * Regresa la lista de variables
    * @return Es una lista de objetos vta_promo_variables(see the {@link Tablas.vta_promo_variables} class)
    */
   public ArrayList<vta_promo_variables> getLstVariables() {
      return lstVariables;
   }

   /**
    * Define la lista de variables
    * @param lstVariables Es la lista de variables de objetos de tipo vta_promo_variables(see the {@link Tablas.vta_promo_variables} class)
    */
   public void setLstVariables(ArrayList<vta_promo_variables> lstVariables) {
      this.lstVariables = lstVariables;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor default
    * @param oConn Es el conexion
    * @param intPromoId   Es el nombre de la promocion
    */
   public Promocion(Conexion oConn, int intPromoId) {
      this.oConn = oConn;
      this.intPromoId = intPromoId;
      this.promocion = new vta_promociones();
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Carga las promociones
    * @param lstVariablesGlobal es la lista de variables global
    */
   public void cargaPromocion(ArrayList<vta_promo_variables> lstVariablesGlobal) {
      //Inicializamos listas de datos 
      this.promocion.ObtenDatos(intPromoId, oConn);
      this.lstConsecuentes = new ArrayList<vta_promo_consecuente>();
      this.lstVariables = new ArrayList<vta_promo_variables>();
      //Cargamos resto de info
      String strSql = "SELECT PCO_ID FROM vta_promo_link_consecuente WHERE PROM_ID =  " + intPromoId;
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intConsecuenteId = rs.getInt("PCO_ID");
            vta_promo_consecuente consecuente = new vta_promo_consecuente();
            consecuente.ObtenDatos(intConsecuenteId, oConn);
            this.lstConsecuentes.add(consecuente);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
      log.debug("Numero consecuentes:" + this.lstConsecuentes.size());
      vta_promo_sucursales sucursales = new vta_promo_sucursales();
      this.lstSucursales = sucursales.ObtenDatosVarios(" PROM_ID = " + intPromoId, oConn);
      log.debug("Numero sucursales:" + this.lstSucursales.size());
      //Identificamos las variables por usar
      parserFormula(lstVariablesGlobal);
   }

   /**Identifica las variables de la formula
    * @param lstVariablesGlobal  Es la lista de variable global
    */
   protected void parserFormula(ArrayList<vta_promo_variables> lstVariablesGlobal) {
      String strFormula = this.promocion.getFieldString("PROM_FORMULA");
      if (strFormula != null) {
         if (!strFormula.isEmpty()) {
            log.debug(" ");
            log.debug("Si hay formula:" + strFormula);
            // <editor-fold defaultstate="collapsed" desc="Iteramos para identificar las variables">
            for (vta_promo_variables variable : lstVariablesGlobal) {
               //log.debug("Buscamos si existe la variable " + variable.getFieldString("PVAR_VARIABLE"));
               StringBuilder strNomVariable = new StringBuilder(variable.getFieldString("PVAR_VARIABLE"));
               if (strNomVariable.toString().contains("(")) {
                  strNomVariable.delete(strNomVariable.indexOf("(") + 1, strNomVariable.length());
               } else {
                  strNomVariable.append(" ");
               }
               // <editor-fold defaultstate="collapsed" desc="Validamos si la formula contiene la variable">
               if (strFormula.contains(strNomVariable.toString())) {
                  //Recuperamos la variable completa que es la que se enviara por xml
                  //Solo en caso de funciones
                  //Afin de tener los parametros dentro de la misma
                  if (strNomVariable.toString().contains("(")) {
                     int index = strFormula.indexOf(strNomVariable.toString());
                     while (index >= 0) {
                        int indexFin = strFormula.indexOf(")", index + 1);
                        String strFuncNva =  strFormula.substring(index, indexFin + 1);
                        log.debug("Variable " + strFuncNva + " encontrada....");
                        //Clonamos el objeto
                        vta_promo_variables variable2 = (vta_promo_variables) (TableMaster) variable.clone();
                        variable2.setFieldString("PVAR_VARIABLE", strFuncNva);
                        variable.setBolUso(true);
                        this.lstVariables.add(variable2);
                        //Buscamos en el resto de la cadena
                        index = strFormula.indexOf(strNomVariable.toString(), index + 1);
                     }
                  } else {
                     log.debug("Variable " + variable.getFieldString("PVAR_VARIABLE") + " encontrada....");
                     variable.setBolUso(true);
                     this.lstVariables.add(variable);
                  }
               }
               // </editor-fold>
            }
            log.debug(" ");
            // </editor-fold>
         } else {
            log.error("No hay formula para la promocion con id " + this.intPromoId + " " + this.promocion.getFieldString("PROM_NOMBRE"));
         }

      } else {
         //promocion nula
         log.error("No hay formula para la promocion con id " + this.intPromoId + " " + this.promocion.getFieldString("PROM_NOMBRE"));
      }
   }

   /**
    * Obtiene el XML de la promoción
    * @return Regresa el xml
    */
   public String getXML() {
      StringBuilder strXml = new StringBuilder();
      strXml.append("<promocion ");
      strXml.append(this.promocion.getFieldPar());
      strXml.append(">\n ");

      //Variables
      strXml.append("<variables>\n");
      for (vta_promo_variables variable : this.lstVariables) {

            strXml.append("<variable ");
            strXml.append(variable.getFieldPar());
            strXml.append("/>\n");

      }
      strXml.append("</variables>\n");

      //Consecuente
      strXml.append("<consecuentes>\n");
      for (vta_promo_consecuente consecuente : this.lstConsecuentes) {

         strXml.append("<consecuente ");
         strXml.append(consecuente.getFieldPar());
         strXml.append("/>\n");

      }
      strXml.append("</consecuentes>\n");

      //Sucursales
      strXml.append("<sucursales>\n");
      for (TableMaster sucursales : this.lstSucursales) {

         strXml.append("<suc ");
         strXml.append(sucursales.getFieldPar());
         strXml.append("/>\n");

      }
      strXml.append("</sucursales>\n");
      strXml.append("</promocion>\n ");
      return strXml.toString();
   }
   // </editor-fold>
}
