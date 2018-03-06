package ERP;

import Core.FirmasElectronicas.Opalina;
import Tablas.vta_cxpagardetalle;
import Tablas.vta_movproddeta;
import Tablas.vta_ncreditoprov;
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
public class NCreditoProv extends Ticket implements ProcesoInterfaz {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(NCredito.class.getName());

   /**
    * Construtor del objeto de notas de credito
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    */
   public NCreditoProv(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new vta_ncreditoprov();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strPrefijoMaster = "NC";
      this.strPrefijoDeta = "NCD";
      this.strNomTablaMaster = "vta_ncreditoprov";
      this.strNomTablaDeta = "vta_ncreditodeta_prov";
      this.strFechaAnul = "";
      this.strTipoVta = "NCREDITO";
      this.strPATHXml = "";
      this.strPATHKeys = "";
      this.strMyPassSecret = "";
      this.bolSendMailMasivo = true;
   }

   /**
    * Construtor del objeto notas de credito
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion
    */
   public NCreditoProv(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new vta_ncreditoprov();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strPrefijoMaster = "NC";
      this.strPrefijoDeta = "NCD";
      this.strNomTablaMaster = "vta_ncreditoprov";
      this.strNomTablaDeta = "vta_ncreditodeta_prov";
      this.strFechaAnul = "";
      this.strTipoVta = "NCREDITO";
      this.strPATHXml = "";
      this.strPATHKeys = "";
      this.strMyPassSecret = "";
      this.bolSendMailMasivo = true;
   }

   @Override
   public void doTrx() {
      this.strResultLast = "OK";

      // <editor-fold defaultstate="collapsed" desc="Definimos valores">
      this.document.setFieldString(this.strPrefijoMaster + "_FECHACREATE", this.fecha.getFechaActual());
      this.document.setFieldString(this.strPrefijoMaster + "_HORA", this.fecha.getHoraActualHHMMSS());
      this.document.setFieldDouble(this.strPrefijoMaster + "_SALDO", 0);
      double dblSaldoaFavor = 0;
      double dblMontoNCredito = 0;
      double dblMontoCXPagar = 0;
      boolean bolSaldoaFavor = false;

      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.document.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.document.getFieldString(this.strPrefijoMaster + "_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.document.getFieldInt("PV_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el proveedor";
      }
      /* if (this.document.getFieldString("FAC_FORMADEPAGO").isEmpty()) {
       this.strResultLast = "ERROR:Falta definir la forma de pago";
       }
       if (this.document.getFieldString("FAC_METODODEPAGO").isEmpty()) {
       this.strResultLast = "ERROR:Falta definir el metodo de pago";
       }*/
      if (this.lstMovs.isEmpty()) {
         this.strResultLast = "ERROR:Debe tener por lo menos un item la venta";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Evaluamos que la nota de credito afecte una factura
      if (this.document.getFieldInt("CXP_ID") == 0) {
         this.strResultLast = "ERROR:La nota de credito debe aplicar a una factura";
      }
      //Evaluamos el saldo y estatus de la NOTA DE CREDITO
      String strSqlC = "select PV_ID,CXP_ANULADO,CXP_FOLIO,CXP_SALDO from vta_cxpagar where  CXP_ID = " + this.document.getFieldInt("CXP_ID");
      boolean bolExisteFac = false;
      try {
         ResultSet rs = oConn.runQuery(strSqlC);
         while (rs.next()) {
            bolExisteFac = true;
            //Evaluamos si esta anulada
            if (rs.getInt("CXP_ANULADO") == 1) {
               this.strResultLast = "ERROR:La factura con folio " + rs.getString("CXP_FOLIO") + " esta anulada";
            } else {
               //Verificamos que el importe de la nota de credito no sea mayor al saldo de la factura
               if (this.document.getFieldDouble("NP_TOTAL") > rs.getDouble("CXP_SALDO")) {
                  dblSaldoaFavor = 0;
                  dblMontoNCredito = 0;
                  dblMontoCXPagar = rs.getDouble("CXP_SALDO");
                  bolSaldoaFavor = true;
               }// else {
               if (this.document.getFieldInt("PV_ID") != rs.getInt("PV_ID")) {
                  this.strResultLast = "ERROR:La factura con folio " + rs.getString("CXP_FOLIO") + " esta asignado a un proveedor diferente al seleccionado";
               }
               //}
            }
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      if (!bolExisteFac) {
         this.strResultLast = "ERROR:La factura con id " + this.document.getFieldInt("CXP_ID") + " no existe";
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
      String strNomCert = "";
      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Copiar datos del cliente">
         boolean bolFindCte = false;
         String strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_CALLE,PV_COLONIA,PV_LOCALIDAD,"
                 + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,"
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
               this.document.setFieldInt(this.strPrefijoMaster + "_DIASCREDITO", rs.getInt("PV_DIASCREDITO"));
               this.document.setFieldInt(this.strPrefijoMaster + "_LPRECIOS", rs.getInt("PV_LPRECIOS"));
               // strCtaVtas = rs.getString("CT_CONTAVTA");
               //strCtaVtasCte = rs.getString("CT_CONTACTE");
               intCT_TIPOPERS = rs.getInt("PV_TIPOPERS");
               bolFindCte = true;
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Validamos si encontramos al proveedor">
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
               ex.fillInStackTrace();
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
                  ex.fillInStackTrace();
               }
               // </editor-fold>
            } else {
               // <editor-fold defaultstate="collapsed" desc="VERSION WEB">
               strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMPNC,"
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,"
                       + "AES_DECRYPT(EMP_PASSKEY, '" + strMyPassSecret + "') AS unencrypted,"
                       + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_USACODBARR,EMP_VTA_DETA,EMP_CFD_CFDI "
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
                        strNomCert = rs.getString("EMP_NOMCERT");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  ex.fillInStackTrace();
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
                  /* if (intEMP_CFD_CFDI == 1) {
                   this.document.setFieldString(this.strPrefijoMaster + "_FOLIO_C", strFolio);
                   } else {
                   this.document.setFieldString(this.strPrefijoMaster + "_FOLIO", strFolio);
                   }*/
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
                        ex.fillInStackTrace();
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
//                     try {
//                        intCant1 = Integer.valueOf(request.getParameter(strPrefijoDeta + "_CAN_DEV" + i));
//                        intCant2 = Integer.valueOf(request.getParameter(strPrefijoDeta + "_DEVOLVER" + i));
//
//                        String strUpdateFacDeta = "UPDATE vta_facturasdeta SET "
//                                + " FACD_CAN_DEV =" + (intCant1 + intCant2)
//                                + " ,FACD_SERIES_DEV ='" + request.getParameter(strPrefijoDeta + "_SERIES_DEV" + i) + "' "
//                                + " WHERE FACD_ID = " + request.getParameter("FACD_ID" + i);
//                        oConn.runQueryLMD(strUpdateFacDeta);
//                        deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Double.valueOf(request.getParameter(strPrefijoDeta + "_DEVOLVER" + i)));
//                     } catch (NumberFormatException ex) {
//                        System.out.println(ex.getMessage());
//                     }
                     //Cargamos el detalle de facturas que se esta devolviendo
                     vta_cxpagardetalle detaFac = new vta_cxpagardetalle();
                     detaFac.ObtenDatos(deta.getFieldInt("CXPD_ID"), oConn);
                     detaFac.setFieldDouble("CXPD_CAN_DEV", detaFac.getFieldDouble("FACD_CAN_DEV") + deta.getFieldDouble("NCD_CANTIDAD"));
                     detaFac.setFieldString("CXPD_SERIES_DEV", deta.getFieldString("NCD_NOSERIE"));
                     detaFac.Modifica(oConn);
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
                        inv.setIntTipoOperacion(Inventario.SALIDA);
                        inv.setIntTipoCosteo(this.intSistemaCostos);//SistemaCostos
                        inv.getMovProd().setFieldInt("TIN_ID", Inventario.SALIDA);
                        //Asignamos los valores master
                        inv.getMovProd().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        // inv.getMovProd().setFieldInt("SC_ID2", this.document.getFieldInt("SC_ID2"));
                        inv.getMovProd().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        inv.getMovProd().setFieldInt("CT_ID", this.document.getFieldInt("PV_ID"));
                        inv.getMovProd().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                        inv.getMovProd().setFieldString("MP_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                        inv.getMovProd().setFieldString("MP_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                        //Asignamos los valores de las partidas
                        it = this.lstMovs.iterator();
                        while (it.hasNext()) {
                           TableMaster deta = it.next();
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
                     // <editor-fold defaultstate="collapsed" desc="Crear objeto de cta proveedor y aplicarlo">
                     //Generamos cargo por el importe de la deuda
                     MovProveedor cta_prov = new MovProveedor(this.oConn, this.varSesiones, this.request);
                     cta_prov.setBolTransaccionalidad(false);
                     //Asignamos valores
                     cta_prov.getCta_prov().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
                     cta_prov.getCta_prov().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                     cta_prov.getCta_prov().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                     cta_prov.getCta_prov().setFieldInt("MP_ESPAGO", 0);
                     cta_prov.getCta_prov().setFieldInt(this.strPrefijoMaster + "_ID", intId);
//                     cta_clie.getCta_clie().setFieldInt("MC_TASAPESO", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                     cta_prov.getCta_prov().setFieldDouble("MP_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                     cta_prov.getCta_prov().setFieldInt("MP_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                     cta_prov.getCta_prov().setFieldString("MP_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                     cta_prov.getCta_prov().setFieldString("MP_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                     if (bolSaldoaFavor) {
                        if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 2) {
                           dblSaldoaFavor = this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL") - dblMontoCXPagar;
                           dblMontoNCredito = dblMontoCXPagar;
                        } else {
                           dblSaldoaFavor = this.document.getFieldDouble(this.strPrefijoMaster + "_NETO") - dblMontoCXPagar;
                           dblMontoNCredito = dblMontoCXPagar;
                        }
                        cta_prov.getCta_prov().setFieldDouble("MP_ABONO", dblMontoNCredito);
                     } else {
                        cta_prov.getCta_prov().setFieldDouble("MP_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_TOTAL"));
                        //Validamos el tipo de comprobante para la parte de honorarios
                        if (this.bolEsLocal) {
                           if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 0) {
                              cta_prov.getCta_prov().setFieldDouble("MP_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                           }
                        } else {
                           if (intCT_TIPOPERS == 1 && intEMP_TIPOPERS == 2) {
                              cta_prov.getCta_prov().setFieldDouble("MP_ABONO", this.document.getFieldDouble(this.strPrefijoMaster + "_NETO"));
                           }
                        }
                     }
                     cta_prov.getCta_prov().setFieldDouble("MP_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                     cta_prov.getCta_prov().setFieldDouble("MP_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                     cta_prov.getCta_prov().setFieldDouble("MP_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                     cta_prov.getCta_prov().setFieldDouble("MP_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                     cta_prov.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                     cta_prov.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                     cta_prov.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                     //Evaluamos si aplica de una factura o remisión
                     if (this.document.getFieldInt("CXP_ID") != 0) {
                        cta_prov.getCta_prov().setFieldInt("CXP_ID", this.document.getFieldInt("CXP_ID"));
                     }

                     cta_prov.Init();
                     cta_prov.doTrx();
                     // </editor-fold>

                     if (bolSaldoaFavor) {
                        // <editor-fold defaultstate="collapsed" desc="Si aplica un saldo a favor aqui lo generamos">
                        //Generamos cargo por el importe de la deuda
                        cta_prov = new MovProveedor(this.oConn, this.varSesiones, this.request);
                        cta_prov.setBolTransaccionalidad(false);
                        //Asignamos valores
                        cta_prov.getCta_prov().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
                        cta_prov.getCta_prov().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        cta_prov.getCta_prov().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        cta_prov.getCta_prov().setFieldInt("MP_ESPAGO", 1);
                        cta_prov.setBolEsAnticipo(true);
                        cta_prov.getCta_prov().setFieldInt("MP_ANTICIPO", 1);
                        cta_prov.getCta_prov().setFieldInt(this.strPrefijoMaster + "_ID", intId);
                        cta_prov.getCta_prov().setFieldDouble("MP_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                        cta_prov.getCta_prov().setFieldInt("MP_MONEDA", this.document.getFieldInt(this.strPrefijoMaster + "_MONEDA"));
                        cta_prov.getCta_prov().setFieldString("MP_FECHA", this.document.getFieldString(this.strPrefijoMaster + "_FECHA"));
                        cta_prov.getCta_prov().setFieldString("MP_NOTAS", this.document.getFieldString(this.strPrefijoMaster + "_NOTAS"));
                        cta_prov.getCta_prov().setFieldDouble("MP_ABONO", dblSaldoaFavor);
                        cta_prov.getCta_prov().setFieldDouble("MP_TASAPESO", this.document.getFieldDouble(this.strPrefijoMaster + "_TASAPESO"));
                        cta_prov.getCta_prov().setFieldDouble("MP_IMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO1"));
                        cta_prov.getCta_prov().setFieldDouble("MP_IMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO2"));
                        cta_prov.getCta_prov().setFieldDouble("MP_IMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_IMPUESTO3"));
                        cta_prov.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA1"));
                        cta_prov.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA2"));
                        cta_prov.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", this.document.getFieldDouble(this.strPrefijoMaster + "_TASA3"));
                        cta_prov.getCta_prov().setFieldInt("CXP_ID", 0);

                        cta_prov.Init();
                        cta_prov.doTrx();
                        // </editor-fold>   
                     }

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
                              ex.fillInStackTrace();
                           }
                        }
                     }
                     // </editor-fold>
                     //Generamos la firma electronica en caso de que proceda
                     if (this.strResultLast.equals("OK")) {

                        // <editor-fold defaultstate="collapsed" desc="Vemos si paso las validaciones para guardar la contabilidad">
                        if (this.strResultLast.equals("OK")) {
                           // <editor-fold defaultstate="collapsed" desc="Solo procede si la empresa esta configurada">
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
                                 ex.fillInStackTrace();
                              }
                              //Actualizamos la poliza contable
                              PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
                              poli.setStrOper("NEW");
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
                              //Validamos si la cuenta de ventas se genera a partir de productos
                              if (intEMP_VTA_DETA == 1) {
                                 //Barremos productos para obtener cuentas agrupadas
                                 this.getPoliDetaProd(poli, false, this.document.getFieldString("NC_FOLIO"));
                              }
                              try {
                                 poli.callRemote(intId, PolizasContables.NCREDITO);
                                 if (poli.strResultLast.startsWith("OK")) {
                                    //Marcamos la venta como procesada
                                    this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
                                            + " SET " + this.strPrefijoMaster + "_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
                                            + " WHERE " + this.strPrefijoMaster + "_ID = " + intId);
                                 } else {
                                    this.bitacora.GeneraBitacora("Polizas NC:", oConn.getStrUsuario(), poli.strResultLast, oConn);
                                    System.out.println("strResultLast?" + poli.strResultLast);
                                 }
                              } catch (Exception ex) {
                                 if (ex.getMessage() != null) {
                                    this.bitacora.GeneraBitacora(ex.getMessage(), oConn.getStrUsuario(), "Polizas NC:", oConn);
                                 }
                                 System.out.println("Error in call webservice?" + ex.getMessage());
                              }
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
            this.strResultLast = "ERROR:El proveedor seleccionado no existe";
         }
         // </editor-fold>
      }
   }

   @Override
   public void doTrxAnul() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.document.getFieldInt(this.strPrefijoMaster + "_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString(this.strPrefijoMaster + "_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
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
                    && (this.strTipoVta.equals(Ticket.TICKET) || this.strTipoVta.equals(Ticket.FACTURA))) {
               // <editor-fold defaultstate="collapsed" desc="Si esta activa la bandera de afectar inventarios procedemos">
               if (bolAfectaInv) {
                  //Aplica inventarios
                  int intInvId = 0;
                  String strSql = "SELECT MP_ID from vta_movprod where " + this.strPrefijoMaster + "_ID = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID");
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intInvId = rs.getInt("MP_ID");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                     ex.fillInStackTrace();
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
               String strSql = "SELECT MP_ID from vta_mov_prov "
                       + "where  MP_ID  = " + this.document.getFieldInt(this.strPrefijoMaster + "_ID")
                       + " AND MP_ESPAGO = 0 AND MP_ANULADO = 0 ";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     //Quitamos la aplicacion de los cargos
                     MovProveedor cta_prov = new MovProveedor(this.oConn, this.varSesiones, this.request);
                     //Definimos el id del inventario
                     cta_prov.getCta_prov().setFieldInt("MP_ID", rs.getInt("MP_ID"));
                     //Inicializamos
                     cta_prov.Init();
                     //Definimos valores
                     cta_prov.setBolTransaccionalidad(false);
                     //Indicamos si el movimiento es por una facturacion de tickets
                     if (this.bolFacturaTicket) {
                        cta_prov.getCta_prov().setFieldInt("MP_FT", 1);
                     }
                     cta_prov.doTrxAnul();
                     if (!cta_prov.getStrResultLast().equals("OK")) {
                        this.strResultLast = cta_prov.getStrResultLast();
                        break;
                     }
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  ex.fillInStackTrace();
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
               } else {/*
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
                   String strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
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
                   ex.fillInStackTrace();
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
                   ex.fillInStackTrace();
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
                   // </editor-fold>*/

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
