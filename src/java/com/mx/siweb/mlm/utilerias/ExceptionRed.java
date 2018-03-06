package com.mx.siweb.mlm.utilerias;

/**
 *Error personalizado
 * @author zeus
 */
public class ExceptionRed extends Exception {
   private String strError;
   /**
    * Constructor default
    */
   public ExceptionRed() {
      super();             // call superclass constructor
      strError = "unknown";
   }

   /**
    * Constructor que recibe un mensaje especial
    * @param err Msg Error
    */
   public ExceptionRed(String err) {
      super(err);     // call super class constructor
      strError = err;  // save message
   }

   /**
    * Metodo publico que se llama por el try para enviar el error
    * @return Es el error sucedido
    */
   public String getError() {
      return strError;
   }
}
