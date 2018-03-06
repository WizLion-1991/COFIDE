package com.mx.siweb.mlm.entidades;

/**
 *Representa un nodo de red
 * @author zeus
 */
public class NodoRed {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intNodoId = 0;
   private int intDeep = 0;
   /**
    * Regresa el id del nodo de red
    * @return Regresa el id
    */
   public int getIntNodoId() {
      return intNodoId;
   }

   /**
    * Define el id del nodo de red
    * @param intNodoId Es el id
    */
   public void setIntNodoId(int intNodoId) {
      this.intNodoId = intNodoId;
   }

   /**
    * Regresa la profundidad del nodo en la red
    * @return Es la profundidad del nodo
    */
   public int getIntDeep() {
      return intDeep;
   }

   /**
    * Define la profundidad del nodo en la red
    * @param intDeep ES la profundidad del nodo
    */
   public void setIntDeep(int intDeep) {
      this.intDeep = intDeep;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

   /**
    * Constructor default
    */
   public NodoRed() {
   }

   /**
    * Constructor con parametros
    * @param intNodoId Es el id del nodo
    * @param intDeep Es el id de la profundidad
    */
   public NodoRed(int intNodoId,int intDeep){
      this.intNodoId = intNodoId;
      this.intDeep = intDeep;
   }

}
