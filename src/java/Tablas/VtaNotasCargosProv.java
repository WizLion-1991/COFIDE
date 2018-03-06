/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla maestra de notas de cargo de proveedor
 * @author ZeusSIWEB
 */
public class VtaNotasCargosProv extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaNotasCargosProv() {
      super("vta_notas_cargosprov", "NCA_ID", "", "");
      this.Fields.put("NCA_ID", 0);
      this.Fields.put("NCA_FOLIO", "");
      this.Fields.put("NCA_FECHA", "");
      this.Fields.put("NCA_HORA", "");
      this.Fields.put("PV_ID", 0);
      this.Fields.put("NCA_IMPORTE", 0.0);
      this.Fields.put("NCA_IMPUESTO1", 0.0);
      this.Fields.put("NCA_IMPUESTO2", 0.0);
      this.Fields.put("NCA_IMPUESTO3", 0.0);
      this.Fields.put("NCA_TOTAL", 0.0);
      this.Fields.put("NCA_SALDO", 0.0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("NCA_RAZONSOCIAL", "");
      this.Fields.put("NCA_RFC", "");
      this.Fields.put("NCA_CALLE", "");
      this.Fields.put("NCA_COLONIA", "");
      this.Fields.put("NCA_LOCALIDAD", "");
      this.Fields.put("NCA_MUNICIPIO", "");
      this.Fields.put("NCA_ESTADO", "");
      this.Fields.put("NCA_CP", "");
      this.Fields.put("NCA_TASA1", 0.0);
      this.Fields.put("NCA_TASA2", 0.0);
      this.Fields.put("NCA_TASA3", 0.0);
      this.Fields.put("NCA_O_REM", 0);
      this.Fields.put("NCA_GANANCIA", 0.0);
      this.Fields.put("ES_ID", 0);
      this.Fields.put("NCA_ANULADA", 0);
      this.Fields.put("NCA_FECHAANUL", "");
      this.Fields.put("NCA_DIASCREDITO", 0);
      this.Fields.put("NCA_NOTAS", "");
      this.Fields.put("NCA_LPRECIOS", 0);
      this.Fields.put("NCA_IDNC", 0);
      this.Fields.put("NCA_ES_SURTIDO", 0);
      this.Fields.put("NCA_US_ALTA", 0);
      this.Fields.put("NCA_US_ANUL", 0);
      this.Fields.put("NCA_MONEDA", 0);
      this.Fields.put("NCA_TASAPESO", 0.0);
      this.Fields.put("VE_ID", 0);
      this.Fields.put("NCA_FECHACREATE", "");
      this.Fields.put("NCA_ESSERV", 0);
      this.Fields.put("NCA_HORANUL", "");
      this.Fields.put("NCA_COSTO", 0.0);
      this.Fields.put("NCA_DESCUENTO", 0.0);
      this.Fields.put("NCA_CADENAORIGINAL", "");
      this.Fields.put("NCA_SELLO", "");
      this.Fields.put("CCJ_ID", 0);
      this.Fields.put("NCA_ESMASIVA", 0);
      this.Fields.put("NCA_NUMERO", "");
      this.Fields.put("NCA_NUMINT", "");
      this.Fields.put("NCA_SERIE", "");
      this.Fields.put("NCA_NOAPROB", "");
      this.Fields.put("NCA_FECHAAPROB", "");
      this.Fields.put("CXP_ID", 0);
      this.Fields.put("NCA_TIPOCOMP", 0);
      this.Fields.put("NCA_RETISR",0.0);
      this.Fields.put("NCA_RETIVA",0.0);
      this.Fields.put("NCA_NETO",0.0);
      this.Fields.put("NCA_NOTASPIE", "");
      this.Fields.put("NCA_REFERENCIA", "");
      this.Fields.put("NCA_CONDPAGO", "");
      this.Fields.put("NCA_ESARRENDA", 0);
      this.Fields.put("NCA_NOMFORMATO", "");
      this.Fields.put("NCA_EXEC_INTER_CP", 0);
      this.Fields.put("NCA_ESRECU", 0);
      this.Fields.put("NCA_PERIODICIDAD", 0);
      this.Fields.put("NCA_DIAPER", 0);
      this.Fields.put("NCA_NUMPEDI", "");
      this.Fields.put("NCA_FECHAPEDI", "");
      this.Fields.put("NCA_ADUANA", "");
      this.Fields.put("TI_ID", 0);
      this.Fields.put("TI_ID2", 0);
      this.Fields.put("TI_ID3", 0);
      this.Fields.put("NCA_EXEC_INTER_CP_ANUL", 0);
      this.Fields.put("NCA_CREDITO", 0);
      this.Fields.put("NCA_USO_IEPS", 0);
      this.Fields.put("NCA_TASA_IEPS", 0);
      this.Fields.put("NCA_IMPORTE_IEPS", 0.0);
      this.Fields.put("NCA_FORMADEPAGO", "");
      this.Fields.put("NCA_METODODEPAGO", "");
      this.Fields.put("NCA_SENDMAIL", 0);
      this.Fields.put("NCA_METODOPAGO", "");
      this.Fields.put("NC_FOLIO_ANUL", "");
      this.Fields.put("NCA_REGIMENFISCAL", "");
      this.Fields.put("NCA_NUMCUENTA", "");
      this.Fields.put("NCA_FECHA_VENCI", "");
      this.Fields.put("NCA_NUM_GUIA", "");
      this.Fields.put("NCA_TIPO_FLETE", 0);
      this.Fields.put("NCA_IMPORTE_FLETE", 0.0);
      this.Fields.put("NCA_ES_POR_PEDIDOS", 0);
      this.Fields.put("NCA_IMPORTE_PUNTOS", 0.0);
      this.Fields.put("NCA_IMPORTE_NEGOCIO", 0.0);
      this.Fields.put("NCA_NO_MLM", 0);
      this.Fields.put("MPE_ID", 0);
      this.Fields.put("NCA_POR_DESC", 0.0);
      this.Fields.put("NCA_CONSIGNACION", 0);
      this.Fields.put("CDE_ID", 0);
      this.Fields.put("DFA_ID", 0);
      this.Fields.put("NCA_RECU_FINAL", "");
      this.Fields.put("NCA_NO_EVENTOS", 0);
      this.Fields.put("TR_ID", 0);
      this.Fields.put("ME_ID", 0);
      this.Fields.put("TF_ID", 0);
      this.Fields.put("NCA_POR_DESCUENTO", 0.0);
      this.Fields.put("NCA_ES_CFD", 0);
      this.Fields.put("NCA_ES_CBB", 0);
      this.Fields.put("NCA_FECHA_COBRO", "");
      this.Fields.put("NCA_TURNO", 0);
      this.Fields.put("NCA_FOLIO_C", "");
      this.Fields.put("CTOA_ID", 0);
      this.Fields.put("NCA_NUM_MENS", 0);
      this.Fields.put("FACE_ID", 0);
      this.Fields.put("CC1_ID", 0);
      this.Fields.put("CXP_ID", 0);
      this.Fields.put("NCA_CADENA_TIMBRE", "");
      this.Fields.put("NCA_SELLOTIMBRE", "");
      this.Fields.put("NCA_HORA_TIMBRE", "");
      this.Fields.put("NCA_PATH_CBB", "");
      this.Fields.put("NCA_NOSERIECERTTIM", "");
      this.Fields.put("NCA_FOLIO_ANUL", "");
      this.Fields.put("NCA_SELLOTIMBRE_ANUL", "");
      this.Fields.put("NCA_HORA_TIMBRE_ANUL", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
