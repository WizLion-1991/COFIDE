package ERP;

import Tablas.vta_producto_inventario_fecha;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Realiza operaciones de recalculo de costos
 *
 * @author ZeusGalindo
 */
public class InventarioCosteo {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private static final Logger log = LogManager.getLogger(InventarioCosteo.class.getName());
   boolean bolSincronizaPromedioActual;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public boolean isBolSincronizaPromedioActual() {
      return bolSincronizaPromedioActual;
   }

   public void setBolSincronizaPromedioActual(boolean bolSincronizaPromedioActual) {
      this.bolSincronizaPromedioActual = bolSincronizaPromedioActual;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public InventarioCosteo(Conexion oConn) {
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public String doRecalculoCalculoPromedio(int intSC_ID, String strFecha) {
      String strResult = "OK";
      //Paso 0 Borramos los registros si hubo un calculo anterior
      String strDel = "delete from vta_producto_inventario_fecha where PR_FECHA = '" + strFecha + "' AND SC_ID = " + intSC_ID;
      this.oConn.runQueryLMD(strDel);
      //Paso 1 Obtenemos items por analizar
      ArrayList<Integer> listProd = ObtenItemsporAnalizar(intSC_ID, strFecha);

      //Paso 2 Analizamos producto por producto
      Iterator<Integer> it = listProd.iterator();
      int intConteos = 0;
      while (it.hasNext()) {
         int intProd = it.next();
         intConteos++;
         AnalizaCostos(intProd, intSC_ID, strFecha);
      }
      log.debug("Total de analizados..." + intConteos);
      return strResult;
   }

   private ArrayList<Integer> ObtenItemsporAnalizar(int intSC_ID, String strFecha) {
      ArrayList<Integer> listProd = new ArrayList<Integer>();
      //Seleccionamos los productos que se recalcularan
      String strSql = "select vta_movproddeta.pr_id,\n"
              + "sum(MPD_ENTRADAS - MPD_SALIDAS) as texist \n"
              + "from vta_producto,vta_movprod,vta_movproddeta where \n"
              + "vta_producto.PR_ID = vta_movproddeta.PR_ID and\n"
              + "vta_movprod.MP_ID = vta_movproddeta.MP_ID and \n"
              + "vta_movprod.MP_FECHA <= '" + strFecha + "' AND vta_movprod.MP_ANULADO = 0\n"
              + " and vta_producto.SC_ID = " + intSC_ID + " \n"
              //              + " and vta_producto.PR_ID = 2398 "
              + "group by vta_movproddeta.pr_id\n"
              + "having texist > 0\n"
              + "ORDER BY vta_movproddeta.pr_id";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            listProd.add(rs.getInt("PR_ID"));
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return listProd;
   }

   private void AnalizaCostos(int intIdProd, int intSC_ID, String strFecha) {
      Monedas moneda = new Monedas(this.oConn);
      moneda.setBoolConversionAutomatica(true);
      double dblExistencia = 0;
      double dblCostoProm = 0;
      log.debug("************************");
      log.debug("Analizando producto " + intIdProd);
      String strSql = "select vta_movprod.OC_ID,PR_MONEDA_COSTO,vta_movprod.PD_ID,MPD_PROPOR_PRORRATEO,MPD_COSTO,"
              + "(SELECT vta_cxpagar.PED_ID  from vta_cxpagar where vta_cxpagar.CXP_ID = vta_movprod.CXP_ID) AS PEDIMENTO,"
              + "vta_movproddeta.* "
              + "from vta_producto,vta_movprod,vta_movproddeta where  "
              + "vta_producto.PR_ID = vta_movproddeta.PR_ID and "
              + "vta_movprod.MP_ID = vta_movproddeta.MP_ID and  "
              + "vta_movprod.MP_FECHA <= '" + strFecha + "' AND vta_movprod.MP_ANULADO = 0 "
              + " and vta_producto.PR_ID = " + intIdProd + " "
              + "ORDER BY vta_movproddeta.MPD_FECHA,vta_movproddeta.MPD_ID";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         int intContaMovs = 0;
         while (rs.next()) {
            intContaMovs++;
            String strFechaMov = rs.getString("MPD_FECHA");
            if (intContaMovs == 1) {
               log.debug(intIdProd + " Ini" + strFechaMov);
            }
            //Evaluamos si es entrada o salida
            if (rs.getDouble("MPD_ENTRADAS") > 0) {
               int intComId = rs.getInt("OC_ID");
               int intMpdIdOrigen = rs.getInt("MPD_IDORIGEN");
               int intPedimId = rs.getInt("PEDIMENTO");
               int intMonedaCosto = rs.getInt("PR_MONEDA_COSTO");
               double dblProporProrra = rs.getDouble("MPD_PROPOR_PRORRATEO");
               if (intComId != 0) {
                  log.debug("ENTRADA POR COMPRA..." + intComId);
                  // <editor-fold defaultstate="collapsed" desc="Analizamos el costo de la COMPRA">
                  String strSql2 = "select COMD_COSTO,MON_ID,PED_ID from vta_compra,vta_compradeta where  "
                          + "vta_compra.COM_ID = vta_compradeta.COM_ID AND "
                          + "COMD_ID = " + intMpdIdOrigen + " and vta_compradeta.COM_ID=" + intComId;
                  ResultSet rs2 = oConn.runQuery(strSql2, true);
                  while (rs2.next()) {
                     double dblCostoCompra = rs2.getDouble("COMD_COSTO");
                     int intMonedaCompra = rs2.getInt("MON_ID");
                     //Evaluamos como warning si la moneda de la orden de compra es diferente a la moneda del costo
                     if (intMonedaCosto != intMonedaCompra) {
                        log.warn(".....HAY UNA DISCREPANCIA EN EL COSTO DE LA ORDEN DE COMPRA, SE TOMARA EL DEL PRODUCTO,");
                        intMonedaCompra = intMonedaCosto;
                     }
                     // <editor-fold defaultstate="collapsed" desc="Si La moneda es diferente de pesos hacemos la conversión">
                     if (intMonedaCompra != 1) {
                        log.debug("Conversion de " + intMonedaCompra + " a pesos COSTO original " + dblCostoCompra);
                        //Evaluamos si viene de un pedido para obtener la tasa de conversion y el prorrateo si existe
                        boolean bolParidad = true;
                        if (intPedimId != 0) {
                           log.debug("+++++++++Pedimento " + intPedimId);
                           //Como viene de un pedimento evaluamos si esta prorrateado para tomar el costo sin conversiones
                           if (dblProporProrra != 0) {
                              bolParidad = false;
                              dblCostoCompra = rs.getDouble("MPD_COSTO");
                           }
                        }
                        //Solo si aplica buscar paridad
                        if (bolParidad) {
                           // <editor-fold defaultstate="collapsed" desc="Buscamos paridad">
                           double dblFactorConv = 1;
                           //Si son euros lo buscamos por programacion
                           if (intMonedaCompra == 3) {
                              // <editor-fold defaultstate="collapsed" desc="EUROS">
                              double dblFactorTmp = 1;
                              String strSqlPari = "select getParidadAuto(2, 1, 4 , '" + strFechaMov + "')";
                              ResultSet rs3 = oConn.runQuery(strSqlPari, true);
                              while (rs3.next()) {
                                 if (rs3.getInt(1) != 0) {
                                    dblFactorTmp = rs3.getDouble(1);
                                 }
                              }
                              rs3.close();
                              //
                              log.debug("Dolares a pesos " + dblFactorTmp);

                              double dblFactorTmp2 = 0;
                              strSqlPari = "select getParidadAuto(" + 3 + ", 2, 4 , '" + strFechaMov + "')";
                              rs3 = oConn.runQuery(strSqlPari, true);
                              while (rs3.next()) {
                                 if (rs3.getInt(1) != 0) {
                                    dblFactorTmp2 = rs3.getDouble(1);
                                 }
                              }
                              rs3.close();
                              // </editor-fold>
                              log.debug("Euros a dolares " + dblFactorTmp2);
                              dblFactorConv = dblFactorTmp * dblFactorTmp2;
                           } else {
                              // <editor-fold defaultstate="collapsed" desc="Dolares">
                              String strSqlPari = "select getParidadAuto(" + intMonedaCompra + ", 1, 4 , '" + strFechaMov + "')";
                              ResultSet rs3 = oConn.runQuery(strSqlPari, true);
                              while (rs3.next()) {
                                 if (rs3.getInt(1) != 0) {
                                    dblFactorConv = rs3.getDouble(1);
                                 }
                              }
                              rs3.close();
                              // </editor-fold>
                           }
                           // </editor-fold>

                           log.debug("Paridad... " + dblFactorConv);
                           dblCostoCompra = dblCostoCompra * dblFactorConv;
                        }
                     }
                     // </editor-fold>

                     // <editor-fold defaultstate="collapsed" desc="Promediamos">
                     if (dblCostoProm == 0) {
                        dblCostoProm = dblCostoCompra;
                     } else {
                        //Promediamos
                        if (dblExistencia <= 0) {
                           dblCostoProm = dblCostoCompra;
                        } else {
                           log.debug("Nuevo promedio !!!!!! Exist Act:" + dblExistencia + " Costo:" + dblCostoProm + " Entrada:" + rs.getDouble("MPD_ENTRADAS") + " Costo entrada:" + dblCostoCompra);
                           double dblInvAct = dblExistencia * dblCostoProm;
                           double dblInvEnt = rs.getDouble("MPD_ENTRADAS") * dblCostoCompra;
                           double dblNvasExist = dblExistencia + rs.getDouble("MPD_ENTRADAS");
                           dblCostoProm = (dblInvAct + dblInvEnt) / dblNvasExist;
                        }
                     }
                     // </editor-fold>
                     log.debug("Costo Mov...Exist Act:" + dblExistencia + " Moneda:" + intMonedaCosto + " Costo MN:" + dblCostoCompra + " Costo Orig:" + rs2.getDouble("COMD_COSTO") + " Existencia:" + dblExistencia + " Promedio:" + dblCostoProm);
                  }
                  rs2.close();
                  // </editor-fold>

               } else {
                  // <editor-fold defaultstate="collapsed" desc="ENTRADA MANUAL">
                  log.debug("ENTRADA MANUAL...");

                  double dblCostoCompra = rs.getDouble("MPD_COSTO");
                  //Si el costo es cero usamos el mismo promedio
                  if (dblCostoCompra == 0) {
                     dblCostoCompra = dblCostoProm;
                     log.debug("Costo en ceros..." + dblCostoProm);
                  } else {
                     if (intMonedaCosto != 1) {
                        log.debug("Conversion de " + intMonedaCosto + " a pesos costo original " + dblCostoCompra);
                        // <editor-fold defaultstate="collapsed" desc="Buscamos paridad">
                        double dblFactorConv = 1;
                        //Si son euros lo buscamos por programacion
                        if (intMonedaCosto == 3) {
                           // <editor-fold defaultstate="collapsed" desc="EUROS">
                           double dblFactorTmp = 1;
                           String strSqlPari = "select getParidadAuto(2, 1, 4 , '" + strFechaMov + "')";
                           ResultSet rs3 = oConn.runQuery(strSqlPari, true);
                           while (rs3.next()) {
                              if (rs3.getInt(1) != 0) {
                                 dblFactorTmp = rs3.getDouble(1);
                              }
                           }
                           rs3.close();
                           //
                           log.debug("Dolares a pesos " + dblFactorTmp);

                           double dblFactorTmp2 = 0;
                           strSqlPari = "select getParidadAuto(" + 3 + ", 2, 4 , '" + strFechaMov + "')";
                           rs3 = oConn.runQuery(strSqlPari, true);
                           while (rs3.next()) {
                              if (rs3.getInt(1) != 0) {
                                 dblFactorTmp2 = rs3.getDouble(1);
                              }
                           }
                           rs3.close();
                           // </editor-fold>
                           log.debug("Euros a dolares " + dblFactorTmp2);
                           dblFactorConv = dblFactorTmp * dblFactorTmp2;
                        } else {
                           // <editor-fold defaultstate="collapsed" desc="Dolares">
                           String strSqlPari = "select getParidadAuto(" + intMonedaCosto + ", 1, 4 , '" + strFechaMov + "')";
                           ResultSet rs3 = oConn.runQuery(strSqlPari, true);
                           while (rs3.next()) {
                              if (rs3.getInt(1) != 0) {
                                 dblFactorConv = rs3.getDouble(1);
                              }
                           }
                           rs3.close();
                           // </editor-fold>
                        }
                        // </editor-fold>
                        dblCostoCompra = dblCostoCompra * dblFactorConv;
                     }
                  }
                  //Promediamos
                  if (dblCostoProm == 0) {
                     dblCostoProm = dblCostoCompra;
                  } else {
                     /*
                      log.debug("Nuevo promedio !!!!!! Exist Act:" + dblExistencia + " Costo:" + dblCostoProm + " Entrada:" + rs.getDouble("MPD_ENTRADAS") + " Costo entrada:" + dblCostoCompra);
                      if (dblExistencia <= 0) {
                      dblCostoProm = dblCostoCompra;
                      } else {
                      double dblInvAct = dblExistencia * dblCostoProm;
                      double dblInvEnt = rs.getDouble("MPD_ENTRADAS") * dblCostoCompra;
                      double dblNvasExist = dblExistencia + rs.getDouble("MPD_ENTRADAS");
                      dblCostoProm = (dblInvAct + dblInvEnt) / dblNvasExist;
                      }
                      * */
                  }

                  log.debug("Costo Mov...Exist Act:" + dblExistencia + " Moneda:" + intMonedaCosto + " Costo MN:" + dblCostoProm/*dblCostoCompra*/ + " Costo Orig:" + rs.getDouble("MPD_COSTO") + " Promedio:" + dblCostoProm);
                  // </editor-fold>
               }
               dblExistencia += rs.getDouble("MPD_ENTRADAS");

            } else {
               //Calculamos la nueva existencia
               dblExistencia -= rs.getDouble("MPD_SALIDAS");
            }
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      log.debug("************************");
      log.debug("COSTO PROMEDIO A LA FECHA " + dblCostoProm + " Existencia " + dblExistencia + " Valuación: " + (dblCostoProm * dblExistencia));
      log.debug("************************");

      //Registramos la informacion en la bd
      vta_producto_inventario_fecha data = new vta_producto_inventario_fecha();
      data.setFieldInt("PR_ID", intIdProd);
      data.setFieldInt("SC_ID", intSC_ID);
      data.setFieldDouble("PR_COSTO_PROM", dblCostoProm);
      data.setFieldDouble("PR_EXISTENCIA", dblExistencia);
      data.setFieldString("PR_FECHA", strFecha);
      data.Agrega(oConn);

      //Actualizamos el costo promedio si esta seleccionada la opcion
      if (this.bolSincronizaPromedioActual) {
         String strUpdate1 = "update vta_producto set PR_COSTOPROM = " + dblCostoProm + "  WHERE PR_ID = " + intIdProd;
         this.oConn.runQueryLMD(strUpdate1);
      }
   }
   // </editor-fold>
}
