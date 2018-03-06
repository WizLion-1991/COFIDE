/*
 * Entidad de promocion
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa una promocion
 * @author aleph_79
 */
public class vta_promociones extends TableMaster {

   public vta_promociones() {
      super("vta_promociones", "PROM_ID", "", "");
      this.Fields.put("PROM_ID", new Integer(0));
      this.Fields.put("PROM_ACTIVO", new Integer(0));
      this.Fields.put("PROM_NOMBRE", "");
      this.Fields.put("PROM_FECHA_INI", "");
      this.Fields.put("PROM_FECHA_FIN", "");
      this.Fields.put("PROM_HORA_INI", "");
      this.Fields.put("PROM_HORA_FIN", "");
      this.Fields.put("PROM_USR_CREATE", new Integer(0));
      this.Fields.put("PROM_USR_MODI", new Integer(0));
      this.Fields.put("PROM_FECHA_CREATE", "");
      this.Fields.put("PROM_FECHA_MODI", "");
      this.Fields.put("PROM_HORA_CREATE", "");
      this.Fields.put("PROM_HORA_MODI", "");
      this.Fields.put("PROM_CONT_EXEC", new Integer(0));
      this.Fields.put("EMP_ID", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("PROM_FORMULA", "");
      this.Fields.put("PROM_DESCRIPCION", "");
      this.Fields.put("PROM_FASE", new Integer(0));
   }
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
