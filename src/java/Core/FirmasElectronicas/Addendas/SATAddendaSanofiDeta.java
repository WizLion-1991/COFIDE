/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Core.FirmasElectronicas.Addendas;

/**
 *
 * @author siweb
 */
public class SATAddendaSanofiDeta {

    double dblPrecio;
    int intCantidad;
    String strPosicion;

    public double getDblImporte() {
        return dblImporte;
    }
    double dblTasaIva;
    double dblImpuesto1;
    String strUMedida;
    double dblImporte;

    public void setDblImporte(double dblImporte) {
        this.dblImporte = dblImporte;
    }

    public void setDblPrecio(double dblPrecio) {
        this.dblPrecio = dblPrecio;
    }

    public void setIntCantidad(int intCantidad) {
        this.intCantidad = intCantidad;
    }

    public void setDblTasaIva(double dblTasaIva) {
        this.dblTasaIva = dblTasaIva;
    }

    public void setDblImpuesto1(double dblImpuesto1) {
        this.dblImpuesto1 = dblImpuesto1;
    }

    public void setStrUMedida(String strUMedida) {
        this.strUMedida = strUMedida;
    }

    public double getDblPrecio() {
        return dblPrecio;
    }

    public int getIntCantidad() {
        return intCantidad;
    }

    public double getDblTasaIva() {
        return dblTasaIva;
    }

    public double getDblImpuesto1() {
        return dblImpuesto1;
    }

    public String getStrUMedida() {
        return strUMedida;
    }

    public SATAddendaSanofiDeta() {
    }

   public String getStrPosicion() {
      return strPosicion;
   }

   public void setStrPosicion(String strPosicion) {
      this.strPosicion = strPosicion;
   }

    
}
