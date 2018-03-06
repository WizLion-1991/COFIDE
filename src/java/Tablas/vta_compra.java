/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa la tabla de compras
 * @author 
 */
public class vta_compra extends TableMaster {

    public vta_compra() {
        super("vta_compra", "COM_ID", "", "");
        this.Fields.put("COM_ID", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("EMP_ID", new Integer(0));
        this.Fields.put("PV_ID", new Integer(0));
        this.Fields.put("PV_NOMBRE", "");
        this.Fields.put("COM_FECHA", "");
        this.Fields.put("COM_OPER", new Integer(0));
        this.Fields.put("COM_ANULADA", new Integer(0));
        this.Fields.put("COM_DESCUENTO1", new Double(0));
        this.Fields.put("COM_FOLIO", "");
        this.Fields.put("COM_PRECIO", "");
        this.Fields.put("COM_NOTAS", "");
        this.Fields.put("COM_NOTASPIE", "");
        this.Fields.put("COM_CONIMPUESTO1", new Integer(0));
        this.Fields.put("COM_CREDITO", new Integer(0));
        this.Fields.put("COM_MONEDA", new Integer(0));
        this.Fields.put("COM_PARIDAD", new Double(0));
        this.Fields.put("COM_FECHAANUL", "");
        this.Fields.put("COM_HORAANUL", "");
        this.Fields.put("COM_US_ANUL", new Integer(0));
        this.Fields.put("COM_TOTAL", new Double(0));
        this.Fields.put("COM_SALDO", new Double(0));
        this.Fields.put("COM_IMPORTE", new Double(0));
        this.Fields.put("COM_IMPORTE_IEPS", new Double(0));
        this.Fields.put("COM_IMPUESTO1", new Double(0));
        this.Fields.put("COM_IMPUESTO2", new Double(0));
        this.Fields.put("COM_IMPUESTO3", new Double(0));
        this.Fields.put("COM_REFERENCIA", "");
        this.Fields.put("COM_FORMADEPAGO", "");
        this.Fields.put("COM_CONDPAGO", "");
        this.Fields.put("COM_METODOPAGO", "");
        this.Fields.put("COM_NUMCUENTA", "");
        this.Fields.put("COM_TASA1", new Integer(0));
        this.Fields.put("COM_TASA2", new Integer(0));
        this.Fields.put("COM_TASA3", new Integer(0));
        this.Fields.put("COM_FECHACREATE", "");
        this.Fields.put("COM_HORACREATE", "");
        this.Fields.put("COM_AUTORIZA", new Integer(0));
        this.Fields.put("COM_USUARIO", new Integer(0));
        this.Fields.put("COM_USUARIO_AUTORIZA", new Integer(0));
        this.Fields.put("COM_FECHA_AUTORIZA", "");
        this.Fields.put("COM_HORA_AUTORIZA", "");
        this.Fields.put("TOD_ID", new Integer(0));
        this.Fields.put("MON_ID", new Integer(0));
        this.Fields.put("PED_ID", new Integer(0));
        this.Fields.put("TI_ID", new Integer(0));
        this.Fields.put("TI_ID2", new Integer(0));
        this.Fields.put("TI_ID3", new Integer(0));
        this.Fields.put("COM_DIASCREDITO", new Integer(0));
        this.Fields.put("PED_COD", "");
        this.Fields.put("COM_METODO_ENVIO", "");
        this.Fields.put("CDE_ID", new Integer(0));
        this.Fields.put("COM_FECHA_PROMESA", "");
        this.Fields.put("COM_TIPOFLETE", "");
        this.Fields.put("COM_SURTIDA", new Integer(0));
        this.Fields.put("COM_USO_IEPS", new Integer(0));
        this.Fields.put("COM_STATUS", new Integer(0));
        this.Fields.put("COM_TASA_IEPS", new Double(0));
        this.Fields.put("COM_USER_CERRADA", new Integer(0));
        this.Fields.put("COM_FECHA_CERRADA", "");
        this.Fields.put("COM_HORA_CERRADA", "");
        this.Fields.put("COM_FECHA_DISP_VTA", "");
        this.Fields.put("TR_ID", new Integer(0));
        this.Fields.put("COM_NUM_GUIA", "");
    }
}