package ERP;

import Tablas.vta_mov_cta_bcos_rela;
import Tablas.vta_mov_prov_mas;
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
public class PagosMasivosCtas extends ProcesoMaster implements ProcesoInterfaz {

   protected vta_mov_prov_mas Masivo;
   protected ArrayList<MovProveedor> lstPagos;
   //FECHA: 12/07/2013
   //NOMBRE: ABRAHAM GONZALEZ HERNANDEZ
   //DESCRIPCION: Se agregaron los atributos moneda, paridad y montopagado a la clase
   protected int intMoneda;
   protected double dblParidad;
   protected double dblMontoPagado;
   protected double dblSaldoFavorUsado = 0;
   private int intProveedor = 0;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PagosMasivosCtas.class.getName());
   protected String strNoCheque;
   protected int intEMP_ID;
   protected int intSC_ID;
   protected boolean bolUsaAnticipo = false;
   protected double dblFactorConv = 0.0;
   protected double dblComision = 0.0;

   protected String strConcepto;
   protected String strRFCBeneficiario;

   public double getDblComision() {
      return dblComision;
   }

   public void setDblComision(double dblComision) {
      this.dblComision = dblComision;
   }

   public double getDblFactorConv() {
      return dblFactorConv;
   }

   public void setDblFactorConv(double dblFactorConv) {
      this.dblFactorConv = dblFactorConv;
   }

   public boolean isBolUsaAnticipo() {
      return bolUsaAnticipo;
   }

   /**
    * Define si se esta usando anticipos para no generar movimientos de bancos
    *
    * @param bolUsaAnticipo
    */
   public void setBolUsaAnticipo(boolean bolUsaAnticipo) {
      this.bolUsaAnticipo = bolUsaAnticipo;
   }

   public int getIntEMP_ID() {
      return intEMP_ID;
   }

   /**
    * Define el id de la empresa
    *
    * @param intEMP_ID Es el id de la empresa
    */
   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   public int getIntSC_ID() {
      return intSC_ID;
   }

   /**
    * Define el id de la sucursal
    *
    * @param intSC_ID
    */
   public void setIntSC_ID(int intSC_ID) {
      this.intSC_ID = intSC_ID;
   }

   /**
    * Es el No de Cheque en caso que lo use...
    *
    *
    * @return Regresa el numero de cheque
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
    * @return Regresa el rfc de beneficiario de terceros
    */
   public String getStrRFCBeneficiario() {
      return strRFCBeneficiario;
   }

   public void setStrRFCBeneficiario(String strRFCBeneficiario) {
      this.strRFCBeneficiario = strRFCBeneficiario;
   }

   public String getStrConcepto() {
      return strConcepto;
   }

   public void setStrConcepto(String strConcepto) {
      this.strConcepto = strConcepto;
   }

   public void setDblSaldoFavorUsado(double dblSaldoFavorUsado) {
      this.dblSaldoFavorUsado = dblSaldoFavorUsado;
   }

   public double getDblParidad() {
      return dblParidad;
   }

   public void setDblParidad(double dblParidad) {
      this.dblParidad = dblParidad;
   }

   public double getDblMontoPagado() {
      return dblMontoPagado;
   }

   public void setDblMontoPagado(double dblMontoPagado) {
      this.dblMontoPagado = dblMontoPagado;
   }

   public int getIntMoneda() {
      return intMoneda;
   }

   public void setIntMoneda(int intMoneda) {
      this.intMoneda = intMoneda;
   }

   public PagosMasivosCtas(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.Masivo = new vta_mov_prov_mas();
      this.lstPagos = new ArrayList<MovProveedor>();
   }

   @Override
   public void Init() {
      this.Masivo.setFieldInt("MPM_IDUSER", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.Masivo.getFieldInt("MPM_ID") != 0) {
         this.Masivo.ObtenDatos(this.Masivo.getFieldInt("MPM_ID"), oConn);
         //Recuperamos todas las operaciones de pagos
         String strSql = "SELECT MP_ID FROM vta_mov_prov where MPM_ID = " + this.Masivo.getFieldInt("MPM_ID") + " AND MP_ESPAGO = 1";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               int intMP_ID = rs.getInt("MP_ID");
               MovProveedor mov = new MovProveedor(oConn, varSesiones, request);
               mov.getCta_prov().setFieldInt("MP_ID", intMP_ID);
               mov.Init();
               this.lstPagos.add(mov);
            }
            rs.close();
         } catch (SQLException ex) {
            Logger.getLogger(PagosMasivos.class.getName()).log(Level.SEVERE, null, ex);
         }

      }
   }

   @Override
   public void doTrx() {
      log.debug("Inicia pagos masivos...");
      //Movimiento de pago inicial para vincularlo a la contabilidad
      MovProveedor movPagoCon = null;
      //Lista para la relacion de pagos
      ArrayList<vta_mov_cta_bcos_rela> relPagos = new ArrayList<vta_mov_cta_bcos_rela>();
      this.strResultLast = "OK";
      this.Masivo.setFieldString("MPM_FECHACREATE", this.fecha.getFechaActual());
      this.Masivo.setFieldString("MPM_HORA", this.fecha.getHoraActual());
      //Validamos que todos los campos basico se encuentren
      if (this.Masivo.getFieldString("MPM_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      if (this.Masivo.getFieldDouble("MPM_TOTOPER") == 0) {
         this.strResultLast = "ERROR:FALTA DEFINIR POR LO MENOS UNA OPERACION";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.Masivo.getFieldString("MPM_FECHA"), this.intEMP_ID, this.intSC_ID);
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
            double dblTotalPago = 0;
            double dblTotalImpuesto1 = 0;
            Iterator<MovProveedor> it = this.lstPagos.iterator();
            while (it.hasNext()) {
               MovProveedor deta = it.next();
               //Movimiento para la contabilidad
               if (movPagoCon == null) {
                  movPagoCon = deta;
               }
               intProveedor = deta.getCta_prov().getFieldInt("PV_ID");
               deta.setBolTransaccionalidad(false);
               deta.getCta_prov().setFieldInt("MPM_ID", intId);
               deta.setBolAplicaBanco(false);
               deta.setBolAplicaConta(false);//No aplica la contabilidad individual solo la masiva
               deta.doTrx();
               dblTotalPago += deta.getCta_prov().getFieldDouble("MP_ABONO");
               String strRes2 = deta.getStrResultLast();
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
                  break;
               } else {
                  dblTotalImpuesto1 += deta.getCta_prov().getFieldDouble("MP_IMPUESTO1");
                  //Guardamos la relacion de pagos con el movimiento del banco
                  vta_mov_cta_bcos_rela newRela = new vta_mov_cta_bcos_rela();
                  newRela.setFieldInt("MP_ID", deta.getCta_prov().getFieldInt("MP_ID"));
                  relPagos.add(newRela);
               }
            }
            //Guardar si todo salio bien....
            if (this.strResultLast.equals("OK")) {
               // <editor-fold defaultstate="collapsed" desc="Guardamos el movimiento del banco si hay un banco seleccionado">
               if (this.lstPagos.get(0).getIntBc_Id() != 0 && !bolUsaAnticipo) {
                  log.debug("Inicia guardado de banco masivo....");
                  Bancos banco = new Bancos(this.oConn, this.varSesiones, this.request);
                  banco.Init();
                  banco.setDblComision(this.dblComision);
                  banco.setBolTransaccionalidad(false);
                  banco.getCta_bcos().setFieldString("MCB_FECHA", this.lstPagos.get(0).cta_prov.getFieldString("MP_FECHA"));
                  banco.getCta_bcos().setFieldInt("SC_ID", this.lstPagos.get(0).cta_prov.getFieldInt("SC_ID"));
                  banco.getCta_bcos().setFieldInt("BC_ID", this.lstPagos.get(0).getIntBc_Id());
                  banco.getCta_bcos().setFieldInt("FAC_ID", this.lstPagos.get(0).cta_prov.getFieldInt("FAC_ID"));
                  banco.getCta_bcos().setFieldInt("TKT_ID", this.lstPagos.get(0).cta_prov.getFieldInt("TKT_ID"));
                  banco.getCta_bcos().setFieldInt("PD_ID", this.lstPagos.get(0).cta_prov.getFieldInt("PD_ID"));
                  banco.getCta_bcos().setFieldInt("MP_ID", this.lstPagos.get(0).cta_prov.getFieldInt("MP_ID"));
                  banco.getCta_bcos().setFieldInt("EMP_ID", this.lstPagos.get(0).cta_prov.getFieldInt("EMP_ID"));
                  banco.getCta_bcos().setFieldInt("MCB_CONCILIADO", 1);
                  banco.getCta_bcos().setFieldDouble("MCB_DEPOSITO", 0);
                  banco.getCta_bcos().setFieldDouble("MCB_RETIRO", this.dblMontoPagado);
                  banco.getCta_bcos().setFieldInt("MCB_MONEDA", this.lstPagos.get(0).cta_prov.getFieldInt("MP_MONEDA"));
                  banco.getCta_bcos().setFieldDouble("MCB_PARIDAD", this.lstPagos.get(0).cta_prov.getFieldInt("MP_TASAPESO"));
                  banco.getCta_bcos().setFieldString("MCB_NOCHEQUE", this.strNoCheque);
                  banco.getCta_bcos().setFieldString("MCB_CONCEPTO", this.strConcepto);
                  banco.getCta_bcos().setFieldString("MCB_RFCBENEFICIARIO", this.strRFCBeneficiario);
                  banco.setGeneraConta(false);

                  // <editor-fold defaultstate="collapsed" desc="Agregar Moneda y paridad">
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
                     if (this.dblSaldoFavorUsado > 0) {
//                     log.debug("Usaremos un saldo a favor " + dblSaldoFavorUsado);
//                     MovProveedor movAjuste = new MovProveedor(oConn, varSesiones, request);
//                     movAjuste.setDblSaldoFavorUsado(dblSaldoFavorUsado);
//                     movAjuste.doAjustaSaldoFavor(this.lstPagos.get(0).cta_prov.getFieldInt("PV_ID"),this.lstPagos.get(0).cta_prov.getFieldInt("MP_MONEDA"),
//                             this.getIntMoneda(), this.getDblParidad());
//                     //Evaluamos si hubo uno falla en el ajuste
//                     if (!movAjuste.getStrResultLast().equals("OK")) {
//                        this.strResultLast = movAjuste.getStrResultLast();
//                     }
                     }
                     // </editor-fold>
                  }
                  // </editor-fold>
               }
            // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Guardamos la contabilidad global">
               if (this.strResultLast.equals("OK")) {
                  if (movPagoCon != null) {
                     ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
                     contaUtil.setDblTmp2(dblTotalPago);
                     contaUtil.setDblTmp3(dblTotalImpuesto1);
                     contaUtil.setPagosMasivosCtas(this);
                     log.debug("dblTotalPago:" + dblTotalPago);
                     contaUtil.CalculaPolizaContablePagos(movPagoCon.getCta_prov().getFieldInt("MP_MONEDA"), movPagoCon.getCta_prov(), movPagoCon.getCta_prov().getFieldInt("MP_ID"), "NEW");
                  }
               }
               // </editor-fold>

               this.saveBitacora("PAGO MASIVO CUENTA", "NUEVA", intId);
            }

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

   @Override
   public void doTrxAnul() {
      this.strResultLast = "OK";
      //Movimiento de pago inicial para vincularlo a la contabilidad
      MovProveedor movPagoCon = null;
      //Validamos que todos los campos basico se encuentren
      if (this.Masivo.getFieldInt("MPM_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.Masivo.getFieldString("MPM_FECHA"), this.intEMP_ID, this.intSC_ID);
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
            this.Masivo.setFieldInt("MPM_IDUSERANUL", this.varSesiones.getIntNoUser());
            this.Masivo.setFieldInt("MPM_ANULADO", 1);
            this.Masivo.setFieldString("MPM_HORAANUL", this.fecha.getHoraActual());
            this.Masivo.setFieldString("MPM_FECHAANUL", this.fecha.getFechaActual());
            String strResp1 = this.Masivo.Modifica(oConn);
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            } else {
               //Cancelamos todos los pagos relacionados...
               Iterator<MovProveedor> it = this.lstPagos.iterator();
               while (it.hasNext()) {
                  MovProveedor deta = it.next();
                  //Movimiento para la contabilidad
                  if (movPagoCon == null) {
                     movPagoCon = deta;
                  }
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
            // <editor-fold defaultstate="collapsed" desc="Guardamos la contabilidad global">
            if (movPagoCon != null) {
               ContabilidadUtil contaUtil = new ContabilidadUtil(oConn, varSesiones);
               //Falta funcionalidad cuando se cancela...
               //contaUtil.(movPagoCon.getCta_prov().getFieldInt("MP_MONEDA"), movPagoCon.getCta_prov(), movPagoCon.getCta_prov().getFieldInt("MP_ID"), "NEW");
            }
            // </editor-fold>

            this.saveBitacora("PAGO MASIVO CUENTA", "ANULAR", this.Masivo.getFieldInt("MPM_ID"));
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

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(MovProveedor deta) {
      this.Masivo.setFieldDouble("MPM_TOTOPER", this.Masivo.getFieldDouble("MPM_TOTOPER") + deta.getCta_prov().getFieldDouble("MP_ABONO"));
      this.lstPagos.add(deta);
   }

   /**
    * Regresa el encabezado de pagos masivos
    *
    * @return Nos regres el objeto maestro de pagos masivos
    */
   public vta_mov_prov_mas getMasivo() {
      return Masivo;
   }
}
