package ERP;

/**
 *Esta clase realiza los calculos de impuestos
 * @author zeus
 */
public class Impuestos {

   protected double dblImpuesto1;
   protected double dblImpuesto2;
   protected double dblImpuesto3;
   protected double dblTasa1;
   protected double dblTasa2;
   protected double dblTasa3;
   protected int intSImp1_2;
   protected int intSImp1_3;
   protected int intSImp2_3;

   public double getDblImpuesto1() {
      return dblImpuesto1;
   }

   public void setDblImpuesto1(double dblImpuesto1) {
      this.dblImpuesto1 = dblImpuesto1;
   }

   public double getDblImpuesto2() {
      return dblImpuesto2;
   }

   public void setDblImpuesto2(double dblImpuesto2) {
      this.dblImpuesto2 = dblImpuesto2;
   }

   public double getDblImpuesto3() {
      return dblImpuesto3;
   }

   public void setDblImpuesto3(double dblImpuesto3) {
      this.dblImpuesto3 = dblImpuesto3;
   }

   public double getDblTasa1() {
      return dblTasa1;
   }

   public void setDblTasa1(double dblTasa1) {
      this.dblTasa1 = dblTasa1;
   }

   public double getDblTasa2() {
      return dblTasa2;
   }

   public void setDblTasa2(double dblTasa2) {
      this.dblTasa2 = dblTasa2;
   }

   public double getDblTasa3() {
      return dblTasa3;
   }

   public void setDblTasa3(double dblTasa3) {
      this.dblTasa3 = dblTasa3;
   }

   public int getIntSImp1_2() {
      return intSImp1_2;
   }

   public void setIntSImp1_2(int intSImp1_2) {
      this.intSImp1_2 = intSImp1_2;
   }

   public int getIntSImp1_3() {
      return intSImp1_3;
   }

   public void setIntSImp1_3(int intSImp1_3) {
      this.intSImp1_3 = intSImp1_3;
   }

   public int getIntSImp2_3() {
      return intSImp2_3;
   }

   public void setIntSImp2_3(int intSImp2_3) {
      this.intSImp2_3 = intSImp2_3;
   }

   /**
    * Constructor del objeto
    * @param dblTasa1 Es la tasa del impuesto 1
    * @param dblTasa2 Es la tasa del impuesto 2
    * @param dblTasa3 Es la tasa del impuesto 3
    * @param intSImp1_2 Indica si el importe del impuesto 1 se usa para calcular el impuesto 2
    * @param intSImp1_3 Indica si el importe del impuesto 1 se usa para calcular el impuesto 3
    * @param intSImp2_3 Indica si el importe del impuesto 2 se usa para calcular el impuesto 3
    */
   public Impuestos(double dblTasa1, double dblTasa2, double dblTasa3,
           int intSImp1_2, int intSImp1_3, int intSImp2_3) {
      this.dblTasa1 = dblTasa1;
      this.dblTasa2 = dblTasa2;
      this.dblTasa3 = dblTasa3;
      this.intSImp1_2 = intSImp1_2;
      this.intSImp1_3 = intSImp1_3;
      this.intSImp2_3 = intSImp2_3;
   }

   /**
    * Calcula el impuesto quitandoselo de la base
    * @param dblBase1 Es la base 1
    * @param dblBase2 Es la base 2
    * @param dblBase3 Es la base 3
    */
   public void CalculaImpuesto(double dblBase1, double dblBase2, double dblBase3) {
      //Inicializamos los impuestos en ceros
      this.dblImpuesto1 = 0;
      this.dblImpuesto2 = 0;
      this.dblImpuesto3 = 0;
      //Calculamos el impuesto 1
      if (dblBase1 > 0) {
         this.dblImpuesto1 = dblBase1 - (dblBase1) / (1 + (dblTasa1 / 100));
      }
      //Calculamos el impuesto 2
      if (dblBase2 > 0) {
         if ((intSImp1_2) > 0) {
            this.dblImpuesto2 = dblBase2 - (dblBase2 + this.dblImpuesto1) / (1 + (dblTasa2 / 100));
         } else {
            this.dblImpuesto2 = dblBase2 - (dblBase2) / (1 + (dblTasa2 / 100));
         }
      }
      //Calculamos el impuesto 3
      if (dblBase3 > 0) {
         if ((intSImp1_3) > 0) {
            if ((intSImp2_3) > 0) {
               this.dblImpuesto3 = dblBase3 - (dblBase3 + this.dblImpuesto1 + this.dblImpuesto2) / (1 + (dblTasa3 / 100));
            } else {
               this.dblImpuesto3 = dblBase3 - (dblBase3 + this.dblImpuesto1) / (1 + (dblTasa3 / 100));
            }
         } else {
            if ((intSImp2_3) > 0) {
               this.dblImpuesto3 = dblBase3 - (dblBase3 + this.dblImpuesto2) / (1 + (dblTasa3 / 100));
            }

         }
      }
   }

   /**
    * Calcula el impuesto agregandoselo al importe
    * @param dblBaseGravable Es la base gravable 1
    * @param dblBaseGravable2 Es la base gravable 2
    * @param dblBaseGravable3 Es la base gravable 3
    */
   public void CalculaImpuestoMas(double dblBaseGravable,
           double dblBaseGravable2,
           double dblBaseGravable3) {
      double dblRepo1 = 0;
      double dblRepo2 = 0;
      double dblRepo3 = 0;
      //Imp1
      dblRepo1 = dblBaseGravable * (this.dblTasa1 / 100);
      //Imp2
      if (this.intSImp1_2 == 1) {
         dblRepo2 = dblBaseGravable2 * (1 + (this.dblTasa1 / 100)) * (this.dblTasa2 / 100);
      } else {
         dblRepo2 = dblBaseGravable2 * (this.dblTasa2 / 100);
      }
      //Imp3
      if (this.intSImp1_3 == 0 && this.intSImp2_3 == 0) {
         dblRepo3 = dblBaseGravable3 * (this.dblTasa3 / 100);
      }
      if (this.intSImp1_3 == 0 && this.intSImp2_3 == 1) {
         dblRepo3 = dblBaseGravable3 * (1 + (this.dblTasa2 / 100)) * (this.dblTasa3 / 100);
      }
      if (this.intSImp1_3 == 1 && this.intSImp2_3 == 0) {
         dblRepo3 = dblBaseGravable3 * (1 + (this.dblTasa1 / 100)) * (this.dblTasa3 / 100);
      }
      if (this.intSImp1_3 == 1 && this.intSImp2_3 == 1) {
         dblRepo3 = dblBaseGravable3 * (1 + (this.dblTasa1 / 100)) * (1 + (this.dblTasa2 / 100)) * (this.dblTasa3 / 100);
      }

      this.dblImpuesto1 = dblRepo1;
      this.dblImpuesto2 = dblRepo2;
      this.dblImpuesto3 = dblRepo3;
   }
}
