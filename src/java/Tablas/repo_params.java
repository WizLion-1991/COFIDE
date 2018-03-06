/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Esta clase representa los parametros de un reporte Jasper
 * @author ZeusGalindo
 */
public class repo_params extends TableMaster {


   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public repo_params() {
      super("repo_params", "REPP_ID", "", "");
      this.Fields.put("REPP_ID", new Integer(0));
      this.Fields.put("REP_ID", new Integer(0));
      this.Fields.put("REPP_NOMBRE", "");
      this.Fields.put("REPP_VARIABLE", "");
      this.Fields.put("REPP_TIPO", "");
      this.Fields.put("REPP_DATO", "");
      this.Fields.put("REPP_TABLAEXT", "");
      this.Fields.put("REPP_ENVIO", "");
      this.Fields.put("REPP_MOSTRAR", "");
      this.Fields.put("REPP_PRE", "");
      this.Fields.put("REPP_POST", "");
      this.Fields.put("REPP_DEFAULT", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
