/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SIWeb.struts;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.ContextoApt.VariableSession;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author zeus
 */
public class SelEmpresaAction extends org.apache.struts.action.Action {

   /* forward name="success" path="" */
   private static final String SUCCESS = "success";
   private final static String FAILURE = "failure";

   /**
    * This is the action called from the Struts framework.
    *
    * @param mapping The ActionMapping used to select this instance.
    * @param form The optional ActionForm bean for this request.
    * @param request The HTTP Request we are processing.
    * @param response The HTTP Response we are processing.
    * @throws java.lang.Exception
    * @return
    */
   @Override
   public ActionForward execute(ActionMapping mapping, ActionForm form,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {
      SelEmpresaForm formBean = (SelEmpresaForm) form;
      System.out.println(formBean.getCliente() + " " + formBean.getAnio());
      if ((formBean.getCliente() == null)
         || formBean.getAnio() == null
         || formBean.getCliente().equals("")
         || formBean.getAnio().equals("")) {
         MessageResources messageResources = getResources(request);
         formBean.setError(messageResources.getMessage("SelE.message6", request));
         return mapping.findForward(FAILURE);
      } else {
         /*Inicializamos las variables de sesion limpias*/
         VariableSession varSesiones = new VariableSession(request);
         int intClienteWork = 0;
         int intAnioWork = 0;
         try {
            intClienteWork = Integer.valueOf(formBean.getCliente());
         } catch (NumberFormatException ex) {
            ex.printStackTrace();
         }
         try {
            intAnioWork = Integer.valueOf(formBean.getAnio());
         } catch (NumberFormatException ex) {
            ex.printStackTrace();
         }
         varSesiones.SetVars(intClienteWork, intAnioWork);
         /*Recuperamos el numero de digitos del cliente*/
         int intNumDigitos = 3;
         /*Abrimos conexion*/
         Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServlet().getServletContext());
         oConn.open();
         /*Validamos si el usuario existe en la base de datos*/
         String SQL_QUERY = "select NumDigitos from Cliente";
         ResultSet rs = oConn.runQuery(SQL_QUERY, true);
         while (rs.next()) {
            intNumDigitos = rs.getInt("NumDigitos");
         }
         //Cerrar conexion
         //if(rs.getStatement() != null )rs.getStatement().close();
         rs.close();
         oConn.close();
         varSesiones.setNumDigitos(intNumDigitos);
      }
      return mapping.findForward(SUCCESS);
   }

   /**
    * Regresa los recursos de struts
    *
    * @param request es la peticion
    * @return Nos regresa la peticion
    */
   public MessageResources getmessageResources(HttpServletRequest request) {
      return getResources(request);
   }

}
