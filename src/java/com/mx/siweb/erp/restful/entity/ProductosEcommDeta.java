/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author siweb
 */
@XmlRootElement
public class ProductosEcommDeta {

    private String codigo;
    private String nombreProducto;
    private String nombreImagenPequena;
    private String nombreImagenGrande;
    private int existencia;
    private double precio;
    private int pr_id;
    private String descripcion;
    private double puntos;
    private double negocio;
    private int exento1;
    private int exento2;
    private int exento3;
    private String cve;
    private int tasaIva1;
    private int unidadDeMedida;
    private int impPuntos;

    public int getUnidadDeMedida() {
        return unidadDeMedida;
    }

    public void setUnidadDeMedida(int unidadDeMedida) {
        this.unidadDeMedida = unidadDeMedida;
    }

    public int getTasaIva1() {
        return tasaIva1;
    }

    public void setTasaIva1(int tasaIva1) {
        this.tasaIva1 = tasaIva1;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
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

    public double getNegocio() {
        return negocio;
    }

    public void setNegocio(double negocio) {
        this.negocio = negocio;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombreImagenPequena() {
        return nombreImagenPequena;
    }

    public void setNombreImagenPequena(String nombreImagenPequena) {

        this.nombreImagenPequena = nombreImagenPequena;
    }

    public String getNombreImagenGrande() {
        return nombreImagenGrande;
    }

    public void setNombreImagenGrande(String nombreImagenGrande) {

        this.nombreImagenGrande = nombreImagenGrande;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getPr_id() {
        return pr_id;
    }

    public void setPr_id(int pr_id) {
        this.pr_id = pr_id;
    }

}
