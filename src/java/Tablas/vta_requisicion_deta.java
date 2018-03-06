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
public class vta_requisicion_deta extends TableMaster{
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public vta_requisicion_deta() {
      super("vta_requisicion_deta", "REQD_ID", "", "");
      this.Fields.put("REQD_ID", new Integer(0));
      this.Fields.put("REQ_ID", new Integer(0));
      this.Fields.put("REQD_CODIGO", "");
      this.Fields.put("REQD_DESCRIPCION", "");
      this.Fields.put("REQD_NOTAS", "");
      this.Fields.put("REQD_PR_ID", new Integer(0));
      this.Fields.put("REQD_CANTIDAD", new Double(0.0));
      this.Fields.put("REQD_COSTO", new Double(0.0));
      this.Fields.put("REQD_IMPORTE", new Double(0.0));
      this.Fields.put("REQD_AUTORIZADO", new Integer(0));
      this.Fields.put("REQD_CANTIDAD_AUTORIZADA", new Integer(0));
      this.Fields.put("REQD_COM_ID", new Integer(0));
   }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
