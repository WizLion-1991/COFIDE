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
public class cat_vencimiento extends TableMaster{
    
    public cat_vencimiento(){
    super("cat_vencimiento", "V_ID", "", "");
      this.Fields.put("V_ID", new Integer(0));
      this.Fields.put("V_MOVIMIENTO", new Integer(0));
      this.Fields.put("V_VENCIMIENTO", "");
      this.Fields.put("CT_ID", new Integer(0));
      this.Fields.put("CTO_ID", new Integer(0));
      this.Fields.put("V_IMPORTE", new Integer(0));
      this.Fields.put("V_IVA", new Integer(0));
      this.Fields.put("V_CAPITAL", new Integer(0));
      this.Fields.put("V_INTERES", new Integer(0));
    }    
    
}
