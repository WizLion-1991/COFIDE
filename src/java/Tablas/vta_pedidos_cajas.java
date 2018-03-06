/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla donde se almacena el detalle del contenido de las cajas de los surtido
 * @author aleph_79
 */
public class vta_pedidos_cajas extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public vta_pedidos_cajas() {
      super("vta_pedidos_cajas", "CAJ_ID", "", "");
      this.Fields.put("CAJ_ID", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("PDD_ID", new Integer(0));
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("CAJ_CANTIDAD", new Double(0));
      this.Fields.put("CAJ_NUMERO", new Integer(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
