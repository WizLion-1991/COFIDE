/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author ZeusSIWEB
 */
public class RhhFaltasDetalle extends TableMaster {

   public RhhFaltasDetalle() {
      super("rhh_faltas_detalle", "RFD_ID", "", "");
      this.Fields.put("RFD_ID", 0);
      this.Fields.put("RFD_FECHA", "");
      this.Fields.put("RTF_ID", 0);
      this.Fields.put("RHIN_ID", 0);
   }
}
