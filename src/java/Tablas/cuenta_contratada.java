package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 * Represetna la tabla CuentaContratada
 */
public class cuenta_contratada extends TableMaster {

   public cuenta_contratada() {
      super("cuenta_contratada", "ctam_id", "", "");
      this.Fields.put("ctam_id", new Integer(0));
      this.Fields.put("nombre", "");
      this.Fields.put("rfc", "");
      this.Fields.put("direccion1", "");
      this.Fields.put("direccion2", "");
      this.Fields.put("telefonooficina", "");
      this.Fields.put("telefonocasa", "");
      this.Fields.put("telefonomovil", "");
      this.Fields.put("email", "");
      this.Fields.put("num_empresas", new Integer(0));
      this.Fields.put("vigencia", "");
      this.Fields.put("paquete", "");
      this.Fields.put("desactivada", new Integer(0));
      this.Fields.put("desactiva_user", new Integer(0));
      this.Fields.put("desactiva_fecha", "");
      this.Fields.put("desactiva_hora", "");
      this.Fields.put("smtp_server", "");
      this.Fields.put("smtp_user", "");
      this.Fields.put("smtp_pass", "");
      this.Fields.put("smtp_port", "");
      this.Fields.put("smtp_usaTLS", new Integer(0));
      this.Fields.put("smtp_usaSTLS", new Integer(0));
      this.Fields.put("PRECIOCONIMP", new Integer(0));
   }
}
