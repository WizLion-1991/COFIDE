/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.wenow;

import com.mx.siweb.mlm.compensacion.Comision_MLM;
import comSIWeb.Operaciones.Conexion;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author ZeusGalindo
 */
public class CalculaComisionSemanal extends Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(com.mx.siweb.mlm.compensacion.wenow.CalculaComisionSemanal.class.getName());

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public CalculaComisionSemanal(Conexion oConn, int intPeriodo, boolean EsCorridaDefinitiva) {
      super(oConn, intPeriodo, EsCorridaDefinitiva);
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void doFase1() {
      super.doFase1();
      //Si todo es ok limpiamos banderas
      if (this.strResultLast.equals("OK")) {
         String strSQL = "UPDATE vta_cliente SET ";
         strSQL += "CT_PPUNTOS    =  0,";
         strSQL += "CT_GPUNTOS    =  0,";
         strSQL += "CT_PNEGOCIO   =  0,";
         strSQL += "CT_GNEGOCIO   =  0,";
         strSQL += "CT_COMISION    =  0,";
         strSQL += "CT_NIVELRED    =  0,";
         strSQL += "CT_CONTEO_HIJOS    =  0,";
         strSQL += "CT_GENERACION1  =  0,";
         strSQL += "CT_GENERACION2  =  0,";
         strSQL += "CT_GENERACION3  =  0 ";
         this.oConn.runQueryLMD(strSQL);
      }
   }

   @Override
   public void doFaseBonos() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doFaseCierre() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   // </editor-fold>
}
