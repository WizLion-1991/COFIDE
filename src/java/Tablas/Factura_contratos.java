/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import comSIWeb.Operaciones.TableMaster;

/**
 *
 * @author N4v1d4d3s
 */
public class Factura_contratos extends TableMaster {
   private int intCT_TIPOPERS;

   public int getIntCT_TIPOPERS() {
      return intCT_TIPOPERS;
   }

   public void setIntCT_TIPOPERS(int intCT_TIPOPERS) {
      this.intCT_TIPOPERS = intCT_TIPOPERS;
   }
   
   public Factura_contratos() {
      super("vta_contrato_arrend", "CTOA_ID", "", "");
      this.Fields.put("CTOA_ID", new Integer(0));
      this.Fields.put("CTE_ID", new Integer(0));//CLIENTE
      this.Fields.put("EMP_ID", new Integer(0)); //EMPRESA
      this.Fields.put("MON_ID", new Integer(0)); //EMPRESA
      this.Fields.put("SC_ID", new Integer(0)); //SUCURSAL
      this.Fields.put("TI_ID", new Integer(0)); //SUCURSAL
      this.Fields.put("CTOA_INICIO", "");//FECHA INICIAL
      this.Fields.put("CTOA_VENCIMIENTO", "");//FECHA FINAL
      this.Fields.put("FECHA_FAC", "");//FECHA DE FACTURA
      this.Fields.put("CTOA_FOLIO", "");//Folio
      this.Fields.put("CTOA_FACTOR_AJUSTE", "");//FACTOR AJUSTE
      this.Fields.put("CTOA_IMPORTE_AJUSTE", "");//IMPORTE AJUSTE
      this.Fields.put("CTOA_CPP", new Double(0.0000)); //cpp
      this.Fields.put("CTOA_MTO_ARRENDAMIENTO", new Double(0.00));//renta mensual
      this.Fields.put("CTOA_TASA_IVA", new Double(0.00));//renta mensual
      this.Fields.put("CTOA_IMPUESTO", new Double(0.00));//renta mensual
      this.Fields.put("CTOA_TOTAL", new Double(0.00));//renta mensual
      this.Fields.put("CTOA_VCP", new Double(0.00));//variacion CPP//TIIE
      this.Fields.put("CTOA_ARRENDAMIENTO", "");//descripcion
      this.Fields.put("CTOA_TEXTO_CPP", "");//CTOA_TEXTO_CPP
      this.Fields.put("CT_METODODEPAGO", "");//CT_METODODEPAGO
      this.Fields.put("CT_FORMADEPAGO", "");//CT_FORMADEPAGO
      this.Fields.put("CT_CTABANCO1", "");//CT_CTABANCO1
      this.Fields.put("CTOA_TIPO_FACTOR_AJUSTE", new Integer(0));//CT_CTABANCO1
      this.Fields.put("CTE_IDF", new Integer(0));//CTE_IDF cliente para facturar
      this.Fields.put("CTOA_MENSUALIDAD_ACTUAL", new Integer(0));//CTOA_MENSUALIDAD_ACTUAL Mensualidad actual
      this.Fields.put("CTOA_MES", new Integer(0));//CTOA_MES Mensualidad total
      this.Fields.put("CTOA_CC1_ID", new Integer(0));//CTOA_MES Mensualidad total


   }
}
