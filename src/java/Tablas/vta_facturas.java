package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa las facturas del sistema
 *
 * @author zeus
 */
public class vta_facturas extends TableMaster {

    /**
     * Constructor
     */
    public vta_facturas() {
        super("vta_facturas", "FAC_ID", "", "");
        this.Fields.put("FAC_ID", new Integer(0));
        this.Fields.put("FAC_FOLIO", "");
        this.Fields.put("FAC_FECHA", "");
        this.Fields.put("FAC_HORA", "");
        this.Fields.put("CT_ID", new Integer(0));
        this.Fields.put("FAC_IMPORTE", new Double(0));
        this.Fields.put("FAC_IMPUESTO1", new Double(0));
        this.Fields.put("FAC_IMPUESTO2", new Double(0));
        this.Fields.put("FAC_IMPUESTO3", new Double(0));
        this.Fields.put("FAC_TOTAL", new Double(0));
        this.Fields.put("FAC_SALDO", new Double(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("EMP_ID", new Integer(0));
        this.Fields.put("FAC_RAZONSOCIAL", "");
        this.Fields.put("FAC_RFC", "");
        this.Fields.put("FAC_CALLE", "");
        this.Fields.put("FAC_COLONIA", "");
        this.Fields.put("FAC_LOCALIDAD", "");
        this.Fields.put("FAC_MUNICIPIO", "");
        this.Fields.put("FAC_ESTADO", "");
        this.Fields.put("FAC_CP", "");
        this.Fields.put("FAC_TASA1", new Double(0));
        this.Fields.put("FAC_TASA2", new Double(0));
        this.Fields.put("FAC_TASA3", new Double(0));
        this.Fields.put("FAC_O_REM", new Integer(0));
        this.Fields.put("FAC_GANANCIA", new Double(0));
        this.Fields.put("ES_ID", new Integer(0));
        this.Fields.put("FAC_ANULADA", new Integer(0));
        this.Fields.put("FAC_FECHAANUL", "");
        this.Fields.put("FAC_DIASCREDITO", new Integer(0));
        this.Fields.put("FAC_NOTAS", "");
        this.Fields.put("FAC_LPRECIOS", new Integer(0));
        this.Fields.put("FAC_IDNC", new Integer(0));
        this.Fields.put("FAC_ES_SURTIDO", new Integer(0));
        this.Fields.put("FAC_US_ALTA", new Integer(0));
        this.Fields.put("FAC_US_ANUL", new Integer(0));
        this.Fields.put("FAC_MONEDA", new Integer(0));
        this.Fields.put("FAC_TASAPESO", new Double(0));
        this.Fields.put("VE_ID", new Integer(0));
        this.Fields.put("FAC_FECHACREATE", "");
        this.Fields.put("FAC_ESSERV", new Integer(0));
        this.Fields.put("FAC_HORANUL", "");
        this.Fields.put("FAC_COSTO", new Double(0));
        this.Fields.put("FAC_DESCUENTO", new Double(0));
        this.Fields.put("FAC_CADENAORIGINAL", "");
        this.Fields.put("FAC_SELLO", "");
        this.Fields.put("CCJ_ID", new Integer(0));
        this.Fields.put("FAC_ESMASIVA", new Integer(0));
        this.Fields.put("FAC_NUMERO", "");
        this.Fields.put("FAC_NUMINT", "");
        this.Fields.put("FAC_SERIE", "");
        this.Fields.put("FAC_NOAPROB", "");
        this.Fields.put("FAC_FECHAAPROB", "");
        this.Fields.put("PD_ID", new Integer(0));
        this.Fields.put("FAC_TIPOCOMP", new Integer(0));
        this.Fields.put("FAC_RETISR",0);
        this.Fields.put("FAC_RETIVA",0);
        this.Fields.put("FAC_NETO",0);
        this.Fields.put("FAC_NOTASPIE", "");
        this.Fields.put("FAC_REFERENCIA", "");
        this.Fields.put("FAC_CONDPAGO", "");
        this.Fields.put("FAC_ESARRENDA", new Integer(0));
        this.Fields.put("FAC_NOMFORMATO", "");
        this.Fields.put("FAC_EXEC_INTER_CP", new Integer(0));
        this.Fields.put("FAC_ESRECU", new Integer(0));
        this.Fields.put("FAC_PERIODICIDAD", new Integer(0));
        this.Fields.put("FAC_DIAPER", new Integer(0));
        this.Fields.put("FAC_NUMPEDI", "");
        this.Fields.put("FAC_FECHAPEDI", "");
        this.Fields.put("FAC_ADUANA", "");
        this.Fields.put("TI_ID", new Integer(0));
        this.Fields.put("TI_ID2", new Integer(0));
        this.Fields.put("TI_ID3", new Integer(0));
        this.Fields.put("FAC_EXEC_INTER_CP_ANUL", new Integer(0));
        this.Fields.put("FAC_CREDITO", new Integer(0));
        this.Fields.put("FAC_USO_IEPS", new Integer(0));
        this.Fields.put("FAC_TASA_IEPS", new Integer(0));
        this.Fields.put("FAC_IMPORTE_IEPS", new Double(0));
        this.Fields.put("FAC_FORMADEPAGO", "");
        this.Fields.put("FAC_METODODEPAGO", "");
        this.Fields.put("FAC_UUID", "");
        this.Fields.put("FAC_SENDMAIL", new Integer(0));
        this.Fields.put("FAC_METODOPAGO", "");
        this.Fields.put("FAC_SELLOTIMBRE", "");
        this.Fields.put("FAC_HORA_TIMBRE", "");
        this.Fields.put("FAC_FOLIO_ANUL", "");
        this.Fields.put("FAC_SELLOTIMBRE_ANUL", "");
        this.Fields.put("FAC_HORA_TIMBRE_ANUL", "");
        this.Fields.put("NC_FOLIO_ANUL", "");
        this.Fields.put("TKT_FOLIO_ANUL", "");
        this.Fields.put("PD_FOLIO_ANUL", "");
        this.Fields.put("COT_FOLIO_ANUL", "");
        this.Fields.put("FAC_CADENA_TIMBRE", "");
        this.Fields.put("FAC_PATH_CBB", "");
        this.Fields.put("FAC_NOSERIECERTTIM", "");
        this.Fields.put("FAC_REGIMENFISCAL", "");
        this.Fields.put("FAC_NUMCUENTA", "");
        this.Fields.put("FAC_FECHA_VENCI", "");
        this.Fields.put("FAC_NUM_GUIA", "");
        this.Fields.put("FAC_TIPO_FLETE", new Integer(0));
        this.Fields.put("FAC_IMPORTE_FLETE", new Double(0));
        this.Fields.put("FAC_ES_POR_PEDIDOS", new Integer(0));
        this.Fields.put("FAC_IMPORTE_PUNTOS", new Double(0));
        this.Fields.put("FAC_IMPORTE_NEGOCIO", new Double(0));
        this.Fields.put("FAC_NO_MLM", new Integer(0));
        this.Fields.put("MPE_ID", new Integer(0));
        this.Fields.put("FAC_POR_DESC", new Double(0));
        this.Fields.put("FAC_CONSIGNACION", new Integer(0));
        this.Fields.put("CDE_ID", new Integer(0));
        this.Fields.put("DFA_ID", new Integer(0));
        this.Fields.put("FAC_RECU_FINAL", "");
        this.Fields.put("FAC_NO_EVENTOS", new Integer(0));
        this.Fields.put("TR_ID", new Integer(0));
        this.Fields.put("ME_ID", new Integer(0));
        this.Fields.put("TF_ID", new Integer(0));
        this.Fields.put("FAC_POR_DESCUENTO", new Double(0));
        this.Fields.put("FAC_ES_CFD", new Integer(0));
        this.Fields.put("FAC_ES_CBB", new Integer(0));
        this.Fields.put("FAC_FECHA_COBRO", "");
        this.Fields.put("PD_RECU_ID", new Integer(0));
        this.Fields.put("FAC_TURNO", new Integer(0));
        this.Fields.put("FAC_FOLIO_C", "");
        this.Fields.put("CTOA_ID", new Integer(0));
        this.Fields.put("FAC_NUM_MENS", new Integer(0));
        this.Fields.put("COT_ID", new Integer(0));
        this.Fields.put("FACE_ID", new Integer(0));
        this.Fields.put("KL_CICLO", new Integer(0));
        this.Fields.put("CC1_ID", new Integer(0));
        this.Fields.put("CAMP_ID", new Integer(0));
        this.Fields.put("FAC_COFIDE_PAGADO", new Integer(0));
        this.Fields.put("CC_CURSO_ID", new Integer(0));
        this.Fields.put("COFIDE_NVO", new Integer(0));
        this.Fields.put("FAC_IMP_PAGADO", new Double(0));
        this.Fields.put("FAC_ES_RESERVACION", new Integer(0));
        this.Fields.put("APC_ID", new Integer(0));
        this.Fields.put("FAC_INBOUND", new Integer(0));
        this.Fields.put("MP_ID", new Integer(0));
        this.Fields.put("FAC_COFIDE_VALIDA", new Integer(0));
        this.Fields.put("FAC_NOMPAGO", "");
        this.Fields.put("FAC_ID_OLD", new Integer(0));
        this.Fields.put("COFIDE_DUPLICIDAD", new Integer(0));
        this.Fields.put("COFIDE_DUPLICIDAD_ID", new Integer(0));
        this.Fields.put("FAC_TIPO_CURSO", new Integer(0));
        this.Fields.put("FAC_US_MOD", new Integer(0));
        this.Fields.put("COFIDE_CARRITO", new Integer(0));
        this.Fields.put("FAC_MOTIVO_DENEGADA", "");
        this.Fields.put("FAC_CANCEL", new Integer(0));
        this.Fields.put("FAC_PROMO", new Integer(0));
        this.Fields.put("FAC_ID_MOV", new Integer(0));
        this.Fields.put("FAC_FACTURAR", new Integer(0));
        this.Fields.put("FAC_COMISION_OBSERVACIONES", "");
        this.Fields.put("FAC_COMISION_PAGADA", new Integer(0));
        this.Fields.put("FAC_COMISION_TOTAL", new Integer(0));
        this.Fields.put("FAC_CONFIRMACION", "");
        this.Fields.put("FAC_ES_CFDI33", new Integer(0));
        this.Fields.put("FAC_FECHA_PAGO_COMISION", "");
        this.Fields.put("FAC_ID_FAC_RELACION", new Integer(0));
        this.Fields.put("FAC_ISR_FACTOR", new Double(0));
        this.Fields.put("FAC_MONTO_SEGURO", new Double(0));
        this.Fields.put("FAC_NUM_REG_ID_TRIB", "");
        this.Fields.put("FAC_RESIDENCIA_FISCAL", "");
        this.Fields.put("FAC_TASACAMBIO", new Double(0));
        this.Fields.put("FAC_TREL_CLAVE", "");
        this.Fields.put("FAC_RFCPROVCERTIF", new Double(0));
        this.Fields.put("FAC_TIENE_FLETE", new Integer(0));
        this.Fields.put("FAC_TIENE_SEGURO", new Integer(0));
        this.Fields.put("FAC_INE", new Integer(0));
        this.Fields.put("FAC_USO_CFDI", "");
        this.Fields.put("TKT_FACTURAR", new Integer(0));
        this.setBolGetAutonumeric(true);
    }
}