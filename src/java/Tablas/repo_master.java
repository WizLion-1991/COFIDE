/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Tabla que representa una tabla maestra de reportes
 * @author ZeusGalindo
 */
public class repo_master extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public repo_master() {
      super("repo_master", "REP_ID", "", "");
      this.Fields.put("REP_ID", new Integer(0));
      this.Fields.put("REP_NOMBRE", "");
      this.Fields.put("REP_PERMISO", new Integer(0));
      this.Fields.put("REP_SECCION", new Integer(0));
      this.Fields.put("REP_ORDEN", new Integer(0));
      this.Fields.put("REP_FZA_VTAS", new Integer(0));
      this.Fields.put("REP_ACTIVO", new Integer(0));
      this.Fields.put("REP_USR_CREATE", new Integer(0));
      this.Fields.put("REP_FECHA_CREATE", "");
      this.Fields.put("REP_HORA_CREATE", "");
      this.Fields.put("REP_FECHA_MODI", "");
      this.Fields.put("REP_HORA_MODI", "");
      this.Fields.put("REP_USR_MODI", new Integer(0));
      this.Fields.put("REP_FECHA_EXEC", "");
      this.Fields.put("REP_HORA_EXEC", "");
      this.Fields.put("REP_USR_EXEC", new Integer(0));
      this.Fields.put("REP_CONT_EXEC", new Integer(0));
      this.Fields.put("REP_PROM_EXEC", new Integer(0));
      this.Fields.put("REP_XLS", new Integer(0));
      this.Fields.put("REP_PDF", new Integer(0));
      this.Fields.put("REP_TXT", new Integer(0));
      this.Fields.put("REP_HTML", new Integer(0));
      this.Fields.put("REP_DESCRIPCION", "");
      this.Fields.put("REP_JRXML", "");
      this.Fields.put("REP_USERS", "");
      this.Fields.put("REP_ABRV", "");
      this.Fields.put("REP_NOM_FILE", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
