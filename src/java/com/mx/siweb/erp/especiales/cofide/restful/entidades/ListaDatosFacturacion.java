/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide.restful.entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa los datos de facturacion
 *
 * @author ZeusSIWEB
 */
public class ListaDatosFacturacion {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String cuentaCorreo;
   private String idClienteCRM;
   private String idUsuarioPaginaWeb;
   private String codigo;
   private List<DatosFacturacion> listadoFacturacion;
   public String getCuentaCorreo() {
      return cuentaCorreo;
   }

   public void setCuentaCorreo(String cuentaCorreo) {
      this.cuentaCorreo = cuentaCorreo;
   }

   public String getIdClienteCRM() {
      return idClienteCRM;
   }

   public void setIdClienteCRM(String idClienteCRM) {
      this.idClienteCRM = idClienteCRM;
   }

   public String getIdUsuarioPaginaWeb() {
      return idUsuarioPaginaWeb;
   }

   public void setIdUsuarioPaginaWeb(String idUsuarioPaginaWeb) {
      this.idUsuarioPaginaWeb = idUsuarioPaginaWeb;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public List<DatosFacturacion> getListadoFacturacion() {
      return listadoFacturacion;
   }

   public void setListadoFacturacion(List<DatosFacturacion> listadoFacturacion) {
      this.listadoFacturacion = listadoFacturacion;
   }   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ListaDatosFacturacion() {
      listadoFacturacion = new ArrayList<DatosFacturacion>();
   }   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>




}
