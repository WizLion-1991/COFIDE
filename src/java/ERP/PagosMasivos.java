package ERP;

import Tablas.vta_mov_cta_bcos_rela;
import Tablas.vta_mov_cte_mas;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Este objeto representa un pago masivo
 *
 * @author zeus
 */
public class PagosMasivos extends ProcesoMaster implements ProcesoInterfaz {

   protected vta_mov_cte_mas Masivo;
   protected ArrayList<movCliente> lstPagos;
   protected int intBanco = 1;
   protected double dblSaldoFavorUsado = 0;
   private int intCliente = 0;
   protected int intMoneda;
   protected double dblParidad;
   private static final  org.apache.logging.log4j.Logger log = LogManager.getLogger(PagosMasivos.class.getName());
   protected int intEMP_ID;
   protected int intSC_ID;
   protected boolean bolUsaAnticipo = false;

   public boolean isBolUsaAnticipo() {
      return bolUsaAnticipo;
   }

   /**
    *Define si se esta usando anticipos para no generar movimientos de bancos
    * @param bolUsaAnticipo
    */
   public void setBolUsaAnticipo(boolean bolUsaAnticipo) {
      this.bolUsaAnticipo = bolUsaAnticipo;
   }
   public int getIntEMP_ID() {
      return intEMP_ID;
   }
   /**
    *Define el id de la empresa
    * @param intEMP_ID Es el id de la empresa
    */
   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   public int getIntSC_ID() {
      return intSC_ID;
   }
   /**
    *Define el id de la sucursal
    * @param intSC_ID
    */
   public void setIntSC_ID(int intSC_ID) {
      this.intSC_ID = intSC_ID;
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

   public double getDblParidad() {
      return dblParidad;
   }

   public void setDblParidad(double dblParidad) {
      this.dblParidad = dblParidad;
   }

   public int getIntBanco() {
      return intBanco;
   }

   public void setIntBanco(int intBanco) {
      this.intBanco = intBanco;
   }

   public int getIntMoneda() {
      return intMoneda;
   }

   public void setIntMoneda(int intMoneda) {
      this.intMoneda = intMoneda;
   }

   public PagosMasivos(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.Masivo = new vta_mov_cte_mas();
      this.lstPagos = new ArrayList<movCliente>();
   }

//    /*Nuevo Metodo Inicializador*/
//    public PagosMasivos(Conexion oConn, VariableSession varSesiones, HttpServletRequest request, int intBanc) {
//        super(oConn, varSesiones, request);
//        this.Masivo = new vta_mov_cte_mas();
//        this.lstPagos = new ArrayList<movCliente>();
//        this.intBanco = intBanc;
//    }
   public void Init() {
      this.Masivo.setFieldInt("MCM_IDUSER", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.Masivo.getFieldInt("MCM_ID") != 0) {
         this.Masivo.ObtenDatos(this.Masivo.getFieldInt("MCM_ID"), oConn);
         //Recuperamos todas las operaciones de pagos
         String strSql = "SELECT MC_ID FROM vta_mov_cte where MCM_ID = " + this.Masivo.getFieldInt("MCM_ID") + " AND MC_ESPAGO = 1";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               int intMC_ID = rs.getInt("MC_ID");
               movCliente mov = new movCliente(oConn, varSesiones, request);
               mov.getCta_clie().setFieldInt("MC_ID", intMC_ID);
               mov.Init();
               this.lstPagos.add(mov);
            }
            rs.close();
         } catch (SQLException ex) {
            Logger.getLogger(PagosMasivos.class.getName()).log(Level.SEVERE, null, ex);
         }

      }
   }

   public void doTrx() {
      //Movimiento de pago inicial para vincularlo a la contabilidad
      movCliente movPagoCon = null;
      //Lista para la relacion de pagos
      ArrayList<vta_mov_cta_bcos_rela> relPagos = new ArrayList<vta_mov_cta_bcos_rela>();
      this.strResultLast = "OK";
      this.Masivo.setFieldString("MCM_FECHACREATE", this.fecha.getFechaActual());
      this.Masivo.setFieldString("MCM_HORA", this.fecha.getHoraActual());
      //Validamos que todos los campos basico se encuentren
      if (this.Masivo.getFieldString("MCM_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.Masivo.getFieldDouble("MCM_TOTOPER") == 0) {
         this.strResultLast = "ERROR:FALTA DEFINIR POR LO MENOS UNA OPERACION";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.Masivo.getFieldString("MCM_FECHA"), this.intEMP_ID, this.intSC_ID);
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Inicializamos la operacion
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         this.Masivo.setBolGetAutonumeric(true);
         String strRes1 = this.Masivo.Agrega(oConn);
         if (strRes1.equals("OK")) {
            int intId = Integer.valueOf(this.Masivo.getValorKey());
            Iterator<movCliente> it = this.lstPagos.iterator();
            double dblTotalPago = 0;
            double dblTotalImpuesto1 = 0;
            while (it.hasNext()) {
               movCliente deta = it.next();
               //Movimiento para la contabilidad
               if (movPagoCon == null) {
                  movPagoCon = deta;
               }
               intCliente = deta.getCta_clie().getFieldInt("CT_ID");
               deta.setBolAplicaBanco(false);
               deta.setBolAplicaConta(false);
               deta.setBolTransaccionalidad(false);
               deta.getCta_clie().setFieldInt("MCM_ID", intId);
               dblTotalPago += deta.getCta_clie().getFieldDouble("MC_ABONO");
               
               deta.doTrx();
               String strRes2 = deta.getStrResultLast();
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
                  break;
               } else {
                  dblTotalImpuesto1 += deta.getCta_clie().getFieldDouble("MC_IMPUESTO1");
                  //Guardamos la relacion de pagos con el movimiento del banco
                  vta_mov_cta_bcos_rela newRela = new vta_mov_cta_bcos_rela();
                  newRela.setFieldInt("MC_ID", deta.getCta_clie().getFieldInt("MC_ID"));
                  relPagos.add(newRela);
               }
            }
            // <editor-fold defaultstate="collapsed" desc="Guardamos el movimiento del banco si hay un banco seleccionado">
            if (this.intBanco != 0 && !bolUsaAnticipo) {
               Bancos banco = new Bancos(this.oConn, this.varSesiones, this.request);
               banco.Init();
               banco.setBolTransaccionalidad(false);
               banco.getCta_bcos().setFieldString("MCB_FECHA", this.lstPagos.get(0).cta_clie.getFieldString("MC_FECHA"));
               banco.getCta_bcos().setFieldInt("SC_ID", this.lstPagos.get(0).cta_clie.getFieldInt("SC_ID"));
               banco.getCta_bcos().setFieldInt("BC_ID", this.lstPagos.get(0).getIntBc_Id());
               banco.getCta_bcos().setFieldInt("FAC_ID", this.lstPagos.get(0).cta_clie.getFieldInt("FAC_ID"));
               banco.getCta_bcos().setFieldInt("TKT_ID", this.lstPagos.get(0).cta_clie.getFieldInt("TKT_ID"));
               banco.getCta_bcos().setFieldInt("PD_ID", this.lstPagos.get(0).cta_clie.getFieldInt("PD_ID"));
               banco.getCta_bcos().setFieldInt("MC_ID", this.lstPagos.get(0).cta_clie.getFieldInt("MC_ID"));
               banco.getCta_bcos().setFieldInt("EMP_ID", this.lstPagos.get(0).cta_clie.getFieldInt("EMP_ID"));
               banco.getCta_bcos().setFieldInt("MCB_CONCILIADO", 1);
               banco.getCta_bcos().setFieldDouble("MCB_DEPOSITO", dblTotalPago);
               banco.getCta_bcos().setFieldDouble("MCB_RETIRO", 0);
               banco.getCta_bcos().setFieldDouble("MCB_MONEDA", this.lstPagos.get(0).cta_clie.getFieldInt("MC_MONEDA"));
               banco.getCta_bcos().setFieldDouble("MCB_PARIDAD", this.lstPagos.get(0).cta_clie.getFieldInt("MC_TASAPESO"));
               banco.setGeneraConta(false);
               banco.doTrx();
               if (!banco.getStrResultLast().equals("OK")) {
                  this.strResultLast = banco.getStrResultLast();
               } else {
                  //Guardamos el id del movimiento del banco con el pago
                  Iterator<vta_mov_cta_bcos_rela> it3 = relPagos.iterator();
                  while (it3.hasNext()) {
                     vta_mov_cta_bcos_rela detaRel = it3.next();
                     detaRel.setFieldInt("MCB_ID", Integer.valueOf(banco.getCta_bcos().getValorKey()));
                     detaRel.Agrega(oConn);
                  }
                  // <editor-fold defaultstate="collapsed" desc="Evaluamos si se ocupo un saldo a favor para ajustarlos">
//                  if (this.dblSaldoFavorUsado > 0) {
//                     movCliente movAjuste = new movCliente(oConn, varSesiones, request);
//                     movAjuste.setDblSaldoFavorUsado(dblSaldoFavorUsado);
//                     movAjuste.doAjustaSaldoFavor(this.lstPagos.get(0).cta_clie.getFieldInt("CT_ID"), this.lstPagos.get(0).cta_clie.getFieldInt("MC_MONEDA"),
//                             this.intMoneda,this.getDblParidad());
//                     //Evaluamos si hubo uno falla en el ajuste
//                     if(!movAjuste.getStrResultLast().equals("OK")){
//                        this.strResultLast = movAjuste.getStrResultLast();
//                     }
//                  }
                  // </editor-fold>                  
               }
            }
            // </editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Guardamos la contabilidad global">
            if (movPagoCon != null) {
               ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
               contaUtil.setDblTmp2(dblTotalPago);
               contaUtil.setDblTmp3(dblTotalImpuesto1);
               contaUtil.setPagosMasivos(this);
               log.debug("dblTotalPago:" + dblTotalPago);
               log.debug("dblTotalImpuesto1:" + dblTotalImpuesto1);
               contaUtil.CalculaPolizaContableCobros(movPagoCon.getCta_clie().getFieldInt("MC_MONEDA"), movPagoCon.getCta_clie(), movPagoCon.getCta_clie().getFieldInt("MC_ID"), "NEW");
            }
            // </editor-fold>
            
            this.saveBitacora("PAGO MASIVO", "NUEVA", intId);
         } else {
            this.strResultLast = strRes1;
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
   }

   public void doTrxAnul() {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.Masivo.getFieldInt("MCM_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.Masivo.getFieldString("MCM_FECHA"), this.intEMP_ID, this.intSC_ID);
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         //Validamos que no este anulado
         if (this.Masivo.getFieldInt("MCM_ANULADO") == 0) {
            //Inicializamos la operacion
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            //Definimos campos
            this.Masivo.setFieldInt("MCM_IDUSERANUL", this.varSesiones.getIntNoUser());
            this.Masivo.setFieldInt("MCM_ANULADO", 1);
            this.Masivo.setFieldString("MCM_HORAANUL", this.fecha.getHoraActual());
            this.Masivo.setFieldString("MCM_FECHAANUL", this.fecha.getFechaActual());
            String strResp1 = this.Masivo.Modifica(oConn);
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               //Cancelamos todos los pagos relacionados...
               Iterator<movCliente> it = this.lstPagos.iterator();
               while (it.hasNext()) {
                  movCliente deta = it.next();
                  deta.setBolTransaccionalidad(false);
                  deta.doTrxAnul();
                  String strRes2 = deta.getStrResultLast();
                  //Validamos si todo fue satisfactorio
                  if (!strRes2.equals("OK")) {
                     this.strResultLast = strRes2;
                     break;
                  }
               }
            }
            this.saveBitacora("PAGO MASIVO", "ANULAR", this.Masivo.getFieldInt("MCM_ID"));
            //Terminamos la operacion
            if (this.bolTransaccionalidad) {
               if (this.strResultLast.equals("OK")) {
                  this.oConn.runQueryLMD("COMMIT");
               } else {
                  this.oConn.runQueryLMD("ROLLBACK");
               }
            }
         } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
         }

      }
   }

   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(movCliente deta) {
      this.Masivo.setFieldDouble("MCM_TOTOPER", this.Masivo.getFieldDouble("MCM_TOTOPER") + deta.getCta_clie().getFieldDouble("MC_ABONO"));
      this.lstPagos.add(deta);
   }

   /**
    * Regresa el encabezado de pagos masivos
    *
    * @return Nos regres el objeto maestro de pagos masivos
    */
   public vta_mov_cte_mas getMasivo() {
      return Masivo;
   }
}
