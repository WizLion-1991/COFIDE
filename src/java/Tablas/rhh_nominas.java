/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla de nominas
 *
 * @author ZeusGalindo
 */
public class rhh_nominas extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public rhh_nominas() {
      super("rhh_nominas", "NOM_ID", "", "");
      this.Fields.put("NOM_ID", new Integer(0));
      this.Fields.put("NOM_FOLIO", "");
      this.Fields.put("NOM_FECHA", "");
      this.Fields.put("NOM_HORA", "");
      this.Fields.put("EMP_NUM", new Integer(0));
      this.Fields.put("NOM_PERCEPCIONES", new Double(0));
      this.Fields.put("NOM_DEDUCCIONES", new Double(0));
      this.Fields.put("NOM_ISR_RETENIDO", new Double(0));
      this.Fields.put("NOM_PERCEPCION_TOTAL", new Double(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("NOM_RAZONSOCIAL", "");
      this.Fields.put("NOM_RFC", "");
      this.Fields.put("NOM_CALLE", "");
      this.Fields.put("NOM_COLONIA", "");
      this.Fields.put("NOM_LOCALIDAD", "");
      this.Fields.put("NOM_MUNICIPIO", "");
      this.Fields.put("NOM_ESTADO", "");
      this.Fields.put("NOM_CP", "");
      this.Fields.put("NOM_TASA_ISR", new Double(0));
      this.Fields.put("NOM_ANULADA", new Integer(0));
      this.Fields.put("NOM_FECHAANUL", "");
      this.Fields.put("NOM_NOTAS", "");
      this.Fields.put("NOM_US_ALTA", new Integer(0));
      this.Fields.put("NOM_US_ANUL", new Integer(0));
      this.Fields.put("NOM_MONEDA", new Integer(0));
      this.Fields.put("NOM_TASAPESO", new Double(0));
      this.Fields.put("NOM_FECHACREATE", "");
      this.Fields.put("NOM_HORANUL", "");
      this.Fields.put("NOM_DESCUENTO", "");
      this.Fields.put("NOM_CADENAORIGINAL", "");
      this.Fields.put("NOM_SELLO", "");
      this.Fields.put("NOM_NUMERO", "");
      this.Fields.put("NOM_NUMINT", "");
      this.Fields.put("NOM_RETISR", new Double(0));
      this.Fields.put("NOM_NOTASPIE", "");
      this.Fields.put("NOM_REFERENCIA", "");
      this.Fields.put("NOM_CONDPAGO", "");
      this.Fields.put("NOM_NOMFORMATO", "");
      this.Fields.put("NOM_NOMFORMATO_RECIBO", "");
      this.Fields.put("NOM_EXEC_INTER_CP", new Integer(0));
      this.Fields.put("NOM_EXEC_INTER_CP_ANUL", new Integer(0));
      this.Fields.put("NOM_FORMADEPAGO", "");
      this.Fields.put("NOM_METODODEPAGO", "");
      this.Fields.put("NOM_UUID", "");
      this.Fields.put("NOM_SENDMAIL", new Integer(0));
      this.Fields.put("NOM_SELLOTIMBRE", "");
      this.Fields.put("NOM_HORA_TIMBRE", "");
      this.Fields.put("NOM_FOLIO_ANUL", "");
      this.Fields.put("NOM_SELLOTIMBRE_ANUL", "");
      this.Fields.put("NOM_HORA_TIMBRE_ANUL", "");
      this.Fields.put("NOM_CADENA_TIMBRE", "");
      this.Fields.put("NOM_PATH_CBB", "");
      this.Fields.put("NOM_NOSERIECERTTIM", "");
      this.Fields.put("NOM_REGIMENFISCAL", "");
      this.Fields.put("NOM_NUMCUENTA", "");
      this.Fields.put("NOM_REGISTRO_PATRONAL", "");
      this.Fields.put("RC_ID", new Integer(0));
      this.Fields.put("NOM_FECHA_INICIAL_PAGO", "");
      this.Fields.put("NOM_FECHA_FINAL_PAGO", "");
      this.Fields.put("NOM_NUM_DIAS_PAGADOS", new Double(0));
      this.Fields.put("DP_ID", new Integer(0));
      this.Fields.put("NOM_DIAS_INCAPACIDAD", new Integer(0));
      this.Fields.put("TI_ID", new Integer(0));
      this.Fields.put("NOM_SE_TIMBRO", new Integer(0));
      this.Fields.put("NOM_US_MOD", new Integer(0));
      this.Fields.put("NOM_ANTIGUEDAD", new Integer(0));
      this.Fields.put("NOM_INCAPACIDAD_DESCUENTO", new Double(0));
      this.Fields.put("NOM_HORA_EXTRA_DIAS1", new Integer(0));
      this.Fields.put("NOM_HORA_EXTRA_HORAS1", new Double(0));
      this.Fields.put("NOM_HORA_EXTRA_IMPORTE1", new Double(0));
      this.Fields.put("NOM_HORA_EXTRA_DIAS2", new Integer(0));
      this.Fields.put("NOM_HORA_EXTRA_HORAS2", new Double(0));
      this.Fields.put("NOM_HORA_EXTRA_IMPORTE2", new Double(0));
      this.Fields.put("NOM_CONCEPTO", "");
      this.Fields.put("NOM_CAMPO_ADICIONAL1", "");
      this.Fields.put("NOM_CAMPO_ADICIONAL2", "");
      this.Fields.put("NOM_SALARIO_DIARIO", new Double(0));
      this.Fields.put("NOM_SALARIO_INTEGRADO", new Double(0));
      this.bolGetAutonumeric = true;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}

