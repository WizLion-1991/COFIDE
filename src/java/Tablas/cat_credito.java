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
public class cat_credito extends TableMaster{
    /**
     * Constructor
     */
    public cat_credito(){
    super("cat_credito", "CTO_ID", "", "");
      this.Fields.put("CTO_ID", new Integer(0));  
      this.Fields.put("CTO_NREFERENCIA", "");
      this.Fields.put("SS_ID", new Integer(0));
      this.Fields.put("F_ID", new Integer(0));
      this.Fields.put("CTO_FECHA", "");
      this.Fields.put("CTO_NCUENTA", "");
      this.Fields.put("TO_ID", new Integer(0));
      this.Fields.put("IM_ID", new Integer(0));
      this.Fields.put("MTO_ID", new Integer(0));
      this.Fields.put("M_ID", new Integer(0));
      this.Fields.put("SPC_ID", new Integer(0));
      this.Fields.put("CTO_HORA", "");
      this.Fields.put("CTO_NIC", "");
      //this.Fields.put("CTO_PCREDITO", new Integer(0));
      this.Fields.put("CT0_FAPERTURA", "");
      this.Fields.put("CT0_FVENCIMIENTO", "");
      this.Fields.put("CT0_PPPERIODO", new Integer(0));
      //this.Fields.put("CTO_SALDO", new Integer(0));   
      this.Fields.put("CT_NOM", "");
      this.Fields.put("F_NOM", "");
      this.Fields.put("CT_ID", "");
      this.Fields.put("TC_ID", new Integer(0));
      this.Fields.put("CTO_AUTORIZADO", new Integer(0));
      this.Fields.put("CTO_FECHA_AUTORIZADO", "");
      this.Fields.put("ID_USUARIO_AUTORIZO", new Integer(0));
      this.setBolGetAutonumeric(true);
    }
    
    
}
