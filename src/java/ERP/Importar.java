/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.vta_cxpagardetalle;
import Tablas.vta_mov_cte_deta;
import Tablas.vta_movproddeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author aleph_79
 */
public class Importar {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   private int intEMP_ID;
   private int intSC_ID;
   private VariableSession varSesiones;

   public int getIntEMP_ID() {
      return intEMP_ID;
   }

   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   public int getIntSC_ID() {
      return intSC_ID;
   }

   public void setIntSC_ID(int intSC_ID) {
      this.intSC_ID = intSC_ID;
   }

   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void SaldosInicialesProv(Conexion oConn, String strPathXLS, VariableSession varSesiones) {
      this.varSesiones = varSesiones;
      InputStream myxls = null;
      try {
         myxls = new FileInputStream(strPathXLS);
         HSSFWorkbook wb = new HSSFWorkbook(myxls);
         System.out.println("Listo...");
         this.SaldoInicialProv(oConn, wb.getSheetAt(0));

      } catch (IOException ex) {
         System.out.println(" Error " + ex.getMessage());
         Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);

      } finally {
         try {
            myxls.close();
         } catch (IOException ex) {
            Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public void SaldosInicialesCte(Conexion oConn, String strPathXLS, VariableSession varSesiones) {
      this.varSesiones = varSesiones;
      InputStream myxls = null;
      try {
         myxls = new FileInputStream(strPathXLS);
         HSSFWorkbook wb = new HSSFWorkbook(myxls);
         System.out.println("Listo...");
         this.SaldoInicialCte(oConn, wb.getSheetAt(0));
      } catch (IOException ex) {
         System.out.println(" Error " + ex.getMessage());
         Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);

      } finally {
         try {
            myxls.close();
         } catch (IOException ex) {
            Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public void SaldosInicialesBco(Conexion oConn, String strPathXLS, VariableSession varSesiones) {
      this.varSesiones = varSesiones;
      InputStream myxls = null;
      try {
         myxls = new FileInputStream(strPathXLS);
         HSSFWorkbook wb = new HSSFWorkbook(myxls);
         System.out.println("Listo...");
         this.SaldoInicialBancos(oConn, wb.getSheetAt(0));
      } catch (IOException ex) {
         System.out.println(" Error " + ex.getMessage());
         Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);

      } finally {
         try {
            myxls.close();
         } catch (IOException ex) {
            Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public void CargarExistenciaProductos(Conexion oConn, String strPathXLS, VariableSession varSesiones) {
      this.varSesiones = varSesiones;
      InputStream myxls = null;
      try {
         myxls = new FileInputStream(strPathXLS);
         HSSFWorkbook wb = new HSSFWorkbook(myxls);
         System.out.println("Listo...");
         this.SaldoInicialProd(oConn, wb.getSheetAt(0));

      } catch (IOException ex) {
         System.out.println(" Error " + ex.getMessage());
         Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);

      } finally {
         try {
            myxls.close();
         } catch (IOException ex) {
            Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public void SaldoInicialBancos(Conexion oConn, HSSFSheet sheet) {
      System.out.println("SALDOS INICIALES BCO " + sheet.getSheetName());
      HSSFRow rowN = sheet.getRow(1);        // third rowN
      Cell cell1 = rowN.getCell(0);
      Cell cell2 = rowN.getCell(1);
      Cell cell3 = rowN.getCell(2);
      Cell cell4 = rowN.getCell(3);
      Cell cell5 = rowN.getCell(4);
      System.out.println(" " + cell1.getStringCellValue());
      System.out.println(" " + cell2.getStringCellValue());
      System.out.println(" " + cell3.getStringCellValue());
      System.out.println(" " + cell4.getStringCellValue());
      System.out.println(" " + cell5.getStringCellValue());
   }

   public void SaldoInicialProv(Conexion oConn, HSSFSheet sheet) {
      System.out.println("SALDOS INICIALES PROV " + sheet.getSheetName());
      int intEncontro = 0;
      int intCuantos = 0;
      Iterator<Row> it = sheet.rowIterator();
      while (it.hasNext()) {
         Row row = it.next();
         if (row.getCell(0) != null) {
            // <editor-fold defaultstate="collapsed" desc="Recuperamos las filas del excel">
            Cell cell1 = row.getCell(0);//Factura anticipo
            Cell cell2 = row.getCell(1);//razon social
            Cell cell3 = row.getCell(2);//numero de factura
            Cell cell4 = row.getCell(3);//Fecha
            Cell cell5 = row.getCell(4);//Importe
            Cell cell6 = row.getCell(5);//IVA
            Cell cell7 = row.getCell(6);//Total
            Cell cell8 = row.getCell(7);//Saldo
            Cell cell9 = row.getCell(8);//Dias de credito
            Cell cell10 = row.getCell(9);//Moneda
            Cell cell11 = row.getCell(10);//Pago
            Cell cell12 = row.getCell(11);//Fecha
            Cell cell13 = row.getCell(12);//Tipo pago
            Cell cell14 = row.getCell(13);//Cuenta contable de gasto
            // </editor-fold>
            //Validamos si estamso en una fila con datos
            if (row.getRowNum() >= 1) {
               intCuantos++;
               System.out.println(" " + row.getRowNum() + " " + cell1.getStringCellValue());
               int intProvId = 0;
               boolean bolEncontroProv = false;
               //Buscamos el proveeedor
               String strSql = "select  * from vta_proveedor where PV_RAZONSOCIAL = '" + cell2.getStringCellValue() + "'"
                       + " AND EMP_ID = " + this.intEMP_ID;
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEncontro++;
                     bolEncontroProv = true;
                     intProvId = rs.getInt("PV_ID");
                     break;
                  }
                  rs.close();
                  // <editor-fold defaultstate="collapsed" desc="si no encontro proveedor lo agregamos">
                  if (!bolEncontroProv) {
                     System.out.println(" Insertamos el proveedor..." + cell2.getStringCellValue());
                     String strInsert = "insert into vta_proveedor(PV_RAZONSOCIAL,EMP_ID,SC_ID)"
                             + "values('" + cell2.getStringCellValue() + "'," + this.intEMP_ID + "," + this.intSC_ID + ")";
                     oConn.runQueryLMD(strInsert);
                     rs = oConn.runQuery("select @@identity", true);
                     while (rs.next()) {
                        intProvId = rs.getInt(1);
                     }
                     rs.close();
                  }
                  // </editor-fold>
                  //Solo si el proveedor es diferente de vacio continuamos
                  if (intProvId != 0) {
                     double dblImporte = 0;
                     double dblImpuesto1 = 0;
                     double dblTotal = 0;
                     int intMoneda = 0;
                     int intDiasCredito = 0;
                     double dblImportePago = 0;
                     String strFecha;
                     String strFechaPago = "";
                     String strTipoPago = "";
                     String strFolio = cell3.getStringCellValue();
                     int intGT_ID = 0;
                     // <editor-fold defaultstate="collapsed" desc="Fecha">
                     if (cell4.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//                        System.out.println("is Date...");
                        Date date = cell4.getDateCellValue();
                        Fechas fecha = new Fechas();
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        strFecha = fecha.getFechaCalendar(calendar);
                     } else {
                        strFecha = cell4.getStringCellValue();
                     }
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Importes y moneda">
                     try {
                        dblImporte = cell5.getNumericCellValue();
                        dblImpuesto1 = cell6.getNumericCellValue();
                        dblTotal = cell7.getNumericCellValue();
                        intDiasCredito = (int) cell9.getNumericCellValue();
                     } catch (NumberFormatException ex) {
                        System.out.println(" ERROR: " + ex.getMessage());
                     }
                     String strNomMoneda = cell10.getStringCellValue();
                     if (strNomMoneda.equals("DOLARES")) {
                        intMoneda = 2;
                     }
                     if (strNomMoneda.equals("DLLS")) {
                        intMoneda = 2;
                     }
                     if (strNomMoneda.equals("DLLS CANADIENSES")) {
                        intMoneda = 4;
                     }
                     if (strNomMoneda.equals("MN") || strNomMoneda.trim().equals("PESOS")) {
                        intMoneda = 1;
                     }
                     if (strNomMoneda.equals("EUROS")) {
                        intMoneda = 3;
                     }
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Datos del pago">
                     try {
                        if (cell11.getNumericCellValue() != 0) {
                           dblImportePago = cell11.getNumericCellValue();
                           strTipoPago = cell13.getStringCellValue();
                           if (cell12.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//                        System.out.println("is Date...");
                              Date date = cell12.getDateCellValue();
                              Fechas fecha = new Fechas();
                              GregorianCalendar calendar = new GregorianCalendar();
                              calendar.setTime(date);
                              strFechaPago = fecha.getFechaCalendar(calendar);
                           } else {
                              strFechaPago = cell12.getStringCellValue();
                           }
                        }
                     } catch (NumberFormatException ex) {
                     } catch (Exception ex) {
                     }
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Datos para el centro de gastos">
                     String strCuentaContable = "";
                     if (cell14 != null) {
                        strCuentaContable = cell14.getStringCellValue();
                        strSql = "SELECT GT_ID from vta_cgastos where GT_CUENTA_CONTABLE= '" + strCuentaContable + "'";
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                           intGT_ID = rs.getInt("GT_ID");
                        }
                        rs.close();
                     }

                     // </editor-fold>
                     //Validamos si es factura o anticipo
                     if (cell1.getStringCellValue().equals("Factura")) {


                        System.out.println(" " + strFecha);
                        System.out.println(" " + dblImporte);
                        System.out.println(" " + dblImpuesto1);
                        System.out.println(" " + dblTotal);
                        System.out.println(" " + intMoneda);
                        System.out.println(" " + intDiasCredito);
                        System.out.println(" " + dblImportePago);
                        System.out.println(" " + strFechaPago);
                        System.out.println(" " + intGT_ID);

                        // <editor-fold defaultstate="collapsed" desc="Genereramos la cuenta por pagar">
                        CuentasxPagar cuenta = new CuentasxPagar(oConn, varSesiones, null);
                        cuenta.setIntEMP_ID(this.intEMP_ID);
                        cuenta.getDocument().setFieldInt("EMP_ID", this.intEMP_ID);
                        String strPrefijoMaster = "CXP";
                        String strPrefijoDeta = "CXPD";
                        cuenta.getDocument().setFieldInt("SC_ID", this.intSC_ID);
                        cuenta.getDocument().setFieldInt("PV_ID", intProvId);

                        cuenta.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", intMoneda);
                        cuenta.getDocument().setFieldString(strPrefijoMaster + "_FECHA", strFecha);
                        cuenta.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", strFolio);
                        cuenta.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", "");
                        cuenta.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", strFolio);

                        cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", dblImporte);
                        cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", dblImpuesto1);
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO2")));
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO3")));
                        cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", dblTotal);
                        cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", 1);
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA2")));
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA3")));
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", Double.valueOf(request.getParameter(strPrefijoMaster + "_RETISR")));
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", Double.valueOf(request.getParameter(strPrefijoMaster + "_RETIVA")));
//                     cuenta.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", Double.valueOf(request.getParameter(strPrefijoMaster + "_NETO")));

                        cuenta.getDocument().setFieldInt(strPrefijoMaster + "_DIASCREDITO", intDiasCredito);

//                     cuenta.getDocument().setFieldString("PED_COD", request.getParameter(strPrefijoMaster + "_NUMPEDI"));
//                     cuenta.getDocument().setFieldString("PED_ID", request.getParameter(strPrefijoMaster + "_PED_ID"));
//                     cuenta.getDocument().setFieldString(strPrefijoMaster + "_ADUANA", request.getParameter(strPrefijoMaster + "_ADUANA"));
                        // </editor-fold>
                     /*
                         String strFechaPedimento = request.getParameter(strPrefijoMaster + "_FECHAPEDI");
                         if (strFechaPedimento.contains("/") && strFechaPedimento.length() == 10) {
                         strFechaPedimento = fecha.FormateaBD(strFechaPedimento, "/");
                         }
                         cuenta.getDocument().setFieldString(strPrefijoMaster + "_FECHAPEDI", strFechaPedimento);*/
                        // <editor-fold defaultstate="collapsed" desc="Tarifas de IVA">
                        int intTI_ID = 1;
                        int intTI_ID2 = 0;
                        int intTI_ID3 = 0;
                        try {
                           intTI_ID = 0;
                           intTI_ID2 = 0;
                           intTI_ID3 = 0;
                        } catch (NumberFormatException ex) {
                           System.out.println("CXPAGAR TI_ID " + ex.getMessage());
                        }
                        // </editor-fold>
                        cuenta.getDocument().setFieldInt("TI_ID", intTI_ID);
                        cuenta.getDocument().setFieldInt("TI_ID2", intTI_ID2);
                        cuenta.getDocument().setFieldInt("TI_ID3", intTI_ID3);
//                     cuenta.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", request.getParameter(strPrefijoMaster + "_NOTASPIE"));
//                     cuenta.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", request.getParameter(strPrefijoMaster + "_CONDPAGO"));


                        // <editor-fold defaultstate="collapsed" desc="Generamos el detalle">
//                     int intCount = Integer.valueOf(request.getParameter("COUNT_ITEM"));
//                     for (int i = 1; i <= intCount; i++) {
                        TableMaster deta = new vta_cxpagardetalle();
                        deta.setFieldInt("PR_ID", 0);
                        deta.setFieldInt(strPrefijoDeta + "_EXENTO1", 0);
                        deta.setFieldInt(strPrefijoDeta + "_EXENTO2", 0);
                        deta.setFieldInt(strPrefijoDeta + "_EXENTO3", 0);
                        deta.setFieldString(strPrefijoDeta + "_CVE", "");
                        deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", "SALDO INICIAL");
                        deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", dblImporte);
                        deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", 1);
                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", 0);
//                     deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA2" + i)));
//                     deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA3" + i)));
                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", 0);
//                     deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO2" + i)));
//                     deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO3" + i)));
                        deta.setFieldDouble(strPrefijoDeta + "_COSTO", dblImporte);
//                     deta.setFieldDouble(strPrefijoDeta + "_RET_ISR", Integer.valueOf(request.getParameter(strPrefijoDeta + "_RET_ISR" + i)));
//                     deta.setFieldDouble(strPrefijoDeta + "_RET_IVA", Integer.valueOf(request.getParameter(strPrefijoDeta + "_RET_IVA" + i)));
                        //Definimos el centro de gastos
                        deta.setFieldInt("GT_ID", intGT_ID);
                        cuenta.AddDetalle(deta);
//                     }
                        // </editor-fold>

                        //Inicializamos objeto
                        cuenta.Init();
                        //Generamos transaccion
                        cuenta.doTrx();

                        String strRes = "";
                        if (cuenta.getStrResultLast().equals("OK")) {
                           strRes = "OK." + cuenta.getDocument().getValorKey();
                           // <editor-fold defaultstate="collapsed" desc="Aplicamos el pago si existe">
                           if (dblImportePago > 0) {
                              MovProveedor movProv = new MovProveedor(oConn, varSesiones, null);
                              movProv.Init();
                              movProv.setBolEsAnticipo(true);
                              movProv.setBolCaja(false);
                              //Recibimos datos para el encabezado
                              movProv.getCta_prov().setFieldInt("PV_ID", intProvId);
                              movProv.getCta_prov().setFieldString("MP_FECHA", strFechaPago);
                              movProv.getCta_prov().setFieldString("MP_NOTAS", strTipoPago);
                              movProv.getCta_prov().setFieldInt("MP_MONEDA", intMoneda);
                              movProv.getCta_prov().setFieldDouble("MP_TASAPESO", 1);
                              movProv.getCta_prov().setFieldInt("CXP_ID", Integer.valueOf(cuenta.getDocument().getValorKey()));
                              movProv.getCta_prov().setFieldInt("MP_ESPAGO", 1);
                              movProv.getCta_prov().setFieldDouble("MP_ABONO", Math.abs(dblImportePago));

                              //Generamos transaccion
                              movProv.doTrx();
                           }
                           // </editor-fold>
                        } else {
                           strRes = cuenta.getStrResultLast();
                        }
                        System.out.println("strRes:" + strRes);
                     } else {
                        // <editor-fold defaultstate="collapsed" desc="Es un anticipo">
                        MovProveedor movProv = new MovProveedor(oConn, varSesiones, null);
                        movProv.Init();
                        movProv.setBolEsAnticipo(true);
                        movProv.setBolCaja(false);
                        //Recibimos datos para el encabezado
                        movProv.getCta_prov().setFieldInt("PV_ID", intProvId);
                        movProv.getCta_prov().setFieldString("MP_FECHA", strFecha);
                        movProv.getCta_prov().setFieldString("MP_NOTAS", "");
                        movProv.getCta_prov().setFieldInt("MP_MONEDA", intMoneda);
                        movProv.getCta_prov().setFieldDouble("MP_TASAPESO", 0);
                        movProv.getCta_prov().setFieldInt("CXP_ID", 0);
                        movProv.getCta_prov().setFieldInt("MP_ESPAGO", 1);
                        movProv.getCta_prov().setFieldDouble("MP_ABONO", Math.abs(dblTotal));
                        movProv.getCta_prov().setFieldInt("MP_ANTICIPO", 1);

                        //Generamos transaccion
                        movProv.doTrx();
                        // </editor-fold>
                     }

                  } else {
                     System.out.println("ERROR: NO HAY PROVEEDOR");
                  }
               } catch (SQLException ex) {
                  Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
               }
               System.out.println(" ");
               System.out.println(" ");
            } else {
               System.out.println(" Titles...");
               System.out.println(" " + cell1.getStringCellValue());
               System.out.println(" " + cell2.getStringCellValue());
               System.out.println(" " + cell3.getStringCellValue());
               System.out.println(" " + cell4.getStringCellValue());
               System.out.println(" " + cell5.getStringCellValue());
               System.out.println(" " + cell6.getStringCellValue());
               System.out.println(" " + cell7.getStringCellValue());
               System.out.println(" " + cell8.getStringCellValue());
//               System.out.println(" " + cell10.getStringCellValue());
//               System.out.println(" " + cell11.getStringCellValue());
//               System.out.println(" " + cell12.getStringCellValue());
//               System.out.println(" " + cell13.getStringCellValue());
//               System.out.println(" " + cell14.getStringCellValue());
//               System.out.println(" " + cell15.getStringCellValue());
//               System.out.println(" " + cell16.getStringCellValue());
               System.out.println(" Titles...");
            }

         }


      }
      //Totales
      System.out.println("total: " + intCuantos);
      System.out.println("encontrados: " + intEncontro);
   }

   public void SaldoInicialCte(Conexion oConn, HSSFSheet sheet) {
      System.out.println("SALDOS INICIALES CTE " + sheet.getSheetName());
      int intEncontro = 0;
      int intCuantos = 0;
      Iterator<Row> it = sheet.rowIterator();
      while (it.hasNext()) {
         Row row = it.next();
         if (row.getCell(0) != null) {
            Cell cell1 = row.getCell(0);
            Cell cell2 = row.getCell(1);
            Cell cell3 = row.getCell(2);
            Cell cell4 = row.getCell(3);
            Cell cell5 = row.getCell(4);
            Cell cell6 = row.getCell(5);
            Cell cell7 = row.getCell(6);
            Cell cell8 = row.getCell(7);
            Cell cell9 = row.getCell(8);
            Cell cell10 = row.getCell(9);
            Cell cell11 = row.getCell(10);
            Cell cell12 = row.getCell(11);
            Cell cell13 = row.getCell(12);
            Cell cell14 = row.getCell(13);
            Cell cell15 = row.getCell(14);
            Cell cell16 = row.getCell(15);
            Cell cell17 = row.getCell(16);
            //Validamos si estamso en una fila con datos
            if (row.getRowNum() >= 2) {
               intCuantos++;
               System.out.println(" " + row.getRowNum() + " " + row.getCell(0));
               int intProvId = 0;
               boolean bolEncontroProv = false;
               //Buscamos el proveeedor
               String strSql = "select  * from vta_cliente where CT_RAZONSOCIAL = '" + cell2.getStringCellValue() + "'"
                       + " AND EMP_ID = " + this.intEMP_ID;
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEncontro++;
                     bolEncontroProv = true;
                     intProvId = rs.getInt("PV_ID");
                     break;
                  }
                  rs.close();
                  //si no encontro lo agregamos
                  if (!bolEncontroProv) {
                     System.out.println(" Insertamos el CLIENTE...");
                     String strInsert = "insert into vta_cliente(CT_RAZONSOCIAL,EMP_ID,SC_ID)"
                             + "values('" + cell2.getStringCellValue() + "'," + this.intEMP_ID + "," + this.intSC_ID + ")";
                     oConn.runQueryLMD(strInsert);
                     rs = oConn.runQuery("select @@identity", true);
                     while (rs.next()) {
                        intProvId = rs.getInt(1);
                     }
                  }
                  //Solo si el proveedor es diferente de vacio continuamos
                  if (intProvId != 0) {
                     double dblImporte = 0;
                     double dblTotal = 0;
                     int intMoneda = 0;
                     if (cell7.getStringCellValue().equals("DLLS")) {
                        intMoneda = 2;
                     }
                     if (cell7.getStringCellValue().equals("DLLS CANADIENSES")) {
                        intMoneda = 4;
                     }
                     if (cell7.getStringCellValue().equals("MN")) {
                        intMoneda = 1;
                     }
                     String strFecha = "";
                     if (cell9.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
//                        System.out.println("is Date...");
                        Date date = cell9.getDateCellValue();
                        Fechas fecha = new Fechas();
                        GregorianCalendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        strFecha = fecha.getFechaCalendar(calendar);
                     } else {
                        strFecha = cell9.getStringCellValue();
                     }

                     try {
                        dblImporte = cell4.getNumericCellValue();
                        dblTotal = cell6.getNumericCellValue();
                     } catch (NumberFormatException ex) {
                        System.out.println(" ERROR: " + ex.getMessage());
                     }
                  } else {
                     System.out.println("ERROR: NO HAY CLIENTE");
                  }
               } catch (SQLException ex) {
                  Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
               }
               System.out.println(" ");
               System.out.println(" ");
            } else {
               System.out.println(" Titles...");
               System.out.println(" " + cell1.getStringCellValue());
               System.out.println(" " + cell2.getStringCellValue());
               System.out.println(" " + cell3.getStringCellValue());
               System.out.println(" " + cell4.getStringCellValue());
               System.out.println(" " + cell5.getStringCellValue());
               System.out.println(" " + cell6.getStringCellValue());
               System.out.println(" " + cell7.getStringCellValue());
               System.out.println(" " + cell8.getStringCellValue());
               System.out.println(" " + cell9.getStringCellValue());
               System.out.println(" " + cell10.getStringCellValue());
               System.out.println(" " + cell11.getStringCellValue());
               System.out.println(" " + cell12.getStringCellValue());
               System.out.println(" " + cell13.getStringCellValue());
               System.out.println(" " + cell14.getStringCellValue());
               System.out.println(" " + cell15.getStringCellValue());
               System.out.println(" " + cell16.getStringCellValue());
               System.out.println(" Titles...");
            }

         }


      }
      //Totales
      System.out.println("total: " + intCuantos);
      System.out.println("encontrados: " + intEncontro);
   }

   /**
    * Carga la existencias iniciales de los productos
    *
    * @param oConn Es la conexion
    * @param sheet Es la hoja del archivo de excel
    */
   public void SaldoInicialProd(Conexion oConn, HSSFSheet sheet) {
      System.out.println("SALDOS INICIALES PROD  " + sheet.getSheetName());
      int intEncontro = 0;
      int intCuantos = 0;

      Fechas fecha = new Fechas();
      Inventario inv = new Inventario(oConn, varSesiones, null);
      //Recuperamos parametros
      inv.getMovProd().setFieldInt("TIN_ID", 0);
      inv.getMovProd().setFieldInt("SC_ID", 1);
      inv.getMovProd().setFieldInt("SC_ID2", 0);
      inv.getMovProd().setFieldString("MP_FECHA", fecha.getFechaActual());
      inv.getMovProd().setFieldString("MP_FOLIO", "");
      inv.getMovProd().setFieldString("MP_NOTAS", "Automatico");
      inv.getMovProd().setFieldInt("MP_IDORIGEN", 0);
      inv.setIntTipoOperacion(Inventario.ENTRADA);
      inv.setIntEMP_ID(this.intEMP_ID);
      //Definimos el sistema de costos
      inv.setIntTipoCosteo(Inventario.ENTRADA);


      //Iteramos por todo el archivo de excel
      Iterator<Row> it = sheet.rowIterator();
      while (it.hasNext()) {
         Row row = it.next();
         if (row.getCell(0) != null) {
            Cell cell1 = row.getCell(0);//codigo
            Cell cell2 = row.getCell(1);//descripcion
            Cell cell3 = row.getCell(2);//Existencia
            Cell cell4 = row.getCell(3);//Existencia

            //Validamos si estamso en una fila con datos
            if (row.getRowNum() >= 1) {
               intCuantos++;
               System.out.println(" " + row.getRowNum() + " " + cell1.getStringCellValue());
               int intProdId = 0;
               boolean bolEncontroProd = false;
               double dblCostoRepo = 0;
               //Buscamos el proveeedor
               String strSql = "select  PR_ID,PR_COSTOREPOSICION from vta_producto where PR_CODIGO = '" + cell1.getStringCellValue() + "'"
                       + " AND EMP_ID = " + this.intEMP_ID;
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intEncontro++;
                     bolEncontroProd = true;
                     intProdId = rs.getInt("PR_ID");
                     dblCostoRepo = rs.getDouble("PR_COSTOREPOSICION");
                     break;
                  }
                  rs.close();
               } catch (SQLException ex) {
                  Logger.getLogger(Importar.class.getName()).log(Level.SEVERE, null, ex);
               }
               //mostramos datos en consola
               System.out.println("|" + cell1.getStringCellValue() + "|"
                       + cell2.getStringCellValue() + "|" + cell3.getNumericCellValue() + "|" + cell4.getNumericCellValue() + "|");
               if (bolEncontroProd) {
                  //Generamos el movimiento de entrada
                  vta_movproddeta movDeta = new vta_movproddeta();
                  movDeta.setFieldInt("PR_ID", intProdId);
                  movDeta.setFieldInt("SC_ID", (int) cell3.getNumericCellValue());
                  movDeta.setFieldInt("MPD_IDORIGEN", 0);
                  movDeta.setFieldString("MPD_FECHA", fecha.getFechaActual());
                  movDeta.setFieldDouble("MPD_COSTO", dblCostoRepo);
                  movDeta.setFieldString("PR_CODIGO", cell1.getStringCellValue());
                  movDeta.setFieldString("MPD_NOTAS", "Automatico");
                  movDeta.setFieldInt("ID_USUARIOS", varSesiones.getIntNoUser());
                  double dblCantidad = cell3.getNumericCellValue();
                  //Si el tipo de movimiento en ENTRADA
                  movDeta.setFieldDouble("MPD_ENTRADAS", dblCantidad);
                  inv.AddDetalle(movDeta);

               }

            } else {
               System.out.println(" Titles...");
               System.out.println(" " + cell1.getStringCellValue());
               System.out.println(" " + cell2.getStringCellValue());
               System.out.println(" " + cell3.getStringCellValue());
               System.out.println(" " + cell4.getStringCellValue());
               System.out.println(" Titles...");
            }
         }
      }
      //Inicializamos objeto

      inv.Init();
      //Almacenamos la operacion

      inv.doTrx();
      //Totales

      System.out.println(
              "total: " + intCuantos);
      System.out.println(
              "encontrados: " + intEncontro);
      System.out.println(
              "Resultado: " + inv.getStrResultLast());
   }

   public void SaldoInicialPagos(Conexion oConn, HSSFSheet sheet, int intEMP_ID, VariableSession varSesiones) {
      System.out.println("SALDOS INICIALES PAGOS FACTURAS  " + sheet.getSheetName());
      int intEncontro = 0;
      int intCuantos = 0;
      String strIds = "";
      Iterator<Row> it = sheet.rowIterator();
      while (it.hasNext()) {
         Row row = it.next();
         if (row.getCell(0) != null) {
            Cell cell1 = row.getCell(0);
            Cell cell2 = row.getCell(1);
            Cell cell3 = row.getCell(2);
            Cell cell4 = row.getCell(3);
            Cell cell5 = row.getCell(4);
            Cell cell6 = row.getCell(13);
            //Cell cell7 = row.getCell(6);
//            Cell cell8 = row.getCell(7);
//            Cell cell9 = row.getCell(8);
//            Cell cell10 = row.getCell(9);
//            Cell cell11 = row.getCell(10);
//            Cell cell12 = row.getCell(11);
//            Cell cell13 = row.getCell(12);
//            Cell cell14 = row.getCell(13);
//            Cell cell15 = row.getCell(14);
//            Cell cell16 = row.getCell(15);
//            Cell cell17 = row.getCell(16);
            //Validamos si estamso en una fila con datos
            if (row.getRowNum() >= 1) {
               System.out.print("|" + cell1.getStringCellValue());
               System.out.print("|" + cell2.getStringCellValue());
               System.out.print("|" + cell3.getNumericCellValue());
               System.out.print("|" + cell4.getDateCellValue());
               System.out.print("|" + cell5.getStringCellValue());
               System.out.print("|" + cell6.getNumericCellValue());
               System.out.println("");
               //Validamos que no sean anticipos o notas de credito
               if (((int) cell3.getNumericCellValue()) != 9999999 && (int) cell3.getNumericCellValue() != 0) {
                  strIds += (int) cell3.getNumericCellValue() + ",";
                  String strSql = "select * from vta_facturas where FAC_ID = " + ((int) cell3.getNumericCellValue());
                  try {
                     ResultSet rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        double dblSaldo = rs.getDouble("FAC_SALDO");
                        double dblPago = dblSaldo - cell6.getNumericCellValue();
                        if (dblPago > 0.999) {

                           //
                           System.out.println("Hay que aplicar pago por: " + dblPago);
                        }
                     }
                  } catch (SQLException ex) {
                  }

               }

            } else {
               System.out.println(" Titles...");
               System.out.println(" " + cell1.getStringCellValue());
               System.out.println(" " + cell2.getStringCellValue());
               System.out.println(" " + cell3.getStringCellValue());
               System.out.println(" " + cell4.getStringCellValue());
               System.out.println(" " + cell5.getStringCellValue());
               System.out.println(" " + cell6.getStringCellValue());
               System.out.println(" Titles...");
            }
         }
      }
      //Totales
      System.out.println("total: " + intCuantos);
      System.out.println("encontrados: " + intEncontro);
      System.out.println("strIds: " + strIds);
      //Aplicamos los pagos a todas las facturas que no vienen listadas
      int intTotFacturas = 0;
      String strSql = "select * from vta_facturas where EMP_ID = " + intEMP_ID + " "
              + "AND FAC_ANULADA = 0  "
              + "AND FAC_SALDO > 0 "
              + "AND FAC_ID NOT IN(" + strIds.substring(0, strIds.length() - 1) + ")";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intTotFacturas++;
            System.out.println(" " + rs.getInt("FAC_ID"));
//Instanciamos el objeto que nos trae las listas de precios
            movCliente movCte = new movCliente(oConn, varSesiones, null);
            //Inicializamos objeto
            movCte.Init();
            //Recibimos datos para el encabezado
            movCte.getCta_clie().setFieldString("MC_FECHA", "20121231");
            movCte.getCta_clie().setFieldString("MC_NOTAS", "");
            movCte.getCta_clie().setFieldInt("MC_MONEDA", rs.getInt("FAC_MONEDA"));
            movCte.getCta_clie().setFieldDouble("MC_TASAPESO", rs.getDouble("FAC_TASAPESO"));
            movCte.getCta_clie().setFieldInt("FAC_ID", rs.getInt("FAC_ID"));
            movCte.getCta_clie().setFieldInt("TKT_ID", 0);
            movCte.getCta_clie().setFieldInt("MC_ESPAGO", 1);
            movCte.getCta_clie().setFieldDouble("MC_ABONO", rs.getDouble("FAC_TOTAL"));

            //Recibimos los pagos

            vta_mov_cte_deta detaPago = new vta_mov_cte_deta();
            detaPago.setFieldInt("CT_ID", 0);
            detaPago.setFieldInt("SC_ID", 0);
            detaPago.setFieldInt("MCD_MONEDA", rs.getInt("FAC_MONEDA"));
            detaPago.setFieldString("MCD_FOLIO", "");
            detaPago.setFieldString("MCD_FORMAPAGO", "");
            detaPago.setFieldString("MCD_NOCHEQUE", "");
            detaPago.setFieldString("MCD_BANCO", "");
            detaPago.setFieldString("MCD_NOTARJETA", "");
            detaPago.setFieldString("MCD_TIPOTARJETA", "");
            detaPago.setFieldDouble("MCD_IMPORTE", rs.getDouble("FAC_TOTAL"));
            detaPago.setFieldDouble("MCD_TASAPESO", rs.getDouble("FAC_TASAPESO"));
            detaPago.setFieldDouble("MCD_CAMBIO", 0.0);
            movCte.AddDetalle(detaPago);

            //Generamos transaccion
            movCte.doTrx();
            String strRes = "";
            if (movCte.getStrResultLast().equals("OK")) {
               strRes = "OK." + movCte.getCta_clie().getValorKey();
            } else {
               strRes = movCte.getStrResultLast();
            }
            System.out.println("Res... " + strRes);
         }
         rs.close();
      } catch (SQLException ex) {
         System.out.println("Excepcion sql " + ex.getMessage());
      }
      System.out.println(
              "Tot Facturas con pagos aplicados: " + intTotFacturas);
   }
   // </editor-fold>
}
