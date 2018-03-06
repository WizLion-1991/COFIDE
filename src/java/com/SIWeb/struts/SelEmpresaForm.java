/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SIWeb.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author zeus
 */
public class SelEmpresaForm extends org.apache.struts.action.ActionForm {
   
   private String error;
   private String name;
   private String cliente;
   private String anio;
   private String[] clientes;
   private String[] anios;
   private int number;

   public String getAnio() {
      return anio;
   }

   public void setAnio(String anio) {
      this.anio = anio;
   }

   public String[] getAnios() {
      return anios;
   }

   public void setAnios(String[] anios) {
      this.anios = anios;
   }

   public String getCliente() {
      return cliente;
   }

   public void setCliente(String cliente) {
      this.cliente = cliente;
   }

   public String[] getClientes() {
      return clientes;
   }

   public void setClientes(String[] clientes) {
      this.clientes = clientes;
   }

   /**
    * @return
    */
   public String getName() {
      return name;
   }

   /**
    * @param string
    */
   public void setName(String string) {
      name = string;
   }

   /**
    * @return
    */
   public int getNumber() {
      return number;
   }

   /**
    * @param i
    */
   public void setNumber(int i) {
      number = i;
   }

   /**
    *
    */
   public SelEmpresaForm() {
      super();
      // TODO Auto-generated constructor stub
      this.anio = "2010";
   }

   /**
    * This is the action called from the Struts framework.
    * @param mapping The ActionMapping used to select this instance.
    * @param request The HTTP Request we are processing.
    * @return
    */
   @Override
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
      ActionErrors errors = new ActionErrors();
      if (getName() == null || getName().length() < 1) {
         errors.add("name", new ActionMessage("error.name.required"));
         // TODO: add 'error.name.required' key to your resources
      }
      return errors;
   }
   public String getError() {
      return error;
   }

   public void setError(String Error) {
      this.error =
              "<span style='color:red'>" + Error + "</span>";
   }
}
