package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa el movimiento principal que afecta CAJA/BANCOS
 *
 * @author zeus
 */
public class vta_mov_cta_bcos extends TableMaster {

   /**
    * Representa el movimiento principal que afecta bancos
    */
   public vta_mov_cta_bcos() {
      super("vta_mov_cta_bcos", "MCB_ID", "", "");
      this.Fields.put("MCB_ID", new Integer(0));
      this.Fields.put("BC_ID", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("MCB_FECHA", "");
      this.Fields.put("MCB_HORA", "");
      this.Fields.put("ID_USUARIOS", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("MCB_CONCILIADO", new Integer(0));
      this.Fields.put("MCB_DEPOSITO", new Double(0));
      this.Fields.put("MCB_RETIRO", new Double(0));
      this.Fields.put("ID_USUARIOSANUL", new Integer(0));
      this.Fields.put("MCB_ANULADO", new Integer(0));
      this.Fields.put("MCB_FECHANUL", "");
      this.Fields.put("MCB_HORAANUL", "");
      this.Fields.put("MCB_FECHACREATE", "");
      this.Fields.put("MCB_CONCEPTO", "");
      this.Fields.put("MCB_BENEFICIARIO", "");
      this.Fields.put("MCB_RFCBENEFICIARIO", "");
      this.Fields.put("MCB_NOCHEQUE", "");
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("TKT_ID", new Integer(0));
      this.Fields.put("OC_ID", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("MC_ID", new Integer(0));
      this.Fields.put("CXP_ID", new Integer(0));
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("MCB_MONEDA", new Integer(0));
      this.Fields.put("MCB_PARIDAD", new Double(0));
      this.Fields.put("MCB_TIPO1", new Integer(0));
      this.Fields.put("MCB_TIPO2", new Integer(0));
      this.Fields.put("MCB_TIPO3", new Integer(0));
      this.Fields.put("MCB_FECENTREGADOC", "");
      this.Fields.put("MCB_NOTAS_ENT", "");
      this.Fields.put("MCB_DEV_PROV", new Integer(0));
      this.Fields.put("MCB_DEV_CLIE", new Integer(0));
      this.Fields.put("MP_ID_DEV", new Integer(0));
      this.Fields.put("MC_ID_DEV", new Integer(0));
      this.Fields.put("MCB_TRASPASO", new Integer(0));//INT
      this.Fields.put("BC_ID2", new Integer(0));
      this.Fields.put("MCB_PARIDAD2", new Double(0));
      this.Fields.put("MCB_PARIDAD_DEV", new Double(0));
      this.Fields.put("MCB_TRAS_ORIGEN", new Integer(0));
      this.Fields.put("RBK_CVE", "");
      this.Fields.put("MCB_CTA_DESTINO", "");
      this.Fields.put("MCB_ES_TRASPASO", new Integer(0));
      this.Fields.put("MCB_EXEC_INTER_CP", new Integer(0));
   }
}
