package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla maestra de cotizacion
 *
 * @author zeus
 */
public class vta_cotiza extends TableMaster {

   /**
    * Constructor
    */
   public vta_cotiza() {
      super("vta_cotiza", "COT_ID", "", "");
      this.Fields.put("COT_ID", new Integer(0));
      this.Fields.put("COT_FOLIO", "");
      this.Fields.put("COT_FECHA", "");
      this.Fields.put("COT_HORA", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("COT_IMPORTE", new Double(0));
      this.Fields.put("COT_IMPUESTO1", new Double(0));
      this.Fields.put("COT_IMPUESTO2", new Double(0));
      this.Fields.put("COT_IMPUESTO3", new Double(0));
      this.Fields.put("COT_TOTAL", new Double(0));
      this.Fields.put("COT_SALDO", new Double(0));
      this.Fields.put("SUC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("COT_RAZONSOCIAL", "");
      this.Fields.put("COT_RFC", "");
      this.Fields.put("COT_CALLE", "");
      this.Fields.put("COT_COLONIA", "");
      this.Fields.put("COT_LOCALIDAD", "");
      this.Fields.put("COT_MUNICIPIO", "");
      this.Fields.put("COT_ESTADO", "");
      this.Fields.put("COT_CP", "");
      this.Fields.put("COT_TASA1", new Double(0));
      this.Fields.put("COT_TASA2", new Double(0));
      this.Fields.put("COT_TASA3", new Double(0));
      this.Fields.put("COT_O_REM", new Integer(0));
      this.Fields.put("COT_GANANCIA", new Double(0));
      this.Fields.put("ES_ID", new Integer(0));
      this.Fields.put("COT_ANULADA", new Integer(0));
      this.Fields.put("COT_FECHAANUL", "");
      this.Fields.put("COT_DIASCREDITO", new Integer(0));
      this.Fields.put("COT_NOTAS", "");
      this.Fields.put("COT_LPRECIOS", new Integer(0));
      this.Fields.put("COT_IDNC", new Integer(0));
      this.Fields.put("COT_ES_SURTIDO", new Integer(0));
      this.Fields.put("COT_US_ALTA", new Integer(0));
      this.Fields.put("COT_US_ANUL", new Integer(0));
      this.Fields.put("COT_MONEDA", new Integer(0));
      this.Fields.put("COT_TASAPESO", new Double(0));
      this.Fields.put("VE_ID", new Integer(0));
      this.Fields.put("COT_FECHACREATE", "");
      this.Fields.put("COT_ESSERV", new Integer(0));
      this.Fields.put("COT_HORANUL", "");
      this.Fields.put("COT_COSTO", new Double(0));
      this.Fields.put("COT_DESCUENTO", new Double(0));
      this.Fields.put("COT_NUMERO", "");
      this.Fields.put("COT_NUMINT", "");
      this.Fields.put("COT_NUMPEDI", "");
      this.Fields.put("COT_FECHAPEDI", "");
      this.Fields.put("COT_ADUANA", "");
      this.Fields.put("TI_ID", new Integer(0));
      this.Fields.put("TI_ID2", new Integer(0));
      this.Fields.put("TI_ID3", new Integer(0));
      this.Fields.put("COT_USO_IEPS", new Integer(0));
      this.Fields.put("COT_TASA_IEPS", new Integer(0));
      this.Fields.put("COT_IMPORTE_IEPS", new Double(0));
      this.Fields.put("COT_SELLOTIMBRE", "");
      this.Fields.put("COT_HORA_TIMBRE", "");
      this.Fields.put("COT_SELLOTIMBRE_ANUL", "");
      this.Fields.put("COT_HORA_TIMBRE_ANUL", "");
      this.Fields.put("COT_FOLIO_ANUL", "");
      this.Fields.put("COT_CADENA_TIMBRE", "");
      this.Fields.put("COT_NUMCUENTA", "");
      this.Fields.put("COT_NUM_GUIA", "");
      this.Fields.put("COT_TIPO_FLETE", new Integer(0));
      this.Fields.put("COT_IMPORTE_FLETE", new Double(0));
      this.Fields.put("COT_IMPORTE_PUNTOS", new Double(0));
      this.Fields.put("COT_IMPORTE_NEGOCIO", new Double(0));
      this.Fields.put("COT_NO_MLM", new Integer(0));
      this.Fields.put("MPE_ID", new Integer(0));
      this.Fields.put("COT_CONSIGNACION", new Integer(0));
      this.Fields.put("CDE_ID", new Integer(0));
      this.Fields.put("DFA_ID", new Integer(0));
      this.Fields.put("COT_RECU_FINAL", "");
      this.Fields.put("COT_NO_EVENTOS", new Integer(0));
      this.Fields.put("TR_ID", new Integer(0));
      this.Fields.put("ME_ID", new Integer(0));
      this.Fields.put("TF_ID", new Integer(0));
      this.Fields.put("COT_POR_DESCUENTO", new Double(0));
      this.Fields.put("COT_TURNO", new Integer(0));
      this.Fields.put("COT_FOLIO_C", "");
      this.Fields.put("COT_CONDPAGO", "");
      this.Fields.put("COT_DIAPER", new Integer(0));
      this.Fields.put("COT_ESRECU", new Integer(0));
      this.Fields.put("COT_FORMADEPAGO", "");
      this.Fields.put("COT_METODODEPAGO", "");
      this.Fields.put("COT_NOTASPIE", "");
      this.Fields.put("COT_PERIODICIDAD", new Integer(0));
      this.Fields.put("COT_REFERENCIA", "");
      this.Fields.put("COT_REGIMENFISCAL", "");
      this.Fields.put("COT_TIPOCOMP", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("TKT_ID", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("CC1_ID", new Integer(0));
      this.Fields.put("COT_SERIE", "");
      this.setBolGetAutonumeric(true);
   }
}
