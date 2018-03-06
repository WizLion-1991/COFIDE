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
import ERP.ContabilidadUtil;
import ERP.ERP_MapeoFormato;
import ERP.Folios;
import ERP.ProcesoInterfaz;
import ERP.Ticket;
import ERP.movCliente;
import Tablas.VtaNotasCargos;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Mail;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Representa las reglas de negocio de las notas de cargo
 *
 * @author ZeusSIWEB
 */
public class NotasCargo extends Ticket implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(NotasCargo.class.getName());

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public NotasCargo(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new VtaNotasCargos();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strPrefijoMaster = "NCA";
      this.strPrefijoDeta = "NCAD";
      this.strNomTablaMaster = "vta_notas_cargos";
      this.strNomTablaDeta = "vta_notas_cargosdeta";
      this.strFechaAnul = "";
      this.strPATHBase = "";
      this.strPATHXml = "";
      this.strPATHKeys = "";
      this.strMyPassSecret = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      this.bolAfectoCargosAbonos = true;
   }

   public NotasCargo(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new VtaNotasCargos();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strPrefijoMaster = "NCA";
      this.strPrefijoDeta = "NCAD";
      this.strNomTablaMaster = "vta_notas_cargos";
      this.strNomTablaDeta = "vta_notas_cargosdeta";
      this.strFechaAnul = "";
      this.strPATHBase = "";
      this.strPATHXml = "";
      this.strPATHKeys = "";
      this.strMyPassSecret = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      this.bolAfectoCargosAbonos = true;
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
      int intCT_TIPOPERS = 0;
      int intEMP_TIPOPERS = 0;
      //Valores para contabilidad
      int intEMP_USECONTA = 0;
      int intEMP_VTA_DETA = 0;
      int intEMP_CFD_CFDI = 0;
      String strCtaVtasGlobal = "";
      String strCtaVtasIVAGlobal = "";
      String strCtaVtasIVATasa = "";
      String strCtaVtasCteGlobal = "";
      String strCtaVtas = "";
      String strCtaVtasCte = "";
      String strEMP_URLCP = "";
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      boolean bolFirmaSAT = false;
      boolean bolUsoSello = false;
      String strNoAprob = "";
      String strFechaAprob = "";
      String strSerie = "";
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
      if (this.document.getFieldInt("CT_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el cliente";
      }
      if (this.document.getFieldString("FAC_FORMADEPAGO").isEmpty()) {
         this.strResultLast = "ERROR:Falta definir la forma de pago";
      }
      if (this.document.getFieldString("FAC_METODODEPAGO").isEmpty()) {
         this.strResultLast = "ERROR:Falta definir el metodo de pago";
      }
      if (this.lstMovs.isEmpty()) {
         this.strResultLast = "ERROR:Debe tener por lo menos un item la venta";
      }
      //Asignamos empresa...
      if (this.document.getFieldInt("EMP_ID") == 0) {
         this.document.setFieldInt("EMP_ID", this.intEMP_ID);
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      //Continuamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {

         // <editor-fold defaultstate="collapsed" desc="Copiar datos del cliente">
         boolean bolFindCte = false;
         String strSql = "SELECT CT_RAZONSOCIAL,CT_RFC,CT_CALLE,CT_COLONIA,CT_LOCALIDAD,"
            + " CT_MUNICIPIO,CT_ESTADO,CT_NUMERO,CT_NUMINT,CT_CONTACTE,CT_CONTAVTA,"
            + " CT_CP,CT_DIASCREDITO,CT_LPRECIOS,CT_TIPOPERS FROM vta_cliente "
            + " where CT_ID=" + this.document.getFieldInt("CT_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               this.document.setFieldString(this.strPrefijoMaster + "_RAZONSOCIAL", rs.getString("CT_RAZONSOCIAL"));
               this.document.setFieldString(this.strPrefijoMaster + "_RFC", rs.getString("CT_RFC"));
               this.document.setFieldString(this.strPrefijoMaster + "_CALLE", rs.getString("CT_CALLE"));
               this.document.setFieldString(this.strPrefijoMaster + "_COLONIA", rs.getString("CT_COLONIA"));
               this.document.setFieldString(this.strPrefijoMaster + "_LOCALIDAD", rs.getString("CT_LOCALIDAD"));
               this.document.setFieldString(this.strPrefijoMaster + "_MUNICIPIO", rs.getString("CT_MUNICIPIO"));
               this.document.setFieldString(this.strPrefijoMaster + "_ESTADO", rs.getString("CT_ESTADO"));
               this.document.setFieldString(this.strPrefijoMaster + "_NUMERO", rs.getString("CT_NUMERO"));
               this.document.setFieldString(this.strPrefijoMaster + "_NUMINT", rs.getString("CT_NUMINT"));
               this.document.setFieldString(this.strPrefijoMaster + "_CP", rs.getString("CT_CP"));
               if (this.document.getFieldInt(this.strPrefijoMaster + "_DIASCREDITO") == 0) {
                  this.document.setFieldInt(this.strPrefijoMaster + "_DIASCREDITO", rs.getInt("CT_DIASCREDITO"));
               }
               if (this.document.getFieldInt(this.strPrefijoMaster + "_LPRECIOS") == 0) {
                  this.document.setFieldInt(this.strPrefijoMaster + "_LPRECIOS", rs.getInt("CT_LPRECIOS"));
               }
               strCtaVtas = rs.getString("CT_CONTAVTA");
               strCtaVtasCte = rs.getString("CT_CONTACTE");
               intCT_TIPOPERS = rs.getInt("CT_TIPOPERS");
               bolFindCte = true;
            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
            //Evaluamos si es un cliente final para copiar sus datos
            if (this.document.getFieldInt("DFA_ID") != 0) {
               strSql = "SELECT DFA_RAZONSOCIAL,DFA_RFC,DFA_CALLE,DFA_COLONIA,DFA_LOCALIDAD,"
                  + " DFA_MUNICIPIO,DFA_ESTADO,DFA_NUMERO,DFA_NUMINT,"
                  + " DFA_CP FROM vta_cliente_facturacion "
                  + " where DFA_ID=" + this.document.getFieldInt("DFA_ID") + "";
               rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  this.document.setFieldString(this.strPrefijoMaster + "_RAZONSOCIAL", rs.getString("DFA_RAZONSOCIAL"));
                  this.document.setFieldString(this.strPrefijoMaster + "_RFC", rs.getString("DFA_RFC"));
                  this.document.setFieldString(this.strPrefijoMaster + "_CALLE", rs.getString("DFA_CALLE"));
                  this.document.setFieldString(this.strPrefijoMaster + "_COLONIA", rs.getString("DFA_COLONIA"));
                  this.document.setFieldString(this.strPrefijoMaster + "_LOCALIDAD", rs.getString("DFA_LOCALIDAD"));
                  this.document.setFieldString(this.strPrefijoMaster + "_MUNICIPIO", rs.getString("DFA_MUNICIPIO"));
                  this.document.setFieldString(this.strPrefijoMaster + "_ESTADO", rs.getString("DFA_ESTADO"));
                  this.document.setFieldString(this.strPrefijoMaster + "_NUMERO", rs.getString("DFA_NUMERO"));
                  this.document.setFieldString(this.strPrefijoMaster + "_NUMINT", rs.getString("DFA_NUMINT"));
                  this.document.setFieldString(this.strPrefijoMaster + "_CP", rs.getString("DFA_CP"));
               }
               if (rs.getStatement() != null) {
                  //rs.getStatement().close();
               }
               rs.close();
            }
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si el cliente se encontro">
         if (bolFindCte) {

            // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara el ticket">
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

            // <editor-fold defaultstate="collapsed" desc="Validamos regimen fiscal">
            int intCuantos = 0;
            String strSqlRegFis = "select count(REGF_DESCRIPCION) as cuantos from vta_empregfiscal,vta_regimenfiscal "
               + " where vta_empregfiscal.REGF_ID = vta_regimenfiscal.REGF_ID "
               + " AND vta_empregfiscal.EMP_ID = " + this.document.getFieldInt("EMP_ID");
            try {
               ResultSet rs = oConn.runQuery(strSqlRegFis, true);
               while (rs.next()) {
                  intCuantos = rs.getInt("cuantos");
               }
               if (rs.getStatement() != null) {
                  //rs.getStatement().close();
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            //Version 2.2
            if (intCuantos == 0) {
               this.strResultLast = "ERROR:FALTA ASIGNAR UN REGIMEN FISCAL A LA EMPRESA";
            }
            // </editor-fold>

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="En caso de factura obtenemos los parametros adicionales para la firma electronica">
            int intEMP_AVISOFOLIO = 0;
            boolean bolTmp = oConn.isBolMostrarQuerys();
            oConn.setBolMostrarQuerys(false);
            // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
            strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
               + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,"
               + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,EMP_CFD_CFDI,"
               + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_URLCP,EMP_USACODBARR,EMP_VTA_DETA "
               + "FROM vta_empresas "
               + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                  this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
                  if (rs.getInt("EMP_USACODBARR") == 1) {
                     bolUsoSello = true;
                  }
                  if (rs.getInt("EMP_FIRMA") == 1) {
                     bolFirmaSAT = true;
                     strNoAprob = rs.getString("EMP_NOAPROB");
                     strFechaAprob = rs.getString("EMP_FECHAPROB");
                     strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                     strNomKey = rs.getString("EMP_NOMKEY");
                     strNomCert = rs.getString("EMP_NOMCERT");
                     strPassKey = rs.getString("unencrypted");
                     intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                     intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                     strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                     strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                     strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                     strEMP_PASSCP = rs.getString("EMP_PASSCP");
                     strEMP_USERCP = rs.getString("EMP_USERCP");
                     strEMP_URLCP = rs.getString("EMP_URLCP");
                     intEMP_AVISOFOLIO = rs.getInt("EMP_AVISOFOLIO");
                     intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                  }
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

            // <editor-fold defaultstate="collapsed" desc="Asignamos el tipo de comprobante">
            this.document.setFieldInt(this.strPrefijoMaster + "_TIPOCOMP", this.intEMP_TIPOCOMP);
            if (this.intFAC_TIPOCOMP != 0) {
               this.document.setFieldInt(this.strPrefijoMaster + "_TIPOCOMP", this.intFAC_TIPOCOMP);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calcular el importe del descuento total">
            double dblDescuentoTotal = 0;
            Iterator<TableMaster> itG = this.lstMovs.iterator();
            while (itG.hasNext()) {
               TableMaster deta = itG.next();
               dblDescuentoTotal += deta.getFieldDouble(this.strPrefijoDeta + "_DESCUENTO");
            }
            this.document.setFieldDouble(this.strPrefijoMaster + "_DESCUENTO", dblDescuentoTotal);
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
                  int intTipoFolio = Folios.NOTA_CARGO;
                  String strFolio = "";
                  folio.setBolEsLocal(this.bolEsLocal);
                  if (this.document.getFieldString(this.strPrefijoMaster + "_FOLIO").equals("")) {
                     if (this.document.getFieldString(this.strPrefijoMaster + "_SERIE") != null) {
                        folio.setStrSerie(this.document.getFieldString(this.strPrefijoMaster + "_SERIE"));
                     }
                     strFolio = folio.doFolio(oConn, intTipoFolio, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                     this.document.setFieldString(this.strPrefijoMaster + "_FOLIO", strFolio);
                     if (intEMP_CFD_CFDI == 1) {
                        this.document.setFieldString(this.strPrefijoMaster + "_FOLIO_C", strFolio);
                     }
                  } else {
                     strFolio = this.document.getFieldString(this.strPrefijoMaster + "_FOLIO");
                     if (intEMP_CFD_CFDI == 1) {
                        this.document.setFieldString(this.strPrefijoMaster + "_FOLIO_C", strFolio);
                     }
                     /*folio.updateFolio(oConn, intTipoFolio, this.document.getFieldString(this.strPrefijoMaster + "_FOLIO"),
                         this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));*/
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
                     if (this.strResultLast.equals("OK")) {
                        if (bolAfectoCargosAbonos) {
                           // <editor-fold defaultstate="collapsed" desc="Cargo clientes">
                           //Crear objeto de cta cliente y aplicarlo
                           //Generamos cargo por el importe de la deuda
                           movCliente cta_clie = new movCliente(this.oConn, this.varSesiones, this.request);
                           cta_clie.setBolTransaccionalidad(false);
                           //Asignamos valores
                           cta_clie.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                           cta_clie.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                           cta_clie.getCta_clie().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                           cta_clie.getCta_clie().setFieldInt("MC_ESPAGO", 0);
                           cta_clie.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                           cta_clie.getCta_clie().setFieldInt("MC_TASAPESO", 1);
                           cta_clie.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                           cta_clie.getCta_clie().setFieldString("MC_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                           cta_clie.getCta_clie().setFieldDouble("MC_CARGO", this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL"));
                           //Validamos el tipo de comprobante para la parte de honorarios
                           if (this.bolEsLocal) {
                              if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 0) {
                                 cta_clie.getCta_clie().setFieldDouble("MC_CARGO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                              }
                           } else if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 2) {
                              cta_clie.getCta_clie().setFieldDouble("MC_CARGO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                           }
                           cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                           cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                           cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                           cta_clie.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                           cta_clie.Init();
                           cta_clie.doTrx();

                           //Fallo algo al aplicar el cargo
                           if (!cta_clie.getStrResultLast().equals("OK")) {
                              this.strResultLast = cta_clie.getStrResultLast();
                           }
                           // </editor-fold>
                        }
                     }
                     // <editor-fold defaultstate="collapsed" desc="Firma Electronica, Contabilidad">
                     if (this.strResultLast.equals("OK")) {

                        // <editor-fold defaultstate="collapsed" desc="Firma electronica">
                        if (bolFirmaSAT) {
                           if (SAT == null) {
                              //Validamos si se usa un CFD o CFDI
                              if (intEMP_CFD_CFDI == 0) {
                                 //CFD
                                 //Instanciamos objeto para generar el XML que pide el SAT
                                 this.SAT = new SATXml(intId, this.strPATHXml,
                                    strNoAprob, strFechaAprob, strNoSerieCert,
                                    this.strPATHKeys + strNomKey, strPassKey, this.varSesiones,
                                    oConn);
                              } else {
                                 //CFDI
                                 //Instanciamos objeto para generar el XML que pide el SAT
                                 this.SAT = new SATXml3_0(intId, this.strPATHXml,
                                    strNoAprob, strFechaAprob, strNoSerieCert,
                                    this.strPATHKeys + strNomKey, strPassKey, this.varSesiones,
                                    oConn);
                                 SATXml3_0 sat3 = (SATXml3_0) this.SAT;
                                 sat3.setStrPathConfigPAC(this.strPATHKeys);
                                 sat3.setBlEmail1(blEmail1);
                                 sat3.setBlEmail2(blEmail2);
                                 sat3.setBlEmail3(blEmail3);
                                 sat3.setBlEmail4(blEmail4);
                                 sat3.setBlEmail5(blEmail5);
                                 sat3.setBlEmail6(blEmail6);
                                 sat3.setBlEmail7(blEmail7);
                                 sat3.setBlEmail8(blEmail8);
                                 sat3.setBlEmail9(blEmail9);
                                 sat3.setBlEmail10(blEmail10);
                                 sat3.setBolEsNotaCargo(true);
                                 sat3.setBolUsoFormatoJasper(true);
                                 sat3.setStrPATHBase(strPATHBase);
                              }
                           } else {
                              this.SAT.setIntTransaccion(intId);
                              this.SAT.setStrPath(this.strPATHXml);
                              this.SAT.setNoAprobacion(strNoAprob);
                              this.SAT.setFechaAprobacion(strFechaAprob);
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
                           SAT.setIntEMP_TIPOCOMP(this.intFAC_TIPOCOMP);

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
                              contaUtil.CalculaPolizaContableNotasCargo(this.document.getFieldInt("EMP_ID"),
                                 strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
                                 strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
                                 this.document.getFieldInt("NCA_MONEDA"), document, this.document.getFieldInt("TI_ID"), intId,
                                 strNomTablaMaster, strPrefijoMaster, "NEW");
                           }
                           // </editor-fold>
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Formato de impresion codigo de barras">
                        if (bolSendMailMasivo) {
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Bitacora">
                        this.saveBitacora(this.strTipoVta, "NUEVA", intId);
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
                     int intTipoFolio = Folios.NOTA_CARGO;
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
      if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // <editor-fold defaultstate="collapsed" desc="Validamos que no tenga pagos masivos aplicados">

      // </editor-fold>
      // </editor-fold>
      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {

         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulada">
         if (this.document.getFieldInt(this.strPrefijoMaster + "_ANULADA") == 0) {

            // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cancelacion de cargos">
            String strSql = "SELECT MC_ID from vta_mov_cte "
               + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
               + " AND MC_ESPAGO = 0 AND MC_ANULADO = 0 ";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  //Quitamos la aplicacion de los cargos
                  movCliente cta_cte = new movCliente(this.oConn, this.varSesiones, this.request);
                  //Definimos el id del inventario
                  cta_cte.getCta_clie().setFieldInt("MC_ID", rs.getInt("MC_ID"));
                  //Inicializamos
                  cta_cte.Init();
                  //Definimos valores
                  cta_cte.setBolTransaccionalidad(false);
                  cta_cte.doTrxAnul();
                  if (!cta_cte.getStrResultLast().equals("OK")) {
                     this.strResultLast = cta_cte.getStrResultLast();
                     break;
                  }
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

            // <editor-fold defaultstate="collapsed" desc="Cancelacion de pagos">
            if (this.strResultLast.equals("OK")) {
               //Cancelar pagos
               strSql = "SELECT MC_ID from vta_mov_cte "
                  + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                  + " AND MC_ESPAGO = 1 AND MC_ANULADO = 0 ";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     //Quitamos la aplicacion de los cargos
                     movCliente cta_cte = new movCliente(this.oConn, this.varSesiones, this.request);
                     //Definimos el id del inventario
                     cta_cte.getCta_clie().setFieldInt("MC_ID", rs.getInt("MC_ID"));
                     //Inicializamos
                     cta_cte.Init();
                     //Definimos valores
                     cta_cte.setBolTransaccionalidad(false);
                     cta_cte.doTrxAnul();
                     if (!cta_cte.getStrResultLast().equals("OK")) {
                        this.strResultLast = cta_cte.getStrResultLast();
                        break;
                     }
                  }
                  if (rs.getStatement() != null) {
                     //rs.getStatement().close();
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
               this.document.setFieldInt(this.strPrefijoMaster + "_US_ANUL", this.varSesiones.getIntNoUser());
               this.document.setFieldInt(this.strPrefijoMaster + "_ANULADA", 1);
               this.document.setFieldString(this.strPrefijoMaster + "_HORANUL", this.fecha.getHoraActual());
               if (strFechaAnul.equals("")) {
                  this.document.setFieldString(this.strPrefijoMaster + "_FECHAANUL", this.fecha.getFechaActual());
               } else {
                  this.document.setFieldString(this.strPrefijoMaster + "_FECHAANUL", strFechaAnul);
               }
               String strResp1 = this.document.Modifica(oConn);
               // </editor-fold>

               if (!strResp1.equals("OK")) {
                  this.strResultLast = strResp1;
               } else {

                  // <editor-fold defaultstate="collapsed" desc="Obtenemos banderas de la empresa">
                  int intEMP_USECONTA = 0;
                  int intEMP_AVISOCANCEL = 0;
                  String strCtaVtasGlobal = "";
                  String strCtaVtasIVAGlobal = "";
                  String strCtaVtasIVATasa = "";
                  String strCtaVtasCteGlobal = "";
                  String strEMP_PASSCP = "";
                  String strEMP_USERCP = "";
                  String strEMP_URLCP = "";
                  int intEMP_VTA_DETA = 0;
                  int intEMP_CFD_CFDI = 0;
                  //Consultamos la info de la empresa
                  strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,EMP_VTA_DETA,EMP_URLCP,"
                     + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,EMP_PASSCP,EMP_USERCP,EMP_AVISOCANCEL,EMP_CFD_CFDI "
                     + " FROM vta_empresas "
                     + " WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                        intEMP_AVISOCANCEL = rs.getInt("EMP_AVISOCANCEL");
                        strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                        strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                        strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                        strEMP_PASSCP = rs.getString("EMP_PASSCP");
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
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
                  // <editor-fold defaultstate="collapsed" desc="Enviamos aviso de cancelacion al SAT si es CDFI">
                  if (intEMP_CFD_CFDI == 1 ) {
                     String strResp = CancelaComprobante();
                     if (!strResp.equals("OK")) {
                        this.strResultLast = strResp;
                     }
                  }
                  // </editor-fold>

                  if (this.strResultLast.equals("OK")) {

                     //<editor-fold defaultstate="collapsed" desc="Contabilidad">
                     if (intEMP_USECONTA == 1) {
                        //Instanciamos objetos para calcular la poliz contable
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        //Calcula la poliza para notas de cargo
                        contaUtil.CalculaPolizaContableNotasCargo(this.document.getFieldInt("EMP_ID"),
                           strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
                           strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
                           this.document.getFieldInt("NCA_MONEDA"), document, this.document.getFieldInt("TI_ID"),
                           this.document.getFieldInt("NCA_ID"),
                           strNomTablaMaster, strPrefijoMaster, "CANCEL");
                     }
                     // </editor-fold>

                     // <editor-fold defaultstate="collapsed" desc="Mail de aviso de cancelacion">
                     //Validamos si se envia mail de aviso
                     if (intEMP_AVISOCANCEL == 1) {
                        EnvioMailCancel();
                     }
                     // </editor-fold>   
                  }

                  // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
                  this.saveBitacora(this.strTipoVta, "ANULAR", this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
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
    * Cancela el comprobante CFDI
    *
    * @return
    */
   @Override
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

      cancela.setStrTablaDoc("vta_notas_cargos");
      cancela.setStrPrefijoDoc("NCA_");

      //Version 2.0
      strNomFile = new StringBuilder("");
      ERP_MapeoFormato mapeoXml = null;
      mapeoXml = new ERP_MapeoFormato(8);
      strNomFile.append(getNombreFileXml(mapeoXml, this.document.getFieldInt(this.strPrefijoMaster + "_ID"),
         this.document.getFieldString(this.strPrefijoMaster + "_RAZONSOCIAL"),
         this.document.getFieldString(this.strPrefijoMaster + "_FECHA"),
         this.document.getFieldString(this.strPrefijoMaster + "_FOLIO")));

      cancela.setIntIdDoc(this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
      cancela.setStrPathXml(this.strPATHXml);
      cancela.setoConn(oConn);
      cancela.setLlavePrivadaEmisor(llavePrivadaEmisor);
      cancela.setCertificadoEmisor(certificadoEmisor);
      cancela.setStrPasswordEmisor(strPassKey);
      String strResp = cancela.timbra_Factura(strNomFile.toString());
      // </editor-fold>

      return strResp;

   }

   /**
    * Envia aviso de que se cancelo la operacion al cliente
    */
   @Override
   protected void EnvioMailCancel() {
      //Buscamos mail del cliente
      String strCT_EMAIL1 = "";
      String strCT_EMAIL2 = "";
      String strSql = "SELECT CT_EMAIL1,CT_EMAIL2 FROM vta_cliente "
         + " where CT_ID=" + this.document.getFieldInt("CT_ID") + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCT_EMAIL1 = rs.getString("CT_EMAIL1");
            strCT_EMAIL2 = rs.getString("CT_EMAIL2");
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage());
      }
      //validamos que hallan puesto el mail
      Mail mail = new Mail();
      if (!strCT_EMAIL1.isEmpty() || !strCT_EMAIL2.isEmpty()) {
         String strLstMail = "";
         //Validamos si el mail del cliente es valido
         if (mail.isEmail(strCT_EMAIL1)) {
            strLstMail += "," + strCT_EMAIL1;
         }
         if (mail.isEmail(strCT_EMAIL2)) {
            strLstMail += "," + strCT_EMAIL2;
         }
         //Intentamos mandar el mail
         mail.setBolDepuracion(false);
         mail.getTemplate("CANCEL_NCARGO", oConn);
         mail.getMensaje();
         mail.setReplaceContent(this.getDocument());
         strSql = "SELECT * FROM vta_empresas "
            + " where EMP_ID=" + this.document.getFieldInt("EMP_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql);
            mail.setReplaceContent(rs);
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
         mail.setDestino(strLstMail);
         boolean bol = mail.sendMail();
         if (bol) {
            //strResp = "MAIL ENVIADO.";
         } else {
            //strResp = "FALLO EL ENVIO DEL MAIL.";
         }

      } else {
         //strResp = "ERROR: INGRESE UN MAIL";
      }
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
      String strNomTablaFolios = "vta_notas_cargos";
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
   // </editor-fold>
}
