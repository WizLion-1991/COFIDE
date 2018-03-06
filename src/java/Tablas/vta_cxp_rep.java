/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author siwebmx5
 */
public class vta_cxp_rep extends TableMaster{
     // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    *Constructor default
    */
   public vta_cxp_rep() {
      super("vta_cxp_rep", "CPR_ID", "", "");
      this.Fields.put("CPR_ID", new Integer(0));
      this.Fields.put("CPR_DESCRIPCION", "");
      this.Fields.put("CPR_PATH", "");
      this.Fields.put("CXP_ID", new Integer(0));
      this.Fields.put("CPR_TAMANIO", new Double(0.0));
      this.Fields.put("CPR_FECHA", "");
      this.Fields.put("CPR_HORA", "");
      this.Fields.put("CPR_USUARIO", new Integer(0));
      
      
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
