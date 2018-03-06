package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de promociones variables
 * @author aleph_79
 */
public class vta_promo_variables extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private boolean bolUso = false;

   /**
    * Define si se esta usando esta variable
    * @return Regresa true si la variable se uso
    */
   public boolean isBolUso() {
      return bolUso;
   }

   /**
    * Indica que se usta usando esta variable
    * @param bolUso Con true si la variable se uso
    */
   public void setBolUso(boolean bolUso) {
      this.bolUso = bolUso;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public vta_promo_variables() {
      super("vta_promo_variables", "PVAR_ID", "", "");
      this.Fields.put("PVAR_ID", new Integer(0));
      this.Fields.put("PVAR_VARIABLE", "");
      this.Fields.put("PVAR_NOMBRE", "");
      this.Fields.put("PVAR_TIPO", "");
      this.Fields.put("PVAR_SQL", "");
      this.Fields.put("PVAR_CLAS_CTE", new Integer(0));
      this.Fields.put("PVAR_VALOR_CLAS_CTE", new Integer(0));
      this.Fields.put("PVAR_CLAS_PROD", new Integer(0));
      this.Fields.put("PVAR_VALOR_CLAS_PROD", new Integer(0));
      this.Fields.put("PVAR_SCRIPT", "");
      this.Fields.put("PVAR_DESCRIPCION", "");
      this.Fields.put("PVAR_READONLY", new Integer(0));
      this.Fields.put("PVAR_DATO", "");
      this.Fields.put("PVAR_SQL_CTE_PROD", new Integer(0));

   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
