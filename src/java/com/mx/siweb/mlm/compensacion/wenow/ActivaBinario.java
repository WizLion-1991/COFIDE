/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.wenow;

import com.mx.siweb.mlm.compensacion.Periodos;
import com.mx.siweb.mlm.utilerias.Redes;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 * Realiza el cálculo de posición de un nodo que ha confirmado su pago y entra a
 * la red binaria
 *
 * @author ZeusGalindo
 */
public class ActivaBinario {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ActivaBinario.class.getName());
   private int intUplineInicial;
   private int intUplineTemporal;
   private int intUplineTemporalAfiliado;
   private int intUplineTemporalGlobal;
   Fechas fecha;
   private int intPeriodoActual;
   private Conexion oConn;

   public int getIntUplineTemporalAfiliado() {
      return intUplineTemporalAfiliado;
   }

   public void setIntUplineTemporalAfiliado(int intUplineTemporalAfiliado) {
      this.intUplineTemporalAfiliado = intUplineTemporalAfiliado;
   }

   public int getIntUplineTemporalGlobal() {
      return intUplineTemporalGlobal;
   }

   public void setIntUplineTemporalGlobal(int intUplineTemporalGlobal) {
      this.intUplineTemporalGlobal = intUplineTemporalGlobal;
   }

   public int getIntUplineInicial() {
      return intUplineInicial;
   }

   public int getIntPeriodoActual() {
      return intPeriodoActual;
   }

   public void setIntPeriodoActual(int intPeriodoActual) {
      this.intPeriodoActual = intPeriodoActual;
   }

   /**
    * Define el upline inicial
    *
    * @param intUplineInicial Es el nodo
    */
   public void setIntUplineInicial(int intUplineInicial) {
      this.intUplineInicial = intUplineInicial;
   }

   public int getIntUplineTemporal() {
      return intUplineTemporal;
   }

   /**
    * Define el id del upline temporal donde se alojan los nodos no confirmados
    *
    * @param intUplineTemporal Es el id del nodo
    */
   public void setIntUplineTemporal(int intUplineTemporal) {
      this.intUplineTemporal = intUplineTemporal;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ActivaBinario(Conexion oConn) {
      fecha = new Fechas();
      Periodos periodo = new Periodos();
      this.intPeriodoActual = periodo.getPeriodoActual(oConn);
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Activa al distribuidor
    *
    * @param intIdCliente Es el id del cliente
    * @return
    */
   public String activarDistribuidor(int intIdCliente) {
      String strRes = "OK";

      log.debug("X acomodar: " + intIdCliente);
      int intUplineActual = 0;
      //Validamos si tiene upline
      String strSql = "select CT_UPLINE from vta_cliente where CT_ID =  " + intIdCliente;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intUplineActual = rs.getInt("CT_UPLINE");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      if (this.intUplineTemporal == intUplineActual) {
         //Buscamos la siguiente posicion en el arbol binario
         Redes redAlgoritmo = new Redes();
         int intUpline = redAlgoritmo.calculaUpline(this.intUplineInicial, 2, "", true, oConn, true);
         log.debug("Nvo upline:" + intUpline + " " + redAlgoritmo.getStrLadoNew());
         //Actualizamos el upline
         String strUpdate = "update vta_cliente set CT_UPLINE = " + intUpline + ""
            + ",CT_LADO = '" + redAlgoritmo.getStrLadoNew() + "' "
            + ",CT_FECHA_ACTIVA = '" + fecha.getFechaActual() + "' "
            + ",CT_ACTIVO = '1' "
            + ",MPE_ID = '" + this.intPeriodoActual + "' "
            + "WHERE CT_ID = " + intIdCliente;
         log.debug(strUpdate);
         oConn.runQueryLMD(strUpdate);
      }

      return strRes;
   }

   /**
    * Activa al distribuidor
    *
    * @param intIdCliente Es el id del cliente
    * @param oConn Es la conexion a la base de datos
    * @return
    */
   public String activarDistribuidorAfiliado(int intIdCliente, Conexion oConn) {
      String strRes = "OK";
      log.debug("X acomodar: " + intIdCliente);
      int intUplineActual = 0;
      //Validamos si tiene upline
      String strSql = "select CT_UPLINE from vta_cliente where CT_ID =  " + intIdCliente;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intUplineActual = rs.getInt("CT_UPLINE");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      if (this.intUplineTemporalAfiliado == intUplineActual) {
         //Buscamos la siguiente posicion en el arbol binario
         Redes redAlgoritmo = new Redes();
         int intUpline = redAlgoritmo.calculaUpline(this.intUplineInicial, 2, "", true, oConn, true);
         log.debug("Nvo upline:" + intUpline + " " + redAlgoritmo.getStrLadoNew());
         //Actualizamos el upline
         String strUpdate = "update vta_cliente set CT_UPLINE = " + intUpline + ",CT_LADO = '" + redAlgoritmo.getStrLadoNew() + "' WHERE CT_ID = " + intIdCliente;
         log.debug(strUpdate);
         oConn.runQueryLMD(strUpdate);
      }

      return strRes;
   }

   /**
    * Activa al distribuidor
    *
    * @param intIdCliente Es el id del cliente
    * @param oConn Es la conexion a la base de datos
    * @return
    */
   public String activarDistribuidorGlobal(int intIdCliente, Conexion oConn) {
      String strRes = "OK";
      log.debug("X acomodar: " + intIdCliente);
      int intUplineActual = 0;
      //Validamos si tiene upline
      String strSql = "select CT_UPLINE from vta_cliente where CT_ID =  " + intIdCliente;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intUplineActual = rs.getInt("CT_UPLINE");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      if (this.intUplineTemporalGlobal == intUplineActual) {
         //Buscamos la siguiente posicion en el arbol binario
         Redes redAlgoritmo = new Redes();
         int intUpline = redAlgoritmo.calculaUpline(this.intUplineInicial, 2, "", true, oConn, true);
         log.debug("Nvo upline:" + intUpline + " " + redAlgoritmo.getStrLadoNew());
         //Actualizamos el upline
         String strUpdate = "update vta_cliente set CT_UPLINE = " + intUpline + ",CT_LADO = '" + redAlgoritmo.getStrLadoNew() + "' WHERE CT_ID = " + intIdCliente;
         log.debug(strUpdate);
         oConn.runQueryLMD(strUpdate);
      }

      return strRes;
   }

   /**
    * Genera una copia de un cliente(Centro de distribucion)
    *
    * @param intCte Es el id del cliente
    * @param bolAfiliado Indica si sera un centro afiliado
    * @param oConn Es la conexion a la base de datos
    * @return Regresa el id del nuevo cliente
    */
   public int CopiaCliente(int intCte, boolean bolAfiliado, Conexion oConn) {
      int intNvoCliente = 0;
      // <editor-fold defaultstate="collapsed" desc="Query Afiliado">
      String strCopia = "insert into vta_cliente(\n"
         + "	CT_RAZONSOCIAL, \n"
         + "	CT_RFC, \n"
         + "	CT_CALLE, \n"
         + "	CT_COLONIA, \n"
         + "	CT_LOCALIDAD, \n"
         + "	CT_TXTIVA, \n"
         + "	CT_TXTIVAAGUA, \n"
         + "	CT_MUNICIPIO, \n"
         + "	CT_ESTADO, \n"
         + "	CT_CP, \n"
         + "	CT_TELEFONO1, \n"
         + "	CT_TELEFONO2, \n"
         + "	CT_CONTACTO1, \n"
         + "	CT_CONTACTO2, \n"
         + "	CT_FOLIO, \n"
         + "	CT_FOLIOPAPA, \n"
         + "	CT_NUMCEROS, \n"
         + "	CT_SALDO, \n"
         + "	CT_EMAIL1, \n"
         + "	CT_EMAIL2, \n"
         + "	CT_NUMERO, \n"
         + "	CT_NUMINT, \n"
         + "	CT_LPRECIOS, \n"
         + "	CT_DIASCREDITO, \n"
         + "	CT_MONTOCRED, \n"
         + "	CT_FECHAREG, \n"
         + "	CT_IDIOMA, \n"
         + "	SC_ID, \n"
         + "	CT_PASSWORD, \n"
         + "	CT_DESCUENTO, \n"
         + "	CT_VENDEDOR, \n"
         + "	CT_CONTAVTA, \n"
         + "	CT_CONTAPAG, \n"
         + "	CT_CONTANC, \n"
         + "	CT_FECHAULTINT, \n"
         + "	CT_HORAULTINT, \n"
         + "	CT_FECHAEXIT, \n"
         + "	CT_HORAEXIT, \n"
         + "	CT_FALLIDOS, \n"
         + "	CT_NOTAS, \n"
         + "	CT_EXITOSOS, \n"
         + "	CT_CATEGORIA1, \n"
         + "	CT_CATEGORIA2, \n"
         + "	CT_CATEGORIA3, \n"
         + "	CT_CATEGORIA4, \n"
         + "	CT_CATEGORIA5, \n"
         + "	CT_TIPOPERS, \n"
         + "	CT_USOIMBUEBLE, \n"
         + "	CT_TIPOFAC, \n"
         + "	CT_TIT_CONT1, \n"
         + "	CT_TIT_CONT2, \n"
         + "	CT_CONT_AP1, \n"
         + "	CT_CONT_AP2, \n"
         + "	CT_CONT_AM1, \n"
         + "	CT_CONT_AM2, \n"
         + "	EMP_ID, \n"
         + "	CT_CONTACTE, \n"
         + "	CT_CUENTAVTACRED, \n"
         + "	CT_UPLINE, \n"
         + "	CT_CONTACTO, \n"
         + "	CT_FECHAULTIMOCONTACTO, \n"
         + "	CT_ARMADOINI, \n"
         + "	CT_ARMADOFIN, \n"
         + "	CT_ARMADONUM, \n"
         + "	CT_ARMADODEEP, \n"
         + "	CT_SPONZOR, \n"
         + "	CT_LADO, \n"
         + "	CT_IS_LOGGED, \n"
         + "	CT_LAST_ACT, \n"
         + "	CT_LASTSESSIONID, \n"
         + "	CT_LASTIPADDRESS, \n"
         + "	CT_LAST_TIME, \n"
         + "	CT_LAST_TIME_FAIL, \n"
         + "	CT_IS_DISABLED, \n"
         + "	CT_CTABANCO1, \n"
         + "	CT_CTABANCO2, \n"
         + "	CT_CTATARJETA, \n"
         + "	CT_NUMPREDIAL, \n"
         + "	PA_ID, \n"
         + "	CT_ACTIVO, \n"
         + "	CT_RAZONCOMERCIAL, \n"
         + "	CT_CATEGORIA6, \n"
         + "	CT_CATEGORIA7, \n"
         + "	CT_CATEGORIA8, \n"
         + "	CT_CATEGORIA9, \n"
         + "	CT_CATEGORIA10, \n"
         + "	MON_ID, \n"
         + "	TI_ID, \n"
         + "	TTC_ID, \n"
         + "	CT_RBANCARIA1, \n"
         + "	CT_RBANCARIA2, \n"
         + "	CT_RBANCARIA3, \n"
         + "	CT_BANCO1, \n"
         + "	CT_BANCO2, \n"
         + "	CT_BANCO3, \n"
         + "	CT_METODODEPAGO, \n"
         + "	CT_FORMADEPAGO, \n"
         + "	CT_FECHA_NAC, \n"
         + "	CT_NOMBRE, \n"
         + "	CT_APATERNO, \n"
         + "	CT_AMATERNO, \n"
         + "	CT_PPUNTOS, \n"
         + "	CT_PNEGOCIO, \n"
         + "	CT_GPUNTOS, \n"
         + "	CT_GNEGOCIO, \n"
         + "	CT_COMISION, \n"
         + "	CT_NIVELRED, \n"
         + "	MPE_ID, \n"
         + "	CT_CONTEO_HIJOS, \n"
         + "	CT_CONTEO_HIJOS_ACTIVOS, \n"
         + "	CT_CONTEO_INGRESOS, \n"
         + "	CT_RLEGAL, \n"
         + "	CT_FIADOR, \n"
         + "	CT_F1DIRECCION, \n"
         + "	CT_F1IFE, \n"
         + "	CT_FIADOR2, \n"
         + "	CT_F2DIRECCION, \n"
         + "	CT_F2IFE, \n"
         + "	CT_FIADOR3, \n"
         + "	CT_F3DIRECCION, \n"
         + "	CT_F3IFE, \n"
         + "	CT_CHANGE_PASSWRD, \n"
         + "	CT_CTA_BANCO1, \n"
         + "	CT_CTA_BANCO2, \n"
         + "	CT_CTA_SUCURSAL1, \n"
         + "	CT_CTA_SUCURSAL2, \n"
         + "	CT_CTA_CLABE1, \n"
         + "	CT_CTA_CLABE2, \n"
         + "	CT_CONTACTE_COMPL, \n"
         + "	CT_CTA_ANTICIPO, \n"
         + "	CT_CTACTE_COMPL_ANTI, \n"
         + "	CT_CONTA_RET_ISR, \n"
         + "	CT_CONTA_RET_IVA, \n"
         + "	TI2_ID, \n"
         + "	TI3_ID, \n"
         + "	CT_BANCO_CTA1, \n"
         + "	CT_BANCO_CTA2, \n"
         + "	CT_CLABE1, \n"
         + "	CT_CLABE2, \n"
         + "	CT_CRED_ELECTOR, \n"
         + "	CT_ES_PROSPECTO, \n"
         + "	CT_FECHA_CONTACTO, \n"
         + "	CAT_MED_CONT_ID, \n"
         + "	CT_UBICACION_GOOGLE, \n"
         + "	CT_FACEBOOK, \n"
         + "	CT_PAGINA_WEB, \n"
         + "	CT_POR_CIERRE, \n"
         + "	EP_ID, \n"
         + "	CAM_ID, \n"
         + "	CT_CURP, \n"
         + "	CT_TELEFONO_RECARGAR, \n"
         + "	CT_COMPANIA_TEL_RECA, \n"
         + "	CT_BCO_CTA_BNCA, \n"
         + "	CT_ID_INVITO, \n"
         + "	CT_NOMBRE_INVITO, \n"
         + "	CT_CLABE_INTERBANCARIA, \n"
         + "	CT_GENERACION1, \n"
         + "	CT_GENERACION2, \n"
         + "	CT_GENERACION3, \n"
         + "	WN_TRAINING, \n"
         + "	WN_AFILIADO, \n"
         + "	WN_GLOBAL, \n"
         + "	WN_ACTIVO_AFILIADO, \n"
         + "	WN_ACTIVO_GLOBAL, \n"
         + "	WN_ID_MASTER, \n"
         + "	WN_ID_TRAINING, \n"
         + "	WN_ID_AFILIADO, \n"
         + "	WN_ID_GLOBAL\n"
         + ")select\n"
         + "	CT_RAZONSOCIAL, \n"
         + "	CT_RFC, \n"
         + "	CT_CALLE, \n"
         + "	CT_COLONIA, \n"
         + "	CT_LOCALIDAD, \n"
         + "	CT_TXTIVA, \n"
         + "	CT_TXTIVAAGUA, \n"
         + "	CT_MUNICIPIO, \n"
         + "	CT_ESTADO, \n"
         + "	CT_CP, \n"
         + "	CT_TELEFONO1, \n"
         + "	CT_TELEFONO2, \n"
         + "	CT_CONTACTO1, \n"
         + "	CT_CONTACTO2, \n"
         + "	CT_FOLIO, \n"
         + "	CT_FOLIOPAPA, \n"
         + "	CT_NUMCEROS, \n"
         + "	CT_SALDO, \n"
         + "	CT_EMAIL1, \n"
         + "	CT_EMAIL2, \n"
         + "	CT_NUMERO, \n"
         + "	CT_NUMINT, \n"
         + "	CT_LPRECIOS, \n"
         + "	CT_DIASCREDITO, \n"
         + "	CT_MONTOCRED, \n"
         + "	CT_FECHAREG, \n"
         + "	CT_IDIOMA, \n"
         + "	SC_ID, \n"
         + "	CT_PASSWORD, \n"
         + "	CT_DESCUENTO, \n"
         + "	CT_VENDEDOR, \n"
         + "	CT_CONTAVTA, \n"
         + "	CT_CONTAPAG, \n"
         + "	CT_CONTANC, \n"
         + "	CT_FECHAULTINT, \n"
         + "	CT_HORAULTINT, \n"
         + "	CT_FECHAEXIT, \n"
         + "	CT_HORAEXIT, \n"
         + "	CT_FALLIDOS, \n"
         + "	CT_NOTAS, \n"
         + "	CT_EXITOSOS, \n"
         + "	CT_CATEGORIA1, \n"
         + "	CT_CATEGORIA2, \n"
         + "	CT_CATEGORIA3, \n"
         + "	CT_CATEGORIA4, \n"
         + "	CT_CATEGORIA5, \n"
         + "	CT_TIPOPERS, \n"
         + "	CT_USOIMBUEBLE, \n"
         + "	CT_TIPOFAC, \n"
         + "	CT_TIT_CONT1, \n"
         + "	CT_TIT_CONT2, \n"
         + "	CT_CONT_AP1, \n"
         + "	CT_CONT_AP2, \n"
         + "	CT_CONT_AM1, \n"
         + "	CT_CONT_AM2, \n"
         + "	EMP_ID, \n"
         + "	CT_CONTACTE, \n"
         + "	CT_CUENTAVTACRED, \n"
         + "	3, \n"
         + "	CT_CONTACTO, \n"
         + "	CT_FECHAULTIMOCONTACTO, \n"
         + "	CT_ARMADOINI, \n"
         + "	CT_ARMADOFIN, \n"
         + "	CT_ARMADONUM, \n"
         + "	CT_ARMADODEEP, \n"
         + "	CT_SPONZOR, \n"
         + "	CT_LADO, \n"
         + "	CT_IS_LOGGED, \n"
         + "	CT_LAST_ACT, \n"
         + "	CT_LASTSESSIONID, \n"
         + "	CT_LASTIPADDRESS, \n"
         + "	CT_LAST_TIME, \n"
         + "	CT_LAST_TIME_FAIL, \n"
         + "	CT_IS_DISABLED, \n"
         + "	CT_CTABANCO1, \n"
         + "	CT_CTABANCO2, \n"
         + "	CT_CTATARJETA, \n"
         + "	CT_NUMPREDIAL, \n"
         + "	PA_ID, \n"
         + "	CT_ACTIVO, \n"
         + "	CT_RAZONCOMERCIAL, \n"
         + "	CT_CATEGORIA6, \n"
         + "	CT_CATEGORIA7, \n"
         + "	CT_CATEGORIA8, \n"
         + "	CT_CATEGORIA9, \n"
         + "	CT_CATEGORIA10, \n"
         + "	MON_ID, \n"
         + "	TI_ID, \n"
         + "	TTC_ID, \n"
         + "	CT_RBANCARIA1, \n"
         + "	CT_RBANCARIA2, \n"
         + "	CT_RBANCARIA3, \n"
         + "	CT_BANCO1, \n"
         + "	CT_BANCO2, \n"
         + "	CT_BANCO3, \n"
         + "	CT_METODODEPAGO, \n"
         + "	CT_FORMADEPAGO, \n"
         + "	CT_FECHA_NAC, \n"
         + "	CT_NOMBRE, \n"
         + "	CT_APATERNO, \n"
         + "	CT_AMATERNO, \n"
         + "	CT_PPUNTOS, \n"
         + "	CT_PNEGOCIO, \n"
         + "	CT_GPUNTOS, \n"
         + "	CT_GNEGOCIO, \n"
         + "	CT_COMISION, \n"
         + "	CT_NIVELRED, \n"
         + "	MPE_ID, \n"
         + "	CT_CONTEO_HIJOS, \n"
         + "	CT_CONTEO_HIJOS_ACTIVOS, \n"
         + "	CT_CONTEO_INGRESOS, \n"
         + "	CT_RLEGAL, \n"
         + "	CT_FIADOR, \n"
         + "	CT_F1DIRECCION, \n"
         + "	CT_F1IFE, \n"
         + "	CT_FIADOR2, \n"
         + "	CT_F2DIRECCION, \n"
         + "	CT_F2IFE, \n"
         + "	CT_FIADOR3, \n"
         + "	CT_F3DIRECCION, \n"
         + "	CT_F3IFE, \n"
         + "	CT_CHANGE_PASSWRD, \n"
         + "	CT_CTA_BANCO1, \n"
         + "	CT_CTA_BANCO2, \n"
         + "	CT_CTA_SUCURSAL1, \n"
         + "	CT_CTA_SUCURSAL2, \n"
         + "	CT_CTA_CLABE1, \n"
         + "	CT_CTA_CLABE2, \n"
         + "	CT_CONTACTE_COMPL, \n"
         + "	CT_CTA_ANTICIPO, \n"
         + "	CT_CTACTE_COMPL_ANTI, \n"
         + "	CT_CONTA_RET_ISR, \n"
         + "	CT_CONTA_RET_IVA, \n"
         + "	TI2_ID, \n"
         + "	TI3_ID, \n"
         + "	CT_BANCO_CTA1, \n"
         + "	CT_BANCO_CTA2, \n"
         + "	CT_CLABE1, \n"
         + "	CT_CLABE2, \n"
         + "	CT_CRED_ELECTOR, \n"
         + "	CT_ES_PROSPECTO, \n"
         + "	CT_FECHA_CONTACTO, \n"
         + "	CAT_MED_CONT_ID, \n"
         + "	CT_UBICACION_GOOGLE, \n"
         + "	CT_FACEBOOK, \n"
         + "	CT_PAGINA_WEB, \n"
         + "	CT_POR_CIERRE, \n"
         + "	EP_ID, \n"
         + "	CAM_ID, \n"
         + "	CT_CURP, \n"
         + "	CT_TELEFONO_RECARGAR, \n"
         + "	CT_COMPANIA_TEL_RECA, \n"
         + "	CT_BCO_CTA_BNCA, \n"
         + "	CT_ID_INVITO, \n"
         + "	CT_NOMBRE_INVITO, \n"
         + "	CT_CLABE_INTERBANCARIA, \n"
         + "	CT_GENERACION1, \n"
         + "	CT_GENERACION2, \n"
         + "	CT_GENERACION3, \n"
         + "	0, \n"
         + "	1, \n"
         + "	0, \n"
         + "	1, \n"
         + "	0, \n"
         + "	CT_ID, \n"
         + "	0, \n"
         + "	0, \n"
         + "	0\n"
         + "from vta_cliente where CT_ID = " + intCte;
      // </editor-fold>
      //Si no es afiliado
      if (!bolAfiliado) {
         // <editor-fold defaultstate="collapsed" desc="Query global">
         strCopia = "insert into vta_cliente(\n"
            + "	CT_RAZONSOCIAL, \n"
            + "	CT_RFC, \n"
            + "	CT_CALLE, \n"
            + "	CT_COLONIA, \n"
            + "	CT_LOCALIDAD, \n"
            + "	CT_TXTIVA, \n"
            + "	CT_TXTIVAAGUA, \n"
            + "	CT_MUNICIPIO, \n"
            + "	CT_ESTADO, \n"
            + "	CT_CP, \n"
            + "	CT_TELEFONO1, \n"
            + "	CT_TELEFONO2, \n"
            + "	CT_CONTACTO1, \n"
            + "	CT_CONTACTO2, \n"
            + "	CT_FOLIO, \n"
            + "	CT_FOLIOPAPA, \n"
            + "	CT_NUMCEROS, \n"
            + "	CT_SALDO, \n"
            + "	CT_EMAIL1, \n"
            + "	CT_EMAIL2, \n"
            + "	CT_NUMERO, \n"
            + "	CT_NUMINT, \n"
            + "	CT_LPRECIOS, \n"
            + "	CT_DIASCREDITO, \n"
            + "	CT_MONTOCRED, \n"
            + "	CT_FECHAREG, \n"
            + "	CT_IDIOMA, \n"
            + "	SC_ID, \n"
            + "	CT_PASSWORD, \n"
            + "	CT_DESCUENTO, \n"
            + "	CT_VENDEDOR, \n"
            + "	CT_CONTAVTA, \n"
            + "	CT_CONTAPAG, \n"
            + "	CT_CONTANC, \n"
            + "	CT_FECHAULTINT, \n"
            + "	CT_HORAULTINT, \n"
            + "	CT_FECHAEXIT, \n"
            + "	CT_HORAEXIT, \n"
            + "	CT_FALLIDOS, \n"
            + "	CT_NOTAS, \n"
            + "	CT_EXITOSOS, \n"
            + "	CT_CATEGORIA1, \n"
            + "	CT_CATEGORIA2, \n"
            + "	CT_CATEGORIA3, \n"
            + "	CT_CATEGORIA4, \n"
            + "	CT_CATEGORIA5, \n"
            + "	CT_TIPOPERS, \n"
            + "	CT_USOIMBUEBLE, \n"
            + "	CT_TIPOFAC, \n"
            + "	CT_TIT_CONT1, \n"
            + "	CT_TIT_CONT2, \n"
            + "	CT_CONT_AP1, \n"
            + "	CT_CONT_AP2, \n"
            + "	CT_CONT_AM1, \n"
            + "	CT_CONT_AM2, \n"
            + "	EMP_ID, \n"
            + "	CT_CONTACTE, \n"
            + "	CT_CUENTAVTACRED, \n"
            + "	CT_UPLINE, \n"
            + "	CT_CONTACTO, \n"
            + "	CT_FECHAULTIMOCONTACTO, \n"
            + "	CT_ARMADOINI, \n"
            + "	CT_ARMADOFIN, \n"
            + "	CT_ARMADONUM, \n"
            + "	CT_ARMADODEEP, \n"
            + "	CT_SPONZOR, \n"
            + "	CT_LADO, \n"
            + "	CT_IS_LOGGED, \n"
            + "	CT_LAST_ACT, \n"
            + "	CT_LASTSESSIONID, \n"
            + "	CT_LASTIPADDRESS, \n"
            + "	CT_LAST_TIME, \n"
            + "	CT_LAST_TIME_FAIL, \n"
            + "	CT_IS_DISABLED, \n"
            + "	CT_CTABANCO1, \n"
            + "	CT_CTABANCO2, \n"
            + "	CT_CTATARJETA, \n"
            + "	CT_NUMPREDIAL, \n"
            + "	PA_ID, \n"
            + "	CT_ACTIVO, \n"
            + "	CT_RAZONCOMERCIAL, \n"
            + "	CT_CATEGORIA6, \n"
            + "	CT_CATEGORIA7, \n"
            + "	CT_CATEGORIA8, \n"
            + "	CT_CATEGORIA9, \n"
            + "	CT_CATEGORIA10, \n"
            + "	MON_ID, \n"
            + "	TI_ID, \n"
            + "	TTC_ID, \n"
            + "	CT_RBANCARIA1, \n"
            + "	CT_RBANCARIA2, \n"
            + "	CT_RBANCARIA3, \n"
            + "	CT_BANCO1, \n"
            + "	CT_BANCO2, \n"
            + "	CT_BANCO3, \n"
            + "	CT_METODODEPAGO, \n"
            + "	CT_FORMADEPAGO, \n"
            + "	CT_FECHA_NAC, \n"
            + "	CT_NOMBRE, \n"
            + "	CT_APATERNO, \n"
            + "	CT_AMATERNO, \n"
            + "	CT_PPUNTOS, \n"
            + "	CT_PNEGOCIO, \n"
            + "	CT_GPUNTOS, \n"
            + "	CT_GNEGOCIO, \n"
            + "	CT_COMISION, \n"
            + "	CT_NIVELRED, \n"
            + "	MPE_ID, \n"
            + "	CT_CONTEO_HIJOS, \n"
            + "	CT_CONTEO_HIJOS_ACTIVOS, \n"
            + "	CT_CONTEO_INGRESOS, \n"
            + "	CT_RLEGAL, \n"
            + "	CT_FIADOR, \n"
            + "	CT_F1DIRECCION, \n"
            + "	CT_F1IFE, \n"
            + "	CT_FIADOR2, \n"
            + "	CT_F2DIRECCION, \n"
            + "	CT_F2IFE, \n"
            + "	CT_FIADOR3, \n"
            + "	CT_F3DIRECCION, \n"
            + "	CT_F3IFE, \n"
            + "	CT_CHANGE_PASSWRD, \n"
            + "	CT_CTA_BANCO1, \n"
            + "	CT_CTA_BANCO2, \n"
            + "	CT_CTA_SUCURSAL1, \n"
            + "	CT_CTA_SUCURSAL2, \n"
            + "	CT_CTA_CLABE1, \n"
            + "	CT_CTA_CLABE2, \n"
            + "	CT_CONTACTE_COMPL, \n"
            + "	CT_CTA_ANTICIPO, \n"
            + "	CT_CTACTE_COMPL_ANTI, \n"
            + "	CT_CONTA_RET_ISR, \n"
            + "	CT_CONTA_RET_IVA, \n"
            + "	TI2_ID, \n"
            + "	TI3_ID, \n"
            + "	CT_BANCO_CTA1, \n"
            + "	CT_BANCO_CTA2, \n"
            + "	CT_CLABE1, \n"
            + "	CT_CLABE2, \n"
            + "	CT_CRED_ELECTOR, \n"
            + "	CT_ES_PROSPECTO, \n"
            + "	CT_FECHA_CONTACTO, \n"
            + "	CAT_MED_CONT_ID, \n"
            + "	CT_UBICACION_GOOGLE, \n"
            + "	CT_FACEBOOK, \n"
            + "	CT_PAGINA_WEB, \n"
            + "	CT_POR_CIERRE, \n"
            + "	EP_ID, \n"
            + "	CAM_ID, \n"
            + "	CT_CURP, \n"
            + "	CT_TELEFONO_RECARGAR, \n"
            + "	CT_COMPANIA_TEL_RECA, \n"
            + "	CT_BCO_CTA_BNCA, \n"
            + "	CT_ID_INVITO, \n"
            + "	CT_NOMBRE_INVITO, \n"
            + "	CT_CLABE_INTERBANCARIA, \n"
            + "	CT_GENERACION1, \n"
            + "	CT_GENERACION2, \n"
            + "	CT_GENERACION3, \n"
            + "	WN_TRAINING, \n"
            + "	WN_AFILIADO, \n"
            + "	WN_GLOBAL, \n"
            + "	WN_ACTIVO_AFILIADO, \n"
            + "	WN_ACTIVO_GLOBAL, \n"
            + "	WN_ID_MASTER, \n"
            + "	WN_ID_TRAINING, \n"
            + "	WN_ID_AFILIADO, \n"
            + "	WN_ID_GLOBAL\n"
            + ")select\n"
            + "	CT_RAZONSOCIAL, \n"
            + "	CT_RFC, \n"
            + "	CT_CALLE, \n"
            + "	CT_COLONIA, \n"
            + "	CT_LOCALIDAD, \n"
            + "	CT_TXTIVA, \n"
            + "	CT_TXTIVAAGUA, \n"
            + "	CT_MUNICIPIO, \n"
            + "	CT_ESTADO, \n"
            + "	CT_CP, \n"
            + "	CT_TELEFONO1, \n"
            + "	CT_TELEFONO2, \n"
            + "	CT_CONTACTO1, \n"
            + "	CT_CONTACTO2, \n"
            + "	CT_FOLIO, \n"
            + "	CT_FOLIOPAPA, \n"
            + "	CT_NUMCEROS, \n"
            + "	CT_SALDO, \n"
            + "	CT_EMAIL1, \n"
            + "	CT_EMAIL2, \n"
            + "	CT_NUMERO, \n"
            + "	CT_NUMINT, \n"
            + "	CT_LPRECIOS, \n"
            + "	CT_DIASCREDITO, \n"
            + "	CT_MONTOCRED, \n"
            + "	CT_FECHAREG, \n"
            + "	CT_IDIOMA, \n"
            + "	SC_ID, \n"
            + "	CT_PASSWORD, \n"
            + "	CT_DESCUENTO, \n"
            + "	CT_VENDEDOR, \n"
            + "	CT_CONTAVTA, \n"
            + "	CT_CONTAPAG, \n"
            + "	CT_CONTANC, \n"
            + "	CT_FECHAULTINT, \n"
            + "	CT_HORAULTINT, \n"
            + "	CT_FECHAEXIT, \n"
            + "	CT_HORAEXIT, \n"
            + "	CT_FALLIDOS, \n"
            + "	CT_NOTAS, \n"
            + "	CT_EXITOSOS, \n"
            + "	CT_CATEGORIA1, \n"
            + "	CT_CATEGORIA2, \n"
            + "	CT_CATEGORIA3, \n"
            + "	CT_CATEGORIA4, \n"
            + "	CT_CATEGORIA5, \n"
            + "	CT_TIPOPERS, \n"
            + "	CT_USOIMBUEBLE, \n"
            + "	CT_TIPOFAC, \n"
            + "	CT_TIT_CONT1, \n"
            + "	CT_TIT_CONT2, \n"
            + "	CT_CONT_AP1, \n"
            + "	CT_CONT_AP2, \n"
            + "	CT_CONT_AM1, \n"
            + "	CT_CONT_AM2, \n"
            + "	EMP_ID, \n"
            + "	CT_CONTACTE, \n"
            + "	CT_CUENTAVTACRED, \n"
            + "	3, \n"
            + "	CT_CONTACTO, \n"
            + "	CT_FECHAULTIMOCONTACTO, \n"
            + "	CT_ARMADOINI, \n"
            + "	CT_ARMADOFIN, \n"
            + "	CT_ARMADONUM, \n"
            + "	CT_ARMADODEEP, \n"
            + "	CT_SPONZOR, \n"
            + "	CT_LADO, \n"
            + "	CT_IS_LOGGED, \n"
            + "	CT_LAST_ACT, \n"
            + "	CT_LASTSESSIONID, \n"
            + "	CT_LASTIPADDRESS, \n"
            + "	CT_LAST_TIME, \n"
            + "	CT_LAST_TIME_FAIL, \n"
            + "	CT_IS_DISABLED, \n"
            + "	CT_CTABANCO1, \n"
            + "	CT_CTABANCO2, \n"
            + "	CT_CTATARJETA, \n"
            + "	CT_NUMPREDIAL, \n"
            + "	PA_ID, \n"
            + "	CT_ACTIVO, \n"
            + "	CT_RAZONCOMERCIAL, \n"
            + "	CT_CATEGORIA6, \n"
            + "	CT_CATEGORIA7, \n"
            + "	CT_CATEGORIA8, \n"
            + "	CT_CATEGORIA9, \n"
            + "	CT_CATEGORIA10, \n"
            + "	MON_ID, \n"
            + "	TI_ID, \n"
            + "	TTC_ID, \n"
            + "	CT_RBANCARIA1, \n"
            + "	CT_RBANCARIA2, \n"
            + "	CT_RBANCARIA3, \n"
            + "	CT_BANCO1, \n"
            + "	CT_BANCO2, \n"
            + "	CT_BANCO3, \n"
            + "	CT_METODODEPAGO, \n"
            + "	CT_FORMADEPAGO, \n"
            + "	CT_FECHA_NAC, \n"
            + "	CT_NOMBRE, \n"
            + "	CT_APATERNO, \n"
            + "	CT_AMATERNO, \n"
            + "	CT_PPUNTOS, \n"
            + "	CT_PNEGOCIO, \n"
            + "	CT_GPUNTOS, \n"
            + "	CT_GNEGOCIO, \n"
            + "	CT_COMISION, \n"
            + "	CT_NIVELRED, \n"
            + "	MPE_ID, \n"
            + "	CT_CONTEO_HIJOS, \n"
            + "	CT_CONTEO_HIJOS_ACTIVOS, \n"
            + "	CT_CONTEO_INGRESOS, \n"
            + "	CT_RLEGAL, \n"
            + "	CT_FIADOR, \n"
            + "	CT_F1DIRECCION, \n"
            + "	CT_F1IFE, \n"
            + "	CT_FIADOR2, \n"
            + "	CT_F2DIRECCION, \n"
            + "	CT_F2IFE, \n"
            + "	CT_FIADOR3, \n"
            + "	CT_F3DIRECCION, \n"
            + "	CT_F3IFE, \n"
            + "	CT_CHANGE_PASSWRD, \n"
            + "	CT_CTA_BANCO1, \n"
            + "	CT_CTA_BANCO2, \n"
            + "	CT_CTA_SUCURSAL1, \n"
            + "	CT_CTA_SUCURSAL2, \n"
            + "	CT_CTA_CLABE1, \n"
            + "	CT_CTA_CLABE2, \n"
            + "	CT_CONTACTE_COMPL, \n"
            + "	CT_CTA_ANTICIPO, \n"
            + "	CT_CTACTE_COMPL_ANTI, \n"
            + "	CT_CONTA_RET_ISR, \n"
            + "	CT_CONTA_RET_IVA, \n"
            + "	TI2_ID, \n"
            + "	TI3_ID, \n"
            + "	CT_BANCO_CTA1, \n"
            + "	CT_BANCO_CTA2, \n"
            + "	CT_CLABE1, \n"
            + "	CT_CLABE2, \n"
            + "	CT_CRED_ELECTOR, \n"
            + "	CT_ES_PROSPECTO, \n"
            + "	CT_FECHA_CONTACTO, \n"
            + "	CAT_MED_CONT_ID, \n"
            + "	CT_UBICACION_GOOGLE, \n"
            + "	CT_FACEBOOK, \n"
            + "	CT_PAGINA_WEB, \n"
            + "	CT_POR_CIERRE, \n"
            + "	EP_ID, \n"
            + "	CAM_ID, \n"
            + "	CT_CURP, \n"
            + "	CT_TELEFONO_RECARGAR, \n"
            + "	CT_COMPANIA_TEL_RECA, \n"
            + "	CT_BCO_CTA_BNCA, \n"
            + "	CT_ID_INVITO, \n"
            + "	CT_NOMBRE_INVITO, \n"
            + "	CT_CLABE_INTERBANCARIA, \n"
            + "	CT_GENERACION1, \n"
            + "	CT_GENERACION2, \n"
            + "	CT_GENERACION3, \n"
            + "	0, \n"
            + "	0, \n"
            + "	1, \n"
            + "	0, \n"
            + "	1, \n"
            + "	CT_ID, \n"
            + "	0, \n"
            + "	0, \n"
            + "	0\n"
            + "from vta_cliente where CT_ID = " + intCte;
         // </editor-fold>
      }
      oConn.runQueryLMD(strCopia);
      ResultSet rs;
      try {
         rs = oConn.runQuery("select @@identity", true);
         while (rs.next()) {
            intNvoCliente = rs.getInt(1);
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return intNvoCliente;
   }
   // </editor-fold>
}
