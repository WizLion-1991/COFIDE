package ERP;

import Tablas.vta_mov_prov;
import Tablas.vta_mov_prov_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Representa las operaciones que podemos realizar con los clientes(pagos y
 * cargos)
 *
 * @author zeus
 */
public class MovProveedor extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected vta_mov_prov cta_prov;//representa el documento de proveedor
   protected ArrayList<vta_mov_prov_deta> lstMovs;//representa el detalle del movimiento de bancos
   protected double dblCargo = 0;
   protected double dblAbono = 0;
   protected String strFechaAnul;
   protected int intBc_Id = 0;
   protected boolean bolCaja = false;
   protected boolean bolEsLocal = false;
   protected boolean bolEsAnticipo = false;
   protected boolean bolAplicaBanco = true;
   protected double dblSaldoFavorUsado = 0;
   protected String strNoCheque;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MovProveedor.class.getName());
   protected boolean bolAplicaConta = true;
   protected String strUUID = "";
   protected String strRFCEmisor = "";
   protected double dblSubTotal = 0.0;
   protected double dblIVA = 0.0;
   protected double dblTasaCambioAnticipoUsar = 1.0;
   protected double dblComision = 0.0;

   public double getDblComision() {
      return dblComision;
   }

   public void setDblComision(double dblComision) {
      this.dblComision = dblComision;
   }

   public double getDblTasaCambioAnticipoUsar() {
      return dblTasaCambioAnticipoUsar;
   }

   public void setDblTasaCambioAnticipoUsar(double dblTasaCambioAnticipoUsar) {
      this.dblTasaCambioAnticipoUsar = dblTasaCambioAnticipoUsar;
   }

   public String getStrRFCEmisor() {
      return strRFCEmisor;
   }

   public void setStrRFCEmisor(String strRFCEmisor) {
      this.strRFCEmisor = strRFCEmisor;
   }

   public double getDblSubTotal() {
      return dblSubTotal;
   }

   public void setDblSubTotal(double dblSubTotal) {
      this.dblSubTotal = dblSubTotal;
   }

   public double getDblIVA() {
      return dblIVA;
   }

   public void setDblIVA(double dblIVA) {
      this.dblIVA = dblIVA;
   }

   public String getStrUUID() {
      return strUUID;
   }

   public void setStrUUID(String strUUID) {
      this.strUUID = strUUID;
   }

   /**
    * Regresa la indicaci��n si se genera la contabilidad
    *
    * @return
    */
   public boolean isBolAplicaConta() {
      return bolAplicaConta;
   }

   /**
    * Indica si se calcula la contabilidad
    *
    * @param bolAplicaConta
    */
   public void setBolAplicaConta(boolean bolAplicaConta) {
      this.bolAplicaConta = bolAplicaConta;
   }

   /**
    * Es el No de Cheque en caso que lo use...
    *
    *
    */
   public String getStrNoCheque() {
      return strNoCheque;
   }

   public void setStrNoCheque(String strNoCheque) {
      this.strNoCheque = strNoCheque;
   }

   /**
    * Es el importe o saldo a favor por usar
    *
    * @return Es un valor double con el saldo a favor usado
    */
   public double getDblSaldoFavorUsado() {
      return dblSaldoFavorUsado;
   }

   /**
    * Es el importe o saldo a favor por usar
    *
    * @param dblSaldoFavorUsado Es un valor double con el saldo a favor usado
    */
   public void setDblSaldoFavorUsado(double dblSaldoFavorUsado) {
      this.dblSaldoFavorUsado = dblSaldoFavorUsado;
   }

   /**
    * Nos dice si el movimiento es de caja
    *
    * @return Nos regres True si es de caja
    */
   public boolean isBolCaja() {
      return bolCaja;
   }

   /**
    * Indica que el movimiento es de caja
    *
    * @param bolCaja Con true es de caja
    */
   public void setBolCaja(boolean bolCaja) {
      this.bolCaja = bolCaja;
   }

   /**
    * Indica si es un movimiento de sistema local
    *
    * @return Si es true es local
    */
   public boolean isBolEsLocal() {
      return bolEsLocal;
   }

   /**
    * Define si es un movimiento de sistema local
    *
    * @param bolEsLocal
    */
   public void setBolEsLocal(boolean bolEsLocal) {
      this.bolEsLocal = bolEsLocal;
   }

   /**
    * Regresa el objeto que contiene los movimientos del proveedor
    *
    * @return Regresa un objeto
    */
   public vta_mov_prov getCta_prov() {
      return cta_prov;
   }

   /**
    * Define el objeto que contiene los movimientos del proveedor
    *
    * @param cta_prov Define un objeto
    */
   public void setCta_prov(vta_mov_prov cta_prov) {
      this.cta_prov = cta_prov;
   }

   /**
    * Regresa el id de la cuenta bancaria
    *
    * @return Regresa el id numerico
    */
   public int getIntBc_Id() {
      return intBc_Id;
   }

   /**
    * Define el id de la cuenta bancaria
    *
    * @param intBc_Id Es el id numerico
    */
   public void setIntBc_Id(int intBc_Id) {
      this.intBc_Id = intBc_Id;
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(vta_mov_prov_deta deta) {
      this.lstMovs.add(deta);
   }

   /**
    * Indica si es un movimiento de anticipo
    *
    * @return Es un boolean que en true indica que es un anticipo
    */
   public boolean isBolEsAnticipo() {
      return bolEsAnticipo;
   }

   /**
    * Indica si es un movimiento de anticipo
    *
    * @param bolEsAnticipo Es un boolean que en true indica que es un anticipo
    */
   public void setBolEsAnticipo(boolean bolEsAnticipo) {
      this.bolEsAnticipo = bolEsAnticipo;
   }

   /**
    * Regresa si aplico un movimiento de bancos
    *
    * @return Regresa true si aplican los movimientos de bancos
    */
   public boolean isBolAplicaBanco() {
      return bolAplicaBanco;
   }

   /**
    * Define que aplican movimientos de banco
    *
    * @param bolAplicaBanco Con true definimos si aplican los movimientos de
    * bancos
    */
   public void setBolAplicaBanco(boolean bolAplicaBanco) {
      this.bolAplicaBanco = bolAplicaBanco;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   /**
    * Constructor del objeto que lleva el registro de cuenta corriente del
    * proveedor
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion
    */
   public MovProveedor(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.cta_prov = new vta_mov_prov();
      this.lstMovs = new ArrayList<vta_mov_prov_deta>();
      bolAplicaConta = true;
   }

   /**
    * Constructor del objeto que lleva el registro de cuenta corriente del
    * proveedor
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    */
   public MovProveedor(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones, null);
      this.cta_prov = new vta_mov_prov();
      this.lstMovs = new ArrayList<vta_mov_prov_deta>();
      bolAplicaConta = true;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      this.cta_prov.setFieldInt("ID_USUARIOS", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.cta_prov.getFieldInt("MP_ID") != 0) {
         this.dblCargo = this.cta_prov.getFieldDouble("MP_CARGO");
         this.dblAbono = this.cta_prov.getFieldDouble("MP_ABONO");
         this.strFechaAnul = this.cta_prov.getFieldString("MP_FECHAANUL");
         this.cta_prov.ObtenDatos(this.cta_prov.getFieldInt("MP_ID"), oConn);
      }
   }

   @Override
   public void doTrx() {
      log.debug("Inicia pagos individuales...");
      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
      this.strResultLast = "OK";
      this.cta_prov.setFieldString("MP_FECHACREATE", this.fecha.getFechaActual());
      this.cta_prov.setFieldString("MP_HORA", this.fecha.getHoraActual());
      int intMonedaCXP = 0;
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_prov.getFieldString("MP_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      //No debe dejar movimientos posteriores a la fecha de elaboracion
      if (!this.cta_prov.getFieldString("MP_FECHACREATE").equals("")) {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
         try {
            Date fechaCreate = formatoFecha.parse(this.cta_prov.getFieldString("MP_FECHACREATE"));
            Date fechaMov = formatoFecha.parse(this.cta_prov.getFieldString("MP_FECHA"));
            if (fechaMov.after(fechaCreate)) {
               this.strResultLast = "ERROR:La fecha del movimiento es mayor a la fecha de creacion " + this.fecha.FormateaDDMMAAAA(this.cta_prov.getFieldString("MP_FECHA"), "/");
            }
         } catch (ParseException e) {
            this.strResultLast = "ERROR:La fecha del movimiento es erronea " + this.fecha.FormateaDDMMAAAA(this.cta_prov.getFieldString("MP_FECHA"), "/");
         }

      }
      //Aplican solo cuando no es un pago entonces si recuperamos datos de la bd
      if (this.cta_prov.getFieldInt("MP_ESPAGO") == 0) {
         if (this.cta_prov.getFieldInt("PV_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir el cliente";
         }
         if (this.cta_prov.getFieldInt("SC_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la sucursal";
         }
      } else {
         if (this.cta_prov.getFieldInt("PV_ID") == 0) {
            //Si no tiene proveedor es un pago de una cuenta por pagar
            if (this.cta_prov.getFieldInt("CXP_ID") == 0) {
               this.strResultLast = "ERROR:Falta definir el id de la cuenta por pagar";
            }
         }
      }
      if (this.cta_prov.getFieldDouble("MP_CARGO") == 0
              && this.cta_prov.getFieldDouble("MP_ABONO") == 0) {
         this.strResultLast = "ERROR:Falta definir un importe";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_prov.getFieldString("MP_FECHA"), this.cta_prov.getFieldInt("EMP_ID"), this.cta_prov.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos si pasamos las validaciones">
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         this.cta_prov.setBolGetAutonumeric(true);
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Validamos si es un pago para operaciones adicionales">
         if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
            // <editor-fold defaultstate="collapsed" desc="Definimos el folio de la operacion">
            Folios folio = new Folios();
            folio.setBolEsLocal(bolEsLocal);
            if (this.cta_prov.getFieldString("MP_FOLIO").equals("")) {
               String strFolio = folio.doFolio(oConn, Folios.RECIBOS_PROV, this.bolFolioGlobal, this.cta_prov.getFieldInt("SC_ID"));
               this.cta_prov.setFieldString("MP_FOLIO", strFolio);
            } else {
               folio.updateFolio(oConn, Folios.RECIBOS_PROV, this.cta_prov.getFieldString("MP_FOLIO"), this.bolFolioGlobal, this.cta_prov.getFieldInt("SC_ID"));
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Calculamos la proporcion del pago con relacion a los impuestos">
            // <editor-fold defaultstate="collapsed" desc="Recuperamos informacion de la transaccion como el proveedor y la sucursal">
            String strPrefijo = "CXP_";
            String strSql = "select PV_ID,SC_ID,CXP_IMPUESTO1,CXP_IMPUESTO2,CXP_IMPUESTO3,"
                    + "CXP_TASA1,CXP_TASA2,CXP_TASA3,CXP_TOTAL,CXP_MONEDA,EMP_ID,CXP_NETO from vta_cxpagar "
                    + "WHERE CXP_ID =" + this.cta_prov.getFieldInt("CXP_ID");
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Consultamos los datos de la cuenta por pagar excepto en anticipos">
            if (!this.bolEsAnticipo) {
               //Datos que obtendremos del sql
               int intIdSuc = 0;
               int intIdProv = 0;
               double dblImpGlob1 = 0;
               double dblImpGlob2 = 0;
               double dblImpGlob3 = 0;
               double dblImp1 = 0;
               double dblImp2 = 0;
               double dblImp3 = 0;
               double dblTasaImp1 = 0;
               double dblTasaImp2 = 0;
               double dblTasaImp3 = 0;
               ResultSet rs;
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intIdSuc = rs.getInt("SC_ID");
                     intIdProv = rs.getInt("PV_ID");
                     this.cta_prov.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
                     dblImpGlob1 = rs.getDouble(strPrefijo + "IMPUESTO1");
                     dblImpGlob2 = rs.getDouble(strPrefijo + "IMPUESTO2");
                     dblImpGlob3 = rs.getDouble(strPrefijo + "IMPUESTO3");
                     dblTasaImp1 = rs.getDouble(strPrefijo + "TASA1");
                     dblTasaImp2 = rs.getDouble(strPrefijo + "TASA2");
                     dblTasaImp3 = rs.getDouble(strPrefijo + "TASA3");
                     intMonedaCXP = rs.getInt("CXP_MONEDA");
                     //Calculamos el impuesto proporcional al pago
                     if (rs.getDouble(strPrefijo + "NETO") > 0) {
                           //Divimos el monto del pago entre el importe de la venta
                           double dblFactorPago = this.cta_prov.getFieldDouble("MP_ABONO") / rs.getDouble(strPrefijo + "NETO");
                           //Aplicamos este factor al total del impuesto
                           dblImp1 = dblImpGlob1 * dblFactorPago;
                           dblImp2 = dblImpGlob2 * dblFactorPago;
                           dblImp3 = dblImpGlob3 * dblFactorPago;
                     } else {
                        if (rs.getDouble(strPrefijo + "TOTAL") > 0) {
                           //Divimos el monto del pago entre el importe de la venta
                           double dblFactorPago = this.cta_prov.getFieldDouble("MP_ABONO") / rs.getDouble(strPrefijo + "TOTAL");
                           //Aplicamos este factor al total del impuesto
                           dblImp1 = dblImpGlob1 * dblFactorPago;
                           dblImp2 = dblImpGlob2 * dblFactorPago;
                           dblImp3 = dblImpGlob3 * dblFactorPago;
                        }
                     }
                  }
                  rs.close();
                  this.cta_prov.setFieldInt("SC_ID", intIdSuc);
                  this.cta_prov.setFieldInt("PV_ID", intIdProv);
                  this.cta_prov.setFieldDouble("MP_TASAIMPUESTO1", dblTasaImp1);
                  this.cta_prov.setFieldDouble("MP_TASAIMPUESTO2", dblTasaImp2);
                  this.cta_prov.setFieldDouble("MP_TASAIMPUESTO3", dblTasaImp3);
                  this.cta_prov.setFieldDouble("MP_IMPUESTO1", dblImp1);
                  this.cta_prov.setFieldDouble("MP_IMPUESTO2", dblImp2);
                  this.cta_prov.setFieldDouble("MP_IMPUESTO3", dblImp3);
               } catch (SQLException ex) {
                  Logger.getLogger(MovProveedor.class.getName()).log(Level.SEVERE, null, ex);
               }
            } else {
               //Asignamos el importe original
               this.cta_prov.setFieldDouble("MP_ANTICIPO_ORIGINAL", this.cta_prov.getFieldDouble("MP_ABONO"));
               this.cta_prov.setFieldDouble("MP_SALDO_ANTICIPO", this.cta_prov.getFieldDouble("MP_ABONO"));
               //Buscamos datos del proveedor
               String strSqlPV = "select SC_ID,EMP_ID from vta_proveedor where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
               ResultSet rs;
               try {
                  rs = oConn.runQuery(strSqlPV, true);
                  while (rs.next()) {
                     this.cta_prov.setFieldInt("SC_ID", rs.getInt("SC_ID"));
                     this.cta_prov.setFieldInt("EMP_ID", rs.getInt("EMP_ID"));
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(MovProveedor.class.getName()).log(Level.SEVERE, null, ex);
               }

            }

            // </editor-fold>
            // </editor-fold>
         } else {
            intMonedaCXP = this.cta_prov.getFieldInt("MP_MONEDA");
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Agregamos el movimiento">
         String strRes1 = this.cta_prov.Agrega(oConn);
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si fue exitoso proseguimos">
         if (strRes1.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Guardamos las formas de pago"> 
            int intId = Integer.valueOf(this.cta_prov.getValorKey());
            Iterator<vta_mov_prov_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_mov_prov_deta deta = it.next();
               deta.setFieldInt("MP_ID", intId);
               deta.setFieldInt("SC_ID", this.cta_prov.getFieldInt("SC_ID"));
               deta.setFieldInt("PV_ID", this.cta_prov.getFieldInt("PV_ID"));
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos los saldos">
            String strSql = "SELECT PV_SALDO,MON_ID from vta_proveedor where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("PV_SALDO");
                  double dblCargoProv = this.cta_prov.getFieldDouble("MP_CARGO");
                  double dblAbonoProv = this.cta_prov.getFieldDouble("MP_ABONO");
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  int intMonedaProv = rs.getInt("MON_ID");
                  if (intMonedaProv == 0) {
                     intMonedaProv = 1;
                  }
                  if (this.cta_prov.getFieldInt("MP_MONEDA") == 0) {
                     this.cta_prov.setFieldInt("MP_MONEDA", 1);
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaProv + " " + this.cta_prov.getFieldInt("MP_MONEDA"));
                  if (intMonedaProv != this.cta_prov.getFieldInt("MP_MONEDA")) {
                     double dblFactor = 0;
                     if (intMonedaCXP == intMonedaProv) {
                        dblFactor = this.cta_prov.getFieldDouble("MP_TASAPESO");
                        if (this.cta_prov.getFieldInt("MP_MONEDA") == 1
                                || (this.cta_prov.getFieldInt("MP_MONEDA") == 2 && intMonedaProv == 3)) {
                           dblFactor = 1 / dblFactor;
                        }
                     } else {
                        log.debug("Moneda CXP " + intMonedaCXP);
                        if (this.cta_prov.getFieldDouble("MP_TASAPESO") > 1 && intMonedaCXP == 0) {
                           dblFactor = this.cta_prov.getFieldDouble("MP_TASAPESO");
                           if (this.cta_prov.getFieldInt("MP_MONEDA") == 1
                                   || (this.cta_prov.getFieldInt("MP_MONEDA") == 2 && intMonedaProv == 3)) {
                              dblFactor = 1 / dblFactor;
                           }
                        } else {
                           Monedas conversion = new Monedas(oConn);
                           dblFactor = conversion.GetFactorConversion(this.cta_prov.getFieldString("MP_FECHA"), 4, this.cta_prov.getFieldInt("MP_MONEDA"), intMonedaProv);
                        }
                     }
                     log.debug("Factor de conversion " + dblFactor);
                     log.debug("dblCargoProv " + dblCargoProv);
                     log.debug("dblAbonoProv " + dblAbonoProv);
                     dblCargoProv = dblCargoProv * dblFactor;
                     dblAbonoProv = dblAbonoProv * dblFactor;
                     log.debug("Post dblCargoProv " + dblCargoProv);
                     log.debug("Post dblAbonoProv " + dblAbonoProv);

                  }
                  // </editor-fold>
                  if (dblCargoProv > 0) {
                     dblSaldo = dblSaldo + dblCargoProv;
                  } else {
                     dblSaldo = dblSaldo - dblAbonoProv;
                  }
                  // <editor-fold defaultstate="collapsed" desc="Actualizamos el saldo del proveedor">
                  String strUpdate = "UPDATE vta_proveedor set PV_SALDO = " + dblSaldo + " where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
                  oConn.runQueryLMD(strUpdate);
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Actualizamos el saldo de la cuenta por pagar">
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  double dblCargoDeta = this.cta_prov.getFieldDouble("MP_CARGO");
                  double dblAbonoDeta = this.cta_prov.getFieldDouble("MP_ABONO");
                  if (intMonedaCXP == 0) {
                     intMonedaCXP = 1;
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaCXP + " " + this.cta_prov.getFieldInt("MP_MONEDA"));
                  if (intMonedaCXP != this.cta_prov.getFieldInt("MP_MONEDA")) {
                     //Monedas conversion = new Monedas(oConn);
//                     double dblFactor = conversion.GetFactorConversion(this.cta_prov.getFieldString("MP_FECHA"), 4, this.cta_prov.getFieldInt("MP_MONEDA"), intMonedaCXP);
                     double dblFactor = this.cta_prov.getFieldDouble("MP_TASAPESO");
                     log.debug("Factor de conversion " + dblFactor);
                     if (this.cta_prov.getFieldInt("MP_MONEDA") == 1 || (this.cta_prov.getFieldInt("MP_MONEDA") == 2 && intMonedaCXP == 3)) {
                        dblCargoDeta = dblCargoDeta / dblFactor;
                        dblAbonoDeta = dblAbonoDeta / dblFactor;
                     } else {
                        dblCargoDeta = dblCargoDeta * dblFactor;
                        dblAbonoDeta = dblAbonoDeta * dblFactor;
                     }
                  }
                  // </editor-fold>
                  //Actualizamos el saldo de la Cuenta por pagar
                  if (dblCargoDeta > 0) {
                     strUpdate = "UPDATE vta_cxpagar set CXP_SALDO = CXP_SALDO + "
                             + dblCargoDeta + "  "
                             + " WHERE CXP_ID = " + this.cta_prov.getFieldInt("CXP_ID");

                  } else {
                     strUpdate = "UPDATE vta_cxpagar set CXP_SALDO = CXP_SALDO - "
                             + dblAbonoDeta + "   "
                             + " WHERE CXP_ID = " + this.cta_prov.getFieldInt("CXP_ID");
                  }
                  oConn.runQueryLMD(strUpdate);
                  // </editor-fold>
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Afectamos bancos">
            //Evaluamos si el movimiento esta usando un anticipo para no afectar bancos
            if (this.cta_prov.getFieldInt("MP_USA_ANTICIPO") == 1) {
               this.bolAplicaBanco = false;
            }
            int intIdBanco = this.intBc_Id;
            //Validamos si aplica el movimiento de bancos
            if (this.bolAplicaBanco) {
               if (this.strResultLast.equals("OK")) {
                  if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
                     // <editor-fold defaultstate="collapsed" desc="Si es caja buscamos la caja">
                     if (this.bolCaja) {
                        strSql = "SELECT BC_ID from vta_bcos where BC_ESCAJA = 1 AND SC_ID = " + this.cta_prov.getFieldInt("SC_ID");
                        try {
                           ResultSet rs = oConn.runQuery(strSql, true);
                           while (rs.next()) {
                              intIdBanco = rs.getInt("BC_ID");
                           }
                           rs.close();
                        } catch (SQLException ex) {
                           this.strResultLast = "ERROR:" + ex.getMessage();
                        }
                     }
                     // </editor-fold>
                     /// <editor-fold defaultstate="collapsed" desc="Si hay un banco seleccionado hacemos el movimiento">
                     if (intIdBanco != 0) {
                        Bancos banco = new Bancos(this.oConn, this.varSesiones, this.request);
                        banco.Init();
                        banco.setBolTransaccionalidad(false);
                        log.debug("Pasamos la comision..." + this.dblComision);
                        banco.setDblComision(this.dblComision);
                        banco.getCta_bcos().setFieldString("MCB_FECHA", this.cta_prov.getFieldString("MP_FECHA"));
                        banco.getCta_bcos().setFieldInt("SC_ID", this.cta_prov.getFieldInt("SC_ID"));
                        banco.getCta_bcos().setFieldInt("EMP_ID", this.cta_prov.getFieldInt("EMP_ID"));
                        banco.getCta_bcos().setFieldInt("BC_ID", intIdBanco);
                        banco.getCta_bcos().setFieldInt("CXP_ID", this.cta_prov.getFieldInt("CXP_ID"));
                        banco.getCta_bcos().setFieldInt("MP_ID", this.cta_prov.getFieldInt("MP_ID"));
                        log.debug("Asignamos moneda al banco..." + this.cta_prov.getFieldInt("MP_MONEDA"));
                        banco.getCta_bcos().setFieldInt("MCB_MONEDA", this.cta_prov.getFieldInt("MP_MONEDA"));
                        banco.getCta_bcos().setFieldDouble("MCB_DEPOSITO", this.cta_prov.getFieldDouble("MP_CARGO"));
                        banco.getCta_bcos().setFieldDouble("MCB_RETIRO", this.cta_prov.getFieldDouble("MP_ABONO"));
                        banco.getCta_bcos().setFieldString("MCB_NOCHEQUE", this.strNoCheque);
                        banco.getCta_bcos().setFieldInt("MCB_CONCILIADO", 1);
                        banco.doTrx();
                        if (!banco.getStrResultLast().equals("OK")) {
                           this.strResultLast = banco.getStrResultLast();
                        }
                     }
                     // </editor-fold>
                  }
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del anticipo">
            if (this.cta_prov.getFieldInt("MP_USA_ANTICIPO") == 1) {
               vta_mov_prov movAnti = new vta_mov_prov();
               movAnti.ObtenDatos(this.cta_prov.getFieldInt("MP_ANTI_ID"), oConn);

               double dblTasaCambio = 1.0; // this.dblTasaCambioAnticipoUsar;
               int intMonAnti = movAnti.getFieldInt("MP_MONEDA");
               int intMonPago = this.cta_prov.getFieldInt("MP_MONEDA");
               if (intMonAnti != intMonPago) {
                  dblTasaCambio = this.dblTasaCambioAnticipoUsar;
                  Monedas MiTasaCambio = new Monedas(oConn);
                  MiTasaCambio.setBoolConversionAutomatica(false);
                  //Se revisa que operacion se tiene que realizar dependiendo de la moneda
                  Fechas FECHAS = new Fechas();

                  MiTasaCambio.GetFactorConversion(FECHAS.getFechaActual(), 4, intMonAnti, intMonPago);
                  if (MiTasaCambio.getIntMonedaBase() == intMonAnti) {
                     dblTasaCambio = 1 / dblTasaCambio;
                  }

               }
               log.debug(movAnti.getFieldDouble("MP_SALDO_ANTICIPO") + " - " + this.cta_prov.getFieldDouble("MP_ABONO") * dblTasaCambio);
               movAnti.setFieldDouble("MP_SALDO_ANTICIPO", movAnti.getFieldDouble("MP_SALDO_ANTICIPO") - (this.cta_prov.getFieldDouble("MP_ABONO") * dblTasaCambio));
               movAnti.Modifica(oConn);

               //Generamos movimiento de cargo
               double dblImportePago = this.cta_prov.getFieldDouble("MP_ABONO");
               int intMonedaPago = this.cta_prov.getFieldInt("MP_MONEDA");
               int intCxp_IdPago = this.cta_prov.getFieldInt("CXP_ID");
               int intPv_IdPago = this.cta_prov.getFieldInt("PV_ID");
               this.cta_prov.setFieldDouble("MP_CARGO", dblImportePago);
               this.cta_prov.setFieldDouble("MP_ABONO", 0);
               this.cta_prov.setFieldInt("MP_USO_ANTI_ID", intId);
               this.cta_prov.setFieldInt("MP_ESPAGO", 0);
               this.cta_prov.setFieldInt("CXP_ID", 0);
               this.cta_prov.setFieldInt("PV_ID", movAnti.getFieldInt("PV_ID"));

               if (intMonAnti != this.cta_prov.getFieldInt("MP_MONEDA")) {
                  if (dblTasaCambio < 1.0) {
                     dblTasaCambio = 1 / dblTasaCambio;
                  }
                  this.cta_prov.setFieldDouble("MP_TASAPESO", dblTasaCambio);
               }

//                  double dblImportePagoConv = dblImportePago;
//                  double dblFactor = this.cta_prov.getFieldDouble("MP_TASAPESO");
//                  log.debug("Factor de conversion " + dblFactor);
//                  if (this.cta_prov.getFieldInt("MP_MONEDA") == 1) {
//                     dblImportePagoConv = dblImportePagoConv / dblFactor;
//                  } else {
//                     dblImportePagoConv = dblImportePagoConv * dblFactor;
//                  }
//                  this.cta_prov.setFieldDouble("MP_CARGO", dblImportePagoConv);
//                  this.cta_prov.setFieldInt("MP_MONEDA", intMonedaCXP);
//               }
               String strRes2 = this.cta_prov.Agrega(oConn);
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               } else {
                  this.cta_prov.setFieldDouble("MP_ABONO", dblImportePago);
                  this.cta_prov.setFieldDouble("MP_CARGO", 0);
                  this.cta_prov.setFieldInt("MP_MONEDA", intMonedaPago);
                  this.cta_prov.setFieldInt("MP_ESPAGO", 1);
                  this.cta_prov.setFieldInt("CXP_ID", intCxp_IdPago);
                  this.cta_prov.setFieldInt("PV_ID", intPv_IdPago);
               }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVICE PARA GENERAR LA POLIZA CONTABLE">
            if (this.strResultLast.equals("OK")) {
               //Solo aplica si es un pago
               if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
                  // <editor-fold defaultstate="collapsed" desc="Evaluamos si se ocupo un saldo a favor para ajustarlos">
                  if (this.dblSaldoFavorUsado > 0) {
//                     this.doAjustaSaldoFavor(this.cta_prov.getFieldInt("PV_ID"), intMonedaCXP, this.cta_prov.getFieldInt("MP_MONEDA"), this.cta_prov.getFieldInt("MP_TASAPESO"));
                  }
                  // </editor-fold>
                  //Solo si no hubo errores
                  if (this.strResultLast.equals("OK")) {

                     if (this.bolAplicaConta) {
                        // <editor-fold defaultstate="collapsed" desc="Calculamos la contabilidad">
                        //Objeto para calculo de poliza contable
                        log.debug("Generamos contabilidad Pagos Provedores...");
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        contaUtil.CalculaPolizaContablePagos(this.cta_prov.getFieldInt("MP_MONEDA"), this.cta_prov, intId, "NEW");
                        //Validamos si es un anticipo
                        if (this.bolEsAnticipo) {
                           contaUtil.setIntEsAnticipo(1);
                        }
                        // </editor-fold>         
                     }

                  }

               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
            if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
               this.saveBitacora("PAGOS_PROV", "NUEVA", intId);
            } else {
               this.saveBitacora("CARGOS_PROV", "NUEVA", intId);
            }
            // </editor-fold>
         } else {
            this.strResultLast = strRes1;
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
      }
      // </editor-fold>
   }

   @Override
   public void doTrxAnul() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_prov.getFieldInt("MP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_prov.getFieldString("MP_FECHA"), this.cta_prov.getFieldInt("EMP_ID"), this.cta_prov.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Validamos si es un anticipo para buscar si hay movimientos relacionados
      if (this.cta_prov.getFieldInt("MP_ANTICIPO") == 1) {
         //Contamos los movimientos que usan el anticipo
         int intCuantos = 0;
         String strSAnti = "select count(MP_ID) as cuantos from vta_mov_prov where MP_ANTI_ID = " + this.cta_prov.getFieldInt("MP_ID") + " AND MP_ANULADO = 0";
         try {
            ResultSet rs = oConn.runQuery(strSAnti, true);
            while (rs.next()) {
               intCuantos = rs.getInt("cuantos");
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
         if (intCuantos > 0) {
            this.strResultLast = "ERROR:El anticipo ya ha sido aplicado";
         }
      }
      // </editor-fold>
      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.cta_prov.getFieldInt("MP_ANULADO") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Validamos si es un pago para operaciones adicionales">
            if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
               // <editor-fold defaultstate="collapsed" desc="Definimos el folio de la operacion">
               Folios folio = new Folios();
               folio.setBolEsLocal(bolEsLocal);
               if (this.cta_prov.getFieldString("MP_FOLIO").equals("")) {
                  String strFolio = folio.doFolio(oConn, Folios.RECIBOS_PROV, this.bolFolioGlobal, this.cta_prov.getFieldInt("SC_ID"));
                  this.cta_prov.setFieldString("MP_FOLIO", strFolio);
               } else {
                  folio.updateFolio(oConn, Folios.RECIBOS_PROV, this.cta_prov.getFieldString("MP_FOLIO"), this.bolFolioGlobal, this.cta_prov.getFieldInt("SC_ID"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Evaluamos si uso anticipo para devolverlo">
               if (this.cta_prov.getFieldInt("MP_USA_ANTICIPO") == 1) {

                  vta_mov_prov movAnti = new vta_mov_prov();
                  movAnti.ObtenDatos(this.cta_prov.getFieldInt("MP_ANTI_ID"), oConn);
                  movAnti.setFieldDouble("MP_SALDO_ANTICIPO", movAnti.getFieldDouble("MP_SALDO_ANTICIPO") + this.cta_prov.getFieldDouble("MP_ABONO"));
                  movAnti.Modifica(oConn);

                  //Cancelamos el cargo generado al usar el anticipo
                  vta_mov_prov movCargo = movAnti.getMovCargoAnticipo(this.cta_prov.getFieldInt("MP_ID"), oConn);
                  movCargo.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
                  movCargo.setFieldInt("MP_ANULADO", 1);
                  movCargo.setFieldString("MP_HORAANUL", this.fecha.getHoraActual());
                  if (strFechaAnul.equals("")) {
                     movCargo.setFieldString("MP_FECHAANUL", this.fecha.getFechaActual());
                  } else {
                     movCargo.setFieldString("MP_FECHAANUL", this.fecha.getFechaActual());
                  }
                  String strResp1 = movCargo.Modifica(oConn);
               }
               // </editor-fold>
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del proveedor">
            String strSql = "SELECT PV_SALDO,MON_ID from vta_proveedor where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("PV_SALDO");
                  double dblCargoProv = this.cta_prov.getFieldDouble("MP_CARGO");
                  double dblAbonoProv = this.cta_prov.getFieldDouble("MP_ABONO");
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  int intMonedaProv = rs.getInt("MON_ID");
                  if (intMonedaProv == 0) {
                     intMonedaProv = 1;
                  }
                  // <editor-fold defaultstate="collapsed" desc="Consultamos la informacion del documento">
                  int intMonedaCXP = 0;
                  try {
                     strSql = "select CXP_MONEDA from vta_cxpagar "
                             + "WHERE CXP_ID =" + this.cta_prov.getFieldInt("CXP_ID");
                     ResultSet rs2 = oConn.runQuery(strSql, true);
                     while (rs2.next()) {
                        intMonedaCXP = rs2.getInt("CXP_MONEDA");
                     }
                     rs2.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                  }
                  if (this.cta_prov.getFieldInt("MP_MONEDA") == 0) {
                     this.cta_prov.setFieldInt("MP_MONEDA", intMonedaCXP);
                  }
                  // </editor-fold>
                  log.debug("Evaluamos conversion de monedas(Proveedor) " + intMonedaProv + " " + this.cta_prov.getFieldInt("MP_MONEDA"));
                  if (intMonedaProv != this.cta_prov.getFieldInt("MP_MONEDA")) {
                     double dblFactor = 0;
                     if (intMonedaCXP == intMonedaProv) {
                        dblFactor = this.cta_prov.getFieldDouble("MP_TASAPESO");
                     } else {
                        Monedas conversion = new Monedas(oConn);
                        dblFactor = conversion.GetFactorConversion(this.cta_prov.getFieldString("MP_FECHA"), 4, this.cta_prov.getFieldInt("MP_MONEDA"), intMonedaProv);
                     }
                     log.debug("Factor de conversion " + dblFactor);
                     dblCargoProv = dblCargoProv * dblFactor;
                     dblAbonoProv = dblAbonoProv * dblFactor;
                  }
                  // </editor-fold>
                  if (dblCargoProv > 0) {
                     dblSaldo = dblSaldo - dblCargoProv;
                  } else {
                     dblSaldo = dblSaldo + dblAbonoProv;
                  }
                  //Actualizamos el saldo
                  String strUpdate = "UPDATE vta_proveedor set PV_SALDO = " + dblSaldo + " where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
                  oConn.runQueryLMD(strUpdate);
                  // <editor-fold defaultstate="collapsed" desc="Actualizamos el saldo de la cuenta por pagar">
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  double dblCargoDeta = this.cta_prov.getFieldDouble("MP_CARGO");
                  double dblAbonoDeta = this.cta_prov.getFieldDouble("MP_ABONO");

                  if (intMonedaCXP == 0) {
                     intMonedaCXP = 1;
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaCXP + " " + this.cta_prov.getFieldInt("MP_MONEDA"));
                  if (intMonedaCXP != this.cta_prov.getFieldInt("MP_MONEDA")) {
                     double dblFactor = this.cta_prov.getFieldDouble("MP_TASAPESO");
                     log.debug("Factor de conversion " + dblFactor);
                     if (this.cta_prov.getFieldInt("MP_MONEDA") == 1) {
                        dblCargoDeta = dblCargoDeta / dblFactor;
                        dblAbonoDeta = dblAbonoDeta / dblFactor;
                     } else {
                        dblCargoDeta = dblCargoDeta * dblFactor;
                        dblAbonoDeta = dblAbonoDeta * dblFactor;
                     }
                  }
                  // </editor-fold>
                  if (dblCargoDeta > 0) {
                     strUpdate = "UPDATE vta_cxpagar set CXP_SALDO = CXP_SALDO - "
                             + dblCargoDeta + "  "
                             + " WHERE CXP_ID = " + this.cta_prov.getFieldInt("CXP_ID");

                  } else {
                     strUpdate = "UPDATE vta_cxpagar set CXP_SALDO = CXP_SALDO + "
                             + dblAbonoDeta + "   "
                             + " WHERE CXP_ID = " + this.cta_prov.getFieldInt("CXP_ID");
                  }
                  oConn.runQueryLMD(strUpdate);
                  // </editor-fold>
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Definimos campos">
            this.cta_prov.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
            this.cta_prov.setFieldInt("MP_ANULADO", 1);
            this.cta_prov.setFieldString("MP_HORAANUL", this.fecha.getHoraActual());
            if (strFechaAnul.equals("")) {
               this.cta_prov.setFieldString("MP_FECHAANUL", this.fecha.getFechaActual());
            } else {
               this.cta_prov.setFieldString("MP_FECHAANUL", this.fecha.getFechaActual());
            }
            // </editor-fold>
            String strResp1 = this.cta_prov.Modifica(oConn);
            int intIdBanco = 0;
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1 || (this.cta_prov.getFieldInt("MP_ANTI_ID") != 0 && this.cta_prov.getFieldDouble("MP_CARGO")> 0.0 ) ) {
                  // <editor-fold defaultstate="collapsed" desc="Consultamos bancos">
                  int intIdMovBco = 0;
                  strSql = "SELECT BC_ID,MCB_ID from vta_mov_cta_bcos where MP_ID = " + this.cta_prov.getFieldInt("MP_ID");
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intIdMovBco = rs.getInt("MCB_ID");
                        intIdBanco = rs.getInt("BC_ID");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     this.strResultLast = "ERROR:" + ex.getMessage();
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Si hay un movimiento de banco seleccionado hacemos el movimiento">
                  if (intIdMovBco != 0) {
                     Bancos banco = new Bancos(this.oConn, this.varSesiones, this.request);
                     banco.getCta_bcos().setFieldInt("MCB_ID", intIdMovBco);
                     banco.setCancelarMovimientoCobranza(true);
                     banco.Init();
                     banco.setBolTransaccionalidad(false);
                     banco.doTrxAnul();
                     if (!banco.getStrResultLast().equals("OK")) {
                        this.strResultLast = banco.getStrResultLast();
                     }
                  }
                  // </editor-fold>
               }
               // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
               if (this.strResultLast.equals("OK")) {
                  //Solo si es pago procede
                  if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
                     if (this.bolAplicaConta) {
                        // <editor-fold defaultstate="collapsed" desc="Calculamos la contabilidad">
                        //Objeto para calculo de poliza contable
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        contaUtil.CalculaPolizaContablePagos(this.cta_prov.getFieldInt("MP_MONEDA"), this.cta_prov,
                                this.cta_prov.getFieldInt("MP_ID"), "CANCEL");
                        //Si es anticipo lo marcamos
                        if (this.cta_prov.getFieldInt("MP_ANTICIPO") == 1) {
                           contaUtil.setIntEsAnticipo(1);
                        }
                        // </editor-fold>   
                     }

                  }
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
               if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
                  this.saveBitacora("PAGOS", "ANULAR", this.cta_prov.getFieldInt("MP_ID"));
               } else {
                  this.saveBitacora("CARGOS", "ANULAR", this.cta_prov.getFieldInt("MP_ID"));
               }
               // </editor-fold>
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

   }

   @Override
   public void doTrxRevive() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_prov.getFieldInt("MP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por revivir";
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
      if (this.cta_prov.getFieldInt("MP_ANULADO") != 0) {
         // <editor-fold defaultstate="collapsed" desc="Ajustamos los saldos">
         String strSql = "SELECT PV_SALDO from vta_proveedor where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               double dblSaldo = rs.getDouble("PV_SALDO");
               if (this.cta_prov.getFieldDouble("MP_CARGO") > 0) {
                  dblSaldo = dblSaldo - this.cta_prov.getFieldDouble("MP_CARGO");
               } else {
                  dblSaldo = dblSaldo + this.cta_prov.getFieldDouble("MP_ABONO");
               }
               //Actualizamos el saldo
               String strUpdate = "UPDATE vta_proveedor set PV_SALDO = " + dblSaldo + " where PV_ID = " + this.cta_prov.getFieldInt("PV_ID");
               oConn.runQueryLMD(strUpdate);
               // <editor-fold defaultstate="collapsed" desc="En caso de venir de un ticket actualizamos el saldo">
               if (this.cta_prov.getFieldInt("TKT_ID") != 0) {
                  //Actualizamos el saldo de la venta
                  if (this.cta_prov.getFieldDouble("MP_CARGO") > 0) {
                     strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO + "
                             + this.cta_prov.getFieldDouble("MP_CARGO") + "  "
                             + " WHERE TKT_ID = " + this.cta_prov.getFieldInt("TKT_ID");

                  } else {
                     strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO - "
                             + this.cta_prov.getFieldDouble("MP_ABONO") + "   "
                             + " WHERE TKT_ID = " + this.cta_prov.getFieldInt("TKT_ID");
                  }
                  oConn.runQueryLMD(strUpdate);
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="En caso de venir de una factura actualizamos el saldo">
               if (this.cta_prov.getFieldInt("FAC_ID") != 0) {
                  //Actualizamos el saldo de la venta
                  if (this.cta_prov.getFieldDouble("MP_CARGO") > 0) {
                     strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO + "
                             + this.cta_prov.getFieldDouble("MP_CARGO") + "  "
                             + " WHERE FAC_ID = " + this.cta_prov.getFieldInt("FAC_ID");

                  } else {
                     strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO - "
                             + this.cta_prov.getFieldDouble("MP_ABONO") + "  "
                             + " WHERE FAC_ID = " + this.cta_prov.getFieldInt("FAC_ID");
                  }
                  oConn.runQueryLMD(strUpdate);
               }
               // </editor-fold>
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
         // </editor-fold>
         //Definimos campos
         this.cta_prov.setFieldInt("ID_USUARIOSANUL", 0);
         this.cta_prov.setFieldInt("MP_ANULADO", 0);
         this.cta_prov.setFieldString("MP_FECHAANUL", "");
         this.cta_prov.setFieldString("MP_HORAANUL", "");
         String strResp1 = this.cta_prov.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         } else {
            // <editor-fold defaultstate="collapsed" desc="Si es un pago buscamos si hay que hacer un movimiento">
            if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
               int intIdMovBco = 0;
               strSql = "SELECT MCB_ID from vta_mov_cta_bcos where MP_ID = " + this.cta_prov.getFieldInt("MP_ID");
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intIdMovBco = rs.getInt("MCB_ID");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
               }
               // <editor-fold defaultstate="collapsed" desc="Si hay un movimiento de banco seleccionado hacemos el movimiento">
               if (intIdMovBco != 0) {
                  Bancos banco = new Bancos(this.oConn, this.varSesiones, this.request);
                  banco.getCta_bcos().setFieldInt("MCB_ID", intIdMovBco);
                  banco.Init();
                  banco.setBolTransaccionalidad(false);
                  banco.doTrxRevive();
                  if (!banco.getStrResultLast().equals("OK")) {
                     this.strResultLast = banco.getStrResultLast();
                  }
               }
               // </editor-fold>
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
            if (this.strResultLast.equals("OK")) {
               //Actualizamos la poliza contable
               PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
               poli.setStrOper("REVIVE");
               poli.callRemote(this.cta_prov.getFieldInt("MP_ID"), PolizasContables.RECIBOS_PROV);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
            if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
               this.saveBitacora("PAGOS_PROV", "REVIVIR", this.cta_prov.getFieldInt("MP_ID"));
            } else {
               this.saveBitacora("CARGOS_PROV", "REVIVIR", this.cta_prov.getFieldInt("MP_ID"));
            }
            // </editor-fold>
         }
      } else {
         this.strResultLast = "ERROR:La operacion no esta anulada";
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
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doTrxMod() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_prov.getFieldInt("MP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por modificar";
      }
      if (this.cta_prov.getFieldInt("PV_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el cliente";
      }
      if (this.cta_prov.getFieldString("MP_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.cta_prov.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.cta_prov.getFieldDouble("MP_CARGO") == 0
              && this.cta_prov.getFieldDouble("MP_ABONO") == 0) {
         this.strResultLast = "ERROR:Falta definir un importe";
      }
      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>
         String strRes1 = this.cta_prov.Modifica(oConn);
         // <editor-fold defaultstate="collapsed" desc="Si se guardo el movimiento proseguimos">
         if (strRes1.equals("OK")) {
            int intId = this.cta_prov.getFieldInt("MP_ID");
            // <editor-fold defaultstate="collapsed" desc="Borramos los movimientos anteriores">
            String strDelete = "delete from vta_mov_prov_deta where MP_ID = " + intId;
            oConn.runQueryLMD(strDelete);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Insertamos el nuevo detalle">
            Iterator<vta_mov_prov_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_mov_prov_deta deta = it.next();
               deta.setFieldInt("MP_ID", intId);
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del proveedor">
            String strSql = "SELECT PV_SALDO from vta_proveedor where PV_ID = " + intId;
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("PV_SALDO");
                  if (this.cta_prov.getFieldDouble("MP_CARGO") > 0) {
                     dblSaldo = dblSaldo + this.cta_prov.getFieldDouble("MP_CARGO") - dblCargo + dblAbono;
                  } else {
                     dblSaldo = dblSaldo - this.cta_prov.getFieldDouble("MP_ABONO") - dblCargo + dblAbono;
                  }
                  //Actualizamos el saldo
                  String strUpdate = "UPDATE vta_proveedor set PV_SALDO = " + dblSaldo + " where PV_ID = " + intId;
                  oConn.runQueryLMD(strUpdate);
                  // <editor-fold defaultstate="collapsed" desc="Actualizamos el saldo del documento">
                  if (this.cta_prov.getFieldDouble("MP_CARGO") > 0) {
                     strUpdate = "UPDATE vta_cxpagar set CXP_SALDO = CXP_SALDO + "
                             + (this.cta_prov.getFieldDouble("MP_CARGO") - dblCargo + dblAbono) + "  "
                             + " WHERE CXP_ID = " + this.cta_prov.getFieldInt("CXP_ID");

                  } else {
                     strUpdate = "UPDATE vta_cxpagar set CXP_SALDO = CXP_SALDO - "
                             + (this.cta_prov.getFieldDouble("MP_ABONO") - dblCargo + dblAbono) + "  "
                             + " WHERE CXP_ID = " + this.cta_prov.getFieldInt("CXP_ID");
                  }
                  oConn.runQueryLMD(strUpdate);
                  // </editor-fold>
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
               // <editor-fold defaultstate="collapsed" desc="Buscamos el banco">
               int intIdMovBco = 0;
               strSql = "SELECT MCB_ID from vta_mov_cta_bcos where MP_ID = " + this.cta_prov.getFieldInt("MP_ID");
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intIdMovBco = rs.getInt("MCB_ID");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Si hay un movimiento de banco seleccionado hacemos el movimiento">
               if (intIdMovBco != 0) {
                  Bancos banco = new Bancos(this.oConn, this.varSesiones, this.request);
                  banco.getCta_bcos().setFieldInt("MCB_ID", intIdMovBco);
                  banco.Init();
                  //Asignamos el nuevo importe afectar bancos
                  banco.getCta_bcos().setFieldDouble("MCB_DEPOSITO", this.cta_prov.getFieldDouble("MP_ABONO"));
                  banco.getCta_bcos().setFieldDouble("MCB_RETIRO", this.cta_prov.getFieldDouble("MP_CARGO"));
                  banco.setBolTransaccionalidad(false);
                  banco.doTrxMod();
                  if (!banco.getStrResultLast().equals("OK")) {
                     this.strResultLast = banco.getStrResultLast();
                  }
               }
               // </editor-fold>
            }
            // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
            if (this.strResultLast.equals("OK")) {
               //Actualizamos la poliza contable
               PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
               poli.setStrOper("MOD");
               poli.callRemote(this.cta_prov.getFieldInt("MP_ID"), PolizasContables.RECIBOS_PROV);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
            if (this.cta_prov.getFieldInt("MP_ESPAGO") == 1) {
               this.saveBitacora("PAGOS_PROV", "MODIFICA", this.cta_prov.getFieldInt("MP_ID"));
            } else {
               this.saveBitacora("CARGOS_PROV", "MODIFICA", this.cta_prov.getFieldInt("MP_ID"));
            }
            // </editor-fold>
         } else {
            this.strResultLast = strRes1;
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
      }
   }

   /**
    * Ajusta el saldo a favor al usar anticipos
    *
    * @param intProv Es el id del cliente
    * @param intMonedaCXP
    * @param intMonedaPago
    * @param dblParidad Es la paridad que se uso
    */
   public void doAjustaSaldoFavor(int intProv, int intMonedaCXP, int intMonedaPago, double dblParidad) {
      this.strResultLast = "OK";
      double dblMontoUsar = this.cta_prov.getFieldDouble("MP_ABONO");
      Monedas MiTasaCambio = new Monedas(oConn);
      double dblTasaCambio = 0.0;
      log.debug("dblMontoUsar." + dblMontoUsar);
      //Consultamos los saldos a favor u anticipos
      String strQuery = "Select * From vta_mov_prov "
              + "Where PV_ID =" + intProv + " and MP_ID = " + this.cta_prov.getFieldInt("MP_ANTI_ID")
              + " and MP_ANTICIPO = 1 AND MP_ANULADO = 0 order by MP_FECHA";
      try {
         ResultSet rs = oConn.runQuery(strQuery, true);
         while (rs.next()) {
            dblTasaCambio = 1.0;

            int intIdMov = rs.getInt("MP_ID");
            log.debug("Doc." + intIdMov);
            double dblImporteAbono = rs.getDouble("MP_SALDO_ANTICIPO");
            log.debug("dblImporteAbono." + dblImporteAbono);
            log.debug("Moneda mov." + rs.getInt("MP_MONEDA") + " moneda pago " + intMonedaPago);
            if (rs.getInt("MP_MONEDA") != intMonedaPago) {
               if (intMonedaCXP == rs.getInt("MP_MONEDA")) {
                  dblTasaCambio = dblParidad;
                  if (intMonedaPago == 1) {
                     dblImporteAbono = dblImporteAbono * dblTasaCambio;
                  } else {
                     dblImporteAbono = dblImporteAbono / dblTasaCambio;
                  }
               } else {
                  dblTasaCambio = MiTasaCambio.GetFactorConversion(4, rs.getInt("MP_MONEDA"), intMonedaPago);
                  dblImporteAbono = dblImporteAbono * dblTasaCambio;
               }
            }
            log.debug("dblTasaCambio." + dblTasaCambio + " dblImporteAbono." + dblImporteAbono);
            //Algoritmo para usar el saldo
            double dblMontoUsado = 0;
            double dblMontoAbonoRest = dblImporteAbono;
            if (dblMontoUsar > dblImporteAbono) {
               dblMontoUsado = dblImporteAbono;
               dblMontoAbonoRest = 0;
               dblMontoUsar = dblMontoUsar - dblMontoUsado;
            } else {
               dblMontoUsado = dblMontoUsar;
               dblMontoAbonoRest = dblImporteAbono - dblMontoUsado;
               dblMontoUsar = 0;
            }
            log.debug("dblMontoUsar." + dblMontoUsar);
            log.debug("dblMontoUsado." + dblMontoUsado);
            log.debug("dblMontoAbonoRest." + dblMontoAbonoRest);
            //Quitamos el abono del movimiento
            //Realizamps la conversion en caso de aplicar
            if (rs.getInt("MP_MONEDA") != intMonedaPago) {
               if (intMonedaCXP == rs.getInt("MP_MONEDA")) {
                  if (rs.getInt("MP_MONEDA") == 1) {
                     dblMontoAbonoRest = dblMontoAbonoRest * dblTasaCambio;
                  } else {
                     dblMontoAbonoRest = dblMontoAbonoRest / dblTasaCambio;
                  }
               } else {
                  dblTasaCambio = MiTasaCambio.GetFactorConversion(4, intMonedaPago, rs.getInt("MP_MONEDA"));
                  dblMontoAbonoRest = dblMontoAbonoRest * dblTasaCambio;
               }
            }
            log.debug("dblMontoUsado guardar...." + dblMontoAbonoRest);
            //Actualizamos el anticipo
            String strUpdate = "update vta_mov_prov "
                    + " set MP_SALDO_ANTICIPO = " + dblMontoAbonoRest + ""
                    + " where MP_ID = " + intIdMov;
            this.oConn.runQueryLMD(strUpdate);
            //Si ya no hay mas monto por usar
            if (dblMontoUsar == 0) {
               break;
            }
         }

      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
      }
      //
   }

   /**
    * Lee los datos del xml que se pasa como path
    *
    * @param strPath Es el path del xml por leer
    */
   public void CargaDatosXML(String strPath) {
      try {
         File fXmlFile = new File(strPath);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         dbFactory.setNamespaceAware(true);
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(fXmlFile);
         doc.setStrictErrorChecking(false);
         NodeList nList = doc.getDocumentElement().getChildNodes();
         NodeList nlNL = doc.getElementsByTagName("cfdi:Comprobante");
         if (nlNL != null) {
            for (int temp = 0; temp < nlNL.getLength(); temp++) {
               Node nNode = nlNL.item(temp);
               log.debug("subTotal.");
               Node nodoTmp = nNode.getAttributes().getNamedItem("subTotal");
               this.dblSubTotal = Double.valueOf(nodoTmp.getTextContent());
            }
         }

         nlNL = doc.getElementsByTagName("cfdi:Impuestos");
         if (nlNL != null) {
            for (int temp = 0; temp < nlNL.getLength(); temp++) {
               Node nNode = nlNL.item(temp);
               log.debug("totalImpuestosTrasladados.");
               Node nodoTmp = nNode.getAttributes().getNamedItem("totalImpuestosTrasladados");
               if (nodoTmp != null) {
                  this.dblIVA = Double.valueOf(nodoTmp.getTextContent());
               }
            }
         }
         Node nodoTmp;
         if (nList != null) {
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               if (nNode.getNodeName().equals("cfdi:Emisor")) {
                  log.debug("rfc.");
                  nodoTmp = nNode.getAttributes().getNamedItem("rfc");
                  this.strRFCEmisor = nodoTmp.getTextContent();
               }
               if (nNode.getNodeName().equals("cfdi:Complemento")) {

                  NodeList nListInt = nNode.getChildNodes();
                  for (int temp2 = 0; temp2 < nListInt.getLength(); temp2++) {
                     Node nNodeInt = nListInt.item(temp2);
                     if (nNodeInt.getNodeName().equals("tfd:TimbreFiscalDigital")) {
                        log.debug("UUID.");
                        nodoTmp = nNodeInt.getAttributes().getNamedItem("UUID");
                        this.strUUID = nodoTmp.getTextContent();
                     }
                  }
               }
            }
         }
      } catch (Exception e) {
         log.error("error " + e.getMessage() + " " + e.getLocalizedMessage());
      }
   }

   /**
    * Vincula las cxpagar con los xml que no esten vinculados
    *
    * @param strPath ES el path donde esta el xml
    */
   public void VinculaXML(String strPath) {
      String strSql = "select * from vta_cxpagar where CXP_UUID <> '' ";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            String strUUIDFind = rs.getString("CXP_UUID");
            boolean bolFound = false;
            strSql = "select CPR_DESCRIPCION,CPR_PATH,CPR_TAMANIO from vta_cxp_rep where CXP_ID = " + rs.getInt("CXP_ID");
            ResultSet rs2 = this.oConn.runQuery(strSql);
            while (rs2.next()) {
               log.debug("Doc encontrado " + rs2.getString("CPR_DESCRIPCION"));
               bolFound = true;
            }
            rs2.close();

            //Buscamos el uuid en los xml
            if (!bolFound) {
               String files;
               File folder = new File(strPath);
               File[] listOfFiles = folder.listFiles();

               for (File listOfFile : listOfFiles) {
                  if (listOfFile.isFile()) {
                     files = listOfFile.getName();
                     if (files.toUpperCase().endsWith(".XML")) {
                        MovProveedor mov = new MovProveedor(oConn, null, null);
                        mov.CargaDatosXML(listOfFile.getAbsolutePath());
                        if (mov.getStrUUID().equals(strUUIDFind)) {
                           CuentasXPagarDoc CPR = new CuentasXPagarDoc();
                           CPR.getCPR().setFieldString("CPR_DESCRIPCION", files);
                           CPR.getCPR().setFieldString("CPR_PATH", listOfFile.getAbsolutePath());
                           CPR.getCPR().setFieldInt("CXP_ID", rs.getInt("CXP_ID"));
                           CPR.getCPR().setFieldDouble("CPR_TAMANIO", 0);
                           CPR.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                           CPR.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                           CPR.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                           CPR.saveCXPDoc(oConn);
                        }
                     }
                  }
               }
            }

         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }
   // </editor-fold>
}
