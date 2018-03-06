/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class CompraAutomaticaDeta {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Importa_Nominas.class.getName());
    private String strResultLast;
    private int intID;
    private String strPR_CODIGO;    
    private String strCOM_FECHA;
    private int intCantidad;
    private double dblTotal;
    private double dblPrecioU;
    private double dblIVA;
    private String strUnidadMedida;
    private String strPR_DESCRIPCION;
    private double dblTasa_IVA;
    private int intPR_EXENTO1;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public CompraAutomaticaDeta() {
        this.strResultLast = "";        
        this.strCOM_FECHA = "";
        this.intID = 0;        
        this.intCantidad = 0;
        this.dblTotal = 0.0;
        this.dblIVA = 0.0;
        this.dblPrecioU = 0.0;
        this.strPR_DESCRIPCION ="";
        this.dblTasa_IVA=0.0;
        this.intPR_EXENTO1=0;
    }

    public String getStrUnidadMedida() {
        return strUnidadMedida;
    }

    public void setStrUnidadMedida(String strUnidadMedida) {
        this.strUnidadMedida = strUnidadMedida;
    }

    public double getDblPrecioU() {
        return dblPrecioU;
    }

    public void setDblPrecioU(double dblPrecioU) {
        this.dblPrecioU = dblPrecioU;
    }

    public void setStrResultLast(String strResultLast) {
        this.strResultLast = strResultLast;
    }

    public int getIntID() {
        return intID;
    }

    public void setIntID(int intID) {
        this.intID = intID;
    }

    public String getStrCOM_FECHA() {
        return strCOM_FECHA;
    }

    public void setStrCOM_FECHA(String strCOM_FECHA) {
        this.strCOM_FECHA = strCOM_FECHA;
    }

    public int getIntCantidad() {
        return intCantidad;
    }

    public void setIntCantidad(int intCantidad) {
        this.intCantidad = intCantidad;
    }

    public double getDblTotal() {
        return dblTotal;
    }

    public void setDblTotal(double dblTotal) {
        this.dblTotal = dblTotal;
    }

    public double getDblIVA() {
        return dblIVA;
    }

    public void setDblIVA(double dblIVA) {
        this.dblIVA = dblIVA;
    }

    public String getStrResultLast() {
        return strResultLast;
    }

    public String getStrPR_CODIGO() {
        return strPR_CODIGO;
    }

    public void setStrPR_CODIGO(String strPR_CODIGO) {
        this.strPR_CODIGO = strPR_CODIGO;
    }
    public String getStrPR_DESCRIPCION() {
        return strPR_DESCRIPCION;
    }

    public int getIntPR_EXENTO1() {
        return intPR_EXENTO1;
    }
    
    public double getDblTasa_IVA() {
        return dblTasa_IVA;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    

    public boolean Encuentra_Producto(int intProv,double dblTasaIVA, Conexion oConn) {


        String strSQL = "SELECT PR_ID,PR_EXENTO1,PR_DESCRIPCIONCOMPRA,PR_COSTOREPOSICION,ME_DESCRIPCION From vta_producto Join vta_unidadmedida On vta_unidadmedida.ME_ID = vta_producto.PR_UNIDADMEDIDA Where PR_CODIGO = '" + this.strPR_CODIGO + "'and PV_ID='" + intProv + "'";
        try {
            ResultSet rs = oConn.runQuery(strSQL);
            int cont = 0;
            while (rs.next()) {
                this.intID = rs.getInt("PR_ID");
                this.dblPrecioU = rs.getFloat("PR_COSTOREPOSICION");
                this.strUnidadMedida = rs.getString("ME_DESCRIPCION");
                this.strPR_DESCRIPCION = rs.getString("PR_DESCRIPCIONCOMPRA");
                this.dblTotal = this.dblPrecioU * this.intCantidad;
                this.dblIVA = this.dblTotal * dblTasaIVA;
                this.dblTasa_IVA = dblTasaIVA;
                this.intPR_EXENTO1 = rs.getInt("PR_EXENTO1");
                cont++;
            }
            if (cont == 0) {
                this.strResultLast = "ERROR: No se encuentra el producto";
                return false;
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage());
            this.strResultLast = "ERROR: " + ex.getMessage();

        }
        return true;


    }
    // </editor-fold>
}
