/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_cxpagar;
import Tablas.vta_cxpagardetalle;
import Tablas.vta_movproddeta;
import Tablas.vta_pedimentos;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Esta clase realiza las operaciones de negocios de los pedimentos
 *
 * @author aleph_79
 */
public class Pedimentos extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   public static final int ENTRADA_COMPRA = 1;
   public static final int GASTOS_PEDIMENTO = 2;
   private int intCXP_ID = 0;
   private int intPED_ID = 0;
   private int intTipoCXP = 0;
   private String strPedimentoCod;
   private int intMonedaDefa = 0;
   private static final Logger log = LogManager.getLogger(Pedimentos.class.getName());
   private Double dblTipoCambio;
   protected int intTipoCosteo;
   protected double dblTotalImporteCostos = 0;
   double dblTotalProrrateo = 0;

   public Double getDblTipoCambio() {
      return dblTipoCambio;
   }

   /**
    * Define el tipo de cambio del pedimento
    *
    * @param dblTipoCambio Es el tipo de cambio
    */
   public void setDblTipoCambio(Double dblTipoCambio) {
      this.dblTipoCambio = dblTipoCambio;
   }

   public int getIntPED_ID() {
      return intPED_ID;
   }

   /**
    * Define el id del pedimento
    *
    * @param intPED_ID Es el id del numero de pedimento
    */
   public void setIntPED_ID(int intPED_ID) {
      this.intPED_ID = intPED_ID;
   }

   public String getStrPedimentoCod() {
      return strPedimentoCod;
   }

   /**
    * Regresa el total de costos despues de calcular el prorrateo
    *
    * @return Es un valor numerico decimal
    */
   public double getDblTotalImporteCostos() {
      return dblTotalImporteCostos;
   }

   /**
    * Regresa el total de gastos despues de calcular el prorrateo
    *
    * @return Es un valor numerico decimal
    */
   public double getDblTotalProrrateo() {
      return dblTotalProrrateo;
   }

   /**
    * Define la clave del pedimento
    *
    * @param strPedimentoCod Es el codigo o clave del pedimento
    */
   public void setStrPedimentoCod(String strPedimentoCod) {
      this.strPedimentoCod = strPedimentoCod;
   }

   public int getIntCXP_ID() {
      return intCXP_ID;
   }

   /**
    * Define la cuenta por pagar
    *
    * @param intCXP_ID Es el id de cuenta por pagar
    */
   public void setIntCXP_ID(int intCXP_ID) {
      this.intCXP_ID = intCXP_ID;
   }

   public int getIntTipoCXP() {
      return intTipoCXP;
   }

   /**
    * Define el tipo de cuenta por pagar
    *
    * @param intTipoCXP Es el id del tipo de cuenta por pagar
    */
   public void setIntTipoCXP(int intTipoCXP) {
      this.intTipoCXP = intTipoCXP;
   }

   public int getIntMonedaDefa() {
      return intMonedaDefa;
   }

   /**
    * Define la moneda default
    *
    * @param intMonedaDefa Es el id de la moneda default
    */
   public void setIntMonedaDefa(int intMonedaDefa) {
      this.intMonedaDefa = intMonedaDefa;
   }

   /**
    * Nos regresa el tipo de costeo PEPS UEPS Y PROMEDIO
    *
    * @return Nos regresa un entero con el tipo de costeo
    */
   public int getIntTipoCosteo() {
      return intTipoCosteo;
   }

   /**
    * Definimos el tipo de costeo PEPS UEPS Y PROMEDIO
    *
    * @param intTipoCosteo Es un entero con el tipo de costeo
    */
   public void setIntTipoCosteo(int intTipoCosteo) {
      this.intTipoCosteo = intTipoCosteo;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Pedimentos(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      strPedimentoCod = "";
   }

   public Pedimentos(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      strPedimentoCod = "";
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   public void Init() {
   }

   public void doTrx() {
      this.strResultLast = "OK";
      //Obtenemos los datos de la cuenta por pagar
      vta_cxpagar cxpagar = new vta_cxpagar();
      cxpagar.ObtenDatos(intCXP_ID, oConn);
      if (cxpagar.getFieldInt("CXP_ID") != 0) {
         //Dependiendo del tipo validamos la ODC
         boolean bolPasa = true;
         //Validamos si viene de una entrada en caso de proceder
         if (this.intTipoCXP == Pedimentos.ENTRADA_COMPRA) {
            if (cxpagar.getFieldInt("ODC_ID") == 0) {
               this.strResultLast = "ERROR: La cuenta por pagar no corresponde a una entrada de compra";
               bolPasa = false;
            }
         }
         //Validamos que no este anulada
         if (cxpagar.getFieldInt("CXP_ANULADO") != 0) {
            this.strResultLast = "ERROR: La cuenta por pagar esta anulada";
            bolPasa = false;
         }
         //Validamos que no este vinculado a un pedimento
         if (cxpagar.getFieldInt("PED_ID") != 0) {
            this.strResultLast = "ERROR: La cuenta por pagar ya esta vinculado a un pedimento";
            bolPasa = false;
         }
         //Si paso las validaciones procedemos a ingresarla
         if (bolPasa) {
            cxpagar.setFieldInt("PED_ID", this.intPED_ID);
            cxpagar.setFieldString("PED_COD", this.strPedimentoCod);
            cxpagar.Modifica(oConn);
         }
      } else {
         this.strResultLast = "ERROR: La cuenta por pagar no existe";
      }
   }

   public void doTrxAnul() {
      this.strResultLast = "OK";
      //Obtenemos los datos de la cuenta por pagar
      vta_cxpagar cxpagar = new vta_cxpagar();
      cxpagar.ObtenDatos(intCXP_ID, oConn);
      if (cxpagar.getFieldInt("CXP_ID") != 0) {
         //Dependiendo del tipo validamos la ODC
         boolean bolPasa = true;
         //Validamos que este vinculada a un pedimento
         if (cxpagar.getFieldInt("PED_ID") == 0) {
            this.strResultLast = "ERROR: La cuenta por pagar no esta vinculado a un pedimento";
            bolPasa = false;
         } else {
            //Validamos que el pedimento no halla generado prorrateo
            vta_pedimentos pedimento = new vta_pedimentos();
            pedimento.ObtenDatos(cxpagar.getFieldInt("PED_ID"), oConn);
            if (pedimento.getFieldInt("PED_APLICADO") != 0) {
               this.strResultLast = "ERROR: El pedimento ya fue aplicado no puede modificarlo";
               bolPasa = false;
            }
         }
         //Si paso las validaciones procedemos a quitarle la vinculacion
         if (bolPasa) {
            cxpagar.setFieldInt("PED_ID", 0);
            cxpagar.setFieldString("PED_COD", "");
            cxpagar.Modifica(oConn);
         }
      } else {
         this.strResultLast = "ERROR: La cuenta por pagar no existe";
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
    * Genera el calculo y aplica el prorrateo
    */
   public void doGeneraProrrateo() {
      this.strResultLast = "OK";
      vta_pedimentos pedimento = new vta_pedimentos();
      pedimento.ObtenDatos(intPED_ID, oConn);
      // <editor-fold defaultstate="collapsed" desc="Validaciones">
      if (pedimento.getFieldInt("PED_ID") == 0) {
         this.strResultLast = "Error:Pedimento no existe";
      }
      if (pedimento.getFieldInt("PED_APLICADO") == 1) {
         this.strResultLast = "Error:Pedimento ya tiene aplicado el prorrateo";
      }
      // </editor-fold>
      //Si fueron exitosas las validaciones continuamos
      if (this.strResultLast.equals("OK")) {
         //Calculamos el prorrateo
         ArrayList<vta_cxpagar> lstCXPxSave = doCalculaProrrateo();
         //Hacemos la conversion
         CambioMoneda(lstCXPxSave, 4, oConn);

         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>

         //Iteramos en cada recepcion de almacen para actualizar costos y movimientos
         Iterator<vta_cxpagar> it = lstCXPxSave.iterator();
         while (it.hasNext()) {
            vta_cxpagar cxp = it.next();
            Iterator<vta_movproddeta> itE = cxp.getLstEntradas().iterator();
            while (itE.hasNext()) {
               vta_movproddeta movDeta = itE.next();

               double dblCostoUsar = movDeta.getFieldDouble("MPD_COSTO");
               double dblCostoPromedio = movDeta.getFieldDouble("MPD_COSTO");
               if (this.intTipoCosteo == Inventario.INV_UEPS) {
               }
               if (this.intTipoCosteo == Inventario.INV_PEPS) {
               }
               if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                  //Calculamos el costo promedio
                  Inventario inv = new Inventario(oConn, varSesiones, null);
                  inv.doCalculaPromedio(movDeta.getFieldInt("PR_ID"),
                          dblCostoUsar,
                          movDeta.getFieldDouble("MPD_ENTRADAS"),
                          true, true, movDeta.getFieldInt("MP_ID"));
                  dblCostoPromedio = inv.getDblLastCostoPromedio();
               }

               movDeta.setFieldDouble("MPD_COSTO_PRORRATEO", movDeta.getFieldDouble("MPD_COSTO"));
               movDeta.setFieldDouble("MPD_COSTO", dblCostoUsar + movDeta.getDblCostoProrrateo());
               movDeta.setFieldDouble("MPD_PROPOR_PRORRATEO", movDeta.getDblProporProrrateo());
               movDeta.Modifica(oConn);
               //Actualizamos todos los movimientos hechos
               String strRespD = doActualizaCostoMovs(movDeta);
               if (!strRespD.equals("OK")) {
                  this.strResultLast = strRespD;
               }
            }
         }
         //Marcamos el pedimento como que ya genero todo

         pedimento.setFieldInt("PED_APLICADO", 1);
         pedimento.setFieldInt("PED_USR_APLIC", this.varSesiones.getIntNoUser());
         pedimento.setFieldString("PED_FECHA_APLIC", fecha.getFechaActual());
         pedimento.setFieldString("PED_HORA_APLIC", fecha.getHoraActual());
         pedimento.Modifica(oConn);

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
    * Calcula el prorrateo
    *
    * @return Regresa la lista de las cuentas por pagar
    */
   public void Calcula() {
   }

   /**
    * Calcula el prorrateo de un pedimento
    *
    * @return Regresa una lista de cuentas por pagar para el prorrateo
    */
   public ArrayList<vta_cxpagar> doCalculaProrrateo() {
      vta_cxpagar cxpagarTmp = new vta_cxpagar();
      //Obtenemos la moneda base del pedimento
      vta_pedimentos pedimento = new vta_pedimentos();
      pedimento.ObtenDatos(intPED_ID, oConn);
      int intMonedaBase = pedimento.getFieldInt("MON_ID");
      this.intMonedaDefa = pedimento.getFieldInt("MON_ID");
      //
      //Objeto para las conversiones
      Monedas moneda = new Monedas(oConn);
      log.debug("Inicia carga de cuentas por pagar...");
      //Cargamos todas las cuentas por pagar del pedimento
      ArrayList<vta_cxpagar> lstCXPagar = cxpagarTmp.getMovCXP(intPED_ID, oConn);
      log.debug("Termina carga de cuentas por pagar...");
      //Calculamos el total por prorratear
      Iterator<vta_cxpagar> it = lstCXPagar.iterator();
      while (it.hasNext()) {
         vta_cxpagar cxpagar = it.next();
         if (cxpagar.getFieldInt("ODC_ID") == 0) {
            double dblImporteProCXP = 0;
            Iterator<vta_cxpagardetalle> it2 = cxpagar.getLstDetalleCXP().iterator();
            while (it2.hasNext()) {
               vta_cxpagardetalle deta = it2.next();
               if (deta.isProrrateable(oConn)) {
                  log.debug("Si se prorratea " + deta.getFieldInt("CXP_ID") + " deta " + deta.getFieldInt("CXPD_ID") + " " + deta.getFieldDouble("CXPD_IMPORTE"));
                  dblImporteProCXP += (deta.getFieldDouble("CXPD_IMPORTE") - deta.getFieldDouble("CXPD_IMPUESTO1"));
               }
            }
            //Convertimos a la moneda base del pedimento
            int intMonedaCXP = cxpagar.getFieldInt("CXP_MONEDA");
            log.debug("MonedaBase: " + intMonedaBase);
            if (intMonedaBase != intMonedaCXP) {
               double dblFactorConversion = 1/*moneda.GetFactorConversion(cxpagar.getFieldString("CXP_FECHA"), 4, intMonedaCXP,intMonedaBase );*/;
               if (intMonedaBase == 1) {
                  if (intMonedaCXP == 2) {
                     dblFactorConversion = pedimento.getFieldDouble("PD_DOLAR");
                  } else {
                     dblFactorConversion = pedimento.getFieldDouble("PD_DOLAR") * pedimento.getFieldDouble("PED_PARIDAD");
                  }
               } else {
                  if (intMonedaBase == 2) {
                     if (intMonedaCXP == 1) {
                        dblFactorConversion = 1 / pedimento.getFieldDouble("PD_DOLAR");
                     } else {
                        dblFactorConversion = pedimento.getFieldDouble("PED_PARIDAD");
                     }
                  } else {
                     if (intMonedaCXP == 1) {
                        dblFactorConversion = 1 / pedimento.getFieldDouble("PD_DOLAR") * 1 / pedimento.getFieldDouble("PED_PARIDAD");
                     } else {
                        if (intMonedaCXP == 2) {
                           dblFactorConversion = 1 / pedimento.getFieldDouble("PED_PARIDAD");
                        } else {
                           dblFactorConversion = 1;
                        }
                     }
                  }
               }
//               if (intMonedaCXP == 1) {
//                  dblFactorConversion = 1/pedimento.getFieldDouble("PD_DOLAR");
//               } else {
//                  if (intMonedaCXP == 2) {
//                     dblFactorConversion = 1;
//                  }else{
//                     dblFactorConversion = pedimento.getFieldDouble("PD_DOLAR") * pedimento.getFieldDouble("PED_PARIDAD");
//                  }
//               }
               log.debug("Hay conversion..... de monedas Base:" + intMonedaBase + " CXP:" + intMonedaCXP + " factor " + dblFactorConversion + " " + dblImporteProCXP);
               dblImporteProCXP = dblImporteProCXP * dblFactorConversion;
            } else {
               log.debug("No hay conversion.....");
            }
            //Asignamos el valor
            cxpagar.setDblImporteProrratear(dblImporteProCXP);
            log.debug("CXP " + cxpagar.getFieldInt("CXP_ID") + " " + dblImporteProCXP);
            //sumamos al total
            dblTotalProrrateo += dblImporteProCXP;
         }
      }
      log.debug("Total por prorratear " + dblTotalProrrateo);
      //Calculamos el total en importe por item de entrada
      //Para obtener despues la proporcion
      //Convertimos los importes del costo en la moneda base para que usemos
      //peras con peras y manzanas con manzanas

      it = lstCXPagar.iterator();
      while (it.hasNext()) {
         vta_cxpagar cxpagar = it.next();
         if (cxpagar.getFieldInt("ODC_ID") != 0) {
            //Iteramos por las entradas
            Iterator<vta_movproddeta> it2 = cxpagar.getLstEntradas().iterator();
            while (it2.hasNext()) {
               vta_movproddeta deta = it2.next();
               //Calculamos que esten en la moneda del pedido
               log.debug("MonedaBase: " + intMonedaBase);
               double dblFactorConversion = 1;
               int intMonedaCXP = cxpagar.getFieldInt("CXP_MONEDA");
               log.debug("intMonedaCXP: " + intMonedaCXP);
               //Solo convertimos cuando las monedas sean diferentes
               if (intMonedaBase != intMonedaCXP) {
                  if (intMonedaBase == 1) {
                     if (intMonedaCXP == 2) {
                        dblFactorConversion = pedimento.getFieldDouble("PD_DOLAR");
                     } else {
                        dblFactorConversion = pedimento.getFieldDouble("PD_DOLAR") * pedimento.getFieldDouble("PED_PARIDAD");
                     }
                  } else {
                     if (intMonedaBase == 2) {
                        if (intMonedaCXP == 1) {
                           dblFactorConversion = 1 / pedimento.getFieldDouble("PD_DOLAR");
                        } else {
                           dblFactorConversion = pedimento.getFieldDouble("PED_PARIDAD");
                        }
                     } else {
                        if (intMonedaCXP == 1) {
                           dblFactorConversion = 1 / pedimento.getFieldDouble("PD_DOLAR") * 1 / pedimento.getFieldDouble("PED_PARIDAD");
                        } else {
                           if (intMonedaCXP == 2) {
                              dblFactorConversion = 1 / pedimento.getFieldDouble("PED_PARIDAD");
                           } else {
                              dblFactorConversion = 1;
                           }
                        }
                     }
                  }
               }
               //Hacemos la conversion
               double dblCostoConvertido = dblFactorConversion * deta.getFieldDouble("MPD_COSTO");
               //Calculamos el importe acumulado en base al costo convertido en la moneda del pedimento
               double dblImportePropor = (dblCostoConvertido * deta.getFieldDouble("MPD_ENTRADAS"));
               dblTotalImporteCostos += dblImportePropor;
               deta.setIntMoneda(cxpagar.getFieldInt("CXP_MONEDA"));
               deta.setFieldDouble("MPD_COSTO", dblCostoConvertido);
               log.debug("CXP ODC:" + cxpagar.getFieldInt("CXP_ID") + " Moneda:" + cxpagar.getFieldInt("CXP_MONEDA") + " " + dblImportePropor + " " + dblCostoConvertido + " " + deta.getFieldDouble("MPD_ENTRADAS"));

            }
         }
      }
      log.debug("Total por importe costo " + dblTotalImporteCostos);
      //Calculamos el costo que se prorrateara a la partida
      it = lstCXPagar.iterator();
      while (it.hasNext()) {
         vta_cxpagar cxpagar = it.next();
         if (cxpagar.getFieldInt("ODC_ID") != 0) {
            //Iteramos por las entradas
            Iterator<vta_movproddeta> it2 = cxpagar.getLstEntradas().iterator();
            while (it2.hasNext()) {
               vta_movproddeta deta = it2.next();
               double dblPropor = (deta.getFieldDouble("MPD_COSTO") * deta.getFieldDouble("MPD_ENTRADAS")) / dblTotalImporteCostos;
               deta.setDblProporProrrateo(dblPropor);
               double dblImportCostoProrra = dblPropor * dblTotalProrrateo;

               log.debug("Proporcion " + dblPropor + " Codigo:" + deta.getFieldString("PR_CODIGO") + " Adicional al costo por prorratear " + dblImportCostoProrra + " cu:" + (dblImportCostoProrra / deta.getFieldDouble("MPD_ENTRADAS")));
               //Costo prorrateado en moneda base 
               deta.setDblCostoProrrateo(dblImportCostoProrra / deta.getFieldDouble("MPD_ENTRADAS"));
            }
         }
      }

      return lstCXPagar;
   }

   /**
    * Cancela el prorrateo aplicado
    */
   public void doCancelaProrrateo() {
      this.strResultLast = "OK";
      vta_pedimentos pedimento = new vta_pedimentos();
      pedimento.ObtenDatos(intPED_ID, oConn);
      // <editor-fold defaultstate="collapsed" desc="Validaciones">
      if (pedimento.getFieldInt("PED_ID") == 0) {
         this.strResultLast = "Error:Pedimento no existe";
      }
      if (pedimento.getFieldInt("PED_APLICADO") == 0) {
         this.strResultLast = "Error:El pedimento aun no se aplica";
      }
      // </editor-fold>
      //Si fueron exitosas las validaciones continuamos
      if (this.strResultLast.equals("OK")) {
         ArrayList<vta_cxpagar> lstCXPxSave = doCalculaProrrateo();

         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>

         //Iteramos en cada recepcion de almacen para actualizar costos y movimientos
         Iterator<vta_cxpagar> it = lstCXPxSave.iterator();
         while (it.hasNext()) {
            vta_cxpagar cxp = it.next();
            int intMonedaBase = cxp.getFieldInt("CXP_MONEDA");
            Iterator<vta_movproddeta> itE = cxp.getLstEntradas().iterator();
            while (itE.hasNext()) {
               vta_movproddeta movDeta = itE.next();
               //Conversion
               double dblCostoConvertido = movDeta.getFieldDouble("MPD_COSTO_PRORRATEO");
               log.debug("intMonedaBase: " + intMonedaBase);
               if (intMonedaBase == 1) {
               } else {
                  if (intMonedaBase == 2) {
                     dblCostoConvertido = (1 / pedimento.getFieldDouble("PD_DOLAR")) * dblCostoConvertido;
                  } else {
                     dblCostoConvertido = (1 / pedimento.getFieldDouble("PD_DOLAR")) * (dblCostoConvertido * (1 / pedimento.getFieldDouble("PED_PARIDAD")));

                  }
               }
               movDeta.setFieldDouble("MPD_COSTO", dblCostoConvertido);
               movDeta.setFieldDouble("MPD_COSTO_PRORRATEO", 0.0);
               movDeta.setFieldDouble("MPD_PROPOR_PRORRATEO", 0.0);
               movDeta.Modifica(oConn);
               //Actualizamos todos los movimientos hechos
               String strRespD = doActualizaCostoMovs(movDeta);
               if (!strRespD.equals("OK")) {
                  this.strResultLast = strRespD;
               }
            }
         }
         //Marcamos el pedimento como que ya genero todo
         pedimento.setFieldInt("PED_APLICADO", 0);
         pedimento.setFieldInt("PED_USR_APLIC", 0);
         pedimento.setFieldString("PED_FECHA_APLIC", "");
         pedimento.setFieldString("PED_HORA_APLIC", "");
         pedimento.Modifica(oConn);

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
    * Genera un xml con el listado de las cuentas por pagar vinculadas al
    * pedimento
    *
    * @return Regresa una cadena con el xml
    */
   public String doGeneraListaXMLCXPagar() {
      String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
      strXML += "<pedimento>";
      UtilXml util = new UtilXml();
      vta_cxpagar cxpagarTmp = new vta_cxpagar();
      ArrayList<TableMaster> lstCXPagar = cxpagarTmp.ObtenDatosVarios(" PED_ID =  " + this.intPED_ID, oConn);
      Iterator<TableMaster> it = lstCXPagar.iterator();
      while (it.hasNext()) {
         TableMaster tbn = it.next();
         strXML += "<pedimentos ";
         strXML += tbn.getFieldPar() + " ";
         //Buscamos el nombre del proveedor
         strXML += " CXP_NOMPROV=\"" + util.Sustituye(cxpagarTmp.getNomProveedor(tbn.getFieldInt("PV_ID"), oConn)) + "\" ";
         strXML += " />";
      }
      strXML += "</pedimento>";
      return strXML;
   }

   /**
    * Actualiza el costo en todos los movimientos que tenga el producto en
    * cuestion
    *
    * @param movDeta Es la partida de la entrada
    * @return Regresa OK si todo fue exitoso
    */
   public String doActualizaCostoMovs(vta_movproddeta movDeta) {
      String strRes = "OK";

      //Actualizamos el id del pedimento en el lote que corresponda al producto
      //Obtenemos el lote del movimiento
      String strSql = "select PL_ID,PL_COSTO from vta_prodlote "
              + " where PR_ID =  " + movDeta.getFieldInt("PR_ID")
              + " AND PL_NUMLOTE='" + movDeta.getFieldString("PL_NUMLOTE") + "'";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intPL_ID = rs.getInt("PL_ID");
            //Actualizamos el lote, poniendo el id del pedimento
            String strUpdate = "UPDATE vta_prodlote "
                    + " set PED_ID = " + intPED_ID + " "
                    + " where PL_ID = '" + intPL_ID + "'";
            this.oConn.runQueryLMD(strUpdate);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(" " + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + ex.getSQLState());
      }

      //Actualizacion contable de la entrada
      doActualizaCostoContable(movDeta);
      //Recuperamos todos los movimientos de este codigo y lote
      vta_movproddeta movDetaTmp = new vta_movproddeta();
      ArrayList<TableMaster> lstMovs = movDetaTmp.ObtenDatosVarios(
              " PR_ID = " + movDeta.getFieldInt("PR_ID")
              + " AND PL_NUMLOTE='" + movDeta.getFieldString("PL_NUMLOTE") + "' "
              + " AND MPD_ID <> " + movDeta.getFieldInt("MPD_ID") + " ", oConn);
      Iterator<TableMaster> it = lstMovs.iterator();
      while (it.hasNext()) {
         TableMaster tbn = it.next();
         tbn.setFieldDouble("MPD_COSTO", movDeta.getFieldDouble("MPD_COSTO"));
         tbn.Modifica(oConn);
         //Validamos si es un movimiento de factura para actualizar el costo de venta ???
      }
      return strRes;
   }

   /**
    * Actualiza el costo en la poliza contable de todos los movimientos que
    * tenga el producto en cuestion
    *
    * @param movDeta Es la partida de la entrada
    * @return Regresa OK si todo fue exitoso
    */
   public String doActualizaCostoContable(vta_movproddeta movDeta) {
      String strRes = "OK";
      return strRes;
   }

   /**
    * Filtra las entradas para dejar solo 1
    *
    * @param Entrada Es el movimiento de entrada
    * @return Regresa una lista agrupada
    */
   public ArrayList<vta_cxpagar> Filtra(ArrayList<vta_cxpagar> Entrada) {
      vta_cxpagar aux;
      Iterator<vta_cxpagar> it = Entrada.iterator();
      int i = 0;
      while (it.hasNext()) {
         aux = it.next();
         //Entrada.remove(i);
         log.debug("Entre:" + i);
         aux.setLstEntradas(aux.CalculaEntradaEditada(aux.getLstEntradas()));
         log.debug("Pase esta fase:" + i);
         Entrada.set(i, aux);
         log.debug("Terminamos de agregar el elemento: " + i);
         i++;
      }
      return Entrada;
   }

   /**
    * Realiza el cambio de moneda despues de calcular el prorrateo
    *
    * @param Entrada Es el movimiento de entrada
    * @param intTipoCambio Es el tipo de cambio
    * @param oConn Es la conexion
    * @return Regresa la lista convertida
    */
   public ArrayList<vta_cxpagar> CambioMoneda(ArrayList<vta_cxpagar> Entrada, int intTipoCambio, Conexion oConn) {
      vta_cxpagar aux;
      vta_movproddeta aux2;
      Double dblParidadMonedaExt = 1.0;
      int intMonedaBase = 1;

      //Falta passarlo si esta en otra moneda... Pasarlos hasta pesos...
      String strSQL = "Select PD_DOLAR,PED_PARIDAD,MON_ID From vta_pedimentos Where PED_ID =" + this.intPED_ID;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSQL, true);
         while (rs.next()) {
            this.dblTipoCambio = rs.getDouble("PD_DOLAR");
            dblParidadMonedaExt = rs.getDouble("PED_PARIDAD");
            intMonedaBase = rs.getInt("MON_ID");
         }
      } catch (SQLException ex) {
         log.error(ex.getSQLState() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "PRORRATEO", oConn);
      }
      log.debug("Conversion de costos...");
      //Recorremos cada partida para hacer la conversion
      Iterator<vta_cxpagar> itcxp = Entrada.iterator();
      while (itcxp.hasNext()) {
         aux = itcxp.next();
         Iterator<vta_movproddeta> eEA = aux.getLstEntradas().iterator();
         while (eEA.hasNext()) {
            aux2 = eEA.next();

            double dblCostoConvertido = aux2.getFieldDouble("MPD_COSTO");
            double dblCostoProrrateo = aux2.getDblCostoProrrateo();
            //Dependiendo de la moneda hacemos la conversion
            if (intMonedaBase == 1) {
            } else {
               if (intMonedaBase == 2) {
                  dblCostoConvertido = this.dblTipoCambio * dblCostoConvertido;
                  dblCostoProrrateo = this.dblTipoCambio * dblCostoProrrateo;
               } else {
                  dblCostoConvertido = this.dblTipoCambio * (dblCostoConvertido * dblParidadMonedaExt);
                  dblCostoProrrateo = this.dblTipoCambio * (dblCostoProrrateo * dblParidadMonedaExt);
               }
            }

            log.debug("intMonedaBase:" + intMonedaBase);
            log.debug("dblTipoCambio:" + this.dblTipoCambio);
            log.debug("dblParidadMonedaExt:" + dblParidadMonedaExt);
            log.debug("Costo:" + this.dblTipoCambio + " * " + aux2.getFieldDouble("MPD_COSTO") + "=" + dblCostoConvertido);
            log.debug("Factor:" + aux2.getDblProporProrrateo());
            log.debug("Prorrateo:" + this.dblTipoCambio + " * " + aux2.getDblCostoProrrateo() + "=" + dblCostoProrrateo);
            aux2.setFieldDouble("MPD_COSTO", dblCostoConvertido);
            aux2.setDblCostoProrrateo(dblCostoProrrateo);
         }
      }

      return Entrada;
   }
   // </editor-fold>
}
