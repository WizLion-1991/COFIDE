/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean que representa los pedidos
 *
 * @author ZeusGalindo
 */
@XmlRootElement
public class Pedidos {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String codigoAcceso;
    private int idPedido;
    private int moneda;
    private int idVendedor;
    private int idTi;
    private int idTi2;
    private int idTi3;
    private int esserv;
    private String fecha;
    private String email1;
    private String email2;
    private String folio;
    private String notas;
    private String notasPie;
    private String referencia;
    private String condpago;
    private String metodopago;
    private String numcuenta;
    private String formadepago;
    private double importe;
    private double impuesto1;
    private double impuesto2;
    private double impuesto3;
    private double total;
    private double tasa1;
    private double tasa2;
    private double tasa3;
    private double tasaPeso;
    private int usoIeps;
    private int tasaIeps;
    private double importeIeps;
    private int consignacion;
    private double negocio;
    private String numPedi;
    private String fechapedi;
    private String aduana;
    private int diasCredito;
    private double retIsr;
    private double retIva;
    private double neto;
    private int esrecu;
    private int periodicidad;
    private int diaper;
    private String regimenFiscal;
    private int idCliente;
    private int idEmpresa;
    
    private int idsucursal;
    private int facTipocomp;
    private double puntos;
    private int tipoVenta;
    private String inv;
    private String facPedi;
    private int countItem;
    private int idProducto;
    private int appDirEntrega;
    
    
    private String CtRazonSocial;
    private String CtRFC;
    private String CtCalle;
    private String CtColonia;
    private String CtMunicipio;
    private String CtEstado;
    private String CtCp;
    private String CtNumero;
    private String CtNumInterior;
    
    
    private List<PedidosDeta> lstItems;
    private List<DirEntrega> lstItems1;

    public String getCtRazonSocial() {
        return CtRazonSocial;
    }

    public void setCtRazonSocial(String CtRazonSocial) {
        this.CtRazonSocial = CtRazonSocial;
    }

    public String getCtRFC() {
        return CtRFC;
    }

    public void setCtRFC(String CtRFC) {
        this.CtRFC = CtRFC;
    }

    public String getCtCalle() {
        return CtCalle;
    }

    public void setCtCalle(String CtCalle) {
        this.CtCalle = CtCalle;
    }

    public String getCtColonia() {
        return CtColonia;
    }

    public void setCtColonia(String CtColonia) {
        this.CtColonia = CtColonia;
    }

    public String getCtMunicipio() {
        return CtMunicipio;
    }

    public void setCtMunicipio(String CtMunicipio) {
        this.CtMunicipio = CtMunicipio;
    }

    public String getCtEstado() {
        return CtEstado;
    }

    public void setCtEstado(String CtEstado) {
        this.CtEstado = CtEstado;
    }

    public String getCtCp() {
        return CtCp;
    }

    public void setCtCp(String CtCp) {
        this.CtCp = CtCp;
    }

    public String getCtNumero() {
        return CtNumero;
    }

    public void setCtNumero(String CtNumero) {
        this.CtNumero = CtNumero;
    }

    public String getCtNumInterior() {
        return CtNumInterior;
    }

    public void setCtNumInterior(String CtNumInterior) {
        this.CtNumInterior = CtNumInterior;
    }
    
    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }
    
    public List<PedidosDeta> getLstItems() {
        return lstItems;
    }

    public void setLstItems(List<PedidosDeta> lstItems) {
        this.lstItems = lstItems;
    }

    public List<DirEntrega> getLstItems1() {
        return lstItems1;
    }

    public void setLstItems1(List<DirEntrega> lstItems1) {
        this.lstItems1 = lstItems1;
    }

    
     public String getNotasPie() {
        return notasPie;
    }

    public void setNotasPie(String notasPie) {
        this.notasPie = notasPie;
    }
       
 public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
    public int getAppDirEntrega() {
        return appDirEntrega;
    }

    public void setAppDirEntrega(int appDirEntrega) {
        this.appDirEntrega = appDirEntrega;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }

    public String getFacPedi() {
        return facPedi;
    }

    public void setFacPedi(String facPedi) {
        this.facPedi = facPedi;
    }

    public String getInv() {
        return inv;
    }

    public void setInv(String inv) {
        this.inv = inv;
    }

    public int getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(int tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public int getFacTipocomp() {
        return facTipocomp;
    }

    public void setFacTipocomp(int facTipocomp) {
        this.facTipocomp = facTipocomp;
    }

    public String getRegimenFiscal() {
        return regimenFiscal;
    }

    public void setRegimenFiscal(String regimenFiscal) {
        this.regimenFiscal = regimenFiscal;
    }

    public int getIdsucursal() {
        return idsucursal;
    }

    public void setIdsucursal(int idsucursal) {
        this.idsucursal = idsucursal;
    }

    public int getDiaper() {
        return diaper;
    }

    public void setDiaper(int diaper) {
        this.diaper = diaper;
    }

    public int getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(int periodicidad) {
        this.periodicidad = periodicidad;
    }

    public int getEsrecu() {
        return esrecu;
    }

    public void setEsrecu(int esrecu) {
        this.esrecu = esrecu;
    }

    public double getNeto() {
        return neto;
    }

    public void setNeto(double neto) {
        this.neto = neto;
    }

    public double getRetIva() {
        return retIva;
    }

    public void setRetIva(double retIva) {
        this.retIva = retIva;
    }

    public double getRetIsr() {
        return retIsr;
    }

    public void setRetIsr(double retIsr) {
        this.retIsr = retIsr;
    }

    public int getDiasCredito() {
        return diasCredito;
    }

    public void setDiasCredito(int diasCredito) {
        this.diasCredito = diasCredito;
    }

    public String getAduana() {
        return aduana;
    }

    public void setAduana(String aduana) {
        this.aduana = aduana;
    }

    public String getFechapedi() {
        return fechapedi;
    }

    public void setFechapedi(String fechapedi) {
        this.fechapedi = fechapedi;
    }

    public String getNumPedi() {
        return numPedi;
    }

    public void setNumPedi(String numPedi) {
        this.numPedi = numPedi;
    }

    public double getNegocio() {
        return negocio;
    }

    public void setNegocio(double negocio) {
        this.negocio = negocio;
    }

    public int getConsignacion() {
        return consignacion;
    }

    public void setConsignacion(int consignacion) {
        this.consignacion = consignacion;
    }

    public double getImporteIeps() {
        return importeIeps;
    }

    public void setImporteIeps(double importeIeps) {
        this.importeIeps = importeIeps;
    }

    public int getTasaIeps() {
        return tasaIeps;
    }

    public void setTasaIeps(int tasaIeps) {
        this.tasaIeps = tasaIeps;
    }

    public int getUsoIeps() {
        return usoIeps;
    }

    public void setUsoIeps(int usoIeps) {
        this.usoIeps = usoIeps;
    }

    public double getTasaPeso() {
        return tasaPeso;
    }

    public void setTasaPeso(double tasaPeso) {
        this.tasaPeso = tasaPeso;
    }

    public double getTasa3() {
        return tasa3;
    }

    public void setTasa3(double tasa3) {
        this.tasa3 = tasa3;
    }

    public double getTasa2() {
        return tasa2;
    }

    public void setTasa2(double tasa2) {
        this.tasa2 = tasa2;
    }

    public double getTasa1() {
        return tasa1;
    }

    public void setTasa1(double tasa1) {
        this.tasa1 = tasa1;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdTi() {
        return idTi;
    }

    public void setIdTi(int idTi) {
        this.idTi = idTi;
    }

    public int getIdTi2() {
        return idTi2;
    }

    public void setIdTi2(int idTi2) {
        this.idTi2 = idTi2;
    }

    public int getIdTi3() {
        return idTi3;
    }

    public void setIdTi3(int idTi3) {
        this.idTi3 = idTi3;
    }

    public int getEsserv() {
        return esserv;
    }

    public void setEsserv(int esserv) {
        this.esserv = esserv;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCondpago() {
        return condpago;
    }

    public void setCondpago(String condpago) {
        this.condpago = condpago;
    }

    public String getMetodopago() {
        return metodopago;
    }

    public void setMetodopago(String metodopago) {
        this.metodopago = metodopago;
    }

    public String getNumcuenta() {
        return numcuenta;
    }

    public void setNumcuenta(String numcuenta) {
        this.numcuenta = numcuenta;
    }

    public String getFormadepago() {
        return formadepago;
    }

    public void setFormadepago(String formadepago) {
        this.formadepago = formadepago;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        this.codigoAcceso = codigoAcceso;
    }
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    public Pedidos() {
        lstItems = new ArrayList<PedidosDeta>();
        lstItems1 = new ArrayList<DirEntrega>();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    // </editor-fold>
    // </editor-fold>
}
