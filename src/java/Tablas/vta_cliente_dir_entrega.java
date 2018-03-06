/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la direccion de entrega de un cliente
 * @author aleph_79
 */
public class vta_cliente_dir_entrega extends TableMaster {


   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    *Constructor default
    */
   public vta_cliente_dir_entrega() {
      super("vta_cliente_dir_entrega", "CDE_ID", "", "");
      this.Fields.put("CDE_ID", new Integer(0));
      this.Fields.put("CDE_ACUDIRCON", "");
      this.Fields.put("CDE_CALLE", "");
      this.Fields.put("CDE_COLONIA", "");
      this.Fields.put("CDE_LOCALIDAD", "");
      this.Fields.put("CDE_MUNICIPIO", "");
      this.Fields.put("CDE_ESTADO", "");
      this.Fields.put("CDE_CP", "");
      this.Fields.put("CDE_NUMERO", "");
      this.Fields.put("CDE_NUMINT", "");
      this.Fields.put("CDE_TELEFONO1", "");
      this.Fields.put("CDE_GOOGLE", "");
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("CDE_DESCRIPCION", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("CDE_NOMBRE", "");
      this.Fields.put("CDE_EMAIL", "");
      this.Fields.put("CDE_PAIS", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
