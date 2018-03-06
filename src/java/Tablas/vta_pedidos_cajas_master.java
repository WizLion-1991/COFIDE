/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa una caja maestra en un pedido surtido
 * @author aleph_79
 */
public class vta_pedidos_cajas_master extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public vta_pedidos_cajas_master() {
      super("vta_pedidos_cajas_master", "CAJM_ID", "", "");
      this.Fields.put("CAJM_ID", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("CAJM_NUMCAJA", new Integer(0));
      this.Fields.put("CAJM_ALTO", new Double(0));
      this.Fields.put("CAJM_ANCHO", new Double(0));
      this.Fields.put("CAJM_LARGO", new Double(0));
      this.Fields.put("CAJM_PESO", new Double(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
