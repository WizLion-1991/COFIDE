package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla Usuariosclientes
 */
public class Usuariosclientes extends TableMaster {

   public Usuariosclientes() {
      super("usuariosclientes", "USCT_ID", "", "");
      this.Fields.put("USCT_ID", new Integer(0));
      this.Fields.put("US_ID", new Integer(0));
      this.Fields.put("CT_ID", new Integer(0));
   }
}
