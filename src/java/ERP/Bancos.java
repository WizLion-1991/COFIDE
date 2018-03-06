package ERP;

import Tablas.vta_mov_cta_bcos;
import Tablas.vta_mov_cta_bcos_deta;
import Tablas.vta_mov_cte;
import Tablas.vta_mov_prov;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Representa las operaciones que podemos realizar con los bancos
 *
 * @author zeus
 */
public class Bancos extends ProcesoMaster implements ProcesoInterfaz {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   protected vta_mov_cta_bcos cta_bcos;//representa el documento de bancos
   protected ArrayList<vta_mov_cta_bcos_deta> lstMovs;//representa el detalle del movimiento de bancos
   protected double dblDeposito = 0;
   protected int intTraspaso = 0; //nuevo para traspaso
   protected double dblRetiro = 0;
   protected String strFechaAnul;
   protected String strFechaMovOriginal;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Bancos.class.getName());
   protected int intIdAnticipDevProv;
   protected int intIdAnticipDevClie;
   protected double dblAnticipoImporteDevolver;
   protected boolean generaConta;
   protected boolean cancelarDepositotras;
   protected boolean cancelarMovimientoCobranza;
   protected double dblComision = 0.0;

   public double getDblComision() {
      return dblComision;
   }

   public void setDblComision(double dblComision) {
      this.dblComision = dblComision;
   }

   public int getIntIdAnticipDevProv() {
      return intIdAnticipDevProv;
   }

   public boolean isGeneraConta() {
      return generaConta;
   }

   /**
    * Define si se genera la contabilidad
    *
    * @param generaConta
    */
   public void setGeneraConta(boolean generaConta) {
      this.generaConta = generaConta;
   }

   /**
    * Define el id del anticipo que esta devolviendo el proveedor
    *
    * @param intIdAnticipDevProv
    */
   public void setIntIdAnticipDevProv(int intIdAnticipDevProv) {
      this.intIdAnticipDevProv = intIdAnticipDevProv;
   }

   public int getIntIdAnticipDevClie() {
      return intIdAnticipDevClie;
   }

   /**
    * Define el id del anticipo por devolver al cliente
    *
    * @param intIdAnticipDevClie
    */
   public void setIntIdAnticipDevClie(int intIdAnticipDevClie) {
      this.intIdAnticipDevClie = intIdAnticipDevClie;
   }

   /**
    * Regresa el movimiento maestro de bancos
    *
    * @return Nos regresa el movimiento de Bancos
    */
   public vta_mov_cta_bcos getCta_bcos() {
      return cta_bcos;
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(vta_mov_cta_bcos_deta deta) {
      this.lstMovs.add(deta);
   }

   public double getDblAnticipoImporteDevolver() {
      return dblAnticipoImporteDevolver;
   }

   public void setDblAnticipoImporteDevolver(double dblAnticipoImporteDevolver) {
      this.dblAnticipoImporteDevolver = dblAnticipoImporteDevolver;
   }

   public boolean isCancelarMovimientoCobranza() {
      return cancelarMovimientoCobranza;
   }

   public void setCancelarMovimientoCobranza(boolean cancelarMovimientoCobranza) {
      this.cancelarMovimientoCobranza = cancelarMovimientoCobranza;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor default
    *
    * @param oConn Conexion
    * @param varSesiones Es la sesion
    * @param request Es la peticion web
    */
   public Bancos(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.cta_bcos = new vta_mov_cta_bcos();
      this.lstMovs = new ArrayList<vta_mov_cta_bcos_deta>();
      //CAMPO DE GENERA CONTA TRUE
      generaConta = true;
      cancelarDepositotras = false;
      cancelarMovimientoCobranza = false;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      this.cta_bcos.setFieldInt("ID_USUARIOS", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.cta_bcos.getFieldInt("MCB_ID") != 0) {
         this.dblDeposito = this.cta_bcos.getFieldDouble("MCB_DEPOSITO");
         this.dblRetiro = this.cta_bcos.getFieldDouble("MCB_RETIRO");
         this.strFechaAnul = this.cta_bcos.getFieldString("MCB_FECHANUL");
         this.cta_bcos.ObtenDatos(this.cta_bcos.getFieldInt("MCB_ID"), oConn);
         this.strFechaMovOriginal = this.cta_bcos.getFieldString("MCB_FECHA");
      }
   }

   @Override
   public void doTrx() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
      this.cta_bcos.setFieldString("MCB_FECHACREATE", this.fecha.getFechaActual());
      this.cta_bcos.setFieldString("MCB_HORA", this.fecha.getHoraActual());
      //Validamos que todos los campos basico se encuentren
      if (this.cta_bcos.getFieldInt("BC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el banco";
      }
      if (this.cta_bcos.getFieldString("MCB_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.cta_bcos.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      //validacion de si es traspaso y que los bancos son diferentes
      this.intTraspaso = this.cta_bcos.getFieldInt("MCB_TRASPASO"); //NUEVA DE TRASPASO
      if (this.intTraspaso == 1) {
         if (this.cta_bcos.getFieldInt("BC_ID2") != 0 && this.cta_bcos.getFieldInt("BC_ID") != 0) {
            if (this.cta_bcos.getFieldInt("BC_ID") == this.cta_bcos.getFieldInt("BC_ID2")) {

               this.strResultLast = "ERROR: Los Bancos para realizar la transaccion son iguales ";
            }
         } else {
            this.strResultLast = "ERROR: Los Bancos no pueden estar vacios ";
         }
      }
      log.debug("Evaluamos si crea el movimiento de comision-- " + this.dblComision + "--" + this.cta_bcos.getFieldInt("BC_ID"));
      if (this.dblComision != 0 && this.cta_bcos.getFieldInt("BC_ID") != 0) {
         String strSQL = "Select GT_ID from vta_bcos Where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");

         vta_mov_cta_bcos_deta MiComision = new vta_mov_cta_bcos_deta();
         MiComision.setFieldDouble("MCBD_IMPORTE", this.dblComision);
         MiComision.setFieldString("MCBD_CONCEPTO", "COMISION");
         try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
               MiComision.setFieldInt("GT_ID", rs.getInt("GT_ID"));
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
         if (this.cta_bcos.getFieldDouble("MCB_DEPOSITO") > 0.0) {
            this.cta_bcos.setFieldDouble("MCB_DEPOSITO", this.cta_bcos.getFieldDouble("MCB_DEPOSITO") + this.dblComision);
         } else {
            this.cta_bcos.setFieldDouble("MCB_RETIRO", this.cta_bcos.getFieldDouble("MCB_RETIRO") + this.dblComision);
         }
         this.lstMovs.add(MiComision);
      }
//      if (this.cta_bcos.getFieldDouble("MCB_DEPOSITO") == 0
//              && this.cta_bcos.getFieldDouble("MCB_RETIRO") == 0) {
//         this.strResultLast = "ERROR:Falta definir un importe";
//      }
      //Evaluamos si es un cheque que no existe
      if (this.cta_bcos.getFieldString("MCB_NOCHEQUE") != null) {
         if (!this.cta_bcos.getFieldString("MCB_NOCHEQUE").trim().isEmpty()) {
            log.debug("Evaluamos si ya existe el cheque " + this.cta_bcos.getFieldString("MCB_NOCHEQUE"));
            boolean bolEncontroCheque = false;
            String strSQL = "select MCB_ID from vta_mov_cta_bcos "
                    + " where MCB_NOCHEQUE = '" + this.cta_bcos.getFieldString("MCB_NOCHEQUE") + "'"
                    + " AND EMP_ID = " + this.cta_bcos.getFieldInt("EMP_ID");
            try {
               ResultSet rs = oConn.runQuery(strSQL, true);
               while (rs.next()) {
                  bolEncontroCheque = true;
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            if(bolEncontroCheque){
               this.strResultLast = "ERROR:El numero de cheque " + this.cta_bcos.getFieldString("MCB_NOCHEQUE") + " ya existe.";
            }
         }
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_bcos.getFieldString("MCB_FECHA"), this.cta_bcos.getFieldInt("EMP_ID"), this.cta_bcos.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>

         //Validamos si hay devoluciones de anticipos
         if (this.intIdAnticipDevProv != 0) {
            //Estamos devolviendo de un anticipo de proveedores
            String strRespDev = this.devolucionAnticipoProv();
            if (!strRespDev.equals("OK")) {
               this.strResultLast = strRespDev;
            }
         }
         if (this.intIdAnticipDevClie != 0) {
            //Estamos devolviendo de un anticipo de clientes.
            String strRespDev = this.devolucionAnticipoClie();
            if (!strRespDev.equals("OK")) {
               this.strResultLast = strRespDev;
            }
         }
         // <editor-fold defaultstate="collapsed" desc="Guardamos documento principal">
         this.cta_bcos.setBolGetAutonumeric(true);
         String strRes1 = this.cta_bcos.Agrega(oConn);
         if (strRes1.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Guardamos partidas">
            int intId = Integer.valueOf(this.cta_bcos.getValorKey());
            Iterator<vta_mov_cta_bcos_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_mov_cta_bcos_deta deta = it.next();
               deta.setFieldInt("MCB_ID", intId);
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            // </editor-fold>"
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del banco">
            String strSql = "SELECT BC_SALDO,BC_MONEDA from vta_bcos where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("BC_SALDO");
                  double dblCargoProv = 0.0;
                  double dblAbonoProv = 0.0;

                  dblCargoProv = this.cta_bcos.getFieldDouble("MCB_DEPOSITO");
                  dblAbonoProv = this.cta_bcos.getFieldDouble("MCB_RETIRO");

                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  int intMonedaBco = rs.getInt("BC_MONEDA");
                  if (intMonedaBco == 0) {
                     intMonedaBco = 1;
                  }
                  if (this.cta_bcos.getFieldInt("MCB_MONEDA") == 0) {
                     this.cta_bcos.setFieldInt("MCB_MONEDA", 1);
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaBco + " " + this.cta_bcos.getFieldInt("MCB_MONEDA"));
                  if (intMonedaBco != this.cta_bcos.getFieldInt("MCB_MONEDA")) {
                     double dblFactor = 0;
//                     if (intMonedaVta == intMonedaCte) {
//                        dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
//                     } else {
                     Monedas conversion = new Monedas(oConn);
                     dblFactor = conversion.GetFactorConversion(this.cta_bcos.getFieldString("MC_FECHA"), 4, this.cta_bcos.getFieldInt("MCB_MONEDA"), intMonedaBco);
//                     }
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
                  //Actualizamos el saldo
                  String strUpdate = "UPDATE vta_bcos set BC_SALDO = '" + dblSaldo + "' where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");
                  oConn.runQueryLMD(strUpdate);
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            if (this.strResultLast.equals("OK")) {
               //Validamos que no sea un movimiento
               //vinculado a pagos o cobranza
               if (this.cta_bcos.getFieldInt("FAC_ID") == 0
                       && this.cta_bcos.getFieldInt("TKT_ID") == 0
                       && (this.cta_bcos.getFieldInt("MC_ID") == 0
                       || (this.cta_bcos.getFieldInt("MC_ID") != 0 && this.cta_bcos.getFieldInt("MCB_DEV_CLIE") == 1))
                       && (this.cta_bcos.getFieldInt("MP_ID") == 0
                       || (this.cta_bcos.getFieldInt("MP_ID") != 0 && this.cta_bcos.getFieldInt("MCB_DEV_PROV") == 1))) {
                  // <editor-fold defaultstate="collapsed" desc="Obtenemos datos del banco">
                  int intIdBanco = this.cta_bcos.getFieldInt("BC_ID");
                  int intIdGasto = 0;
                  int intEmpId = 0;
                  String strEMP_USERCP = "";
                  String strEMP_PASSCP = "";
                  String strEMP_URLCP = "";
                  int intEMP_USECONTA = 0;
                  //Obtenemos datos del banco
                  strSql = "SELECT BC_CTA_CONT,EMP_ID "
                          + " FROM vta_bcos "
                          + " WHERE BC_ID = " + intIdBanco + "";
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intEmpId = rs.getInt("EMP_ID");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     log.debug("ERROR:" + ex.getMessage());

                     ex.fillInStackTrace();
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Buscamos datos de la empresa y validamos si usa contabilidad">
                  strSql = "SELECT * FROM vta_empresas where EMP_ID=" + intEmpId;
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        strEMP_PASSCP = rs.getString("EMP_PASSCP");
                        strEMP_URLCP = rs.getString("EMP_URLCP");
                        intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     ex.fillInStackTrace();
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Evaluamos si tiene activada la contabilidad la empresa">
                  if (intEMP_USECONTA == 1 && generaConta == true) {//AQUI BANDERA TRUE  //
                     log.debug("Generamos contabilidad Bancos...");
                     //Objeto para calculo de poliza contable
                     ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                     contaUtil.CalculaPolizaContableBco(intEmpId, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
                             this.cta_bcos.getFieldInt("MCB_MONEDA"), this.cta_bcos,
                             intId, intIdBanco, 0, "NEW");
                  }
                  // </editor-fold>
               }

            }

            //Es un traspaso hacemos el movientos adicional.
            if (this.intTraspaso == 1) {
               //Obtenemos datos del banco
               int intMoneda1 = this.getCta_bcos().getFieldInt("MCB_MONEDA");
               int intMoneda2 = 0;
               strSql = "SELECT BC_MONEDA "
                       + " FROM vta_bcos "
                       + " WHERE BC_ID = " + this.cta_bcos.getFieldInt("BC_ID2") + "";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intMoneda2 = rs.getInt("BC_MONEDA");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  log.debug("ERROR:" + ex.getMessage());

                  ex.fillInStackTrace();
               }
               this.getCta_bcos().setFieldInt("BC_ID", this.cta_bcos.getFieldInt("BC_ID2"));
               double dblMCB_DEPOSITO = this.cta_bcos.getFieldDouble("MCB_RETIRO");
               double dblParidad = this.cta_bcos.getFieldDouble("MCB_PARIDAD2");
               if (intMoneda1 == 1 && intMoneda2 == 2) {
                  dblMCB_DEPOSITO = dblMCB_DEPOSITO / dblParidad;
               } else {
                  if (intMoneda1 == 2 && intMoneda2 == 1) {
                     dblMCB_DEPOSITO = dblMCB_DEPOSITO * dblParidad;
                  } else {
                     if (intMoneda1 == 2 && intMoneda2 == 3) {
                        dblMCB_DEPOSITO = dblMCB_DEPOSITO / dblParidad;
                     } else {
                        dblMCB_DEPOSITO = dblMCB_DEPOSITO * dblParidad;
                     }
                  }
               }

               this.getCta_bcos().setFieldDouble("MCB_DEPOSITO", dblMCB_DEPOSITO);
               this.getCta_bcos().setFieldDouble("MCB_RETIRO", 0);
               this.getCta_bcos().setFieldInt("MCB_MONEDA", intMoneda2);
               this.getCta_bcos().setFieldInt("BC_ID2", 0);
               this.getCta_bcos().setFieldDouble("MCB_TRASPASO", 0);
               this.getCta_bcos().setFieldDouble("MCB_PARIDAD2", 0);
               this.getCta_bcos().setFieldInt("MCB_TRAS_ORIGEN", intId);
               //Nuevo campo de MCB_TRAS_ORIGEN SUSTITUIR CON intId
               this.bolTransaccionalidad = false;
               this.setGeneraConta(false);
               this.doTrx();
               this.bolTransaccionalidad = true;
               this.getCta_bcos().setValorKey(intId + "");
            }

            this.saveBitacora("BANCOS", "NUEVA", intId);
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

   @Override
   public void doTrxAnul() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_bcos.getFieldString("MCB_FECHA"), this.cta_bcos.getFieldInt("EMP_ID"), this.cta_bcos.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      //si ya no se puede anular la operacion
      if (this.cta_bcos.getFieldInt("MCB_TRAS_ORIGEN") != 0 && !cancelarDepositotras) {
         this.strResultLast = "ERROR: Esta operacion no se puede anular";

      }
      if (!cancelarMovimientoCobranza) {
         if (this.cta_bcos.getFieldInt("MP_ID") != 0) {
            this.strResultLast = "ERROR: Esta operacion no se puede anular porque esta vinculado a un pago de proveedor.";
         }
         if (this.cta_bcos.getFieldInt("MC_ID") != 0) {
            this.strResultLast = "ERROR: Esta operacion no se puede anular porque esta vinculado a un pago de cliente.";
         }
      }
      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.cta_bcos.getFieldInt("MCB_ANULADO") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del banco">
            String strSql = "SELECT BC_SALDO,BC_MONEDA from vta_bcos where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("BC_SALDO");
                  double dblCargoProv = this.cta_bcos.getFieldDouble("MCB_DEPOSITO");
                  double dblAbonoProv = this.cta_bcos.getFieldDouble("MCB_RETIRO");
                  // <editor-fold defaultstate="collapsed" desc="Realizamos conversion de monedas en caso de aplicar">
                  int intMonedaBco = rs.getInt("BC_MONEDA");
                  if (intMonedaBco == 0) {
                     intMonedaBco = 1;
                  }
                  if (this.cta_bcos.getFieldInt("MCB_MONEDA") == 0) {
                     this.cta_bcos.setFieldInt("MCB_MONEDA", 1);
                  }
                  log.debug("Evaluamos conversion de monedas " + intMonedaBco + " " + this.cta_bcos.getFieldInt("MCB_MONEDA"));
                  if (intMonedaBco != this.cta_bcos.getFieldInt("MCB_MONEDA")) {
                     double dblFactor = 0;
//                     if (intMonedaVta == intMonedaCte) {
//                        dblFactor = this.cta_clie.getFieldDouble("MC_TASAPESO");
//                     } else {
                     Monedas conversion = new Monedas(oConn);
                     dblFactor = conversion.GetFactorConversion(this.cta_bcos.getFieldString("MC_FECHA"), 4, this.cta_bcos.getFieldInt("MCB_MONEDA"), intMonedaBco);
//                     }
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
                  String strUpdate = "UPDATE vta_bcos set BC_SALDO = '" + dblSaldo + "' where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");
                  oConn.runQueryLMD(strUpdate);
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Actualizamos documento principal">
            this.cta_bcos.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
            this.cta_bcos.setFieldInt("MCB_ANULADO", 1);
            this.cta_bcos.setFieldString("MCB_HORAANUL", this.fecha.getHoraActual());
            if (strFechaAnul.equals("")) {
               this.cta_bcos.setFieldString("MCB_FECHANUL", this.fecha.getFechaActual());
            } else {
               this.cta_bcos.setFieldString("MCB_FECHANUL", strFechaAnul);
            }
            String strResp1 = this.cta_bcos.Modifica(oConn);
            // </editor-fold>
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               // <editor-fold defaultstate="collapsed" desc="Validamos que no sea un movimiento vinculado a pagos o cobranza">
               if (this.cta_bcos.getFieldInt("FAC_ID") == 0
                       && this.cta_bcos.getFieldInt("TKT_ID") == 0
                       && this.cta_bcos.getFieldInt("MC_ID") == 0
                       && this.cta_bcos.getFieldInt("MP_ID") == 0) {
                  //Actualizamos la poliza contable
                  int intIdBanco = this.cta_bcos.getFieldInt("BC_ID");
                  int intEmpId = 0;
                  String strEMP_USERCP = "";
                  String strEMP_PASSCP = "";
                  String strEMP_URLCP = "";
                  int intEMP_USECONTA = 0;
                  // <editor-fold defaultstate="collapsed" desc="Obtenemos datos del banco">
                  strSql = "SELECT BC_CTA_CONT,EMP_ID "
                          + " FROM vta_bcos "
                          + " WHERE BC_ID = " + intIdBanco + "";
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        intEmpId = rs.getInt("EMP_ID");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     log.debug("ERROR:" + ex.getMessage());

                     ex.fillInStackTrace();
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Buscamos datos de la empresa y validamos si usa contabilidad">
                  strSql = "SELECT * FROM vta_empresas where EMP_ID=" + intEmpId;
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        strEMP_USERCP = rs.getString("EMP_USERCP");
                        strEMP_PASSCP = rs.getString("EMP_PASSCP");
                        strEMP_URLCP = rs.getString("EMP_URLCP");
                        intEMP_USECONTA = rs.getInt("EMP_USECONTA");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     ex.fillInStackTrace();
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Evaluamos si tiene activada la contabilidad la empresa">
                  if (intEMP_USECONTA == 1) {
                     //Objeto para calculo de poliza contable
                     ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                     contaUtil.CalculaPolizaContableBco(intEmpId, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
                             this.cta_bcos.getFieldInt("MCB_MONEDA"), this.cta_bcos,
                             this.cta_bcos.getFieldInt("MCB_ID"), intIdBanco, 0, "CANCEL");
                  }
                  // </editor-fold>
               }
               // </editor-fold>
            }
            //Evaluamos que no sea una devolucion
            if (this.cta_bcos.getFieldInt("MCB_DEV_PROV") == 1) {
               String strRespDev = this.devolucionAnticipoProvCancelar();
               if (!strRespDev.equals("OK")) {
                  this.strResultLast = strRespDev;
               }
            }
            if (this.cta_bcos.getFieldInt("MCB_DEV_CLIE") == 1) {
               String strRespDev = this.devolucionAnticipoClieCancelar();
               if (!strRespDev.equals("OK")) {
                  this.strResultLast = strRespDev;
               }
            }

            //si es traspaso
            if (this.cta_bcos.getFieldInt("MCB_TRASPASO") == 1) {
               //buscamos id movimiento destino MCB_ID(actual)
               strSql = "SELECT MCB_ID from vta_mov_cta_bcos where MCB_TRAS_ORIGEN = " + this.cta_bcos.getFieldInt("MCB_ID");
               int intMCB_TRAS_ORIGEN = 0;
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intMCB_TRAS_ORIGEN = rs.getInt("MCB_ID");
                  }
                  rs.close();
               } catch (SQLException ex) {
                  log.debug("ERROR:" + ex.getMessage());

                  ex.fillInStackTrace();
               }
               //sustituir el id actual(2d0) por el destino
               if (intMCB_TRAS_ORIGEN != 0) {
                  this.getCta_bcos().setFieldInt("MCB_ID", intMCB_TRAS_ORIGEN);
                  Init();
                  //Ejecutar cancelacion de 2do movimiento

                  this.bolTransaccionalidad = false;
                  cancelarDepositotras = true;
                  this.doTrxAnul();

                  this.bolTransaccionalidad = true;
                  generaConta = false;
               }
            }

            this.saveBitacora("BANCOS", "ANULAR", this.cta_bcos.getFieldInt("MCB_ID"));
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
      //Validamos que todos los campos basico se encuentren
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por revivir";
      }
      //Inicializamos la operacion
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      //Validamos que no este anulado
      if (this.cta_bcos.getFieldInt("MCB_ANULADO") != 0) {
         //Ajustamos el saldo del banco
         String strSql = "SELECT BC_SALDO from vta_bcos where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               double dblSaldo = rs.getDouble("BC_SALDO");
               if (this.cta_bcos.getFieldDouble("MCB_DEPOSITO") > 0) {
                  dblSaldo = dblSaldo + this.cta_bcos.getFieldDouble("MCB_DEPOSITO");
               } else {
                  dblSaldo = dblSaldo - this.cta_bcos.getFieldDouble("MCB_RETIRO");
               }
               //Actualizamos el saldo
               String strUpdate = "UPDATE vta_bcos set BC_SALDO = '" + dblSaldo + "' where BC_ID = " + this.cta_bcos.getFieldInt("BC_ID");
               oConn.runQueryLMD(strUpdate);
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
         //Definimos campos
         this.cta_bcos.setFieldInt("ID_USUARIOSANUL", 0);
         this.cta_bcos.setFieldInt("MCB_ANULADO", 0);
         this.cta_bcos.setFieldString("MCB_FECHANUL", "");
         this.cta_bcos.setFieldString("MCB_HORAANUL", "");
         String strResp1 = this.cta_bcos.Modifica(oConn);
         if (!strResp1.equals("OK")) {
            this.strResultLast = strResp1;
         } else {
            //Actualizamos la poliza contable
            PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
            poli.setStrOper("REVIVE");
            poli.callRemote(this.cta_bcos.getFieldInt("MCB_ID"), PolizasContables.BANCOS);
         }
         this.saveBitacora("BANCOS", "REVIVE", this.cta_bcos.getFieldInt("MCB_ID"));
      } else {
         this.strResultLast = "ERROR:La operacion no esta anulada";
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

   @Override
   public void doTrxSaldo() {
   }

   @Override
   public void doTrxMod() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por modificar";
      }
      if (this.cta_bcos.getFieldInt("BC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el banco";
      }
      if (this.cta_bcos.getFieldString("MCB_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.cta_bcos.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
//      if (this.cta_bcos.getFieldDouble("MCB_DEPOSITO") == 0
//              && this.cta_bcos.getFieldDouble("MCB_RETIRO") == 0) {
//         this.strResultLast = "ERROR:Falta definir un importe";
//      }
      if (this.cta_bcos.getFieldInt("MCB_ANULADO") == 1) {
         this.strResultLast = "ERROR:La operacion ya fue anulada";
      }
      //si ya no se puede anular la operacion
      log.debug(this.cta_bcos.getFieldInt("MCB_ID") + " " + this.cta_bcos.getFieldInt("MCB_TRAS_ORIGEN"));
      if (this.cta_bcos.getFieldInt("MCB_TRAS_ORIGEN") != 0) {
         this.strResultLast = "ERROR: Esta operacion no se puede modificar porque es un traspaso";

      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.cta_bcos.getFieldString("MCB_FECHA"), this.cta_bcos.getFieldInt("EMP_ID"), this.cta_bcos.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Si la fecha de modificacion es diferente de nulo es una modificacion
      if (this.strFechaMovOriginal != null) {
         if (!this.strFechaMovOriginal.isEmpty()) {
            bolEvalCierre = evaluaFechaCierre(strFechaMovOriginal, this.cta_bcos.getFieldInt("EMP_ID"), this.cta_bcos.getFieldInt("SC_ID"));
            if (!bolEvalCierre) {
               this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
            }
         }
      }
      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>
         String strRes1 = this.cta_bcos.Modifica(oConn);
         if (strRes1.equals("OK")) {
            int intId = this.cta_bcos.getFieldInt("MCB_ID");
            // <editor-fold defaultstate="collapsed" desc="Borramos los movimientos anteriores">
            String strDelete = "delete from vta_mov_cta_bcos_deta where MCB_ID = " + intId;
            oConn.runQueryLMD(strDelete);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Insertamos el nuevo detalle">
            Iterator<vta_mov_cta_bcos_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_mov_cta_bcos_deta deta = it.next();
               deta.setFieldInt("MCB_ID", intId);
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos el saldo del banco">
            String strSql = "SELECT BC_SALDO from vta_bcos where BC_ID = " + intId;
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  double dblSaldo = rs.getDouble("BC_SALDO");
                  if (this.cta_bcos.getFieldDouble("MCB_DEPOSITO") > 0) {
                     dblSaldo = dblSaldo + this.cta_bcos.getFieldDouble("MCB_DEPOSITO") - this.dblDeposito + this.dblRetiro;
                  } else {
                     dblSaldo = dblSaldo - this.cta_bcos.getFieldDouble("MCB_RETIRO") - this.dblDeposito + this.dblRetiro;
                  }
                  //Actualizamos el saldo
                  String strUpdate = "UPDATE vta_bcos set BC_SALDO = '" + dblSaldo + "' where BC_ID = " + intId;
                  oConn.runQueryLMD(strUpdate);
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Actualizamos la contabilidad">
            if (this.strResultLast.equals("OK")) {
               //Actualizamos la poliza contable
               PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
               poli.setStrOper("MODIFY");
               poli.callRemote(this.cta_bcos.getFieldInt("MCB_ID"), PolizasContables.BANCOS);
            }
            // </editor-fold>
            this.saveBitacora("BANCOS", "MODIFICA", this.cta_bcos.getFieldInt("MCB_ID"));
         } else {
            this.strResultLast = strRes1;
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
      }
   }

   /**
    * Marca o desmarca la operacion como conciliado
    *
    * @param bolConcilia true/false
    */
   public void doConcilia(boolean bolConcilia) {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      if (this.cta_bcos.getFieldInt("BC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el banco";
      }
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Inicializamos la operacion
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         //Actualizamos el saldo
         String strUpdate = "UPDATE vta_mov_cta_bcos set MCB_CONCILIADO = '1' "
                 + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         if (!bolConcilia) {
            strUpdate = "UPDATE vta_mov_cta_bcos set MCB_CONCILIADO = '0' "
                    + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         }
         oConn.runQueryLMD(strUpdate);
         if (this.strResultLast.equals("OK")) {
            //Actualizamos la poliza contable
            PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
            poli.setStrOper("CONCILIA");
            poli.callRemote(this.cta_bcos.getFieldInt("MCB_ID"), PolizasContables.BANCOS);
         }
         this.saveBitacora("BANCOS", "CONCILIA", this.cta_bcos.getFieldInt("MCB_ID"));
         //Terminamos la operacion
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
      }
   }

   /**
    * Marca o desmarca la operacion como deposito usado
    *
    * @param bolDeposito true/false
    */
   public void doUsoDeposito(boolean bolDeposito) {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      if (this.cta_bcos.getFieldInt("BC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el banco";
      }
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Inicializamos la operacion
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         //Actualizamos el saldo
         String strUpdate = "UPDATE vta_mov_cta_bcos set MCB_TIPO1 = '1' "
                 + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         if (!bolDeposito) {
            strUpdate = "UPDATE vta_mov_cta_bcos set MCB_TIPO1 = '0' "
                    + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         }
         oConn.runQueryLMD(strUpdate);
         if (this.strResultLast.equals("OK")) {
            //Actualizamos la poliza contable
            PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
            poli.setStrOper("DEPOSITO");
            poli.callRemote(this.cta_bcos.getFieldInt("MCB_ID"), PolizasContables.BANCOS);
         }
         this.saveBitacora("BANCOS", "DEPOSITO", this.cta_bcos.getFieldInt("MCB_ID"));
         //Terminamos la operacion
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
      }
   }

   /**
    * Marca o desmarca la operacion como Entrega Documentaci��n
    *
    * @param bolEntrega true/false
    */
   public void doEntrega(boolean bolEntrega) {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      if (this.cta_bcos.getFieldInt("BC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el banco";
      }
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Inicializamos la operacion
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         //Actualizamos el saldo
         String strUpdate = "UPDATE vta_mov_cta_bcos set MCB_TIPO2 = '1' "
                 + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         if (!bolEntrega) {
            strUpdate = "UPDATE vta_mov_cta_bcos set MCB_TIPO2 = '0' "
                    + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         }
         oConn.runQueryLMD(strUpdate);
         this.saveBitacora("BANCOS", "MCB_TIPO2", this.cta_bcos.getFieldInt("MCB_ID"));
         //Terminamos la operacion
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
      }
   }

   /**
    * Marca o desmarca la operacion de Status3
    *
    * @param bolEntrega true/false
    */
   public void doStatus3(boolean bolStatus3) {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.cta_bcos.getFieldInt("MCB_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      if (this.cta_bcos.getFieldInt("BC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir el banco";
      }
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Inicializamos la operacion
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         //Actualizamos el saldo
         String strUpdate = "UPDATE vta_mov_cta_bcos set MCB_TIPO3 = '1' "
                 + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         if (!bolStatus3) {
            strUpdate = "UPDATE vta_mov_cta_bcos set MCB_TIPO3 = '0' "
                    + "where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         }
         oConn.runQueryLMD(strUpdate);
         this.saveBitacora("BANCOS", "MCB_TIPO3", this.cta_bcos.getFieldInt("MCB_ID"));
         //Terminamos la operacion
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
      }
   }

   /**
    * Regresa el XML del movimiento completo para su edicion
    *
    * @return Regresa un xml del movimiento realizado
    *
    */
   public String getXMLMovimiento() {
      //Obtenemos el detalle del movimiento
      getDetaMov();
      //Inicializamos el XML
      String strXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
      //XML del cabezal
      strXml += "<movbco " + this.cta_bcos.getFieldPar() + ">";
      //XML del detalle
      Iterator<vta_mov_cta_bcos_deta> it = this.lstMovs.iterator();
      while (it.hasNext()) {
         vta_mov_cta_bcos_deta deta = it.next();
         strXml += "<movdeta " + deta.getFieldPar() + " ";
         //Obtenemos los conceptos de centro de costos y gastos
         try {
            String strSql = "SELECT * FROM vta_ccostos where CC_ID = " + deta.getFieldInt("CC_ID");
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strXml += " CC_DESC = \"" + rs.getString("CC_DESCRIPCION") + "\" ";
            }
            rs.close();
            strSql = "SELECT * FROM vta_cgastos where GT_ID = " + deta.getFieldInt("GT_ID");
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strXml += " GT_DESC = \"" + rs.getString("GT_DESCRIPCION") + "\" ";
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
         strXml += "/>";
      }
      strXml += "</movbco> ";
      return strXml;
   }

   private void getDetaMov() {
      //Obtenemos la lista de campos
      vta_mov_cta_bcos_deta deta = new vta_mov_cta_bcos_deta();
      ArrayList<String> lstFieldsName = deta.getNomFields();

      try {
         String strSql = "select * from vta_mov_cta_bcos_deta where MCB_ID = " + this.cta_bcos.getFieldInt("MCB_ID");
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            deta = new vta_mov_cta_bcos_deta();
            //Llenamos el objeto
            for (int i = 0; i < lstFieldsName.size(); i++) {
               //Asignamos el valor
               Object obj = deta.getFieldObj(lstFieldsName.get(i));
               if (obj.getClass().equals(Integer.class)) {
                  deta.setFieldInt(lstFieldsName.get(i), rs.getInt(lstFieldsName.get(i)));
               }
               if (obj.getClass().equals(String.class)) {
                  deta.setFieldString(lstFieldsName.get(i), rs.getString(lstFieldsName.get(i)));
               }
               if (obj.getClass().equals(Double.class)) {
                  deta.setFieldDouble(lstFieldsName.get(i), rs.getDouble(lstFieldsName.get(i)));
               }
            }
            this.lstMovs.add(deta);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Genera la devolucion de un anticipo de proveedor
    *
    * @return Regresa OK si todo fue exitoso
    */
   protected String devolucionAnticipoProv() {
      String strRes = "OK";
      //Consultamos el anticipo
      vta_mov_prov movAnticipo = new vta_mov_prov();
      movAnticipo.ObtenDatos(this.intIdAnticipDevProv, oConn);
      if (movAnticipo.getFieldInt("PV_ID") != 0) {
         //Validamos que no este anulado el anticipo
         if (movAnticipo.getFieldInt("MP_ANULADO") == 0) {
            //Generar cargo a proveedores
            MovProveedor movProv = new MovProveedor(this.oConn, this.varSesiones, this.request);
            movProv.setBolTransaccionalidad(false);
            //Asignamos valores
            movProv.getCta_prov().setFieldInt("MP_ANTI_ID", movAnticipo.getFieldInt("MP_ID"));
            movProv.getCta_prov().setFieldInt("PV_ID", movAnticipo.getFieldInt("PV_ID"));
            movProv.getCta_prov().setFieldInt("SC_ID", movAnticipo.getFieldInt("SC_ID"));
            movProv.getCta_prov().setFieldInt("EMP_ID", movAnticipo.getFieldInt("EMP_ID"));
            movProv.getCta_prov().setFieldInt("MP_ESPAGO", 0);
            movProv.getCta_prov().setFieldInt("CXP_ID", 0);
            movProv.getCta_prov().setFieldInt("MP_MONEDA", this.cta_bcos.getFieldInt("MCB_MONEDA"));
            movProv.getCta_prov().setFieldDouble("MP_TASAPESO", this.cta_bcos.getFieldDouble("MCB_PARIDAD_DEV"));

            movProv.getCta_prov().setFieldString("MP_FECHA", this.cta_bcos.getFieldString("MCB_FECHA"));
            movProv.getCta_prov().setFieldString("MP_NOTAS", "Cargo por devolucion");
            movProv.getCta_prov().setFieldDouble("MP_CARGO", this.dblAnticipoImporteDevolver);

            movProv.getCta_prov().setFieldDouble("MP_IMPUESTO1", 0);
            movProv.getCta_prov().setFieldDouble("MP_IMPUESTO2", 0);
            movProv.getCta_prov().setFieldDouble("MP_IMPUESTO3", 0);
            movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO1", 0);
            movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO2", 0);
            movProv.getCta_prov().setFieldDouble("MP_TASAIMPUESTO3", 0);
            movProv.Init();
            movProv.doTrx();
            if (movProv.getStrResultLast().equals("OK")) {
               try {
                  this.cta_bcos.setFieldInt("MP_ID", Integer.valueOf(movProv.getCta_prov().getValorKey()));
               } catch (NumberFormatException ex) {
                  log.error(ex.getMessage());
               }
               this.cta_bcos.setFieldInt("MP_ID_DEV", this.intIdAnticipDevProv);
               this.cta_bcos.setFieldInt("MCB_DEV_PROV", 1);
               //Evaluamos que el deposito no sea mayor al saldo del anticipo
               double dblAnticipoDevolverConvertido = this.dblAnticipoImporteDevolver;
               if (this.cta_bcos.getFieldDouble("MCB_PARIDAD_DEV") > 1) {
                  if (this.cta_bcos.getFieldInt("MCB_MONEDA") == 1
                          && (movAnticipo.getFieldInt("MP_MONEDA") == 2 || movAnticipo.getFieldInt("MP_MONEDA") == 3)) {
                     dblAnticipoDevolverConvertido = this.dblAnticipoImporteDevolver / this.cta_bcos.getFieldDouble("MCB_PARIDAD_DEV");
                  }
               }
               double dblDiff = dblAnticipoDevolverConvertido - movAnticipo.getFieldDouble("MP_SALDO_ANTICIPO");
               log.debug("Evaluamos importes " + this.dblAnticipoImporteDevolver + " convertido:" + dblAnticipoDevolverConvertido + " " + movAnticipo.getFieldDouble("MP_SALDO_ANTICIPO") + " dblDiff:" + dblDiff);
               if (dblDiff > 1) {
                  strRes = "ERROR:La devolucion no puede ser mayor al saldo del anticipo...";
               } else {
                  //Disminuir saldo en anticipo  
                  double dblNvoSaldo = movAnticipo.getFieldDouble("MP_SALDO_ANTICIPO") - dblAnticipoDevolverConvertido;
                  log.debug(movAnticipo.getFieldDouble("MP_SALDO_ANTICIPO") + " - " + dblAnticipoDevolverConvertido);

                  movAnticipo.setFieldDouble("MP_SALDO_ANTICIPO", dblNvoSaldo);
                  movAnticipo.Modifica(oConn);
               }
            } else {
               strRes = movProv.getStrResultLast();
            }

         } else {
            strRes = "ERROR:El anticipo esta anulado";
         }
      } else {
         strRes = "ERROR:No existe el anticipo.";
      }

      return strRes;
   }

   /**
    * Genera la devolucion de un anticipo de cliente
    *
    * @return Regresa OK si todo fue exitoso
    */
   protected String devolucionAnticipoClie() {
      String strRes = "OK";
      //Recuperamos el anticipo
      vta_mov_cte movAnticipo = new vta_mov_cte();
      movAnticipo.ObtenDatos(this.intIdAnticipDevClie, oConn);
      if (movAnticipo.getFieldInt("CT_ID") != 0) {
         //Validamos que no este anulado el anticipo
         if (movAnticipo.getFieldInt("MC_ANULADO") == 0) {
            //Generar cargo a proveedores
            movCliente movClie = new movCliente(this.oConn, this.varSesiones, this.request);
            movClie.setBolTransaccionalidad(false);
            //Asignamos valores
            movClie.getCta_clie().setFieldInt("MC_ANTI_ID", movAnticipo.getFieldInt("MC_ID"));
            movClie.getCta_clie().setFieldInt("CT_ID", movAnticipo.getFieldInt("CT_ID"));
            movClie.getCta_clie().setFieldInt("SC_ID", movAnticipo.getFieldInt("SC_ID"));
            movClie.getCta_clie().setFieldInt("EMP_ID", movAnticipo.getFieldInt("EMP_ID"));
            movClie.getCta_clie().setFieldInt("MC_ESPAGO", 0);
            movClie.getCta_clie().setFieldInt("FAC_ID", 0);
            movClie.getCta_clie().setFieldInt("TKT_ID", 0);
            movClie.getCta_clie().setFieldInt("MC_MONEDA", this.cta_bcos.getFieldInt("MCB_MONEDA"));
            movClie.getCta_clie().setFieldDouble("MC_TASAPESO", this.cta_bcos.getFieldDouble("MCB_PARIDAD_DEV"));

            movClie.getCta_clie().setFieldString("MC_FECHA", this.cta_bcos.getFieldString("MCB_FECHA"));
            movClie.getCta_clie().setFieldString("MC_NOTAS", "Cargo por devolucion");
            movClie.getCta_clie().setFieldDouble("MC_CARGO", this.dblAnticipoImporteDevolver);

            movClie.getCta_clie().setFieldDouble("MC_IMPUESTO1", 0);
            movClie.getCta_clie().setFieldDouble("MC_IMPUESTO2", 0);
            movClie.getCta_clie().setFieldDouble("MC_IMPUESTO3", 0);
            movClie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO1", 0);
            movClie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO2", 0);
            movClie.getCta_clie().setFieldDouble("MC_TASAIMPUESTO3", 0);
            movClie.Init();
            movClie.doTrx();
            if (movClie.getStrResultLast().equals("OK")) {
               try {
                  this.cta_bcos.setFieldInt("MC_ID", Integer.valueOf(movClie.getCta_clie().getValorKey()));
               } catch (NumberFormatException ex) {
                  log.error(ex.getMessage());
               }
               this.cta_bcos.setFieldInt("MC_ID_DEV", this.intIdAnticipDevClie);
               this.cta_bcos.setFieldInt("MCB_DEV_CLIE", 1);
               //Evaluamos que el deposito no sea mayor al saldo del anticipo
               double dblAnticipoDevolverConvertido = this.dblAnticipoImporteDevolver;
               if (this.cta_bcos.getFieldDouble("MCB_PARIDAD_DEV") > 1) {
                  if (this.cta_bcos.getFieldInt("MCB_MONEDA") == 1
                          && (movAnticipo.getFieldInt("MC_MONEDA") == 2 || movAnticipo.getFieldInt("MC_MONEDA") == 3)) {
                     dblAnticipoDevolverConvertido = this.dblAnticipoImporteDevolver / this.cta_bcos.getFieldDouble("MCB_PARIDAD_DEV");
                  }
               }
               double dblDiff = dblAnticipoDevolverConvertido - movAnticipo.getFieldDouble("MC_SALDO_ANTICIPO");
               log.debug("Evaluamos importes " + this.dblAnticipoImporteDevolver + " convertido: " + dblAnticipoDevolverConvertido + " " + movAnticipo.getFieldDouble("MC_SALDO_ANTICIPO") + " dblDiff:" + dblDiff);
               if (dblDiff > 1) {
                  strRes = "ERROR:La devolucion no puede ser mayor al saldo del anticipo...";
               } else {
                  //Disminuir saldo en anticipo  
                  double dblNvoSaldo = movAnticipo.getFieldDouble("MC_SALDO_ANTICIPO") - dblAnticipoDevolverConvertido;
                  log.debug(movAnticipo.getFieldDouble("MC_SALDO_ANTICIPO") + " - " + dblAnticipoDevolverConvertido + " " + dblNvoSaldo);
                  movAnticipo.setFieldDouble("MC_SALDO_ANTICIPO", dblNvoSaldo);
                  movAnticipo.Modifica(oConn);
               }
            } else {
               strRes = movClie.getStrResultLast();
            }

         } else {
            strRes = "ERROR:El anticipo esta anulado";
         }
      } else {
         strRes = "ERROR:No existe el anticipo.";
      }
      return strRes;
   }

   /**
    * Genera la cancelacion de la devolucion de un anticipo de proveedor
    *
    * @return Regresa OK si todo fue exitoso
    */
   protected String devolucionAnticipoProvCancelar() {
      String strRes = "OK";
      //Obtenemos el anticipo
      vta_mov_prov movAnticipo = new vta_mov_prov();
      movAnticipo.ObtenDatos(this.cta_bcos.getFieldInt("MP_ID_DEV"), oConn);
      if (movAnticipo.getFieldInt("PV_ID") != 0) {

         //Buscamos el cargo
         vta_mov_prov movCargo = new vta_mov_prov();
         movCargo.ObtenDatos(this.cta_bcos.getFieldInt("MP_ID"), oConn);
         if (movCargo.getFieldInt("PV_ID") != 0) {

            //Aumentar saldo en anticipo  
            log.debug(movAnticipo.getFieldDouble("MP_SALDO_ANTICIPO") + " + " + movCargo.getFieldDouble("MP_CARGO"));
            movAnticipo.setFieldDouble("MP_SALDO_ANTICIPO", movAnticipo.getFieldDouble("MP_SALDO_ANTICIPO") + movCargo.getFieldDouble("MP_CARGO"));
            movAnticipo.Modifica(oConn);

            //Cancelar el cargo
            movCargo.setFieldInt("MP_ANULADO", 1);
            movCargo.setFieldString("MP_FECHAANUL", this.fecha.getFechaActual());
            movCargo.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
            movCargo.setFieldString("MP_HORAANUL", this.fecha.getHoraActual());
            movCargo.Modifica(oConn);
         } else {
            strRes = "ERROR:No existe el cargo de la devolucion.";
         }
      } else {
         strRes = "ERROR:No existe el anticipo.";
      }
      return strRes;
   }

   /**
    * Genera la cancelacion de la devolucion de un anticipo de cliente
    *
    * @return Regresa OK si todo fue exitoso
    */
   protected String devolucionAnticipoClieCancelar() {
      String strRes = "OK";
      //Obtenemos el anticipo
      vta_mov_cte movAnticipo = new vta_mov_cte();
      movAnticipo.ObtenDatos(this.cta_bcos.getFieldInt("MC_ID_DEV"), oConn);
      if (movAnticipo.getFieldInt("CT_ID") != 0) {
         //Buscamos el cargo
         vta_mov_cte movCargo = new vta_mov_cte();
         movCargo.ObtenDatos(this.cta_bcos.getFieldInt("MC_ID"), oConn);
         if (movCargo.getFieldInt("CT_ID") != 0) {
            //Aumentar saldo en anticipo  
            log.debug(movAnticipo.getFieldDouble("MC_SALDO_ANTICIPO") + " + " + movCargo.getFieldDouble("MC_CARGO"));
            movAnticipo.setFieldDouble("MC_SALDO_ANTICIPO", movAnticipo.getFieldDouble("MC_SALDO_ANTICIPO") + movCargo.getFieldDouble("MC_CARGO"));
            movAnticipo.Modifica(oConn);
            //Cancelar el cargo
            movCargo.setFieldInt("MC_ANULADO", 1);
            movCargo.setFieldString("MC_FECHAANUL", this.fecha.getFechaActual());
            movCargo.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
            movCargo.setFieldString("MC_HORAANUL", this.fecha.getHoraActual());
            movCargo.Modifica(oConn);
         } else {
            strRes = "ERROR:No existe el cargo de la devolucion.";
         }
      } else {
         strRes = "ERROR:No existe el anticipo.";
      }
      return strRes;
   }
   // </editor-fold>
}
