package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla maestra de pedidos
 *
 * @author zeus
 */
public class vta_pedidos extends TableMaster {

   /**
    * Constructor
    */
   public vta_pedidos() {
      super("vta_pedidos", "PD_ID", "", "");
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("PD_FOLIO", "");
      this.Fields.put("PD_FOLIO_C", "");
      this.Fields.put("PD_FECHA", "");
      this.Fields.put("PD_HORA", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("PD_IMPORTE", new Double(0));
      this.Fields.put("PD_IMPUESTO1", new Double(0));
      this.Fields.put("PD_IMPUESTO2", new Double(0));
      this.Fields.put("PD_IMPUESTO3", new Double(0));
      this.Fields.put("PD_TOTAL", new Double(0));
      this.Fields.put("PD_SALDO", new Double(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("PD_RAZONSOCIAL", "");
      this.Fields.put("PD_RFC", "");
      this.Fields.put("PD_CALLE", "");
      this.Fields.put("PD_COLONIA", "");
      this.Fields.put("PD_LOCALIDAD", "");
      this.Fields.put("PD_MUNICIPIO", "");
      this.Fields.put("PD_ESTADO", "");
      this.Fields.put("PD_CP", "");
      this.Fields.put("PD_TASA1", new Double(0));
      this.Fields.put("PD_TASA2", new Double(0));
      this.Fields.put("PD_TASA3", new Double(0));
      this.Fields.put("PD_O_REM", new Integer(0));
      this.Fields.put("PD_GANANCIA", new Double(0));
      this.Fields.put("ES_ID", new Integer(0));
      this.Fields.put("PD_ANULADA", new Integer(0));
      this.Fields.put("PD_FECHAANUL", "");
      this.Fields.put("PD_DIASCREDITO", new Integer(0));
      this.Fields.put("PD_NOTAS", "");
      this.Fields.put("PD_LPRECIOS", new Integer(0));
      this.Fields.put("PD_IDNC", new Integer(0));
      this.Fields.put("PD_ES_SURTIDO", new Integer(0));
      this.Fields.put("PD_US_ALTA", new Integer(0));
      this.Fields.put("PD_US_ANUL", new Integer(0));
      this.Fields.put("PD_US_MOD", new Integer(0));
      this.Fields.put("PD_MONEDA", new Integer(0));
      this.Fields.put("PD_TASAPESO", new Double(0));
      this.Fields.put("VE_ID", new Integer(0));
      this.Fields.put("PD_FECHACREATE", "");
      this.Fields.put("PD_ESSERV", new Integer(0));
      this.Fields.put("PD_HORANUL", "");
      this.Fields.put("PD_COSTO", new Double(0));
      this.Fields.put("PD_DESCUENTO", new Double(0));
      this.Fields.put("TKT_ID", new Integer(0));
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("PD_ESBK", new Integer(0));
      this.Fields.put("TKT_IDBK", new Integer(0));
      this.Fields.put("FAC_IDBK", new Integer(0));
      this.Fields.put("PD_IDBK", new Integer(0));
      this.Fields.put("PD_NOTASPIE", "");
      this.Fields.put("PD_REFERENCIA", "");
      this.Fields.put("PD_CONDPAGO", "");
      this.Fields.put("PD_ESRECU", new Integer(0));
      this.Fields.put("PD_PERIODICIDAD", new Integer(0));
      this.Fields.put("PD_DIAPER", new Integer(0));
      this.Fields.put("PD_VENCI", "");
      this.Fields.put("PD_RETISR", new Double(0));
      this.Fields.put("PD_RETIVA", new Double(0));
      this.Fields.put("PD_NETO", new Double(0));
      this.Fields.put("PD_ESARRENDA", new Integer(0));
      this.Fields.put("PD_NOMFORMATO", "");
      this.Fields.put("PD_NUMPEDI", "");
      this.Fields.put("PD_FECHAPEDI", "");
      this.Fields.put("PD_ADUANA", "");
      this.Fields.put("TI_ID", new Integer(0));
      this.Fields.put("TI_ID2", new Integer(0));
      this.Fields.put("TI_ID3", new Integer(0));
      this.Fields.put("PD_USO_IEPS", new Integer(0));
      this.Fields.put("PD_TASA_IEPS", new Integer(0));
      this.Fields.put("PD_IMPORTE_IEPS", new Double(0));
      this.Fields.put("PD_FORMADEPAGO", "");
      this.Fields.put("PD_NUMCUENTA", "");
      this.Fields.put("PD_METODODEPAGO", "");
      this.Fields.put("PD_REGIMENFISCAL", "");
      this.Fields.put("PD_STATUS", new Integer(0));
      this.Fields.put("PD_APARTADO", new Integer(0));
      this.Fields.put("PD_NUM_GUIA", "");
      this.Fields.put("PD_TIPO_FLETE", new Integer(0));
      this.Fields.put("PD_IMPORTE_FLETE", new Double(0));
      this.Fields.put("PD_IMPORTE_PUNTOS", new Double(0));
      this.Fields.put("PD_IMPORTE_NEGOCIO", new Double(0));
      this.Fields.put("PD_NO_MLM", new Integer(0));
      this.Fields.put("PD_CONSIGNACION", new Integer(0));
      this.Fields.put("CDE_ID", new Integer(0));
      this.Fields.put("DFA_ID", new Integer(0));
      this.Fields.put("MPE_ID", new Integer(0));
      this.Fields.put("PD_NO_EVENTOS", new Integer(0));
      this.Fields.put("PD_RECU_FINAL", "");
      this.setBolGetAutonumeric(true);
      this.Fields.put("TR_ID", new Integer(0));
      this.Fields.put("ME_ID", new Integer(0));
      this.Fields.put("TF_ID", new Integer(0));
      this.Fields.put("PD_POR_DESCUENTO", new Double(0));
      this.Fields.put("COT_ID", new Integer(0));
      this.Fields.put("CC1_ID", new Integer(0));
      this.Fields.put("PD_SERIE", "");
      
   }
}
