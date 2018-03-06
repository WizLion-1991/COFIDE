package ERP;

import Tablas.vta_mov_cte;
import Tablas.vta_mov_cte_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
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
import org.apache.logging.log4j.LogManager;

/**
 * Representa las operaciones que podemos realizar con los clientes(pagos y
 * cargos)
 *
 * @author zeus
 */
public class movCliente extends ProcesoMaster implements ProcesoInterfaz {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   protected vta_mov_cte cta_clie;//representa el documento de bancos
   protected ArrayList<vta_mov_cte_deta> lstMovs;//representa el detalle del movimiento de bancos
   protected double dblCargo = 0;
   protected double dblAbono = 0;
   protected String strFechaAnul;
   protected int intBc_Id = 0;
   protected boolean bolCaja = false;
   protected boolean bolEsLocal = false;
   protected boolean bolLinkPedidos = false;
   protected boolean bolAplicaBanco = true;
   protected boolean bolEsAnticipo = false;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(movCliente.class.getName());
   protected double dblSaldoFavorUsado = 0;
   protected boolean bolAplicaConta = true;

   /**
    * Regresa la indicación si se genera la contabilidad
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
    * Regresa el movimiento maestro de clientes
    *
    * @return Nos regresa el movimiento de clientes
    */
   public vta_mov_cte getCta_clie() {
      return cta_clie;
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(vta_mov_cte_deta deta) {
      this.lstMovs.add(deta);
   }

   /**
    * Nos dice si la operacion es de caja
    *
    * @return true/false
    */
   public boolean isBolCaja() {
      return bolCaja;
   }

   /**
    * Definimos si la operacion es de caja
    *
    * @param bolCaja true/false
    */
   public void setBolCaja(boolean bolCaja) {
      this.bolCaja = bolCaja;
   }

   /**
    * Nos regresa el id del banco
    *
    * @return Es un valor entero con el id del banco
    */
   public int getIntBc_Id() {
      return intBc_Id;
   }

   /**
    * Definimos el id del banco relacionado con este pago
    *
    * @param intBc_Id Es un valor entero con el id del banco
    */
   public void setIntBc_Id(int intBc_Id) {
      this.intBc_Id = intBc_Id;
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
    * Nos indica si se vincula el anticipo con los pedidos pendientes por
    * facturar del cliente
    *
    * @return Es un valor boolean
    */
   public boolean isBolLinkPedidos() {
      return bolLinkPedidos;
   }

   /**
    * Vincula el anticipo con los pedidos pendientes por facturar del cliente
    *
    * @param bolLinkPedidos Es un valor boolean
    */
   public void setBolLinkPedidos(boolean bolLinkPedidos) {
      this.bolLinkPedidos = bolLinkPedidos;
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

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor del objeto que lleva el registro de cuenta corriente del
    * cliente
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion
    */
   public movCliente(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.cta_clie = new vta_mov_cte();
      this.lstMovs = new ArrayList<vta_mov_cte_deta>();
      bolAplicaConta = true;
   }

   /**
    * Constructor del objeto que lleva el registro de cuenta corriente del
    * cliente
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    */
   public movCliente(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones, null);
      this.cta_clie = new vta_mov_cte();
      this.lstMovs = new ArrayList<vta_mov_cte_deta>();
      bolAplicaConta = true;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void Init() {
      this.cta_clie.setFieldInt("ID_USUARIOS", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.cta_clie.getFieldInt("MC_ID") != 0) {
         this.dblCargo = this.cta_clie.getFieldDouble("MC_CARGO");
         this.dblAbono = this.cta_clie.getFieldDouble("MC_ABONO");
         this.strFechaAnul = this.cta_clie.getFieldString("MC_FECHAANUL");
         this.cta_clie.ObtenDatos(this.cta_clie.getFieldInt("MC_ID"), oConn);
      }
   }

   public void doTrx() {

      // <editor-fold defaultstate="collapsed" desc="Valores iniciales">
      this.strResultLast = "OK";
      this.cta_clie.setFieldString("MC_FECHACREATE", this.fecha.getFechaActual());
      this.cta_clie.setFieldString("MC_HORA", this.fecha.getHoraActual());
      int intMonedaVta = 0;
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_clie.getFieldString("MC_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      //No debe dejar movimientos posteriores a la fecha de elaboracion
      if (!this.cta_clie.getFieldString("MC_FECHACREATE").equals("")) {
         SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
         try {
            Date fechaCreate = formatoFecha.parse(this.cta_clie.getFieldString("MC_FECHACREATE"));
            Date fechaMov = formatoFecha.parse(this.cta_clie.getFieldString("MC_FECHA"));
            if (fechaMov.after(fechaCreate)) {
               this.strResultLast = "ERROR:La fecha del movimiento es mayor a la fecha de creacion " + this.fecha.FormateaDDMMAAAA(this.cta_clie.getFieldString("MC_FECHA"), "/");
            }
         } catch (ParseException e) {
            this.strResultLast = "ERROR:La fecha del movimiento es erronea " + this.fecha.FormateaDDMMAAAA(this.cta_clie.getFieldString("MC_FECHA"), "/");
         }

      }
      //Aplican solo cuando no es un pago entonces si recuperamos datos de la bd
      if (this.cta_clie.getFieldInt("MC_ESPAGO") == 0) {
         if (this.cta_clie.getFieldInt("CT_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir el cliente";
         }
         if (this.cta_clie.getFieldInt("SC_ID") == 0) {
            this.strResultLast = "ERROR:Falta definir la sucursal";
         }
      } else {
         //Evaluamos campos basicos para un pago
         if (this.cta_clie.getFieldInt("CT_ID") == 0) {
            if (this.cta_clie.getFieldInt("FAC_ID") == 0 && this.cta_clie.getFieldInt("TKT_ID") == 0
                    && this.cta_clie.getFieldInt("PD_ID") == 0
                    && this.cta_clie.getFieldInt("MC_ANTICIPO") == 0) {
               this.strResultLast = "ERROR:Falta definir el id de la factura o el ticket";
            }
         }

      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_clie.getFieldString("MC_FECHA"), this.cta_clie.getFieldInt("EMP_ID"), this.cta_clie.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
//      if (this.cta_clie.getFieldDouble("MC_CARGO") == 0
//              && this.cta_clie.getFieldDouble("MC_ABONO") == 0) {
//         this.strResultLast = "ERROR:Falta definir un importe";
//      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Validamos si pasamos las validaciones">
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         this.cta_clie.setBolGetAutonumeric(true);
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Validamos si es un pago para operaciones adicionales">
         if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
            // <editor-fold defaultstate="collapsed" desc="Definimos el folio de la operacion">
            Folios folio = new Folios();
            folio.setBolEsLocal(bolEsLocal);
            if (this.cta_clie.getFieldString("MC_FOLIO").equals("")) {
               String strFolio = folio.doFolio(oConn, Folios.RECIBOS, this.bolFolioGlobal, this.cta_clie.getFieldInt("SC_ID"));
               this.cta_clie.setFieldString("MC_FOLIO", strFolio);
            } else {
               folio.updateFolio(oConn, Folios.RECIBOS, this.cta_clie.getFieldString("MC_FOLIO"), this.bolFolioGlobal, this.cta_clie.getFieldInt("SC_ID"));
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Calculamos la proporcion del pago con relacion a los impuestos">
            // <editor-fold defaultstate="collapsed" desc="Recuperamos informacion de la transaccion como el cliente y la sucursal">
            String strPrefijo = "TKT_";
            String strSql = "select CT_ID,SC_ID,TKT_IMPUESTO1,TKT_IMPUESTO2,TKT_IMPUESTO3,"
                    + "TKT_TASA1,TKT_TASA2,TKT_TASA3,TKT_TOTAL,TKT_MONEDA,EMP_ID from vta_tickets "
                    + "WHERE TKT_ID =" + this.cta_clie.getFieldInt("TKT_ID");
            if (this.cta_clie.getFieldInt("FAC_ID") != 0) {
               //FACTURA
               strPrefijo = "FAC_";
               strSql = "select CT_ID,SC_ID,FAC_IMPUESTO1,FAC_IMPUESTO2,FAC_IMPUESTO3,"
                       + "FAC_TASA1,FAC_TASA2,FAC_TASA3,FAC_TOTAL,FAC_MONEDA,EMP_ID from vta_facturas "
                       + "WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
            }
            //Como es un anticipo recuperamos solo datos del cliente
            if (this.cta_clie.getFieldInt("TKT_ID") == 0
                    && this.cta_clie.getFieldInt("FAC_ID") == 0) {
               strSql = "select CT_ID,SC_ID,EMP_ID from vta_cliente "
                       + " WHERE CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
            }
            // </editor-fold>
            //Datos que obtendremos del sql
            int intIdSuc = 0;
            int intIdCte = 0;
            int intIdEmp = 0;
            double dblImpGlob1 = 0;
            double dblImpGlob2 = 0;
            double dblImpGlob3 = 0;
            double dblImp1 = 0;
            double dblImp2 = 0;
            double dblImp3 = 0;
            double dblTasaImp1 = 0;
            double dblTasaImp2 = 0;
            double dblTasaImp3 = 0;

            // <editor-fold defaultstate="collapsed" desc="Consultamos los datos de la venta">
            if (!this.bolEsAnticipo) {
               ResultSet rs;
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intIdSuc = rs.getInt("SC_ID");
                     intIdEmp = rs.getInt("EMP_ID");
                     intIdCte = rs.getInt("CT_ID");

                     //Solo si fon tickets o facturas
                     if (this.cta_clie.getFieldInt("TKT_ID") != 0
                             || this.cta_clie.getFieldInt("FAC_ID") != 0) {
                        dblImpGlob1 = rs.getDouble(strPrefijo + "IMPUESTO1");
                        dblImpGlob2 = rs.getDouble(strPrefijo + "IMPUESTO2");
                        dblImpGlob3 = rs.getDouble(strPrefijo + "IMPUESTO3");
                        dblTasaImp1 = rs.getDouble(strPrefijo + "TASA1");
                        dblTasaImp2 = rs.getDouble(strPrefijo + "TASA2");
                        dblTasaImp3 = rs.getDouble(strPrefijo + "TASA3");
                        intMonedaVta = rs.getInt(strPrefijo + "MONEDA");
                        //Calculamos el impuesto proporcional al pago
                        if (rs.getDouble(strPrefijo + "TOTAL") > 0) {
                           //Divimos el monto del pago entre el importe de la venta
                           double dblFactorPago = this.cta_clie.getFieldDouble("MC_ABONO") / rs.getDouble(strPrefijo + "TOTAL");
                           //Aplicamos este factor al total del impuesto
                           dblImp1 = dblImpGlob1 * dblFactorPago;
                           dblImp2 = dblImpGlob2 * dblFactorPago;
                           dblImp3 = dblImpGlob3 * dblFactorPago;
                        }
                     }

                  }
                  rs.close();


                  this.cta_clie.setFieldInt("SC_ID", intIdSuc);
                  this.cta_clie.setFieldInt("EMP_ID", intIdEmp);
                  this.cta_clie.setFieldInt("CT_ID", intIdCte);
                  this.cta_clie.setFieldDouble("MC_TASAIMPUESTO1", dblTasaImp1);
                  this.cta_clie.setFieldDouble("MC_TASAIMPUESTO2", dblTasaImp2);
                  this.cta_clie.setFieldDouble("MC_TASAIMPUESTO3", dblTasaImp3);
                  this.cta_clie.setFieldDouble("MC_IMPUESTO1", dblImp1);
                  this.cta_clie.setFieldDouble("MC_IMPUESTO2", dblImp2);
                  this.cta_clie.setFieldDouble("MC_IMPUESTO3", dblImp3);
               } catch (SQLException ex) {
                  Logger.getLogger(movCliente.class.getName()).log(Level.SEVERE, null, ex);
               }
            } else {
               ResultSet rs;
               try {
                  rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intIdSuc = rs.getInt("SC_ID");
                     intIdEmp = rs.getInt("EMP_ID");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(movCliente.class.getName()).log(Level.SEVERE, null, ex);
               }
               //Asignamos el importe original
               this.cta_clie.setFieldDouble("MC_ANTICIPO_ORIGINAL", this.cta_clie.getFieldDouble("MC_ABONO"));
               this.cta_clie.setFieldDouble("MC_SALDO_ANTICIPO", this.cta_clie.getFieldDouble("MC_ABONO"));
               this.cta_clie.setFieldInt("SC_ID", intIdSuc);
               this.cta_clie.setFieldInt("EMP_ID", intIdEmp);
            }
            // </editor-fold>
            // </editor-fold>
         } else {
            //Definimos la moneda de venta igual a la de la factura
            intMonedaVta = this.cta_clie.getFieldInt("MC_MONEDA");
         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Agregamos el movimiento">
         String strRes1 = this.cta_clie.Agrega(oConn);
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Si fue exitoso proseguimos">
         if (strRes1.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Guardamos las formas de pago"> 
            int intId = Integer.valueOf(this.cta_clie.getValorKey());
            Iterator<vta_mov_cte_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_mov_cte_deta deta = it.next();
               deta.setFieldInt("MC_ID", intId);
               deta.setFieldInt("SC_ID", this.cta_clie.getFieldInt("SC_ID"));
               deta.setFieldInt("CT_ID", this.cta_clie.getFieldInt("CT_ID"));
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del cliente">
            String strSql = "SELECT CT_SALDO,MON_ID from vta_cliente where CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("CT_SALDO");
                  double dblCargoProv = this.cta_clie.getFieldDouble("MC_CARGO");
                  double dblAbonoProv = this.cta_clie.getFieldDouble("MC_ABONO");
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  int intMonedaCte = rs.getInt("MON_ID");
                  if (intMonedaCte == 0) {
                     intMonedaCte = 1;
                  }
                  if (this.cta_clie.getFieldInt("MC_MONEDA") == 0) {
                     this.cta_clie.setFieldInt("MC_MONEDA", 1);
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaCte + " " + this.cta_clie.getFieldInt("MC_MONEDA"));
                  if (intMonedaCte != this.cta_clie.getFieldInt("MC_MONEDA")) {
                     double dblFactor = 0;
                     if (intMonedaVta == intMonedaCte) {
                        dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
                     } else {
                        Monedas conversion = new Monedas(oConn);
                        dblFactor = conversion.GetFactorConversion(this.cta_clie.getFieldString("MC_FECHA"), 4, this.cta_clie.getFieldInt("MC_MONEDA"), intMonedaCte);
                     }
                     log.debug("Factor de conversion " + dblFactor);
                     dblCargoProv = dblCargoProv * dblFactor;
                     dblAbonoProv = dblAbonoProv * dblFactor;
                  }
                  // </editor-fold>
                  if (dblCargoProv > 0) {
                     dblSaldo = dblSaldo + dblCargoProv;
                  } else {
                     dblSaldo = dblSaldo - dblAbonoProv;
                  }
                  // <editor-fold defaultstate="collapsed" desc="Actualizamos el saldo">
                  String strUpdate = "UPDATE vta_cliente set CT_SALDO = " + dblSaldo + " where CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
                  oConn.runQueryLMD(strUpdate);

                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  double dblCargoDeta = this.cta_clie.getFieldDouble("MC_CARGO");
                  double dblAbonoDeta = this.cta_clie.getFieldDouble("MC_ABONO");
                  if (intMonedaVta == 0) {
                     intMonedaVta = 1;
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaVta + " " + this.cta_clie.getFieldInt("MC_MONEDA"));
                  if (intMonedaVta != this.cta_clie.getFieldInt("MC_MONEDA")) {
//                     Monedas conversion = new Monedas(oConn);
                     double dblFactor = 0;
//                     if (intMonedaVta == this.cta_clie.getFieldInt("MC_MONEDA")) {
                     dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
//                     } else {
//                        dblFactor = conversion.GetFactorConversion(this.cta_clie.getFieldString("MC_FECHA"), 4, this.cta_clie.getFieldInt("MC_MONEDA"), intMonedaCte);
//                     }
                     if (this.cta_clie.getFieldInt("MC_MONEDA") == 1 || (this.cta_clie.getFieldInt("MC_MONEDA") == 2 && intMonedaVta == 3)) {
                        dblCargoDeta = dblCargoDeta / dblFactor;
                        dblAbonoDeta = dblAbonoDeta / dblFactor;
                     } else {
                        dblCargoDeta = dblCargoDeta * dblFactor;
                        dblAbonoDeta = dblAbonoDeta * dblFactor;
                     }
                     log.debug("Factor de conversion " + dblFactor);
                  }
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="En caso de venir de un ticket actualizamos el saldo">
                  if (this.cta_clie.getFieldInt("TKT_ID") != 0) {
                     //Actualizamos el saldo de la venta
                     if (dblCargoDeta > 0) {
                        strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO + "
                                + dblCargoDeta + "  "
                                + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");

                     } else {
                        strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO - "
                                + dblAbonoDeta + "   "
                                + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");
                     }
                     oConn.runQueryLMD(strUpdate);
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="En caso de venir de una factura actualizamos el saldo">
                  if (this.cta_clie.getFieldInt("FAC_ID") != 0) {
                     //Actualizamos el saldo de la venta
                     if (dblCargoDeta > 0) {
                        strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO + "
                                + dblCargoDeta + "  "
                                + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");

                     } else {
                        strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO - "
                                + dblAbonoDeta + "  "
                                + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
                     }
                     oConn.runQueryLMD(strUpdate);
                  }
                  // </editor-fold>
                  // </editor-fold>
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Afectamos bancos">
            //Evaluamos si el movimiento esta usando un anticipo para no afectar bancos
            if (this.cta_clie.getFieldInt("MC_USA_ANTICIPO") == 1) {
               this.bolAplicaBanco = false;
            }
            int intIdBanco = this.intBc_Id;
            //Validamos si aplica el movimiento de bancos
            if (this.bolAplicaBanco) {
               if (this.strResultLast.equals("OK")) {
                  if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
                     // <editor-fold defaultstate="collapsed" desc="Si es caja buscamos la caja">
                     if (this.bolCaja) {
                        strSql = "SELECT BC_ID from vta_bcos where BC_ESCAJA = 1 AND SC_ID = " + this.cta_clie.getFieldInt("SC_ID");
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
                        banco.getCta_bcos().setFieldString("MCB_FECHA", this.cta_clie.getFieldString("MC_FECHA"));
                        banco.getCta_bcos().setFieldInt("SC_ID", this.cta_clie.getFieldInt("SC_ID"));
                        banco.getCta_bcos().setFieldInt("BC_ID", intIdBanco);
                        banco.getCta_bcos().setFieldInt("EMP_ID", this.cta_clie.getFieldInt("EMP_ID"));
                        banco.getCta_bcos().setFieldInt("FAC_ID", this.cta_clie.getFieldInt("FAC_ID"));
                        banco.getCta_bcos().setFieldInt("TKT_ID", this.cta_clie.getFieldInt("TKT_ID"));
                        banco.getCta_bcos().setFieldInt("PD_ID", this.cta_clie.getFieldInt("PD_ID"));
                        banco.getCta_bcos().setFieldInt("MC_ID", this.cta_clie.getFieldInt("MC_ID"));
                        banco.getCta_bcos().setFieldInt("MCB_MONEDA", this.cta_clie.getFieldInt("MC_MONEDA"));
                        banco.getCta_bcos().setFieldDouble("MCB_DEPOSITO", this.cta_clie.getFieldDouble("MC_ABONO"));
                        banco.getCta_bcos().setFieldDouble("MCB_RETIRO", this.cta_clie.getFieldDouble("MC_CARGO"));
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

            // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
            if (this.strResultLast.equals("OK")) {
               //Solo aplica si es un pago
               if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
                  // <editor-fold defaultstate="collapsed" desc="Evaluamos si se ocupo un saldo a favor para ajustarlos">
//                  if (this.dblSaldoFavorUsado > 0) {
//                     this.doAjustaSaldoFavor(this.cta_clie.getFieldInt("CT_ID"), intMonedaVta,
//                             this.cta_clie.getFieldInt("MC_MONEDA"), this.cta_clie.getFieldInt("MC_TASAPESO"));
//                  }
                  // </editor-fold>   
                  //Solo si no hubo errores
                  if (this.strResultLast.equals("OK")) {
                     if (this.bolAplicaConta) {
                        // <editor-fold defaultstate="collapsed" desc="Calculamos la contabilidad">
                        //Objeto para calculo de poliza contable
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        contaUtil.CalculaPolizaContableCobros(this.cta_clie.getFieldInt("MC_MONEDA"), this.cta_clie, intId, "NEW");
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
            // <editor-fold defaultstate="collapsed" desc="Si es una anticipo y esta activada la vinculacion, se vincula el pedido con el pago">
            if (bolLinkPedidos && this.cta_clie.getFieldInt("MC_ANTICIPO") == 1) {
               this.doLinkPaymentsPedidos(this.cta_clie);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del anticipo">
            if (this.cta_clie.getFieldInt("MC_USA_ANTICIPO") == 1) {
               vta_mov_cte movAnti = new vta_mov_cte();
               movAnti.ObtenDatos(this.cta_clie.getFieldInt("MC_ANTI_ID"), oConn);
               movAnti.setFieldDouble("MC_SALDO_ANTICIPO", movAnti.getFieldDouble("MC_SALDO_ANTICIPO") - this.cta_clie.getFieldDouble("MC_ABONO"));
               movAnti.Modifica(oConn);

               //Generamos movimiento de cargo
               double dblImportePago = this.cta_clie.getFieldDouble("MC_ABONO");
               int intMonedaPago = this.cta_clie.getFieldInt("MC_MONEDA");
               int intFac_IdPago = this.cta_clie.getFieldInt("FAC_ID");
               int intTkt_IdPago = this.cta_clie.getFieldInt("TKT_ID");
               this.cta_clie.setFieldDouble("MC_CARGO", dblImportePago);
               this.cta_clie.setFieldDouble("MC_ABONO", 0);
               this.cta_clie.setFieldInt("MC_USO_ANTI_ID", intId);
               this.cta_clie.setFieldInt("MC_ESPAGO", 0);
               this.cta_clie.setFieldInt("FAC_ID", 0);
               this.cta_clie.setFieldInt("TKT_ID", 0);
//               if (intMonedaVta != this.cta_clie.getFieldInt("MC_MONEDA")) {
//                  double dblImportePagoConv = dblImportePago;
//                  double dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
//                  log.debug("Factor de conversion " + dblFactor);
//                  if (this.cta_clie.getFieldInt("MC_MONEDA") == 1) {
//                     dblImportePagoConv = dblImportePagoConv / dblFactor;
//                  } else {
//                     dblImportePagoConv = dblImportePagoConv * dblFactor;
//                  }
//                  this.cta_clie.setFieldDouble("MC_CARGO", dblImportePagoConv);
//                  this.cta_clie.setFieldInt("MC_MONEDA", intMonedaVta);
//               }
               String strRes2 = this.cta_clie.Agrega(oConn);
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               } else {
                  this.cta_clie.setFieldDouble("MC_ABONO", dblImportePago);
                  this.cta_clie.setFieldDouble("MC_CARGO", 0);
                  this.cta_clie.setFieldInt("MC_MONEDA", intMonedaPago);
                  this.cta_clie.setFieldInt("MC_ESPAGO", 1);
                  this.cta_clie.setFieldInt("FAC_ID", intFac_IdPago);
                  this.cta_clie.setFieldInt("TKT_ID", intTkt_IdPago);
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
            if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
               this.saveBitacora("PAGOS", "NUEVA", intId);
            } else {
               this.saveBitacora("CARGOS", "NUEVA", intId);
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

   public void doTrxAnul() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_clie.getFieldInt("MC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      if (this.cta_clie.getFieldInt("MC_MONEDA") == 0) {
         this.strResultLast = "ERROR:Falta definir la moneda de la operacion";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_clie.getFieldString("MC_FECHA"), this.cta_clie.getFieldInt("EMP_ID"), this.cta_clie.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Validamos si es un anticipo para buscar si hay movimientos relacionados
      if (this.cta_clie.getFieldInt("MC_ANTICIPO") == 1) {
         //Contamos los movimientos que usan el anticipo
         int intCuantos = 0;
         String strSAnti = "select count(MC_ID) as cuantos from vta_mov_cte where MC_ANTI_ID = " + this.cta_clie.getFieldInt("MC_ID") + " AND MC_ANULADO = 0";
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
         if (this.cta_clie.getFieldInt("MC_ANULADO") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del cliente">
            String strSql = "SELECT CT_SALDO,MON_ID from vta_cliente where CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("CT_SALDO");
                  double dblCargoProv = this.cta_clie.getFieldDouble("MC_CARGO");
                  double dblAbonoProv = this.cta_clie.getFieldDouble("MC_ABONO");

                  // <editor-fold defaultstate="collapsed" desc="Consultamos la informacion del documento">
                  int intMonedaVta = 0;
                  String strPrefijo = "TKT_";
                  strSql = "select CT_ID,SC_ID,TKT_IMPUESTO1,TKT_IMPUESTO2,TKT_IMPUESTO3,"
                          + "TKT_TASA1,TKT_TASA2,TKT_TASA3,TKT_TOTAL,TKT_MONEDA from vta_tickets "
                          + "WHERE TKT_ID =" + this.cta_clie.getFieldInt("TKT_ID");
                  if (this.cta_clie.getFieldInt("FAC_ID") != 0) {
                     //FACTURA
                     strPrefijo = "FAC_";
                     strSql = "select CT_ID,SC_ID,FAC_IMPUESTO1,FAC_IMPUESTO2,FAC_IMPUESTO3,"
                             + "FAC_TASA1,FAC_TASA2,FAC_TASA3,FAC_TOTAL,FAC_MONEDA from vta_facturas "
                             + "WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
                  }
                  //Como es un anticipo recuperamos solo datos del cliente
                  if (this.cta_clie.getFieldInt("TKT_ID") == 0
                          && this.cta_clie.getFieldInt("FAC_ID") == 0) {
                     strSql = "select CT_ID,SC_ID from vta_cliente "
                             + " WHERE CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
                  }
                  try {
                     ResultSet rs2 = oConn.runQuery(strSql, true);
                     while (rs2.next()) {
                        //Solo si fon tickets o facturas
                        if (this.cta_clie.getFieldInt("TKT_ID") != 0
                                || this.cta_clie.getFieldInt("FAC_ID") != 0) {

                           intMonedaVta = rs2.getInt(strPrefijo + "MONEDA");
                        }

                     }
                     rs2.close();
                  } catch (SQLException ex) {
                     Logger.getLogger(movCliente.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  int intMonedaCte = rs.getInt("MON_ID");
                  if (intMonedaCte == 0) {
                     intMonedaCte = 1;
                  }
                  if (this.cta_clie.getFieldInt("MC_MONEDA") == 0) {
                     this.cta_clie.setFieldInt("MC_MONEDA", 1);
                  }
                  log.debug("(Cliente)Evaluamos conversion de monedas " + intMonedaCte + " " + this.cta_clie.getFieldInt("MC_MONEDA"));
                  if (intMonedaCte != this.cta_clie.getFieldInt("MC_MONEDA")) {
                     double dblFactor = 0;
                     if (intMonedaVta == intMonedaCte) {
                        dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
                     } else {
                        Monedas conversion = new Monedas(oConn);
                        dblFactor = conversion.GetFactorConversion(this.cta_clie.getFieldString("MC_FECHA"), 4, this.cta_clie.getFieldInt("MC_MONEDA"), intMonedaCte);
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
                  String strUpdate = "UPDATE vta_cliente set CT_SALDO = " + dblSaldo + " where CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
                  oConn.runQueryLMD(strUpdate);


                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  double dblCargoDeta = this.cta_clie.getFieldDouble("MC_CARGO");
                  double dblAbonoDeta = this.cta_clie.getFieldDouble("MC_ABONO");
                  if (intMonedaVta == 0) {
                     intMonedaVta = 1;
                  }
                  log.debug("(MovCte)Evaluamos conversion de monedas " + intMonedaVta + " " + this.cta_clie.getFieldInt("MC_MONEDA"));
                  if (intMonedaVta != this.cta_clie.getFieldInt("MC_MONEDA")) {
//                  Monedas conversion = new Monedas(oConn);
//                  double dblFactor = conversion.GetFactorConversion(this.cta_clie.getFieldString("MC_FECHA"), 4, this.cta_clie.getFieldInt("MC_MONEDA"), intMonedaVta);
                     double dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
                     log.debug("Factor de conversion " + dblFactor);
                     if (this.cta_clie.getFieldInt("MC_MONEDA") == 1) {
                        dblCargoDeta = dblCargoDeta / dblFactor;
                        dblAbonoDeta = dblAbonoDeta / dblFactor;
                     } else {
                        dblCargoDeta = dblCargoDeta * dblFactor;
                        dblAbonoDeta = dblAbonoDeta * dblFactor;
                     }
                     log.debug("Factor de conversion " + this.cta_clie.getFieldDouble("MC_ABONO") + " " + dblAbonoDeta);
                  }
                  // </editor-fold>

                  // <editor-fold defaultstate="collapsed" desc="En caso de venir de un ticket actualizamos el saldo">
                  if (this.cta_clie.getFieldInt("TKT_ID") != 0) {
                     //Actualizamos el saldo de la venta
                     if (dblCargoDeta > 0) {
                        strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO - "
                                + dblCargoDeta + "  "
                                + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");

                     } else {
                        strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO + "
                                + dblAbonoDeta + "   "
                                + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");
                     }
                     oConn.runQueryLMD(strUpdate);
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="En caso de venir de una factura actualizamos el saldo">
                  if (this.cta_clie.getFieldInt("FAC_ID") != 0) {
                     if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1 || this.cta_clie.getFieldInt("NC_ID") != 0) {
                        strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO + "
                                + dblAbonoDeta + "  "
                                + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
                        oConn.runQueryLMD(strUpdate);
                     } else {
                        //Actualizamos el saldo de la venta
                        if (dblCargoDeta > 0) {
                           strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO - "
                                   + dblCargoDeta + "  "
                                   + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");

                        } else {
                           strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO + "
                                   + dblCargoDeta + "   "
                                   + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
                        }
                        oConn.runQueryLMD(strUpdate);

                     }
                  }
                  // </editor-fold>
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Evaluamos si uso anticipo para devolverlo">
            if (this.cta_clie.getFieldInt("MC_USA_ANTICIPO") == 1) {
               vta_mov_cte movAnti = new vta_mov_cte();
               movAnti.ObtenDatos(this.cta_clie.getFieldInt("MC_ANTI_ID"), oConn);
               movAnti.setFieldDouble("MC_SALDO_ANTICIPO", movAnti.getFieldDouble("MC_SALDO_ANTICIPO") + this.cta_clie.getFieldDouble("MC_ABONO"));
               movAnti.Modifica(oConn);

               //Cancelamos el cargo generado al usar el anticipo
               vta_mov_cte movCargo = movAnti.getMovCargoAnticipo(this.cta_clie.getFieldInt("MC_ID"), oConn);
               movCargo.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
               movCargo.setFieldInt("MC_ANULADO", 1);
               movCargo.setFieldString("MC_HORAANUL", this.fecha.getHoraActual());
               if (strFechaAnul.equals("")) {
                  movCargo.setFieldString("MC_FECHAANUL", this.fecha.getFechaActual());
               } else {
                  movCargo.setFieldString("MC_FECHAANUL", this.fecha.getFechaActual());
               }
               String strResp1 = movCargo.Modifica(oConn);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Definimos campos">
            this.cta_clie.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
            this.cta_clie.setFieldInt("MC_ANULADO", 1);
            this.cta_clie.setFieldString("MC_HORAANUL", this.fecha.getHoraActual());
            if (strFechaAnul.equals("")) {
               this.cta_clie.setFieldString("MC_FECHAANUL", this.fecha.getFechaActual());
            } else {
               this.cta_clie.setFieldString("MC_FECHAANUL", this.fecha.getFechaActual());
            }
            // </editor-fold>
            String strResp1 = this.cta_clie.Modifica(oConn);
            int intIdBanco = 0;
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
                  // <editor-fold defaultstate="collapsed" desc="Consultamos bancos">
                  int intIdMovBco = 0;
                  strSql = "SELECT BC_ID,MCB_ID from vta_mov_cta_bcos where MC_ID = " + this.cta_clie.getFieldInt("MC_ID");
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
                  if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
                     if (this.bolAplicaConta) {
                        // <editor-fold defaultstate="collapsed" desc="Calculamos la contabilidad">
                        //Objeto para calculo de poliza contable
                        ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                        contaUtil.CalculaPolizaContableCobros(this.cta_clie.getFieldInt("MC_MONEDA"), this.cta_clie, this.cta_clie.getFieldInt("MC_ID"), "CANCEL");
                        //Si es anticipo lo marcamos
                        if (this.cta_clie.getFieldInt("MC_ANTICIPO") == 1) {
                           contaUtil.setIntEsAnticipo(1);
                        }
                        // </editor-fold>
                     }
                  }
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
               if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
                  this.saveBitacora("PAGOS", "ANULAR", this.cta_clie.getFieldInt("MC_ID"));
               } else {
                  this.saveBitacora("CARGOS", "ANULAR", this.cta_clie.getFieldInt("MC_ID"));
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

   public void doTrxRevive() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_clie.getFieldInt("MC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por revivir";
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
      if (this.cta_clie.getFieldInt("MC_ANULADO") != 0) {
         // <editor-fold defaultstate="collapsed" desc="Ajustamos los saldos">
         String strSql = "SELECT CT_SALDO from vta_cliente where CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               double dblSaldo = rs.getDouble("CT_SALDO");
               if (this.cta_clie.getFieldDouble("MC_CARGO") > 0) {
                  dblSaldo = dblSaldo - this.cta_clie.getFieldDouble("MC_CARGO");
               } else {
                  dblSaldo = dblSaldo + this.cta_clie.getFieldDouble("MC_ABONO");
               }
               //Actualizamos el saldo
               String strUpdate = "UPDATE vta_cliente set CT_SALDO = " + dblSaldo + " where CT_ID = " + this.cta_clie.getFieldInt("CT_ID");
               oConn.runQueryLMD(strUpdate);
               // <editor-fold defaultstate="collapsed" desc="En caso de venir de un ticket actualizamos el saldo">
               if (this.cta_clie.getFieldInt("TKT_ID") != 0) {
                  //Actualizamos el saldo de la venta
                  if (this.cta_clie.getFieldDouble("MC_CARGO") > 0) {
                     strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO + "
                             + this.cta_clie.getFieldDouble("MC_CARGO") + "  "
                             + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");

                  } else {
                     strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO - "
                             + this.cta_clie.getFieldDouble("MC_ABONO") + "   "
                             + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");
                  }
                  oConn.runQueryLMD(strUpdate);
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="En caso de venir de una factura actualizamos el saldo">
               if (this.cta_clie.getFieldInt("FAC_ID") != 0) {
                  //Actualizamos el saldo de la venta
                  if (this.cta_clie.getFieldDouble("MC_CARGO") > 0) {
                     strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO + "
                             + this.cta_clie.getFieldDouble("MC_CARGO") + "  "
                             + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");

                  } else {
                     strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO - "
                             + this.cta_clie.getFieldDouble("MC_ABONO") + "  "
                             + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
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
         this.cta_clie.setFieldInt("ID_USUARIOSANUL", 0);
         this.cta_clie.setFieldInt("MC_ANULADO", 0);
         this.cta_clie.setFieldString("MC_FECHAANUL", "");
         this.cta_clie.setFieldString("MC_HORAANUL", "");
         String strResp1 = this.cta_clie.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         } else {
            // <editor-fold defaultstate="collapsed" desc="Si es un pago buscamos si hay que hacer un movimiento">
            if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
               int intIdMovBco = 0;
               strSql = "SELECT MCB_ID from vta_mov_cta_bcos where MC_ID = " + this.cta_clie.getFieldInt("MC_ID");
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
               poli.callRemote(this.cta_clie.getFieldInt("MC_ID"), PolizasContables.RECIBOS);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
            if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
               this.saveBitacora("PAGOS", "REVIVIR", this.cta_clie.getFieldInt("MC_ID"));
            } else {
               this.saveBitacora("CARGOS", "REVIVIR", this.cta_clie.getFieldInt("MC_ID"));
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

   public void doTrxSaldo() {
   }

   public void doTrxMod() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_clie.getFieldInt("MC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por modificar";
      }
      if (this.cta_clie.getFieldInt("CT_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el cliente";
      }
      if (this.cta_clie.getFieldString("MC_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.cta_clie.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.cta_clie.getFieldDouble("MC_CARGO") == 0
              && this.cta_clie.getFieldDouble("MC_ABONO") == 0) {
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
         String strRes1 = this.cta_clie.Modifica(oConn);
         // <editor-fold defaultstate="collapsed" desc="Si se guardo el movimiento proseguimos">
         if (strRes1.equals("OK")) {
            int intId = this.cta_clie.getFieldInt("MC_ID");
            // <editor-fold defaultstate="collapsed" desc="Borramos los movimientos anteriores">
            String strDelete = "delete from vta_mov_cte_deta where MC_ID = " + intId;
            oConn.runQueryLMD(strDelete);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Insertamos el nuevo detalle">
            Iterator<vta_mov_cte_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_mov_cte_deta deta = it.next();
               deta.setFieldInt("MC_ID", intId);
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del cliente">
            String strSql = "SELECT CT_SALDO from vta_cliente where CT_ID = " + intId;
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("CT_SALDO");
                  if (this.cta_clie.getFieldDouble("MC_CARGO") > 0) {
                     dblSaldo = dblSaldo + this.cta_clie.getFieldDouble("MC_CARGO") - dblCargo + dblAbono;
                  } else {
                     dblSaldo = dblSaldo - this.cta_clie.getFieldDouble("MC_ABONO") - dblCargo + dblAbono;
                  }
                  //Actualizamos el saldo
                  String strUpdate = "UPDATE vta_cliente set CT_SALDO = " + dblSaldo + " where CT_ID = " + intId;
                  oConn.runQueryLMD(strUpdate);
                  // <editor-fold defaultstate="collapsed" desc="En caso de venir de un ticket actualizamos el saldo">
                  if (this.cta_clie.getFieldInt("TKT_ID") != 0) {
                     //Actualizamos el saldo de la venta
                     if (this.cta_clie.getFieldDouble("MC_CARGO") > 0) {
                        strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO + "
                                + (this.cta_clie.getFieldDouble("MC_CARGO") - dblCargo + dblAbono) + "  "
                                + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");

                     } else {
                        strUpdate = "UPDATE vta_tickets set TKT_SALDO = TKT_SALDO - "
                                + (this.cta_clie.getFieldDouble("MC_ABONO") - dblCargo + dblAbono) + "   "
                                + " WHERE TKT_ID = " + this.cta_clie.getFieldInt("TKT_ID");
                     }
                     oConn.runQueryLMD(strUpdate);
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="En caso de venir de una factura actualizamos el saldo">
                  if (this.cta_clie.getFieldInt("FAC_ID") != 0) {
                     //Actualizamos el saldo de la venta
                     if (this.cta_clie.getFieldDouble("MC_CARGO") > 0) {
                        strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO + "
                                + (this.cta_clie.getFieldDouble("MC_CARGO") - dblCargo + dblAbono) + "  "
                                + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");

                     } else {
                        strUpdate = "UPDATE vta_facturas set FAC_SALDO = FAC_SALDO - "
                                + (this.cta_clie.getFieldDouble("MC_ABONO") - dblCargo + dblAbono) + "  "
                                + " WHERE FAC_ID = " + this.cta_clie.getFieldInt("FAC_ID");
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
            if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
               // <editor-fold defaultstate="collapsed" desc="Buscamos el banco">
               int intIdMovBco = 0;
               strSql = "SELECT MCB_ID from vta_mov_cta_bcos where MC_ID = " + this.cta_clie.getFieldInt("MC_ID");
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
                  banco.getCta_bcos().setFieldDouble("MCB_DEPOSITO", this.cta_clie.getFieldDouble("MC_ABONO"));
                  banco.getCta_bcos().setFieldDouble("MCB_RETIRO", this.cta_clie.getFieldDouble("MC_CARGO"));
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
               poli.callRemote(this.cta_clie.getFieldInt("MC_ID"), PolizasContables.RECIBOS);
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Guardamos la bitacora">
            if (this.cta_clie.getFieldInt("MC_ESPAGO") == 1) {
               this.saveBitacora("PAGOS", "MODIFICA", this.cta_clie.getFieldInt("MC_ID"));
            } else {
               this.saveBitacora("CARGOS", "MODIFICA", this.cta_clie.getFieldInt("MC_ID"));
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
    * Vincula el pago con los pedidos del cliente en caso de encontrarse
    *
    * @param cta_clie Es un objeto de pagos
    */
   public void doLinkPaymentsPedidos(vta_mov_cte cta_clie) {
      double dblImportePago = cta_clie.getFieldDouble("MC_ABONO");
      //Consultamos los pedidos del cliente del anticipo
      String strSql = "Select PD_ID,PD_TOTAL from vta_pedidos where CT_ID = " + cta_clie.getFieldInt("CT_ID")
              + " AND PD_ANULADA = 0 AND TKT_ID = 0 AND FAC_ID = 0";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intPD_ID = rs.getInt("PD_ID");
            double dblTotal = rs.getDouble("PD_TOTAL");
            if (dblImportePago >= dblTotal) {
               //Si cubre el importe del pago lo marcamos como pedidos pagado
            } else {
            }
         }
         rs.close();
         //Aplicamos el importe en todos los pedidos que se alcancen a pagar
      } catch (SQLException ex) {
         Logger.getLogger(movCliente.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Ajusta el saldo a favor al usar anticipos
    *
    * @param intCte Es el id del cliente
    * @param intMonedaDoc Es el id de la moneda del documento
    * @param intMonedaPago Es el id de la moneda en que se hizo uso del saldo a
    * favor
    * @param dblParidad Es la paridad que se uso
    */
   public void doAjustaSaldoFavor(int intCte, int intMonedaDoc, int intMonedaPago, double dblParidad) {
      double dblMontoUsar = this.cta_clie.getFieldDouble("MC_ABONO");
      Monedas MiTasaCambio = new Monedas(oConn);
      double dblTasaCambio = 0.0;
      log.debug("dblMontoUsar." + dblMontoUsar);
      //Consultamos los saldos a favor u anticipos
      String strQuery = "Select * From vta_mov_cte "
              + "Where CT_ID =" + intCte + " and MC_ID = " + this.cta_clie.getFieldInt("MC_ANTI_ID")
              + " and MC_ANTICIPO = 1 AND MC_ANULADO = 0 order by MC_FECHA";
      try {
         ResultSet rs = oConn.runQuery(strQuery, true);
         while (rs.next()) {
            dblTasaCambio = 1.0;
            int intIdMov = rs.getInt("MC_ID");
            log.debug("Doc." + intIdMov);
            double dblImporteAbono = rs.getDouble("MC_SALDO_ANTICIPO");
            log.debug("dblImporteAbono." + dblImporteAbono);
            log.debug("Moneda mov." + rs.getInt("MC_MONEDA") + " moneda pago " + intMonedaPago);
            if (rs.getInt("MC_MONEDA") != intMonedaPago) {
               if (intMonedaDoc == rs.getInt("MC_MONEDA")) {
                  dblTasaCambio = dblParidad;
                  if (intMonedaPago == 1) {
                     dblImporteAbono = dblImporteAbono * dblTasaCambio;
                  } else {
                     dblImporteAbono = dblImporteAbono / dblTasaCambio;
                  }
               } else {
                  dblTasaCambio = MiTasaCambio.GetFactorConversion(4, rs.getInt("MC_MONEDA"), intMonedaPago);
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
            if (rs.getInt("MC_MONEDA") != intMonedaPago) {
               if (intMonedaDoc == rs.getInt("MC_MONEDA")) {
                  if (rs.getInt("MC_MONEDA") == 1) {
                     dblMontoAbonoRest = dblMontoAbonoRest * dblTasaCambio;
                  } else {
                     dblMontoAbonoRest = dblMontoAbonoRest / dblTasaCambio;
                  }
               } else {
                  dblTasaCambio = MiTasaCambio.GetFactorConversion(4, intMonedaPago, rs.getInt("MC_MONEDA"));
                  dblMontoAbonoRest = dblMontoAbonoRest * dblTasaCambio;
               }
            }
            log.debug("dblMontoUsado guardar...." + dblMontoAbonoRest);
            //Actualizamos el anticipo
            String strUpdate = "update vta_mov_cte "
                    + " set MC_SALDO_ANTICIPO = " + dblMontoAbonoRest + ""
                    + " where MC_ID = " + intIdMov;
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
// </editor-fold>
}
