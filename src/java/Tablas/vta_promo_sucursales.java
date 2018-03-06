/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa las sucursales de las promociones
 * @author aleph_79
 */
public class vta_promo_sucursales extends TableMaster {

   public vta_promo_sucursales() {
      super("vta_promo_sucursales", "PROM_ID", "", "");
      this.Fields.put("PROM_ID", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
   }
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
