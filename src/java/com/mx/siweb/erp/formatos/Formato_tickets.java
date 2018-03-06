/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.formatos;

import Tablas.formatos_tickets;
import Tablas.formatos_tickets_sql;
import comSIWeb.Operaciones.Conexion;
import java.util.ArrayList;

/**
 * Genera el txt del formato de tickets
 *
 * @author ZeusGalindo
 */
public class Formato_tickets {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private String strResultLast;
   private String strTxtHead;
   private String strTxtBody;
   private String strTxtFoot;
   private formatos_tickets formatos;
   private ArrayList<formatos_tickets_sql> lstDeta;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public String getStrResultLast() {
      return strResultLast;
   }

   public void setStrResultLast(String strResultLast) {
      this.strResultLast = strResultLast;
   }

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Formato_tickets(Conexion oConn) {
      this.oConn = oConn;

   }
   // </editor-fold>


   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public String DoFormato(String strAbrv, int intId) {
      String strFormato = null;
      this.strResultLast = "OK";
      
      //Ejecutamos los querys
      Execsql(strAbrv, intId);
      
      //Realizamos el reemplazo de los campos
      ReplaceAll();
      
      //Juntamos y formamos el ticket
      strFormato = JoinAll();
      
      return strFormato;
   }
   
   private void Execsql(String strAbrv, int intId){
      
   }
   
   private void ReplaceAll(){
      //Iteramos por instrucciones sql
      
      //Si es encabezado o pie
      
      //Si es cuerpo
      
   }
   
   private String JoinAll(){
      String strJoinAll = null;
      strJoinAll = this.strTxtHead + this.strTxtBody + this.strTxtFoot;
      return strJoinAll;
   }
   // </editor-fold>
}
