/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa los parametros de los dashboards
 *
 * @author ZeusGalindo
 */
public class Dashboard_Params extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Dashboard_Params() {
      super("dashboard_params", "DBP_ID", "", "");
      this.Fields.put("DBP_ID", 0);
      this.Fields.put("DB_ID", 0);
      this.Fields.put("DBP_TITLE", "");
      this.Fields.put("DBP_NAME", "");
      this.Fields.put("DBP_TIPO", "");
      this.Fields.put("DBP_VALOR_DEFA", "");
      this.Fields.put("DBP_TABLA", "");
      this.Fields.put("DBP_KEY", "");
      this.Fields.put("DBP_SHOW", "");
      this.Fields.put("DBP_PRE", "");
      this.Fields.put("DBP_POST", "");
      this.Fields.put("DBP_KEY_ALIAS", "");
      this.Fields.put("DBP_SHOW_ALIAS", "");
      this.Fields.put("DBD_ID", 0);
      this.Fields.put("DBP_TIPO_DATO", "");
      this.Fields.put("DBP_ORDEN", 0);
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
