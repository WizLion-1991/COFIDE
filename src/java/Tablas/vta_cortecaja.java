package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa un corte de caja
 * @author zeus
 */
public class vta_cortecaja extends TableMaster {

   public vta_cortecaja() {
      super("vta_cortecaja", "CCJ_ID", "", "");
      this.Fields.put("CCJ_ID", new Integer(0));
      this.Fields.put("CCJ_FECHA", "");
      this.Fields.put("CCJ_FECHAREAL", "");
      this.Fields.put("CCJ_HORAREAL", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("CCJ_EFECTIVO", new Double(0));
      this.Fields.put("CCJ_CHEQUE", new Double(0));
      this.Fields.put("CCJ_TC", new Double(0));
      this.Fields.put("CCJ_SALDO", new Double(0));
      this.Fields.put("CCJ_EFECTIVOREAL", new Double(0));
      this.Fields.put("CCJ_CHEQUEREAL", new Double(0));
      this.Fields.put("CCJ_TCREAL", new Double(0));
      this.Fields.put("CCJ_SALDOREAL", new Double(0));
      this.Fields.put("CCJ_TOTALTICKETS", new Double(0));
      this.Fields.put("CCJ_TOTALFACTURA", new Double(0));
      this.Fields.put("CCJ_TOTALPAGOS", new Double(0));
      this.Fields.put("IDUSUARIO", new Integer(0));
      this.Fields.put("CCJ_TURNO", new Integer(0));
      this.Fields.put("CCJ_TICKETIMPUESTO1", new Double(0));
      this.Fields.put("CCJ_TICKETIMPUESTO2", new Double(0));
      this.Fields.put("CCJ_TICKETIMPUESTO3", new Double(0));
      this.Fields.put("CCJ_FACTURAIMPUESTO1", new Double(0));
      this.Fields.put("CCJ_FACTURAIMPUESTO2", new Double(0));
      this.Fields.put("CCJ_FACTURAIMPUESTO3", new Double(0));
      this.Fields.put("CCJ_PAGOIMPUESTO1", new Double(0));
      this.Fields.put("CCJ_PAGOIMPUESTO2", new Double(0));
      this.Fields.put("CCJ_PAGOIMPUESTO3", new Double(0));
      this.Fields.put("CCJ_TICKETIMPORTE", new Double(0));
      this.Fields.put("CCJ_FACTURAIMPORTE", new Double(0));
      this.Fields.put("CCJ_PAGOIMPORTE", new Double(0));
      this.Fields.put("CCJ_ANULADO", new Integer(0));
      this.Fields.put("IDUSUARIOANULA", new Integer(0));
      this.Fields.put("CCJ_FECHAANUL", "");
      this.Fields.put("CCJ_HORAANUL", "");

   }
}
