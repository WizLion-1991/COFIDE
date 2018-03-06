/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author SIWEB
 */
public class vta_datos_archivo extends TableMaster{
    /**
     * Constructor
     */
    public vta_datos_archivo(){
    super("vta_datos_archivo", "CPMD_ID", "", "");
      this.Fields.put("CPMD_ID", new Integer(0));
      this.Fields.put("CPM_ID", new Integer(0));
      this.Fields.put("CPMD_NOMBRE_CAMPO", "");
      this.Fields.put("CPMD_TIPO", "");
      this.Fields.put("CPMD_EXP_REG", "");
      this.Fields.put("CPMD_ORDEN", new Integer(0));
    }
    
}
