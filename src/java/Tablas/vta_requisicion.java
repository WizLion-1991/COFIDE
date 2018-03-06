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
public class vta_requisicion extends TableMaster{
    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public vta_requisicion() {
      super("vta_requisicion", "REQ_ID", "", "");
      this.Fields.put("REQ_ID", new Integer(0));
      this.Fields.put("REQ_FOLIO", "");
      this.Fields.put("REQ_FECHA", "");
      this.Fields.put("REQ_USUARIO_SOLICITO", new Integer(0));
      this.Fields.put("REQ_HORA", "");
      this.Fields.put("REQ_ID_CLIENTE", new Integer(0));
      this.Fields.put("REQ_NOTAS", "");
      this.Fields.put("REQ_AUTORIZADO", "");
      this.Fields.put("REQ_STATUS", new Integer(0));
      this.Fields.put("REQ_FECHA_AUTORIZADA", "");
      this.Fields.put("REQ_USUARIO_AUTORIZA", new Integer(0));
      this.Fields.put("REQ_COM_ID", new Integer(0));
      this.Fields.put("REQ_PD_ID", new Integer(0));
      
   }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
