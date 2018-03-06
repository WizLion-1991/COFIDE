package ERP;

import ERP.BusinessEntities.PoliCtas;
import Tablas.vta_cxpagar;
import Tablas.vta_mov_prov_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Modulo de negocios para generar las cuentas por pagar
 *
 * @author zeus
 */
public class CuentasxPagar extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected TableMaster document;
   protected ArrayList<TableMaster> lstMovs;
   protected ArrayList<vta_mov_prov_deta> lstPagos;
   protected int intEMP_ID = 0;
   protected int intEMP_TIPOCOMP = 0;
   protected boolean bolSendMailMasivo;
   protected String strFechaAnul;
   protected String strXMLFileName;
   protected String strPDFFileName;
   protected String strPathBase2;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CuentasxPagar.class.getName());
   /**
    * Cuenta contable por default en las partidas
    */
   protected String strCtaEmpty;
   protected int intCXP_TIPOCOMP = 0;
   protected boolean bolEsLocal = false;

   /*
    * Almacena la ruta destino de los archivos XML y PDF
    */

   public String getstrPathBase2() {
      return strPathBase2;
   }

   public void setstrPathBase2(String strPathBase) {
      this.strPathBase2 = strPathBase;
   }

   /**
    * Regresa el nombre del archivo XML
    *
    * @return
    */
   public String getStrXMLFileName() {
      return strXMLFileName;
   }

   /**
    * Anade el nombre del archivo XML
    *
    * @param strXMLFileName Es el objeto de detalle
    */
   public void setStrXMLFileName(String strXMLFileName) {
      this.strXMLFileName = strXMLFileName;
   }

   /**
    * Regresa el nombre del archivo PDF
    *
    * @return
    */
   public String getStrPDFFileName() {
      return strPDFFileName;
   }

   /**
    * Anade el nombre del archivo PDF
    *
    * @param strPDFFileName Es el objeto de detalle
    */
   public void setStrPDFFileName(String strPDFFileName) {
      this.strPDFFileName = strPDFFileName;
   }

   /**
    * Regresa el movimiento maestro de tickets
    *
    * @return Nos regresa el movimiento de tickets
    */
   public TableMaster getDocument() {
      return this.document;
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(TableMaster deta) {
      this.lstMovs.add(deta);
   }

   /**
    * Anade un objeto de forma de pago al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(vta_mov_prov_deta deta) {
      this.lstPagos.add(deta);
   }

   /**
    * Nos regresa el id de la empresa default
    *
    * @return Es un numero con el id de la empresa
    */
   public int getIntEMP_ID() {
      return intEMP_ID;
   }

   /**
    * Definimos el id de la empresa
    *
    * @param intEMP_ID Es un numero con el id de la empresa
    */
   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   /**
    * Nos dice si se envia el mail del formato
    *
    * @return Regresa true si se envia el mail
    */
   public boolean isBolSendMailMasivo() {
      return bolSendMailMasivo;
   }

   /**
    * Indica si se envia el mail masivo
    *
    * @param bolSendMailMasivo Nos regresa true si se envia el mail del formato
    */
   public void setBolSendMailMasivo(boolean bolSendMailMasivo) {
      this.bolSendMailMasivo = bolSendMailMasivo;
   }

   /**
    * Nos regresa el tipo de comprobante seleccionado
    *
    * @return Regresa el id del comprobante
    */
   public int getIntCXP_TIPOCOMP() {
      return intCXP_TIPOCOMP;
   }

   /**
    * Es el tipo de comprobante de la factura
    *
    * @param intCXP_TIPOCOMP Es el id del comprobante
    */
   public void setIntCXP_TIPOCOMP(int intCXP_TIPOCOMP) {
      this.intCXP_TIPOCOMP = intCXP_TIPOCOMP;
   }

   /**
    * Nos dice si estamos en modo local
    *
    * @return Es un valor boolean
    */
   public boolean isBolEsLocal() {
      return bolEsLocal;
   }

   /**
    * Define si esta en modo local
    *
    * @param bolEsLocal Es un valor boolean
    */
   public void setBolEsLocal(boolean bolEsLocal) {
      this.bolEsLocal = bolEsLocal;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CuentasxPagar(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new vta_cxpagar();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strFechaAnul = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      this.lstPagos = new ArrayList<vta_mov_prov_deta>();
   }

   public CuentasxPagar(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new vta_cxpagar();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strFechaAnul = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      this.lstPagos = new ArrayList<vta_mov_prov_deta>();
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void Init() {
      this.document.setFieldInt("CXP_USUARIO", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.document.getFieldInt("CXP_ID") != 0) {
         this.strFechaAnul = this.document.getFieldString("CXP_ANULFECHA");
         this.document.ObtenDatos(this.document.getFieldInt("CXP_ID"), oConn);
      }
   }

   public void doTrx() {
      // <editor-fold defaultstate="collapsed" desc="Inicializamos valores">
      this.strResultLast = "OK";
      this.document.setFieldString("CXP_FECHACREATE", this.fecha.getFechaActual());
      this.document.setFieldString("CXP_HORACREATE", this.fecha.getHoraActualHHMMSS());
      this.document.setFieldString("CXP_HORA", this.fecha.getHoraActual());
      //Valores para retencion
      int intCT_TIPOPERS = 0;
      int intEMP_TIPOPERS = 0;
      //Valores para contabilidad
      int intEMP_USECONTA = 0;
      int intEMP_VTA_DETA = 0;//cuenta de compras se genera a partir de productos
      String strCXPGlobal = "";
      String strCXPIVAGlobal = "";
      String strCXPIVATasa = "";
      String strCXPCteGlobal = "";
      String strCXPVtas = "";
      String strCXPVtasProv = "";
      String strEMP_URLCP = "";
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      int intSC_SOBRIMP1_2 = 0;
      int intSC_SOBRIMP1_3 = 0;
      int intSC_SOBRIMP2_3 = 0;
      double dblSC_TASA1 = 0;
      double dblSC_TASA2 = 0;
      double dblSC_TASA3 = 0;
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
      if (this.document.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.document.getFieldString("CXP_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.document.getFieldString("CXP_FECHA_PROVISION").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de provision";
      }
      if (this.document.getFieldInt("PV_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el proveedor";
      }
      if (this.lstMovs.isEmpty()) {
         this.strResultLast = "ERROR:Debe tener por lo menos un item la venta";
      }
      //Si el folio de la factura esta vacio lo buscamos
      if (!this.document.getFieldString("CXP_FOLIO").equals("")) {
         try {
            //Buscamos si existe en la misma empresa este folio
            boolean bolFound = false;
            ResultSet rs2 = this.oConn.runQuery("select CXP_ID from vta_cxpagar "
                    + " where CXP_FOLIO = '" + this.document.getFieldString("CXP_FOLIO") + "' and CXP_ANULADO = 0  "
                    + " and PV_ID = " + this.document.getFieldInt("PV_ID")
                    + " and EMP_ID = " + this.document.getFieldInt("EMP_ID"), true);
            while (rs2.next()) {
               bolFound = true;
               break;
            }
            rs2.close();
            if (bolFound) {
               this.strResultLast = "ERROR:el numero de factura de la cuenta por pagar ya existe.";
            }
         } catch (SQLException ex) {
            Logger.getLogger(CuentasxPagar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("CXP_FECHA_PROVISION"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Si tiene activo el permiso de autorizar marcamos autorizado el movimiento
      boolean bolPermisoAutoriza;
      bolPermisoAutoriza = ValidaPermisoAutoriza(460);
      if (bolPermisoAutoriza) {
         this.document.setFieldInt("CXP_AUTORIZA", 1);
         this.document.setFieldInt("CXP_USUARIO_AUTORIZA", this.varSesiones.getIntNoUser());
         this.document.setFieldString("CXP_HORA_AUTORIZA", this.fecha.getHoraActual());
         this.document.setFieldString("CXP_FECHA_AUTORIZA", this.fecha.getFechaActual());
         //this.document.setFieldInt("CXP_STATUS", 2);
      }
      // </editor-fold>

      //Validamos si pasamos las validaciones
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
               this.document.setFieldString("CXP_RAZONSOCIAL", rs.getString("PV_RAZONSOCIAL"));
               this.document.setFieldString("CXP_CALLE", rs.getString("PV_CALLE"));
               this.document.setFieldString("CXP_COLONIA", rs.getString("PV_COLONIA"));
               this.document.setFieldString("CXP_LOCALIDAD", rs.getString("PV_LOCALIDAD"));
               this.document.setFieldString("CXP_MUNICIPIO", rs.getString("PV_MUNICIPIO"));
               this.document.setFieldString("CXP_ESTADO", rs.getString("PV_ESTADO"));
               this.document.setFieldString("CXP_NUMERO", rs.getString("PV_NUMERO"));
               this.document.setFieldString("CXP_NUMINT", rs.getString("PV_NUMINT"));
               this.document.setFieldString("CXP_CP", rs.getString("PV_CP"));
               this.document.setFieldInt("CXP_DIASCREDITO", rs.getInt("PV_DIASCREDITO"));
               this.document.setFieldInt("CXP_LPRECIOS", rs.getInt("PV_LPRECIOS"));
               strCXPVtas = rs.getString("PV_CONTACOMP");
               strCXPVtasProv = rs.getString("PV_CONTAPROV");
               intCT_TIPOPERS = rs.getInt("PV_TIPOPERS");
               bolFindProv = true;
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si el proveedor se encontro">
         if (bolFindProv) {

            // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara la cuenta por pagar">
            strSql = "SELECT EMP_ID,SC_TASA1,SC_TASA2,SC_TASA3,"
                    + "SC_SOBRIMP1_2,SC_SOBRIMP1_3,SC_SOBRIMP2_3 "
                    + "FROM vta_sucursal WHERE SC_ID = " + this.document.getFieldInt("SC_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  this.document.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
                  dblSC_TASA1 = rs.getDouble("SC_TASA1");
                  dblSC_TASA2 = rs.getDouble("SC_TASA2");
                  dblSC_TASA3 = rs.getDouble("SC_TASA3");
                  intSC_SOBRIMP1_2 = rs.getInt("SC_SOBRIMP1_2");
                  intSC_SOBRIMP1_3 = rs.getInt("SC_SOBRIMP1_3");
                  intSC_SOBRIMP2_3 = rs.getInt("SC_SOBRIMP2_3");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               ex.fillInStackTrace();
            }
            //Si se definio el id de la empresa independiente de la sucursal
            if (this.intEMP_ID != 0) {
               this.document.setFieldInt("EMP_ID", this.intEMP_ID);
               this.bolFolioGlobal = false;
            }

            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Obtenemos informacion de la empresa">
            boolean bolTmp = oConn.isBolMostrarQuerys();
            oConn.setBolMostrarQuerys(false);
            if (this.bolEsLocal) {
               // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(LOCAL)">
               strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,"/**
                        * ,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTAPROV
                        */
                       + "EMP_PASSKEY AS unencrypted,EMP_CFD_CFDI,"
                       + "EMP_FIRMA,EMP_USACODBARR "
                       + "FROM vta_empresas "
                       + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                     this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");

                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  ex.fillInStackTrace();
               }
               // </editor-fold>
            } else {
               // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
               strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTAPROV,"
                       + "EMP_CFD_CFDI,"
                       + "EMP_FIRMA,EMP_URLCP,EMP_PASSCP,EMP_USERCP,EMP_USACODBARR,EMP_VTA_DETA "
                       + "FROM vta_empresas "
                       + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                     this.intEMP_TIPOCOMP = rs.getInt("EMP_TIPOCOMP");
                     intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                     intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                     strCXPGlobal = rs.getString("EMP_CTAVTA");
                     strCXPIVAGlobal = rs.getString("EMP_CTAIVA");
                     strCXPCteGlobal = rs.getString("EMP_CTAPROV");
                     strEMP_URLCP = rs.getString("EMP_URLCP");
                     strEMP_PASSCP = rs.getString("EMP_PASSCP");
                     strEMP_USERCP = rs.getString("EMP_USERCP");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  ex.fillInStackTrace();
               }
               // </editor-fold>
            }
            oConn.setBolMostrarQuerys(bolTmp);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Asignamos el tipo de comprobante">
            this.document.setFieldInt("CXP_TIPOCOMP", this.intEMP_TIPOCOMP);
            if (this.intCXP_TIPOCOMP != 0) {
               this.document.setFieldInt("CXP_TIPOCOMP", this.intCXP_TIPOCOMP);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calcular el importe del descuento total">
            double dblDescuentoTotal = 0;
            Iterator<TableMaster> itG = this.lstMovs.iterator();
            while (itG.hasNext()) {
               TableMaster deta = itG.next();
               dblDescuentoTotal += deta.getFieldDouble("CXPD_DESCUENTO");
            }
            this.document.setFieldDouble("CXP_DESCUENTO", dblDescuentoTotal);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Proceso de guardado del documento">
            if (this.strResultLast.equals("OK")) {

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
                  //Determinamos el tipo de documento
                  int intTipoFolio = Folios.CXPAGAR;
                  String strFolio = "";
                  folio.setBolEsLocal(this.bolEsLocal);
                  if (this.document.getFieldString("CXP_FOLIO").equals("")) {
                     strFolio = folio.doFolio(oConn, intTipoFolio, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                     this.document.setFieldString("CXP_FOLIO", strFolio);
                  } else {
                     strFolio = this.document.getFieldString("CXP_FOLIO");
                     folio.updateFolio(oConn, intTipoFolio, this.document.getFieldString("CXP_FOLIO"),
                             this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
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

                  // <editor-fold defaultstate="collapsed" desc="Cxpagar Recurrente. No implementado">
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Guardar detalle">
                  int intId = Integer.valueOf(this.document.getValorKey());
                  Iterator<TableMaster> it = this.lstMovs.iterator();
                  while (it.hasNext()) {
                     TableMaster deta = it.next();
                     deta.setFieldInt("CXP_ID", intId);
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
                        // <editor-fold defaultstate="collapsed" desc="Cargo proveedores">
                        //Crear objeto de cta vta_mov_prov y aplicarlo
                        //Generamos cargo por el importe de la deuda
                        MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
                        movProv.setBolTransaccionalidad(false);
                        //Asignamos valores
                        movProv.getCta_prov().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
                        movProv.getCta_prov().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                        movProv.getCta_prov().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
                        movProv.getCta_prov().setFieldInt("MP_ESPAGO", 0);
                        movProv.getCta_prov().setFieldInt("CXP_ID", intId);
                        movProv.getCta_prov().setFieldInt("MP_MONEDA", this.document.getFieldInt("CXP_MONEDA"));
                        movProv.getCta_prov().setFieldInt("MP_TASAPESO", 1);
                        movProv.getCta_prov().setFieldString("MP_FECHA", this.document.getFieldString("CXP_FECHA_PROVISION"));
                        movProv.getCta_prov().setFieldString("MP_NOTAS", this.document.getFieldString("CXP_NOTAS"));
                        movProv.getCta_prov().setFieldDouble("MP_CARGO", this.document.getFieldDouble("CXP_TOTAL"));
                        //Validamos el tipo de comprobante para la parte de honorarios
                        if (this.bolEsLocal) {
                           if (intCT_TIPOPERS == 2 && intEMP_TIPOPERS == 0) {
                              movProv.getCta_prov().setFieldDouble("MP_CARGO", this.document.getFieldDouble("CXP_NETO"));
                           }
                        } else if (intCT_TIPOPERS == 2 && intEMP_TIPOPERS == 1) {
                           movProv.getCta_prov().setFieldDouble("MP_CARGO", this.document.getFieldDouble("CXP_NETO"));
                        }

                        movProv.getCta_prov().setFieldDouble("MP_IMPUESTO1", this.document.getFieldDouble("CXP_IMPUESTO1"));
                        movProv.getCta_prov().setFieldDouble("MP_IMPUESTO2", this.document.getFieldDouble("CXP_IMPUESTO2"));
                        movProv.getCta_prov().setFieldDouble("MP_IMPUESTO3", this.document.getFieldDouble("CXP_IMPUESTO3"));
                        movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", this.document.getFieldDouble("CXP_TASA1"));
                        movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", this.document.getFieldDouble("CXP_TASA2"));
                        movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", this.document.getFieldDouble("CXP_TASA3"));
                        movProv.Init();
                        movProv.doTrx();
                        // </editor-fold>

                        //Validamos que el cargo halla pasado
                        if (movProv.getStrResultLast().equals("OK")) {
                           // <editor-fold defaultstate="collapsed" desc="Generamos el abono por el importe de los pagos">
                           MovProveedor movProvPago = new MovProveedor(this.oConn, this.varSesiones, this.request);
                           movProvPago.setBolCaja(true);
                           movProvPago.setBolTransaccionalidad(false);
                           Iterator<vta_mov_prov_deta> itPago = this.lstPagos.iterator();
                           double dblImportePago = 0;
                           double dblImpuestoPago1 = 0;
                           double dblImpuestoPago2 = 0;
                           double dblImpuestoPago3 = 0;
                           while (itPago.hasNext()) {
                              vta_mov_prov_deta detaPago = itPago.next();
                              detaPago.setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
                              detaPago.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                              detaPago.setFieldDouble("MPD_TASAIMPUESTO1", this.document.getFieldDouble("CXP_TASA1"));
                              detaPago.setFieldDouble("MPD_TASAIMPUESTO1", this.document.getFieldDouble("CXP_TASA2"));
                              detaPago.setFieldDouble("MPD_TASAIMPUESTO1", this.document.getFieldDouble("CXP_TASA3"));
                              dblImportePago += detaPago.getFieldDouble("MPD_IMPORTE");
                              //Calculamos proporcion del pago
                              if (this.document.getFieldDouble("CXP_TOTAL") > 0) {
                                 double dblPropor = detaPago.getFieldDouble("MPD_IMPORTE") / this.document.getFieldDouble("CXP_TOTAL");
                                 double dblImpuesto1 = this.document.getFieldDouble("CXP_IMPUESTO1") * dblPropor;
                                 double dblImpuesto2 = this.document.getFieldDouble("CXP_IMPUESTO2") * dblPropor;
                                 double dblImpuesto3 = this.document.getFieldDouble("CXP_IMPUESTO3") * dblPropor;
                                 detaPago.setFieldDouble("MPD_IMPUESTO1", dblImpuesto1);
                                 detaPago.setFieldDouble("MPD_IMPUESTO2", dblImpuesto2);
                                 detaPago.setFieldDouble("MPD_IMPUESTO3", dblImpuesto3);
                                 dblImpuestoPago1 += dblImpuesto1;
                                 dblImpuestoPago2 += dblImpuesto2;
                                 dblImpuestoPago3 += dblImpuesto3;
                              }
                              movProvPago.AddDetalle(detaPago);
                           }
                           //Asignamos valores
                           movProvPago.getCta_prov().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
                           movProvPago.getCta_prov().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                           movProvPago.getCta_prov().setFieldInt("MP_ESPAGO", 1);
                           movProvPago.getCta_prov().setFieldInt("CXP_ID", intId);
                           movProvPago.getCta_prov().setFieldInt("MP_TASAPESO", this.document.getFieldInt("CXP_MONEDA"));
                           movProvPago.getCta_prov().setFieldString("MP_FECHA", this.document.getFieldString("CXP_FECHA_PROVISION"));//Ponemos fecha de provision para no afectar cierres anteriores
                           movProvPago.getCta_prov().setFieldDouble("MP_ABONO", dblImportePago);
                           movProvPago.getCta_prov().setFieldDouble("MP_TASAPESO", this.document.getFieldDouble("CXP_TASAPESO"));
                           movProvPago.getCta_prov().setFieldDouble("MP_IMPUESTO1", dblImpuestoPago1);
                           movProvPago.getCta_prov().setFieldDouble("MP_IMPUESTO2", dblImpuestoPago2);
                           movProvPago.getCta_prov().setFieldDouble("MP_IMPUESTO3", dblImpuestoPago3);
                           movProvPago.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", this.document.getFieldDouble("CXP_TASA1"));
                           movProvPago.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", this.document.getFieldDouble("CXP_TASA2"));
                           movProvPago.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", this.document.getFieldDouble("CXP_TASA3"));
                           movProvPago.Init();
                           movProvPago.doTrx();
                           //Validamos que el o los pagos halla pasado
                           if (!movProvPago.getStrResultLast().equals("OK")) {
                              //Fallo algo al aplicar el cargo
                              this.strResultLast = movProv.getStrResultLast();
                           }
                           // </editor-fold>
                        } else {
                           //Fallo algo al aplicar el cargo
                           this.strResultLast = movProv.getStrResultLast();
                        }

                     }
                     // <editor-fold defaultstate="collapsed" desc="Calculos adicionales a la generacion del documento">
                     if (this.strResultLast.equals("OK")) {

                        // <editor-fold defaultstate="collapsed" desc="Calculamos la ganancia global en base al costo de la operacion">
                        //y el importe de ventas
                        strSql = "SELECT sum(CXPD_IMPORTE) as venta,"
                                + "sum(CXPD_COSTO*CXPD_CANTIDAD) as costo "
                                + "FROM vta_cxpagardetalle "
                                + " WHERE vta_cxpagardetalle.CXP_ID = "
                                + " " + this.document.getFieldInt("CXP_ID") + " ";
                        try {
                           ResultSet rs = oConn.runQuery(strSql, true);
                           while (rs.next()) {
                              double dblVenta = rs.getDouble("venta");
                              double dblCosto = rs.getDouble("costo");
                              double dblGanancia = dblVenta - dblCosto;
                              //Actualizamos la ganancia y el costo total
                              String strUpdate = "UPDATE vta_cxpagar set "
                                      + " "
                                      + "CXP_COSTO =  " + dblCosto + " "
                                      + " WHERE " + "CXP_ID = " + this.document.getFieldInt("CXP_ID");
                              oConn.runQueryLMD(strUpdate);
                           }
                           rs.close();
                        } catch (SQLException ex) {
                           this.strResultLast = "ERROR:" + ex.getMessage();
                           ex.fillInStackTrace();
                        }
                        // </editor-fold>

                        // <editor-fold defaultstate="collapsed" desc="Firma Electronica, Contabilidad">
                        if (this.strResultLast.equals("OK")) {

                           // <editor-fold defaultstate="collapsed" desc="Vemos si paso las validaciones para guardar la contabilidad">
                           if (this.strResultLast.equals("OK")) {

                              // <editor-fold defaultstate="collapsed" desc="Guardamos archivos vinculados">
                              try {
                                 String strSeparator = System.getProperty("file.separator");
                                 if (strPathBase2 != null) {
                                    if (strSeparator.equals("\\")) {
                                       strSeparator = "/";
                                       this.strPathBase2 = this.strPathBase2.replace("\\", "/");
                                    }
                                 }
                                 // <editor-fold defaultstate="collapsed" desc="Una vez guardados los archivos los buscamos y asignamos a la CXP">
                                 if (this.strXMLFileName != null) {
                                    // XML 
                                    String fileName = this.strXMLFileName;
                                    String strPathUsado = "";
                                    File saveTo;
                                    if (!fileName.equals("")) {
                                       if (fileName.contains("\\")) {
                                          fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                                       }
                                       if (fileName.contains("/")) {
                                          fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                                       }
                                       strPathUsado = this.strPathBase2 + "document" + strSeparator + "CuentasXPagar" + strSeparator + fileName;
                                       saveTo = new File(strPathUsado);
                                       if (saveTo.exists()) {
                                          CuentasXPagarDoc cxpDoc = new CuentasXPagarDoc();
                                          cxpDoc.getCPR().setFieldString("CPR_DESCRIPCION", fileName);
                                          cxpDoc.getCPR().setFieldString("CPR_PATH", strPathUsado);
                                          cxpDoc.getCPR().setFieldInt("CXP_ID", Integer.valueOf(this.getDocument().getValorKey()));
                                          cxpDoc.getCPR().setFieldDouble("CPR_TAMANIO", saveTo.length());
                                          cxpDoc.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                                          cxpDoc.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                                          cxpDoc.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                                          cxpDoc.saveCXPDoc(oConn);
                                       }

                                    }
                                 }
                                 // </editor-fold>
                                 // <editor-fold defaultstate="collapsed" desc="PDF">
                                 if (this.strPDFFileName != null) {
                                    String fileName = this.strPDFFileName;
                                    if (!fileName.equals("")) {
                                       if (fileName.contains("\\")) {
                                          fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                                       }
                                       if (fileName.contains("/")) {
                                          fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                                       }
                                       String strPathUsado = this.strPathBase2 + "document" + strSeparator + "CuentasXPagar" + strSeparator + fileName;
                                       File saveTo = new File(strPathUsado);
                                       if (saveTo.exists()) {
                                          CuentasXPagarDoc cxpDoc = new CuentasXPagarDoc();
                                          cxpDoc.getCPR().setFieldString("CPR_DESCRIPCION", fileName);
                                          cxpDoc.getCPR().setFieldString("CPR_PATH", strPathUsado);
                                          cxpDoc.getCPR().setFieldInt("CXP_ID", Integer.valueOf(this.getDocument().getValorKey()));
                                          cxpDoc.getCPR().setFieldDouble("CPR_TAMANIO", saveTo.length());
                                          cxpDoc.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                                          cxpDoc.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                                          cxpDoc.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                                          cxpDoc.saveCXPDoc(oConn);
                                       }
                                    }
                                 }
                                 // </editor-fold>
                              } catch (Exception ex) {
                                 log.error(ex.getMessage());
                              }

                              // </editor-fold>
                              // <editor-fold defaultstate="collapsed" desc="Solo procede si la empresa esta configurada">
                              if (intEMP_USECONTA == 1) {
                                 //Objeto para calculo de poliza contable
                                 ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                                 contaUtil.CalculaPolizaContableCXP(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
                                         strCXPGlobal, strCXPCteGlobal, strCXPIVAGlobal,
                                         this.document.getFieldInt("CXP_MONEDA"), document,
                                         this.document.getFieldInt("TI_ID"), intId, "NEW");
                              }
                              // </editor-fold>
                           }
                           // </editor-fold>

                           // <editor-fold defaultstate="collapsed" desc="Formato de impresion codigo de barras">
                           if (bolSendMailMasivo) {
                           }
                           // </editor-fold>

                           // <editor-fold defaultstate="collapsed" desc="Bitacora">
                           this.saveBitacora("CXPAGAR", "NUEVA", intId);
                           // </editor-fold>
                        }
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
                  }
               }
               // </editor-fold>  
            }
            // </editor-fold>
         }
         // </editor-fold>
      }

   }

   /**
    * Genera los items para las partidas contables cuando el detalle de la
    * compras es de acuerdo a los centros de gasto
    *
    * @param poli Es la poliza
    * @param bolEsCargo Es el tipo de movimiento(cargo u abono)
    * @param strFolio Es el folio
    */
   protected void getPoliDetaProd(PolizasContables poli, boolean bolEsCargo, String strFolio) {
      //Lista de los cuentas agrupadas
      ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
      //Asignamos los valores de las partidas
      Iterator<TableMaster> it = this.lstMovs.iterator();
      while (it.hasNext()) {
         TableMaster deta = it.next();
         //Consultamos en la tabla de productos a que cuenta pertenecen...
         String strSql = "select GT_CUENTA_CONTABLE from vta_cgastos where GT_ID = " + deta.getFieldInt("GT_ID");
         ResultSet rs3;
         try {
            rs3 = this.oConn.runQuery(strSql);
            while (rs3.next()) {
               String strPR_CTA_GASTO = rs3.getString("GT_CUENTA_CONTABLE");
               if (strPR_CTA_GASTO.isEmpty()) {
                  strPR_CTA_GASTO = this.strCtaEmpty;
               }
               //Validamos si existe
               boolean bolExiste = false;
               Iterator<PoliCtas> it2 = lstCuentasAG.iterator();
               while (it2.hasNext()) {
                  PoliCtas polDeta = it2.next();
                  if (polDeta.getStrCuenta().equals(strPR_CTA_GASTO)) {
                     bolExiste = true;
                     //Ya existe la cuenta solo sumamos el importe
                     polDeta.setDblImporte(polDeta.getDblImporte() + deta.getFieldDouble("CXPD_IMPORTE"));
                  }
               }
               //Si no existe lo agregamos
               if (!bolExiste) {
                  PoliCtas pol = new PoliCtas();
                  pol.setStrCuenta(strPR_CTA_GASTO);
                  pol.setBolEsCargo(bolEsCargo);
                  pol.setDblImporte(deta.getFieldDouble("CXPD_IMPORTE"));
                  pol.setStrFolioRef(strFolio);
                  lstCuentasAG.add(pol);
               }
            }
            rs3.close();
         } catch (SQLException ex) {
            Logger.getLogger(CuentasxPagar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      //Vaciamos el listado de cuentas en las polizas contables
      poli.setLstCuentasAG(lstCuentasAG);
   }

   @Override
   public void doTrxAnul() {
      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("CXP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("CXP_FECHA_PROVISION"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulada">
         if (this.document.getFieldInt("CXP_ANULADA") == 0) {

            // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cancelacion de cargos">
            String strSql = "SELECT MP_ID from vta_mov_prov "
                    + "where CXP_ID = " + this.document.getFieldInt("CXP_ID")
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
               ex.fillInStackTrace();
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Cancelacion de pagos">
            if (this.strResultLast.equals("OK")) {
               //Cancelar pagos
               strSql = "SELECT MP_ID from vta_mov_prov "
                       + "where CXP_ID = " + this.document.getFieldInt("CXP_ID")
                       + " AND MP_ESPAGO = 1 AND MP_ANULADO = 0 ";
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
                  ex.fillInStackTrace();
               }

            }
            // </editor-fold>

            if (this.strResultLast.equals("OK")) {

               // <editor-fold defaultstate="collapsed" desc="Guardamos el documento principal">
               this.document.setFieldInt("CXP_USUARIOANUL", this.varSesiones.getIntNoUser());
               this.document.setFieldInt("CXP_ANULADO", 1);
               this.document.setFieldString("CXP_HORANUL", this.fecha.getHoraActual());
               if (strFechaAnul.equals("")) {
                  this.document.setFieldString("CXP_ANULFECHA", this.fecha.getFechaActual());
               } else {
                  this.document.setFieldString("CXP_ANULFECHA", strFechaAnul);
               }
               // <editor-fold defaultstate="collapsed" desc="Limpiamos el pedimento">
               this.document.setFieldString("PED_COD", "");
               this.document.setFieldInt("PED_ID", 0);
               // </editor-fold>
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
                  int intEMP_VTA_DETA = 0;
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
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        strEMP_URLCP = rs.getString("EMP_URLCP");
                        intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                     ex.fillInStackTrace();
                  }
                  // </editor-fold>

                  if (this.strResultLast.equals("OK")) {

                     //<editor-fold defaultstate="collapsed" desc="Contabilidad">
                     if (intEMP_USECONTA == 1) {
                        //Objeto para calculo de poliza contable
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        contaUtil.CalculaPolizaContableCXP(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
                                strCtaCXPGlobal, strCtaCXPProvGlobal, strCtaCXPIVAGlobal,
                                this.document.getFieldInt("CXP_MONEDA"), document,
                                this.document.getFieldInt("TI_ID"), this.document.getFieldInt("CXP_ID"), "CANCEL");
                     }
                     // </editor-fold>

                  }

               }

               // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
               this.saveBitacora("CXPAGAR", "ANULAR", this.document.getFieldInt("CXP_ID"));
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
      //Aplica solo cuando se cancela una factura y los tickets se reviven
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("CXP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por REVIVIR";
      }
      //Inicializamos la operacion
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      //Validamos que este anulado
      if (this.document.getFieldInt("CXP_ANULADA") == 1) {
         String strFiltroFactRem = "";
         //Revivir cargos
         String strSql = "SELECT MP_ID from vta_mov_prov "
                 + "where CXP_ID = " + this.document.getFieldInt("CXP_ID")
                 + " AND MP_ESPAGO = 0 AND MP_ANULADO = 1 " + strFiltroFactRem;
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               //Quitamos la aplicacion de los cargos
               MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
               //Definimos el id del del cargo
               movProv.getCta_prov().setFieldInt("MP_ID", rs.getInt("MP_ID"));
               //Inicializamos
               movProv.Init();
               //Definimos valores
               movProv.setBolTransaccionalidad(false);
               movProv.doTrxRevive();
               if (!movProv.getStrResultLast().equals("OK")) {
                  this.strResultLast = movProv.getStrResultLast();
                  break;
               }
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            ex.fillInStackTrace();
         }
         //termina revivir cargos
         if (this.strResultLast.equals("OK")) {
            //Revivir pagos
            strSql = "SELECT MP_ID from vta_mov_prov "
                    + "where CXP_ID = " + this.document.getFieldInt("CXP_ID")
                    + " AND MP_ESPAGO = 1 AND MP_ANULADO = 1 " + strFiltroFactRem;
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  //Quitamos la aplicacion de los cargos
                  MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
                  //Definimos el id del pago
                  movProv.getCta_prov().setFieldInt("MP_ID", rs.getInt("MP_ID"));
                  //Inicializamos
                  movProv.Init();
                  //Definimos valores
                  movProv.setBolTransaccionalidad(false);
                  movProv.doTrxRevive();
                  if (!movProv.getStrResultLast().equals("OK")) {
                     this.strResultLast = movProv.getStrResultLast();
                     break;
                  }
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               ex.fillInStackTrace();
            }
         }
         //Termina revivir de pagos
         if (this.strResultLast.equals("OK")) {
            //Definimos campos
            this.document.setFieldInt("CXP_US_ANUL", 0);
            this.document.setFieldInt("CXP_ANULADA", 0);
            this.document.setFieldString("CXP_HORANUL", "");
            this.document.setFieldString("CXP_FECHAANUL", "");
            String strResp1 = this.document.Modifica(oConn);
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               //Actualizamos la poliza contable (FALTA POR CONCLUIR)
               PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
               poli.setStrOper("REVIVE");
               int intValOper = PolizasContables.CUENTASXPAGAR;
               poli.callRemote(this.document.getFieldInt("CXP_ID"), intValOper);
            }
            //Guardamos la bitacora
            this.saveBitacora("CXPAGAR", "REVIVIR", this.document.getFieldInt("CXP_ID"));
         }
      } else {
         this.strResultLast = "ERROR:LA OPERACION NO ESTA ANULADA";
      }
      //Terminamos la operacion
      if (this.bolTransaccionalidad) {
         if (this.strResultLast.equals("OK")) {
            this.oConn.runQueryLMD("COMMIT");
         } else {
            this.oConn.runQueryLMD("ROLLBACK");
         }
      }
   }

   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxMod() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.document.getFieldInt("CXP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por modificar";
      }
      //Evaluamos que no halla movimientos en cuentas proveedor asociada a esta cuenta por pagar(solo una que es el cargo inicial)
      int intCuantos = 0;
      String strSqlC = "select count(cxp_id) as cuantos from vta_mov_prov where  CXP_ID = " + this.document.getFieldInt("CXP_ID") + "  AND MP_ANULADO = 0";
      try {
         ResultSet rs = oConn.runQuery(strSqlC);
         while (rs.next()) {
            intCuantos = rs.getInt("cuantos");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      if (intCuantos > 1) {
         this.strResultLast = "ERROR: La cuenta por pagar ya tiene movimientos asociados, cancelelos de favor";
      }

      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("CXP_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>
      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.document.getFieldInt("CXP_ANULADA") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Obtenemos datos fiscales">
            int intCT_TIPOPERS = 0;
            int intEMP_USECONTA = 0;
            int intEMP_VTA_DETA = 0;//cuenta de compras se genera a partir de productos
            String strCXPGlobal = "";
            String strCXPIVAGlobal = "";
            String strCXPIVATasa = "";
            String strCXPCteGlobal = "";
            String strCXPVtas = "";
            String strCXPVtasProv = "";
            String strEMP_URLCP = "";
            String strEMP_PASSCP = "";
            String strEMP_USERCP = "";
            String strSql = "SELECT PV_TIPOPERS FROM vta_proveedor "
                    + " where PV_ID=" + this.document.getFieldInt("PV_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intCT_TIPOPERS = rs.getInt("PV_TIPOPERS");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            int intEMP_TIPOPERS = 0;
            strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                    + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTAPROV,"
                    + "EMP_CFD_CFDI,"
                    + "EMP_FIRMA,EMP_URLCP,EMP_PASSCP,EMP_USERCP,EMP_USACODBARR,EMP_VTA_DETA "
                    + "FROM vta_empresas "
                    + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                  intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                  intEMP_VTA_DETA = rs.getInt("EMP_VTA_DETA");
                  strCXPGlobal = rs.getString("EMP_CTAVTA");
                  strCXPIVAGlobal = rs.getString("EMP_CTAIVA");
                  strCXPCteGlobal = rs.getString("EMP_CTAPROV");
                  strEMP_URLCP = rs.getString("EMP_URLCP");
                  strEMP_PASSCP = rs.getString("EMP_PASSCP");
                  strEMP_USERCP = rs.getString("EMP_USERCP");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos documento principal">
            //Quitamos la bandera de autorizacion para que procedan autorizar el cambio
            this.document.setFieldInt("CXP_AUTORIZA", 0);
            //Marcamos que la orden de compra este en estatus 1:ABIERTO
//         this.document.setFieldInt("CXP_STATUS", 1);
            this.document.setFieldInt("CXP_USUARIO_AUTORIZA", 0);
            this.document.setFieldString("CXP_HORA_AUTORIZA", "");
            this.document.setFieldString("CXP_FECHA_AUTORIZA", "");
            //Definimos campos
            this.document.setFieldInt("CXP_US_MOD", this.varSesiones.getIntNoUser());
            this.document.setFieldDouble("CXP_SALDO", 0);
            String strResp1 = this.document.Modifica(oConn);
            // </editor-fold>
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               // <editor-fold defaultstate="collapsed" desc="Borrar el detalle">
               String strNomTable = "vta_cxpagardetalle";
               String strDelete = "delete from " + strNomTable + " where CXP_ID = " + this.document.getFieldInt("CXP_ID");
               oConn.runQueryLMD(strDelete);
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="guardar detalle">
               int intId = Integer.valueOf(this.document.getValorKey());
               Iterator<TableMaster> it = this.lstMovs.iterator();
               while (it.hasNext()) {
                  TableMaster deta = it.next();
                  deta.setFieldInt("CXP_ID", intId);
                  deta.setBolGetAutonumeric(true);
                  String strRes2 = deta.Agrega(oConn);
                  //Validamos si todo fue satisfactorio
                  if (!strRes2.equals("OK")) {
                     this.strResultLast = strRes2;
                     break;
                  }
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Borrar el MOVIMIENTO en mov-prov">
               String strDelMovs = "delete from vta_mov_prov where CXP_ID = " + this.document.getFieldInt("CXP_ID") + "  ";
               oConn.runQueryLMD(strDelMovs);
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Cargo proveedores">
               //Crear objeto de cta vta_mov_prov y aplicarlo
               //Generamos cargo por el importe de la deuda
               MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
               movProv.setBolTransaccionalidad(false);
               //Asignamos valores
               movProv.getCta_prov().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
               movProv.getCta_prov().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
               movProv.getCta_prov().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
               movProv.getCta_prov().setFieldInt("MP_ESPAGO", 0);
               movProv.getCta_prov().setFieldInt("CXP_ID", intId);
               movProv.getCta_prov().setFieldInt("MP_MONEDA", this.document.getFieldInt("CXP_MONEDA"));
               movProv.getCta_prov().setFieldInt("MP_TASAPESO", 1);
               movProv.getCta_prov().setFieldString("MP_FECHA", this.document.getFieldString("CXP_FECHA"));
               movProv.getCta_prov().setFieldString("MP_NOTAS", this.document.getFieldString("CXP_NOTAS"));
               movProv.getCta_prov().setFieldDouble("MP_CARGO", this.document.getFieldDouble("CXP_TOTAL"));
               //Validamos el tipo de comprobante para la parte de honorarios
               if (this.bolEsLocal) {
                  if (intCT_TIPOPERS == 2 && intEMP_TIPOPERS == 0) {
                     movProv.getCta_prov().setFieldDouble("MP_CARGO", this.document.getFieldDouble("CXP_NETO"));
                  }
               } else if (intCT_TIPOPERS == 2 && intEMP_TIPOPERS == 1) {
                  movProv.getCta_prov().setFieldDouble("MP_CARGO", this.document.getFieldDouble("CXP_NETO"));
               }
               movProv.getCta_prov().setFieldDouble("MP_IMPUESTO1", this.document.getFieldDouble("CXP_IMPUESTO1"));
               movProv.getCta_prov().setFieldDouble("MP_IMPUESTO2", this.document.getFieldDouble("CXP_IMPUESTO2"));
               movProv.getCta_prov().setFieldDouble("MP_IMPUESTO3", this.document.getFieldDouble("CXP_IMPUESTO3"));
               movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", this.document.getFieldDouble("CXP_TASA1"));
               movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", this.document.getFieldDouble("CXP_TASA2"));
               movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", this.document.getFieldDouble("CXP_TASA3"));
               movProv.Init();
               movProv.doTrx();
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Actualizamos contabilidad">
               if (this.strResultLast.equals("OK")) {
                  // <editor-fold defaultstate="collapsed" desc="Guardamos archivos vinculados">
                  try {
                     String strSeparator = System.getProperty("file.separator");
                     if (strPathBase2 != null) {
                        if (strSeparator.equals("\\")) {
                           strSeparator = "/";
                           this.strPathBase2 = this.strPathBase2.replace("\\", "/");
                        }
                     }
                     // <editor-fold defaultstate="collapsed" desc="Una vez guardados los archivos los buscamos y asignamos a la CXP">
                     if (this.strXMLFileName != null) {
                        // XML 
                        String fileName = this.strXMLFileName;
                        String strPathUsado = "";
                        File saveTo;
                        if (!fileName.equals("")) {
                           if (fileName.contains("\\")) {
                              fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                           }
                           if (fileName.contains("/")) {
                              fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                           }
                           strPathUsado = this.strPathBase2 + "document" + strSeparator + "CuentasXPagar" + strSeparator + fileName;
                           saveTo = new File(strPathUsado);
                           if (saveTo.exists()) {
                              CuentasXPagarDoc cxpDoc = new CuentasXPagarDoc();
                              cxpDoc.getCPR().setFieldString("CPR_DESCRIPCION", fileName);
                              cxpDoc.getCPR().setFieldString("CPR_PATH", strPathUsado);
                              cxpDoc.getCPR().setFieldInt("CXP_ID", Integer.valueOf(this.getDocument().getValorKey()));
                              cxpDoc.getCPR().setFieldDouble("CPR_TAMANIO", saveTo.length());
                              cxpDoc.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                              cxpDoc.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                              cxpDoc.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                              cxpDoc.saveCXPDoc(oConn);
                           }

                        }
                     }
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="PDF">
                     if (this.strPDFFileName != null) {
                        String fileName = this.strPDFFileName;
                        if (!fileName.equals("")) {
                           if (fileName.contains("\\")) {
                              fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                           }
                           if (fileName.contains("/")) {
                              fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
                           }
                           String strPathUsado = this.strPathBase2 + "document" + strSeparator + "CuentasXPagar" + strSeparator + fileName;
                           File saveTo = new File(strPathUsado);
                           if (saveTo.exists()) {
                              CuentasXPagarDoc cxpDoc = new CuentasXPagarDoc();
                              cxpDoc.getCPR().setFieldString("CPR_DESCRIPCION", fileName);
                              cxpDoc.getCPR().setFieldString("CPR_PATH", strPathUsado);
                              cxpDoc.getCPR().setFieldInt("CXP_ID", Integer.valueOf(this.getDocument().getValorKey()));
                              cxpDoc.getCPR().setFieldDouble("CPR_TAMANIO", saveTo.length());
                              cxpDoc.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                              cxpDoc.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                              cxpDoc.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                              cxpDoc.saveCXPDoc(oConn);
                           }
                        }
                     }
                     // </editor-fold>
                  } catch (Exception ex) {
                     log.error(ex.getMessage());
                  }
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="Solo procede si la empresa esta configurada">
                  if (intEMP_USECONTA == 1) {
                     //Objeto para calculo de poliza contable
                     ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                     contaUtil.CalculaPolizaContableCXP(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
                             strCXPGlobal, strCXPCteGlobal, strCXPIVAGlobal,
                             this.document.getFieldInt("CXP_MONEDA"), document,
                             this.document.getFieldInt("TI_ID"), intId, "NEW");
                  }
                  // </editor-fold>
               }
               // </editor-fold>

            }
            //Guardamos la bitacora
            this.saveBitacora("CXPAGAR", "MODIFICAR", this.document.getFieldInt("CXP_ID"));
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
   }

   /**
    * Valida si tiene acceso el usuario actual a cierto permiso
    *
    * @param intIdPermiso Es el id del permiso
    * @return Regresa true si tiene acceso
    */
   protected boolean ValidaPermisoAutoriza(int intIdPermiso) {
      boolean bolTieneAcceso = false;
      String strSql = "SELECT PS_ID "
              + "FROM perfiles_permisos where "
              + " PS_ID in (" + intIdPermiso + ") AND  PF_ID = " + varSesiones.getIntIdPerfil() + " ";
      ResultSet rsCombo;
      try {
         rsCombo = oConn.runQuery(strSql, true);
         while (rsCombo.next()) {
            bolTieneAcceso = true;
         }
         rsCombo.close();

      } catch (SQLException ex) {
         ex.fillInStackTrace();

      }
      return bolTieneAcceso;
   }

   /**
    * Aplica cuando se autoriza la cuenta por pagar
    */
   public void doAutoriza() {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("CXP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la Cuenta por pagar por autorizar";
      }
      // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      // </editor-fold>
      //Validamos que no este anulado
      if (this.document.getFieldInt("CXP_ANULADA") == 0) {
         //Actualizamos el estatus de la orden de compra como autorizada
         this.document.setFieldInt("CXP_AUTORIZA", 1);
         this.document.setFieldInt("CXP_USUARIO_AUTORIZA", this.varSesiones.getIntNoUser());
         this.document.setFieldString("CXP_HORA_AUTORIZA", this.fecha.getHoraActual());
         this.document.setFieldString("CXP_FECHA_AUTORIZA", this.fecha.getFechaActual());
//         this.document.setFieldInt("CXP_STATUS", 2);
         String strResp1 = this.document.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         } else {
//            String strMailProv1 = "";
//            String strMailProv2 = "";
//            String strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_EMAIL1,PV_EMAIL2,PV_LOCALIDAD,"
//                    + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_CONTAPROV,PV_CONTACOMP,"
//                    + " PV_CP,PV_DIASCREDITO,PV_LPRECIOS,PV_TIPOPERS,PV_ENVIO_MAIL FROM vta_proveedor "
//                    + " where PV_ID=" + this.document.getFieldInt("PV_ID") + "";
//            try {
//               ResultSet rs = oConn.runQuery(strSql, true);
//               while (rs.next()) {
//                  this.document.setFieldString("PV_NOMBRE", rs.getString("PV_RAZONSOCIAL"));
//                  strMailProv1 = rs.getString("PV_EMAIL1");
//                  strMailProv2 = rs.getString("PV_EMAIL2");
//                  bolSendMailMasivo = rs.getBoolean("PV_ENVIO_MAIL");
//               }
//               rs.close();
//            } catch (SQLException ex) {
//               this.strResultLast = "ERROR:" + ex.getMessage();
//               ex.fillInStackTrace();
//            }
//            // <editor-fold defaultstate="collapsed" desc="Envio de orden de compra al proveedor">
//            if (bolSendMailMasivo) {
//               if (this.document.getFieldInt("CXP_ID") != 0) {
//                  this.document.setValorKey(this.document.getFieldInt("CXP_ID") + "");
//               }
////               EnvioMasivo(1, strMailProv1, strMailProv2, true);
//            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Bitacora">
            this.saveBitacora("CXPAGAR", "AUTORIZA", this.document.getFieldInt("CXP_ID"));
            // </editor-fold>
         }
      } else {
         this.strResultLast = "ERROR:La cuenta por pagar ya fue anulada";
      }
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
   // </editor-fold>
}
