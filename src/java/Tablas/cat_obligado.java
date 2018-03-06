/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author SIWEB
 */
public class cat_obligado extends TableMaster {
    /**
     * Constructor
     */
    public cat_obligado(){
      super("cat_obligado", "OB_ID", "", "");
      this.Fields.put("OB_ID", new Integer(0));   
      this.Fields.put("OB_RAZONSOCIAL", "");
      this.Fields.put("OB_RFC", "");
      this.Fields.put("OB_CALLE", "");
      this.Fields.put("OB_NUMERO", "");
      this.Fields.put("OB_NUMEROIN", "");
      this.Fields.put("OB_COLONIA", "");
      this.Fields.put("OB_LOCALIDAD", "");
      this.Fields.put("OB_MUNICIPIO", "");
      this.Fields.put("OB_ESTADO", "");
      this.Fields.put("OB_CP", "");
      this.Fields.put("OB_TELEFONO1", "");
      this.Fields.put("OB_TELEFONO2", "");
      this.Fields.put("OB_CONTACTO1", "");
      this.Fields.put("OB_CONTACTO2", "");
      this.Fields.put("OB_EMAIL1", "");
      this.Fields.put("OB_EMAIL2", "");   
      this.Fields.put("CTO_ID", new Integer(0));   
      this.Fields.put("OB_EDAD", new Integer(0)); 
      this.Fields.put("N_ID", new Integer(0)); 
      this.Fields.put("EC_ID", new Integer(0)); 
      this.Fields.put("RM_ID", new Integer(0)); 
      this.Fields.put("OB_NCONYUGUE", "");
      this.Fields.put("N_CID", new Integer(0)); 
      this.Fields.put("OB_CEDAD", new Integer(0)); 
    }
}






