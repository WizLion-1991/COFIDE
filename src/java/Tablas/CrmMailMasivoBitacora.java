/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *Representa una bitacora de mail masivo
 * @author ZeusSIWEB
 */
public class CrmMailMasivoBitacora extends TableMaster {

   public CrmMailMasivoBitacora() {
      super("crm_mail_masivo_bitacora", "BM_ID", "", "");
      this.Fields.put("BM_ID", 0);
      this.Fields.put("BM_FECHA", "");
      this.Fields.put("BM_HORA", "");
      this.Fields.put("BM_USUARIO", 0);
      this.Fields.put("BM_TEMPLATE", "");
      this.Fields.put("BM_ENVIO", 0);
      this.Fields.put("BM_RESPUESTA", "");
      this.Fields.put("BM_MAIL", "");
   }
}
