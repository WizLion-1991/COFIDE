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
public class RhhPrimaDominical extends TableMaster {

   public RhhPrimaDominical() {
      super("rhh_prima_dominical", "RHPD_ID", "", "");
      this.Fields.put("RHPD_ID", 0);
      this.Fields.put("RHIN_ID", 0);
      this.Fields.put("RHPD_FECHA", "");
   }
}
