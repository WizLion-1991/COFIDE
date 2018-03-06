/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.pedidos;

import com.mx.siweb.erp.pedidos.entidades.ItemPedir;
import comSIWeb.Operaciones.Conexion;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase procesa y genera los pedidos internos y las requisiciones de
 * compra
 *
 * @author ZeusSIWEB
 */
public class RequisionPedidoInterno {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private ArrayList<ItemPedir> lstItemsReq;
   private ArrayList<ItemPedir> lstItemsPediInterno;

   public ArrayList<ItemPedir> getLstItemsReq() {
      return lstItemsReq;
   }

   public void setLstItemsReq(ArrayList<ItemPedir> lstItemsReq) {
      this.lstItemsReq = lstItemsReq;
   }

   public ArrayList<ItemPedir> getLstItemsPediInterno() {
      return lstItemsPediInterno;
   }

   public void setLstItemsPediInterno(ArrayList<ItemPedir> lstItemsPediInterno) {
      this.lstItemsPediInterno = lstItemsPediInterno;
   }

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(RequisionPedidoInterno.class.getName());

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public RequisionPedidoInterno() {
      lstItemsReq = new ArrayList<ItemPedir>();
      lstItemsPediInterno = new ArrayList<ItemPedir>();
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void doIdentificaReqCompra(String strCodigo, double dblCompra, int intBodega) {
      ItemPedir item =  new ItemPedir();
      item.setStrCodigo(strCodigo);
      item.setDblCantidad(dblCompra);
      item.setIntBodega(intBodega);
      this.lstItemsReq.add(item);
   }

   public void doIdentificaPedidoInterno(String strCodigo, String strTokenizer) {

   }

   public String doGenerarPedidoInternoReq(Conexion oConn) {
      String strRes = "OK";
      doOrdenaArrays();
      
      return strRes;
   }
   
   private void doOrdenaArrays(){
      Collections.sort(lstItemsReq);
      Collections.sort(lstItemsPediInterno);
   }
   // </editor-fold>

}
