/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP.BusinessEntities;

/**
 *
 * @author siweb
 */
public class PercepcionesDeduccionesE {

   int intIdPercDedu = 0;
   int intIdTipo = 0;

   public PercepcionesDeduccionesE(int intIdPercDedu, int intIdTipo) {
      this.intIdPercDedu = intIdPercDedu;
      this.intIdTipo = intIdTipo;
   }

   public int getIntIdPercDedu() {
      return intIdPercDedu;
   }

   public void setIntIdPercDedu(int intIdPercDedu) {
      this.intIdPercDedu = intIdPercDedu;
   }

   public int getIntIdTipo() {
      return intIdTipo;
   }

   public void setIntIdTipo(int intIdTipo) {
      this.intIdTipo = intIdTipo;
   }

}
