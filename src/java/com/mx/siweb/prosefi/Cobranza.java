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
public class Cobranza extends CobranzaLayouts {

    private static final Logger log = LogManager.getLogger(Cobranza.class.getName());

    public Cobranza(int intCPM_ID, Conexion oConn, VariableSession varSesiones) {
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
        String strCadena = "";
        String strSucursal = "";
        String strTmovimiento = "";
        double dblImporte = 0;
        double dblSaldo = 0;
        int intConsecutivo = 0, intTipoMov = 0, intIndefinido = 0, intSucursal = 0;
        String strNumAutoriza = "", strImporte;
        String strCargoAbono = "";
        double dblIndefinida2 = 0;
        //Procesamos la fila
        String strResp = "OK";
        //System.out.println();
        String[] lstColsTxt = strLine.split(this.strSeparador);
        //if que sean 10
        if (lstColsTxt.length < 4) {
            System.out.println("Error de formato");
        } else {
            for (int i = 0; i < lstColsTxt.length; i++) {
                String strValor = lstColsTxt[i];

                //System.out.println(lstColsTxt.length + "Valor");
                try {
                    //for(int t=0;t<this.lstCols.size();t++){
                    TableMaster tbn = this.lstCols.get(i);
                    String strNombreCampo = tbn.getFieldString("CPMD_NOMBRE_CAMPO");
                    System.out.println(strNombreCampo);
                    //Validamos cada tipo de campo
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
                        System.out.println("FECHA " + strFecha);
                    }
                    if (strNombreCampo.equals("$Cadena")) {
                        try {
                            strCadena = strValor;
                        } catch (NumberFormatException ex) {
                            strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                            log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                        }

                        if (!strCadena.equals("")) {
                            System.out.println("CADENA " + strCadena);
                            //String substring = strCadena;
                            strReferencia1 = strCadena.substring(6, 16);
                            //Si es referencia buscamos el id del cliente

                            //strReferencia1 = strValor;
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

                            strTmovimiento = strCadena.substring(17, 28);
                            strSucursal = strCadena.substring(46, 57);
                            strNumAutoriza = strCadena.substring(62, 70);
                            System.out.println("REFERENCIA " + strReferencia1);
                            System.out.println("MOVIMIENTO " + strTmovimiento);
                            System.out.println("SUCURSAL " + strSucursal);
                            System.out.println("autorizado " + strNumAutoriza);
                        }
                    }
                    if (strNombreCampo.equals("$Indefinido1")) {
                        try {
                            intIndefinido = Integer.valueOf(strValor);
                        } catch (NumberFormatException ex) {
                            strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                            log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                        }
                        System.out.println("INDEFINIDO " + intIndefinido);
                    }
                    if (strNombreCampo.equals("$Importe")) {
                        try {
                            dblImporte = Double.valueOf(strValor.replace(",", ""));
                        } catch (NumberFormatException ex) {
                            strResp = "ERROR:" + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + i;
                            log.error("NumberFormatException:" + ex.getMessage() + " " + ex.getLocalizedMessage());
                        }
                        System.out.println("IMPORTE " + dblImporte);
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
    /*    if (intCliente == 0) {
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
        if (!strTmovimiento.equals("DEPOSITO DE")) {
            strResp = "ERROR:El registro no es un deposito" + strResp;
            return strResp;
        }
        /* if (intTipoMov != 7) {
        strResp = "ERROR:El registro no es un abono" + strResp;
        return strResp;
        }
         */
        System.out.println("Ya va aplicar el pago");
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
//      //Recibimos datos para el encabezado
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
                        System.out.println("Cliente  " + intCliente);
                    }
                    //if(rs.getStatement() != null )rs.getStatement().close(); 
                    rs.close();
                    System.out.println("Es el id del cliente  " + intCliente);
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
                                System.out.println(dblPago);
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
                            System.out.println(dblImporte);
                            if (dblImporte < dblSaldo) {
                                System.out.println("el pago es menor al saldo");
                                dblPago = dblSaldo - dblImporte;
                                String strSql20 = "update cat_vencimiento "
                                        + "set V_SALDO = " + dblPago + " "
                                        + "where CT_ID = " + intCliente + " AND V_SALDO != 0 LIMIT 1";
                                oConn.runQueryLMD(strSql20);
                                System.out.println(dblPago);
                            }
                        }
                    }

//                    if(rs1.getStatement() != null )rs1.getStatement().close(); 
                    rs1.close();
                    /*if (dblImporte == 0) {
                        break;
                    }*/
                } catch (SQLException ex) {
                    log.error("SQLException:" + ex.getMessage() + " " + ex.getSQLState() + " " + ex.getLocalizedMessage());
                }
            


        

        return strResp;
    }
}
