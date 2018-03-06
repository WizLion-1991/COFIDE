/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author ZeusGalindo
 */
public class vta_cierre_periodo extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public vta_cierre_periodo() {
      super("vta_cierre_periodo", "CP_ID", "", "");
      this.Fields.put("CP_ID", new Integer(0));
      this.Fields.put("CP_DESCRIPCION", "");
      this.Fields.put("CP_MES", "");
      this.Fields.put("CP_ANIO", new Integer(0));
      this.Fields.put("CP_USUARIO", new Integer(0));
      this.Fields.put("CP_FECHA_REG", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
