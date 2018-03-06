/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_compradeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siwebmx5
 */
public class OrdenCompraAutomatica {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(OrdenCompraAutomatica.class.getName());
    private String strResultLast;
    private String strPath;
    private ArrayList<CompraAutomatica> lstListas;
    private String strPV_RAZONSOCIAL;
    private int intCont;
    private int intContLinea;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public OrdenCompraAutomatica() {
        this.strResultLast = "";
        this.strPath = "";
        this.lstListas = new ArrayList<CompraAutomatica>();
        this.strPV_RAZONSOCIAL = "";
        this.intCont = 0;
        this.intContLinea = 0;
    }

    public String getStrPath() {
        return strPath;
    }

    public void setStrPath(String strPath) {
        this.strPath = strPath;
    }

    public String getStrResultLast() {
        return strResultLast;
    }

    public ArrayList<CompraAutomatica> getLstListas() {
        return lstListas;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public boolean CargaDatos(Conexion oConn) {
        File OC_IN = new File(this.strPath);
        BufferedReader InBuffer;
        boolean Valida = true;
        CompraAutomatica CA = new CompraAutomatica();
        try {
            InBuffer = new BufferedReader(new FileReader(OC_IN));
            String strRenglon;
            String[] strDatos;
            CompraAutomatica ListProv = null;
            while (InBuffer.ready()) {
                strRenglon = InBuffer.readLine();
                strDatos = strRenglon.split("\\|");
                this.intContLinea++;
                if (!strDatos[2].equals("0")) {
                    if (!strPV_RAZONSOCIAL.equals(strDatos[3])) {
                        if (ListProv != null) {
                            this.lstListas.add(ListProv);
                        }
                        this.strPV_RAZONSOCIAL = strDatos[3];
                        ListProv = new CompraAutomatica();
                        this.intCont++;
                    }
                    Valida = ListProv.AgregaProducto(strDatos[0], Integer.parseInt(strDatos[2]), strDatos[3], oConn);
                    if (Valida) {
                        
                        if (ListProv.getIntCont() >= 20) {
                            this.lstListas.add(ListProv);
                            ListProv = new CompraAutomatica();
                            this.intCont++;
                        }
                    } else {
                        this.strResultLast = ListProv.getStrResultLast() +" En la linea:"+this.intContLinea;
                        return false;
                    }
                }
            }
            if (ListProv != null) {
                if (ListProv.getIntCont() >= 1) {
                    this.lstListas.add(ListProv);
                }
            }
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
            this.strResultLast = "ERROR: " + ex.getMessage();
            return false;
        } catch (IOException ex) {
            Logger.getLogger(OrdenCompraAutomatica.class.getName()).log(Level.SEVERE, null, ex);
            this.strResultLast = "ERROR: " + ex.getMessage();
            return false;
        }
        return true;
    }

    public boolean SubeOC(Conexion oConn, VariableSession varSesiones) {
        String strPrefijoMaster = "COM";
        String strPrefijoDeta = "COMD";
        Fechas fecha = new Fechas();
        for (CompraAutomatica MiLst : this.lstListas) {
            Compras compra = new Compras(oConn, varSesiones);
            compra.setBolFolioGlobal(false);
            compra.getDocument().setFieldInt("TOD_ID", 1);
            compra.getDocument().setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
            compra.getDocument().setFieldInt("PV_ID", MiLst.getIntPV_ID());
            compra.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
            compra.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fecha.getFechaActual());
            compra.getDocument().setFieldString("PED_COD", "");
            compra.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", "OC Automatico");
            compra.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", "");
            compra.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", "");
            compra.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", "");
            compra.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", "");
            compra.getDocument().setFieldInt("MON_ID", MiLst.getIntMon_ID());
            compra.getDocument().setFieldInt("CDE_ID", 1);
            
            compra.getDocument().setFieldInt("TI_ID", MiLst.getIntTI_ID()); //ID Tasa de Iva del Proveedor Esta en su tabla
            compra.getDocument().setFieldInt("TI_ID2", 0);
            compra.getDocument().setFieldInt("TI_ID3", 0);

            compra.getDocument().setFieldString(strPrefijoMaster + "_DIASCREDITO", MiLst.getStrPV_DIASCREDITO());//Proveedor_ Dias de Credito
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", MiLst.getDblTotalPartidas());//Suma de Ttoal todas las partidas
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", MiLst.getDblTotalIVAPartidas());//IVA del Total
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", 0.0);
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", 0.0);
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", MiLst.getDblTotalPartidas() + MiLst.getDblTotalIVAPartidas());//Imporrte + Impuesto 1
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", 0.0);//Sale de tasa de iva
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", 0.0);
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", 0.0);
            compra.getDocument().setFieldDouble(strPrefijoMaster + "_PORDESCUENTO", 0.0);
            compra.getDocument().setFieldString(strPrefijoMaster + "_METODO_ENVIO", MiLst.getStrMetodo_Envio());//Viene en Proveedor
            compra.getDocument().setFieldString(strPrefijoMaster + "_TIPOFLETE", "");//Preguntar a Emanuel si se usara o no

            for (CompraAutomaticaDeta Producto : MiLst.getLstElementos()) {
                TableMaster deta = null;
                deta = new vta_compradeta();
                deta.setFieldInt("SC_ID", Integer.valueOf(varSesiones.getIntSucursalDefault()));
                deta.setFieldInt("PR_ID", Producto.getIntID());
                deta.setFieldInt(strPrefijoDeta + "_EXENTO1", Producto.getIntPR_EXENTO1());//Viene en su tabla
                deta.setFieldInt(strPrefijoDeta + "_EXENTO2", 0);
                deta.setFieldInt(strPrefijoDeta + "_EXENTO3", 0);
                deta.setFieldString(strPrefijoDeta + "_CVE", Producto.getStrPR_CODIGO());
                deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", Producto.getStrPR_DESCRIPCION());//Descripcion de Compra
                deta.setFieldString(strPrefijoDeta + "_NOSERIE", "");
                deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", Producto.getDblTotal());
                deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Producto.getIntCantidad());
                deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Producto.getDblTasa_IVA());
                deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", 0.0);
                deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", 0.0);
                deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", Producto.getDblIVA());
                deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", 0.0);
                deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", 0.0);
                deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", Producto.getIntCantidad());
                deta.setFieldDouble(strPrefijoDeta + "_COSTO", Producto.getDblPrecioU());
                deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", 0.0);
                deta.setFieldDouble(strPrefijoDeta + "_PORDESC", 0.0);
                deta.setFieldDouble(strPrefijoDeta + "_COSTOREAL", Producto.getDblPrecioU());
                deta.setFieldString(strPrefijoDeta + "_NOTAS", "OC Automatico");
                deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", Producto.getStrUnidadMedida());//Viene en producto
                deta.setFieldInt(strPrefijoDeta + "_MONEDA", MiLst.getIntMon_ID());//Proveedor
                deta.setFieldDouble(strPrefijoDeta + "_PARIDAD", 1.0);
                compra.AddDetalle(deta);
            }
            compra.setBolSendMailMasivo(false);
            compra.doTrx();
            if (compra.getStrResultLast().equals("OK")) {
               this.strResultLast = "OK." + compra.getDocument().getValorKey();
            } else {
               this.strResultLast = compra.getStrResultLast();
               return false;
            }
        }
        
        return true;
    }
    // </editor-fold>
}







