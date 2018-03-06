/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SIWeb.struts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author zeus
 */
public class LoginForm extends org.apache.struts.action.ActionForm {
// error message

   private String error;
   private String name;
   private int number;
   private String usuario;
   private String password;

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

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getUsuario() {
      return usuario;
   }

   public void setUsuario(String usuario) {
      this.usuario = usuario;
   }

   /**
    *
    */
   public LoginForm() {
      super();
      // TODO Auto-generated constructor stub
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

   public void setError() {
      this.error =
              "<span style='color:red'>Necesitas llenar ambos campos</span>";
   }
   /**
    * Establece el mensaje de error al realizar diferentes validaciones
    * @param msgRes Es el mesnaje de error
    */
   public void setErrorAcess(MessageResources msgRes) {
      this.error =
              "<span style='color:red'>" + msgRes.getMessage("errors.Access")  + "</span>";
   }
   /**
    * Establece el mensaje de error al detectar que el usuario esta logueado
    * @param msgRes Es el mensaje de error
    */
   public void setErrorLogged(MessageResources msgRes) {
      this.error =
              "<span style='color:red'>" + msgRes.getMessage("errors.Logged")  + "</span>";
   }
   /**
    * Establece el mensaje de error al detectar que son varios intentos por acceder
    * @param msgRes Es el mensaje de error
    */
   public void setErrorBloqued(MessageResources msgRes) {
      this.error =
              "<span style='color:red'>" + msgRes.getMessage("errors.Bloqued")  + "</span>";
   }
}
