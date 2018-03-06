/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa un estado
 * @author aleph_79
 */
public class EstadosPais extends TableMaster {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   /**
    * Es el constructor
    */
   public EstadosPais() {
      super("estadospais", "ESP_ID", "", "");
      this.Fields.put("ESP_ID", new Integer(0));
      this.Fields.put("ESP_NOMBRE", "");
      this.Fields.put("PA_ID", new Integer(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
