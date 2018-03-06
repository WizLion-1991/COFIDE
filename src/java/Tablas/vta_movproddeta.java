package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa el detalle del movimiento de productos
 * @author zeus
 */
public class vta_movproddeta extends TableMaster {
   private double dblCostoProrrateo = 0;
   private double dblProporProrrateo = 0;
   private int intEsPaquete = 0;
   private boolean bolMarca = false;
   private boolean bolUsaSeries = false;
   private int intMoneda = 0;
   /**
    * Regresa el costo de prorrateo
    * @return Regresa Es el importe del costo adicional por prorrateo
    */
   public double getDblCostoProrrateo() {
      return dblCostoProrrateo;
   }

   /**
    * Define el costo por aplicar al prorrateo
    * @param dblCostoProrrateo Es el importe del costo adicional por prorrateo
    */
   public void setDblCostoProrrateo(double dblCostoProrrateo) {
      this.dblCostoProrrateo = dblCostoProrrateo;
   }

   /**
    * Regresa la proporcion del item en base al global por prorratear
    * @return Es la proporcion
    */
   public double getDblProporProrrateo() {
      return dblProporProrrateo;
   }

   /**
    * Define la proporcion del item en base al global por prorratear
    * @param dblProporProrrateo Es la proporcion
    */ 
   public void setDblProporProrrateo(double dblProporProrrateo) {
      this.dblProporProrrateo = dblProporProrrateo;
   }

   /**
    * Nos indica si es un paquete
    * @return Regresa 1 si es un paquete
    */
   public int getIntEsPaquete() {
      return intEsPaquete;
   }

   /**
    * Definimos si es un paquete
    * @param intEsPaquete 1 si es un paquete
    */
   public void setIntEsPaquete(int intEsPaquete) {
      this.intEsPaquete = intEsPaquete;
   }

   /**
    * Regresa tru si esta marcado el item
    * @return Regresa true/false segun este marcado el item
    */
   public boolean isBolMarca() {
      return bolMarca;
   }

   /**
    * Marca el item
    * @param bolMarca true/false segun este marcado el item
    */
   public void setBolMarca(boolean bolMarca) {
      this.bolMarca = bolMarca;
   }

   /**
    * Indica si usa numero de serie el item
    * @return Regresa true si usa numero de serie
    */
   public boolean isBolUsaSeries() {
      return bolUsaSeries;
   }

   /**
    * Indica si usa numero de serie el item
    * @param bolUsaSeries Con true definimos si usa numero de serie
    */
   public void setBolUsaSeries(boolean bolUsaSeries) {
      this.bolUsaSeries = bolUsaSeries;
   }

   /**
    *Regresa la moneda
    * @return Es el id de la moneda
    */
   public int getIntMoneda() {
      return intMoneda;
   }

   /**
    *Define la moneda
    * @param intMoneda Es el id de la moneda
    */
   public void setIntMoneda(int intMoneda) {
      this.intMoneda = intMoneda;
   }
   
   /**
    * Constructor
    */
   public vta_movproddeta( ) {
      super("vta_movproddeta", "MPD_ID", "", "");
      this.Fields.put("MP_ID", new Integer(0));
      this.Fields.put("MPD_ID", new Integer(0));
      this.Fields.put("PR_ID", new Integer(0));
      this.Fields.put("PL_NUMLOTE", "");
      this.Fields.put("MPD_ENTRADAS", new Double(0));
      this.Fields.put("MPD_SALIDAS", new Double(0));
      this.Fields.put("MPD_FECHA", "");
      this.Fields.put("MPD_COSTO", new Double(0));
      this.Fields.put("MPD_COSTO_PRORRATEO", new Double(0));
      this.Fields.put("MPD_PROPOR_PRORRATEO", new Double(0));
      this.Fields.put("MPD_COSTO_PRORRATEO_CXP", new Double(0));
      this.Fields.put("MPD_PROPOR_PRORRATEO_CXP", new Double(0));
      this.Fields.put("PR_CODIGO", "");
      this.Fields.put("MPD_NOTAS", "");
      this.Fields.put("ID_USUARIOS", new Integer(0));
      this.Fields.put("SC_ID", new Integer(0));
      this.Fields.put("CXP_ID", new Integer(0));
      this.Fields.put("MPD_IDORIGEN", new Integer(0));
      this.Fields.put("MPD_CANT_CONF", new Double(0));
      this.Fields.put("MON_ID", new Integer(0));
      this.Fields.put("PR_ID_MASTER", new Integer(0));
      this.Fields.put("MPD_PARIDAD", new Double(0));
      this.Fields.put("MPD_SERIE_VENDIDO", new Integer(0));
      this.Fields.put("MPD_CADFECHA", "");
      this.setBolGetAutonumeric(false);
   }
}
