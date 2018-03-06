/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.notascargo;

import Core.FirmasElectronicas.Opalina;
import Core.FirmasElectronicas.SATXml;
import Core.FirmasElectronicas.SATXml3_0;
import Core.FirmasElectronicas.SatCancelaCFDI;
import Core.FirmasElectronicas.Utils.UtilCert;
import ERP.ContabilidadUtil;
import ERP.ProcesoInterfaz;
import ERP.CuentasxPagar;
import ERP.ERP_MapeoFormato;
import ERP.Folios;
import ERP.MovProveedor;
import ERP.Ticket;
import Tablas.VtaNotasCargosProv;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Genera los objetos para las notas de cargo del proveedor
 *
 * @author ZeusSIWEB
 */
public class NotasCargoProveedor extends CuentasxPagar implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(NotasCargoProveedor.class.getName());
   String strPrefijoMaster = null;
   String strNomTablaMaster = null;
   //Campos para impuestos a usar en el BakOrder
   protected int intSC_SOBRIMP1_2 = 0;
   protected int intSC_SOBRIMP1_3 = 0;
   protected int intSC_SOBRIMP2_3 = 0;
   protected double dblSC_TASA1 = 0;
   protected double dblSC_TASA2 = 0;
   protected double dblSC_TASA3 = 0;
   private boolean bolAfectoCargosAbonos;
   private boolean bolRecoveryIdLost;
   protected SATXml SAT = null;
   protected String strPATHXml;
   protected String strPATHKeys;
   protected String strMyPassSecret;
   private boolean bolUsoLugarExpEmp;
   protected boolean bolUsaTrxComoFolio = false;
   protected String strPATHFonts;

   public String getStrMyPassSecret() {
      return strMyPassSecret;
   }

   public void setStrMyPassSecret(String strMyPassSecret) {
      this.strMyPassSecret = strMyPassSecret;
   }

   /**
    * Regresa el path donde se encuentran las fuentes
    *
    * @return Es una cadena de texto con un path
    */
   public String getStrPATHFonts() {
      return strPATHFonts;
   }

   /**
    * Define el path donde se encuentran las fuentes
    *
    * @param strPATHFonts Es una cadena de texto con un path
    */
   public void setStrPATHFonts(String strPATHFonts) {
      this.strPATHFonts = strPATHFonts;
   }

   /**
    * Nos regresa si usaremos la transaccion como el folio por mostrar
    *
    * @return
    */
   public boolean isBolUsaTrxComoFolio() {
      return bolUsaTrxComoFolio;
   }

   /**
    * Definimos que usaremos la transaccion como el folio por mostrar
    *
    * @param bolUsaTrxComoFolio Con true activamos esta propiedad
    */
   public void setBolUsaTrxComoFolio(boolean bolUsaTrxComoFolio) {
      this.bolUsaTrxComoFolio = bolUsaTrxComoFolio;
   }

   /**
    * Nos regresa que el lugar de expedicion lo saque de la empresa
    *
    * @return Indica true si se usa la empresa como lugar de expedicion
    */
   public boolean isBolUsoLugarExpEmp() {
      return bolUsoLugarExpEmp;
   }

   /**
    * Indica que el lugar de expedicion lo saque de la empresa
    *
    * @param bolUsoLugarExpEmp Indica true si se usa la empresa como lugar de
    * expedicion
    */
   public void setBolUsoLugarExpEmp(boolean bolUsoLugarExpEmp) {
      this.bolUsoLugarExpEmp = bolUsoLugarExpEmp;
   }

   public boolean isBolRecoveryIdLost() {
      return bolRecoveryIdLost;
   }

   public void setBolRecoveryIdLost(boolean bolRecoveryIdLost) {
      this.bolRecoveryIdLost = bolRecoveryIdLost;
   }

   public boolean isBolAfectoCargosAbonos() {
      return bolAfectoCargosAbonos;
   }

   public void setBolAfectoCargosAbonos(boolean bolAfectoCargosAbonos) {
      this.bolAfectoCargosAbonos = bolAfectoCargosAbonos;
   }

   /**
    * Es el path donde guardamos las llaves privadas
    *
    * @return Regresa una cadena
    */
   public String getStrPATHKeys() {
      return strPATHKeys;
   }

   /**
    * Es el path donde guardamos las llaves privadas
    *
    * @param strPATHKeys Regresa una cadena
    */
   public void setStrPATHKeys(String strPATHKeys) {
      this.strPATHKeys = strPATHKeys;
   }

   /**
    * Es el path donde guardamos los xml de las facturas electronica
    *
    * @return Regresa una cadena
    */
   public String getStrPATHXml() {
      return strPATHXml;
   }

   /**
    * Es el path donde guardamos los xml de las facturas electronica
    *
    * @param strPATHXml Regresa una cadena
    */
   public void setStrPATHXml(String strPATHXml) {
      this.strPATHXml = strPATHXml;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public NotasCargoProveedor(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new VtaNotasCargosProv();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strFechaAnul = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      strPrefijoMaster = "NCA";
      strNomTablaMaster = "vta_notas_cargosprov";
      bolAfectoCargosAbonos = true;
      this.strMyPassSecret = "";
   }

   public NotasCargoProveedor(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new VtaNotasCargosProv();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strFechaAnul = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      strPrefijoMaster = "NCA";
      strNomTablaMaster = "vta_notas_cargosprov";
      bolAfectoCargosAbonos = true;
      this.strMyPassSecret = "";
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      this.document.setFieldInt(this.strPrefijoMaster + "_US_ALTA", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") != 0) {
         this.strFechaAnul = this.document.getFieldString(this.strPrefijoMaster + "_FECHAANUL");
         //¿Validamos o asignamos la tabla?
         this.document.ObtenDatos(this.document.getFieldInt(this.strPrefijoMaster + "_ID"), oConn);
      }
   }

   @Override
   public void doTrx() {
      // <editor-fold defaultstate="collapsed" desc="Inicializamos valores">
      this.strResultLast = "OK";
      this.document.setFieldString(this.strPrefijoMaster + "_FECHACREATE", this.fecha.getFechaActual());
      this.document.setFieldString(this.strPrefijoMaster + "_HORA", this.fecha.getHoraActualHHMMSS());
      //Valores para retencion
      int intPV_TIPOPERS = 0;
      int intEMP_TIPOPERS = 0;
      //Valores para contabilidad
      int intEMP_USECONTA = 0;
      int intEMP_CFD_CFDI = 0;
      String strCXPGlobal = "";
      String strCXPProvGlobal = "";
      String strCXPIVAGlobal = "";
      String strEMP_URLCP = "";
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      boolean bolFirmaSAT = false;
      String strNoSerieCert = "";
      String strNomKey = "";
      String strNomCert = "";
      String strPassKey = "";

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
      if (this.document.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.document.getFieldString(this.strPrefijoMaster + "_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.document.getFieldString(this.strPrefijoMaster + "_METODODEPAGO").equals("")) {
         this.document.setFieldString(this.strPrefijoMaster + "_METODODEPAGO", "NO IDENTIFICADO");
      }
      if (this.document.getFieldInt("PV_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el proveedor";
      }
      if (this.document.getFieldInt("CXP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la cuenta por pagar";
      }
      if (this.lstMovs.isEmpty()) {
         this.strResultLast = "ERROR:Debe tener por lo menos un item la venta";
      }
      //Asignamos empresa...
      if (this.document.getFieldInt("EMP_ID") == 0) {
         this.document.setFieldInt("EMP_ID", this.intEMP_ID);
      }
      //Evaluamos la fecha de cierre
      System.out.println(this.strPrefijoMaster + "_FECHA" + this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      //Continuamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {

         // <editor-fold defaultstate="collapsed" desc="Copiar datos del proveedor">
         boolean bolFindProv = false;
         String strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_CALLE,PV_COLONIA,PV_LOCALIDAD,"
            + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_CONTAPROV,PV_CONTACOMP,"
            + " PV_CP,PV_DIASCREDITO,PV_LPRECIOS,PV_TIPOPERS FROM vta_proveedor "
            + " where PV_ID=" + this.document.getFieldInt("PV_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               this.document.setFieldString(this.strPrefijoMaster + "_RAZONSOCIAL", rs.getString("PV_RAZONSOCIAL"));
               this.document.setFieldString(this.strPrefijoMaster + "_RFC", rs.getString("PV_RFC"));
               this.document.setFieldString(this.strPrefijoMaster + "_CALLE", rs.getString("PV_CALLE"));
               this.document.setFieldString(this.strPrefijoMaster + "_COLONIA", rs.getString("PV_COLONIA"));
               this.document.setFieldString(this.strPrefijoMaster + "_LOCALIDAD", rs.getString("PV_LOCALIDAD"));
               this.document.setFieldString(this.strPrefijoMaster + "_MUNICIPIO", rs.getString("PV_MUNICIPIO"));
               this.document.setFieldString(this.strPrefijoMaster + "_ESTADO", rs.getString("PV_ESTADO"));
               this.document.setFieldString(this.strPrefijoMaster + "_NUMERO", rs.getString("PV_NUMERO"));
               this.document.setFieldString(this.strPrefijoMaster + "_NUMINT", rs.getString("PV_NUMINT"));
               this.document.setFieldString(this.strPrefijoMaster + "_CP", rs.getString("PV_CP"));
               if (this.document.getFieldInt(this.strPrefijoMaster + "_DIASCREDITO") == 0) {
                  this.document.setFieldInt(this.strPrefijoMaster + "_DIASCREDITO", rs.getInt("PV_DIASCREDITO"));
               }
               if (this.document.getFieldInt(this.strPrefijoMaster + "_LPRECIOS") == 0) {
                  this.document.setFieldInt(this.strPrefijoMaster + "_LPRECIOS", rs.getInt("PV_LPRECIOS"));
               }

               intPV_TIPOPERS = rs.getInt("PV_TIPOPERS");
               bolFindProv = true;
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si el proveedor se encontro">
         if (bolFindProv) {

            // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara la nota de cargo">
            strSql = "SELECT EMP_ID,SC_TASA1,SC_TASA2,SC_TASA3,"
               + "SC_SOBRIMP1_2,SC_SOBRIMP1_3,SC_SOBRIMP2_3 "
               + "FROM vta_sucursal WHERE SC_ID = " + this.document.getFieldInt("SC_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  this.document.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
                  this.dblSC_TASA1 = rs.getDouble("SC_TASA1");
                  this.dblSC_TASA2 = rs.getDouble("SC_TASA2");
                  this.dblSC_TASA3 = rs.getDouble("SC_TASA3");
                  this.intSC_SOBRIMP1_2 = rs.getInt("SC_SOBRIMP1_2");
                  this.intSC_SOBRIMP1_3 = rs.getInt("SC_SOBRIMP1_3");
                  this.intSC_SOBRIMP2_3 = rs.getInt("SC_SOBRIMP2_3");
               }
               if (rs.getStatement() != null) {
                  //rs.getStatement().close();
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            //Si se definio el id de la empresa independiente de la sucursal
            if (this.intEMP_ID != 0) {
               this.document.setFieldInt("EMP_ID", this.intEMP_ID);
               this.bolFolioGlobal = false;
            }

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="En caso de factura obtenemos los parametros adicionales para la firma electronica">
            boolean bolTmp = oConn.isBolMostrarQuerys();
            oConn.setBolMostrarQuerys(false);
            // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
            strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
               + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_USECONTA,EMP_CTAVTA,EMP_CTAPROV,EMP_CTAIVA,"
               + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,"
               + "EMP_CFD_CFDI,"
               + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_URLCP,EMP_USACODBARR,EMP_VTA_DETA "
               + "FROM vta_empresas "
               + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                  this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
                  if (rs.getInt("EMP_FIRMA") == 1) {
                     bolFirmaSAT = true;
                     strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                     strNomKey = rs.getString("EMP_NOMKEY");
                     strNomCert = rs.getString("EMP_NOMCERT");
                     strPassKey = rs.getString("unencrypted");
                  }
                  intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                  strCXPGlobal = rs.getString("EMP_CTAVTA");
                  strCXPIVAGlobal = rs.getString("EMP_CTAIVA");
                  strCXPProvGlobal = rs.getString("EMP_CTAPROV");
                  strEMP_PASSCP = rs.getString("EMP_PASSCP");
                  strEMP_USERCP = rs.getString("EMP_USERCP");
                  strEMP_URLCP = rs.getString("EMP_URLCP");
               }
               if (rs.getStatement() != null) {
                  //rs.getStatement().close();
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            // </editor-fold>
            oConn.setBolMostrarQuerys(bolTmp);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Proceso de guardado del documento">
            if (this.strResultLast.equals("OK")) {
               int intId = 0;

               // <editor-fold defaultstate="collapsed" desc="Iniciar transaccion">
               if (this.bolTransaccionalidad) {
                  this.oConn.runQueryLMD("BEGIN");
               }
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Folios para el documento">
               this.document.setBolGetAutonumeric(true);
               boolean bolCalcula = true;
               if (bolCalcula) {
                  // <editor-fold defaultstate="collapsed" desc="Calculo del folio">
                  Folios folio = new Folios();
                  //Determinamos el tipo de ticket
                  int intTipoFolio = Folios.NOTA_CARGO_PROV;
                  folio.setBolEsLocal(this.bolEsLocal);
                  if (this.document.getFieldString(this.strPrefijoMaster + "_FOLIO").equals("")) {
                     if (this.document.getFieldString(this.strPrefijoMaster + "_SERIE") != null) {
                        folio.setStrSerie(this.document.getFieldString(this.strPrefijoMaster + "_SERIE"));
                     }
                     String strFolio = folio.doFolio(oConn, intTipoFolio, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                     this.document.setFieldString(this.strPrefijoMaster + "_FOLIO", strFolio);
                  } else {
                     String strFolio = this.document.getFieldString(this.strPrefijoMaster + "_FOLIO");
                  }
                  // </editor-fold>

               }
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Si todo esta ok guardamos el documento">
               if (this.strResultLast.equals("OK")) {
                  String strRes1 = this.document.Agrega(oConn);
                  if (!strRes1.equals("OK")) {
                     this.strResultLast = strRes1;
                  }
               }
               // </editor-fold>

               if (this.strResultLast.equals("OK")) {

                  // <editor-fold defaultstate="collapsed" desc="Guardar detalle">
                  intId = Integer.valueOf(this.document.getValorKey());
                  Iterator<TableMaster> it = this.lstMovs.iterator();
                  while (it.hasNext()) {
                     TableMaster deta = it.next();
                     deta.setFieldInt(this.strPrefijoMaster + "_ID", intId);
                     deta.setBolGetAutonumeric(true);
                     String strRes2 = deta.Agrega(oConn);
                     //Validamos si todo fue satisfactorio
                     if (!strRes2.equals("OK")) {
                        this.strResultLast = strRes2;
                        break;
                     }
                  }
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="Afectaciones a otros objetos de negocio">
                  if (this.strResultLast.equals("OK")) {
                     //Si todo resulto bien proseguimos
                     if (bolAfectoCargosAbonos) {
                        // <editor-fold defaultstate="collapsed" desc="Cargo a proveedores">
                        //Crear objeto de cta cliente y aplicarlo
                        //Generamos cargo por el importe de la deuda
                        MovProveedor ctaProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
                        ctaProv.setBolTransaccionalidad(false);
                        //Asignamos valores
                        ctaProv.getCta_prov().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
                        ctaProv.getCta_prov().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        ctaProv.getCta_prov().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        ctaProv.getCta_prov().setFieldInt("CXP_ID", this.document.getFieldInt("CXP_ID"));
                        ctaProv.getCta_prov().setFieldInt("NCA_ID", this.document.getFieldInt("NCA_ID"));
                        ctaProv.getCta_prov().setFieldInt("MP_ESPAGO", 0);
                        ctaProv.getCta_prov().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                        ctaProv.getCta_prov().setFieldInt("MP_TASAPESO", 1);
                        ctaProv.getCta_prov().setFieldString("MP_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                        ctaProv.getCta_prov().setFieldString("MP_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                        ctaProv.getCta_prov().setFieldDouble("MP_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL"));
                        //Validamos el tipo de comprobante para la parte de honorarios
                        if (intPV_TIPOPERS == 1 && intEMP_TIPOPERS == 2) {
                           ctaProv.getCta_prov().setFieldDouble("MP_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                        }
                        ctaProv.getCta_prov().setFieldDouble("MP_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                        ctaProv.getCta_prov().setFieldDouble("MP_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                        ctaProv.getCta_prov().setFieldDouble("MP_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                        ctaProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                        ctaProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                        ctaProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                        ctaProv.getCta_prov().setFieldInt("MP_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                        ctaProv.Init();
                        ctaProv.doTrx();

                        //Fallo algo al aplicar el cargo
                        if (!ctaProv.getStrResultLast().equals("OK")) {
                           this.strResultLast = ctaProv.getStrResultLast();
                        }
                        // </editor-fold>
                     }
                     // <editor-fold defaultstate="collapsed" desc="Firma Electronica, Contabilidad">
                     if (this.strResultLast.equals("OK")) {

                        // <editor-fold defaultstate="collapsed" desc="Firma electronica">
                        if (bolFirmaSAT) {
                           if (SAT == null) {
                              //CFDI
                              //Instanciamos objeto para generar el XML que pide el SAT
                              this.SAT = new SATXml3_0(intId, this.strPATHXml,
                                 "", "", strNoSerieCert,
                                 this.strPATHKeys + strNomKey, strPassKey, this.varSesiones,
                                 oConn);
                              SATXml3_0 sat3 = (SATXml3_0) this.SAT;
                              sat3.setStrPathConfigPAC(this.strPATHKeys);
                              sat3.setBolEsNotaCargoProveedor(true);
                              sat3.setBolUsoFormatoJasper(true);
                              sat3.setStrPATHBase(strPATHBase);
                              sat3.setBolSendMailMasivo(false);
                           } else {
                              this.SAT.setIntTransaccion(intId);
                              this.SAT.setStrPath(this.strPATHXml);
                              this.SAT.setNoAprobacion("");
                              this.SAT.setFechaAprobacion("");
                              this.SAT.setNoSerieCert(strNoSerieCert);
                              this.SAT.setStrPathKey(this.strPATHKeys + strNomKey);
                              this.SAT.setStrPassKey(strPassKey);
                              this.SAT.setVarSesiones(varSesiones);
                              this.SAT.setoConn(oConn);
                           }
                           //Validamos que el path del certificado no sea vacio
                           if (!strNomCert.isEmpty()) {
                              SAT.setStrPathCert(this.strPATHKeys + strNomCert);
                           }
                           SAT.setBolEsLocal(bolEsLocal);
                           SAT.setStrPATHFonts(strPATHFonts);
                           SAT.setBolSendMailMasivo(bolSendMailMasivo);
                           SAT.setIntEMP_TIPOCOMP(0);

                           if (this.bolUsoLugarExpEmp) {
                              SAT.setBolUsoLugarExpEmp(bolUsoLugarExpEmp);
                           }
                           if (this.bolUsaTrxComoFolio) {
                              SAT.setBolUsaTrxComoFolio(bolUsaTrxComoFolio);
                           }
                           //Atrapamos cualquier error con el timbrado de la factura para evitar la perdida
                           try {

                              String strRes = SAT.GeneraXml();
                              if (!strRes.equals("OK")) {
                                 this.strResultLast = strRes;
                              }
                           } catch (Exception ex) {
                              this.strResultLast = "ERROR:" + ex.getMessage();
                           }

                        }
                        // </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc="Vemos si paso las validaciones para guardar la contabilidad">
                        if (this.strResultLast.equals("OK")) {
                           // <editor-fold defaultstate="collapsed" desc="Solo procede si la empresa esta configurada">
                           if (intEMP_USECONTA == 1) {
                              log.debug("Generamos la contabilidad.....");
                              //Objeto para calculo de poliza contable
                              ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                              //Calcula la poliza para facturas
                              contaUtil.CalculaPolizaContableCXPNotaCargo(this.document.getFieldInt("EMP_ID"),
                                 strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCXPGlobal, strCXPProvGlobal, strCXPIVAGlobal,
                                 this.document.getFieldInt("NCA_MONEDA"), document, this.document.getFieldInt("TI_ID"), intId,
                                 "NEW");

                           }
                           // </editor-fold>
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Bitacora">
                        this.saveBitacora("NCARGO_PROV", "NUEVA", intId);
                        // </editor-fold>
                     }
                     // </editor-fold>

                  }
                  // </editor-fold>
               }

               // <editor-fold defaultstate="collapsed" desc="Termina la transaccion">
               if (this.bolTransaccionalidad) {
                  if (this.strResultLast.equals("OK")) {
                     this.oConn.runQueryLMD("COMMIT");
                  } else {
                     this.oConn.runQueryLMD("ROLLBACK");
                     // <editor-fold defaultstate="collapsed" desc="Proceso de recuperacion de folio">
                     //Determinamos el tipo de ticket
                     int intTipoFolio = Folios.NOTA_CARGO_PROV;
                     String strNomFolioC = "_FOLIO_C";
                     Folios folio = new Folios();
                     //Agregamos la serie si corresponde
                     if (this.document.getFieldString(this.strPrefijoMaster + "_SERIE") != null) {
                        folio.setStrSerie(this.document.getFieldString(this.strPrefijoMaster + "_SERIE"));
                     }
                     folio.recoveryFolioLost(oConn, intTipoFolio,
                        this.document.getFieldString(this.strPrefijoMaster + strNomFolioC),
                        bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                     //Solo si esta configurado recuperar el id
                     if (bolRecoveryIdLost) {
                        //Recuperamos el id usado
                        recoveryIdLost(oConn,
                           intId,
                           this.document.getFieldInt("EMP_ID"));
                     }
                     // </editor-fold>
                  }
               }
               // </editor-fold>  
            }
            // </editor-fold>

         } else {
            this.strResultLast = "ERROR:El cliente seleccionado no existe";
         }
         // </editor-fold>
      }
   }

   @Override
   public void doTrxAnul() {
      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("NCA_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("NCA_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulada">
         if (this.document.getFieldInt("NCA_ANULADA") == 0) {

            // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cancelacion de cargos">
            String strSql = "SELECT MP_ID from vta_mov_prov "
               + "where NCA_ID = " + this.document.getFieldInt("NCA_ID")
               + " AND MP_ESPAGO = 0 AND MP_ANULADO = 0 ";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  //Quitamos la aplicacion de los cargos
                  MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
                  //Definimos el id del inventario
                  movProv.getCta_prov().setFieldInt("MP_ID", rs.getInt("MP_ID"));
                  //Inicializamos
                  movProv.Init();
                  //Definimos valores
                  movProv.setBolTransaccionalidad(false);
                  movProv.doTrxAnul();
                  if (!movProv.getStrResultLast().equals("OK")) {
                     this.strResultLast = movProv.getStrResultLast();
                     break;
                  }
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cancelacion de pagos">
            if (this.strResultLast.equals("OK")) {
               //Cancelar pagos
               strSql = "SELECT MP_ID from vta_mov_prov "
                  + "where NCA_ID = " + this.document.getFieldInt("NCA_ID")
                  + " AND MP_ESPAGO = 0 AND MP_ANULADO = 0 ";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     //Quitamos la aplicacion de los cargos
                     MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
                     //Definimos el id del inventario
                     movProv.getCta_prov().setFieldInt("MP_ID", rs.getInt("MP_ID"));
                     //Inicializamos
                     movProv.Init();
                     //Definimos valores
                     movProv.setBolTransaccionalidad(false);
                     movProv.doTrxAnul();
                     if (!movProv.getStrResultLast().equals("OK")) {
                        this.strResultLast = movProv.getStrResultLast();
                        break;
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }

            }
            // </editor-fold>

            if (this.strResultLast.equals("OK")) {

               // <editor-fold defaultstate="collapsed" desc="Guardamos el documento principal">
               this.document.setFieldInt("NCA_US_ANUL", this.varSesiones.getIntNoUser());
               this.document.setFieldInt("NCA_ANULADA", 1);
               this.document.setFieldString("NCA_HORANUL", this.fecha.getHoraActual());
               if (strFechaAnul.equals("")) {
                  this.document.setFieldString("NCA_FECHAANUL", this.fecha.getFechaActual());
               } else {
                  this.document.setFieldString("NCA_FECHAANUL", strFechaAnul);
               }
               String strResp1 = this.document.Modifica(oConn);
               // </editor-fold>

               if (!strResp1.equals("OK")) {
                  this.strResultLast = strResp1;
               } else {
                  //Otros movimientos
                  // <editor-fold defaultstate="collapsed" desc="Obtenemos banderas de la empresa">
                  int intEMP_USECONTA = 0;
                  String strCtaCXPGlobal = "";
                  String strCtaCXPIVAGlobal = "";
                  String strCtaCXPIVATasa = "";
                  String strCtaCXPProvGlobal = "";
                  String strCtaCXP = "";
                  String strCtaCXPProv = "";
                  String strEMP_PASSCP = "";
                  String strEMP_USERCP = "";
                  String strEMP_URLCP = "";
                  int intEMP_CFD_CFDI = 0;
                  //Consultamos la info de la empresa
                  String strSql2 = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,EMP_VTA_DETA,EMP_URLCP,"
                     + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTAPROV,EMP_PASSCP,EMP_USERCP,EMP_AVISOCANCEL,EMP_CFD_CFDI "
                     + " FROM vta_empresas "
                     + " WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                  try {
                     ResultSet rs = oConn.runQuery(strSql2, true);
                     while (rs.next()) {
                        intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                        strCtaCXPGlobal = rs.getString("EMP_CTAVTA");
                        strCtaCXPIVAGlobal = rs.getString("EMP_CTAIVA");
                        strCtaCXPProvGlobal = rs.getString("EMP_CTAPROV");
                        strEMP_PASSCP = rs.getString("EMP_PASSCP");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        strEMP_URLCP = rs.getString("EMP_URLCP");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                     log.error(ex.getMessage());
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Enviamos aviso de cancelacion al SAT si es CDFI">
                  if (intEMP_CFD_CFDI == 1) {
                     String strResp = CancelaComprobante();
                     if (!strResp.equals("OK")) {
                        this.strResultLast = strResp;
                     }
                  }
                  // </editor-fold>
                  if (this.strResultLast.equals("OK")) {

                     //<editor-fold defaultstate="collapsed" desc="Contabilidad">
                     if (intEMP_USECONTA == 1) {
                        //Objeto para calculo de poliza contable
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        contaUtil.CalculaPolizaContableCXPNotaCargo(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
                           strCtaCXPGlobal, strCtaCXPProvGlobal, strCtaCXPIVAGlobal,
                           this.document.getFieldInt("NCA_MONEDA"), document,
                           this.document.getFieldInt("TI_ID"), this.document.getFieldInt("NCA_ID"), "CANCEL");
                     }
                     // </editor-fold>

                  }

               }

               // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
               this.saveBitacora("NCARGO_PROV", "ANULAR", this.document.getFieldInt("NCA_ID"));
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
               if (this.bolTransaccionalidad) {
                  if (this.strResultLast.equals("OK")) {
                     this.oConn.runQueryLMD("COMMIT");
                  } else {
                     this.oConn.runQueryLMD("ROLLBACK");
                  }
               }
               // </editor-fold>
            }
         } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
         }
         // </editor-fold>
      }
   }

   @Override
   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   /**
    * Este metodo borra el folio indicado y regresa el autoincremental al valor
    * anterior
    *
    * @param oConn Es la conexion
    * @param intIdDoc Es el id del documento
    * @param intIdEmp Es el id de la sucursal
    * @return Regresa una cadena con el folio nuevo
    */
   public String recoveryIdLost(Conexion oConn, int intIdDoc, int intIdEmp) {
      String strRes = "OK";
      String strNomTablaFolios = "vta_notas_cargosprov";
      try {
         intIdDoc--;
         //Cambiamos el autoincremental al folio anterior
         String strInit = "alter table " + strNomTablaFolios + " auto_increment = " + intIdDoc;
         oConn.runQueryLMD(strInit);
      } catch (NumberFormatException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), oConn.getStrUsuario(), "recoveryId", oConn);
      }
      return strRes;
   }

   /**
    * Cancela el comprobante CFDI
    *
    * @return
    */
   protected String CancelaComprobante() {

      // <editor-fold defaultstate="collapsed" desc="Obtenemos datos del sello">
      String strNomKey = null;
      String strNomCert = null;
      String strPassKey = null;
      if (this.bolEsLocal) {
         // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(LOCAL)">
         String strSql = "SELECT "
            + "EMP_NOMKEY,EMP_NOMCERT,"/**
             * ,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE
             */
            + "EMP_PASSKEY AS unencrypted "
            + "FROM vta_empresas "
            + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strNomKey = rs.getString("EMP_NOMKEY");
               strNomCert = rs.getString("EMP_NOMCERT");
               strPassKey = rs.getString("unencrypted");
               //La encripcion se hace desde el programa
               Opalina opa = new Opalina();
               try {
                  strPassKey = opa.DesEncripta(strPassKey, strMyPassSecret);
               } catch (NoSuchAlgorithmException ex) {
                  log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
               } catch (NoSuchPaddingException ex) {
                  log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
               } catch (InvalidKeyException ex) {
                  log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
               } catch (IllegalBlockSizeException ex) {
                  log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
               } catch (BadPaddingException ex) {
                  log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
               }
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
         // </editor-fold>
      } else {
         // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
         String strSql = "SELECT "
            + "EMP_NOMKEY,EMP_NOMCERT,"
            + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted "
            + "FROM vta_empresas "
            + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strNomKey = rs.getString("EMP_NOMKEY");
               strNomCert = rs.getString("EMP_NOMCERT");
               strPassKey = rs.getString("unencrypted");
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
         // </editor-fold>
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Preparamos y ejecutamos objeto de cancelacion">
      log.debug("Certificado..." + this.strPATHKeys + System.getProperty("file.separator") + strNomCert);
      log.debug("Key..." + this.strPATHKeys + System.getProperty("file.separator") + strNomKey);
      byte[] certificadoEmisor = PreparaCertificado(this.strPATHKeys + System.getProperty("file.separator") + strNomCert);
      //byte[] llavePrivadaEmisor = PreparaLlave(this.strPATHKeys + System.getProperty("file.separator") + strNomKey, strPassKey);
      byte[] llavePrivadaEmisor = getPrivateKey(this.strPATHKeys + System.getProperty("file.separator") + strNomKey);
      StringBuilder strNomFile = new StringBuilder("");
      //Instanciamos objeto que cancela
      SatCancelaCFDI cancela = new SatCancelaCFDI(this.strPATHKeys);
      cancela.setStrTablaDoc("vta_notas_cargosprov");
      cancela.setStrPrefijoDoc("NCA_");

      //Version 2.0
      strNomFile = new StringBuilder("");
      ERP_MapeoFormato mapeoXml = null;
      mapeoXml = new ERP_MapeoFormato(9);
      Ticket ticket = new Ticket(oConn, varSesiones);
      strNomFile.append(ticket.getNombreFileXml(mapeoXml, this.document.getFieldInt(this.strPrefijoMaster + "_ID"),
         this.document.getFieldString(this.strPrefijoMaster + "_RAZONSOCIAL"),
         this.document.getFieldString(this.strPrefijoMaster + "_FECHA"),
         this.document.getFieldString(this.strPrefijoMaster + "_FOLIO")));

      cancela.setIntIdDoc(this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
      cancela.setStrPathXml(this.strPATHXml);
      cancela.setoConn(oConn);
      cancela.setLlavePrivadaEmisor(llavePrivadaEmisor);
      cancela.setCertificadoEmisor(certificadoEmisor);
      cancela.setStrPasswordEmisor(strPassKey);
      log.debug("strNomFile:" + strNomFile.toString());
      String strResp = cancela.timbra_Factura(strNomFile.toString());
      // </editor-fold>

      return strResp;

   }

   // <editor-fold defaultstate="collapsed" desc="metodos de timbrado">
   //Prepara la llave a usar para cancelar el comprobante
   protected byte[] PreparaCertificado(String strPathCert) {
      byte[] certificado = null;
      UtilCert cert = new UtilCert();
      cert.OpenCert(strPathCert);
      if (!cert.getStrResult().startsWith("ERROR")) {
         try {
            certificado = cert.getCert().getEncoded();
         } catch (CertificateEncodingException ex) {
            log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
      }
      return certificado;
   }

   /**
    * Obtiene la llave privada
    *
    * @param strPath Es el path del archivo
    * @return Regresa la llave en binario
    */
   public byte[] getPrivateKey(String strPath) {
      RandomAccessFile f = null;
      byte[] b = null;
      try {
         f = new RandomAccessFile(strPath, "r");
         b = new byte[(int) f.length()];
         f.read(b);
      } catch (FileNotFoundException ex) {
         java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         java.util.logging.Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
      }

      return b;
   }

   /**
    * Obtiene el password para encriptar
    *
    * @param context Es el contexto del servlet
    */
   public void initMyPass(ServletContext context) {
      try {
         String strPassB64 = context.getInitParameter("SecretWord");
         Opalina opa = new Opalina();
         strMyPassSecret = opa.DesEncripta(strPassB64, "dWAM1YhbGAeu7CTULai4eQ==");
      } catch (NoSuchAlgorithmException ex) {
         log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      } catch (NoSuchPaddingException ex) {
         log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      } catch (InvalidKeyException ex) {
         log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      } catch (IllegalBlockSizeException ex) {
         log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      } catch (BadPaddingException ex) {
         log.error(ex.toString() + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
   }
   // </editor-fold>
   // </editor-fold>

}
