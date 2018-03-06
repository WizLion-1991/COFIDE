/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa los datos de facturacion para un cliente generalmente sera para el
 * cliente de ventas mostrador
 *
 * @author aleph_79
 */
public class vta_cliente_facturacion extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">   
   /**
    *Constructor default
    */
   public vta_cliente_facturacion() {
      super("vta_cliente_facturacion", "DFA_ID", "", "");
      this.Fields.put("DFA_ID", new Integer(0));
      this.Fields.put("DFA_RAZONSOCIAL", "");
      this.Fields.put("DFA_RFC", "");
      this.Fields.put("DFA_CALLE", "");
      this.Fields.put("DFA_NUMERO", "");
      this.Fields.put("DFA_NUMINT", "");
      this.Fields.put("DFA_COLONIA", "");
      this.Fields.put("DFA_LOCALIDAD", "");
      this.Fields.put("DFA_MUNICIPIO", "");
      this.Fields.put("DFA_ESTADO", "");
      this.Fields.put("DFA_CP", "");
      this.Fields.put("DFA_TELEFONO", "");
      this.Fields.put("DFA_EMAIL", "");
      this.Fields.put("DFA_PAIS", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("DFA_VISIBLE", new Integer(0));
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
