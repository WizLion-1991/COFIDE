/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ERP;

/**
 *Esta interfaz define el esqueleto principal de los proceso a usarse
 * @author zeus
 */
public interface ProcesoInterfaz {

   /**
    * Inicializa la transaccion
   */
   public void Init();
   /**
    * Realiza la transaccion
    */
   public void doTrx();
   /**
    * Anula la transaccion
    */
   public void doTrxAnul();
   /**
    * Revive la transaccion
    */
   public void doTrxRevive();
   /**
    * Ajusta el saldo de la transaccion
    */
   public void doTrxSaldo();
   /**
    * Modifica la transaccion
    */
   public void doTrxMod();
}
