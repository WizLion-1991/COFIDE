package ERP;

import Tablas.vta_compra;
import Tablas.vta_compradeta;
import Tablas.vta_movprod;
import Tablas.vta_movproddeta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Mail;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Módulo de negocios para las compras
 *
 * @author zeus
 */
public class Compras extends ProcesoMaster implements ProcesoInterfaz {

// <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected TableMaster document;
   protected ArrayList<TableMaster> lstMovs;
   protected ArrayList<TableMaster> lstMovsRecep;
   protected int intEMP_ID = 0;
   protected boolean bolSendMailMasivo;
   protected String strFechaAnul;
   /**
    * Cuenta contable por default en las partidas
    */
   protected String strCtaEmpty;
   protected boolean bolEsLocal = false;
   protected String strPathXml;
   protected String strPathFonts;
   protected String strNomFormato;
   protected String strFolioAct;
   protected int intSistemaCostos = 0;
   private static final Logger log = LogManager.getLogger(Compras.class.getName());

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

   /**
    * Obtiene el path donde se recuperaran los archivos xml de proveedores y los
    * formatos
    *
    * @return Es una cadena con una ruta a un directorio
    */
   public String getStrPathXml() {
      return strPathXml;
   }

   /**
    * Define el path donde se recuperaran los archivos xml de proveedores y los
    * formatos
    *
    * @param strPathXml Es una cadena con una ruta a un directorio
    */
   public void setStrPathXml(String strPathXml) {
      this.strPathXml = strPathXml;
   }

   /**
    * Obtiene el path para las fuentes a usar en los formatos
    *
    * @return Es una cadena con una ruta a un directorio
    */
   public String getStrPathFonts() {
      return strPathFonts;
   }

   /**
    * Define el path para las fuentes a usar en los formatos
    *
    * @param strPathFonts Es una cadena con una ruta a un directorio
    */
   public void setStrPathFonts(String strPathFonts) {
      this.strPathFonts = strPathFonts;
   }

   /**
    * Regresa el nombre del formato
    *
    * @return
    */
   public String getStrNomFormato() {
      return strNomFormato;
   }

   /**
    * Define el nombre del formato
    *
    * @param strNomFormato
    */
   public void setStrNomFormato(String strNomFormato) {
      this.strNomFormato = strNomFormato;
   }

   /**
    * Regresa el sistema de costo
    *
    * @return Es una constante con el sistema de costos ver clase Inventarios
    */
   public int getIntSistemaCostos() {
      return intSistemaCostos;
   }

   /**
    * Define el sistema de costo
    *
    * @param intSistemaCostos Es una constante con el sistema de costos ver
    * clase Inventarios
    */
   public void setIntSistemaCostos(int intSistemaCostos) {
      this.intSistemaCostos = intSistemaCostos;
   }

   /**
    * Regresa la lista de movimientos para confirmar en la recepcion
    *
    * @return Es un arrayList de objetos vta_cxpagardetalle
    */
   public ArrayList<TableMaster> getLstMovsRecep() {
      if (lstMovsRecep == null) {
         lstMovsRecep = new ArrayList<TableMaster>();
      }
      return lstMovsRecep;
   }

   /**
    * Define la lista de movimientos para confirmar en la recepcion
    *
    * @param lstMovsRecep Es un arrayList de objetos vta_cxpagardetalle
    */
   public void setLstMovsRecep(ArrayList<TableMaster> lstMovsRecep) {
      this.lstMovsRecep = lstMovsRecep;
   }

// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Constructores">
   public Compras(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new vta_compra();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strFechaAnul = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      this.strNomFormato = "COMPRAS";
   }

   public Compras(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new vta_compra();
      this.lstMovs = new ArrayList<TableMaster>();
      this.strFechaAnul = "";
      this.bolSendMailMasivo = true;
      this.strCtaEmpty = "0000";
      this.strNomFormato = "COMPRAS";
   }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      this.document.setFieldInt("COM_USUARIO", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.document.getFieldInt("COM_ID") != 0) {
         this.strFechaAnul = this.document.getFieldString("COM_FECHAANUL");
         this.document.ObtenDatos(this.document.getFieldInt("COM_ID"), oConn);
         this.strFolioAct = this.document.getFieldString("COM_FOLIO");
      }
   }

   @Override
   public void doTrx() {
      // <editor-fold defaultstate="collapsed" desc="Inicializamos valores">
      this.strResultLast = "OK";
      this.document.setFieldString("COM_FECHACREATE", this.fecha.getFechaActual());
      this.document.setFieldString("COM_HORACREATE", this.fecha.getHoraActualHHMMSS());
      //Valores para retencion
      int intCT_TIPOPERS = 0;
      int intEMP_TIPOPERS = 0;
      int intSC_SOBRIMP1_2 = 0;
      int intSC_SOBRIMP1_3 = 0;
      int intSC_SOBRIMP2_3 = 0;
      double dblSC_TASA1 = 0;
      double dblSC_TASA2 = 0;
      double dblSC_TASA3 = 0;
      int intEMP_ACUSEFACTURA = 0;
      boolean bolPermisoAutoriza = false;
      bolPermisoAutoriza = ValidaPermisoAutoriza(150);

      String strMailProv1 = "";
      String strMailProv2 = "";
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Ajustes a la orden de compra">
      this.document.setFieldInt("COM_STATUS", 1);
      //Si tiene activo el permiso de autorizar marcamos autorizado el movimiento
      if (bolPermisoAutoriza) {
         this.document.setFieldInt("COM_AUTORIZA", 1);
         this.document.setFieldInt("COM_USUARIO_AUTORIZA", this.varSesiones.getIntNoUser());
         this.document.setFieldString("COM_HORA_AUTORIZA", this.fecha.getHoraActual());
         this.document.setFieldString("COM_FECHA_AUTORIZA", this.fecha.getFechaActual());
         this.document.setFieldInt("COM_STATUS", 2);
      }

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
      if (this.document.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.document.getFieldString("COM_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.document.getFieldInt("PV_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el proveedor";
      }
      if (this.lstMovs.isEmpty()) {
         this.strResultLast = "ERROR:Debe tener por lo menos un item la compra";
      }
      //Evaluamos la fecha de cierre
//      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("COM_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
//      if (!bolEvalCierre) {
//         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
//      }
      // </editor-fold>

      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Copiar datos del proveedor">
         boolean bolFindProv = false;
         String strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_EMAIL1,PV_EMAIL2,PV_LOCALIDAD,"
                 + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_CONTAPROV,PV_CONTACOMP,"
                 + " PV_CP,PV_DIASCREDITO,PV_LPRECIOS,PV_TIPOPERS,PV_ENVIO_MAIL FROM vta_proveedor "
                 + " where PV_ID=" + this.document.getFieldInt("PV_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               this.document.setFieldString("PV_NOMBRE", rs.getString("PV_RAZONSOCIAL"));
               intCT_TIPOPERS = rs.getInt("PV_TIPOPERS");
               bolFindProv = true;
               strMailProv1 = rs.getString("PV_EMAIL1");
               strMailProv2 = rs.getString("PV_EMAIL2");
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Si el proveedor se encontro">
         if (bolFindProv) {
            // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara la compra">
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
               log.error(ex.getMessage());
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
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_ACUSEFACTURA,"/**
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
                     intEMP_ACUSEFACTURA = rs.getInt("EMP_ACUSEFACTURA");

                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               // </editor-fold>
            } else {
               // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la empresa(WEB)">
               strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,"
                       + "EMP_NOMKEY,EMP_NOMCERT,EMP_TIPOPERS,EMP_AVISOFOLIO,EMP_ACUSEFACTURA,"
                       + "EMP_CFD_CFDI,"
                       + "EMP_FIRMA,EMP_PASSCP,EMP_USERCP,EMP_USACODBARR,EMP_VTA_DETA "
                       + "FROM vta_empresas "
                       + "WHERE EMP_ID = " + this.document.getFieldInt("EMP_ID") + "";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEMP_TIPOPERS = rs.getInt("EMP_TIPOPERS");
                     intEMP_ACUSEFACTURA = rs.getInt("EMP_ACUSEFACTURA");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               // </editor-fold>
            }
            oConn.setBolMostrarQuerys(bolTmp);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calcular el importe del descuento total">
            double dblDescuentoTotal = 0;
            Iterator<TableMaster> itG = this.lstMovs.iterator();
            while (itG.hasNext()) {
               TableMaster deta = itG.next();
               dblDescuentoTotal += deta.getFieldDouble("COMD_DESCUENTO");
            }
            this.document.setFieldDouble("COM_DESCUENTO1", dblDescuentoTotal);
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
                  int intTipoFolio = Folios.OCOMPRA;
                  String strFolio = "";
                  folio.setBolEsLocal(this.bolEsLocal);
//                  if (this.document.getFieldString("COM_FOLIO").equals("")) {
                  strFolio = folio.doFolio(oConn, intTipoFolio, this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
                  this.document.setFieldString("COM_FOLIO", strFolio);
//                  } else {
//                     strFolio = this.document.getFieldString("COM_FOLIO");
//                     folio.updateFolio(oConn, intTipoFolio, this.document.getFieldString("COM_FOLIO"),
//                             this.bolFolioGlobal, this.document.getFieldInt("EMP_ID"));
//                  }
                  // </editor-fold>
               }
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Si todo esta ok guardamos el documento">
               if (this.strResultLast.equals("OK")) {
                  this.document.setBolGetAutonumeric(true);
                  String strRes1 = this.document.Agrega(oConn);
                  if (!strRes1.equals("OK")) {
                     this.strResultLast = strRes1;
                  }
               }
               // </editor-fold>

               //Proseguimos si todo esta bien
               if (this.strResultLast.equals("OK")) {

                  // <editor-fold defaultstate="collapsed" desc="Guardar detalle">
                  int intId = Integer.valueOf(this.document.getValorKey());
                  Iterator<TableMaster> it = this.lstMovs.iterator();
                  while (it.hasNext()) {
                     TableMaster deta = it.next();
                     deta.setFieldInt("COM_ID", intId);
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
                     // <editor-fold defaultstate="collapsed" desc="Formato de impresion codigo de barras">
                     if (bolSendMailMasivo) {
                        EnvioMasivo(intEMP_ACUSEFACTURA, strMailProv1, strMailProv2, false);
                     }
                     // </editor-fold>

                     // <editor-fold defaultstate="collapsed" desc="Bitacora">
                     this.saveBitacora("COMPRAS", "NUEVA", intId);

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
         }
         // </editor-fold>

      }
   }

   @Override
   public void doTrxAnul() {
      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("COM_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
//      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("COM_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
//      if (!bolEvalCierre) {
//         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
//      }

      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulada">
         if (this.document.getFieldInt("COM_ANULADA") == 0) {

            // <editor-fold defaultstate="collapsed" desc="Obtenemos cuantas recepciones hay">
            int intCuantasRecepciones = 0;
            String strSql = "select count(*) as cuantos from vta_movprod where OC_ID = " + document.getFieldInt("COM_ID");
            ResultSet rs;
            try {
               rs = oConn.runQuery(strSql);
               while (rs.next()) {
                  intCuantasRecepciones = rs.getInt("cuantos");
               }
               rs.close();
            } catch (SQLException ex) {
               log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Si no hay recepciones continuamos">
            if (intCuantasRecepciones == 0) {
               //Marcamos el estatus
               this.document.setFieldInt("COM_STATUS", 7);
               // <editor-fold defaultstate="collapsed" desc="Iniciamos transaccion">
               if (this.bolTransaccionalidad) {
                  this.oConn.runQueryLMD("BEGIN");
               }
               // </editor-fold>

               if (this.strResultLast.equals("OK")) {
                  // <editor-fold defaultstate="collapsed" desc="Guardamos el documento principal">
                  this.document.setFieldInt("COM_US_ANUL", this.varSesiones.getIntNoUser());
                  this.document.setFieldInt("COM_ANULADA", 1);
                  this.document.setFieldString("COM_HORAANUL", this.fecha.getHoraActual());
                  if (strFechaAnul.equals("")) {
                     this.document.setFieldString("COM_FECHAANUL", this.fecha.getFechaActual());
                  } else {
                     this.document.setFieldString("COM_FECHAANUL", strFechaAnul);
                  }
                  String strResp1 = this.document.Modifica(oConn);
                  // </editor-fold>

                  if (!strResp1.equals("OK")) {
                     this.strResultLast = strResp1;
                  } else {
                  }
               }

               // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
               this.saveBitacora("COMPRAS", "ANULAR", this.document.getFieldInt("CXP_ID"));
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
            } else {
               this.strResultLast = "ERROR:La Orden de Compra ya tiene recepciones";
            }
            // </editor-fold>

         } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
         }
         // </editor-fold>
      }

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
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validaciones">
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("COM_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la Orden de Compra por modificar";
      }

      //Evaluamos si el folio esta cambiando
      if (!this.strFolioAct.equals(this.document.getFieldString("COM_FOLIO"))) {
         this.strResultLast = "ERROR:No se puede modificar el folio de la orden de compra.";
      }
      //Evaluamos la fecha de cierre
//      boolean bolEvalCierre = evaluaFechaCierre(this.document.getFieldString("COM_FECHA"), this.document.getFieldInt("EMP_ID"), this.document.getFieldInt("SC_ID"));
//      if (!bolEvalCierre) {
//         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
//      }
      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.document.getFieldInt("COM_ANULADA") == 0) {

            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Guardamos el documento principal">
            this.document.setFieldInt("COM_AUTORIZA", 0);
            //Marcamos que la orden de compra este en estatus 1:ABIERTO
            this.document.setFieldInt("COM_STATUS", 1);
            this.document.setFieldInt("COM_USUARIO_AUTORIZA", 0);
            this.document.setFieldString("COM_HORA_AUTORIZA", "");
            this.document.setFieldString("COM_FECHA_AUTORIZA", "");
            //Definimos campos
            this.document.setFieldInt("COM_US_MOD", this.varSesiones.getIntNoUser());
            String strResp1 = this.document.Modifica(oConn);
            // </editor-fold>
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               // <editor-fold defaultstate="collapsed" desc="Borrar el detalle">
               String strNomTable = "vta_compradeta";
               String strDelete = "delete from " + strNomTable + " where COM_ID = " + this.document.getFieldInt("COM_ID");
               oConn.runQueryLMD(strDelete);
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="guardar detalle">
               int intId = Integer.valueOf(this.document.getValorKey());
               Iterator<TableMaster> it = this.lstMovs.iterator();
               while (it.hasNext()) {
                  TableMaster deta = it.next();
                  deta.setFieldInt("COM_ID", intId);
                  deta.setBolGetAutonumeric(true);
                  String strRes2 = deta.Agrega(oConn);
                  //Validamos si todo fue satisfactorio
                  if (!strRes2.equals("OK")) {
                     this.strResultLast = strRes2;
                     break;
                  }
               }
               // </editor-fold>
            }
            //Guardamos la bitacora
            this.saveBitacora("COMPRAS", "MODIFICAR", this.document.getFieldInt("COM_ID"));
            // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
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
    * Aplica cuando se autoriza la orden de compra
    */
   public void doAutoriza() {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.document.getFieldInt("COM_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la Orden de Compra por autorizar";
      }
      // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      // </editor-fold>
      //Validamos que no este anulado
      if (this.document.getFieldInt("COM_ANULADA") == 0) {
         //Actualizamos el estatus de la orden de compra como autorizada
         this.document.setFieldInt("COM_AUTORIZA", 1);
         this.document.setFieldInt("COM_USUARIO_AUTORIZA", this.varSesiones.getIntNoUser());
         this.document.setFieldString("COM_HORA_AUTORIZA", this.fecha.getHoraActual());
         this.document.setFieldString("COM_FECHA_AUTORIZA", this.fecha.getFechaActual());
         this.document.setFieldInt("COM_STATUS", 2);
         String strResp1 = this.document.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         } else {
            String strMailProv1 = "";
            String strMailProv2 = "";
            String strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_EMAIL1,PV_EMAIL2,PV_LOCALIDAD,"
                    + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_CONTAPROV,PV_CONTACOMP,"
                    + " PV_CP,PV_DIASCREDITO,PV_LPRECIOS,PV_TIPOPERS,PV_ENVIO_MAIL FROM vta_proveedor "
                    + " where PV_ID=" + this.document.getFieldInt("PV_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  this.document.setFieldString("PV_NOMBRE", rs.getString("PV_RAZONSOCIAL"));
                  strMailProv1 = rs.getString("PV_EMAIL1");
                  strMailProv2 = rs.getString("PV_EMAIL2");
                  bolSendMailMasivo = rs.getBoolean("PV_ENVIO_MAIL");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            // <editor-fold defaultstate="collapsed" desc="Envio de orden de compra al proveedor">
            if (bolSendMailMasivo) {
               if (this.document.getFieldInt("COM_ID") != 0) {
                  this.document.setValorKey(this.document.getFieldInt("COM_ID") + "");
               }
               EnvioMasivo(1, strMailProv1, strMailProv2, true);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Bitacora">
            this.saveBitacora("COMPRAS", "AUTORIZA", this.document.getFieldInt("COM_ID"));
            // </editor-fold>
         }
      } else {
         this.strResultLast = "ERROR:La operacion ya fue anulada";
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

   /**
    * Registra la recepción de la orden de compra afectando inventarios y
    * cuentas por pagar
    *
    * @param strFechaRecepcion Es la fecha de recepción
    * @param strNotas Son las notas
    * @param strAduana Es la aduana
    * @param strNumLote Es el numero de lote
    */
   public void doRecepcion(String strFechaRecepcion, String strNotas, String strAduana, String strNumLote) {
      this.strResultLast = "OK";

      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.document.getFieldInt("COM_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la Orden de Compra por recibir";
      }
      //Validamos que hallan registrado entradas
      boolean bolSurtio = false;
      Iterator<TableMaster> it = this.lstMovs.iterator();
      while (it.hasNext()) {
         TableMaster tbn = it.next();
         if (tbn.getFieldDouble("COMD_CANTIDADSURTIDA") > 0) {
            bolSurtio = true;
         }
      }
      //si no surtio nada marcamos error
      if (!bolSurtio) {
         this.strResultLast = "ERROR:Necesita recibir mercancia";
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      // </editor-fold>
      //Validamos que no este anulado
      if (this.document.getFieldInt("COM_ANULADA") == 0) {
         //Realizamos la entrada al almacen
         Inventario inv = new Inventario(oConn, varSesiones, request);
         //Inicializamos
         inv.Init();
         inv.setBolControlEstrictoInv(true);
         //Definimos valores
         inv.setBolTransaccionalidad(false);
         inv.setIntTipoOperacion(Inventario.ENTRADA);
         inv.setIntTipoCosteo(this.intSistemaCostos);//SistemaCostos
         inv.getMovProd().setFieldInt("TIN_ID", Inventario.ENTRADA);
         //Asignamos los valores master
         inv.getMovProd().setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
         inv.getMovProd().setFieldInt("PV_ID", this.document.getFieldInt("PV_ID"));
         inv.getMovProd().setFieldInt("OC_ID", this.document.getFieldInt("COM_ID"));
         inv.getMovProd().setFieldInt("EMP_ID", this.document.getFieldInt("EMP_ID"));
         inv.getMovProd().setFieldInt("MON_ID", this.document.getFieldInt("MON_ID"));
         inv.getMovProd().setFieldString("MP_FECHA", strFechaRecepcion);
         inv.getMovProd().setFieldString("MP_NOTAS", strNotas);
         inv.getMovProd().setFieldString("MP_ORIGENLOTE", strAduana);

         //Asignamos los valores de las partidas
         it = this.lstMovs.iterator();
         while (it.hasNext()) {
            TableMaster deta = it.next();
            if (deta.getFieldDouble("COMD_CANTIDADSURTIDA") > 0) {
               //Validamos si maneja numeros de serie
               if (deta.getFieldString("COMD_USASERIE").equals("1")) {
                  String[] lstSeries = deta.getFieldString("COMD_SERIES").split(",");
                  //Recorremos cada numero de serie para ingresarlo
                  for (String lstSerie : lstSeries) {
                     //Copiamos valores del ticket
                     vta_movproddeta detaInv = new vta_movproddeta();
                     detaInv.setBolUsaSeries(true);
                     detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                     detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                     detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("COMD_ID"));
                     detaInv.setFieldDouble("MPD_ENTRADAS", 1);
                     //Validamos si aplica un factor para la conversion
                     if (deta.getFieldDouble("COMD_FACTOR") != 0) {
                        detaInv.setFieldDouble("MPD_ENTRADAS", 1 / deta.getFieldDouble("COMD_FACTOR"));
                     }
                     detaInv.setFieldDouble("MPD_COSTO", deta.getFieldDouble("COMD_COSTO"));
                     detaInv.setFieldString("PR_CODIGO", deta.getFieldString("COMD_CVE"));
                     detaInv.setFieldString("PL_NUMLOTE", lstSerie.trim());
                     detaInv.setFieldString("MPD_FECHA", strFechaRecepcion);

                     detaInv.setFieldString("MPD_CADFECHA", deta.getFieldString("COMD_FECHACAD"));
                     inv.AddDetalle(detaInv);
                  }
               } else {
                  //Copiamos valores del ticket
                  vta_movproddeta detaInv = new vta_movproddeta();
                  detaInv.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
                  detaInv.setFieldInt("SC_ID", this.document.getFieldInt("SC_ID"));
                  detaInv.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("COMD_ID"));
                  detaInv.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble("COMD_CANTIDADSURTIDA"));
                  //Validamos si aplica un factor para la conversion
                  if (deta.getFieldDouble("COMD_FACTOR") != 0) {
                     detaInv.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble("COMD_CANTIDADSURTIDA") / deta.getFieldDouble("COMD_FACTOR"));
                  }
                  detaInv.setFieldDouble("MPD_COSTO", deta.getFieldDouble("COMD_COSTO"));
                  detaInv.setFieldString("PR_CODIGO", deta.getFieldString("COMD_CVE"));
                  detaInv.setFieldString("PL_NUMLOTE", strNumLote);
                  detaInv.setFieldString("MPD_FECHA", strFechaRecepcion);

                  detaInv.setFieldString("MPD_CADFECHA", deta.getFieldString("COMD_FECHACAD"));
                  inv.AddDetalle(detaInv);
               }

            }
         }
         //Generamos la operacion de inventarios
         inv.doTrx();
         if (!inv.getStrResultLast().equals("OK")) {
            //Fallo algo al guardar el movimiento
            this.strResultLast = inv.getStrResultLast();
         } else {
            //Actualizamos las cantidades recibidas por cada item
            it = this.lstMovs.iterator();
            while (it.hasNext()) {
               TableMaster deta = it.next();
               //Obtenemos la cantidad y el id
               double dblRecibida = deta.getFieldDouble("COMD_CANTIDADSURTIDA");
               int intIdDeta = deta.getFieldInt("COMD_ID");
               //Recuperamos objeto
               vta_compradeta detaTmp = new vta_compradeta();
               detaTmp.ObtenDatos(intIdDeta, oConn);
               //Actualizamos
               detaTmp.setFieldDouble("COMD_CANTIDADSURTIDA", dblRecibida + detaTmp.getFieldDouble("COMD_CANTIDADSURTIDA"));
               detaTmp.Modifica(oConn);
            }
            //Recalculamos los importes de la cuenta por pagar en base a las cantidades recibidas
            //Realizamos la cuenta por pagar
            //CuentasxPagar cuentasxPagar = new CuentasxPagar(oConn, varSesiones, request);
            //Actualizamos la bandera de la recepcion de la orden de compra
            vta_compradeta detaTmp2 = new vta_compradeta();
            ArrayList<TableMaster> lstDetalle = detaTmp2.ObtenDatosVarios(" COM_ID = " + this.document.getFieldInt("COM_ID"), oConn);
            Iterator<TableMaster> it2 = lstDetalle.iterator();
            double dblSumFaltantes = 0;
            while (it2.hasNext()) {
               TableMaster tbn = it2.next();
               double dblFaltante = tbn.getFieldDouble("COMD_CANTIDAD") - tbn.getFieldDouble("COMD_CANTIDADSURTIDA");
               if (dblFaltante < 0) {
                  dblFaltante = 0;
               }
               dblSumFaltantes += dblFaltante;
            }
            //Si no hay faltantes marcamos la orden de compra como surtida
            if (dblSumFaltantes == 0) {
               this.document.setFieldInt("COM_SURTIDA", 1);
               this.document.Modifica(oConn);
               //Marcamos el estatus como el 3:RECIBIDA
               this.document.setFieldInt("COM_STATUS", 3);
            } else {
               //Marcamos el estatus como el 4:RECIBIDA PARCIALMENTE
               this.document.setFieldInt("COM_STATUS", 4);
            }
            String strResp1 = this.document.Modifica(oConn);
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               //Enviamos el id de la entrada para poderlo imprimir
               this.strResultLast = "OK." + inv.getMovProd().getValorKey();
               // <editor-fold defaultstate="collapsed" desc="Bitacora">
               this.saveBitacora("COMPRAS", "RECEPCION", this.document.getFieldInt("COM_ID"));
               // </editor-fold>
            }

         }

      } else {
         this.strResultLast = "ERROR:La operacion ya fue anulada";
      }
      // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
      if (this.bolTransaccionalidad) {
         if (this.strResultLast.startsWith("OK")) {
            this.oConn.runQueryLMD("COMMIT");
         } else {
            this.oConn.runQueryLMD("ROLLBACK");
         }
      }
      // </editor-fold>
   }

   /**
    * Registra la recepción de la orden de compra afectando inventarios y
    * cuentas por pagar
    *
    * @param strLstCOM_ID Es la lista de las ordenes de compra recibidas
    * @param strFechaRecepcion Es la fecha de la cuenta por pagar
    * @param strFechaConfirma Es la fecha de recepción
    * @param strPedimento Es el pedimento
    * @param strFolioFactura Es el folio de la factura del proveedor
    * @param intIdPedimento Es el id del pedimento
    * @param dblImporte Es el importe antes de iva
    * @param dblImpuesto1 Es el iva
    * @param dblImpuesto2 Es el impuesto 2
    * @param dblImpuesto3 Es el impuesto 3
    * @param dblTotal Es el total
    * @param dblIEPS Es el total de IEPS
    * @param dblDescuento Es el descuento global
    * @param strCXP_UUID Es el UUID de la cuenta por pagar
    */
   public void doConfirmacion(String strLstCOM_ID, String strFechaRecepcion, String strFechaConfirma,
           String strPedimento, String strFolioFactura, int intIdPedimento,
           double dblImporte, double dblImpuesto1, double dblImpuesto2, double dblImpuesto3, double dblTotal,
           double dblIEPS, double dblDescuento, String strCXP_UUID) {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      //Parseamos la lista de ordenes de compra
      String[] arrLstCompIds = strLstCOM_ID.split(",");
      ArrayList<vta_compra> lstCompras = new ArrayList<vta_compra>();
      for (String arrLstCompId : arrLstCompIds) {
         int intIdCompra = 0;
         log.debug("ODC:" + arrLstCompId);
         try {
            intIdCompra = Integer.valueOf(arrLstCompId);
            if (intIdCompra != 0) {
               vta_compra objCompra = new vta_compra();
               objCompra.ObtenDatos(intIdCompra, oConn);
               lstCompras.add(objCompra);
               //Evaluamos que no esten anuladas
               if (objCompra.getFieldInt("COM_ANULADA") == 1) {
                  this.strResultLast = "ERROR:La orden de compra ya esta anulada";
               }
            }
         } catch (NumberFormatException ex) {
            log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
      }
      if (lstCompras.isEmpty()) {
         this.strResultLast = "ERROR:Falta definir la Orden de Compra por recibir";
      }
      //Validamos que hallan registrado items
      boolean bolSurtio = true;
      if (lstMovsRecep.isEmpty()) {
         bolSurtio = false;
      }
      //si no surtio nada marcamos error
      if (!bolSurtio) {
         this.strResultLast = "ERROR:Necesita recibir mercancia";
      }
      // </editor-fold>
      //Preguntamos si paso todas las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Recalculamos los importes de la cuenta por pagar en base a las cantidades recibidas">
         //Obtenemos la lista de folios de orden de compra
         StringBuilder strlstFolios = new StringBuilder();
         Iterator<vta_compra> itCompras = lstCompras.iterator();
         while (itCompras.hasNext()) {
            vta_compra objCompra = itCompras.next();
            strlstFolios.append(objCompra.getFieldString("COM_FOLIO")).append(" ");
         }
         //Realizamos la cuenta por pagar
         CuentasxPagar cuenta = new CuentasxPagar(oConn, varSesiones, request);
         cuenta.setBolTransaccionalidad(false);
         //Llenamos con informacion de la compra
         cuenta.getDocument().setFieldInt("SC_ID", lstCompras.get(0).getFieldInt("SC_ID"));
         cuenta.getDocument().setFieldInt("PV_ID", lstCompras.get(0).getFieldInt("PV_ID"));
         log.debug("Moneda:" + lstCompras.get(0).getFieldInt("MON_ID"));
         cuenta.getDocument().setFieldInt("CXP_MONEDA", lstCompras.get(0).getFieldInt("MON_ID"));
         cuenta.getDocument().setFieldInt("PED_ID", intIdPedimento);
         cuenta.getDocument().setFieldInt("ODC_ID", lstCompras.get(0).getFieldInt("COM_ID"));
         cuenta.getDocument().setFieldString("PED_COD", strPedimento);
         cuenta.getDocument().setFieldString("CXP_FECHA", strFechaRecepcion);
         cuenta.getDocument().setFieldString("CXP_FECHA_CONFIRMA", strFechaConfirma);
         cuenta.getDocument().setFieldString("CXP_FECHA_PROVISION", strFechaConfirma);
         cuenta.getDocument().setFieldString("CXP_FOLIO", strFolioFactura);
         cuenta.getDocument().setFieldString("CXP_NOTAS", "Recepcion de ODC " + strlstFolios.toString());
         cuenta.getDocument().setFieldString("CXP_REFERENCIA", strlstFolios.toString());
         cuenta.getDocument().setFieldDouble("CXP_IMPORTE", dblImporte);
         cuenta.getDocument().setFieldDouble("CXP_IMPUESTO1", dblImpuesto1);
         cuenta.getDocument().setFieldDouble("CXP_IMPUESTO2", dblImpuesto2);
         cuenta.getDocument().setFieldDouble("CXP_IMPUESTO3", dblImpuesto3);
         cuenta.getDocument().setFieldDouble("CXP_TOTAL", dblTotal);
         cuenta.getDocument().setFieldDouble("CXP_TASA1", lstCompras.get(0).getFieldDouble("COM_TASA1"));
         cuenta.getDocument().setFieldDouble("CXP_TASA2", lstCompras.get(0).getFieldDouble("COM_TASA2"));
         cuenta.getDocument().setFieldDouble("CXP_TASA3", lstCompras.get(0).getFieldDouble("COM_TASA3"));
         cuenta.getDocument().setFieldDouble("CXP_RETISR", 0);
         cuenta.getDocument().setFieldDouble("CXP_RETIVA", 0);
         cuenta.getDocument().setFieldDouble("CXP_NETO", 0);
         cuenta.getDocument().setFieldDouble("CXP_IMPORTE_DESCUENTO", dblDescuento);
         cuenta.getDocument().setFieldInt("CXP_USO_IEPS", lstCompras.get(0).getFieldInt("COM_USO_IEPS"));
         cuenta.getDocument().setFieldInt("CXP_TASA_IEPS", lstCompras.get(0).getFieldInt("COM_TASA_IEPS"));
         cuenta.getDocument().setFieldDouble("CXP_IMPORTE_IEPS", dblIEPS);
         cuenta.getDocument().setFieldInt("CXP_DIASCREDITO", lstCompras.get(0).getFieldInt("COM_DIASCREDITO"));
         cuenta.getDocument().setFieldString("CXP_NUMPEDI", strPedimento);
         cuenta.getDocument().setFieldString("CXP_ADUANA", "");
         cuenta.getDocument().setFieldString("CXP_FECHAPEDI", "");
         cuenta.getDocument().setFieldString("CXP_UUID", strCXP_UUID);
         //Tarifas de IVA
         int intTI_ID = 0;
         int intTI_ID2 = 0;
         int intTI_ID3 = 0;
         try {
            intTI_ID = lstCompras.get(0).getFieldInt("TI_ID");
            intTI_ID2 = lstCompras.get(0).getFieldInt("TI_ID2");
            intTI_ID3 = lstCompras.get(0).getFieldInt("TI_ID3");
         } catch (NumberFormatException ex) {
            log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
         cuenta.getDocument().setFieldInt("TI_ID", intTI_ID);
         cuenta.getDocument().setFieldInt("TI_ID2", intTI_ID2);
         cuenta.getDocument().setFieldInt("TI_ID3", intTI_ID3);
         //Obtenemos el detalle
         Iterator<TableMaster> it2 = this.lstMovsRecep.iterator();
         while (it2.hasNext()) {
            TableMaster deta = it2.next();
            deta.setFieldDouble("CXPD_TASAIVA1", lstCompras.get(0).getFieldDouble("COM_TASA1"));
            deta.setFieldDouble("CXPD_TASAIVA2", lstCompras.get(0).getFieldDouble("COM_TASA2"));
            deta.setFieldDouble("CXPD_TASAIVA3", lstCompras.get(0).getFieldDouble("COM_TASA3"));
            cuenta.AddDetalle(deta);
         }
         // </editor-fold>
         //Inicializamos objeto
         cuenta.Init();
         //Generamos transaccion
         cuenta.doTrx();
         //Validamos el resultado de la operacion
         if (cuenta.getStrResultLast().startsWith("OK")) {
            //Lista de Id's de inventario
            ArrayList<Integer> lstInv = new ArrayList<Integer>();
            // <editor-fold defaultstate="collapsed" desc="Realizamos la sumatoria de la cantidad a prorratear si existe">
            double dblProrratearTot = 0;
            double dblTotMov = 0;

            it2 = this.lstMovsRecep.iterator();
            while (it2.hasNext()) {
               TableMaster deta = it2.next();
               int intCXPD_PRORRATEO = deta.getFieldInt("CXPD_PRORRATEO");
               //Si se prorratea lo sumamos
               if (intCXPD_PRORRATEO == 1) {
                  dblProrratearTot += deta.getFieldDouble("CXPD_IMPORTE");
               } else {
                  dblTotMov += deta.getFieldDouble("CXPD_IMPORTE");
               }
            }
            //Solo si hay piezas en donde vaciar el prorrateo
            log.debug("dblTotMov: " + dblTotMov);
            log.debug("dblProrratearTot: " + dblProrratearTot);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Actualizamos la bandera de la confirmacion en la entrada de inventario">
            it2 = this.lstMovsRecep.iterator();
            while (it2.hasNext()) {
               TableMaster deta = it2.next();
               int intMPD_ID = deta.getFieldInt("MPD_ID");
               //Solo aplica con movimientos de la orden de compra
               //Los importes por gastos no aplican
               if (intMPD_ID != 0) {
                  //Cargamos movimiento de inventario
                  vta_movproddeta detProd = new vta_movproddeta();
                  detProd.ObtenDatos(intMPD_ID, oConn);
                  double dblRecibidaTot = detProd.getFieldDouble("MPD_CANT_CONF") + deta.getFieldDouble("CXPD_CANTIDAD");
                  detProd.setFieldDouble("MPD_CANT_CONF", dblRecibidaTot);//Asignamos el total recibido
                  detProd.setFieldInt("CXP_ID", Integer.valueOf(cuenta.getDocument().getValorKey()));//Asignamos el id de la cuenta por pagar

                  //Si hay un importe por prorratear lo aplicamos
                  boolean bolAplicoProrrateo = false;
                  if (dblProrratearTot != 0) {
                     if (deta.getFieldDouble("CXPD_CANTIDAD") > 0) {
                        bolAplicoProrrateo = true;
                        double dblFactorProrrateo = 0;
                        dblFactorProrrateo = deta.getFieldDouble("CXPD_IMPORTE") / dblTotMov;
                        log.debug("dblFactorProrrateo: " + dblFactorProrrateo);
                        double dblImporteProrrateo = dblProrratearTot * dblFactorProrrateo;
                        log.debug("dblImporteProrrateo: " + dblImporteProrrateo);
                        dblImporteProrrateo = dblImporteProrrateo / deta.getFieldDouble("CXPD_CANTIDAD");
                        log.debug("dblImporteProrrateo: " + dblImporteProrrateo);
                        detProd.setFieldDouble("MPD_COSTO_PRORRATEO_CXP", detProd.getFieldDouble("MPD_COSTO"));
                        detProd.setFieldDouble("MPD_PROPOR_PRORRATEO_CXP", dblFactorProrrateo);
                        double dblNuevoCosto = detProd.getFieldDouble("MPD_COSTO") + dblImporteProrrateo;
                        detProd.setFieldDouble("MPD_COSTO", dblNuevoCosto);
                     } else {
                        log.debug("la cantidad de la cxp es cero");
                     }
                  }
                  detProd.Modifica(oConn);
                  //Si se aplico prorrateo actualizar el lote
                  if (bolAplicoProrrateo) {
                     //Obtenemos el lote del movimiento
                     String strSql = "select PL_ID,PL_COSTO from vta_prodlote "
                             + " where PR_ID =  " + detProd.getFieldInt("PR_ID")
                             + " AND PL_NUMLOTE='" + detProd.getFieldString("PL_NUMLOTE") + "'";
                     try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                           int intPL_ID = rs.getInt("PL_ID");
                           //Actualizamos el lote
                           String strUpdate = "UPDATE vta_prodlote "
                                   + " set PL_COSTO = " + detProd.getFieldDouble("MPD_COSTO") + " "
                                   + " where PL_ID = '" + intPL_ID + "'";
                           this.oConn.runQueryLMD(strUpdate);
                        }
                        rs.close();

                        //Actualizamos cualquier otro movimiento con este lote
                        String strUpdate = "UPDATE vta_movproddeta "
                                + " set MPD_COSTO = " + detProd.getFieldDouble("MPD_COSTO") + " "
                                + " where PR_ID = " + detProd.getFieldInt("PR_ID") + " AND"
                                + " PL_NUMLOTE='" + detProd.getFieldString("PL_NUMLOTE") + "'";
                        this.oConn.runQueryLMD(strUpdate);
                     } catch (SQLException ex) {
                        log.error(" " + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + ex.getSQLState());
                     }
                  }

                  //Validamos el cabezal de la entrada
                  int intMP_ID = detProd.getFieldInt("MP_ID");
                  if (intMP_ID != 0) {
                     //Llenamos lista de entradas
                     if (!lstInv.contains(intMP_ID)) {
                        lstInv.add(intMP_ID);
                     }
                  }
               }

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Validamos la lista de entradas para verificar cuales ya fueron recibidas por completo">
            Iterator<Integer> it3 = lstInv.iterator();
            while (it3.hasNext()) {
               int intMP_ID = it3.next();
               vta_movprod modProd = new vta_movprod();
               modProd.ObtenDatos(intMP_ID, oConn);
               //Obtenemos todos sus elementos para verificar si estan confirmados todos
               vta_movproddeta detProdtmp = new vta_movproddeta();
               ArrayList<TableMaster> lstDetalles = detProdtmp.ObtenDatosVarios(" MP_ID = " + intMP_ID, oConn);
               //Verificamos si ya fue confirmada en su totalidad la recepcion
               Iterator<TableMaster> itM = lstDetalles.iterator();
               boolean bolRecibioTodo = true;//Indica si la recepcion ha sido completa para marcar el movimiento
               while (itM.hasNext()) {
                  TableMaster tbn = itM.next();
                  if (tbn.getFieldDouble("MPD_CANT_CONF") < tbn.getFieldDouble("MPD_ENTRADAS")) {
                     bolRecibioTodo = false;
                  }
               }
               //Si todo se recibio marcamos la entrada
               if (bolRecibioTodo) {
                  modProd.setFieldInt("CXP_ID", Integer.valueOf(cuenta.getDocument().getValorKey()));
                  modProd.setFieldInt("MP_CONF_RECEP", 1);
                  modProd.Modifica(oConn);
               }
            }
            // </editor-fold>
            //Enviamos el id de la entrada para poderlo imprimir
            this.strResultLast = "OK." + cuenta.getDocument().getValorKey();
         } else {
            //Hubo un error regresamos la respuesta
            this.strResultLast = cuenta.getStrResultLast();
         }
         // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.startsWith("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
         // </editor-fold>
      }

   }

   /**
    * Cierra una orden de compra aunque no se halla recibido toda
    */
   public void doCerrar() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Inicializamos la transaccionalidad">
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      // </editor-fold>
      //Validamos que no este anulado
      if (this.document.getFieldInt("COM_ANULADA") == 0) {
         //Marcamos el estatus como el 5:CERRADA
         this.document.setFieldInt("COM_STATUS", 5);
         //Definimos campos
         this.document.setFieldInt("COM_USER_CERRADA", this.varSesiones.getIntNoUser());
         this.document.setFieldString("COM_HORA_CERRADA", this.fecha.getHoraActual());
         this.document.setFieldString("COM_FECHA_CERRADA", this.fecha.getFechaActual());
         String strResp1 = this.document.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         } else {
            // <editor-fold defaultstate="collapsed" desc="Bitacora">
            this.saveBitacora("COMPRAS", "CERRAR", this.document.getFieldInt("COM_ID"));
            // </editor-fold>
         }

      } else {
         this.strResultLast = "ERROR:La operacion ya fue anulada";
      }
      // <editor-fold defaultstate="collapsed" desc="Terminamos la transaccion">
      if (this.bolTransaccionalidad) {
         if (this.strResultLast.startsWith("OK")) {
            this.oConn.runQueryLMD("COMMIT");
         } else {
            this.oConn.runQueryLMD("ROLLBACK");
         }
      }
      // </editor-fold>
   }

   // <editor-fold defaultstate="collapsed" desc="Envio de mails">
   /**
    * Se encarga de enviar de manera automatico un mail al proveedor con la
    * orden de compra
    *
    * @param intEMP_ACUSEFACTURA
    * @param strMailProv Es el mail del proveedor
    * @param strMailProv2 Es el mail del proveedor 2
    * @param bolEnvioProveedor Indica si se le envia la orden de compra al
    * proveedor
    * @return Regres OK si todo salio correctamente
    */
   protected String EnvioMasivo(int intEMP_ACUSEFACTURA,
           String strMailProv,
           String strMailProv2,
           boolean bolEnvioProveedor) {
      //Generamos el formato de impresión
      GeneraImpresionPDF(Integer.valueOf(this.document.getValorKey()),
              document.getFieldString("COM_FOLIO"),
              document.getFieldInt("EMP_ID"));

      //preparamos el envio del mail
      String strResp = "OK";
      //Obtenemos datos del smtp
      String strsmtp_server = "";
      String strsmtp_user = "";
      String strsmtp_pass = "";
      String strsmtp_port = "";
      String strsmtp_usaTLS = "";
      String strsmtp_usaSTLS = "";
      //Buscamos los datos del SMTP
      String strSql = "select * from cuenta_contratada where ctam_id = 1";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strsmtp_server = rs.getString("smtp_server");
            strsmtp_user = rs.getString("smtp_user");
            strsmtp_pass = rs.getString("smtp_pass");
            strsmtp_port = rs.getString("smtp_port");
            strsmtp_usaTLS = rs.getString("smtp_usaTLS");
            strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }

      /**
       * Si estan llenos todos los datos mandamos el mail
       */
      if (!strsmtp_server.equals("")
              && !strsmtp_user.equals("")
              && !strsmtp_pass.equals("")) {
         //armamos el mail
         Mail mail = new Mail();
         //Activamos envio de acuse de recibo
         if (intEMP_ACUSEFACTURA == 1) {
            mail.setBolAcuseRecibo(true);
         }
         //Obtenemos los usuarios a los que mandaremos el mail
         String strLstMail = "";
         strSql = "select * from usuarios where "
                 + " BOL_MAIL_COMPRAS = 1 AND "
                 + " (SELECT count(EMP_ID) as cuantos FROM vta_userempresa "
                 + " WHERE EMP_ID =" + document.getFieldInt("EMP_ID") + " "
                 + " AND vta_userempresa.ID_USUARIOS =usuarios.id_usuarios) <> 0";
         try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               if (mail.isEmail(rs.getString("EMAIL"))) {
                  strLstMail += rs.getString("EMAIL") + ",";
               }
            }
            rs.close();
            if (strLstMail.endsWith(",")) {
               strLstMail = strLstMail.substring(0, strLstMail.length() - 1);
            }
         } catch (SQLException ex) {
            log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         }
         if (bolEnvioProveedor) {
            //Validamos si el mail del proveedor es valido
            if (mail.isEmail(strMailProv)) {
               strLstMail += "," + strMailProv;
            }
            if (mail.isEmail(strMailProv2)) {
               strLstMail += "," + strMailProv2;
            }
         }

         //Mandamos mail si hay usuarios
         if (!strLstMail.equals("")) {
            String strMsgMail = "Estimado Usuario<br> "
                    + "Le informamos que tiene una nueva Orden de Compra realizada "
                    + "con folio " + document.getFieldString("COM_FOLIO") + ", por lo que le enviamos"
                    + " adjunto el documento electronico en PDF: "
                    + "<br>"
                    + "<b><u>Archivo PDF</u></b><br>" + "";
            if (!bolEnvioProveedor) {
               strMsgMail = "Estimado Usuario<br> "
                       + "Le informamos que tiene una nueva Orden de Compra por autorizar "
                       + "con folio " + document.getFieldString("COM_FOLIO") + ", por lo que le enviamos"
                       + " adjunto el documento electronico en PDF: "
                       + "<br>"
                       + "<b><u>Archivo PDF</u></b><br>" + "";
            }
            //Establecemos parametros
            mail.setUsuario(strsmtp_user);
            mail.setContrasenia(strsmtp_pass);
            mail.setHost(strsmtp_server);
            mail.setPuerto(strsmtp_port);
            mail.setAsunto("Orden de Compra con folio " + document.getFieldString("COM_FOLIO") + "...");
            mail.setDestino(strLstMail);
            mail.setMensaje(strMsgMail);
            //Adjuntamos el PDF
            mail.setFichero(this.strPathXml + "compra_" + document.getFieldString("COM_FOLIO") + ".pdf");
            if (strsmtp_usaTLS.equals("1")) {
               mail.setBolUsaTls(true);
            }
            if (strsmtp_usaSTLS.equals("1")) {
               mail.setBolUsaStartTls(true);
            }
            boolean bol = mail.sendMail();
            if (!bol) {
               strResp = "Fallo el envio del Mail.";
            }
         }
      }
      return strResp;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Impresion y PDF">

   /**
    * Genera el formato de impresion en PDF
    *
    * @param intCompraId Es el id de la compra
    * @param strFolio Es el folio de la orden de compra
    * @param intEmpId Es el id de la empresa
    * @return Regresa OK si se genero el formato
    */
   protected String GeneraImpresionPDF(int intCompraId, String strFolio, int intEmpId) {
      String strResp = "OK";
      /*Validaciones dependiendo del tipo de comprobante*/
      try {
         Document PDF = new Document();
         PdfWriter writer = PdfWriter.getInstance(PDF, new FileOutputStream(this.strPathXml + "compra_" + strFolio + ".pdf"));
         PDF.open();
         Formateador format = new Formateador();
         format.InitFormat(oConn, this.strNomFormato);
         format.setIntTypeOut(Formateador.FILE);
         format.setStrPath(this.strPathXml);
         String strRes = format.DoFormat(oConn, intCompraId);
         if (strRes.equals("OK")) {
            CIP_Formato fPDF = new CIP_Formato();
            fPDF.setDocument(PDF);
            fPDF.setWriter(writer);
            fPDF.setStrPathFonts(this.strPathFonts);
            fPDF.EmiteFormato(format.getFmXML());
         } else {
            strResp = strRes;
         }
         PDF.close();
         writer.close();
      } catch (FileNotFoundException ex) {
         log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         strResp = "ERROR:" + ex.getMessage();
      } catch (DocumentException ex) {
         log.error(ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         strResp = "ERROR:" + ex.getMessage();
      }
      return strResp;
   }
   // </editor-fold>

   /**
    * Nos regresa el numero de recepciones hechas para esta orden de compra
    *
    * @param intNumODC Es el numero de Orden de compra
    * @return Regrea un entero con el valor solicitado
    */
   public int GetNumRecep(int intNumODC) {
      int intNumRecepciones = 0;
      String strSql = "SELECT count(MP_ID) as cuantos FROM vta_movprod "
              + " where OC_ID=" + intNumODC + " and MP_ANULADO =  0 ";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intNumRecepciones = rs.getInt("cuantos");
         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage());
      }
      return intNumRecepciones;
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
         log.error(ex.getMessage());

      }
      return bolTieneAcceso;
   }

   /**
    * Genera el XML de confirmacion/recepcion de ordenes de compra
    *
    * @param strComId Es el id de la orden de compra
    * @param strInvIdO Es el id del inventario
    * @return Regresa el XML
    */
   public String GeneraXMLConfirmacion(String strComId, String strInvIdO) {
      //Instanciamos el objeto de inventarios
      ArrayList<vta_movprod> listMovProd = new ArrayList<vta_movprod>();
      ArrayList<String> listODC = new ArrayList<String>();

      //Instanciamos el objeto de compras
      Compras compras = new Compras(oConn, varSesiones, request);
      //String strCOM_ID = strComId;
      if (strComId == null) {
         //strCOM_ID = "0";
         String strInvId = strInvIdO;
         if (strInvId != null) {
            if (strInvId.contains(",")) {
               String[] lstItems = strInvId.split(",");
               for (String lstItem : lstItems) {
                  //Objeto de la recepción
                  vta_movprod movProd = new vta_movprod();
                  //Buscamos primero la información de inventarios
                  movProd.ObtenDatos(Integer.valueOf(lstItem), oConn);
                  //Validamos si ya existe la orden de compra
                  if (!listODC.contains(movProd.getFieldString("OC_ID"))) {
                     listODC.add(movProd.getFieldString("OC_ID"));
                  }
                  listMovProd.add(movProd);
               }
            } else {
               //Una sola recepción
               vta_movprod movProd = new vta_movprod();
               //Buscamos primero la información de inventarios
               movProd.ObtenDatos(Integer.valueOf(strInvId), oConn);
               listMovProd.add(movProd);
               listODC.add(movProd.getFieldString("OC_ID"));
            }
         }

      } else {
         //anexamos la orden de compra
         listODC.add(strComId);
      }
      //comenzamos a generar el XML
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      strXML.append("<vta_compras> ");
      //Listamos ordenes de compra
      Iterator<String> itOdc = listODC.iterator();
      while (itOdc.hasNext()) {
         String strCOM_ID = itOdc.next();
         strXML.append("<vta_compra ");
         //Pintamos todas las ordenes de compra
         //Recuperamos info de pedidos
         vta_compra compra = new vta_compra();
         compra.ObtenDatos(Integer.valueOf(strCOM_ID), oConn);
         String strValorPar = compra.getFieldPar();
         //Obtenemos el numero de entradas de esta orden de compra
         strXML.append(strValorPar);
         strXML.append(" COMP_RECEP = \"").append(compras.GetNumRecep(Integer.valueOf(strCOM_ID))).append("\"  > ");
         //Obtenemos el detalle
         vta_compradeta deta = new vta_compradeta();
         ArrayList<TableMaster> lstDeta = deta.ObtenDatosVarios(" COM_ID = " + compra.getFieldInt("COM_ID"), oConn);
         Iterator<TableMaster> it = lstDeta.iterator();
         while (it.hasNext()) {
            TableMaster tbn = it.next();
            strXML.append("<deta ");
            strXML.append(tbn.getFieldPar()).append(" ");
            strXML.append("/>");
         }
         //Si obtuvimos datos de una entrada lo anexamos
         if (!listMovProd.isEmpty()) {
            Iterator<vta_movprod> itMovProd = listMovProd.iterator();
            while (itMovProd.hasNext()) {
               vta_movprod movProd = itMovProd.next();
               //Validamos si corresponde a la orden de compra
               if (movProd.getFieldString("OC_ID").equals(strCOM_ID)) {
                  strXML.append("<Inv ");
                  strXML.append(movProd.getFieldPar());
                  strXML.append(">");
                  //Obtenemos el detalle
                  vta_movproddeta detaInv = new vta_movproddeta();
                  ArrayList<TableMaster> lstDetaInv = detaInv.ObtenDatosVarios(" MP_ID = " + movProd.getFieldInt("MP_ID"), oConn);
                  Iterator<TableMaster> it2 = lstDetaInv.iterator();
                  while (it2.hasNext()) {
                     TableMaster tbn = it2.next();
                     strXML.append("<detaInv ");
                     strXML.append(tbn.getFieldPar()).append(" ");
                     strXML.append("/>");
                  }
                  strXML.append("</Inv>");
               }
            }

         }
         //Termina ordenes de compra
         strXML.append("</vta_compra>");
      }
      strXML.append("</vta_compras>");
      return strXML.toString();
   }

   /**
    * Regresa en un xml la lista de productos con conversiones y el factor de
    * conversion
    *
    * @param strLista Es la lista de id's de productos por analizar
    * @return Regresa la lista de items indicando si usaran conversion Ejemplo
    * del XML:
    * <?xml version="1.0" encoding="UTF-8" ?>
    * <conv_unidad>
    * <unidad PR_ID="1" UNIDAD="0" FACTOR="0" />
    * <unidad PR_ID="2" UNIDAD="1" FACTOR="10" />
    * </conv_unidad>
    */
   public String RegresaConversion(String strLista) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      //Limpiamos coma al inicio o final
      if (strLista.startsWith(",")) {
         strLista = strLista.substring(1, strLista.length());
      }
      if (strLista.endsWith(",")) {
         strLista = strLista.substring(0, strLista.length() - 1);
      }
      try {
         strXML.append("<conv_unidad>");
         //Validamos si tenemos algun id
         if (!strLista.trim().isEmpty()) {
            String strSql = "select PR_ID,PR_UNIDADMEDIDA,PR_UNIDADMEDIDA_COMPRA FROM vta_producto where PR_ID IN (" + strLista + ")";
            ResultSet rs = this.oConn.runQuery(strSql);
            while (rs.next()) {
               if (rs.getInt("PR_UNIDADMEDIDA") != 0 && rs.getInt("PR_UNIDADMEDIDA_COMPRA") != 0) {
                  if (rs.getInt("PR_UNIDADMEDIDA") != rs.getInt("PR_UNIDADMEDIDA_COMPRA")) {
                     //Buscamos el factor de conversion en la base de datos
                     double dblFactor = 0;
                     String strSql2 = "select UMC_FACTOR FROM vta_unidadmedida_conversion "
                             + " where "
                             + " (UMC_ME_ID = " + rs.getInt("PR_UNIDADMEDIDA") + " AND UMC_ME2_ID = " + rs.getInt("PR_UNIDADMEDIDA_COMPRA") + ") OR "
                             + " (UMC_ME_ID = " + rs.getInt("PR_UNIDADMEDIDA_COMPRA") + " AND UMC_ME2_ID = " + rs.getInt("PR_UNIDADMEDIDA") + ") order by UMC_ID";
                     ResultSet rs2 = this.oConn.runQuery(strSql2);
                     while (rs2.next()) {
                        dblFactor = rs2.getDouble("UMC_FACTOR");
                     }
                     rs2.close();
                     strXML.append("<unidad PR_ID=\"").append(rs.getInt("PR_ID")).append("\" CONV=\"1\" FACTOR=\"").append(dblFactor).append("\" />");
                  } else {
                     strXML.append("<unidad PR_ID=\"").append(rs.getInt("PR_ID")).append("\" CONV=\"0\" FACTOR=\"0\" />");
                  }
               } else {
                  strXML.append("<unidad PR_ID=\"").append(rs.getInt("PR_ID")).append("\" CONV=\"0\" FACTOR=\"0\" />");
               }

            }
            rs.close();
         }
         strXML.append("</conv_unidad>");
      } // </editor-fold>
      catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
      }
      return strXML.toString();
   }
// </editor-fold>
}
