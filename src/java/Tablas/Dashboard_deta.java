/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Es el detalle del dash board, especifica la grafica por mostrar
 * @author aleph_79
 */
public class Dashboard_deta extends TableMaster {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Dashboard_deta() {
      super("dashboard_deta", "DBD_ID", "", "");
      this.Fields.put("DBD_ID", 0);
      this.Fields.put("DB_ID", 0);
      this.Fields.put("DB_TITULO", "");
      this.Fields.put("DB_NOMBRE", "");
      this.Fields.put("DB_CAMPOX", "");
      this.Fields.put("DB_CAMPOY", "");
      this.Fields.put("DB_CAMPOZ", "");
      this.Fields.put("DB_CAMPOA", "");
      this.Fields.put("DB_CAMPOX_TITULO", "");
      this.Fields.put("DB_CAMPOY_TITULO", "");
      this.Fields.put("DB_CAMPOZ_TITULO", "");
      this.Fields.put("DB_CAMPOA_TITULO", "");
      this.Fields.put("DB_FILAS", 0);
      this.Fields.put("DB_CAMPOX_LINK1", "");
      this.Fields.put("DB_CAMPOY_LINK1", "");
      this.Fields.put("DB_CAMPOZ_LINK1", "");
      this.Fields.put("DB_CAMPOA_LINK1", "");
      this.Fields.put("DB_WIDTH", "");
      this.Fields.put("DB_HEIGHT", "");
      this.Fields.put("DB_TIPO_GRAFICA", "");
      this.Fields.put("DB_SUBTITULO", "");
      this.Fields.put("DB_TITULO_AXIS", "");
      this.Fields.put("DB_ACTIVO", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
