/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean que representa el detalle de los pedidos
 *
 * @author ZeusGalindo
 */
@XmlRootElement
public class PedidosDeta {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String codigo;
    private String descripcion;
    private String comentario;
    private double cantidad;
    private double precio;
    private double puntos;
    private double negocio;
    private int exento1;
    private int exento2;
    private int exento3;
    private int esRegalo;
    private String cve;
    private String noSerie;
    private double importe;
    private double tasaIva1;
    private double tasaIva2;
    private double tasaIva3;
    private double impuesto1;
    private double impuesto2;
    private double impuesto3;
    private double importeReal;
    private double precReal;
    private int retIsr;
    private int retIva;
    private int retFlete;
    private int esdevo;
    private int precfijo;
    private String notas;
    private String unidadMedida;
    private double impPuntos;
    private double impVnegocio;
    private double descori;
    private int descprec;
    private int descpto;
    private int descvn;
    private int regalo;
    private int idPromo;
    private String seriesmpd;
    private String existenciaActual;

    private double descuento;
    private double pordesc;
    private double vnegocio;

    public double getVnegocio() {
        return vnegocio;
    }

    public void setVnegocio(double vnegocio) {
        this.vnegocio = vnegocio;
    }

    public double getPordesc() {
        return pordesc;
    }

    public void setPordesc(double pordesc) {
        this.pordesc = pordesc;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getSeriesmpd() {
        return seriesmpd;
    }

    public void setSeriesmpd(String seriesmpd) {
        this.seriesmpd = seriesmpd;
    }

    public int getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(int idPromo) {
        this.idPromo = idPromo;
    }

    public int getRegalo() {
        return regalo;
    }

    public void setRegalo(int regalo) {
        this.regalo = regalo;
    }

    public int getDescvn() {
        return descvn;
    }

    public void setDescvn(int descvn) {
        this.descvn = descvn;
    }

    public int getDescpto() {
        return descpto;
    }

    public void setDescpto(int descpto) {
        this.descpto = descpto;
    }

    public int getDescprec() {
        return descprec;
    }

    public void setDescprec(int descprec) {
        this.descprec = descprec;
    }

    public double getDescori() {
        return descori;
    }

    public void setDescori(double descori) {
        this.descori = descori;
    }

    public double getImpVnegocio() {
        return impVnegocio;
    }

    public void setImpVnegocio(double impVnegocio) {
        this.impVnegocio = impVnegocio;
    }

    public double getImpPuntos() {
        return impPuntos;
    }

    public void setImpPuntos(double impPuntos) {
        this.impPuntos = impPuntos;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getPrecfijo() {
        return precfijo;
    }

    public void setPrecfijo(int precfijo) {
        this.precfijo = precfijo;
    }

    public int getEsdevo() {
        return esdevo;
    }

    public void setEsdevo(int esdevo) {
        this.esdevo = esdevo;
    }

    public int getRetFlete() {
        return retFlete;
    }

    public void setRetFlete(int retFlete) {
        this.retFlete = retFlete;
    }

    public int getRetIva() {
        return retIva;
    }

    public void setRetIva(int retIva) {
        this.retIva = retIva;
    }

    public int getRetIsr() {
        return retIsr;
    }

    public void setRetIsr(int retIsr) {
        this.retIsr = retIsr;
    }

    public double getPrecReal() {
        return precReal;
    }

    public void setPrecReal(double precReal) {
        this.precReal = precReal;
    }

    public double getImporteReal() {
        return importeReal;
    }

    public void setImporteReal(double importeReal) {
        this.importeReal = importeReal;
    }

    public double getImpuesto1() {
        return impuesto1;
    }

    public void setImpuesto1(double impuesto1) {
        this.impuesto1 = impuesto1;
    }

    public double getImpuesto2() {
        return impuesto2;
    }

    public void setImpuesto2(double impuesto2) {
        this.impuesto2 = impuesto2;
    }

    public double getImpuesto3() {
        return impuesto3;
    }

    public void setImpuesto3(double impuesto3) {
        this.impuesto3 = impuesto3;
    }

    public double getTasaIva1() {
        return tasaIva1;
    }

    public void setTasaIva1(double tasaIva1) {
        this.tasaIva1 = tasaIva1;
    }

    public double getTasaIva2() {
        return tasaIva2;
    }

    public void setTasaIva2(double tasaIva2) {
        this.tasaIva2 = tasaIva2;
    }

    public double getTasaIva3() {
        return tasaIva3;
    }

    public void setTasaIva3(double tasaIva3) {
        this.tasaIva3 = tasaIva3;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getNoSerie() {
        return noSerie;
    }

    public void setNoSerie(String noSerie) {
        this.noSerie = noSerie;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public int getEsRegalo() {
        return esRegalo;
    }

    public void setEsRegalo(int esRegalo) {
        this.esRegalo = esRegalo;
    }

    public int getExento3() {
        return exento3;
    }

    public void setExento3(int exento3) {
        this.exento3 = exento3;
    }

    public int getExento2() {
        return exento2;
    }

    public void setExento2(int exento2) {
        this.exento2 = exento2;
    }

    public int getExento1() {
        return exento1;
    }

    public void setExento1(int exento1) {
        this.exento1 = exento1;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public double getNegocio() {
        return negocio;
    }

    public void setNegocio(double negocio) {
        this.negocio = negocio;
    }

    public String getExistenciaActual() {
        return existenciaActual;
    }

    public void setExistenciaActual(String existenciaActual) {
        this.existenciaActual = existenciaActual;
    }

   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
}
