package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa el movimiento principal que afecta inventarios de productos
 * @author zeus
 */
public class vta_movprod extends TableMaster {

   /**
    * Constructor
    */
   public vta_movprod() {
      super("vta_movprod", "MP_ID", "", "");
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("MP_FECHA", "");
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("SC_ID2", new Integer(0));
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("ID_USUARIOS", new Integer(0));
      this.Fields.put("MP_NOTAS", "");
      this.Fields.put("OC_ID", new Integer(0));
      this.Fields.put("PROV_ID", new Integer(0));
      this.Fields.put("MP_HORA", "");
      this.Fields.put("MP_ANULADO", new Integer(0));
      this.Fields.put("MP_FECHAANUL", "");
      this.Fields.put("MP_HORANUL", "");
      this.Fields.put("ID_USUARIOSANUL", new Integer(0));
      this.Fields.put("FAC_ID", new Integer(0));
      this.Fields.put("TKT_ID", new Integer(0));
      this.Fields.put("PD_ID", new Integer(0));
      this.Fields.put("MP_FECHACREATE", "");
      this.Fields.put("MP_FECHALOTE", "");
      this.Fields.put("MP_ORIGENLOTE", "");
      this.Fields.put("MP_NUMLOTE", "");
      this.Fields.put("MP_CADFECHA", "");
      this.Fields.put("MP_FOLIO", "");
      this.Fields.put("TIN_ID", new Integer(0));
      this.Fields.put("MP_RECIBIDO", new Integer(0));
      this.Fields.put("MP_IDRECEP", new Integer(0));
      this.Fields.put("MP_IDORIGEN", new Integer(0));
      this.Fields.put("CXP_ID", new Integer(0));
      this.Fields.put("MP_FECHARECEP", "");
      this.Fields.put("MP_HORARECEP", "");
      this.Fields.put("MP_CANT_RECEP", new Double(0));
      this.Fields.put("MP_CANT_FALT", new Double(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("MP_CONF_RECEP", new Integer(0));
      this.Fields.put("MON_ID", new Integer(0));
      this.Fields.put("TMP_ID", new Integer(0));
   }
}
