/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Entidad que representa 
 * @author ZeusGalindo
 */
public class vta_producto_inventario_fecha extends TableMaster {

   public vta_producto_inventario_fecha() {
      super("vta_producto_inventario_fecha", "", "", "");
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("PR_COSTO_PROM",0);
      this.Fields.put("PR_EXISTENCIA",0);
      this.Fields.put("PR_FECHA", "");
      this.Fields.put("SC_ID", new Integer(0));
   }
}
