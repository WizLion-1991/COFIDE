package Tablas;
// Generated 14/02/2010 11:10:21 AM by Hibernate Tools 3.2.1.GA

import comSIWeb.Operaciones.TableMaster;

/**
 * Representa la tabla Proveedor
 */
public class Proveedor extends TableMaster {

   public Proveedor() {
      super("proveedor", "idproveedor", "", "");
      this.Fields.put("idproveedor", new Integer(0));
      this.Fields.put("RazonSocial", "");
      this.Fields.put("RFC", "");
      this.Fields.put("Direccion1", "");
      this.Fields.put("Direccion2", "");
      this.Fields.put("Cp", "");
      this.Fields.put("Colonia", "");
      this.Fields.put("Delegacion", "");
      this.Fields.put("Estado", "");
      this.Fields.put("Contacto1", "");
      this.Fields.put("Contacto2", "");
      this.Fields.put("Telefono", "");
      this.Fields.put("TelefonoCelular", "");
      this.Fields.put("idcliente", new Integer(0));
   }
}
