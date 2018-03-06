/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author ZeusGalindo
 */
public class MlmMovComis extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public MlmMovComis() {
      super("mlm_mov_comis", "MMC_ID", "", "");
      this.Fields.put("MMC_ID", 0);
      this.Fields.put("MMC_FECHA", "");
      this.Fields.put("CT_ID", 0);
      this.Fields.put("MMC_CARGO", 0.0);
      this.Fields.put("MMC_ABONO", 0.0);
      this.Fields.put("MMC_NOTAS", "");
      this.Fields.put("MMC_USUARIO", 0);
      this.Fields.put("FAC_ID", 0);
      this.Fields.put("MC_ID", 0);
      this.Fields.put("NC_ID", 0);
      this.Fields.put("MPE_ID", 0);
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
