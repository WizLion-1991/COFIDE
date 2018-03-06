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
public class RhhDiasFestivos extends TableMaster {

   public RhhDiasFestivos() {
      super("rhh_dias_festivos", "RHDF_ID", "", "");
      this.Fields.put("RHDF_ID", 0);
      this.Fields.put("RHIN_ID", 0);
      this.Fields.put("RHDF_FECHA", "");
   }
}
