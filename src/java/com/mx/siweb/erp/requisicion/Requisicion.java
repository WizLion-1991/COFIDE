/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.requisicion;

import ERP.ProcesoInterfaz;
import ERP.ProcesoMaster;
import Tablas.vta_requisicion;
import Tablas.vta_requisicion_deta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Genera las requisiciones de compra
 *
 * @author ZeusSIWEB
 */
public class Requisicion extends ProcesoMaster implements ProcesoInterfaz {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected vta_requisicion document;//representa el documento de bancos
   protected ArrayList<vta_requisicion_deta> lstMovs;//representa el detalle del movimiento de bancos
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Requisicion.class.getName());

   public vta_requisicion getDocument() {
      return document;
   }

   public void setDocument(vta_requisicion document) {
      this.document = document;
   }

   public ArrayList<vta_requisicion_deta> getLstMovs() {
      return lstMovs;
   }

   public void setLstMovs(ArrayList<vta_requisicion_deta> lstMovs) {
      this.lstMovs = lstMovs;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Requisicion(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.document = new vta_requisicion();
      this.lstMovs = new ArrayList<vta_requisicion_deta>();

   }

   public Requisicion(Conexion oConn, VariableSession varSesiones) {
      super(oConn, varSesiones);
      this.document = new vta_requisicion();
      this.lstMovs = new ArrayList<vta_requisicion_deta>();

   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      this.document.setFieldInt("REQ_USUARIO_SOLICITO", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.document.getFieldInt("REQ_ID") != 0) {
         this.document.ObtenDatos(this.document.getFieldInt("REQ_ID"), oConn);
      }
   }

   @Override
   public void doTrx() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validaciones previas al guardado">
      this.document.setFieldString("REQ_FECHA", this.fecha.getFechaActual());
      this.document.setFieldString("REQ_HORA", this.fecha.getHoraActual());
      //Validamos que todos los campos basico se encuentren
      // </editor-fold>
      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>
         
        // <editor-fold defaultstate="collapsed" desc="Guardamos documento principal">
         this.document.setBolGetAutonumeric(true);
         String strRes1 = this.document.Agrega(oConn);
         if (strRes1.equals("OK")) {
            // <editor-fold defaultstate="collapsed" desc="Guardamos partidas">
            int intId = Integer.valueOf(this.document.getValorKey());
            Iterator<vta_requisicion_deta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_requisicion_deta deta = it.next();
               deta.setFieldInt("REQ_ID", intId);
               String strRes2 = deta.Agrega(oConn);
               //Validamos si todo fue satisfactorio
               if (!strRes2.equals("OK")) {
                  this.strResultLast = strRes2;
               }
            }
            this.saveBitacora("REQUISICION", "NUEVA", intId);
            // </editor-fold>"
         }
         // </editor-fold>
         
         // <editor-fold defaultstate="collapsed" desc="Terminamos la operacion">
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
         // </editor-fold>
      }
   }

   @Override
   public void doTrxAnul() {
this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.document.getFieldInt("REQ_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }

      // </editor-fold>

      //si ya no se puede anular la operacion
      
      //Validamos que halla pasado las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.document.getFieldInt("REQ_ANULADO") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Actualizamos documento principal">
            this.document.setFieldInt("REQ_USUARIOSANUL", this.varSesiones.getIntNoUser());
            this.document.setFieldInt("REQ_ANULADO", 1);
            this.document.setFieldString("REQ_HORAANUL", this.fecha.getHoraActual());
            this.document.setFieldString("REQ_FECHANUL", this.fecha.getFechaActual());
            String strResp1 = this.document.Modifica(oConn);
            // </editor-fold>
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            }
          

            this.saveBitacora("REQUISICION", "ANULAR", this.document.getFieldInt("REQ_ID"));
            // <editor-fold defaultstate="collapsed" desc="Terminamos la operacion">
            if (this.bolTransaccionalidad) {
               if (this.strResultLast.equals("OK")) {
                  this.oConn.runQueryLMD("COMMIT");
               } else {
                  this.oConn.runQueryLMD("ROLLBACK");
               }
            }
            // </editor-fold>
         } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
         }
         // </editor-fold>
      }
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
