/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes.entities;

/**
 *
 * @author siwebmx5
 */
public class EV_DetalleFactura {
    private int SC_NUM;
    private String SC_CLAVE;
    private String SC_NOMBRE;
    private String FOLIO;
    private String FECHA;
    private String NOMBRE;
    private String VENDEDOR;
    private Double TSUBTOTAL;
    private Double TIMPUESTOS;
    private Double TTOTAL;
    private Double TCOBROS;
    private Double TNOTASCREDITO;
    private Double TRETENCIONES;
    private Double TSALDO;
    
    public EV_DetalleFactura()
    {
       
    }

    public int getSC_NUM() {
        return SC_NUM;
    }

    public void setSC_NUM(int SC_NUM) {
        this.SC_NUM = SC_NUM;
    }

    public String getSC_CLAVE() {
        return SC_CLAVE;
    }

    public void setSC_CLAVE(String SC_CLAVE) {
        this.SC_CLAVE = SC_CLAVE;
    }

    public String getSC_NOMBRE() {
        return SC_NOMBRE;
    }

    public void setSC_NOMBRE(String SC_NOMBRE) {
        this.SC_NOMBRE = SC_NOMBRE;
    }

    public String getFOLIO() {
        return FOLIO;
    }

    public void setFOLIO(String FOLIO) {
        this.FOLIO = FOLIO;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getVENDEDOR() {
        return VENDEDOR;
    }

    public void setVENDEDOR(String VENDEDOR) {
        this.VENDEDOR = VENDEDOR;
    }

    public Double getTSUBTOTAL() {
        return TSUBTOTAL;
    }

    public void setTSUBTOTAL(Double TSUBTOTAL) {
        this.TSUBTOTAL = TSUBTOTAL;
    }

    public Double getTIMPUESTOS() {
        return TIMPUESTOS;
    }

    public void setTIMPUESTOS(Double TIMPUESTOS) {
        this.TIMPUESTOS = TIMPUESTOS;
    }

    public Double getTTOTAL() {
        return TTOTAL;
    }

    public void setTTOTAL(Double TTOTAL) {
        this.TTOTAL = TTOTAL;
    }

    public Double getTCOBROS() {
        return TCOBROS;
    }

    public void setTCOBROS(Double TCOBROS) {
        this.TCOBROS = TCOBROS;
    }

    public Double getTNOTASCREDITO() {
        return TNOTASCREDITO;
    }

    public void setTNOTASCREDITO(Double TNOTASCREDITO) {
        this.TNOTASCREDITO = TNOTASCREDITO;
    }

    public Double getTRETENCIONES() {
        return TRETENCIONES;
    }

    public void setTRETENCIONES(Double TRETENCIONES) {
        this.TRETENCIONES = TRETENCIONES;
    }

    public Double getTSALDO() {
        return TSALDO;
    }

    public void setTSALDO(Double TSALDO) {
        this.TSALDO = TSALDO;
    }
    
    
}
