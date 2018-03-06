/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.siweb;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class CargarXLSPAC {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CargarXLSPAC.class.getName());

   String rfc_ID = "";
   String CTOA_ID = "";
   String strRFC = "";
   String strTipo = "";
   double strConsumo;
   String strNoEncontrados = "";
   private int intEMP_ID;
   private int intSC_ID;
   private VariableSession varSesiones;
   protected Fechas fecha;
   protected Conexion oConn;
   protected String strResultLast;
   ArrayList<String> ArrayNoEncontrados = new ArrayList<String>();
   ArrayList<String> ArrayYaRegistrados = new ArrayList<String>();
   ArrayList<String> ArrayConDiferencia = new ArrayList<String>();
   ArrayList<String> ArrayCorrectos = new ArrayList<String>();

   public ArrayList<String> getYaRegistrados() {
      return ArrayYaRegistrados;
   }

   public void setYaRegistrados(ArrayList<String> ArrayYaRegistrados) {
      this.ArrayYaRegistrados = ArrayYaRegistrados;
   }

   public ArrayList<String> getCorrectos() {
      return ArrayCorrectos;
   }

   public void setCorrectos(ArrayList<String> ArrayCorrectos) {
      this.ArrayCorrectos = ArrayCorrectos;
   }

   public ArrayList<String> getNoEncontrados() {
      return ArrayNoEncontrados;
   }

   public void setNoEncontrados(ArrayList<String> ArrayNoEncontrados) {
      this.ArrayNoEncontrados = ArrayNoEncontrados;
   }

   public ArrayList<String> getConDiferencia() {
      return ArrayConDiferencia;
   }

   public void setConDiferencia(ArrayList<String> ArrayConDiferencia) {
      this.ArrayConDiferencia = ArrayConDiferencia;
   }

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
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   public void AbrirXLS(Conexion oConn, String strPathXLS, VariableSession varSesiones) throws SQLException {
      this.varSesiones = varSesiones;
      FileInputStream myxls = null;
      try {
         myxls = new FileInputStream(strPathXLS);
         HSSFWorkbook wb = new HSSFWorkbook(myxls);
         this.LlenaPack(oConn, wb.getSheetAt(0));
      } catch (IOException ex) {
         Logger.getLogger(CargarXLSPAC.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            myxls.close();
         } catch (IOException ex) {
            Logger.getLogger(CargarXLSPAC.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public void LlenaPack(Conexion oConn, HSSFSheet sheet) throws SQLException {

      Row rowFecha = sheet.getRow(3);
      String strFecha = rowFecha.getCell(2).getStringCellValue();
      String mes = strFecha.substring(3, 5);
      String año = strFecha.substring(6, 10);
      Iterator<Row> it = sheet.rowIterator();
      while (it.hasNext()) {
         Row row = it.next();
         Cell cellRFC = row.getCell(1);
         Cell cellTipo = row.getCell(2);
         Cell cellConsumo = row.getCell(4);
         if (row.getRowNum() > 6) {
            try {
               strRFC = cellRFC.getStringCellValue();
               if (!strRFC.contains("Total en Unidades")) {
                  strTipo = cellTipo.getStringCellValue();
                  strConsumo = cellConsumo.getNumericCellValue();
                  String strQueryID = "select * from vta_rfc_contratados where RFC_CONT_RFC = '" + strRFC + "';";
                  ResultSet rs = oConn.runQuery(strQueryID);
                  while (rs.next()) {
                     rfc_ID = rs.getString("RFC_CONT_ID");
                     CTOA_ID = rs.getString("CTOA_ID");
                  }
                  //Valida que exista el RFC en la tabla vta_RFC_contratado
                  if (rfc_ID.equals("")) {
                     String strNoEncontrado = "El RFC " + strRFC + " no esta registrado";
                     ArrayNoEncontrados.add(strNoEncontrado);
                     setNoEncontrados(ArrayNoEncontrados);
                  }//Si el RFC ya esta registrado se busca que no este duplicado 
                  else {
                     String strQueryDuplicado = "select vtu_rfc, vtu_anio, vtu_mes, vtu_tipo_servicio,"
                             + "vtu_consumo from vta_timbres_usados where vtu_rfc ='" + strRFC + "'"
                             + " and vtu_anio = " + año + " and vtu_mes = " + mes + " and vtu_tipo_servicio = '" + strTipo + "';";

                     ResultSet rsDuplicado = oConn.runQuery(strQueryDuplicado);
                     if (rsDuplicado.next()) {
                        double consumo = rsDuplicado.getDouble("vtu_consumo");
                        if (consumo == strConsumo) {
                           String strDuplicado = "Se repite este RFC: " + strRFC + " " + strTipo + " " + strConsumo;
                           ArrayYaRegistrados.add(strDuplicado);
                           setYaRegistrados(ArrayYaRegistrados);
                        } else {
                           String strDuplicadoDif = "Se repite este RFC " + strRFC + " con diferencia en la cantidad de consumo";
                           ArrayConDiferencia.add(strDuplicadoDif);
                           setConDiferencia(ArrayConDiferencia);
                           saveBitacora("CARGAR XLS", "ARCHIVO MODIFICADO");
                        }
                     }//Fin IF rsDuplicado
                     else {
                        String strQuery = "insert into vta_timbres_usados(vtu_rfc,vtu_tipo_servicio,vtu_consumo,vtu_anio,vtu_mes,vtu_CTOA_ID,vtu_RFC_ID)values \n"
                                + "('" + strRFC + "','" + strTipo + "'," + strConsumo + "," + año + "," + mes + "," + CTOA_ID + "," + rfc_ID + ");";
                        oConn.runQueryLMD(strQuery);
                        String rfcCorrectos = "El RFC " + strRFC + " fue agregado correctamente";
                        ArrayCorrectos.add(rfcCorrectos);
                        setCorrectos(ArrayCorrectos);
                     }//Fin INSERT

                  }//FIN ELSE
               }//Fin IF CONTAINS
               else {
                  saveBitacora("CARGAR XLS", "SE CARGO EL ARCHIVO CORRECTAMENTE");
                  break;
               }
            } catch (Exception e) {
               log.debug(e.getLocalizedMessage() + "Variable NULL");
            }//Fin catch
         }//Termina de recorrer los RFC
         strRFC = "";
         strTipo = "";
         strConsumo = 0;
      }//Fin WHILE Iterator

   }//Fin método llena Pack

   protected void saveBitacora(String Modulo, String Asunto) throws SQLException {
      String insert = "INSERT INTO bitacorausers (BTU_FECHA, BTU_HORA, BTU_IDUSER,"
              + " BTU_NOMUSER, BTU_NOMMOD, BTU_NOMACTION, BTU_IDOPER) "
              + "VALUES ('" + fecha.getFechaActual() + "', '" + fecha.getHoraActual() + "', '1', '" + varSesiones.getStrUser() + "',"
              + " '" + Modulo + "', '" + Asunto + "', '1');";
      oConn.runQuery(insert);

   }

}//FIN CLASS CargarXLSPAC
