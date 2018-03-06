/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion;

import ERP.ProcesoInterfaz;
import ERP.ProcesoMaster;
import Tablas.Mlm_ptos_lealtad;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Representa las operaciones de los puntos de lealtad
 *
 * @author ZeusGalindo
 */
public class Lealtad extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Mlm_ptos_lealtad docs;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MovLealtad.class.getName());

   public Mlm_ptos_lealtad getDocs() {
      return docs;
   }

   public void setDocs(Mlm_ptos_lealtad docs) {
      this.docs = docs;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Lealtad(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {

   }

   @Override
   public void doTrx() {
      // <editor-fold defaultstate="collapsed" desc="Evaluamos campos obligatorios">
      
      // </editor-fold>
   }

   @Override
   public void doTrxAnul() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxRevive() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrxMod() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   // </editor-fold>
}
