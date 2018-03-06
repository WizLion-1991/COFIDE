/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import comSIWeb.ContextoApt.CIP_Permiso;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase evalua que al sesion sea valida
 *
 * @author ZeusGalindo
 */
public class EvalSesion {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EvalSesion.class.getName());
   private int intIdUser = 0;
   private int intIdPerfil = 0;
   private boolean bolEsCliente;
   /**
    * Regresa el id de usuario
    *
    * @return Es un entero con el id de usuario
    */
   public int getIntIdUser() {
      return intIdUser;
   }

   /**
    * Define el id de usuario
    *
    * @param intIdUser Es un entero con el id de usuario
    */
   public void setIntIdUser(int intIdUser) {
      this.intIdUser = intIdUser;
   }

   /**
    *Regresa el id del perfil
    * @return Regresa un entero con el id del perfil
    */
   public int getIntIdPerfil() {
      return intIdPerfil;
   }

   /**
    *Define el id del perfil
    * @param intIdPerfil Define el id del perfil
    */
   public void setIntIdPerfil(int intIdPerfil) {
      this.intIdPerfil = intIdPerfil;
   }

   /**
    *Indica que el usuario logueado es un cliente
    * @return boolean
    */
   public boolean isBolEsCliente() {
      return bolEsCliente;
   }

   /**
    *Indica que el usuario logueado es un cliente
    * @param bolEsCliente boolean
    */
   public void setBolEsCliente(boolean bolEsCliente) {
      this.bolEsCliente = bolEsCliente;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public EvalSesion() {
      this.bolEsCliente = false;

   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Evalua si el codigo es de un usuario autenticado y valido
    *
    * @param strCodigo Es el codigo del usuario logueado
    * @param oConn Es la conexion
    * @return Regresa el true si tiene acceso
    */
   public boolean evaluaSesion(String strCodigo, Conexion oConn) {
      boolean bolSesionValida = false;
      String strSql = "select CT_ID,CT_LASTSESSIONID,CT_IS_LOGGED from vta_cliente where CT_LASTSESSIONID = '" + strCodigo + "' and CT_IS_LOGGED = 1";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
             
            this.intIdUser = rs.getInt("CT_ID");
            bolSesionValida = true;
            this.bolEsCliente = true;

         }
         rs.close();
         //Sino es valido como cliente probamos cono usuarop
         if (!bolSesionValida) {
            strSql = "select id_usuarios,LASTSESSIONID,IS_LOGGED,PERF_ID from usuarios where LASTSESSIONID = '" + strCodigo + "' and IS_LOGGED = 1";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               this.intIdUser = rs.getInt("id_usuarios");
               this.intIdPerfil = rs.getInt("PERF_ID");
               bolSesionValida = true;
            }
            rs.close();
         }
      } catch (SQLException ex) {
         log.error(" " + ex.getMessage());
      }

      return bolSesionValida;
   }
   
   /**
    *Evalua si el usuario tiene acceso al permiso indicado
    * @param intIdPermiso Es el id del permiso
    * @return Regresa true si tiene acceso al menu
    */
   public boolean evaluaPermiso(int intIdPermiso,Conexion oConn){
      boolean bolSitieneAcceso  = CIP_Permiso.ValidaPermiso(intIdPermiso, this.intIdPerfil, oConn);
      return bolSitieneAcceso;
   }
   // </editor-fold>
}
