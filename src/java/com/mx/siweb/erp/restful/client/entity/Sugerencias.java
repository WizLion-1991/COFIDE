/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.client.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ZeusGalindo
 */
@XmlRootElement
public class Sugerencias {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int id;
   private int fecha;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getFecha() {
      return fecha;
   }

   public void setFecha(int fecha) {
      this.fecha = fecha;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
