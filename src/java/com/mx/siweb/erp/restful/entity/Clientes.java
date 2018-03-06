/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.entity;

import Tablas.vta_cliente;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.generateData;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.logging.log4j.LogManager;

/**
 * Representa los clientes a usarse por medio de restful
 *
 * @author ZeusGalindo
 */
@XmlRootElement
public class Clientes {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Clientes.class.getName());

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int id;
   private int idReferencia;//Match
   private int idUpline;
   private String fechaNacimiento;
   private String fechaRegistro;
   private String razonSocial;
   private String razonComercial;
   private String rfc;
   private String calle;
   private String numeroExt;
   private String numeroInt;
   private String colonia;
   private String municipo;
   private String localidad;
   private String estado;
   private String cp;
   private String telefono1;
   private String telefono2;
   private String email1;
   private String email2;
   private String numeroCuenta1;
   private String institucionBancaria1;
   private String titularCuenta1;
   private String numeroCuenta2;
   private String institucionBancaria2;
   private String titularCuenta2;
   private String numeroTarjeta;
   private String notas;
   private String referenciaBancaria1;
   private String referenciaBancaria2;
   private String referenciaBancariaBanco1;
   private String referenciaBancariaBanco2;
   private String credencialIFE;
   private String pais;
   private String codigo;
   private String tipoOperacion;
   private String posicion;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getIdUpline() {
      return idUpline;
   }

   public void setIdUpline(int idUpline) {
      this.idUpline = idUpline;
   }

   public String getRazonSocial() {
      return razonSocial;
   }

   public void setRazonSocial(String razonSocial) {
      this.razonSocial = razonSocial;
   }

   public String getRazonComercial() {
      return razonComercial;
   }

   public void setRazonComercial(String razonComercial) {
      this.razonComercial = razonComercial;
   }

   public String getRfc() {
      return rfc;
   }

   public void setRfc(String rfc) {
      this.rfc = rfc;
   }

   public String getCalle() {
      return calle;
   }

   public void setCalle(String calle) {
      this.calle = calle;
   }

   public String getNumeroExt() {
      return numeroExt;
   }

   public void setNumeroExt(String numeroExt) {
      this.numeroExt = numeroExt;
   }

   public String getNumeroInt() {
      return numeroInt;
   }

   public void setNumeroInt(String numeroInt) {
      this.numeroInt = numeroInt;
   }

   public String getColonia() {
      return colonia;
   }

   public void setColonia(String colonia) {
      this.colonia = colonia;
   }

   public String getMunicipo() {
      return municipo;
   }

   public void setMunicipo(String municipo) {
      this.municipo = municipo;
   }

   public String getLocalidad() {
      return localidad;
   }

   public void setLocalidad(String localidad) {
      this.localidad = localidad;
   }

   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public String getCp() {
      return cp;
   }

   public void setCp(String cp) {
      this.cp = cp;
   }

   public String getTelefono1() {
      return telefono1;
   }

   public void setTelefono1(String telefono1) {
      this.telefono1 = telefono1;
   }

   public String getTelefono2() {
      return telefono2;
   }

   public void setTelefono2(String telefono2) {
      this.telefono2 = telefono2;
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

   public String getNumeroCuenta1() {
      return numeroCuenta1;
   }

   public void setNumeroCuenta1(String numeroCuenta1) {
      this.numeroCuenta1 = numeroCuenta1;
   }

   public String getInstitucionBancaria1() {
      return institucionBancaria1;
   }

   public void setInstitucionBancaria1(String institucionBancaria1) {
      this.institucionBancaria1 = institucionBancaria1;
   }

   public String getTitularCuenta1() {
      return titularCuenta1;
   }

   public void setTitularCuenta1(String titularCuenta1) {
      this.titularCuenta1 = titularCuenta1;
   }

   public String getNumeroCuenta2() {
      return numeroCuenta2;
   }

   public void setNumeroCuenta2(String numeroCuenta2) {
      this.numeroCuenta2 = numeroCuenta2;
   }

   public String getInstitucionBancaria2() {
      return institucionBancaria2;
   }

   public void setInstitucionBancaria2(String institucionBancaria2) {
      this.institucionBancaria2 = institucionBancaria2;
   }

   public String getTitularCuenta2() {
      return titularCuenta2;
   }

   public void setTitularCuenta2(String titularCuenta2) {
      this.titularCuenta2 = titularCuenta2;
   }

   public String getNumeroTarjeta() {
      return numeroTarjeta;
   }

   public void setNumeroTarjeta(String numeroTarjeta) {
      this.numeroTarjeta = numeroTarjeta;
   }

   public String getNotas() {
      return notas;
   }

   public void setNotas(String notas) {
      this.notas = notas;
   }

   public String getReferenciaBancaria1() {
      return referenciaBancaria1;
   }

   public void setReferenciaBancaria1(String referenciaBancaria1) {
      this.referenciaBancaria1 = referenciaBancaria1;
   }

   public String getReferenciaBancaria2() {
      return referenciaBancaria2;
   }

   public void setReferenciaBancaria2(String referenciaBancaria2) {
      this.referenciaBancaria2 = referenciaBancaria2;
   }

   public String getReferenciaBancariaBanco1() {
      return referenciaBancariaBanco1;
   }

   public void setReferenciaBancariaBanco1(String referenciaBancariaBanco1) {
      this.referenciaBancariaBanco1 = referenciaBancariaBanco1;
   }

   public String getReferenciaBancariaBanco2() {
      return referenciaBancariaBanco2;
   }

   public void setReferenciaBancariaBanco2(String referenciaBancariaBanco2) {
      this.referenciaBancariaBanco2 = referenciaBancariaBanco2;
   }

   public int getIdReferencia() {
      return idReferencia;
   }

   public void setIdReferencia(int idReferencia) {
      this.idReferencia = idReferencia;
   }

   public String getFechaNacimiento() {
      return fechaNacimiento;
   }

   public void setFechaNacimiento(String fechaNacimiento) {
      this.fechaNacimiento = fechaNacimiento;
   }

   public String getFechaRegistro() {
      return fechaRegistro;
   }

   public void setFechaRegistro(String fechaRegistro) {
      this.fechaRegistro = fechaRegistro;
   }

   public String getCredencialIFE() {
      return credencialIFE;
   }

   public void setCredencialIFE(String credencialIFE) {
      this.credencialIFE = credencialIFE;
   }

   public String getPais() {
      return pais;
   }

   public void setPais(String pais) {
      this.pais = pais;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public String getTipoOperacion() {
      return tipoOperacion;
   }

   public String getPosicion() {
      return posicion;
   }

   /**
    *Es la posicion en la red.
    * @param posicion
    */
   public void setPosicion(String posicion) {
      this.posicion = posicion;
   }
   
   

   /**
    * Indica el tipo de operacion por realizar A es alta, M es modificacion
    *
    * @param tipoOperacion Es un valor de texto con A o M
    */
   public void setTipoOperacion(String tipoOperacion) {
      this.tipoOperacion = tipoOperacion;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
    public Clientes() {
        this.tipoOperacion = "M";
        this.fechaNacimiento = "";
        this.fechaRegistro = "";
        this.razonSocial = "";
        this.razonComercial = "";
        this.rfc = "";
        this.calle = "";
        this.numeroExt = "";
        this.numeroInt = "";
        this.colonia = "";
        this.municipo = "";
        this.localidad = "";
        this.estado = "";
        this.cp = "";
        this.telefono1 = "";
        this.telefono2 = "";
        this.email1 = "";
        this.email2 = "";
        this.numeroCuenta1 = "";
        this.institucionBancaria1 = "";
        this.titularCuenta1 = "";
        this.numeroCuenta2 = "";
        this.institucionBancaria2 = "";
        this.titularCuenta2 = "";
        this.numeroTarjeta = "";
        this.notas = "";
        this.referenciaBancaria1 = "";
        this.referenciaBancaria2 = "";
        this.referenciaBancariaBanco1 = "";
        this.referenciaBancariaBanco2 = "";
        this.credencialIFE = "";
        this.pais = "";
        this.codigo = "";
        this.posicion = "";
    }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void ObtenerDatos(Conexion oConn) {
      //Instanciamos clase para clientes
      vta_cliente cliente = new vta_cliente();
      cliente.ObtenDatos(this.id, oConn);
      //Asignamos valores recuperados
      this.idReferencia = 0;//Match
      this.idUpline = cliente.getFieldInt("CT_UPLINE");
      this.fechaNacimiento = cliente.getFieldString("CT_FECHA_NAC");
      this.fechaRegistro = cliente.getFieldString("CT_FECHAREG");
      this.razonSocial = cliente.getFieldString("CT_RAZONSOCIAL");
      this.razonComercial = cliente.getFieldString("CT_RAZONCOMERCIAL");
      this.rfc = cliente.getFieldString("CT_RFC");
      this.calle = cliente.getFieldString("CT_CALLE");
      this.numeroExt = cliente.getFieldString("CT_NUMERO");
      this.numeroInt = cliente.getFieldString("CT_NUMINT");
      this.colonia = cliente.getFieldString("CT_COLONIA");
      this.municipo = cliente.getFieldString("CT_MUNICIPIO");
      this.localidad = cliente.getFieldString("CT_LOCALIDAD");
      this.estado = cliente.getFieldString("CT_ESTADO");
      this.cp = cliente.getFieldString("CT_CP");
      this.telefono1 = cliente.getFieldString("CT_TELEFONO1");
      this.telefono2 = cliente.getFieldString("CT_TELEFONO2");
      this.email1 = cliente.getFieldString("CT_EMAIL1");
      this.email2 = cliente.getFieldString("CT_EMAIL2");
      this.numeroCuenta1 = cliente.getFieldString("CT_CTABANCO1");
      this.institucionBancaria1 = cliente.getFieldString("CT_CTA_BANCO1");
      this.titularCuenta1 = cliente.getFieldString("CT_CONTACTO1");;
      this.numeroCuenta2 = cliente.getFieldString("CT_CTABANCO2");
      this.institucionBancaria2 = cliente.getFieldString("CT_CTA_BANCO2");
      this.titularCuenta2 = cliente.getFieldString("CT_CONTACTO2");
      this.numeroTarjeta = cliente.getFieldString("CT_CTATARJETA");
      this.notas = cliente.getFieldString("CT_NOTAS");
      this.referenciaBancaria1 = cliente.getFieldString("CT_RBANCARIA1");
      this.referenciaBancaria2 = cliente.getFieldString("CT_RBANCARIA2");
      this.referenciaBancariaBanco1 = cliente.getFieldString("CT_BANCO1");
      this.referenciaBancariaBanco2 = cliente.getFieldString("CT_BANCO1");
   }

   public void AlmacenaDatos(Conexion oConn) {
      //Instanciamos clase para clientes
      vta_cliente cliente = new vta_cliente();
      if (tipoOperacion.equals("M")) {
         log.debug("Guardando datos del cliente " + this.id);
         cliente.ObtenDatos(this.id, oConn);
      } else {
         log.debug("Cliente nuevo");
      }
      //Asignamos valores recuperados
      if (idUpline != 0) {
         cliente.setFieldInt("CT_UPLINE", this.idUpline);
      }
      if (this.fechaNacimiento != null) {
         if (!this.fechaNacimiento.isEmpty()) {
            cliente.setFieldString("CT_FECHA_NAC", this.fechaNacimiento);
         }
      }
      if(this.fechaRegistro != null){
          if(!this.fechaRegistro.isEmpty()){
              cliente.setFieldString("CT_FECHAREG", this.fechaRegistro);
          }
      }
      if (this.razonSocial != null) {
         if (!this.razonSocial.isEmpty()) {
            cliente.setFieldString("CT_RAZONSOCIAL", this.razonSocial);
         }
      }
      if (this.razonComercial != null) {
         if (!this.razonComercial.isEmpty()) {
            cliente.setFieldString("CT_RAZONCOMERCIAL", this.razonComercial);
         }
      }
      if (this.rfc != null) {
         if (!this.rfc.isEmpty()) {
            cliente.setFieldString("CT_RFC", this.rfc);
         }
      }
      if (this.calle != null) {
         if (!this.calle.isEmpty()) {
            cliente.setFieldString("CT_CALLE", this.calle);
         }
      }
      if (this.numeroExt != null) {
         if (!this.numeroExt.isEmpty()) {
            cliente.setFieldString("CT_NUMERO", this.numeroExt);
         }
      }
      if (this.numeroInt != null) {
         if (!this.numeroInt.isEmpty()) {
            cliente.setFieldString("CT_NUMINT", this.numeroInt);
         }
      }
      if (this.colonia != null) {
         if (!this.colonia.isEmpty()) {
            cliente.setFieldString("CT_COLONIA", this.colonia);
         }
      }
      if (this.municipo != null) {
         if (!this.municipo.isEmpty()) {
            cliente.setFieldString("CT_MUNICIPIO", this.municipo);
         }
      }
      if (this.localidad != null) {
         if (!this.localidad.isEmpty()) {
            cliente.setFieldString("CT_LOCALIDAD", this.localidad);
         }
      }
      if (this.estado != null) {
         if (!this.estado.isEmpty()) {
            cliente.setFieldString("CT_ESTADO", this.estado);
         }
      }
      if (this.cp != null) {
         if (!this.cp.isEmpty()) {
            cliente.setFieldString("CT_CP", this.cp);
         }
      }
      if (this.telefono1 != null) {
         if (!this.telefono1.isEmpty()) {
            cliente.setFieldString("CT_TELEFONO1", this.telefono1);
         }
      }
      if (this.telefono2 != null) {
         if (!this.telefono2.isEmpty()) {
            cliente.setFieldString("CT_TELEFONO2", this.telefono2);
         }
      }
      if (this.email1 != null) {
         if (!this.email1.isEmpty()) {
            cliente.setFieldString("CT_EMAIL1", this.email1);
         }
      }
      if (this.email2 != null) {
         if (!this.email2.isEmpty()) {
            cliente.setFieldString("CT_EMAIL2", this.email2);
         }
      }
      
      cliente.setFieldInt("CT_ID", this.id);
      //Guardamos los datos modificados o nuevos
       if (tipoOperacion.equals("A")) {
           Fechas fecha = new Fechas();
           String strFecha = fecha.getFechaActual();
           cliente.setFieldString("CT_FECHAREG", strFecha);
           cliente.setFieldString("CT_PASSWORD", generateData.getPassword(8));
           cliente.Agrega(oConn);
           this.id = Integer.valueOf(cliente.getValorKey());
           log.debug("Id nuevo " + this.id);
       } else {
           cliente.Modifica(oConn);
       }
   }//Fin almacena Datos
   
    // </editor-fold>
}