/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.entidades;

/**
 *Entidad para indexar las listas de clientes en el calculo de comisiones
 * @author aleph_79
 */
public class ClienteIndice implements Comparable {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   /**
    * Es el id del cliente
    */
   private int intCte = 0;
   /**
    * Es el indice del cliente
    */
   private int intIndice = 0;

   public int getIntCte() {
      return intCte;
   }

   public void setIntCte(int intCte) {
      this.intCte = intCte;
   }

   public int getIntIndice() {
      return intIndice;
   }

   public void setIntIndice(int intIndice) {
      this.intIndice = intIndice;
   }
   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Este metodo ha de devolver un valor negativo si este objeto es menor 
    * que el recibido, 0 si son iguales o un valor positivo si es mayor, 
    * segun el Ûrden natural de los objetos. 
    * @param o
    * @return  Regresa true si es mayor o menor el cliente
    */
   public int compareTo(Object o) {
      ClienteIndice ct2 = (ClienteIndice) o;
      if (this.intCte < ct2.intCte) {
         return -1;
      } else {
         if (this.intCte == ct2.intCte) {
            return 0;
         } else {
            return 1;
         }
      }
   }
   // </editor-fold>
}
