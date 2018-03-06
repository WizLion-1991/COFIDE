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
public class vta_pedimento_rep extends TableMaster{
       // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    *Constructor default
    */
   public vta_pedimento_rep() {
      super("vta_pedimento_rep", "PRE_ID", "", "");
      this.Fields.put("PRE_ID", new Integer(0));
      this.Fields.put("PRE_DESCRIPCION", "");
      this.Fields.put("PRE_PATH", "");
      this.Fields.put("PED_ID", new Integer(0));
      this.Fields.put("PRE_TAMANIO", new Double(0.0));
      this.Fields.put("PRE_FECHA", "");
      this.Fields.put("PRE_HORA", "");
      this.Fields.put("PRE_USUARIO", new Integer(0));
      
      
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
