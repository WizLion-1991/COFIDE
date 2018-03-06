package com.mx.siweb.mlm.utilerias;

import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase genera los archivos xml y json para las diferentes maneras de ver
 * las redes de multinivel
 *
 * @author zeus
 */
public class VistaRed {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   private Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(VistaRed.class.getName());
   protected final String strXMLHEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
   private boolean bolUsaSponzor = false;

   public boolean isBolUsaSponzor() {
      return bolUsaSponzor;
   }

   /**
    *Define si manejamos la red por el sponzor
    * @param bolUsaSponzor
    */
   public void setBolUsaSponzor(boolean bolUsaSponzor) {
      this.bolUsaSponzor = bolUsaSponzor;
   }
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   /**
    * Constructor default
    *
    * @param oConn Es la conexion
    */
   public VistaRed(Conexion oConn) {
      this.oConn = oConn;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Genera el xml para el control jqgrid tree
    *
    * @param intNodoId Es el nodo raiz a obtener
    * @return Regresa el xml de la red
    */
   public String doXMLtreeGrid(int intNodoId) {
      StringBuilder strXML = new StringBuilder("");
      strXML.append(strXMLHEAD);
      strXML.append("<rows>");
      strXML.append("<page>1</page>");
      strXML.append("<total>1</total>");
      //Consultamos la descendencia de este cliente
      String strSql = "SELECT count(CT_ID) as cuantos FROM vta_cliente WHERE "
              + " CT_ARMADONUM>=(SELECT CT_ARMADOINI FROM vta_cliente where CT_ID = " + intNodoId + ") AND "
              + " CT_ARMADONUM<=(SELECT CT_ARMADOFIN FROM vta_cliente where CT_ID = " + intNodoId + ") "
              + " ORDER BY CT_ARMADONUM";
      try {
         //Calculamos cuantyos
         ResultSet rs = oConn.runQuery(strSql, true);
         int intCuantos = 0;
         while (rs.next()) {
            intCuantos = rs.getInt("cuantos");
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         strXML.append("<records>").append(intCuantos).append("</records>");
         //Obtiene la lista de la red
         strSql = "SELECT CT_ID,CT_UPLINE,CT_RAZONSOCIAL,CT_TELEFONO1,"
                 + "CT_TELEFONO2,CT_EMAIL1,CT_ARMADODEEP,CT_ARMADOINI,CT_ARMADOFIN"
                 + ",CT_PPUNTOS,CT_PNEGOCIO,CT_GPUNTOS,CT_GNEGOCIO,CT_COMISION"
                 + " FROM vta_cliente WHERE "
                 + " CT_ARMADONUM>=(SELECT CT_ARMADOINI FROM vta_cliente where CT_ID = " + intNodoId + ") AND "
                 + " CT_ARMADONUM<=(SELECT CT_ARMADOFIN FROM vta_cliente where CT_ID = " + intNodoId + ") "
                 + " ORDER BY CT_ARMADONUM";
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strXML.append("<row>");
            strXML.append("<cell>").append(rs.getInt("CT_ID")).append("</cell>");
            strXML.append("<cell>").append(rs.getInt("CT_ID")).append("</cell>");
            strXML.append("<cell>").append(rs.getInt("CT_UPLINE")).append("</cell>");// parent row id 
            strXML.append("<cell>").append(rs.getString("CT_RAZONSOCIAL")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("CT_EMAIL1")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("CT_TELEFONO1")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_PPUNTOS")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_PNEGOCIO")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_GPUNTOS")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_GNEGOCIO")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_COMISION")).append("</cell>");
            strXML.append("<cell>").append(rs.getInt("CT_ARMADODEEP") - 1).append("</cell>");// node level 0 
            strXML.append("<cell>").append(rs.getInt("CT_UPLINE")).append("</cell>");// parent row id 
            if (rs.getInt("CT_ARMADOINI") == rs.getInt("CT_ARMADOFIN")) {
               strXML.append("<cell>true</cell>");// is this a leaf? 
            } else {
               strXML.append("<cell>false</cell>");// is this a leaf? 
            }
            strXML.append("<cell>false</cell>");// expand? 
            strXML.append("</row>");
            /*
             <row> 
             <cell>1</cell> # row id 
             <cell>Tabelle</cell> # expanding column 
             <cell>1a2</cell> # column 2 
             <cell>1a2</cell> # column 3 
             <cell>0</cell> # node level 0 
             <cell>NULL</cell> # parent row id 
             <cell>false</cell> # is this a leaf? 
             <cell>false</cell> # expand? 
             </row> 
             */
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         strXML.append("</rows> ");
      } catch (SQLException ex) {
         this.log.error("Error en la consulta de la red " + ex.getMessage() + " " + ex.getSQLState());
      }
      return strXML.toString();
   }

   /**
    * Genera el json para el control Jit
    *
    * @param intNodoId Es el nodo raiz a obtener
    * @return Regresa el Json de la red
    */
   public String doJsonJit(int intNodoId) {
      String strJson = null;
      try {
         //Iniciamos objeto
         JSONObject objJson = new JSONObject();
         //Consultamos la descendencia de este cliente
         try {

            //Obtiene la informacion del nodo raiz
            String strSql = "SELECT CT_ID,CT_RAZONSOCIAL "
                    + " FROM vta_cliente WHERE "
                    + " CT_ID = " + intNodoId;
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               objJson.put("id", "node" + rs.getInt("CT_ID"));
               objJson.put("name", rs.getString("CT_RAZONSOCIAL"));
               JSONObject objJsonData = new JSONObject();
               objJson.put("data", objJsonData);
            }
             //if(rs.getStatement() != null )rs.getStatement().close(); 
             rs.close();
            //Llamadas recursivas
            JSONArray jsonChild = getHijosde(intNodoId);
            objJson.put("children", jsonChild);

         } catch (SQLException ex) {
            this.log.error("Error en la consulta de la red " + ex.getMessage() + " " + ex.getSQLState());
         }
         strJson = objJson.toString();
      } // </editor-fold>
      catch (JSONException ex) {
         this.log.error("Error en la generacion de Json de la red " + ex.getMessage() + " " + ex.getLocalizedMessage());
      }
      return strJson;
   }

   public JSONArray getHijosde(int intNodoPadre) {
      JSONArray jsonChild = new JSONArray();
      //Consultamos la descendencia de este cliente
      try {

         //Obtiene la informacion del nodo raiz
         String strSql = "SELECT CT_ID,CT_RAZONSOCIAL "
                 + " FROM vta_cliente WHERE "
                 + " CT_UPLINE = " + intNodoPadre;
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            JSONObject objJson = new JSONObject();
            objJson.put("id", "node" + rs.getInt("CT_ID"));
            objJson.put("name", rs.getString("CT_RAZONSOCIAL"));
            JSONObject objJsonData = new JSONObject();
            objJson.put("data", objJsonData);
            //Llamadas recursivas
            JSONArray jsonChild2 = getHijosde(rs.getInt("CT_ID"));
            objJson.put("children", jsonChild2);
            jsonChild.put(objJson);
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();


      } catch (JSONException ex) {
         this.log.error("Error en la generacion de Json recursiva " + ex.getMessage() + " " + ex.getLocalizedMessage());
      } catch (SQLException ex) {
         this.log.error("Error en la consulta de la red " + ex.getMessage() + " " + ex.getSQLState());
      }
      return jsonChild;
   }

   /**
    * Genera el xml para el control jqgrid tree
    *
    * @param intNodoId Es el nodo raiz a obtener
    * @param strNombre Es el nombre del cliente que se esta buscando
    * @param intCT_ID  Es el id
    * @param intCT_UPLINE  Es el id del upline
    * @param intNivelRed Es el nivel de red
    * @return Regresa el xml de la red
    */
   public String doXMLGrid(int intNodoId,String strNombre, int intCT_ID, int intCT_UPLINE, int intNivelRed) {
      comSIWeb.Utilerias.UtilXml utilXml = new comSIWeb.Utilerias.UtilXml();
      StringBuilder strXML = new StringBuilder("");
      strXML.append(strXMLHEAD);
      strXML.append("<rows>");
      strXML.append("<page>1</page>");
      String strFiltro  ="";
      if(!strNombre.isEmpty()){
         strFiltro  = " AND CT_RAZONSOCIAL LIKE '%" + strNombre + "%'";
      }
      if(intCT_ID != 0 ){
         strFiltro  = " AND vta_cliente.CT_ID = " + intCT_ID + " ";
      }
      if(intCT_UPLINE != 0 ){
         strFiltro  = " AND vta_cliente.CT_UPLINE = " + intCT_UPLINE + " ";
      }
      if(intNivelRed != 0 ){
         strFiltro  = " AND vta_cliente.CT_NIVELRED = " + intNivelRed + " ";
      }
      //Consultamos la descendencia de este cliente
      String strSql = "SELECT count(CT_ID) as cuantos FROM vta_cliente WHERE "
              + " CT_ARMADONUM>=(SELECT CT_ARMADOINI FROM vta_cliente where CT_ID = " + intNodoId + ") AND "
              + " CT_ARMADONUM<=(SELECT CT_ARMADOFIN FROM vta_cliente where CT_ID = " + intNodoId + ") " + strFiltro
              + " ORDER BY CT_ARMADONUM";
      try {
         //Calculamos cuantyos
         ResultSet rs = oConn.runQuery(strSql, true);
         int intCuantos = 0;
         while (rs.next()) {
            intCuantos = rs.getInt("cuantos");
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         strXML.append("<records>").append(intCuantos).append("</records>");
         //Obtiene la lista de la red
         strSql = "SELECT vta_cliente.CT_ID,CT_UPLINE,CT_RAZONSOCIAL,CT_TELEFONO1,vta_cliente.SC_ID,SC_NUM,"
                 + "CT_TELEFONO2,CT_EMAIL1,CT_ARMADODEEP,CT_ARMADOINI,CT_ARMADOFIN"
                 + ",CT_PPUNTOS,CT_PNEGOCIO,CT_GPUNTOS,CT_GNEGOCIO,CT_COMISION,CT_NIVELRED "
                 + ",CT_NOMBRE,CT_APATERNO,CT_AMATERNO"
                 + " FROM vta_cliente,vta_sucursal WHERE "
                 + " CT_ARMADONUM>=(SELECT CT_ARMADOINI FROM vta_cliente where CT_ID = " + intNodoId + ") AND "
                 + " CT_ARMADONUM<=(SELECT CT_ARMADOFIN FROM vta_cliente where CT_ID = " + intNodoId + ") "
                 + " AND vta_cliente.SC_ID = vta_sucursal.SC_ID " + strFiltro
                 + " ORDER BY CT_ARMADONUM";
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            String strNivelRed = rs.getInt("CT_NIVELRED") + "";
            strXML.append("<row>");
            strXML.append("<cell>").append(rs.getInt("SC_NUM")).append("</cell>");
            strXML.append("<cell>").append(rs.getInt("CT_UPLINE")).append("</cell>");
            strXML.append("<cell>").append(rs.getInt("CT_ID")).append("</cell>");
            strXML.append("<cell>").append(rs.getString("CT_NOMBRE")).append(" ").append(rs.getString("CT_APATERNO")).append(" ").append(rs.getString("CT_AMATERNO")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_PPUNTOS")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_GPUNTOS")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_PNEGOCIO")).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_GNEGOCIO")).append("</cell>");
            strXML.append("<cell>").append(utilXml.Sustituye(rs.getString("CT_TELEFONO1"))).append("</cell>");
            strXML.append("<cell>").append(utilXml.Sustituye(rs.getString("CT_TELEFONO2"))).append("</cell>");
            strXML.append("<cell>").append(utilXml.Sustituye(rs.getString("CT_EMAIL1"))).append("</cell>");
            strXML.append("<cell>").append(rs.getDouble("CT_COMISION")).append("</cell>");
            strXML.append("<cell>").append(strNivelRed).append("</cell>");
            strXML.append("</row>");
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         strXML.append("</rows> ");
      } catch (SQLException ex) {
         this.log.error("Error en la consulta de la red " + ex.getMessage() + " " + ex.getSQLState());
      }
      return strXML.toString();
   }
   // </editor-fold>
}
