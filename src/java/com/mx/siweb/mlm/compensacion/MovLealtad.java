/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion;

import ERP.ProcesoInterfaz;
import ERP.ProcesoMaster;
import Tablas.Mlm_ptos_lealtad_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Representa los cargos y abonos que se realizan con los puntos de lealtad
 *
 * @author ZeusGalindo
 */
public class MovLealtad extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Mlm_ptos_lealtad_deta movs;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MovLealtad.class.getName());

   public Mlm_ptos_lealtad_deta getMovs() {
      return movs;
   }

   public void setMovs(Mlm_ptos_lealtad_deta movs) {
      this.movs = movs;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public MovLealtad(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void doTrx() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
