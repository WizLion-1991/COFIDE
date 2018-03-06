/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;
import java.util.ArrayList;

/**
 * Representa las incidencias
 *
 * @author ZeusGalindo
 */
public class RhhIncidencias extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private ArrayList<RhhFaltasDetalle> listFaltas;
   private ArrayList<RhhIncapacidadesDeta> listIncapacidades;

   public ArrayList<RhhIncapacidadesDeta> getListIncapacidades() {
      if (listIncapacidades == null) {
         listIncapacidades = new ArrayList<RhhIncapacidadesDeta>();
      }
      return listIncapacidades;
   }

   public void setListIncapacidades(ArrayList<RhhIncapacidadesDeta> listIncapacidades) {
      this.listIncapacidades = listIncapacidades;
   }

   /**
    * Regresa la lista de faltas de la incidencia
    *
    * @return
    */
   public ArrayList<RhhFaltasDetalle> getListFaltas() {
      if (listFaltas == null) {
         listFaltas = new ArrayList<RhhFaltasDetalle>();
      }
      return listFaltas;
   }

   public void setListFaltas(ArrayList<RhhFaltasDetalle> listFaltas) {
      this.listFaltas = listFaltas;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public RhhIncidencias() {
      super("rhh_incidencias", "RHIN_ID", "", "");
      this.Fields.put("RHIN_ID", 0);
      this.Fields.put("RHN_ID", 0);
      this.Fields.put("RHN_DESCRIPCION", "");
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("EMP_NUM", 0);
      this.Fields.put("EMP_NOMBRE", "");
      this.Fields.put("RHIN_NUM_FALTAS", 0);
      this.Fields.put("RTF_ID", 0);
      this.Fields.put("RHIN_DIAS_INCAPACIDAD", 0);
      this.Fields.put("RHIN_CERTIFICADO_INCAPACIDAD", "");
      this.Fields.put("RTI_ID", 0);
      this.Fields.put("RTR_ID", 0);
      this.Fields.put("RTD_ID", 0);
      this.Fields.put("RTC_ID", 0);
      this.Fields.put("RHIN_APLICA_PREMIO_ASISTENCIA", 0);
      this.Fields.put("RHIN_APLICA_PREMIO_PUNTUALIDAD", 0);
      this.Fields.put("RHIN_APLICA_PREMIO_PRODUCTIVIDAD", 0);
      this.Fields.put("RHIN_APLICA_PRIMA_DOMINICAL", 0);
      this.Fields.put("RHIN_APLICA_VACACIONES", 0);
      this.Fields.put("RHIN_APLICA_BONOS_GRATIFICACIONES", 0);
      this.Fields.put("RHIN_RETARDOS", 0);
      this.Fields.put("RHIN_DIAS_RETROACTIVO", 0);
      this.Fields.put("RH_NOM_ID", 0);
      this.Fields.put("RHIN_PREM_ASIS_PORC", 0);
      this.Fields.put("RHIN_PREM_PUNT_PORC", 0);
      this.Fields.put("RHIN_PREM_PROD_PORC", 0);
      this.Fields.put("RHIN_PREM_BONOYGRAT_PORC", 0);
      this.Fields.put("RHIN_FALTAS_SANCION", 0);
      this.Fields.put("RHIN_NUM_FALTAS_INJUSTIFICADAS", 0);

   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
