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
public class cat_documentacion extends TableMaster{
    
    public cat_documentacion(){
    super("cat_documentacion", "DMN_ID", "", "");
        this.Fields.put("DMN_ID", new Integer(0));  
        this.Fields.put("DMN_NOMBRE", "");
        this.Fields.put("DMN_PATHIMGFORM", "");
        this.Fields.put("CTO_ID", new Integer(0));
    }
    
}
