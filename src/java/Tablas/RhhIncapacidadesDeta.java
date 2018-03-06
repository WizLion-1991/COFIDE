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
public class RhhIncapacidadesDeta extends TableMaster {

   public RhhIncapacidadesDeta() {
      super("rhh_incapacidades_deta", "RID_ID", "", "");
      this.Fields.put("RID_ID", 0);
      this.Fields.put("RID_FECHA", "");
      this.Fields.put("RID_NUM_DIAS", 0);
      this.Fields.put("RHIN_ID", 0);
   }
}
