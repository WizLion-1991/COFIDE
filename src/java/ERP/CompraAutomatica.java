/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import comSIWeb.Operaciones.Conexion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class CompraAutomatica {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CompraAutomatica.class.getName());
    private String strResultLast;
    private String strPV_RAZONSOCIAL;
    private int intPV_ID;
    private double dblTasaIVA;
    private int intCont;
    private int intTI_ID;
    private String strPV_DIASCREDITO;
    private double dblTotalPartidas;
    private double dblTotalIVAPartidas;
    private String strMetodo_Envio;
    private int intMon_ID;
    private ArrayList<CompraAutomaticaDeta> lstElementos;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    public CompraAutomatica() {
        this.strResultLast = "";
        this.strPV_RAZONSOCIAL = "";
        this.lstElementos = new ArrayList<CompraAutomaticaDeta>();
        this.intCont = 0;
        this.dblTotalPartidas=0.0;
        this.dblTotalIVAPartidas=0.0;
    }

    public int getIntCont() {
        return intCont;
    }

    public void setIntCont(int intCont) {
        this.intCont = intCont;
    }

    public String getStrResultLast() {
        return strResultLast;
    }

    public int getIntTI_ID() {
        return intTI_ID;
    }

    public String getStrPV_DIASCREDITO() {
        return strPV_DIASCREDITO;
    }

    public double getDblTotalPartidas() {
        return dblTotalPartidas;
    }

    public double getDblTotalIVAPartidas() {
        return dblTotalIVAPartidas;
    }

    public String getStrMetodo_Envio() {
        return strMetodo_Envio;
    }

    public int getIntMon_ID() {
        return intMon_ID;
    }

    public int getIntPV_ID() {
        return intPV_ID;
    }

    public ArrayList<CompraAutomaticaDeta> getLstElementos() {
        return lstElementos;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    private boolean Encuentra_Proveedor(Conexion oConn) {
        String strSQL = "SELECT PV_ID,vta_proveedor.MON_ID,PV_METODO_ENVIO,vta_proveedor.TI_ID,TI_TASA,PV_DIASCREDITO From vta_proveedor Join vta_tasaiva on vta_tasaiva.TI_ID = vta_proveedor.TI_ID Where PV_RAZONSOCIAL = '" + this.strPV_RAZONSOCIAL + "'";
        try {
            ResultSet rs = oConn.runQuery(strSQL);
            int cont = 0;
            while (rs.next()) {
                this.intPV_ID = rs.getInt("PV_ID");
                this.dblTasaIVA = rs.getDouble("TI_TASA");
                this.intTI_ID = rs.getInt("TI_ID");
                this.strPV_DIASCREDITO = rs.getString("PV_DIASCREDITO");
                this.strMetodo_Envio = rs.getString("PV_METODO_ENVIO");
                this.intMon_ID = rs.getInt("MON_ID");
                cont++;
            }
            if (cont == 0) {
                this.strResultLast = "ERROR: No se encuentra el proveedor";
                return false;
            }
            return true;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            this.strResultLast = "ERROR: " + ex.getMessage();
            return false;
        }
    }   

    public boolean AgregaProducto(String strCodigoPR, int intCantidadPR, String strNombreProv, Conexion oConn) {
        this.strPV_RAZONSOCIAL = strNombreProv;
        if (this.Encuentra_Proveedor(oConn)) {
            CompraAutomaticaDeta Producto = new CompraAutomaticaDeta();
            Producto.setStrPR_CODIGO(strCodigoPR);
            Producto.setIntCantidad(intCantidadPR);            
            if(Producto.Encuentra_Producto(this.intPV_ID, dblTasaIVA, oConn))
            {
                if (this.lstElementos == null) {
                    this.lstElementos = new ArrayList<CompraAutomaticaDeta>();
                }
                this.lstElementos.add(Producto);
                this.dblTotalPartidas += Producto.getDblTotal();
                this.dblTotalIVAPartidas += Producto.getDblIVA();
                this.intCont++;
            }
            else
            {
                this.strResultLast = Producto.getStrResultLast();
                return false;
            }
            return true;
        }        
        return false;
    }
    // </editor-fold>
}
