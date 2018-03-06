/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa el sql de una grafica del dashboard
 *
 * @author aleph_79
 */
public class Dashboard_sql extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Dashboard_sql() {
      super("dashboard_sql", "DBS_ID", "", "");
      this.Fields.put("DBS_ID", 0);
      this.Fields.put("DB_ID", 0);
      this.Fields.put("DBD_ID", 0);
      this.Fields.put("DBS_NOMBRE", "");
      this.Fields.put("DBS_SQL", "");
      this.Fields.put("DBS_CAMPO_SERIE", "");
      this.Fields.put("DBS_CAMPO_CATEGORIA", "");
      this.Fields.put("DBS_TIPO_DATO", "");
      this.Fields.put("DBS_ACTIVO", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
