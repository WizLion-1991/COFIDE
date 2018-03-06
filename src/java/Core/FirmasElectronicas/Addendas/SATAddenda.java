
package Core.FirmasElectronicas.Addendas;

import Core.FirmasElectronicas.SAT22.Comprobante;
import Core.FirmasElectronicas.SAT22.Comprobante.Addenda;
import comSIWeb.Operaciones.Conexion;

/**
 *Clase base para generar la addenda
 * @author zeus
 */
public abstract class SATAddenda {
   /**
    * Llena la addenda dependiendo del tipo solicitado
    * @param addenda Es el objeto de addenda
    * @param objComp Es el comprobante
    * @param strPath Es el path
    * @param intTransaccion Es el id de la operacion
    * @param oConn  Es la conexion
    */
   public void FillAddenda(Addenda addenda, Comprobante objComp,
           String strPath, int intTransaccion,Conexion oConn){
   }
   /**
    * Llena la addenda dependiendo del tipo solicitado
    * @param addenda Es el objeto de addenda
    * @param objComp Es el comprobante
    * @param strPath Es el path
    * @param intTransaccion Es el id de la operacion
    * @param oConn  Es la conexion
    */
   public void FillAddenda(Core.FirmasElectronicas.SAT3_2.Comprobante.Addenda addenda, Core.FirmasElectronicas.SAT3_2.Comprobante objComp,
           String strPath, int intTransaccion,Conexion oConn){
   }
   /**
    * Añade los namespace necesarios para la addenda
    * @param strPath Es el path
    * @param intTransaccion Es el id de la operacion
    * @param oConn  Es la conexion
    */
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion,Conexion oConn){
   }
}
