/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SIWeb.struts;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.usuarios_accesos;
import comSIWeb.Utilerias.Fechas;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Clase que se encarga de realizar la autenticacion en la aplicacion
 *
 * @author zeus
 */
public class LoginAction extends org.apache.struts.action.Action {

   /*
    * forward name="success" path=""
    */
   private static final String SUCCESS = "success";
   private static final String SUCCESSMULTI = "successMulti";
   private final static String FAILURE = "failure";
   private final static String SUCESSCTE = "successcte";
   private int intMultiEmpresa = 0;
   private boolean bolAccesoCte = false;
   private boolean bolAccesoProv = false;
   private boolean bolAccesoEmpl = false;
   boolean bolErrorLogged = false;
   boolean bolErrorBloqued = false;
   boolean bolTieneAcceso = false;
   boolean bolSoloCliente = false;
   boolean bolCambiarPass = false;
   boolean bolEsBackOffice = true;

   public boolean isBolEsBackOffice() {
      return bolEsBackOffice;
   }

   public void setBolEsBackOffice(boolean bolEsBackOffice) {
      this.bolEsBackOffice = bolEsBackOffice;
   }

   /**
    * Regresa la bandera de solo acceso al cliente
    *
    * @return Con true solo permite loguearse a los clientes
    */
   public boolean isBolSoloCliente() {
      return bolSoloCliente;
   }

   /**
    * Indica que solo debe permitir logueo de cliente
    *
    * @param bolSoloCliente Con true solo permite loguearse a los clientes
    */
   public void setBolSoloCliente(boolean bolSoloCliente) {
      this.bolSoloCliente = bolSoloCliente;
   }

   public boolean isBolAccesoCte() {
      return bolAccesoCte;
   }

   public void setBolAccesoCte(boolean bolAccesoCte) {
      this.bolAccesoCte = bolAccesoCte;
   }

   public boolean isBolAccesoProv() {
      return bolAccesoProv;
   }

   public void setBolAccesoProv(boolean bolAccesoProv) {
      this.bolAccesoProv = bolAccesoProv;
   }

   public boolean isBolAccesoEmpl() {
      return bolAccesoEmpl;
   }

   public void setBolAccesoEmpl(boolean bolAccesoEmpl) {
      this.bolAccesoEmpl = bolAccesoEmpl;
   }

   public boolean isBolErrorBloqued() {
      return bolErrorBloqued;
   }

   public void setBolErrorBloqued(boolean bolErrorBloqued) {
      this.bolErrorBloqued = bolErrorBloqued;
   }

   public boolean isBolErrorLogged() {
      return bolErrorLogged;
   }

   public void setBolErrorLogged(boolean bolErrorLogged) {
      this.bolErrorLogged = bolErrorLogged;
   }

   public boolean isBolTieneAcceso() {
      return bolTieneAcceso;
   }

   public void setBolTieneAcceso(boolean bolTieneAcceso) {
      this.bolTieneAcceso = bolTieneAcceso;
   }

   public int getIntMultiEmpresa() {
      return intMultiEmpresa;
   }

   public void setIntMultiEmpresa(int intMultiEmpresa) {
      this.intMultiEmpresa = intMultiEmpresa;
   }

   /**
    * Nos dice si el usuario debe cambiar la contraseña
    *
    * @return Indica con true que debe cambiar la contraseña
    */
   public boolean isBolCambiarPass() {
      return bolCambiarPass;
   }

   /**
    * Define si el usuario debe cambiar la contraseña
    *
    * @param bolCambiarPass Indica con true que debe cambiar la contraseña
    */
   public void setBolCambiarPass(boolean bolCambiarPass) {
      this.bolCambiarPass = bolCambiarPass;
   }

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
      // extract user data
      LoginForm formBean = (LoginForm) form;
      String usuario = formBean.getUsuario();
      String password = formBean.getPassword();

      // perform validation
      if ((usuario == null) || // name parameter does not exist
              password == null || // email parameter does not exist
              usuario.equals("") || // name parameter is empty
              password.equals("")) {   // email lacks '@'
         formBean.setError();
         return mapping.findForward(FAILURE);
      } else {
         /*
          * Abrimos conexion
          */
         Conexion oConn = new Conexion(usuario.toUpperCase(), this.getServlet().getServletContext());
         oConn.open();
         bolErrorLogged = false;
         bolErrorBloqued = false;
         bolTieneAcceso = false;
         bolSoloCliente = false;
         bolEsBackOffice = true;
         authentication_user(oConn, usuario, password, request);
         //Cerrar conexion
         oConn.close();

         //Si tiene acceso
         if (!bolTieneAcceso) {
            if (bolErrorLogged) {
               formBean.setErrorLogged(this.getResources(request));
            } else {
               if (bolErrorBloqued) {
                  formBean.setErrorBloqued(this.getResources(request));
               } else {
                  formBean.setErrorAcess(this.getResources(request));
               }
            }
            return mapping.findForward(FAILURE);
         } else {
            if (bolErrorLogged) {
               formBean.setErrorLogged(this.getResources(request));
               return mapping.findForward(FAILURE);
            }
         }
      }

      //Si es acceso de cliente enrutamos hacia otro lado
      if (!bolAccesoCte) {
         //Si tiene la opcion de multiempresa seleccionada lo mandamos a la pantalla de multiempresa
         if (intMultiEmpresa == 1) {
            return mapping.findForward(SUCCESSMULTI);
         } else {
            return mapping.findForward(SUCCESS);
         }
      } else {
         return mapping.findForward(SUCESSCTE);
      }
   }

   /**
    * Metodo para autenticar usuarios
    *
    * @param oConn Es la conexion
    * @param usuario Es el usuario
    * @param password Es el password
    * @param request Es el objeto de peticion web
    */
   public void authentication_user(Conexion oConn, String usuario, String password, HttpServletRequest request) {
      try {


         /*
          * Eliminamos ' para evitar las inyecciones
          */
         usuario = usuario.replace("'", "");

         int intIdUser = 0;
         int intIntentosFallidos = 5;
         int intIS_DISABLED = 0;
         int intIsLogged = 0;
         //Recuperamos el numero de intentos fallidos
         String strSqlInt2 = "SELECT num_intentos_erroneos FROM cuenta_contratada;";
         ResultSet rsI2 = oConn.runQuery(strSqlInt2, true);
         while (rsI2.next()) {
            intIntentosFallidos = rsI2.getInt("num_intentos_erroneos");
         }
//         if (rsI2.getStatement() != null) {
//            rsI2.getStatement().close();
//         }
         rsI2.close();
         //Obtenemos la fecha y el momento actual
         java.util.Date today = (java.util.Date) Calendar.getInstance().getTime();
         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

         // <editor-fold defaultstate="collapsed" desc="Validamos si el usuario existe en la base de datos">
         VariableSession varSesiones = new VariableSession(request);
         //Si no esta activa la bandera de solo cliente
         boolean bolEsUsuario = false;
         if (!this.bolSoloCliente) {
            String SQL_QUERY = "select * from usuarios where user = '" + usuario.toUpperCase() + "'";
            if(oConn.getStrDriverName().contains("jTDS")){
               SQL_QUERY = "select * from usuarios where [user] = '" + usuario.toUpperCase() + "'";
            }
            ResultSet rs = oConn.runQuery(SQL_QUERY, true);
            while (rs.next()) {
               bolEsUsuario = true;
               intIdUser = rs.getInt("id_usuarios");
               intMultiEmpresa = rs.getInt("BOLMULTIEM");
               intIsLogged = rs.getInt("IS_LOGGED");
               intIS_DISABLED = rs.getInt("IS_DISABLED");
               //Validamos los intentos fallidos para bloquear la cuenta
               int intFallidos = rs.getInt("COUNT_FAILED");
               if ((intFallidos > intIntentosFallidos || intIS_DISABLED == 1) /*&& intIsLogged == 0*/) {
                  if (intIS_DISABLED == 0) {
                     intIS_DISABLED = 1;
                  }
                  /**
                   * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                   * VARIAS VECES***
                   */
                  //Obtenemos el tiempo de inactividad 
                  long intMinutosInactividad = 15;
                  String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
                  ResultSet rsI = oConn.runQuery(strSqlInt, true);
                  while (rsI.next()) {
                     intMinutosInactividad = rsI.getLong("intervalo_inactividad");
                  }
//                  if (rsI.getStatement() != null) {
//                     rsI.getStatement().close();
//                  }
                  rsI.close();
                  //Obtenemos el tiempo de la ultima actividad
                  Date dLastAct = new Date(rs.getLong("LAST_TIME_FAIL"));
                  //Comparamos si ya paso el tiempo estimado
                  long diff = today.getTime() - dLastAct.getTime();
                  long diffMinutes = diff / (60 * 1000);
                  if (diffMinutes >= intMinutosInactividad) {
                     bolErrorBloqued = false;
                     intIS_DISABLED = 0;
                  } else {
                     bolErrorBloqued = true;
                  }

               }
               //Si no esta bloqueado continuamos
               if (!bolErrorBloqued) {
                  // <editor-fold defaultstate="collapsed" desc="Validamos si correspone el password y esta activo">
                  if (password.equals(rs.getString("password")) && rs.getInt("UsuarioActivo") == 1) {
                     /**
                      * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO
                      * USUARIO VARIAS VECES***
                      */
                     boolean bolPasaLogged = false;
                     //Validamos si ya esta logueado para validar si ya paso el tiempo de espera de n tiempo
                     if (intIsLogged == 1) {
                        //Obtenemos el tiempo de inactividad 
                        long intMinutosInactividad = 15;
                        String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
                        ResultSet rsI = oConn.runQuery(strSqlInt, true);
                        while (rsI.next()) {
                           intMinutosInactividad = rsI.getLong("intervalo_inactividad");
                        }
//                        if (rsI.getStatement() != null) {
//                           rsI.getStatement().close();
//                        }
                        rsI.close();
                        //Obtenemos el tiempo de la ultima actividad
                        Date dLastAct = new Date(rs.getLong("LAST_TIME"));

                        //Comparamos si ya paso el tiempo estimado
                        long diff = today.getTime() - dLastAct.getTime();
                        long diffMinutes = diff / (60 * 1000);
                        if (diffMinutes >= intMinutosInactividad) {
                           bolPasaLogged = true;
                        } else {
                           bolErrorLogged = true;
                        }
                        //Si no ha pasado bloqueamos
                     } else {
                        //Si pasa
                        bolPasaLogged = true;
                     }
                     /**
                      * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO
                      * USUARIO VARIAS VECES***
                      */
                     if (bolPasaLogged) {
                        /*
                         * Validamos el horario
                         */
                        Fechas fecha = new Fechas();
                        int intDia = fecha.getDayWeek();//El dia 1 comienza desde el domingo
                        String strPrevNom = TraduceDia(intDia);
                        String strHoraIni = rs.getString(strPrevNom + "1");
                        String strHoraFin = rs.getString(strPrevNom + "2");
                        String strHoraAct = fecha.getHoraActual();
                        //Compara si estamos en el horario permitido
                        if (fecha.theHourIsPlus(strHoraAct, strHoraIni) && fecha.theHourIsLess(strHoraAct, strHoraFin)) {
                           /*
                            * Posteriormente validaremos el acceso en base
                            * al horario del usuario y a la macadress
                            */
                           bolTieneAcceso = true;
                           String strLstSuc = "";
                           /*
                            * Obtenemos la lista de sucursales
                            */
                           SQL_QUERY = "select SC_ID from usuarios_sucursales where id_usuarios = '" + rs.getInt("id_usuarios") + "'";
                           ResultSet rs2 = oConn.runQuery(SQL_QUERY, true);
                           while (rs2.next()) {
                              strLstSuc += rs2.getString("SC_ID") + ",";
                           }
//                           if (rs2.getStatement() != null) {
//                              rs2.getStatement().close();
//                           }
                           rs2.close();
                           if (strLstSuc.endsWith(",")) {
                              strLstSuc = strLstSuc.substring(0, strLstSuc.length() - 1);
                           }
                           /*
                            * Obtenemos el nombre de la base de datos
                            */
                           varSesiones.SetVars(rs.getInt("id_usuarios"), rs.getInt("UsuarioCaptura"), 0, 0, rs.getString("nombre_usuario"), "", 0, strLstSuc, rs.getInt("SUC_DEFA"));
                           varSesiones.setIntIdPerfil(rs.getInt("PERF_ID"));
                        } else {
                        }
                     }

                  }
                  // </editor-fold>
               }

            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();
         }

         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Si no tiene acceso intentamos probar con un cliente">
         if (!bolTieneAcceso && !bolEsUsuario && !bolEsBackOffice) {

            String SQL_QUERY = "select * from vta_cliente where CT_ID = '" + usuario.toUpperCase() + "'";
            ResultSet rs = oConn.runQuery(SQL_QUERY, true);
            while (rs.next()) {
               intIdUser = rs.getInt("CT_ID");
               bolAccesoCte = true;
               this.bolCambiarPass = rs.getBoolean("CT_CHANGE_PASSWRD");
               intIsLogged = rs.getInt("CT_IS_LOGGED");
               intIS_DISABLED = rs.getInt("CT_IS_DISABLED");
               //Validamos los intentos fallidos para bloquear la cuenta
               int intFallidos = rs.getInt("CT_FALLIDOS");
               if ((intFallidos > intIntentosFallidos || intIS_DISABLED == 1) && intIsLogged == 0) {
                  /**
                   * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                   * VARIAS VECES***
                   */
                  //Obtenemos el tiempo de inactividad 
                  long intMinutosInactividad = 15;
                  String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
                  ResultSet rsI = oConn.runQuery(strSqlInt, true);
                  while (rsI.next()) {
                     intMinutosInactividad = rsI.getLong("intervalo_inactividad");
                  }
//                  if (rsI.getStatement() != null) {
//                     rsI.getStatement().close();
//                  }
                  rsI.close();
                  //Obtenemos el tiempo de la ultima actividad
                  Date dLastAct = new Date(rs.getLong("CT_LAST_TIME"));
                  //Comparamos si ya paso el tiempo estimado
                  long diff = today.getTime() - dLastAct.getTime();
                  long diffMinutes = diff / (60 * 1000);
                  if (diffMinutes >= intMinutosInactividad) {
                     bolErrorBloqued = false;
                  } else {
                     bolErrorBloqued = true;
                  }

               }
               //Si no esta bloqueado continuamos
               if (!bolErrorBloqued) {
                  // <editor-fold defaultstate="collapsed" desc="Validamos si correspone el password y esta activo">
                  if (password.toUpperCase().equals(rs.getString("CT_PASSWORD").toUpperCase())) {
                     //Validamos si esta logueado ya

                     /**
                      * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO
                      * USUARIO VARIAS VECES***
                      */
                     boolean bolPasaLogged = false;
                     //Validamos si ya esta logueado para validar si ya paso el tiempo de espera de n tiempo
                     if (intIsLogged == 1) {
                        //Obtenemos el tiempo de inactividad 
                        long intMinutosInactividad = 15;
                        String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
                        ResultSet rsI = oConn.runQuery(strSqlInt, true);
                        while (rsI.next()) {
                           intMinutosInactividad = rsI.getLong("intervalo_inactividad");
                        }
//                        if (rsI.getStatement() != null) {
//                           rsI.getStatement().close();
//                        }
                        rsI.close();
                        //Obtenemos el tiempo de la ultima actividad
                        Date dLastAct = new Date(rs.getLong("CT_LAST_TIME"));
                        //Comparamos si ya paso el tiempo estimado
                        long diff = today.getTime() - dLastAct.getTime();
                        long diffMinutes = diff / (60 * 1000);
                        if (diffMinutes >= intMinutosInactividad) {
                           bolPasaLogged = true;
                        } else {
                           bolErrorLogged = true;
                        }
                        //Si no ha pasado bloqueamos
                     } else {
                        //Si pasa
                        bolPasaLogged = true;
                     }
                     /**
                      * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO
                      * USUARIO VARIAS VECES***
                      */
                     if (bolPasaLogged) {
                        /*
                         * Posteriormente validaremos el acceso en base
                         * al horario del usuario y a la macadress
                         */
                        bolTieneAcceso = true;
                        /*
                         * Obtenemos el nombre de la base de datos
                         */
                        varSesiones.SetVars(rs.getInt("CT_ID"), 0, rs.getInt("CT_ID"), 0, rs.getString("CT_RAZONSOCIAL"), "", 0, "", 0);
                        varSesiones.setintIdCliente(rs.getInt("CT_ID"));
                     }

                  }
                  // </editor-fold>
               }

            }
            if (rs.getStatement() != null) {
               //rs.getStatement().close();
            }
            rs.close();

         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Guardamos las estadisticas del usuario">
         Fechas fecha = new Fechas();

         //Bitacora de accesos
         usuarios_accesos bitaAcceso = new usuarios_accesos();
         bitaAcceso.setFieldInt("id_usuario", intIdUser);
         bitaAcceso.setFieldLong("seg_date", today.getTime());
         bitaAcceso.setFieldInt("seg_total_time", 0);
         bitaAcceso.setFieldString("seg_nombre_user", varSesiones.getStrUser());
         bitaAcceso.setFieldString("seg_fecha", fecha.getFechaActual());
         bitaAcceso.setFieldString("seg_hora", fecha.getHoraActualHHMMSS());
         bitaAcceso.setFieldString("seg_ip_remote", request.getRemoteAddr());
         bitaAcceso.setFieldString("seg_computer_name", request.getRemoteHost());
         bitaAcceso.setFieldString("seg_session_id", request.getSession().getId());

         String strUpdate = "";
         String strTabla = "";
         String strKey = "";
         if (!bolAccesoCte) {
            strTabla = "usuarios";
            strKey = "id_usuarios";
         } else {
            strTabla = "vta_cliente";
            strKey = "CT_ID";
         }

         if (!bolTieneAcceso) {
            bitaAcceso.setFieldInt("seg_tipo_ini_fin", 3);
            if (!bolAccesoCte) {
               String strDisabled = "";
               if (intIS_DISABLED == 1) {
                  strDisabled += ",IS_DISABLED=1 ";
               }
               strUpdate = "UPDATE " + strTabla + " set "
                       + "COUNT_FAILED=COUNT_FAILED + 1 ,LAST_FAIL_DATE='" + fecha.getFechaActual() + "',"
                       + "LAST_FAIL_HOUR='" + fecha.getHoraActual() + "' "
                       + ",LAST_TIME_FAIL='" + today.getTime() + "'"
                       + strDisabled
                       + "where " + strKey + "=" + intIdUser;
            } else {
               String strDisabled = "";
               if (intIS_DISABLED == 1) {
                  strDisabled += ",CT_IS_DISABLED= 1 ";
               }
               strUpdate = "UPDATE " + strTabla + " set "
                       + "CT_FALLIDOS=CT_FALLIDOS + 1 ,CT_FECHAULTINT='" + fecha.getFechaActual() + "',"
                       + "CT_HORAULTINT='" + fecha.getHoraActual() + "'"
                       + ",CT_LAST_TIME_FAIL='" + today.getTime() + "' "
                       + strDisabled
                       + "where " + strKey + "=" + intIdUser;
            }
         } else {
            if (!bolAccesoCte) {
               bitaAcceso.setFieldInt("seg_tipo_ini_fin", 1);
               strUpdate = "UPDATE " + strTabla + " set "
                       + "COUNT_ACCESS=COUNT_ACCESS + 1 ,LAST_LOGIN_DATE='" + fecha.getFechaActual() + "',"
                       + "LAST_LOGIN_HOUR='" + fecha.getHoraActual() + "',LAST_ACT='" + dateFormat.format(today) + "',IS_LOGGED=1 "
                       + ",LASTSESSIONID='" + request.getSession().getId() + "' "
                       + ",LASTIPADDRESS='" + request.getRemoteAddr() + "' "
                       + ",LAST_TIME='" + today.getTime() + "' "
                       + ",COUNT_FAILED=0 "
                       + ",IS_DISABLED=0 "
                       + "where " + strKey + "=" + varSesiones.getIntNoUser();
            } else {
               bitaAcceso.setFieldInt("seg_tipo_ini_fin", 2);
               strUpdate = "UPDATE " + strTabla + " set "
                       + "CT_EXITOSOS=CT_EXITOSOS + 1 ,CT_FECHAEXIT='" + fecha.getFechaActual() + "',"
                       + "CT_HORAEXIT='" + fecha.getHoraActual() + "',CT_LAST_ACT='" + dateFormat.format(today) + "',CT_IS_LOGGED=1 "
                       + ",CT_LASTSESSIONID='" + request.getSession().getId() + "' "
                       + ",CT_LASTIPADDRESS='" + request.getRemoteAddr() + "' "
                       + ",CT_LAST_TIME='" + today.getTime() + "' "
                       + ",CT_FALLIDOS=0 "
                       + ",CT_IS_DISABLED=0 "
                       + "where " + strKey + "=" + varSesiones.getIntNoUser();
            }
         }
         oConn.runQueryLMD(strUpdate);
         //Guardamos estadistica
         bitaAcceso.Agrega(oConn);
         // </editor-fold>

      } catch (SQLException ex) {
         Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void authentication_proveedor(Conexion oConn, String proveedor, String password, HttpServletRequest request) {
      try {

         /*
          * Eliminamos ' para evitar las inyecciones
          */
         proveedor = proveedor.replace("'", "");

         int intIdproveedor = 0;
         int intIntentosFallidos = 5;
         int intIS_DISABLED = 0;
         int intIsLogged = 0;

         /* Recuperamos los datos del Proveedor*/
         String strProveedor = "";
         //Recuperamos el numero de intentos fallidos
         String strSqlInt2 = "SELECT num_intentos_erroneos FROM cuenta_contratada;";
         ResultSet rsI2 = oConn.runQuery(strSqlInt2, true);
         while (rsI2.next()) {
            intIntentosFallidos = rsI2.getInt("num_intentos_erroneos");
         }
//         if (rsI2.getStatement() != null) {
//            rsI2.getStatement().close();
//         }
         rsI2.close();
         //Obtenemos la fecha y el momento actual
         java.util.Date today = (java.util.Date) Calendar.getInstance().getTime();
         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

         // <editor-fold defaultstate="collapsed" desc="Validamos si el proveedor existe en la base de datos">
         VariableSession varSesiones = new VariableSession(request);
         //Si no esta activa la bandera de solo cliente

         String SQL_QUERY = "select * from vta_proveedor where PV_ID = '" + proveedor.toUpperCase() + "'";
         ResultSet rs = oConn.runQuery(SQL_QUERY, true);
         while (rs.next()) {
            intIdproveedor = rs.getInt("PV_ID");
            bolAccesoProv = true;
            this.bolCambiarPass = rs.getBoolean("PV_CHANGE_PASSWRD");
            intIsLogged = rs.getInt("PV_IS_LOGGED");
            intIS_DISABLED = rs.getInt("PV_IS_DISABLED");
            strProveedor = rs.getString("PV_RAZONSOCIAL");
            //Validamos los intentos fallidos para bloquear la cuenta
            int intFallidos = rs.getInt("PV_COUNT_FAILED");
            if ((intFallidos > intIntentosFallidos || intIS_DISABLED == 1) && intIsLogged == 0) {
               /**
                * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                * VARIAS VECES***
                */
               //Obtenemos el tiempo de inactividad 
               long intMinutosInactividad = 15;
               String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
               ResultSet rsI = oConn.runQuery(strSqlInt, true);
               while (rsI.next()) {
                  intMinutosInactividad = rsI.getLong("intervalo_inactividad");
               }
//               if (rsI.getStatement() != null) {
//                  rsI.getStatement().close();
//               }
               rsI.close();
               //Obtenemos el tiempo de la ultima actividad
               Date dLastAct = new Date(rs.getLong("PV_LAST_TIME_FAIL"));
               //Comparamos si ya paso el tiempo estimado
               long diff = today.getTime() - dLastAct.getTime();
               long diffMinutes = diff / (60 * 1000);
               if (diffMinutes >= intMinutosInactividad) {
                  bolErrorBloqued = false;
               } else {
                  bolErrorBloqued = true;
               }

            }
            //Si no esta bloqueado continuamos
            if (!bolErrorBloqued) {
               // <editor-fold defaultstate="collapsed" desc="Validamos si correspone el password y esta activo">
               if (password.toUpperCase().equals(rs.getString("PV_PASSWORD").toUpperCase())) {
                  //Validamos si esta logueado ya

                  /**
                   * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                   * VARIAS VECES***
                   */
                  boolean bolPasaLogged = false;
                  //Validamos si ya esta logueado para validar si ya paso el tiempo de espera de n tiempo
                  if (intIsLogged == 1) {
                     //Obtenemos el tiempo de inactividad 
                     long intMinutosInactividad = 15;
                     String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
                     ResultSet rsI = oConn.runQuery(strSqlInt, true);
                     while (rsI.next()) {
                        intMinutosInactividad = rsI.getLong("intervalo_inactividad");
                     }
//                     if (rsI.getStatement() != null) {
//                        rsI.getStatement().close();
//                     }
                     rsI.close();
                     //Obtenemos el tiempo de la ultima actividad
                     Date dLastAct = new Date(rs.getLong("PV_LAST_TIME"));
                     //Comparamos si ya paso el tiempo estimado
                     long diff = today.getTime() - dLastAct.getTime();
                     long diffMinutes = diff / (60 * 1000);
                     if (diffMinutes >= intMinutosInactividad) {
                        bolPasaLogged = true;
                     } else {
                        bolErrorLogged = true;
                     }
                     //Si no ha pasado bloqueamos
                  } else {
                     //Si pasa
                     bolPasaLogged = true;
                  }
                  /**
                   * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                   * VARIAS VECES***
                   */
                  if (bolPasaLogged) {
                     /*
                      * Posteriormente validaremos el acceso en base
                      * al horario del usuario y a la macadress
                      */
                     bolTieneAcceso = true;
                     /*
                      * Obtenemos el nombre de la base de datos
                      */
                     varSesiones.SetVars(rs.getInt("PV_ID"), 0, rs.getInt("PV_ID"), 0, rs.getString("PV_RAZONSOCIAL"), "", 0, "", 0);
                     varSesiones.setintIdCliente(rs.getInt("PV_ID"));
                  }

               }
               // </editor-fold>
            }

         }
//         if (rs.getStatement() != null) {
//            //rs.getStatement().close();
//         }
         rs.close();

         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Guardamos las estadisticas del usuario">
         Fechas fecha = new Fechas();

         //Bitacora de accesos
         usuarios_accesos bitaAcceso = new usuarios_accesos();
         bitaAcceso.setFieldInt("id_usuario", intIdproveedor);
         bitaAcceso.setFieldLong("seg_date", today.getTime());
         bitaAcceso.setFieldInt("seg_total_time", 0);
         bitaAcceso.setFieldString("seg_nombre_user", strProveedor);
         bitaAcceso.setFieldString("seg_fecha", fecha.getFechaActual());
         bitaAcceso.setFieldString("seg_hora", fecha.getHoraActualHHMMSS());
         bitaAcceso.setFieldString("seg_ip_remote", request.getRemoteAddr());
         bitaAcceso.setFieldString("seg_computer_name", request.getRemoteHost());
         bitaAcceso.setFieldString("seg_session_id", request.getSession().getId());
         bitaAcceso.setFieldInt("seg_is_proveedor", 1);
         bitaAcceso.setFieldInt("seg_is_empleado", 0);

         String strUpdate = "";
         String strTabla = "";
         String strKey = "";

         strTabla = "vta_proveedor";
         strKey = "PV_ID";

         bitaAcceso.setFieldInt("seg_tipo_ini_fin", 4);
         String strDisabled = "";
         if (intIS_DISABLED == 1) {
            strDisabled += ",PV_IS_DISABLED=1 ";
         }
         strUpdate = "UPDATE " + strTabla + " set "
                 + "PV_COUNT_FAILED=PV_COUNT_FAILED + 1 ,PV_LAST_TIME_FAIL='" + fecha.getFechaActual() + "',"
                 + "PV_LAST_FAIL_HOUR='" + fecha.getHoraActual() + "' "
                 + ",PV_LAST_TIME_FAIL='" + today.getTime() + "'"
                 + strDisabled
                 + "where " + strKey + "=" + intIdproveedor;

         oConn.runQueryLMD(strUpdate);
         //Guardamos estadistica
         bitaAcceso.Agrega(oConn);
         // </editor-fold>

      } catch (SQLException ex) {
         Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void authentication_empleado(Conexion oConn, String empleado, String password, HttpServletRequest request) {
      try {

         /*
          * Eliminamos ' para evitar las inyecciones
          */
         empleado = empleado.replace("'", "");

         int intIdEmpleado = 0;
         int intIntentosFallidos = 5;
         int intIS_DISABLED = 0;
         int intIsLogged = 0;

         /* Recuperamos los datos del Proveedor*/
         String strEmpleado = "";
         //Recuperamos el numero de intentos fallidos
         String strSqlInt2 = "SELECT num_intentos_erroneos FROM cuenta_contratada;";
         ResultSet rsI2 = oConn.runQuery(strSqlInt2, true);
         while (rsI2.next()) {
            intIntentosFallidos = rsI2.getInt("num_intentos_erroneos");
         }
//         if (rsI2.getStatement() != null) {
//            rsI2.getStatement().close();
//         }
         rsI2.close();
         //Obtenemos la fecha y el momento actual
         java.util.Date today = (java.util.Date) Calendar.getInstance().getTime();
         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

         // <editor-fold defaultstate="collapsed" desc="Validamos si el proveedor existe en la base de datos">
         VariableSession varSesiones = new VariableSession(request);
         //Si no esta activa la bandera de solo cliente

         String SQL_QUERY = "select * from rhh_emplados where EMP_NUM = '" + empleado.toUpperCase() + "'";
         ResultSet rs = oConn.runQuery(SQL_QUERY, true);
         while (rs.next()) {
            intIdEmpleado = rs.getInt("EMP_NUM");
            bolAccesoEmpl = true;
            this.bolCambiarPass = rs.getBoolean("EMP_CHANGE_PASSWRD");
            intIsLogged = rs.getInt("EMP_ISLOGGED");
            intIS_DISABLED = rs.getInt("EMP_IS_DISABLED");
            strEmpleado = rs.getString("EMP_NOMBRE");
            //Validamos los intentos fallidos para bloquear la cuenta
            int intFallidos = rs.getInt("EMP_COUNT_FAILED");
            if ((intFallidos > intIntentosFallidos || intIS_DISABLED == 1) && intIsLogged == 0) {
               /**
                * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                * VARIAS VECES***
                */
               //Obtenemos el tiempo de inactividad 
               long intMinutosInactividad = 15;
               String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
               ResultSet rsI = oConn.runQuery(strSqlInt, true);
               while (rsI.next()) {
                  intMinutosInactividad = rsI.getLong("intervalo_inactividad");
               }
//               if (rsI.getStatement() != null) {
//                  rsI.getStatement().close();
//               }
               rsI.close();
               //Obtenemos el tiempo de la ultima actividad
               Date dLastAct = new Date(rs.getLong("EMP_LAST_TIME_FAIL"));
               //Comparamos si ya paso el tiempo estimado
               long diff = today.getTime() - dLastAct.getTime();
               long diffMinutes = diff / (60 * 1000);
               if (diffMinutes >= intMinutosInactividad) {
                  bolErrorBloqued = false;
               } else {
                  bolErrorBloqued = true;
               }

            }
            //Si no esta bloqueado continuamos
            if (!bolErrorBloqued) {
               // <editor-fold defaultstate="collapsed" desc="Validamos si correspone el password y esta activo">
               if (password.toUpperCase().equals(rs.getString("EMP_CONTRASENIA").toUpperCase())) {
                  //Validamos si esta logueado ya

                  /**
                   * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                   * VARIAS VECES***
                   */
                  boolean bolPasaLogged = false;
                  //Validamos si ya esta logueado para validar si ya paso el tiempo de espera de n tiempo
                  if (intIsLogged == 1) {
                     //Obtenemos el tiempo de inactividad 
                     long intMinutosInactividad = 15;
                     String strSqlInt = "SELECT intervalo_inactividad FROM cuenta_contratada;";
                     ResultSet rsI = oConn.runQuery(strSqlInt, true);
                     while (rsI.next()) {
                        intMinutosInactividad = rsI.getLong("intervalo_inactividad");
                     }
//                     if (rsI.getStatement() != null) {
//                        rsI.getStatement().close();
//                     }
                     rsI.close();
                     //Obtenemos el tiempo de la ultima actividad
                     Date dLastAct = new Date(rs.getLong("EMP_LAST_TIME"));
                     //Comparamos si ya paso el tiempo estimado
                     long diff = today.getTime() - dLastAct.getTime();
                     long diffMinutes = diff / (60 * 1000);
                     if (diffMinutes >= intMinutosInactividad) {
                        bolPasaLogged = true;
                     } else {
                        bolErrorLogged = true;
                     }
                     //Si no ha pasado bloqueamos
                  } else {
                     //Si pasa
                     bolPasaLogged = true;
                  }
                  /**
                   * *********RUTINA PARA VALIDAR QUE NO ENTRE EL MISMO USUARIO
                   * VARIAS VECES***
                   */
                  if (bolPasaLogged) {
                     /*
                      * Posteriormente validaremos el acceso en base
                      * al horario del usuario y a la macadress
                      */
                     bolTieneAcceso = true;
                     /*
                      * Obtenemos el nombre de la base de datos
                      */
                     varSesiones.SetVars(rs.getInt("EMP_NUM"), 0, rs.getInt("EMP_NUM"), 0, rs.getString("EMP_NOMBRE"), "", 0, "", 0);
                     varSesiones.setintIdCliente(rs.getInt("EMP_NUM"));
                  }

               }
               // </editor-fold>
            }

         }
//         if (rs.getStatement() != null) {
//            //rs.getStatement().close();
//         }
         rs.close();

         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Guardamos las estadisticas del usuario">
         Fechas fecha = new Fechas();

         //Bitacora de accesos
         usuarios_accesos bitaAcceso = new usuarios_accesos();
         bitaAcceso.setFieldInt("id_usuario", intIdEmpleado);
         bitaAcceso.setFieldLong("seg_date", today.getTime());
         bitaAcceso.setFieldInt("seg_total_time", 0);
         bitaAcceso.setFieldString("seg_nombre_user", strEmpleado);
         bitaAcceso.setFieldString("seg_fecha", fecha.getFechaActual());
         bitaAcceso.setFieldString("seg_hora", fecha.getHoraActualHHMMSS());
         bitaAcceso.setFieldString("seg_ip_remote", request.getRemoteAddr());
         bitaAcceso.setFieldString("seg_computer_name", request.getRemoteHost());
         bitaAcceso.setFieldString("seg_session_id", request.getSession().getId());
         bitaAcceso.setFieldInt("seg_is_proveedor", 1);
         bitaAcceso.setFieldInt("seg_is_empleado", 0);

         String strUpdate = "";
         String strTabla = "";
         String strKey = "";

         strTabla = "rhh_empleados";
         strKey = "EMP_NUM";

         bitaAcceso.setFieldInt("seg_tipo_ini_fin", 5);
         String strDisabled = "";
         if (intIS_DISABLED == 1) {
            strDisabled += ",EMP_IS_DISABLED=1 ";
         }
         strUpdate = "UPDATE " + strTabla + " set "
                 + "EMP_COUNT_FAILED=EMP_COUNT_FAILED + 1 ,EMP_LAST_TIME_FAIL='" + fecha.getFechaActual() + "',"
                 + "EMP_LAST_FAIL_HOUR='" + fecha.getHoraActual() + "' "
                 + ",EMP_LAST_TIME_FAIL='" + today.getTime() + "'"
                 + strDisabled
                 + "where " + strKey + "=" + intIdEmpleado;

         oConn.runQueryLMD(strUpdate);
         //Guardamos estadistica
         bitaAcceso.Agrega(oConn);
         // </editor-fold>

      } catch (SQLException ex) {
         Logger.getLogger(LoginAction.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   /**
    * Dependiendo del dia lo traduce en el campo que vamos analizar
    */
   private String TraduceDia(int intDia) {
      String strNomField = "";
      switch (intDia) {
         case 1:
            strNomField = "HOR_DOM";
            break;
         case 2:
            strNomField = "HOR_LUNES";
            break;
         case 3:
            strNomField = "HOR_MARTES";
            break;
         case 4:
            strNomField = "HOR_MIERC";
            break;
         case 5:
            strNomField = "HOR_JUEV";
            break;
         case 6:
            strNomField = "HOR_VIER";
            break;
         case 7:
            strNomField = "HOR_SAB";
            break;
      }
      return strNomField;
   }
}
