/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.wenow;

import com.mx.siweb.mlm.utilerias.Redes;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Realiza la lectura de un archivo en excel con el que activa a los clientes
 *
 * @author ZeusGalindo
 */
public class CobrosMasivosXLS {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CobrosMasivosXLS.class.getName());
   private Conexion oConn;
   private String strPathExcel;
   private VariableSession varSesiones;
   private int intContadorItems;
   private int intUplineInicial;
   private int intUplineTemporal;
   private int intUplineTemporalAfiliado;
   private int intUplineTemporalGlobal;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public String getStrPathExcel() {
      return strPathExcel;
   }

   public void setStrPathExcel(String strPathExcel) {
      this.strPathExcel = strPathExcel;
   }

   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

   public int getIntContadorItems() {
      return intContadorItems;
   }

   public void setIntContadorItems(int intContadorItems) {
      this.intContadorItems = intContadorItems;
   }

   public int getIntUplineInicial() {
      return intUplineInicial;
   }

   /**
    * Define el upline inicial
    *
    * @param intUplineInicial Es el nodo
    */
   public void setIntUplineInicial(int intUplineInicial) {
      this.intUplineInicial = intUplineInicial;
   }

   public int getIntUplineTemporal() {
      return intUplineTemporal;
   }

   /**
    * Define el id del upline temporal donde se alojan los nodos no confirmados
    *
    * @param intUplineTemporal Es el id del nodo
    */
   public void setIntUplineTemporal(int intUplineTemporal) {
      this.intUplineTemporal = intUplineTemporal;
   }

   public int getIntUplineTemporalAfiliado() {
      return intUplineTemporalAfiliado;
   }

   public void setIntUplineTemporalAfiliado(int intUplineTemporalAfiliado) {
      this.intUplineTemporalAfiliado = intUplineTemporalAfiliado;
   }

   public int getIntUplineTemporalGlobal() {
      return intUplineTemporalGlobal;
   }

   public void setIntUplineTemporalGlobal(int intUplineTemporalGlobal) {
      this.intUplineTemporalGlobal = intUplineTemporalGlobal;
   }



   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Lee el archivo de excel y comienza activar a los distribuidores
    *
    * @return
    */
   public String processXLS() {
      String strRes = "OK";
      //Armamos primero la red
      Redes red = new Redes();

      boolean bolArmo = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
      boolean bolArmoA = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE_A", 1, "CTA_", "", " ORDER BY CT_ID", false, true, oConn);
      boolean bolArmoG = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE_G", 1, "CTG_", "", " ORDER BY CT_ID", false, true, oConn);
      if (bolArmo) {
         strRes = "ERROR:" + red.getStrError();
      } else {
         //Leemos el archivo
         InputStream myxls = null;
         ActivaBinario activador = new ActivaBinario(this.oConn);
         activador.setIntUplineInicial(this.intUplineInicial);
         activador.setIntUplineTemporal(this.intUplineTemporal);
         activador.setIntUplineTemporalAfiliado(intUplineTemporalAfiliado);
         activador.setIntUplineTemporalGlobal(intUplineTemporalGlobal);

         try {
            myxls = new FileInputStream(this.strPathExcel);
            HSSFWorkbook archivoExcel = new HSSFWorkbook(myxls);
            Iterator<Row> it = archivoExcel.getSheetAt(0).rowIterator();
            while (it.hasNext()) {
               Row row = it.next();
               Cell cellDistribuidor = row.getCell(0);
               Cell cellTipoRed = row.getCell(1);
               //Solo procesamos los que no son nulos
               if (cellDistribuidor != null) {
                  intContadorItems++;
                  int intIdDis = 0;
                  if (cellDistribuidor.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                     intIdDis = (int) cellDistribuidor.getNumericCellValue();
                  } else {
                     String strValor = cellDistribuidor.getStringCellValue();
                     try {
                        intIdDis = Integer.valueOf(strValor);
                     } catch (NumberFormatException ex) {
                        log.error(ex.getMessage());
                     }
                  }
                  if (intIdDis != 0) {
                     log.debug("intIdDis:" + intIdDis);
                     //Verificamos el tipo de red
                     String strTipoRed = "T";
                     if (cellTipoRed != null) {
                        strTipoRed = cellTipoRed.getStringCellValue();
                     }
                     if (strTipoRed.equals("T")) {
                        String strRes1 = activador.activarDistribuidor(intIdDis);
                        if (!strRes1.equals("OK")) {
                           strRes += "ERROR:" + strRes1 + "\n";
                        }
                     } else {
                        if (strTipoRed.equals("A")) {
                           String strRes1 = activador.activarDistribuidorAfiliado(intIdDis, oConn);
                           if (!strRes1.equals("OK")) {
                              strRes += "ERROR:" + strRes1 + "\n";
                           }
                        } else {
                           if (strTipoRed.equals("G")) {
                              String strRes1 = activador.activarDistribuidorGlobal(intIdDis, oConn);
                              if (!strRes1.equals("OK")) {
                                 strRes += "ERROR:" + strRes1 + "\n";
                              }
                           }
                        }
                     }

//                     if(intContadorItems == 14)break;
                  }
               }
            }

         } catch (IOException ex) {
            strRes = "ERROR:" + ex.getMessage();
         }
         log.debug("Numero de items procesados " + intContadorItems);
         red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
      }

      return strRes;
   }
   // </editor-fold>
}
