package ERP;

import Core.FirmasElectronicas.Opalina;
import Core.FirmasElectronicas.SATXml;
import Core.FirmasElectronicas.SATXml3_0;
import Tablas.vta_facturadeta;
import Tablas.vta_movproddeta;
import Tablas.vta_ncredito;
import Tablas.vta_ticketsdeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Representa una nota de credito
 *
 * @author zeus
 */
public class NCredito extends Ticket implements ProcesoInterfaz {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(NCredito.class.getName());
   private double saldoAnticipo;
   private boolean bolTimbrar;

   /**
    * Nos indica si se timbra la nota de credito
    *
    * @return Regresa true si se timbra
    */
   public boolean isBolTimbrar() {
      return bolTimbrar;
   }

   /**
    * Define si se timbra la nota de credito
    *
    * @param bolTimbrar true es el valor default
    */
   public void setBolTimbrar(boolean bolTimbrar) {
      this.bolTimbrar = bolTimbrar;
   }

   /**
    * Construtor del objeto de notas de credito
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    */
   public NCredito(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new vta_ncredito();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strPrefijoMaster = "NC";
      this.strPrefijoDeta = "NCD";
      this.strNomTablaMaster = "vta_ncredito";
      this.strNomTablaDeta = "vta_ncreditodeta";
      this.strFechaAnul = "";
      this.strTipoVta = "NCREDITO";
      this.strPATHXml = "";
      this.strPATHKeys = "";
      this.strMyPassSecret = "";
      this.bolSendMailMasivo = true;
      this.bolTimbrar = true;
   }

   /**
    * Construtor del objeto notas de credito
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion
    */
   public NCredito(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new vta_ncredito();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strPrefijoMaster = "NC";
      this.strPrefijoDeta = "NCD";
      this.strNomTablaMaster = "vta_ncredito";
      this.strNomTablaDeta = "vta_ncreditodeta";
      this.strFechaAnul = "";
      this.strTipoVta = "NCREDITO";
      this.strPATHXml = "";
      this.strPATHKeys = "";
      this.strMyPassSecret = "";
      this.bolSendMailMasivo = true;
      this.bolTimbrar = true;
   }

   @Override
   public void doTrx() {
      saldoAnticipo = 0.0;
      this.strResultLast = "OK";
      double dblMontoAplicaNotaCredito = this.document.getFieldDouble("NC_TOTAL");

      // <editor-fold defaultstate="collapsed" desc="Definimos valores">
      this.document.setFieldString(this.strPrefijoMaster + "_FECHACREATE", this.fecha.getFechaActual());
      this.document.setFieldString(this.strPrefijoMaster + "_HORA", this.fecha.getHoraActualHHMMSS());
      this.document.setFieldDouble(this.strPrefijoMaster + "_SALDO", 0);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
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
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Evaluamos que la nota de credito afecte una factura
      if (this.document.getFieldInt("FAC_ID") == 0 && this.document.getFieldInt("TKT_ID") == 0) {
         this.strResultLast = "ERROR:La nota de credito debe aplicar a una factura/remision";
      }
      //Validamos existencia del documento

      if (this.document.getFieldInt("FAC_ID") != 0) {
         boolean bolExisteFac = false;
         //Evaluamos el saldo y estatus de la factura
         String strSqlC = "select CT_ID,FAC_ANULADA,FAC_FOLIO_C,FAC_SALDO from vta_facturas where  FAC_ID = " + this.document.getFieldInt("FAC_ID");
         try {
            ResultSet rs = oConn.runQuery(strSqlC);
            while (rs.next()) {
               bolExisteFac = true;
               //Evaluamos si esta anulada
               if (rs.getInt("FAC_ANULADA") == 1) {
                  this.strResultLast = "ERROR:La factura con folio " + rs.getString("FAC_FOLIO_C") + " esta anulada";
               } else {
                  //Verificamos que el importe de la nota de credito no sea mayor al saldo de la factura
                  if (this.document.getFieldDouble("NC_TOTAL") > rs.getDouble("FAC_SALDO")) {
                     this.saldoAnticipo = this.document.getFieldDouble("NC_TOTAL") - rs.getDouble("FAC_SALDO");
                     //this.document.setFieldDouble("NC_TOTAL", rs.getDouble("FAC_SALDO"));
                     dblMontoAplicaNotaCredito = rs.getDouble("FAC_SALDO");
                  } else {
                     if (this.document.getFieldInt("CT_ID") != rs.getInt("CT_ID")) {
                        this.strResultLast = "ERROR:La factura con folio " + rs.getString("FAC_FOLIO_C") + " esta asignado a un cliente diferente al seleccionado";
                     }
                  }
               }
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
         if (!bolExisteFac) {
            this.strResultLast = "ERROR:La factura con id " + this.document.getFieldInt("FAC_ID") + " no existe";
         }

      } else {
         //Evaluamos el saldo y estatus de la factura
         boolean bolExisteFac = false;
         String strSqlC = "select CT_ID,TKT_ANULADA,TKT_FOLIO_C,TKT_SALDO from vta_tickets where  TKT_ID = " + this.document.getFieldInt("TKT_ID");
         try {
            ResultSet rs = oConn.runQuery(strSqlC);
            while (rs.next()) {
               bolExisteFac = true;
               //Evaluamos si esta anulada
               if (rs.getInt("TKT_ANULADA") == 1) {
                  this.strResultLast = "ERROR:La factura con folio " + rs.getString("TKT_FOLIO") + " esta anulada";
               } else {
                  //Verificamos que el importe de la nota de credito no sea mayor al saldo de la factura
                  if (this.document.getFieldDouble("NC_TOTAL") > rs.getDouble("TKT_SALDO")) {
                     this.saldoAnticipo = this.document.getFieldDouble("NC_TOTAL") - rs.getDouble("TKT_SALDO");
                     //this.document.setFieldDouble("NC_TOTAL", rs.getDouble("TKT_SALDO"));
                     dblMontoAplicaNotaCredito = rs.getDouble("TKT_SALDO");
                  } else {
                     if (this.document.getFieldInt("CT_ID") != rs.getInt("CT_ID")) {
                        this.strResultLast = "ERROR:La factura con folio " + rs.getString("TKT_FOLIO_C") + " esta asignado a un cliente diferente al seleccionado";
                     }
                  }
               }
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
         if (!bolExisteFac) {
            this.strResultLast = "ERROR:La remision con id " + this.document.getFieldInt("TKT_ID") + " no existe";
         }

      }

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
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
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      String strEMP_URLCP = "";
      String strNomCert = "";
      // </editor-fold>
      //Validamos si pasamos las validaciones
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
               this.document.setFieldInt(this.strPrefijoMaster + "_DIASCREDITO", rs.getInt("CT_DIASCREDITO"));
               this.document.setFieldInt(this.strPrefijoMaster + "_LPRECIOS", rs.getInt("CT_LPRECIOS"));
               strCtaVtas = rs.getString("CT_CONTAVTA");
               strCtaVtasCte = rs.getString("CT_CONTACTE");
               intCT_TIPOPERS = rs.getInt("CT_TIPOPERS");
               bolFindCte = true;
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.fillInStackTrace());
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Validamos si encontramos el cliente">
         if (bolFindCte) {
            // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara la nota de credito">
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
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.fillInStackTrace());
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Si se definio el id de la empresa independiente de la sucursal">
            if (this.intEMP_ID != 0) {
               this.document.setFieldInt("EMP_ID", this.intEMP_ID);
               this.bolFolioGlobal = false;
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="En caso de NCREDITO obtenemos los parametros adicionales para la firma electronica">
            boolean bolFirmaSAT = false;
            boolean bolUsoSello = false;
            String strNoAprob = "";
            String strFechaAprob = "";
            String strSerie = "";
            String strNoSerieCert = "";
            String strNomKey = "";
            String strPassKey = "";
            boolean bolTmp = oConn.isBolMostrarQuerys();
            oConn.setBolMostrarQuerys(false);
            if (this.bolEsLocal) {
               // <editor-fold defaultstate="collapsed" desc="VERSION LOCAL">
               strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMPNC,"
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,"/**
                        * ,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE
                        */
                       + "EMP_PASSKEY AS unencrypted,"
                       + "EMP_FIRMA,EMP_USACODBARR,EMP_CFD_CFDI "
                       + "FROM vta_empresas "
                       + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                     this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMPNC");
                     if (rs.getInt("EMP_USACODBARR") == 1) {
                        bolUsoSello = true;
                     }
                     if (rs.getInt("EMP_FIRMA") == 1) {
                        bolFirmaSAT = true;
                        strNoAprob = rs.getString("EMP_NOAPROB");
                        strFechaAprob = rs.getString("EMP_FECHAPROB");
                        strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                        strNomKey = rs.getString("EMP_NOMKEY");
                        strPassKey = rs.getString("unencrypted");
                        strNomCert = rs.getString("EMP_NOMCERT");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                        /*intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                         strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                         strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                         strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");*/
                        //La encripcion se hace desde el programa
                        Opalina opa = new Opalina();
                        try {
                           strPassKey = opa.DesEncripta(strPassKey, strMyPassSecret);
                        } catch (NoSuchAlgorithmException ex) {
                           Logger.getLogger(NCredito.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchPaddingException ex) {
                           Logger.getLogger(NCredito.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InvalidKeyException ex) {
                           Logger.getLogger(NCredito.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalBlockSizeException ex) {
                           Logger.getLogger(NCredito.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (BadPaddingException ex) {
                           Logger.getLogger(NCredito.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               // </editor-fold>
            } else {
               // <editor-fold defaultstate="collapsed" desc="VERSION WEB">
               strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMPNC,"
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,"
                       + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,"
                       + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_USACODBARR,EMP_VTA_DETA,EMP_CFD_CFDI,EMP_URLCP "
                       + "FROM vta_empresas "
                       + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                     this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMPNC");
                     if (rs.getInt("EMP_USACODBARR") == 1) {
                        bolUsoSello = true;
                     }
                     if (rs.getInt("EMP_FIRMA") == 1) {
                        bolFirmaSAT = true;
                        strNoAprob = rs.getString("EMP_NOAPROB");
                        strFechaAprob = rs.getString("EMP_FECHAPROB");
                        strNoSerieCert = rs.getString("EMP_NOSERIECERT");
                        strNomKey = rs.getString("EMP_NOMKEY");
                        strPassKey = rs.getString("unencrypted");
                        intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                        intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                        strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                        strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                        strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                        strEMP_PASSCP = rs.getString("EMP_PASSCP");
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        strEMP_URLCP = rs.getString("EMP_URLCP");
                        strNomCert = rs.getString("EMP_NOMCERT");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               // </editor-fold>
            }
            // </editor-fold>
            oConn.setBolMostrarQuerys(bolTmp);
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
            // <editor-fold defaultstate="collapsed" desc="Iniciar transaccion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            //guardar master
            this.document.setBolGetAutonumeric(true);
            // <editor-fold defaultstate="collapsed" desc="Validamos si se calcula el folio">
            boolean bolCalcula = true;
            if (bolCalcula) {
               // <editor-fold defaultstate="collapsed" desc="Definimos el folio de la operacion">
               Folios folio = new Folios();
               String strFolio = "";
               folio.setBolEsLocal(this.bolEsLocal);
               if (this.document.getFieldString(this.strPrefijoMaster + "_FOLIO").equals("")) {
                  strFolio = folio.doFolio(oConn, Folios.NCREDITO, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                  if (intEMP_CFD_CFDI == 1) {
                     this.document.setFieldString(this.strPrefijoMaster + "_FOLIO_C", strFolio);
                  } else {
                     this.document.setFieldString(this.strPrefijoMaster + "_FOLIO", strFolio);
                  }
               } else {
                  strFolio = this.document.getFieldString(this.strPrefijoMaster + "_FOLIO");
                  folio.updateFolio(oConn, Folios.NCREDITO, this.document.getFieldString(this.strPrefijoMaster + "_FOLIO"),
                          this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Validamos que sea valido el folio">
               //Validamos que sea valido si no es cfdi
               if (intEMP_CFD_CFDI == 0) {
                  int intFolio = 0;
                  try {
                     intFolio = Integer.valueOf(strFolio);
                     strSql = "SELECT * FROM vta_foliosempresa WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID")
                             + " AND " + intFolio + ">= FE_FOLIOINI  AND " + intFolio + "<=FE_FOLIOFIN AND FE_ESNC = 1 ";
                     try {
                        boolean bolFindFolio = false;
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                           bolFindFolio = true;
                           strNoAprob = rs.getString("FE_NOAPROB");
                           strFechaAprob = rs.getString("FE_FECHA");
                           strSerie = rs.getString("FE_SERIE");
                        }
                        rs.close();
                        //Si encontro folios hacemos la nota de credito
                        if (!bolFindFolio) {
                           strFolio = folio.doFolio(oConn, Folios.FACTURA, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                           this.document.setFieldString(this.strPrefijoMaster + "_FOLIO", strFolio);
                           intFolio = Integer.valueOf(strFolio);
                           //Sino tiene folios vemos si tiene folios de facturas para utilizarlos
                           strSql = "SELECT * FROM vta_foliosempresa WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID")
                                   + " AND " + intFolio + ">= FE_FOLIOINI  AND " + intFolio + "<=FE_FOLIOFIN ";
                           bolFindFolio = false;
                           rs = oConn.runQuery(strSql, true);
                           while (rs.next()) {
                              bolFindFolio = true;
                              strNoAprob = rs.getString("FE_NOAPROB");
                              strFechaAprob = rs.getString("FE_FECHA");
                              strSerie = rs.getString("FE_SERIE");
                           }
                           rs.close();
                           if (!bolFindFolio) {
                              this.strResultLast = "ERROR:NO CUENTA CON FOLIOS PARA HACER SU NOTA DE CREDITO";
                           } else {
                              //Guardamos los datos en la factura
                              this.document.setFieldString("NC_SERIE", strSerie);
                              this.document.setFieldString("NC_NOAPROB", strNoAprob);
                              this.document.setFieldString("NC_FECHAAPROB", strFechaAprob);
                           }
                        } else {
                           //Guardamos los datos en la factura
                           this.document.setFieldString("NC_SERIE", strSerie);
                           this.document.setFieldString("NC_NOAPROB", strNoAprob);
                           this.document.setFieldString("NC_FECHAAPROB", strFechaAprob);
                        }
                     } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        log.error(ex.getMessage());
                     }
                  } catch (NumberFormatException ex) {
                     this.strResultLast = "Error:" + ex.getMessage();
                  }
               }
               // </editor-fold>
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Si todo esta ok guardamos la operacion">
            if (this.strResultLast.equals("OK")) {
               String strRes1 = this.document.Agrega(oConn);
               if (!strRes1.equals("OK")) {
                  this.strResultLast = strRes1;
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Si se guardo de manera exitosa la operacion continuamos">
            if (this.strResultLast.equals("OK")) {
               //Reemplazamos los comodines que definimos
               FormatoDescripNotas();
               // <editor-fold defaultstate="collapsed" desc="guardar detalle">
               int intId = Integer.valueOf(this.document.getValorKey());
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
                  } else {
                     //Guardamos la cantidad por devolver

                     //Cargamos el detalle de facturas que se esta devolviendo
                     if (deta.getFieldInt("FACD_ID") != 0) {
                        vta_facturadeta detaFac = new vta_facturadeta();
                        detaFac.ObtenDatos(deta.getFieldInt("FACD_ID"), oConn);
                        detaFac.setFieldDouble("FACD_CAN_DEV", detaFac.getFieldDouble("FACD_CAN_DEV") + deta.getFieldDouble("NCD_CANTIDAD"));
                        detaFac.setFieldString("FACD_SERIES_DEV", deta.getFieldString("NCD_NOSERIE"));
                        detaFac.Modifica(oConn);
                     }
                     if (deta.getFieldInt("TKTD_ID") != 0) {
                        vta_ticketsdeta detaFac = new vta_ticketsdeta();
                        detaFac.ObtenDatos(deta.getFieldInt("TKTD_ID"), oConn);
                        detaFac.setFieldDouble("TKTD_CAN_DEV", detaFac.getFieldDouble("TKTD_CAN_DEV") + deta.getFieldDouble("NCD_CANTIDAD"));
                        detaFac.setFieldString("TKTD_SERIES_DEV", deta.getFieldString("NCD_NOSERIE"));
                        detaFac.Modifica(oConn);
                     }

                  }
               }
               // </editor-fold>
               //Si almacenamos bien la nota de credito
               if (this.strResultLast.equals("OK")) {
                  //Obtenemos el id del movimiento de inventario para ligar los costos
                  int intInv = 0;
                  //Aplicamos inventarios unicamente si el ticket no es de servicios
                  //Y es un ticket o una factura
                  if (this.document.getFieldInt(this.strPrefijoMaster + "_ESSERV") == 0) {
                     // <editor-fold defaultstate="collapsed" desc="Si esta activa la bandera de afectar inventarios procedemos">
                     if (bolAfectaInv) {
                        //Crear objeto de inventario y aplicarlo
                        Inventario inv = new Inventario(this.oConn, this.varSesiones, this.request);
                        //Inicializamos
                        inv.Init();
                        //Definimos valores
                        inv.setBolTransaccionalidad(false);
                        inv.setIntTipoOperacion(Inventario.ENTRADA);
                        inv.setIntTipoCosteo(this.intSistemaCostos);//SistemaCostos
                        inv.getMovProd().setFieldInt("TIN_ID", Inventario.ENTRADA);
                        //Asignamos los valores master
                        inv.getMovProd().setFieldInt("SC_ID", this.document.getFieldInt("NC_SC_ID2"));
                        // inv.getMovProd().setFieldInt("SC_ID2", this.document.getFieldInt("SC_ID2"));
                        inv.getMovProd().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        inv.getMovProd().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                        inv.getMovProd().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                        inv.getMovProd().setFieldString("MP_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                        inv.getMovProd().setFieldString("MP_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                        //Asignamos los valores de las partidas
                        it = this.lstMovs.iterator();
                        while (it.hasNext()) {
                           TableMaster deta = it.next();
                           if (!deta.getFieldString(this.strPrefijoDeta + "_NOSERIE").equals("") && !deta.getFieldString(this.strPrefijoDeta + "_NOSERIE").equals("null")) {
                              String[] lstSeriesI = deta.getFieldString(this.strPrefijoDeta + "_NOSERIE").split(",");
                              //Recorremos cada numero de serie para ingresarlo
                              for (int y = 0; y < lstSeriesI.length; y++) {
                                 //Copiamos valores del ticket
                                 vta_movproddeta detaInv = new vta_movproddeta();
                                 detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                                 detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                                 detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt(this.strPrefijoDeta + "_ID"));
                                 detaInv.setFieldDouble("MPD_ENTRADAS", 1);
                                 detaInv.setFieldString("PR_CODIGO", deta.getFieldString(this.strPrefijoDeta + "_CVE"));
                                 detaInv.setFieldString("PL_NUMLOTE", lstSeriesI[y]);
                                 detaInv.setFieldString("MPD_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                                 inv.AddDetalle(detaInv);
                              }
                           } else {
                              //Copiamos valores del ticket
                              vta_movproddeta detaInv = new vta_movproddeta();
                              detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                              detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                              detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt(this.strPrefijoDeta + "_ID"));
                              detaInv.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD"));
                              detaInv.setFieldString("PR_CODIGO", deta.getFieldString(this.strPrefijoDeta + "_CVE"));
                              detaInv.setFieldString("PL_NUMLOTE", deta.getFieldString(this.strPrefijoDeta + "_NOSERIE"));
                              detaInv.setFieldString("MPD_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                              inv.AddDetalle(detaInv);
                           }

                        }
                        //Generamos la operacion de inventarios
                        inv.doTrx();
                        if (!inv.getStrResultLast().equals("OK")) {
                           //Fallo algo al aplicar el cargo
                           this.strResultLast = inv.getStrResultLast();
                        } else {
                           //Obtenemos el id del movimiento de inventario para ligar los costos
                           intInv = inv.getMovProd().getFieldInt("MP_ID");
                        }
                     }
                  }
                  // </editor-fold>
                  //***********
                  //Si todo resulto bien proseguimos
                  if (this.strResultLast.equals("OK")) {
                     // <editor-fold defaultstate="collapsed" desc="Crear objeto de cta cliente y aplicarlo">
                     //Generamos cargo por el importe de la deuda
                     if (this.saldoAnticipo > 0.0) {

                        // <editor-fold defaultstate="collapsed" desc="Credito">
                        movCliente cta_clie = new movCliente(this.oConn, this.varSesiones, this.request);
                        if (dblMontoAplicaNotaCredito > 0) {

                           cta_clie.setBolTransaccionalidad(false);
                           //Asignamos valores
                           cta_clie.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                           cta_clie.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                           cta_clie.getCta_clie().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                           cta_clie.getCta_clie().setFieldInt("MC_ESPAGO", 0);
                           cta_clie.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
//                     cta_clie.getCta_clie().setFieldInt("MC_TASAPESO", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                           cta_clie.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                           cta_clie.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                           cta_clie.getCta_clie().setFieldString("MC_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                           cta_clie.getCta_clie().setFieldDouble("MC_ABONO", dblMontoAplicaNotaCredito);
                           //Validamos el tipo de comprobante para la parte de honorarios
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                           cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                           cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                           cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                           cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                           //Evaluamos si aplica de una factura o remisi��n
                           if (this.document.getFieldInt("FAC_ID") != 0) {
                              cta_clie.getCta_clie().setFieldInt("FAC_ID", this.document.getFieldInt("FAC_ID"));
                           }
                           if (this.document.getFieldInt("TKT_ID") != 0) {
                              cta_clie.getCta_clie().setFieldInt("TKT_ID", this.document.getFieldInt("TKT_ID"));
                           }
                           cta_clie.Init();
                           cta_clie.doTrx();
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Saldo a favor">
                        cta_clie = new movCliente(this.oConn, this.varSesiones, this.request);
                        cta_clie.setBolTransaccionalidad(false);
                        //Asignamos valores
                        cta_clie.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                        cta_clie.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        cta_clie.getCta_clie().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        cta_clie.getCta_clie().setFieldInt("MC_ESPAGO", 1);
                        cta_clie.getCta_clie().setFieldInt("MC_ANTICIPO", 1);
                        cta_clie.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
//                     cta_clie.getCta_clie().setFieldInt("MC_TASAPESO", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                        cta_clie.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                        cta_clie.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                        cta_clie.getCta_clie().setFieldString("MC_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                        cta_clie.getCta_clie().setFieldDouble("MC_ABONO", this.saldoAnticipo);
                        //Validamos el tipo de comprobante para la parte de honorarios
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO1", 0);
                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO2", 0);
                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO3", 0);
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", 0);
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", 0);
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", 0);
                        cta_clie.getCta_clie().setFieldDouble("MC_SALDO_ANTICIPO", this.saldoAnticipo);
                        cta_clie.setBolEsAnticipo(true);
                        //Evaluamos si aplica de una factura o remisi��n
                        cta_clie.Init();
                        cta_clie.doTrx();
                        // </editor-fold>
                     } else {
                        movCliente cta_clie = new movCliente(this.oConn, this.varSesiones, this.request);
                        cta_clie.setBolTransaccionalidad(false);
                        //Asignamos valores
                        cta_clie.getCta_clie().setFieldInt("CT_ID", this.document.getFieldInt("CT_ID"));
                        cta_clie.getCta_clie().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        cta_clie.getCta_clie().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        cta_clie.getCta_clie().setFieldInt(this.strPrefijoMaster + "_ID", intId);
//                     cta_clie.getCta_clie().setFieldInt("MC_TASAPESO", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                        cta_clie.getCta_clie().setFieldInt("MC_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                        cta_clie.getCta_clie().setFieldString("MC_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                        cta_clie.getCta_clie().setFieldString("MC_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                        cta_clie.getCta_clie().setFieldDouble("MC_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL"));
                        //Validamos el tipo de comprobante para la parte de honorarios
                        if (this.bolEsLocal) {
                           if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 0) {
                              cta_clie.getCta_clie().setFieldDouble("MC_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                           }
                        } else {
                           if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 2) {
                              cta_clie.getCta_clie().setFieldDouble("MC_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                           }
                        }
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                        cta_clie.getCta_clie().setFieldDouble("MC_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                        cta_clie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                        //Evaluamos si aplica de una factura o remisi��n
                        if (this.document.getFieldInt("FAC_ID") != 0) {
                           cta_clie.getCta_clie().setFieldInt("FAC_ID", this.document.getFieldInt("FAC_ID"));
                        }
                        if (this.document.getFieldInt("TKT_ID") != 0) {
                           cta_clie.getCta_clie().setFieldInt("TKT_ID", this.document.getFieldInt("TKT_ID"));
                        }
                        cta_clie.Init();
                        cta_clie.doTrx();
                     }
                     // </editor-fold>

                     //Aqui validamos si todo paso ok
                     // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
                     if (this.strResultLast.equals("OK")) {
                        //obtener costo del detalle Y actualizarlo
                        //Recorremos todas las partidas para buscar y  actualizar su costo
                        it = this.lstMovs.iterator();
                        while (it.hasNext()) {
                           TableMaster deta = it.next();
                           strSql = "SELECT sum(MPD_SALIDAS * MPD_COSTO) as costo FROM vta_movproddeta "
                                   + " WHERE vta_movproddeta.MPD_IDORIGEN = " + deta.getFieldInt(this.strPrefijoDeta + "_ID") + " "
                                   + " AND MP_ID = " + intInv;
                           try {
                              ResultSet rs = oConn.runQuery(strSql, true);
                              while (rs.next()) {
                                 double dblCosto = rs.getDouble("costo");
                                 double dblCostoAplica = dblCosto / deta.getFieldDouble(this.strPrefijoDeta + "_CANTIDAD");
                                 //Actualizamos el costo
                                 String strUpdate = "UPDATE " + this.strNomTablaDeta + " set "
                                         + this.strPrefijoDeta + "_COSTO =  " + dblCostoAplica + " "
                                         + " WHERE " + this.strNomTablaDeta + "." + this.strPrefijoDeta + "_ID = " + deta.getFieldInt(this.strPrefijoDeta + "_ID");
                                 oConn.runQueryLMD(strUpdate);
                              }
                              rs.close();
                           } catch (SQLException ex) {
                              this.strResultLast = "ERROR:" + ex.getMessage();
                              log.error(ex.getMessage());
                           }
                        }
                     }
                     // </editor-fold>
                     //Generamos la firma electronica en caso de que proceda
                     if (this.strResultLast.equals("OK")) {
                        // <editor-fold defaultstate="collapsed" desc="Firma electronica">
                        if (bolFirmaSAT && this.bolTimbrar) {
                           //Instanciamos objeto para generar el XML que pide el SAT
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
                           SAT.setBolEsNc(true);
                           SAT.setIntEMP_TIPOCOMP(intFAC_TIPOCOMP);
                           String strRes = SAT.GeneraXml();
                           if (!strRes.equals("OK")) {
                              this.strResultLast = strRes;
                           }
                        }
                        // </editor-fold>
                        // <editor-fold defaultstate="collapsed" desc="Vemos si paso las validaciones para guardar la contabilidad">
                        if (this.strResultLast.equals("OK")) {
                           // <editor-fold defaultstate="collapsed" desc="Solo procede si la empresa esta configurada">
                           if (intEMP_USECONTA == 1) {

                              //Objeto para calculo de poliza contable
                              ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                              //Calcula la poliza para facturas
                              contaUtil.CalculaPolizaContablenNCredito(this.document.getFieldInt("EMP_ID"),
                                      strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
                                      strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
                                      this.document.getFieldInt("NC_MONEDA"), document, this.document.getFieldInt("TI_ID"), intId,
                                      strNomTablaMaster, strPrefijoMaster, "NEW");

                           }
                           // </editor-fold>
                           //Guardamos la bitacora
                           this.saveBitacora(this.strTipoVta, "NUEVA", intId);
                        }
                        // </editor-fold>
                     }
                  }
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Terminamos la operacion">
            if (this.bolTransaccionalidad) {
               if (this.strResultLast.equals("OK")) {
                  this.oConn.runQueryLMD("COMMIT");
               } else {
                  this.oConn.runQueryLMD("ROLLBACK");
               }
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
      this.strResultLast = "OK";
      double abono = 0.0;
      String strSql = "SELECT MC_ABONO,MC_SALDO_ANTICIPO from vta_mov_cte where NC_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID") + " AND FAC_ID=0 AND TKT_ID =0";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            abono = rs.getDouble("MC_ABONO");
            saldoAnticipo = rs.getDouble("MC_SALDO_ANTICIPO");
         }
         rs.close();
         log.debug("Aono y ant: " + abono + " " + saldoAnticipo);
         if (abono > saldoAnticipo) {
            this.strResultLast = "ERROR:El saldo del anticpo ya fue usado no se puede anular la nota";
         }
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage());
      }
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Activamos bandera de timbrado
      if (this.document.getFieldInt(this.strPrefijoMaster + "_ES_DEVO") == 1) {
         this.bolTimbrar = false;
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos si pasamos las validaciones">
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.document.getFieldInt(this.strPrefijoMaster + "_ANULADA") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos la existencia de los productos">
            String strResultInv = "OK";
            if (this.document.getFieldInt(this.strPrefijoMaster + "_ESSERV") == 0
                    /*&& (this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA))*/) {
               // <editor-fold defaultstate="collapsed" desc="Si esta activa la bandera de afectar inventarios procedemos">
               if (bolAfectaInv) {
                  //Aplica inventarios
                  int intInvId = 0;
                  strSql = "SELECT MP_ID from vta_movprod where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intInvId = rs.getInt("MP_ID");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                     log.error(ex.getMessage());
                  }
                  //Crear objeto de inventario y aplicarlo
                  Inventario inv = new Inventario(this.oConn, this.varSesiones, this.request);
                  //Definimos el id del inventario
                  inv.getMovProd().setFieldInt("MP_ID", intInvId);
                  //Inicializamos
                  inv.Init();
                  //Definimos valores
                  inv.setBolTransaccionalidad(false);
                  inv.doTrxAnul();
                  strResultInv = inv.getStrResultLast();
               }
               // </editor-fold>
            }
            // </editor-fold>
            //Validamos si no sucedio algun error al cancelar el inventario y todo sigue OK
            if (!strResultInv.equals("OK")) {
               this.strResultLast = strResultInv;
            } else {
               // <editor-fold defaultstate="collapsed" desc="Cancelar cargos">
               strSql = "SELECT MC_ID from vta_mov_cte "
                       + "where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                       + " AND MC_ANULADO = 0 ";
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
                     //Indicamos si el movimiento es por una facturacion de tickets
                     if (this.bolFacturaTicket) {
                        cta_cte.getCta_clie().setFieldInt("MC_FT", 1);
                     }
                     cta_cte.doTrxAnul();
                     if (!cta_cte.getStrResultLast().equals("OK")) {
                        this.strResultLast = cta_cte.getStrResultLast();
                        break;
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               // </editor-fold>
            }
            if (this.strResultLast.equals("OK")) {
               // <editor-fold defaultstate="collapsed" desc="Definimos campos">
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
                  //Actualizamos la poliza contable
                  // <editor-fold defaultstate="collapsed" desc="Obtenemos banderas de la empresa">
                  int intEMP_USECONTA = 0;
                  String strCtaVtasGlobal = "";
                  String strCtaVtasIVAGlobal = "";
                  String strCtaVtasIVATasa = "";
                  String strCtaVtasCteGlobal = "";
                  String strCtaVtas = "";
                  String strCtaVtasCte = "";
                  String strEMP_PASSCP = "";
                  String strEMP_USERCP = "";
                  int intEMP_CFD_CFDI = 0;
                  //Consultamos la info de la empresa
                  strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                          + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,EMP_PASSCP,EMP_USERCP,EMP_CFD_CFDI "
                          + " FROM vta_empresas "
                          + " WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                        strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
                        strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
                        strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
                        strEMP_PASSCP = rs.getString("EMP_PASSCP");
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                     log.error(ex.getMessage());
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Enviamos aviso de cancelacion al SAT si es CDFI">
                  if (document.getFieldInt(this.strPrefijoMaster + "_ES_CFD") == 0
                          && document.getFieldInt(this.strPrefijoMaster + "_ES_CBB") == 0
                          && this.bolTimbrar) {
                     String strResp = CancelaComprobante();
                     if (!strResp.equals("OK")) {
                        this.strResultLast = strResp;
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Quitamos la cantidad devuelta">
                  String srtRes = "Select * from vta_ncreditodeta where NC_ID=" + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                  ResultSet rs1;
                  try {
                     rs1 = oConn.runQuery(srtRes);
                     while (rs1.next()) {

                        if (rs1.getInt("FACD_ID") != 0) {
                           vta_facturadeta detaFac = new vta_facturadeta();
                           detaFac.ObtenDatos(rs1.getInt("FACD_ID"), oConn);
                           detaFac.setFieldDouble("FACD_CAN_DEV", detaFac.getFieldDouble("FACD_CAN_DEV") - rs1.getDouble("NCD_CANTIDAD"));
                           detaFac.setFieldString("FACD_SERIES_DEV", "");
                           detaFac.Modifica(oConn);
                        }
                        if (rs1.getInt("TKTD_ID") != 0) {
                           vta_ticketsdeta detaFac = new vta_ticketsdeta();
                           detaFac.ObtenDatos(rs1.getInt("TKTD_ID"), oConn);
                           detaFac.setFieldDouble("TKTD_CAN_DEV", detaFac.getFieldDouble("TKTD_CAN_DEV") - rs1.getDouble("NCD_CANTIDAD"));
                           detaFac.setFieldString("TKTD_SERIES_DEV", "");
                           detaFac.Modifica(oConn);
                        }
                     }
                     rs1.close();
                  } catch (SQLException ex) {
                     log.debug(ex.getLocalizedMessage());
                  }
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="Solo aplica si esta configurada la contabilidad">
                  if (intEMP_USECONTA == 1) {
                     //Buscamos cuentas para los ivas
                     strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB FROM vta_tasaiva "
                             + " where TI_ID=" + this.document.getFieldInt("TI_ID") + "";
                     try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                           strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
                        }
                        rs.close();
                     } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        log.error(ex.getMessage());
                     }
                     //Actualizamos la poliza contable
                     PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
                     poli.setStrOper("CANCEL");
                     poli.setStrUserCte(strEMP_USERCP);
                     poli.setStrPassCte(strEMP_PASSCP);
                     poli.setDocumentMaster(document);
                     //Validamos las cuentas a usar
                     //Ventas
                     if (strCtaVtas.isEmpty()) {
                        poli.getLstCuentas().add(strCtaVtasGlobal);
                     } else {
                        poli.getLstCuentas().add(strCtaVtas);
                     }
                     //IVA
                     if (strCtaVtasIVATasa.isEmpty()) {
                        poli.getLstCuentas().add(strCtaVtasIVAGlobal);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasIVATasa);
                     }
                     //Cte
                     if (strCtaVtasCte.isEmpty()) {
                        poli.getLstCuentas().add(strCtaVtasCteGlobal);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasCte);
                     }
                     try {
                        poli.callRemote(this.document.getFieldInt(this.strPrefijoMaster + "_ID"), PolizasContables.NCREDITO);
                        if (poli.strResultLast.startsWith("OK")) {
                           //Marcamos la venta como procesada
                           this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
                                   + " SET " + this.strPrefijoMaster + "_EXEC_INTER_CP_ANUL =  " + poli.strResultLast.replace("OK.", "")
                                   + " WHERE " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
                        } else {
                           System.out.println("RESULT CONTAB:" + poli.strResultLast);
                        }
                     } catch (Exception ex) {
                        if (ex.getMessage() != null) {
                           this.bitacora.GeneraBitacora(ex.getMessage(), oConn.getStrUsuario(), "Polizas NC:", oConn);
                           System.out.println("Error in call webservice?" + ex.getMessage());
                        }
                     }
                  }
                  // </editor-fold>
               }
               //Guardamos la bitacora
               this.saveBitacora(this.strTipoVta, "ANULAR", this.document.getFieldInt(this.strPrefijoMaster + "_ID"));
            }
            // <editor-fold defaultstate="collapsed" desc="Terminamos la operacion">
            if (this.bolTransaccionalidad) {
               if (this.strResultLast.equals("OK")) {
                  this.oConn.runQueryLMD("COMMIT");
               } else {
                  this.oConn.runQueryLMD("ROLLBACK");
               }
            }
            // </editor-fold>
         } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
         }
         // </editor-fold>
      }
      // </editor-fold>

   }

   @Override
   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
