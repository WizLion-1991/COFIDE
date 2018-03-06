/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa un dashboard
 * @author aleph_79
 */
public class Dashboard extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Dashboard() {
      super("dashboard", "DB_ID", "", "");
      this.Fields.put("DB_ID", 0);
      this.Fields.put("DB_NOMBRE", "");
      this.Fields.put("DB_ABRV", "");
      this.Fields.put("DB_COLS", 0);
      this.Fields.put("DB_ACTIVO", 0);
      this.Fields.put("DB_REFRESH", 0);
      this.Fields.put("DB_REFRESH_TIME", 0);
      this.Fields.put("DB_WIDTH", 0);
      this.Fields.put("DB_HEIGHT", 0);
      this.Fields.put("DB_TIPO", "");
      this.Fields.put("DB_STYLE", "");
      this.Fields.put("PF_ID", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
