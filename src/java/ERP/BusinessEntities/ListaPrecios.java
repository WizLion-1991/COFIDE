/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP.BusinessEntities;

/**
 * Representa una lista de precios
 *
 * @author aleph_79
 */
public class ListaPrecios {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   private int intNumLista = 0;
   private double dblPorcentaje = 0;
   private String strFormula;
   private double dblPrecioNvo = 0;
   private int intLp_ConvMoneda = 0;
   private int intLp_TipoRedondeo = 0;
   private int intLp_RedondeoCalculo = 0;

   public double getDblPorcentaje() {
      return dblPorcentaje;
   }

   public void setDblPorcentaje(double dblPorcentaje) {
      this.dblPorcentaje = dblPorcentaje;
   }

   public int getIntNumLista() {
      return intNumLista;
   }

   public void setIntNumLista(int intNumLista) {
      this.intNumLista = intNumLista;
   }

   public int getIntLp_ConvMoneda() {
      return intLp_ConvMoneda;
   }

   public void setIntLp_ConvMoneda(int intLp_ConvMoneda) {
      this.intLp_ConvMoneda = intLp_ConvMoneda;
   }

   public int getIntLp_TipoRedondeo() {
      return intLp_TipoRedondeo;
   }

   public void setIntLp_TipoRedondeo(int intLp_TipoRedondeo) {
      this.intLp_TipoRedondeo = intLp_TipoRedondeo;
   }

   public int getIntLp_RedondeoCalculo() {
      return intLp_RedondeoCalculo;
   }

   public void setIntLp_RedondeoCalculo(int intLp_RedondeoCalculo) {
      this.intLp_RedondeoCalculo = intLp_RedondeoCalculo;
   }

   /**
    * Regresa la formula del precio
    *
    * @return Regresa una cadena con la formula del precio
    */
   public String getStrFormula() {
      return strFormula;
   }

   /**
    * Define la formula del precios
    *
    * @param strFormula Es una cadena con la formula del precio
    */
   public void setStrFormula(String strFormula) {
      this.strFormula = strFormula;
   }

   public double getDblPrecioNvo() {
      return dblPrecioNvo;
   }

   public void setDblPrecioNvo(double dblPrecioNvo) {
      this.dblPrecioNvo = dblPrecioNvo;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
