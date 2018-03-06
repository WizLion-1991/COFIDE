/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.EstadosPais;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *Clase que se encarga de realizar operaciones de globalizacion, como obtener estados por pais, etc
 * @author aleph_79
 */
public class Globalizacion {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Obtiene los estados por pais en un array list
    * @param oConn Es la conexion
    * @param intIdPais Es el id del pais
    * @return Regresa la lista de estados en un array list
    */
   public ArrayList<TableMaster> GetStates(Conexion oConn, int intIdPais) {
      EstadosPais estadoTmp = new EstadosPais();
      ArrayList<TableMaster> list = estadoTmp.ObtenDatosVarios("  PA_ID = " + intIdPais, oConn);
      return list;

   }

   /**
    * Obtiene los estados por pais en un array list
    * @param oConn Es la conexion
    * @param intIdPais Es el id del pais
    * @return Regresa un xml con los estados por pais
    */
   public String GetStatesXml(Conexion oConn, int intIdPais) {
      String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
      strXML += "<estados> ";
      ArrayList<TableMaster> list = this.GetStates(oConn, intIdPais);
      Iterator<TableMaster> it = list.iterator();
      while (it.hasNext()) {
         TableMaster tbn = it.next();
         strXML += "<estado ";
         strXML += " id = \"" + tbn.getFieldString("ESP_ID") + "\" "
                 + " desc=\"" + tbn.getFieldString("ESP_NOMBRE") + "\" ";
         strXML += "/>";
      }
      strXML += "</estados>";
      return strXML;

   }
   // </editor-fold>
}
