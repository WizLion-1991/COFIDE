/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa una nómina
 * @author ZeusGalindo
 */
public class Rhh_Nominas_Master extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Rhh_Nominas_Master() {
      super("rhh_nominas_master", "RHN_ID", "", "");
      this.Fields.put("RHN_ID", 0);
      this.Fields.put("RHN_DESCRIPCION", "");
      this.Fields.put("RHN_FECHA_INICIAL", "");
      this.Fields.put("RHN_FECHA_FINAL", "");
      this.Fields.put("RHN_FECHA_REGISTRO", "");
      this.Fields.put("RHN_USUARIO", 0);
      this.Fields.put("RHN_HORA_REGISTRO", "");
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("RHN_ES_AGUINALDO", 0);
      this.Fields.put("RHN_NUMERO_DIAS", 0);
      this.Fields.put("RHN_PAGA_SUBSIDIO", 0);
      this.Fields.put("RHN_ISR_ACOMULADO", 0);
      this.Fields.put("RHN_CALCULO_ABONOS", 0);
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
