package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa una nota de credito
 *
 * @author zeus
 */
public class vta_ncreditoprov extends TableMaster {

    /**
     * Constructor
     */
    public vta_ncreditoprov() {
        super("vta_ncreditoprov", "NC_ID", "", "");
        this.Fields.put("NC_ID", new Integer(0));
        this.Fields.put("NC_FOLIO", "");
        this.Fields.put("NC_FECHA", "");
        this.Fields.put("NC_HORA", "");
        this.Fields.put("PV_ID", new Integer(0));
        this.Fields.put("NC_IMPORTE", new Double(0));
        this.Fields.put("NC_IMPUESTO1", new Double(0));
        this.Fields.put("NC_IMPUESTO2", new Double(0));
        this.Fields.put("NC_IMPUESTO3", new Double(0));
        this.Fields.put("NC_TOTAL", new Double(0));
        this.Fields.put("NC_SALDO", new Double(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("EMP_ID", new Integer(0));
        this.Fields.put("NC_RAZONSOCIAL", "");
        this.Fields.put("NC_RFC", "");
        this.Fields.put("NC_CALLE", "");
        this.Fields.put("NC_COLONIA", "");
        this.Fields.put("NC_LOCALIDAD", "");
        this.Fields.put("NC_MUNICIPIO", "");
        this.Fields.put("NC_ESTADO", "");
        this.Fields.put("NC_CP", "");
        this.Fields.put("NC_TASA1", new Double(0));
        this.Fields.put("NC_TASA2", new Double(0));
        this.Fields.put("NC_TASA3", new Double(0));
        this.Fields.put("NC_O_REM", new Integer(0));
        //this.Fields.put("NC_GANANCIA", new Double(0));
       // this.Fields.put("ES_ID", new Integer(0));
        this.Fields.put("NC_ANULADA", new Integer(0));
        this.Fields.put("NC_FECHAANUL", "");
        this.Fields.put("NC_DIASCREDITO", new Integer(0));
        this.Fields.put("NC_NOTAS", "");
        this.Fields.put("NC_LPRECIOS", new Integer(0));
        this.Fields.put("NC_IDNC", new Integer(0));
        this.Fields.put("NC_ES_SURTIDO", new Integer(0));
        this.Fields.put("NC_US_ALTA", new Integer(0));
        this.Fields.put("NC_US_ANUL", new Integer(0));
        this.Fields.put("NC_MONEDA", new Integer(0));
        this.Fields.put("NC_TASAPESO", new Double(0));
       // this.Fields.put("VE_ID", new Integer(0));
        this.Fields.put("NC_FECHACREATE", "");
        this.Fields.put("NC_ESSERV", new Integer(0));
        this.Fields.put("NC_HORANUL", "");
        this.Fields.put("NC_COSTO", new Double(0));
        this.Fields.put("NC_DESCUENTO", new Double(0));
      //  this.Fields.put("NC_CADENAORIGINAL", "");
      //  this.Fields.put("NC_SELLO", "");
        this.Fields.put("CCJ_ID", new Integer(0));
        this.Fields.put("NC_ESMASIVA", new Integer(0));
        this.Fields.put("NC_NUMERO", "");
        this.Fields.put("NC_NUMINT", "");
       // this.Fields.put("NC_SERIE", "");
      //  this.Fields.put("NC_NOAPROB", "");
      //  this.Fields.put("NC_FECHAAPROB", "");
      //  this.Fields.put("PD_ID", new Integer(0));
       // this.Fields.put("NC_TIPOCOMP", new Integer(0));
        this.Fields.put("NC_RETISR", new Double(0));
        this.Fields.put("NC_RETIVA", new Double(0));
        this.Fields.put("NC_NETO", new Double(0));
        this.Fields.put("NC_NOTASPIE", "");
        this.Fields.put("NC_REFERENCIA", "");
        this.Fields.put("NC_CONDPAGO", "");
       // this.Fields.put("NC_ESARRENDA", new Integer(0));
        this.Fields.put("NC_NOMFORMATO", "");
        this.Fields.put("NC_EXEC_INTER_CP", new Integer(0));
      //  this.Fields.put("NC_ESRECU", new Integer(0));
     //   this.Fields.put("NC_PERIODICIDAD", new Integer(0));
      //  this.Fields.put("NC_DIAPER", new Integer(0));
      //  this.Fields.put("NC_NUMPEDI", "");
      //  this.Fields.put("NC_FECHAPEDI", "");
      //  this.Fields.put("NC_ADUANA", "");
        this.Fields.put("TI_ID", new Integer(0));
        this.Fields.put("TI_ID2", new Integer(0));
        this.Fields.put("TI_ID3", new Integer(0));
        this.Fields.put("NC_USO_IEPS", new Integer(0));
        this.Fields.put("NC_TASA_IEPS", new Integer(0));
        this.Fields.put("NC_IMPORTE_IEPS", new Double(0));
    //    this.Fields.put("NC_REGIMENFISCAL", "");
        this.Fields.put("CXP_ID", new Integer(0));
      //  this.Fields.put("TKT_ID", new Integer(0));
        this.Fields.put("NC_SC_ID2", new Integer(0));
    }
}
