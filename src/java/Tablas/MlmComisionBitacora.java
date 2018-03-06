/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la bitacora de las comisiones
 * @author ZeusSIWEB
 */
public class MlmComisionBitacora extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public MlmComisionBitacora() {
      super("mlm_comision_bitacora", "CCO_ID", "", "");
      this.Fields.put("CCO_ID", 0);
      this.Fields.put("CCO_FECHA", "");
      this.Fields.put("CCO_HORA", "");
      this.Fields.put("CCO_USUARIO", 0);
      this.Fields.put("MPE_ID", 0);
      this.Fields.put("CCO_IP", "");
      this.Fields.put("CCO_SESION", "");
      this.Fields.put("CCO_DEFINITIVAS", 0);
      this.Fields.put("MPEM_ID", 0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
