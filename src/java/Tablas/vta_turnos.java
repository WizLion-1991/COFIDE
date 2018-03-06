/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de turnos
 * @author ZeusGalindo
 */
public class vta_turnos extends TableMaster {

   public vta_turnos() {
      super("vta_turnos", "TU_ID", "", "");
      this.Fields.put("TU_ID", new Integer(0));
      this.Fields.put("TU_FECHA", "");
      this.Fields.put("TU_HORA", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("IDUSUARIO", new Integer(0));
      this.Fields.put("TU_TURNO", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
   }
}