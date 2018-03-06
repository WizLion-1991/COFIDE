/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

//import jxl.*; 
import ERP.BusinessEntities.ConceptoNomina;
import Tablas.rhh_empleados;
import java.io.*;
import Tablas.rhh_nominas_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author siwebmx5
 */
public class Importa_Nominas {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Importa_Nominas.class.getName());
   private String strResultLast;
   private boolean bolLimpiarInfo = false;
   private String strFolioIni;
   private String strFolioFin;

   public String getStrResultLast() {
      return strResultLast;
   }

   public void setStrResultLast(String strResultLast) {
      this.strResultLast = strResultLast;
   }

   public boolean isBolLimpiarInfo() {
      return bolLimpiarInfo;
   }

   public void setBolLimpiarInfo(boolean bolLimpiarInfo) {
      this.bolLimpiarInfo = bolLimpiarInfo;
   }

   public String getStrFolioIni() {
      return strFolioIni;
   }

   public void setStrFolioIni(String strFolioIni) {
      this.strFolioIni = strFolioIni;
   }

   public String getStrFolioFin() {
      return strFolioFin;
   }

   public void setStrFolioFin(String strFolioFin) {
      this.strFolioFin = strFolioFin;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Importa_Nominas() {
      this.strResultLast = "";
      this.bolLimpiarInfo = true;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void Carga_Nominas(String strFecha, String strRegimenFiscal, String strPathExcel, Conexion oConn, VariableSession varSesiones) {
      this.strResultLast = "OK";
      log.debug("Validamos...");
      if (ValidaExcel(strPathExcel, oConn, varSesiones)) {
         log.debug("Limpieza inicial...");
         //Si tiene activa la bandera de limpiar que borre la info
         if (this.bolLimpiarInfo) {

            //Evaluamos si el folio es por sucursal
            boolean bolEsFolioSucursal = false;
            String strSql3 = "SELECT EMP_REGISTRO_PATRONAL,EMP_FOLIO_SUCURSAL_NOMINA "
                    + " FROM vta_empresas "
                    + " WHERE EMP_ID = " + varSesiones.getIntIdEmpresa() + "";
            try {
               ResultSet rs = oConn.runQuery(strSql3, true);
               while (rs.next()) {
                  if (rs.getInt("EMP_FOLIO_SUCURSAL_NOMINA") == 1) {
                     bolEsFolioSucursal = true;
                  }
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               ex.fillInStackTrace();
            }

            //Borramos las nominas
            String strSql = "delete from rhh_nominas where NOM_SE_TIMBRO = 0 AND EMP_ID = " + varSesiones.getIntIdEmpresa();
            if (bolEsFolioSucursal) {
               strSql = "delete from rhh_nominas where NOM_SE_TIMBRO = 0 AND EMP_ID = " + varSesiones.getIntIdEmpresa()
                       + " AND SC_ID = " + varSesiones.getIntSucursalDefault();
            }
            oConn.runQueryLMD(strSql);

            //Regeneramos el folio
            int intFolio = 1;
            strSql = "select convert(max(nom_folio) ,unsigned integer) as num_folio from rhh_nominas where EMP_ID = " + varSesiones.getIntIdEmpresa();
            if (bolEsFolioSucursal) {
               strSql = "select convert(max(nom_folio) ,unsigned integer) as num_folio from rhh_nominas where EMP_ID = " + varSesiones.getIntIdEmpresa()
                       + " AND SC_ID = " + varSesiones.getIntSucursalDefault();
            }
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intFolio = rs.getInt("num_folio");
               }
               rs.close();
            } catch (SQLException ex) {
               log.error(ex.getMessage());
            }
            //Especificamos el nombre de la tabla
            String strNomTablaFolios = "vta_folionomina" + varSesiones.getIntIdEmpresa();
            if (bolEsFolioSucursal) {
               strNomTablaFolios = "vta_folionomina" + varSesiones.getIntIdEmpresa() + "_" + varSesiones.getIntSucursalDefault();
            }
            //Borramos los items
            strSql = "delete from " + strNomTablaFolios + " where FOL_ID > " + intFolio;
            oConn.runQueryLMD(strSql);
            //Alteramos el autoincremental
            if (intFolio == 0) {
               strSql = "alter table " + strNomTablaFolios + " AUTO_INCREMENT = 1";
            } else {
               intFolio++;
               strSql = "alter table " + strNomTablaFolios + " AUTO_INCREMENT = " + intFolio;
            }
            oConn.runQueryLMD(strSql);
         }
         try {
            InputStream myxls = null;
            myxls = new FileInputStream(strPathExcel);

            HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);
            SimpleDateFormat sdNormal = new SimpleDateFormat("yyyyMMdd");
            Cell cllintClave;
            Cell clldateFecha_inicial_de_pago;
            Cell cllFecha_final_de_pago;
            Cell cllISR_Retenido;
            Cell cllNumero_de_dias_pagados;
//            Cell cllMetododePago;
            Cell cllTasaISR;
            Cell cllConceptoNomina;
            Cell cllNotas1;
            Cell cllNotas2;

            Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
            it.next();
            //Identificamos los codigos de los conceptos
            Row row2 = it.next();
            //Identificamos el detalle
            log.debug("Identificamos encabezados...");
            //de la fila 6 34 son percepciones
            //de la fila 35 55 son deducciones
            ArrayList<ConceptoNomina> lstConceptos = new ArrayList<ConceptoNomina>();
            log.debug("Percepciones...");
            for (int iContC = 0; iContC <= 7; iContC++) {
               lstConceptos.add(null);
            }
            for (int iContC = 8; iContC <= 35; iContC++) {
               Cell cellTmp = row2.getCell(iContC);
               ConceptoNomina concepto = new ConceptoNomina();
               concepto.getPercepcion(cellTmp.getStringCellValue(), varSesiones.getIntIdEmpresa(), oConn);
               lstConceptos.add(concepto);
               log.debug(" " + cellTmp.getStringCellValue());
            }
            log.debug("Deducciones...");
            for (int iContC = 36; iContC <= 56; iContC++) {
               Cell cellTmp = row2.getCell(iContC);
               ConceptoNomina concepto = new ConceptoNomina();
               concepto.getDeduccion(cellTmp.getStringCellValue(), varSesiones.getIntIdEmpresa(), oConn);
               lstConceptos.add(concepto);
               log.debug(" " + cellTmp.getStringCellValue());
            }
            //ciclo para las nominas
            log.debug("Ciclo para las nominas.F...");
            while (it.hasNext()) {
               Row row = it.next();

               //Recuperamos las celdas principales
               cllintClave = row.getCell(1);
               clldateFecha_inicial_de_pago = row.getCell(2);
               cllFecha_final_de_pago = row.getCell(3);
               cllISR_Retenido = row.getCell(4);
               cllTasaISR = row.getCell(5);
               cllNumero_de_dias_pagados = row.getCell(6);
//               cllMetododePago = row.getCell(7);
               cllConceptoNomina = row.getCell(7);
               cllNotas1 = row.getCell(57);
               cllNotas2 = row.getCell(58);

               //Solo si tiene un numero de empleado continuamos
               if (cllintClave != null) {
                  if (cllintClave.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                     //Obtenemos datos del empleado
                     rhh_empleados dEmpleado = new rhh_empleados();
                     dEmpleado.ObtenDatos((int) cllintClave.getNumericCellValue(), oConn);
                     //Datos iniciales
                     Date dateFechaInicial = null;
                     dateFechaInicial = clldateFecha_inicial_de_pago.getDateCellValue();
                     Date dateFechaFinal = null;
                     dateFechaFinal = cllFecha_final_de_pago.getDateCellValue();

                     Nominas nomina = new Nominas(oConn, varSesiones, null);
                     nomina.Init();
                     nomina.setIntEMP_ID(varSesiones.getIntIdEmpresa());
                     nomina.getDocument().setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
                     nomina.getDocument().setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
                     nomina.getDocument().setFieldString("NOM_FECHA", strFecha);
                     nomina.getDocument().setFieldString("NOM_FECHA_INICIAL_PAGO", sdNormal.format(dateFechaInicial));
                     nomina.getDocument().setFieldString("NOM_FECHA_FINAL_PAGO", sdNormal.format(dateFechaFinal));
                     nomina.getDocument().setFieldInt("EMP_NUM", (int) cllintClave.getNumericCellValue());
                     nomina.getDocument().setFieldInt("NOM_MONEDA", 1);

                     nomina.getDocument().setFieldString("NOM_REGIMENFISCAL", strRegimenFiscal);
                     nomina.getDocument().setFieldString("NOM_CONCEPTO", cllConceptoNomina.getStringCellValue());
                     nomina.getDocument().setFieldDouble("NOM_ISR_RETENIDO", cllISR_Retenido.getNumericCellValue());
                     nomina.getDocument().setFieldDouble("NOM_TASA_ISR", cllTasaISR.getNumericCellValue());
                     nomina.getDocument().setFieldDouble("NOM_RETISR", cllISR_Retenido.getNumericCellValue());
                     nomina.getDocument().setFieldDouble("NOM_NUM_DIAS_PAGADOS", (double) cllNumero_de_dias_pagados.getNumericCellValue());
                     //Datos que copiamos de los empleados
                     nomina.getDocument().setFieldString("NOM_METODODEPAGO", dEmpleado.getFieldString("EMP_METODO_PAGO"));
                     nomina.getDocument().setFieldString("NOM_NUMCUENTA", dEmpleado.getFieldString("EMP_NUM_CTA"));
                     nomina.getDocument().setFieldString("NOM_CONDPAGO", dEmpleado.getFieldString("EMP_CONDICIONES"));
                     nomina.getDocument().setFieldString("NOM_FORMADEPAGO", dEmpleado.getFieldString("EMP_FORMA_DE_PAGO"));

                     //Evaluamos los campos adicionales
                     if (cllNotas1 != null) {
                        if (cllNotas1.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                           BigDecimal big = new BigDecimal(cllNotas1.getNumericCellValue());
                           nomina.getDocument().setFieldString("NOM_CAMPO_ADICIONAL1", big.toString());
                        } else {
                           nomina.getDocument().setFieldString("NOM_CAMPO_ADICIONAL1", cllNotas1.getStringCellValue());
                        }
                     }
                     //Evaluamos los campos adicionales
                     if (cllNotas2 != null) {
                        if (cllNotas2.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                           BigDecimal big = new BigDecimal(cllNotas2.getNumericCellValue());
                           nomina.getDocument().setFieldString("NOM_CAMPO_ADICIONAL2", big.toString());
                        } else {
                           nomina.getDocument().setFieldString("NOM_CAMPO_ADICIONAL2", cllNotas2.getStringCellValue());
                        }
                     }

                     //Identificamos el detalle
                     //de la fila 6 34 son percepciones
                     //de la fila 35 55 son deducciones
                     double dblTotPercepcion = 0;
                     double dblTotDeduccion = 0;
                     for (int iContC = 8; iContC <= 35; iContC++) {
                        Cell cellPercepcion = row.getCell(iContC);
                        if (cellPercepcion != null) {
                           if (cellPercepcion.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                              double dblPercepcion = cellPercepcion.getNumericCellValue();
                              double dblPercepcionExenta = 0;
                              double dblPercepcionTotalFila = 0;
                              

                              //Buscamos si pusieron algo en la parte no gravada o exenta
                              Cell cellPercepcionExenta = row.getCell(iContC + 100);
                              if (cellPercepcionExenta != null) {
                                 if (cellPercepcionExenta.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    dblPercepcionExenta = cellPercepcionExenta.getNumericCellValue();
                                 }
                              }
                              //Sumatorias
                              dblPercepcionTotalFila = (dblPercepcion + dblPercepcionExenta);
                              dblTotPercepcion += dblPercepcionTotalFila;

                              if (dblPercepcionTotalFila > 0) {
                                 //agregamos una partida de la nomina
                                 rhh_nominas_deta detalle = new rhh_nominas_deta();
                                 detalle.setFieldInt("TP_ID", lstConceptos.get(iContC).getIntIdCategoria());
                                 detalle.setFieldInt("PERC_ID", lstConceptos.get(iContC).getIntIdConcepto());
                                 detalle.setFieldInt("TD_ID", 0);
                                 detalle.setFieldInt("DEDU_ID", 0);
                                 detalle.setFieldInt("NOMD_CANTIDAD", 1);
                                 detalle.setFieldDouble("NOMD_UNITARIO", dblPercepcionTotalFila);
                                 detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblPercepcion);
                                 detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", dblPercepcionExenta);
                                 detalle.setFieldDouble("NOMD_GRAVADO", lstConceptos.get(iContC).getIntGravado());

                                                      //
                                 //de la fila 6 34 son percepciones
                                 //de la fila 35 55 son deducciones
                                 nomina.getLstConceptos().add(detalle);
                                 //Buscamos las notas en las filas de notas a detalle
                                 Cell cllConceptoNominaDeta = row.getCell(iContC + 51);
                                 if (cllConceptoNominaDeta != null) {
                                    if (cllConceptoNominaDeta.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                                       detalle.setFieldString("NOMD_NOTAS", cllConceptoNominaDeta.getStringCellValue());
                                    }
                                 }

                              }
                           }
                        }

                     }
                     //Ciclo deducciones
                     for (int iContC = 36; iContC <= 56; iContC++) {
                        Cell cellDeduccion = row.getCell(iContC);
                        if (cellDeduccion != null) {
                           if (cellDeduccion.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                              double dblDeduccion = cellDeduccion.getNumericCellValue();
                              double dblDeduccionExenta = 0;
                              double dblDeduccionTotalFila = 0;
                              

                              //Buscamos si pusieron algo en la parte no gravada o exenta
                              Cell cellDeduccionExenta = row.getCell(iContC + 100);
                              if (cellDeduccionExenta != null) {
                                 if (cellDeduccionExenta.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                    dblDeduccionExenta = cellDeduccionExenta.getNumericCellValue();
                                 }
                              }
                              //Sumatorias
                              dblDeduccionTotalFila = (dblDeduccion + dblDeduccionExenta);                              
                              dblTotDeduccion += dblDeduccionTotalFila;
                              
                              if (dblDeduccionTotalFila > 0) {
                                 //agregamos una partida de la nomina
                                 rhh_nominas_deta detalle = new rhh_nominas_deta();
                                 detalle.setFieldInt("TP_ID", 0);
                                 detalle.setFieldInt("PERC_ID", 0);
                                 detalle.setFieldInt("TD_ID", lstConceptos.get(iContC).getIntIdCategoria());
                                 detalle.setFieldInt("DEDU_ID", lstConceptos.get(iContC).getIntIdConcepto());
                                 detalle.setFieldInt("NOMD_CANTIDAD", 1);
                                 detalle.setFieldDouble("NOMD_UNITARIO", dblDeduccionTotalFila);
                                 detalle.setFieldDouble("NOMD_IMPORTE_GRAVADO", dblDeduccion);
                                 detalle.setFieldDouble("NOMD_IMPORTE_EXENTO", dblDeduccionExenta);
                                 detalle.setFieldDouble("NOMD_GRAVADO", lstConceptos.get(iContC).getIntGravado());
                                 nomina.getLstConceptos().add(detalle);
                                 //Validamos si es el ISR
                                 if (lstConceptos.get(iContC).getIntIdCategoria() == 2) {
                                    //Asignamos el importe del ISR Retenido
                                    nomina.getDocument().setFieldDouble("NOM_ISR_RETENIDO", dblDeduccion);
                                    nomina.getDocument().setFieldDouble("NOM_RETISR", dblDeduccion);
                                 }
                                 //Buscamos las notas en las filas de notas a detalle

                                 Cell cllConceptoNominaDeta = row.getCell(iContC + 51);
                                 if (cllConceptoNominaDeta != null) {
                                    if (cllConceptoNominaDeta.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                                       detalle.setFieldString("NOMD_NOTAS", cllConceptoNominaDeta.getStringCellValue());
                                    }
                                 }

                              }
                           }

                        }
                     }

                     nomina.getDocument().setFieldDouble("NOM_PERCEPCIONES", dblTotPercepcion);
                     nomina.getDocument().setFieldDouble("NOM_DEDUCCIONES", dblTotDeduccion);
                     nomina.getDocument().setFieldDouble("NOM_DESCUENTO", dblTotDeduccion);
                     double dblTotal = dblTotPercepcion - dblTotDeduccion /*- cllISR_Retenido.getNumericCellValue()*/;//Solo se retiene en las deducciones el ISR
                     nomina.getDocument().setFieldDouble("NOM_PERCEPCION_TOTAL", dblTotal);
                     nomina.doTrx();
                     String strResult = nomina.getStrResultLast();
                     if (!strResult.equals("OK")) {
                        this.strResultLast = strResult;
                        break;
                     } else {
                        if (this.strFolioIni == null) {
                           this.strFolioIni = nomina.getDocument().getFieldString("NOM_FOLIO");
                        }
                        this.strFolioFin = nomina.getDocument().getFieldString("NOM_FOLIO");
                     }
                  }
               }

            }
            myxls.close();
         } catch (IOException ex) {
            log.error(ex.getMessage());
         }
      } else {
         log.debug("No hice nada");
      }
   }

   public boolean ValidaExcel(String strPathExcel, Conexion oConn, VariableSession varSesiones) {
      boolean boolValido = true;
      try {
         InputStream myxls = null;
         myxls = new FileInputStream(strPathExcel);
         HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);

         Cell intClave;
         Cell dateFecha_inicial_de_pago;
         Cell Fecha_final_de_pago;
         Cell ISR_Retenido;
         Cell Numero_de_dias_pagados;

         boolean bolintClave;
         boolean boldateFecha_inicial_de_pago;
         boolean bolFecha_final_de_pago;
         boolean bolISR_Retenido;
         boolean bolNumero_de_dias_pagados;

         Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
         it.next();
         it.next();
         while (it.hasNext()) {
            Row row = it.next();

            intClave = row.getCell(1);
            dateFecha_inicial_de_pago = row.getCell(2);
            Fecha_final_de_pago = row.getCell(3);
            ISR_Retenido = row.getCell(4);
            Numero_de_dias_pagados = row.getCell(6);

            bolintClave = intClave == null;
            boldateFecha_inicial_de_pago = dateFecha_inicial_de_pago == null;
            bolFecha_final_de_pago = Fecha_final_de_pago == null;
            bolISR_Retenido = ISR_Retenido == null;
            bolNumero_de_dias_pagados = Numero_de_dias_pagados == null;

            //Buscamos el numero de empleado
            if (intClave != null) {
               if (intClave.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                  boolean bolExiste = false;
                  String strSql = "select EMP_NUM,EMP_ID,SC_ID from rhh_empleados WHERE EMP_NUM = " + (int) intClave.getNumericCellValue();
                  ResultSet rs;
                  try {
                     rs = oConn.runQuery(strSql, true);
                     while (rs.next()) {
                        bolExiste = true;
                        //Evaluamos si la empresa corresponde con la sesion
                        if (varSesiones.getIntIdEmpresa() != rs.getInt("EMP_ID")) {
                           this.strResultLast = "ERROR: El empleado no corresponde a esta empresa, revise la fila " + (row.getRowNum() + 1);
                           boolValido = false;
                           break;
                        }
                        //Evaluamos si la sucursal o bodega corresponde con la sesion
                        if (varSesiones.getIntSucursalDefault() != rs.getInt("SC_ID")) {
                           this.strResultLast = "ERROR: El empleado no corresponde a esta sucursal, revise la fila " + (row.getRowNum() + 1);
                           boolValido = false;
                           break;
                        }
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     log.error(ex.getMessage());
                  }
                  if (!bolExiste) {
                     boolValido = false;
                     this.strResultLast = "ERROR: El empleado no existe, revise la fila " + (row.getRowNum() + 1);
                     break;

                  }
                  if (!boolValido) {
                     break;
                  }

                  //Validamos que las fechas sean validas
                  if (dateFecha_inicial_de_pago.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                     boolValido = false;
                     this.strResultLast = "ERROR: La fecha inicial es erronea en la fila " + (row.getRowNum() + 1);
                     break;
                  }
                  if (Fecha_final_de_pago.getCellType() != Cell.CELL_TYPE_NUMERIC) {
                     boolValido = false;
                     this.strResultLast = "ERROR: La fecha inicial es erronea en la fila " + (row.getRowNum() + 1);
                     break;
                  }
                  if (bolintClave || boldateFecha_inicial_de_pago || bolFecha_final_de_pago || bolNumero_de_dias_pagados) {
                     boolValido = false;
                     if (bolintClave) {
                        this.strResultLast = "ERROR: Se requiere del campo clave en el renglon " + (row.getRowNum() + 1);
                     }
                     if (boldateFecha_inicial_de_pago) {
                        this.strResultLast = "ERROR: Se requiere del campo fecha inicial de pago en el renglon " + (row.getRowNum() + 1);
                     }
                     if (bolFecha_final_de_pago) {
                        this.strResultLast = "ERROR: Se requiere del campo fecha final de pago en el renglon " + (row.getRowNum() + 1);
                     }
                     if (bolISR_Retenido) {
                        this.strResultLast = "ERROR: Se requiere del campo ISR Retenido en el renglon " + (row.getRowNum() + 1);
                     }
                     if (bolNumero_de_dias_pagados) {
                        this.strResultLast = "ERROR: Se requiere del campo numero de dias pagados en el renglon " + (row.getRowNum() + 1);
                     }

                     break;
                  }
               }

            }

         }

      } catch (IOException ex) {
         log.error(ex.getMessage());
      }
      return boolValido;
   }
}
