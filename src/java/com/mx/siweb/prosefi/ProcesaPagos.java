/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.prosefi;

import ERP.CobranzaLayouts;
import ERP.movCliente;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author SIWEB
 */
public class ProcesaPagos extends CobranzaLayouts {

   private static final Logger log = LogManager.getLogger(ProcesaPagos.class.getName());

   public ProcesaPagos(int intCPM_ID, Conexion oConn, VariableSession varSesiones) {
      super(intCPM_ID, oConn, varSesiones);
   }

   @Override
   public String ProcesaLayout(String strFilePath) {
      String strResp = "OK";
      File file = new File(strFilePath);
      //Validamos que exista el archivo
      if (file.exists()) {
         //Lo recorremos
         try {
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(file);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
               // Print the content on the console
               System.out.println(strLine);
               strResp = ParserLine(strLine);
//               if(!strResp.equals("OK")){
//                  break;
//               }
            }
            //Close the input stream
            in.close();
         } catch (Exception ex) {//Catch exception if any
            log.error(" Exception file:" + ex.getMessage() + " " + ex.getLocalizedMessage());
         }

      }
      return strResp;
   }

   @Override
   public String ParserLine(String strLine) {
      //Valor que usaremos para el archivo
      int intCliente = 0;
      int intCredito = 0;
      int intVencimiento = 0;
      String strReferencia1 = "";
      String strFecha = "";
      String strNumAutoriza = "";
      double dblImporte = 0;
      double dblSaldo = 0;
      int intConsecutivo = 0, intTipoMov = 0, intIndefinido = 0, intSucursal = 0;
      String strCargoAbono = "";
      double dblIndefinida2 = 0;
      //Procesamos la fila
      String strResp = "OK";
      //System.out.println();
      String[] lstColsTxt = strLine.split(this.strSeparador);
      //if que sean 10
      if (lstColsTxt.length < 10) {
         System.out.println("Error de formato");
      } else {
         for (int i = 0; i < lstColsTxt.length; i++) {
            String strValor = lstColsTxt[i];

            //System.out.println(lstColsTxt.length + "Valor");
            try {
               //for(int t=0;t<this.lstCols.size();t++){
               TableMaster tbn = this.lstCols.get(i);
               String strNombreCampo = tbn.getFieldString("CPMD_NOMBRE_CAMPO");
               //Validamos cada tipo de campo
               if (strNombreCampo.equals("$Consecutivo")) {
                  try {
                     intConsecutivo = Integer.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("consecutivo: " + intConsecutivo);
               }
               if (strNombreCampo.equals("$Fecha")) {
                  strFecha = strValor;
                  SimpleDateFormat formatoDeFecha = new SimpleDateFormat(tbn.getFieldString("CPMD_EXP_REG"));
                  Date date1 = null;
                  try {
                     date1 = formatoDeFecha.parse(strFecha);
                     Fechas fecha = new Fechas();
                     strFecha = fecha.getFechaDate(date1);

                  } catch (ParseException ex) {
                     log.error("ParseException:" + tbn.getFieldString("CPMD_EXP_REG") + " " + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("fecha: " + strFecha);
               }
               if (strNombreCampo.equals("$CargoAbono")) {
                  try {
                     strCargoAbono = String.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("cargoabono: " + strCargoAbono);
               }
               if (strNombreCampo.equals("$TipoMov")) {
                  try {
                     intTipoMov = Integer.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("tipo movimiento: " + intTipoMov);
               }
               if (strNombreCampo.equals("$Indefinido1")) {
                  try {
                     intIndefinido = Integer.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("indefinido: " + intIndefinido);
               }
               if (strNombreCampo.equals("$Sucursal")) {
                  try {
                     intSucursal = Integer.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("sucursal: " + intSucursal);
               }
               //Si es referencia buscamos el id del cliente
               if (strNombreCampo.equals("$Referencia1")) {
                  strReferencia1 = strValor;
                  String strSql = "select CT_ID from "
                          + " vta_cliente where "
                          + "    CT_RBANCARIA1 = '" + strReferencia1 + "' "
                          + " or CT_RBANCARIA1 = '" + strReferencia1 + "' "
                          + " or CT_RBANCARIA1 = '" + strReferencia1 + "' ";
                  ResultSet rs;
                  try {
                     rs = this.oConn.runQuery(strSql);
                     while (rs.next()) {
                        intCliente = rs.getInt("CT_ID");
                     }
                     //if(rs.getStatement() != null )rs.getStatement().close(); 
                     rs.close();
                  } catch (SQLException ex) {
                     log.error("SQLException:" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("referencia: " + strReferencia1);
               }
               if (strNombreCampo.equals("$Importe")) {
                  try {
                     dblImporte = Double.valueOf(strValor.replace(",", ""));
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("importe: " + dblImporte);
               }
               if (strNombreCampo.equals("$Indefinida2")) {
                  try {
                     dblIndefinida2 = Double.valueOf(strValor.replace(",", ""));
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("indefinida: " + dblIndefinida2);
               }
               if (strNombreCampo.equals("$NumAutoriza")) {
                  try {
                     strNumAutoriza = String.valueOf(strValor);
                  } catch (NumberFormatException ex) {
                     strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                     log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                  }
                  //System.out.println("autoriza: " + intNumAutoriza);
               }
               /*
                if (strNombreCampo.equals("$Hora")) {
                strHora = strValor;
                }
                */
               //}
            } catch (IndexOutOfBoundsException ex) {
               strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
               log.error("IndexOutOfBoundsException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
            }
         }

      }
      log.debug(intCliente + " " + strReferencia1 + " " + strFecha + " " + dblImporte);
      //Evaluamos si contamos con los campos minimos para registrar el pago
      /*  if (intCliente == 0) {
       strResp = "ERROR:Requiere de colocar el numero de cliente o referencia" + strResp;
       return strResp;

       }*/
      if (strFecha.isEmpty()) {
         strResp = "ERROR:Requiere de colocar una fecha " + strResp;
         return strResp;
      }
      if (dblImporte == 0) {
         strResp = "ERROR:Requiere de colocar un monto " + strResp;
         return strResp;
      }
      if (!strCargoAbono.equals("A")) {
         strResp = "ERROR:El registro no es un abono" + strResp;
         return strResp;
      }
      if (intTipoMov != 7) {
         strResp = "ERROR:El registro no es un abono" + strResp;
         return strResp;
      }
      System.out.println("Ya va aplicar el pago");
      Fechas fecha = new Fechas();
      //hacemos el insert de cada uno de los registros
      String strInsert = "INSERT INTO cat_pagos_procesados(PP_CONSECUTIVO,PP_FECHA,PP_CARGO_ABONO,PP_TIPO_MOV,PP_INDEFINIDO,PP_SUCURSAL,PP_REFERENCIA,PP_IMPORTE,"
              + "PP_INDEFINIDA2,PP_NUM_AUTORIZA,PP_FECHA_APLICACION,PP_HORA_APLICACION,PP_APLICADO)values "
              + "('" + intConsecutivo + "','" + strFecha + "','" + strCargoAbono + "','" + intTipoMov + "','" + intIndefinido + "','" + intSucursal + "','" + strReferencia1 + "',"
              + "'" + dblImporte + "','" + dblIndefinida2 + "','" + strNumAutoriza + "','" + fecha.getFechaActual() + "','" + fecha.getHoraActual() + "','0')";
      oConn.runQueryLMD(strInsert);
      //Recuperamos el id
      int intIdPago = 0;
      try {
         ResultSet rs = oConn.runQuery("select @@identity", true);
         while (rs.next()) {
            intIdPago = rs.getInt(1);
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error("SQLException:" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getLocalizedMessage());
      }
      if (intCliente != 0) {

         String strUpdate = "UPDATE cat_pagos_procesados SET PP_APLICADO = 'SE APLICO'  where PP_ID = " + intIdPago + "";
         oConn.runQueryLMD(strUpdate);
      } else {
         String strUpdate2 = "UPDATE cat_pagos_procesados SET PP_APLICADO = 'FALLO POR LA REFERENCIA BANCARIA' where PP_ID = " + intIdPago + "";
         oConn.runQueryLMD(strUpdate2);
      }

      //Paso todas las validaciones entonces aplicamos el pago
//      Instanciamos el objeto que nos trae las listas de precios
      movCliente movCte = new movCliente(oConn, this.varSesiones, null);
      //Inicializamos objeto
      movCte.Init();
      //Buscamos el banco donde aplicara
      if (intBc_Id != 0) {
         movCte.setIntBc_Id(intBc_Id);
      } else {
         movCte.setBolCaja(true);
      }
      //Recibimos datos para el encabezado
      movCte.getCta_clie().setFieldString("MC_FECHA", strFecha);
      movCte.getCta_clie().setFieldString("MC_NOTAS", "Archivo de cobranza " + strReferencia1);
//      movCte.getCta_clie().setFieldInt("MC_MONEDA", Integer.valueOf(request.getParameter("MONEDA")));
      movCte.getCta_clie().setFieldDouble("MC_TASAPESO", 1);
      movCte.getCta_clie().setFieldInt("CT_ID", intCliente);
      movCte.getCta_clie().setFieldInt("FAC_ID", 0);
      movCte.getCta_clie().setFieldInt("TKT_ID", 0);
      movCte.getCta_clie().setFieldInt("MC_ESPAGO", 1);
      movCte.getCta_clie().setFieldInt("MC_ANTICIPO", 1);
      movCte.getCta_clie().setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
      movCte.getCta_clie().setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
      movCte.getCta_clie().setFieldDouble("MC_ABONO", dblImporte);
      if (this.bolLinkPedidos) {
         movCte.setBolLinkPedidos(true);
      }

      //Generamos transaccion
      movCte.doTrx();
      if (movCte.getStrResultLast().equals("OK")) {
         strResp = "OK." + movCte.getCta_clie().getValorKey();
      } else {
         strResp = movCte.getStrResultLast();
      }

      //Vamos a recuperar el id del cliente
      for (int i = 0; i < lstColsTxt.length; i++) {
         String strValor = lstColsTxt[i];

         TableMaster tbn = this.lstCols.get(i);
         String strNombreCampo = tbn.getFieldString("CPMD_NOMBRE_CAMPO");

         if (strNombreCampo.equals("$Referencia1")) {
            strReferencia1 = strValor;
            try {
               String strSql2 = "select * from cat_vencimiento "
                       + "where CT_ID = " + intCliente;
               ResultSet rs1;
               rs1 = this.oConn.runQuery(strSql2);
               double dblAplica = 0;
               double dblPago = 0;



               while (rs1.next()) {

                  intVencimiento = rs1.getInt("V_ID");
                  intCredito = rs1.getInt("CTO_ID");
                  intCliente = rs1.getInt("CT_ID");
                  dblSaldo = rs1.getInt("V_SALDO");

                  if (dblImporte >= dblSaldo) {

                     if (dblPago != 0 && dblImporte < dblSaldo) {
                        dblPago = dblSaldo - dblImporte;
                        String strSql15 = "update cat_vencimiento "
                                + "set V_SALDO = " + dblPago + " "
                                + "where CT_ID = " + intCliente + " and V_ID=" + intVencimiento;
                        oConn.runQueryLMD(strSql15);
                        log.debug(dblPago);
                        dblPago = 0;
                        String strSql16 = "insert into movcte_vencimiento (V_ID)"
                                + "values(" + intVencimiento + ")";
                        oConn.runQueryLMD(strSql16);
                     } else {
                        if (dblImporte >= dblSaldo) {
                           dblPago = dblImporte - dblSaldo;
                           dblImporte = dblPago;
                           String strSql14 = "update cat_vencimiento "
                                   + "set V_SALDO = " + 0 + " "
                                   + "where CT_ID = " + intCliente + " and V_ID=" + intVencimiento;
                           oConn.runQueryLMD(strSql14);
                           String strSql17 = "insert into movcte_vencimiento (V_ID)"
                                   + "values(" + intVencimiento + ")";
                           oConn.runQueryLMD(strSql17);
                        }
                     }
                  } else {
                     log.debug(dblImporte);
                     if (dblImporte < dblSaldo) {
                        log.debug("el pago es menor al saldo");
                        dblPago = dblSaldo - dblImporte;
                        String strSql20 = "update cat_vencimiento "
                                + "set V_SALDO = " + dblPago + " "
                                + "where CT_ID = " + intCliente + " AND V_SALDO != 0 LIMIT 1";
                        oConn.runQueryLMD(strSql20);
                        log.debug(dblPago);
                     }
                  }
               }

//               if(rs1.getStatement() != null )rs1.getStatement().close(); 
               rs1.close();
               if (dblImporte == 0) {
                  break;
               }

            } catch (SQLException ex) {
               log.error("SQLException:" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getLocalizedMessage());
            }
         }


      }

      return strResp;
   }
}